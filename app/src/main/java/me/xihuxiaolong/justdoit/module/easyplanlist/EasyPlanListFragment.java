package me.xihuxiaolong.justdoit.module.easyplanlist;

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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.github.ksoichiro.android.observablescrollview.Scrollable;
import com.google.android.flexbox.FlexboxLayout;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.orhanobut.logger.Logger;
import com.rengwuxian.materialedittext.MaterialEditText;

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
import me.xihuxiaolong.justdoit.common.util.DayNightModeUtils;
import me.xihuxiaolong.justdoit.common.util.ImageUtils;
import me.xihuxiaolong.justdoit.common.util.ProjectActivityUtils;
import me.xihuxiaolong.justdoit.module.adapter.BacklogListAdapter;
import me.xihuxiaolong.justdoit.module.adapter.PlanListAdapter;
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
public class EasyPlanListFragment extends BaseMvpFragment<EasyPlanListContract.IView, EasyPlanListContract.IPresenter> implements EasyPlanListContract.IView, ObservableScrollViewCallbacks, PlanListAdapter.PlanListOnClickListener {

    EasyPlanListComponent easyPlanListComponent;

    @BindView(R.id.recyclerView)
    ObservableRecyclerView recyclerView;

    int mScollY;

    PlanListAdapter planListAdapter;

    long dayTime;

    ScrollListener scrollListener;

    public static EasyPlanListFragment newInstance(Long dayTime) {
        EasyPlanListFragment fragment = new EasyPlanListFragment();
        Bundle args = new Bundle();
        if(dayTime != null)
            args.putLong("dayTime", dayTime);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public EasyPlanListContract.IPresenter createPresenter() {
        return easyPlanListComponent.presenter();
    }

    @Override
    protected void injectDependencies() {
        easyPlanListComponent = DaggerEasyPlanListComponent.builder()
                .appComponent(ProjectActivityUtils.getAppComponent(getActivity()))
                .easyPlanListModule(new EasyPlanListModule(dayTime))
                .build();
    }

    public void onAttachToParentFragment(Fragment fragment) {
        if (fragment instanceof ScrollListener) {
            scrollListener = ((ScrollListener) fragment);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.loadPlans();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onAttachToParentFragment(getParentFragment());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        dayTime = getArguments().getLong("dayTime", -1L);
        injectDependencies();
        View view = inflater.inflate(R.layout.fragment_easy_plan_list, container, false);
        ButterKnife.bind(this, view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setScrollViewCallbacks(this);
        return view;
    }

    void createPlanList(){
        planListAdapter = new PlanListAdapter(getActivity(), new ArrayList<PlanDO>(), this);
        planListAdapter.setEmptyView(LayoutInflater.from(getActivity()).inflate(R.layout.empty_view_planlist, (ViewGroup) recyclerView.getParent(), false));
        planListAdapter.setHeaderFooterEmpty(true, true);
        final View footView = LayoutInflater.from(getActivity()).inflate(R.layout.item_plan_bottom, recyclerView, false);
        planListAdapter.addFooterView(footView);
        final View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.item_plan_header, recyclerView, false);
        planListAdapter.addHeaderView(headerView);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
        if(planListAdapter == null)
            createPlanList();
        recyclerView.setAdapter(planListAdapter);
        planListAdapter.setNewData(plans);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    public void setScrollY(int scrollY, int threshold) {
        if(scrollY == mScollY)
            return;
        if (recyclerView == null) {
            return;
        }
        mScollY = scrollY;
        View firstVisibleChild = recyclerView.getChildAt(0);
        if (firstVisibleChild != null) {
            int offset = scrollY;
            int position = 0;
            if (threshold < scrollY) {
                int baseHeight = firstVisibleChild.getHeight();
                position = scrollY / baseHeight;
                offset = scrollY % baseHeight;
            }
            RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
            if (lm != null && lm instanceof LinearLayoutManager) {
                ((LinearLayoutManager) lm).scrollToPositionWithOffset(position, -offset);
            }
        }
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        if(scrollListener != null)
            scrollListener.onScrollChanged(scrollY, firstScroll, dragging);
        mScollY = scrollY;
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

    @Override
    public void planClick(PlanDO planDO) {
        startActivity(new Intent(getActivity(), EditPlanActivity.class).putExtra(EditPlanActivity.ARGUMENT_EDIT_PLAN_ID, planDO.getId()));
    }

    @Override
    public void alertClick(PlanDO planDO) {
        startActivity(new Intent(getActivity(), EditAlertActivity.class).putExtra(EditAlertActivity.ARGUMENT_EDIT_ALERT_ID, planDO.getId()));
    }

    @Override
    public void deleteClick(final PlanDO planDO) {
        DialogUtils.showDialog(getContext(), getResources().getString(R.string.delete_all_type), "确定要删除本条记录吗？", new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                presenter.deletePlan(planDO);
            }
        });
    }

    @Override
    public void shareClick(PlanDO planDO) {
        presenter.sharePlan(planDO);
    }

}
