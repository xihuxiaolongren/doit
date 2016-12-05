package me.xihuxiaolong.justdoit.module.editplan;

import android.support.annotation.Nullable;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedHashSet;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
import me.xihuxiaolong.justdoit.common.database.localentity.TagDO;
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

    @Inject @Nullable
    String targetName;

    @Inject
    IPlanDataSource planDataSource;

    PlanDO editPlanDO;

    @Inject
    public EditPlanPresenter() {}

    @Override
    public void loadPlan() {
        if(planId != -1L) {
            editPlanDO = planDataSource.getPlanDOById(planId);
            if (isViewAttached()) {
                getView().showPlan(editPlanDO);
            }
        }
    }

    @Override
    public void savePlan(int startHour, int startMinute, int endHour, int endMinute, String content, String tags, String linkAppName, String linkAppPackageName, int repeatMode) {
        PlanDO plan = new PlanDO();
        plan.setType(PlanDO.TYPE_PLAN);
        plan.setContent(content);
        plan.setStartHour(startHour);
        plan.setStartMinute(startMinute);
        plan.setStartTime(startHour * 60 + startMinute);
        plan.setEndHour(endHour);
        plan.setEndMinute(endMinute);
        plan.setEndTime(endHour * 60 + endMinute);
        plan.setTags(tags);
        plan.setLinkAppName(linkAppName);
        plan.setLinkAppPackageName(linkAppPackageName);
        plan.setTempRepeatmode(repeatMode);
        if(planId != -1L && editPlanDO != null) {
            plan.setId(planId);
            plan.setDayTime(editPlanDO.getDayTime());
            planDataSource.insertOrReplacePlanDO(plan, targetName);
            EventBus.getDefault().post(new Event.UpdatePlan(plan));
        }else {
            plan.setDayTime(dayTime);
            long planId = planDataSource.insertOrReplacePlanDO(plan, targetName);
            plan.setId(planId);
            EventBus.getDefault().post(new Event.AddPlan(plan));
        }
        if(isViewAttached())
            getView().savePlanSuccess();


    }

    @Override
    public void deletePlan() {
        PlanDO planDO = planDataSource.getPlanDOById(planId);
        planDataSource.deletePlanById(planId);
        if(isViewAttached())
            getView().deletePlanSuccess();
        EventBus.getDefault().post(new Event.DeletePlan(planDO));
    }

    LinkedHashSet<String> mSelectedTags, mAllTags;


    @Override
    public void loadTags() {
        mSelectedTags = new LinkedHashSet<>();
        mAllTags = new LinkedHashSet<>();
        for(TagDO tagDO : planDataSource.listAllTag()){
            mAllTags.add(tagDO.getName());
        }
        if(isViewAttached())
            getView().showTagDialog(mSelectedTags, mAllTags);
    }

}
