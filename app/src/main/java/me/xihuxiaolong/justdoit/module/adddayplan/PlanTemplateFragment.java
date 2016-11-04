package me.xihuxiaolong.justdoit.module.adddayplan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.xihuxiaolong.justdoit.R;
import me.xihuxiaolong.justdoit.common.base.BaseMvpFragment;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
import me.xihuxiaolong.justdoit.common.util.ProjectActivityUtils;
import me.xihuxiaolong.justdoit.module.adapter.PlanListWrapper;
import me.xihuxiaolong.justdoit.module.editalert.EditAlertActivity;
import me.xihuxiaolong.justdoit.module.editplan.EditPlanActivity;


/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/7/5.
 */
public class PlanTemplateFragment extends BaseMvpFragment<PlanTemplateContract.IView, PlanTemplateContract.IPresenter> implements PlanTemplateContract.IView{

    long dayTime;

    PlanTemplateComponent planTemplateComponent;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    PlanListWrapper planListWrapper;

    public static PlanTemplateFragment newInstance(long dayTime) {
        PlanTemplateFragment fragment = new PlanTemplateFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("dayTime", dayTime);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public PlanTemplateContract.IPresenter createPresenter() {
        return planTemplateComponent.presenter();
    }

    @Override
    protected void injectDependencies() {
        planTemplateComponent = DaggerPlanTemplateComponent.builder()
                .appComponent(ProjectActivityUtils.getAppComponent(getActivity()))
                .planTemplateModule(new PlanTemplateModule(dayTime))
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
        View view = inflater.inflate(R.layout.fragment_plan_template, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        planListWrapper = new PlanListWrapper(getContext(), recyclerView);
        final View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.item_plan_template_header, recyclerView, false);
        planListWrapper.addHeaderView(headerView);

        return view;
    }

    @Override
    public void showPlans(final List<PlanDO> plans) {
        planListWrapper.setItems(plans);
    }

    @Override
    public void showSignature(String signature) {

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
