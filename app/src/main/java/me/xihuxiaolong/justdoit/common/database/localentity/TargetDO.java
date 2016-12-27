package me.xihuxiaolong.justdoit.common.database.localentity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/10/27.
 */
@Entity
public class TargetDO implements Serializable {

    static final long serialVersionUID = -1L;

    public final static int TYPE_NORMAL = 0;
    public final static int TYPE_PUNCH = 1;

    @Id
    private String name;

    private int type;

    private Long createdTime;
    private Long modifiedTime;

    private String headerImageUri;
    private boolean customTheme;
    private int themeColor;
    private int textColor;

    private int count;

    @Transient
    private List<RedoPlanDO> redoPlanDOList;

    @Transient
    private List<PlanDO> punchList;

    @Generated(hash = 743143549)
    public TargetDO(String name, int type, Long createdTime, Long modifiedTime,
            String headerImageUri, boolean customTheme, int themeColor,
            int textColor, int count) {
        this.name = name;
        this.type = type;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
        this.headerImageUri = headerImageUri;
        this.customTheme = customTheme;
        this.themeColor = themeColor;
        this.textColor = textColor;
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

    public List<PlanDO> getPunchList() {
        return punchList;
    }

    public void setPunchList(List<PlanDO> punchList) {
        this.punchList = punchList;
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

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean getCustomTheme() {
        return this.customTheme;
    }

    public void setCustomTheme(boolean customTheme) {
        this.customTheme = customTheme;
    }

    public int getThemeColor() {
        return this.themeColor;
    }

    public void setThemeColor(int themeColor) {
        this.themeColor = themeColor;
    }

    public int getTextColor() {
        return this.textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

}
