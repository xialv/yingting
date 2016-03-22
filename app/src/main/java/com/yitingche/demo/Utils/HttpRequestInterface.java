package com.yitingche.demo.Utils;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.litesuits.http.HttpConfig;
import com.litesuits.http.LiteHttp;
import com.litesuits.http.annotation.HttpMethod;
import com.litesuits.http.data.GsonImpl;
import com.litesuits.http.data.Json;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.listener.HttpListener;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;
import com.michael.corelib.internet.InternetClient;
import com.michael.corelib.internet.core.NetworkResponse;
import com.michael.corelib.internet.core.RequestBase;
import com.yitingche.demo.app.GlobalApplication;
import com.yitingche.demo.controller.SerResponse;
import com.yitingche.demo.request.AddCardRequest;
import com.yitingche.demo.request.CardRequest;
import com.yitingche.demo.request.ConsumeRequest;
import com.yitingche.demo.request.LoginRequest;
import com.yitingche.demo.request.ParkRequest;
import com.yitingche.demo.request.RechargeRequest;
import com.yitingche.demo.request.RegisterRequest;
//import com.nova.scenic.projectlibs.util.debug.mylog;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by gaolixiao on 16/1/5.
 */
public class HttpRequestInterface {

    private static final String TAG = "HttpRequestInterface";
    private Context mContext;
    private LiteHttp liteHttp;
    //private String server_url = "http://101.200.240.228:8080/mgServer/app/";
    private static String server_url = "http://101.200.240.228/centerServer/app/";

    public HttpRequestInterface(Context context) {

        this.mContext = GlobalApplication.getInstance().getApplicationContext();
        initLiteHttp();

    }





    /**
     * 获得管理终端编码.
     *
     * @return 终端编码(全局唯一)
     */
    public void getNewTerminalCode() {

        String url = server_url + "getNewCarParkCode.rs";

        Log.v("gao", " url: " + url);

        StringRequest stringRequest = new StringRequest(url).setMethod(HttpMethods.Post).
                setHttpListener(new HttpListener<String>() {
                    @Override
                    public void onSuccess(String s, Response<String> response) {

                        Log.v("gao", " onsuccess: " + s + " response : " + response.getResult());
                    }

                    @Override
                    public void onFailure(HttpException e, Response<String> response) {
//                super.onFailure(e, response);
                        Log.v("gao", " onFailure: " + " response : " + response.getResult());
                        Log.v("gao", Log.getStackTraceString(e));

                    }
                });

        liteHttp.executeAsync(stringRequest);

    }

    /**
     * 单例 keep an singleton instance of litehttp
     */
    private void initLiteHttp() {
        if (liteHttp == null) {
            HttpConfig config = new HttpConfig(mContext) // configuration quickly
                    .setDebugged(true)                   // log output when debugged
                    .setDetectNetwork(true)              // detect network before connect
                    .setDoStatistics(true)               // statistics of createTime and traffic
                    .setUserAgent("Mozilla/5.0 (...)")   // set custom User-Agent
                    .setTimeOut(10000, 10000);             // connect and socket timeout: 10s
            liteHttp = LiteHttp.newApacheHttpClient(config);
        } else {
            liteHttp.getConfig()                        // configuration directly
                    .setDebugged(true)                  // log output when debugged
                    .setDetectNetwork(true)             // detect network before connect
                    .setDoStatistics(true)              // statistics of createTime and traffic
                    .setUserAgent("Mozilla/5.0 (...)")  // set custom User-Agent
                    .setTimeOut(10000, 10000);            // connect and socket timeout: 10s
        }
    }



    //////////////////////////////////////////////////////////////////////

    /***
     * 登录
     * @param account
     * @param password
     * @return∂
     * @throws IOException
     */

    public static void login(Context context, String account, String password, InternetClient.NetworkCallback<String> callback){
        final LoginRequest request = new LoginRequest(account, password);
        InternetClient.getInstance(context).postRequest(request, callback);
    }

    /***
     * 注册
     * @param account
     * @param password
     * @return
     * @throws IOException
     */

