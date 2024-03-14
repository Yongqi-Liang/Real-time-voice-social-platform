package com.liangyongqi.autoapp.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * File: ConfigurationManager.java
 * Author: liangyongqi
 * Date: 2024/3/12
 * Time: 23:36
 */
public class ConfigurationManager {

    private static final String PREF_NAME = "UserConfig";
    private static final String KEY_UUID = "uuid";
    private static final String KEY_SESSION_ID = "session_id";
    private static final String KEY_PRIVACY_AGREED = "privacy_agreed";

    //获取UUID
    public static String getUUID(Context context) {
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (TextUtils.isEmpty(androidId)) {// 如果Android ID为空，则可能需要处理异常情况，例如返回null或默认字符串
            return null; // 或者你可以返回一个默认的UUID或其他标识符
        }
        return androidId;
    }
    //获取SessionID
    public static String getSessionID(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_SESSION_ID, null);
    }
    //获取隐私政策同意状态
    public static boolean isPrivacyAgreed(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_PRIVACY_AGREED, false);
    }
}