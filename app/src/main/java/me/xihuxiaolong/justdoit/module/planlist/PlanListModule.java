package me.xihuxiaolong.justdoit.module.planlist;

import org.joda.time.DateTime;

import dagger.Module;
import dagger.Provides;
import me.xihuxiaolong.justdoit.common.database.repo.DbUtil;
import me.xihuxiaolong.justdoit.common.database.service.PlanDataService;
import me.xihuxiaolong.justdoit.common.database.service.RedoPlanDataService;
import me.xihuxiaolong.justdoit.common.database.repo.PlanRepo;
import me.xihuxiaolong.justdoit.common.database.service.PlanDataServiceImpl;
import me.xihuxiaolong.justdoit.common.database.repo.RedoPlanRepo;
import me.xihuxiaolong.justdoit.common.database.service.RedoPlanDataServiceImpl;
import me.xihuxiaolong.justdoit.common.database.service.TargetDataService;
import me.xihuxiaolong.justdoit.common.database.service.TargetDataServiceImpl;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/7/6.
 */
@Module
public class PlanListModule {

    long dayTime;

    public PlanListModule(long dayTime){
        this.dayTime = dayTime;
    }

    @Provides
    long provideDayTime() {
        if(dayTime == -1L)
            return DateTime.now().withTimeAtStartOfDay().getMillis();
        else
            return dayTime;
    }

    @Provides
    PlanDataService providePlanDataService() {
        return new PlanDataServiceImpl();
    }

    @Provides
    TargetDataService provideTargetDataService() {
        return new TargetDataServiceImpl();
    }

    @Provides
    RedoPlanRepo provideRedoPlanDBService() {
        return DbUtil.getRedoPlanRepo();
    }

}