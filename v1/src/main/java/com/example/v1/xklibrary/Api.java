package com.example.v1.xklibrary;

/**
 * Created by xuekai on 2017/6/15.
 */

public class Api {
    private String api;
    private String url;
    private String method;
    //过期时间，秒
    private long exceed;

    private String mockClass;


    public Api(String url, String method, long exceed) {
        this.url = url;
        this.method = method;
        this.exceed = exceed;
    }

    public String getMockClass() {
        return mockClass;
    }

    public void setMockClass(String mockClass) {
        this.mockClass = mockClass;
    }

    //key
    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
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

    public long getExceed() {
        return exceed;
    }

    public void setExceed(long readoutTime) {
        this.exceed = exceed;
    }
}
