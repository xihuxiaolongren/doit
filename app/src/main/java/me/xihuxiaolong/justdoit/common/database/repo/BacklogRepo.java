package me.xihuxiaolong.justdoit.common.database.repo;

import me.xihuxiaolong.justdoit.common.database.localentity.BacklogDO;
import me.xihuxiaolong.justdoit.common.database.localentity.BacklogDODao;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanDODao;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/10/25.
 */

public class BacklogRepo extends BaseRepo<BacklogDO, Long> {


    public BacklogRepo(BacklogDODao dao) {
        super(dao);
    }

}