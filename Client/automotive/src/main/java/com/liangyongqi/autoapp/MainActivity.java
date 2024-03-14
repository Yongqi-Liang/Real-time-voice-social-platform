/**
 * @File: MainActivity.java
 * @Author: liangyongqi
 * @Email: 
 * @Date: 2024/3/12
 * @Time: 18:13
 */
package com.liangyongqi.autoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.liangyongqi.autoapp.util.CheckPermissions;
import com.liangyongqi.autoapp.util.ConfigurationManager;
import com.liangyongqi.autoapp.util.PrivacyDialog;
import com.liangyongqi.autoapp.util.UserSessionManager;

public class MainActivity extends AppCompatActivity {

    String Uuid = ConfigurationManager.getUUID(getApplicationContext());
    String SessionId = ConfigurationManager.getSessionID(getApplicationContext());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设置启动页布局
        setContentView(R.layout.activity_main_splash);
        // 查询隐私政策同意状态的逻辑
        boolean isPrivacyAgreed = ConfigurationManager.isPrivacyAgreed(getApplicationContext());
        if (!isPrivacyAgreed){
            // 如果用户未同意隐私政策，则弹出隐私政策对话框
            PrivacyDialog privacyDialog = new PrivacyDialog();
            privacyDialog.showPrivacyDialog(MainActivity.this);
        }
        //调用CheckPermission.java中的检查权限的方法
        CheckPermissions permissionsChecker = new CheckPermissions();
        permissionsChecker.checkAndRequestLocationPermissions(MainActivity.this);//位置信息权限
        permissionsChecker.checkAndRequestMicrophonePermissions(MainActivity.this);//麦克风权限
        //采用SessionLogin方式登录
        String sessionId = ConfigurationManager.getSessionID(getApplicationContext());
        if (sessionId == null || sessionId.isEmpty()) {
            // 如果 SessionId 为空，则跳转至登录页面
            // 这里假设登录页面的 Activity 类名为 LoginActivity
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish(); // 结束当前 Activity
        }
        UserSessionManager sessionManager = new UserSessionManager();
        sessionManager.SessionLogin(getApplicationContext(), sessionId);

        setContentView(R.layout.activity_main);
    }
}