package com.yitingche.demo.view;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;

/**
 * Created by lvxia on 16/2/19.
 */
public class PagerBaseAdapter extends PagerAdapter {
    private List<View> mViewList;

    public PagerBaseAdapter(List<View> viewList){
        this.mViewList = viewList;
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2){
        ((ViewPager) arg0).removeView(mViewList.get(arg1));
    }

    @Override
    public int getCount() {
        return mViewList.size();
    }

    @Override
    public Object instantiateItem(View arg0, int arg1){
        ((ViewPager) arg0).addView(mViewList.get(arg1), 0);
        return mViewList.get(arg1);
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }
    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}
