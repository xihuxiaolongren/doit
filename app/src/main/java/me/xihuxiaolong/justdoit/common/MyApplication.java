package me.xihuxiaolong.justdoit.common;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;

import me.xihuxiaolong.justdoit.BuildConfig;
import me.xihuxiaolong.justdoit.common.dagger.component.AppComponent;
import me.xihuxiaolong.justdoit.common.dagger.component.DaggerAppComponent;
import me.xihuxiaolong.justdoit.common.dagger.module.AppModule;
import me.xihuxiaolong.justdoit.common.database.localentity.UserSettingsDO;
import me.xihuxiaolong.justdoit.common.database.manager.IUserSettingsDataSource;
import me.xihuxiaolong.justdoit.common.database.manager.UserSettingsDataSource;
import timber.log.Timber;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/7/6.
 */
//@ReportsCrashes(
//        httpMethod = HttpSender.Method.PUT,
//        reportType = HttpSender.Type.JSON,
//        formUri = "http://10.0.50.73:5984/acra-generalcomponent/_design/acra-storage/_update/report",
//        formUriBasicAuthLogin = "yxl",
//        formUriBasicAuthPassword = "123456"
//)
public class MyApplication extends Application {

    private static MyApplication myApplication;

    AppComponent mAppComponent;

    IUserSettingsDataSource userSettingsDataSource;

    public static MyApplication getInstance() {
        return myApplication;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //内存泄露
        LeakCanary.install(this);

        //acra初始化
//        ACRA.init(this);

        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO);
        myApplication = this;
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree(){
                @Override protected void log(int priority, String tag, String message, Throwable t) {
                    Logger.log(priority, tag, message, t);
                }
            });
        } else {
            Timber.plant(new CrashReportingTree());
        }

        userSettingsDataSource = new UserSettingsDataSource();
        if(userSettingsDataSource.getUserSettingsDOById(-1L) == null){
            UserSettingsDO userSettingsDO = new UserSettingsDO();
            userSettingsDO.setUserId(-1L);
            userSettingsDO.setMotto("此时，此地，此身");
            userSettingsDO.setMottoPlanStart("一日之计在于晨");
            userSettingsDO.setMottoPlanEnd("今日事毕");
            userSettingsDataSource.insertOrReplaceUserSettingsDO(userSettingsDO);
        }
    }

    public AppComponent getAppComponent(){
        return mAppComponent;
    }

    /** A tree which logs important information for crash reporting. */
    private static class CrashReportingTree extends Timber.Tree {
        @Override protected void log(int priority, String tag, String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }
            Logger.log(priority, tag, message, t);
//            FakeCrashLibrary.log(priority, tag, message);
//
//            if (t != null) {
//                if (priority == Log.ERROR) {
//                    FakeCrashLibrary.logError(t);
//                } else if (priority == Log.WARN) {
//                    FakeCrashLibrary.logWarning(t);
//                }
//            }
        }
    }
}
