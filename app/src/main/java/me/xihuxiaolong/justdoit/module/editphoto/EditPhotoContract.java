package me.xihuxiaolong.justdoit.module.editphoto;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/8/12.
 */
public class EditPhotoContract {

    interface IView extends MvpView {

        void saveSuccess();

        void deleteSuccess();

        void sharePhoto();

    }

    interface IPresenter extends MvpPresenter<IView> {

        void savePhoto(String content, String pictures);

    }
}
