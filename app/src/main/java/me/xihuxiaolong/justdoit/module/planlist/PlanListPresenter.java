package me.xihuxiaolong.justdoit.module.planlist;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.joda.time.DateTime;

import java.util.List;

import javax.inject.Inject;

import me.xihuxiaolong.justdoit.common.cache.ICacheService;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
import me.xihuxiaolong.justdoit.common.cache.entity.UserSettings;
import me.xihuxiaolong.justdoit.common.database.manager.IPlanDataSource;
import me.xihuxiaolong.justdoit.common.event.Event;
import me.xihuxiaolong.library.utils.CollectionUtils;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/9/27.
 */

public class PlanListPresenter extends MvpBasePresenter<PlanListContract.IView> implements PlanListContract.IPresenter{

    @Inject
    long dayTime;

    @Inject
    IPlanDataSource planDataSource;

    @Inject
    ICacheService cacheService;

    @Inject
    public PlanListPresenter() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void loadPlans() {
        List<PlanDO> planDOs = planDataSource.listPlanDOsByOneDay(dayTime);
        if(CollectionUtils.isEmpty(planDOs)){
            int count = planDataSource.createOneDayPlanDOs(dayTime);
            if(count > 0)
                planDOs = planDataSource.listPlanDOsByOneDay(dayTime);
        }
        if (isViewAttached()) {
            getView().showPlans(planDOs);
            if(CollectionUtils.isEmpty(planDOs))
                getView().gotoTemplates();
        }
    }

    @Override
    public void loadDayInfo() {
        if(!DateTime.now().withTimeAtStartOfDay().equals(new DateTime(dayTime).withTimeAtStartOfDay()) && isViewAttached())
            getView().showOtherDayUI();
    }

    @Override
    public void loadUserSettings() {
        UserSettings userSettings = cacheService.getSettings();
        if(userSettings != null && isViewAttached()){
            int hour = DateTime.now().getHourOfDay();
            if(hour > 6 && hour < 10){
                getView().showSignature(userSettings.getMotto(), userSettings.getMottoPlanStart());
            }else if(hour > 18 && hour < 24){
                getView().showSignature(userSettings.getMotto(), userSettings.getMottoPlanEnd());
            }else
                getView().showSignature(userSettings.getMotto(), null);
            getView().showDayInfo(userSettings.isShowAvatar() ? userSettings.getAvatarUri() : null, new DateTime(dayTime));
        }
    }

    @Subscribe
    public void onEvent(Event.AddPlan addPlanEvent) {
        if(dayTime == addPlanEvent.plan.getDayTime())
            loadPlans();
    }

    @Subscribe
    public void onEvent(Event.UpdatePlan updatePlanEvent) {
        if(dayTime == updatePlanEvent.plan.getDayTime())
            loadPlans();
    }

    @Subscribe
    public void onEvent(Event.DeletePlan deletePlanEvent) {
        if(dayTime == deletePlanEvent.plan.getDayTime())
            loadPlans();
    }

    @Subscribe
    public void onEvent(Event.UpdateSettings updateSettings) {
        loadUserSettings();
    }

//    @Subscribe
//    public void onEvent(Event.ChangeDayNightTheme changeDayNightTheme) {
//        if(isViewAttached())
//            getView().changeDayNight();
//    }

}
