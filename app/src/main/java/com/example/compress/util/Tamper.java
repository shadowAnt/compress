package com.example.compress.util;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * Created by ShadowAnt on 2017/5/11.
 */

public class Tamper {

    public static Bitmap tamper(Bitmap bitmap, int oh, int eh, int ow, int ew, int pix) {
        Bitmap temp = bitmap;
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        Log.e("处理后的图像的长宽 ", bitmap.getHeight() + "  " + bitmap.getWidth());
        int sumPixel = height * width;
        //237, 256, 1, 20, 0
        if (oh >= 0 && eh <= height && ow >= 0 && ew <= width) {
            int[] pixels = RGB2Grey.bitmap2array(bitmap);
            for (int i = oh; i < eh; i++) {
                for (int j = ow; j < ew; j++) {
                    pixels[i * width + j] = pix;
                    Log.e("篡改 ", pix + "");
                }
            }
            //TODO 一维矩阵转为Bitmap
            int alpha = 0xFF << 24;
            for (int i = 0; i < sumPixel; i++) {
                pixels[i] = alpha | (pixels[i] << 16) | (pixels[i] << 8) | pixels[i];
            }
            temp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);//int数组转为bitmap ALPHA_8 ARGB_8888
            temp.setPixels(pixels, 0, width, 0, 0, width, height);
        }
        return temp;
    }
}
