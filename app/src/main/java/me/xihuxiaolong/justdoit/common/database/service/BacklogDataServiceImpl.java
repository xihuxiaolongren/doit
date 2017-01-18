package me.xihuxiaolong.justdoit.common.database.service;

import java.util.List;

import me.xihuxiaolong.justdoit.common.database.localentity.BacklogDO;
import me.xihuxiaolong.justdoit.common.database.localentity.BacklogDODao;
import me.xihuxiaolong.justdoit.common.database.repo.BacklogRepo;
import me.xihuxiaolong.justdoit.common.database.repo.DbUtil;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 17/1/18.
 */

public class BacklogDataServiceImpl implements BacklogDataService {

    private BacklogRepo backlogRepo;

    public BacklogDataServiceImpl(){
        backlogRepo = DbUtil.getBacklogRepo();
    }

    @Override
    public void deleteBacklogById(Long id) {
        backlogRepo.deleteByKey(id);
    }

    @Override
    public BacklogDO getBacklogById(Long id) {
        return backlogRepo.query(id);
    }

    @Override
    public long insertOrReplaceBacklogDO(BacklogDO backlogDO) {
        if (backlogDO.getId() == null) {
            backlogDO.setCreatedTime(System.currentTimeMillis());
        }
        backlogDO.setModifiedTime(System.currentTimeMillis());
        return backlogRepo.saveOrUpdate(backlogDO);
    }

    @Override
    public List<BacklogDO> listBacklogDOs() {
        return backlogRepo.queryBuilder()
                .orderDesc(BacklogDODao.Properties.Id).list();
    }
}
