package com.yitingche.demo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.yitingche.demo.R;

/**
 * Created by lvxia on 16/3/7.
 */
public class RechargeActivity extends MyBaseActivity implements View.OnClickListener {
    private EditText mMoneyEdit;
    private View mPayBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isUseCustomActionBar = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        initview();
//        if(!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this);
//        }
    }

    private void initview(){
        mMoneyEdit = (EditText) findViewById(R.id.money_edit);
        mPayBtn = findViewById(R.id.pay_btn);
        mPayBtn.setOnClickListener(this);
        setCustomActionBarTitle("充值");
    }
    @Override
    public void onClick(View v) {
        int viewid = v.getId();
        switch (viewid){
            case R.id.pay_btn:
                Toast.makeText(this, "等待后期开发", Toast.LENGTH_SHORT).show();
                break;
        }
    }

//    private void doLogin(String account, String password) {
//        SerResponse result = null;
//        HttpRequestInterface.login(this, account, password, new InternetClient.NetworkCallback<String>() {
//            @Override
//            public void onSuccess(RequestBase<String> stringRequestBase, String s) {
//                Gson gson = new Gson();
//                SerResponse response = gson.fromJson(s, SerResponse.class);
//                if (response != null) {
//                    if (response.rs) {
//                        EventBus.getDefault().post(new LoginEvent(true, ""));
//                    } else {
//                        EventBus.getDefault().post(new LoginEvent(false, response.msg));
//                    }
//                }
//            }
//
//            @Override
//            public void onFailed(RequestBase<String> stringRequestBase, NetworkResponse networkResponse) {
//                EventBus.getDefault().post(new LoginEvent(false, "网络请求出错"));
//            }
//        });
//    }
//
//    public void onEventMainThread(LoginEvent event) {
//        if (event != null){
//            if(!event.loginSuccess){
//                Toast.makeText(RechargeActivity.this, TextUtils.isEmpty(event.message) ? "登录出错" : event.message, Toast.LENGTH_SHORT).show();
//            } else {
//            }
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if(EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().unregister(this);
//        }
    }
}
