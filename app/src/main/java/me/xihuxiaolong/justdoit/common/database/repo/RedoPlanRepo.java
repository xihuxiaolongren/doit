package me.xihuxiaolong.justdoit.common.database.repo;

import me.xihuxiaolong.justdoit.common.database.localentity.RedoPlanDO;
import me.xihuxiaolong.justdoit.common.database.localentity.RedoPlanDODao;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/10/25.
 */

public class RedoPlanRepo extends BaseRepo<RedoPlanDO, Long> {


    public RedoPlanRepo(RedoPlanDODao dao) {
        super(dao);
    }

}