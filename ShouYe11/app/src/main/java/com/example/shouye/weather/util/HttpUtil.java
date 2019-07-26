package com.example.shouye.weather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by ASUS on 2017/10/9.
 */

public class HttpUtil {
    //工程其他部分若想发起http请求，只需调用sendokhttprequest方法即可
    //传入请求地址，注册一个回调来处理服务器响应
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
