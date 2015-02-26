package com.mail163.view;

import com.example.mail163.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;


public class RefreshListView extends ListView implements OnScrollListener , OnItemClickListener{
	// view
	private View header;
	private View tail;
	// values
	private int headerHeight; // header 高度

	private final String TAG = "RefreshListView";
	private int firstVisableItem;
	// listItem0 在屏幕顶端，且手指按下
	private boolean isRemark;
	// listItem0 在屏幕顶端，且手指按下时起始位置
	private int startY;
	// listView 当前滚动状态
	private int scrollState;

	// 下拉状态
	private int HeaderState;
	private final int NONE = 0;
	private final int PULL = 1;
	private final int RELEASE = 2;
	private final int REFLASHING = 3;

	public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);

		// TODO Auto-generated constructor stub
	}

	public RefreshListView(Context context, AttributeSet attrs) {

		this(context, attrs, 0);

	}

	public RefreshListView(Context context) {
		this(context, null, 0);

	}

	/**
	 * 初始化界面，添加顶部Header, 添加更多按钮，tail
	 * 
	 * @param context
	 */
	private void initView(Context context) {
		// 添加Header
		LayoutInflater inflater = LayoutInflater.from(context);
		header = inflater.inflate(R.layout.header_layout, null);
		// 计算header高度
		measureView(header);
		headerHeight = header.getMeasuredHeight();
		Log.e("TAG", "headerHeight" + headerHeight);
		// 隐藏header
		topPadding(-headerHeight);
		this.addHeaderView(header);
		// 添加onScrollListener 监听
		this.setOnScrollListener(this);
		
		//添加tail
		tail=inflater.inflate(R.layout.tail_layout, null);
	
		this.addFooterView(tail);
		this.setOnItemClickListener(this);
		
	}

	// ???????????

	/**
	 * 通知父布局，组建大小
	 * 
	 * @param view
	 */
	private void measureView(View view) {
		ViewGroup.LayoutParams params = view.getLayoutParams();
		if (params == null) {
			params = new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);

		}

		int width = ViewGroup.getChildMeasureSpec(0, 0, params.width);
		int tempHeight = params.height;
		int height;
		if (tempHeight > 0) {
			height = MeasureSpec.makeMeasureSpec(tempHeight,
					MeasureSpec.EXACTLY);
		} else {
			height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		view.measure(width, height);

	}

	/**
	 * 设置header上边距
	 * 
	 * @param topPadding
	 */
	private void topPadding(int topPadding) {
		header.setPadding(header.getPaddingLeft(), topPadding,
				header.getPaddingRight(), header.getPaddingBottom());

	}

	/**
	 * 获取首可见的空间
	 * 
	 * @param view
	 * @param firstVisibleItem
	 * @param visibleItemCount
	 * @param totalItemCount
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.firstVisableItem = firstVisibleItem;

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		this.scrollState = scrollState;

	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (firstVisableItem == 0) {
				isRemark = true;
				startY = (int) ev.getY();
			}
			break;
		case MotionEvent.ACTION_UP:
			if (HeaderState==RELEASE) {
				HeaderState=REFLASHING;
				//加载新数据
				reflashViewByState();
			}else if (HeaderState==PULL) {
				isRemark=false;
			}
			
			break;

		case MotionEvent.ACTION_MOVE:
			
			onMove(ev);
	
			
			
			break;

		default:
			break;
		}

		return super.onTouchEvent(ev);
	}

	/**
	 * 判断移动中操作
	 * 
	 * @param ev
	 */
	private void onMove(MotionEvent ev) {
		if (!isRemark) {
			return;
		}
		int tempY = (int) ev.getY();
		int space = tempY - startY;
		int topPadding=space-headerHeight;

		
		
		switch (HeaderState) {
		case NONE:
			if (space > 0) {
				HeaderState = PULL;
			}

			break;

		case PULL:
			
			topPadding(topPadding);
			if (space > headerHeight + 30
					&& scrollState == SCROLL_STATE_TOUCH_SCROLL) {
				HeaderState = RELEASE;
				reflashViewByState();
			}

			break;

		case RELEASE:
			// 释放
			topPadding(topPadding);
			if (space < headerHeight + 30) {
				HeaderState = PULL;
			} else if (space <= 0) {
				HeaderState = NONE;
				isRemark = false;
				reflashViewByState();
			}
			
			
			break;

		case REFLASHING:
			// 刷新
	

			break;
		}

	}
	
	
	/**
	 * 根据状态，改变显示
	 */
	private void reflashViewByState() {
		TextView tip =(TextView) header.findViewById(R.id.textview_tip);
		ProgressBar progressBar=(ProgressBar) header.findViewById(R.id.progessbar);
		
		switch (HeaderState) {
		case NONE:
			topPadding(-headerHeight);

			break;

		case PULL:
			tip.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.INVISIBLE);
			tip.setText("下拉刷新");

			break;

		case RELEASE:
			// 释放
			tip.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.INVISIBLE);
			tip.setText("松开刷新");
			
			
			break;

		case REFLASHING:
			// 刷新
			topPadding(headerHeight);
			tip.setVisibility(View.INVISIBLE);
			progressBar.setVisibility(View.VISIBLE);
			tip.setText("正在刷新...");

			break;
		}
		
	}
	
	
	/**
	 * 获取完数据后执行
	 */
	public void reflashCompleted () {
		HeaderState=NONE;
		isRemark=false;
		reflashViewByState();
	}



	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		//arg 3=-1, 为tail
		//Log.e(TAG, "VIEW_ID"+arg1.getId()+"; ARG2"+arg2+"; ARG3"+arg3);

		if (arg3==(-1)) {
			TextView moreTextView=(TextView)findViewById(R.id.textview_more);
			ProgressBar moreBar=(ProgressBar)findViewById(R.id.progessbar_more);
			moreTextView.setText("正在加载...");
			moreBar.setVisibility(View.VISIBLE);
			
		
		}

		
	}
	
	/**
	 * 重写该方法以返回更多数据
	 */
	public void getMore(){
		
		
		
	}

}
