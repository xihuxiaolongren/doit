package me.xihuxiaolong.library.utils;

import android.graphics.Color;

/**
 * Created by yangxiaolong on 15/11/28.
 */
public class ColorUtils {

    public static final int blackColor = Color.parseColor("#000000");
    public static final int grayColor = Color.parseColor("#999999");
    public static final int whiteColor = Color.parseColor("#ffffff");

    /**
     * 根据backgroundColor 判断 font or other icon color
     * 参考：@android.support.v4.graphics.ColorUtils.calculateLuminance 等
     * @param backgroundColor
     * @return
     */
    public static int contrastColor(int backgroundColor) {
        int r = (backgroundColor >> 16) & 0xFF;
        int g = (backgroundColor >> 8) & 0xFF;
        int b = (backgroundColor >> 0) & 0xFF;

        // Counting the perceptive luminance - human eye favors green color...
        double a = 1 - ( 0.299 * r + 0.587 * g + 0.114 * b)/255;

        if (a < 0.2)
            return grayColor;
        else
            return whiteColor;
    }

    public static int getDarkColor(int color){
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8f; // value component
        return Color.HSVToColor(hsv);
    }

    public static int getLighterColor(int color){
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] = 0.2f + 0.8f * hsv[2];
        return Color.HSVToColor(hsv);
    }

}
