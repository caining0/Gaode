package com.oslanka.caining;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Poi;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements LocationSource,
        AMapLocationListener, AMap.OnPOIClickListener,
        AMap.OnMarkerClickListener, PoiSearch.OnPoiSearchListener,AMap.InfoWindowAdapter,AMap.OnMapClickListener {
    private MapView mapView;
    private AMap aMap;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private Marker currentMarker;

    /**
     * 搜索
     */
    private AutoCompleteTextView searchText;// 输入搜索关键字
    private String keyWord = "";// 要输入的poi搜索关键字
    private ProgressDialog progDialog = null;// 搜索时进度条
    private EditText editCity;// 要输入的城市名字或者城市区号
    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapView = (MapView) findViewById(R.id.amap);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        aMap = mapView.getMap();
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);// 矢量地图模式
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
// 设置定位的类型为定位模式，参见类AMap。
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        setUpMap();
//        doSearchQuery();


        cnnTest();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void cnnTest() {
//        39.896634,116.398068
//        39.865751,116.431571
        List<PoiItem> poiItems = new ArrayList<>();// poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
        poiItems.add(new PoiItem("a", new LatLonPoint(39.896634, 116.398068), "测试", "测试"));
        poiItems.add(new PoiItem("a", new LatLonPoint(39.875751, 116.471571), "测试", "测试"));
        poiItems.add(new PoiItem("a", new LatLonPoint(39.885751, 116.481571), "测试", "测试"));
        poiItems.add(new PoiItem("a", new LatLonPoint(39.845751, 116.491571), "测试", "测试"));
        poiItems.add(new PoiItem("a", new LatLonPoint(39.825751, 116.411571), "测试", "测试"));
        poiItems.add(new PoiItem("a", new LatLonPoint(39.835751, 116.431571), "测试", "测试"));
        poiItems.add(new PoiItem("a", new LatLonPoint(39.815751, 116.441571), "测试", "测试"));
        poiItems.add(new PoiItem("a", new LatLonPoint(39.865751, 116.451571), "测试", "测试"));
//        List<Integer> srcs = new ArrayList<>();
//        final View infoContent = getLayoutInflater().inflate(
//                R.layout.custom_info_contents, null);
        final int[] srcs = new int[]{R.mipmap.poi_marker_1, R.mipmap.poi_marker_2, R.mipmap.poi_marker_3, R.mipmap.poi_marker_4, R.mipmap.poi_marker_5, R.mipmap.poi_marker_6, R.mipmap.poi_marker_7, R.mipmap.poi_marker_8};
//        final View[] vies = new View[]{infoContent};

        aMap.clear();// 清理之前的图标
        final PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems,this,srcs) ;

        poiOverlay.removeFromMap();
        poiOverlay.addToMap();
        poiOverlay.zoomToSpan();

//        for (int i = 0; i < srcs.length; i++) {
//            poiOverlay.getMark().get(i)
//            .showInfoWindow();
//        }




    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }


    /**
     * amap添加一些事件监听器
     */
    private void setUpMap() {
        initxiaolandian();
        aMap.setOnPOIClickListener(this);
        aMap.setOnMarkerClickListener(this);
//        aMap.setOnInfoWindowClickListener(this);
        aMap.setOnMapClickListener(this);
        aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
//        aMap.setOnMapClickListener(this);// 对amap添加单击地图事件监听器
//        aMap.setOnMapLongClickListener(this);// 对amap添加长按地图事件监听器
//        aMap.setOnCameraChangeListener(this);// 对amap添加移动地图事件监听器
//        aMap.setOnMapTouchListener(this);// 对amap添加触摸地图事件监听器

//        //添加标记
//        LatLng ll_meiyijia = new LatLng(39.816634, 116.408068);
//        LatLng ll_bolian = new LatLng(39.876634, 116.408068);
//        LatLng ll_cunzhihua = new LatLng(39.886634, 116.408068);
//
//        aMap.addMarker(new MarkerOptions()
//                .position(ll_meiyijia)
//                .title("美事多超市")
//                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.poi_marker_1)).perspective(true)
//                .draggable(true)).showInfoWindow();// 设置远小近大效果,2.1.0版本新增
//
//        aMap.addMarker(new MarkerOptions()
//                .position(ll_bolian)
//                .title("博联超市")
//                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.poi_marker_1)).perspective(true)
//                .draggable(true)).showInfoWindow();// 设置远小近大效果,2.1.0版本新增
//
//        aMap.addMarker(new MarkerOptions()
//                .position(ll_cunzhihua)
//                .title("春之花超市")
//                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.poi_marker_1)).perspective(true)
//                .draggable(true)).showInfoWindow();// 设置远小近大效果,2.1.0版本新增
    }

    private void initxiaolandian() {


        MyLocationStyle myLocationStyle = new MyLocationStyle();
//        Bitmap bitmapFromView = BitmapUtils.getInstance().getBitmapFromView(getLayoutInflater().inflate(R.layout.null_view,null));
        ColorDrawable colorDrawable = new ColorDrawable(Color.TRANSPARENT);
        Bitmap bitmap1 = BitmapUtils.getInstance().drawableToBitmap(colorDrawable);
//        Log.i("info",""+bitmapFromView);
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromBitmap(bitmap1));// 设置小蓝点的图标
//        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
//        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
//        myLocationStyle.strokeWidth(0f);// 设置圆形的边框粗细
        aMap.setMyLocationStyle(myLocationStyle);
