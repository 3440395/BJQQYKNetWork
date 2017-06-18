package com.example.myhttp.net;

import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by xuekai on 2017/6/17.
 */

public abstract class MCallback<T> {
    Type type;

    static Type getSuperclassTypeParameter(Class subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException ("Missing type parameter");

        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    public MCallback() {
        type = getSuperclassTypeParameter(getClass());

    }

    public abstract void onSuccess( T t);
    public abstract void onFailed(String errorMsg);

    public void showDialog(){};

}
