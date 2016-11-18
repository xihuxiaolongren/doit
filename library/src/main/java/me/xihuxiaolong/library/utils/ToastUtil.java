package me.xihuxiaolong.library.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/7/6.
 */
public class ToastUtil {

    public static void showToast(Context context, String message, int duration, int gravity){
        Toast toast = Toast.makeText(context, message, duration);
        toast.setGravity(gravity, 0, 0);
        toast.show();
    }

    public static void showToast(Context context, String message, int duration){
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

}
