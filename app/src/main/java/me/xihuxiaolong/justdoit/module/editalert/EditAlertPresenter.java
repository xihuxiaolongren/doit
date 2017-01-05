package me.xihuxiaolong.justdoit.module.editalert;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.EventBus;

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

public class EditAlertPresenter extends MvpBasePresenter<EditAlertContract.IView> implements EditAlertContract.IPresenter {

    @Inject @Named("alertId")
    Long alertId;

    @Inject @Named("dayTime")
    long dayTime;

    @Inject @Nullable
    String targetName;

    @Inject
    IPlanDataSource planDataSource;

    PlanDO editAlert;

    @Inject
    public EditAlertPresenter() {}

    @Override
    public void loadAlert() {
        if(alertId != -1L) {
            editAlert = planDataSource.getPlanDOById(alertId);
            if(editAlert == null){
                editAlert = new PlanDO();
                PlanDO lastPlanDO = planDataSource.getPlanByLastEndTime();
                if(lastPlanDO != null) {
                    editAlert.setStartTime(lastPlanDO.getEndTime() + 30);
                    editAlert.setStartHour(editAlert.getStartTime() / 60);
                    editAlert.setStartTime(editAlert.getStartTime() % 60);
                    editAlert.setEndTime(editAlert.getEndTime() % 60);
                    editAlert.setStartTime(editAlert.getEndTime() % 60);
                    editAlert.setStartTime(editAlert.getEndTime() % 60);
                }
            }
            if (isViewAttached())
                getView().showAlert(editAlert);
        }
    }

    @Override
    public void saveAlert(int hour, int minute, String content, int repeatMode) {
        PlanDO alert = new PlanDO();
        alert.setType(PlanDO.TYPE_ALERT);
        alert.setContent(content);
        alert.setStartHour(hour);
        alert.setStartMinute(minute);
        alert.setStartTime(hour * 60 + minute);
        alert.setEndHour(hour);
        alert.setEndMinute(minute);
        alert.setEndTime(hour * 60 + minute);
        alert.setTempRepeatmode(repeatMode);
        alert.setTargetName(targetName);

        if(alertId != -1L) {
            alert.setId(alertId);
            planDataSource.insertOrReplacePlanDO(alert);
            EventBus.getDefault().post(new Event.UpdatePlan(alert));
        }else {
            alert.setDayTime(dayTime);
            long alertId = planDataSource.insertOrReplacePlanDO(alert);
            alert.setId(alertId);
            EventBus.getDefault().post(new Event.AddPlan(alert));
        }
        if(!TextUtils.isEmpty(targetName))
            EventBus.getDefault().post(new Event.UpdateTarget(targetName));
        if(isViewAttached())
            getView().saveSuccess();
    }

    @Override
    public void deleteAlert() {
        PlanDO alert = planDataSource.getPlanDOById(alertId);
        planDataSource.deleteAlertById(alertId);
        if(isViewAttached())
            getView().deleteSuccess();
        EventBus.getDefault().post(new Event.DeletePlan(alert));
    }

}
