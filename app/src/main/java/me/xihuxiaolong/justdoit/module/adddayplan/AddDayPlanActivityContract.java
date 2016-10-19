package me.xihuxiaolong.justdoit.module.adddayplan;

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
public class AddDayPlanActivityContract {

    interface IView extends MvpView {

        void showDayInfo(DateTime dateTime);

        void showContent(List<PlanDO> planDOs);

        void showEmptyView();

    }

    interface IPresenter extends MvpPresenter<IView> {

        void loadDayInfo();

        void loadData();

        void createDayPlan(boolean useYesterdayTemplate);

    }
}
