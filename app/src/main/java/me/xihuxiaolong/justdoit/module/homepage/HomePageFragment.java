package me.xihuxiaolong.justdoit.module.homepage;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.google.android.flexbox.FlexboxLayout;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.orhanobut.logger.Logger;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import me.grantland.widget.AutofitTextView;
import me.xihuxiaolong.justdoit.R;
import me.xihuxiaolong.justdoit.common.base.BaseMvpFragment;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
import me.xihuxiaolong.justdoit.common.database.localentity.TargetDO;
import me.xihuxiaolong.justdoit.common.event.Event;
import me.xihuxiaolong.justdoit.common.util.DayNightModeUtils;
import me.xihuxiaolong.justdoit.common.util.ImageUtils;
import me.xihuxiaolong.justdoit.common.util.ProjectActivityUtils;
import me.xihuxiaolong.justdoit.module.adapter.BacklogListAdapter;
import me.xihuxiaolong.justdoit.module.adapter.PlanListAdapter;
import me.xihuxiaolong.justdoit.module.adddayplan.PlanTemplateFragment;
import me.xihuxiaolong.justdoit.module.easybackloglist.EasyBacklogListFragment;
import me.xihuxiaolong.justdoit.module.easyplanlist.EasyPlanListFragment;
import me.xihuxiaolong.justdoit.module.editalert.EditAlertActivity;
import me.xihuxiaolong.justdoit.module.editphoto.EditPhotoActivity;
import me.xihuxiaolong.justdoit.module.editplan.EditPlanActivity;
import me.xihuxiaolong.justdoit.module.main.MainActivityListener;
import me.xihuxiaolong.justdoit.module.main.ScrollListener;
import me.xihuxiaolong.justdoit.module.planhistory.PlanHistoryActivity;
import me.xihuxiaolong.justdoit.module.planlist.DaggerPlanListComponent;
import me.xihuxiaolong.justdoit.module.planlist.OtherDayActivity;
import me.xihuxiaolong.justdoit.module.redoplanlist.RedoPlanListActivity;
import me.xihuxiaolong.justdoit.module.settings.SettingsActivity;
import me.xihuxiaolong.justdoit.module.settings.SettingsFragment;
import me.xihuxiaolong.justdoit.module.targetlist.TargetListFragment;
import me.xihuxiaolong.library.utils.ActivityUtils;
import me.xihuxiaolong.library.utils.CollectionUtils;
import me.xihuxiaolong.library.utils.DialogUtils;
import me.xihuxiaolongren.photoga.MediaChoseActivity;
import mehdi.sakout.fancybuttons.FancyButton;


/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/7/5.
 */
public class HomePageFragment extends BaseMvpFragment<HomePageContract.IView, HomePageContract.IPresenter> implements HomePageContract.IView, ScrollListener, MainActivityListener, CalendarDatePickerDialogFragment.OnDateSetListener{

    private static final String FRAG_TAG_DATE_PICKER = "FRAG_TAG_DATE_PICKER";

    private static final float MAX_TEXT_SCALE_DELTA = 0.5f;
    private static final int REQUEST_PUNCH = 1;

    HomePageComponent homePageComponent;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

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
    @BindView(R.id.signatureTV)
    AutofitTextView signatureTV;
    @BindView(R.id.avatarIV)
    CircleImageView avatarIV;
    @BindView(R.id.calendar_rl)
    LinearLayout calendarRl;
    @BindView(R.id.planFab)
    FloatingActionButton planFab;
    @BindView(R.id.alertFab)
    FloatingActionButton alertFab;
    @BindView(R.id.photoFab)
    FloatingActionButton photoFab;
    @BindView(R.id.punchFab)
    FloatingActionButton punchFab;
    @BindView(R.id.backlogFab)
    FloatingActionButton backlogFab;
    @BindView(R.id.fab)
    FloatingActionMenu fab;
    @BindView(R.id.headerIV)
    ImageView headerIV;
    @BindView(R.id.shadowFrameHomePage)
    FrameLayout shadowFrameHomePage;
    @BindView(R.id.viewPagerTab)
    SmartTabLayout viewPagerTab;

    private int mFlexibleSpaceImageHeight, mFlexibleRecyclerOffset, mFlexibleSpaceShowFabOffset, mFlexibleSpaceCalendarBottomOffset, mFlexibleSpaceCalendarLeftOffset,
            mFlexibleSpaceSignatureBottomOffset, mFabSizeNormal, tablayoutHeight;
    private int mActionBarSize, mStatusBarSize;

