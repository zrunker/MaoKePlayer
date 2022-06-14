package cc.zrunker.android.zmaokeplayer;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import cc.zrunker.android.maokeplayerlib.mkplayer.core.MKPlayer;
import cc.zrunker.android.maokeplayerlib.mkplayer.core.listener.IMKListener;
import cc.zrunker.android.maokeplayerlib.mkplayer.video.MKVideoView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MKVideoView mkVideoView = findViewById(R.id.mkVideo);
        mkVideoView.setOnErrorListener(new IMKListener.OnErrorListener() {
            @Override
            public void onError(MKPlayer mkPlayer, int what, int extra, String error) {
                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
        mkVideoView.play("https://s27.aconvert.com/convert/p3r68-cdx67/lo1qa-tl6b5.mp4");
    }
}
