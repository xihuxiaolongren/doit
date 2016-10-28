package me.xihuxiaolong.justdoit.common.database.localentity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/8/8.
 */
@Entity
public class PlanDO {

    public final static int TYPE_PLAN = 0;
    public final static int TYPE_ALERT = 1;
    public final static int TYPE_PHOTO = 2;

    @Id(autoincrement = true)
    private Long id;
    private long createdTime;
    private long modifiedTime;

    private int type;

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

    @Generated(hash = 789130426)
    public PlanDO(Long id, long createdTime, long modifiedTime, int type, String title,
            String content, String tags, String linkAppName, String linkAppPackageName,
            int startTime, int startHour, int startMinute, int endTime, int endHour,
            int endMinute, long dayTime) {
        this.id = id;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
        this.type = type;
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
    }

    @Generated(hash = 1345978329)
    public PlanDO() {
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

}
