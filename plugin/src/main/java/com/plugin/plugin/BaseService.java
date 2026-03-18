package com.plugin.plugin;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.plugin.stander.ServiceInterface;

public class BaseService extends Service implements ServiceInterface {

    public Context appService;
    @Override
    public void insertServiceContext(Service service) {
        this.appService = service;
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
