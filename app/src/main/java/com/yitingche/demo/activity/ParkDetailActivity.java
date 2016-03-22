package com.yitingche.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.michael.corelib.internet.InternetClient;
import com.michael.corelib.internet.core.NetworkResponse;
import com.michael.corelib.internet.core.RequestBase;
import com.yitingche.demo.R;
import com.yitingche.demo.Utils.HttpRequestInterface;
import com.yitingche.demo.adapter.CardAdapter;
import com.yitingche.demo.controller.CardNo;
import com.yitingche.demo.controller.CardResponse;
import com.yitingche.demo.controller.LoginManager;
import com.yitingche.demo.controller.SerResponse;
import com.yitingche.demo.event.ResultEvent;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by lvxia on 16/3/7.
 */
public class ParkDetailActivity extends MyBaseActivity{
    private TextView mParkName;
    private TextView mParkSite;
    private TextView mParkAddress;
    private TextView mParkFee;
    private TextView mParkService;
    private View mNaviBtn;


    protected void onCreate(Bundle savedInstanceState) {
        isUseCustomActionBar = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        initview();
        if(!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    private void initview(){
        View view = View.inflate(this, R.layout.activity_park_detail, null);
        mParkName = (TextView) view.findViewById(R.id.park_name);
        mParkAddress = (TextView) view.findViewById(R.id.park_address);
        mParkFee = (TextView) view.findViewById(R.id.park_fee);
        mParkService = (TextView) view.findViewById(R.id.park_service);
        mParkSite = (TextView) view.findViewById(R.id.park_site);
        mNaviBtn = view.findViewById(R.id.navi_btn);
        setCustomActionBarTitle("详情");
//        getData();
    }

    private void getData() {
        final SerResponse result = null;
        HttpRequestInterface.getCardList(this, LoginManager.getInstance().getUserId(), new InternetClient.NetworkCallback<String>() {
            @Override
            public void onSuccess(RequestBase<String> stringRequestBase, String s) {
//                CardResponse response = JsonUtils.parse(s, CardResponse.class);
                Gson gson = new Gson();
                CardResponse response = gson.fromJson(s, CardResponse.class);
//                //test
//                response = new CardResponse();
//                response.carNo = new ArrayList<CardNo>();
//                CardNo cardNo = new CardNo();
//                cardNo.no="湘NYJA48";
//                response.carNo.add(cardNo);
//                response.carNo.add(cardNo);

                if (response != null) {
                    if (response.carNo != null) {
                        EventBus.getDefault().post(new ResultEvent(1,response.carNo, 0));
                    } else {
                        EventBus.getDefault().post(new ResultEvent(0, null, 0));
                    }
                }
            }

            @Override
            public void onFailed(RequestBase<String> stringRequestBase, NetworkResponse networkResponse) {
                EventBus.getDefault().post(new ResultEvent(0, "网络请求出错", 0));
            }
        });
    }

    public void onEventMainThread(ResultEvent event) {
        if (event != null){
            if(event.state == 0){
                Toast.makeText(ParkDetailActivity.this, "网络请求出错", Toast.LENGTH_SHORT).show();
            } else {

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
