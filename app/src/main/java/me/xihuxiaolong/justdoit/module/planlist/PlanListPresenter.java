package me.xihuxiaolong.justdoit.module.planlist;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
import me.xihuxiaolong.justdoit.common.database.localentity.UserSettingsDO;
import me.xihuxiaolong.justdoit.common.database.manager.IPlanDataSource;
import me.xihuxiaolong.justdoit.common.database.manager.IUserSettingsDataSource;
import me.xihuxiaolong.justdoit.common.event.Event;

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
    IUserSettingsDataSource userSettingsDataSource;

    @Inject
    public PlanListPresenter() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void loadPlans() {
        List<PlanDO> planDOs = planDataSource.listPlanDOsByOneDay(DateTime.now().withTimeAtStartOfDay().getMillis());
        if (isViewAttached()) {
            getView().showPlans(planDOs);
        }
//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//                List<PlanDO> planDOs = setDummyData();
//
//                if (isViewAttached()) {
//                    getView().showPlans(planDOs);
//                }
//            }
//        });

    }

    @Override
    public void loadDayInfo() {
    }

    @Override
    public void loadUserSettings() {
        UserSettingsDO userSettingsDO = userSettingsDataSource.getUserSettingsDOById(-1L);
        if(userSettingsDO != null && isViewAttached()){
            int hour = DateTime.now().getHourOfDay();
            if(hour > 0 && hour < 10){
                getView().showSignature(userSettingsDO.getMottoPlanStart());
            }else if(hour > 18 && hour < 24){
                getView().showSignature(userSettingsDO.getMottoPlanEnd());
            }else
                getView().showSignature(userSettingsDO.getMotto());
//            getView().showDayInfo(userSettingsDO.getShowAvatar() ? userSettingsDO.getAvatarUri() : null, new DateTime());
            getView().showDayInfo("", new DateTime());
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

}
