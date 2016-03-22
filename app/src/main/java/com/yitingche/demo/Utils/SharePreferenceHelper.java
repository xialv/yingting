package com.yitingche.demo.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Jason on 2015/7/22.
 */
public class SharePreferenceHelper {
    private static final String SP_NAME = "YingTingCheSharePreference";
    private static final String KEY_LOGIN_ID = "LOGIN_ID";
    private static final String KEY_LOGIN_ACCOUNT = "LOGIN_ACCOUNT";
    private static final String KEY_IS_LOGIN = "IS_LOGIN";

    private volatile static SharePreferenceHelper sInstance;

    private Context mContext;

    private SharePreferenceHelper(Context context) {
        this.mContext = context;
    }

    public static SharePreferenceHelper instance(Context context) {
        if (sInstance == null) {
            synchronized (SharePreferenceHelper.class) {
                if (sInstance == null) {
                    sInstance = new SharePreferenceHelper(context);
                }
            }
        }
        return sInstance;
    }

    private SharedPreferences getSp() {
        return mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    private SharedPreferences.Editor getEditor() {
        return getSp().edit();
    }

    public void saveLoginAccount(long userId, String account) {
        if (userId > 0){
            getEditor().putBoolean(KEY_IS_LOGIN, true).apply();
        } else {
            getEditor().putBoolean(KEY_IS_LOGIN, false).apply();
        }
        getEditor().putLong(KEY_LOGIN_ID, userId).apply();
        getEditor().putString(KEY_LOGIN_ACCOUNT, account).apply();
    }

    public boolean getIsLogined(){
        return getSp().getBoolean(KEY_IS_LOGIN, false);
    }

    public String getLoginAccount() {
        return getSp().getString(KEY_LOGIN_ACCOUNT, null);
    }

    public long getLoginId() {
        return getSp().getLong(KEY_LOGIN_ID, 0L);
    }
}
