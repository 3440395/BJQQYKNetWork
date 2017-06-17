package com.example.myhttp.mock;

import com.example.myhttp.entry.ResponseEntry;
import com.google.gson.Gson;

/**
 * Created by xuekai on 2017/6/17.
 */

public abstract class MockServer<T> {
    private Gson gson;

    public MockServer() {
        gson = new Gson();
    }

    public String getSuccessResult() {
        ResponseEntry<T> tResponseEntry = new ResponseEntry<>();
        tResponseEntry.setErrorCode(0);
        tResponseEntry.setErrorMsg("");
        tResponseEntry.setResult(setResult());
        return gson.toJson(tResponseEntry);
    }

    public String getFailedResult() {
        ResponseEntry<T> tResponseEntry = new ResponseEntry<>();
        tResponseEntry.setErrorCode(1);
        tResponseEntry.setErrorMsg("参数错误");
        return gson.toJson(tResponseEntry);
    }

    public abstract T setResult();
}
