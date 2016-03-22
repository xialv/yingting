package com.yitingche.demo.map;

import android.os.Bundle;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.yitingche.demo.R;
import com.yitingche.demo.controller.Park;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于显示poi的overly
 */
public class PoiOverlayNew extends OverlayManager {

    private static final int MAX_POI_SIZE = 10;

    private PoiResult mPoiResult = null;

    private List<Park> mParkList = new ArrayList<Park>();

    private Marker mMarker = null;

    /**
     * 构造函数
     *
     * @param baiduMap
     *            该 PoiOverlay 引用的 BaiduMap 对象
     */
    public PoiOverlayNew(BaiduMap baiduMap) {
        super(baiduMap);
    }

    /**
     * 设置POI数据
     * 
     * @param poiResult
     *            设置POI数据
     */
    public void setData(PoiResult poiResult) {
        this.mPoiResult = poiResult;
    }

    public void setParkList(List<Park> list){
        this.mParkList = list;
    }

    @Override
    public final List<OverlayOptions> getOverlayOptions() {

        if (mParkList == null || mParkList.size() == 0)
            return null;

        List<OverlayOptions> markerList = new ArrayList<OverlayOptions>();
        int markerSize = 0;
        for (int i = 0; i < mParkList.size()
                && markerSize < MAX_POI_SIZE; i++) {
            markerSize++;
            Bundle bundle = new Bundle();
            bundle.putInt("index", i);
            LatLng location = new LatLng(mParkList.get(i).coordinateX, mParkList.get(i).coordinateY);
            markerList.add(new MarkerOptions()
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.icon_park))
                    .extraInfo(bundle)
                    .position(location));
            
        }
        return markerList;
    }

    /**
     * 获取该 PoiOverlay 的 poi数据
     * 
     * @return
     */
    public PoiResult getPoiResult() {
        return mPoiResult;
    }


    public PoiInfo getPoiInfo(int position){
        if (position >= 0 && position < mParkList.size()){
            Park park = mParkList.get(position);
            PoiInfo info = new PoiInfo();
            info.location = new LatLng(park.coordinateX, park.coordinateY);
            info.address = park.addr;
            info.name = park.name;
            return info;
        }
        return null;
    }

    public Park getParkInfo(int position){
        if (position >= 0 && position < mParkList.size()){
            return mParkList.get(position);
        }
        return null;
    }

    /**
     * 覆写此方法以改变默认点击行为
     * 
     * @param i
     *            被点击的poi在
     *            {@link com.baidu.mapapi.search.poi.PoiResult#getAllPoi()} 中的索引
     * @return
     */
    public boolean onPoiClick(int i) {
//        if (mPoiResult.getAllPoi() != null
//                && mPoiResult.getAllPoi().get(i) != null) {
//            Toast.makeText(BMapManager.getInstance().getContext(),
//                    mPoiResult.getAllPoi().get(i).name, Toast.LENGTH_LONG)
//                    .show();
//        }
        return false;
    }

    @Override
    public final boolean onMarkerClick(Marker marker) {
        if (!mOverlayList.contains(marker)) {
            return false;
        }

        if (mMarker != null){
            mMarker.setIcon(BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_park));
        }

        marker.setIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.icon_park_select));
        mMarker = marker;

        if (marker.getExtraInfo() != null) {
            return onPoiClick(marker.getExtraInfo().getInt("index"));
        }
        return false;
    }

    @Override
    public boolean onPolylineClick(Polyline polyline) {
        // TODO Auto-generated method stub
        return false;
    }
}
