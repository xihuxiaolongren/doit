package me.xihuxiaolong.justdoit.module.targetdetail;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.joda.time.DateTime;

import javax.inject.Inject;

import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
import me.xihuxiaolong.justdoit.common.database.localentity.TargetDO;
import me.xihuxiaolong.justdoit.common.database.service.PlanDataService;
import me.xihuxiaolong.justdoit.common.database.service.TargetDataService;
import me.xihuxiaolong.justdoit.common.event.Event;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/9/27.
 */

public class TargetDetailPresenter extends MvpBasePresenter<TargetDetailContract.IView> implements TargetDetailContract.IPresenter {

    @Inject
    String targetName;

    @Inject
    PlanDataService planDataSource;

    @Inject
    TargetDataService targetDataService;

    TargetDO targetDO;

    @Inject
    public TargetDetailPresenter() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void loadTarget() {
        targetDO = targetDataService.getTargetByKey(targetName);
        if (targetDO != null) {
            targetDO.setPlanDOList(planDataSource.listPlanDOsByTargetName(targetName));
            long currentDayTime = 0;
            for (PlanDO planDO : targetDO.getPlanDOList()) {
                if (currentDayTime != planDO.getDayTime()) {
                    currentDayTime = planDO.getDayTime();
                    planDO.setTempDayTime(currentDayTime);
                }
            }
            if (isViewAttached()) {
                getView().showTarget(targetDO);
            }
        }
    }

    @Override
    public void updateTarget(String headerImageUri) {
        if (targetDO != null) {
            targetDO.setHeaderImageUri(headerImageUri);
            targetDataService.insertOrReplaceTarget(targetDO);
            EventBus.getDefault().post(new Event.UpdateTarget(targetName));
            if (isViewAttached())
                getView().updateTargetSuccess();
        }
    }

    @Override
    public void updateTarget(boolean customTheme, int themeColor, int textColor) {
        if (targetDO != null) {
            targetDO.setCustomTheme(customTheme);
            targetDO.setTextColor(textColor);
            targetDO.setThemeColor(themeColor);
            targetDataService.insertOrReplaceTarget(targetDO);
            EventBus.getDefault().post(new Event.UpdateTarget(targetName));
            if (isViewAttached())
                getView().updateTargetSuccess();
        }
    }

    @Override
    public void deleteTarget() {
        if (targetName != null) {
            targetDataService.deleteTargetByKey(targetName);
            EventBus.getDefault().post(new Event.DeleteTarget(targetName));
            if (isViewAttached())
                getView().deleteTargetSuccess();
        }
    }

    @Override
    public void savePunch(String content, String pictures) {
        DateTime dateTime = DateTime.now();
        PlanDO punch = new PlanDO();
        punch.setType(PlanDO.TYPE_PUNCH);
        punch.setContent(content);
        punch.setStartHour(dateTime.getHourOfDay());
        punch.setStartMinute(dateTime.getMinuteOfHour());
        punch.setStartTime(dateTime.getMinuteOfDay());
        punch.setPicUrls(pictures);
        punch.setTargetName(targetName);

        punch.setDayTime(dateTime.withTimeAtStartOfDay().getMillis());
        long punchId = planDataSource.insertOrReplacePlanDO(punch);
        punch.setId(punchId);
        EventBus.getDefault().post(new Event.AddPlan(punch));
        loadTarget();
    }

    @Subscribe
    public void onEvent(Event.UpdateTarget updateTargetEvent) {
        loadTarget();
    }

    @Subscribe
    public void onEvent(Event.DeleteTarget deleteTargetEvent) {
        loadTarget();
    }

    @Subscribe
    public void onEvent(Event.AddPlan addPlanEvent) {
        if (targetName.equals(addPlanEvent.plan.getTargetName()))
            loadTarget();
    }

    @Subscribe
    public void onEvent(Event.UpdatePlan updatePlanEvent) {
        if (targetName.equals(updatePlanEvent.plan.getTargetName()))
            loadTarget();
    }

    @Subscribe
    public void onEvent(Event.DeletePlan deletePlanEvent) {
        if (targetName.equals(deletePlanEvent.plan.getTargetName()))
            loadTarget();
    }

}