package com.plugin.plguindemo;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class PluginManager {
    private static PluginManager pluginManager;
    private Context context;

    private DexClassLoader classLoader;

    //存放插件包里的资源
    private Resources resources;

    public static final String TAG = PluginManager.class.getSimpleName();

    public static PluginManager getInstance(Context context) {
        if (pluginManager == null) {
            synchronized (PluginManager.class) {
                if (pluginManager == null) {
                    pluginManager = new PluginManager(context);
                }
            }
        }

        return pluginManager;
    }


    private PluginManager(Context context) {
        this.context = context;
    }

    /**
     *    context.getFilesDir()
     *    /data/user/0/com.plugin.plguindemo/files/plugin.js
     *
     *    context.getExternalFilesDir("");
     *    storage/emulated/0/Android/data/com.plugin.plugindemo/plugin.js
     */
    public void loadPlugin() {

        String pluginName = "plugin.js";

            //这一句也很重要，放在私有目录下getFilesDir setReadOnly才有作用
            //实测证明 context.getExternalFilesDir(""); 这样获取到的是外部存储，对外部存储的文件设置setReadOnly不起作用。
            //Attempt to load writeable dex file: storage/emulated/0/Android/data/com.plugin.plugindemo/plugin.js
            File pluginFile = new File(context.getFilesDir(), pluginName);

            Log.e("xxx",pluginFile.getAbsolutePath());

            if (!pluginFile.exists()) {
                // 调用之前学过的拷贝方法，将assets中的APK复制到私有目录
                if (!FileUtils.copyApkFromAssetsToPrivateDir(context, "plugin.js", pluginName)) {
                    Log.e(TAG, "插件文件复制失败");
                }else{
                    load(pluginFile);
                }
            }else{
                Log.d(TAG,pluginFile.getAbsolutePath());

                load(pluginFile);
            }
    }

    private void load(File pluginFile){

        try{
            //这一句很重要。
            pluginFile.setReadOnly();

            // 2. 确定优化目录 (DexClassLoader 用于存放优化后的dex文件)
            File optimizedDir;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                // Android 5.0+ 推荐使用 code_cache 目录 [citation:3]
                optimizedDir = context.getCodeCacheDir();
            } else {
                // 低版本手动创建私有目录 [citation:1][citation:4][citation:5]
                optimizedDir = context.getDir("dexopt", Context.MODE_PRIVATE);
            }

            // 3. 创建 DexClassLoader 实例来加载APK
            // 参数：dexPath(apk路径), optimizedDirectory(优化目录), librarySearchPath(so库路径，此处为null), parent(父加载器) [citation:1][citation:4]
            classLoader = new DexClassLoader(
                    pluginFile.getAbsolutePath(),
                    optimizedDir.getAbsolutePath(),
                    null,
                    context.getClassLoader()
            );


            //1.获取到操作插件里资源的Resources
            //资源管理器 可通过反射方式创建对象
            AssetManager assetManager = AssetManager.class.newInstance(); //context.getAssets();
            //我们要调用添加资源路径的方法,String参数
            Method addAssetPathMethod = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPathMethod.invoke(assetManager, pluginFile.getAbsolutePath()); //执行了这一句后，assetManager就能寻找插件包里的资源

            //宿主里的资源配置信息，如x-dpi等，拿到这些信息给插件项目使用。
            Resources hostResources = context.getResources();

            //加载插件里资源的Resources
            resources = new Resources(assetManager, hostResources.getDisplayMetrics(), hostResources.getConfiguration());

            Log.e("xxx","加载成功");
        }catch(Exception e){
            e.fillInStackTrace();
        }

    }

    public DexClassLoader getClassLoader() {
        return classLoader;
    }

    public Resources getResources() {
        return resources;
    }
}
