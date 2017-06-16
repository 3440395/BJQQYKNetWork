package com.example.v1.xklibrary;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;

/**
 * Created by xuekai on 2017/6/16.
 */

public class BaseActivity extends Activity {
    private Dialog dialog;

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
}
