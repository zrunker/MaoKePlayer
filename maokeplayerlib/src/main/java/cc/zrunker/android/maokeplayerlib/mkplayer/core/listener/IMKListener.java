package cc.zrunker.android.maokeplayerlib.mkplayer.core.listener;

import cc.zrunker.android.maokeplayerlib.mkplayer.core.MKPlayer;
import tv.danmaku.ijk.media.player.IjkTimedText;

/**
 * @program: ZMaoKePlayer
 * @description: 监听接口
 * @author: zoufengli01
 * @create: 2021/12/9 2:38 下午
 **/
public interface IMKListener {

    /**
     * 设置播放异常监听
     */
    void setOnErrorListener(OnErrorListener listener);

    /**
     * 设置预播放监听
     */
    void setOnPreparedListener(OnPreparedListener listener);

    /**
     * 设置播放完成监听
     */
    void setOnCompletionListener(OnCompletionListener listener);

    /**
     * 设置Buffer更新监听
     */
    void setOnBufferingUpdateListener(OnBufferingUpdateListener listener);

    /**
     * 媒体信息回调
     */
    void setOnInfoListener(OnInfoListener listener);

    /**
     * Seek完成回调
     */
    void setOnSeekCompleteListener(OnSeekCompleteListener listener);

    /**
     * 可用定时文本回调 - 字幕
     */
    void setOnTimedTextListener(OnTimedTextListener listener);

    /**
     * 视频大小改变监听
     */
    void setOnVideoSizeChangedListener(OnVideoSizeChangedListener listener);

    interface OnErrorListener {
        void onError(MKPlayer mkPlayer, int what, int extra, String error);
    }

    interface OnPreparedListener {
        void onPrepared(MKPlayer mkPlayer);
    }

    interface OnTimedTextListener {
        void onTimedText(MKPlayer mkPlayer, IjkTimedText text);
    }

    interface OnInfoListener {
        boolean onInfo(MKPlayer mkPlayer, int what, int extra);
    }

    interface OnVideoSizeChangedListener {
        void onVideoSizeChanged(MKPlayer mkPlayer, int width, int height,
                                int sar_num, int sar_den);
    }

    interface OnSeekCompleteListener {
        void onSeekComplete(MKPlayer mkPlayer);
    }

    interface OnBufferingUpdateListener {
        void onBufferingUpdate(MKPlayer mkPlayer, int percent);
    }

    interface OnCompletionListener {
        void onCompletion(MKPlayer mkPlayer);
    }

}
