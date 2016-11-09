package me.xihuxiaolong.justdoit.common.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.xihuxiaolong.library.utils.ToastUtil;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/10/26.
 */

public class ThirdAppUtils {

    /**
     * 查询手机内非系统应用
     * @param context
     * @return
     */
    public static List<AppItem> getAllApps(Context context) {
        List<AppItem> apps = new ArrayList<>();
        PackageManager pManager = context.getPackageManager();
        //获取手机内所有应用
        List<PackageInfo> paklist = pManager.getInstalledPackages(0);
        for (int i = 0; i < paklist.size(); i++) {
            PackageInfo pak = paklist.get(i);
            //判断是否为非系统预装的应用程序
            if ((pak.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                // customs applications
                apps.add(new AppItem(pManager.getApplicationIcon(pak.applicationInfo),
                        pManager.getApplicationLabel(pak.applicationInfo).toString(), pak.applicationInfo.packageName));
            }
        }
        return apps;
    }

    public static Drawable getIcon(Context context, String appPackageName) {
        try {
            ApplicationInfo app = context.getPackageManager().getApplicationInfo(appPackageName, 0);
            return context.getPackageManager().getApplicationIcon(app);
        } catch (PackageManager.NameNotFoundException e) {
            ToastUtil.showToast(context, "应用已删除", Toast.LENGTH_SHORT);
        }
        return null;
    }

    public static boolean launchapp(Context context, String appPackageName) {
        // 判断是否安装过App，否则去市场下载
        if (isAppInstalled(context, appPackageName)) {
            context.startActivity(context.getPackageManager().getLaunchIntentForPackage(appPackageName));
            return true;
        } else {
            //            goToMarket(context, appPackageName);
            ToastUtil.showToast(context, "应用已删除", Toast.LENGTH_SHORT);
            return false;
        }
    }

    /**
     * 检测某个应用是否安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static class AppItem{
        private Drawable appIcon;
        private String appName;
        private String appPackageName;

        public AppItem(Drawable appIcon, String appName, String appPackageName) {
            this.appIcon = appIcon;
            this.appName = appName;
            this.appPackageName = appPackageName;
        }

        public Drawable getAppIcon() {
            return appIcon;
        }

        public void setAppIcon(Drawable appIcon) {
            this.appIcon = appIcon;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getAppPackageName() {
            return appPackageName;
        }

        public void setAppPackageName(String appPackageName) {
            this.appPackageName = appPackageName;
        }
    }
}
