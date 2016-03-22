package com.yitingche.demo.app;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.os.Vibrator;

import com.baidu.mapapi.SDKInitializer;
import com.yitingche.demo.Utils.WriteLog;
import com.yitingche.demo.map.LocationService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lvxia on 16/2/16.
 */
public class GlobalApplication extends Application{
    public LocationService locationService;
    public Vibrator mVibrator;

    private final String TAG = GlobalApplication.class.getSimpleName();
    private static GlobalApplication sInstance;
    private List<Activity> backFinishList = new ArrayList<Activity>();


    public static GlobalApplication getInstance() {
        if (null == sInstance){
            synchronized (GlobalApplication.class){
                if (null == sInstance){
                    sInstance = new GlobalApplication();
                }
            }
        }
        return sInstance;
    }

    public List<Activity> getBackFinishList() {
        return backFinishList;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        WriteLog.getInstance().init(); // 初始化日志
        SDKInitializer.initialize(getApplicationContext());

    }
}