    boolean mFabIsShown = true;

    MenuItem addMenuItem;

    Drawable shadow;

    int vibrant;

    int mScollY;

    long dayTime;

    ScrollListener scrollListener;

    public static HomePageFragment newInstance(Long dayTime) {
        HomePageFragment fragment = new HomePageFragment();
        Bundle args = new Bundle();
        if(dayTime != null)
            args.putLong("dayTime", dayTime);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public HomePageContract.IPresenter createPresenter() {
        return homePageComponent.presenter();
    }

    @Override
    protected void injectDependencies() {
        homePageComponent = DaggerHomePageComponent.builder()
                .appComponent(ProjectActivityUtils.getAppComponent(getActivity()))
                .homePageModule(new HomePageModule(dayTime))
                .build();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof ScrollListener){
            scrollListener = ((ScrollListener)activity);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.loadUserSettings();
        presenter.loadBacklogCount();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        dayTime = getArguments().getLong("dayTime", -1L);
        injectDependencies();
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        ButterKnife.bind(this, view);
        Logger.d(toolbar);
        initToolbar(toolbar, false);
        setHasOptionsMenu(true);
        EventBus.getDefault().register(this);

        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mFlexibleRecyclerOffset = getResources().getDimensionPixelSize(R.dimen.flexible_recyclerview_header_height);
        mFlexibleSpaceShowFabOffset = getResources().getDimensionPixelSize(R.dimen.flexible_space_show_fab_offset);
        tablayoutHeight = getResources().getDimensionPixelSize(R.dimen.tablayout_height);
        mFlexibleSpaceCalendarBottomOffset = getResources().getDimensionPixelSize(R.dimen.flexible_space_calendar_bottom_offset);
        mFlexibleSpaceCalendarLeftOffset = getResources().getDimensionPixelSize(R.dimen.flexible_space_calendar_left_offset);
        mFlexibleSpaceSignatureBottomOffset = getResources().getDimensionPixelSize(R.dimen.flexible_space_signature_bottom_offset);
        shadow = ContextCompat.getDrawable(getContext(), R.drawable.bottom_shadow);
        mFabSizeNormal = getResources().getDimensionPixelSize(R.dimen.fab_menu_size_normal);

        ViewGroup.LayoutParams layoutParams = toolbar.getLayoutParams();
        mStatusBarSize = getStatusBarHeight();
        mActionBarSize = layoutParams.height - mStatusBarSize;

        planFab.setOnClickListener(fabListener);
        alertFab.setOnClickListener(fabListener);
        photoFab.setOnClickListener(fabListener);
        punchFab.setOnClickListener(fabListener);
        backlogFab.setOnClickListener(fabListener);
        fab.setClosedOnTouchOutside(true);

        vibrant = ContextCompat.getColor(getContext(), R.color.sky);
        ObjectAnimator signatureAnimator = ObjectAnimator
                .ofFloat(signatureTV, "alpha", 0, 1.0f)
                .setDuration(1000);
        signatureAnimator.start();
//        signatureTV.post(new Runnable() {
//            @Override
//            public void run() {
//                // Translate title text
//                int maxSignatureTranslationY = mFlexibleSpaceImageHeight - signatureTV.getHeight() - mFlexibleSpaceSignatureBottomOffset;
//                ViewHelper.setTranslationY(signatureTV, maxSignatureTranslationY);
//                ViewHelper.setAlpha(signatureTV, maxSignatureTranslationY);
//            }
//        });

        FragmentPagerItems.Creator creator = FragmentPagerItems.with(getContext())
                .add("日程", EasyPlanListFragment.class)
                .add("待办", EasyBacklogListFragment.class);

        final FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getChildFragmentManager(), creator.create());
        viewPager.setAdapter(adapter);
        viewPagerTab.setViewPager(viewPager);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_frament_planlist, menu);
        addMenuItem = menu.findItem(R.id.action_add);
        addMenuItem.setVisible(!mFabIsShown);
        addMenuItem.setEnabled(!mFabIsShown);
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
                startAlert();
                return true;
            case R.id.action_add_plan:
                startPlan();
                return true;
            case R.id.action_add_photo:
                startPhoto();
                return true;
            case R.id.action_add_punch:
                startPunch();
                return true;
            case R.id.action_add_tomorrow_plan:
                startActivity(new Intent(getActivity(), OtherDayActivity.class).putExtra("dayTime", DateTime.now().withTimeAtStartOfDay().plusDays(1).getMillis()));
                return true;
            case R.id.action_settings:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            case R.id.action_redo_plan:
                startActivity(new Intent(getActivity(), RedoPlanListActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private View.OnClickListener fabListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.planFab:
                    startPlan();
                    break;
                case R.id.alertFab:
                    startAlert();
                    break;
                case R.id.photoFab:
                    startPhoto();
                    break;
                case R.id.punchFab:
                    startPunch();
                    break;
                case R.id.backlogFab:
                    showBacklogDialog();
                    break;
//                case R.id.tomorrowPlanFab:
//                    startActivity(new Intent(getActivity(), AddDayPlanActivity.class).putExtra("dayTime", DateTime.now().withTimeAtStartOfDay().plusDays(1).getMillis()));
//                    break;
            }
            fab.close(true);
        }
    };

    void startPunch(){
        presenter.startAddPunch();
    }

    void startPhoto(){
        startActivity(new Intent(getActivity(), EditPhotoActivity.class));
    }

    void startPlan(){
        startActivity(new Intent(getActivity(), EditPlanActivity.class).putExtra(EditPlanActivity.ARGUMENT_DAY_TIME, DateTime.now().withTimeAtStartOfDay().getMillis()));
    }

    void startAlert(){
        startActivity(new Intent(getActivity(), EditAlertActivity.class).putExtra(EditAlertActivity.ARGUMENT_DAY_TIME, DateTime.now().withTimeAtStartOfDay().getMillis()));
    }

    @OnClick(R.id.calendar_rl)
    void calendarClick(View v){
        DateTime now = DateTime.now();
        MonthAdapter.CalendarDay minDate = new MonthAdapter.CalendarDay(now.getYear(), now.getMonthOfYear() - 1, now.getDayOfMonth() + 1);
        CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                .setOnDateSetListener(this)
                .setDoneText(getResources().getString(R.string.action_confirm))
                .setCancelText(getResources().getString(R.string.action_cancel))
                .setDateRange(minDate, null);
        if (DayNightModeUtils.isCurrentNight())
            cdp.setThemeDark();
        else
            cdp.setThemeLight();
        cdp.show(getChildFragmentManager(), FRAG_TAG_DATE_PICKER);
    }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        DateTime dateTime = new DateTime(year, monthOfYear + 1, dayOfMonth, 0, 0);
        startActivity(new Intent(getActivity(), OtherDayActivity.class).putExtra("dayTime", dateTime.withTimeAtStartOfDay().getMillis()));
    }

    @Override
    public void showDayInfo(String avatarUrl, DateTime dateTime) {
        calendarWeekTv.setText(dateTime.toString(DateTimeFormat.forPattern("EEEE")));
        if (avatarUrl == null) {
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
        signatureTV.post(new Runnable() {
            @Override
            public void run() {
                updateSignature(mScollY);
            }
        });
        if (preSignature == null)
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && !CollectionUtils.isEmpty(data.getStringArrayListExtra("data"))) {
            ArrayList<String> uris = data.getStringArrayListExtra("data");
            if (requestCode == REQUEST_PUNCH) {
                picUri = uris.get(0);
                if(picIV != null) {
                    picIV.setVisibility(View.VISIBLE);
                    ImageUtils.loadImageFromFile(getActivity(), picIV, picUri, ImageView.ScaleType.CENTER_CROP);
                }
                if(operIV != null){
                    operIV.setImageResource(R.drawable.icon_delete);
                }
            }
        }
    }

    @Override
    public void savePunchSuccess() {

    }

    @Override
    public void onScrollChanged(int scrollY, int flag) {
        int i = viewPager.getCurrentItem();
        if(flag != i)
            return;
        if(scrollListener != null)
            scrollListener.onScrollChanged(scrollY, flag);
        mScollY = scrollY;

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

        int maxIndicatorTranslationY = mFlexibleSpaceImageHeight - tablayoutHeight / 2;
        float indicatorTranslationY = ScrollUtils.getFloat(
                -scrollY + mFlexibleSpaceImageHeight - tablayoutHeight / 2,
                mActionBarSize,
                maxIndicatorTranslationY);
        ViewHelper.setTranslationY(viewPagerTab, indicatorTranslationY);

        // Translate imageView parallax
        ViewHelper.setTranslationY(headerIV, -scrollY / 2);
        ViewHelper.setTranslationY(recyclerBackground, Math.max(0, -scrollY + mFlexibleSpaceImageHeight));

        animateCanlendarRl(scrollY);

        // Translate signature text
        updateSignature(scrollY);

        // Translate toolbar
        float alpha1 = Math.min(1, (float) scrollY / (mFlexibleRecyclerOffset - 20));
        toolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha1, vibrant));

        // Translate toolbar
        if (mFlexibleSpaceImageHeight - mActionBarSize < scrollY)
            shadowFrameHomePage.setForeground(shadow);
        else
            shadowFrameHomePage.setForeground(null);

    }

    public ViewGroup getInterView(){
        return shadowFrameHomePage;
    }

    // Scale calendarRl
    void animateCanlendarRl(int scrollY) {
        float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
        float scale = 1 + ScrollUtils.getFloat((flexibleRange - scrollY) / flexibleRange, 0, MAX_TEXT_SCALE_DELTA);
        ViewHelper.setPivotX(calendarRl, 0);
        ViewHelper.setPivotY(calendarRl, 0);
        ViewHelper.setScaleX(calendarRl, scale);
        ViewHelper.setScaleY(calendarRl, scale);
        if (avatarIV.getVisibility() == View.VISIBLE) {
            float scaleAvatar = ScrollUtils.getFloat((flexibleRange - scrollY) / flexibleRange, 0.8f, 1f);
            ViewHelper.setScaleX(avatarIV, scaleAvatar);
            ViewHelper.setScaleY(avatarIV, scaleAvatar);
        }

        // Translate calendarRl
        int maxTitleTranslationY = (int) (mFlexibleSpaceImageHeight - calendarRl.getMeasuredHeight() * scale - mFlexibleSpaceCalendarBottomOffset);
        int titleTranslationY = maxTitleTranslationY - scrollY;
        ViewHelper.setTranslationY(calendarRl, ScrollUtils.getFloat(titleTranslationY, (mActionBarSize - calendarRl.getMeasuredHeight()) / 2 + mStatusBarSize,
                maxTitleTranslationY));
        ViewHelper.setTranslationX(calendarRl, ScrollUtils.getFloat(scrollY, 0,
                mFlexibleSpaceCalendarLeftOffset));
    }


    private void updateSignature(int scrollY){
        int signatureTVHeight = signatureTV.getHeight() == 0 ? signatureTV.getMeasuredHeight() : signatureTV.getHeight();
        int maxSignatureTranslationY = (mFlexibleSpaceImageHeight + mFlexibleSpaceSignatureBottomOffset - signatureTVHeight) / 2;
        int signatureTranslationY = maxSignatureTranslationY - scrollY;
        ViewHelper.setTranslationY(signatureTV, signatureTranslationY);
        float alpha = Math.min(1, (float) (mFlexibleSpaceImageHeight - (scrollY * 1.4)) / mFlexibleSpaceImageHeight);
        ViewHelper.setAlpha(signatureTV, alpha);
    }

    private void showFab() {
        if (!mFabIsShown) {
//            fab.setVisibility(View.VISIBLE);
            for (int i = 0; i < fab.getChildCount(); ++i) {
                ViewPropertyAnimator.animate(fab.getChildAt(i)).cancel();
                ViewPropertyAnimator.animate(fab.getChildAt(i)).scaleX(1).scaleY(1).setDuration(200).start();
            }
            getActivity().invalidateOptionsMenu();
            mFabIsShown = true;
        }
    }

    private void hideFab() {
        if (mFabIsShown) {
            for (int i = 0; i < fab.getChildCount(); ++i) {
                ViewPropertyAnimator.animate(fab.getChildAt(i)).cancel();
                ViewPropertyAnimator.animate(fab.getChildAt(i)).scaleX(0).scaleY(0).setDuration(200).start();
            }
//            fab.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    fab.setVisibility(View.GONE);
//                }
//            }, 300);
            getActivity().invalidateOptionsMenu();
            mFabIsShown = false;
        }
    }

    private  MaterialDialog addPunchDialog;
    FlexboxLayout allTargetsFl;
    MaterialEditText punchET;
    TextView selectTipTV;
    private ImageView picIV;
    private ImageView operIV;
    String picUri;
    View targetPositive;
    String selectTarget;

    @Override
    public void showPunchDialog(List<TargetDO> targetList){
        picUri = null;
        selectTarget = null;
        addPunchDialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.add_punch_title)
                .customView(R.layout.dialog_add_punch_planlist, true)
                .positiveText(getResources().getString(R.string.action_confirm))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        presenter.savePunch(punchET.getText().toString(), picUri, selectTarget);
                    }
                })
                .negativeText(getResources().getString(R.string.action_cancel))
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        ActivityUtils.hideSoftKeyboard(getActivity());
                    }
                })
                .build();
        addPunchDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        targetPositive = addPunchDialog.getActionButton(DialogAction.POSITIVE);
        targetPositive.setEnabled(false);
        selectTipTV = (TextView) addPunchDialog.findViewById(R.id.selectTipTV);
        selectTipTV.setVisibility(CollectionUtils.isEmpty(targetList) ? View.GONE : View.VISIBLE);
        allTargetsFl = (FlexboxLayout) addPunchDialog.findViewById(R.id.all_target_fl);
        punchET = (MaterialEditText) addPunchDialog.findViewById(R.id.addPunchET);
        punchET.requestFocus();
        punchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                targetPositive.setEnabled(s.length() > 0 ? true : false);
            }
        });
        picIV = (ImageView) addPunchDialog.findViewById(R.id.picIV);
        operIV = (ImageView) addPunchDialog.findViewById(R.id.operIV);
        operIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(picUri == null) {
                    Intent intent = new Intent(getActivity(), MediaChoseActivity.class);
                    //chose_mode选择模式 0单选 1多选
                    intent.putExtra("chose_mode", 0);
                    //是否显示需要第一个是图片相机按钮
                    intent.putExtra("isNeedfcamera", true);
                    startActivityForResult(intent, REQUEST_PUNCH);
                }else{
                    picUri = null;
                    picIV.setVisibility(View.GONE);
                    operIV.setImageResource(R.drawable.menu_add_pic);
                }
            }
        });
        for(TargetDO target : targetList)
            addTagToUnselectView(target);
        addPunchDialog.show();
    }

    @Override
    public void updateBacklogCount(long count) {
        ((TextView)viewPagerTab.getTabAt(1)).setText("待办 (" + count + ")");
    }

    private  MaterialDialog addBacklogDialog;
    MaterialEditText backlogET;
    View backlogPositive;

    public void showBacklogDialog(){
        picUri = null;
        selectTarget = null;
        addBacklogDialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.add_backlog_title)
                .customView(R.layout.dialog_add_backlog, true)
                .positiveText(getResources().getString(R.string.action_confirm))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        presenter.saveBacklog(backlogET.getText().toString());
                    }
                })
                .negativeText(getResources().getString(R.string.action_cancel))
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        ActivityUtils.hideSoftKeyboard(getActivity());
                    }
                })
                .build();
        addBacklogDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        backlogPositive = addBacklogDialog.getActionButton(DialogAction.POSITIVE);
        backlogPositive.setEnabled(false);
        backlogET = (MaterialEditText) addBacklogDialog.findViewById(R.id.backlogET);
        backlogET.requestFocus();
        backlogET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                backlogPositive.setEnabled(s.length() > 0 ? true : false);
            }
        });
        addBacklogDialog.show();
    }

    private void addTagToUnselectView(TargetDO target) {
        FancyButton fancyButton = (FancyButton) LayoutInflater.from(getActivity()).inflate(R.layout.item_unselected_tag, allTargetsFl, false);
        fancyButton.setText(target.getName());
        fancyButton.setTag(target.getName());
        fancyButton.setOnClickListener(unselectedTagClickListener);
        allTargetsFl.addView(fancyButton);
    }

    private View.OnClickListener unselectedTagClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(selectTarget != null && allTargetsFl.findViewWithTag(selectTarget) != null){
                FancyButton fancyButton = (FancyButton) allTargetsFl.findViewWithTag(selectTarget);
                fancyButton.setBackgroundColor(Color.TRANSPARENT);
                fancyButton.setTextColor(ContextCompat.getColor(getActivity(), R.color.titleTextColor));
            }
            FancyButton fancyButton = (FancyButton) v;
            fancyButton.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.titleTextColor));
            fancyButton.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
            selectTarget = (String) fancyButton.getTag();
            targetPositive.setEnabled(true);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        //TODO 判断当前页面的日期是否等于当前日期，不是则重新加载当前页面
    }

    @Override
    public void reloadToolbar() {
        if(toolbar != null) {
            Logger.d(toolbar);
            setToolbar(toolbar, false);
        }
    }

    @Subscribe
    public void onEvent(Event.AddPlan addPlan) {
        if(viewPager != null)
            viewPager.setCurrentItem(0, true);
    }

    @Subscribe
    public void onEvent(Event.AddBacklog addBacklog) {
        if(viewPager != null)
            viewPager.setCurrentItem(1, true);
    }

}
