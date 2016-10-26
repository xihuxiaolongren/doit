package me.xihuxiaolong.justdoit.module.editplan;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.DateTime;

import javax.inject.Inject;
import javax.inject.Named;

import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
import me.xihuxiaolong.justdoit.common.database.manager.IPlanDataSource;
import me.xihuxiaolong.justdoit.common.event.Event;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/9/28.
 */

public class EditPlanPresenter extends MvpBasePresenter<EditPlanContract.IView> implements EditPlanContract.IPresenter {

    @Inject @Named("planId")
    long planId;

    @Inject @Named("dayTime")
    long dayTime;

    @Inject
    IPlanDataSource planDataSource;

    @Inject
    public EditPlanPresenter() {}

    @Override
    public void loadPlan() {
        if(planId != -1L) {
            PlanDO planDO = planDataSource.getPlanDOById(planId);
            if (isViewAttached()) {
                getView().showPlan(planDO);
            }
        }
    }

    @Override
    public void savePlan(int startHour, int startMinute, int endHour, int endMinute, String content) {
        PlanDO plan = new PlanDO();
        plan.setType(PlanDO.TYPE_PLAN);
        plan.setContent(content);
        plan.setStartHour(startHour);
        plan.setStartMinute(startMinute);
        plan.setStartTime(startHour * 60 + startMinute);
        plan.setEndHour(endHour);
        plan.setEndMinute(endMinute);
        plan.setEndTime(endHour * 60 + endMinute);
        if(planId != -1L) {
            plan.setId(planId);
            planDataSource.insertOrReplacePlanDO(plan);
            EventBus.getDefault().post(new Event.UpdatePlan(plan));
        }else {
            plan.setDayTime(dayTime);
            long planId = planDataSource.insertOrReplacePlanDO(plan);
            plan.setId(planId);
            EventBus.getDefault().post(new Event.AddPlan(plan));
        }
        if(isViewAttached())
            getView().saveSuccess();


    }

    @Override
    public void deletePlan() {
        PlanDO planDO = planDataSource.getPlanDOById(planId);
        planDataSource.deletePlanById(planId);
        if(isViewAttached())
            getView().deleteSuccess();
        EventBus.getDefault().post(new Event.DeletePlan(planDO));
    }

}
