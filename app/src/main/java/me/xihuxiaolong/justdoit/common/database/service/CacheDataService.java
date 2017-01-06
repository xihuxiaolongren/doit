package me.xihuxiaolong.justdoit.common.database.service;

import me.xihuxiaolong.justdoit.common.database.localentity.CacheDO;

public interface CacheDataService {

	void deleteCacheByKey(String key);

	CacheDO getCacheByKey(String key);

	long insertOrReplaceCache(CacheDO dbCache);

}
