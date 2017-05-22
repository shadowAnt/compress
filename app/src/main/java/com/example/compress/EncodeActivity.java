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
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.compress.util.GetCompressionRatio;
import com.example.compress.util.Joint_en;
import com.example.compress.util.Tamper;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import net.lemonsoft.lemonhello.LemonHello;
import net.lemonsoft.lemonhello.LemonHelloAction;
import net.lemonsoft.lemonhello.LemonHelloInfo;
import net.lemonsoft.lemonhello.LemonHelloView;
import net.lemonsoft.lemonhello.interfaces.LemonHelloActionDelegate;

import java.io.File;
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
    String resultString = "";
    public static final int CHOOSE_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_encode);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);

        enAuthenticationBitmap = getIntent().getParcelableExtra("enAuthenticationBitmap");
        encodeBinaryArray = To.BitmapToArray(enAuthenticationBitmap);
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollEncode);
        scrollView.setVerticalScrollBarEnabled(false);
        choose = (Button) findViewById(R.id.changeButton_encode);
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
        Log.e("originBitmap", originBitmap.getHeight() + "  " + originBitmap.getWidth());
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.changeButton_encode:
                //TODO 更改图像
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
                break;
            case R.id.startButton_encode:
                double[] key = {0.78, 3.59, Math.pow(7, 5), 0, Math.pow(2, 31) - 1, 102};
                int[][][] originArray= To.BitmapToArray(originBitmap);
                int[][][] resultArray = To.En(originArray, m, n, encodeBinaryArray, key);//(int) Math.ceil(height / m) * 2;
                resultBitmap = To.ArraytoBitmap(resultArray);
                resultImage.setImageBitmap(resultBitmap);
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
            originBitmap = BitmapFactory.decodeFile(imagePath);
            resultString = "";
            resultString += "认证图像位置:    " + imagePath + "\n";
            originImage.setImageBitmap(originBitmap);
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
