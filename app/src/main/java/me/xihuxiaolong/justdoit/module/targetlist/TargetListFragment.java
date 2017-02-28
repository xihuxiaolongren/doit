package me.xihuxiaolong.justdoit.module.targetlist;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter;
import com.github.clans.fab.FloatingActionButton;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.orhanobut.logger.Logger;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.RadioButton;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.iwgang.countdownview.CountdownView;
import me.xihuxiaolong.justdoit.R;
import me.xihuxiaolong.justdoit.common.base.BaseMvpFragment;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanHistoryDO;
import me.xihuxiaolong.justdoit.common.database.localentity.TargetDO;
import me.xihuxiaolong.justdoit.common.util.DayNightModeUtils;
import me.xihuxiaolong.justdoit.common.util.DeviceUtil;
import me.xihuxiaolong.justdoit.common.util.ImageUtils;
import me.xihuxiaolong.justdoit.common.util.ProjectActivityUtils;
import me.xihuxiaolong.justdoit.common.widget.LineChartManager;
import me.xihuxiaolong.justdoit.module.main.MainActivityFragmentListener;
import me.xihuxiaolong.justdoit.module.main.MainActivityListener;
import me.xihuxiaolong.justdoit.module.settings.SettingsActivity;
import me.xihuxiaolong.justdoit.module.targetdetail.TargetDetailActivity;
import me.xihuxiaolong.library.utils.CollectionUtils;
import me.xihuxiaolongren.photoga.MediaChoseActivity;
import mehdi.sakout.fancybuttons.FancyButton;

import static me.xihuxiaolong.justdoit.module.targetdetail.TargetDetailActivity.ARG_TARGET;
import static me.xihuxiaolong.justdoit.module.targetdetail.TargetPunchDetailFragment.REQUEST_PUNCH;


/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/7/5.
 */
public class TargetListFragment extends BaseMvpFragment<TargetListContract.IView, TargetListContract.IPresenter> implements TargetListContract.IView, ObservableScrollViewCallbacks, MainActivityFragmentListener, CalendarDatePickerDialogFragment.OnDateSetListener {

    private static final float MAX_TEXT_SCALE_DELTA = 0.5f;
    private static final String FRAG_TAG_DATE_PICKER = "FRAG_TAG_DATE_PICKER";

    TargetListComponent targetListComponent;

    @BindView(R.id.recyclerView)
    ObservableRecyclerView recyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_background)
    View recyclerBackground;
    @BindView(R.id.lineChart)
    LineChart lineChart;
    @BindView(R.id.calendar_rl)
    LinearLayout calendarRl;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.headerIV)
    ImageView headerIV;
    @BindView(R.id.shadowFrame)
    FrameLayout shadowFrame;
    @BindView(R.id.calendar_day_tv)
    TextView calendarDayTv;
