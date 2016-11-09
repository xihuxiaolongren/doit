package me.xihuxiaolong.justdoit.common.database.manager;

import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.google.gson.Gson;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import me.xihuxiaolong.justdoit.common.database.localentity.DaoMaster;
import me.xihuxiaolong.justdoit.common.database.localentity.DaoSession;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanDODao;
import me.xihuxiaolong.justdoit.common.database.localentity.RedoPlanDO;
import me.xihuxiaolong.justdoit.common.database.localentity.TagDO;
import me.xihuxiaolong.justdoit.common.database.localentity.TagDODao;

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
        if(planDO.getId() == null){
            planDO.setCreatedTime(System.currentTimeMillis());
            if(planDO.getTempRepeatmode() != 0){
//                ObjectMapper mapper = new ObjectMapper();
//                RedoPlanDO redoPlanDO = mapper.convertValue(planDO, RedoPlanDO.class);
                Gson gson = new Gson();
                RedoPlanDO redoPlanDO = gson.fromJson(gson.toJson(planDO), RedoPlanDO.class);
                redoPlanDO.setRepeatMode(planDO.getTempRepeatmode());
                redoPlanDataSource.insertOrReplaceRedoPlanDO(redoPlanDO);
            }
            if(!TextUtils.isEmpty(planDO.getTags())){
                for(String tag : planDO.getTags().split(","))
                    addTag(tag);
            }
            planHistoryDataSource.addPlan(planDO.getDayTime());
        }
        planDO.setModifiedTime(System.currentTimeMillis());

        SQLiteDatabase database = helper.getWritableDatabase();
        DaoSession daoSession = new DaoMaster(database).newSession();

        long planId = daoSession.getPlanDODao().insertOrReplace(planDO);

        clear(daoSession, database);
        return planId;
    }

    @Override
    public List<PlanDO> listPlanDOs(Long id, int count) {
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoSession daoSession = new DaoMaster(database).newSession();
        List<PlanDO> planDOs;
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

        List<PlanDO> planDOs = daoSession.getPlanDODao().queryBuilder().where(PlanDODao.Properties.DayTime.eq(dayTime)).orderAsc(PlanDODao.Properties.StartTime).list();

        clear(daoSession, database);
        return planDOs;
    }

    @Override
    public int createOneDayPlanDOs(Long dayTime) {
        List<RedoPlanDO> redoPlanDOs = redoPlanDataSource.listRedoPlanDOs();
        List<PlanDO> planDOs = new ArrayList<>();
        int week = new DateTime(dayTime).getDayOfWeek();
        for(RedoPlanDO redoPlanDO : redoPlanDOs){
            if(inRedoDayTime(week, redoPlanDO.getRepeatMode())){
                Gson gson = new Gson();
                PlanDO planDO = gson.fromJson(gson.toJson(redoPlanDO), PlanDO.class);
                planDO.setId(null);
                planDO.setDayTime(dayTime);
                insertOrReplacePlanDO(planDO);
                planDOs.add(planDO);
            }
        }
        return planDOs.size();
    }

    private boolean inRedoDayTime(int week, int repeatMode){
        return (repeatMode & (1L << (week - 1))) != 0;
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

    @Override
    public List<TagDO> listAllTag() {
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoSession daoSession = new DaoMaster(database).newSession();

        List<TagDO> tagDOs = daoSession.getTagDODao().loadAll();

        clear(daoSession, database);
        return tagDOs;
    }

    private void addTag(String tag){
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoSession daoSession = new DaoMaster(database).newSession();
        TagDO tagDO = daoSession.getTagDODao().queryBuilder().where(TagDODao.Properties.Name.eq(tag)).unique();
        if(tagDO == null){
            tagDO = new TagDO(tag, 1);
        }else{
            tagDO.setCount(tagDO.getCount() + 1);
        }
        daoSession.getTagDODao().insertOrReplace(tagDO);
        clear(daoSession, database);
    }

    private void deleteTag(String tag){
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoSession daoSession = new DaoMaster(database).newSession();
        daoSession.getTagDODao().deleteByKey(tag);
        clear(daoSession, database);
    }

}
