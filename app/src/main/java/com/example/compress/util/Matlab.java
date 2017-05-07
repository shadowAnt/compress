package com.example.compress.util;

import android.graphics.Bitmap;

/**
 * Created by ShadowAnt on 2017/5/7.
 */

public class Matlab {
    public static int numel(Bitmap bitmap){
        int width = bitmap.getWidth();         //获取位图的宽
        int height = bitmap.getHeight();       //获取位图的高
        int ans = width * height;
        return ans;
    }

    /**
     * int到byte[]
     * @param
     * @return
     */
    public static byte[] intToByteArray(int[] intArray) {
        byte[] result = new byte[intArray.length * 4];
        for(int j=0; j<intArray.length; j++){
            //由高位到低位
            result[j*4+0] = (byte)((intArray[j] >> 24) & 0xFF);
            result[j*4+1] = (byte)((intArray[j] >> 16) & 0xFF);
            result[j*4+2] = (byte)((intArray[j] >> 8) & 0xFF);
            result[j*4+3] = (byte)(intArray[j] & 0xFF);
        }
        return result;
    }

    /**
     * byte[]转int
     * @param bytes
     * @return
     */
    public static int byteArrayToInt(byte[] bytes) {
        int value=0;
        //由高位到低位
        for(int i = 0; i < 4; i++) {
            int shift= (4-1-i) * 8;
            value +=(bytes[i] & 0x000000FF) << shift;//往高位游
        }
        return value;
    }


}
