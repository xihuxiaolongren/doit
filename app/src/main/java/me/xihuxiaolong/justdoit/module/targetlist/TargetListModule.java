package me.xihuxiaolong.justdoit.module.targetlist;

import dagger.Module;
import dagger.Provides;
import me.xihuxiaolong.justdoit.common.database.repo.DbUtil;
import me.xihuxiaolong.justdoit.common.database.service.PlanDataService;
import me.xihuxiaolong.justdoit.common.database.service.PlanHistoryDataService;
import me.xihuxiaolong.justdoit.common.database.service.RedoPlanDataService;
import me.xihuxiaolong.justdoit.common.database.repo.PlanRepo;
import me.xihuxiaolong.justdoit.common.database.service.PlanDataServiceImpl;
import me.xihuxiaolong.justdoit.common.database.service.PlanHistoryDataServiceImpl;
import me.xihuxiaolong.justdoit.common.database.repo.RedoPlanRepo;
import me.xihuxiaolong.justdoit.common.database.service.RedoPlanDataServiceImpl;
import me.xihuxiaolong.justdoit.common.database.repo.TargetRepo;
import me.xihuxiaolong.justdoit.common.database.service.TargetDataService;
import me.xihuxiaolong.justdoit.common.database.service.TargetDataServiceImpl;

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
    TargetDataService provideTargetDataService() {
        return new TargetDataServiceImpl();
    }

    @Provides
    PlanDataService providePlanDataService() {
        return new PlanDataServiceImpl();
    }

    @Provides
    PlanHistoryDataService providePlanHistoryDataService() {
        return new PlanHistoryDataServiceImpl();
    }

}