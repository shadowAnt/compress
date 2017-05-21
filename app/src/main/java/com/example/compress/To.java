package com.example.compress;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.example.compress.util.Chaotic;
import com.example.compress.util.Embed;
import com.example.compress.util.Encryption;
import com.example.compress.util.My_bin2dec;
import com.example.compress.util.My_rand;
import com.example.compress.util.Rand_numbers;

import java.util.Arrays;

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

    /**
     * @param array                      载体图像三维数组
     * @param m                          分块大小
     * @param n                          分块大小
     * @param enAuthenticationThreeArray 加密后的认证图像三维数组
     * @param key                        加密密钥
     * @return 待发送的三维数组
     */
    public static int[][][] En(int[][][] array, int m, int n, int[][][] enAuthenticationThreeArray, double[] key) {
        //TODO 认证信息变为一维数组
        int enAuthenticationHeight = enAuthenticationThreeArray.length;
        int enAuthenticationWidth = enAuthenticationThreeArray[0].length;
        int[] enAuthenticationArray = new int[enAuthenticationHeight * enAuthenticationWidth];
        int index = 0;
        for (int i = 0; i < enAuthenticationHeight; i++) {
            for (int j = 0; j < enAuthenticationWidth; j++) {
                enAuthenticationArray[index++] = enAuthenticationThreeArray[i][j][1] == 255 ? 1 : 0;
            }
        }
        //TODO 对载体图像进行处理
        int height = array.length;
        int width = array[0].length;
        int[][] rArray = new int[height][width];
        int[][] gArray = new int[height][width];
        int[][] bArray = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                rArray[i][j] = array[i][j][1];
                gArray[i][j] = array[i][j][2];
                bArray[i][j] = array[i][j][3];
            }
        }
        //TODO 二维矩阵变为分块矩阵
        int[][] IcR = To.im2col(rArray, m, n);
        int[][] IcG = To.im2col(gArray, m, n);
        int[][] IcB = To.im2col(bArray, m, n);
        //TODO 分块矩阵输入得到压缩加密的各个通道矩阵
        int[][] I_compressR = AMBTC(IcR, m, n, key, enAuthenticationArray);
        int[][] I_compressG = AMBTC(IcG, m, n, key, enAuthenticationArray);
        int[][] I_compressB = AMBTC(IcB, m, n, key, enAuthenticationArray);
        //TODO 合并图层得到三维矩阵
        height = (int) Math.ceil(height / m) * 2;
        width = (int) Math.ceil(width / n) * 2;
        int[][] R = To.col2im(I_compressR, 2, 2, height, width);
        int[][] G = To.col2im(I_compressG, 2, 2, height, width);
        int[][] B = To.col2im(I_compressB, 2, 2, height, width);
        int h = R.length;
        int w = R[0].length;
        int[][][] ans = new int[h][w][4];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                ans[i][j][0] = 0xff;
                ans[i][j][1] = R[i][j];
                ans[i][j][2] = G[i][j];
                ans[i][j][3] = B[i][j];
            }
        }
        return ans;
    }

    /**
     * 把RGB单独的一层进行分块处理
     *
     * @param array 单独的一层
     * @param m     分块的大小
     * @param n     分块的大小
     * @return 分块结果二维矩阵
     */
    public static int[][] im2col(int[][] array, int m, int n) {
        int height = array.length;
        int width = array[0].length;
        int blockHeight = (int) Math.ceil(height / m);//横着分可以分几块
        int blockWidth = (int) Math.ceil(width / n);//竖着分可以分几块
        int sumBlock = blockHeight * blockWidth;
        int blockPixel = m * n;//一块中的像素个数
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
                    block2Array[i][j] = array[row + rowIndex][col + colIndex];
                }
            }
        }
        return block2Array;
    }

    /**
     * 把分块矩阵压缩加密
     *
     * @param array                 分块矩阵
     * @param m                     分块大小
     * @param n                     分块大小
     * @param key                   密钥
     * @param enAuthenticationArray 待嵌入的一维认证信息
     * @return 加密压缩后的矩阵 I_compress
     */
    public static int[][] AMBTC(int[][] array, int m, int n, double[] key, int[] enAuthenticationArray) {
        //TODO 处理过程key会改变，保证下次使用相同的key这里复制一份
        int len = key.length;
        double[] cpyKey = new double[len];
        for (int i = 0; i < len; i++) {
            cpyKey[i] = key[i];
        }
        int blockNum = array[0].length;//列数 即是块数
        double[] xor_key = Rand_numbers.Rand_numbers(key, blockNum, 256);
        double[] sequence = Chaotic.chaotic_maping_sequence(key[0], key[1], blockNum);
        int[] bitmap = new int[m * n];//一块的16个像素矩阵
        int[][] I_compress = new int[4][blockNum];//保留结果
        //TODO 对每块进行处理 AMBTC
        for (int j = 0; j < blockNum; j++) {
            int sum = 0;
            for (int i = 0; i < m * n; i++) {
                sum += array[i][j];
            }
            int ave = sum / (m * n);//均值
            int highSum = 0;
            int highNum = 0;
            for (int i = 0; i < m * n; i++) {
                if (array[i][j] >= ave) {
                    highNum++;
                    highSum += array[i][j];
                    bitmap[i] = 1;
                } else {
                    bitmap[i] = 0;
                }
            }
            int b = highSum / highNum;   //高均值
            int a;
            if (highNum == m * n) {
                a = b;
            } else {//这里不会出现除以0的情况
                a = (sum - highSum) / (m * n - highNum);  //低均值
            }
            bitmap = My_rand.my_rand(bitmap, sequence[j], key[1], j);
            int[] dec = My_bin2dec.my_bin2dec(bitmap, 8);
            double[] keyTemp = key;
            keyTemp[5] = xor_key[j];
            int[] group4 = Encryption.encryption(a, b, dec, keyTemp); //异或加密
            a = group4[0];
            b = group4[1];
            dec[0] = group4[2];
            dec[1] = group4[3];
            group4 = Embed.embed(a, b, dec, enAuthenticationArray[j]);     //嵌入认证信息
            if (j == 0 || j == 1)
                System.out.println("嵌入认证信息之后的四元组  " + Arrays.toString(group4));
            I_compress[0][j] = group4[0];
            I_compress[1][j] = group4[1];
            I_compress[2][j] = group4[2];
            I_compress[3][j] = group4[3];
        }
        return I_compress;
    }

    /**
     * 把分块压缩后的二维矩阵转化为单图通道的一层
     *
     * @param I_compress 分块压缩后的二维矩阵
     * @param m          分块大小
     * @param n          分块大小
     * @param height     期望生成的高
     * @param width      期望生成的宽
     * @return 单图通道的一层
     */
    public static int[][] col2im(int[][] I_compress, int m, int n, int height, int width) {
        int blockNum = I_compress[0].length;
        int blockHeight = (int) Math.ceil(height / m);//横着分可以分几块
        int[][] bitmap2Array = new int[height + m - 1][width + n - 1];
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
        return bitmap2Array;
    }

    /**
     * 对压缩加密后的矩阵图像进行篡改操作
     *
     * @param array 压缩机密后的矩阵
     * @param oh    篡改的起始高
     * @param eh    篡改的终止高
     * @param ow    篡改的起始宽
     * @param ew    篡改的终止宽
     * @param pix   要篡改的像素值
     * @return 返回篡改后的图像
     */
    public static int[][][] tamper(int[][][] array, int oh, int eh, int ow, int ew, int pix) {
        int height = array.length;
        int width = array[0].length;
        int[][][] ans = new int[height][width][4];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                ans[i][j][0] = 0xff;
                ans[i][j][1] = array[i][j][1];
                ans[i][j][2] = array[i][j][2];
                ans[i][j][3] = array[i][j][3];
            }
        }
        for (int i = oh; i < eh; i++) {
            for (int j = ow; j < ew; j++) {
                ans[i][j][1] = pix;
                ans[i][j][2] = pix;
                ans[i][j][3] = pix;
            }
        }
        return ans;
    }


}
