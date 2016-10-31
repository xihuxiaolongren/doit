package me.xihuxiaolong.justdoit.module.adddayplan;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.crosswall.lib.coverflow.CoverFlow;
import me.crosswall.lib.coverflow.core.PagerContainer;
import me.xihuxiaolong.justdoit.R;
import me.xihuxiaolong.justdoit.common.base.BaseMvpActivity;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
import me.xihuxiaolong.justdoit.common.util.ActivityUtils;
import me.xihuxiaolong.justdoit.module.editalert.EditAlertActivity;
import me.xihuxiaolong.justdoit.module.editplan.EditPlanActivity;
import me.xihuxiaolong.justdoit.module.planhistory.PlanHistoryFragment;

public class AddDayPlanActivity extends BaseMvpActivity<AddDayPlanActivityContract.IView, AddDayPlanActivityContract.IPresenter> implements AddDayPlanActivityContract.IView {

    AddDayPlanActivityComponent addDayPlanActivityComponent;

    long dayTime;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.calendar_day_tv)
    TextView calendarDayTv;
    @BindView(R.id.calendar_week_tv)
    TextView calendarWeekTv;
    @BindView(R.id.calendar_month_year_tv)
    TextView calendarMonthYearTv;
    @BindView(R.id.backgroundIV)
    ImageView backgroundIV;
    //    @BindView(R.id.pager_container)
//    PagerContainer pagerContainer;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dayTime = getIntent().getLongExtra("dayTime", -1L);
        injectDependencies();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_day_plan);
        ButterKnife.bind(this);
        setToolbar(toolbar, true);
        viewPager.setPageMargin(20);
        viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                final float normalizedposition = Math.abs(Math.abs(position) - 1);
//                page.setScaleX(normalizedposition / 1.1f);
//                page.setScaleY(normalizedposition / 1.1f);
            }
        });
//        viewPager.setClipToPadding(false);
//        viewPager.setPadding(100,0,100,0);
        presenter.loadTemplateList();
    }

    @NonNull
    @Override
    public AddDayPlanActivityContract.IPresenter createPresenter() {
        return addDayPlanActivityComponent.presenter();
    }

    @Override
    protected void injectDependencies() {
        addDayPlanActivityComponent = DaggerAddDayPlanActivityComponent.builder().appComponent(ActivityUtils.getAppComponent(this))
                .addDayPlanActivityModule(new AddDayPlanActivityModule(dayTime)).build();
    }

    @Override
    public void showDayInfo(DateTime dateTime) {
        if (dateTime.isEqual(DateTime.now().withTimeAtStartOfDay())) {
            getSupportActionBar().setTitle("今日计划");
        } else {
            getSupportActionBar().setTitle("明日计划");
        }
        calendarDayTv.setText(dateTime.toString(DateTimeFormat.forPattern("d")));
        calendarWeekTv.setText(dateTime.toString(DateTimeFormat.forPattern("EEEE")));
        calendarMonthYearTv.setText(dateTime.toString(DateTimeFormat.forPattern("MMMM yyyy")));
    }

    @Override
    public void showContent(List<PlanDO> planDOs) {
    }

    @Override
    public void showTemplateList(List<String> planDOs) {
        viewPager.setOffscreenPageLimit(planDOs.size());
        FragmentPagerItems.Creator creator = FragmentPagerItems.with(this);
        for (int i = 0; i < planDOs.size(); ++i) {
            Bundle bundle = new Bundle();
            bundle.putLong("dayTime", System.currentTimeMillis());
            creator.add("", PlanTemplateFragment.class, bundle);
        }
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), creator.create());
        viewPager.setAdapter(adapter);
    }

    @Override
    public void showEmptyView() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_add_day_plan, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_alert:
                startActivity(new Intent(this, EditAlertActivity.class).putExtra(EditPlanActivity.ARGUMENT_DAY_TIME, dayTime));
                return true;
            case R.id.action_add_plan:
                startActivity(new Intent(this, EditPlanActivity.class).putExtra(EditPlanActivity.ARGUMENT_DAY_TIME, dayTime));
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
