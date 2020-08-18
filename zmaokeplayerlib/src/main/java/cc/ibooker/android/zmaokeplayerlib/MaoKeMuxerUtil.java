package cc.ibooker.android.zmaokeplayerlib;

/**
 * FFmpeg4管理类
 *
 * @author 邹峰立
 */
public class MaoKeMuxerUtil {
    static {
        System.loadLibrary("muxer-util");
    }

    /**
     * 格式转换
     *
     * @param inPath  输入文件路径
     * @param outPath 输入文件路径
     * @return boolean true-转换成功，false-转换失败
     */
    public native static boolean reMuxer(String inPath, String outPath);
}