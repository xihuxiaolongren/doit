package me.xihuxiaolong.justdoit.module.targetdetail;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import me.xihuxiaolong.justdoit.common.database.localentity.TargetDO;
import me.xihuxiaolong.justdoit.common.database.manager.IRedoPlanDataSource;
import me.xihuxiaolong.justdoit.common.event.Event;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/9/27.
 */

public class TargetDetailPresenter extends MvpBasePresenter<TargetDetailContract.IView> implements TargetDetailContract.IPresenter {

    @Inject
    String targetName;

    @Inject
    IRedoPlanDataSource redoPlanDataSource;

    TargetDO targetDO;

    @Inject
    public TargetDetailPresenter() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void loadTarget() {
        targetDO = redoPlanDataSource.getTargetByName(targetName, true);
        if (isViewAttached()) {
            getView().showTarget(targetDO);
        }
    }

    @Override
    public void updateTarget(String headerImageUri) {
        if (targetDO != null) {
            targetDO.setHeaderImageUri(headerImageUri);
            redoPlanDataSource.insertOrReplaceTargetDO(targetDO);
            EventBus.getDefault().post(new Event.UpdateTarget(targetName));
            if (isViewAttached())
                getView().updateTargetSuccess();
        }
    }

    @Override
    public void deleteTarget() {
        if (targetName != null) {
            redoPlanDataSource.deleteTargetByName(targetName);
            EventBus.getDefault().post(new Event.DeleteTarget(targetName));
            if (isViewAttached())
                getView().deleteTargetSuccess();
        }
    }

    @Subscribe
    public void onEvent(Event.UpdateTarget updateTargetEvent) {
        loadTarget();
    }

    @Subscribe
    public void onEvent(Event.DeleteTarget deleteTargetEvent) {
        loadTarget();
    }

}