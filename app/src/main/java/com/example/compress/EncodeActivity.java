package com.example.compress;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.compress.util.GetCompressionRatio;
import com.example.compress.util.Joint_en;
import com.example.compress.util.Tamper;

import java.io.IOException;
import java.io.InputStream;

import pl.droidsonroids.gif.GifImageView;

public class EncodeActivity extends AppCompatActivity implements CardView.OnClickListener {

    Button choose;
    Button start;
    Button quit;
    Button next;
    ImageView originImage;
    GifImageView resultImage;
    TextView resultText;
    Bitmap enAuthenticationBitmap = null;
    int[][][] encodeBinaryArray = null;
    Bitmap originBitmap;
    Bitmap resultBitmap;
    InputStream is;
    int m = 4;
    int n = 4;
    String originUrl = "lena.bmp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encode);
        enAuthenticationBitmap = getIntent().getParcelableExtra("enAuthenticationBitmap");
        encodeBinaryArray = To.BitmapToArray(enAuthenticationBitmap);

        choose = (Button) findViewById(R.id.changeButton_encode);
        choose.setEnabled(false);
        start = (Button) findViewById(R.id.startButton_encode);
        quit = (Button) findViewById(R.id.quitButton_encode);
        next = (Button) findViewById(R.id.nextButton_encode);
        originImage = (ImageView) findViewById(R.id.originImage);
        resultImage = (GifImageView) findViewById(R.id.encodeResult);
        resultText = (TextView) findViewById(R.id.authenticText_encode);
        choose.setOnClickListener(this);
        start.setOnClickListener(this);
        quit.setOnClickListener(this);
        next.setOnClickListener(this);
        originBitmap = loadAnyImage(originUrl);
        originImage.setImageBitmap(originBitmap);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.changeButton_encode:
                //TODO 更改图像
//                Intent intent = new Intent(this, .class);
//                startActivityForResult(intent, 1);
                break;
            case R.id.startButton_encode:
                double[] key = {0.78, 3.59, Math.pow(7, 5), 0, Math.pow(2, 31) - 1, 102};
                int[][][] originArray= To.BitmapToArray(originBitmap);
                int[][][] resultArray = To.En(originArray, m, n, encodeBinaryArray, key);//(int) Math.ceil(height / m) * 2;
                resultBitmap = To.ArraytoBitmap(resultArray);
                resultImage.setImageBitmap(resultBitmap);





//                //TODO 开始加密 get originBitmap
//                long startMili = System.currentTimeMillis();// 当前时间对应的毫秒数
//                String resultString = "";
//                resultBitmap = Joint_en.joint_en(originBitmap, m, n, enAuthenticationBitmap, key);
//                long endMili = System.currentTimeMillis();
//                resultString += ("总耗时为：" + (endMili - startMili) + "毫秒" + "\n");
//                resultString += ("\n压缩率：" + GetCompressionRatio.getCompressionRatio(originBitmap, resultBitmap)) + "%\n";
//                resultText.setText(resultString);
//                resultImage.setImageBitmap(resultBitmap);
//                Log.e("tag----------", "对载体图像的处理");
                break;
            case R.id.quitButton_encode:
                //TODO 退出
                finish();
                break;
            case R.id.nextButton_encode:
                //TODO 下一步
                if (resultBitmap == null) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setTitle("错误！");
                    dialog.setMessage("还未进行压缩加密认证处理！");
                    dialog.setCancelable(false);
                    dialog.setNegativeButton("明白", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    dialog.show();
                    break;
                }
                int originHight = originBitmap.getHeight();
                int originWidth = originBitmap.getWidth();
                Intent intent1 = new Intent(this, TamperActivity.class);
                intent1.putExtra("resultBitmap", resultBitmap);
                intent1.putExtra("originHight", originHight);
                intent1.putExtra("originWidth", originWidth);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case 1:
                originBitmap = data.getParcelableExtra("changeBitmap");
                originImage.setImageBitmap(originBitmap);
                break;
        }
    }

}
