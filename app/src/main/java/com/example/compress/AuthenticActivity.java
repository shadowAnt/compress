package com.example.compress;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.compress.util.Authentication_codes;
import com.example.compress.util.ConvertGreyImg;

import net.lemonsoft.lemonhello.LemonHello;
import net.lemonsoft.lemonhello.LemonHelloAction;
import net.lemonsoft.lemonhello.LemonHelloInfo;
import net.lemonsoft.lemonhello.LemonHelloView;
import net.lemonsoft.lemonhello.interfaces.LemonHelloActionDelegate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
    public static final int CHOOSE_PHOTO = 1;
    Bitmap authenticationBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO 顶部融为一体
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_authentic);
        choose = (Button) findViewById(R.id.changeButton);
        choose.setOnClickListener(this);
//        choose.setEnabled(false);
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
        authenticationBitmap = loadAnyImage(authenticationUrl);
        authentic.setImageBitmap(authenticationBitmap);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startButton:
                //TODO 开始处理 初始化得到Bitmap
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
                //TODO 更换处理的认证图像 从图库中获取
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
                break;
            case R.id.quitButton:
                //TODO 结束退出
                finish();
                break;
            case R.id.nextButton:
                //TODO 传递EnAuthenticationBitmap[0]过去 待嵌入的认证图像
                if (EnAuthenticationBitmap == null) {
                    LemonHello.getErrorHello("发生错误", "还未对待嵌入的认证图像进行处理！")
                            .addAction(new LemonHelloAction("关闭", new LemonHelloActionDelegate() {
                                @Override
                                public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                                    helloView.hide();
                                }
                            }))
                            .show(this);
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

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    LemonHello.getErrorHello("发生错误", "拒绝了访问相册请求！")
                            .addAction(new LemonHelloAction("关闭", new LemonHelloActionDelegate() {
                                @Override
                                public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                                    helloView.hide();
                                }
                            }))
                            .show(this);
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnkitKat(data);
                    } else {
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnkitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                //解析成数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            authentic.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "获取图片失败", Toast.LENGTH_SHORT).show();
        }
    }
}
