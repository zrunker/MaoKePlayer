package cc.zrunker.android.maokeplayerlib.mkplayer.view.media;

import android.view.SurfaceHolder;

import cc.zrunker.android.maokeplayerlib.mkplayer.core.IMKPlayer;
import cc.zrunker.android.maokeplayerlib.mkplayer.core.listener.IMKListener;

/**
 * @program: ZMaoKePlayer
 * @description: 媒体View接口
 * @author: zoufengli01
 * @create: 2021/12/6 3:08 下午
 **/
public interface IMKMediaView extends IMKPlayer, IMKListener {

    void setHolderCallBack(SurfaceHolder.Callback holderCallBack);

}
