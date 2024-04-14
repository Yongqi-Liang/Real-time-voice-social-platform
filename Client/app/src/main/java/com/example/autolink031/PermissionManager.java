// PermissionManager.java
package com.example.autolink031;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionManager {

    public interface PermissionCallback {
        void onPermissionGranted();

        void onPermissionDenied();
    }

    public interface PrivacyPolicyCallback {
        void onPrivacyPolicyAgreed() throws Exception;
    }

    public static boolean hasUserAgreedToPrivacyPolicy(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("privacy_policy_agreed", false);
    }

    public static void setUserAgreedToPrivacyPolicy(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean("privacy_policy_agreed", true).apply();
    }

    // 新增方法
    public static void updatePrivacyShow(boolean value) {
        // 实现更新隐私合规显示的逻辑
        // 这里可以设置相关的标志或调用相应的接口
    }

    // 新增方法
    public static void updatePrivacyAgree(boolean value) {
        // 实现更新隐私合规同意的逻辑
        // 这里可以设置相关的标志或调用相应的接口
    }

    public static void requestLocationPermission(MainActivity activity, PermissionCallback callback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                if (callback != null) {
                    callback.onPermissionGranted();
                }
            } else {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        } else {
            if (callback != null) {
                callback.onPermissionGranted();
            }
        }
    }

    public static void onRequestPermissionsResult(MainActivity activity, int requestCode,
                                                  @NonNull String[] permissions,
                                                  @NonNull int[] grantResults, PermissionCallback callback) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (callback != null) {
                    callback.onPermissionGranted();
                }
            } else {
                if (callback != null) {
                    callback.onPermissionDenied();
                }
            }
        }
    }

    // 修改这个方法的参数类型为 AppCompatActivity
    public static void showPrivacyPolicyDialog(AppCompatActivity activity, PrivacyPolicyCallback callback) {
        if (!hasUserAgreedToPrivacyPolicy(activity)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("隐私政策");
            builder.setMessage("请同意我们的隐私政策以继续使用应用。");

            builder.setPositiveButton("同意", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (callback != null) {
                        try {
                            callback.onPrivacyPolicyAgreed();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }

                    setUserAgreedToPrivacyPolicy(activity);

                    // 调用更新隐私合规显示和同意的接口
                    updatePrivacyShow(true);
                    updatePrivacyAgree(true);
                }
            });

            builder.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 用户拒绝隐私政策，可以在这里采取相应的处理
                }
            });

            builder.setCancelable(false);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}