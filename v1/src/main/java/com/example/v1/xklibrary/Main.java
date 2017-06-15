package com.example.v1.xklibrary;

import com.example.v1.baobaomylibrary.utils.Utils;

import java.util.ArrayList;

/**
 * Created by xuekai on 2017/6/15.
 */

public class Main {
    public static void main(String [] args){
//        new Main().doPost();
        String s = Utils.UrlEncodeUnicode("薛凯abc123!@#+_/?");
        System.out.println(s);
    }
    private static final String TAG = "Network";

    public void doGet() {
        ArrayList<Params> paramses = new ArrayList<>();
        Params kkk = new Params("kkk", "111");
        Params kkk1 = new Params("kkk11", "22");
        paramses.add(kkk);
        paramses.add(kkk1);
        Network.getInstance().invoke("getMovieDetail", paramses, new MyCallback() {
            @Override
            public void onSuccess(String content) {
                System.out.println("success     "+content);
            }

            @Override
            public void onFail(String errorMessage) {
                System.out.println("nFai     "+errorMessage);
            }
        });
    }


    public void doPost() {
        ArrayList<Params> paramses = new ArrayList<>();
        Params kkk = new Params("kkk", "111");
        Params kkk1 = new Params("kkk11", "22");
        paramses.add(kkk);
        paramses.add(kkk1);
        Network.getInstance().invoke("registerUser", paramses, new MyCallback() {
            @Override
            public void onSuccess(String content) {
                System.out.println("success     "+content);
            }

            @Override
            public void onFail(String errorMessage) {
                System.out.println("nFai     "+errorMessage);
            }
        });
    }
}
