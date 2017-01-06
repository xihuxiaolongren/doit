package me.xihuxiaolong.justdoit.module.targetdetail;

import dagger.Module;
import dagger.Provides;
import me.xihuxiaolong.justdoit.common.database.repo.DbUtil;
import me.xihuxiaolong.justdoit.common.database.service.PlanDataService;
import me.xihuxiaolong.justdoit.common.database.service.RedoPlanDataService;
import me.xihuxiaolong.justdoit.common.database.repo.PlanRepo;
import me.xihuxiaolong.justdoit.common.database.service.PlanDataServiceImpl;
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
public class TargetDetailModule {

    String targetName;

    public TargetDetailModule(String targetName){
        this.targetName = targetName;
    }

    @Provides
    String provideTargetName() {
        return targetName;
    }

    @Provides
    TargetDataService provideTargetDataService() {
        return new TargetDataServiceImpl();
    }

    @Provides
    PlanDataService providePlanDataSource() {
        return new PlanDataServiceImpl();
    }

}