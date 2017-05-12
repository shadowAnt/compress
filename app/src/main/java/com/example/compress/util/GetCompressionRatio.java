package com.example.compress.util;

import android.graphics.Bitmap;

/**
 * Created by ShadowAnt on 2017/5/12.
 */

public class GetCompressionRatio {
    public static double getCompressionRatio(Bitmap ori, Bitmap result) {
        int oriHeight = ori.getHeight();
        int oriWidth = ori.getWidth();
        int resultHeight = result.getHeight();
        int resultWidth = result.getWidth();
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.0000");
        double ans = (resultHeight * resultWidth) / (double)(oriHeight * oriWidth) * 100;
        df.format(ans);
        return ans;
    }
}
