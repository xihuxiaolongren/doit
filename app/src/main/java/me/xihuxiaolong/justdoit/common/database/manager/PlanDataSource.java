package me.xihuxiaolong.justdoit.common.database.manager;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import me.xihuxiaolong.justdoit.common.database.localentity.DaoMaster;
import me.xihuxiaolong.justdoit.common.database.localentity.DaoSession;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanDODao;

/**
 * Created by yangxiaolong on 15/11/2.
 */
public class PlanDataSource extends BaseDataSource implements IPlanDataSource {

    IPlanHistoryDataSource planHistoryDataSource;

    public PlanDataSource(){
        super();
        planHistoryDataSource = new PlanHistoryDataSource();
    }

    @Override
    public void deletePlanById(Long id) {
        PlanDO planDO = getPlanDOById(id);
        if(planDO != null) {
            deletePlanDOById(id);
            planHistoryDataSource.deletePlan(planDO.getDayTime());
        }
    }

    @Override
    public void deleteAlertById(Long id) {
        PlanDO planDO = getPlanDOById(id);
        if(planDO != null) {
            deletePlanDOById(id);
            planHistoryDataSource.deleteAlert(planDO.getDayTime());
        }
    }

    @Override
    public void deletePhotoById(Long id) {
        PlanDO planDO = getPlanDOById(id);
        if(planDO != null) {
            deletePlanDOById(id);
            planHistoryDataSource.deletePhoto(planDO.getDayTime());
        }
    }

    private void deletePlanDOById(Long id) {
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoSession daoSession = new DaoMaster(database).newSession();
        daoSession.getPlanDODao().deleteByKey(id);
        clear(daoSession, database);
    }

    @Override
    public PlanDO getPlanDOById(Long id) {
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoSession daoSession = new DaoMaster(database).newSession();

        PlanDO planDO = daoSession.getPlanDODao().queryBuilder()
                .where(PlanDODao.Properties.Id.eq(id)).unique();

        clear(daoSession, database);
        return planDO;
    }

    @Override
    public long insertOrReplacePlanDO(PlanDO planDO) {
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoSession daoSession = new DaoMaster(database).newSession();

        if(planDO.getId() == null){
            planDO.setCreatedTime(System.currentTimeMillis());
        }else {
            planHistoryDataSource.addPlan(planDO.getDayTime());
        }
        planDO.setModifiedTime(System.currentTimeMillis());
        long planId = daoSession.getPlanDODao().insertOrReplace(planDO);

        clear(daoSession, database);
        return planId;
    }

    @Override
    public List<PlanDO> listPlanDOs(Long id, int count) {
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoSession daoSession = new DaoMaster(database).newSession();
        List<PlanDO> planDOs = null;
        if(id == null)
            planDOs = daoSession.getPlanDODao().queryBuilder().orderDesc(PlanDODao.Properties.Id).limit(count).list();
        else
            planDOs = daoSession.getPlanDODao().queryBuilder()
                    .where(PlanDODao.Properties.Id.lt(id)).orderDesc(PlanDODao.Properties.Id).limit(count).list();

        clear(daoSession, database);
        return planDOs;
    }

    @Override
    public List<PlanDO> listPlanDOsByOneDay(Long dayTime) {
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoSession daoSession = new DaoMaster(database).newSession();

        List<PlanDO> planDOs = null;
        planDOs = daoSession.getPlanDODao().queryBuilder().where(PlanDODao.Properties.DayTime.eq(dayTime)).orderAsc(PlanDODao.Properties.StartTime).list();

        clear(daoSession, database);
        return planDOs;
    }

    private void addTag(String tag){

    }

    private void deleteTag(String label){

    }

}
