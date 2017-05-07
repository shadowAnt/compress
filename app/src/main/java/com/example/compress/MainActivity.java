package com.example.compress;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.compress.util.Authentication_codes;
import com.example.compress.util.ConvertGreyImg;
import com.example.compress.util.Joint_de;
import com.example.compress.util.Joint_en;
import com.example.compress.util.RGB2Grey;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ImageView enAuthenticationImage;
    ImageView authenticationImage;
    ImageView originImage;
    ImageView enOriginImage;
    ImageView receiveImage;
    ImageView restoreImage;
    InputStream is;
    double[] key = {0.78, 3.59, Math.pow(7, 5), 0, Math.pow(2, 31) - 1, 102};
    int m = 4;
    int n = 4;
    String authenticationUrl = "AHU.bmp";
    String originUrl = "lena.bmp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO 对认证图像的处理
        authenticationImage = (ImageView) findViewById(R.id.authentication);
        enAuthenticationImage = (ImageView) findViewById(R.id.enAuthentication);
        Bitmap authenticationBitmap = loadAnyImage(authenticationUrl);
        authenticationImage.setImageBitmap(authenticationBitmap);
        Bitmap tempBitmap = ConvertGreyImg.convertGreyImg(authenticationBitmap);
        Bitmap EnAuthenticationBitmap = Authentication_codes.authentication_codes(tempBitmap, key);
        enAuthenticationImage.setImageBitmap(EnAuthenticationBitmap);

        //TODO 对载体图像的处理
        originImage = (ImageView) findViewById(R.id.origin);
        enOriginImage = (ImageView) findViewById(R.id.enOrigin);
        Bitmap originBitmap = loadAnyImage(originUrl);
        originImage.setImageBitmap(originBitmap);
        tempBitmap = ConvertGreyImg.convertGreyImg(originBitmap);
        Bitmap resultBitmap = Joint_en.joint_en(tempBitmap, m, n, EnAuthenticationBitmap, key);
        enOriginImage.setImageBitmap(resultBitmap);

        //TODO 收到图像解码
        receiveImage = (ImageView) findViewById(R.id.receive);
        receiveImage.setImageBitmap(resultBitmap);
        Bitmap restoreBitmap = Joint_de.joint_de(resultBitmap, m, n, EnAuthenticationBitmap, key);
        restoreImage = (ImageView) findViewById(R.id.restore);
        restoreImage.setImageBitmap(restoreBitmap);
    }

    public Bitmap loadAnyImage(String url) {
        try {
            //is = getAssets().open("lena.bmp");
            is = getAssets().open(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return bitmap;
    }

    /**
     * rgb->grey
     */
    public void show(Bitmap bitmap) {
        int width = bitmap.getWidth();         //获取位图的宽
        int height = bitmap.getHeight();       //获取位图的高
        int ans = width * height;
        //512  512  262144
        int[] pixels = new int[ans]; //通过位图的大小创建像素点数组
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int temp = RGB2Grey.RGB2Grey(pixels, width, i, j);
                System.out.println(temp);
            }
        }
        System.out.println("width: " + width + "  height:  " + height);
    }
}
