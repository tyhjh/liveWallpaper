package com.tyhj.wallpaper.ui.common;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.display.loglibrary.LogUtil;

public class BaseActivity extends AppCompatActivity {

    private static final boolean ISDEBUG = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void log(String msg) {
        if (ISDEBUG){
            LogUtil.i(getClass().getName(), msg);
        }

    }

    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void snackbar(View view, String msg, int time) {
        Snackbar.make(view, msg, time).show();
    }

}
