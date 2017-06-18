package com.xk.bjqqyknetwork;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.myhttp.HttpBaseActivity;
import com.example.myhttp.net.MCallback;
import com.example.myhttp.net.NetWork;
import com.example.myhttp.net.Params;
import com.example.myhttp.util.LogUtil;
import com.example.myhttp.xml.BaseXmlParser;
import com.xk.bjqqyknetwork.entry.Movie;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class MainActivity extends HttpBaseActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void request(View v) {
        Observable<Movie> getMovie = invokeByRx("getMovie", null, -100, false, new BaseXmlParser<Movie>() {
            @Override
            public Movie parser(String xml) {
                return null;
            }
        });
        getMovie.subscribe(new Consumer<Movie>() {
            @Override
            public void accept(@NonNull Movie o) throws Exception {
                LogUtil.d("MainActivity:accept-->"+o);

            }
        });
    }

    public  Observable<Movie> invokeByRx(final String key, final List<Params> params
            , final int retryTimes, final boolean forceUpdate, final BaseXmlParser<Movie> parser) {
        return Observable.create(new ObservableOnSubscribe<Movie>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<Movie> e) throws Exception {
                NetWork.getInstance().invoke(key, params, retryTimes, forceUpdate, new MCallback<Movie>() {
                    @Override
                    public void onSuccess(Movie t) {
                        e.onNext(t);
                        e.onComplete();
                    }

                    @Override
                    public void onFailed(String errorMsg) {
                        e.onError(new Throwable(errorMsg));
                    }
                }, parser);
            }
        });

    }
}
