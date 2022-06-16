package cc.zrunker.android.maokeplayer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import cc.zrunker.android.maokeplayerlib.mkplayer.audio.AudioExecutor;
import cc.zrunker.android.maokeplayerlib.mkplayer.audio.visualizer.view.VisualizerView;

public class MainActivity extends AppCompatActivity {
    private AudioExecutor audioExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        // 播放视频
//        MKVideoView mkVideoView = findViewById(R.id.mkVideo);
//        mkVideoView.setOnErrorListener(new IMKListener.OnErrorListener() {
//            @Override
//            public void onError(MKPlayer mkPlayer, int what, int extra, String error) {
//                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
//            }
//        });
//        mkVideoView.play("https://s27.aconvert.com/convert/p3r68-cdx67/lo1qa-tl6b5.mp4");

        // 播放音频
//        audioExecutor = new AudioExecutor(this);
//        audioExecutor.play("https://s33.aconvert.com/convert/p3r68-cdx67/yehlm-ar2ff.mp3");

//        VisualizerView visualizerView = findViewById(R.id.visualizerView);
//        audioExecutor = new AudioExecutor(this, visualizerView);
//        audioExecutor.play("https://s33.aconvert.com/convert/p3r68-cdx67/yehlm-ar2ff.mp3");

        VisualizerView visualizerView = findViewById(R.id.visualizerView);
        visualizerView.addBarGraphRenderers();
        visualizerView.addCircleBarRenderer();
        visualizerView.addCircleRenderer();
        visualizerView.addLineRenderer();
        audioExecutor = new AudioExecutor(this, visualizerView);
        audioExecutor.play("https://s19.aconvert.com/convert/p3r68-cdx67/zkbt4-uofws.mp3");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        audioExecutor.destroy();
    }
}
