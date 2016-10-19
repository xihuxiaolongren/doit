package me.xihuxiaolong.justdoit.common.util;

import android.support.v7.app.AppCompatDelegate;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/10/17.
 */

public class DayNightModeUtils {


    public static boolean isCurrentNight(){
        return AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
    }
}
