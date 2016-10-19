package me.xihuxiaolong.justdoit.common.database.manager;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import me.xihuxiaolong.justdoit.common.database.localentity.DaoMaster;
import me.xihuxiaolong.justdoit.common.database.localentity.DaoSession;
import me.xihuxiaolong.justdoit.common.database.localentity.UserSettingsDO;
import me.xihuxiaolong.justdoit.common.database.localentity.UserSettingsDODao;
import me.xihuxiaolong.justdoit.common.database.localentity.UserSettingsDO;

/**
 * Created by yangxiaolong on 15/11/2.
 */
public class UserSettingsDataSource extends BaseDataSource implements IUserSettingsDataSource {

    public UserSettingsDataSource(){
        super();
    }

    @Override
    public UserSettingsDO getUserSettingsDOById(Long id) {
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoSession daoSession = new DaoMaster(database).newSession();

        UserSettingsDO userSettingsDO = daoSession.getUserSettingsDODao().queryBuilder()
                .where(UserSettingsDODao.Properties.UserId.eq(id)).unique();

        daoSession.clear();
        if(database != null && database.isOpen())
            database.close();

        return userSettingsDO;
    }

    @Override
    public long insertOrReplaceUserSettingsDO(UserSettingsDO userSettingsDO) {
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoSession daoSession = new DaoMaster(database).newSession();

        if(userSettingsDO.getUserId() == null){
            userSettingsDO.setCreatedTime(System.currentTimeMillis());
        }
        userSettingsDO.setModifiedTime(System.currentTimeMillis());
        long userSettingsId = daoSession.getUserSettingsDODao().insertOrReplace(userSettingsDO);

        daoSession.clear();
        if(database != null && database.isOpen())
            database.close();

        return userSettingsId;
    }
}
