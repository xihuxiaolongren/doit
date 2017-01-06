package me.xihuxiaolong.justdoit.common.database.service;

import java.util.List;

import me.xihuxiaolong.justdoit.common.database.localentity.RedoPlanDO;
import me.xihuxiaolong.justdoit.common.database.localentity.RedoPlanDODao;
import me.xihuxiaolong.justdoit.common.database.repo.DbUtil;
import me.xihuxiaolong.justdoit.common.database.repo.RedoPlanRepo;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/10/25.
 */

public class RedoPlanDataServiceImpl implements RedoPlanDataService {

    private RedoPlanRepo redoPlanRepo;

    public RedoPlanDataServiceImpl(){
        redoPlanRepo = DbUtil.getRedoPlanRepo();
    }

    @Override
    public void deleteRedoPlanById(Long id) {
        redoPlanRepo.deleteByKey(id);
    }

    @Override
    public RedoPlanDO getRedoPlanById(Long id) {
        return redoPlanRepo.query(id);
    }

    @Override
    public long insertOrReplaceRedoPlanDO(RedoPlanDO redoPlanDO) {
        if (redoPlanDO.getId() == null) {
            redoPlanDO.setCreatedTime(System.currentTimeMillis());
        }
        redoPlanDO.setModifiedTime(System.currentTimeMillis());
        return redoPlanRepo.saveOrUpdate(redoPlanDO);
    }

    @Override
    public List<RedoPlanDO> listRedoPlanDOs() {
        return redoPlanRepo.queryAll();
    }

    @Override
    public List<RedoPlanDO> listRedoPlanDOsByTarget(String targetName) {
        return redoPlanRepo.queryBuilder()
                .where(RedoPlanDODao.Properties.TargetName.eq(targetName))
                .orderDesc(RedoPlanDODao.Properties.CreatedTime).list();
    }

}