    public static void register(Context context, String account, String password, InternetClient.NetworkCallback<String> callback){
        final RegisterRequest request = new RegisterRequest(account, password, account);
        InternetClient.getInstance(context).postRequest(request, callback);
    }

    /***
     * 充值记录
     * @return
     * @throws IOException
     */

    public static void getRechargeList(Context context, long id, InternetClient.NetworkCallback<String> callback){
        final RechargeRequest request = new RechargeRequest(id);
        InternetClient.getInstance(context).postRequest(request, callback);
    }

    /***
     * 消费记录
     * @return
     * @throws IOException
     */

    public static void getConsumeList(Context context, long id, InternetClient.NetworkCallback<String> callback){
        final ConsumeRequest request = new ConsumeRequest(id);
        InternetClient.getInstance(context).postRequest(request, callback);
    }

    /***
     * 消费记录
     * @return
     * @throws IOException
     */

    public static void getCardList(Context context, long id, InternetClient.NetworkCallback<String> callback){
        final CardRequest request = new CardRequest(id);
        InternetClient.getInstance(context).postRequest(request, callback);
    }

    public static void addCardNo(Context context, long id, String no, InternetClient.NetworkCallback<String> callback){
        final AddCardRequest request = new AddCardRequest(id, no);
        InternetClient.getInstance(context).postRequest(request, callback);
    }

    public static void getNearbyPark(Context context, String lat, String lng, int max, InternetClient.NetworkCallback<String> callback){
        final ParkRequest request = new ParkRequest(lat, lng, max);
        InternetClient.getInstance(context).postRequest(request, callback);
    }

    /**
     * 根据A和Z端Moc编码及类型获得关系类型.
     *
     * @param aMocCode
     *            A端类编码
     * @param zMocCode
     *            Z端类编码
     * @param type
     *            关系
     * @return MocRs 关系对象
     */

