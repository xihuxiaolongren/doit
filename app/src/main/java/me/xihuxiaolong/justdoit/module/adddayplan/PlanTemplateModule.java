package me.xihuxiaolong.justdoit.module.adddayplan;

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
public class PlanTemplateModule {

    private long dayTime;

    public PlanTemplateModule(long dayTime){
        this.dayTime = dayTime;
    }

    @Provides
    IPlanDataSource providePlanDataSource() {
        return new PlanDataSource();
    }

    @Provides
    long provideDayTime() {
        return dayTime;
    }

}