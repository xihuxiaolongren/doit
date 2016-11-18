package me.xihuxiaolong.justdoit.module.targetdetail;

import android.support.annotation.Nullable;

import dagger.Module;
import dagger.Provides;
import me.xihuxiaolong.justdoit.common.database.manager.IRedoPlanDataSource;
import me.xihuxiaolong.justdoit.common.database.manager.RedoPlanDataSource;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/7/6.
 */
@Module
public class TargetDetailModule {

    String targetName;

    public TargetDetailModule(String targetName){
        this.targetName = targetName;
    }

    @Provides
    String provideTargetName() {
        return targetName;
    }

    @Provides
    IRedoPlanDataSource provideRedoPlanDataSource() {
        return new RedoPlanDataSource();
    }

}