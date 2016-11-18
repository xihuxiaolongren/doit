package me.xihuxiaolong.justdoit.module.targetdetail;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import me.xihuxiaolong.justdoit.common.database.localentity.TargetDO;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/8/12.
 */
public class TargetDetailContract {

    interface IView extends MvpView {

        void removePlanItem(long targetId);

        void addPlanItem(TargetDO targetDO);

        void updatePlanItem(TargetDO targetDO);

        void showTarget(TargetDO targetDO);

    }

    interface IPresenter extends MvpPresenter<IView> {

        void loadTarget();

    }
}
