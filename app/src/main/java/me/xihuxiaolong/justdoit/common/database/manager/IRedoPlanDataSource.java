package me.xihuxiaolong.justdoit.common.database.manager;

import java.util.List;

import me.xihuxiaolong.justdoit.common.database.localentity.RedoPlanDO;
import me.xihuxiaolong.justdoit.common.database.localentity.TargetDO;


/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/8/8.
 */
public interface IRedoPlanDataSource {

    void deleteRedoPlanById(Long id);

    RedoPlanDO getRedoPlanById(Long id);

    TargetDO getTargetByName(String targetName, boolean withRedoPlanList);

    long insertOrReplaceRedoPlanDO(RedoPlanDO redoPlanDO);

    List<RedoPlanDO> listRedoPlanDOs();

    List<RedoPlanDO> listRedoPlanDOsByTarget(String targetName);

    List<TargetDO> listAllTarget(boolean withRedoPlanList);

    List<TargetDO> listAllPunchTarget(boolean withPunchList);

    long insertOrReplaceTargetDO(TargetDO targetDO);

    void deleteTargetByName(String targetName);

}
