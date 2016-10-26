package me.xihuxiaolong.justdoit.module.planlist;

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
public class PlanListContract {

    interface IView extends MvpView {

        void removePlanItem(long planId);

        void addPlanItem(PlanDO planDO);

        void updatePlanItem(PlanDO planDO);

        void showPlans(List<PlanDO> plans);

        void showDayInfo(String avatarUrl, DateTime dateTime);

        void showSignature(String signature, String preSignature);

        void changeDayNight();

    }

    interface IPresenter extends MvpPresenter<IView> {

        void loadPlans();

        void loadDayInfo();

        void loadUserSettings();

    }
}
