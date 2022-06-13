package cc.zrunker.android.maokeplayerlib.mkplayer.audio;

import android.media.audiofx.Visualizer;

import cc.zrunker.android.maokeplayerlib.mkplayer.audio.visualizer.view.VisualizerView;
import cc.zrunker.android.maokeplayerlib.mkplayer.core.MKPlayer;

/**
 * @program: ZMaoKePlayer
 * @description: 音频执行类
 * @author: zoufengli01
 * @create: 2022/6/13 16:15
 **/
public class AudioExecutor {
    private Visualizer visualizer;

    public AudioExecutor(MKPlayer mkPlayer, VisualizerView visualizerView) {
       int audioSessionId = mkPlayer.getAudioSessionId();
    }
}
