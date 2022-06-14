package cc.zrunker.android.maokeplayerlib.mkplayer.video.media;

import android.view.SurfaceHolder;

import cc.zrunker.android.maokeplayerlib.mkplayer.core.IMKPlayer;
import cc.zrunker.android.maokeplayerlib.mkplayer.core.listener.IMKListener;

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
    void addOnErrorListener(IMKListener.OnErrorListener listener);

    void removeOnErrorListener(IMKListener.OnErrorListener listener);

    /**
     * 设置预播放监听
     */
    void addOnPreparedListener(IMKListener.OnPreparedListener listener);

    void removeOnPreparedListener(IMKListener.OnPreparedListener listener);

    /**
     * 设置播放完成监听
     */
    void addOnCompletionListener(IMKListener.OnCompletionListener listener);

    void removeOnCompletionListener(IMKListener.OnCompletionListener listener);

    /**
     * 设置Buffer更新监听
     */
    void addOnBufferingUpdateListener(IMKListener.OnBufferingUpdateListener listener);

    void removeOnBufferingUpdateListener(IMKListener.OnBufferingUpdateListener listener);

    /**
     * 媒体信息回调
     */
    void addOnInfoListener(IMKListener.OnInfoListener listener);

    void removeOnInfoListener(IMKListener.OnInfoListener listener);

    /**
     * Seek完成回调
     */
    void addOnSeekCompleteListener(IMKListener.OnSeekCompleteListener listener);

    void removeOnSeekCompleteListener(IMKListener.OnSeekCompleteListener listener);

    /**
     * 可用定时文本回调 - 字幕
     */
    void addOnTimedTextListener(IMKListener.OnTimedTextListener listener);

    void removeOnTimedTextListener(IMKListener.OnTimedTextListener listener);

    /**
     * 视频大小改变监听
     */
    void addOnVideoSizeChangedListener(IMKListener.OnVideoSizeChangedListener listener);

    void removeOnVideoSizeChangedListener(IMKListener.OnVideoSizeChangedListener listener);
}