//        aMap.setMyLocationRotateAngle(180);
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);

            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * tingzhi
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null) {
//            aMapLocation.setAltitude();//haiba
            aMapLocation.setLongitude(116.348934);//jingdu
            aMapLocation.setLatitude(40.099026);//weidu

            mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点

            Log.i("info", "" + aMapLocation.getLatitude() + "---" + aMapLocation.getLongitude());
        }
    }


    //底图poi点击回调
    @Override
    public void onPOIClick(Poi poi) {
        aMap.clear();


        MarkerOptions markOptiopns = new MarkerOptions();

        markOptiopns.position(poi.getCoordinate());
//        TextView textView = new TextView(getApplicationContext());
//        textView.setText("到" + poi.getName() + "去");
//        textView.setGravity(Gravity.CENTER);
//        textView.setTextColor(Color.BLACK);
//        textView.setBackgroundResource(R.drawable.custom_info_bubble);

        View inflate = getLayoutInflater().inflate(R.layout.poi_touch, null);
        TextView text = (TextView) inflate.findViewById(R.id.text);
        text.setText("小田田" + poi.getName());

        markOptiopns.icon(BitmapDescriptorFactory.fromView(inflate));
        aMap.addMarker(markOptiopns);


    }

    @Override
    public boolean onMarkerClick(Marker marker) {

//        // 构造导航参数
//        NaviPara naviPara = new NaviPara();
//        // 设置终点位置
//        naviPara.setTargetPoint(marker.getPosition());
//        // 设置导航策略，这里是避免拥堵
//        naviPara.setNaviStyle(AMapUtils.DRIVING_AVOID_CONGESTION);
//        try {
//            // 调起高德地图导航
//            AMapUtils.openAMapNavi(naviPara, getApplicationContext());
//        } catch (AMapException e) {
//            // 如果没安装会进入异常，调起下载页面
//            AMapUtils.getLatestAMapApp(getApplicationContext());
//        }
//        aMap.clear();
        ToastUtil.show(getApplicationContext(), "测试");
        marker.hideInfoWindow();


        return false;////返回:true 表示点击marker 后marker 不会移动到地图中心；返回false 表示点击marker 后marker 会自动移动到地图中心
    }


    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {
        currentPage = 0;
        query = new PoiSearch.Query("全聚德", "汽车维修|餐饮服务", "110000");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
// keyWord表示搜索字符串，第二个参数表示POI搜索类型，默认为：生活服务、餐饮服务、商务住宅
//共分为以下20种：汽车服务|汽车销售|
//汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|
//住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|
//金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施
//cityCode表示POI搜索区域，（这里可以传空字符串，空字符串代表全国在全国范围内进行搜索）
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页

        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        if (rCode == 1000) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    // 取得搜索到的poiitems有多少页

                    List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息

                    if (poiItems != null && poiItems.size() > 0) {
                        aMap.clear();// 清理之前的图标
                        PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems,MainActivity.this,new int[]{}) ;
                        poiOverlay.removeFromMap();
                        poiOverlay.addToMap();
                        poiOverlay.zoomToSpan();
                    } else if (suggestionCities != null
                            && suggestionCities.size() > 0) {
                        showSuggestCity(suggestionCities);
                    } else {
                        ToastUtil.show(MainActivity.this,
                                R.string.no_result);
                    }
                }
            } else {
                ToastUtil.show(MainActivity.this,
                        R.string.no_result);
            }
        } else {
            ToastUtil.showerror(this, rCode);
        }
    }

    /**
     * poi没有搜索到数据，返回一些推荐城市的信息
     */
    private void showSuggestCity(List<SuggestionCity> cities) {
        String infomation = "推荐城市\n";
        for (int i = 0; i < cities.size(); i++) {
            infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
                    + cities.get(i).getCityCode() + "城市编码:"
                    + cities.get(i).getAdCode() + "\n";
        }
        ToastUtil.show(MainActivity.this, infomation);

    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.oslanka.caining/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.oslanka.caining/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        return new View(getApplicationContext());
    }



    @Override
    public void onMapClick(LatLng latLng) {


    }
}