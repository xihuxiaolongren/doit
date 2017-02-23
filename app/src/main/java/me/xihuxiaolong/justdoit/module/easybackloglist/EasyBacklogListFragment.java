package me.xihuxiaolong.justdoit.module.easybackloglist;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionButton;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.orhanobut.logger.Logger;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.xihuxiaolong.justdoit.R;
import me.xihuxiaolong.justdoit.common.base.BaseMvpFragment;
import me.xihuxiaolong.justdoit.common.database.localentity.BacklogDO;
import me.xihuxiaolong.justdoit.common.event.Event;
import me.xihuxiaolong.justdoit.common.util.DeviceUtil;
import me.xihuxiaolong.justdoit.common.util.ProjectActivityUtils;
import me.xihuxiaolong.justdoit.module.adapter.BacklogListAdapter;
import me.xihuxiaolong.justdoit.module.main.MainActivityFragmentListener;
import me.xihuxiaolong.justdoit.module.main.MainActivityListener;
import me.xihuxiaolong.library.utils.ActivityUtils;
import me.xihuxiaolong.library.utils.DialogUtils;


/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/7/5.
 */
public class EasyBacklogListFragment extends BaseMvpFragment<EasyBacklogListContract.IView, EasyBacklogListContract.IPresenter> implements EasyBacklogListContract.IView, MainActivityFragmentListener, ObservableScrollViewCallbacks, BacklogListAdapter.BacklogListOnClickListener {

    private static final float MAX_TEXT_SCALE_DELTA = 0.5f;

    EasyBacklogListComponent easyBacklogListComponent;

    @BindView(R.id.recyclerView)
    ObservableRecyclerView recyclerView;

    int mScollY;

    int vibrant;

    Drawable shadow;

    BacklogListAdapter backlogListAdapter;

    long dayTime;

    MainActivityListener mainActivityListener;
    @BindView(R.id.headerIV)
    ImageView headerIV;
    @BindView(R.id.recycler_background)
    View recyclerBackground;
    @BindView(R.id.shadowFrame)
    FrameLayout shadowFrame;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.calendar_day_tv)
    TextView calendarDayTv;
