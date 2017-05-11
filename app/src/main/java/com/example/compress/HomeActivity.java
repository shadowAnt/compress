package com.example.compress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class HomeActivity extends AppCompatActivity implements CardView.OnClickListener {
    CardView cardView1Control;
    CardView cardView2Control;
    CardView cardView3Control;
    CardView cardView4Control;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        cardView1Control = (CardView) findViewById(R.id.cardView1);
        cardView2Control = (CardView) findViewById(R.id.cardView2);
        cardView3Control = (CardView) findViewById(R.id.cardView3);
        cardView4Control = (CardView) findViewById(R.id.cardView4);
        cardView1Control.setOnClickListener(this);
        cardView2Control.setOnClickListener(this);
        cardView3Control.setOnClickListener(this);
        cardView4Control.setOnClickListener(this);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cardView1:
                //TODO 认证
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
            default:
                break;
        }
    }
}
