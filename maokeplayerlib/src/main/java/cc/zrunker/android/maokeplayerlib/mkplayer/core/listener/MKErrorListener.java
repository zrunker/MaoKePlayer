package cc.zrunker.android.maokeplayerlib.mkplayer.core.listener;


import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * @program: ZMaoKePlayer
 * @description: 包装IMediaPlayer.OnErrorListener
 * @author: zoufengli01
 * @create: 2021/12/10 3:23 下午
 **/
public abstract class MKErrorListener
        implements IMediaPlayer.OnErrorListener {

    public abstract void onError(IMediaPlayer iMediaPlayer, int what, int extra, String error);

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int what, int extra) {
        onError(iMediaPlayer, what, extra, ErrorTrans.trans(what));
        return false;
    }
}
