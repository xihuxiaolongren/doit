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

    long insertOrReplaceRedoPlanDO(RedoPlanDO redoPlanDO);

    List<RedoPlanDO> listRedoPlanDOs();

    List<TargetDO> listAllTarget();

    long insertOrReplaceTargetDO(TargetDO targetDO);

    void deleteTargetById(TargetDO targetDO);

}
