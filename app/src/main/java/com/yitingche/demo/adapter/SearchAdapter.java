package com.yitingche.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.yitingche.demo.R;
import com.yitingche.demo.view.SearchItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lvxia on 16/3/5.
 */
public class SearchAdapter extends BaseAdapter {
    List<SearchItem> mDataList = new ArrayList<SearchItem>();
    private Context mContext;

    public SearchAdapter(Context context, List<SearchItem> mDataList) {
        this.mContext = context;
        if (mDataList != null) {
            this.mDataList.addAll(mDataList);
        }
    }

    public void setDataList(List<SearchItem> list){
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
    public SearchItem getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.search_list_item, parent, false);
            holder = new ViewHolder();
            holder.mIcon = convertView.findViewById(R.id.type_icon);
            holder.mTextTitle = (TextView)convertView.findViewById(R.id.search_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.mTextTitle.setText(mDataList.get(position).searchname);
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
        public View mIcon;
    }
}
