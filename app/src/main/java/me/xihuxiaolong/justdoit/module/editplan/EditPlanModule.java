package me.xihuxiaolong.justdoit.module.editplan;

import android.support.annotation.Nullable;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import me.xihuxiaolong.justdoit.common.database.service.PlanDataService;
import me.xihuxiaolong.justdoit.common.database.service.PlanDataServiceImpl;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/7/6.
 */
@Module
public class EditPlanModule {

    private long planId;

    private long dayTime;

    private String targetName;

    public EditPlanModule(Long planId, long dayTime, String targetName){
        this.planId = planId;
        this.dayTime = dayTime;
        this.targetName = targetName;
    }

    @Provides @Named("planId")
    long providePlanId() {
        return planId;
    }

    @Provides @Named("dayTime")
    long provideDayTime() {
        return dayTime;
    }

    @Provides @Nullable
    String provideTargetName() {
        return targetName;
    }

    @Provides
    PlanDataService providePlanDataSource() {
        return new PlanDataServiceImpl();
    }
}