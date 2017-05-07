package com.example.compress;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.compress.util.Authentication_codes;
import com.example.compress.util.ConvertGreyImg;
import com.example.compress.util.RGB2Grey;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ImageView enAuthenticationImage;
    ImageView authenticationImage;
    InputStream is;
    double[] key = {0.78, 3.59, Math.pow(7, 5), 0, Math.pow(2, 31) - 1, 102};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authenticationImage = (ImageView) findViewById(R.id.authentication);
        enAuthenticationImage = (ImageView) findViewById(R.id.enAuthentication);
        String authenticationUrl = "lena.bmp";
        Bitmap authenticationBitmap = loadAnyImage(authenticationUrl);
        authenticationImage.setImageBitmap(authenticationBitmap);
        Bitmap EnAuthenticationBitmap = authenticate(authenticationUrl);
        enAuthenticationImage.setImageBitmap(EnAuthenticationBitmap);
//        show(bitmap);
    }

    public Bitmap loadAnyImage(String url){
        try {
            //is = getAssets().open("lena.bmp");
            is = getAssets().open(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return bitmap;
    }

    public Bitmap authenticate(String url){
        try {
            //is = getAssets().open("lena.bmp");
            is = getAssets().open(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        bitmap = ConvertGreyImg.convertGreyImg(bitmap);
        //TODO 生成待加入的认证信息
        bitmap = Authentication_codes.authentication_codes(bitmap, key);
        return bitmap;
    }

    /**
    * rgb->grey
    *
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
        System.out.println("width: " + width+"  height:  "+height);
    }
}

