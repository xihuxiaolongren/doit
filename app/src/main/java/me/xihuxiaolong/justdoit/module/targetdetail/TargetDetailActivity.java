package me.xihuxiaolong.justdoit.module.targetdetail;

import android.content.ServiceConnection;
import android.os.Bundle;

import butterknife.ButterKnife;
import me.xihuxiaolong.justdoit.R;
import me.xihuxiaolong.justdoit.common.base.BaseActivity;
import me.xihuxiaolong.justdoit.module.planlist.PlanListFragment;
import me.xihuxiaolong.justdoit.module.service.PlanService;
import me.xihuxiaolong.library.utils.ActivityUtils;

public class TargetDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_day_plan_list);
            ButterKnife.bind(this);

        TargetDetailFragment targetDetailFragment =
                (TargetDetailFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if(targetDetailFragment == null) {
            targetDetailFragment = TargetDetailFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    targetDetailFragment, R.id.contentFrame);
        }
    }

}
