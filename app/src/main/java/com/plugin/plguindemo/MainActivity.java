package com.plugin.plguindemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private Integer  REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);

        findViewById(R.id.bt_load).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPlugin();
            }
        });

        findViewById(R.id.to_plugin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPluginActivity();
            }
        });

    }


    //加载插件代码和资源
    public void loadPlugin(){
        Toast.makeText(this, "加载plugin", Toast.LENGTH_SHORT).show();
        //加载插件项目
        PluginManager.getInstance(this).loadPlugin();
    }

    public void startPluginActivity(){
        //启动占位，代理Activity, 借ProxyActivity这个壳，真正的加载插件Activity里的内容
        String pluginName = "plugin.apk";
        PackageManager packageManager = getPackageManager();
        File pluginFile = new File(getFilesDir(), pluginName);
        if(!pluginFile.exists()){
            return;
        }
        String packagePath = pluginFile.getAbsolutePath();
        PackageInfo info = packageManager.getPackageArchiveInfo(packagePath, PackageManager.GET_ACTIVITIES);

        //经测试发现：info.activities插件里Activity的顺序和插件里Manifest.xml里声明的顺序一样。
        if(info!=null){
            ActivityInfo activityInfo = info.activities[0];

             for(int i=0;i<info.activities.length;i++){
                 Log.e("xxx",info.activities[i].name);
             }
            Intent intent = new Intent(this,ProxyActivity.class);
            intent.putExtra("className",activityInfo.name);
            startActivity(intent);
        }
    }

}