package me.xihuxiaolong.justdoit.module.targetdetail;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import me.xihuxiaolong.justdoit.common.database.localentity.RedoPlanDO;
import me.xihuxiaolong.justdoit.common.database.localentity.TargetDO;
import me.xihuxiaolong.justdoit.common.database.manager.IRedoPlanDataSource;
import me.xihuxiaolong.library.utils.NumberUtils;

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

    @Inject
    public TargetDetailPresenter() {
    }

    @Override
    public void loadTarget() {
        TargetDO targetDO = redoPlanDataSource.getTargetByName(targetName, true);
        if (isViewAttached()) {
            getView().showTarget(targetDO);
        }
    }

}
