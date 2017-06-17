package com.example.myhttp.net;

import android.os.Handler;
import android.os.Looper;

import com.example.myhttp.api.Api;
import com.example.myhttp.api.ApiManager;
import com.example.myhttp.util.LogUtil;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

/**
 * Created by xuekai on 2017/6/17.
 */

public class NetWork {
    private OkHttpClient okHttpClient;
    private Handler handler;
    private static NetWork netWork;
    private final ApiManager apiManager;

    private NetWork() {
        okHttpClient = new OkHttpClient();
        handler = new Handler(Looper.getMainLooper());
        apiManager = new ApiManager();
    }

    public static synchronized NetWork getInstance() {
        if (netWork == null) {
            netWork = new NetWork();
        }
        return netWork;
    }


    /**
     *
     * @param key
     * @param clazz 传入返回类型，如果是集合 传入数组
     * @param params
     * @param callback
     * @param <T>
     */
    public <T> void invoke(String key,Class<T> clazz, List<Params> params, Callback<T> callback) {
        Api api = apiManager.findApi(key);
        String url = api.getUrl();
        Request build = new Request.Builder()
                .url(url)
                .build();
         okHttpClient.newCall(build).enqueue(new com.squareup.okhttp.Callback() {
             @Override
             public void onFailure(Request request, IOException e) {

             }

             @Override
             public void onResponse(Response response) throws IOException {
                 String string = response.body().string();
                 LogUtil.d("NetWork:onResponse-->"+string);
             }
         });

    }

}
