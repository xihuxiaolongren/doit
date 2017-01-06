package me.xihuxiaolong.justdoit.common.database.service;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import me.xihuxiaolong.justdoit.common.database.localentity.DaoMaster;
import me.xihuxiaolong.justdoit.common.database.localentity.DaoSession;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanDODao;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanHistoryDO;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanHistoryDODao;
import me.xihuxiaolong.justdoit.common.database.repo.DbUtil;
import me.xihuxiaolong.justdoit.common.database.repo.PlanHistoryRepo;

/**
 * Created by yangxiaolong on 15/11/2.
 */
public class PlanHistoryDataServiceImpl implements PlanHistoryDataService {

    private PlanHistoryRepo planHistoryRepo;

    public PlanHistoryDataServiceImpl() {
        planHistoryRepo = DbUtil.getPlanHistoryRepo();
    }

    @Override
    public void addPlanDO(long dayTime, int type) {
        switch (type) {
            case PlanDO.TYPE_ALERT:
                addAlert(dayTime);
                break;
            case PlanDO.TYPE_PHOTO:
                addPhoto(dayTime);
                break;
            case PlanDO.TYPE_PLAN:
                addPlan(dayTime);
                break;
            case PlanDO.TYPE_PUNCH:
                addPunch(dayTime);
                break;
        }
    }

    @Override
    public void deletePlanDO(long dayTime, int type) {

    }

    private void addPlan(long dayTime) {
        PlanHistoryDO planHistoryDO = getPlanHistoryDOByDayTime(dayTime);
        if (planHistoryDO != null) {
            planHistoryDO.setPlanCount(planHistoryDO.getPlanCount() + 1);
        } else {
            planHistoryDO = new PlanHistoryDO();
            planHistoryDO.setPlanCount(1);
            planHistoryDO.setDayTime(dayTime);
        }
        planHistoryRepo.saveOrUpdate(planHistoryDO);
    }

    @Override
    public void deletePlan(long dayTime) {
        PlanHistoryDO planHistoryDO = getPlanHistoryDOByDayTime(dayTime);
        if (planHistoryDO != null) {
            planHistoryDO.setPlanCount(planHistoryDO.getPlanCount() - 1);
            planHistoryRepo.update(planHistoryDO);
        }
    }

    //    @Override
    private void addAlert(long dayTime) {
        PlanHistoryDO planHistoryDO = getPlanHistoryDOByDayTime(dayTime);
        if (planHistoryDO != null) {
            planHistoryDO.setAlertCount(planHistoryDO.getAlertCount() + 1);
        } else {
            planHistoryDO = new PlanHistoryDO();
            planHistoryDO.setAlertCount(1);
            planHistoryDO.setDayTime(dayTime);
        }
        planHistoryRepo.saveOrUpdate(planHistoryDO);
    }

    @Override
    public void deleteAlert(long dayTime) {
        PlanHistoryDO planHistoryDO = getPlanHistoryDOByDayTime(dayTime);
        if (planHistoryDO != null) {
            planHistoryDO.setAlertCount(planHistoryDO.getAlertCount() - 1);
            planHistoryRepo.update(planHistoryDO);
        }
    }

    //    @Override
    private void addPhoto(long dayTime) {
        PlanHistoryDO planHistoryDO = getPlanHistoryDOByDayTime(dayTime);
        if (planHistoryDO != null) {
            planHistoryDO.setPhotoCount(planHistoryDO.getPhotoCount() + 1);
        } else {
            planHistoryDO = new PlanHistoryDO();
            planHistoryDO.setPhotoCount(1);
            planHistoryDO.setDayTime(dayTime);
        }
        planHistoryRepo.saveOrUpdate(planHistoryDO);
    }

    @Override
    public void deletePhoto(long dayTime) {
        PlanHistoryDO planHistoryDO = getPlanHistoryDOByDayTime(dayTime);
        if (planHistoryDO != null) {
            planHistoryDO.setPhotoCount(planHistoryDO.getPhotoCount() - 1);
            planHistoryRepo.update(planHistoryDO);
        }
    }

    //    @Override
    private void addPunch(long dayTime) {
        PlanHistoryDO planHistoryDO = getPlanHistoryDOByDayTime(dayTime);
        if (planHistoryDO != null) {
            planHistoryDO.setPunchCount(planHistoryDO.getPunchCount() + 1);
        } else {
            planHistoryDO = new PlanHistoryDO();
            planHistoryDO.setPunchCount(1);
            planHistoryDO.setDayTime(dayTime);
        }
        planHistoryRepo.saveOrUpdate(planHistoryDO);
    }

    @Override
    public void deletePunch(long dayTime) {
        PlanHistoryDO planHistoryDO = getPlanHistoryDOByDayTime(dayTime);
        if (planHistoryDO != null) {
            planHistoryDO.setPunchCount(planHistoryDO.getPunchCount() - 1);
            planHistoryRepo.update(planHistoryDO);
        }
    }

    @Override
    public PlanHistoryDO getPlanHistoryDOById(Long id) {
        return planHistoryRepo.query(id);
    }

    @Override
    public PlanHistoryDO getPlanHistoryDOByDayTime(long dayTime) {
        return planHistoryRepo.queryByDayTime(dayTime);
    }

    @Override
    public List<PlanHistoryDO> listPlanHistoryDOs(long currentDayTime) {
        return  planHistoryRepo.queryBuilder().where(PlanHistoryDODao.Properties.DayTime.lt(currentDayTime)).orderDesc(PlanDODao.Properties.DayTime).list();
    }

    @Override
    public List<PlanHistoryDO> listPlanHistoryDOs(long startDayTime, long endDayTime) {
        return planHistoryRepo.queryBuilder()
                .where(PlanHistoryDODao.Properties.DayTime.le(endDayTime), PlanHistoryDODao.Properties.DayTime.ge(startDayTime))
                .orderAsc(PlanHistoryDODao.Properties.DayTime).list();
    }
}
