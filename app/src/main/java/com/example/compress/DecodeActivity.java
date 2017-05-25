package com.example.compress;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.apkfuns.xprogressdialog.XProgressDialog;
import com.bigkoo.pickerview.OptionsPickerView;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import net.lemonsoft.lemonhello.LemonHello;
import net.lemonsoft.lemonhello.LemonHelloAction;
import net.lemonsoft.lemonhello.LemonHelloInfo;
import net.lemonsoft.lemonhello.LemonHelloView;
import net.lemonsoft.lemonhello.interfaces.LemonHelloActionDelegate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class DecodeActivity extends AppCompatActivity implements CardView.OnClickListener {

    GifImageView decodeImage;
    GifImageView decodeResultImage;
    GifImageView originImage;
    GifImageView whereImage;
    GifImageView authenticImage;
    Button start;
    Button overButton;
    Button chooseAuthentic;
    Button chooseOrigin;
    Button inputDekey;
    Button psnr;
    TextView resultText;
    TextView dekeyText;
    Bitmap resultBitmap;
    Bitmap originBitmap;
    InputStream is;
    ScrollView scrollView;
    String authenticationUrl = "ahu.bmp";
    String originUrl = "lena.bmp";
    String resultString = "";
    int m = 4;
    int n = 4;
    int originWidth;
    int originHight;
    Bitmap authenticationBitmap;
    private Handler handler = new Handler();
    XProgressDialog dialog;
    Bitmap icBitmap;
    Bitmap ic2Bitmap;
    List<String> keyList;
    public static final int CHOOSE_PHOTO = 1;
    int flag;
    double[][][] resultArray;
    double[] key = {0.78, 3.59, Math.pow(7, 5), 0, Math.pow(2, 31) - 1, 102};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_decode);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);

        GlobalVaries globalVaries = (GlobalVaries) getApplication();
        originHight = globalVaries.getOriginHeight();
        originWidth = globalVaries.getOriginWidth();
        resultArray = globalVaries.getTamperResultArray();
        resultBitmap = To.ArraytoBitmap(resultArray);

        authenticImage = (GifImageView) findViewById(R.id.authentic_decode);
        scrollView = (ScrollView) findViewById(R.id.scrollView_decode);
        scrollView.setVerticalScrollBarEnabled(false);
        decodeImage = (GifImageView) findViewById(R.id.decode);
        decodeResultImage = (GifImageView) findViewById(R.id.decodeResult);//结果
        originImage = (GifImageView) findViewById(R.id.originImage_decode);
        whereImage = (GifImageView) findViewById(R.id.where);
        start = (Button) findViewById(R.id.startButton_decode);
        overButton = (Button) findViewById(R.id.over);
        chooseAuthentic = (Button) findViewById(R.id.choose_authentic);
        inputDekey = (Button) findViewById(R.id.input_dekey);
        psnr = (Button) findViewById(R.id.psnrButton);
        chooseOrigin = (Button) findViewById(R.id.choose_origin);
        resultText = (TextView) findViewById(R.id.authenticText_decode);
        dekeyText = (TextView) findViewById(R.id.dekeyText);

        chooseOrigin.setOnClickListener(this);
        start.setOnClickListener(this);
        psnr.setOnClickListener(this);
        chooseAuthentic.setOnClickListener(this);
        inputDekey.setOnClickListener(this);
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
            case R.id.input_dekey:
                String[] keyArray = new String[150];
                for(int i=0; i<keyArray.length; i++){
                    keyArray[i] = i+"";
                }
                keyList = Arrays.asList(keyArray);
                OptionsPickerView pvOptions = new  OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3 ,View v) {
                        //返回的分别是三个级别的选中位置
                        String tx = keyList.get(options1) + "  "
                                + keyList.get(options2) + "  "
                                + keyList.get(options3);
                        key[1] =  Double.parseDouble(keyList.get(options1));
                        key[3] =  Double.parseDouble(keyList.get(options2));
                        key[5] =  Double.parseDouble(keyList.get(options3));
                        dekeyText.setText(tx);
                    }
                })
                        .setSubmitText("确定")//确定按钮文字
                        .setCancelText("取消")//取消按钮文字
                        .setTitleText("加密密钥选择")//标题
                        .setSubCalSize(18)//确定和取消文字大小
                        .setTitleSize(20)//标题文字大小
                        .setContentTextSize(18)//滚轮文字大小
                        .setLabels("", "", "")//设置选择的三级单位
                        .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                        .setCyclic(true, true, true)//循环与否
                        .setSelectOptions(1, 1, 1)  //设置默认选中项
                        .setOutSideCancelable(false)//点击外部dismiss default true
                        .isDialog(true)//是否显示为对话框样式
                        .build();
                pvOptions.setNPicker(keyList, keyList, keyList);//添加数据源
                pvOptions.show();
                break;
            case R.id.startButton_decode:
                dialog = new XProgressDialog(this, "正在处理图像...", XProgressDialog.THEME_CIRCLE_PROGRESS);
                dialog.show();
                new Thread() {
                    public void run() {
                        //TODO 处理认证图像
                        double[][][] threeArray = To.BitmapToArray(authenticationBitmap);//原始图像的三位数组
                        double[][][] binaryArray = To.RGBtoBinary(threeArray);//二值化后的三位数组
                        double[][][] encodeBinaryArray = To.EncodeBinaryArray(binaryArray, key);//加密后的认证图像三维数组

                        //TODO 解密
                        double[][][][] ans = To.De(resultArray, originHight, originWidth, m, n, encodeBinaryArray, key);
                        double[][][] Ic = ans[0];
                        double[][][] Ic2 = ans[1];
                        icBitmap = To.ArraytoBitmap(Ic);
                        ic2Bitmap = To.ArraytoBitmap(Ic2);
                        handler.post(new Runnable() {    // 在新线程中使用Handler向主线程发送一段代码, 主线程自动执行run()方法
                            public void run() {
                                decodeResultImage.setImageBitmap(icBitmap);
                                whereImage.setImageBitmap(ic2Bitmap);
                                GlobalVaries globalVaries = (GlobalVaries) getApplication();
                                String url = globalVaries.getURL();
                                if (url != null) {
                                    originBitmap = BitmapFactory.decodeFile(url);
                                    originImage.setImageBitmap(originBitmap);
                                }
                                dialog.dismiss();
                            }
                        });
                    }
                }.start();
                break;
            case R.id.over:
                GlobalVaries globalVaries = (GlobalVaries) getApplication();
                globalVaries.toNull();
                finish();
                break;
            case R.id.choose_authentic:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    flag = 1;
                    openAlbum();
                }
                break;
            case R.id.choose_origin:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    flag = 2;
                    openAlbum();
                }
                break;
            case R.id.psnrButton:
                dialog = new XProgressDialog(this, "正在计算PSNR...", XProgressDialog.THEME_CIRCLE_PROGRESS);
                dialog.show();
                new Thread() {
                    public void run() {
                        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
                        resultString += ("PSNR: " + df.format(To.PSNR(originBitmap, icBitmap)) + "\n");
                        handler.post(new Runnable() {    // 在新线程中使用Handler向主线程发送一段代码, 主线程自动执行run()方法
                            public void run() {
                                resultText.setText(resultString);
                                dialog.dismiss();
                            }
                        });
                    }
                }.start();
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
            if (flag == 1) {//选择认证图像
                authenticationBitmap = BitmapFactory.decodeFile(imagePath);
                authenticImage.setImageBitmap(authenticationBitmap);
            } else if (flag == 2) {
                originBitmap = BitmapFactory.decodeFile(imagePath);
                originImage.setImageBitmap(originBitmap);
            }
        } else {
            Toast.makeText(this, "获取图片失败", Toast.LENGTH_SHORT).show();
        }
    }
}
