package com.xk.bjqqyknetwork;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.myhttp.net.Callback;
import com.example.myhttp.net.NetWork;
import com.xk.bjqqyknetwork.entry.Movie;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(()-> NetWork.getInstance().invoke("getMovie", Movie.class, null, new Callback<Movie>() {
            @Override
            public void onSuccess(Movie movie) {

            }

            @Override
            public void onFile(String errorMsg) {

            }
        })).start();
    }
}
