package com.yitingche.demo.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.google.gson.Gson;
import com.michael.corelib.internet.InternetClient;
import com.michael.corelib.internet.core.NetworkResponse;
import com.michael.corelib.internet.core.RequestBase;
import com.yitingche.demo.R;
import com.yitingche.demo.Utils.HttpRequestInterface;
import com.yitingche.demo.adapter.DrawerAdapter;
import com.yitingche.demo.controller.LoginManager;
import com.yitingche.demo.controller.MenuInfo;
import com.yitingche.demo.controller.Park;
import com.yitingche.demo.controller.ParkResponse;
import com.yitingche.demo.event.LoginEvent;
import com.yitingche.demo.event.ParkEvent;
import com.yitingche.demo.map.MapGuideActivity;
import com.yitingche.demo.map.PoiOverlayNew;
import com.yitingche.demo.view.ClearEditText;
import com.yitingche.demo.view.MenuLoginView;
import com.yitingche.demo.view.ParkInfoView;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.greenrobot.event.EventBus;


public class MainActivity extends Activity implements OnGetPoiSearchResultListener {
    private DrawerLayout mDrawerLayout;
    private MenuLoginView mMenuLoginView;
    private ListView mDrawerList;
    private View mLogoutBtn;
    private DrawerAdapter mDrawerAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String[] mPlanetTitles;
    private View mMenuBtn;
    private ClearEditText mEditText;
    private boolean mMenuOpen = false;
    private ParkInfoView mParkInfo;
    private Park mCurrentPark;

    /**
     * MapView 是地图主控件
     */
    private MapView mMapView = null;

    private BaiduMap mBaiduMap;

    private PoiSearch mPoiSearch = null;

    private BDLocation mLocation = null;

    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
    BitmapDescriptor mCurrentMarker;
    private static final int accuracyCircleFillColor = 0x00FFFFFF;
    private static final int accuracyCircleStrokeColor = 0x00FFFFFF;
    boolean isFirstLoc = true; // 是否首次定位

    private String mSDCardPath = null;
    private static final String APP_FOLDER_NAME = "yitingcheDemo";
    public static final String ROUTE_PLAN_NODE = "routePlanNode";

