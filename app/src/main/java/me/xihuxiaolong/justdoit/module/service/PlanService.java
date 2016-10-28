package me.xihuxiaolong.justdoit.module.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Random;

import me.xihuxiaolong.justdoit.R;
import me.xihuxiaolong.justdoit.module.planlist.PlanListActivity;

public class PlanService extends Service {

    public static final String ALARM_ALERT_ACTION = "me.xihuxiaolong.justdoit.ALARM_ALERT";

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
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.custom_notice);
        remoteViews.setTextViewText(R.id.contentTV, "测试用,测试用,测试用,测试用,测试用");
//        remoteViews.setTextViewText(R.id.time_tv, getTime());
        remoteViews.setImageViewResource(R.id.dayThemeIV, R.drawable.icon_launcher);
        Intent intent = new Intent(this, PlanListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        int requestCode = (int) SystemClock.uptimeMillis();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.dayThemeIV, pendingIntent);
        int requestCode1 = (int) SystemClock.uptimeMillis();
//        Intent intent1 = new Intent(ACTION_CLOSE_NOTICE);
//        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(this, requestCode1, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
//        remoteViews.setOnClickPendingIntent(R.id.widget_title, pendingIntent1);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("some string")
                .setContentText("Slide down on note to expand")
                .setCustomBigContentView(remoteViews)
                .setSmallIcon(R.drawable.icon_launcher).build();
        NotificationManager notifyMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = new Random().nextInt();
// Builds the notification and issues it.
        notifyMgr.notify(notificationId, notification);
    }
}