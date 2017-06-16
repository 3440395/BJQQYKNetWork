package com.example.v1.xklibrary.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.v1.xklibrary.LogUtil;
import com.example.v1.xklibrary.Params;
import com.example.v1.xklibrary.net.Network;

import java.util.ArrayList;

/**
 * Created by xuekai on 2017/6/16.
 */

public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sendGet();
    }


    void sendGet(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Network.getInstance().invoke(MainActivity.this,"getMovieDetail", new ArrayList<Params>(), new FailedDialogCallback() {
                    @Override
                    public void onSuccess(String content) {
                        LogUtil.d("MainActivity:onSuccess--->"+content);
                        Toast.makeText(MainActivity.this,content+"111",Toast.LENGTH_SHORT).show();
                    }
                },true,-100);
            }
        }).start();
    }

    /**
     * 例如汇报，不需要返回结果
     */
    void sendReport(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Network.getInstance().invoke(MainActivity.this,"getMovieDetail", new ArrayList<Params>());
            }
        }).start();
    }
}
