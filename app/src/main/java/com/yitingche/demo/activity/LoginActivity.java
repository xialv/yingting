package com.yitingche.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

import de.greenrobot.event.EventBus;

/**
 * Created by lvxia on 16/3/7.
 */
public class LoginActivity extends MyBaseActivity implements View.OnClickListener {
    private EditText mAcountEdit;
    private EditText mPasswordEdit;
    private View mLoginBtn;
    private View mRegisterBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isUseCustomActionBar = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initview();
        if(!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    private void initview(){
        mAcountEdit = (EditText) findViewById(R.id.account_edit_text);
        mPasswordEdit = (EditText) findViewById(R.id.password_edit_text);
        mLoginBtn = findViewById(R.id.login_btn);
        mRegisterBtn = findViewById(R.id.register_btn);
        setCustomActionBarTitle("登录");
        initEvent();
    }

    private void initEvent(){
        mLoginBtn.setOnClickListener(this);
        mRegisterBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewid = v.getId();
        switch (viewid){
            case R.id.login_btn:
                doLogin(mAcountEdit.getText().toString(), mPasswordEdit.getText().toString());
                break;
            case R.id.register_btn:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void doLogin(final String account, String password) {
        SerResponse result = null;
        HttpRequestInterface.login(this, account, password, new InternetClient.NetworkCallback<String>() {
            @Override
            public void onSuccess(RequestBase<String> stringRequestBase, String s) {
                Gson gson = new Gson();
                SerResponse response = gson.fromJson(s, SerResponse.class);
                if (response != null) {
                    LoginEvent event = new LoginEvent(true, "");
                    if (response.rs) {
                        event.loginSuccess = true;
                        event.userId = response.id;
                        event.account = account;
                        EventBus.getDefault().post(event);
                    } else {
                        event.loginSuccess = false;
                        event.message = response.msg;
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

    public void onEventMainThread(LoginEvent event) {
        if (event != null){
            if(!event.loginSuccess){
                Toast.makeText(LoginActivity.this, TextUtils.isEmpty(event.message) ? "登录出错" : event.message, Toast.LENGTH_SHORT).show();
            } else {
                LoginManager.getInstance().setLoginState(this, event.userId, event.account);
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
