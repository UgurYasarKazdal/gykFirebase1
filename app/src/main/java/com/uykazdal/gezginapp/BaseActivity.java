package com.uykazdal.gezginapp;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.uykazdal.gezginapp.helper.ViewHelper;

public class BaseActivity extends Activity {
    Dialog progressDialogBase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showDialog(String _mesaj , String _baslik) {
        progressDialogBase = ViewHelper.getDialog(this, _mesaj,_baslik);
        progressDialogBase.show();
    }

    public void dismissDiloag() {
        if (progressDialogBase != null) {
            if (progressDialogBase.isShowing()) {
                progressDialogBase.dismiss();
            }
        }
    }
}
