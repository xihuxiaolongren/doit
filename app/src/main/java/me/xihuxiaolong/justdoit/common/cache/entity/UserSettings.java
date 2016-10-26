package me.xihuxiaolong.justdoit.common.cache.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/10/19.
 */
public class UserSettings {

    private boolean showAvatar;
    private String avatarUri;
    private String motto;
    private String mottoPlanStart;
    private String mottoPlanEnd;
    private int dayNightTheme;  // 0 : day , 1 : night; 2 : auto;
    private int dayStartHour;
    private int dayStartMinute;
    private int nightStartHour;
    private int nightStartMinute;

    public UserSettings() {
    }

    public void setShowAvatar(boolean showAvatar) {
        this.showAvatar = showAvatar;
    }
    public boolean isShowAvatar() {
        return showAvatar;
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

    public int getDayNightTheme() {
        return dayNightTheme;
    }

    public void setDayNightTheme(int dayNightTheme) {
        this.dayNightTheme = dayNightTheme;
    }

    public int getDayStartHour() {
        return dayStartHour;
    }

    public void setDayStartHour(int dayStartHour) {
        this.dayStartHour = dayStartHour;
    }

    public int getDayStartMinute() {
        return dayStartMinute;
    }

    public void setDayStartMinute(int dayStartMinute) {
        this.dayStartMinute = dayStartMinute;
    }

    public int getNightStartHour() {
        return nightStartHour;
    }

    public void setNightStartHour(int nightStartHour) {
        this.nightStartHour = nightStartHour;
    }

    public int getNightStartMinute() {
        return nightStartMinute;
    }

    public void setNightStartMinute(int nightStartMinute) {
        this.nightStartMinute = nightStartMinute;
    }
}
