package me.xihuxiaolong.justdoit.module.redoplandetail;

import dagger.Module;
import dagger.Provides;
import me.xihuxiaolong.justdoit.common.database.localentity.RedoPlanDO;
import me.xihuxiaolong.justdoit.common.database.manager.IRedoPlanDataSource;
import me.xihuxiaolong.justdoit.common.database.manager.RedoPlanDataSource;

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
    IRedoPlanDataSource provideRedoPlanDataSource() {
        return new RedoPlanDataSource();
    }

}