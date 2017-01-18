package me.xihuxiaolong.justdoit.module.homepage;

import org.joda.time.DateTime;

import dagger.Module;
import dagger.Provides;
import me.xihuxiaolong.justdoit.common.database.repo.DbUtil;
import me.xihuxiaolong.justdoit.common.database.repo.RedoPlanRepo;
import me.xihuxiaolong.justdoit.common.database.service.BacklogDataService;
import me.xihuxiaolong.justdoit.common.database.service.BacklogDataServiceImpl;
import me.xihuxiaolong.justdoit.common.database.service.PlanDataService;
import me.xihuxiaolong.justdoit.common.database.service.PlanDataServiceImpl;
import me.xihuxiaolong.justdoit.common.database.service.TargetDataService;
import me.xihuxiaolong.justdoit.common.database.service.TargetDataServiceImpl;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/7/6.
 */
@Module
public class HomePageModule {

    long dayTime;

    public HomePageModule(long dayTime){
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
    BacklogDataService provideBacklogDataService() {
        return new BacklogDataServiceImpl();
    }

    @Provides
    TargetDataService provideTargetDataService() {
        return new TargetDataServiceImpl();
    }

}