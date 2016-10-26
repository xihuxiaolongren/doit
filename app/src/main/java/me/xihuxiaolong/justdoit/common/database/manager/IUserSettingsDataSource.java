package me.xihuxiaolong.justdoit.common.database.manager;

import me.xihuxiaolong.justdoit.common.cache.entity.UserSettings;


/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/8/8.
 */
public interface IUserSettingsDataSource {

    UserSettings getUserSettingsDOById(Long id);

    long insertOrReplaceUserSettingsDO(UserSettings planDO);

}
