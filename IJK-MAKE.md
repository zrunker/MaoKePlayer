[ijkplayer](https://github.com/bilibili/ijkplayer)是哔哩哔哩公司开源的一款基于FFmpeg的轻量级音视频播放插件，它以强大的功能支持和简易操作方式深受广大开发者喜爱。目前支持在Android系统上的音视频播放器也有很多，主要是以下几种：

|  播放器   |    优点   |   缺点（已发现） |  开发难度 |
|:------|:-------:|--------:|--------:|
| Mediaplayer   | Android系统自带组件，开发简单，兼容性好，流程由程序自身控制等。  | 支持视频格式较少，目前对MP4和MP3兼容比较好。 | 初 |
| ExoPlayer | Google开源Android播放器，开发简单只需要简单的API调用即可，支持格式较多等。 | 退出后台再次进入黑屏 | 中 |
| ijkplayer（FFmpeg） | 开发者可自己编译配置，可支持任意格式视频，支持硬解码和软解码等。 | SeekTo只支持I帧。 | 高 |

## ijkPlayer编译流程（支持全音视频格式，支持https）

注意：以下采用Mac Arm系统进行编译。

### 编译，支持Https和全格式视频编解码。
1. 加载源码：
```
git clone https://gitee.com/ibooker/ijkplayer.git ijkplayer-android
cd ijkplayer-android
git checkout -b latest k0.8.8
```
2. 修改配置文件：
- init-android-libyuv.sh
```
# IJK_LIBYUV_UPSTREAM=https://github.com/Bilibili/libyuv.git
IJK_LIBYUV_UPSTREAM=
https://gitee.com/ibooker/libyuv.git
# IJK_LIBYUV_FORK=https://github.com/Bilibili/libyuv.git
IJK_LIBYUV_FORK=
https://gitee.com/ibooker/libyuv.git
```
- init-android-openssl.sh
```
# IJK_OPENSSL_UPSTREAM=https://github.com/Bilibili/openssl.git
IJK_OPENSSL_UPSTREAM=
https://gitee.com/ibooker/openssl.git
# IJK_OPENSSL_FORK=https://github.com/Bilibili/openssl.git
IJK_OPENSSL_FORK=
https://gitee.com/ibooker/openssl.git
```
- init-android-soundtouch.sh
```
# IJK_SOUNDTOUCH_UPSTREAM=https://github.com/Bilibili/soundtouch.git
IJK_SOUNDTOUCH_UPSTREAM=
https://gitee.com/ibooker/soundtouch.git
# IJK_SOUNDTOUCH_FORK=https://github.com/Bilibili/soundtouch.git
IJK_SOUNDTOUCH_FORK=
https://gitee.com/ibooker/soundtouch.git
```
- init-android.sh
```
# IJK_FFMPEG_UPSTREAM=https://github.com/Bilibili/FFmpeg.git
IJK_FFMPEG_UPSTREAM=
https://gitee.com/ibooker/FFmpeg.git
# IJK_FFMPEG_FORK=https://github.com/Bilibili/FFmpeg.git
IJK_FFMPEG_FORK=
https://gitee.com/ibooker/FFmpeg.git
```

### NDK编译：
```
./init-android.sh
./init-android-openssl.sh
cd android/contrib
./compile-openssl.sh clean
./compile-openssl.sh all
cd ../..
cd config
rm module.sh
ln -s module-default.sh module.sh
cd ..
cd android/contrib
./compile-ffmpeg.sh clean
./compile-ffmpeg.sh all
cd ..
./compile-ijk.sh all
```

**注意NDK：**
ijkplayer脚本里代码限制NDK版本是 11-14。
例如：.bash_profile配置
```
export ANDROID_SDK=/Users/zoufengli01/Library/Android/sdk
export ANDROID_NDK=/Users/zoufengli01/Library/Android/android-ndk-r14b
export PATH=$PATH:$ANDROID_SDK
export PATH=$PATH:$ANDROID_NDK
```

至此ijkplayer-android的编译就完成了，它不仅支持https还支持更多的音视频格式。

最后我们将编译出来的so动态连接库添加到Android工程中，并在gradle配置文件中添加如下依赖就能够开心的使用了：
```
dependencies {
    # required
    compile 'tv.danmaku.ijk.media:ijkplayer-java:0.8.8'

    # ExoPlayer as IMediaPlayer: optional, experimental
    # compile 'tv.danmaku.ijk.media:ijkplayer-exo:0.8.8'
}
```

### 补充

[MaoKePlayer猫客影音播放器 - 基于ijk打造的一款万能音视频播放器插件](https://github.com/zrunker/ZMaoKePlayer)

[阅读原文](http://ibooker.cc/article/355/detail)