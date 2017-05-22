package com.example.compress;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.apkfuns.xprogressdialog.XProgressDialog;
import com.example.compress.util.Authentication_codes;
import com.example.compress.util.ConvertGreyImg;
import com.example.compress.util.Joint_de;
import com.example.compress.util.PSNR;
import com.example.compress.util.Tamper;
import com.readystatesoftware.systembartint.SystemBarTintManager;

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
    String originUrl = "lena.bmp";
    int m = 4;
    int n = 4;
    int originWidth;
    int originHight;
    Bitmap authenticationBitmap;
    private Handler handler = new Handler();
    XProgressDialog dialog;
    Bitmap icBitmap;
    Bitmap ic2Bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_decode);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);

        GlobalVaries globalVaries = (GlobalVaries) getApplication();
        originHight = globalVaries.getOriginHeight();
        originWidth = globalVaries.getOriginWidth();
        resultBitmap = globalVaries.getTamperResultBitmap();

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
        originImage.setImageBitmap(originBitmap);
        authenticationBitmap = loadAnyImage(authenticationUrl);
        authenticImage.setImageBitmap(authenticationBitmap);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startButton_decode:
                dialog = new XProgressDialog(this, "正在处理图像...", XProgressDialog.THEME_CIRCLE_PROGRESS);
                dialog.show();
                new Thread() {
                    public void run() {
                        double[] key = {0.78, 3.59, Math.pow(7, 5), 0, Math.pow(2, 31) - 1, 102};
                        //TODO 处理认证图像
                        int[][][] threeArray = To.BitmapToArray(authenticationBitmap);//原始图像的三位数组
                        int[][][] binaryArray = To.RGBtoBinary(threeArray);//二值化后的三位数组
                        int[][][] encodeBinaryArray = To.EncodeBinaryArray(binaryArray, key);//加密后的认证图像三维数组
                        int[][][] resultArray = To.BitmapToArray(resultBitmap);
                        //TODO 解密
                        int[][][][] ans = To.De(resultArray, originHight, originWidth, m, n, encodeBinaryArray, key);
                        int[][][] Ic = ans[0];
                        int[][][] Ic2 = ans[1];
                        icBitmap = To.ArraytoBitmap(Ic);
                        ic2Bitmap = To.ArraytoBitmap(Ic2);
                        handler.post(new Runnable() {    // 在新线程中使用Handler向主线程发送一段代码, 主线程自动执行run()方法
                            public void run() {
                                decodeResultImage.setImageBitmap(icBitmap);
                                whereImage.setImageBitmap(ic2Bitmap);
                                dialog.dismiss();
                            }
                        });
                    }
                }.start();
//                restoreBitmap = Joint_de.joint_de(resultBitmap, originHight, originWidth, m, n, resultBitmapArray[0], key2);
//                decodeResultImage.setImageBitmap(restoreBitmap[0]);
//                java.text.DecimalFormat df = new java.text.DecimalFormat("#.0000");
//                resultString += ("PSNR: " + df.format(PSNR.psnr(originBitmap, restoreBitmap[0])) + "\n");
                break;
            case R.id.over:
                Intent intent = new Intent(this, ChooseActivity.class);
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
