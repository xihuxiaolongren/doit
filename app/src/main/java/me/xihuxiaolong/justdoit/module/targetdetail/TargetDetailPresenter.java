package me.xihuxiaolong.justdoit.module.targetdetail;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import javax.inject.Inject;

import me.xihuxiaolong.justdoit.common.database.localentity.TargetDO;
import me.xihuxiaolong.justdoit.common.database.manager.IRedoPlanDataSource;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/9/27.
 */

public class TargetDetailPresenter extends MvpBasePresenter<TargetDetailContract.IView> implements TargetDetailContract.IPresenter{

    @Inject
    String targetName;

    @Inject
    IRedoPlanDataSource redoPlanDataSource;

    TargetDO targetDO;

    @Inject
    public TargetDetailPresenter() {
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
        if(targetDO != null) {
            targetDO.setHeaderImageUri(headerImageUri);
            redoPlanDataSource.insertOrReplaceTargetDO(targetDO);
            if (isViewAttached())
                getView().updateTargetSuccess();
        }
    }

}
