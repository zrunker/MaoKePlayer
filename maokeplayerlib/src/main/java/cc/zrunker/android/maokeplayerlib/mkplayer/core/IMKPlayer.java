package cc.zrunker.android.maokeplayerlib.mkplayer.core;

import android.content.Context;
import android.net.Uri;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.Map;

/**
 * @program: ZMaoKePlayer
 * @description: 媒体播放对外接口
 * @author: zoufengli01
 * @create: 2021/12/6 11:12 上午
 **/
public interface IMKPlayer {

    /**
     * 是否循环
     */
    boolean isLooping();

    /**
     * 屏幕常亮
     */
    void setScreenOnWhilePlaying(boolean bool);

    /**
     * 设置流的类型
     *
     * @param streamtype 类型，如：AudioManager.STREAM_MUSIC
     */
    void setAudioStreamType(int streamtype);

    /**
     * 设置音量
     */
    void setVolume(float left, float right);

    /**
     * 是否循环播放
     *
     * @param looping 是否循环
     */
    void setLooping(boolean looping);

    /**
     * 是否正在播放
     */
    boolean isPlaying();

    /**
     * 播放指定路径视频
     *
     * @param path 资源路径
     */
    void prepareAsync(String path) throws IOException;

    void prepareAsync();

    /**
     * 设置呈现
     *
     * @param sHolder Holder显示
     */
    void setDisplay(SurfaceHolder sHolder);

    /**
     * 开始播放
     */
    void start();

    /**
     * 暂停播放
     */
    void pause();

    /**
     * 停止播放
     */
    void stop();

    /**
     * 回收
     */
    void release();

    /**
     * 重置
     */
    void reset();

    /**
     * 获取当前进度
     */
    long getCurrentPosition();

    /**
     * 设置进度
     *
     * @param position 进度值
     */
    void seekTo(long position);

    /**
     * 获取资源总长度
     */
    long getDuration();

    /**
     * 获取播放资源
     */
    String getDataSource();

    /**
     * 获取视频宽度
     */
    int getVideoWidth();

    /**
     * 获取视频高度
     */
    int getVideoHeight();

    /**
     * 采样长宽比:num / den
     */
    int getVideoSarNum();

    /**
     * 采样长宽比:num / den
     */
    int getVideoSarDen();

    /**
     * 获取Audio Session ID
     */
    int getAudioSessionId();

    /**
     * 设置唤醒模式
     */
    void setWakeMode(Context context, int level);

    void setDataSource(Context context, Uri uri) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException;

    void setDataSource(Context context, Uri uri, Map<String, String> map) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException;

    void setDataSource(String path) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException;

}
