package com.mail163.view;

import com.example.mail163.R;

import android.R.anim;
import android.R.color;
import android.R.integer;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class SlidingMenu extends HorizontalScrollView {

	// view
	private LinearLayout mLinearLayout;
	private ViewGroup mMenu;
	private ViewGroup mContend;

	// values
	private int mScreenWidth;
	// mLeftMenuGroup 与右侧边距
	private int mMenuRightPadding = 50;
	private boolean once = true;
	private int mMenuWidth;

	/**
	 * 
	 * 未定义参数时，调用
	 * 
	 * @param context
	 * @param attrs
	 */

	public SlidingMenu(Context context, AttributeSet attrs) {

		this(context, attrs, 0);

	}

	/**
	 * 当使用自定义属性时，调用本构造方法
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// 获取自定义属性的值

		TypedArray array = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.SlidingMenu, defStyle, 0);

		int n = array.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = array.getIndex(i);
			switch (attr) {

			case R.styleable.SlidingMenu_rightPadding:
				mMenuRightPadding=array.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources()
						.getDisplayMetrics()));
				

				break;

			}

		}

		array.recycle();

		// 获取屏幕宽
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(metrics);
		mScreenWidth = metrics.widthPixels;
		// 转换DP>>px 像素
		//mMenuRightPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics());

		mMenuWidth = mScreenWidth - mMenuRightPadding;
	}

	public SlidingMenu(Context context) {
		this(context, null, 0);
	}

	/*
	 * 设置子View的宽和高，自己的宽和高
	 * 
	 * @see android.widget.HorizontalScrollView#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		if (once) {
			// 获得布局组建
			mLinearLayout = (LinearLayout) getChildAt(0);
			mMenu = (ViewGroup) mLinearLayout.getChildAt(0);
			mContend = (ViewGroup) mLinearLayout.getChildAt(1);

			mMenu.getLayoutParams().width = mScreenWidth - mMenuRightPadding;

			mContend.getLayoutParams().width = mScreenWidth;

			Log.e("TAG", "mMenuWidth:" + mMenuWidth);
			once = false;
		}

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/*
	 * 决定子View放置位置
	 * 
	 * @see android.widget.HorizontalScrollView#onLayout(boolean, int, int, int,
	 * int)
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
		// Log.e("TAG", "mMenuWidth:"+mMenuWidth);
		// if (!changed) {
		this.scrollTo(mMenuWidth, 0);
		// }
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction();

		switch (action) {
		case MotionEvent.ACTION_UP:
			int scrollX = getScrollX();
			if (scrollX > mMenuWidth / 2) {
				// Menu隐藏部分宽度大于一半，则隐藏菜单
				this.smoothScrollTo(mMenuWidth, 0);
				mContend.setFocusable(true);
				//mContend.setBackgroundColor(Color.WHITE);
			} else {
				this.smoothScrollTo(0, 0);
				mContend.setFocusable(false);
		
				//mContend.setBackgroundColor(Color.DKGRAY);
			}

			return true;
		}

		return super.onTouchEvent(ev);

	}

}
