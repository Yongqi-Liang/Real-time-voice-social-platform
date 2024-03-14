package com.liangyongqi.autoapp.util;

/**
 * File: PrivacyDialog.java
 * Author: liangyongqi
 * Date: 2024/3/12
 * Time: 19:53
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.liangyongqi.autoapp.R;

public class PrivacyDialog {

    public void showPrivacyDialog(Activity activity) {
        // 弹出隐私政策页面逻辑
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.privacy_dialog);

        // 设置对话框标题
        dialog.setTitle("用户隐私协议");

        // 初始化 WebView 并加载指定的 URL
        WebView webView = dialog.findViewById(R.id.webview);
        webView.loadUrl("https://liangyongqi.top/privacy-policy");

        // 获取“我同意”按钮
        Button agreeButton = dialog.findViewById(R.id.agree_button);
        agreeButton.setOnClickListener(v -> {
            // 点击“我同意”按钮事件逻辑
            // 使用 SharedPreferences 编辑器来修改隐私政策同意状态的事件逻辑
            SharedPreferences sharedPreferences = activity.getSharedPreferences("privacy_pref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("privacy_agreed", true); // 将隐私政策同意状态设置为 true
            editor.apply(); // 提交修改
            // 关闭对话框
            dialog.dismiss();
        });
        // 获取“拒绝并退出”按钮
        Button rejectButton = dialog.findViewById(R.id.reject_button);
        rejectButton.setOnClickListener(v -> {
            // 点击“拒绝并退出”按钮事件逻辑
            // 关闭应用程序
            activity.finish();
        });

        // 显示对话框
        dialog.show();
    }
}
