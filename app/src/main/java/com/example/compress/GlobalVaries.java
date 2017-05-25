package com.example.compress;

import android.app.Application;
import android.graphics.Bitmap;

/**
 * Created by ShadowAnt on 2017/5/23.
 */

public final class GlobalVaries extends Application {
    private Boolean isEn = false;
    private Boolean isTamper = false;
    private String URL;
    private double[][][] encodeBinaryArray;
    private double[] key;

    public double[] getKey() {
        return key;
    }

    public void setKey(double[] key) {
        this.key = key;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    private double[][][] resultArray;
    private double[][][] tamperResultArray;

    public double[][][] getEncodeBinaryArray() {
        return encodeBinaryArray;
    }

    public void setEncodeBinaryArray(double[][][] encodeBinaryArray) {
        this.encodeBinaryArray = encodeBinaryArray;
    }

    public double[][][] getResultArray() {
        return resultArray;
    }

    public void setResultArray(double[][][] resultArray) {
        this.resultArray = resultArray;
    }

    public double[][][] getTamperResultArray() {
        return tamperResultArray;
    }

    public void setTamperResultArray(double[][][] tamperResultArray) {
        this.tamperResultArray = tamperResultArray;
    }

    public void toNull(){//清零
        encodeBinaryArray = null;
        resultArray = null;
        tamperResultArray = null;
        isEn = false;
        isTamper = false;
        URL = null;
    }

    public Boolean getEn() {
        return isEn;
    }

    public void setEn(Boolean en) {
        isEn = en;
    }

    public Boolean getTamper() {
        return isTamper;
    }

    public void setTamper(Boolean tamper) {
        isTamper = tamper;
    }

    private int originHeight;
    private int originWidth;

    public int getOriginHeight() {
        return originHeight;
    }

    public void setOriginHeight(int originHeight) {
        this.originHeight = originHeight;
    }

    public int getOriginWidth() {
        return originWidth;
    }

    public void setOriginWidth(int originWidth) {
        this.originWidth = originWidth;
    }
}
