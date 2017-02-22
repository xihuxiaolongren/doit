package me.xihuxiaolong.justdoit.module.easybackloglist;

import org.joda.time.DateTime;

import dagger.Module;
import dagger.Provides;
import me.xihuxiaolong.justdoit.common.database.service.BacklogDataService;
import me.xihuxiaolong.justdoit.common.database.service.BacklogDataServiceImpl;
import me.xihuxiaolong.justdoit.common.database.service.PlanDataService;
import me.xihuxiaolong.justdoit.common.database.service.PlanDataServiceImpl;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/7/6.
 */
@Module
public class EasyBacklogListModule {

    long dayTime;

    public EasyBacklogListModule(long dayTime){
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

}