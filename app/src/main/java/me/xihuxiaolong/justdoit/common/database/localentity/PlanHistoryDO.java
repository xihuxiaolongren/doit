package me.xihuxiaolong.justdoit.common.database.localentity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/10/12.
 */
@Entity
public class PlanHistoryDO implements Serializable{

    static final long serialVersionUID = -1L;

    @Id(autoincrement = true)
    private Long id;
    private Long dayTime;
    private int planCount;
    private int alertCount;
    private int photoCount;
    private int punchCount;
    @Generated(hash = 1255143889)
    public PlanHistoryDO(Long id, Long dayTime, int planCount, int alertCount,
            int photoCount, int punchCount) {
        this.id = id;
        this.dayTime = dayTime;
        this.planCount = planCount;
        this.alertCount = alertCount;
        this.photoCount = photoCount;
        this.punchCount = punchCount;
    }
    @Generated(hash = 1603826141)
    public PlanHistoryDO() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public long getDayTime() {
        return this.dayTime;
    }
    public void setDayTime(long dayTime) {
        this.dayTime = dayTime;
    }
    public int getPlanCount() {
        return this.planCount;
    }
    public void setPlanCount(int planCount) {
        this.planCount = planCount;
    }
    public int getAlertCount() {
        return this.alertCount;
    }
    public void setAlertCount(int alertCount) {
        this.alertCount = alertCount;
    }
    public int getPhotoCount() {
        return this.photoCount;
    }
    public void setPhotoCount(int photoCount) {
        this.photoCount = photoCount;
    }
    public int getPunchCount() {
        return this.punchCount;
    }
    public void setPunchCount(int punchCount) {
        this.punchCount = punchCount;
    }
    public void setDayTime(Long dayTime) {
        this.dayTime = dayTime;
    }
    

}
