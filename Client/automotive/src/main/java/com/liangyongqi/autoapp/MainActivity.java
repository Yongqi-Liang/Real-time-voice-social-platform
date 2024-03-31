package com.liangyongqi.autoapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import com.xuexiang.xui.*;


public class MainActivity extends AppCompatActivity {
    private static final int SPLASH_SCREEN_TIMEOUT = 3000; // 3秒启动页显示时间  

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        XUI.initTheme(this);
        super.onCreate(savedInstanceState);

        // 设置全屏显示，无标题栏  
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 设置启动页布局  
        setContentView(R.layout.activity_home);


    }
}

