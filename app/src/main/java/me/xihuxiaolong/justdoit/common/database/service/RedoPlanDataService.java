package me.xihuxiaolong.justdoit.common.database.service;

import java.util.List;

import me.xihuxiaolong.justdoit.common.database.localentity.RedoPlanDO;


/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/8/8.
 */
public interface RedoPlanDataService {

    void deleteRedoPlanById(Long id);

    RedoPlanDO getRedoPlanById(Long id);

    long insertOrReplaceRedoPlanDO(RedoPlanDO redoPlanDO);

    List<RedoPlanDO> listRedoPlanDOs();

    List<RedoPlanDO> listRedoPlanDOsByTarget(String targetName);

}
