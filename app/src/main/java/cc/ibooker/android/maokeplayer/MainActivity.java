package cc.ibooker.android.maokeplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import cc.ibooker.android.zmaokeplayerlib.MaoKeMuxerUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaoKeMuxerUtil.reMuxer("", "");
    }
}
