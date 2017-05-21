package com.example.compress;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import net.lemonsoft.lemonhello.LemonHello;
import net.lemonsoft.lemonhello.LemonHelloAction;
import net.lemonsoft.lemonhello.LemonHelloInfo;
import net.lemonsoft.lemonhello.LemonHelloView;
import net.lemonsoft.lemonhello.interfaces.LemonHelloActionDelegate;

import info.hoang8f.widget.FButton;

public class ChooseActivity extends AppCompatActivity implements CardView.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO 顶部融为一体
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_choose);
        //TODO 加载ToolBar 设置标题及字体颜色
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("压缩加密认证联合编码系统");
        toolbar.setTitleTextColor(Color.parseColor("#ecf0f1"));
        setSupportActionBar(toolbar);

        CardView enButton = (CardView) findViewById(R.id.en);
        CardView deButton = (CardView) findViewById(R.id.de);
        FButton tamperButton = (FButton) findViewById(R.id.tamper);
        enButton.setOnClickListener(this);
        deButton.setOnClickListener(this);
        tamperButton.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.aboutme:
                // TODO 弹出对话框显示内容
                LemonHello.getSuccessHello("关于", "制作人: 唐聪 牛雪静 \n全国大学生信息安全竞赛 \n个人主页：shadowant.top")
                        .addAction(new LemonHelloAction("我知道啦", new LemonHelloActionDelegate() {
                            @Override
                            public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                                helloView.hide();
                            }
                        }))
                        .show(this);
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);//加载menu文件到布局
        return true;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.en:
                //TODO 认证
                Intent intent1 = new Intent(this, AuthenticActivity.class);
                startActivity(intent1);
                break;
            case R.id.tamper:
                //TODO 篡改
                LemonHello.getInformationHello("选择", "传输过程是否要模拟篡改？")
                        .addAction(new LemonHelloAction("安全传输", new LemonHelloActionDelegate() {
                            @Override
                            public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                                helloView.hide();
                            }
                        }))
                        .addAction(new LemonHelloAction("篡改", Color.RED, new LemonHelloActionDelegate() {
                            @Override
                            public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                                helloView.hide();
                            }
                        }))
                        .addAction(new LemonHelloAction("取消", Color.BLACK, new LemonHelloActionDelegate() {
                            @Override
                            public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                                helloView.hide();
                            }
                        }))
                        .show(this);
                break;
            case R.id.de:
                //TODO 解密
                break;
            default:
                break;
        }
    }
}
