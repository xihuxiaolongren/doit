package me.xihuxiaolong.justdoit.common.database.localentity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/10/27.
 */
@Entity
public class TagDO implements Serializable{

    static final long serialVersionUID = -1L;

    @Id
    private String name;

    private int count;

    @Generated(hash = 515220314)
    public TagDO(String name, int count) {
        this.name = name;
        this.count = count;
    }

    @Generated(hash = 581639550)
    public TagDO() {
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

}
