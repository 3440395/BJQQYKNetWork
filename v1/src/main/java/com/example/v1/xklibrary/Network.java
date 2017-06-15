package com.example.v1.xklibrary;

import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

/**
 * Created by xuekai on 2017/6/15.
 */

public class Network {
    private static Network network;
    private static OkHttpClient okHttpClient;
    private String baseUrl = "http://127.0.0.1:8888";

    private Network() {

    }

    public static synchronized Network getInstance() {
        if (network == null) {
            network = new Network();
            okHttpClient = new OkHttpClient();
        }
        return network;
    }

    public void invoke(String api, List<Params> paramses, final MyCallback callback) {

        Callback okCallback = new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
                callback.onFail("网络问题");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Gson gson = new Gson();
                String string = response.body().string();
                System.out.println(string);
                Data data = gson.fromJson(string, Data.class);
                if (data.getIsError() == 0) {
                    callback.onSuccess(data.getResult().toString());
                } else {
                    callback.onFail(data.getErrorMessage() + "");
                }
            }
        };

        Api api1 = ApiUtil.getApi(api);
        String url = baseUrl + api1.getUrl();
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
        okHttpClient.newCall(request).enqueue(okCallback);
    }
}
