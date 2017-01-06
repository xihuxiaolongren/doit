package me.xihuxiaolong.justdoit.module.redoplandetail;

import dagger.Module;
import dagger.Provides;
import me.xihuxiaolong.justdoit.common.database.localentity.RedoPlanDO;
import me.xihuxiaolong.justdoit.common.database.repo.DbUtil;
import me.xihuxiaolong.justdoit.common.database.service.RedoPlanDataService;
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
public class RedoPlanDetailModule {


    RedoPlanDO redoPlanDO;

    public RedoPlanDetailModule(RedoPlanDO redoPlanDO){
        this.redoPlanDO = redoPlanDO;
    }

    @Provides
    RedoPlanDO provideRedoPlan(){
        return redoPlanDO;
    }

    @Provides
    RedoPlanDataService provideRedoPlanDataSource() {
        return new RedoPlanDataServiceImpl();
    }

}