package com.plugin.plguindemo;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.plugin.stander.ServiceInterface;

public class ProxyService extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    //其实是插件里的classLoader
    @Override
    public ClassLoader getClassLoader() {
        return PluginManager.getInstance(this).getClassLoader();
    }

    //其实是插件里的resources
    @Override
    public Resources getResources() {
        return PluginManager.getInstance(this).getResources();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String serviceName = intent.getStringExtra("serviceName");

        try {
            Class serviceClass = getClassLoader().loadClass(serviceName);
            Object object = serviceClass.newInstance();
            ServiceInterface serviceInterface = (ServiceInterface) object;

            //把宿主占位Service的环境传给插件Service
            serviceInterface.insertServiceContext(this);

            serviceInterface.onStartCommand(intent,flags,startId);
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}