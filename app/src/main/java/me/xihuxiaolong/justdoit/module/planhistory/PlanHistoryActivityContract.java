package me.xihuxiaolong.justdoit.module.planhistory;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import org.joda.time.DateTime;

import java.util.List;

import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/8/12.
 */
public class PlanHistoryActivityContract {

    interface IView extends MvpView {

        void showHistorys(List<DateTime> dayTimes);

        void showDayInfo(DateTime dateTime);

    }

    interface IPresenter extends MvpPresenter<IView> {

        void loadHistorys();

        void loadCurrentDayTime(int position);

    }
}
