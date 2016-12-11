package me.xihuxiaolong.justdoit.common.database.localentity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/10/27.
 */
@Entity
public class TagToPlanDO {

    private Long planId;

    private String tagName;

    @Generated(hash = 226136809)
    public TagToPlanDO(Long planId, String tagName) {
        this.planId = planId;
        this.tagName = tagName;
    }

    @Generated(hash = 810672552)
    public TagToPlanDO() {
    }

    public String getTagName() {
        return this.tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public Long getPlanId() {
        return this.planId;
    }
}
