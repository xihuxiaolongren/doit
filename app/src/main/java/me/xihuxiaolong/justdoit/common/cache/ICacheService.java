package me.xihuxiaolong.justdoit.common.cache;

import me.xihuxiaolong.justdoit.common.cache.entity.UserSettings;

public interface ICacheService {

	void delete(String key) ;

	String get(String key);

	UserSettings getSettings();

	long put(String key, Object value);

	long put(String key, Object value, int expireTime);

}
