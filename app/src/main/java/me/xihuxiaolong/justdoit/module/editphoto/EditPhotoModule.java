package me.xihuxiaolong.justdoit.module.editphoto;

import android.support.annotation.Nullable;

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
public class EditPhotoModule {

    private String targetName;

    public EditPhotoModule(String targetName){
        this.targetName = targetName;
    }

    @Provides @Nullable
    String provideTargetName() {
        return targetName;
    }

    @Provides
    PlanDataService providePlanDataSource() {
        return new PlanDataServiceImpl();
    }

}