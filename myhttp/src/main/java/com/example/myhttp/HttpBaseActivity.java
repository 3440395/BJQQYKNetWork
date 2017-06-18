package com.example.myhttp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.myhttp.net.MCallback;

/**
 * 该activity用于Http框架
 * Created by xuekai on 2017/6/17.
 */

public class HttpBaseActivity extends Activity {
    private Dialog dialog;

    abstract class FailedDialogCallback<T> extends MCallback<T> {
        @Override
        public void onFailed(String errorMsg) {

            if (dialog == null) {
                dialog = new AlertDialog.Builder(HttpBaseActivity.this)
                        .create();
            }
            dialog.setTitle(errorMsg);
            dialog.show();
        }
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onPause() {
        super.onPause();
        //这里不需要了 由rx来管理
//        NetWork.getInstance().cancleAllRequest(this);
    }
}
