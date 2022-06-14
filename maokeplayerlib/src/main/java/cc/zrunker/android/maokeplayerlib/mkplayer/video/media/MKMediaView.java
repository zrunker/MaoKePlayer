package cc.zrunker.android.maokeplayerlib.mkplayer.video.media;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PixelFormat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.HashSet;
import java.util.Set;

import cc.zrunker.android.maokeplayerlib.R;
import cc.zrunker.android.maokeplayerlib.mkplayer.core.MKPlayer;
import cc.zrunker.android.maokeplayerlib.mkplayer.core.listener.IMKListener;
import tv.danmaku.ijk.media.player.IjkTimedText;

/**
 * @program: ZMaoKePlayer
 * @description: 媒体View
 * @author: zoufengli01
 * @create: 2021/12/6 11:38 上午
 **/
public class MKMediaView extends SurfaceView
        implements SurfaceHolder.Callback, IMKMediaView {
    private MKPlayer mkPlayer;
    private boolean isCanPlay;
    private String mediaPath;
    private IMKListener.OnErrorListener onErrorListener;

    // 绑定监听
    private final Set<SurfaceHolder.Callback> holderCallBackSet = new HashSet<>();
    private final Set<IMKListener.OnPreparedListener> onPreparedListenerSet = new HashSet<>();
    private final Set<IMKListener.OnErrorListener> mkErrorListenerSet = new HashSet<>();
    private final Set<IMKListener.OnCompletionListener> onCompletionListenerSet = new HashSet<>();
    private final Set<IMKListener.OnBufferingUpdateListener> onBufferingUpdateListenerSet = new HashSet<>();
    private final Set<IMKListener.OnInfoListener> onInfoListenerSet = new HashSet<>();
    private final Set<IMKListener.OnSeekCompleteListener> onSeekCompleteListenerSet = new HashSet<>();
    private final Set<IMKListener.OnTimedTextListener> onTimedTextListenerSet = new HashSet<>();
    private final Set<IMKListener.OnVideoSizeChangedListener> onVideoSizeChangedListenerSet = new HashSet<>();

    // 视频居于Z轴顶部
    private final boolean isZOrderOnTop;
    // 视频自动大小
    private final boolean isAutoSize;
    // 保持屏幕常亮
    private final boolean isKeepScreenOn;

    public MKMediaView(Context context) {
        this(context, null);
    }

    public MKMediaView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MKMediaView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArr = context.obtainStyledAttributes(attrs, R.styleable.MKMediaView);
        isZOrderOnTop = typedArr.getBoolean(R.styleable.MKMediaView_isZOrderOnTop, true);
        isAutoSize = typedArr.getBoolean(R.styleable.MKMediaView_isAutoSize, true);
        isKeepScreenOn = typedArr.getBoolean(R.styleable.MKMediaView_isKeepScreenOn, true);
        typedArr.recycle();

        // 初始化
        init();
    }

    private void init() {
        if (isZOrderOnTop) {
            // 置顶支持透明
            setZOrderOnTop(true);
            // 背景色跟主题一致
            setZOrderMediaOverlay(true);
        }
        // 设置SurfaceHolder
        SurfaceHolder sHolder = getHolder();
        sHolder.addCallback(this);
        sHolder.setKeepScreenOn(isKeepScreenOn);
        sHolder.setFormat(PixelFormat.TRANSPARENT);
        // 处理焦点
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        // 初始化MKPlayer
        mkPlayer = new MKPlayer();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isCanPlay = true;
        requestFocus();
        setDisplay(holder);
        if (!TextUtils.isEmpty(mediaPath) && !isPlaying()) {
            if (!mediaPath.equals(getDataSource())) {
                prepareAsync(mediaPath);
            } else {
                start();
            }
        }
        for (SurfaceHolder.Callback item : holderCallBackSet) {
            if (item != null) {
                item.surfaceCreated(holder);
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        for (SurfaceHolder.Callback item : holderCallBackSet) {
            if (item != null) {
                item.surfaceChanged(holder, format, width, height);
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isCanPlay = false;
        setDisplay(null);
        if (isPlaying()) {
            pause();
        }
        for (SurfaceHolder.Callback item : holderCallBackSet) {
            if (item != null) {
                item.surfaceDestroyed(holder);
            }
        }
    }

    @Override
    public void addHolderCallBack(SurfaceHolder.Callback holderCallBack) {
        if (holderCallBack != null) {
            holderCallBackSet.add(holderCallBack);
        }
    }

    @Override
    public void removeHolderCallBack(SurfaceHolder.Callback holderCallBack) {
        holderCallBackSet.remove(holderCallBack);
    }

    @Override
    public boolean isLooping() {
        return mkPlayer.isLooping();
    }

    @Override
    public void setScreenOnWhilePlaying(boolean bool) {
        mkPlayer.setScreenOnWhilePlaying(bool);
    }

    @Override
    public void setAudioStreamType(int streamType) {
        mkPlayer.setAudioStreamType(streamType);
    }

    @Override
    public void setVolume(float left, float right) {
        mkPlayer.setVolume(left, right);
    }

    @Override
    public void setLooping(boolean looping) {
        mkPlayer.setLooping(looping);
    }

    @Override
    public boolean isPlaying() {
        return mkPlayer.isPlaying();
    }

    @Override
    public void prepareAsync(String path) {
        String error = null;
        if (TextUtils.isEmpty(path)) {
            error = "播放地址不能为空！";
        } else {
            this.mediaPath = path;
            if (isCanPlay) {
                try {
                    mkPlayer.prepareAsync(path);
                } catch (Exception e) {
                    error = e.getMessage();
                }
            }
        }
        if (onErrorListener != null && !TextUtils.isEmpty(error)) {
            onErrorListener.onError(null, 0, 0, error);
        }
    }

    @Override
    public void setDisplay(SurfaceHolder sHolder) {
        mkPlayer.setDisplay(sHolder);
    }

    @Override
    public void start() {
        mkPlayer.start();
    }

    @Override
    public void pause() {
        mkPlayer.pause();
    }

    @Override
    public void stop() {
        mkPlayer.stop();
    }

    @Override
    public void release() {
        mkPlayer.release();
    }

    @Override
    public void reset() {
        mkPlayer.reset();
    }

    @Override
    public long getCurrentPosition() {
        return mkPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(long position) {
        mkPlayer.seekTo(position);
    }

    @Override
    public long getDuration() {
        return mkPlayer.getDuration();
    }

    @Override
    public String getDataSource() {
        return mkPlayer.getDataSource();
    }

    @Override
    public int getVideoWidth() {
        return mkPlayer.getVideoWidth();
    }

    @Override
    public int getVideoHeight() {
        return mkPlayer.getVideoHeight();
    }

    @Override
    public int getVideoSarNum() {
        return mkPlayer.getVideoSarNum();
    }

    @Override
    public int getVideoSarDen() {
        return mkPlayer.getVideoSarDen();
    }

    @Override
    public int getAudioSessionId() {
        return mkPlayer.getAudioSessionId();
    }

    @Override
    public void setWakeMode(Context context, int level) {
        mkPlayer.setWakeMode(context, level);
    }

    @Override
    public void addOnErrorListener(IMKListener.OnErrorListener listener) {
        if (listener != null) {
            mkErrorListenerSet.add(listener);
        }
        if (mkPlayer.getMkListener() != null) {
            onErrorListener = new IMKListener.OnErrorListener() {
                @Override
                public void onError(MKPlayer mkPlayer, int what, int extra, String error) {
                    for (IMKListener.OnErrorListener item : mkErrorListenerSet) {
                        if (item != null) {
                            item.onError(mkPlayer, what, extra, error);
                        }
                    }
                }
            };
            mkPlayer.getMkListener().setOnErrorListener(onErrorListener);
        }
    }

    @Override
    public void removeOnErrorListener(IMKListener.OnErrorListener listener) {
        mkErrorListenerSet.remove(listener);
    }

    @Override
    public void addOnPreparedListener(IMKListener.OnPreparedListener listener) {
        if (listener != null) {
            onPreparedListenerSet.add(listener);
        }
        // 设置监听
        if (mkPlayer.getMkListener() != null) {
            mkPlayer.getMkListener().setOnPreparedListener(new IMKListener.OnPreparedListener() {
                @Override
                public void onPrepared(MKPlayer mkPlayer) {
                    // 设置SurfaceView大小
                    if (isAutoSize) {
                        int width = mkPlayer.getVideoWidth();
                        int height = mkPlayer.getVideoHeight();
                        getHolder().setFixedSize(width, height);
                    }
                    for (IMKListener.OnPreparedListener item : onPreparedListenerSet) {
                        if (item != null) {
                            item.onPrepared(mkPlayer);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void removeOnPreparedListener(IMKListener.OnPreparedListener listener) {
        onPreparedListenerSet.remove(listener);
    }

    @Override
    public void addOnCompletionListener(IMKListener.OnCompletionListener listener) {
        if (listener != null) {
            onCompletionListenerSet.add(listener);
        }
        if (mkPlayer.getMkListener() != null) {
            mkPlayer.getMkListener().setOnCompletionListener(new IMKListener.OnCompletionListener() {
                @Override
                public void onCompletion(MKPlayer mkPlayer) {
                    for (IMKListener.OnCompletionListener item : onCompletionListenerSet) {
                        if (item != null) {
                            item.onCompletion(mkPlayer);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void removeOnCompletionListener(IMKListener.OnCompletionListener listener) {
        onCompletionListenerSet.remove(listener);
    }

    @Override
    public void addOnBufferingUpdateListener(IMKListener.OnBufferingUpdateListener listener) {
        if (listener != null) {
            onBufferingUpdateListenerSet.add(listener);
        }
        if (mkPlayer.getMkListener() != null) {
            mkPlayer.getMkListener().setOnBufferingUpdateListener(new IMKListener.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MKPlayer mkPlayer, int i) {
                    for (IMKListener.OnBufferingUpdateListener item : onBufferingUpdateListenerSet) {
                        if (item != null) {
                            item.onBufferingUpdate(mkPlayer, i);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void removeOnBufferingUpdateListener(IMKListener.OnBufferingUpdateListener listener) {
        onBufferingUpdateListenerSet.remove(listener);
    }

    @Override
    public void addOnInfoListener(IMKListener.OnInfoListener listener) {
        if (listener != null) {
            onInfoListenerSet.add(listener);
        }
        if (mkPlayer.getMkListener() != null) {
            mkPlayer.getMkListener().setOnInfoListener(new IMKListener.OnInfoListener() {
                @Override
                public boolean onInfo(MKPlayer mkPlayer, int i, int i1) {
                    for (IMKListener.OnInfoListener item : onInfoListenerSet) {
                        if (item != null) {
                            item.onInfo(mkPlayer, i, i1);
                        }
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public void removeOnInfoListener(IMKListener.OnInfoListener listener) {
        onInfoListenerSet.remove(listener);
    }

    @Override
    public void addOnSeekCompleteListener(IMKListener.OnSeekCompleteListener listener) {
        if (listener != null) {
            onSeekCompleteListenerSet.add(listener);
        }
        if (mkPlayer.getMkListener() != null) {
            mkPlayer.getMkListener().setOnSeekCompleteListener(new IMKListener.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MKPlayer mkPlayer) {
                    for (IMKListener.OnSeekCompleteListener item : onSeekCompleteListenerSet) {
                        if (item != null) {
                            item.onSeekComplete(mkPlayer);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void removeOnSeekCompleteListener(IMKListener.OnSeekCompleteListener listener) {
        onSeekCompleteListenerSet.remove(listener);
    }

    @Override
    public void addOnTimedTextListener(IMKListener.OnTimedTextListener listener) {
        if (listener != null) {
            onTimedTextListenerSet.add(listener);
        }
        if (mkPlayer.getMkListener() != null) {
            mkPlayer.getMkListener().setOnTimedTextListener(new IMKListener.OnTimedTextListener() {
                @Override
                public void onTimedText(MKPlayer mkPlayer, IjkTimedText ijkTimedText) {
                    for (IMKListener.OnTimedTextListener item : onTimedTextListenerSet) {
                        if (item != null) {
                            item.onTimedText(mkPlayer, ijkTimedText);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void removeOnTimedTextListener(IMKListener.OnTimedTextListener listener) {
        onTimedTextListenerSet.remove(listener);
    }

    @Override
    public void addOnVideoSizeChangedListener(IMKListener.OnVideoSizeChangedListener listener) {
        if (listener != null) {
            onVideoSizeChangedListenerSet.add(listener);
        }
        if (mkPlayer.getMkListener() != null) {
            mkPlayer.getMkListener().setOnVideoSizeChangedListener(new IMKListener.OnVideoSizeChangedListener() {
                @Override
                public void onVideoSizeChanged(MKPlayer mkPlayer, int i, int i1, int i2, int i3) {
                    for (IMKListener.OnVideoSizeChangedListener item : onVideoSizeChangedListenerSet) {
                        if (item != null) {
                            item.onVideoSizeChanged(mkPlayer, i, i1, i2, i3);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void removeOnVideoSizeChangedListener(IMKListener.OnVideoSizeChangedListener listener) {
        onVideoSizeChangedListenerSet.remove(listener);
    }
}
