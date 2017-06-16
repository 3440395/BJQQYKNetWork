package com.example.v1.xklibrary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;
import java.util.ArrayList;

/**
 * Created by xuekai on 2017/6/16.
 */

public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Network.getInstance().invoke("getMovieDetail", new ArrayList<Params>(), new FailedDialogCallback() {
            @Override
            public void onSuccess(String content) {
                Toast.makeText(MainActivity.this,content,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
