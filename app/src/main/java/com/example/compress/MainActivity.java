package com.example.compress;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity{

    ImageView originImage;
    InputStream is;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        originImage = (ImageView) findViewById(R.id.origin);
        try {
            is=getAssets().open("peppers.bmp");
        } catch (IOException e){
            e.printStackTrace();
        }
        Bitmap bitmap=BitmapFactory.decodeStream(is);
        originImage.setImageBitmap(bitmap);

    }
}
