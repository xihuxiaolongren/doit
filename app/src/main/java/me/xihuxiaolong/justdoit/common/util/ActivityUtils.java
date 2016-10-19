package me.xihuxiaolong.justdoit.common.util;

import android.app.Activity;

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
}
