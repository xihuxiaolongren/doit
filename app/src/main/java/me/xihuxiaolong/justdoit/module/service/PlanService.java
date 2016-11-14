package me.xihuxiaolong.justdoit.module.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import me.xihuxiaolong.justdoit.R;
import me.xihuxiaolong.justdoit.common.util.DayNightModeUtils;
import me.xihuxiaolong.justdoit.module.main.MainActivity;

public class PlanService extends Service {

    public static final String ALARM_ALERT_ACTION = "me.xihuxiaolong.justdoit.ALARM_ALERT";
    public static final int CLOSE_NOTIFY = 1;

    public class LocalBinder extends Binder {
        public String stringToSend = "I'm the test String";

        public PlanService getService() {
            Log.i("TAG", "getService ---> " + PlanService.this);
            return PlanService.this;
        }
    }

    private final IBinder mBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        Log.i("TAG", "onBind~~~~~~~~~~~~");
        return mBinder;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        Log.i("TAG", "onCreate~~~~~~~~~~");
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.i("TAG", "onDestroy~~~~~~~~~~~");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        Log.i("TAG", "onStartCommand~~~~~~~~~~~~");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // TODO Auto-generated method stub
        Log.i("TAG", "onUnbind~~~~~~~~~~~~~~~~");
        return super.onUnbind(intent);
    }

    public void sendNotification(){
        //当前版本remoteView 无法自动识别到夜间模式的资源目录
        RemoteViews remoteViewBig;
        RemoteViews remoteViewNormal;
        if(DayNightModeUtils.isCurrentNight()) {
            remoteViewBig = new RemoteViews(getPackageName(), R.layout.notice_night_big_plan);
            remoteViewNormal = new RemoteViews(getPackageName(), R.layout.notice_night_normal_plan);
        }else {
            remoteViewBig = new RemoteViews(getPackageName(), R.layout.notice_day_big_plan);
            remoteViewNormal = new RemoteViews(getPackageName(), R.layout.notice_day_normal_plan);
        }
        remoteViewNormal.setTextViewText(R.id.contentTV, "1、需求v1.1评审；\n2、v1.0 bug处理；");

        remoteViewBig.setTextViewText(R.id.contentTV, "1、需求v1.1评审；\n2、v1.0 bug处理；");
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        int requestCode = (int) SystemClock.uptimeMillis();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViewBig.setOnClickPendingIntent(R.id.contentTV, pendingIntent);

        Intent closeIntent = new Intent(getApplicationContext(), PlanService.class);
        closeIntent.putExtra("1", CLOSE_NOTIFY);
        PendingIntent pendingCloseIntent = PendingIntent.getService(this, 1, closeIntent, 0);
        remoteViewBig.setOnClickPendingIntent(R.id.deleteIV, pendingCloseIntent);
        Notification notification = new NotificationCompat.Builder(this)
                .setCustomBigContentView(remoteViewBig)
                .setContent(remoteViewNormal)
//                .setLargeIcon(ContextCompat.getDrawable(this, R.drawable.icon_launcher))
                .setSmallIcon(R.drawable.icon_small_notify).build();
        NotificationManager notifyMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //第一个状态保证在top位置,第二个状态保证常驻
        notification.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;
        //        int notificationId = new Random().nextInt();
        int notificationId = 0; //id相同的notification只会保留一个,并且会更新该通知栏
        notifyMgr.notify(notificationId, notification);
    }
}