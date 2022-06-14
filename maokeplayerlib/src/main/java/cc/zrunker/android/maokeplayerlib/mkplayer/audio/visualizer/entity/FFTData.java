package cc.zrunker.android.maokeplayerlib.mkplayer.audio.visualizer.entity;

// Data class to explicitly indicate that these bytes are the FFT of audio data
public class FFTData {
    public FFTData(byte[] bytes) {
        this.bytes = bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] bytes;
}
