package com.cheng.test.rotatemenu.utils;

import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;

public class AnimationUtils {

	/**
	 * 旋转出去来的动画
	 * 
	 * @param layout 需要旋转的布局
	 * @param delay 延时事件
	 */
	public static void rotateOutAnim(ViewGroup layout, long delay) {
		
		//布局隐藏后，使其所有的子控件都失效
		int childCount = layout.getChildCount();
		for (int i = 0; i < childCount; i++) {
			layout.getChildAt(i).setEnabled(false);
			
		}
		
		RotateAnimation rotateAnimation = new RotateAnimation(
				0f, -180f, 
				Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 1f);

		rotateAnimation.setDuration(1000);
		rotateAnimation.setFillAfter(true);
		rotateAnimation.setStartOffset(delay);
		rotateAnimation.setAnimationListener(new MyAnimationListener());

		layout.startAnimation(rotateAnimation);
	}

	/**
	 * 旋转进来的动画
	 * 
	 * @param layout 需要旋转的布局
	 * @param delay 延时事件
	 */
	public static void rotateInAnim(ViewGroup layout, long delay) {
		
		//布局出现后，使其所有的子控件都生效
		int childCount = layout.getChildCount();
		for (int i = 0; i < childCount; i++) {
			layout.getChildAt(i).setEnabled(true);
			
		}
		
		RotateAnimation rotateAnimation = new RotateAnimation(
				-180f, 0f, 
				Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 1f);

		rotateAnimation.setDuration(1000);
		rotateAnimation.setFillAfter(true);
		rotateAnimation.setStartOffset(delay);
		rotateAnimation.setAnimationListener(new MyAnimationListener());
		layout.startAnimation(rotateAnimation);
	}

	public static int runningAnimCount = 0;

	static class MyAnimationListener implements AnimationListener {

		@Override
		public void onAnimationStart(Animation animation) {
			// 动画开始执行
			runningAnimCount++;
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			// 动画执行完毕
			runningAnimCount--;

		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub

		}

	}
}
