package me.xihuxiaolong.justdoit.common.util;

import android.support.v7.app.AppCompatDelegate;

import org.joda.time.DateTime;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/10/17.
 */

public class DayNightModeUtils {


    public static boolean isCurrentNight(){
        return AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
    }

    public static void setManualTheme(int dayNightTheme){
        switch (dayNightTheme){
            case 0:
                AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case 1:
                AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_YES);
                break;
        }
    }

    public static boolean setAutoTheme(int dayStartHour, int dayStartMinute, int nightStartHour, int nightStartMinute){
        int hour = DateTime.now().getHourOfDay();
        int minute = DateTime.now().getMinuteOfHour();
        int preMode = AppCompatDelegate.getDefaultNightMode();
        if((hour * 60 + minute) > (dayStartHour * 60 + dayStartMinute) && (hour * 60 + minute) < (nightStartHour * 60 + nightStartMinute))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        return preMode != AppCompatDelegate.getDefaultNightMode();
    }
}
