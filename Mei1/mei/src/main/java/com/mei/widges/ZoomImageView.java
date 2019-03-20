package com.mei.widges;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;

public class ZoomImageView extends ImageView implements OnGlobalLayoutListener, OnScaleGestureListener, OnTouchListener {

	private boolean mOnce;

	/**
	 */
	private float mInitScale;
	/**
	 */
	private float mMidScale;
	/**
	 */
	private float mMaxScale;

	private Matrix mScaleMatrix;

	/**
	 */
	private ScaleGestureDetector mScaleGestureDetector;


	private int mLastPointerCount;
	private float mLastX;
	private float mLastY;

	private int mTouchSlop;

	private boolean isCanDrag;
	
	private GestureDetector mGestureDetector;
	
	private boolean isAutoScale;

	public ZoomImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		// init
		mScaleMatrix = new Matrix();
		setScaleType(ScaleType.MATRIX);
		mScaleGestureDetector = new ScaleGestureDetector(context, this);
		setOnTouchListener(this);

		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		
		mGestureDetector=new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
			@Override
			public boolean onDoubleTap(MotionEvent e) {
				
				if (isAutoScale) {
					return true;
				}
				float x=e.getX();
				float y=e.getY();
				
				if (getScale()<mMidScale) {
//					mScaleMatrix.postScale(mMidScale/getScale(), mMidScale/getScale(),x,y);
//					setImageMatrix(mScaleMatrix);
					
					postDelayed(new AutoScaleRunnable(mMidScale, x, y), 4);
					isAutoScale=true;
				}else {
//					mScaleMatrix.postScale(mInitScale/getScale(), mInitScale/getScale(),x,y);
//					setImageMatrix(mScaleMatrix);
					
					postDelayed(new AutoScaleRunnable(mInitScale, x, y), 4);
					isAutoScale=true;
				}
				
				
				return true;
			}
		});
	}
	
	private class AutoScaleRunnable implements Runnable{

		
		/**
		 */
		private float mTargetScale;
		private float x;
		private float y;
		
	    private final float BIGGER=1.02f;
	    private final float SMALL=0.98f;
	    
	    private float tmpScale;
	    
	    
	    
		public AutoScaleRunnable(float mTargetScale, float x, float y) {
			super();
			this.mTargetScale = mTargetScale;
			this.x = x;
			this.y = y;
			
			if (getScale()<mTargetScale) {
				tmpScale=BIGGER;
			}
			if(getScale()>mTargetScale) {
				tmpScale=SMALL;
			}
		}



		@Override
		public void run() {
			
			mScaleMatrix.postScale(tmpScale, tmpScale,x,y);
			checkBorderAndCenterWhenScale();
			setImageMatrix(mScaleMatrix);
			
			float currentScale=getScale();
			
			if ((tmpScale>1.0f&&currentScale<mTargetScale)
					||(tmpScale<1.0f&&currentScale>mTargetScale)) {
				postDelayed(this, 4);
			}else {
				float scale=mTargetScale/currentScale;
				mScaleMatrix.postScale(scale, scale, x, y);
				checkBorderAndCenterWhenScale();
				setImageMatrix(mScaleMatrix);
				
				isAutoScale=false;
			}
			
		}
		
	}

	public ZoomImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ZoomImageView(Context context) {
		this(context, null);
	}

	@Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
		getViewTreeObserver().addOnGlobalLayoutListener(this);
	}

	@SuppressLint("NewApi")
	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		super.onDetachedFromWindow();
		getViewTreeObserver().removeOnGlobalLayoutListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * onGlobalLayout()
	 */
	@Override
	public void onGlobalLayout() {
		if (!mOnce) {
			int width = getWidth();
			int height = getHeight();
			Drawable d = getDrawable();
			if (d == null) {
				return;
			}
			int dw = d.getIntrinsicWidth();
			int dh = d.getIntrinsicHeight();

			float scale = 1.0f;

			if (dw > width && dh < height) {
				scale = width * 1.0f / dw;
			}
			if (dh > height && dw < width) {
				scale = height * 1.0f / dh;
			}
			if ((dw > width && dh > height) || (dw < width && dh < height)) {
				scale = Math.min(width * 1.0f / dw, height * 1.0f / dh);
			}

			mInitScale = scale;
			mMidScale = mInitScale * 2;
			mMaxScale = mInitScale * 4;

			int dx = getWidth() / 2 - dw / 2;
			int dy = getHeight() / 2 - dh / 2;

			mScaleMatrix.postTranslate(dx, dy);
			mScaleMatrix.postScale(mInitScale, mInitScale, width / 2, height / 2);
			setImageMatrix(mScaleMatrix);

			mOnce = true;
		}
	}

	/**
	 *
	 * @return
	 */
	public float getScale() {
		float[] values = new float[9];
		mScaleMatrix.getValues(values);
		return values[Matrix.MSCALE_X];
	}

	@Override
	public boolean onScale(ScaleGestureDetector detector) {
		float scale = getScale();
		float scaleFactor = detector.getScaleFactor();

		if (getDrawable() == null) {
			return true;
		}
		if ((scale < mMaxScale && scaleFactor > 1.0f) || (scale > mInitScale && scaleFactor < 1.0f)) {
			if (scale * scaleFactor < mInitScale) {
				scaleFactor = mInitScale / scale;
			}
			if (scale * scaleFactor > mMaxScale) {
				scaleFactor = mMaxScale / scale;
			}
			// mScaleMatrix.postScale(scaleFactor, scaleFactor, getWidth() / 2,
			// getHeight() / 2);

			mScaleMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());

			checkBorderAndCenterWhenScale();
			setImageMatrix(mScaleMatrix);
		}
		return true;
	}

	/**
	 *
	 * @return
	 */
	private RectF getMatrixRectF() {
		Matrix matrix = mScaleMatrix;
		RectF rectF = new RectF();

		Drawable d = getDrawable();
		if (d != null) {
			rectF.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
			matrix.mapRect(rectF);
		}
		return rectF;
	}

	/**
	 */
	private void checkBorderAndCenterWhenScale() {
		RectF rect = getMatrixRectF();

		float deltaX = 0;
		float deltaY = 0;

		int width = getWidth();
		int height = getHeight();

		if (rect.width() >= width) {
			if (rect.left > 0) {
				deltaX = -rect.left;
			}
			if (rect.right < width) {
				deltaX = width - rect.right;
			}
		}

		if (rect.height() >= height) {
			if (rect.top > 0) {
				deltaY = -rect.top;
			}
			if (rect.bottom < height) {
				deltaY = height - rect.bottom;
			}
		}

		if (rect.width() < width) {
			deltaX = width / 2 - rect.right + rect.width() / 2;
		}
		if (rect.height() < height) {
			deltaY = height / 2 - rect.bottom + rect.height() / 2;
		}
		mScaleMatrix.postTranslate(deltaX, deltaY);
	}

	@Override
	public boolean onScaleBegin(ScaleGestureDetector detector) {
		return true;
	}

	@Override
	public void onScaleEnd(ScaleGestureDetector detector) {

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		if (mGestureDetector.onTouchEvent(event)) {
			return true;
		}
		mScaleGestureDetector.onTouchEvent(event);
		float x = 0;
		float y = 0;
		int pointerCount = event.getPointerCount();
		for (int i = 0; i < pointerCount; i++) {
			x += event.getX(i);
			y += event.getY(i);
		}
		x /= pointerCount;
		y /= pointerCount;

		if (mLastPointerCount != pointerCount) {
			isCanDrag = false;
			mLastX = x;
			mLastY = y;
		}
		mLastPointerCount = pointerCount;

		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:

			float dx = x - mLastX;
			float dy = y - mLastY;

			if (!isCanDrag) {
				isCanDrag = isMoveAction(dx, dy);
			}
			if (isCanDrag) {
				RectF rectF = getMatrixRectF();
				if (getDrawable() != null) {
					if (rectF.width() < getWidth()) {
						dx = 0;
					}
					if (rectF.height() < getHeight()) {
						dy = 0;
					}
					mScaleMatrix.postTranslate(dx, dy);
					checkBorderAndCenterWhenScale();
					setImageMatrix(mScaleMatrix);
				}
			}

			mLastX = x;
			mLastY = y;
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			mLastPointerCount = 0;
			break;

		default:
			break;
		}
		// <------------------------------------------------>

		return true;
	}

	/**
	 *
	 * @param dx
	 * @param dy
	 * @return
	 */
	private boolean isMoveAction(float dx, float dy) {
		return Math.sqrt(dx * dx + dy * dy) > mTouchSlop;
	}

}
