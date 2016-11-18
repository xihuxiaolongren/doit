package me.xihuxiaolong.justdoit.common.database.localentity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/10/27.
 */
@Entity
public class TargetDO {

    @Id
    private String name;

    private long createdTime;
    private long modifiedTime;

    private int count;

    @Transient
    private List<RedoPlanDO> redoPlanDOList;

    @Generated(hash = 778472336)
    public TargetDO(String name, long createdTime, long modifiedTime, int count) {
        this.name = name;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
        this.count = count;
    }

    @Generated(hash = 1463211331)
    public TargetDO() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<RedoPlanDO> getRedoPlanDOList() {
        return redoPlanDOList;
    }

    public void setRedoPlanDOList(List<RedoPlanDO> redoPlanDOList) {
        this.redoPlanDOList = redoPlanDOList;
    }

    public long getCreatedTime() {
        return this.createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public long getModifiedTime() {
        return this.modifiedTime;
    }

    public void setModifiedTime(long modifiedTime) {
        this.modifiedTime = modifiedTime;
    }
}
