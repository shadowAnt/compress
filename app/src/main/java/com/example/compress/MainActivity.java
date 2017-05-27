package com.example.compress;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.compress.util.Decryption;
import com.example.compress.util.Encryption;
import com.example.compress.util.My_bin2dec;
import com.example.compress.util.My_dec2bin;
import com.example.compress.util.Rand_numbers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bitmap lena = BitmapFactory.decodeResource(getResources(), R.drawable.lena);
        Bitmap peppers = BitmapFactory.decodeResource(getResources(), R.drawable.peppers);
        Bitmap AHU = BitmapFactory.decodeResource(getResources(), R.drawable.ahu);
        Bitmap boat = BitmapFactory.decodeResource(getResources(), R.drawable.boat);
        Bitmap tiffany = BitmapFactory.decodeResource(getResources(), R.drawable.tiffany);
        Bitmap jet = BitmapFactory.decodeResource(getResources(), R.drawable.jet);
        Bitmap splash = BitmapFactory.decodeResource(getResources(), R.drawable.splash);
        To.saveImageToGallery(this, lena, "lena", 0);
        To.saveImageToGallery(this, peppers, "peppers", 0);
        To.saveImageToGallery(this, AHU, "AHU", 0);
        To.saveImageToGallery(this, boat, "boat", 0);
        To.saveImageToGallery(this, tiffany, "tiffany", 0);
        To.saveImageToGallery(this, jet, "jet", 0);
        To.saveImageToGallery(this, splash, "splash", 0);
        Intent intent = new Intent(this, ChooseActivity.class);
        startActivity(intent);
        finish();

//        TextView textView = (TextView) findViewById(R.id.test);
//        String s = "";
//        double[] a = {0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 0, 0};
//        double[] b = My_bin2dec.my_bin2dec(a, 8);
//
//        textView.setText(b[0] + " " + b[1]);//96 152    192 49
//        System.out.println(b[0] + " " + b[1]);
//        double[] c = My_dec2bin.my_dec2bin(b, 8);
//        for (int i = 0; i < c.length; i++) {
//            s += c[i] + " ";
//        }
//        textView.setText(s);

//        String s = "";
//        double[][] d = new double[5][5];
//        double index = 0;
//        for (int i = 0; i < 5; i++) {
//            for (int j = 0; j < 5; j++) {
//                d[i][j] = index;
//                index++;
//                s += (int) d[i][j] + "  ";
//            }
//            s += "\n";
//        }
//        s += "\n";
//
//        double[][] e = To.im2col(d, 4, 4);
//        int height = e.length;
//        int width = e[0].length;
//
//        for (int i = 0; i < height; i++) {
//            for (int j = 0; j < width; j++) {
//                s += (int) e[i][j] + "  ";
//            }
//            s += "\n";
//        }
//        s += "\n";
//
//        double[][] f = To.col2im(e, 4, 4, 5, 5);
//        int heightf = f.length;
//        int widthf = f[0].length;
//
//        for (int i = 0; i < heightf; i++) {
//            for (int j = 0; j < widthf; j++) {
//                s += (int) f[i][j] + "  ";
//            }
//            s += "\n";
//        }
//
//        double[] dec = {12.0, 240.0};
//        double[] key = {0.78, 3.59, Math.pow(7, 5), 0, Math.pow(2, 31) - 1, 102};
//        double[] group4 = Encryption.encryption(0, 255, dec, key);
//        int len = group4.length;
//        for (int i = 0; i < len; i++) {
//            s += group4[i] + " ";
//        }
//        s += "\n\n";
//
//        double[] dec1 = new double[2];
//        dec1[0] = group4[2];
//        dec1[1] = group4[3];
//        double[] group4De = Decryption.decrption(group4[0], group4[1], dec1, key);
//        for (int i = 0; i < len; i++) {
//            s += group4De[i] + " ";
//        }
//        s += "\n\n";
//        double[] xor_data = Rand_numbers.Rand_numbers(key, 3, 256);
//        for(int i=0; i<3; i++){
//            s +=  xor_data[i]+ "   ";
//        }
//        s += "\n\n";
//        xor_data = Rand_numbers.Rand_numbers(key, 3, 256);
//        for(int i=0; i<3; i++){
//            s +=  xor_data[i]+ "   ";
//        }
//        s += "\n\n";
//        textView.setText(s);
    }
}
