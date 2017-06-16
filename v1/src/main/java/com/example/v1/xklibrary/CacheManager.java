package com.example.v1.xklibrary;

/**
 * Created by xuekai on 2017/6/16.
 */

public class CacheManager {

    /**
     * 根据api去取缓存
     *
     * @param api
     * @return
     */
    public static CacheItem getCache(String api) {
        //通过api从本地拿到数据，然后强转成cacheitem，然后拿出过期时间与当前时间对比，然后return
        return new CacheItem();
    }

    /**
     * 根据api去取缓存
     *
     * @param api
     * @return
     */
    public static void saveCache(String api, String data, long exceed) {
//        return new CacheItem();
        //构建一个cacheitem，里面有到期的时间，然后保存到磁盘，名称为api
    }

}
