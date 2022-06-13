package cc.zrunker.android.maokeplayerlib.mkplayer.video.media;

import android.view.SurfaceHolder;

import cc.zrunker.android.maokeplayerlib.mkplayer.core.IMKPlayer;
import cc.zrunker.android.maokeplayerlib.mkplayer.core.listener.MKErrorListener;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * @program: ZMaoKePlayer
 * @description: 媒体View接口
 * @author: zoufengli01
 * @create: 2021/12/6 3:08 下午
 **/
public interface IMKMediaView extends IMKPlayer {

    /**
     * 对SurfaceHolder状态变化监听
     */
    void addHolderCallBack(SurfaceHolder.Callback holderCallBack);

    void removeHolderCallBack(SurfaceHolder.Callback holderCallBack);

    /**
     * 设置播放异常监听
     */
    void addOnErrorListener(MKErrorListener listener);

    void removeOnErrorListener(MKErrorListener listener);

    /**
     * 设置预播放监听
     */
    void addOnPreparedListener(IMediaPlayer.OnPreparedListener listener);

    void removeOnPreparedListener(IMediaPlayer.OnPreparedListener listener);

    /**
     * 设置播放完成监听
     */
    void addOnCompletionListener(IMediaPlayer.OnCompletionListener listener);

    void removeOnCompletionListener(IMediaPlayer.OnCompletionListener listener);

    /**
     * 设置Buffer更新监听
     */
    void addOnBufferingUpdateListener(IMediaPlayer.OnBufferingUpdateListener listener);

    void removeOnBufferingUpdateListener(IMediaPlayer.OnBufferingUpdateListener listener);

    /**
     * 媒体信息回调
     */
    void addOnInfoListener(IMediaPlayer.OnInfoListener listener);

    void removeOnInfoListener(IMediaPlayer.OnInfoListener listener);

    /**
     * Seek完成回调
     */
    void addOnSeekCompleteListener(IMediaPlayer.OnSeekCompleteListener listener);

    void removeOnSeekCompleteListener(IMediaPlayer.OnSeekCompleteListener listener);

    /**
     * 可用定时文本回调 - 字幕
     */
    void addOnTimedTextListener(IMediaPlayer.OnTimedTextListener listener);

    void removeOnTimedTextListener(IMediaPlayer.OnTimedTextListener listener);

    /**
     * 视频大小改变监听
     */
    void addOnVideoSizeChangedListener(IMediaPlayer.OnVideoSizeChangedListener listener);

    void removeOnVideoSizeChangedListener(IMediaPlayer.OnVideoSizeChangedListener listener);
}
