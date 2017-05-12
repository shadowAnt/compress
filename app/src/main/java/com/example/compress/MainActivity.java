package com.example.compress;

import android.content.Intent;
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
import com.example.compress.util.PSNR;
import com.example.compress.util.Show;
import com.example.compress.util.Tamper;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    ImageView enAuthenticationImage;
    ImageView authenticationImage;
    ImageView twoDataAuthenticationImage;
    ImageView originImage;
    ImageView enOriginImage;
    ImageView receiveImage;
    ImageView restoreImage;
    ImageView findWhereImage;
    InputStream is;
    int m = 4;
    int n = 4;
    //    String authenticationUrl = "ahu.bmp";
    String authenticationUrl = "24.bmp";
    //    String originUrl = "24.bmp";
//    String originUrl = "lena.bmp";
    String originUrl = "peppers.bmp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, HomeActivity.class);
//        Intent intent = new Intent(this, AuthenticActivity.class);
        startActivity(intent);

        if (false) {
            //TODO 对认证图像的处理
            double[] key = {0.78, 3.59, Math.pow(7, 5), 0, Math.pow(2, 31) - 1, 102};
            authenticationImage = (ImageView) findViewById(R.id.authentication);
            enAuthenticationImage = (ImageView) findViewById(R.id.enAuthentication);
            twoDataAuthenticationImage = (ImageView) findViewById(R.id.twoDataAuthentication);
            originImage = (ImageView) findViewById(R.id.origin);
            enOriginImage = (ImageView) findViewById(R.id.enOrigin);
            receiveImage = (ImageView) findViewById(R.id.receive);
            restoreImage = (ImageView) findViewById(R.id.restore);

            Bitmap authenticationBitmap = loadAnyImage(authenticationUrl);
            authenticationImage.setImageBitmap(authenticationBitmap);
            //灰度
            Bitmap tempBitmap = ConvertGreyImg.convertGreyImg(authenticationBitmap);
            Bitmap[] EnAuthenticationBitmap = Authentication_codes.authentication_codes(tempBitmap, key);
            twoDataAuthenticationImage.setImageBitmap(EnAuthenticationBitmap[1]);
            enAuthenticationImage.setImageBitmap(EnAuthenticationBitmap[0]);
            Show.showBitmap(EnAuthenticationBitmap[0]);
            Log.e("tag----------", "对认证图像的处理完成");

            //TODO 对载体图像的处理

            Bitmap originBitmap = loadAnyImage(originUrl);
            tempBitmap = ConvertGreyImg.convertGreyImg(originBitmap);//灰度处理
            originImage.setImageBitmap(tempBitmap);
            Bitmap resultBitmap = Joint_en.joint_en(tempBitmap, m, n, EnAuthenticationBitmap[0], key);
            enOriginImage.setImageBitmap(resultBitmap);
            Log.e("tag----------", "对载体图像的处理");

            //TODO 对加密后的图像进行篡改
            if (false) {
                resultBitmap = Tamper.tamper(resultBitmap, 121, 140, 121, 140, 255);
                resultBitmap = Tamper.tamper(resultBitmap, 1, 20, 1, 20, 0);
                resultBitmap = Tamper.tamper(resultBitmap, 237, 256, 1, 20, 0);
                resultBitmap = Tamper.tamper(resultBitmap, 1, 20, 237, 256, 0);
                resultBitmap = Tamper.tamper(resultBitmap, 237, 256, 237, 256, 0);
            }

            //TODO 对收到图像解码
            double[] key2 = {0.78, 3.59, Math.pow(7, 5), 0, Math.pow(2, 31) - 1, 102};
            receiveImage.setImageBitmap(resultBitmap);
            int originHight = originBitmap.getHeight();
            int originWidth = originBitmap.getWidth();
            Bitmap[] restoreBitmap = Joint_de.joint_de(resultBitmap, originHight, originWidth, m, n, EnAuthenticationBitmap[0], key2);
            restoreImage.setImageBitmap(restoreBitmap[0]);
            Log.e("PSNR: ", PSNR.psnr(tempBitmap, restoreBitmap[0]) + "");
            Log.e("tag----------", "对收到图像解码完成");
            //TODO 检测篡改位置
            findWhereImage = (ImageView) findViewById(R.id.findWhere);
            findWhereImage.setImageBitmap(restoreBitmap[1]);
        }

    }

    /**
     * 从asset中读取图片
     *
     * @param url
     * @return以Bitmap形式返回
     */
    public Bitmap loadAnyImage(String url) {
        try {
            is = getAssets().open(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return bitmap;
    }

}
