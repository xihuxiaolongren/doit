package me.xihuxiaolong.justdoit.common.database.repo;

import me.xihuxiaolong.justdoit.common.database.localentity.TargetDO;
import me.xihuxiaolong.justdoit.common.database.localentity.TargetDODao;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/10/25.
 */

public class TargetRepo extends BaseRepo<TargetDO, String> {

    public TargetRepo(TargetDODao dao) {
        super(dao);
    }

}