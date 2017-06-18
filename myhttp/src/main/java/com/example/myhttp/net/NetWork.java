package com.example.myhttp.net;

import android.os.Handler;
import android.os.Looper;

import com.example.myhttp.api.Api;
import com.example.myhttp.api.ApiManager;
import com.example.myhttp.cache.CacheManager;
import com.example.myhttp.entry.ResponseEntry;
import com.example.myhttp.util.LogUtil;
import com.example.myhttp.xml.BaseXmlParser;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * fragment的生命周期来控制 没办到：用rx来管理
 * 加载进度条也有rx来管理
 * Created by xuekai on 2017/6/17.
 */

public class NetWork {
    private OkHttpClient okHttpClient;
    private Handler handler;
    private static NetWork netWork;
    private final ApiManager apiManager;
    private Gson gson;
    private int netCount = 0;

    private NetWork() {
        Interceptor logging = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                String url = request.url().toString();
                LogUtil.d("HttpLog:" + request.header("logkey") + "-->" + url);
                return chain.proceed(request);
            }
        };
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(2, TimeUnit.SECONDS)
                .build();

        handler = new Handler(Looper.getMainLooper());
        handler = new Handler(Looper.getMainLooper());
        apiManager = new ApiManager();
        gson = new Gson();
    }

    public static synchronized NetWork getInstance() {
        if (netWork == null) {
            netWork = new NetWork();
        }
        return netWork;
    }


    /**
     * TODO 1.baseActivity传入空，设置tag的时候注意
     *
     * @param key
     * @param params
     * @param retryTimes
     * @param forceUpdate
     * @param callback
     * @param parser
     * @param <T>
     */
    public <T> void invoke(String key, List<Params> params
            , int retryTimes, boolean forceUpdate,
                           MCallback<T> callback, BaseXmlParser<T> parser) {
        Api api = apiManager.findApi(key);
        //强制更新
        if (forceUpdate) {
            api.setExceed(0);
        }
        int retryTimesInApi = api.getRetryTimes();
        final int[] finalRetryTimes = new int[1];
        finalRetryTimes[0] = retryTimes;
        Callback internalCallback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //失败重试
                if (finalRetryTimes[0] > 0) {
                    invoke(key, params, --finalRetryTimes[0], forceUpdate, callback, parser);
                    return;
                }
                if (callback != null) {
                    handler.post(() -> callback.onFailed("网络错误"));
                }

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String data = response.body().string();
                    if (api.getFormat().equalsIgnoreCase("json")) {
                        ResponseEntry responseEntry = gson.fromJson(data, ResponseEntry.class);
                        if (responseEntry.getErrorCode() != 0) {
                            if (callback != null) {
                                handler.post(() -> callback.onFailed(responseEntry.getErrorMsg()));
                            }

                        } else {
                            String resultJson = gson.toJson(responseEntry.getResult());
                            T t = gson.fromJson(resultJson, callback.type);

                            // 把成功获取到的数据记录到缓存
                            if (api.getExceed() > 0) {
                                CacheManager.getInstance().putFileCache(api.getKey(), resultJson, api.getExceed());
                            }
                            LogUtil.d("NetWork:onResponse-->从网络拿的数据");
                            if (callback != null) {

                                handler.post(() -> callback.onSuccess(t));
                            }

                        }
                    } else {
                        // TODO: by xk 2017/6/17 23:46 xml 的话传入一个方法，用它来解析
                        if (parser != null) {
                            T t = parser.parser(data);
                            //...
                        } else {
                            throw new IllegalStateException("未传入xml解析器");
                        }
                    }
                } else {
                    if (callback != null) {
                        handler.post(() -> callback.onFailed("服务器返回错误"));
                    }
                }

            }
        };
        //缓存逻辑，RxJava
        Observable<String> disk = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                if (api.getExceed() > 0) {
                    final String content = CacheManager.getInstance().getFileCache(api.getKey());
                    if (content != null) {
                        if (callback != null) {
                            T t = gson.fromJson(content, callback.type);

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    LogUtil.d("NetWork:run-->从缓存拿的数据");
                                    callback.onSuccess(t);
                                }
                            });
                        }
                        e.onNext("");
                    }
                }
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io());


        Observable<String> network = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                //在这里进行重试机制
                if (retryTimes == -100 && retryTimesInApi > 0) {//还未进行重试
                    finalRetryTimes[0] = retryTimesInApi - 1;
                }
                String url = api.getUrl();
                Request req = buildRequest(url, params, api.getMethod(), key);

                Call call = okHttpClient.newCall(req);
                LogUtil.d("NetWork:invoke-->发起网络");
                call.enqueue(internalCallback);
                Thread.sleep(5000);
                e.onComplete();
            }

        }).subscribeOn(Schedulers.io());
        Observable.concat(disk, network)
                .first("")
                .subscribe();
    }


    public Request buildRequest(String url, List<Params> params, String method, String key) {
        Request.Builder requestBuilder = new Request.Builder()
//                .tag(httpBaseActivity)
                .addHeader("logkey", key);
        if (method.equalsIgnoreCase("get")) {
            final StringBuffer paramBuffer = new StringBuffer();
            if ((params != null) && (params.size() > 0)) {
                for (final Params p : params) {
                    if (paramBuffer.length() == 0) {
                        paramBuffer.append(p.getKey() + "="
                                + p.getValue());
                    } else {
                        paramBuffer.append("&" + p.getKey() + "=" + p.getValue());
                    }
                }
                url += "?" + paramBuffer.toString();
            }
        } else if (method.equalsIgnoreCase("post")) {
            FormBody.Builder builder = new FormBody.Builder();
            for (Params paramse : params) {
                builder.add(paramse.getKey(), paramse.getValue());
            }
            requestBuilder.post(builder.build());
        } else {
            throw new IllegalStateException("错误的请求方式");
        }
        return requestBuilder
                .url(url)
                .build();
    }


//    public  Observable invokeByRx(String key, List<Params> params
//            , int retryTimes, boolean forceUpdate, BaseXmlParser<T> parser) {
//        return Observable.create(new ObservableOnSubscribe<T>() {
//            @Override
//            public void subscribe(@NonNull ObservableEmitter<T> e) throws Exception {
//                invoke(key, params, retryTimes, forceUpdate, new MCallback<T>() {
//                    @Override
//                    public void onSuccess(T t) {
//                        e.onNext(t);
//                        e.onComplete();
//                    }
//
//                    @Override
//                    public void onFailed(String errorMsg) {
//                        e.onError(new Throwable(errorMsg));
//                    }
//                }, parser);
//            }
//        });
//
//    }
//    /**
//     * 这里通过okhttpclient的cancel方法，通过tag来取消，tag在构建request的时候指定
//     * 猜想可以用activity的类名作为tag，这样应该会更准确
//     * TODO 是否可行有待验证
//     */
//    public void cancleAllRequest(HttpBaseActivity httpBaseActivity) {
//        for (Call call : okHttpClient.dispatcher().queuedCalls()) {
//            if (call.request().tag() == httpBaseActivity)
//                call.cancel();
//        }
//        for (Call call : okHttpClient.dispatcher().runningCalls()) {
//            if (call.request().tag() == httpBaseActivity)
//                call.cancel();
//        }
//    }
}
