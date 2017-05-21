package com.example.compress.util;

import android.graphics.Bitmap;

/**
 * Created by ShadowAnt on 2017/5/7.
 */

public class Matlab {
    public static int numel(Bitmap bitmap) {
        int width = bitmap.getWidth();         //获取位图的宽
        int height = bitmap.getHeight();       //获取位图的高
        int ans = width * height;
        return ans;
    }

    /**
     * @param bitmap
     * @param m      分块大小
     * @param n
     * @return bitmap的二维矩阵，按照mn分块，第i列是第i块的所有像素值
     */
    public static int[][] im2col(Bitmap bitmap, int m, int n) {
        int[] bitmap1Array = RGB2Grey.bitmap2array(bitmap);
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
//        System.out.println("图像分块变为二维数组 输入图像的长宽"+height+ " "+width);
        int[][] bitmap2Array = new int[height][width];

        //TODO 一维数组变二维数组
        int index = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                bitmap2Array[i][j] = bitmap1Array[index++];
            }
        }

        //TODO 二维数组分块处理
        int blockHeight = (int) Math.ceil(height / m);//横着分可以分几块
        int blockWidth = (int) Math.ceil(width / n);//竖着分可以分几块
        int sumBlock = blockHeight * blockWidth;
        int blockPixel = m * n;
        int[][] block2Array = new int[blockPixel][sumBlock];
        for (int j = 0; j < sumBlock; j++) {
            //TODO 把一列对应的一块赋值 i为正在处理的当前块序号,(row,col)为当前块对应的原始二维数组第一个像素
            int row = (j * m) % height;
            int col = j / blockHeight * n;
            for (int i = 0; i < blockPixel; i++) {
                int rowIndex = i % m;
                int colIndex = i / n;
                if (row + rowIndex >= height || col + colIndex >= width) {
                    block2Array[i][j] = 0;
                } else {
                    block2Array[i][j] = bitmap2Array[row + rowIndex][col + colIndex];
                }
//                if(j==0) System.out.println(block2Array[i][j]);
            }
        }
        return block2Array;
    }

    /**
     * @param I_compress 行数为 m*n
     * @param m
     * @param n
     * @param height     复原后普通矩阵的高
     * @param width
     * @return
     */
    public static Bitmap col2im(int[][] I_compress, int m, int n, int height, int width) {
        //TODO 二维分块矩阵转为二维普通矩阵
        int blockNum = I_compress[0].length;
        int blockHeight = (int) Math.ceil(height / m);//横着分可以分几块
        int[][] bitmap2Array = new int[height + m - 1][width + n - 1];
//        Log.e("bigHeight  ",bigHeight + "  " +  bigWidth);
        int blockPixel = m * n;
        for (int j = 0; j < blockNum; j++) {
            //TODO 把一列对应的一块赋值 i为正在处理的当前块序号,(row,col)为当前块对应的原始二维数组第一个像素
            int row = (j * m) % height;
            int col = j / blockHeight;
            col *= n;
            for (int i = 0; i < blockPixel; i++) {//4
                int rowIndex = i % m;
                int colIndex = i / n;
                bitmap2Array[row + rowIndex][col + colIndex] = I_compress[i][j];
            }
        }
//        System.out.println(bitmap2Array[0][0]+" "+bitmap2Array[0][1]+" "+bitmap2Array[0][1]+" "+bitmap2Array[1][1]+" ");//正常
        //TODO 二维普通矩阵转为一维矩阵
        int sumPixel = height * width;
        int[] bitmap1Array = new int[sumPixel];
        int index = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                bitmap1Array[index++] = bitmap2Array[i][j];
            }
        }
        //TODO 一维矩阵转为Bitmap
        int alpha = 0xFF << 24;
        for (int i = 0; i < sumPixel; i++) {
            bitmap1Array[i] = alpha | (bitmap1Array[i] << 16) | (bitmap1Array[i] << 8) | bitmap1Array[i];
        }
        System.out.println(bitmap1Array[0] + " " + bitmap1Array[1]);
        Bitmap resultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);//int数组转为bitmap ALPHA_8 ARGB_8888
        resultBitmap.setPixels(bitmap1Array, 0, width, 0, 0, width, height);
        return resultBitmap;
    }

    public static double min(double[] array) {
        double min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }

    public static int find(double[] array, double num) {
        int pos = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == num) {
                pos = i;
                break;
            }
        }
        return pos;
    }

    public static double mean(int[][] input) {
        int height = input.length;
        int width = input[0].length;
        double sum = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                sum += input[i][j];
            }
        }
        sum = sum / (double) (height * width);
        return sum;
    }

    public static int[][] multip2(int[][] x) {
        int m = x.length;
        int n = x[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                x[i][j] = x[i][j] * x[i][j];
            }
        }
        return x;
    }
}
