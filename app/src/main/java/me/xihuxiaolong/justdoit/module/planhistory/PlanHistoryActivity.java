package me.xihuxiaolong.justdoit.module.planhistory;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.xihuxiaolong.justdoit.R;
import me.xihuxiaolong.justdoit.common.base.BaseMvpActivity;
import me.xihuxiaolong.justdoit.common.util.DayNightModeUtils;
import me.xihuxiaolong.justdoit.common.util.ProjectActivityUtils;

public class PlanHistoryActivity extends BaseMvpActivity<PlanHistoryActivityContract.IView, PlanHistoryActivityContract.IPresenter> implements PlanHistoryActivityContract.IView, ViewPager.OnPageChangeListener, CalendarDatePickerDialogFragment.OnDateSetListener {

    private static final String FRAG_TAG_DATE_PICKER = "FRAG_TAG_DATE_PICKER";

    PlanHistoryActivityComponent planHistoryComponent;

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.calendar_day_tv)
    TextView calendarDayTv;
    @BindView(R.id.calendar_week_tv)
    TextView calendarWeekTv;
    @BindView(R.id.calendar_month_year_tv)
    TextView calendarMonthYearTv;

    FragmentPagerItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        injectDependencies();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_history);
        ButterKnife.bind(this);
        setToolbar(toolbar, true);

        viewpager.addOnPageChangeListener(this);
        presenter.loadHistorys();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_frament_plan_history, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public PlanHistoryActivityContract.IPresenter createPresenter() {
        return planHistoryComponent.presenter();
    }

    protected void injectDependencies() {
        planHistoryComponent = DaggerPlanHistoryActivityComponent.builder()
                .appComponent(ProjectActivityUtils.getAppComponent(this))
                .build();
    }

    @OnClick(R.id.calendar_rl)
    void calendarClick(View v){
        presenter.loadStartAndEnd();
    }

    @Override
    public void showHistorys(List<Long> dayTimes) {
        FragmentPagerItems.Creator creator = FragmentPagerItems.with(this);
        for(int i = 0; i < dayTimes.size(); ++i){
            Bundle bundle = new Bundle();
            bundle.putLong("dayTime", dayTimes.get(i));
            creator.add("", PlanHistoryFragment.class, bundle);
        }
        adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), creator.create());
        viewpager.setAdapter(adapter);
        presenter.loadCurrentDayTime(0);
    }

    DateTime currentDateTime;

    @Override
    public void showDayInfo(Long dateTime) {
        currentDateTime = new DateTime(dateTime);
        calendarDayTv.setText(currentDateTime.toString(DateTimeFormat.forPattern("d")));
        calendarWeekTv.setText(currentDateTime.toString(DateTimeFormat.forPattern("EEEE")));
        calendarMonthYearTv.setText(currentDateTime.toString(DateTimeFormat.forPattern("MMMM yyyy")));
    }

    @Override
    public void showDay(int position) {
        if(viewpager != null){
            viewpager.setCurrentItem(position, false);
        }
    }

    @Override
    public void showCalendar(Long start, Long end) {
        DateTime startDateTime = new DateTime(start);
        DateTime endDateTime = new DateTime(end);
        MonthAdapter.CalendarDay minDate = new MonthAdapter.CalendarDay(startDateTime.getYear(), startDateTime.getMonthOfYear() - 1, startDateTime.getDayOfMonth());
        MonthAdapter.CalendarDay maxDate = new MonthAdapter.CalendarDay(endDateTime.getYear(), endDateTime.getMonthOfYear() - 1, endDateTime.getDayOfMonth());
        CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                .setOnDateSetListener(this)
                .setDoneText(getResources().getString(R.string.action_confirm))
                .setCancelText(getResources().getString(R.string.action_cancel))
                .setPreselectedDate(currentDateTime.getYear(), currentDateTime.getMonthOfYear() - 1, currentDateTime.getDayOfMonth())
                .setDateRange(minDate, maxDate);
        if (DayNightModeUtils.isCurrentNight())
            cdp.setThemeDark();
        else
            cdp.setThemeLight();
        cdp.show(getSupportFragmentManager(), FRAG_TAG_DATE_PICKER);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        presenter.loadCurrentDayTime(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        DateTime dateTime = new DateTime(year, monthOfYear + 1, dayOfMonth, 0, 0);
        presenter.loadDay(dateTime);
    }
}
