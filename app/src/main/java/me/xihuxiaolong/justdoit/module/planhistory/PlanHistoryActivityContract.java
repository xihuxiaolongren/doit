package me.xihuxiaolong.justdoit.module.planhistory;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/8/12.
 */
public class PlanHistoryActivityContract {

    interface IView extends MvpView {

        void showHistorys(List<Long> dayTimes);

        void showDayInfo(Long dateTime);

        void showDay(int position);

        void showCalendar(Long start, Long end);
    }

    interface IPresenter extends MvpPresenter<IView> {

        void loadHistorys();

        void loadCurrentDayTime(int position);

        void loadDay(DateTime dateTime);

        void loadStartAndEnd();
    }
}
