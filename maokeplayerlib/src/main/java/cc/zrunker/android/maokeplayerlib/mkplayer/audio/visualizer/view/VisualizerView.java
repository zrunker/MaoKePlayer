package cc.zrunker.android.maokeplayerlib.mkplayer.audio.visualizer.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.audiofx.Visualizer;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashSet;
import java.util.Set;

import cc.zrunker.android.maokeplayerlib.mkplayer.audio.visualizer.entity.AudioData;
import cc.zrunker.android.maokeplayerlib.mkplayer.audio.visualizer.entity.FFTData;
import cc.zrunker.android.maokeplayerlib.mkplayer.audio.visualizer.renderer.BarGraphRenderer;
import cc.zrunker.android.maokeplayerlib.mkplayer.audio.visualizer.renderer.CircleBarRenderer;
import cc.zrunker.android.maokeplayerlib.mkplayer.audio.visualizer.renderer.CircleRenderer;
import cc.zrunker.android.maokeplayerlib.mkplayer.audio.visualizer.renderer.LineRenderer;
import cc.zrunker.android.maokeplayerlib.mkplayer.audio.visualizer.renderer.Renderer;
import cc.zrunker.android.maokeplayerlib.mkplayer.utils.DensityUtil;


/**
 * A class that draws visualizations of data received from a
 * {@link Visualizer.OnDataCaptureListener#onWaveFormDataCapture } and
 * {@link Visualizer.OnDataCaptureListener#onFftDataCapture }
 */
public class VisualizerView extends View {
    private byte[] mBytes;
    private byte[] mFFTBytes;
    private final Rect mRect = new Rect();

    private Set<Renderer> mRenderers;

    public boolean isAddRenderer() {
        return mRenderers != null && mRenderers.size() > 0;
    }

//    private Paint mFadePaint = new Paint();

    public VisualizerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        init();
    }

    public VisualizerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VisualizerView(Context context) {
        this(context, null, 0);
    }

    private void init() {
        mBytes = null;
        mFFTBytes = null;

//        mFadePaint.setColor(Color.argb(238, 255, 255, 255)); // Adjust alpha to change how quickly the image fades
//        mFadePaint.setXfermode(new PorterDuffXfermode(Mode.MULTIPLY));

        mRenderers = new HashSet<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int screenSize = DensityUtil.getScreenW(getContext());
        if (screenSize > 0)
            setMeasuredDimension(screenSize, screenSize);
    }

    /**
     * 展示顶部、底部柱状音频图
     */
    public void addBarGraphRenderers() {
        //底部柱状条
        Paint paint = new Paint();
        paint.setStrokeWidth(50f);
        paint.setAntiAlias(true);
        paint.setColor(Color.argb(200, 56, 138, 252));
        BarGraphRenderer barGraphRendererBottom = new BarGraphRenderer(16, paint, false);
        addRenderer(barGraphRendererBottom);

        //顶部柱状条
        Paint paint2 = new Paint();
        paint2.setStrokeWidth(12f);
        paint2.setAntiAlias(true);
        paint2.setColor(Color.argb(200, 181, 111, 233));
        BarGraphRenderer barGraphRendererTop = new BarGraphRenderer(4, paint2, true);
        addRenderer(barGraphRendererTop);
    }

    /**
     * 展示圆形柱状音频图
     */
    public void addCircleBarRenderer() {
        Paint paint = new Paint();
        paint.setStrokeWidth(8f);
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(Mode.LIGHTEN));
        paint.setColor(Color.argb(255, 222, 92, 143));
        CircleBarRenderer circleBarRenderer = new CircleBarRenderer(paint, 32, true);
        addRenderer(circleBarRenderer);
    }

    /**
     * 展示圆形音频图
     */
    public void addCircleRenderer() {
        Paint paint = new Paint();
        paint.setStrokeWidth(3f);
        paint.setAntiAlias(true);
        paint.setColor(Color.argb(255, 222, 92, 143));
        CircleRenderer circleRenderer = new CircleRenderer(paint, true);
        addRenderer(circleRenderer);
    }

    /**
     * 展示线条音频图
     */
    public void addLineRenderer() {
        Paint linePaint = new Paint();
        linePaint.setStrokeWidth(1f);
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.argb(88, 0, 128, 255));

        Paint lineFlashPaint = new Paint();
        lineFlashPaint.setStrokeWidth(5f);
        lineFlashPaint.setAntiAlias(true);
        lineFlashPaint.setColor(Color.argb(188, 255, 255, 255));
        LineRenderer lineRenderer = new LineRenderer(linePaint, lineFlashPaint, true);
        addRenderer(lineRenderer);
    }

    /**
     * 添加渲染器
     *
     * @param renderer 待添加对象
     */
    public void addRenderer(Renderer renderer) {
        if (renderer != null) {
            mRenderers.add(renderer);
        }
    }

    /**
     * 清空渲染器
     */
    public void clearRenderers() {
        mRenderers.clear();
    }

    /**
     * Pass data to the visualizer. Typically this will be obtained from the
     * Android Visualizer.OnDataCaptureListener call back. See
     * {@link Visualizer.OnDataCaptureListener#onWaveFormDataCapture }
     *
     * @param bytes 待处理音频数据
     */
    public void updateVisualizer(byte[] bytes) {
        mBytes = bytes;
        invalidate();
    }

    /**
     * Pass FFT data to the visualizer. Typically this will be obtained from the
     * Android Visualizer.OnDataCaptureListener call back. See
     * {@link Visualizer.OnDataCaptureListener#onFftDataCapture }
     *
     * @param bytes 待处理傅里叶数据
     */
    public void updateVisualizerFFT(byte[] bytes) {
        mFFTBytes = bytes;
        invalidate();
    }

//    boolean mFlash = false;

    private Bitmap mCanvasBitmap;
    private Canvas mCanvas;
    private AudioData audioData;
    private FFTData fftData;
    private Matrix matrix;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Create canvas once we're ready to draw
        mRect.set(0, 0, getWidth(), getHeight());

        if (mCanvasBitmap == null)
            mCanvasBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
        if (mCanvas == null)
            mCanvas = new Canvas(mCanvasBitmap);
        // 清空旧视图
        mCanvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);

        if (mBytes != null) {
            // Render all audio renderers
            if (audioData == null)
                audioData = new AudioData(mBytes);
            else
                audioData.setBytes(mBytes);
            for (Renderer r : mRenderers) {
                r.render(mCanvas, audioData, mRect);
            }
        }

        if (mFFTBytes != null) {
            // Render all FFT renderers
            if (fftData == null)
                fftData = new FFTData(mFFTBytes);
            else
                fftData.setBytes(mFFTBytes);
            for (Renderer r : mRenderers) {
                r.render(mCanvas, fftData, mRect);
            }
        }
//        // 渐变产生的阴影的效果
//        mCanvas.drawPaint(mFadePaint);

        if (matrix == null)
            matrix = new Matrix();
        canvas.drawBitmap(mCanvasBitmap, matrix, null);
    }

}