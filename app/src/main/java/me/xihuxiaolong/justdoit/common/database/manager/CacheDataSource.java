package me.xihuxiaolong.justdoit.common.database.manager;

import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;

import me.xihuxiaolong.justdoit.common.database.localentity.CacheDO;
import me.xihuxiaolong.justdoit.common.database.localentity.CacheDODao;
import me.xihuxiaolong.justdoit.common.database.localentity.DaoMaster;
import me.xihuxiaolong.justdoit.common.database.localentity.DaoSession;

public class CacheDataSource extends BaseDataSource implements ICacheDataSource {


	@Override
	public void deleteCacheByKey(String key) {
		SQLiteDatabase database = helper.getWritableDatabase();
		DaoSession daoSession = new DaoMaster(database).newSession();
		daoSession.getCacheDODao().queryBuilder()
				.where(CacheDODao.Properties.Key.eq(key))
				.buildDelete()
				.executeDeleteWithoutDetachingEntities();
        clear(daoSession, database);
    }

	@Override
	public CacheDO getCacheByKey(String key) {
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoSession daoSession = new DaoMaster(database).newSession();

        CacheDO cacheDO = daoSession.getCacheDODao().queryBuilder()
                .where(CacheDODao.Properties.Key.eq(key)).unique();

        clear(daoSession, database);
        return cacheDO;
	}

	@Override
	public long insertOrReplaceCache(CacheDO cacheDO) {
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoSession daoSession = new DaoMaster(database).newSession();

        long cacheId = daoSession.getCacheDODao().insertOrReplace(cacheDO);

        clear(daoSession, database);
        return cacheId;
    }
}
