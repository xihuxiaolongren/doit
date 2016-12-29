package me.xihuxiaolong.justdoit.module.main;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.xihuxiaolong.justdoit.R;
import me.xihuxiaolong.justdoit.common.base.BaseActivity;
import me.xihuxiaolong.justdoit.common.event.Event;
import me.xihuxiaolong.justdoit.common.service.PlanService;
import me.xihuxiaolong.justdoit.common.widget.DayNightBackgroundView;
import me.xihuxiaolong.justdoit.module.planlist.PlanListFragment;
import me.xihuxiaolong.justdoit.module.settings.SettingsFragment;
import me.xihuxiaolong.justdoit.module.targetlist.TargetListFragment;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity implements ScrollListener {

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.day_night_background_view)
    DayNightBackgroundView dayNightBackgroundView;
    @BindView(R.id.bottomBar)
    BottomBar bottomBar;

    MainFragmentPageAdapter mainFragmentPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        if (savedInstanceState != null) {
            dayNightBackgroundView.setAnimationDuration(0);
        }
        mainFragmentPageAdapter = new MainFragmentPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mainFragmentPageAdapter);
        viewPager.setOffscreenPageLimit(3);
        bottomBar.setOnTabSelectListener(
                new OnTabSelectListener() {
                    @Override
                    public void onTabSelected(@IdRes int tabId) {
                        switch (tabId) {
                            case R.id.item1:
                                viewPager.setCurrentItem(0, false);
                                invalidateFragmentMenus(0);
                                break;
                            case R.id.item2:
                                viewPager.setCurrentItem(1, false);
                                invalidateFragmentMenus(1);
                                break;
                            case R.id.item3:
                                viewPager.setCurrentItem(2, false);
                                invalidateFragmentMenus(2);
                                break;
                        }
                    }
                }
        );
        if (getIntent().getBooleanExtra("restart", false)) {
            viewPager.setCurrentItem(2);
            bottomBar.setDefaultTabPosition(2);
            dayNightBackgroundView.setAnimationDuration(0);
            bottomBar.post(new Runnable() {
                @Override
                public void run() {
                    showBottom(0);
                }
            });
        } else {
            bottomBar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showBottom(600);
                }
            }, 3500);
        }
        viewPager.post(new Runnable() {
            @Override
            public void run() {
                invalidateFragmentMenus(0);
            }
        });

        Intent intent = new Intent(MainActivity.this,
                PlanService.class);
        startService(intent);
    }

    void invalidateFragmentMenus(int position) {
        Logger.d("position%d", position);
        for (int i = 0; i < mainFragmentPageAdapter.getCount(); i++) {
            if (mainFragmentPageAdapter.getRegisteredFragment(i) != null) {
                if (i == position)
                    ((MainActivityListener) mainFragmentPageAdapter.getRegisteredFragment(i)).reloadToolbar();
            }
        }
    }

    int mScrollY;
    boolean isBottomVisible = true;

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        if (scrollY - mScrollY > 10 && isBottomVisible) {
            hideBottom(500);
        } else if (scrollY - mScrollY < -10 && !isBottomVisible) {
            showBottom(500);
        }
        mScrollY = scrollY;
    }

    private void hideBottom(int duration) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(bottomBar, "translationY", 0);
        animator.setDuration(duration);
        animator.start();
        isBottomVisible = false;
    }

    private void showBottom(int duration) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(bottomBar, "translationY", -bottomBar.getHeight());
        animator.setDuration(duration);
        animator.start();
        isBottomVisible = true;
    }

    @Subscribe
    public void onEvent(Event.ChangeDayNightTheme changeDayNightTheme) {
        restart();
    }

    public class MainFragmentPageAdapter extends FragmentPagerAdapter {
        SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

        public MainFragmentPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return bottomBar.getTabCount();
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    PlanListFragment planListFragment = PlanListFragment.newInstance();
                    return planListFragment;
                case 1:
                    TargetListFragment targetListFragment = TargetListFragment.newInstance();
                    return targetListFragment;
                case 2:
                    SettingsFragment settingsFragment = SettingsFragment.newInstance();
                    return settingsFragment;
                default:
                    return null;
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}
