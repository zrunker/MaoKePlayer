package cc.zrunker.android.maokeplayerlib.mkplayer.audio;

import android.content.Context;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.AutomaticGainControl;
import android.media.audiofx.BassBoost;
import android.media.audiofx.NoiseSuppressor;
import android.media.audiofx.Visualizer;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import cc.zrunker.android.maokeplayerlib.mkplayer.audio.visualizer.view.VisualizerView;
import cc.zrunker.android.maokeplayerlib.mkplayer.core.MKPlayer;
import cc.zrunker.android.maokeplayerlib.mkplayer.core.listener.IMKListener;

/**
 * @program: ZMaoKePlayer
 * @description: 音频执行类，需要动态申请录音权限，否则无法展示音频效果
 * @author: zoufengli01
 * @create: 2022/6/13 16:15
 **/
public class AudioExecutor {
    private final String TAG = AudioExecutor.class.getSimpleName();
    private final Context context;
    private MKPlayer mkPlayer;
    private AudioManager audioManager;
    private WifiManager.WifiLock wifiLock;
    private Visualizer visualizer;// 音频波形图
    private AcousticEchoCanceler acousticEchoCanceler;// 回声消除器
    private AutomaticGainControl automaticGainControl;// 自动增强控制器
    private NoiseSuppressor noiseSuppressor;// 噪音抑制器
    private BassBoost bassBoost;// 重低音调节器

    private VisualizerView visualizerView;

    public AudioExecutor setVisualizerView(VisualizerView visualizerView) {
        this.visualizerView = visualizerView;
        if (visualizerView != null && !visualizerView.isAddRenderer()) {
            // 添加默认Renderer
            visualizerView.addCircleRenderer();
        }
        return this;
    }

    public AudioExecutor(Context context) {
        this.context = context;
        init();
    }

    public AudioExecutor(Context context, VisualizerView visualizerView) {
        this.context = context;
        setVisualizerView(visualizerView);
        init();
    }

    // 初始化
    private void init() {
        initMKPlayer();
        initVisualizer();
        if (mkPlayer != null) {
            int audioSession = mkPlayer.getAudioSessionId();
            try {
                acousticEchoCanceler = AcousticEchoCanceler.create(audioSession);
                if (AcousticEchoCanceler.isAvailable() && acousticEchoCanceler != null) {
                    acousticEchoCanceler.setEnabled(true);
                }
                automaticGainControl = AutomaticGainControl.create(audioSession);
                if (AutomaticGainControl.isAvailable() && automaticGainControl != null) {
                    automaticGainControl.setEnabled(true);
                }
                noiseSuppressor = NoiseSuppressor.create(audioSession);
                if (NoiseSuppressor.isAvailable() && noiseSuppressor != null) {
                    noiseSuppressor.setEnabled(true);
                }
                bassBoost = new BassBoost(0, audioSession);
                bassBoost.setStrength((short) 1000);
            } catch (Exception e) {
                Log.e(TAG, "请检查录音权限");
            }
        }
    }

    // 获取AudioManager
    private AudioManager getAudioManager() {
        if (audioManager == null) {
            audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        }
        return audioManager;
    }

