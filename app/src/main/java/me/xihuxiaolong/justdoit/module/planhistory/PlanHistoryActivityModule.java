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
public class PlanHistoryActivityModule {

    public PlanHistoryActivityModule(){}

    @Provides
    PlanDataService providePlanDataSource() {
        return new PlanDataServiceImpl();
    }

}