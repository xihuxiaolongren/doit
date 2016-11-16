package me.xihuxiaolong.justdoit.module.targetlist;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import me.xihuxiaolong.justdoit.common.cache.ICacheService;
import me.xihuxiaolong.justdoit.common.cache.entity.UserSettings;
import me.xihuxiaolong.justdoit.common.database.localentity.RedoPlanDO;
import me.xihuxiaolong.justdoit.common.database.localentity.TargetDO;
import me.xihuxiaolong.justdoit.common.database.manager.IPlanDataSource;
import me.xihuxiaolong.justdoit.common.database.manager.IRedoPlanDataSource;
import me.xihuxiaolong.justdoit.common.event.Event;
import me.xihuxiaolong.library.utils.CollectionUtils;
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
        List<TargetDO> targetDOs = redoPlanDataSource.listAllTarget();
        if(targetDOs == null)
            targetDOs = new ArrayList<>();
        for(int i = 0; i < 10; ++i){
            TargetDO targetDO = new TargetDO("重复任务 - " + i, 3);
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

}
