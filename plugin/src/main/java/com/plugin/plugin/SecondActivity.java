package com.plugin.plugin;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SecondActivity extends BaseActivity {

    private MyBroadcastReceiver myReceiver;
    private TextView tv_progress;

    //this不能用，父类不能用
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //注意1
            setContentView(R.layout.activity_second);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

        if(savedInstanceState != null && savedInstanceState.getString("data")!=null) {
            String data = savedInstanceState.getString("data");
            Log.e("xxx", data);
            Toast.makeText(appActivity, "我是插件，" + data, Toast.LENGTH_SHORT).show();
        }

        findViewById(R.id.to_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(appActivity,TestService.class);
                appActivity.startService(intent);
            }
        });

        tv_progress = (TextView) findViewById(R.id.tv_progress);

        Log.e("xxx","注册myReceiver");
        myReceiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter("com.example.broadcast.MY_BROADCAST");

        try{
            appActivity.registerReceiver(myReceiver, filter,Context.RECEIVER_EXPORTED); // 动态注册接收器
        }catch(Exception e){
            e.fillInStackTrace();
        }
    }


    @Override
    public void onDestory() {
        Log.e("xxx","onDestory:"+SecondActivity.class.getSimpleName());
        try{
            appActivity.unregisterReceiver(myReceiver); // 注销接收器
        }catch (Exception e){
            e.fillInStackTrace();
        }
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            //广播接收器可能在非UI线程上运行

            appActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String message = intent.getStringExtra("message");
                    Log.e("xxx","收到通知:"+message);
                    // 处理接收到的广播
                    tv_progress.setText(message);
                }
            });
        }
    }

}