package me.xihuxiaolong.justdoit.module.settings;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import me.xihuxiaolong.justdoit.common.cache.entity.UserSettings;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/8/12.
 */
public class SettingsContract {

    interface IView extends MvpView {

        void showSettings(UserSettings userSettings);

        void saveSuccess();

    }

    interface IPresenter extends MvpPresenter<IView> {

        void loadSettings();

        void saveSettings(UserSettings userSettings);

    }
}
