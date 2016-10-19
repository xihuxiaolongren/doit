package me.xihuxiaolong.justdoit.module.planhistory;

import dagger.Module;
import dagger.Provides;
import me.xihuxiaolong.justdoit.common.database.manager.IPlanDataSource;
import me.xihuxiaolong.justdoit.common.database.manager.PlanDataSource;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/7/6.
 */
@Module
public class PlanHistoryActivityModule {

    public PlanHistoryActivityModule(){}

    @Provides
    IPlanDataSource providePlanDataSource() {
        return new PlanDataSource();
    }

}