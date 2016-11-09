package me.xihuxiaolong.justdoit.module.planlist;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.grantland.widget.AutofitTextView;
import me.xihuxiaolong.justdoit.R;
import me.xihuxiaolong.justdoit.common.base.BaseActivity;
import me.xihuxiaolong.justdoit.common.base.BaseMvpActivity;
import me.xihuxiaolong.justdoit.common.base.BaseMvpFragment;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
import me.xihuxiaolong.justdoit.common.util.ProjectActivityUtils;
import me.xihuxiaolong.justdoit.common.widget.DayNightBackgroundView;
import me.xihuxiaolong.justdoit.module.adapter.PlanListWrapper;
import me.xihuxiaolong.justdoit.module.adddayplan.AddDayPlanActivity;
import me.xihuxiaolong.justdoit.module.editalert.EditAlertActivity;
import me.xihuxiaolong.justdoit.module.editplan.EditPlanActivity;
import me.xihuxiaolong.justdoit.module.planhistory.PlanHistoryActivity;
import me.xihuxiaolong.justdoit.module.settings.SettingsActivity;
import me.xihuxiaolong.library.utils.ActivityUtils;


/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/7/5.
 */
public class PlanListFragment extends BaseMvpFragment<PlanListContract.IView, PlanListContract.IPresenter> implements PlanListContract.IView, ObservableScrollViewCallbacks, PlanListWrapper.PlanListOnClickListener {

    private static final float MAX_TEXT_SCALE_DELTA = 0.5f;
    private static final int SELECT_TEMPLATE_REQUEST = 1;

    PlanListComponent planListComponent;

    @BindView(R.id.recyclerView)
    ObservableRecyclerView recyclerView;
    private int mFlexibleSpaceImageHeight, mFlexibleSpaceShowFabOffset, mFlexibleSpaceCalendarBottomOffset, mFlexibleSpaceCalendarLeftOffset,
            mFlexibleSpaceSignatureBottomOffset, mFabSizeNormal;
    private int mActionBarSize, mStatusBarSize;

    boolean mFabIsShown = true;

    MenuItem addMenuItem;

    Toolbar toolbar;
    DayNightBackgroundView dayNightBackgroundView;
    FloatingActionMenu fab;
    ImageView headerIV;
    ImageView avatarIV;
    View recyclerBackground;
    View calendarRL;
    TextView calendarDayTv, calendarWeekTv, calendarMonthYearTv;
    AutofitTextView signatureTV;
    FrameLayout shadowFrame;
    Drawable shadow;

    int vibrant, darkVibrant;

    int mScollY;

    PlanListWrapper planListWrapper;

    long dayTime;

    public static PlanListFragment newInstance() {
        PlanListFragment fragment = new PlanListFragment();
        return fragment;
    }

    @Override
    public PlanListContract.IPresenter createPresenter() {
        return planListComponent.presenter();
    }

