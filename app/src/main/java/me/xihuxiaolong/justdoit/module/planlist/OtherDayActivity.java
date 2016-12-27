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

        PlanListFragment planListFragment =
                (PlanListFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if(planListFragment == null) {
            planListFragment = PlanListFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    planListFragment, R.id.contentFrame);
        }
    }

}
