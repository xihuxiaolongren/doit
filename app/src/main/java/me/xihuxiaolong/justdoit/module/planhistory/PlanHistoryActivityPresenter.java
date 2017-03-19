package me.xihuxiaolong.justdoit.module.planhistory;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import me.xihuxiaolong.justdoit.common.database.service.PlanDataService;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/9/27.
 */

public class PlanHistoryActivityPresenter extends MvpBasePresenter<PlanHistoryActivityContract.IView> implements PlanHistoryActivityContract.IPresenter {

    @Inject
    PlanDataService planDataSource;

    List<Long> dayTimes;

    @Inject
    public PlanHistoryActivityPresenter() {
    }

    @Override
    public void loadHistorys() {
        DateTime endTime = DateTime.now().withTimeAtStartOfDay().minusDays(1);
//        DateTime startTime = DateTime.now().withDate(2016, 11, 1).withTimeAtStartOfDay();
        Long startTimeLong = planDataSource.getFirstPlanTime();
        DateTime startTime;
        if (startTimeLong == null)
            startTime = endTime.minusDays(1);
        else
            startTime = new DateTime(startTimeLong).withTimeAtStartOfDay();
        dayTimes = new ArrayList<>();
        for (; endTime.isAfter(startTime); endTime = endTime.minusDays(1)) {
            dayTimes.add(endTime.getMillis());
        }
        if (isViewAttached())
            getView().showHistorys(dayTimes);

    }

    @Override
    public void loadCurrentDayTime(int position) {
        if (dayTimes != null && position < dayTimes.size() && isViewAttached())
            getView().showDayInfo(dayTimes.get(position));
    }

    @Override
    public void loadDay(DateTime dateTime) {
        int pos = dayTimes.indexOf(dateTime.withTimeAtStartOfDay().getMillis());
        if (pos != -1 && isViewAttached())
            getView().showDay(pos);
    }

    @Override
    public void loadStartAndEnd() {
        if(isViewAttached())
            getView().showCalendar(dayTimes.get(dayTimes.size() - 1), dayTimes.get(0));
    }
}
