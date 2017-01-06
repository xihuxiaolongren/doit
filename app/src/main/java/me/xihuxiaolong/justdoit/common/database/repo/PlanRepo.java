package me.xihuxiaolong.justdoit.common.database.repo;

import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanDODao;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/10/25.
 */

public class PlanRepo extends BaseRepo<PlanDO, Long> {


    public PlanRepo(PlanDODao dao) {
        super(dao);
    }

}