package me.xihuxiaolong.justdoit.common.database.localentity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/8/8.
 */
@Entity
public class RedoPlanDO {

    @Id(autoincrement = true)
    private Long id;
    private long createdTime;
    private long modifiedTime;

    private int planType;

    /**
     * 以星期为重复周期,0-6位表示周- - 周日是否重复
     */
    private int repeatMode;

    private String title;

    private String content;

    private int startTime;
    private int startHour;
    private int startMinute;

    private int endTime;
    private int endHour;
    private int endMinute;

    @Generated(hash = 645018138)
    public RedoPlanDO(Long id, long createdTime, long modifiedTime, int planType,
            int repeatMode, String title, String content, int startTime,
            int startHour, int startMinute, int endTime, int endHour, int endMinute) {
        this.id = id;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
        this.planType = planType;
        this.repeatMode = repeatMode;
        this.title = title;
        this.content = content;
        this.startTime = startTime;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endTime = endTime;
        this.endHour = endHour;
        this.endMinute = endMinute;
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
    public int getPlanType() {
        return this.planType;
    }
    public void setPlanType(int planType) {
        this.planType = planType;
    }
    public int getRepeatMode() {
        return this.repeatMode;
    }
    public void setRepeatMode(int repeatMode) {
        this.repeatMode = repeatMode;
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

}
