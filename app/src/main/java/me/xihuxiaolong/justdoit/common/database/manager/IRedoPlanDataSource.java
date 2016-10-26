package me.xihuxiaolong.justdoit.common.database.manager;

import java.util.List;

import me.xihuxiaolong.justdoit.common.database.localentity.RedoPlanDO;


/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/8/8.
 */
public interface IRedoPlanDataSource {

    void deleteRedoPlanById(Long id);

    RedoPlanDO getRedoPlanDOById(Long id);

    long insertOrReplaceRedoPlanDO(RedoPlanDO redoPlanDO);

    List<RedoPlanDO> listRedoPlanDOs(Long id);

}
