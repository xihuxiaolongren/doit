package me.xihuxiaolong.justdoit.module.editalert;

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

public class EditAlertPresenter extends MvpBasePresenter<EditAlertContract.IView> implements EditAlertContract.IPresenter {

    @Inject @Named("alertId")
    Long alertId;

    @Inject @Named("dayTime")
    long dayTime;

    @Inject
    IPlanDataSource planDataSource;

    @Inject
    public EditAlertPresenter() {}

    @Override
    public void loadAlert() {
        if(alertId != -1L) {
            PlanDO alert = planDataSource.getPlanDOById(alertId);
            if (isViewAttached())
                getView().showAlert(alert);
        }
    }

    @Override
    public void saveAlert(int hour, int minute, String content) {
        PlanDO alert = new PlanDO();
        alert.setType(PlanDO.TYPE_ALERT);
        alert.setContent(content);
        alert.setStartHour(hour);
        alert.setStartMinute(minute);
        alert.setStartTime(hour * 60 + minute);
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
