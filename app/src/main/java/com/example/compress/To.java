package com.example.compress;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.example.compress.util.Rand_numbers;

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

    /***
     * 把三维矩阵变为Bitmap
     *
     * @param array
     * @return
     */
    public static Bitmap ArraytoBitmap(int[][][] array) {
        int height = array.length;
        int width = array[0].length;
        int[] ans = new int[height * width];
        int index = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                ans[index++] = Color.argb(array[i][j][0], array[i][j][1], array[i][j][2], array[i][j][3]);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(ans, width, height, Bitmap.Config.ARGB_8888);
        return bitmap;
    }

    /**
     * RGB to binary image
     *
     * @param array
     * @return
     */
    public static int[][][] RGBtoBinary(int[][][] array) {
        int height = array.length;
        int width = array[0].length;
        int alpha = 0xff;
        int sum = 0;
        int[][][] ans = new int[height][width][4];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                sum += array[i][j][2];
            }
        }
        sum /= (height * width);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int green = array[i][j][2];
                if (green >= sum) {
                    ans[i][j][2] = 255;
                    ans[i][j][0] = alpha;
                    ans[i][j][1] = 255;
                    ans[i][j][3] = 255;
                } else {
                    ans[i][j][2] = 0;
                    ans[i][j][0] = alpha;
                    ans[i][j][1] = 0;
                    ans[i][j][3] = 0;
                }
            }
        }
        return ans;
    }

    /**
     * 加密认证图像
     *
     * @param array binary array
     * @return
     */
    public static int[][][] EncodeBinaryArray(int[][][] array, double[] key) {
        int height = array.length;
        int width = array[0].length;
        int num = height * width;
        int[][][] ans = new int[height][width][4];
        int index = 0;
        int temp;
        double[] sequence = Rand_numbers.Rand_numbers(key, num, 2);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int pixel = array[i][j][1];
                if (pixel == 255) {
                    temp = 1 ^ (int) sequence[index];
                } else {
                    temp = 0 ^ (int) sequence[index];
                }
                if (temp == 1) {
                    temp = 255;
                }
                index++;
                ans[i][j][0] = 0xff;
                ans[i][j][1] = temp;
                ans[i][j][2] = temp;
                ans[i][j][3] = temp;
            }
        }
        return ans;
    }
}
