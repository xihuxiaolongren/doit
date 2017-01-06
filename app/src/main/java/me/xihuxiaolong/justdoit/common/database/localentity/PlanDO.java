package me.xihuxiaolong.justdoit.common.database.localentity;

//import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class PlanDO implements MultiItemEntity, Serializable{

    static final long serialVersionUID = -1L;

    public final static int TYPE_PLAN = 0;
    public final static int TYPE_ALERT = 1;
    public final static int TYPE_PHOTO = 2;
    public final static int TYPE_PUNCH = 3;

    @Id(autoincrement = true)
    private Long id;
    private Long createdTime;
    private Long modifiedTime;

    private int type;

    private Long redoPlanId;

    private String title;

    private String content;

    private String tags;
    private String linkAppName;
    private String linkAppPackageName;

    private String picUrls;

    private int startTime;
    private int startHour;
    private int startMinute;

    private int endTime;
    private int endHour;
    private int endMinute;

    private long dayTime;

    private int alarmStatus;    //0:未设置；1:已设置；2:已完成

    private String targetName;

    @Transient
    private int tempRepeatmode;

    @Transient
    private Long tempDayTime;

    @Generated(hash = 2119872504)
    public PlanDO(Long id, Long createdTime, Long modifiedTime, int type, String title,
            String content, String tags, String linkAppName, String linkAppPackageName,
            String picUrls, int startTime, int startHour, int startMinute, int endTime,
            int endHour, int endMinute, long dayTime, int alarmStatus, String targetName) {
        this.id = id;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
        this.type = type;
        this.redoPlanId = redoPlanId;
        this.title = title;
        this.content = content;
        this.tags = tags;
        this.linkAppName = linkAppName;
        this.linkAppPackageName = linkAppPackageName;
        this.picUrls = picUrls;
        this.startTime = startTime;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endTime = endTime;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.dayTime = dayTime;
        this.alarmStatus = alarmStatus;
        this.targetName = targetName;
    }

    @Generated(hash = 1345978329)
    public PlanDO() {
    }

    public int getTempRepeatmode() {
        return tempRepeatmode;
    }

    public void setTempRepeatmode(int tempRepeatmode) {
        this.tempRepeatmode = tempRepeatmode;
    }

    public Long getTempDayTime() {
        return tempDayTime;
    }

    public void setTempDayTime(Long tempDayTime) {
        this.tempDayTime = tempDayTime;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
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

    @Keep
    @Override
    public int getItemType() {
        return type;
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

    public int getAlarmStatus() {
        return this.alarmStatus;
    }

    public void setAlarmStatus(int alarmStatus) {
        this.alarmStatus = alarmStatus;
    }

    public String getPicUrls() {
        return this.picUrls;
    }

    public void setPicUrls(String picUrls) {
        this.picUrls = picUrls;
    }

    public String getTargetName() {
        return this.targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public Long getRedoPlanId() {
        return this.redoPlanId;
    }

    public void setRedoPlanId(Long redoPlanId) {
        this.redoPlanId = redoPlanId;
    }
}
