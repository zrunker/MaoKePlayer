# MaoKePlayer
猫客影音播放器 - 基于ijk打造的一款万能音视频播放器插件
- 支持AVI、WMV、MPEG、MP4、M4V、MOV、ASF、FLV、F4V、RMVB、RM、3GP、VOB等视频格式。
- 支持CD、WAVE、AIFF、MPEG、MP3、MPEG-4、MIDI、WMA、RealAudio、VQF、OggVorbis、AMR、APE、FLAC等音频格式。

## MKPlayer
MKPlayer媒体播放实现类，继承自IMKPlayer，用来对外提供了音视频播放、设置等一系列API，如：
```
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
}
```
如此同时，MKPlayer还支持视频配置，详情可查看：
`cc.zrunker.android.maokeplayerlib.mkplayer.core.option.IMKOption`

```
// 获取MKPlayer中默认视频配置IMKOption
public IMKOption getMkOption() {
    return mkOption;
}
```

## MKMediaView（视频）
MKMediaView是基于SurfaceView用来呈现视频渲染结果，此View不包含视频控制器，用户可以自行定制想要的控制器效果。

**使用：**
1. 在布局中添加MKMediaView：
```
<cc.zrunker.android.maokeplayerlib.mkplayer.video.media.MKMediaView
    android:id="@+id/media_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:isAutoSize="true"
    app:isKeepScreenOn="true"
    app:isZOrderOnTop="true" />
```
2. 执行MKMediaView播放：
```
MKMediaView mkMediaView = findViewById(R.id.media_view);
mkMediaView.addOnErrorListener(new IMKListener.OnErrorListener() {
    @Override
    public void onError(MKPlayer mkPlayer, int what, int extra, String error) {
                
    }
});
mkMediaView.prepareAsync("视频地址");
```

## 懒人模式 - MKVideoView（视频）
MKVideoView是针对于视频播放，在MKMediaView的基础上进行了包装。它支持绑定视频控制器，视频播放相关监听器等。

**使用：**
1. 在布局中添加MKVideoView：
```
<cc.zrunker.android.maokeplayerlib.mkplayer.video.MKVideoView
    android:id="@+id/mkVideo"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:isBindController="true" />
```
2. 执行MKVideoView播放：
```
MKVideoView mkVideoView = findViewById(R.id.mkVideo);
mkVideoView.setOnErrorListener(new IMKListener.OnErrorListener() {
    @Override
    public void onError(MKPlayer mkPlayer, int what, int extra, String error) {
        Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
    }
});
mkVideoView.play("视频地址");
```

关于视频控制器，详情可查看：`cc.zrunker.android.maokeplayerlib.mkplayer.video.controller.MKController`

```
// 获取MKVideoView中默认视频控制器
public MKController getDefaultController() {
    return defaultController;
}
```

![猫客影音播放器视频效果图](https://github.com/zrunker/ZMaoKePlayer/blob/v_1.0_ijk/device-2022-06-10-153103.png)

## AudioExecutor（音频 - 执行类）
AudioExecutor是专门针对于音频播放的封装类，它支持几乎所有常见的音频播放的操作API，例如播放音频：
```
AudioExecutor audioExecutor = new AudioExecutor(this);
audioExecutor.play("音频地址");
```

通常为防止内存泄露，会在Activity/Fragment的销毁方法中执行AudioExecutor的销毁事件：
```
@Override
protected void onDestroy() {
    super.onDestroy();
    audioExecutor.destroy();
}
```

## VisualizerView/AudioView（音频 - 音波图）
VisualizerView是用来显示音频的傅立叶数据图，目前支持四种音波效果图。

**使用：**
1. 在布局中添加VisualizerView：
```
<cc.zrunker.android.maokeplayerlib.mkplayer.audio.visualizer.view.VisualizerView
    android:id="@+id/visualizerView"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```
2. 绑定VisualizerView，并执行AudioExecutor播放，注意四种音波图选择一种即可，不选择将展示默认音波图效果：
```
VisualizerView visualizerView = findViewById(R.id.visualizerView);
// 第一种音波图，也是默认音波图
visualizerView.addCircleRenderer();
// 第二种音波图
visualizerView.addBarGraphRenderers();
// 第三种音波图
visualizerView.addCircleBarRenderer();
// 第四种音波图
visualizerView.addLineRenderer();
// 绑定AudioExecutor
AudioExecutor audioExecutor = new AudioExecutor(this, visualizerView);
// 播放音频
audioExecutor.play("音频地址");
```
3. 注意事项：Android 6.0+版本需要动态申请录音权限`android.permission.RECORD_AUDIO`，否则将无法展示音频图效果。

![猫客影音播放器音频效果图](https://github.com/zrunker/ZMaoKePlayer/blob/v_1.0_ijk/device-2022-06-14-195003.png)

