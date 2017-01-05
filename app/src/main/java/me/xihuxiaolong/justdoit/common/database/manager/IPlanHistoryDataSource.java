package me.xihuxiaolong.justdoit.common.database.manager;

import java.util.List;

import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanHistoryDO;


/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/8/8.
 */
public interface IPlanHistoryDataSource {

    void addPlanDO(long dayTime, int type);
    void deletePlanDO(long dayTime, int type);
//    void addPlan(long dayTime);
    void deletePlan(long dayTime);
//    void addAlert(long dayTime);
    void deleteAlert(long dayTime);
//    void addPhoto(long dayTime);
    void deletePhoto(long dayTime);
//    void addPunch(long dayTime);
    void deletePunch(long dayTime);

    PlanHistoryDO getPlanHistoryDOById(Long id);

    PlanHistoryDO getPlanHistoryDOByDayTime(long dayTime);

    List<PlanHistoryDO> listPlanHistoryDOs(long currentDayTime);

    List<PlanHistoryDO> listPlanHistoryDOs(long startDayTime, long endDayTime);

}
