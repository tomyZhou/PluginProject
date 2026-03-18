package com.plugin.plguindemo;

import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.os.IBinder;

import com.plugin.stander.ServiceInterface;

public class ProxyService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
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
            //注入宿主占位Service的环境
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