// GetUser.java
package com.example.autolink031;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetUser {

    private static final String SERVER_URL = "http://192.168.0.121/api/getUsername";

    public interface GetUserCallback {
        void onUsernameReceived(String username);

        void onError(String errorMessage);
    }

    public static void getUsername(Context context, GetUserCallback callback) {
        String uuid = GetUuid.getAndroidId(context);
        String requestUrl = SERVER_URL + "?uuid=" + uuid;
        System.out.println(requestUrl);
        // 异步任务发送网络请求
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    // 设置请求方法为 GET
                    connection.setRequestMethod("GET");

                    // 获取服务器响应的输入流
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    // 关闭连接和输入流
                    reader.close();
                    connection.disconnect();

                    return response.toString();
                } catch (IOException e) {
                    Log.e("GetUser", "Error during network request: " + e.getMessage());
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String response) {
                if (response != null) {
                    try {
                        // 解析 JSON 响应，提取用户名
                        String username = parseUsername(response);
                        callback.onUsernameReceived(username);
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onError("Error parsing server response");
                    }
                } else {
                    callback.onError("Failed to connect to the server");
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, requestUrl);

    }

    private static String parseUsername(String json) throws Exception {
        try {
            // 创建 JSONObject 对象
            JSONObject jsonObject = new JSONObject(json);

            // 直接从 JSON 对象中获取 "username" 字段的值并返回
            return jsonObject.getString("username");
        } catch (JSONException e) {
            // JSON 解析出错
            throw new Exception("Failed to parse JSON", e);
        }
    }
}