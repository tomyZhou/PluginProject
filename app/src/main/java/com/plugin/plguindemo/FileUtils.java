package com.plugin.plguindemo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {

    /**
     * 将apk文件从assets里拷贝到私有目录下
     * @param context
     * @param assetFileName
     * @param destFileName
     * @return
     */
        public static boolean copyApkFromAssetsToPrivateDir(Context context, String assetFileName, String destFileName) {
            InputStream is = null;
            FileOutputStream fos = null;
            Log.e("xxx","拷贝开始");
            try {
                // 打开assets中的文件
                is = context.getAssets().open(assetFileName);

                // 目标文件：私有目录下的files文件夹
                File pluginFile = new File(context.getFilesDir(), destFileName);
                fos = new FileOutputStream(pluginFile);

                // 缓冲区复制
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
                fos.flush();

                Log.e("xxx","拷贝完成");
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                try {
                    if (is != null) is.close();
                    if (fos != null) fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    /**
     * 获取到插件包里第一个要打开的Activity类名
     * @param context
     * @return
     */
    public static String  getLaunchActivityName(Context context){
            //启动占位，代理Activity, 借ProxyActivity这个壳，真正的加载插件Activity里的内容
            String pluginName = "plugin.apk";
            PackageManager packageManager = context.getPackageManager();
            File pluginFile = new File(context.getFilesDir(), pluginName);
            if(!pluginFile.exists()){
                return null;
            }
            String packagePath = pluginFile.getAbsolutePath();
            PackageInfo info = packageManager.getPackageArchiveInfo(packagePath, PackageManager.GET_ACTIVITIES);

            //经测试发现：info.activities插件里Activity的顺序和插件里Manifest.xml里声明的顺序一样。
            if(info!=null){
                ActivityInfo activityInfo = info.activities[0];

                for(int i=0;i<info.activities.length;i++){
                    Log.e("xxx",info.activities[i].name);
                }
                return activityInfo.name;
            }else{
                return null;
            }
        }

    }