package me.xihuxiaolong.justdoit.module.planlist;

import android.os.Bundle;

import butterknife.ButterKnife;
import me.xihuxiaolong.justdoit.R;
import me.xihuxiaolong.justdoit.common.base.BaseActivity;
import me.xihuxiaolong.library.utils.ActivityUtils;

public class OtherDayActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        dayTime = getIntent().getLongExtra("dayTime", -1L);
        setContentView(R.layout.activity_single_fragment);
            ButterKnife.bind(this);
//        setToolbar(toolbar, false);
//        if(dayTime != -1L)
//            dayNightBackgroundView.setAnimationDuration(0);

        OtherDayPlanListFragment otherDayPlanListFragment =
                (OtherDayPlanListFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if(otherDayPlanListFragment == null) {
            otherDayPlanListFragment = OtherDayPlanListFragment.newInstance(getIntent().getLongExtra("dayTime", -1L));
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    otherDayPlanListFragment, R.id.contentFrame);
        }
    }

}
