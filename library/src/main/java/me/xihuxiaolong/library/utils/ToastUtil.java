package me.xihuxiaolong.library.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/7/6.
 */
public class ToastUtil {

    private Context mContext;

    public ToastUtil(Context context){
        this.mContext = context;
    }

    public void showToast(String message, int duration){
        Toast.makeText(mContext,message, duration).show();
    }

    public void showToast(String message, int duration, int gravity){
        Toast toast = Toast.makeText(mContext,message, duration);
        toast.setGravity(gravity, 0, 0);
        toast.show();
    }

    public static void showToast1(Context context, String message, int duration){
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }
}
