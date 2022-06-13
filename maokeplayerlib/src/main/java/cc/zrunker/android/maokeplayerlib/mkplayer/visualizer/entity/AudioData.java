package cc.zrunker.android.maokeplayerlib.mkplayer.visualizer.entity;

// Data class to explicitly indicate that these bytes are raw audio data
public class AudioData {
    public AudioData(byte[] bytes) {
        this.bytes = bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] bytes;
}
