package me.xihuxiaolong.justdoit.module.targetlist;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.graphics.Target;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.github.clans.fab.FloatingActionButton;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.github.lzyzsd.circleprogress.ArcProgress;
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
import me.grantland.widget.AutofitTextView;
import me.xihuxiaolong.justdoit.R;
import me.xihuxiaolong.justdoit.common.base.BaseMvpFragment;
import me.xihuxiaolong.justdoit.common.database.localentity.RedoPlanDO;
import me.xihuxiaolong.justdoit.common.database.localentity.TargetDO;
import me.xihuxiaolong.justdoit.common.util.BusinessUtils;
import me.xihuxiaolong.justdoit.common.util.ImageUtils;
import me.xihuxiaolong.justdoit.common.util.ProjectActivityUtils;
import me.xihuxiaolong.justdoit.common.widget.LineChartManager;
import me.xihuxiaolong.justdoit.module.main.MainActivityListener;
import me.xihuxiaolong.justdoit.module.main.ScrollListener;
import me.xihuxiaolong.justdoit.module.settings.SettingsActivity;
import me.xihuxiaolong.justdoit.module.targetdetail.TargetDetailActivity;
import me.xihuxiaolong.library.utils.CollectionUtils;

import static me.xihuxiaolong.justdoit.module.targetdetail.TargetDetailActivity.ARG_TARGET;


/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/7/5.
 */
public class TargetListFragment extends BaseMvpFragment<TargetListContract.IView, TargetListContract.IPresenter> implements TargetListContract.IView, ObservableScrollViewCallbacks, MainActivityListener {

    private static final float MAX_TEXT_SCALE_DELTA = 1.3f;
    private static final int SELECT_TEMPLATE_REQUEST = 1;

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
    @BindView(R.id.arc_progress)
    ArcProgress arcProgress;

    private int mFlexibleSpaceImageHeight, mFlexibleRecyclerOffset, mFlexibleSpaceShowFabOffset, mFlexibleSpaceCalendarBottomOffset, mFlexibleSpaceCalendarLeftOffset,
            mFlexibleSpaceSignatureBottomOffset, mFabSizeNormal;
    private int mActionBarSize, mStatusBarSize;

    boolean mFabIsShown = true;

    MenuItem addMenuItem;

    Drawable shadow;

    int vibrant, darkVibrant;

    int mScollY;

    TargetAdapter targetAdapter;

    ScrollListener scrollListener;

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
        if (activity instanceof ScrollListener) {
            scrollListener = ((ScrollListener) activity);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.loadTargets();
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

        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mFlexibleRecyclerOffset = getResources().getDimensionPixelSize(R.dimen.flexible_recyclerview_header_height);
        mFlexibleSpaceShowFabOffset = getResources().getDimensionPixelSize(R.dimen.flexible_space_show_fab_offset);
        mFlexibleSpaceCalendarBottomOffset = getResources().getDimensionPixelSize(R.dimen.flexible_space_arcprogress_bottom_offset);
        mFlexibleSpaceCalendarLeftOffset = getResources().getDimensionPixelSize(R.dimen.flexible_space_calendar_left_offset);
        mFlexibleSpaceSignatureBottomOffset = getResources().getDimensionPixelSize(R.dimen.flexible_space_signature_bottom_offset);
        shadow = ContextCompat.getDrawable(getContext(), R.drawable.bottom_shadow);
        mFabSizeNormal = getResources().getDimensionPixelSize(R.dimen.fab_menu_size_normal);

        ViewGroup.LayoutParams layoutParams = toolbar.getLayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mStatusBarSize = getStatusBarHeight();
        }
        mActionBarSize = layoutParams.height - mStatusBarSize;

