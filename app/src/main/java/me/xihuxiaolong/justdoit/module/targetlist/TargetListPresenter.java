package me.xihuxiaolong.justdoit.module.targetlist;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import javax.inject.Inject;

import me.xihuxiaolong.justdoit.common.database.localentity.TargetDO;
import me.xihuxiaolong.justdoit.common.database.manager.IRedoPlanDataSource;
import me.xihuxiaolong.justdoit.common.event.Event;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/9/27.
 */

public class TargetListPresenter extends MvpBasePresenter<TargetListContract.IView> implements TargetListContract.IPresenter {

    @Inject
    IRedoPlanDataSource redoPlanDataSource;

    @Inject
    public TargetListPresenter() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void loadTargets() {
        List<TargetDO> targetDOs = redoPlanDataSource.listAllTarget(true);
        if (isViewAttached()) {
            getView().showTargets(targetDOs);
        }
    }

    @Override
    public void createTarget(String name, int type) {
        TargetDO targetDO = new TargetDO();
        targetDO.setName(name);
        targetDO.setType(type);
        redoPlanDataSource.insertOrReplaceTargetDO(targetDO);
        if (isViewAttached()) {
            getView().createTargetSuccess(targetDO);
        }
        EventBus.getDefault().post(new Event.AddTarget(targetDO.getName()));
    }

    @Subscribe
    public void onEvent(Event.AddTarget addTargetEvent) {
        loadTargets();
    }

    @Subscribe
    public void onEvent(Event.UpdateTarget updateTargetEvent) {
        loadTargets();
    }

    @Subscribe
    public void onEvent(Event.DeleteTarget deleteTargetEvent) {
        loadTargets();
    }
}
