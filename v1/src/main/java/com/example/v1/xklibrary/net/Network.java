package com.example.v1.xklibrary.net;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.v1.xklibrary.Data;
import com.example.v1.xklibrary.MyCallback;
import com.example.v1.xklibrary.Params;
import com.example.v1.xklibrary.activity.BaseActivity;
import com.example.v1.xklibrary.api.Api;
import com.example.v1.xklibrary.api.ApiUtil;
import com.example.v1.xklibrary.cache.CacheItem;
import com.example.v1.xklibrary.cache.CacheManager;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by xuekai on 2017/6/15.
 */

public class Network {
    private static Network network;
    private static OkHttpClient okHttpClient;
    private String baseUrl = "http://10.88.4.136:8888";
    private static Handler handler;
    private int netCount = 0;

    private Network() {

    }

    public static synchronized Network getInstance() {
        if (network == null) {
            network = new Network();
            okHttpClient = new OkHttpClient();
            okHttpClient.setConnectTimeout(2, TimeUnit.SECONDS);
            okHttpClient.setReadTimeout(1, TimeUnit.SECONDS);
            handler = new Handler(Looper.getMainLooper());
        }
        return network;
    }

    public void invoke(Context context, String api) {
        invoke(context, api, null, null,false);
    }

    public void invoke(Context context, String api, List<Params> paramses) {
        invoke(context, api, paramses, null,false);
    }

    public void invoke(Context context, final String apiKey, final MyCallback callBack) {
        invoke(context, apiKey, null, callBack,false);
    }

    public void invoke(final Context context, String api, List<Params> paramses, final MyCallback callback, final boolean focusRefresh) {
        if (context instanceof BaseActivity) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    ((BaseActivity) context).pd.show();
                    netCount++;
                }
            });
        }
        final Api api1 = ApiUtil.getApi(api);

        Callback okCallback = new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                if (context instanceof BaseActivity) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            netCount--;
                            if (netCount == 0) {
                                ((BaseActivity) context).pd.dismiss();
                            }
                        }
                    });
                }
                e.printStackTrace();
                if (callback == null) {
                    return;
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFail("网络问题");
                    }
                });

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String date = response.header("Date");//用于本地时间校准
                //date和本地时间做差，然后保存到全局，以后使用类似于new Date /System.currentTimeMillis()等方法，都把这个差加上就好了
                if (context instanceof BaseActivity) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            netCount--;
                            if (netCount == 0) {
                                ((BaseActivity) context).pd.dismiss();
                            }
                        }
                    });
                }

                if (callback == null) {
                    return;
                }
                Gson gson = new Gson();
                String string = response.body().string();
                System.out.println(string);
                final Data data = gson.fromJson(string, Data.class);
                if (data.getIsError() == 0) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onSuccess(data.getResult().toString());
                        }
                    });

                    if (api1.getExceed() > 0) {//这个数据需要缓存
                        CacheManager.saveCache(api1.getApi(), data.getResult().toString(), api1.getExceed());
                    }


                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFail(data.getErrorMessage() + "");
                        }
                    });
                }
            }
        };

        String url = baseUrl + api1.getUrl();

        //查看api中是否有mockclass
        if (api1.getMockClass() != null) {
            //配置了mockclass，所以不进行网络请求，直接claaback
            String mockClass = api1.getMockClass();
            //反射
//            MovieMock movieMock;
//            movieMock.getData();
//            callback.onSuccess();
//            callback.onFail();
        }



        //执行请求前优先取缓存
        if (api1.getExceed() > 0 && !focusRefresh) {//该api是可缓存类型，那么可能有缓存,第二个条件是强制刷新，避免客户端和服务端的缓存叠加起来的时间很长
            final CacheItem cache = CacheManager.getCache(api1.getApi());
            if (cache != null) {
                //有缓存，通知callback，然后return
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(cache.getData());
                    }
                });
                return;
            }
        }


//准备请求
        Request request;
        if (api1.getMethod().equals("get")) {
            StringBuffer sb = new StringBuffer("?");

            for (Params paramse : paramses) {
                sb.append(paramse.getKey() + "=" + paramse.getValue());
            }

            request = new Request.Builder()
                    .url(url + sb.toString())
                    .build();


        } else {
            FormEncodingBuilder builder = new FormEncodingBuilder();
            for (Params paramse : paramses) {
                builder.add(paramse.getKey(), paramse.getValue());
            }
            request = new Request.Builder()
                    .url(url)
                    .post(builder.build())
                    .build();
        }


        //执行请求
        okHttpClient.newCall(request).enqueue(okCallback);
    }
}
