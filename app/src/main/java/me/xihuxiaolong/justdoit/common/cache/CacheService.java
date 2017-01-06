package me.xihuxiaolong.justdoit.common.cache;

import com.google.gson.Gson;

import me.xihuxiaolong.justdoit.common.database.localentity.CacheDO;
import me.xihuxiaolong.justdoit.common.cache.entity.UserSettings;
import me.xihuxiaolong.justdoit.common.database.repo.CacheRepo;
import me.xihuxiaolong.justdoit.common.database.repo.DbUtil;

public class CacheService implements ICacheService {

	public static final int MONTH_EXPIRE = 30 * 24 * 60 * 60;
	public static final int WEEK_EXPIRE = 7 * 24 * 60 * 60;
	public static final int DAY_EXPIRE = 24 * 60 * 60;
	public static final int HOUR_EXPIRE = 60 * 60;
	public static final int HALF_HOUR_EXPIRE = 30 * 60;
	public static final int MINUTES_EXPIRE = 60;

	public static class Keys{
		public static final String settings = "settings";
	}


	private static CacheService sInstance;

	private static Gson gson = new Gson();

	private CacheRepo cacheRepo;

//	private ICacheDataSource cacheDataSource;

	public CacheService(){
//		cacheDataSource = new CacheDataSource();
		cacheRepo = DbUtil.getCacheRepo();
	}

	@Override
	public void delete(String key) {
		cacheRepo.deleteByKey(key);
//		cacheDataSource.deleteCacheByKey(key);
	}

	@Override
	public String get(String key) {
		CacheDO cacheDO = cacheRepo.query(key);
//		CacheDO cacheDO = cacheDataSource.getCacheByKey(key);
		if(cacheDO == null)
			return null;
		if(cacheDO.getExpire() != -1 && ((Math.abs(System.currentTimeMillis() - cacheDO.getCreateTime())) / 1000) - cacheDO.getExpire() > 0){
			delete(key);
			return null;
		}
		return cacheDO.getValue();
	}

	@Override
	public UserSettings getSettings() {
		UserSettings userSettings = gson.fromJson(get(Keys.settings), UserSettings.class);
		return userSettings;
	}

	public long put(String key, Object value) {
		return cacheRepo.saveOrUpdate(new CacheDO(key, gson.toJson(value), System.currentTimeMillis(), -1));
//		return cacheDataSource.insertOrReplaceCache(new CacheDO(key, gson.toJson(value), System.currentTimeMillis(), -1));
	}

	public long put(String key, Object value, int expireTime) {
		return cacheRepo.saveOrUpdate(new CacheDO(key, gson.toJson(value), System.currentTimeMillis(), expireTime));
//		return cacheDataSource.insertOrReplaceCache(new CacheDO(key, gson.toJson(value), System.currentTimeMillis(), expireTime));
	}

}
