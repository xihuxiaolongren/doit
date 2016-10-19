package me.xihuxiaolong.justdoit.common.dagger.module;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.xihuxiaolong.library.utils.ToastUtil;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/7/6.
 */
@Module
public class AppModule {

    Application application;

    public AppModule(Application application){
        this.application = application;
    }

    @Provides
    public Application provideApplication(){
        return application;
    }

    @Provides
    @Singleton
    public ToastUtil provideToastUtil(){
        return new ToastUtil(application);
    }

}