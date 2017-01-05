package me.xihuxiaolong.justdoit.module.redoplanlist;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import me.xihuxiaolong.justdoit.common.database.localentity.RedoPlanDO;
import me.xihuxiaolong.justdoit.common.database.localentity.TargetDO;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/8/12.
 */
public class RedoPlanListContract {

    interface IView extends MvpView {

        void showRedoPlans(List<RedoPlanDO> redoPlanDOList);

        void updateTargetSuccess();

        void deleteTargetSuccess();

    }

    interface IPresenter extends MvpPresenter<IView> {

        void loadRedoPlans();

    }
}
