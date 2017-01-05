package me.xihuxiaolong.justdoit.module.editphoto;

import android.support.annotation.Nullable;

import javax.inject.Named;

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
    IPlanDataSource providePlanDataSource() {
        return new PlanDataSource();
    }

}