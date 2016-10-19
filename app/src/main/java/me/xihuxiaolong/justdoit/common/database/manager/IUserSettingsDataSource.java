package me.xihuxiaolong.justdoit.common.database.manager;

import me.xihuxiaolong.justdoit.common.database.localentity.UserSettingsDO;


/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/8/8.
 */
public interface IUserSettingsDataSource {

    UserSettingsDO getUserSettingsDOById(Long id);

    long insertOrReplaceUserSettingsDO(UserSettingsDO planDO);

}
