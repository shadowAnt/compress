package com.example.compress;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.compress.util.Authentication_codes;
import com.example.compress.util.ConvertGreyImg;

import java.io.IOException;
import java.io.InputStream;

import pl.droidsonroids.gif.GifImageView;

public class AuthenticActivity extends AppCompatActivity implements CardView.OnClickListener {

    Button choose;
    Button start;
    Button quit;
    Button next;
    ScrollView scrollView;
    ImageView authentic;
    GifImageView twoDataAuthenticImage;
    GifImageView authenticResultImage;
    TextView resultText;
    InputStream is;
    String authenticationUrl = "ahu.bmp";
    String resultString;
    double[] key = {0.78, 3.59, Math.pow(7, 5), 0, Math.pow(2, 31) - 1, 102};
    Bitmap[] EnAuthenticationBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentic);
        choose = (Button) findViewById(R.id.changeButton);
        choose.setEnabled(false);
        start = (Button) findViewById(R.id.startButton);
        authentic = (ImageView) findViewById(R.id.authenticImage);
        scrollView = (ScrollView) findViewById(R.id.scrollAuthentic);
        scrollView.setVerticalScrollBarEnabled(false);
        twoDataAuthenticImage = (GifImageView) findViewById(R.id.twoDataAuthenticationImage);
        authenticResultImage = (GifImageView) findViewById(R.id.authenticResult);
        resultText = (TextView) findViewById(R.id.authenticText);
        quit = (Button) findViewById(R.id.quitButton);
        next = (Button) findViewById(R.id.nextButton);
        start.setOnClickListener(this);
        quit.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startButton:
                //TODO 开始处理 初始化得到Bitmap
                Bitmap authenticationBitmap = loadAnyImage(authenticationUrl);
                authentic.setImageBitmap(authenticationBitmap);
                long startMili = System.currentTimeMillis();// 当前时间对应的毫秒数
                resultString = "";
                resultString += ("开始 " + startMili + "\n");
                Bitmap tempBitmap = ConvertGreyImg.convertGreyImg(authenticationBitmap);
                EnAuthenticationBitmap = Authentication_codes.authentication_codes(tempBitmap, key);
                authenticResultImage.setImageBitmap(EnAuthenticationBitmap[0]);
                twoDataAuthenticImage.setImageBitmap(EnAuthenticationBitmap[1]);
                long endMili = System.currentTimeMillis();
                resultString += ("结束 " + endMili + "\n");
                resultString += ("总耗时为：" + (endMili - startMili) + "毫秒" + "\n");
                resultText.setText(resultString);
                break;
            case R.id.changeButton:
                //TODO 更换处理的认证图像
                break;
            case R.id.quitButton:
                finish();
                break;
            case R.id.nextButton:
                //TODO 传递EnAuthenticationBitmap[0]过去 待嵌入的认证图像
                if (EnAuthenticationBitmap == null) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setTitle("错误！");
                    dialog.setMessage("还未对待嵌入的认证图像进行处理！");
                    dialog.setCancelable(false);
                    dialog.setNegativeButton("明白", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    dialog.show();
                    break;
                }
                Intent intent1 = new Intent(this, EncodeActivity.class);
                intent1.putExtra("enAuthenticationBitmap", EnAuthenticationBitmap[0]);
                startActivity(intent1);
                break;
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
