package me.xihuxiaolong.justdoit.common.database.service;

import java.util.List;

import me.xihuxiaolong.justdoit.common.database.localentity.TargetDO;

public interface TargetDataService {

	void deleteTargetByKey(String key);

	TargetDO getTargetByKey(String key);

	long insertOrReplaceTarget(TargetDO targetDO);

	List<TargetDO> listAllPunchTargets();

	List<TargetDO> listAllTargets();

	List<TargetDO> listAllNormalTargets();

}
