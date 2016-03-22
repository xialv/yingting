package com.yitingche.demo.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yitingche.demo.R;
import com.yitingche.demo.activity.ParkDetailActivity;
import com.yitingche.demo.controller.LoginManager;
import com.yitingche.demo.controller.Park;

/**
 * Created by lvxia on 16/3/7.
 */
public class ParkInfoView extends LinearLayout {
    private Context mContext;
    private TextView mParkName;
    private TextView mParkAddress;
    private TextView mParkSite;
    private View mParkDetailBtn;
    private View mParkNaviBtn;

    private OnNaviClickListener mListener;
    private Park mParkInfo;

    public ParkInfoView(Context context) {
        this(context, null, 0);
    }

    public ParkInfoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ParkInfoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initView();
    }

    public void setData(Park park){
        if (park == null){
            return;
        }
        mParkInfo = park;
        mParkName.setText(park.name);
        mParkAddress.setText(park.addr);
        mParkSite.setText("(车位:" + park.freeSeatNum + "/" + park.seatNum + ")");
    }

    private void initView(){
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.park_info, this, true);
        mParkNaviBtn = rootView.findViewById(R.id.park_navi_btn);
        mParkNaviBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onNaviClick(mParkInfo);
                }
            }
        });
        mParkName = (TextView)rootView.findViewById(R.id.park_name);
        mParkAddress = (TextView)rootView.findViewById(R.id.park_address);
        mParkSite = (TextView)rootView.findViewById(R.id.park_site);
        mParkDetailBtn = rootView.findViewById(R.id.detail_btn);
        mParkDetailBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ParkDetailActivity.class);
                intent.putExtra("name", mParkInfo.name);
                intent.putExtra("address", mParkInfo.addr);
                intent.putExtra("seatnum", mParkInfo.seatNum);
                intent.putExtra("freeseat", mParkInfo.freeSeatNum);
                intent.putExtra("lng", mParkInfo.coordinateY);
                intent.putExtra("lat", mParkInfo.coordinateX);
                mContext.startActivity(intent);
            }
        });
    }

    public void setNaviClickListener(OnNaviClickListener listener){
        mListener = listener;
    }

    public interface OnNaviClickListener{
        public void onNaviClick(Park park);
    }
}
