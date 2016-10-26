package me.xihuxiaolong.justdoit.common.database.localentity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Index;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/8/6.
 */
@Entity
public class CacheDO {

    @Index(unique = true)
    private String key;

    private String value;

    private long createTime;

    private int expire;

    @Generated(hash = 1255356223)
    public CacheDO(String key, String value, long createTime, int expire) {
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

    public long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getExpire() {
        return this.expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

}
