package me.xihuxiaolong.justdoit.module.targetlist;

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

public class TargetListPresenter extends MvpBasePresenter<TargetListContract.IView> implements TargetListContract.IPresenter{

    @Inject
    IRedoPlanDataSource redoPlanDataSource;

    @Inject
    public TargetListPresenter() {
    }

    @Override
    public void loadTargets() {
        List<TargetDO> targetDOs = redoPlanDataSource.listAllTarget(true);
        if(targetDOs == null)
            targetDOs = new ArrayList<>();
        for(int i = 0; i < 10; ++i){
            TargetDO targetDO = new TargetDO("重复任务 - " + i, 0, 0 , "", 3);
            List<RedoPlanDO> redoPlanDOs = new ArrayList<>();
            for(int j = 0; j < NumberUtils.randInt(0, 3); ++j){
                RedoPlanDO redoPlanDO = new RedoPlanDO();
                redoPlanDO.setContent("测试任务" + j);
                redoPlanDO.setRepeatMode(NumberUtils.randInt(0, 6));
                redoPlanDOs.add(redoPlanDO);
            }
            targetDO.setRedoPlanDOList(redoPlanDOs);
            targetDOs.add(targetDO);
        }
        if (isViewAttached()) {
            getView().showTargets(targetDOs);
        }
    }

    @Override
    public void createTarget(String name) {
        TargetDO targetDO = new TargetDO();
        targetDO.setName(name);
        redoPlanDataSource.insertOrReplaceTargetDO(targetDO);
        if(isViewAttached())
            getView().createTargetSuccess(name);
    }

}
