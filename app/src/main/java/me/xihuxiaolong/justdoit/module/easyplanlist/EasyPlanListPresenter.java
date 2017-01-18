package me.xihuxiaolong.justdoit.module.easyplanlist;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import me.xihuxiaolong.justdoit.common.cache.ICacheService;
import me.xihuxiaolong.justdoit.common.cache.entity.UserSettings;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
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

public class EasyPlanListPresenter extends MvpBasePresenter<EasyPlanListContract.IView> implements EasyPlanListContract.IPresenter {

    @Inject
    long dayTime;

    @Inject
    PlanDataService planDataSource;

    @Inject
    ICacheService cacheService;

    int currentListMode;

    @Inject
    public EasyPlanListPresenter() {
        EventBus.getDefault().register(this);
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

}
