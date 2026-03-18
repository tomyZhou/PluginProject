package com.plugin.stander;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public interface ServiceInterface {

    public void insertServiceContext(Service service);

    public IBinder onBind(Intent intent);
    public void onCreate();

    public int onStartCommand(Intent intent, int flags, int startId);

    public void onDestroy();

}
