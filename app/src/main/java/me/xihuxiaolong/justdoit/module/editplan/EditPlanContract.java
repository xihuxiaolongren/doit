package me.xihuxiaolong.justdoit.module.editplan;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/8/12.
 */
public class EditPlanContract {

    interface IView extends MvpView {

        void showPlan(PlanDO plan);

        void saveSuccess();

        void deleteSuccess();

        void sharePlan();

    }

    interface IPresenter extends MvpPresenter<IView> {

        void loadPlan();

        void savePlan(int startHour, int startMinute, int endHour, int endMinute, String content);

        void deletePlan();

    }
}
