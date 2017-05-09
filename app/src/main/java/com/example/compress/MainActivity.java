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
import com.example.compress.util.Matlab;
import com.example.compress.util.PSNR;
import com.example.compress.util.RGB2Grey;
import com.example.compress.util.Show;

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
    int m = 4;
    int n = 4;
    String authenticationUrl = "AHU.bmp";
    String originUrl = "lena.bmp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO 对认证图像的处理
        double[] key = {0.78, 3.59, Math.pow(7, 5), 0, Math.pow(2, 31) - 1, 102};
        authenticationImage = (ImageView) findViewById(R.id.authentication);
        enAuthenticationImage = (ImageView) findViewById(R.id.enAuthentication);
        Bitmap authenticationBitmap = loadAnyImage(authenticationUrl);
        authenticationImage.setImageBitmap(authenticationBitmap);
        Bitmap tempBitmap = ConvertGreyImg.convertGreyImg(authenticationBitmap);
        Bitmap EnAuthenticationBitmap = Authentication_codes.authentication_codes(tempBitmap, key);
        enAuthenticationImage.setImageBitmap(EnAuthenticationBitmap);
        Show.showBitmap(EnAuthenticationBitmap);
        Log.e("tag----------", "对认证图像的处理完成");

        //TODO 对载体图像的处理
        originImage = (ImageView) findViewById(R.id.origin);
        enOriginImage = (ImageView) findViewById(R.id.enOrigin);
        Bitmap originBitmap = loadAnyImage(originUrl);
        tempBitmap = ConvertGreyImg.convertGreyImg(originBitmap);//灰度处理
        originImage.setImageBitmap(tempBitmap);//
        Bitmap resultBitmap = Joint_en.joint_en(tempBitmap, m, n, EnAuthenticationBitmap, key);
        enOriginImage.setImageBitmap(resultBitmap);
        Log.e("tag----------", "对载体图像的处理");

        //TODO 对收到图像解码
        double[] key2 = {0.78, 3.59, Math.pow(7, 5), 0, Math.pow(2, 31) - 1, 102};
        receiveImage = (ImageView) findViewById(R.id.receive);
        receiveImage.setImageBitmap(resultBitmap);
        int originHight = originBitmap.getHeight();
        int originWidth = originBitmap.getWidth();
        Bitmap restoreBitmap = Joint_de.joint_de(resultBitmap, originHight, originWidth, m, n, EnAuthenticationBitmap, key2);
//        show(EnAuthenticationBitmap);// 0 255
        restoreImage = (ImageView) findViewById(R.id.restore);
        restoreImage.setImageBitmap(restoreBitmap);
        Log.e("tag----------", "对收到图像解码完成");
        Log.w("PSNR: ", PSNR.psnr(tempBitmap, restoreBitmap)+"");
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

}
