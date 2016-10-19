package me.xihuxiaolong.justdoit.module.editalert;

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
public class EditAlertModule {

    private long alertId;

    private long dayTime;

    public EditAlertModule(Long alertId, long dayTime){
        this.alertId = alertId;
        this.dayTime = dayTime;
    }

    @Provides @Named("alertId")
    long providePlanId() {
        return alertId;
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