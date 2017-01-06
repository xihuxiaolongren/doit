package me.xihuxiaolong.justdoit.module.redoplanlist;

import android.support.annotation.Nullable;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.List;

import javax.inject.Inject;

import me.xihuxiaolong.justdoit.common.database.localentity.RedoPlanDO;
import me.xihuxiaolong.justdoit.common.database.service.PlanDataService;
import me.xihuxiaolong.justdoit.common.database.service.RedoPlanDataService;
import me.xihuxiaolong.justdoit.common.database.repo.RedoPlanRepo;
import me.xihuxiaolong.justdoit.common.database.service.TargetDataService;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/9/27.
 */

public class RedoPlanListPresenter extends MvpBasePresenter<RedoPlanListContract.IView> implements RedoPlanListContract.IPresenter {

    @Inject @Nullable
    String targetName;

    @Inject
    RedoPlanDataService redoPlanDataSource;

    @Inject
    PlanDataService planDataSource;

    List<RedoPlanDO> redoPlanDOs;

    @Inject
    public RedoPlanListPresenter() {
    }

    @Override
    public void loadRedoPlans() {
        redoPlanDOs = redoPlanDataSource.listRedoPlanDOs();
        if (isViewAttached()) {
            getView().showRedoPlans(redoPlanDOs);
        }
    }

}