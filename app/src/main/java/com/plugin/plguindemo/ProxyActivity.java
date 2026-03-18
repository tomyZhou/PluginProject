package com.plugin.plguindemo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.Nullable;

import com.plugin.stander.ActivityInterface;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.List;

import dalvik.system.DexClassLoader;

//占位（代理）Activity
public class ProxyActivity extends Activity {

    @Override
    public ClassLoader getClassLoader() {
        return PluginManager.getInstance(this).getClassLoader();
    }

    @Override
    public Resources getResources() {
        return PluginManager.getInstance(this).getResources();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("xxx","进入到proxy类");

        String className = getIntent().getStringExtra("className");

        Log.e("xxx",className);

        try {
            //用的是加载了插件包内容的classLoader，加载插件包里的Activity并通过反射创建对象。
            Class clazz = getClassLoader().loadClass(className);
            Constructor constructor = clazz.getConstructor(new Class[]{});
            Object mPluginActivity = constructor.newInstance(new Object[]{});
            ActivityInterface activityInterface = (ActivityInterface) mPluginActivity;

            //将宿主占位Activity的context传给插件Activity。
            activityInterface.insertAppContext(this);

            Bundle bundle = new Bundle();
            bundle.putString("data","我是宿主传递过来的信息,"+className);

            //手动调用插件里的onCreate
            activityInterface.onCreate(bundle);

        } catch (Exception e) {
            e.fillInStackTrace();
        }

    }

    @Override
    public void startActivity(Intent intent) {

            String appName = intent.getComponent().getClassName();

            Intent proxyIntent = new Intent(this,ProxyActivity.class);
            proxyIntent.putExtra("className",appName);
            //不要死循环调用自己的方法，要用父类的方法。super
            super.startActivity(proxyIntent);
    }
}
