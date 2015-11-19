package com.lostad.app.demo.fragment;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lostad.app.base.util.DialogUtil;
import com.lostad.app.base.view.fragment.BaseFragment;
import com.lostad.app.demo.R;
import com.lostad.app.demo.entity.Tour;
import com.lostad.app.demo.entity.TourList4j;
import com.lostad.app.demo.manager.TourManager;
import com.lostad.app.demo.tour.OrderPayActivity;
import com.lostad.applib.util.Validator;
import com.lostad.applib.view.listview.ListViewPull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sszvip
 * 
 */
public class ListTourFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2,
		PullToRefreshBase.OnLastItemVisibleListener,OnItemClickListener {

	@ViewInject(R.id.lv_data)
	private ListViewPull lv_data;

    //正在加载
	@ViewInject(R.id.ll_loading)
	private View ll_loading;

	@ViewInject( R.id.tv_loading)
	private TextView tv_loading;

	@ViewInject(R.id.iv_loading)
	private ImageView iv_loading;

	private ListTourAdapter mAdapter;
	private List<Tour> mListData;
    private String mType = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		super.onCreateView(inflater,container,savedInstanceState);
		mType = getArguments().getString("type");

		View rootView = inflater.inflate(R.layout.fragment_list_rank, container, false);
		ViewUtils.inject(this, rootView);//注入

		lv_data.setMode(ListViewPull.Mode.BOTH);
		lv_data.setOnRefreshListener(this);
		lv_data.setOnLastItemVisibleListener(this);
		lv_data.setOnItemClickListener(this);
		//lv_data.getRefreshableView().setDivider(null);

		ListView actualListView = lv_data.getRefreshableView();
		registerForContextMenu(actualListView);
		mListData= new ArrayList<Tour>();
		mAdapter = new ListTourAdapter(mType,ctx, mListData);
		actualListView.setAdapter(mAdapter);
		//主动加载
		lv_data.setRefreshing();

		return rootView;
	}

	@Override
	public void onLastItemVisible() {
		lv_data.setLoadingMore();
	}
	@Override
	public void onPullDownToRefresh(PullToRefreshBase refreshView) {
		//获取格式化的时间
		String label = DateUtils.formatDateTime(ctx, System.currentTimeMillis(),DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
		//	更新LastUpdatedLabel
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
		loadData(false);//自动加载更多
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase refreshView) {
		loadData(true);//自动加载更多
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Tour t = mListData.get(position-1);
		Intent i = new Intent(ctx,OrderPayActivity.class);
		i.putExtra("bean",t);
		startActivity(i);
	}

//	 @Override
//	 public void setUserVisibleHint(boolean isVisibleToUser) {
//	       super.setUserVisibleHint(isVisibleToUser);
//	       LogMe.d("rank","----------setUserVisibleHint------isVisibleToUser:"+isVisibleToUser);
//	       if (isVisibleToUser) {
//
//	       } else {
//	           //相当于Fragment的onPause
//	       }
//	   }
	 
    /**
     * 如果是下拉刷新，先不要清空数据，以免闪屏体验不好。
     * 上拉加载数据时，不清空数据
     * 功能描述:       isRefresh 是否为下拉刷新操作
     * @param:
     * @return:
     * @Author:      sszvip@qq.com
     * @Create Date: 2015-9-18下午5:10:16
     */
	private void loadData(final boolean isLoadMore) {
		showLoading();
		new Thread() {
			TourList4j g4j;
			public void run() {
                int start = 0;
				if(isLoadMore){//加载更多
					start = mListData.size();
				}
				g4j = TourManager.getInstance().listTourAll(mType, start);
				ctx.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (g4j.isSuccess()) {
							if(g4j.list!=null && g4j.list.size()>0){
								if(isLoadMore==false){//如果是刷新数据
									mListData.clear();//清空以前的
								}
								mListData.addAll(g4j.list);
								mAdapter.notifyDataSetChanged();
								dismissLoding(null);

							}else{
								dismissLoding("未查询到任何游学项目！");
							}
						} else {
							DialogUtil.showToastCust(ctx, g4j.getMsg());
							dismissLoding(null);
						}
						lv_data.onRefreshComplete();

					}


				});
			}
		}.start();
	}

	// ////////////////加载效果////////////////////////////////////////////////////////////////////////////////
	private void showLoading() {
		if (mListData == null || mListData.size() == 0) {
			ll_loading.setVisibility(View.VISIBLE);
			int res = R.drawable.frame;
			iv_loading.setImageResource(res);
			((AnimationDrawable) iv_loading.getDrawable()).start(); //
			tv_loading.setText("正在加载...");
		}
	}

	private void dismissLoding(String msg) {
       try{
		   ((AnimationDrawable) iv_loading.getDrawable()).stop();
		   if (mListData == null || mListData.size() == 0) {
			   ll_loading.setVisibility(View.VISIBLE);
			   iv_loading.setImageResource(R.mipmap.load_fail);
			   tv_loading.setText("加载数据失败，点击继续加载！");
		   } else {
			   ll_loading.setVisibility(View.GONE);
		   }

		   if(!Validator.isBlank(msg)){
			   tv_loading.setText(msg);
		   }
	   }catch (Exception e){
		   e.printStackTrace();
	   }

	}

}
