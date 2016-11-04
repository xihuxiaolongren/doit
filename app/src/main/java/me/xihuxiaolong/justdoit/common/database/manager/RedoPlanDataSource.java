package me.xihuxiaolong.justdoit.common.database.manager;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import me.xihuxiaolong.justdoit.common.database.localentity.DaoMaster;
import me.xihuxiaolong.justdoit.common.database.localentity.DaoSession;
import me.xihuxiaolong.justdoit.common.database.localentity.RedoPlanDO;
import me.xihuxiaolong.justdoit.common.database.localentity.RedoPlanDODao;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/10/25.
 */

public class RedoPlanDataSource extends BaseDataSource implements IRedoPlanDataSource {

    @Override
    public void deleteRedoPlanById(Long id) {
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoSession daoSession = new DaoMaster(database).newSession();
        daoSession.getRedoPlanDODao().deleteByKey(id);
        clear(daoSession, database);
    }

    @Override
    public RedoPlanDO getRedoPlanDOById(Long id) {
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoSession daoSession = new DaoMaster(database).newSession();

        RedoPlanDO redoPlanDO = daoSession.getRedoPlanDODao().queryBuilder()
                .where(RedoPlanDODao.Properties.Id.eq(id)).unique();

        clear(daoSession, database);
        return redoPlanDO;
    }

    @Override
    public long insertOrReplaceRedoPlanDO(RedoPlanDO redoPlanDO) {
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoSession daoSession = new DaoMaster(database).newSession();

        if(redoPlanDO.getId() == null){
            redoPlanDO.setCreatedTime(System.currentTimeMillis());
        }
        redoPlanDO.setModifiedTime(System.currentTimeMillis());
        long redoPlanId = daoSession.getRedoPlanDODao().insertOrReplace(redoPlanDO);

        clear(daoSession, database);
        return redoPlanId;
    }

    @Override
    public List<RedoPlanDO> listRedoPlanDOs() {
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoSession daoSession = new DaoMaster(database).newSession();
        List<RedoPlanDO> redoPlanDOs = daoSession.getRedoPlanDODao().queryBuilder().orderDesc(RedoPlanDODao.Properties.Id).list();
        clear(daoSession, database);
        return redoPlanDOs;
    }
}
