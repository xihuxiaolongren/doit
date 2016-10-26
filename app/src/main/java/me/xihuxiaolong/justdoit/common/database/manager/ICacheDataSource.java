package me.xihuxiaolong.justdoit.common.database.manager;

import me.xihuxiaolong.justdoit.common.database.localentity.CacheDO;

public interface ICacheDataSource {

	void deleteCacheByKey(String key);

	CacheDO getCacheByKey(String key);

	long insertOrReplaceCache(CacheDO dbCache);

}
