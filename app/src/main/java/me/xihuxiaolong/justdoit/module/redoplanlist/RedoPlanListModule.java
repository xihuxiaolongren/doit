package me.xihuxiaolong.justdoit.module.redoplanlist;

import android.support.annotation.Nullable;

import dagger.Module;
import dagger.Provides;
import me.xihuxiaolong.justdoit.common.database.manager.IPlanDataSource;
import me.xihuxiaolong.justdoit.common.database.manager.IRedoPlanDataSource;
import me.xihuxiaolong.justdoit.common.database.manager.PlanDataSource;
import me.xihuxiaolong.justdoit.common.database.manager.RedoPlanDataSource;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/7/6.
 */
@Module
public class RedoPlanListModule {

    String targetName;

    public RedoPlanListModule(String targetName){
        this.targetName = targetName;
    }

    @Provides @Nullable
    String provideTargetName() {
        return targetName;
    }

    @Provides
    IRedoPlanDataSource provideRedoPlanDataSource() {
        return new RedoPlanDataSource();
    }

    @Provides
    IPlanDataSource providePlanDataSource() {
        return new PlanDataSource();
    }

}