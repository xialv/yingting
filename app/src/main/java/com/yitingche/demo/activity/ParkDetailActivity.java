package com.yitingche.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.michael.corelib.internet.InternetClient;
import com.michael.corelib.internet.core.NetworkResponse;
import com.michael.corelib.internet.core.RequestBase;
import com.yitingche.demo.R;
import com.yitingche.demo.Utils.HttpRequestInterface;
import com.yitingche.demo.controller.CardNo;
import com.yitingche.demo.controller.CardResponse;
import com.yitingche.demo.controller.LoginManager;
import com.yitingche.demo.controller.Park;
import com.yitingche.demo.controller.ParkInfoResponse;
import com.yitingche.demo.controller.ParkResponse;
import com.yitingche.demo.controller.SerResponse;
import com.yitingche.demo.event.ResultEvent;

import java.lang.reflect.Type;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by lvxia on 16/3/7.
 */
public class ParkDetailActivity extends MyBaseActivity{

    public static final String PARK_ID = "park_id";
    public static final String PARK_NAME = "park_name";
    public static final String PARK_SEAT = "park_seat";
    public static final String PARK_ADDRESS = "park_address";
    public static final String PARK_FREE_SEAT = "park_free_seat";
    public static final String PARK_LNG = "park_lng";
    public static final String PARK_LAT = "park_lat";

    private TextView mParkNameTv;
    private TextView mParkSiteTv;
    private TextView mParkAddressTv;
    private TextView mParkFeeTv;
    private TextView mParkServiceTv;
    private View mNaviBtn;

    private long mParkId;
    private String mParkName;
    private int mParkSeat;
    private int mParkFreeSeat;
    private String mParkAddress;
    private double mParkLng;
    private double mParkLat;



    protected void onCreate(Bundle savedInstanceState) {
        isUseCustomActionBar = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park_detail);
        initview();
        if(!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    private void initview(){
        mParkNameTv = (TextView) findViewById(R.id.park_name);
        mParkAddressTv = (TextView) findViewById(R.id.park_address);
        mParkFeeTv = (TextView) findViewById(R.id.park_fee);
        mParkServiceTv = (TextView) findViewById(R.id.park_service);
        mParkSiteTv = (TextView) findViewById(R.id.park_site);
        mNaviBtn = findViewById(R.id.navi_btn);
        setCustomActionBarTitle("详情");

        getIntentData();

        mNaviBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intents = new Intent();
                intents.putExtra(PARK_LNG, mParkLng);
                intents.putExtra(PARK_LAT, mParkLat);
                setResult(4, intents);
                finish();
            }
        });
        getData();
    }

    private void getIntentData(){
        Intent intent = getIntent();
        mParkId = intent.getLongExtra(PARK_ID, 0L);
        mParkName = intent.getStringExtra(PARK_ADDRESS);
        mParkAddress = intent.getStringExtra(PARK_ADDRESS);
        mParkSeat = intent.getIntExtra(PARK_SEAT, 0);
        mParkFreeSeat = intent.getIntExtra(PARK_FREE_SEAT, 0);
        mParkLng= intent.getDoubleExtra(PARK_LNG, 0);
        mParkLat = intent.getDoubleExtra(PARK_LAT, 0);

        mParkNameTv.setText(mParkName);
        mParkAddressTv.setText(mParkAddress);
        mParkSiteTv.setText("" + mParkFreeSeat + "/" + mParkSeat);
    }

    private void getData() {
        final SerResponse result = null;
        HttpRequestInterface.getParkInfo(this, mParkId, new InternetClient.NetworkCallback<String>() {
            @Override
            public void onSuccess(RequestBase<String> stringRequestBase, String s) {
                Gson gson = new Gson();
                ParkInfoResponse response = gson.fromJson(s, ParkInfoResponse.class);

                if (response != null) {
                    EventBus.getDefault().post(new ResultEvent(1, response, 0));
                } else {
                    EventBus.getDefault().post(new ResultEvent(0, null, 0));
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
                if (event.result != null) {
                    ParkInfoResponse response = (ParkInfoResponse) event.result;
                    updateData(response);
                }
            }
        }
    }

    private void updateData(ParkInfoResponse response){
        String parkservice = "";
        if (response.parkServices == null || response.parkServices.size() == 0){
            parkservice = "无";
        } else {
            for (ParkInfoResponse.ParkService service : response.parkServices) {
                parkservice += service.content + "\n";
            }
        }
        mParkServiceTv.setText(parkservice);
        String parkRete = "";
        if (response.reteRuleInfo != null && response.reteRuleInfo.capPrice != 0) {
            if (response.reteRuleInfo.containFreeTime.equals("Y")) {
                parkRete += response.reteRuleInfo.freeTime + "分钟内免费\n";
            }

            for (ParkInfoResponse.TimeSeg seg : response.reteRuleInfo.timeSegLst){
                parkRete += seg.startTime + " ~ "+ seg.endTime + "  " + seg.unitPrice + "元/" + seg.unitTime + "分钟\n";
            }
        } else {
            parkRete = "免费";
        }
        mParkFeeTv.setText(parkRete);

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
