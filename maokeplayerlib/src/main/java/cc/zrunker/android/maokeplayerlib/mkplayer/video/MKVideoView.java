package cc.zrunker.android.maokeplayerlib.mkplayer.video;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import cc.zrunker.android.maokeplayerlib.R;
import cc.zrunker.android.maokeplayerlib.mkplayer.core.MKPlayer;
import cc.zrunker.android.maokeplayerlib.mkplayer.core.listener.IMKListener;
import cc.zrunker.android.maokeplayerlib.mkplayer.video.controller.MKController;
import cc.zrunker.android.maokeplayerlib.mkplayer.video.media.MKMediaView;

/**
 * @program: ZMaoKePlayer
 * @description: 视频View
 * @author: zoufengli01
 * @create: 2021/12/6 11:05 上午
 **/
public class MKVideoView extends FrameLayout {
    private final MKMediaView mkMediaView;
    private final ProgressBar progressCircular;
    private MKController defaultController;

    private IMKListener.OnErrorListener mkErrorListener;

    public MKController getDefaultController() {
        return defaultController;
    }

    public MKVideoView(Context context) {
        this(context, null);
    }

    public MKVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MKVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArr = context.obtainStyledAttributes(attrs, R.styleable.MKVideoView);
        boolean isBindController = typedArr.getBoolean(R.styleable.MKVideoView_isBindController, true);
        typedArr.recycle();

        LayoutInflater.from(context).inflate(R.layout.mk_video_view, this, true);
        progressCircular = findViewById(R.id.progress_circular);
        mkMediaView = findViewById(R.id.media_view);
        if (isBindController) {
            defaultController = new MKController(context);
            defaultController.register(mkMediaView);
        }
        initListener();
    }

    /**
     * 初始化监听事件
     */
    private void initListener() {
        // 视频预备监听
        setOnPreparedListener(null);
        // 视频错误异常监听
        setOnErrorListener(null);
        // 视频播放完成
        setOnCompletionListener(null);
    }

    /**
     * 设置控制器监听
     */
    public MKVideoView setControllerListener(MKController.OnControllerListener onControllerListener) {
        if (onControllerListener != null && defaultController != null) {
            defaultController.setOnControllerListener(onControllerListener);
        }
        return this;
    }

    /**
     * 播放视频
     *
     * @param path 视频地址
     */
    public void play(String path) {
        if (!TextUtils.isEmpty(path)) {
            Uri uri = Uri.parse(path);
            play(uri);
        }
    }

    // 播放2
    public void play(Uri uri) {
        try {
            if (uri != null) {
                progressCircular.setVisibility(VISIBLE);
                // 重置MKPlayer
                mkMediaView.reset();
                // 重新加载音频资源
                mkMediaView.setDataSource(getContext(), uri);
                // 准备播放（异步）
                mkMediaView.prepareAsync();
            }
        } catch (Exception e) {
            e.printStackTrace();
            progressCircular.setVisibility(GONE);
            if (mkErrorListener != null) {
                mkErrorListener.onError(null, 0, 0, e.getMessage());
            }
        }
    }

    public MKVideoView setOnPreparedListener(IMKListener.OnPreparedListener listener) {
        mkMediaView.addOnPreparedListener(new IMKListener.OnPreparedListener() {
            @Override
            public void onPrepared(MKPlayer mkPlayer) {
                progressCircular.setVisibility(GONE);
                if (listener != null) {
                    listener.onPrepared(mkPlayer);
                }
            }
        });
        return this;
    }

    public MKVideoView setOnErrorListener(IMKListener.OnErrorListener listener) {
        mkMediaView.addOnErrorListener(new IMKListener.OnErrorListener() {
            @Override
            public void onError(MKPlayer mkPlayer, int what, int extra, String error) {
                progressCircular.setVisibility(GONE);
                if (listener != null) {
                    listener.onError(mkPlayer, what, extra, error);
                }
            }
        });
        return this;
    }

    public MKVideoView setOnCompletionListener(IMKListener.OnCompletionListener listener) {
        mkMediaView.addOnCompletionListener(new IMKListener.OnCompletionListener() {
            @Override
            public void onCompletion(MKPlayer mkPlayer) {
                progressCircular.setVisibility(GONE);
                if (listener != null) {
                    listener.onCompletion(mkPlayer);
                }
            }
        });
        return this;
    }
}