//    @BindView(R.id.calendar_week_tv)
//    TextView calendarWeekTv;
//    @BindView(R.id.calendar_month_year_tv)
//    TextView calendarMonthYearTv;
    @BindView(R.id.calendar_rl)
    LinearLayout calendarRl;

    private int mFlexibleSpaceImageHeight, mFlexibleRecyclerOffset, mFlexibleSpaceShowFabOffset, mFlexibleSpaceCalendarBottomOffset, mFlexibleSpaceCalendarLeftOffset,
            mFlexibleSpaceSignatureBottomOffset, mFabSizeNormal;
    private int mActionBarSize, mStatusBarSize;

    boolean mFabIsShown = true;

    public static EasyBacklogListFragment newInstance() {
        EasyBacklogListFragment fragment = new EasyBacklogListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public EasyBacklogListContract.IPresenter createPresenter() {
        return easyBacklogListComponent.presenter();
    }

    @Override
    protected void injectDependencies() {
        easyBacklogListComponent = DaggerEasyBacklogListComponent.builder()
                .appComponent(ProjectActivityUtils.getAppComponent(getActivity()))
                .easyBacklogListModule(new EasyBacklogListModule(dayTime))
                .build();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof MainActivityListener){
            mainActivityListener = ((MainActivityListener)activity);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.loadBacklogs();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        dayTime = getArguments().getLong("dayTime", -1L);
        injectDependencies();
        View view = inflater.inflate(R.layout.fragment_easy_plan_list, container, false);
        ButterKnife.bind(this, view);
        initToolbar(toolbar, false);
        setHasOptionsMenu(true);


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

        EventBus.getDefault().register(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setScrollViewCallbacks(this);
        fab.setOnClickListener(fabListener);
        return view;
    }

    void createBacklogList() {
        backlogListAdapter = new BacklogListAdapter(getActivity(), R.layout.item_card_backlog, new ArrayList<BacklogDO>(), this);
        backlogListAdapter.setEmptyView(LayoutInflater.from(getActivity()).inflate(R.layout.empty_view_backloglist, (ViewGroup) recyclerView.getParent(), false));
        backlogListAdapter.setHeaderFooterEmpty(true, true);
        final View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.item_backlog_header, recyclerView, false);
        backlogListAdapter.addHeaderView(headerView);
        recyclerView.setAdapter(backlogListAdapter);
    }

    private View.OnClickListener fabListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showBacklogDialog();
        }
    };

    private MaterialDialog addBacklogDialog;
    MaterialEditText backlogET;
    View backlogPositive;

    public void showBacklogDialog(){
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void removeBacklogItem(long backlogId) {

    }

    @Override
    public void addBacklogItem(BacklogDO backlogDO) {

    }

    @Override
    public void updateBacklogItem(BacklogDO backlogDO) {

    }

    @Override
    public void showBacklogs(final List<BacklogDO> backlogDOs) {
        if (backlogListAdapter == null)
            createBacklogList();
        backlogListAdapter.setNewData(backlogDOs);
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                int mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
                int minHeight = DeviceUtil.getScreenHeight() - mActionBarSize * 2 + mFlexibleSpaceImageHeight;
                int bottom = minHeight - recyclerView.computeVerticalScrollRange();
                if (bottom > 0)
                    recyclerView.setPadding(0, 0, 0, bottom);
                else
                    recyclerView.setPadding(0, 0, 0, 50);
            }
        }, 100);

        calendarDayTv.setText(String.valueOf(backlogDOs.size()));
        if (mainActivityListener != null)
            mainActivityListener.setBottomBarBadge(1, backlogDOs.size());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    public void setScrollY(int scrollY, int threshold) {
//        if (scrollY == mScollY)
//            return;
//        if (recyclerView == null) {
//            return;
//        }
//        mScollY = scrollY;
//        Logger.e("backlog" + mScollY);
//        View firstVisibleChild = recyclerView.getChildAt(0);
//        if (firstVisibleChild != null) {
//            int offset = scrollY;
//            int position = 0;
//            if (threshold < scrollY) {
//                int baseHeight = firstVisibleChild.getHeight();
//                position = scrollY / baseHeight;
//                offset = scrollY % baseHeight;
//            }
//            RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
//            if (lm != null && lm instanceof LinearLayoutManager) {
//                ((LinearLayoutManager) lm).scrollToPositionWithOffset(position, -offset);
//            }
//        }
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        Logger.e("backlog" + scrollY + "  " + firstScroll + "  " + dragging);
        if (mainActivityListener != null)
            mainActivityListener.onScrollChanged(scrollY, 0);
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

        // Translate imageView parallax
        ViewHelper.setTranslationY(headerIV, -scrollY / 2);
        ViewHelper.setTranslationY(recyclerBackground, Math.max(0, -scrollY + mFlexibleSpaceImageHeight));

        animateCanlendarRl(scrollY);

        // Translate toolbar
        float alpha1 = Math.min(1, (float) scrollY / (mFlexibleRecyclerOffset - 20));
        toolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha1, vibrant));

        // Translate toolbar
        if (mFlexibleSpaceImageHeight - mActionBarSize < scrollY)
            shadowFrame.setForeground(shadow);
        else
            shadowFrame.setForeground(null);

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
    public final void onDownMotionEvent() {
        // We don't use this callback in this pattern.
    }

    @Override
    public final void onUpOrCancelMotionEvent(ScrollState scrollState) {
        // We don't use this callback in this pattern.
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        //TODO 判断当前页面的日期是否等于当前日期，不是则重新加载当前页面
    }

    public void deleteClick(final BacklogDO backlogDO) {
        DialogUtils.showDialog(getContext(), getResources().getString(R.string.delete_all_type), "确定要删除本条记录吗？", new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                presenter.deleteBacklog(backlogDO);
            }
        });
    }

    public void shareClick(BacklogDO backlogDO) {
        presenter.shareBacklog(backlogDO);
    }

    @Override
    public void backlogClick(BacklogDO backlogDO) {

    }

    @Subscribe
    public void onEvent(Event.PlanListScroll planListScroll) {
        setScrollY(planListScroll.scrollY, mFlexibleSpaceImageHeight);
    }

    @Override
    public void reloadToolbar() {
        if (toolbar != null) {
            Logger.d(toolbar);
            setToolbar(toolbar, false);
        }
    }
}
