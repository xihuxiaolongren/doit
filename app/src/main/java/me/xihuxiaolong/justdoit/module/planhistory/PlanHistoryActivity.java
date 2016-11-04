package me.xihuxiaolong.justdoit.module.planhistory;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.xihuxiaolong.justdoit.R;
import me.xihuxiaolong.justdoit.common.base.BaseMvpActivity;
import me.xihuxiaolong.justdoit.common.util.ProjectActivityUtils;

public class PlanHistoryActivity extends BaseMvpActivity<PlanHistoryActivityContract.IView, PlanHistoryActivityContract.IPresenter> implements PlanHistoryActivityContract.IView, ViewPager.OnPageChangeListener {

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

    private int vibrant, darkVibrant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        injectDependencies();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_history);
        ButterKnife.bind(this);
        initWindow();

        setToolbar(toolbar, true);

        viewpager.addOnPageChangeListener(this);
        presenter.loadHistorys();
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

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @TargetApi(19)
    private void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            // enable status bar tint
            tintManager.setStatusBarTintEnabled(true);
            // enable navigation bar tint
//        tintManager.setNavigationBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.colorStatus);
        }
    }

    @Override
    public void showHistorys(List<DateTime> dayTimes) {
        FragmentPagerItems.Creator creator = FragmentPagerItems.with(this);
        for(int i = 0; i < dayTimes.size(); ++i){
            Bundle bundle = new Bundle();
            bundle.putLong("dayTime", dayTimes.get(i).getMillis());
            creator.add("", PlanHistoryFragment.class, bundle);
        }
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), creator.create());
        viewpager.setAdapter(adapter);
    }

    @Override
    public void showDayInfo(DateTime dateTime) {
        calendarDayTv.setText(dateTime.toString(DateTimeFormat.forPattern("d")));
        calendarWeekTv.setText(dateTime.toString(DateTimeFormat.forPattern("EEEE")));
        calendarMonthYearTv.setText(dateTime.toString(DateTimeFormat.forPattern("MMMM yyyy")));
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
}
