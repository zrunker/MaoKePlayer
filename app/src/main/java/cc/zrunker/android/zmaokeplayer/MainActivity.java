package cc.zrunker.android.zmaokeplayer;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import cc.zrunker.android.maokeplayerlib.mkplayer.core.listener.MKErrorListener;
import cc.zrunker.android.maokeplayerlib.mkplayer.view.MKVideoView;
import tv.danmaku.ijk.media.player.IMediaPlayer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MKVideoView mkVideoView = findViewById(R.id.mkVideo);
        mkVideoView.setOnErrorListener(new MKErrorListener() {
            @Override
            public void onError(IMediaPlayer iMediaPlayer, int what, int extra, String error) {
                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
        mkVideoView.play("https://s17.aconvert.com/convert/p3r68-cdx67/lidfd-3xc80.mp4");
    }
}
