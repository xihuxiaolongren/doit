package me.xihuxiaolong.justdoit.module.homepage;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import me.xihuxiaolong.justdoit.common.cache.ICacheService;
import me.xihuxiaolong.justdoit.common.cache.entity.UserSettings;
import me.xihuxiaolong.justdoit.common.database.localentity.BacklogDO;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
import me.xihuxiaolong.justdoit.common.database.localentity.TargetDO;
import me.xihuxiaolong.justdoit.common.database.service.BacklogDataService;
import me.xihuxiaolong.justdoit.common.database.service.PlanDataService;
import me.xihuxiaolong.justdoit.common.database.service.TargetDataService;
import me.xihuxiaolong.justdoit.common.event.Event;
import me.xihuxiaolong.library.utils.CollectionUtils;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/9/27.
 */

public class HomePagePresenter extends MvpBasePresenter<HomePageContract.IView> implements HomePageContract.IPresenter {

    @Inject
    long dayTime;

    @Inject
    PlanDataService planDataSource;

    @Inject
    BacklogDataService backlogDataService;

    @Inject
    TargetDataService targetDataService;

    @Inject
    ICacheService cacheService;

    int currentListMode;

    @Inject
    public HomePagePresenter() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void loadUserSettings() {
        UserSettings userSettings = cacheService.getSettings();
        if (userSettings != null && isViewAttached()) {
            int hour = DateTime.now().getHourOfDay();
            if (hour > 6 && hour < 10) {
                getView().showSignature(userSettings.getMotto(), userSettings.getMottoPlanStart());
            } else if (hour > 18 && hour < 24) {
                getView().showSignature(userSettings.getMotto(), userSettings.getMottoPlanEnd());
            } else
                getView().showSignature(userSettings.getMotto(), null);
            getView().showDayInfo(userSettings.isShowAvatar() ? userSettings.getAvatarUri() : null, new DateTime(dayTime));
        }
    }

    @Override
    public void startAddPunch() {
        List<TargetDO> targetDOs = targetDataService.listAllPunchTargets();
        if (isViewAttached())
            getView().showPunchDialog(targetDOs);
    }

    @Override
    public void savePunch(String content, String pictures, String targetName) {
        DateTime dateTime = DateTime.now();
        PlanDO punch = new PlanDO();
        punch.setType(PlanDO.TYPE_PUNCH);
        punch.setContent(content);
        punch.setStartHour(dateTime.getHourOfDay());
        punch.setStartMinute(dateTime.getMinuteOfHour());
        punch.setStartTime(dateTime.getMillis());
        punch.setPicUrls(pictures);
        punch.setTargetName(targetName);

        punch.setDayTime(dateTime.withTimeAtStartOfDay().getMillis());
        long punchId = planDataSource.insertOrReplacePlanDO(punch);
        punch.setId(punchId);
        EventBus.getDefault().post(new Event.AddPlan(punch));
    }

    @Override
    public void saveBacklog(String content) {
        BacklogDO punch = new BacklogDO();
        punch.setContent(content);
        long punchId = backlogDataService.insertOrReplaceBacklogDO(punch);
        punch.setId(punchId);
        EventBus.getDefault().post(new Event.AddBacklog(punch));
    }

    @Subscribe
    public void onEvent(Event.UpdateSettings updateSettings) {
        loadUserSettings();
    }

}
