package me.xihuxiaolong.justdoit.common.database.repo;

import me.xihuxiaolong.justdoit.common.database.localentity.TagDO;
import me.xihuxiaolong.justdoit.common.database.localentity.TagDODao;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/10/25.
 */

public class TagRepo extends BaseRepo<TagDO, String> {


    public TagRepo(TagDODao dao) {
        super(dao);
    }

}