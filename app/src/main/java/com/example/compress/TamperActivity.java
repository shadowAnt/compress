package com.example.compress;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.compress.util.Tamper;

import pl.droidsonroids.gif.GifImageView;

public class TamperActivity extends AppCompatActivity implements CardView.OnClickListener{

    GifImageView attackImage;
    GifImageView attackResultImage;
    Button start;
    Button goback;
    Button skipButton;
    Button next;
    TextView resultText;
    Bitmap resultBitmap;
    int originWidth;
    int originHight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tamper);
        resultBitmap = getIntent().getParcelableExtra("resultBitmap");
        originWidth = getIntent().getIntExtra("originWidth", 0);
        originHight = getIntent().getIntExtra("originHight", 0);

        attackImage = (GifImageView) findViewById(R.id.attack);
        attackResultImage = (GifImageView) findViewById(R.id.attackResult);
        start = (Button) findViewById(R.id.startButton_tamper);
        resultText = (TextView) findViewById(R.id.authenticText_tamper);
        next = (Button) findViewById(R.id.nextButton_tamper);
        goback = (Button) findViewById(R.id.quitButton_tamper);
        skipButton = (Button) findViewById(R.id.skip);
        skipButton.setOnClickListener(this);
        start.setOnClickListener(this);
        next.setOnClickListener(this);
        goback.setOnClickListener(this);
        attackImage.setImageBitmap(resultBitmap);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.skip:
                Intent intent1 = new Intent(this, DecodeActivity.class);
                intent1.putExtra("resultBitmap", resultBitmap);
                intent1.putExtra("originHight", originHight);
                intent1.putExtra("originWidth", originWidth);
                startActivity(intent1);
                break;
            case R.id.startButton_tamper:
                //TODO 开始篡改
                resultBitmap = Tamper.tamper(resultBitmap, 121, 140, 121, 140, 255);
                resultBitmap = Tamper.tamper(resultBitmap, 1, 20, 1, 20, 0);
                resultBitmap = Tamper.tamper(resultBitmap, 237, 256, 1, 20, 0);
                resultBitmap = Tamper.tamper(resultBitmap, 1, 20, 237, 256, 0);
                resultBitmap = Tamper.tamper(resultBitmap, 237, 256, 237, 256, 0);
                attackResultImage.setImageBitmap(resultBitmap);
                skipButton.setEnabled(false);
                break;
            case R.id.nextButton_tamper:
                //TODO 下一步开始还原
                Intent intent2 = new Intent(this, DecodeActivity.class);
                intent2.putExtra("resultBitmap", resultBitmap);
                intent2.putExtra("originHight", originHight);
                intent2.putExtra("originWidth", originWidth);
                startActivity(intent2);
                break;
            case R.id.quitButton_tamper:
                //TODO 返回上一步
                finish();
                break;
            default:
                break;
        }
    }
}
