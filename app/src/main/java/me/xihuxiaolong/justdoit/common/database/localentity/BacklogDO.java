package me.xihuxiaolong.justdoit.common.database.localentity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/8/8.
 */
@Entity
public class BacklogDO implements Serializable{

    static final long serialVersionUID = -1L;

    @Id(autoincrement = true)
    private Long id;
    private Long createdTime;
    private Long modifiedTime;

    private String content;

    @Generated(hash = 518650937)
    public BacklogDO(Long id, Long createdTime, Long modifiedTime, String content) {
        this.id = id;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
        this.content = content;
    }

    @Generated(hash = 1457940267)
    public BacklogDO() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatedTime() {
        return this.createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public Long getModifiedTime() {
        return this.modifiedTime;
    }

    public void setModifiedTime(Long modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
