package me.xihuxiaolong.justdoit.module.adddayplan;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
import me.xihuxiaolong.justdoit.common.database.manager.IPlanDataSource;
import me.xihuxiaolong.justdoit.common.event.Event;
import me.xihuxiaolong.justdoit.module.editalert.EditAlertContract;
import me.xihuxiaolong.library.utils.CollectionUtil;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/9/28.
 */

public class AddDayPlanActivityPresenter extends MvpBasePresenter<AddDayPlanActivityContract.IView> implements AddDayPlanActivityContract.IPresenter {

    @Inject
    long dayTime;

    @Inject
    IPlanDataSource planDataSource;

    @Inject
    public AddDayPlanActivityPresenter() {EventBus.getDefault().register(this);}

    @Override
    public void loadDayInfo() {
        if(isViewAttached())
            getView().showDayInfo(new DateTime(dayTime));
    }

    @Override
    public void loadData() {
        List<PlanDO> planDOs = planDataSource.listPlanDOsByOneDay(dayTime);
        if(isViewAttached()) {
            if (CollectionUtil.isEmpty(planDOs))
                getView().showEmptyView();
            else
                getView().showContent(planDOs);
        }
    }

    @Override
    public void loadTemplateList() {
        List<String> templates = new ArrayList<>();
        templates.add("sss");
        templates.add("sss");
        templates.add("sss");
        templates.add("sss");
        if(isViewAttached()) {
            getView().showTemplateList(templates);
        }
    }

    @Override
    public void createDayPlan(boolean useYesterdayTemplate) {
        if(isViewAttached()) {
            getView().showContent(new ArrayList<PlanDO>());
        }
    }

    @Subscribe
    public void onEvent(Event.AddPlan addPlanEvent) {
        if(dayTime == addPlanEvent.plan.getDayTime())
            loadData();
    }

    @Subscribe
    public void onEvent(Event.UpdatePlan updatePlanEvent) {
        if(dayTime == updatePlanEvent.plan.getDayTime())
            loadData();
    }

    @Subscribe
    public void onEvent(Event.DeletePlan deletePlanEvent) {
        if(dayTime == deletePlanEvent.plan.getDayTime())
            loadData();
    }
}
