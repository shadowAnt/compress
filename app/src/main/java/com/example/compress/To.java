package com.example.compress;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;

import com.example.compress.util.Chaotic;
import com.example.compress.util.Decryption;
import com.example.compress.util.Embed;
import com.example.compress.util.Encryption;
import com.example.compress.util.Extr;
import com.example.compress.util.Matlab;
import com.example.compress.util.My_bin2dec;
import com.example.compress.util.My_dec2bin;
import com.example.compress.util.My_derand;
import com.example.compress.util.My_rand;
import com.example.compress.util.Rand_numbers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.Math.log10;

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
    public static double[][][] BitmapToArray(Bitmap bitmap) {
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        double[][][] ans = new double[height][width][4];
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
    public static Bitmap ArraytoBitmap(double[][][] array) {
        int height = array.length;
        int width = array[0].length;
        int z = array[0][0].length;
        int[] ans = new int[height * width];
        int[][][] temp = new int[height][width][z];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                for (int k = 0; k < z; k++)
                    temp[i][j][0] = (int) Math.round(array[i][j][0]);
                temp[i][j][1] = (int) Math.round(array[i][j][1]);
                temp[i][j][2] = (int) Math.round(array[i][j][2]);
                temp[i][j][3] = (int) Math.round(array[i][j][3]);
            }
        }

        int index = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                ans[index++] = Color.argb(temp[i][j][0], temp[i][j][1], temp[i][j][2], temp[i][j][3]);
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
    public static double[][][] RGBtoBinary(double[][][] array) {
        int height = array.length;
        int width = array[0].length;
        int alpha = 0xff;
        int sum = 0;
        double[][][] ans = new double[height][width][4];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                sum += array[i][j][2];
            }
        }
        sum /= (height * width);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                double green = array[i][j][2];
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
    public static double[][][] EncodeBinaryArray(double[][][] array, double[] key) {
        //TODO 处理过程key会改变，保证下次使用相同的key这里复制一份
        int len = key.length;
        double[] cpyKey = new double[len];
        for (int i = 0; i < len; i++) {
            cpyKey[i] = key[i];
        }
        int height = array.length;
        int width = array[0].length;
        int num = height * width;
        double[][][] ans = new double[height][width][4];
        int index = 0;
        int temp;
        double[] sequence = Rand_numbers.Rand_numbers(cpyKey, num, 2);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                double pixel = array[i][j][1];
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
    public static double[][][] En(double[][][] array, int m, int n, double[][][] enAuthenticationThreeArray, double[] key) {
        //TODO 认证信息变为一维数组
        int enAuthenticationHeight = enAuthenticationThreeArray.length;
        int enAuthenticationWidth = enAuthenticationThreeArray[0].length;
        double[] enAuthenticationArray = new double[enAuthenticationHeight * enAuthenticationWidth];
        int index = 0;
        for (int i = 0; i < enAuthenticationHeight; i++) {
            for (int j = 0; j < enAuthenticationWidth; j++) {
                enAuthenticationArray[index++] = enAuthenticationThreeArray[i][j][1] == 255 ? 1 : 0;
            }
        }
        //TODO 对载体图像分层单通道
        int height = array.length;//原始高
        int width = array[0].length;
        double[][] rArray = new double[height][width];
        double[][] gArray = new double[height][width];
        double[][] bArray = new double[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                rArray[i][j] = array[i][j][1];
                gArray[i][j] = array[i][j][2];
                bArray[i][j] = array[i][j][3];
            }
        }
        //TODO 二维矩阵变为分块矩阵
        double[][] IcR = To.im2col(rArray, m, n);
        double[][] IcG = To.im2col(gArray, m, n);
        double[][] IcB = To.im2col(bArray, m, n);
        //TODO 分块矩阵输入得到压缩加密的各个通道矩阵
        double[][] I_compressR = To.AMBTC(IcR, m, n, key, enAuthenticationArray);
        double[][] I_compressG = To.AMBTC(IcG, m, n, key, enAuthenticationArray);
        double[][] I_compressB = To.AMBTC(IcB, m, n, key, enAuthenticationArray);
        //TODO 合并图层得到三维矩阵
        height = (int) Math.ceil(height / (double) m) * 2;  //5 -> 2
        width = (int) Math.ceil(width / (double) n) * 2;
        double[][] R = To.col2im(I_compressR, 2, 2, height, width);
        double[][] G = To.col2im(I_compressG, 2, 2, height, width);
        double[][] B = To.col2im(I_compressB, 2, 2, height, width);
        double[][][] ans = new double[height][width][4];
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

    public static double[][][][] De(double[][][] array, int oH, int oW, int m, int n, double[][][] enAuthenticationThreeArray, double[] key) {
        //TODO 认证信息变为一维数组
        int enAuthenticationHeight = enAuthenticationThreeArray.length;
        int enAuthenticationWidth = enAuthenticationThreeArray[0].length;
        double[] enAuthenticationArray = new double[enAuthenticationHeight * enAuthenticationWidth];
        int index = 0;
        for (int i = 0; i < enAuthenticationHeight; i++) {
            for (int j = 0; j < enAuthenticationWidth; j++) {
                enAuthenticationArray[index++] = enAuthenticationThreeArray[i][j][1] == 255 ? 1 : 0;
            }
        }
        //TODO 对收到的图像进行分块
        int height = array.length;
        int width = array[0].length;
        double[][] rArray = new double[height][width];
        double[][] gArray = new double[height][width];
        double[][] bArray = new double[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                rArray[i][j] = array[i][j][1];
                gArray[i][j] = array[i][j][2];
                bArray[i][j] = array[i][j][3];
            }
        }
        //TODO 二维矩阵变为分块矩阵
        double[][] I_compressR = To.im2col(rArray, 2, 2);
        double[][] I_compressG = To.im2col(gArray, 2, 2);
        double[][] I_compressB = To.im2col(bArray, 2, 2);
        double[][][] ansR = To.DeAMBTC(I_compressR, m, n, key, enAuthenticationArray);
        double[][][] ansG = To.DeAMBTC(I_compressG, m, n, key, enAuthenticationArray);
        double[][][] ansB = To.DeAMBTC(I_compressB, m, n, key, enAuthenticationArray);
        double[][] IcR = ansR[0];
        double[][] IcG = ansG[0];
        double[][] IcB = ansB[0];
        double[][] Ic2R = ansR[1];
        double[][] Ic2G = ansG[1];
        double[][] Ic2B = ansB[1];
        IcR = To.col2im(IcR, m, n, oH, oW);
        IcG = To.col2im(IcG, m, n, oH, oW);
        IcB = To.col2im(IcB, m, n, oH, oW);
        Ic2R = To.col2im(Ic2R, m, n, oH, oW);
        Ic2G = To.col2im(Ic2G, m, n, oH, oW);
        Ic2B = To.col2im(Ic2B, m, n, oH, oW);
        double[][][] Ic = new double[oH][oW][4];
        double[][][] Ic2 = new double[oH][oW][4];
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
        double[][][][] ans = new double[2][oH][oW][4];
        ans[0] = Ic;
        ans[1] = Ic2;
        return ans;
    }

    public static double[][][] DeAMBTC(double[][] I_compress, int m, int n, double[] key, double[] enAuthenticationArray) {
        //TODO 处理过程key会改变，保证下次使用相同的key这里复制一份
        int len = key.length;
        double[] cpyKey = new double[len];
        for (int i = 0; i < len; i++) {
            cpyKey[i] = key[i];
        }
        int sumBlock = I_compress[0].length;
        int sumPixel = m * n;
        double[][] Ic = new double[sumPixel][sumBlock];
        double[][] Ic2 = new double[sumPixel][sumBlock];
        double[] xor_key = Rand_numbers.Rand_numbers(cpyKey, sumBlock, 256);
        double[] sequence = Chaotic.chaotic_maping_sequence(cpyKey[0], cpyKey[1], sumBlock);
        double[] temp_bitmap = new double[m * n];//一块中的像素
        for (int j = 0; j < sumBlock; j++) {
            double a = I_compress[0][j];
            double b = I_compress[1][j];
            double[] dec = new double[2];
            dec[0] = I_compress[2][j];
            dec[1] = I_compress[3][j];
            //TODO %认证信息
            double code = Extr.extr(a, b, dec);
            for (int k = 0; k < sumPixel; k++) {
                if (code != enAuthenticationArray[j]) {
                    Ic2[k][j] = 0;
                } else {
                    Ic2[k][j] = 255;
                }
            }
//            //TODO 为直接解压缩保留
//            int[] bitmap2 = My_dec2bin.my_dec2bin(dec, 8);
//            for (int k = 0; k < m * n; k++) {
//                if (bitmap2[k] == 0) {
//                    temp_bitmap[k] = a;
//                }
//                if (bitmap2[k] == 1) {
//                    temp_bitmap[k] = b;
//                }
//            }
            //  解密
            cpyKey[5] = xor_key[j];
            double[] group4 = Decryption.decrption(a, b, dec, cpyKey);//得到ab
            a = group4[0];
            b = group4[1];
            dec[0] = group4[2];
            dec[1] = group4[3];
            double[] longBitmap = My_dec2bin.my_dec2bin(dec, 8);
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
        double[][][] ans = new double[2][sumPixel][sumBlock];
        ans[0] = Ic;//16*分块数
        ans[1] = Ic2;
        return ans;
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
    public static double[][] AMBTC(double[][] array, int m, int n, double[] key, double[] enAuthenticationArray) {
        //TODO 处理过程key会改变，保证下次使用相同的key这里复制一份
        int len = key.length;
        double[] cpyKey = new double[len];
        for (int i = 0; i < len; i++) {
            cpyKey[i] = key[i];
        }
        int blockNum = array[0].length;//列数 即是块数
        double[] xor_key = Rand_numbers.Rand_numbers(cpyKey, blockNum, 256);
        double[] sequence = Chaotic.chaotic_maping_sequence(cpyKey[0], cpyKey[1], blockNum);
        int sumPixel = m * n;
        double[] bitmap = new double[sumPixel];//一块的16个像素矩阵
        double[][] I_compress = new double[4][blockNum];//返回结果
        //TODO 对每块进行处理 AMBTC
        for (int j = 0; j < blockNum; j++) {
            double sum = 0;
            for (int i = 0; i < sumPixel; i++) {
                sum += array[i][j];
            }
            double ave = sum / sumPixel;//均值
            double highSum = 0;
            double lowSum = 0;
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
            double b = highSum / highNum;   //高均值
            double a;
            if (highNum == sumPixel) {
                a = b;
            } else {//这里不会出现除以0的情况
                a = lowSum / lowNum;  //低均值
            }
            bitmap = My_rand.my_rand(bitmap, sequence[j], cpyKey[1]);
            double[] dec = My_bin2dec.my_bin2dec(bitmap, 8);
            double[] keyTemp = new double[len];//复制key
            for (int i = 0; i < len; i++) {
                cpyKey[i] = key[i];
            }
            keyTemp[5] = xor_key[j];
            double[] group4 = Encryption.encryption(a, b, dec, keyTemp); //异或加密 改变ab dec
            a = group4[0];
            b = group4[1];
            dec[0] = group4[2];
            dec[1] = group4[3];
            group4 = Embed.embed(a, b, dec, enAuthenticationArray[j]);     //嵌入认证信息交换ab位置 dec不变
            I_compress[0][j] = group4[0];
            I_compress[1][j] = group4[1];
            I_compress[2][j] = group4[2];
            I_compress[3][j] = group4[3];
        }
        return I_compress;
    }

    /**
     * 把RGB单独的一层进行分块处理
     *
     * @param array 单独的一层
     * @param m     分块的大小
     * @param n     分块的大小
     * @return 分块结果二维矩阵
     */
    public static double[][] im2col(double[][] array, int m, int n) {
        int height = array.length;
        int width = array[0].length;
        int blockHeight = (int) Math.ceil(height / (double) m);//横着分可以分几块
        int blockWidth = (int) Math.ceil(width / (double) n);//竖着分可以分几块
        int sumBlock = blockHeight * blockWidth;
        int blockPixel = m * n;//一块中的像素个数
        double[][] block2Array = new double[blockPixel][sumBlock];
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
     * 把分块压缩后的二维矩阵转化为单图通道的一层
     *
     * @param I_compress 分块压缩后的二维矩阵
     * @param m          分块大小
     * @param n          分块大小
     * @param height     期望生成的高
     * @param width      期望生成的宽
     * @return 单图通道的一层，组装成正常矩阵
     */
    public static double[][] col2im(double[][] I_compress, int m, int n, int height, int width) {
        int blockNum = I_compress[0].length;
        int blockPixel = I_compress.length;
        int blockHeight = (int) Math.ceil(height / (double) m);//横着分可以分几块
        double[][] bitmap2Array = new double[height][width];//返回结果
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
    public static double[][][] tamper(double[][][] array, int oh, int eh, int ow, int ew, double pix) {
        int height = array.length;
        int width = array[0].length;
        double[][][] ans = new double[height][width][4];
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
     * @param name    存储时候使用的名字
     * @param flag    0时候不加当前时间，则为覆盖存储 1为加时间的
     */
    public static void saveImageToGallery(Context context, Bitmap bmp, String name, int flag) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "a0");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = name + ".png";
        if (flag == 1) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis());
            String str = formatter.format(curDate);
            fileName = str + fileName;
        }
        File file = new File(appDir, fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 最后通知图库更新
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.parse(file.getPath()));
        context.sendBroadcast(intent);
    }

    /**
     * 输入两幅图像，输出PSNR
     *
     * @param bitmap1 图像1
     * @param bitmap2 图像2
     * @return double PSNR
     */
    public static double PSNR(Bitmap bitmap1, Bitmap bitmap2) {
        double[][][] array1 = To.BitmapToArray(bitmap1);
        double[][][] array2 = To.BitmapToArray(bitmap2);
        int width = array1[0].length;
        int height = array1.length;
        double[][] eR = new double[height][width];
        double[][] eG = new double[height][width];
        double[][] eB = new double[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                eR[i][j] = array1[i][j][1] - array2[i][j][1];
                eG[i][j] = array1[i][j][2] - array2[i][j][2];
                eB[i][j] = array1[i][j][3] - array2[i][j][3];
            }
        }
        double MSER = Matlab.mean(Matlab.multip2(eR));
        double MSEG = Matlab.mean(Matlab.multip2(eG));
        double MSEB = Matlab.mean(Matlab.multip2(eB));
        double MSE = (MSER + MSEG + MSEB) / 3;
        double ans;
        if (MSE == 0) ans = -1;
        else ans = 10 * log10(255 * 255 / MSE);
        return ans;
    }
}