    // 获取音量
    public int getStreamVolume() {
        return getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    public int getStreamVolume(int streamType) {
        if (audioManager != null) {
            return audioManager.getStreamVolume(streamType);
        }
        return 0;
    }

    // 获取最大音量
    public int getStreamMaxVolume() {
        return getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    public int getStreamMaxVolume(int streamType) {
        if (audioManager != null) {
            return audioManager.getStreamMaxVolume(streamType);
        }
        return 0;
    }

    /**
     * 设置音量
     *
     * @param streamType 流类型
     * @param index      大小
     * @param flags      种类-特征
     */
    public AudioExecutor setStreamVolume(int streamType, int index, int flags) {
        if (audioManager != null) {
            audioManager.setStreamVolume(streamType, index, flags);
        }
        return this;
    }

    // 开始播放
    public AudioExecutor start() {
        if (mkPlayer != null) {
            mkPlayer.start();
        }
        initVisualizer();
        return this;
    }

    // 重置
    public AudioExecutor reset() {
        if (mkPlayer != null) {
            mkPlayer.pause();
            mkPlayer.stop();
            mkPlayer.reset();
            if (visualizer != null) {
                visualizer.setEnabled(true);
            }
        }
        return this;
    }

    /**
     * 快进
     *
     * @param msec 毫秒数
     */
    public AudioExecutor seekTo(int msec) {
        if (mkPlayer != null) {
            mkPlayer.seekTo(msec);
        }
        return this;
    }

    // 获取音频总时长
    public long getDuration() {
        if (mkPlayer != null) {
            return mkPlayer.getDuration();
        }
        return 0;
    }

    // 获取音频当前进度
    public long getCurrentPosition() {
        if (mkPlayer != null) {
            return mkPlayer.getCurrentPosition();
        }
        return 0;
    }

    // 是否正在播放
    public boolean isPlaying() {
        return mkPlayer != null && mkPlayer.isPlaying();
    }

    // 播放
    public AudioExecutor play(String url) {
        if (!TextUtils.isEmpty(url)) {
            // 开启播放
            if (mkPlayer == null) {
                init();
            }
//            // 重置MKPlayer
//            mkPlayer.reset();
            // 准备播放（异步）
            try {
                mkPlayer.prepareAsync(url);
            } catch (IOException e) {
                e.printStackTrace();
                if (mkPlayerListener != null) {
                    mkPlayerListener.onError(mkPlayer, 0, 0, e.getMessage());
                }
            }
        } else {
            if (mkPlayerListener != null) {
                mkPlayerListener.onError(mkPlayer, 0, 0, "播放地址为空！");
            }
        }
        return this;
    }

    // 暂停
    public AudioExecutor pause() {
        if (mkPlayer != null) {
            mkPlayer.pause();
        }
        return this;
    }

    // 停止
    public AudioExecutor stop() {
        if (mkPlayer != null) {
            mkPlayer.stop();
        }
        return this;
    }

    // 销毁
    public void destroy() {
        if (mkPlayer != null) {
            mkPlayer.pause();
            mkPlayer.stop();
            mkPlayer.reset();
            mkPlayer.release();
            mkPlayer = null;
        }
        if (wifiLock != null) {
            wifiLock.release();
            wifiLock = null;
        }
        if (acousticEchoCanceler != null) {
            acousticEchoCanceler.release();
        }
        if (automaticGainControl != null) {
            automaticGainControl.release();
        }
        if (noiseSuppressor != null) {
            noiseSuppressor.release();
        }
        if (bassBoost != null) {
            bassBoost.release();
        }
        visualizerDestroy();
    }

    // 销毁visualizer
    private void visualizerDestroy() {
        if (visualizer != null) {
            visualizer.setEnabled(false);
            visualizer.release();
        }
    }

    // 初始化MKPlayer
    private void initMKPlayer() {
        if (mkPlayer == null) {
            mkPlayer = new MKPlayer();
        }
        // 设置音量，参数分别表示左右声道声音大小，取值范围为0~1
        mkPlayer.setVolume(0.5f, 0.5f);

        // 设置是否循环播放
        mkPlayer.setLooping(false);

        // 设置设备进入锁状态模式-可在后台播放或者缓冲音乐-CPU一直工作
        mkPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
        // 当播放的时候一直让屏幕变亮
        mkPlayer.setScreenOnWhilePlaying(true);

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
        mkPlayer.getMkListener().setOnErrorListener(new IMKListener.OnErrorListener() {
            @Override
            public void onError(MKPlayer mkPlayer, int what, int extra, String error) {
                if (mkPlayerListener != null) {
                    mkPlayerListener.onError(mkPlayer, what, extra, error);
                }
                mkPlayer.pause();
                mkPlayer.stop();
                mkPlayer.reset();
            }
        });

        // 设置播放完成监听
        mkPlayer.getMkListener().setOnCompletionListener(new IMKListener.OnCompletionListener() {
            @Override
            public void onCompletion(MKPlayer mkPlayer) {
                if (visualizer != null)
                    visualizer.setEnabled(false);
                if (mkPlayerListener != null)
                    mkPlayerListener.onCompletion(mkPlayer);
            }
        });

        // 异步准备Prepared完成监听
        mkPlayer.getMkListener().setOnPreparedListener(new IMKListener.OnPreparedListener() {
            @Override
            public void onPrepared(MKPlayer mkPlayer) {
                if (mkPlayerListener != null) {
                    mkPlayerListener.onPrepared(mkPlayer);
                }
                mkPlayer.start();
                initVisualizer();
            }
        });

        // 进度调整完成SeekComplete监听
        mkPlayer.getMkListener().setOnSeekCompleteListener(new IMKListener.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MKPlayer mkPlayer) {
                if (mkPlayerListener != null) {
                    mkPlayerListener.onSeekComplete(mkPlayer);
                }
            }
        });

        // 网络流媒体缓冲监听
        mkPlayer.getMkListener().setOnBufferingUpdateListener(new IMKListener.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MKPlayer mkPlayer, int progress) {
                // i 0~100
                Log.d(TAG, "缓存进度" + progress + "%");
                if (mkPlayerListener != null) {
                    mkPlayerListener.onBufferingUpdate(mkPlayer, progress);
                }
            }
        });
    }

    private final AudioManager.OnAudioFocusChangeListener focusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (mkPlayer == null) {
                init();
            }
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    // 获取audio focus
                    if (mkPlayer == null) {
                        mkPlayer = new MKPlayer();
                    } else if (!mkPlayer.isPlaying()) {
                        mkPlayer.start();
                        initVisualizer();
                    }
                    mkPlayer.setVolume(1.0f, 1.0f);
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    // 失去audio focus很长一段时间，必须停止所有的audio播放，清理资源
                    if (mkPlayer != null) {
                        if (mkPlayer.isPlaying()) {
                            mkPlayer.stop();
                        }
                        mkPlayer.reset();
                        mkPlayer.release();
                        mkPlayer = null;
                    }
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    // 暂时失去audio focus，但是很快就会重新获得，在此状态应该暂停所有音频播放，但是不能清除资源
                    if (mkPlayer.isPlaying()) {
                        mkPlayer.pause();
                    }
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    // 暂时失去 audio focus，但是允许持续播放音频(以很小的声音)，不需要完全停止播放。
                    if (mkPlayer.isPlaying()) {
                        mkPlayer.setVolume(0.1f, 0.1f);
                    }
                    break;
            }
        }
    };

    // 音频数据监听
    private final Visualizer.OnDataCaptureListener dataCaptureListener = new Visualizer.OnDataCaptureListener() {

        // 音频数据
        @Override
        public void onWaveFormDataCapture(Visualizer visualizer, final byte[] waveform, int samplingRate) {
            if (visualizerView != null) {
                visualizerView.updateVisualizer(waveform);
            }
            if (mkPlayerListener != null) {
                mkPlayerListener.onWaveFormDataCapture(visualizer, waveform, samplingRate);
            }
        }

        // 傅里叶数据
        @Override
        public void onFftDataCapture(Visualizer visualizer, final byte[] fft, int samplingRate) {
            if (visualizerView != null) {
                visualizerView.updateVisualizerFFT(fft);
            }
            if (mkPlayerListener != null) {
                mkPlayerListener.onFftDataCapture(visualizer, fft, samplingRate);
            }
        }
    };

    /**
     * 初始化Visualizer
     */
    private void initVisualizer() {
        try {
            if (mkPlayer != null) {
                visualizerDestroy();
                visualizer = new Visualizer(mkPlayer.getAudioSessionId());

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
            Log.e(TAG, "请检查录音权限");
        }
    }

    // 对外监听接口
    public interface MKPlayerListener {
        boolean onError(MKPlayer mkPlayer, int i, int i1, String error);

        void onCompletion(MKPlayer mkPlayer);

        void onPrepared(MKPlayer mkPlayer);

        void onSeekComplete(MKPlayer mkPlayer);

        void onBufferingUpdate(MKPlayer mkPlayer, int i);

        void onFftDataCapture(Visualizer visualizer, final byte[] fft, int samplingRate);

        void onWaveFormDataCapture(Visualizer visualizer, final byte[] waveform, int samplingRate);
    }

    private MKPlayerListener mkPlayerListener;

    public AudioExecutor setMkPlayerListener(MKPlayerListener mkPlayerListener) {
        this.mkPlayerListener = mkPlayerListener;
        return this;
    }
}

