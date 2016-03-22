package com.yitingche.demo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.michael.corelib.internet.InternetClient;
import com.michael.corelib.internet.core.NetworkResponse;
import com.michael.corelib.internet.core.RequestBase;
import com.yitingche.demo.R;
import com.yitingche.demo.Utils.HttpRequestInterface;
import com.yitingche.demo.controller.LoginManager;
import com.yitingche.demo.controller.SerResponse;
import com.yitingche.demo.event.LoginEvent;
import com.yitingche.demo.event.ResultEvent;

import de.greenrobot.event.EventBus;

/**
 * Created by lvxia on 16/3/7.
 */
public class AddCardActivity extends MyBaseActivity implements View.OnClickListener {
    private EditText mCardEdit;
    private View mAddCardBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isUseCustomActionBar = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);
        initview();
        if(!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    private void initview(){
        mCardEdit = (EditText) findViewById(R.id.card_edit_text);
        mAddCardBtn = findViewById(R.id.add_card_btn);
        mAddCardBtn.setOnClickListener(this);

        setCustomActionBarTitle("添加车辆");
    }

    @Override
    public void onClick(View v) {
        int viewid = v.getId();
        switch (viewid){
            case R.id.add_card_btn:
                addCard(mCardEdit.getText().toString());
                break;
        }
    }

    private void addCard(String cardNo) {
        final SerResponse result = null;
        HttpRequestInterface.addCardNo(this, LoginManager.getInstance().getUserId(), cardNo, new InternetClient.NetworkCallback<String>() {
            @Override
            public void onSuccess(RequestBase<String> stringRequestBase, String s) {
                Gson gson = new Gson();
                SerResponse response = gson.fromJson(s, SerResponse.class);
                if (response != null) {
                    ResultEvent event = new ResultEvent(0, response, 0);
                    if (response.rs) {
                        event.state = 1;
                        EventBus.getDefault().post(event);
                    } else {
                        event.state = 0;
                        EventBus.getDefault().post(event);
                    }
                }
            }

            @Override
            public void onFailed(RequestBase<String> stringRequestBase, NetworkResponse networkResponse) {
                EventBus.getDefault().post(new LoginEvent(false, "网络请求出错"));
            }
        });
    }

    public void onEventMainThread(ResultEvent event) {
        if (event != null){
            if(event.state == 0){
                Toast.makeText(AddCardActivity.this, "网络请求出错", Toast.LENGTH_SHORT).show();
            } else {
                finish();
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
