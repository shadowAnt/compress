package com.example.compress;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bitmap lena = BitmapFactory.decodeResource(getResources(), R.drawable.lena);
        Bitmap peppers = BitmapFactory.decodeResource(getResources(), R.drawable.peppers);
        Bitmap AHU = BitmapFactory.decodeResource(getResources(), R.drawable.ahu);
        To.saveImageToGallery(this, lena);
        To.saveImageToGallery(this, peppers);
        To.saveImageToGallery(this, AHU);

        Intent intent = new Intent(this, ChooseActivity.class);
        startActivity(intent);
        finish();
    }
}
