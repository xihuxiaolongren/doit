package me.xihuxiaolong.justdoit.module.planlist;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import me.xihuxiaolong.justdoit.common.cache.ICacheService;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
import me.xihuxiaolong.justdoit.common.cache.entity.UserSettings;
import me.xihuxiaolong.justdoit.common.database.localentity.TargetDO;
import me.xihuxiaolong.justdoit.common.database.service.PlanDataService;
import me.xihuxiaolong.justdoit.common.database.service.TargetDataService;
import me.xihuxiaolong.justdoit.common.event.Event;
import me.xihuxiaolong.library.utils.CollectionUtils;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/9/27.
 */

public class PlanListPresenter extends MvpBasePresenter<PlanListContract.IView> implements PlanListContract.IPresenter {

    @Inject
    long dayTime;

    @Inject
    PlanDataService planDataSource;

    @Inject
    TargetDataService targetDataService;

    @Inject
    ICacheService cacheService;

    int currentListMode;

    @Inject
    public PlanListPresenter() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void loadPlansByMode() {
        if (currentListMode == 0) {
            loadPlans();
        } else {
//            loadBacklogs();
        }
    }

    @Override
    public void loadPlans() {
        List<PlanDO> planDOs = new ArrayList<>();
        if (dayTime == DateTime.now().withTimeAtStartOfDay().getMillis()) {
            planDOs = planDataSource.listPlanDOsByOneDay(dayTime);
            if (CollectionUtils.isEmpty(planDOs)) {       //为空，根据redoPlan创建
                int count = planDataSource.createOneDayPlanDOs(dayTime);
                if (count > 0)
                    planDOs = planDataSource.listPlanDOsByOneDay(dayTime);
            }
        } else if (dayTime > DateTime.now().withTimeAtStartOfDay().getMillis()) {
            planDataSource.createOneDayPlanDOs(dayTime);
            planDOs = planDataSource.listPlanDOsByOneDay(dayTime);
        } else {
            planDOs = planDataSource.listPlanDOsByOneDay(dayTime);

        }
        if (isViewAttached()) {
            getView().showPlans(planDOs);
        }
    }

    @Override
    public void loadBacklogs() {
//        List<PlanDO> planDOs = planDataSource.listBacklogs(null, 2000);
//        if (isViewAttached()) {
//            getView().showBacklogs(planDOs);
//        }
    }

    @Override
    public void loadDayInfo() {
        if (!DateTime.now().withTimeAtStartOfDay().equals(new DateTime(dayTime).withTimeAtStartOfDay()) && isViewAttached())
            getView().showOtherDayUI();
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
    public void deletePlan(PlanDO planDO) {
        switch (planDO.getType()) {
            case PlanDO.TYPE_PHOTO:
                planDataSource.deletePhotoById(planDO.getId());
                break;
            case PlanDO.TYPE_PUNCH:
                planDataSource.deletePunchById(planDO.getId());
                break;
        }
        EventBus.getDefault().post(new Event.DeletePlan(planDO));
    }

    @Override
    public void sharePlan(PlanDO planDO) {

    }

    @Override
    public void switchPlansByMode() {
        if(currentListMode == 0)
            currentListMode = 1;
        else
            currentListMode = 0;
        loadPlansByMode();
    }

    @Subscribe
    public void onEvent(Event.AddPlan addPlanEvent) {
        if (dayTime == addPlanEvent.plan.getDayTime())
            loadPlans();
    }

    @Subscribe
    public void onEvent(Event.UpdatePlan updatePlanEvent) {
        if (dayTime == updatePlanEvent.plan.getDayTime())
            loadPlans();
    }

    @Subscribe
    public void onEvent(Event.DeletePlan deletePlanEvent) {
        if (dayTime == deletePlanEvent.plan.getDayTime())
            loadPlans();
    }

    @Subscribe
    public void onEvent(Event.UpdateSettings updateSettings) {
        loadUserSettings();
    }

}
