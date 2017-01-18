package me.xihuxiaolong.justdoit.common.database.service;

import java.util.List;

import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
import me.xihuxiaolong.justdoit.common.database.localentity.TagDO;


/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/8/8.
 */
public interface PlanDataService {

    void deletePlanById(Long id);

    void deleteAlertById(Long id);

    void deletePhotoById(Long id);

    void deletePunchById(Long id);

    PlanDO getPlanDOById(Long id);

    long insertOrReplacePlanDO(PlanDO planDO);

    List<PlanDO> listPlanDOs(Long id, int count);

    List<PlanDO> listPlanDOsByOneDay(Long dayTime);

    List<PlanDO> listBacklogs(Long id, int count);

    List<PlanDO> listPlanDOsByTargetName(String targetName);

    int createOneDayPlanDOs(Long dayTime);

    PlanDO getPlanByLastEndTime();

    List<PlanDO> listPlanDOsByDefault();

    List<TagDO> listAllTag();

    List<Integer> listPlanCount(int type, List<Long> time);

}
