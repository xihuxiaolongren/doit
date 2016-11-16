package me.xihuxiaolong.justdoit.module.targetdetail;

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
public class TargetListModule {

    public TargetListModule(){
    }

    @Provides
    IRedoPlanDataSource provideRedoPlanDataSource() {
        return new RedoPlanDataSource();
    }

}