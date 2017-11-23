package com.gsoft.gcweather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by edison on 2017/11/23 0023.
 */

public class HttpUtil {
    public static void sendOkHttpRequest(String address, okhttp3.Callback callBack){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callBack);
    }
}
