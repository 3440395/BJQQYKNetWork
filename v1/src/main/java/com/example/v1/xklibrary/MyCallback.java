package com.example.v1.xklibrary;

/**
 * 为了和okhttp解耦和 所以不用它的callback
 * Created by xuekai on 2017/6/15.
 */

public interface MyCallback {
    public void onSuccess(String content);

    public void onFail(String errorMessage);
}
