package com.example.compress.util;

/**
 * Created by ShadowAnt on 2017/5/7.
 */

public class RGB2Grey {
    public static int RGB2Grey(int[] pixels, int width, int i, int j){
        int grey = pixels[width * i + j];
        int red = ((grey & 0x00FF0000) >> 16);
        int green = ((grey & 0x0000FF00) >> 8);
        int blue = (grey & 0x000000FF);
        grey = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
        return grey;
    }
}
