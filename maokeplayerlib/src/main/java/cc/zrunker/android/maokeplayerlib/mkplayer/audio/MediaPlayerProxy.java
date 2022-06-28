package cc.zrunker.android.maokeplayerlib.mkplayer.audio;

import android.content.Context;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.AutomaticGainControl;
import android.media.audiofx.BassBoost;
import android.media.audiofx.NoiseSuppressor;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

/**
 * MediaPlayer工具类
 *
 * @author 邹峰立
 */
public class MediaPlayerProxy {
    private final String TAG = MediaPlayerProxy.class.getSimpleName();
    private final Context context;
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private WifiManager.WifiLock wifiLock;
    private Visualizer visualizer;// 音频波形图
    private AcousticEchoCanceler acousticEchoCanceler;// 回声消除器
    private AutomaticGainControl automaticGainControl;// 自动增强控制器
    private NoiseSuppressor noiseSuppressor;// 噪音抑制器
    private BassBoost bassBoost;// 重低音调节器

    public static synchronized MediaPlayerProxy create(Context context) {
        return new MediaPlayerProxy(context);
    }

    private MediaPlayerProxy(Context context) {
        this.context = context.getApplicationContext();
        init();
    }

    // 初始化
    private void init() {
        this.initMediaPlayer();
        this.initVisualizer();
        if (mediaPlayer != null) {
            int audioSession = mediaPlayer.getAudioSessionId();
            try {
                acousticEchoCanceler = AcousticEchoCanceler.create(audioSession);
                if (AcousticEchoCanceler.isAvailable() && acousticEchoCanceler != null)
                    acousticEchoCanceler.setEnabled(true);
                automaticGainControl = AutomaticGainControl.create(audioSession);
                if (AutomaticGainControl.isAvailable() && automaticGainControl != null)
                    automaticGainControl.setEnabled(true);
                noiseSuppressor = NoiseSuppressor.create(audioSession);
                if (NoiseSuppressor.isAvailable() && noiseSuppressor != null)
                    noiseSuppressor.setEnabled(true);
                bassBoost = new BassBoost(0, audioSession);
                bassBoost.setStrength((short) 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 获取AudioManager
    public AudioManager getAudioManager() {
        if (audioManager == null)
            audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return audioManager;
    }

    // 获取音量
    public int getStreamVolume() {
        return getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    public int getStreamVolume(int streamType) {
        if (audioManager != null)
            return audioManager.getStreamVolume(streamType);
        return 0;
    }

    // 获取最大音量
    public int getStreamMaxVolume() {
        return getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    public int getStreamMaxVolume(int streamType) {
        if (audioManager != null)
            return audioManager.getStreamMaxVolume(streamType);
        return 0;
    }

    /**
     * 设置音量
     *
     * @param streamType 流类型
     * @param index      大小
     * @param flags      种类-特征
     */
    public MediaPlayerProxy setStreamVolume(int streamType, int index, int flags) {
        if (audioManager != null)
            audioManager.setStreamVolume(streamType, index, flags);
        return this;
    }

    // 开始播放
    public MediaPlayerProxy start() {
        if (mediaPlayer != null)
            mediaPlayer.start();
        initVisualizer();
        return this;
    }

    // 重置
    public MediaPlayerProxy reset() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            mediaPlayer.stop();
            mediaPlayer.reset();
            if (visualizer != null)
                visualizer.setEnabled(true);
        }
        return this;
    }

    /**
     * 快进
     *
     * @param msec 毫秒数
     */
    public MediaPlayerProxy seekTo(int msec) {
        if (mediaPlayer != null)
            mediaPlayer.seekTo(msec);
        return this;
    }

    // 获取音频总时长
    public int getDuration() {
        if (mediaPlayer != null)
            return mediaPlayer.getDuration();
        return 0;
    }

    // 获取音频当前进度
    public int getCurrentPosition() {
        if (mediaPlayer != null)
            return mediaPlayer.getCurrentPosition();
        return 0;
    }

    // 是否正在播放
    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    // 播放1
    public MediaPlayerProxy play(String url) {
        if (!TextUtils.isEmpty(url)) {
            Uri uri = Uri.parse(url);
            play(uri);
        }
        return this;
    }

    // 播放2
    public MediaPlayerProxy play(Uri uri) {
        try {
            if (uri != null) {
                // 开启播放
                if (mediaPlayer == null)
                    init();
                // 重置mediaPlayer
                mediaPlayer.reset();
                // 重新加载音频资源
                mediaPlayer.setDataSource(context, uri);
                // 准备播放（异步）
                mediaPlayer.prepareAsync();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (mediaPlayerListener != null)
                mediaPlayerListener.onError(mediaPlayer, 0, 0);
        }
        return this;
    }

    // 暂停
    public MediaPlayerProxy pause() {
        if (mediaPlayer != null)
            mediaPlayer.pause();
        return this;
    }

    // 停止
    public MediaPlayerProxy stop() {
        if (mediaPlayer != null)
            mediaPlayer.stop();
        return this;
    }

    // 销毁
    public void destroy() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    mediaPlayer.releaseDrm();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mediaPlayer = null;
        }
        if (wifiLock != null) {
            wifiLock.release();
            wifiLock = null;
        }
        if (acousticEchoCanceler != null)
            acousticEchoCanceler.release();
        if (automaticGainControl != null)
            automaticGainControl.release();
        if (noiseSuppressor != null)
            noiseSuppressor.release();
        if (bassBoost != null)
            bassBoost.release();
        visualizerDestroy();
    }

    // 销毁visualizer
    private void visualizerDestroy() {
        if (visualizer != null) {
            visualizer.setEnabled(false);
            visualizer.release();
        }
    }

    // 初始化MediaPlayer
    private void initMediaPlayer() {
        if (mediaPlayer == null)
            mediaPlayer = new MediaPlayer();

        // 设置音量，参数分别表示左右声道声音大小，取值范围为0~1
        mediaPlayer.setVolume(0.5f, 0.5f);

        // 设置是否循环播放
        mediaPlayer.setLooping(false);

        // 设置设备进入锁状态模式-可在后台播放或者缓冲音乐-CPU一直工作
        mediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
        // 当播放的时候一直让屏幕变亮
        mediaPlayer.setScreenOnWhilePlaying(true);

        // 如果你使用wifi播放流媒体，你还需要持有wifi锁
        WifiManager wifiManager = ((WifiManager) context.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE));
        if (wifiManager != null) {
            wifiLock = wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL, "wifilock");
            wifiLock.acquire();
        }

        // 处理音频焦点-处理多个程序会来竞争音频输出设备
        if (audioManager == null)
            audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 征对于Android 8.0
            AudioFocusRequest audioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                    .setOnAudioFocusChangeListener(focusChangeListener).build();
            audioFocusRequest.acceptsDelayedFocusGain();
            if (audioManager != null) {
                audioManager.requestAudioFocus(audioFocusRequest);
            }
        } else {
            // 小于Android 8.0
            int result = 0;
            if (audioManager != null) {
                result = audioManager.requestAudioFocus(focusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
            }
            if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                // could not get audio focus.
                Toast.makeText(context, "无法获取焦点！", Toast.LENGTH_SHORT).show();
            }
        }

        // 设置播放错误监听
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                if (mediaPlayerListener != null)
                    mediaPlayerListener.onError(mediaPlayer, i, i1);
                mediaPlayer.pause();
                mediaPlayer.stop();
                mediaPlayer.reset();
                return false;
            }
        });

        // 设置播放完成监听
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (visualizer != null)
                    visualizer.setEnabled(false);
                if (mediaPlayerListener != null)
                    mediaPlayerListener.onCompletion(mediaPlayer);
            }
        });

        // 异步准备Prepared完成监听
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                if (mediaPlayerListener != null)
                    mediaPlayerListener.onPrepared(mediaPlayer);
                mediaPlayer.start();
                initVisualizer();
            }
        });

        // 进度调整完成SeekComplete监听
        mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mediaPlayer) {
                if (mediaPlayerListener != null)
                    mediaPlayerListener.onSeekComplete(mediaPlayer);
            }
        });

        // 网络流媒体缓冲监听
        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mediaPlayer, int progress) {
                // i 0~100
                Log.d(TAG, "缓存进度" + progress + "%");
                if (mediaPlayerListener != null)
                    mediaPlayerListener.onBufferingUpdate(mediaPlayer, progress);
            }
        });
    }

    private final AudioManager.OnAudioFocusChangeListener focusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (mediaPlayer == null)
                init();
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    // 获取audio focus
                    if (mediaPlayer == null)
                        mediaPlayer = new MediaPlayer();
                    else if (!mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
                        initVisualizer();
                    }
                    mediaPlayer.setVolume(1.0f, 1.0f);
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    // 失去audio focus很长一段时间，必须停止所有的audio播放，清理资源
                    if (mediaPlayer != null) {
                        if (mediaPlayer.isPlaying())
                            mediaPlayer.stop();
                        mediaPlayer.reset();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    // 暂时失去audio focus，但是很快就会重新获得，在此状态应该暂停所有音频播放，但是不能清除资源
                    if (mediaPlayer.isPlaying())
                        mediaPlayer.pause();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    // 暂时失去 audio focus，但是允许持续播放音频(以很小的声音)，不需要完全停止播放。
                    if (mediaPlayer.isPlaying())
                        mediaPlayer.setVolume(0.1f, 0.1f);
                    break;
            }
        }
    };

    // 音频数据监听
    private final Visualizer.OnDataCaptureListener dataCaptureListener = new Visualizer.OnDataCaptureListener() {

        // 音频数据
        @Override
        public void onWaveFormDataCapture(Visualizer visualizer, final byte[] waveform, int samplingRate) {
            if (mediaPlayerListener != null)
                mediaPlayerListener.onWaveFormDataCapture(visualizer, waveform, samplingRate);
        }

        // 傅里叶数据
        @Override
        public void onFftDataCapture(Visualizer visualizer, final byte[] fft, int samplingRate) {
            if (mediaPlayerListener != null)
                mediaPlayerListener.onFftDataCapture(visualizer, fft, samplingRate);
        }
    };

    /**
     * 初始化Visualizer
     */
    public void initVisualizer() {
        try {
            if (mediaPlayer != null) {
                visualizerDestroy();
                visualizer = new Visualizer(mediaPlayer.getAudioSessionId());

                /*
                 *可视化数据的大小：
                 * getCaptureSizeRange()[0]为最小值
                 * getCaptureSizeRange()[1]为最大值
                 */
                int captureSize = Visualizer.getCaptureSizeRange()[1];
                int captureRate = Visualizer.getMaxCaptureRate() * 3 / 4;
                visualizer.setCaptureSize(captureSize);

                visualizer.setDataCaptureListener(dataCaptureListener, captureRate, true, true);
                visualizer.setScalingMode(Visualizer.SCALING_MODE_NORMALIZED);
                visualizer.setEnabled(true);
            }
        } catch (Exception e) {
            Log.d(TAG, "请检查录音权限");
        }
    }

    // 监听接口
    public interface MediaPlayerListener {
        boolean onError(MediaPlayer mediaPlayer, int i, int i1);

        void onCompletion(MediaPlayer mediaPlayer);

        void onPrepared(MediaPlayer mediaPlayer);

        void onSeekComplete(MediaPlayer mediaPlayer);

        void onBufferingUpdate(MediaPlayer mediaPlayer, int i);

        void onFftDataCapture(Visualizer visualizer, final byte[] fft, int samplingRate);

        void onWaveFormDataCapture(Visualizer visualizer, final byte[] waveform, int samplingRate);
    }

    private MediaPlayerListener mediaPlayerListener;

    public MediaPlayerProxy setMediaPlayerListener(MediaPlayerListener mediaPlayerListener) {
        this.mediaPlayerListener = mediaPlayerListener;
        return this;
    }
}
