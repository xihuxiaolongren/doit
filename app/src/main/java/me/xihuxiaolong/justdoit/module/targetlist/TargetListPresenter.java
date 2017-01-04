package me.xihuxiaolong.justdoit.module.targetlist;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.joda.time.DateTime;

import java.util.List;

import javax.inject.Inject;

import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanHistoryDO;
import me.xihuxiaolong.justdoit.common.database.localentity.TargetDO;
import me.xihuxiaolong.justdoit.common.database.manager.IPlanDataSource;
import me.xihuxiaolong.justdoit.common.database.manager.IPlanHistoryDataSource;
import me.xihuxiaolong.justdoit.common.database.manager.IRedoPlanDataSource;
import me.xihuxiaolong.justdoit.common.event.Event;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/9/27.
 */

public class TargetListPresenter extends MvpBasePresenter<TargetListContract.IView> implements TargetListContract.IPresenter {

    @Inject
    IRedoPlanDataSource redoPlanDataSource;

    @Inject
    IPlanDataSource planDataSource;

    @Inject
    IPlanHistoryDataSource planHistoryDataSource;

    @Inject
    public TargetListPresenter() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void loadTargets() {
        List<TargetDO> targetDOs = redoPlanDataSource.listAllTarget(true);
        if (isViewAttached()) {
            getView().showTargets(targetDOs);
        }
    }

    @Override
    public void loadStatistics() {
        List<PlanHistoryDO> planHistoryDOs = planHistoryDataSource.listPlanHistoryDOs(DateTime.now().minusDays(7).withTimeAtStartOfDay().getMillis(),
                DateTime.now().withTimeAtStartOfDay().getMillis());
        List<TargetDO> targetDOs = redoPlanDataSource.listNormalTarget();
        if (isViewAttached()) {
            getView().showStatistics(planHistoryDOs, targetDOs);
        }
    }

    @Override
    public void createTarget(String name, int type) {
        TargetDO targetDO = new TargetDO();
        targetDO.setName(name);
        targetDO.setType(type);
        redoPlanDataSource.insertOrReplaceTargetDO(targetDO);
        if (isViewAttached()) {
            getView().createTargetSuccess(targetDO);
        }
        EventBus.getDefault().post(new Event.AddTarget(targetDO.getName()));
    }

    @Override
    public void savePunch(String targetName, String content, String pictures) {
        DateTime dateTime = DateTime.now();
        PlanDO punch = new PlanDO();
        punch.setType(PlanDO.TYPE_PUNCH);
        punch.setContent(content);
        punch.setStartHour(dateTime.getHourOfDay());
        punch.setStartMinute(dateTime.getMinuteOfHour());
        punch.setStartTime(dateTime.getMillisOfDay());
        punch.setPicUrls(pictures);
        punch.setTargetName(targetName);

        punch.setDayTime(dateTime.withTimeAtStartOfDay().getMillis());
        long punchId = planDataSource.insertOrReplacePlanDO(punch, null);
        punch.setId(punchId);
        EventBus.getDefault().post(new Event.AddPlan(punch));
        loadTargets();
    }

    @Subscribe
    public void onEvent(Event.AddTarget addTargetEvent) {
        loadTargets();
    }

    @Subscribe
    public void onEvent(Event.UpdateTarget updateTargetEvent) {
        loadTargets();
    }

    @Subscribe
    public void onEvent(Event.DeleteTarget deleteTargetEvent) {
        loadTargets();
    }

    @Subscribe
    public void onEvent(Event.AddPlan addPlanEvent) {
        if(addPlanEvent.plan.getType() == PlanDO.TYPE_PUNCH)
            loadStatistics();
    }

    @Subscribe
    public void onEvent(Event.DeletePlan deletePlan) {
        if(deletePlan.plan.getType() == PlanDO.TYPE_PUNCH)
            loadStatistics();
    }
}
