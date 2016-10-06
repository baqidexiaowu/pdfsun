package com.jz.pdf.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.jz.pdf.R;
import com.jz.pdf.utils.CommonUtils;


/***
 * 抽取的类LoadingPage继承 FrameLayout
 * 
 * 1.可以让 LoadingPage中添加View显示和隐藏
 * 
 * 2.可以作为 Fragment碎片需要添加的View操作
 */
public abstract class LoadingPage extends FrameLayout {

	// 需要使用的枚举类型
	enum PageState {
		STATE_LOADING, // 加载中
		STATE_ERROR, // 加载失败
		STATE_SUCCESS// 加载成功
	}

	// 定义当前的默认状态
	private PageState mstate = PageState.STATE_LOADING;

	// 需要使用的三个视图的操作
	private View loadingView;
	private View errorView;
	private View successView;

	// ====【构造方法】======
	public LoadingPage(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// 每一个构造方法当中,都去实现的方法
		initLoadingPage();
	}

	public LoadingPage(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 每一个构造方法当中,都去实现的方法
		initLoadingPage();
	}

	public LoadingPage(Context context) {
		super(context);
		// 每一个构造方法当中,都去实现的方法
		initLoadingPage();
	}

	/***
	 * 初始化当前的视图操作
	 */
	private void initLoadingPage() {

		// 定义当前的LayoutParams的状态
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);

		// 定义当前的进度条的View.视图填充器
		// 判断上面是否是空值
		if (loadingView == null) {
			// 创建视图
			loadingView = View.inflate(getContext(), R.layout.page_loading,
					null);
		}
		// 添加到帧布局当中
		addView(loadingView, params);
		//-----------------------------------------------------------
		
		
		
		// 判断上面的是否是空值
		if (errorView == null) {
			// 创建视图
			errorView = View.inflate(getContext(), R.layout.page_error, null);
			//设置当前的按钮的点击事件.在我们点击按钮的时候进行重新加载的操作
			Button btn_reload = (Button) errorView.findViewById(R.id.btn_reload);
			//设置当前的按钮的点击事件
			btn_reload.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					//在调用操作之前.更改当前的状态值
					mstate = PageState.STATE_LOADING;
					//显示当前的状态
					showPage();
					//重新调用当前的加载数据刷新的操作
					LoadDataAndRefreshPage();
				}
			});
		}
		// 添加到帧布局当中
		addView(errorView, params);
		//----------------------------------------------------------
		
		
		
		
		// 判断上面的是否是空值
		if (successView == null) {
			// 需要自己去动态的添加显示的视图操作
			//createSuccessView()是一个抽象方法.交给各自的子类去实现不同的碎片界面
			successView = createSuccessView();
		}
		// 判断当前的是否实现了当前的视图
		if (successView == null) {
			// 抛出非法参数的异常
			throw new IllegalArgumentException(
					"The method createSuccessView() can not null");
		} else {
			// 添加到视图当中
			addView(successView, params);
		}

		// 显示当前的Page
		showPage();
		// 去服务器请求数据,并且加载
		LoadDataAndRefreshPage();
	}

	
	
	
	
	/***
	 * 根据不同的状态去设置不同的显示情况,基础思路:
	 * 
	 * 1.先将所有的界面都设置为 INVISIBLE 不显示状态
	 * 
	 * 2.根据当前的状态值.选择指定的显示  VISIBLE
	 */
	private void showPage() {
		// 如何实现当前的视图的加载显示或者隐藏过程
		loadingView.setVisibility(View.INVISIBLE);
		errorView.setVisibility(View.INVISIBLE);
		successView.setVisibility(View.INVISIBLE);
		// 分支选择当前的状态
		switch (mstate) {
		// 正在加载的状态
		case STATE_LOADING:
			loadingView.setVisibility(View.VISIBLE);
			break;
		// 加载失败的状态
		case STATE_ERROR:
			errorView.setVisibility(View.VISIBLE);
			break;
		// 加载成功的状态
		case STATE_SUCCESS:
			successView.setVisibility(View.VISIBLE);
			break;
		}
	}

	// 等待着继承的实现构造方法.子类分别去实现各自的视图操作
	public abstract View createSuccessView();
	
	//加载过程当中实现的方法
	protected abstract Object loadData();

	/***
	 * 加载按数据.并且刷新当前的页面
	 */
	protected void LoadDataAndRefreshPage() {

		// 开启子线程去加载数据
		new Thread() {
			public void run() {
				//测试耗时的操作
				//SystemClock.sleep(1500);
				// 加载数据的操作
				Object data = loadData();
				// 得到加载的数据.判断当前的数据是否是空值.根据当前的值去判断是否加载成功
				// 如果当前的结果是空值.那么就是加载失败.
				// 如果当前的结果不是空值.那么就是加载成功.
				mstate = (data == null ? PageState.STATE_ERROR
						: PageState.STATE_SUCCESS);
				// 得到当前的状态之后.提交当前的数据.在主线程当中,实现界面刷新的操作
				CommonUtils.runOnUIThread(new Runnable() {
					public void run() {
						//提交数据到主线程
						showPage();
					}
				});
			};
		}.start();
	}



}
