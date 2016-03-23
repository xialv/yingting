package com.yitingche.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.michael.corelib.internet.InternetClient;
import com.michael.corelib.internet.core.NetworkResponse;
import com.michael.corelib.internet.core.RequestBase;
import com.michael.corelib.internet.core.util.JsonUtils;
import com.yitingche.demo.R;
import com.yitingche.demo.Utils.HttpRequestInterface;
import com.yitingche.demo.adapter.CardAdapter;
import com.yitingche.demo.controller.CardNo;
import com.yitingche.demo.controller.CardResponse;
import com.yitingche.demo.controller.LoginManager;
import com.yitingche.demo.controller.SerResponse;
import com.yitingche.demo.event.ResultEvent;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by lvxia on 16/3/7.
 */
public class CardActivity extends MyBaseActivity implements View.OnClickListener {
    private ListView mListview;
    private View mAddCardView;
    private CardAdapter mAdapter;


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
        View view = View.inflate(this, R.layout.add_card, null);
        mAddCardView = view.findViewById(R.id.add_card_btn);
        mAddCardView.setOnClickListener(this);
        mListview = (ListView)findViewById(R.id.card_listview);
        mListview.addFooterView(view);

        mAdapter = new CardAdapter(this);
        mListview.setAdapter(mAdapter);
        setCustomActionBarTitle("我的车牌");
        getData();
    }

    @Override
    public void onClick(View v) {
        int viewid = v.getId();
        switch (viewid){
            case R.id.add_card_btn:
                Intent intent = new Intent(this, AddCardActivity.class);
                startActivityForResult(intent, 1000);
                break;
        }
    }

    private void getData() {
        final SerResponse result = null;
        HttpRequestInterface.getCardList(this, LoginManager.getInstance().getUserId(), new InternetClient.NetworkCallback<String>() {
            @Override
            public void onSuccess(RequestBase<String> stringRequestBase, String s) {
//                CardResponse response = JsonUtils.parse(s, CardResponse.class);
                Gson gson = new Gson();
                Type listType = new TypeToken<List<CardNo>>(){}.getType();
                CardResponse response = new CardResponse();
                response.carNo = gson.fromJson(s, listType);
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
                Toast.makeText(CardActivity.this, "网络请求出错", Toast.LENGTH_SHORT).show();
            } else {
                mAdapter.setDataList((List<CardNo>) event.result);
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
