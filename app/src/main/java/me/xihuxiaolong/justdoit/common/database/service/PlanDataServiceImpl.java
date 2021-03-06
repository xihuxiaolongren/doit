package me.xihuxiaolong.justdoit.common.database.service;

import android.text.TextUtils;

import com.google.gson.Gson;

import org.joda.time.DateTime;

import java.util.List;

import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanDODao;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanHistoryDO;
import me.xihuxiaolong.justdoit.common.database.localentity.RedoPlanDO;
import me.xihuxiaolong.justdoit.common.database.localentity.TagDO;
import me.xihuxiaolong.justdoit.common.database.repo.DbUtil;
import me.xihuxiaolong.justdoit.common.database.repo.PlanRepo;
import me.xihuxiaolong.justdoit.common.database.repo.TagRepo;

/**
 * Created by yangxiaolong on 15/11/2.
 */
public class PlanDataServiceImpl implements PlanDataService {

    PlanHistoryDataService planHistoryDataService;

    RedoPlanDataService redoPlanDataService;

    TargetDataService targetDataService;

    Gson gson = new Gson();

    private PlanRepo planRepo;
    private TagRepo tagRepo;

    public PlanDataServiceImpl(){
        planRepo = DbUtil.getPlanRepo();
        tagRepo = DbUtil.getTagRepo();
        planHistoryDataService = new PlanHistoryDataServiceImpl();
        redoPlanDataService = new RedoPlanDataServiceImpl();
        targetDataService = new TargetDataServiceImpl();
    }

    @Override
    public void deletePlanById(Long id) {
        PlanDO planDO = planRepo.query(id);
        if(planDO != null) {
            planRepo.deleteByKey(id);
            PlanHistoryDO planHistoryDO = planHistoryDataService.getPlanHistoryDOByDayTime(planDO.getDayTime());
            if(planHistoryDO != null)
                planHistoryDO.setPlanCount(planHistoryDO.getPlanCount() - 1);
                planHistoryDataService.deletePlan(planDO.getDayTime());
        }
    }

    @Override
    public void deleteAlertById(Long id) {
        PlanDO planDO = getPlanDOById(id);
        if(planDO != null) {
            deletePlanDOById(id);
            planHistoryDataService.deleteAlert(planDO.getDayTime());
        }
    }

    @Override
    public void deletePhotoById(Long id) {
        PlanDO planDO = getPlanDOById(id);
        if(planDO != null) {
            deletePlanDOById(id);
            planHistoryDataService.deletePhoto(planDO.getDayTime());
        }
    }

    @Override
    public void deletePunchById(Long id) {
        PlanDO planDO = getPlanDOById(id);
        if(planDO != null) {
            deletePlanDOById(id);
            planHistoryDataService.deletePunch(planDO.getDayTime());
        }
    }

    private void deletePlanDOById(Long id) {
        PlanDO planDO = getPlanDOById(id);
        planRepo.deleteByKey(id);
        if(planDO != null && !TextUtils.isEmpty(planDO.getTargetName()))
            targetDataService.incrTargetCount(planDO.getTargetName(), -1);
    }

    @Override
    public PlanDO getPlanDOById(Long id) {
        return planRepo.query(id);
    }

    @Override
    public long insertOrReplacePlanDO(PlanDO planDO) {
        if(planDO.getId() == null){
            planDO.setCreatedTime(System.currentTimeMillis());
            if(planDO.getTempRepeatmode() != 0){
                RedoPlanDO redoPlanDO = gson.fromJson(gson.toJson(planDO), RedoPlanDO.class);
                redoPlanDO.setRepeatMode(planDO.getTempRepeatmode());
                redoPlanDO.setTargetName(planDO.getTargetName());
                redoPlanDO.setPlanType(planDO.getType());
                Long redoPlanId = redoPlanDataService.insertOrReplaceRedoPlanDO(redoPlanDO);
                planDO.setRedoPlanId(redoPlanId);
            }
            if(!TextUtils.isEmpty(planDO.getTags())){
                for(String tag : planDO.getTags().split(","))
                    addTag(tag);
            }
            planHistoryDataService.addPlanDO(planDO.getDayTime(), planDO.getType());
            if(!TextUtils.isEmpty(planDO.getTargetName())){
                targetDataService.incrTargetCount(planDO.getTargetName(), 1);
            }
        }
        planDO.setModifiedTime(System.currentTimeMillis());

        return planRepo.saveOrUpdate(planDO);
    }

