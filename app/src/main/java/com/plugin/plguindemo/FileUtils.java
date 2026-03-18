package com.plugin.plguindemo;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {

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

    }