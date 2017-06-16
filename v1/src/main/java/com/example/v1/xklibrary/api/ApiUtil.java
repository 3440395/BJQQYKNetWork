package com.example.v1.xklibrary.api;

/**
 * Created by xuekai on 2017/6/15.
 */

public class ApiUtil {
// <Node
//    Key="getMovieDetail"
//    Expires="300"
//    NetType="get"
//    retryTimes="3"
//    Url="/movie/getmoviedetail" />
//        <!--MockClass="jianqiang.com.youngheart.mockdata.MockMovie" />-->
//
//    <Node
//    Key="registerUser"
//    Expires="0"
//    NetType="post"
//    retryTimes="0"
//    Url="/others/register" />
//
//    <Node
//    Key="sendReport"
//    Expires="0"
//    NetType="post"
//    retryTimes="0"
//    Url="/others/sendReport" />

    public static Api getApi(String api) {
        if (api.equals("getMovieDetail")) {
            return new Api("/movie/getmoviedetail", "get", 100);
        } else if (api.equals("registerUser")) {
            return new Api("/others/register", "post", 100);
        }
        return null;
    }
}


