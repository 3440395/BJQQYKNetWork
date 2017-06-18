package com.example.myhttp.net;

/**
 * Created by xuekai on 2017/6/17.
 */

public class Params {
    private String key;
    private String value;

    public Params(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
