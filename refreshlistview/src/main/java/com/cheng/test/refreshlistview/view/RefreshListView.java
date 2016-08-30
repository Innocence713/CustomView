package com.cheng.test.refreshlistview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cheng.test.refreshlistview.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 包含下拉刷新的ListView
 *
 * @author Cheng
 *
 */
public class RefreshListView extends ListView implements OnScrollListener {

	private static final int RELEASE_REFRESH = 0;
	private static final int PULL_TO_REFRESH = 1;
	private static final int REFRESHING = 2;
	private View mHeaderView;
	private View mFooterView;
	private float downY;
	private float moveY;
	private int measuredHeight;
	private TextView tv_title;
	private TextView tv_last_time;
	private ProgressBar pb_refresh;
	private ImageView iv_arrow;
	private String last_refresh_time;
	private int currentState;
	private RotateAnimation mRotateDownAnim;
	private RotateAnimation mRotateUpAnim;
	private int paddingTop;
	private OnRefreshListener mListener;
	private TextView footerTitle;
	private ProgressBar footerPB;
	private int footerMeasuredHeight;
	private boolean isLoadingMore;

	public RefreshListView(Context context) {
		super(context);
		init();
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	/**
	 * 初始化头布局， 脚布局， 监听
	 */
	private void init() {
		initHeadView();
		initAnimation();
		initFooterView();

		setOnScrollListener(this);
	}

	/**
	 * 初始化脚布局
	 */
	private void initFooterView() {
		mFooterView = View.inflate(getContext(), R.layout.layout_footer_list, null);

		footerTitle = (TextView) mFooterView.findViewById(R.id.tv_title);
		footerPB = (ProgressBar) mFooterView.findViewById(R.id.pb_refresh);

		mFooterView.measure(0, 0);// 按照布局文件中设置的规则测量
		footerMeasuredHeight = mFooterView.getMeasuredHeight();

		mFooterView.setPadding(0, -footerMeasuredHeight, 0, 0);
		addFooterView(mFooterView);

	}

	/**
	 * 初始化头布局
	 */
	private void initHeadView() {
		mHeaderView = View.inflate(getContext(), R.layout.layout_header_list, null);

		tv_title = (TextView) mHeaderView.findViewById(R.id.tv_title);
		tv_last_time = (TextView) mHeaderView.findViewById(R.id.tv_last_time);
		pb_refresh = (ProgressBar) mHeaderView.findViewById(R.id.pb_refresh);
		iv_arrow = (ImageView) mHeaderView.findViewById(R.id.iv_arrow);

		last_refresh_time = "";
		tv_last_time.setText(last_refresh_time);
		// 由于onResume还未执行，所以onMeasure()还未执行，getHeight()得不到高度
		// 因此此处要手动测量控件尺寸
		mHeaderView.measure(0, 0);// 按照布局文件中设置的规则测量
		measuredHeight = mHeaderView.getMeasuredHeight();

		// 设置内边距为自身高度，初始状态影藏头部件
		mHeaderView.setPadding(0, -measuredHeight, 0, 0);
		addHeaderView(mHeaderView);
	}

	/**
	 * 初始化动画
	 */
	private void initAnimation() {
		mRotateUpAnim = new RotateAnimation(0, -180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateUpAnim.setDuration(500);
		mRotateUpAnim.setFillAfter(true);

		mRotateDownAnim = new RotateAnimation(-180f, -360f, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		mRotateDownAnim.setDuration(500);
		mRotateDownAnim.setFillAfter(true);

	}

	// 设置触摸事件
	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downY = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:

			if (currentState == REFRESHING) {
				return super.onTouchEvent(ev);
			}

			moveY = ev.getY();
			float offset = moveY - downY;

			// 当偏移量大于0且第一个条目在顶端
			if (offset > 0 && getFirstVisiblePosition() == 0) {
				tv_last_time.setText(last_refresh_time);
				paddingTop = (int) (offset - measuredHeight);
				mHeaderView.setPadding(0, paddingTop, 0, 0);

				if (paddingTop > 0 && currentState != RELEASE_REFRESH) {
					// 不为释放刷新状态
					System.out.println("切换为释放刷新状态");
					currentState = RELEASE_REFRESH;
					updateHeader(currentState);
				} else if (paddingTop <= 0 && currentState != PULL_TO_REFRESH) {
					// 不为下拉刷新状态
					System.out.println("切换为下拉刷新状态");
					currentState = PULL_TO_REFRESH;
					updateHeader(currentState);
				}

				return true;
			}
			break;
		case MotionEvent.ACTION_UP:

			if (paddingTop <= 0) {
				// 不完全显示，恢复
				mHeaderView.setPadding(0, -measuredHeight, 0, 0);
			} else if (paddingTop > 0 && getFirstVisiblePosition() == 0) {
				last_refresh_time = getCurrentTime();
				System.out.println(last_refresh_time);
				// 完全显示，执行正在刷新
				mHeaderView.setPadding(0, 0, 0, 0);
				currentState = REFRESHING;
				updateHeader(currentState);
			}

			break;

		default:
			break;
		}

		return super.onTouchEvent(ev);// ListView的一些触摸事件由此响应，此处不能删除
	}

	public void updateHeader(int currentState) {
		switch (currentState) {
		case RELEASE_REFRESH:

			tv_title.setText("释放刷新");
			iv_arrow.setAnimation(mRotateUpAnim);
			break;

		case PULL_TO_REFRESH:

			tv_title.setText("下拉刷新");
			iv_arrow.setAnimation(mRotateDownAnim);
			break;

		case REFRESHING:
			iv_arrow.clearAnimation();
			iv_arrow.setVisibility(View.INVISIBLE);
			pb_refresh.setVisibility(View.VISIBLE);
			tv_title.setText("正在刷新");

			if (mListener != null) {
				mListener.onRefresh();// 通知调用者加载数据

			}
			break;
		default:
			break;
		}
	}

	public interface OnRefreshListener {

		public void onRefresh();

		public void loadMore();
	}

	public void setOnRefreshListener(OnRefreshListener mListener) {
		this.mListener = mListener;

	}

	/**
	 * 获取系统24小时制的时间
	 * 
	 * @return 系统24小时制的时间
	 */
	public String getCurrentTime() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.getDefault());
		String currentTime = sdf.format(new Date());

