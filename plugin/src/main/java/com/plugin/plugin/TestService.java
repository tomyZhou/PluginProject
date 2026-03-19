package com.plugin.plugin;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class TestService extends BaseService{

    private boolean isDownload = false;

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
                if(isDownload){
                    return;
                }
                while (progress<=95){
                    try {
                        isDownload = true;
                        Thread.sleep(500);
                        progress+=5;

                        Intent intent = new Intent("com.example.broadcast.MY_BROADCAST");
                        intent.putExtra("message", "下载进度:"+progress+"%"); // 添加一些数据
                        appService.sendBroadcast(intent); // 直接在Service中使用

                        Log.e("xxx","发送通知");

                    } catch (InterruptedException e) {
                        e.fillInStackTrace();
                    }finally {
                        Log.e("xxx","插件里面的服务，正在执行中..."+progress+"%");
                    }
                }
                isDownload = false;

            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
