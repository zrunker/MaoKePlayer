package cc.zrunker.android.maokeplayer;

import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import cc.zrunker.android.maokeplayerlib.mkplayer.audio.MediaPlayerExecutor;
import cc.zrunker.android.maokeplayerlib.mkplayer.audio.visualizer.view.VisualizerView;

public class MainActivity extends AppCompatActivity {
    private MediaPlayerExecutor mediaPlayerExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 播放视频
        final VideoView videoView = findViewById(R.id.video);
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(MainActivity.this, what + " " + extra, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.start();
            }
        });
        videoView.setVideoPath("https://s27.aconvert.com/convert/p3r68-cdx67/lo1qa-tl6b5.mp4");

//        // 播放音频
//        mediaPlayerExecutor = MediaPlayerExecutor.create(this);
//        mediaPlayerExecutor.play("https://s33.aconvert.com/convert/p3r68-cdx67/yehlm-ar2ff.mp3");
//
//        final VisualizerView visualizerView = findViewById(R.id.visualizerView);
//        visualizerView.addBarGraphRenderers();
//        visualizerView.addCircleBarRenderer();
//        visualizerView.addCircleRenderer();
//        visualizerView.addLineRenderer();
//        mediaPlayerExecutor = MediaPlayerExecutor.create(this)
//                .setMediaPlayerListener(new MediaPlayerExecutor.MediaPlayerListener() {
//                    @Override
//                    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
//                        return false;
//                    }
//
//                    @Override
//                    public void onCompletion(MediaPlayer mediaPlayer) {
//
//                    }
//
//                    @Override
//                    public void onPrepared(MediaPlayer mediaPlayer) {
//
//                    }
//
//                    @Override
//                    public void onSeekComplete(MediaPlayer mediaPlayer) {
//
//                    }
//
//                    @Override
//                    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
//
//                    }
//
//                    @Override
//                    public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
//                        visualizerView.updateVisualizerFFT(fft);
//                    }
//
//                    @Override
//                    public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
//                        visualizerView.updateVisualizer(waveform);
//                    }
//                });
//        mediaPlayerExecutor.play("https://s19.aconvert.com/convert/p3r68-cdx67/zkbt4-uofws.mp3");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayerExecutor.destroy();
    }
}
