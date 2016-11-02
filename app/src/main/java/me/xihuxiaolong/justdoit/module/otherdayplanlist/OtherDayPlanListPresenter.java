package me.xihuxiaolong.justdoit.module.otherdayplanlist;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.joda.time.DateTime;

import java.util.List;

import javax.inject.Inject;

import me.xihuxiaolong.justdoit.common.cache.ICacheService;
import me.xihuxiaolong.justdoit.common.cache.entity.UserSettings;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
import me.xihuxiaolong.justdoit.common.database.manager.IPlanDataSource;
import me.xihuxiaolong.justdoit.common.event.Event;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/9/27.
 */

public class OtherDayPlanListPresenter extends MvpBasePresenter<OtherDayPlanListContract.IView> implements OtherDayPlanListContract.IPresenter{

    @Inject
    long dayTime;

    @Inject
    IPlanDataSource planDataSource;

    @Inject
    ICacheService cacheService;

    @Inject
    public OtherDayPlanListPresenter() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void loadPlans() {
        List<PlanDO> planDOs = planDataSource.listPlanDOsByOneDay(DateTime.now().withTimeAtStartOfDay().getMillis());
        if (isViewAttached()) {
            getView().showPlans(planDOs);
        }
    }

    @Override
    public void loadDayInfo() {

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

    @Subscribe
    public void onEvent(Event.ChangeDayNightTheme changeDayNightTheme) {
    }

}
