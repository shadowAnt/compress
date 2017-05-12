package com.example.compress;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.compress.util.Authentication_codes;
import com.example.compress.util.ConvertGreyImg;
import com.example.compress.util.Joint_de;
import com.example.compress.util.PSNR;
import com.example.compress.util.Tamper;

import java.io.IOException;
import java.io.InputStream;

import pl.droidsonroids.gif.GifImageView;

public class DecodeActivity extends AppCompatActivity implements CardView.OnClickListener {

    GifImageView decodeImage;
    GifImageView decodeResultImage;
    GifImageView originImage;
    GifImageView whereImage;
    GifImageView authenticImage;
    Button start;
    Button overButton;
    TextView resultText;
    Bitmap resultBitmap;
    Bitmap originBitmap;
    InputStream is;
    ScrollView scrollView;
    String authenticationUrl = "ahu.bmp";
    int m = 4;
    int n = 4;
    String resultString;
    Bitmap[] resultBitmapArray;
    Bitmap[] restoreBitmap;//最终结果
    String originUrl = "peppers.bmp";
    int originWidth;
    int originHight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decode);
        resultBitmap = getIntent().getParcelableExtra("resultBitmap");
        originWidth = getIntent().getIntExtra("originWidth", 0);
        originHight = getIntent().getIntExtra("originHight", 0);

        authenticImage = (GifImageView) findViewById(R.id.authentic_decode);
        scrollView = (ScrollView) findViewById(R.id.scrollView_decode);
        scrollView.setVerticalScrollBarEnabled(false);
        decodeImage = (GifImageView) findViewById(R.id.decode);
        decodeResultImage = (GifImageView) findViewById(R.id.decodeResult);//结果
        originImage = (GifImageView) findViewById(R.id.originImage_decode);
        whereImage = (GifImageView) findViewById(R.id.where);
        start = (Button) findViewById(R.id.startButton_decode);
        overButton = (Button) findViewById(R.id.over);
        resultText = (TextView) findViewById(R.id.authenticText_decode);
        start.setOnClickListener(this);
        overButton.setOnClickListener(this);
        //加载初始图像
        decodeImage.setImageBitmap(resultBitmap);
        originBitmap = loadAnyImage(originUrl);
        originBitmap = ConvertGreyImg.convertGreyImg(originBitmap);
        originImage.setImageBitmap(originBitmap);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startButton_decode:
                Bitmap authenticationBitmap = loadAnyImage(authenticationUrl);
                authenticImage.setImageBitmap(authenticationBitmap);
                long startMili = System.currentTimeMillis();// 当前时间对应的毫秒数
                resultString = "";
                resultString += ("开始处理认证图像 " + startMili + "\n");
                Bitmap tempBitmap = ConvertGreyImg.convertGreyImg(authenticationBitmap);
                double[] key = {0.78, 3.59, Math.pow(7, 5), 0, Math.pow(2, 31) - 1, 102};
                resultBitmapArray = Authentication_codes.authentication_codes(tempBitmap, key);
                long endMili = System.currentTimeMillis();
                resultString += ("认证图像结束 " + endMili + "\n");
                resultString += ("认证图像总耗时为：" + (endMili - startMili) + " 毫秒" + "\n\n");
                resultText.setText(resultString);

                double[] key2 = {0.78, 3.59, Math.pow(7, 5), 0, Math.pow(2, 31) - 1, 102};
                startMili = System.currentTimeMillis();// 当前时间对应的毫秒数
                resultString += ("开始解密解压缩 " + startMili + "\n");
                restoreBitmap = Joint_de.joint_de(resultBitmap, originHight, originWidth, m, n, resultBitmapArray[0], key2);
                endMili = System.currentTimeMillis();
                resultString += ("解密解压缩图像结束 " + endMili + "\n");
                resultString += ("解密解压缩总耗时为：" + (endMili - startMili) + " 毫秒" + "\n\n");
                decodeResultImage.setImageBitmap(restoreBitmap[0]);
                java.text.DecimalFormat df = new java.text.DecimalFormat("#.0000");
                resultString += ("PSNR: " + df.format(PSNR.psnr(originBitmap, restoreBitmap[0])) + "\n");
                Log.e("tag----------", "对收到图像解码完成");
                //TODO 检测篡改位置
                whereImage.setImageBitmap(restoreBitmap[1]);
                resultText.setText(resultString);
                break;
            case R.id.over:
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
            default:
                break;
        }
    }

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
