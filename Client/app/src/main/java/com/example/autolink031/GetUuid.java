// GetUuid.java
package com.example.autolink031;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;

public class GetUuid {

    @SuppressLint("HardwareIds")
    public static String getAndroidId(Context context) {
        try {
            // 获取 Android ID
            String uuid = Settings.Secure.getString(
                    context.getContentResolver(),
                    Settings.Secure.ANDROID_ID
            );
            // 打印 Android ID，以便调试
            Log.d("GetUuid", "Android ID: " + uuid);
            System.out.println("Android ID: " + uuid);

            return uuid;
        } catch (Exception e) {
            // 打印异常信息，以便调试
            Log.e("GetUuid", "Error getting Android ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}