package me.xihuxiaolong.justdoit.module.main;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;

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

public class MainActivity extends BaseActivity implements ScrollListener {

    @BindView(R.id.viewPager)
    ViewPager viewPager;
//    private ServiceConnection sc;
//    private PlanService planService;

    @BindView(R.id.bottomBar)
    BottomBar bottomBar;

    MainFragmentPageAdapter mainFragmentPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

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
            bottomBar.setVisibility(View.VISIBLE);
        } else {
            ActivityUtils.delay(200, new ActivityUtils.DelayCallback() {
                @Override
                public void afterDelay() {
                    hideBottom(0);
                }
            });
            ActivityUtils.delay(3500, new ActivityUtils.DelayCallback() {
                @Override
                public void afterDelay() {
                    showBottom(600);
                }
            });
            viewPager.setCurrentItem(0);
        }
        viewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                invalidateFragmentMenus(0);
            }
        }, 0);

//        sc = new ServiceConnection() {
//            /*
//             * 只有在MyService中的onBind方法中返回一个IBinder实例才会在Bind的时候
//             * 调用onServiceConnection回调方法
//             * 第二个参数service就是MyService中onBind方法return的那个IBinder实例，可以利用这个来传递数据
//             */
//            @Override
//            public void onServiceConnected(ComponentName name, IBinder service) {
//                // TODO Auto-generated method stub
//                planService = ((PlanService.LocalBinder) service).getService();
////                planService.sendNotification();
//                String recStr = ((PlanService.LocalBinder) service).stringToSend;
//                //利用IBinder对象传递过来的字符串数据（其他数据也可以啦，哪怕是一个对象也OK~~）
//                Log.i("TAG", "The String is : " + recStr);
//                Log.i("TAG", "onServiceConnected : myService ---> " + planService);
//            }
//
//            @Override
//            public void onServiceDisconnected(ComponentName name) {
//                /* SDK上是这么说的：
//                 * This is called when the connection with the service has been unexpectedly disconnected
//                 * that is, its process crashed. Because it is running in our same process, we should never see this happen.
//                 * 所以说，只有在service因异常而断开连接的时候，这个方法才会用到*/
//                // TODO Auto-generated method stub
//                sc = null;
//                Log.i("TAG", "onServiceDisconnected : ServiceConnection --->"
//                        + sc);
//            }
//
//        };

//        Intent intent = new Intent(MainActivity.this,
//                PlanService.class);
//        startService(intent);

//        bindService(intent, sc, Context.BIND_AUTO_CREATE);

        //Create an offset from the current time in which the alarm will go off.
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 150);

        //Create a new PendingIntent and add it to the AlarmManager
        Intent intent1 = new Intent(getApplicationContext(), PlanService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this,
                12345, intent1, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am =
                (AlarmManager)getSystemService(Activity.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                pendingIntent);
    }

    private void invalidateFragmentMenus(int position) {
        Logger.d("position%d", position);
        for (int i = 0; i < mainFragmentPageAdapter.getCount(); i++) {
            if (mainFragmentPageAdapter.getRegisteredFragment(i) != null) {
//                mainFragmentPageAdapter.getRegisteredFragment(i).setHasOptionsMenu(i == position);
                if(i == position)
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
        ObjectAnimator animator = ObjectAnimator.ofFloat(bottomBar, "translationY", bottomBar.getHeight());
        animator.setDuration(duration);
        animator.start();
        isBottomVisible = false;
    }

    private void showBottom(int duration) {
        bottomBar.setVisibility(View.VISIBLE);
        ObjectAnimator animator = ObjectAnimator.ofFloat(bottomBar, "translationY", 0);
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

}
