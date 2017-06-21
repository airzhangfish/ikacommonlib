package com.ikags.ikalib.androidview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.ikags.ikalib.util.IKALog;
/**
 * 封装了所有内部运行的一个低级界面的SurfaceView,简化使用步骤重载2个方法即可使用
 * @author airzhangfish
 *
 */
public class IKALowSurfaceView extends SurfaceView implements Runnable, Callback {

	public SurfaceHolder mSurfaceHolder = null;
	public Context mcontext = null;
	public int mWidth = 800;
	public int mHeight = 480;
	private long starttime = 0;
	private long endtime = 0;
	private long spendtime = 0;
	public int waitingTime = 80;
	public IKALowSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public IKALowSurfaceView(Context context) {
		super(context);
		initView(context);
	}

	public void initView(Context context) {
		mSurfaceHolder = getHolder();
		mSurfaceHolder.addCallback(this);
		mcontext = context;
	}



	public void draw(Canvas mCanvas) {
		try {
			mWidth=mCanvas.getWidth();
			mHeight=mCanvas.getHeight();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
			onUpdateDraw(mCanvas);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void run() {
		while (mIsRunning) {
			try {
				spendtime = endtime - starttime;
				if (spendtime >= waitingTime) {
					Thread.sleep(20);

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
		mWidth = width;
		mHeight = height;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		IKALog.v("" + this.getClass().getSimpleName(), "surfaceCreated");
		onInitView(mcontext);
		mIsRunning = true;
		mThread = new Thread(this);
		if (!mThread.isAlive()) {
			mThread.start();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		IKALog.v("" + this.getClass().getSimpleName(), "surfaceDestroyed");
		mIsRunning = false;
		onSurfaceDestroyed(holder);
		System.gc();
	}

	boolean mIsRunning = true;
	Thread mThread = null;

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
	public void onInitView(Context context){};
	/**
	 * 每帧绘制view调用的方法
	 *
	 * @param context
	 */
	public void onUpdateDraw(Canvas mCanvas){};
	/**
	 * 销毁本View调用的方法,要做一些释放处理
	 *
	 * @param context
	 */
	public void onSurfaceDestroyed(SurfaceHolder holder){};


//	public boolean onTouchEvent(MotionEvent event) {
//		switch (event.getAction()) {
//			case MotionEvent.ACTION_DOWN :
//			break;
//			case MotionEvent.ACTION_MOVE :
//			break;
//			case MotionEvent.ACTION_UP :
//			break;
//		}
//		return super.onTouchEvent(event);
//	}

//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		return super.onKeyDown(keyCode, event);
//	}
}