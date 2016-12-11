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

    private Long createdTime;
    private Long modifiedTime;

    private String headerImageUri;

    private int count;

    @Transient
    private List<RedoPlanDO> redoPlanDOList;

    @Generated(hash = 1388450240)
    public TargetDO(String name, Long createdTime, Long modifiedTime,
            String headerImageUri, int count) {
        this.name = name;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
        this.headerImageUri = headerImageUri;
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

    public String getHeaderImageUri() {
        return this.headerImageUri;
    }

    public void setHeaderImageUri(String headerImageUri) {
        this.headerImageUri = headerImageUri;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public void setModifiedTime(Long modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public Long getCreatedTime() {
        return this.createdTime;
    }

    public Long getModifiedTime() {
        return this.modifiedTime;
    }
}
