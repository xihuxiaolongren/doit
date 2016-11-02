package me.xihuxiaolong.justdoit.module.otherdayplanlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.xihuxiaolong.justdoit.R;
import me.xihuxiaolong.justdoit.common.base.BaseMvpFragment;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
import me.xihuxiaolong.justdoit.common.util.ActivityUtils;
import me.xihuxiaolong.justdoit.module.adapter.PlanListWrapper;
import me.xihuxiaolong.justdoit.module.editalert.EditAlertActivity;
import me.xihuxiaolong.justdoit.module.editplan.EditPlanActivity;
import me.xihuxiaolong.justdoit.module.planhistory.DaggerPlanHistoryComponent;
import me.xihuxiaolong.justdoit.module.planhistory.PlanHistoryComponent;
import me.xihuxiaolong.justdoit.module.planhistory.PlanHistoryContract;
import me.xihuxiaolong.justdoit.module.planhistory.PlanHistoryFragment;
import me.xihuxiaolong.justdoit.module.planhistory.PlanHistoryModule;


/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/7/5.
 */
public class OtherDayPlanListFragment extends BaseMvpFragment<OtherDayPlanListContract.IView, OtherDayPlanListContract.IPresenter> implements OtherDayPlanListContract.IView{

    long dayTime;

    OtherDayPlanListComponent otherDayPlanListComponent;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


//    PlanListAdapter adapter;
//    EmptyWrapper mEmptyWrapper;
//    HeaderAndFooterWrapper mHeaderAndFooterWrapper;

    PlanListWrapper planListWrapper;

    public static OtherDayPlanListFragment newInstance(long dayTime) {
        OtherDayPlanListFragment fragment = new OtherDayPlanListFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("dayTime", dayTime);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public OtherDayPlanListContract.IPresenter createPresenter() {
        return otherDayPlanListComponent.presenter();
    }

    @Override
    protected void injectDependencies() {
        otherDayPlanListComponent = DaggerOtherDayPlanListComponent.builder()
                .appComponent(ActivityUtils.getAppComponent(getActivity()))
                .otherDayPlanListModule(new OtherDayPlanListModule())
                .build();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter.loadPlans();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        dayTime = getArguments().getLong("dayTime");
        injectDependencies();
        View view = inflater.inflate(R.layout.fragment_plan_history, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        planListWrapper = new PlanListWrapper(getContext(), recyclerView);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_frament_plan_history, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
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
    public void showSignature(String signature, String sig) {

    }

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

}
