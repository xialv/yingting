package com.yitingche.demo.view;

import com.baidu.mapapi.model.LatLng;

/**
 * Created by lvxia on 16/3/5.
 */
public class SearchItem {
    public int type;
    public String searchname;
    public LatLng latLng;

    public SearchItem(){

    }
    public SearchItem(int type, String name, LatLng latLng){
        this.type = type;
        this.searchname = name;
        this.latLng = latLng;
    }
}
