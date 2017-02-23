package me.xihuxiaolong.justdoit.common.database.service;

import java.util.List;

import me.xihuxiaolong.justdoit.common.database.localentity.TargetDO;
import me.xihuxiaolong.justdoit.common.database.localentity.TargetDODao;
import me.xihuxiaolong.justdoit.common.database.repo.DbUtil;
import me.xihuxiaolong.justdoit.common.database.repo.TargetRepo;

public class TargetDataServiceImpl implements TargetDataService {

    private TargetRepo targetRepo;

    public TargetDataServiceImpl(){
        targetRepo = DbUtil.getTargetRepo();
    }

    @Override
    public void deleteTargetByKey(String key) {
        targetRepo.deleteByKey(key);
    }

    @Override
    public TargetDO getTargetByKey(String key) {
        return targetRepo.query(key);
    }

    @Override
    public long insertOrReplaceTarget(TargetDO targetDO) {
        return targetRepo.saveOrUpdate(targetDO);
    }

    @Override
    public List<TargetDO> listAllPunchTargets() {
        return targetRepo.queryBuilder().where(TargetDODao.Properties.Type.eq(TargetDO.TYPE_PUNCH))
                .orderDesc(TargetDODao.Properties.CreatedTime).list();
    }

    @Override
    public List<TargetDO> listAllTargets() {
        return targetRepo.queryBuilder().orderDesc(TargetDODao.Properties.CreatedTime).list();
//        return targetRepo.queryAll();
    }

    @Override
    public List<TargetDO> listAllNormalTargets() {
        return targetRepo.queryBuilder().where(TargetDODao.Properties.Type.eq(TargetDO.TYPE_NORMAL))
                .orderDesc(TargetDODao.Properties.CreatedTime).list();
    }
}
