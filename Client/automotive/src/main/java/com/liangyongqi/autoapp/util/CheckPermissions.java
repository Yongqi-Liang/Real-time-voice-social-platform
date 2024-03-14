package com.liangyongqi.autoapp.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * File: CheckPermissions.java
 * Author: liangyongqi
 * Date: 2024/3/12
 * Time: 22:37
 */
public class CheckPermissions extends Activity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private static final int MICROPHONE_PERMISSION_REQUEST_CODE = 101;

    public void checkAndRequestLocationPermissions(Activity activity) {
        // 检查是否已经获取了位置权限
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 如果没有权限，则请求权限
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    public void checkAndRequestMicrophonePermissions(Activity activity) {
        // 检查是否已经获取了麦克风权限
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // 如果没有权限，则请求权限
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    MICROPHONE_PERMISSION_REQUEST_CODE);
        }
    }

    // 处理权限请求的结果
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // 根据请求码判断是哪个权限请求
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                // 处理位置权限请求的结果
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 用户同意了位置权限
                    Toast.makeText(this, "位置权限已获取", Toast.LENGTH_SHORT).show();
                } else {
                    // 用户拒绝了位置权限
                    Toast.makeText(this, "位置权限被拒绝", Toast.LENGTH_SHORT).show();
                }
                break;
            case MICROPHONE_PERMISSION_REQUEST_CODE:
                // 处理麦克风权限请求的结果
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 用户同意了麦克风权限
                    Toast.makeText(this, "麦克风权限已获取", Toast.LENGTH_SHORT).show();
                } else {
                    // 用户拒绝了麦克风权限
                    Toast.makeText(this, "麦克风权限被拒绝", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
