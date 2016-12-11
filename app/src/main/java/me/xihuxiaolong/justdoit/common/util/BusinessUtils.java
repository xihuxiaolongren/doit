package me.xihuxiaolong.justdoit.common.util;

import me.xihuxiaolong.justdoit.R;
import me.xihuxiaolong.justdoit.common.MyApplication;

/**
 * Created by yangxiaolong on 2016/12/11.
 */

public class BusinessUtils {

    public static int repeatMode(int type, int[] repeatSelectedArr) {
        int repeatMode = 0;
        switch (type) {
            case 0:
                for (int i = 0; i < 7; ++i)
                    repeatMode |= 1 << i;
                break;
            case 1:
                for (int i = 0; i < 5; ++i)
                    repeatMode |= 1 << i;
                break;
            case 2:
                for (int i : repeatSelectedArr) {
                    repeatMode |= 1 << i;
                }
                break;
        }
        return repeatMode;
    }

    public static String repeatModeStr(int repeatMode) {
        StringBuilder builder = new StringBuilder();
        switch (repeatMode) {
            case 127:
                builder.append("每日");
                return builder.toString();
            case 31:
                builder.append("周一至周五");
                return builder.toString();
            default:
                CharSequence[] weekArr = MyApplication.getInstance().getResources().getTextArray(R.array.repeat_week_arr);
                for (int i = 0; i < 7; ++i) {
                    if ((repeatMode & (1 << i)) != 0) {
                        CharSequence weekDay = weekArr[i];
                        builder.append(weekDay);
                        builder.append(",");
                    }
                }
                if (builder.length() > 0)
                    return builder.substring(0, builder.length() - 1);
                return null;
        }
    }
}
