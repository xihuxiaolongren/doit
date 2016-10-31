package me.xihuxiaolong.justdoit.module.adddayplan;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/8/12.
 */
public class PlanTemplateContract {

    interface IView extends MvpView {

        void showPlans(List<PlanDO> plans);

        void showSignature(String signature);

    }

    interface IPresenter extends MvpPresenter<IView> {

        void loadPlans();

    }
}
