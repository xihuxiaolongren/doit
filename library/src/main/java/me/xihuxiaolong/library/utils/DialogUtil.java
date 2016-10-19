package me.xihuxiaolong.library.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.afollestad.materialdialogs.MaterialDialog;

import me.xihuxiaolong.library.R;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/7/6.
 */
public class DialogUtil {

    public static MaterialDialog showDialog(Context context, CharSequence title, CharSequence content){
        return showDialog(context, title, content, null);
    }

    public static MaterialDialog showDialog(Context context, CharSequence title, CharSequence content, MaterialDialog.SingleButtonCallback positiveCallback){
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        if(title != null)
            builder.title(title);
        if(content != null)
            builder.content(content);
        if(positiveCallback != null) {
            builder.onPositive(positiveCallback)
                    .positiveText(R.string.agree)
                    .negativeText(R.string.disagree);
        }else {
            builder.negativeText(R.string.close);
        }
        return builder
                .limitIconToDefaultSize()
                .show();
    }

}
