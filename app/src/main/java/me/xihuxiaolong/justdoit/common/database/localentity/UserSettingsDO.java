package me.xihuxiaolong.justdoit.common.database.localentity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/10/19.
 */
@Entity
public class UserSettingsDO {

    @Id
    private Long userId;
    private long createdTime;
    private long modifiedTime;
    private boolean showAvatar;
    private String avatarUri;
    private String motto;
    private String mottoPlanStart;
    private String mottoPlanEnd;
    @Generated(hash = 2047570528)
    public UserSettingsDO(Long userId, long createdTime, long modifiedTime,
            boolean showAvatar, String avatarUri, String motto,
            String mottoPlanStart, String mottoPlanEnd) {
        this.userId = userId;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
        this.showAvatar = showAvatar;
        this.avatarUri = avatarUri;
        this.motto = motto;
        this.mottoPlanStart = mottoPlanStart;
        this.mottoPlanEnd = mottoPlanEnd;
    }
    @Generated(hash = 1023699730)
    public UserSettingsDO() {
    }
    public Long getUserId() {
        return this.userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
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
    public boolean getShowAvatar() {
        return this.showAvatar;
    }
    public void setShowAvatar(boolean showAvatar) {
        this.showAvatar = showAvatar;
    }
    public String getAvatarUri() {
        return this.avatarUri;
    }
    public void setAvatarUri(String avatarUri) {
        this.avatarUri = avatarUri;
    }
    public String getMotto() {
        return this.motto;
    }
    public void setMotto(String motto) {
        this.motto = motto;
    }
    public String getMottoPlanStart() {
        return this.mottoPlanStart;
    }
    public void setMottoPlanStart(String mottoPlanStart) {
        this.mottoPlanStart = mottoPlanStart;
    }
    public String getMottoPlanEnd() {
        return this.mottoPlanEnd;
    }
    public void setMottoPlanEnd(String mottoPlanEnd) {
        this.mottoPlanEnd = mottoPlanEnd;
    }

}
