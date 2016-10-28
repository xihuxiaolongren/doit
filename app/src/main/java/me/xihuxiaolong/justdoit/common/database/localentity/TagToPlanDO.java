package me.xihuxiaolong.justdoit.common.database.localentity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/10/27.
 */
@Entity
public class TagToPlanDO {

    private long planId;

    private String tagName;

    @Generated(hash = 1205280131)
    public TagToPlanDO(long planId, String tagName) {
        this.planId = planId;
        this.tagName = tagName;
    }

    @Generated(hash = 810672552)
    public TagToPlanDO() {
    }

    public long getPlanId() {
        return this.planId;
    }

    public void setPlanId(long planId) {
        this.planId = planId;
    }

    public String getTagName() {
        return this.tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
