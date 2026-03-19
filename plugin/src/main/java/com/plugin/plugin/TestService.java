package com.plugin.plugin;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class TestService extends BaseService{

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int progress = 0;
                while (progress<=95){
                    try {
                        Thread.sleep(1000);
                        progress+=5;
                    } catch (InterruptedException e) {
                        e.fillInStackTrace();
                    }finally {
                        Log.e("xxx","插件里面的服务，正在执行中..."+progress+"%");
                    }
                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
