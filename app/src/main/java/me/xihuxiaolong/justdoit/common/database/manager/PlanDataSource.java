package me.xihuxiaolong.justdoit.common.database.manager;

import android.database.sqlite.SQLiteDatabase;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import me.xihuxiaolong.justdoit.common.database.localentity.DaoMaster;
import me.xihuxiaolong.justdoit.common.database.localentity.DaoSession;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanDODao;
import me.xihuxiaolong.justdoit.common.database.localentity.RedoPlanDO;

/**
 * Created by yangxiaolong on 15/11/2.
 */
public class PlanDataSource extends BaseDataSource implements IPlanDataSource {

    IPlanHistoryDataSource planHistoryDataSource;
    IRedoPlanDataSource redoPlanDataSource;

    public PlanDataSource(){
        super();
        planHistoryDataSource = new PlanHistoryDataSource();
        redoPlanDataSource = new RedoPlanDataSource();
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
            if(planDO.getTempRepeatmode() != 0){
                ObjectMapper mapper = new ObjectMapper();
                RedoPlanDO redoPlanDO = mapper.convertValue(planDO, RedoPlanDO.class);
                redoPlanDataSource.insertOrReplaceRedoPlanDO(redoPlanDO);
            }
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

    @Override
    public List<PlanDO> createOneDayPlanDOs(Long dayTime) {
        List<RedoPlanDO> redoPlanDOs = redoPlanDataSource.listRedoPlanDOs();
        List<PlanDO> planDOs = new ArrayList<>();
        int week = new DateTime(dayTime).getDayOfWeek();
        for(RedoPlanDO redoPlanDO : redoPlanDOs){
            if(inRedoDayTime(week, redoPlanDO.getRepeatMode())){
                ObjectMapper mapper = new ObjectMapper();
                PlanDO planDO = mapper.convertValue(redoPlanDO, PlanDO.class);
                planDO.setId(null);
                planDOs.add(planDO);
            }
        }
        return planDOs;
    }

    private boolean inRedoDayTime(int week, int repeatMode){
        return ((repeatMode & (1L << week)) != 0);
    }

    @Override
    public PlanDO getPlanByLastEndTime() {
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoSession daoSession = new DaoMaster(database).newSession();

        PlanDO planDO = daoSession.getPlanDODao().queryBuilder()
                .orderDesc(PlanDODao.Properties.EndTime).unique();

        clear(daoSession, database);
        return planDO;
    }

    @Override
    public List<PlanDO> listPlanDOsByDefault() {
        return null;
    }

    private void addTag(String tag){

    }

    private void deleteTag(String label){

    }

}
