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

        Intent intent = new Intent(this, ChooseActivity.class);
//        Intent intent = new Intent(this, AuthenticActivity.class);
        startActivity(intent);
        finish();

//        ImageView image1 = (ImageView) findViewById(R.id.image1);
//        ImageView image2 = (ImageView) findViewById(R.id.image2);
//        ImageView image3 = (ImageView) findViewById(R.id.image3);
//        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.lena);
//        int[][][] threeArray = To.BitmapToArray(bitmap1);
//        Bitmap bitmap2 = To.ArraytoBitmap(threeArray);
//        int[][][] binaryArray = To.RGBtoBinary(threeArray);
//        Bitmap bitmap3 = To.ArraytoBitmap(binaryArray);
//        image1.setImageBitmap(bitmap1);
//        image2.setImageBitmap(bitmap2);
//        image3.setImageBitmap(bitmap3);
    }
}
