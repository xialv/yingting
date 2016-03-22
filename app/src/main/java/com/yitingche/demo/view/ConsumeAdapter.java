package com.yitingche.demo.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yitingche.demo.R;
import com.yitingche.demo.controller.ConsumeInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lvxia on 16/3/11.
 */
public class ConsumeAdapter extends BaseAdapter {
    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private Context mContext;
    List<ConsumeInfo> mDataList = new ArrayList<ConsumeInfo>();

    public ConsumeAdapter(Context context){
        this.mContext = context;
    }
    public void setData(List<ConsumeInfo> list){
        mDataList.clear();
        if (list != null){
            mDataList.addAll(list);
        }
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public ConsumeInfo getItem(int position) {
        if (mDataList.size() > 0 && position < mDataList.size()) {
            return mDataList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.consume_item, null, false);
                holder = new ViewHolder();
                holder.timeTv = (TextView) convertView.findViewById(R.id.time_tv);
                holder.priceTv = (TextView) convertView.findViewById(R.id.price_tv);
                holder.locationTv = (TextView) convertView.findViewById(R.id.location_tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ConsumeInfo info = mDataList.get(position);
            if (TextUtils.isEmpty(info.parkId)) {
                holder.locationTv.setVisibility(View.GONE);
            } else {
                holder.locationTv.setVisibility(View.VISIBLE);
            }
            String location = mContext.getString(R.string.consume_location, info.parkId);
            holder.locationTv.setText(location);
            String time = mContext.getString(R.string.consume_time, info.createTime);
            holder.timeTv.setText(time);
            String price = mContext.getString(R.string.consume_price, info.money);
            holder.priceTv.setText(price);
        return convertView;
    }

    public static class ViewHolder{
        TextView timeTv;
        TextView priceTv;
        TextView locationTv;
    }
}
