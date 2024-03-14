package com.liangyongqi.autoapp.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * File: UserSessionManager.java
 * Author: liangyongqi
 * Date: 2024/3/12
 * Time: 22:56
 */

public class UserSessionManager {
    private static final String TAG = "UserSessionManager";

    public void SessionLogin(final Context context, final String sessionId) {
        AsyncTask.execute(() -> {
            try {
                // 构建请求URL
                String urlString = "https://autoapp.liangyongqi.top/SessionLogin?SessionId=" + sessionId;
                URL url = new URL(urlString);

                // 创建连接
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // 发起请求并获取响应
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();

                // 处理响应数据，这里只是打印响应内容
                Log.d(TAG, "Response: " + response.toString());

                // 关闭连接
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}

