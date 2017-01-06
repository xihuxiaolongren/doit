package me.xihuxiaolong.justdoit.common.database.service;

import android.database.sqlite.SQLiteDatabase;

import me.xihuxiaolong.justdoit.common.database.localentity.CacheDO;
import me.xihuxiaolong.justdoit.common.database.localentity.CacheDODao;
import me.xihuxiaolong.justdoit.common.database.localentity.DaoMaster;
import me.xihuxiaolong.justdoit.common.database.localentity.DaoSession;
import me.xihuxiaolong.justdoit.common.database.repo.CacheRepo;
import me.xihuxiaolong.justdoit.common.database.repo.DbUtil;

public class CacheDataServiceImpl implements CacheDataService {

    private CacheRepo cacheRepo;

    public CacheDataServiceImpl(){
        cacheRepo = DbUtil.getCacheRepo();
    }

	@Override
	public void deleteCacheByKey(String key) {
        cacheRepo.deleteByKey(key);
    }

	@Override
	public CacheDO getCacheByKey(String key) {
        return cacheRepo.query(key);
	}

	@Override
	public long insertOrReplaceCache(CacheDO cacheDO) {
        return cacheRepo.saveOrUpdate(cacheDO);
    }
}
