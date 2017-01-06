package me.xihuxiaolong.justdoit.common.database.repo;

import me.xihuxiaolong.justdoit.common.database.localentity.PlanDODao;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanHistoryDO;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanHistoryDODao;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/10/25.
 */

public class PlanHistoryRepo extends BaseRepo<PlanHistoryDO, Long> {


    public PlanHistoryRepo(PlanHistoryDODao dao) {
        super(dao);
    }

    public PlanHistoryDO queryByDayTime(long dayTime){
        return queryBuilder().where(PlanHistoryDODao.Properties.DayTime.eq(dayTime)).unique();
    }

}