package me.xihuxiaolong.justdoit.module.planlist;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import butterknife.ButterKnife;
import me.xihuxiaolong.justdoit.R;
import me.xihuxiaolong.justdoit.common.base.BaseActivity;
import me.xihuxiaolong.justdoit.module.service.PlanService;
import me.xihuxiaolong.library.utils.ActivityUtils;

public class OtherDayActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        dayTime = getIntent().getLongExtra("dayTime", -1L);
        setContentView(R.layout.activity_other_day_plan_list);
            ButterKnife.bind(this);
//        setToolbar(toolbar, false);
//        if(dayTime != -1L)
//            dayNightBackgroundView.setAnimationDuration(0);

        PlanListFragment planListFragment =
                (PlanListFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if(planListFragment == null) {
            planListFragment = PlanListFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    planListFragment, R.id.contentFrame);
        }
    }

}
