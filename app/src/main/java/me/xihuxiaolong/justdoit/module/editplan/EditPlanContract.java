package me.xihuxiaolong.justdoit.module.editplan;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.LinkedHashSet;
import java.util.List;

import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/8/12.
 */
public class EditPlanContract {

    interface IView extends MvpView {

        void showPlan(PlanDO plan);

        void savePlanSuccess();

        void deletePlanSuccess();

        void sharePlan();

        void showTagDialog(LinkedHashSet<String> selectedTags, LinkedHashSet<String> allTags);

        void showThirdAppDialog();

    }

    interface IPresenter extends MvpPresenter<IView> {

        void loadPlan();

        void savePlan(int startHour, int startMinute, int endHour, int endMinute, String content, String tags, String linkAppName, String linkAppPackageName, int repeatMode);

        void deletePlan();

        void loadTags();

//        void deleteTag(String tag);
//
//        void addTag(String tag);
//
//        void saveTag();

    }
}
