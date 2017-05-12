package com.example.compress;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import pl.droidsonroids.gif.GifImageView;

public class HomeActivity extends AppCompatActivity implements CardView.OnClickListener {
    CardView cardView1Control;
    CardView cardView2Control;
    CardView cardView3Control;
    CardView cardView4Control;
    GifImageView aboutmeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        cardView1Control = (CardView) findViewById(R.id.cardView1);
        cardView2Control = (CardView) findViewById(R.id.cardView2);
        cardView3Control = (CardView) findViewById(R.id.cardView3);
        cardView4Control = (CardView) findViewById(R.id.cardView4);
        aboutmeImage = (GifImageView) findViewById(R.id.aboutme);
        cardView1Control.setOnClickListener(this);
        cardView2Control.setOnClickListener(this);
        cardView3Control.setOnClickListener(this);
        cardView4Control.setOnClickListener(this);
        aboutmeImage.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cardView1:
                //TODO 认证
                Intent intent1 = new Intent(this, AuthenticActivity.class);
                startActivity(intent1);
                break;
            case R.id.cardView2:
                //TODO 加密
                break;
            case R.id.cardView3:
                //TODO 篡改
                break;
            case R.id.cardView4:
                //TODO 解密
                break;
            case R.id.aboutme:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("关于");
                dialog.setMessage("制作人: 唐聪 牛雪静 \n项目：安徽大学2015科研训练计划 \n个人主页：shadowant.top");
                dialog.setCancelable(false);
                dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                dialog.show();
                break;
            default:
                break;
        }
    }
}
