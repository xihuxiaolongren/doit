package me.xihuxiaolong.justdoit.module.settings;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import me.xihuxiaolong.justdoit.common.cache.CacheService;
import me.xihuxiaolong.justdoit.common.cache.ICacheService;
import me.xihuxiaolong.justdoit.common.cache.entity.UserSettings;
import me.xihuxiaolong.justdoit.common.event.Event;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/9/28.
 */

public class SettingsPresenter extends MvpBasePresenter<SettingsContract.IView> implements SettingsContract.IPresenter {

    @Inject
    ICacheService cacheService;

    @Inject
    public SettingsPresenter() {}

    @Override
    public void loadSettings() {
        UserSettings userSettings = cacheService.getSettings();
        if(userSettings != null && isViewAttached())
            getView().showSettings(userSettings);
    }

    @Override
    public void saveSettings(UserSettings userSettings) {
        cacheService.put(CacheService.Keys.settings, userSettings);
        EventBus.getDefault().post(new Event.UpdateSettings(userSettings));
        if(isViewAttached())
            getView().saveSuccess();
    }
}
