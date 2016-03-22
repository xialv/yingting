package com.yitingche.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.yitingche.demo.R;
import com.yitingche.demo.controller.LoginManager;
import com.yitingche.demo.event.LoginEvent;

import de.greenrobot.event.EventBus;

/**
 * Created by lvxia on 16/3/7.
 */
public class MenuLoginView extends LinearLayout {
    private Context mContext;
    private View mLoginedView;
    private View mLoginBtn;
    private OnLoginClickListener mLoginClickListener = null;

    public MenuLoginView(Context context) {
        this(context, null, 0);
    }

    public MenuLoginView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuLoginView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initView();
    }

    public void setLoginClickListener(OnLoginClickListener listener){
        mLoginClickListener = listener;
    }
    private void initView(){
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.menu_login_item, this, true);
        mLoginedView = rootView.findViewById(R.id.menu_login);
        mLoginBtn = rootView.findViewById(R.id.login_btn);
        if (LoginManager.getInstance().isLogined(mContext)){
            mLoginedView.setVisibility(VISIBLE);
            mLoginBtn.setVisibility(GONE);
        } else {
            mLoginBtn.setVisibility(VISIBLE);
            mLoginedView.setVisibility(GONE);
        }
        mLoginBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoginClickListener != null){
                    mLoginClickListener.onLoginClick();
                }
                LoginManager.getInstance().gotoLoginActivity(mContext);
            }
        });
    }

    public void showLogin(boolean isLogin){
        if (isLogin){
            mLoginBtn.setVisibility(GONE);
            mLoginedView.setVisibility(VISIBLE);
        } else {
            mLoginedView.setVisibility(GONE);
            mLoginBtn.setVisibility(VISIBLE);
        }
    }



    public interface OnLoginClickListener{
        public void onLoginClick();
    }
}
