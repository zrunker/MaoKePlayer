# MaoKePlayer
使用MediaPlayer执行音视频播放，MediaPlayer是Android系统自带的视频播放器插件，目前对MP4和MP3兼容性最好。

## 视频播放 - VideoView
VideoView是Android系统自带控件，内部包装了MediaPlayer，另外VideoView支持绑定视频播放控制器，所以VideoView能够很简单的实现视频播放。

**使用：**
1. 将VideoView添加到布局文件：
```
<VideoView
    android:id="@+id/video"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```
2. 执行视频播放：
```
final VideoView videoView = findViewById(R.id.video);
// 错误事件监听
videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Toast.makeText(MainActivity.this, what + " " + extra, Toast.LENGTH_SHORT).show();
        return false;
    }
});
// 预加载监听，这里一定要执行videoView.start()，否则视频无法播放
videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
    @Override
    public void onPrepared(MediaPlayer mp) {
        videoView.start();
    }
});
// 添加控制器
videoView.setMediaController(new MediaController(this));
// 播放视频
videoView.setVideoPath("视频地址");
```

## 音频播放 - MediaPlayerExecutor
MediaPlayerExecutor对MediaPlayer进行了封装，并且对音频属性做了一些调整，只需要如下两步就能实现音频播放：
```
MediaPlayerExecutor mediaPlayerExecutor = MediaPlayerExecutor.create(this);
mediaPlayerExecutor.play("音频地址");
```
为了防止内存泄露，建议在Activity/Fragment销毁时，执行如下方法：
```
@Override
protected void onDestroy() {
    super.onDestroy();
    mediaPlayerExecutor.destroy();
}
```

![猫客影音播放器视频效果图](https://github.com/zrunker/ZMaoKePlayer/blob/v_1.0_media/device-2022-06-17-163225.png)

## 音频播放 - 音波图 - VisualizerView
VisualizerView是用来显示音频的傅立叶数据图，目前支持四种音波效果图。

**使用：**
1. 在布局中添加VisualizerView：
```
<cc.zrunker.android.maokeplayerlib.mkplayer.audio.visualizer.view.VisualizerView
    android:id="@+id/visualizerView"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```
2. 绑定VisualizerView，并执行MediaPlayerExecutor播放，注意四种音波图选择一种即可：
```
final VisualizerView visualizerView = findViewById(R.id.visualizerView);
// 第一种音波图
visualizerView.addCircleRenderer();
// 第二种音波图
visualizerView.addBarGraphRenderers();
// 第三种音波图
visualizerView.addCircleBarRenderer();
// 第四种音波图
visualizerView.addLineRenderer();
// 绑定MediaPlayerExecutor
mediaPlayerExecutor = MediaPlayerExecutor.create(this)
              .setMediaPlayerListener(new MediaPlayerExecutor.MediaPlayerListener() {
                    @Override
                    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                        return false;
                    }

                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {

                    }

                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {

                    }

                    @Override
                    public void onSeekComplete(MediaPlayer mediaPlayer) {

                    }

                    @Override
                    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {

                    }

                    @Override
                    public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
                        visualizerView.updateVisualizerFFT(fft);
                    }

                    @Override
                    public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
                        visualizerView.updateVisualizer(waveform);
                    }
                });
// 播放音频
mediaPlayerExecutor.play.play("音频地址");
```
3. 注意事项：Android 6.0+版本需要动态申请录音权限`android.permission.RECORD_AUDIO`，否则将无法展示音频图效果。

![猫客影音播放器音频效果图](https://github.com/zrunker/ZMaoKePlayer/blob/v_1.0_media/device-2022-06-14-195003.png)

