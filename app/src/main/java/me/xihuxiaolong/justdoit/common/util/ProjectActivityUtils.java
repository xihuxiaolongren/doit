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
public class ProjectActivityUtils {

    public static AppComponent getAppComponent(Activity activity){
        return ((MyApplication) (activity.getApplication())).getAppComponent();
    }

}
