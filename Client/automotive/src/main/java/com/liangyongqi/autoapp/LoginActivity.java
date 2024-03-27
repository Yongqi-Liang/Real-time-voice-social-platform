package com.liangyongqi.autoapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import com.liangyongqi.autoapp.util.ConfigurationManager;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * File: LoginActivity.java
 * Author: liangyongqi
 * Date: 2024/3/13
 * Time: 16:44
 */
public class LoginActivity extends AppCompatActivity {
    ImageView qrCodeImageView;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Context context = this;
        String uuid = ConfigurationManager.getUUID(context);
        // 设置启动页布局
        setContentView(R.layout.activity_login);
        // 创建二维码生成
        qrCodeImageView = findViewById(R.id.qr_code_image_view);
        // 此处是完整的登录url，将转换为二维码供手机扫描登录
        String loginurl = "https://122.9.46.63/login?uuid=" + uuid;
        try {
            Bitmap bitmap = encodeAsBitmap(loginurl);
            qrCodeImageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
    private Bitmap encodeAsBitmap(String text) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 400, 400);

        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bitmap.setPixel(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }

        return bitmap;
    }
}