//    @BindView(R.id.arc_progress)
//    ArcProgress arcProgress;

    private int mFlexibleSpaceImageHeight, mFlexibleRecyclerOffset, mFlexibleSpaceShowFabOffset, mFlexibleSpaceCalendarBottomOffset, mFlexibleSpaceCalendarLeftOffset,
            mFlexibleSpaceSignatureBottomOffset, mFabSizeNormal;
    private int mActionBarSize, mStatusBarSize;

    boolean mFabIsShown = true;

    MenuItem addMenuItem;

    Drawable shadow;

    int vibrant;

    int mScollY;

    TargetAdapter targetAdapter;

    MainActivityListener mainActivityListener;

    int textColor;

    public static TargetListFragment newInstance() {
        TargetListFragment fragment = new TargetListFragment();
        return fragment;
    }

    @Override
    public TargetListContract.IPresenter createPresenter() {
        return targetListComponent.presenter();
    }

    @Override
    protected void injectDependencies() {
        targetListComponent = DaggerTargetListComponent.builder()
                .appComponent(ProjectActivityUtils.getAppComponent(getActivity()))
                .targetListModule(new TargetListModule())
                .build();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //对传递进来的Activity进行接口转换
        if (activity instanceof MainActivityListener) {
            mainActivityListener = ((MainActivityListener) activity);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.loadTargets();
        presenter.loadStatistics();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        injectDependencies();
        View view = inflater.inflate(R.layout.fragment_target_list, container, false);
        ButterKnife.bind(this, view);
        Logger.d(toolbar);
        initToolbar(toolbar, false);
        setHasOptionsMenu(true);

        textColor = ContextCompat.getColor(getContext(), R.color.titleTextColor);

        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mFlexibleRecyclerOffset = getResources().getDimensionPixelSize(R.dimen.target_list_header_height);
        mFlexibleSpaceShowFabOffset = getResources().getDimensionPixelSize(R.dimen.flexible_space_show_fab_offset);
        mFlexibleSpaceCalendarBottomOffset = getResources().getDimensionPixelSize(R.dimen.flexible_space_calendar_bottom_offset);
        mFlexibleSpaceCalendarLeftOffset = getResources().getDimensionPixelSize(R.dimen.flexible_space_calendar_left_offset);
        mFlexibleSpaceSignatureBottomOffset = getResources().getDimensionPixelSize(R.dimen.flexible_space_signature_bottom_offset);
        shadow = ContextCompat.getDrawable(getContext(), R.drawable.bottom_shadow);
        mFabSizeNormal = getResources().getDimensionPixelSize(R.dimen.fab_menu_size_normal);

        ViewGroup.LayoutParams layoutParams = toolbar.getLayoutParams();
        mStatusBarSize = getStatusBarHeight();
        mActionBarSize = layoutParams.height - mStatusBarSize;

        vibrant = ContextCompat.getColor(getContext(), R.color.sky);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        targetAdapter = new TargetAdapter(new ArrayList<TargetDO>());
        final View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.item_target_header, recyclerView, false);
        targetAdapter.addHeaderView(headerView);
        final View emptyView = LayoutInflater.from(getActivity()).inflate(R.layout.empty_view_target, (ViewGroup) recyclerView.getParent(), false);
        targetAdapter.setEmptyView(emptyView);
        targetAdapter.setHeaderAndEmpty(true);
        recyclerView.setAdapter(targetAdapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                TargetDO targetDO = ((TargetDO) adapter.getItem(position));
                Intent intent = new Intent(getActivity(), TargetDetailActivity.class).putExtra(ARG_TARGET, targetDO);
                if (!TextUtils.isEmpty(targetDO.getHeaderImageUri()) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    View view1 = view.findViewById(R.id.bgIV);
                    Pair<View, String> p1 = Pair.create(view1, view1.getTransitionName());
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), p1);
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                }
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                TargetDO targetDO = ((TargetDO) adapter.getItem(position));
                switch (view.getId()) {
                    case R.id.fab:
                        openPunch(targetDO.getName());
                        break;
                }
            }
        });
        recyclerView.setScrollViewCallbacks(this);

        fab.setOnClickListener(fabListener);

        return view;
    }

    class TargetAdapter extends BaseMultiItemQuickAdapter<TargetDO, BaseViewHolder> {

        public TargetAdapter(List<TargetDO> datas) {
            super(datas);
            addItemType(TargetDO.TYPE_NORMAL, R.layout.item_target_normal);
            addItemType(TargetDO.TYPE_NORMAL_END_TIME, R.layout.item_target_normal_end_time);
            addItemType(TargetDO.TYPE_PUNCH, R.layout.item_target_punch);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, final TargetDO targetDO) {
            switch (targetDO.getItemType()) {
                case TargetDO.TYPE_NORMAL:
                    convertNormal(baseViewHolder, targetDO);
                    break;
                case TargetDO.TYPE_NORMAL_END_TIME:
                    convertNormalEndTime(baseViewHolder, targetDO);
                    break;
                case TargetDO.TYPE_PUNCH:
                    convertPunch(baseViewHolder, targetDO);
                    break;
            }
        }

        private void convertPunch(BaseViewHolder baseViewHolder, TargetDO targetDO) {
            TargetViewHolder holder = (TargetViewHolder) baseViewHolder;
            holder.setText(R.id.title, targetDO.getName());
            ImageView targetIconIV = holder.getView(R.id.targetIconIV);
            targetIconIV.setAlpha(0.55f);
//            FloatingActionButton fab = holder.getView(R.id.fab);
//            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.menu_punch_little_black);
//            drawable = drawable.mutate();
//            drawable.setColorFilter(textColor, PorterDuff.Mode.SRC_IN);
//            fab.setImageDrawable(drawable);
            ImageView bgIV = holder.getView(R.id.bgIV);
            bgIV.setColorFilter(ContextCompat.getColor(getContext(), R.color.bgImageColor), PorterDuff.Mode.SRC_ATOP);
            ImageUtils.loadImageFromFile(getContext(), bgIV, targetDO.getHeaderImageUri(), ImageView.ScaleType.CENTER_CROP);
//            holder.addOnClickListener(R.id.fab);
//            TextView countTV = holder.getView(R.id.countTV);
//            countTV.setText(String.format("今日打卡 %d 次   共计打卡 %d 次", targetDO.getCount(), targetDO.getCount()));
            FancyButton fancyButton = holder.getView(R.id.persistTV);
            int days = Days.daysBetween(new DateTime(targetDO.getCreatedTime()), DateTime.now()).getDays() + 1;
            fancyButton.setText(String.valueOf(days));
            fancyButton.getTextViewObject().setTypeface(null, Typeface.BOLD);
        }

        private void convertNormal(BaseViewHolder baseViewHolder, TargetDO targetDO) {
            TargetViewHolder holder = (TargetViewHolder) baseViewHolder;
            holder.setText(R.id.titleTV, targetDO.getName());
            ImageView targetIconIV = holder.getView(R.id.targetIconIV);
            targetIconIV.setAlpha(0.55f);
            ImageView bgIV = holder.getView(R.id.bgIV);
            bgIV.setColorFilter(ContextCompat.getColor(getContext(), R.color.bgImageColor), PorterDuff.Mode.SRC_ATOP);
            ImageUtils.loadImageFromFile(getContext(), bgIV, targetDO.getHeaderImageUri(), ImageView.ScaleType.CENTER_CROP);
            FancyButton fancyButton = holder.getView(R.id.persistTV);
            int days = Days.daysBetween(new DateTime(targetDO.getCreatedTime()), DateTime.now()).getDays() + 1;
            fancyButton.setText(String.valueOf(days));
            fancyButton.getTextViewObject().setTypeface(null, Typeface.BOLD);
        }

        private void convertNormalEndTime(BaseViewHolder baseViewHolder, TargetDO targetDO) {
            TargetViewHolder holder = (TargetViewHolder) baseViewHolder;
            holder.setText(R.id.titleTV, targetDO.getName());
            ImageView targetIconIV = holder.getView(R.id.targetIconIV);
            targetIconIV.setAlpha(0.55f);
            ImageView bgIV = holder.getView(R.id.bgIV);
            bgIV.setColorFilter(ContextCompat.getColor(getContext(), R.color.bgImageColor), PorterDuff.Mode.SRC_ATOP);
            ImageUtils.loadImageFromFile(getContext(), bgIV, targetDO.getHeaderImageUri(), ImageView.ScaleType.CENTER_CROP);
            CountdownView countdownView = holder.getView(R.id.countdownView);
            long interval = targetDO.getEndTime() - DateTime.now().getMillis();
            long max = targetDO.getEndTime() - targetDO.getCreatedTime();
            long progress = DateTime.now().getMillis() - targetDO.getCreatedTime();
            if(interval > 0)
                countdownView.start(interval);
            RoundCornerProgressBar roundCornerProgressBar = holder.getView(R.id.roundCornerProgressBar);
            roundCornerProgressBar.setMax(max);
            roundCornerProgressBar.setProgress(progress);
            roundCornerProgressBar.setSecondaryProgress(progress);
        }

        @Override
        protected TargetViewHolder createBaseViewHolder(View view) {
            return new TargetViewHolder(view);
        }

        public class TargetViewHolder extends BaseViewHolder {

            public TargetViewHolder(View view) {
                super(view);
            }

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_frament_targetlist, menu);
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
            case R.id.action_add:
                fabListener.onClick(null);
                return true;
            case R.id.action_settings:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        if (mainActivityListener != null)
            mainActivityListener.onScrollChanged(scrollY, 0);
        mScollY = scrollY;

        // Translate FAB
        int maxFabTranslationY = mFlexibleSpaceImageHeight - mFabSizeNormal / 2;

        float fabTranslationY = ScrollUtils.getFloat(
                -scrollY + mFlexibleSpaceImageHeight - mFabSizeNormal / 2,
                mActionBarSize / 2,
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

        animateCanlendarRl(scrollY);

        updateSignature(scrollY);

        // Translate toolbar
        float alpha1 = Math.min(1, (float) scrollY / (mFlexibleRecyclerOffset - 20));
        toolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha1, vibrant));

        // Translate toolbar
        if (mFlexibleSpaceImageHeight - mActionBarSize < scrollY)
            shadowFrame.setForeground(shadow);
        else
            shadowFrame.setForeground(null);

    }

    private void updateSignature(int scrollY) {
        int signatureTVHeight = lineChart.getHeight() == 0 ? lineChart.getMeasuredHeight() : lineChart.getHeight();
        int maxSignatureTranslationY = (mFlexibleSpaceImageHeight + mFlexibleSpaceSignatureBottomOffset - signatureTVHeight) / 2;
        int signatureTranslationY = maxSignatureTranslationY - scrollY;
        ViewHelper.setTranslationY(lineChart, signatureTranslationY);
        float alpha = Math.min(1, (float) (mFlexibleSpaceImageHeight - (scrollY * 1.4)) / mFlexibleSpaceImageHeight);
        ViewHelper.setAlpha(lineChart, alpha);
    }

    // Scale calendarRl
    void animateCanlendarRl(int scrollY) {
        float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
        float scale = 1 + ScrollUtils.getFloat((flexibleRange - scrollY) / flexibleRange, 0, MAX_TEXT_SCALE_DELTA);
        ViewHelper.setPivotX(calendarRl, 0);
        ViewHelper.setPivotY(calendarRl, 0);
        ViewHelper.setScaleX(calendarRl, scale);
        ViewHelper.setScaleY(calendarRl, scale);
//        if (avatarIV.getVisibility() == View.VISIBLE) {
//            float scaleAvatar = ScrollUtils.getFloat((flexibleRange - scrollY) / flexibleRange, 0.8f, 1f);
//            ViewHelper.setScaleX(avatarIV, scaleAvatar);
//            ViewHelper.setScaleY(avatarIV, scaleAvatar);
//        }

        // Translate calendarRl
        int maxTitleTranslationY = (int) (mFlexibleSpaceImageHeight - calendarRl.getMeasuredHeight() * scale - mFlexibleSpaceCalendarBottomOffset);
        int titleTranslationY = maxTitleTranslationY - scrollY;
        ViewHelper.setTranslationY(calendarRl, ScrollUtils.getFloat(titleTranslationY, (mActionBarSize - calendarRl.getMeasuredHeight()) / 2 + mStatusBarSize,
                maxTitleTranslationY));
        ViewHelper.setTranslationX(calendarRl, ScrollUtils.getFloat(scrollY, 0,
                mFlexibleSpaceCalendarLeftOffset));
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }

    private void showFab() {
        if (!mFabIsShown) {
            ViewPropertyAnimator.animate(fab).cancel();
            ViewPropertyAnimator.animate(fab).scaleX(1).scaleY(1).setDuration(200).start();
            getActivity().invalidateOptionsMenu();
            mFabIsShown = true;
        }
    }

    private void hideFab() {
        if (mFabIsShown) {
            ViewPropertyAnimator.animate(fab).cancel();
            ViewPropertyAnimator.animate(fab).scaleX(0).scaleY(0).setDuration(200).start();
            getActivity().invalidateOptionsMenu();
            mFabIsShown = false;
        }
    }

    private View.OnClickListener fabListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showAddTargetDialog();
        }
    };

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void reloadToolbar() {
        if (toolbar != null) {
            Logger.d(toolbar);
            setToolbar(toolbar, false);
        }
    }

    @Override
    public void removeTargetItem(long targetId) {

    }

    @Override
    public void addTargetItem(TargetDO targetDO) {

    }

    @Override
    public void updateTargetItem(TargetDO targetDO) {

    }

    @Override
    public void showTargets(List<TargetDO> targets) {
        targetAdapter.setNewData(targets);
        recyclerView.setPadding(0, 0, 0, 0);
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                int mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
                int minHeight = DeviceUtil.getScreenHeight() - mActionBarSize * 2 + mFlexibleSpaceImageHeight;
                int bottom = minHeight - recyclerView.computeVerticalScrollRange();
                if (bottom > 0)
                    recyclerView.setPadding(0, 0, 0, bottom);
                else
                    recyclerView.setPadding(0, 0, 0, DeviceUtil.dpToPx(50));
            }
        }, 100);
        calendarDayTv.setText(String.valueOf(targets.size()));
    }

    @Override
    public void showStatistics(List<PlanHistoryDO> planHistoryDOs, List<TargetDO> targetDOs) {
        if (CollectionUtils.isEmpty(targetDOs)) {
            calendarRl.setVisibility(View.INVISIBLE);
        } else {
            calendarRl.setVisibility(View.VISIBLE);
        }
        if (CollectionUtils.isEmpty(planHistoryDOs)) {
            lineChart.setVisibility(View.INVISIBLE);
        } else {
            lineChart.setVisibility(View.VISIBLE);
            if (planHistoryDOs.size() < 7) {
                int count = 7 - planHistoryDOs.size();
                for (int i = 0; i < count; ++i) {
                    PlanHistoryDO planHistoryDO = new PlanHistoryDO();
                    planHistoryDOs.add(i, planHistoryDO);
                }
            }
            List<Entry> xyValues = new ArrayList<>();
            int i = 1;
            for (PlanHistoryDO planHistoryDO : planHistoryDOs) {
                xyValues.add(new Entry(i, planHistoryDO.getPunchCount()));
                i++;
            }
            LineChartManager.initSingleLineChart(getContext(), lineChart, xyValues);
        }
    }

    @Override
    public void createTargetSuccess(TargetDO targetDO) {
//        targetAdapter.addData(0, targetDO);
//        updateArcProgress(targetAdapter.getData().size());
        startActivity(new Intent(getActivity(), TargetDetailActivity.class).putExtra(ARG_TARGET, targetDO));
    }

    private MaterialDialog addTargetDialog;
    private MaterialEditText addTargetET;
    private RadioButton normalRB, punchRB;
    private TextView explainTV;
    private TextView deadlineTV;
    private View deadLineLL;
    private Long deadLine;

    @Override
    public void showAddTargetDialog() {
        addTargetDialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.add_target_title)
                .customView(R.layout.dialog_add_target, true)
                .positiveText(R.string.action_confirm)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        presenter.createTarget(addTargetET.getText().toString(), normalRB.isChecked() ? TargetDO.TYPE_NORMAL : TargetDO.TYPE_PUNCH, deadLine);
                        deadLine = null;
                    }
                })
                .negativeText(R.string.action_cancel)
                .build();
        addTargetDialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
        addTargetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        addTargetET = (MaterialEditText) addTargetDialog.findViewById(R.id.addTargetET);
        addTargetET.requestFocus();
        addTargetET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                addTargetDialog.getActionButton(DialogAction.POSITIVE).setEnabled(!TextUtils.isEmpty(s.toString().trim()));
            }
        });
        normalRB = (RadioButton) addTargetDialog.findViewById(R.id.normalRB);
        punchRB = (RadioButton) addTargetDialog.findViewById(R.id.punchRB);
        explainTV = (TextView) addTargetDialog.findViewById(R.id.explain);
        deadlineTV = (TextView) addTargetDialog.findViewById(R.id.deadlineTV);
        deadLineLL = addTargetDialog.findViewById(R.id.deadLineLL);
        explainTV.setText("说明：该目标下可任意添加多种类型任务");
        normalRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                punchRB.setChecked(!isChecked);
                if (isChecked)
                    explainTV.setText("说明：该目标下可任意添加多种类型任务");
            }
        });
        punchRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                normalRB.setChecked(!isChecked);
                if (isChecked)
                    explainTV.setText("说明：该目标下仅可进行打卡操作");
            }
        });
        deadLineLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTime now = DateTime.now();
                MonthAdapter.CalendarDay minDate = new MonthAdapter.CalendarDay(now.getYear(), now.getMonthOfYear() - 1, now.getDayOfMonth());
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener(TargetListFragment.this)
                        .setDoneText(getResources().getString(R.string.action_confirm))
                        .setCancelText(getResources().getString(R.string.action_cancel))
                        .setDateRange(minDate, null);
                if (DayNightModeUtils.isCurrentNight())
                    cdp.setThemeDark();
                else
                    cdp.setThemeLight();
                cdp.show(getChildFragmentManager(), FRAG_TAG_DATE_PICKER);
            }
        });
        addTargetDialog.show();
    }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        DateTime dateTime = new DateTime(year, monthOfYear + 1, dayOfMonth, 0, 0);
        deadlineTV.setText(getString(R.string.calendar_date_picker_result_values, year, monthOfYear + 1, dayOfMonth));
        deadLine = dateTime.getMillis();
    }

    private MaterialDialog addPunchDialog;
    private MaterialEditText addPunchET;
    private ImageView picIV;
    String picUri;

    private void openPunch(final String targetName) {
        addPunchDialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.add_punch_title)
                .widgetColorRes(R.color.colorAccent)
                .customView(R.layout.dialog_add_punch_targetdetail, true)
                .positiveText(R.string.action_confirm)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        presenter.savePunch(targetName, addPunchET.getText().toString(), picUri);
                    }
                })
                .negativeText(R.string.action_cancel)
                .build();
        addPunchDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        addPunchET = (MaterialEditText) addPunchDialog.findViewById(R.id.addPunchET);
        addPunchET.requestFocus();
        picIV = (ImageView) addPunchDialog.findViewById(R.id.picIV);
        picIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MediaChoseActivity.class);
                //chose_mode选择模式 0单选 1多选
                intent.putExtra("chose_mode", 0);
                //是否显示需要第一个是图片相机按钮
                intent.putExtra("isNeedfcamera", true);
                startActivityForResult(intent, REQUEST_PUNCH);
            }
        });
        addPunchDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && !CollectionUtils.isEmpty(data.getStringArrayListExtra("data"))) {
            ArrayList<String> uris = data.getStringArrayListExtra("data");
            picUri = uris.get(0);
            ImageUtils.loadImageFromFile(getActivity(), picIV, picUri, ImageView.ScaleType.CENTER_CROP);
        }
    }
}
