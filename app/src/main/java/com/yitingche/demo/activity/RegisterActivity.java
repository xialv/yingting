package com.yitingche.demo.activity;

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
import com.yitingche.demo.controller.SerResponse;
import com.yitingche.demo.event.LoginEvent;

import de.greenrobot.event.EventBus;

/**
 * Created by lvxia on 16/3/7.
 */
public class RegisterActivity extends MyBaseActivity implements View.OnClickListener {
    private EditText mAcountEdit;
    private EditText mPasswordEdit;
    private EditText mRePasswordEdit;
    private View mRegisterBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isUseCustomActionBar = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initview();
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    private void initview(){
        setCustomActionBarTitle("注册");
        mAcountEdit = (EditText) findViewById(R.id.account_edit_text);
        mPasswordEdit = (EditText) findViewById(R.id.password_edit_text);
        mRePasswordEdit = (EditText) findViewById(R.id.password_re_edit_text);
        mRegisterBtn = findViewById(R.id.register_btn);

        initEvent();
    }

    private void initEvent(){
        mRegisterBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewid = v.getId();
        switch (viewid){
            case R.id.register_btn:
                if (!mAcountEdit.getText().toString().isEmpty()
                        && !mPasswordEdit.getText().toString().isEmpty()
                        && !mRePasswordEdit.getText().toString().isEmpty()
                        && mRePasswordEdit.getText().toString().equals(mPasswordEdit.getText().toString())){
                    doRegister(mAcountEdit.getText().toString(), mPasswordEdit.getText().toString());
                } else {
                    Toast.makeText(this, "信息有误", Toast.LENGTH_SHORT);
                }
                break;
        }
    }

    private void doRegister(final String account, String password) {
        SerResponse result = null;
        HttpRequestInterface.register(this, account, password, new InternetClient.NetworkCallback<String>() {
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
                Toast.makeText(RegisterActivity.this, TextUtils.isEmpty(event.message) ? "注册失败" : event.message, Toast.LENGTH_SHORT).show();
            } else {
//                LoginManager.getInstance().setLoginState(this, event.userId, event.account);
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }
}
