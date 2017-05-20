package com.example.compress;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by ShadowAnt on 2017/5/20.
 */

public class To {

    /**
     * 把Bitmap转换为三维矩阵
     *
     * @param bitmap 待处理位图
     * @return 三维数组
     */
    public static int[][][] BitmapToArray(Bitmap bitmap) {
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int[][][] ans = new int[height][width][4];
        int alpha = 0xff;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int pixel = bitmap.getPixel(j, i);
                int red = Color.red(pixel);
                int green = Color.green(pixel);
                int blue = Color.blue(pixel);
                ans[i][j][0] = alpha;
                ans[i][j][1] = red;
                ans[i][j][2] = green;
                ans[i][j][3] = blue;
            }
        }
        return ans;
    }

    public static Bitmap ArraytoBitmap(int[][][] array) {
        int height = array.length;
        int width = array[0].length;
        int[] ans = new int[height * width];
        int index = 0;
        for (int j = 0; j < width; j++) {
            for (int i = 0; i < height; i++) {
                ans[index++] = Color.argb(array[j][i][0], array[j][i][1], array[j][i][2], array[j][i][3]);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(ans, width, height, Bitmap.Config.ARGB_8888);
        return bitmap;
    }

    /**
     * 使用分两法
     *
     * @param bitmap
     * @return
     */
    public static int[][][] RGBtoGrayArray(int[][][] bitmap) {
        return null;
    }
}
