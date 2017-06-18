package com.example.myhttp.xml;

/**
 * Created by xuekai on 2017/6/18.
 */

public abstract class BaseXmlParser<T> {
    public abstract T parser(String xml);
}