    @Override
    protected void injectDependencies() {
        planListComponent = DaggerPlanListComponent.builder()
                .appComponent(ProjectActivityUtils.getAppComponent(getActivity()))
                .planListModule(new PlanListModule(dayTime))
                .build();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.loadDayInfo();
        presenter.loadPlans();
        presenter.loadUserSettings();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        dayTime = getActivity().getIntent().getLongExtra("dayTime", -1L);
        injectDependencies();
        View view = inflater.inflate(R.layout.fragment_plan_list, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mFlexibleSpaceShowFabOffset = getResources().getDimensionPixelSize(R.dimen.flexible_space_show_fab_offset);
        mFlexibleSpaceCalendarBottomOffset = getResources().getDimensionPixelSize(R.dimen.flexible_space_calendar_bottom_offset);
        mFlexibleSpaceCalendarLeftOffset = getResources().getDimensionPixelSize(R.dimen.flexible_space_calendar_left_offset);
        mFlexibleSpaceSignatureBottomOffset = getResources().getDimensionPixelSize(R.dimen.flexible_space_signature_bottom_offset);
        shadow = ContextCompat.getDrawable(getContext(), R.drawable.bottom_shadow);
        mFabSizeNormal = getResources().getDimensionPixelSize(R.dimen.fab_menu_size_normal);

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        dayNightBackgroundView = (DayNightBackgroundView) getActivity().findViewById(R.id.day_night_background_view);
        ViewGroup.LayoutParams layoutParams = toolbar.getLayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mStatusBarSize = getStatusBarHeight();
        }
        mActionBarSize = layoutParams.height - mStatusBarSize;

        avatarIV = (ImageView) getActivity().findViewById(R.id.avatarIV);
        fab = (FloatingActionMenu) getActivity().findViewById(R.id.fab);
        fab.findViewById(R.id.planFab).setOnClickListener(fabListener);
        fab.findViewById(R.id.alertFab).setOnClickListener(fabListener);
//        fab.findViewById(R.id.tomorrowPlanFab).setOnClickListener(fabListener);
        fab.setClosedOnTouchOutside(true);
        headerIV = (ImageView) getActivity().findViewById(R.id.headerIV);
        recyclerBackground = getActivity().findViewById(R.id.recycler_background);
        calendarRL = getActivity().findViewById(R.id.calendar_rl);
        calendarDayTv = (TextView) getActivity().findViewById(R.id.calendar_day_tv);
        calendarWeekTv = (TextView) getActivity().findViewById(R.id.calendar_week_tv);
        calendarMonthYearTv = (TextView) getActivity().findViewById(R.id.calendar_month_year_tv);
        signatureTV = (AutofitTextView) getActivity().findViewById(R.id.signatureTV);
        shadowFrame = (FrameLayout) getActivity().findViewById(R.id.shadowFrame);
//        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.example);
//        Palette.Builder builder = new Palette.Builder(largeIcon);
//        Palette palette = builder.generate();
//        vibrant = palette.getVibrantColor(0x000000);
//        darkVibrant = palette.getDarkVibrantColor(0x000000);
//        recyclerBackground.setBackgroundColor(vibrant);
        vibrant = ContextCompat.getColor(getContext(), R.color.sky);
        darkVibrant = ContextCompat.getColor(getContext(), R.color.dark_sky);

        calendarRL.post(new Runnable() {
            @Override
            public void run() {
                float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
                float scale = 1 + ScrollUtils.getFloat((flexibleRange - mScollY) / flexibleRange, 0, MAX_TEXT_SCALE_DELTA);
                ViewHelper.setPivotX(calendarRL, 0);
                ViewHelper.setPivotY(calendarRL, 0);
                ViewHelper.setScaleX(calendarRL, scale);
                ViewHelper.setScaleY(calendarRL, scale);
                int maxTitleTranslationY = (int) (mFlexibleSpaceImageHeight - calendarRL.getHeight() * scale - mFlexibleSpaceCalendarBottomOffset);
                int titleTranslationY = maxTitleTranslationY - mScollY;
                ViewHelper.setTranslationY(calendarRL, ScrollUtils.getFloat(titleTranslationY, (mActionBarSize - calendarRL.getHeight()) / 2 + mStatusBarSize,
                        maxTitleTranslationY));
                ViewHelper.setTranslationX(calendarRL, ScrollUtils.getFloat(mScollY, 0,
                        mActionBarSize));
            }
        });
        signatureTV.post(new Runnable() {
            @Override
            public void run() {
                // Translate title text
                int maxSignatureTranslationY = mFlexibleSpaceImageHeight - signatureTV.getHeight() - mFlexibleSpaceSignatureBottomOffset;
                ViewHelper.setTranslationY(signatureTV, maxSignatureTranslationY);
            }
        });

        planListWrapper = new PlanListWrapper(getContext(), recyclerView, this);
        final View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.item_plan_header, recyclerView, false);
        planListWrapper.addHeaderView(headerView);
        recyclerView.setScrollViewCallbacks(this);
//        recyclerView.post(new Runnable() {
//            @Override
//            public void run() {
//                ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(4, mActionBarSize + mStatusBarSize);
//            }
//        });
//        Icepick.restoreInstanceState(this, savedInstanceState);
        return view;
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        Icepick.saveInstanceState(this, outState);
    }

    boolean isTodayDay = true;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(isTodayDay) {
            inflater.inflate(R.menu.menu_frament_planlist, menu);
        }else{
            inflater.inflate(R.menu.menu_frament_planlist_other_day, menu);
        }
        addMenuItem = menu.findItem(R.id.action_add);
        addMenuItem.setVisible(!mFabIsShown);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
            case R.id.action_history:
                startActivity(new Intent(getActivity(), PlanHistoryActivity.class));
                return true;
            case R.id.action_add_alert:
                startActivity(new Intent(getActivity(), EditAlertActivity.class).putExtra(EditAlertActivity.ARGUMENT_DAY_TIME, DateTime.now().withTimeAtStartOfDay().getMillis()));
                return true;
            case R.id.action_add_plan:
                startActivity(new Intent(getActivity(), EditPlanActivity.class).putExtra(EditPlanActivity.ARGUMENT_DAY_TIME, DateTime.now().withTimeAtStartOfDay().getMillis()));
                return true;
            case R.id.action_add_tomorrow_plan:
                startActivity(new Intent(getActivity(), PlanListActivity.class).putExtra("dayTime", DateTime.now().withTimeAtStartOfDay().plusDays(1).getMillis()));
                return true;
            case R.id.action_settings:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void removePlanItem(long planId) {

    }

    @Override
    public void addPlanItem(PlanDO planDO) {

    }

    @Override
    public void updatePlanItem(PlanDO planDO) {

    }

    @Override
    public void showPlans(final List<PlanDO> plans) {
        planListWrapper.setItems(plans);
    }

    @Override
    public void showDayInfo(String avatarUrl, DateTime dateTime) {
        calendarWeekTv.setText(dateTime.toString(DateTimeFormat.forPattern("EEEE")));
        if(avatarUrl == null){
            avatarIV.setVisibility(View.GONE);
            calendarDayTv.setVisibility(View.VISIBLE);
            calendarDayTv.setText(dateTime.toString(DateTimeFormat.forPattern("d")));
            calendarMonthYearTv.setText(dateTime.toString(DateTimeFormat.forPattern("MM月 yyyy")));
        } else {
            avatarIV.setVisibility(View.VISIBLE);
            calendarDayTv.setVisibility(View.GONE);
            avatarIV.setImageURI(null);
            avatarIV.setImageURI(Uri.parse(avatarUrl));
            calendarMonthYearTv.setText(dateTime.toString(DateTimeFormat.forPattern("MM月dd日 yyyy")));
        }
    }

    @Override
    public void showSignature(final String signature, final String preSignature) {
        if(preSignature == null)
            signatureTV.setText(signature);
        else {
            signatureTV.setText(preSignature);
            signatureTV.postDelayed(new Runnable() {
                @Override
                public void run() {
                    signatureTV.animate()
                            .alpha(0.0f)
                            .setDuration(600)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    signatureTV.setText(signature);
                                    signatureTV.animate().alpha(1.0f).setDuration(600).setListener(null);
                                }
                            });
                }
            }, 8000);

        }
    }

    boolean hasChangeDayNight = false;

    @Override
    public void changeDayNight() {
        hasChangeDayNight = true;
    }

    @Override
    public void gotoTemplates() {
//        ActivityUtils.delay(0, new ActivityUtils.DelayCallback() {
//            @Override
//            public void afterDelay() {
//                startActivityForResult(new Intent(getActivity(), AddDayPlanActivity.class).putExtra("dayTime", DateTime.now().withTimeAtStartOfDay().getMillis()), 1);
//            }
//        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == SELECT_TEMPLATE_REQUEST) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {

            }
        }
    }

    @Override
    public void showOtherDayUI() {
        isTodayDay = false;
        mFlexibleSpaceCalendarLeftOffset = getResources().getDimensionPixelSize(R.dimen.flexible_space_calendar_left_offset_other);
//        setToolbar(toolbar, true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("明日计划");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        mScollY = scrollY;
        float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;

        // Translate FAB
        int maxFabTranslationY = mFlexibleSpaceImageHeight - mFabSizeNormal / 2;

        float fabTranslationY = ScrollUtils.getFloat(
                -scrollY + mFlexibleSpaceImageHeight - mFabSizeNormal / 2,
                mActionBarSize,
                maxFabTranslationY);
        ViewHelper.setTranslationY(fab, fabTranslationY);
        // Show/hide FAB
        if (fabTranslationY < mFlexibleSpaceShowFabOffset) {
            hideFab();
        } else {
            showFab();
        }

        // Translate imageView parallax
        ViewHelper.setTranslationY(headerIV, -scrollY / 2);
        ViewHelper.setTranslationY(recyclerBackground, Math.max(0, -scrollY + mFlexibleSpaceImageHeight));

        // Scale calendarRL
        float scale = 1 + ScrollUtils.getFloat((flexibleRange - scrollY) / flexibleRange, 0, MAX_TEXT_SCALE_DELTA);
//        ViewHelper.setPivotX(calendarRL, 0);
//        ViewHelper.setPivotY(calendarRL, 0);
        ViewHelper.setScaleX(calendarRL, scale);
        ViewHelper.setScaleY(calendarRL, scale);
        if(avatarIV.getVisibility() == View.VISIBLE) {
            float scaleAvatar = ScrollUtils.getFloat((flexibleRange - scrollY) / flexibleRange, 0.8f, 1f);
            ViewHelper.setScaleX(avatarIV, scaleAvatar);
            ViewHelper.setScaleY(avatarIV, scaleAvatar);
        }

        // Translate calendarRL
        int maxTitleTranslationY = (int) (mFlexibleSpaceImageHeight - calendarRL.getHeight() * scale - mFlexibleSpaceCalendarBottomOffset);
        int titleTranslationY = maxTitleTranslationY - scrollY;
        ViewHelper.setTranslationY(calendarRL, ScrollUtils.getFloat(titleTranslationY, (mActionBarSize - calendarRL.getHeight()) / 2 + mStatusBarSize,
                maxTitleTranslationY));
        ViewHelper.setTranslationX(calendarRL, ScrollUtils.getFloat(scrollY, 0,
                mFlexibleSpaceCalendarLeftOffset));

        // Translate signature text
        int maxSignatureTranslationY = mFlexibleSpaceImageHeight - signatureTV.getHeight() - mFlexibleSpaceSignatureBottomOffset;
        int signatureTranslationY = maxSignatureTranslationY - scrollY;
        ViewHelper.setTranslationY(signatureTV, signatureTranslationY);
        float alpha = Math.min(1, (float) (mFlexibleSpaceImageHeight - (scrollY * 1.4)) / mFlexibleSpaceImageHeight );
        ViewHelper.setAlpha(signatureTV, alpha);

        // Translate toolbar
        float alpha1 = Math.min(1, (float) scrollY / (mFlexibleSpaceImageHeight - mActionBarSize));
        toolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha1, vibrant));

        // Translate toolbar
        if(scale == 1.0f)
            shadowFrame.setForeground(shadow);
        else
            shadowFrame.setForeground(null);

    }

    @Override
    public void onDownMotionEvent() {}

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {}

    private void showFab() {
        if(!mFabIsShown) {
            for (int i = 0; i < fab.getChildCount(); ++i) {
                ViewPropertyAnimator.animate(fab.getChildAt(i)).cancel();
                ViewPropertyAnimator.animate(fab.getChildAt(i)).scaleX(1).scaleY(1).setDuration(200).start();
            }
            getActivity().invalidateOptionsMenu();
            mFabIsShown = true;
        }
    }

    private void hideFab() {
        if(mFabIsShown) {
            for (int i = 0; i < fab.getChildCount(); ++i) {
                ViewPropertyAnimator.animate(fab.getChildAt(i)).cancel();
                ViewPropertyAnimator.animate(fab.getChildAt(i)).scaleX(0).scaleY(0).setDuration(200).start();
            }
            getActivity().invalidateOptionsMenu();
            mFabIsShown = false;
        }
    }

    private View.OnClickListener fabListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.planFab:
                    startActivity(new Intent(getActivity(), EditPlanActivity.class).putExtra(EditPlanActivity.ARGUMENT_DAY_TIME, DateTime.now().withTimeAtStartOfDay().getMillis()));
                    break;
                case R.id.alertFab:
                    startActivity(new Intent(getActivity(), EditAlertActivity.class).putExtra(EditAlertActivity.ARGUMENT_DAY_TIME, DateTime.now().withTimeAtStartOfDay().getMillis()));
                    break;
