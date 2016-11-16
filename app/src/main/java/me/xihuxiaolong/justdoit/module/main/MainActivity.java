package me.xihuxiaolong.justdoit.module.main;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.xihuxiaolong.justdoit.R;
import me.xihuxiaolong.justdoit.common.base.BaseActivity;
import me.xihuxiaolong.justdoit.common.event.Event;
import me.xihuxiaolong.justdoit.module.planlist.PlanListFragment;
import me.xihuxiaolong.justdoit.module.service.PlanService;
import me.xihuxiaolong.justdoit.module.settings.SettingsFragment;
import me.xihuxiaolong.justdoit.module.targetlist.TargetListFragment;
import me.xihuxiaolong.library.utils.ActivityUtils;
import me.xihuxiaolong.library.widget.BottomTabView;

public class MainActivity extends BaseActivity implements ScrollListener {

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private ServiceConnection sc;
    private PlanService planService;

//    private long dayTime;

    @BindView(R.id.bottomTabView)
    BottomTabView bottomTabView;

    @BindView(R.id.bt_cardview)
    CardView btCardview;

    private List<BottomTabView.TabData> bottomTabDatas;

    MainFragmentPageAdapter mainFragmentPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        dayTime = getIntent().getLongExtra("dayTime", -1L);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
//        setToolbar(toolbar, false);
//        if(dayTime != -1L)
//            dayNightBackgroundView.setAnimationDuration(0);
        //生成底部tab
        bottomTabDatas = new ArrayList<>();
        bottomTabDatas.add(new BottomTabView.TabData("每日计划", R.color.bottom_color_state_list, R.drawable.tab_state_every_plan));
        bottomTabDatas.add(new BottomTabView.TabData("目标管理", R.color.bottom_color_state_list, R.drawable.tab_state_target));
        bottomTabDatas.add(new BottomTabView.TabData("我的", R.color.bottom_color_state_list, R.drawable.tab_state_me));
        bottomTabView.init(bottomTabDatas);
        btCardview.setVisibility(View.INVISIBLE);

        mainFragmentPageAdapter = new MainFragmentPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mainFragmentPageAdapter);
        bottomTabView.setViewPager(viewPager, false);
        viewPager.setOffscreenPageLimit(3);
        if(getIntent().getBooleanExtra("restart", false)) {
            viewPager.setCurrentItem(2);
            bottomTabView.setCurrentItem(2);
            btCardview.setVisibility(View.VISIBLE);
        }else {
            ActivityUtils.delay(500, new ActivityUtils.DelayCallback() {
                @Override
                public void afterDelay() {
                    hideBottom(0);
                }
            });
            ActivityUtils.delay(5000, new ActivityUtils.DelayCallback() {
                @Override
                public void afterDelay() {
                    showBottom(500);
                }
            });
            viewPager.setCurrentItem(0);
            bottomTabView.setCurrentItem(0);
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                invalidateFragmentMenus(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        sc = new ServiceConnection() {
            /*
             * 只有在MyService中的onBind方法中返回一个IBinder实例才会在Bind的时候
             * 调用onServiceConnection回调方法
             * 第二个参数service就是MyService中onBind方法return的那个IBinder实例，可以利用这个来传递数据
             */
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                // TODO Auto-generated method stub
                planService = ((PlanService.LocalBinder) service).getService();
                planService.sendNotification();
                String recStr = ((PlanService.LocalBinder) service).stringToSend;
                //利用IBinder对象传递过来的字符串数据（其他数据也可以啦，哪怕是一个对象也OK~~）
                Log.i("TAG", "The String is : " + recStr);
                Log.i("TAG", "onServiceConnected : myService ---> " + planService);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                /* SDK上是这么说的：
                 * This is called when the connection with the service has been unexpectedly disconnected
                 * that is, its process crashed. Because it is running in our same process, we should never see this happen.
                 * 所以说，只有在service因异常而断开连接的时候，这个方法才会用到*/
                // TODO Auto-generated method stub
                sc = null;
                Log.i("TAG", "onServiceDisconnected : ServiceConnection --->"
                        + sc);
            }

        };

        Intent intent = new Intent(MainActivity.this,
                PlanService.class);
        startService(intent);

        bindService(intent, sc, Context.BIND_AUTO_CREATE);
    }

    private void invalidateFragmentMenus(int position) {
        for (int i = 0; i < mainFragmentPageAdapter.getCount(); i++) {
            mainFragmentPageAdapter.getItem(i).setHasOptionsMenu(i == position);
            if (i == position)
                ((MainActivityListener) mainFragmentPageAdapter.getItem(i)).reloadToolbar();
        }
        invalidateOptionsMenu(); //or respectively its support method.
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
        ObjectAnimator animator = ObjectAnimator.ofFloat(btCardview, "translationY", btCardview.getHeight());
        animator.setDuration(duration);
        animator.start();
        isBottomVisible = false;
    }

    private void showBottom(int duration) {
        btCardview.setVisibility(View.VISIBLE);
        ObjectAnimator animator = ObjectAnimator.ofFloat(btCardview, "translationY", 0);
        animator.setDuration(duration);
        animator.start();
        isBottomVisible = true;
    }

    @Subscribe
    public void onEvent(Event.ChangeDayNightTheme changeDayNightTheme) {
//        recreate();
        restart();
    }

    PlanListFragment planListFragment;
    TargetListFragment targetListFragment;
    SettingsFragment settingsFragment;

    public class MainFragmentPageAdapter extends FragmentPagerAdapter {
        public MainFragmentPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return bottomTabDatas.size();
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (planListFragment == null) {
                        planListFragment = PlanListFragment.newInstance();
                        planListFragment.setScrollListener(MainActivity.this);
                    }
                    return planListFragment;
                case 1:
                    if (targetListFragment == null) {
                        targetListFragment = TargetListFragment.newInstance();
                        targetListFragment.setScrollListener(MainActivity.this);
                    }
                    return targetListFragment;
                case 2:
                    if (settingsFragment == null) {
                        settingsFragment = SettingsFragment.newInstance();
                    }
                    return settingsFragment;
//                case 2:
//                    return MineFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

    }

}
