package com.cheng.test.toggleview.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 自定义开关
 * 
 * @author Administrator
 * @Description Android 的界面绘制流程
 *              <p>
 *              测量 布局 绘制
 *              <p>
 *              measure -> layout -> draw
 *              <p>
 *              这些方法都在onResume()之后执行
 *              <p>
 *              <p>
 *              对于View来说： onMeasure() (在这个方法里指定自己的宽高) --> onDraw() (在这个方法里绘制内容)
 *              <p>
 *              对于ViewGroup来说： onMeasure() (在这个方法里指定自己的宽高，所有子View的宽高) -->
 *              onLayout() (设置布局) --> onDraw() (在这个方法里绘制内容)
 */
public class ToggleView extends View {

	private Bitmap switchBackgroundBitmap;
	private Bitmap slideButtonBitmap;
	private Paint paint;
	private boolean mSwitchState = false;
	private float currentX;
	private boolean isTouchMode = false;
	private OnSwitchStateUpdateListener onSwitchStateUpdateListener;

	/**
	 * 用于代码创建控件
	 * 
	 * @param context
	 */
	public ToggleView(Context context) {
		super(context);
		initData();
	}

	/**
	 * 在XML中使用，可指定自定义属性
	 * 
	 * @param context
	 * @param attrs
	 */
	public ToggleView(Context context, AttributeSet attrs) {
		super(context, attrs);

		initData();
		
		String namespace = "http://schemas.android.com/apk/res-auto";
		int switchBackgroundResource = attrs.getAttributeResourceValue(namespace, "switch_background", -1);
		int slideButtonResource = attrs.getAttributeResourceValue(namespace, "slide_button", -1);
		mSwitchState = attrs.getAttributeBooleanValue(namespace, "switch_state", false);
		
		setSwitchBackgroundResource(switchBackgroundResource);
		setSlideButtonResource(slideButtonResource);
	}

	/**
	 * 在XML中使用，可指定自定义属性， 如果自定义了样式，则用此构造方法
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyleAttr
	 */
	public ToggleView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initData();
	}

	private void initData() {
		paint = new Paint();

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if(switchBackgroundBitmap == null){
			Log.d("ToggleView", "switchBackgroundBitmap为空！");
		}
		setMeasuredDimension(switchBackgroundBitmap.getWidth(), switchBackgroundBitmap.getHeight());

	}

	// canvas 画布画板， 在上边绘制的内容都会显示到屏幕上
	@Override
	protected void onDraw(Canvas canvas) {
		// [1]绘制背景
		canvas.drawBitmap(switchBackgroundBitmap, 0, 0, paint);

		// [2]绘制滑块
		if (isTouchMode) {
			// 开
			float newLeft = currentX - slideButtonBitmap.getWidth() / 2;
			int maxLeft = switchBackgroundBitmap.getWidth() - slideButtonBitmap.getWidth();

			if (newLeft < 0) {
				newLeft = 0;
			} else if (newLeft > maxLeft) {
				newLeft = maxLeft;
			}
			canvas.drawBitmap(slideButtonBitmap, newLeft, 0, paint);
		} else {

			if (mSwitchState) {
				// 开
				int newLeft = switchBackgroundBitmap.getWidth() - slideButtonBitmap.getWidth();
				canvas.drawBitmap(slideButtonBitmap, newLeft, 0, paint);
			} else {
				// 关
				canvas.drawBitmap(slideButtonBitmap, 0, 0, paint);
			}

		}

	}

	// 重写触摸事件，响应用户触摸
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:

			isTouchMode = true;
			currentX = event.getX();
			System.out.println("ACTION_DOWN:" + currentX);
			break;

		case MotionEvent.ACTION_MOVE:

			currentX = event.getX();
			System.out.println("ACTION_MOVE:" + currentX);
			break;

		case MotionEvent.ACTION_UP:

			isTouchMode = false;
			currentX = event.getX();

			// 根据当前位置，和控件中心位置比较
			boolean state = currentX > switchBackgroundBitmap.getWidth() / 2;

			//如果开关变化了，通知界面里面开关状态发生改变
			if (state != mSwitchState && onSwitchStateUpdateListener != null) {
				//把最新的boolean状态传出去
				onSwitchStateUpdateListener.stateUpdate(state);
			}
			
			mSwitchState = state;

			System.out.println("ACTION_UP:" + currentX);
			break;
		}
		// 会引发onDraw被调用，里面的变量会重新生效
		invalidate();

		// 消费用户的触摸事件，才能响应其他事件
		return true;
	}

	/**
	 * 设置背景图片
	 * 
	 * @param switchBackground
	 *            背景图片id
	 */
	public void setSwitchBackgroundResource(int switchBackground) {

		switchBackgroundBitmap = BitmapFactory.decodeResource(getResources(), switchBackground);

	}

	/**
	 * 设置开关滑块图片
	 * 
	 * @param slideButton
	 *            图片id
	 */
	public void setSlideButtonResource(int slideButton) {

		slideButtonBitmap = BitmapFactory.decodeResource(getResources(), slideButton);

	}

	/**
	 * 设置开关状态
	 * 
	 * @param
	 *
	 */
	public void setSwitchState(boolean mSwitchState) {
		this.mSwitchState = mSwitchState;

	}

	public interface OnSwitchStateUpdateListener {
		void stateUpdate(boolean state);
	}

	public void setOnSwitchStateUpdateListener(OnSwitchStateUpdateListener onSwitchStateUpdateListener) {
		this.onSwitchStateUpdateListener = onSwitchStateUpdateListener;

	}

}
