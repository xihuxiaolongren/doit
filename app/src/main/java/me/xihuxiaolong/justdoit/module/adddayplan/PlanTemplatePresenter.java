package me.xihuxiaolong.justdoit.module.adddayplan;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import javax.inject.Inject;

import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
import me.xihuxiaolong.justdoit.common.database.service.PlanDataService;
import me.xihuxiaolong.justdoit.common.event.Event;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/9/27.
 */

public class PlanTemplatePresenter extends MvpBasePresenter<PlanTemplateContract.IView> implements PlanTemplateContract.IPresenter{

    @Inject
    long dayTime;

    @Inject
    PlanDataService planDataSource;

    @Inject
    public PlanTemplatePresenter() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void loadPlans() {
        List<PlanDO> planDOs = planDataSource.listPlanDOsByOneDay(dayTime);
        if (isViewAttached()) {
            getView().showPlans(planDOs);
        }
    }

    @Subscribe
    public void onEvent(Event.AddPlan addPlanEvent) {
        loadPlans();
    }

    @Subscribe
    public void onEvent(Event.UpdatePlan updatePlanEvent) {
        loadPlans();
    }

    @Subscribe
    public void onEvent(Event.DeletePlan deletePlanEvent) {
        loadPlans();
    }

}
