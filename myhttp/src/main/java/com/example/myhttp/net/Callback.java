package com.example.myhttp.net;

/**
 * Created by xuekai on 2017/6/17.
 */

public interface Callback<T> {
    void onSuccess(T t);

    void onFile(String errorMsg);
}
