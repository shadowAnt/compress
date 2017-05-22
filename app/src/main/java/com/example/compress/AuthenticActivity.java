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
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import android.widget.Toast;

import com.apkfuns.xprogressdialog.XProgressDialog;
import com.example.compress.util.Authentication_codes;
import com.example.compress.util.ConvertGreyImg;
import com.readystatesoftware.systembartint.SystemBarTintManager;

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

    Bitmap bitmap;
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
    String resultString = "";
    double[] key = {0.78, 3.59, Math.pow(7, 5), 0, Math.pow(2, 31) - 1, 102};
    public static final int CHOOSE_PHOTO = 1;
    Bitmap authenticationBitmap;
    int[][][] encodeBinaryArray = null;
    Bitmap encodeBinaryBitmap;
    private Handler handler = new Handler();
    XProgressDialog dialog;
    Bitmap binaryBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_authentic);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);

        choose = (Button) findViewById(R.id.changeButton);
        choose.setOnClickListener(this);
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
        bitmap = authenticationBitmap;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startButton:
                dialog = new XProgressDialog(this, "正在处理图像...", XProgressDialog.THEME_CIRCLE_PROGRESS);
                dialog.show();
                new Thread() {
                    public void run() {
                        long startMili = System.currentTimeMillis();// 当前时间对应的毫秒数

                        int[][][] threeArray = To.BitmapToArray(bitmap);//原始图像的三位数组
                        int[][][] binaryArray = To.RGBtoBinary(threeArray);//二值化后的三位数组
                        binaryBitmap = To.ArraytoBitmap(binaryArray);//二值化后的Bitmap

                        encodeBinaryArray = To.EncodeBinaryArray(binaryArray, key);//加密后的认证图像三维数组
                        encodeBinaryBitmap = To.ArraytoBitmap(encodeBinaryArray);//加密后的Bitmap

                        long endMili = System.currentTimeMillis();
                        resultString += ("总耗时为：" + (endMili - startMili) + "毫秒" + "\n");
                        handler.post(new Runnable() {    // 在新线程中使用Handler向主线程发送一段代码, 主线程自动执行run()方法
                            public void run() {
                                resultText.setText(resultString);
                                twoDataAuthenticImage.setImageBitmap(binaryBitmap);
                                authenticResultImage.setImageBitmap(encodeBinaryBitmap);
                                GlobalVaries globalVaries = (GlobalVaries) getApplication();
                                globalVaries.setEncodeBinaryBitmap(encodeBinaryBitmap);
                                dialog.dismiss();
                            }
                        });
                    }
                }.start();
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
                //TODO 传递待嵌入的认证图像过去
                if (encodeBinaryArray == null) {
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
                startActivity(intent1);
                finish();
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
            bitmap = BitmapFactory.decodeFile(imagePath);
            resultString = "";
            resultString += "认证图像位置:    " + imagePath + "\n";
            authentic.setImageBitmap(bitmap);
            long fileSize = new File(imagePath).length();
            java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
            String fileSizeString = df.format(fileSize / 1024.00);
            resultString += "认证图像大小： " + fileSizeString + " kb\n";
            resultText.setText(resultString);
        } else {
            Toast.makeText(this, "获取图片失败", Toast.LENGTH_SHORT).show();
        }
    }
}
