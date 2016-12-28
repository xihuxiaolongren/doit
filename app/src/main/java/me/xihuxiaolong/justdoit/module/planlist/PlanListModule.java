package me.xihuxiaolong.justdoit.module.planlist;

import org.joda.time.DateTime;

import dagger.Module;
import dagger.Provides;
import me.xihuxiaolong.justdoit.common.database.manager.IPlanDataSource;
import me.xihuxiaolong.justdoit.common.database.manager.IRedoPlanDataSource;
import me.xihuxiaolong.justdoit.common.database.manager.PlanDataSource;
import me.xihuxiaolong.justdoit.common.database.manager.RedoPlanDataSource;

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
    IPlanDataSource providePlanDataSource() {
        return new PlanDataSource();
    }

    @Provides
    IRedoPlanDataSource provideRedoPlanDataSource() {
        return new RedoPlanDataSource();
    }

}