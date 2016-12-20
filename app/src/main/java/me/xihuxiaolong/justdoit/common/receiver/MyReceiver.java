package me.xihuxiaolong.justdoit.common.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/12/13.
 */

public class MyReceiver extends BroadcastReceiver {
    private boolean screenOff;

    //TODO 依靠监听广播事件同样无效。。。 考虑用alarmManager再尝试下，看是否能实现像qq音乐这样几乎永远不死的前台通知
    @Deprecated
    @Override
    public void onReceive(Context context, Intent intent) {
//        if (Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {
//            Logger.e("TAG 手机被唤醒了.....userPresent");
//            Intent service = new Intent();
//            service.setClass(context, PlanService.class);
//            context.startService(service);
//        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
//            screenOff = true;
//            Logger.e("TAG screenOff~~~~~~~~~~~~");
//        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
//            screenOff = false;
//            Logger.e("TAG screenOn~~~~~~~~~~~~");
//            Intent i = new Intent(context, PlanService.class);
//            i.putExtra("screen_state", screenOff);
//            context.startService(i);
//        }
    }
}
