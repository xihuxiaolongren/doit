package me.xihuxiaolong.justdoit.module.easybackloglist;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import javax.inject.Inject;

import me.xihuxiaolong.justdoit.common.database.localentity.BacklogDO;
import me.xihuxiaolong.justdoit.common.database.service.BacklogDataService;
import me.xihuxiaolong.justdoit.common.event.Event;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/9/27.
 */

public class EasyBacklogListPresenter extends MvpBasePresenter<EasyBacklogListContract.IView> implements EasyBacklogListContract.IPresenter {

    @Inject
    BacklogDataService backlogDataService;

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
    }

    @Override
    public void deleteBacklog(BacklogDO backlogDO) {
        backlogDataService.deleteBacklogById(backlogDO.getId());
        loadBacklogs();
    }

    @Override
    public void shareBacklog(BacklogDO backlogDO) {

    }

    @Override
    public void saveBacklog(String content) {
        BacklogDO punch = new BacklogDO();
        punch.setContent(content);
        long punchId = backlogDataService.insertOrReplaceBacklogDO(punch);
        punch.setId(punchId);
        EventBus.getDefault().post(new Event.AddBacklog(punch));
    }

    @Override
    public void modifyBacklog(Long id, String content) {
        BacklogDO backlogDO = new BacklogDO();
        backlogDO.setId(id);
        backlogDO.setContent(content);
        long backlogId = backlogDataService.insertOrReplaceBacklogDO(backlogDO);
        backlogDO.setId(backlogId);
        EventBus.getDefault().post(new Event.UpdateBacklog(backlogDO));
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
