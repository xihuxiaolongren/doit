package me.xihuxiaolong.justdoit.module.adddayplan;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.xihuxiaolong.justdoit.R;
import me.xihuxiaolong.justdoit.common.base.BaseMvpActivity;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
import me.xihuxiaolong.justdoit.common.util.ActivityUtils;
import me.xihuxiaolong.justdoit.common.widget.DayNightBackgroundView;
import me.xihuxiaolong.justdoit.module.adapter.PlanListWrapper;
import me.xihuxiaolong.justdoit.module.editalert.EditAlertActivity;
import me.xihuxiaolong.justdoit.module.editplan.EditPlanActivity;

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
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.emptyView)
    LinearLayout emptyView;

    PlanListWrapper planListWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dayTime = getIntent().getLongExtra("dayTime", -1L);
        injectDependencies();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_day_plan);
        ButterKnife.bind(this);
        setToolbar(toolbar, true);

        planListWrapper = new PlanListWrapper(this, recyclerView);

        presenter.loadDayInfo();
        presenter.loadData();
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
        recyclerView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        planListWrapper.setItems(planDOs);
    }

    @Override
    public void showEmptyView() {
        recyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
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

    @OnClick(R.id.use_today_template)
    void useTodayTemplate(View v) {
        presenter.createDayPlan(true);
    }

    @OnClick(R.id.empty_template)
    void emptyTemplate(View v) {
        presenter.createDayPlan(false);
    }
}
