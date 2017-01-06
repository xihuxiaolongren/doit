package me.xihuxiaolong.justdoit.module.redoplandetail;

import android.text.TextUtils;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import me.xihuxiaolong.justdoit.common.database.localentity.RedoPlanDO;
import me.xihuxiaolong.justdoit.common.database.service.RedoPlanDataService;
import me.xihuxiaolong.justdoit.common.database.repo.RedoPlanRepo;
import me.xihuxiaolong.justdoit.common.event.Event;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/9/27.
 */

public class RedoPlanPresenter extends MvpBasePresenter<RedoPlanContract.IView> implements RedoPlanContract.IPresenter {

    @Inject
    RedoPlanDO redoPlanDO;

    @Inject
    RedoPlanDataService redoPlanDataService;

    @Inject
    public RedoPlanPresenter() {
    }

    @Override
    public void loadRedoPlan() {
        if (isViewAttached()) {
            getView().showRedoPlan(redoPlanDO);
        }
    }

    @Override
    public void removeRedoPlan() {
        redoPlanDataService.deleteRedoPlanById(redoPlanDO.getId());
        if(!TextUtils.isEmpty(redoPlanDO.getTargetName()))
            EventBus.getDefault().post(new Event.UpdateTarget(redoPlanDO.getTargetName()));
        if(isViewAttached())
            getView().removeRedoPlanSuccess();
    }
}