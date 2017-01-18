package me.xihuxiaolong.justdoit.module.easybackloglist;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import javax.inject.Inject;

import me.xihuxiaolong.justdoit.common.cache.ICacheService;
import me.xihuxiaolong.justdoit.common.database.localentity.BacklogDO;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
import me.xihuxiaolong.justdoit.common.database.service.BacklogDataService;
import me.xihuxiaolong.justdoit.common.database.service.PlanDataService;
import me.xihuxiaolong.justdoit.common.event.Event;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/9/27.
 */

public class EasyBacklogListPresenter extends MvpBasePresenter<EasyBacklogListContract.IView> implements EasyBacklogListContract.IPresenter {

    @Inject
    long dayTime;

    @Inject
    BacklogDataService backlogDataService;

    @Inject
    ICacheService cacheService;

    @Inject
    public EasyBacklogListPresenter() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void loadBacklogs() {
        List<BacklogDO> backlogDOs = backlogDataService.listBacklogDOs();
        if (isViewAttached()) {
            getView().showBacklogs(backlogDOs);
        }
//        EventBus.getDefault().post(new Event.DeletePlan(backlogDO));
    }

    @Override
    public void deleteBacklog(BacklogDO backlogDO) {
        backlogDataService.deleteBacklogById(backlogDO.getId());
    }

    @Override
    public void shareBacklog(BacklogDO backlogDO) {

    }

    @Subscribe
    public void onEvent(Event.AddBacklog addBacklogEvent) {
        loadBacklogs();
    }

    @Subscribe
    public void onEvent(Event.UpdateBacklog updateBacklogEvent) {
        loadBacklogs();
    }

    @Subscribe
    public void onEvent(Event.DeleteBacklog deleteBacklogEvent) {
        loadBacklogs();
    }

}
