package com.example.v1.xklibrary.net;


import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by xuekai on 2017/6/16.
 */

public class RxTest {
    public static void main(String[] args) {
        final boolean disk = false;
        Observable diskObservable = Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(@NonNull ObservableEmitter e) throws Exception {
                if (disk) {
                    System.out.println("进行了disk");
                    e.onNext("");
                }
                e.onComplete();
            }
        });
        Observable netObservable = Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(@NonNull ObservableEmitter e) throws Exception {
                System.out.println("进行了net");
                e.onComplete();
            }
        });

        Observable.concat(diskObservable, netObservable)
                .first("")
                .subscribe();
    }
}
