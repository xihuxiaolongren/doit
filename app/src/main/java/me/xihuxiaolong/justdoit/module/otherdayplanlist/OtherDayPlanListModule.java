package me.xihuxiaolong.justdoit.module.otherdayplanlist;

import org.joda.time.DateTime;

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
public class OtherDayPlanListModule {

    public OtherDayPlanListModule(){}

    @Provides
    long provideDayTime() {
        return DateTime.now().withTimeAtStartOfDay().getMillis();
    }

    @Provides
    IPlanDataSource providePlanDataSource() {
        return new PlanDataSource();
    }

//    @Provides
//    IUserSettingsDataSource provideUserSettingsDataSource() {
//        return new UserSettingsDataSource();
//    }

}