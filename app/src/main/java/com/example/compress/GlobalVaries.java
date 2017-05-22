package com.example.compress;

import android.app.Application;
import android.graphics.Bitmap;

/**
 * Created by ShadowAnt on 2017/5/23.
 */

public final class GlobalVaries extends Application {
    private Bitmap encodeBinaryBitmap;
    private Bitmap resultBitmap;
    private Bitmap tamperResultBitmap;
    private Boolean isEn = false;
    private Boolean isTamper = false;

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

    public Bitmap getTamperResultBitmap() {
        return tamperResultBitmap;
    }

    public void setTamperResultBitmap(Bitmap tamperResultBitmap) {
        this.tamperResultBitmap = tamperResultBitmap;
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

    public Bitmap getResultBitmap() {
        return resultBitmap;
    }

    public void setResultBitmap(Bitmap resultBitmap) {
        this.resultBitmap = resultBitmap;
    }

    public Bitmap getEncodeBinaryBitmap() {
        return encodeBinaryBitmap;
    }

    public void setEncodeBinaryBitmap(Bitmap encodeBinaryBitmap) {
        this.encodeBinaryBitmap = encodeBinaryBitmap;
    }
}
