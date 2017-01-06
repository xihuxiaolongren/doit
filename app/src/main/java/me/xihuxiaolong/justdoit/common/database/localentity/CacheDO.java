package me.xihuxiaolong.justdoit.common.database.localentity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/8/6.
 */
@Entity
public class CacheDO implements Serializable{

    static final long serialVersionUID = -1L;

    @Id
    private String key;

    private String value;

    private Long createTime;

    private int expire;

    @Generated(hash = 615759278)
    public CacheDO(String key, String value, Long createTime, int expire) {
        this.key = key;
        this.value = value;
        this.createTime = createTime;
        this.expire = expire;
    }

    @Generated(hash = 1279157005)
    public CacheDO() {
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getExpire() {
        return this.expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getCreateTime() {
        return this.createTime;
    }

}
