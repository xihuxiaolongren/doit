package me.xihuxiaolong.justdoit.common.database.service;

import java.util.List;

import me.xihuxiaolong.justdoit.common.database.localentity.BacklogDO;


/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/8/8.
 */
public interface BacklogDataService {

    void deleteBacklogById(Long id);

    BacklogDO getBacklogById(Long id);

    long insertOrReplaceBacklogDO(BacklogDO backlogDO);

    List<BacklogDO> listBacklogDOs();

    long count();

}
