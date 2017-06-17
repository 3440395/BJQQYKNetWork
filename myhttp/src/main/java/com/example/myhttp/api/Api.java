package com.example.myhttp.api;

/**
 * Created by xuekai on 2017/6/17.
 */

public class Api {
    private String key;
    private String url;
    private int retryTimes;
    private String method;
    private long exceed;
    private String format;
    private String mockClass;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
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

    public void setExceed(long exceed) {
        this.exceed = exceed;
    }

    public String getMockClass() {
        return mockClass;
    }

    public void setMockClass(String mockClass) {
        this.mockClass = mockClass;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    public String toString() {
        return "Api{" +
                "key='" + key + '\'' +
                ", url='" + url + '\'' +
                ", retryTimes=" + retryTimes +
                ", method='" + method + '\'' +
                ", exceed=" + exceed +
                ", format='" + format + '\'' +
                ", mockClass='" + mockClass + '\'' +
                '}';
    }
}
