package me.xihuxiaolong.justdoit.module.targetlist;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import org.joda.time.DateTime;

import java.util.List;

import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
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

        void createTargetSuccess(String targetName);

    }

    interface IPresenter extends MvpPresenter<IView> {

        void loadTargets();

        void createTarget(String name);

    }
}