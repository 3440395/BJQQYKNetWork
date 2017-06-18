package com.xk.bjqqyknetwork;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.example.myhttp.HttpBaseActivity;
import com.example.myhttp.net.MCallback;
import com.example.myhttp.net.NetWork;
import com.example.myhttp.net.Params;
import com.example.myhttp.xml.BaseXmlParser;
import com.xk.bjqqyknetwork.entry.Movie;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends HttpBaseActivity {
    final int[] f = {1};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        while (true) {
//
//                        }
//                    }
//                });
//            }
//        }).start();
    }

    public void request(View v) {


        Observable<Movie> getMovie = invokeByRx("getMovie", null, -100, false, new BaseXmlParser<Movie>() {
            @Override
            public Movie parser(String xml) {
                return null;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        getMovie.subscribe(new Consumer<Movie>() {
            @Override
            public void accept(@NonNull Movie o) throws Exception {
                Toast.makeText(MainActivity.this, o.getMovieName(), Toast.LENGTH_SHORT).show();
            }
        });

//        NetWork.getInstance().invokeByRx("getMovie", null, 3, false, new BaseXmlParser<Object>() {
//            @Override
//            public Object parser(String xml) {
//                return null;
//            }
//        }).subscribe(new Consumer<Object>() {
//            @Override
//            public void accept(@NonNull Object o) throws Exception {
//                Movie o1 = (Movie) o;
//                LogUtil.d("MainActivity:accept-->"+o1);
//            }
//        });
    }

    public Observable<Movie> invokeByRx(final String key, final List<Params> params
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


    public Observable<Movie> invokeByRx1() {
        return Observable.create(new ObservableOnSubscribe<Movie>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<Movie> e) throws Exception {
                e.onNext(new Movie());
                e.onComplete();
            }
        });

    }
}