    public static String queryMocRsByType(String aMocCode, String zMocCode, int type) throws IOException {
        JSONObject param = new JSONObject();
        try {
            param.put("aMocCode", aMocCode);
            param.put("zMocCode", zMocCode);
            param.put("type", type);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return executeNetwork(server_url + "queryMocByCode.rs",param);
    }


    /**
     * 根据类编码查询此类的元数据及元数据属性.
     *
     * @param mocCode
     *            类编码
     * @return Moc 类,包含元数据属性
     */

    public static String queryMocByCode(String mocCode) throws IOException {
        JSONObject param = new JSONObject();
        try {
            param.put("mocCode", mocCode);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return executeNetwork(server_url + "queryMocByCode.rs",param);
    }

    /**
     * 查询终端账号所拥有的功能点.
     * <br>注意：只查询inSystem==1的功能点.
     * @param code 终端编码
     * @param account 账号
     * @param password 密码
     * @return 功能点集合
     */
    public static String queryTerminalAccountFunction(String code,String account,String password)
            throws IOException {
        JSONObject param = new JSONObject();
        try {
            param.put("code", code);
            param.put("account", account);
            param.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return executeNetwork(server_url + "queryTerminalAccountFunction.rs",param);
    }


    /**
     * 终端登出.
     * <br>实现类内部逻辑：
     * <br>  判断1.该账号&密码是否正确;
     * <br>  判断2.该账号是否属于要登出的管理终端;
     * <br>  做入库记录(登陆记录表).
     * @param code 终端编码
     * @param account 账号
     * @param password 密码
     * @return true:退出成功;false:退出失败.
     */
    public static String terminalLogout(String code,String account,String password) throws IOException {
        JSONObject param = new JSONObject();
        try {
            param.put("code", code);
            param.put("account", account);
            param.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return executeNetwork(server_url + "terminalLogout.rs",param);
    }


    /**
     * 终端登录.
     * <br>实现类内部逻辑：
     * <br>  判断1.该账号是否存在;
     * <br>  判断2.该账号是否属于要登录的管理终端;
     * <br>  做入库记录(登陆记录表).
     * @param code 终端编码
     * @param account 账号
     * @param password 密码
     * @return true:有登录权限;false:无登录权限.
     */

    public static String terminalLogin(String code, String account, String password) throws IOException {
        JSONObject param = new JSONObject();
        try {
            param.put("code", code);
            param.put("account", account);
            param.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return executeNetwork(server_url + "terminalLogin.rs",param);
    }

    /**
     * 管理终端注册.
     * <br>如果takeAdvice==true表示需要管理员同意后终端才可加入该停车场，
     * <br>此时需要将管理终端的信息保存到临时表(即：管理终端注册临时表)中，管理员人工处理/同意后再新增该终端的moi;
     * <br>如果takeAdvice==false直接新增该终端moi.
     * <br>备注：
     * <br>  1.新增moi时不要忘记管理终端与停车场的关系moi_rs也要新增记录.
     * <br>  2.'终端编码'是终端的唯一性标识,如果系统中已经存在注册的编码则进行更新操作(有可能是修改名称的情况).
     * @param carParkCode 停车场编码(即：终端要加入的停车场的编码,全局唯一)
     * @param name 终端名称
     * @param code 终端编码(全局唯一)
     * @param memo 备注. (可以为空)
     * @param takeAdvice 是否征求停车场管理人员的同意?true:是;false:否.
     * @return true:成功;false:失败.
     */
    public static String  registerTerminalEx(
            String carParkCode,
            String name,
            String code,String memo,
            boolean takeAdvice) throws IOException {

        JSONObject param = new JSONObject();
        try {
            param.put("carParkCode", carParkCode);
            param.put("name", name);
            param.put("code", code);
            param.put("memo", memo);
            param.put("takeAdvice", takeAdvice);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return executeNetwork(server_url + "regTermianl.rs",param);
    }

    private static String executeNetwork(String url,JSONObject param) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/json");
        StringEntity se = new StringEntity(param.toString());
        httpPost.setEntity(se);
        HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost);
        int code = httpResponse.getStatusLine().getStatusCode();
//        if(code != 200){
//            mylog.e(TAG,"executeNetwork error " + code + "  url " + url);
//        }
        HttpEntity entity = httpResponse.getEntity();
        if(entity != null){

            String retSrc = EntityUtils.toString(entity);
            return retSrc;
        }else {
            return null;
        }

    }

    public static String getNewCarParkCode() throws IOException {

        HttpPost httpPost = new HttpPost(server_url + "getNewCarParkCode.rs");
        HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost);
        if(httpResponse.getStatusLine().getStatusCode() == 200){
            String retSrc = EntityUtils.toString(httpResponse.getEntity());

            //{"result":"carPark10"} 返回结果每次调用+1
            return retSrc;

        }else {
            return  null;
        }
    }


    public static String getNewTerminalCodeEx() throws IOException {

        HttpPost httpPost = new HttpPost(server_url + "getNewTerminalCode.rs");
        HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost);
        if(httpResponse.getStatusLine().getStatusCode() == 200){
            String retSrc = EntityUtils.toString(httpResponse.getEntity());

            //{"result":"carPark10"} 返回结果每次调用+1
            return retSrc;

        }else {
            return  null;
        }
    }


    /**
     *
     * 根据车牌号码从'在场车辆'表中获得最后一个记录的'业务ID'.
     * @param carParkId 所属停车场ID
     * @param carNo 车牌号码
     * @return String 业务ID
     */
    public static String getBizId(long carParkId, String carNo) throws IOException {
        HttpPost httpPost = new HttpPost(server_url + "regTermianl.rs");
        httpPost.addHeader("Content-Type", "application/json");
        JSONObject param = new JSONObject();
        try {
            param.put("carParkId", carParkId);
            param.put("carNo", carNo);
            param.put("name", "6前门岗亭");
            param.put("memo", "45新增前门岗亭");
            param.put("takeAdvice", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringEntity se = new StringEntity(param.toString());
        httpPost.setEntity(se);
        // 发送请求
        HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost);
        String retSrc = EntityUtils.toString(httpResponse.getEntity());
//        mylog.i(TAG, "value is " + retSrc);
        return  retSrc;
    }





}
