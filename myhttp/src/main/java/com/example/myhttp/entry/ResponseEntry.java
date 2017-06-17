package com.example.myhttp.entry;

/**
 * Created by xuekai on 2017/6/17.
 */

public class ResponseEntry<T> {
    private int errorCode;
    private String errorMsg;
    private T result;


    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