		return "最近刷新:" + currentTime;
	}

	/**
	 * 刷新完成, 恢复界面
	 */
	public void refreshComplete() {

		if (isLoadingMore) {
			// 上滑加载更多
			setSelection(getCount());
			// 影藏脚布局
			mFooterView.setPadding(0, -footerMeasuredHeight, 0, 0);
			isLoadingMore = false;
		} else {
			// 下拉刷新
			currentState = PULL_TO_REFRESH;
			mHeaderView.setPadding(0, -measuredHeight, 0, 0);
			tv_title.setText("下拉刷新");
			iv_arrow.setVisibility(View.VISIBLE);
			pb_refresh.setVisibility(View.INVISIBLE);
		}

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// 状态改变
		// SCROLL_STATE_IDLE = 0;//空闲
		// SCROLL_STATE_TOUCH_SCROLL = 1;//触摸滑动
		// SCROLL_STATE_FLING = 2;//松手后滑行
		System.out.println("scrollState" + scrollState);
		if (isLoadingMore) {
			return;
		} else {
			// 最新状态为空闲状态，并且当前界面显示了最后一条， 则加载更多
			if (scrollState == SCROLL_STATE_IDLE && getLastVisiblePosition() >= getCount() - 1) {
				System.out.println("加载更多");
				isLoadingMore = true;
				mFooterView.setPadding(0, 0, 0, 0);
				setSelection(getCount());// 让脚布局显示出来
				if (mListener != null) {
					System.out.println("通知调用者加载更多");
					mListener.loadMore();// 通知调用者加载更多
				}
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// 滑动过程中

	}

}
