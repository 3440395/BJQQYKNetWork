package com.example.v1.xklibrary.net;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.v1.xklibrary.Data;
import com.example.v1.xklibrary.LogUtil;
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

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xuekai on 2017/6/15.
 */

public class Network {
    private static Network network;
    private static OkHttpClient okHttpClient;
    private static Handler handler;
    private int netCount = 0;

    private String tag = "allRequest";

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
        invoke(context, api, null, null, false, -100);
    }

    public void invoke(Context context, String api, List<Params> paramses) {
        invoke(context, api, paramses, null, false, -100);
    }

    public void invoke(Context context, final String apiKey, final MyCallback callBack) {
        invoke(context, apiKey, null, callBack, false, -100);
    }

    public void invoke(final Context context, final String api, final List<Params> paramses, final MyCallback callback
            , final boolean focusRefresh, int retryTimes) {
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

        int retryTimesInApi = api1.getRetryTimes();
        final int[] finalRetryTimes = new int[1];

        //在这里进行重试机制
        if (retryTimes == -100 && retryTimesInApi > 0) {//还未进行重试
            finalRetryTimes[0] = retryTimesInApi;
        }
        final Callback okCallback = new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                //失败重试
                if (finalRetryTimes[0] > 0) {
                    invoke(context, api, paramses, callback, focusRefresh, --finalRetryTimes[0]);
                    return;
                }
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

        String baseUrl = "http://10.88.4.136:8888";
        final String url = baseUrl + api1.getUrl();

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


        Observable disk = Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(@NonNull ObservableEmitter e) throws Exception {
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
                        e.onNext("");
                        LogUtil.d("Network:subscribe-->通过本地拿数据");
                    }
                }
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io());

        Observable net = Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(@NonNull ObservableEmitter e) throws Exception {
                //准备请求
                Request request;
                if (api1.getMethod().equals("get")) {
                    StringBuffer sb = new StringBuffer("?");
                    for (Params paramse : paramses) {
                        sb.append(paramse.getKey() + "=" + paramse.getValue());
                    }
                    request = new Request.Builder()
                            .tag(tag)
                            .url(url + sb.toString())
                            .build();
                } else {
                    FormEncodingBuilder builder = new FormEncodingBuilder();
                    for (Params paramse : paramses) {
                        builder.add(paramse.getKey(), paramse.getValue());
                    }
                    request = new Request.Builder()
                            .url(url)
                            .tag(tag)
                            .post(builder.build())
                            .build();
                }
                //执行请求
                okHttpClient.newCall(request).enqueue(okCallback);
            }
        }).subscribeOn(Schedulers.io());

        Observable.concat(disk, net)
                .first(disk)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();


      /*  //执行请求前优先取缓存
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
                    .tag(tag)
                    .url(url + sb.toString())
                    .build();
        } else {
            FormEncodingBuilder builder = new FormEncodingBuilder();
            for (Params paramse : paramses) {
                builder.add(paramse.getKey(), paramse.getValue());
            }
            request = new Request.Builder()
                    .url(url)
                    .tag(tag)
                    .post(builder.build())
                    .build();
        }
        //执行请求
        okHttpClient.newCall(request).enqueue(okCallback);*/
    }

    /**
     * 这里通过okhttpclient的cancel方法，通过tag来取消，tag在构建request的时候指定
     * 猜想可以用activity的类名作为tag，这样应该会更准确
     */
    public void cancleAllRequest() {
        okHttpClient.cancel(tag);
    }
}
