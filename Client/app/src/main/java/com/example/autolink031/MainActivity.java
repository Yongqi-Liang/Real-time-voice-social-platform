// MainActivity.java
package com.example.autolink031;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.netease.lava.nertc.foreground.ForegroundKit;
import com.netease.lava.nertc.sdk.LastmileProbeResult;
import com.netease.lava.nertc.sdk.NERtcCallbackEx;
import com.netease.lava.nertc.sdk.NERtcConstants;
import com.netease.lava.nertc.sdk.NERtcEx;
import com.netease.lava.nertc.sdk.NERtcParameters;
import com.netease.lava.nertc.sdk.NERtcUserJoinExtraInfo;
import com.netease.lava.nertc.sdk.NERtcUserLeaveExtraInfo;
import com.netease.lava.nertc.sdk.stats.NERtcAudioVolumeInfo;
import com.netease.lava.nertc.sdk.video.NERtcRemoteVideoStreamType;
import com.netease.lava.nertc.sdk.video.NERtcVideoStreamType;
import com.netease.lava.nertc.sdk.video.NERtcVideoView;

import java.nio.ByteBuffer;

public class MainActivity extends AppCompatActivity {

    private static final String APP_KEY = "5111c5547e55d57d704adadc5ed444d7";
    private GetLocation getLocation;
    private static final int LOCATION_CODE = 301;
    private static final int LOCATION_UPDATE_INTERVAL = 300000; // 5000毫秒
    private final Handler handler = new Handler();
    WebView meet;


    private final Runnable locationUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            if (hasLocationPermission()) {
                System.out.println("有位置权限");
                handler.postDelayed(this, LOCATION_UPDATE_INTERVAL);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.WHITE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                View decor = window.getDecorView();
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }

        setContentView(R.layout.activity_main);

        String uuid = GetUuid.getAndroidId(getApplicationContext());

        if (uuid != null) {
            TextView uuidTextView = findViewById(R.id.uuidTextView);
            uuidTextView.setText("UUID: " + uuid);

            getUserInfo();

        } else {
            Log.e("MainActivity", "Error getting UUID");
        }

        getLocation = new GetLocation(this); // Initialize GetLocation instance

        if (!PermissionManager.hasUserAgreedToPrivacyPolicy(this)) {
            PermissionManager.showPrivacyPolicyDialog(this, new PermissionManager.PrivacyPolicyCallback() {
                @Override
                public void onPrivacyPolicyAgreed() throws Exception {
                    initializeApp();
                }
            });
        } else {
            try {
                initializeApp();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (hasLocationPermission()) {
            Log.d("MainActivity", "有位置权限，开始获取位置信息");
            getLocation.getLocation();
            handler.postDelayed(locationUpdateRunnable, LOCATION_UPDATE_INTERVAL);
        } else {
            requestLocationPermission();
        }
        // 获取 WebView 实例
        meet = findViewById(R.id.meet);
        Meet.createWebView(this,String.valueOf(getLocation.locationListener));
    }

    private boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                LOCATION_CODE);
    }

    private void initializeApp() throws Exception {
        PermissionManager.updatePrivacyShow(true);
        PermissionManager.updatePrivacyAgree(true);
    }

    private void getUserInfo() {
        GetUser.getUsername(getApplicationContext(), new GetUser.GetUserCallback() {
            @Override
            public void onUsernameReceived(String username) {
                runOnUiThread(() -> {
                    TextView textViewUsername = findViewById(R.id.Username);
                    if (username != null && !username.isEmpty()) {
                        textViewUsername.setText(username);
                    } else {
                        textViewUsername.setText(username);
                    }
                    Log.d("MainActivity", "getUserInfo callback, calling getLocation");
                });
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("MainActivity", "Error: " + errorMessage);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Log.d("MainActivity", "获取位置权限成功，开始获取位置信息");
                getLocation.getLocation();
            } else {
                Log.e("TAG", "权限被拒绝");
            }
        }
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getLocation.onDestroy();
    }
}