//                case R.id.tomorrowPlanFab:
//                    startActivity(new Intent(getActivity(), AddDayPlanActivity.class).putExtra("dayTime", DateTime.now().withTimeAtStartOfDay().plusDays(1).getMillis()));
//                    break;
            }
            fab.close(true);
        }
    };

    private View.OnClickListener planListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PlanDO planDO = (PlanDO) v.getTag();
            startActivity(new Intent(getActivity(), EditPlanActivity.class).putExtra(EditPlanActivity.ARGUMENT_EDIT_PLAN_ID, planDO.getId()));
        }
    };

    private View.OnClickListener alertListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PlanDO planDO = (PlanDO) v.getTag();
            startActivity(new Intent(getActivity(), EditAlertActivity.class).putExtra(EditAlertActivity.ARGUMENT_EDIT_ALERT_ID, planDO.getId()));
        }
    };

    @Override
    public void onPause() {
        super.onPause();
//        dayNightBackgroundView.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(hasChangeDayNight) {
            ((PlanListActivity) getActivity()).restart();
            hasChangeDayNight = false;
        }
    }

    @Override
    public void planListenr(PlanDO planDO) {
        startActivity(new Intent(getActivity(), EditPlanActivity.class).putExtra(EditPlanActivity.ARGUMENT_EDIT_PLAN_ID, planDO.getId()));
    }

    @Override
    public void alertListenr(PlanDO planDO) {
        startActivity(new Intent(getActivity(), EditAlertActivity.class).putExtra(EditAlertActivity.ARGUMENT_EDIT_ALERT_ID, planDO.getId()));
    }
}
