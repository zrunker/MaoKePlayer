package cc.zrunker.android.maokeplayerlib.mkplayer.core.listener;

import cc.zrunker.android.maokeplayerlib.mkplayer.core.MKPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkTimedText;

/**
 * @program: ZMaoKePlayer
 * @description: 监听器
 * @author: zoufengli01
 * @create: 2021/12/9 3:38 下午
 **/
public class MKListener implements IMKListener {
    private final MKPlayer mkPlayer;
    private OnErrorListener onErrorListener;
    private OnPreparedListener onPreparedListener;
    private OnCompletionListener onCompletionListener;
    private OnBufferingUpdateListener onBufferingUpdateListener;
    private OnInfoListener onInfoListener;
    private OnSeekCompleteListener onSeekCompleteListener;
    private OnTimedTextListener onTimedTextListener;
    private OnVideoSizeChangedListener onVideoSizeChangedListener;

    public MKListener(MKPlayer mkPlayer) {
        this.mkPlayer = mkPlayer;
        if (onPreparedListener != null) {
            setOnPreparedListener(onPreparedListener);
        }
        if (onErrorListener != null) {
            setOnErrorListener(onErrorListener);
        }
        if (onCompletionListener != null) {
            setOnCompletionListener(onCompletionListener);
        }
        if (onBufferingUpdateListener != null) {
            setOnBufferingUpdateListener(onBufferingUpdateListener);
        }
        if (onInfoListener != null) {
            setOnInfoListener(onInfoListener);
        }
        if (onSeekCompleteListener != null) {
            setOnSeekCompleteListener(onSeekCompleteListener);
        }
        if (onTimedTextListener != null) {
            setOnTimedTextListener(onTimedTextListener);
        }
        if (onVideoSizeChangedListener != null) {
            setOnVideoSizeChangedListener(onVideoSizeChangedListener);
        }
    }

    @Override
    public void setOnPreparedListener(OnPreparedListener listener) {
        if (listener != null) {
            this.onPreparedListener = listener;
            if (mkPlayer != null) {
                mkPlayer.getMediaPlayer().setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(IMediaPlayer iMediaPlayer) {
                        onPreparedListener.onPrepared(mkPlayer);
                    }
                });
            }
        }
    }

    @Override
    public void setOnCompletionListener(OnCompletionListener listener) {
        if (listener != null) {
            this.onCompletionListener = listener;
            if (mkPlayer != null) {
                mkPlayer.getMediaPlayer().setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(IMediaPlayer iMediaPlayer) {
                        onCompletionListener.onCompletion(mkPlayer);
                    }
                });
            }
        }
    }

    @Override
    public void setOnBufferingUpdateListener(OnBufferingUpdateListener listener) {
        if (listener != null) {
            this.onBufferingUpdateListener = listener;
            if (mkPlayer != null) {
                mkPlayer.getMediaPlayer().setOnBufferingUpdateListener(new IMediaPlayer.OnBufferingUpdateListener() {
                    @Override
                    public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {
                        onBufferingUpdateListener.onBufferingUpdate(mkPlayer, i);
                    }
                });
            }
        }
    }

    @Override
    public void setOnInfoListener(OnInfoListener listener) {
        if (listener != null) {
            this.onInfoListener = listener;
            if (mkPlayer != null) {
                mkPlayer.getMediaPlayer().setOnInfoListener(new IMediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
                        onInfoListener.onInfo(mkPlayer, i, i1);
                        return false;
                    }
                });
            }
        }
    }

    @Override
    public void setOnSeekCompleteListener(OnSeekCompleteListener listener) {
        if (listener != null) {
            this.onSeekCompleteListener = listener;
            if (mkPlayer != null) {
                mkPlayer.getMediaPlayer().setOnSeekCompleteListener(new IMediaPlayer.OnSeekCompleteListener() {
                    @Override
                    public void onSeekComplete(IMediaPlayer iMediaPlayer) {
                        onSeekCompleteListener.onSeekComplete(mkPlayer);
                    }
                });
            }
        }
    }

    @Override
    public void setOnTimedTextListener(OnTimedTextListener listener) {
        if (listener != null) {
            this.onTimedTextListener = listener;
            if (mkPlayer != null) {
                mkPlayer.getMediaPlayer().setOnTimedTextListener(new IMediaPlayer.OnTimedTextListener() {
                    @Override
                    public void onTimedText(IMediaPlayer iMediaPlayer, IjkTimedText ijkTimedText) {
                        onTimedTextListener.onTimedText(mkPlayer, ijkTimedText);
                    }
                });
            }
        }
    }

    @Override
    public void setOnVideoSizeChangedListener(OnVideoSizeChangedListener listener) {
        if (listener != null) {
            this.onVideoSizeChangedListener = listener;
            if (mkPlayer != null) {
                mkPlayer.getMediaPlayer().setOnVideoSizeChangedListener(new IMediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {
                        onVideoSizeChangedListener.onVideoSizeChanged(mkPlayer, i, i1, i2, i3);
                    }
                });
            }
        }
    }

    @Override
    public void setOnErrorListener(OnErrorListener listener) {
        if (listener != null) {
            this.onErrorListener = listener;
            if (mkPlayer != null) {
                mkPlayer.getMediaPlayer().setOnErrorListener(new IMediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(IMediaPlayer iMediaPlayer, int what, int extra) {
                        listener.onError(mkPlayer, what, extra, ErrorTrans.trans(what));
                        return false;
                    }
                });
            }
        }
    }
}
