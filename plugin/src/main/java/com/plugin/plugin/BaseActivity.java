package com.plugin.plugin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.ComponentActivity;

import com.plugin.stander.ActivityInterface;

public class BaseActivity extends ComponentActivity implements ActivityInterface {

    //宿主的Activity环境
    public Activity appActivity;

    //将宿主的Activity环境传进来
    @Override
    public void insertAppContext(Activity appActivity) {
        this.appActivity = appActivity;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onStart() {
        super.onStart();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onResume() {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onStop() {

    }

    @Override
    public void onDestory() {

    }

    public void setContentView(int resId){
        appActivity.setContentView(resId);
    }

    //因为pluginManager里面将插件资源设置进去了，所以能找到。
    public View findViewById(int resId){
        return appActivity.findViewById(resId);
    }
}
