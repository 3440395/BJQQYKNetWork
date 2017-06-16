package com.example.v1.xklibrary.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.v1.xklibrary.MyCallback;
import com.example.v1.xklibrary.net.Network;

/**
 * Created by xuekai on 2017/6/16.
 */

public class BaseActivity extends Activity {
    private Dialog dialog;
    public ProgressDialog pd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pd=new ProgressDialog(this);
    }

    abstract class FailedDialogCallback implements MyCallback {
        @Override
        public void onFail(String errorMessage) {

            if (dialog == null) {
                dialog = new AlertDialog.Builder(BaseActivity.this)
                        .create();
            }
            dialog.setTitle(errorMessage);
            dialog.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Network.getInstance().cancleAllRequest();
    }
}
