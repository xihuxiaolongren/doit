package me.xihuxiaolong.justdoit.module.otherdayplanlist;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.xihuxiaolong.justdoit.R;
import me.xihuxiaolong.justdoit.common.base.BaseActivity;
import me.xihuxiaolong.library.utils.ActivityUtil;

public class OtherDayPlanListActivity extends BaseActivity {

    @BindView(R.id.contentFrame)
    FrameLayout contentFrame;
    @BindView(R.id.headerIV)
    ImageView headerIV;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_day_plan_list);
        ButterKnife.bind(this);
        setToolbar(toolbar, false);

        OtherDayPlanListFragment otherDayPlanListFragment =
                (OtherDayPlanListFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if(otherDayPlanListFragment == null) {
            otherDayPlanListFragment = OtherDayPlanListFragment.newInstance(-1L);
            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),
                    otherDayPlanListFragment, R.id.contentFrame);
        }

    }

}
