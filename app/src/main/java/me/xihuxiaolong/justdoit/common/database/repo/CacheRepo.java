package me.xihuxiaolong.justdoit.common.database.repo;

import me.xihuxiaolong.justdoit.common.database.localentity.CacheDO;
import me.xihuxiaolong.justdoit.common.database.localentity.CacheDODao;

public class CacheRepo extends BaseRepo<CacheDO, String> {


    public CacheRepo(CacheDODao dao) {
        super(dao);
    }

}
