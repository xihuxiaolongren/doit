package me.xihuxiaolong.justdoit.common.database.manager;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import me.xihuxiaolong.justdoit.common.database.localentity.DaoMaster;
import me.xihuxiaolong.justdoit.common.database.localentity.DaoSession;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanDODao;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanHistoryDO;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanHistoryDODao;

/**
 * Created by yangxiaolong on 15/11/2.
 */
public class PlanHistoryDataSource extends BaseDataSource implements IPlanHistoryDataSource {

    public PlanHistoryDataSource(){
        super();
    }

    @Override
    public void addPlanDO(long dayTime, int type) {
        switch (type){
            case PlanDO.TYPE_ALERT:
                addAlert(dayTime);
                break;
            case PlanDO.TYPE_PHOTO:
                addPhoto(dayTime);
                break;
            case PlanDO.TYPE_PLAN:
                addPlan(dayTime);
                break;
        }
    }

    @Override
    public void deletePlanDO(long dayTime, int type) {

    }

    @Override
    public void addPlan(long dayTime) {
        PlanHistoryDO planHistoryDO = getPlanHistoryDOByDayTime(dayTime);

        SQLiteDatabase database = helper.getWritableDatabase();
        DaoSession daoSession = new DaoMaster(database).newSession();
        if(planHistoryDO != null){
            planHistoryDO.setPlanCount(planHistoryDO.getPlanCount() + 1);
        }else {
            planHistoryDO = new PlanHistoryDO(null, dayTime, 1, 0, 0);
        }
        daoSession.getPlanHistoryDODao().insertOrReplace(planHistoryDO);

        clear(daoSession, database);
    }

    @Override
    public void deletePlan(long dayTime) {
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoSession daoSession = new DaoMaster(database).newSession();
        PlanHistoryDO planHistoryDO = getPlanHistoryDOByDayTime(dayTime);
        if(planHistoryDO != null){
            planHistoryDO.setPlanCount(planHistoryDO.getPlanCount() - 1);
            if(planHistoryDO.getAlertCount() <= 0 && planHistoryDO.getPhotoCount() <= 0 && planHistoryDO.getPlanCount() <= 0)
                daoSession.getPlanHistoryDODao().deleteByKey(planHistoryDO.getId());
            else
                daoSession.getPlanHistoryDODao().update(planHistoryDO);
        }

        clear(daoSession, database);
    }

    @Override
    public void addAlert(long dayTime) {
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoSession daoSession = new DaoMaster(database).newSession();
        PlanHistoryDO planHistoryDO = getPlanHistoryDOByDayTime(dayTime);
        if(planHistoryDO != null){
            planHistoryDO.setPlanCount(planHistoryDO.getAlertCount() + 1);
        }else {
            planHistoryDO = new PlanHistoryDO(null, dayTime, 0, 1, 0);
        }
        daoSession.getPlanHistoryDODao().insertOrReplace(planHistoryDO);

        clear(daoSession, database);
    }

    @Override
    public void deleteAlert(long dayTime) {
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoSession daoSession = new DaoMaster(database).newSession();
        PlanHistoryDO planHistoryDO = getPlanHistoryDOByDayTime(dayTime);
        if(planHistoryDO != null){
            planHistoryDO.setAlertCount(planHistoryDO.getAlertCount() - 1);
            if(planHistoryDO.getAlertCount() <= 0 && planHistoryDO.getPhotoCount() <= 0 && planHistoryDO.getPlanCount() <= 0)
                daoSession.getPlanHistoryDODao().deleteByKey(planHistoryDO.getId());
            else
                daoSession.getPlanHistoryDODao().update(planHistoryDO);
        }

        clear(daoSession, database);
    }

    @Override
    public void addPhoto(long dayTime) {
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoSession daoSession = new DaoMaster(database).newSession();
        PlanHistoryDO planHistoryDO = getPlanHistoryDOByDayTime(dayTime);
        if(planHistoryDO != null){
            planHistoryDO.setPlanCount(planHistoryDO.getPhotoCount() + 1);
        }else {
            planHistoryDO = new PlanHistoryDO(null, dayTime, 0, 0, 1);
        }
        daoSession.getPlanHistoryDODao().insertOrReplace(planHistoryDO);

        clear(daoSession, database);
    }

    @Override
    public void deletePhoto(long dayTime) {
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoSession daoSession = new DaoMaster(database).newSession();
        PlanHistoryDO planHistoryDO = getPlanHistoryDOByDayTime(dayTime);
        if(planHistoryDO != null){
            planHistoryDO.setPhotoCount(planHistoryDO.getPhotoCount() - 1);
            if(planHistoryDO.getAlertCount() <= 0 && planHistoryDO.getPhotoCount() <= 0 && planHistoryDO.getPlanCount() <= 0)
                daoSession.getPlanHistoryDODao().deleteByKey(planHistoryDO.getId());
            else
                daoSession.getPlanHistoryDODao().update(planHistoryDO);
        }

        clear(daoSession, database);
    }

    @Override
    public PlanHistoryDO getPlanHistoryDOById(Long id) {
        return null;
    }

    @Override
    public PlanHistoryDO getPlanHistoryDOByDayTime(long dayTime) {
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoSession daoSession = new DaoMaster(database).newSession();
        PlanHistoryDO planHistoryDO = daoSession.getPlanHistoryDODao().queryBuilder().where(PlanHistoryDODao.Properties.DayTime.eq(dayTime)).unique();

        clear(daoSession, database);
        return planHistoryDO;
    }

    @Override
    public List<PlanHistoryDO> listPlanHistoryDOs(long currentDayTime) {
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoSession daoSession = new DaoMaster(database).newSession();

        List<PlanHistoryDO> planHistoryDOs = daoSession.getPlanHistoryDODao().queryBuilder().where(PlanHistoryDODao.Properties.DayTime.lt(currentDayTime)).orderDesc(PlanDODao.Properties.DayTime).list();

        clear(daoSession, database);
        return planHistoryDOs;
    }

    @Override
    public List<PlanHistoryDO> listPlanHistoryDOs(long startDayTime, long endDayTime) {
        return null;
    }
}
