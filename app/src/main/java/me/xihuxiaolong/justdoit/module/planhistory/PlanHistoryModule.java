package me.xihuxiaolong.justdoit.module.planhistory;

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
public class PlanHistoryModule {

    private long dayTime;

    public PlanHistoryModule(long dayTime){
        this.dayTime = dayTime;
    }

    @Provides
    PlanDataService providePlanDataSource() {
        return new PlanDataServiceImpl();
    }

    @Provides
    long provideDayTime() {
        return dayTime;
    }

}