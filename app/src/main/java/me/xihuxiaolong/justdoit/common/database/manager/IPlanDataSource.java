package me.xihuxiaolong.justdoit.common.database.manager;

import java.util.List;

import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;


/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/8/8.
 */
public interface IPlanDataSource {

    void deletePlanById(Long id);

    void deleteAlertById(Long id);

    void deletePhotoById(Long id);

    PlanDO getPlanDOById(Long id);

    long insertOrReplacePlanDO(PlanDO planDO);

    List<PlanDO> listPlanDOs(Long id, int count);

    List<PlanDO> listPlanDOsByOneDay(Long dayTime);

    PlanDO getPlanByLastEndTime();
}
