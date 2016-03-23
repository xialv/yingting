package com.yitingche.demo.controller;

import android.content.Context;
import android.content.Intent;

import com.litesuits.http.data.GsonImpl;
import com.michael.corelib.internet.InternetClient;
import com.michael.corelib.internet.core.NetworkResponse;
import com.michael.corelib.internet.core.RequestBase;
import com.yitingche.demo.Utils.HttpRequestInterface;
import com.yitingche.demo.Utils.SharePreferenceHelper;
import com.yitingche.demo.activity.LoginActivity;
import com.yitingche.demo.event.LoginEvent;

import java.io.IOException;
import java.net.NetworkInterface;

import de.greenrobot.event.EventBus;

/**
 * Created by lvxia on 16/3/7.
 */
public class LoginManager {
    private static LoginManager mInstance = null;
    private boolean mLogined = false;
    private long mUserId = 0L;
    private String mAccount = "";

    private LoginManager(){
    }

    public static final LoginManager getInstance(){
        if (mInstance == null){
            synchronized (LoginManager.class){
                if (mInstance == null){
                    mInstance = new LoginManager();
                }
            }
        }
        return mInstance;
    }

    private void initLoginState(){
    }

    public boolean isLogined(Context context){
        mLogined = SharePreferenceHelper.instance(context).getIsLogined();
        mAccount = SharePreferenceHelper.instance(context).getLoginAccount();
        mUserId = SharePreferenceHelper.instance(context).getLoginId();
        return mLogined;
    }

    public long getUserId(){
        if (mLogined) {
            return mUserId;
        }
        return 0L;
    }

    public String getAccount(){
        if (mLogined) {
            return mAccount;
        }
        return "";
    }

    public synchronized void setLoginState(Context context, long userId, String account){
        mLogined = userId > 0;
        mUserId = userId;
        mAccount = account;
        SharePreferenceHelper.instance(context).saveLoginAccount(userId, mAccount);
    }

    public void logout(){
        mLogined = false;
        EventBus.getDefault().postSticky(new LoginEvent(false, "退出登录"));
    }
    public void gotoLoginActivity(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }


}
