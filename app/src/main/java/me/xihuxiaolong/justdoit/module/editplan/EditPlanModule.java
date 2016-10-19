package me.xihuxiaolong.justdoit.module.editplan;

import javax.inject.Named;

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
public class EditPlanModule {

    private long planId;

    private long dayTime;

    public EditPlanModule(Long planId, long dayTime){
        this.planId = planId;
        this.dayTime = dayTime;
    }

    @Provides @Named("planId")
    long providePlanId() {
        return planId;
    }

    @Provides @Named("dayTime")
    long provideDayTime() {
        return dayTime;
    }

    @Provides
    IPlanDataSource providePlanDataSource() {
        return new PlanDataSource();
    }
}