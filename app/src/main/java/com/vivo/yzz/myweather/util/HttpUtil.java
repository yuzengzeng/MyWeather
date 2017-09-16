package com.vivo.yzz.myweather.util;

import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

/**
 * Created by Administrator on 2017/9/16.
 */

public class HttpUtil {
    private static final String TAG="HttpUtil";
    public static void sendOkHttpRequest(String address, Callback callback){

        OkHttpClient client=new OkHttpClient();

        Request request=new Request.Builder().url(address).build();
        Log.d(TAG, "sendOkHttpRequest: oooooooooooooooooooooooooooooooooooooo");

        client.newCall(request).enqueue(callback);

    }
}
