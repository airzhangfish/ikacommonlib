package com.ikags.ikalib.gameutil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.ikags.ikalib.util.IKALog;

/**
 * 基本的2D游戏场景类. 用法: 写个新view extends IKA2DGameScreenView, 重载onInitView,onUpdateDraw,onSurfaceDestroyed 这3个方法. view.setEnableBuffer(100,100);//设置buffer大小(固定画布大小)
 *
 * @author airzhangfish
 *
 */
public class IKA2DGameScreenView extends SurfaceView implements Runnable, Callback {

	public static final String TAG = "IKA2DGameScreenView";
	// 底层控制
	private SurfaceHolder mSurfaceHolder = null;
	private Context mcontext = null;
	public int mRealWidth = 800;
	public int mRealHeight = 480;
	private long starttime = 0;
	private long endtime = 0;
	private long spendtime = 0;
	public static int waitingTime = 34; // 固定每秒30fps
	public float mScreenScaleX = 0;
	public float mScreenScaleY = 0;
	public Bitmap mCurFrameBitmap; // 可对外提供截图
	private Canvas mCurFrameCanvas;
	public int mFrameWidth = 570;
	public int mFrameHeight = 320;
	private Paint mScreenPaint = new Paint();
	private Matrix mScreenMatrix = new Matrix();
	private boolean isResetMatrix = true;
	public boolean isAntiAlias = true;

	public IKA2DGameScreenView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public IKA2DGameScreenView(Context context) {
		super(context);
		initView(context);
	}

	private void initView(Context context) {
		mSurfaceHolder = getHolder();
		mSurfaceHolder.addCallback(this);
		mcontext = context;
	}

	private PaintFlagsDrawFilter pfdf = null;

	private PaintFlagsDrawFilter getAtiPaintFlagsDrawFilter() {
		if (pfdf == null) {
			pfdf = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
		}
		return pfdf;
	}

	/**
	 * 重要方法.目的是为了适配所有分辨率
	 */
	public void setEnableBuffer(int frameWidth, int frameHeight) {
		mFrameWidth = frameWidth;
		mFrameHeight = frameHeight;
		mCurFrameBitmap = Bitmap.createBitmap(mFrameWidth, mFrameHeight, Bitmap.Config.ARGB_8888);
		mCurFrameCanvas = new Canvas(mCurFrameBitmap);
		isResetMatrix = true;
	}

	public void draw(Canvas mCanvas) {
		try {
			mRealWidth = mCanvas.getWidth();
			mRealHeight = mCanvas.getHeight();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
			if (mCurFrameBitmap != null && mCurFrameCanvas != null) {
				if (isAntiAlias == true) {
					mCanvas.setDrawFilter(getAtiPaintFlagsDrawFilter());
				}
				if (isResetMatrix == true) {
					if (isAntiAlias == true) {
						mScreenPaint.setAntiAlias(true);
						mCurFrameCanvas.setDrawFilter(getAtiPaintFlagsDrawFilter());
					}
					mScreenScaleX = (float) mRealWidth / (float) mFrameWidth;
					mScreenScaleY = (float) mRealHeight / (float) mFrameHeight;
					mScreenMatrix.reset();
					mScreenMatrix.setScale(mScreenScaleX, mScreenScaleY, 0, 0);
					isResetMatrix = false;
				}
				// 缓存绘制方法
				onUpdateDraw(mCurFrameCanvas);
				mCanvas.drawBitmap(mCurFrameBitmap, mScreenMatrix, mScreenPaint);
			} else {
				onUpdateDraw(mCanvas);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public int getFPS() {
		float myfps = 0;
		if (spendtime != 0) {
			if (spendtime >= waitingTime) {
				myfps = 1000f / (5 + spendtime);
			} else {
				myfps = 1000f / (float) waitingTime;
			}
		}
		return (int) myfps;
	}

	public void run() {
		while (mScreenIsRunning) {
			try {
				spendtime = endtime - starttime;
				if (spendtime >= waitingTime) {
					Thread.sleep(5);

				} else {
					Thread.sleep(waitingTime - spendtime);
				}
				starttime = System.currentTimeMillis();
				Canvas mCanvas = mSurfaceHolder.lockCanvas();
				draw(mCanvas);
				mSurfaceHolder.unlockCanvasAndPost(mCanvas);
				endtime = System.currentTimeMillis();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		IKALog.v("" + this.getClass().getSimpleName(), "surfaceChanged size=" + width + "x" + height);
		mRealWidth = width;
		mRealHeight = height;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		IKALog.v("" + this.getClass().getSimpleName(), "surfaceCreated");
		onInitView(mcontext);
		mScreenIsRunning = true;
		mScreenThread = new Thread(this);
		if (!mScreenThread.isAlive()) {
			mScreenThread.start();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		IKALog.v("" + this.getClass().getSimpleName(), "surfaceDestroyed");
		mScreenIsRunning = false;
		onSurfaceDestroyed(holder);
		System.gc();
	}

	boolean mScreenIsRunning = true;
	Thread mScreenThread = null;

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int specMode = MeasureSpec.getMode(widthMeasureSpec);
		int specSize = MeasureSpec.getSize(widthMeasureSpec);
		IKALog.v("" + this.getClass().getSimpleName(), "surface-onMeasure" + specMode + "." + specSize);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/**
	 * 初始化view调用的方法
	 *
	 * @param context
	 */
	public void onInitView(Context context) {};

	/**
	 * 每帧绘制view调用的方法
	 *
	 * @param context
	 */
	public void onUpdateDraw(Canvas mCanvas) {};

	/**
	 * 销毁本View调用的方法,要做一些释放处理
	 *
	 * @param context
	 */
	public void onSurfaceDestroyed(SurfaceHolder holder) {};

	// public boolean onTouchEvent(MotionEvent event) {
	// switch (event.getAction()) {
	// case MotionEvent.ACTION_DOWN :
	// break;
	// case MotionEvent.ACTION_MOVE :
	// break;
	// case MotionEvent.ACTION_UP :
	// break;
	// }
	// return super.onTouchEvent(event);
	// }

	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// return super.onKeyDown(keyCode, event);
	// }

}
