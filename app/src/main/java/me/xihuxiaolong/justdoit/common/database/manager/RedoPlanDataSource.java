package me.xihuxiaolong.justdoit.common.database.manager;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import me.xihuxiaolong.justdoit.common.database.localentity.DaoMaster;
import me.xihuxiaolong.justdoit.common.database.localentity.DaoSession;
import me.xihuxiaolong.justdoit.common.database.localentity.RedoPlanDO;
import me.xihuxiaolong.justdoit.common.database.localentity.RedoPlanDODao;
import me.xihuxiaolong.justdoit.common.database.localentity.TargetDO;
import me.xihuxiaolong.justdoit.common.database.localentity.TargetDODao;
import me.xihuxiaolong.library.utils.CollectionUtils;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/10/25.
 */

public class RedoPlanDataSource extends BaseDataSource implements IRedoPlanDataSource {


    public RedoPlanDataSource(){
        super();
    }

    @Override
    public void deleteRedoPlanById(Long id) {
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoSession daoSession = new DaoMaster(database).newSession();
        daoSession.getRedoPlanDODao().deleteByKey(id);
        clear(daoSession, database);
    }

    @Override
    public RedoPlanDO getRedoPlanById(Long id) {
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoSession daoSession = new DaoMaster(database).newSession();

        RedoPlanDO redoPlanDO = daoSession.getRedoPlanDODao().queryBuilder()
                .where(RedoPlanDODao.Properties.Id.eq(id)).unique();

        clear(daoSession, database);
        return redoPlanDO;
    }

    @Override
    public TargetDO getTargetByName(String targetName, boolean withRedoPlanList) {
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoSession daoSession = new DaoMaster(database).newSession();

        TargetDO targetDO = daoSession.getTargetDODao().queryBuilder()
                .where(TargetDODao.Properties.Name.eq(targetName)).unique();
        if(targetDO != null && withRedoPlanList) {
            if(targetDO.getType() == TargetDO.TYPE_NORMAL) {
                PlanDataSource planDataSource = new PlanDataSource();
                targetDO.setPunchList(planDataSource.listPlanDOsByTargetName(targetName));
            } else if(targetDO.getType() == TargetDO.TYPE_PUNCH) {
                PlanDataSource planDataSource = new PlanDataSource();
                targetDO.setPunchList(planDataSource.listPlanDOsByTargetName(targetName));
            }
        }
        clear(daoSession, database);
        return targetDO;
    }

    @Override
    public long insertOrReplaceRedoPlanDO(RedoPlanDO redoPlanDO) {
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoSession daoSession = new DaoMaster(database).newSession();

        if (redoPlanDO.getId() == null) {
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

    @Override
    public List<RedoPlanDO> listRedoPlanDOsByTarget(String targetName) {
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoSession daoSession = new DaoMaster(database).newSession();

        List<RedoPlanDO> redoPlanDOs = daoSession.getRedoPlanDODao().queryBuilder().where(RedoPlanDODao.Properties.TargetName.eq(targetName)).orderDesc(RedoPlanDODao.Properties.CreatedTime).list();

        clear(daoSession, database);
        return redoPlanDOs;
    }

    @Override
    public List<TargetDO> listAllTarget(boolean withRedoPlanList) {
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoSession daoSession = new DaoMaster(database).newSession();
        List<TargetDO> targetDOs = daoSession.getTargetDODao().queryBuilder().orderDesc(TargetDODao.Properties.CreatedTime).list();
        if(!CollectionUtils.isEmpty(targetDOs) && withRedoPlanList) {
            for(TargetDO targetDO : targetDOs)
                targetDO.setRedoPlanDOList(listRedoPlanDOsByTarget(targetDO.getName()));
        }
        clear(daoSession, database);
        return targetDOs;
    }

    @Override
    public List<TargetDO> listAllPunchTarget(boolean withPunchList) {
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoSession daoSession = new DaoMaster(database).newSession();
        List<TargetDO> targetDOs = daoSession.getTargetDODao().queryBuilder().where(TargetDODao.Properties.Type.eq(TargetDO.TYPE_PUNCH))
                .orderDesc(TargetDODao.Properties.CreatedTime).list();
        clear(daoSession, database);
        return targetDOs;
    }

    @Override
    public List<TargetDO> listNormalTarget() {
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoSession daoSession = new DaoMaster(database).newSession();
        List<TargetDO> targetDOs = daoSession.getTargetDODao().queryBuilder().where(TargetDODao.Properties.Type.eq(TargetDO.TYPE_NORMAL))
                .orderDesc(TargetDODao.Properties.CreatedTime).list();
        clear(daoSession, database);
        return targetDOs;
    }

    @Override
    public long insertOrReplaceTargetDO(TargetDO targetDO) {
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoSession daoSession = new DaoMaster(database).newSession();

        if (targetDO.getCreatedTime() == null) {
            targetDO.setCreatedTime(System.currentTimeMillis());
        }
        targetDO.setModifiedTime(System.currentTimeMillis());
        long targetId = daoSession.getTargetDODao().insertOrReplace(targetDO);

        clear(daoSession, database);
        return targetId;
    }

    @Override
    public void deleteTargetByName(String targetName) {
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoSession daoSession = new DaoMaster(database).newSession();
        daoSession.getTargetDODao().deleteByKey(targetName);
        clear(daoSession, database);
    }
}
