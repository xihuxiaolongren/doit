package me.xihuxiaolong.justdoit.module.targetlist;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import me.xihuxiaolong.justdoit.common.database.localentity.PlanHistoryDO;
import me.xihuxiaolong.justdoit.common.database.localentity.TargetDO;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/8/12.
 */
public class TargetListContract {

    interface IView extends MvpView {

        void removeTargetItem(long targetId);

        void addTargetItem(TargetDO targetDO);

        void updateTargetItem(TargetDO targetDO);

        void showTargets(List<TargetDO> targets);

        void showStatistics(List<PlanHistoryDO> planHistoryDOs, List<TargetDO> targetDOs);

        void createTargetSuccess(TargetDO target);

        void showAddTargetDialog();

    }

    interface IPresenter extends MvpPresenter<IView> {

        void loadTargets();

        void loadStatistics();

        void createTarget(String name, int type);

        void savePunch(String targetName, String content, String pictures);

    }
}
