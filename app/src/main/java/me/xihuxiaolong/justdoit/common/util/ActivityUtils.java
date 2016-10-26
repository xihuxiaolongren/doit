package me.xihuxiaolong.justdoit.common.util;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import me.xihuxiaolong.justdoit.common.MyApplication;
import me.xihuxiaolong.justdoit.common.dagger.component.AppComponent;


/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/8/10.
 */
public class ActivityUtils {

    public static AppComponent getAppComponent(Activity activity){
        return ((MyApplication) (activity.getApplication())).getAppComponent();
    }

    public static void openSoftKeyboard(Activity activity, EditText editText){
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInputFromWindow(
                editText.getApplicationWindowToken(),
                InputMethodManager.SHOW_FORCED, 0);
    }

    public static void hideSoftKeyboard(EditText editText, ViewGroup focusOtherView){
        InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        focusOtherView.requestFocus();
        editText.clearFocus();
    }
}
