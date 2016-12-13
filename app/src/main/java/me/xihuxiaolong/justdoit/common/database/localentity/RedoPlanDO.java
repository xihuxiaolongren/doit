package me.xihuxiaolong.justdoit.common.database.localentity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/8/8.
 */
@Entity
public class RedoPlanDO implements Serializable{

    static final long serialVersionUID = 42L;

    @Id(autoincrement = true)
    private Long id;
    private Long createdTime;
    private Long modifiedTime;

    /**
     * 以星期为重复周期,0-6位表示周- 到 周日是否重复
     */
    private int repeatMode;

    private int planType;

    private String title;

    private String content;

    private String tags;
    private String linkAppName;
    private String linkAppPackageName;

    private int startTime;
    private int startHour;
    private int startMinute;

    private int endTime;
    private int endHour;
    private int endMinute;

    private long dayTime;

    private String targetName;

    @Generated(hash = 251670105)
    public RedoPlanDO(Long id, Long createdTime, Long modifiedTime, int repeatMode,
            int planType, String title, String content, String tags,
            String linkAppName, String linkAppPackageName, int startTime,
            int startHour, int startMinute, int endTime, int endHour,
            int endMinute, long dayTime, String targetName) {
        this.id = id;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
        this.repeatMode = repeatMode;
        this.planType = planType;
        this.title = title;
        this.content = content;
        this.tags = tags;
        this.linkAppName = linkAppName;
        this.linkAppPackageName = linkAppPackageName;
        this.startTime = startTime;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endTime = endTime;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.dayTime = dayTime;
        this.targetName = targetName;
    }

    @Generated(hash = 585329377)
    public RedoPlanDO() {
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

    public int getRepeatMode() {
        return this.repeatMode;
    }

    public void setRepeatMode(int repeatMode) {
        this.repeatMode = repeatMode;
    }

    public int getPlanType() {
        return this.planType;
    }

    public void setPlanType(int planType) {
        this.planType = planType;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTags() {
        return this.tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getLinkAppName() {
        return this.linkAppName;
    }

    public void setLinkAppName(String linkAppName) {
        this.linkAppName = linkAppName;
    }

    public String getLinkAppPackageName() {
        return this.linkAppPackageName;
    }

    public void setLinkAppPackageName(String linkAppPackageName) {
        this.linkAppPackageName = linkAppPackageName;
    }

    public int getStartTime() {
        return this.startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getStartHour() {
        return this.startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getStartMinute() {
        return this.startMinute;
    }

    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }

    public int getEndTime() {
        return this.endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public int getEndHour() {
        return this.endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getEndMinute() {
        return this.endMinute;
    }

    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }

    public long getDayTime() {
        return this.dayTime;
    }

    public void setDayTime(long dayTime) {
        this.dayTime = dayTime;
    }

    public String getTargetName() {
        return this.targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }


}
