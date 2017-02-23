package me.xihuxiaolong.justdoit.module.main;

public interface MainActivityListener {

    void onScrollChanged(int scrollY, int flag);

    void setBottomBarBadge(int position, int badgeCount);
}