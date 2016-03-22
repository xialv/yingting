package com.yitingche.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yitingche.demo.R;
import com.yitingche.demo.controller.CardNo;
import com.yitingche.demo.view.SearchItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lvxia on 16/3/5.
 */
public class CardAdapter extends BaseAdapter {
    List<CardNo> mDataList = new ArrayList<CardNo>();
    private Context mContext;

    public CardAdapter(Context context) {
        this.mContext = context;
    }

    public void setDataList(List<CardNo> list){
        mDataList.clear();
        if (list != null){
            mDataList.addAll(list);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public CardNo getItem(int position) {
        if (mDataList != null && position < getCount()){
            return mDataList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.card_list_item, parent, false);
            holder = new ViewHolder();
            holder.mTextTitle = (TextView)convertView.findViewById(R.id.card_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.mTextTitle.setText(mDataList.get(position).no);
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    class ViewHolder {
        public TextView mTextTitle;
    }
}
