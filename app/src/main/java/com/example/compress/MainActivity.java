package com.example.compress;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.compress.util.ConvertGreyImg;
import com.example.compress.util.RGB2Grey;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    ImageView originImage;
    InputStream is;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        originImage = (ImageView) findViewById(R.id.origin);
        try {
            is = getAssets().open("lena.bmp");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        Bitmap bitmapResult = ConvertGreyImg.convertGreyImg(bitmap);
        originImage.setImageBitmap(bitmapResult);
        show(bitmapResult);
    }

    public void show(Bitmap bitmap) {
        int width = bitmap.getWidth();         //获取位图的宽
        int height = bitmap.getHeight();       //获取位图的高
        int ans = width * height;
        int[] pixels = new int[ans]; //通过位图的大小创建像素点数组
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int temp = RGB2Grey.RGB2Grey(pixels, width, i, j);
                System.out.println(temp);
            }
        }
        System.out.println(width + "  " + height + "  " + ans);
    }
}

