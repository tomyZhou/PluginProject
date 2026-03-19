package com.plugin.plguindemo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import com.plugin.stander.ActivityInterface;
import java.lang.reflect.Constructor;


//占位（代理）Activity
public class ProxyActivity extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String className = getIntent().getStringExtra("className");
        Log.e("xxx", "进入到proxy类");

        ActivityInterface activityInterface = getActivityInterface();
        if(activityInterface!=null){
            Bundle bundle = new Bundle();
            bundle.putString("data", "我是宿主传递过来的信息," + className);

            //手动调用插件里的onCreate
            activityInterface.onCreate(bundle);
        }
    }

    public ActivityInterface getActivityInterface() {
        String className = getIntent().getStringExtra("className");

        Log.e("xxx", className);

        try {
            //用的是加载了插件包内容的classLoader，加载插件包里的Activity并通过反射创建对象。
            Class clazz = getClassLoader().loadClass(className);
            Constructor constructor = clazz.getConstructor(new Class[]{});
            Object mPluginActivity = constructor.newInstance(new Object[]{});
            ActivityInterface activityInterface = (ActivityInterface) mPluginActivity;

            //将宿主占位Activity的context传给插件Activity。
            activityInterface.insertAppContext(this);

            return activityInterface;

        } catch (Exception e) {
            e.fillInStackTrace();
        }
        return null;
    }


    @Override
    protected void onDestroy() {
        ActivityInterface activityInterface = getActivityInterface();
        activityInterface.onDestory();
        super.onDestroy();
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
    public void startActivity(Intent intent) {

            //获取Intent中的第二个参数的类名。intent.getComponent().getClassName();
            String appName = intent.getComponent().getClassName();

            Intent proxyIntent = new Intent(this,ProxyActivity.class);
            proxyIntent.putExtra("className",appName);
            //不要死循环调用自己的方法，要用父类的方法。super
            super.startActivity(proxyIntent);
    }

    //启动service
    @Nullable
    @Override
    public ComponentName startService(Intent service) {
        Intent intent = new Intent(this,ProxyService.class);
        intent.putExtra("serviceName",service.getComponent().getClassName());
        return super.startService(intent);
    }

}
