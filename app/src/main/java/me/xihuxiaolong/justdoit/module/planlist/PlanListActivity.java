package me.xihuxiaolong.justdoit.module.planlist;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.xihuxiaolong.justdoit.R;
import me.xihuxiaolong.justdoit.common.base.BaseActivity;
import me.xihuxiaolong.justdoit.module.service.PlanService;
import me.xihuxiaolong.library.utils.ActivityUtils;

public class PlanListActivity extends BaseActivity {

    private ServiceConnection sc;
    private PlanService planService;

    @BindView(R.id.contentFrame)
    FrameLayout contentFrame;
    @BindView(R.id.headerIV)
    ImageView headerIV;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.calendar_day_tv)
    TextView calendarDayTv;
    @BindView(R.id.calendar_week_tv)
    TextView calendarWeekTv;
    @BindView(R.id.calendar_month_year_tv)
    TextView calendarMonthYearTv;
    @BindView(R.id.recycler_background)
    View recyclerBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_list);
        ButterKnife.bind(this);
        setToolbar(toolbar, false);

        PlanListFragment planListFragment =
                (PlanListFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if(planListFragment == null) {
            planListFragment = PlanListFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    planListFragment, R.id.contentFrame);
        }

        sc = new ServiceConnection() {
            /*
             * 只有在MyService中的onBind方法中返回一个IBinder实例才会在Bind的时候
             * 调用onServiceConnection回调方法
             * 第二个参数service就是MyService中onBind方法return的那个IBinder实例，可以利用这个来传递数据
             */
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                // TODO Auto-generated method stub
                planService = ((PlanService.LocalBinder) service).getService();
                planService.sendNotification();
                String recStr = ((PlanService.LocalBinder) service).stringToSend;
                //利用IBinder对象传递过来的字符串数据（其他数据也可以啦，哪怕是一个对象也OK~~）
                Log.i("TAG","The String is : " + recStr);
                Log.i("TAG", "onServiceConnected : myService ---> " + planService);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                /* SDK上是这么说的：
                 * This is called when the connection with the service has been unexpectedly disconnected
                 * that is, its process crashed. Because it is running in our same process, we should never see this happen.
                 * 所以说，只有在service因异常而断开连接的时候，这个方法才会用到*/
                // TODO Auto-generated method stub
                sc = null;
                Log.i("TAG", "onServiceDisconnected : ServiceConnection --->"
                        + sc);
            }

        };

        Intent intent = new Intent(PlanListActivity.this,
                PlanService.class);
        startService(intent);

        bindService(intent, sc, Context.BIND_AUTO_CREATE);
    }

}