        vibrant = ContextCompat.getColor(getContext(), R.color.sky);
        darkVibrant = ContextCompat.getColor(getContext(), R.color.dark_sky);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        targetAdapter = new TargetAdapter(R.layout.item_target, new ArrayList<TargetDO>());
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
        });
        recyclerView.setScrollViewCallbacks(this);

        fab.setOnClickListener(fabListener);

        return view;
    }

    class TargetAdapter extends BaseQuickAdapter<TargetDO, TargetAdapter.TargetViewHolder> {

        int showRedoPlanCount = 3;

        public TargetAdapter(int layoutId, List<TargetDO> datas) {
            super(layoutId, datas);
        }

        @Override
        protected void convert(TargetViewHolder holder, final TargetDO targetDO) {
            holder.setText(R.id.title, targetDO.getName());
            ImageView targetIconIV = holder.getView(R.id.targetIconIV);
            targetIconIV.setAlpha(0.65f);
            ImageUtils.loadImageFromFile(getContext(), (ImageView) holder.getView(R.id.bgIV), targetDO.getHeaderImageUri(), ImageView.ScaleType.CENTER_CROP);
            LinearLayout linearLayout = holder.getView(R.id.redoPlanLL);
            if (linearLayout.getChildCount() <= 0) {
                for (int i = 0; i < showRedoPlanCount; i++) {
                    View redoView = LayoutInflater.from(getContext()).inflate(R.layout.item_redo_plan, linearLayout, false);
                    holder.addRedoPlanItem(new BaseViewHolder(redoView));
                    linearLayout.addView(redoView);
                }
            }
            int redoSize = CollectionUtils.isEmpty(targetDO.getRedoPlanDOList()) ? 0 : targetDO.getRedoPlanDOList().size();
            for (int i = 0; i < showRedoPlanCount; i++) {
                BaseViewHolder redoPlanViewHolder = holder.redoPlanItems.get(i);
                if (i < redoSize) {
                    redoPlanViewHolder.setVisible(R.id.redoLL, true);
                    RedoPlanDO redoPlanDO = targetDO.getRedoPlanDOList().get(i);
                    redoPlanViewHolder.setText(R.id.redoTitleTV, redoPlanDO.getContent());
                    redoPlanViewHolder.setText(R.id.redoModeTV, BusinessUtils.repeatModeStr(redoPlanDO.getRepeatMode()));
                    redoPlanViewHolder.setText(R.id.lastTimeTV, "已持续 " + Days.daysBetween(DateTime.now(), new DateTime(redoPlanDO.getCreatedTime())).getDays() + " 天");
                } else {
                    redoPlanViewHolder.setVisible(R.id.redoLL, false);
                }
            }
        }

        @Override
        protected TargetViewHolder createBaseViewHolder(View view) {
            return new TargetViewHolder(view);
        }

        public class TargetViewHolder extends BaseViewHolder {

            public List<BaseViewHolder> redoPlanItems = new ArrayList<>();

            public TargetViewHolder(View view) {
                super(view);
            }

            public void addRedoPlanItem(BaseViewHolder redoPlanViewHolder) {
                redoPlanItems.add(redoPlanViewHolder);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == SELECT_TEMPLATE_REQUEST) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {

            }
        }
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        if (scrollListener != null)
            scrollListener.onScrollChanged(scrollY, firstScroll, dragging);
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

        // Translate signature text
        int maxSignatureTranslationY = mFlexibleSpaceImageHeight - lineChart.getMeasuredHeight() - mFlexibleSpaceSignatureBottomOffset;
        int signatureTranslationY = maxSignatureTranslationY - scrollY;
        ViewHelper.setTranslationY(lineChart, signatureTranslationY);
        float alpha = Math.min(1, (float) (mFlexibleSpaceImageHeight - (scrollY * 1.4)) / mFlexibleSpaceImageHeight);
        ViewHelper.setAlpha(lineChart, alpha);

        // Translate toolbar
        float alpha1 = Math.min(1, (float) scrollY / (mFlexibleRecyclerOffset - 20));
        toolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha1, vibrant));

        // Translate toolbar
        if (mFlexibleSpaceImageHeight - mActionBarSize < scrollY)
            shadowFrame.setForeground(shadow);
        else
            shadowFrame.setForeground(null);

    }

    // Scale calendarRl
    void animateCanlendarRl(int scrollY) {
        float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
        float scale = 1 + ScrollUtils.getFloat((flexibleRange - scrollY) * 1.3f / flexibleRange, 0, MAX_TEXT_SCALE_DELTA);
        ViewHelper.setPivotX(calendarRl, 0);
        ViewHelper.setPivotY(calendarRl, 0);
        ViewHelper.setScaleX(calendarRl, scale);
        ViewHelper.setScaleY(calendarRl, scale);

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
        if (CollectionUtils.isEmpty(targets)) {
//            fab.setVisibility(View.INVISIBLE);
            calendarRl.setVisibility(View.INVISIBLE);
            lineChart.setVisibility(View.INVISIBLE);
        } else {
//            fab.setVisibility(View.VISIBLE);
            calendarRl.setVisibility(View.VISIBLE);
            lineChart.setVisibility(View.VISIBLE);
            List<Entry> xyValues = new ArrayList<>();
            xyValues.add(new Entry(1, 4));
            xyValues.add(new Entry(2, 2));
            xyValues.add(new Entry(3, 3));
            xyValues.add(new Entry(4, 2));
            xyValues.add(new Entry(5, 5));
            xyValues.add(new Entry(6, 5));
            xyValues.add(new Entry(7, 5));
            LineChartManager.initSingleLineChart(getContext(), lineChart, xyValues);
        }
        updateArcProgress(targets.size());
        targetAdapter.setNewData(targets);
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

    @Override
    public void showAddTargetDialog() {
        addTargetDialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.add_target_title)
                .customView(R.layout.dialog_add_target, true)
                .positiveText(R.string.action_confirm)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        presenter.createTarget(addTargetET.getText().toString(), normalRB.isChecked() ? TargetDO.TYPE_NORMAL : TargetDO.TYPE_PUNCH);
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
        explainTV.setText("说明：在该目标模式下可添加重复计划/提醒");
        normalRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                punchRB.setChecked(!isChecked);
                if (isChecked)
                    explainTV.setText("说明：在该目标模式下可添加重复计划/提醒");
            }
        });
        punchRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                normalRB.setChecked(!isChecked);
                if (isChecked)
                    explainTV.setText("说明：在该目标模式下可进行打卡操作");
            }
        });
        addTargetDialog.show();
    }

    private void updateArcProgress(int count) {
        arcProgress.setBottomText(count + " 目标 ");
    }
}
