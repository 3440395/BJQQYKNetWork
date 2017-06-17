package com.example.myhttp;

import android.app.Application;

/**
 * Created by xuekai on 2017/6/17.
 */

public class BaseApplication extends Application {
    public static BaseApplication app;
    @Override
    public void onCreate() {
        super.onCreate();
        app=this;



        //从动态入口读取数据，并且缓存到本地
    }

}
