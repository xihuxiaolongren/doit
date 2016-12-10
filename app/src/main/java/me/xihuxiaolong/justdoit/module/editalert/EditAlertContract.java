package me.xihuxiaolong.justdoit.module.editalert;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/8/12.
 */
public class EditAlertContract {

    interface IView extends MvpView {

        void showAlert(PlanDO alert);

        void saveSuccess();

        void deleteSuccess();

        void shareAlert();

    }

    interface IPresenter extends MvpPresenter<IView> {

        void loadAlert();

        void saveAlert(int hour, int minute, String content, int repeatMode);

        void deleteAlert();

    }
}
