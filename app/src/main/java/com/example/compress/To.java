package com.example.compress;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.example.compress.util.Chaotic;
import com.example.compress.util.Decryption;
import com.example.compress.util.Embed;
import com.example.compress.util.Encryption;
import com.example.compress.util.Extr;
import com.example.compress.util.My_bin2dec;
import com.example.compress.util.My_dec2bin;
import com.example.compress.util.My_derand;
import com.example.compress.util.My_rand;
import com.example.compress.util.Rand_numbers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.R.attr.path;

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
        //TODO 处理过程key会改变，保证下次使用相同的key这里复制一份
        int len = key.length;
        double[] cpyKey = new double[len];
        for (int i = 0; i < len; i++) {
            cpyKey[i] = key[i];
        }

        int height = array.length;
        int width = array[0].length;
        int num = height * width;
        int[][][] ans = new int[height][width][4];
        int index = 0;
        int temp;
        double[] sequence = Rand_numbers.Rand_numbers(cpyKey, num, 2);
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
        //TODO 对载体图像分层单通道
        int height = array.length;//原始高
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
//        int[][] I_compressG = AMBTC(IcR, m, n, key, enAuthenticationArray);
//        int[][] I_compressB = AMBTC(IcR, m, n, key, enAuthenticationArray);
        int[][] I_compressG = AMBTC(IcG, m, n, key, enAuthenticationArray);
        int[][] I_compressB = AMBTC(IcB, m, n, key, enAuthenticationArray);
        //TODO 合并图层得到三维矩阵
        height = (int) Math.ceil(height / (double) m) * 2;  //5 -> 2
        width = (int) Math.ceil(width / (double) n) * 2;
        int[][] R = To.col2im(I_compressR, 2, 2, height, width);
        int[][] G = To.col2im(I_compressG, 2, 2, height, width);
        int[][] B = To.col2im(I_compressB, 2, 2, height, width);
        int[][][] ans = new int[height][width][4];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                ans[i][j][0] = 0xff;
                ans[i][j][1] = R[i][j];
                ans[i][j][2] = G[i][j];
                ans[i][j][3] = B[i][j];
            }
        }
        return ans;
    }

    public static int[][][][] De(int[][][] array, int oH, int oW, int m, int n, int[][][] enAuthenticationThreeArray, double[] key) {
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
        //TODO 对收到的图像进行分块
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
        int[][] I_compressR = To.im2col(rArray, 2, 2);
        int[][] I_compressG = To.im2col(gArray, 2, 2);
        int[][] I_compressB = To.im2col(bArray, 2, 2);
        int[][][] ansR = DeAMBTC(I_compressR, m, n, key, enAuthenticationArray);
        int[][][] ansG = DeAMBTC(I_compressG, m, n, key, enAuthenticationArray);
        int[][][] ansB = DeAMBTC(I_compressB, m, n, key, enAuthenticationArray);
        int[][] IcR = ansR[0];
        int[][] IcG = ansG[0];
        int[][] IcB = ansB[0];
        int[][] Ic2R = ansR[1];
        int[][] Ic2G = ansG[1];
        int[][] Ic2B = ansB[1];
        IcR = To.col2im(IcR, m, n, oH, oW);
        IcG = To.col2im(IcG, m, n, oH, oW);
        IcB = To.col2im(IcB, m, n, oH, oW);
        Ic2R = To.col2im(Ic2R, m, n, oH, oW);
        Ic2G = To.col2im(Ic2G, m, n, oH, oW);
        Ic2B = To.col2im(Ic2B, m, n, oH, oW);
        int[][][] Ic = new int[oH][oW][4];
        int[][][] Ic2 = new int[oH][oW][4];
        for (int i = 0; i < oH; i++) {
            for (int j = 0; j < oW; j++) {
                Ic[i][j][0] = 0xff;
                Ic[i][j][1] = IcR[i][j];
                Ic[i][j][2] = IcG[i][j];
                Ic[i][j][3] = IcB[i][j];
                Ic2[i][j][0] = 0xff;
                Ic2[i][j][1] = Ic2R[i][j];
                Ic2[i][j][2] = Ic2G[i][j];
                Ic2[i][j][3] = Ic2B[i][j];
            }
        }
        int[][][][] ans = new int[2][oH][oW][4];
        ans[0] = Ic;
        ans[1] = Ic2;
        return ans;
    }

    public static int[][][] DeAMBTC(int[][] I_compress, int m, int n, double[] key, int[] enAuthenticationArray) {
        //TODO 处理过程key会改变，保证下次使用相同的key这里复制一份
        int len = key.length;
        double[] cpyKey = new double[len];
        for (int i = 0; i < len; i++) {
            cpyKey[i] = key[i];
        }
        int sumBlock = I_compress[0].length;
        int sumPixel = m * n;
        int[][] Ic = new int[sumPixel][sumBlock];
        int[][] Ic2 = new int[sumPixel][sumBlock];
        double[] xor_key = Rand_numbers.Rand_numbers(cpyKey, sumBlock, 256);
        double[] sequence = Chaotic.chaotic_maping_sequence(cpyKey[0], cpyKey[1], sumBlock);
        int[] temp_bitmap = new int[m * n];//一块中的像素
        for (int j = 0; j < sumBlock; j++) {
            int a = I_compress[0][j];
            int b = I_compress[1][j];
            int[] dec = new int[2];
            dec[0] = I_compress[2][j];
            dec[1] = I_compress[3][j];
            //TODO %认证信息
            int code = Extr.extr(a, b, dec);
            for (int k = 0; k < sumPixel; k++) {
                if (code != enAuthenticationArray[j]) {
                    Ic2[k][j] = 0;
                } else {
                    Ic2[k][j] = 255;
                }
            }
            //TODO 为直接解压缩保留
            int[] bitmap2 = My_dec2bin.my_dec2bin(dec, 8);
            for (int k = 0; k < m * n; k++) {
                if (bitmap2[k] == 0) {
                    temp_bitmap[k] = a;
                }
                if (bitmap2[k] == 1) {
                    temp_bitmap[k] = b;
                }
            }
            //  解密
            cpyKey[5] = xor_key[j];
            int[] group4 = Decryption.decrption(a, b, dec, cpyKey);
            a = group4[0];
            b = group4[1];
            dec[0] = group4[2];
            dec[1] = group4[3];
            int[] longBitmap = My_dec2bin.my_dec2bin(dec, 8);
            // 用key对bitmap置乱
            longBitmap = My_derand.my_derand(longBitmap, sequence[j], cpyKey[1]);
            for (int k = 0; k < m * n; k++) {
                if (longBitmap[k] == 0) {
                    temp_bitmap[k] = a;
                } else if (longBitmap[k] == 1) {
                    temp_bitmap[k] = b;
                }
            }
            for (int k = 0; k < sumPixel; k++) {
                Ic[k][j] = temp_bitmap[k];
            }
        }
        int[][][] ans = new int[2][sumPixel][sumBlock];
        ans[0] = Ic;
        ans[1] = Ic2;
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
        int blockHeight = (int) Math.ceil(height / (double) m);//横着分可以分几块
        int blockWidth = (int) Math.ceil(width / (double) n);//竖着分可以分几块
        int sumBlock = blockHeight * blockWidth;
        int blockPixel = m * n;//一块中的像素个数
        int[][] block2Array = new int[blockPixel][sumBlock];
        for (int j = 0; j < sumBlock; j++) {
            //TODO 把一列对应的一块赋值 i为正在处理的当前块序号,(row,col)为当前块对应的原始二维数组第一个像素
            int row = (j % blockHeight) * m;
            int col = j / blockHeight * n;
            for (int i = 0; i < blockPixel; i++) {
                int rowIndex = i % m;
                int colIndex = i / m;
                int x = row + rowIndex;
                int y = col + colIndex;
                if (x >= height || y >= width) {
                    block2Array[i][j] = 0;
                } else {
                    block2Array[i][j] = array[x][y];
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
        double[] xor_key = Rand_numbers.Rand_numbers(cpyKey, blockNum, 256);
        double[] sequence = Chaotic.chaotic_maping_sequence(cpyKey[0], cpyKey[1], blockNum);
        int[] bitmap = new int[m * n];//一块的16个像素矩阵
        int[][] I_compress = new int[4][blockNum];//返回结果
        int sumPixel = m * n;
        //TODO 对每块进行处理 AMBTC
        for (int j = 0; j < blockNum; j++) {
            int sum = 0;
            for (int i = 0; i < sumPixel; i++) {
                sum += array[i][j];
            }
            int ave = sum / sumPixel;//均值
            int highSum = 0;
            int lowSum = 0;
            int highNum = 0;
            int lowNum = 0;
            for (int i = 0; i < sumPixel; i++) {
                if (array[i][j] >= ave) {
                    highNum++;
                    highSum += array[i][j];
                    bitmap[i] = 1;
                } else {
                    lowNum++;
                    lowSum += array[i][j];
                    bitmap[i] = 0;
                }
            }
            int b = highSum / highNum;   //高均值
            int a;
            if (highNum == sumPixel) {
                a = b;
            } else {//这里不会出现除以0的情况
                a = lowSum / lowNum;  //低均值
            }
            bitmap = My_rand.my_rand(bitmap, sequence[j], cpyKey[1], j);
            int[] dec = My_bin2dec.my_bin2dec(bitmap, 8);
            double[] keyTemp = new double[len];//复制key
            for (int i = 0; i < len; i++) {
                cpyKey[i] = key[i];
            }
            keyTemp[5] = xor_key[j];
            int[] group4 = Encryption.encryption(a, b, dec, keyTemp); //异或加密
            a = group4[0];
            b = group4[1];
            dec[0] = group4[2];
            dec[1] = group4[3];
            group4 = Embed.embed(a, b, dec, enAuthenticationArray[j]);     //嵌入认证信息
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
     * @return 单图通道的一层，组装成正常矩阵
     */
    public static int[][] col2im(int[][] I_compress, int m, int n, int height, int width) {
        int blockNum = I_compress[0].length;
        int blockHeight = height / m;//横着分可以分几块
        int[][] bitmap2Array = new int[height][width];//返回结果
        int blockPixel = m * n;
        for (int j = 0; j < blockNum; j++) {
            //TODO 把一列对应的一块赋值 i为正在处理的当前块序号,(row,col)为当前块对应的原始二维数组第一个像素
            int row = (j % blockHeight) * m;
            int col = j / blockHeight * n;
            for (int i = 0; i < blockPixel; i++) {//4
                int rowIndex = i % m;
                int colIndex = i / m;
                int x = row + rowIndex;
                int y = col + colIndex;
                if (x < height && y < width) {
                    bitmap2Array[x][y] = I_compress[i][j];
                }
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
        if (oh > height || eh > height || ow > width || ew > width) {
            for (int i = 0; i < height / 2; i++) {
                for (int j = 0; j < width / 2; j++) {
                    ans[i][j][1] = pix;
                    ans[i][j][2] = pix;
                    ans[i][j][3] = pix;
                }
            }
        } else {
            for (int i = oh; i < eh; i++) {
                for (int j = ow; j < ew; j++) {
                    ans[i][j][1] = pix;
                    ans[i][j][2] = pix;
                    ans[i][j][3] = pix;
                }
            }
        }
        return ans;
    }

    /**
     * 把bitmap jpeg存入图库
     *
     * @param context 上下文
     * @param bmp     要处理的bitmap
     */
    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
    }
}
