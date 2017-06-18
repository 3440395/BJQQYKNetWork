package com.example.myhttp.api;

import android.content.res.AssetManager;
import android.util.Xml;

import com.example.myhttp.BaseApplication;
import com.example.myhttp.util.LogUtil;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class ApiManager {
    private static final long DEFAULT_EXCEED = 20;
    private static final String DEFAULT_METHOD = "get";
    private static final int DEFAULT_RETRY_TIMES = 5;
    private static final String DEFAULT_FORMAT = "json";
    private static ArrayList<Api> apiList;


    /**
     * 从动态入口地址取数据 TODO 模拟数据
     */
    private synchronized void fetchUrlFromLocal() {
        LogUtil.d("ApiManager:fetchUrlFromLocal-->");
        //从本地的动态入口缓存中拿到数据解析成apiList
        apiList = new ArrayList<>();
        Api api1 = new Api();
        api1.setExceed(DEFAULT_EXCEED);
        api1.setKey("getMovie");//从文件中获取
        api1.setUrl("http://192.168.31.173:8888/movie/getmoviedetail");//从文件中获取
        api1.setMethod(DEFAULT_METHOD);
        api1.setFormat(DEFAULT_FORMAT);
        api1.setRetryTimes(DEFAULT_RETRY_TIMES);

        Api api2 = new Api();
        api2.setExceed(DEFAULT_EXCEED);
        api2.setKey("getMovieList");//从文件中获取
        api2.setUrl("www.getMovieList.com");//从文件中获取
        api2.setMethod(DEFAULT_METHOD);
        api2.setFormat(DEFAULT_FORMAT);
        api2.setRetryTimes(DEFAULT_RETRY_TIMES);

        Api api3 = new Api();
        api3.setExceed(DEFAULT_EXCEED);
        api3.setKey("sendReport");//从文件中获取
        api3.setUrl("www.sendReport.com");//从文件中获取
        api3.setMethod(DEFAULT_METHOD);
        api3.setFormat(DEFAULT_FORMAT);
        api3.setRetryTimes(DEFAULT_RETRY_TIMES);
        apiList.add(api1);
        apiList.add(api2);
        apiList.add(api3);
    }

    private synchronized void fetchApiDataFromXml() {
        LogUtil.d("ApiManager:fetchApiDataFromXml-->");
        if (apiList != null) {
            InputStream is = null;
            XmlPullParser xmlParser = Xml.newPullParser();
            AssetManager assets = BaseApplication.app.getAssets();
            try {
                is = assets.open("api.xml");
                xmlParser.setInput(is, "utf-8");
                int eventCode;
                eventCode = xmlParser.getEventType();
                while (eventCode != XmlPullParser.END_DOCUMENT) {
                    switch (eventCode) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            if ("node".equals(xmlParser.getName())) {
                                final String key = xmlParser.getAttributeValue(null,
                                        "key");
                                for (Api api : apiList) {
                                    if (api.getKey().equals(key)) {
                                        String exceed = xmlParser.getAttributeValue(null, "exceed");
                                        if (exceed != null) {
                                            api.setExceed(Long.parseLong(exceed));
                                        }
                                        String method = xmlParser.getAttributeValue(null, "method");
                                        if (method != null) {
                                            api.setMethod(method);
                                        }
                                        String mockClass = xmlParser.getAttributeValue(null, "mockClass");
                                        if (mockClass != null) {
                                            api.setMockClass(mockClass);
                                        }
                                        String retryTimes = xmlParser.getAttributeValue(null, "retryTimes");
                                        if (retryTimes != null) {
                                            api.setRetryTimes(Integer.parseInt(retryTimes));
                                        }
                                        String format = xmlParser.getAttributeValue(null, "format");
                                        if (format != null) {
                                            api.setFormat(format);
                                        }
                                        break;
                                    }
                                }
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            break;
                        default:
                            break;
                    }
                    eventCode = xmlParser.next();
                }
            } catch (final XmlPullParserException e) {
                e.printStackTrace();
            } catch (final IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public  Api findApi(final String findKey) {
        // 如果urlList还没有数据（第一次），或者被回收了，那么（重新）加载xml
        if (apiList == null || apiList.isEmpty()) {
            //先解析动态入口，得到apilist，然后解析api.xml，适当的修改上一部得到的数据
            fetchUrlFromLocal();
            fetchApiDataFromXml();
        }

        for (Api api : apiList) {
            if (findKey.equals(api.getKey())) {
                return api;
            }
        }

        return null;
    }

    public  void print() {
        // 如果urlList还没有数据（第一次），或者被回收了，那么（重新）加载xml
        if (apiList == null || apiList.isEmpty()) {
            //先解析动态入口，得到apilist，然后解析api.xml，适当的修改上一部得到的数据
            fetchUrlFromLocal();
            fetchApiDataFromXml();
        }

        for (Api api : apiList) {
            LogUtil.d("ApiManager:print-->" + api);
        }
    }
}

