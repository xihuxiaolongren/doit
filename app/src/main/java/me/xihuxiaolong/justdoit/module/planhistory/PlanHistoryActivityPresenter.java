package me.xihuxiaolong.justdoit.module.planhistory;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import me.xihuxiaolong.justdoit.common.database.manager.IPlanDataSource;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/9/27.
 */

public class PlanHistoryActivityPresenter extends MvpBasePresenter<PlanHistoryActivityContract.IView> implements PlanHistoryActivityContract.IPresenter{

    @Inject
    IPlanDataSource planDataSource;

    List<DateTime> dayTimes;

    @Inject
    public PlanHistoryActivityPresenter() {
    }

    @Override
    public void loadHistorys() {
        DateTime startTime = DateTime.now().withDate(2016, 11, 1).withTimeAtStartOfDay();
        DateTime endTime = DateTime.now().withTimeAtStartOfDay();
        dayTimes = new ArrayList<>();
        for(; endTime.isAfter(startTime); endTime = endTime.minusDays(1)){
            dayTimes.add(endTime);
        }
        if(isViewAttached())
            getView().showHistorys(dayTimes);

    }

    @Override
    public void loadCurrentDayTime(int position) {
        if(dayTimes != null && position < dayTimes.size() && isViewAttached())
            getView().showDayInfo(dayTimes.get(position));
    }
}
