package me.xihuxiaolong.justdoit.module.planlist;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.xihuxiaolong.justdoit.R;
import me.xihuxiaolong.justdoit.common.base.BaseActivity;
import me.xihuxiaolong.library.utils.ActivityUtil;

public class PlanListActivity extends BaseActivity {

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
            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),
                    planListFragment, R.id.contentFrame);
        }

    }

}
