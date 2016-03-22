package com.yitingche.demo.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.michael.corelib.internet.InternetClient;
import com.michael.corelib.internet.core.NetworkResponse;
import com.michael.corelib.internet.core.RequestBase;
import com.yitingche.demo.R;
import com.yitingche.demo.Utils.HttpRequestInterface;
import com.yitingche.demo.controller.ConsumeInfo;
import com.yitingche.demo.controller.ConsumeResponse;
import com.yitingche.demo.controller.LoginManager;
import com.yitingche.demo.controller.RechargeResponse;
import com.yitingche.demo.event.LoginEvent;
import com.yitingche.demo.event.ResultEvent;
import com.yitingche.demo.view.ConsumeAdapter;
import com.yitingche.demo.view.PagerBaseAdapter;
import com.yitingche.demo.view.SlidingTab;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by lvxia on 16/3/7.
 */
public class DealActivity extends MyBaseActivity{
    private SlidingTab mTab;
    private ViewPager mPager;
    private View mRechargeView;
    private View mConsumeView;
    private ConsumeAdapter mRechargeAdapter;
    private ConsumeAdapter mConsumeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isUseCustomActionBar = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal);
        initview();
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        initData();
    }

    private void initview(){
        mTab = (SlidingTab) findViewById(R.id.id_tab);
        mPager = (ViewPager) findViewById(R.id.id_view_pager);
        List<String> tabs = new ArrayList<String>();
        tabs.add("消费");
        tabs.add("充值");
        List<View> viewList = new ArrayList<View>();
        mRechargeView = LayoutInflater.from(this).inflate(R.layout.list_layout, null, false);
        mConsumeView = LayoutInflater.from(this).inflate(R.layout.list_layout, null, false);
        viewList.add(mConsumeView);
        viewList.add(mRechargeView);
        mConsumeAdapter = new ConsumeAdapter(this);
        mRechargeAdapter = new ConsumeAdapter(this);

        ListView rechargeList = (ListView) mRechargeView.findViewById(R.id.list_view);
        ListView consumeList = (ListView) mConsumeView.findViewById(R.id.list_view);
        rechargeList.setAdapter(mRechargeAdapter);
        consumeList.setAdapter(mConsumeAdapter);


        View emptyView = mRechargeView.findViewById(R.id.empty_view);
        rechargeList.setEmptyView(emptyView);
        View emptyView2 = mConsumeView.findViewById(R.id.empty_view);
        consumeList.setEmptyView(emptyView2);

        PagerBaseAdapter adapter = new PagerBaseAdapter(viewList);
        mPager.setAdapter(adapter);
        mTab.setViewPager(tabs, mPager);
        setCustomActionBarTitle("消息记录");
    }

    private void initData() {
        HttpRequestInterface.getRechargeList(this, LoginManager.getInstance().getUserId(), new InternetClient.NetworkCallback<String>() {
            @Override
            public void onSuccess(RequestBase<String> stringRequestBase, String s) {
                Gson gson = new Gson();
                ConsumeResponse response = gson.fromJson(s, ConsumeResponse.class);
                response = new ConsumeResponse();
                List<ConsumeInfo> infos = new ArrayList<ConsumeInfo>();
                ConsumeInfo item = new ConsumeInfo();
                item.createTime = "15年12月3号";
                item.money= "12";
                item.parkId="三元桥停车场";
                infos.add(item);
                if (response != null) {
                        EventBus.getDefault().post(new ResultEvent(1, infos, ResultEvent.FROM_RECHARGE));
                    } else {
                        EventBus.getDefault().post(new ResultEvent(0, null, ResultEvent.FROM_RECHARGE));
                    }
            }

            @Override
            public void onFailed(RequestBase<String> stringRequestBase, NetworkResponse networkResponse) {
                EventBus.getDefault().post(new ResultEvent());
            }
        });
    }

    public void onEventMainThread(ResultEvent event) {
        if (event != null){
            if(event.state == 1 && event.result != null){
                List<ConsumeInfo> infos = (List<ConsumeInfo>) event.result;
                if (event.from == ResultEvent.FROM_RECHARGE){
                    mRechargeAdapter.setData(infos);
                } else {
                    mConsumeAdapter.setData(null);
                }
            } else {
                if (event.from == ResultEvent.FROM_RECHARGE){
                    mRechargeAdapter.setData(null);
                } else {
                    mConsumeAdapter.setData(null);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
