package com.yitingche.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yitingche.demo.R;
import com.yitingche.demo.controller.MenuInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lvxia on 16/3/7.
 */
public class DrawerAdapter extends BaseAdapter {
    private Context mContext;
    private List<MenuInfo> mDataList = new ArrayList<MenuInfo>();
    public DrawerAdapter(Context context) {
        this.mContext = context;
    }

    public void setDataList(List<MenuInfo> list){
        mDataList.clear();
        if (list != null) {
            mDataList.addAll(list);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDataList != null ? mDataList.size() : 0;
    }

    @Override
    public MenuInfo getItem(int position) {
        if (mDataList != null && position < mDataList.size()){
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
        MenuHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.menu_item, parent, false);
            holder = new MenuHolder();
            holder.menuName = (TextView) convertView.findViewById(R.id.menu_tv);
            holder.menuContent = (TextView) convertView.findViewById(R.id.menu_right);
            convertView.setTag(holder);
        } else {
            holder = (MenuHolder) convertView.getTag();
        }
        MenuInfo info = getItem(position);
        holder.menuName.setText(info.menuName);
        holder.menuContent.setText(info.menuDesc);
        return convertView;
    }

    public static class MenuHolder{
        TextView menuName;
        TextView menuContent;
    }
}
