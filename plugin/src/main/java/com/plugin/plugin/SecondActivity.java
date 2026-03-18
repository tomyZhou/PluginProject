package com.plugin.plugin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SecondActivity extends BaseActivity {

    //this不能用，父类不能用
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

        if(savedInstanceState == null && savedInstanceState.getString("data")!=null) {
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

    }
}