package me.xihuxiaolong.justdoit.module.easybackloglist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.xihuxiaolong.justdoit.R;
import me.xihuxiaolong.justdoit.common.base.BaseMvpFragment;
import me.xihuxiaolong.justdoit.common.database.localentity.BacklogDO;
import me.xihuxiaolong.justdoit.common.database.localentity.BacklogDO;
import me.xihuxiaolong.justdoit.common.util.ProjectActivityUtils;
import me.xihuxiaolong.justdoit.module.adapter.BacklogListAdapter;
import me.xihuxiaolong.justdoit.module.editalert.EditAlertActivity;
import me.xihuxiaolong.justdoit.module.homepage.HomePageFragment;
import me.xihuxiaolong.justdoit.module.main.ScrollListener;
import me.xihuxiaolong.library.utils.DialogUtils;


/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/7/5.
 */
public class EasyBacklogListFragment extends BaseMvpFragment<EasyBacklogListContract.IView, EasyBacklogListContract.IPresenter> implements EasyBacklogListContract.IView, ObservableScrollViewCallbacks, BacklogListAdapter.BacklogListOnClickListener {

    EasyBacklogListComponent easyBacklogListComponent;

    @BindView(R.id.recyclerView)
    ObservableRecyclerView recyclerView;

    int mScollY;

    BacklogListAdapter backlogListAdapter;

    long dayTime;

    ScrollListener scrollListener;

    public static EasyBacklogListFragment newInstance(Long dayTime) {
        EasyBacklogListFragment fragment = new EasyBacklogListFragment();
        Bundle args = new Bundle();
        if(dayTime != null)
            args.putLong("dayTime", dayTime);
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

    public void onAttachToParentFragment(Fragment fragment) {
        if (fragment instanceof ScrollListener) {
            scrollListener = ((ScrollListener) fragment);
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
        recyclerView.setTouchInterceptionViewGroup(((HomePageFragment)getParentFragment()).getInterView());
        recyclerView.setScrollViewCallbacks(this);
        return view;
    }

    void createBacklogList(){
        backlogListAdapter = new BacklogListAdapter(getActivity(), R.layout.item_card_backlog, new ArrayList<BacklogDO>(), this);
        backlogListAdapter.setEmptyView(LayoutInflater.from(getActivity()).inflate(R.layout.empty_view_backloglist, (ViewGroup) recyclerView.getParent(), false));
        backlogListAdapter.setHeaderFooterEmpty(true, true);
        final View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.item_backlog_header, recyclerView, false);
        backlogListAdapter.addHeaderView(headerView);
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
        if(backlogListAdapter == null)
            createBacklogList();
        recyclerView.setAdapter(backlogListAdapter);
        backlogListAdapter.setNewData(backlogDOs);
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
}