    @Override
    public List<PlanDO> listPlanDOs(Long id, int count) {
        List<PlanDO> planDOs;
        if(id == null)
            planDOs = planRepo.queryBuilder().orderDesc(PlanDODao.Properties.Id).limit(count).list();
        else
            planDOs = planRepo.queryBuilder().where(PlanDODao.Properties.Id.lt(id))
                    .orderDesc(PlanDODao.Properties.Id).limit(count).list();
        return planDOs;
    }

    @Override
    public List<PlanDO> listPlanDOsByOneDay(Long dayTime) {
        return planRepo.queryBuilder().where(PlanDODao.Properties.DayTime.eq(dayTime))
                .orderAsc(PlanDODao.Properties.StartTime).list();
    }

    @Override
    public List<PlanDO> listBacklogs(Long id, int count) {
//        List<PlanDO> planDOs;
//        if(id == null)
//            planDOs = planRepo.queryBuilder().where(PlanDODao.Properties.Type.eq(PlanDO.TYPE_BACKLOG)).orderDesc(PlanDODao.Properties.Id).limit(count).list();
//        else
//            planDOs = planRepo.queryBuilder().where(PlanDODao.Properties.Type.eq(PlanDO.TYPE_BACKLOG), PlanDODao.Properties.Id.lt(id))
//                    .orderDesc(PlanDODao.Properties.Id).limit(count).list();
//        return planDOs;
        return null;
    }

    private List<PlanDO> listRedoPlanDOsByOneDay(Long dayTime) {
        return planRepo.queryBuilder()
                .where(PlanDODao.Properties.DayTime.eq(dayTime), PlanDODao.Properties.RedoPlanId.isNotNull())
                .orderAsc(PlanDODao.Properties.StartTime).list();
    }

    @Override
    public List<PlanDO> listPlanDOsByTargetName(String targetName) {
        return planRepo.queryBuilder()
                .where(PlanDODao.Properties.TargetName.eq(targetName))
                .orderDesc(PlanDODao.Properties.StartTime).list();
    }

    @Override
    public int createOneDayPlanDOs(Long dayTime) {
        List<RedoPlanDO> redoPlanDOs = redoPlanDataService.listRedoPlanDOs();
        List<PlanDO> planDOs = listRedoPlanDOsByOneDay(dayTime);
        int count = 0;
        int weekDay = new DateTime(dayTime).getDayOfWeek();
        for(RedoPlanDO redoPlanDO : redoPlanDOs){
            if(inRedoDayTime(weekDay, redoPlanDO.getRepeatMode()) && !hasExist(redoPlanDO, planDOs)){
                Gson gson = new Gson();
                PlanDO planDO = gson.fromJson(gson.toJson(redoPlanDO), PlanDO.class);
                planDO.setId(null);
                planDO.setRedoPlanId(redoPlanDO.getId());
                planDO.setDayTime(dayTime);
                planDO.setStartTime(new DateTime(dayTime).plusHours(redoPlanDO.getStartHour()).plusMinutes(redoPlanDO.getStartMinute()).getMillis());
                planDO.setEndTime(new DateTime(dayTime).plusHours(redoPlanDO.getEndHour()).plusMinutes(redoPlanDO.getEndMinute()).getMillis());
                insertOrReplacePlanDO(planDO);
                count++;
            }
        }
        return count;
    }

    private boolean inRedoDayTime(int weekDay, int repeatMode){
        return (repeatMode & (1L << (weekDay - 1))) != 0;
    }

    private boolean hasExist(RedoPlanDO redoPlanDO, List<PlanDO> planDOs){
        for(PlanDO planDO : planDOs){
            if(redoPlanDO.getId().equals(planDO.getRedoPlanId())){
                return true;
            }
        }
        return false;
    }

    @Override
    public PlanDO getPlanByLastEndTime() {
        return planRepo.queryBuilder()
                .orderDesc(PlanDODao.Properties.EndTime).limit(1).unique();
    }

    @Override
    public List<PlanDO> listPlanDOsByDefault() {
        return null;
    }

    @Override
    public List<TagDO> listAllTag() {
        return tagRepo.queryAll();
    }

    @Override
    public List<Integer> listPlanCount(int type, List<Long> time) {
        return null;
    }

    @Override
    public Long getFirstPlanTime() {
        PlanDO planDO = planRepo.queryBuilder().orderAsc(PlanDODao.Properties.CreatedTime).limit(1).unique();
        if(planDO != null)
            return planDO.getCreatedTime();
        return null;
    }

    private void addTag(String tag){
        TagDO tagDO = tagRepo.query(tag);
        if(tagDO == null){
            tagDO = new TagDO(tag, 1);
        }else{
            tagDO.setCount(tagDO.getCount() + 1);
        }
        tagRepo.saveOrUpdate(tagDO);
    }

    private void deleteTag(String tag){
        tagRepo.deleteByKey(tag);
    }

}
