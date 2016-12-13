package me.xihuxiaolong.justdoit.module.redoplandetail;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import me.xihuxiaolong.justdoit.common.database.localentity.RedoPlanDO;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/8/12.
 */
public class RedoPlanContract {

    interface IView extends MvpView {


        void showRedoPlan(RedoPlanDO redoPlanDO);

        void removeRedoPlanSuccess();

    }

    interface IPresenter extends MvpPresenter<IView> {

        void loadRedoPlan();

        void removeRedoPlan();
    }
}