    public static List<Activity> activityList = new LinkedList<Activity>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 介绍如何使用个性化地图，需在MapView 构造前设置个性化地图文件路径
        // 注: 设置个性化地图，将会影响所有地图实例。
        // MapView.setCustomMapStylePath("个性化地图config绝对路径");
        super.onCreate(savedInstanceState);
        activityList.add(this);
        //注意该方法要再setContentView方法之前实现
        setContentView(R.layout.activity_main);

        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }

        initMenu();

        initMap();

    }

    private void initMenu(){

        mParkInfo = (ParkInfoView)findViewById(R.id.park_info_view);
        mParkInfo.setNaviClickListener(new ParkInfoView.OnNaviClickListener() {
            @Override
            public void onNaviClick(Park park) {
                mLocation.getLocType();
                BNRoutePlanNode snode = new BNRoutePlanNode(mLocation.getLongitude(),
                        mLocation.getLatitude(), mLocation.getBuildingName(), mLocation.getAddrStr(),
                        BNRoutePlanNode.CoordinateType.GCJ02);
                BNRoutePlanNode enode = new BNRoutePlanNode(park.coordinateY, park.coordinateX,
                        park.name, park.addr,
                        BNRoutePlanNode.CoordinateType.GCJ02);
                if (BaiduNaviManager.isNaviInited()) {
                    routeplanToNavi(BNRoutePlanNode.CoordinateType.GCJ02, snode, enode);
                } else {
                    if (initDirs()) {
                        initNavi(snode, enode);
                    }
                }
            }

            @Override
            public void onDetailClick(Park park) {
                Intent intent = new Intent(MainActivity.this, ParkDetailActivity.class);
                intent.putExtra(ParkDetailActivity.PARK_ID, park.id);
                intent.putExtra(ParkDetailActivity.PARK_NAME, park.name);
                intent.putExtra(ParkDetailActivity.PARK_ADDRESS, park.addr);
                intent.putExtra(ParkDetailActivity.PARK_SEAT, park.seatNum);
                intent.putExtra(ParkDetailActivity.PARK_FREE_SEAT, park.freeSeatNum);
                intent.putExtra(ParkDetailActivity.PARK_LNG, park.coordinateY);
                intent.putExtra(ParkDetailActivity.PARK_LAT, park.coordinateX);
                startActivityForResult(intent, 2222);
            }
        });

//        mTitle = mDrawerTitle = getTitle();
        mMenuBtn = findViewById(R.id.title);
        mMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mMenuOpen) {
                    mDrawerLayout.openDrawer(mDrawerList);
                } else {
                    mDrawerLayout.closeDrawer(mDrawerList);
                }
            }
        });

        mEditText = (ClearEditText)findViewById(R.id.search_edit_text);
        mEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivityForResult(intent, 1000);
            }
        });

        mPlanetTitles = getResources().getStringArray(R.array.planets_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView)findViewById(R.id.drawer_listview);
        View footerView = LayoutInflater.from(this).inflate(R.layout.logout, null, false);
        mLogoutBtn = footerView.findViewById(R.id.logout_btn);
        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logout();
                mDrawerLayout.closeDrawer(mDrawerList);
            }
        });

        mDrawerAdapter = new DrawerAdapter(this);
        List<MenuInfo> list = new ArrayList<MenuInfo>();
        for (int i = 0; i < mPlanetTitles.length; i++){
            MenuInfo info = new MenuInfo(mPlanetTitles[i], "", i);
            list.add(info);
        }
        mDrawerAdapter.setDataList(list);
        mMenuLoginView = new MenuLoginView(this);
        mMenuLoginView.setLoginClickListener(new MenuLoginView.OnLoginClickListener() {
            @Override
            public void onLoginClick() {
                mDrawerLayout.closeDrawer(mDrawerList);
            }
        });
        mDrawerList.addHeaderView(mMenuLoginView);
        mDrawerList.addFooterView(footerView);
        mDrawerList.setAdapter(mDrawerAdapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout, R.drawable.icon_menu, R.string.open_menu, R.string.close_menu);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mMenuLoginView.showLogin(LoginManager.getInstance().isLogined(this), LoginManager.getInstance().getAccount());
        showLogoutBtn(LoginManager.getInstance().isLogined(this));
    }

    private void initMap(){
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(15));
        //开启交通图
        mBaiduMap.setTrafficEnabled(true);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);

        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();

        // 修改为自定义marker
        mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_location);
        mBaiduMap
                .setMyLocationConfigeration(new MyLocationConfiguration(
                        mCurrentMode, true, mCurrentMarker,
                        accuracyCircleFillColor, accuracyCircleStrokeColor));

        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });
    }

    public void onEventMainThread(LoginEvent event){
        if (event != null){
            mMenuLoginView.showLogin(event.loginSuccess, event.account);
            showLogoutBtn(event.loginSuccess);
        }
    }

    public void onEventMainThread(ParkEvent event){
        if (event != null){
            if (event.result != null){
                PoiOverlayNew overlay = new MyPoiOverlay(mBaiduMap);
                mBaiduMap.setOnMarkerClickListener(overlay);
                overlay.setParkList(event.result);
                overlay.addToMap();
//                overlay.zoomToSpan();

            }
        }
    }

    private void showLogoutBtn(boolean isShow){
        if (isShow){
            mLogoutBtn.setVisibility(View.VISIBLE);
        } else {
            mLogoutBtn.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mParkInfo.setVisibility(View.GONE);
        Log.d("haha", "requestCode=" + requestCode + "  resultCode=" + resultCode + "data:" + data);

        if(requestCode == 1000){
            if(resultCode == 0){
                if (data != null){
                    mCurrentPark = null;
                    Double lat = data.getDoubleExtra("lat", 0);
                    Double lng = data.getDoubleExtra("lng", 0);
                    if(lat > 0 && lng > 0) {
                        mBaiduMap.clear();
                        MyLocationData locData = new MyLocationData.Builder()
//                            .accuracy(location.getRadius())
                                // 此处设置开发者获取到的方向信息，顺时针0-360
                                .direction(100)
                                .latitude(lat)
                                .longitude(lng).build();
                        mBaiduMap.setMyLocationData(locData);
                        mBaiduMap.clear();
//                        //准备 marker 的图片
//                        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_location);
//                        //准备 marker option 添加 marker 使用
//                        MarkerOptions markerOptions = new MarkerOptions().icon(bitmap).position(new LatLng(lat, lng));
//                        //获取添加的 marker 这样便于后续的操作
//                        Marker marker = (Marker) mBaiduMap.addOverlay(markerOptions);

                        LatLng ll = new LatLng(lat, lng);
                        MapStatus.Builder builder = new MapStatus.Builder();
                        builder.target(ll).zoom(18.0f);
                        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

//                        nearbySearch(0, lat, lng);
                        getNearbyPark(lat, lng, 10);
                    }
                }
            }
        } else if (requestCode == 2222 && data != null){
            Double lat = data.getDoubleExtra(ParkDetailActivity.PARK_LAT, 0);
            Double lng = data.getDoubleExtra(ParkDetailActivity.PARK_LNG, 0);
            BNRoutePlanNode snode = new BNRoutePlanNode(mLocation.getLongitude(),
                    mLocation.getLatitude(), mLocation.getBuildingName(), mLocation.getAddrStr(),
                    BNRoutePlanNode.CoordinateType.GCJ02);
            BNRoutePlanNode enode = new BNRoutePlanNode(lng, lat,
                    "", "",
                    BNRoutePlanNode.CoordinateType.GCJ02);
            if (BaiduNaviManager.isNaviInited()) {
                routeplanToNavi(BNRoutePlanNode.CoordinateType.GCJ02, snode, enode);
            } else {
                if (initDirs()) {
                    initNavi(snode, enode);
                }
            }
        }

        if (mCurrentPark != null && mParkInfo != null){
            mParkInfo.setData(mCurrentPark);
            mParkInfo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onGetPoiResult(PoiResult result) {
        if (result == null
                || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(MainActivity.this, "未找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
//            mBaiduMap.clear();
            PoiOverlayNew overlay = new MyPoiOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(result);
            overlay.addToMap();
            overlay.zoomToSpan();
            return;
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

            // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
            String strInfo = "在";
            for (CityInfo cityInfo : result.getSuggestCityList()) {
                strInfo += cityInfo.city;
                strInfo += ",";
            }
            strInfo += "找到结果";
            Toast.makeText(MainActivity.this, strInfo, Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
        if (poiDetailResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(MainActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(MainActivity.this, poiDetailResult.getName() + ": " + poiDetailResult.getAddress(), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }

            mLocation = location;
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            if (isFirstLoc) {
                mBaiduMap.setMyLocationData(locData);
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
//                nearbySearch(0, location.getLatitude(), location.getLongitude());
                getNearbyPark(location.getLatitude(), location.getLongitude(), 10);

            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    private boolean equalLocation(BDLocation locationA, BDLocation locationB){
        boolean isEqual = false;
        if (locationA != null && locationB != null){
            isEqual = locationA.getLongitude() == locationB.getLongitude() &&
            locationA.getLatitude() == locationB.getLatitude();
        }
        return isEqual;
    }

    private void nearbySearch(int page, double latitude, double longitude){
        PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption();
        nearbySearchOption.keyword("停车场");
        nearbySearchOption.location(new LatLng(latitude, longitude));
        nearbySearchOption.radius(1000);// 检索半径，单位是米
        nearbySearchOption.pageCapacity(20);
        nearbySearchOption.pageNum(page);
        mPoiSearch.searchNearby(nearbySearchOption);
    }

    Handler mhandler = new Handler();
    String authinfo = null;
    private void initNavi(final BNRoutePlanNode snode, final BNRoutePlanNode enode) {
        // BaiduNaviManager.getInstance().setNativeLibraryPath(mSDCardPath +
        // "/BaiduNaviSDK_SO");


        BNOuterTTSPlayerCallback ttsCallback = null;

        BaiduNaviManager.getInstance().init(this, mSDCardPath, APP_FOLDER_NAME, new BaiduNaviManager.NaviInitListener() {
            @Override
            public void onAuthResult(int status, String msg) {
                if (0 == status) {
                    authinfo = "key校验成功!";
                } else {
                    authinfo = "key校验失败, " + msg;
                }
                Log.i("daohang", "authinfo=" + authinfo);
//                mhandler.postDelayed(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        Toast.makeText(MainActivity.this, authinfo, Toast.LENGTH_LONG).show();
//                    }
//                }, 300);
            }

            public void initSuccess() {
                Log.i("daohang", "initSuccess=");
//                Toast.makeText(MainActivity.this, "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
                if (snode != null && enode != null)
                routeplanToNavi(BNRoutePlanNode.CoordinateType.GCJ02, snode, enode);
            }

            public void initStart() {
                Log.i("daohang", "initstart=");
//                Toast.makeText(MainActivity.this, "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
            }

            public void initFailed() {
                Log.i("daohang", "initFailed=");
                Toast.makeText(MainActivity.this, "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
            }

        },  null/* null mTTSCallback */);
    }

    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        // activity 暂停时同时暂停地图控件
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // activity 恢复时同时恢复地图控件
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        mCurrentMarker.recycle();
        mCurrentMarker = null;

        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }


    private class MyPoiOverlay extends PoiOverlayNew {

        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int index) {
            super.onPoiClick(index);
            Park park = getParkInfo(index);
            if (park != null) {
                mCurrentPark = park;
                mParkInfo.setData(park);
                mParkInfo.setVisibility(View.VISIBLE);
            } else {
                mCurrentPark = null;
                mParkInfo.setVisibility(View.GONE);
            }
            if (BaiduNaviManager.isNaviInited()) {

                if (initDirs()) {
                    initNavi(null, null);
                }
            }
            return true;
        }
    }

    private void routeplanToNavi(BNRoutePlanNode.CoordinateType coType, BNRoutePlanNode sNode, BNRoutePlanNode eNode) {
//        switch (coType) {
//            case GCJ02: {
//                sNode = new BNRoutePlanNode(116.30142, 40.05087, "百度大厦", null, coType);
//                eNode = new BNRoutePlanNode(116.39750, 39.90882, "北京天安门", null, coType);
//                break;
//            }
//            case WGS84: {
//                sNode = new BNRoutePlanNode(116.300821, 40.050969, "百度大厦", null, coType);
//                eNode = new BNRoutePlanNode(116.397491, 39.908749, "北京天安门", null, coType);
//                break;
//            }
//            case BD09_MC: {
//                sNode = new BNRoutePlanNode(12947471, 4846474, "百度大厦", null, coType);
//                eNode = new BNRoutePlanNode(12958160, 4825947, "北京天安门", null, coType);
//                break;
//            }
//            case BD09LL: {
//                sNode = new BNRoutePlanNode(116.30784537597782, 40.057009624099436, "百度大厦", null, coType);
//                eNode = new BNRoutePlanNode(116.40386525193937, 39.915160800132085, "北京天安门", null, coType);
//                break;
//            }
//            default:
//                ;
//        }
        if (sNode != null && eNode != null) {
            List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
            list.add(sNode);
            list.add(eNode);
            BaiduNaviManager.getInstance().launchNavigator(this, list, 1, true, new DemoRoutePlanListener(sNode));
        }
    }

    public class DemoRoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;

        public DemoRoutePlanListener(BNRoutePlanNode node) {
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
			/*
			 * 设置途径点以及resetEndNode会回调该接口
			 */

            for (Activity ac : activityList) {

                if (ac.getClass().getName().endsWith("BNDemoGuideActivity")) {

                    return;
                }
            }
            Intent intent = new Intent(MainActivity.this, MapGuideActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ROUTE_PLAN_NODE, (BNRoutePlanNode) mBNRoutePlanNode);
            intent.putExtras(bundle);
            startActivity(intent);

        }

        @Override
        public void onRoutePlanFailed() {
            // TODO Auto-generated method stub
            Toast.makeText(MainActivity.this, "算路失败", Toast.LENGTH_SHORT).show();
        }
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if (position > 0)
            selectItem(position);
        }
    }
    private void selectItem(int position) {
        // update the main content by replacing fragments
        Fragment fragment = new PlanetFragment();
        Bundle args = new Bundle();
        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
        fragment.setArguments(args);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_layout, fragment).commit();
        // update selected item and title, then close the drawer
//        mDrawerList.setItemChecked(position, true);
//        setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
        onMenuClick(position);
    }

    private void onMenuClick(int position){
        if (position > 0 && position <= mDrawerAdapter.getCount()){
            int type = mDrawerAdapter.getItem(position - 1).operatorType;
            Intent intent = null;
            boolean needLogin = false;
            if (type == MenuInfo.TYPE_CARD || type == MenuInfo.TYPE_DEAL || type == MenuInfo.TYPE_RECHARGE){
                needLogin = true;
            }
            if (!needLogin || LoginManager.getInstance().isLogined(this)) {
                switch (type) {
                    case MenuInfo.TYPE_CARD:
                        intent = new Intent(this, CardActivity.class);
                        break;
                    case MenuInfo.TYPE_DEAL:
                        intent = new Intent(this, DealActivity.class);
                        break;
                    case MenuInfo.TYPE_RECHARGE:
                        intent = new Intent(this, RechargeActivity.class);
                        break;
                }
                if (intent != null)
                    startActivity(intent);
            } else {
                LoginManager.getInstance().gotoLoginActivity(this);
            }
        }
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public static class PlanetFragment extends Fragment {
        public static final String ARG_PLANET_NUMBER = "planet_number";
        public PlanetFragment() {
            // Empty constructor required for fragment subclasses
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_planet, container, false);
//            int i = getArguments().getInt(ARG_PLANET_NUMBER);
//            String planet = getResources().getStringArray(R.array.planets_array)[i];
//            int imageId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()),
//                    "drawable", getActivity().getPackageName());
//            ((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);
//            getActivity().setTitle(planet);
            return rootView;
        }
    }

    private void getNearbyPark(final Double lat, final Double lng, int max){
        HttpRequestInterface.getNearbyPark(this, String.valueOf(lat), String.valueOf(lng), max, new InternetClient.NetworkCallback<String>() {
            @Override
            public void onSuccess(RequestBase<String> stringRequestBase, String s) {
                if (!TextUtils.isEmpty(s)){
                    Gson gson = new Gson();
                    String lat1 = String.valueOf(lat + 0.001);
                    String lng1 = String.valueOf(lng);
                    String lat2 = String.valueOf(lat - 0.001);
                    String lng2 = String.valueOf(lng + 0.001);
                    //test data
                    s = "{\"park\":[{\"addr\":\"北京市海淀区\",\"code\":\"park1\",\"coordinateX\":\"" + lat1 + "\",\"coordinateY\":\""+ lng1 + "\",\"createTime\":\"2016-02-27T17:38:01+08:00\",\"freeSeatNum\":\"30\",\"id\":\"1\",\"name\":\"摩尔停车场\",\"seatNum\":\"500\",\"updateTime\":\"2016-02-27T17:38:06+08:00\"},{\"addr\":\"北京市朝阳区\",\"code\":\"park2\",\"coordinateX\":\""
                    + lat2 + "\",\"coordinateY\":\""+ lng2 + "\",\"createTime\":\"2016-02-18T19:38:06+08:00\",\"freeSeatNum\":\"10\",\"id\":\"2\",\"name\":\"福成停车场\",\"seatNum\":\"200\",\"updateTime\":\"2016-02-20T19:38:09+08:00\"}]}";
                    ParkResponse response = gson.fromJson(s, ParkResponse.class);
                    ParkEvent event = new ParkEvent();
                    event.result = response.park;
                    event.state = 1;
                    EventBus.getDefault().postSticky(event);
                }
            }

            @Override
            public void onFailed(RequestBase<String> stringRequestBase, NetworkResponse networkResponse) {
                if (networkResponse !=null){

                }

                ParkEvent event = new ParkEvent();
                event.result = null;
                event.state = 0;
                EventBus.getDefault().postSticky(event);
            }
        });
    }
}
