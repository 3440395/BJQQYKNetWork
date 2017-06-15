package com.example.v1.xklibrary;

/**
 * Created by xuekai on 2017/6/15.
 */

public class Api {
    private String url;
    private String method;
    private long readoutTime;

    public Api(String url, String method, long readoutTime) {
        this.url = url;
        this.method = method;
        this.readoutTime = readoutTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public long getReadoutTime() {
        return readoutTime;
    }

    public void setReadoutTime(long readoutTime) {
        this.readoutTime = readoutTime;
    }
}
