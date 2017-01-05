package me.xihuxiaolong.justdoit.module.redoplanlist;

import android.support.annotation.Nullable;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.joda.time.DateTime;

import java.util.List;

import javax.inject.Inject;

import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
import me.xihuxiaolong.justdoit.common.database.localentity.RedoPlanDO;
import me.xihuxiaolong.justdoit.common.database.localentity.TargetDO;
import me.xihuxiaolong.justdoit.common.database.manager.IPlanDataSource;
import me.xihuxiaolong.justdoit.common.database.manager.IRedoPlanDataSource;
import me.xihuxiaolong.justdoit.common.event.Event;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/9/27.
 */

public class RedoPlanListPresenter extends MvpBasePresenter<RedoPlanListContract.IView> implements RedoPlanListContract.IPresenter {

    @Inject @Nullable
    String targetName;

    @Inject
    IRedoPlanDataSource redoPlanDataSource;

    @Inject
    IPlanDataSource planDataSource;

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