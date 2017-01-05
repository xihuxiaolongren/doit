package me.xihuxiaolong.justdoit.common.service;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.joda.time.DateTime;

import java.util.List;

import me.xihuxiaolong.justdoit.R;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
import me.xihuxiaolong.justdoit.common.database.manager.PlanDataSource;
import me.xihuxiaolong.justdoit.common.event.Event;
import me.xihuxiaolong.justdoit.common.util.DayNightModeUtils;
import me.xihuxiaolong.justdoit.module.main.MainActivity;

public class PlanService extends Service {

    public static final int CLOSE_NOTIFY = 10;

    public class LocalBinder extends Binder {
        public String stringToSend = "I'm the test String";

        public PlanService getService() {
            Logger.i("TAG getService ---> " + PlanService.this);
            return PlanService.this;
        }
    }

    private final IBinder mBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        Logger.i("TAG onBind~~~~~~~~~~~~");
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        Logger.e("TAG onCreate~~~~~~~~~~");
    }

    @Override
    public void onDestroy() {
        Logger.e("TAG onDestroy~~~~~~~~~~~");
        EventBus.getDefault().unregister(this);
        stopForeground(true);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.e("TAG onStartCommand~~~~~~~~~~~~");
//        data=(String) intent.getSerializableExtra();
        setAlarmForPlan();
        if (intent != null && intent.getBooleanExtra("alarm", false) && intent.getSerializableExtra("planDO") != null)
            processAlarmOpen((PlanDO) intent.getSerializableExtra("planDO"));
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Logger.e("TAG onUnbind~~~~~~~~~~~~~~~~");
        return super.onUnbind(intent);
    }

    //设置闹钟&通知栏
    private void setAlarmForPlan() {
        PlanDataSource planDataSource = new PlanDataSource();
        List<PlanDO> list = planDataSource.listPlanDOsByOneDay(DateTime.now().withTimeAtStartOfDay().getMillis());
        for (PlanDO planDO : list) {
            int cMinute = DateTime.now().minuteOfDay().get();
            if (planDO.getStartTime() < cMinute && planDO.getEndTime() > cMinute)
                sendNotification(planDO, false);
            if (planDO.getStartTime() > cMinute) {
                if (planDO.getAlarmStatus() == 1)
                    break;
                else if (planDO.getAlarmStatus() == 0) {
                    setAlarm(DateTime.now().withTimeAtStartOfDay().plusMinutes(planDO.getStartTime()), planDO);
                    break;
                }
            }
        }
    }

    private void setAlarm(DateTime dateTime, PlanDO planDO) {
        //Create a new PendingIntent and add it to the AlarmManager
        planDO.setAlarmStatus(1);
        PlanDataSource planDataSource = new PlanDataSource();
        planDataSource.insertOrReplacePlanDO(planDO);
        Intent intent = new Intent(getApplicationContext(), PlanService.class);
        intent.putExtra("planDO", planDO);
        intent.putExtra("alarm", true);
        PendingIntent pendingIntent = PendingIntent.getService(this,
                12345, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am =
                (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, dateTime.getMillis(),
                pendingIntent);
    }

    //闹钟响起
    private void processAlarmOpen(PlanDO planDO) {
        PlanDataSource planDataSource = new PlanDataSource();
        planDO.setAlarmStatus(2);
        planDataSource.insertOrReplacePlanDO(planDO);
        Logger.e("TAG sendNotification");
        sendNotification(planDO, true);
    }

    private void sendNotification(PlanDO planDO, boolean vibr) {
        //当前版本remoteView 无法自动识别到夜间模式的资源目录
        RemoteViews remoteViewBig;
        RemoteViews remoteViewNormal;
        if (DayNightModeUtils.isCurrentNight()) {
            remoteViewBig = new RemoteViews(getPackageName(), R.layout.notice_night_big_plan);
            remoteViewNormal = new RemoteViews(getPackageName(), R.layout.notice_night_normal_plan);
        } else {
            remoteViewBig = new RemoteViews(getPackageName(), R.layout.notice_day_big_plan);
            remoteViewNormal = new RemoteViews(getPackageName(), R.layout.notice_day_normal_plan);
        }
        remoteViewNormal.setTextViewText(R.id.contentTV, planDO.getContent());

        remoteViewBig.setTextViewText(R.id.contentTV, planDO.getContent());
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
                .setPriority(2)
//                .setLargeIcon(ContextCompat.getDrawable(this, R.drawable.icon_launcher))
                .setSmallIcon(R.drawable.icon_small_notify).build();
        if(vibr) {
            notification.defaults |= Notification.DEFAULT_VIBRATE;
            notification.defaults |= Notification.DEFAULT_SOUND;
        }
        //        NotificationManager notifyMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //第一个状态保证在top位置,第二个状态保证常驻
        notification.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR | Notification.FLAG_FOREGROUND_SERVICE;
        //        int notificationId = new Random().nextInt();
//        notifyMgr.notify(notificationId, notification);
        startForeground(110, notification);// 开始前台服务
    }

    @Subscribe
    public void onEvent(Event.AddPlan addPlanEvent) {
        if(DateTime.now().withTimeAtStartOfDay().getMillis() == addPlanEvent.plan.getDayTime()){
            setAlarmForPlan();
        }
    }

    @Subscribe
    public void onEvent(Event.UpdatePlan updatePlanEvent) {
        if(DateTime.now().withTimeAtStartOfDay().getMillis() == updatePlanEvent.plan.getDayTime())
            setAlarmForPlan();
    }

    @Subscribe
    public void onEvent(Event.DeletePlan deletePlanEvent) {
        if(DateTime.now().withTimeAtStartOfDay().getMillis() == deletePlanEvent.plan.getDayTime())
            setAlarmForPlan();
    }

}