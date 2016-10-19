package me.xihuxiaolong.justdoit.module.editplan;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.joda.time.DateTime;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.xihuxiaolong.justdoit.R;
import me.xihuxiaolong.justdoit.common.base.BaseMvpActivity;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
import me.xihuxiaolong.justdoit.common.util.ActivityUtils;
import me.xihuxiaolong.justdoit.common.widget.StartAndEndTimeView;
import me.xihuxiaolong.library.utils.DialogUtil;
import me.xihuxiaolong.library.utils.ToastUtil;

public class EditPlanActivity extends BaseMvpActivity<EditPlanContract.IView, EditPlanContract.IPresenter> implements EditPlanContract.IView, StartAndEndTimeView.StartAndEndListener, CalendarDatePickerDialogFragment.OnDateSetListener, RadialTimePickerDialogFragment.OnTimeSetListener {

    public static final String ARGUMENT_EDIT_PLAN_ID = "EDIT_PLAN_ID";
    public static final String ARGUMENT_DAY_TIME = "DAY_TIME";
    private static final String FRAG_TAG_TIME_PICKER_START = "frag_tag_time_picker_start";
    private static final String FRAG_TAG_TIME_PICKER_END = "frag_tag_time_picker_end";

    EditPlanComponent editPlanComponent;

    private Menu menu;

    @Inject
    ToastUtil toastUtil;


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.backgroundIV)
    ImageView backgroundIV;
    @BindView(R.id.startAndEndTV)
    StartAndEndTimeView startAndEndTV;
    @BindView(R.id.contentET)
    MaterialEditText contentET;

    Long planId;

    long dayTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        planId = getIntent().getLongExtra(ARGUMENT_EDIT_PLAN_ID, -1L);
        dayTime = getIntent().getLongExtra(ARGUMENT_DAY_TIME, -1L);
        injectDependencies();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plan);
        ButterKnife.bind(this);

        setToolbar(toolbar, true);
        startAndEndTV.setStartAndEndListener(this);
        presenter.loadPlan();
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    protected void injectDependencies() {
        editPlanComponent = DaggerEditPlanComponent.builder().appComponent(ActivityUtils.getAppComponent(this))
                .editPlanModule(new EditPlanModule(planId, dayTime)).build();
    }

    @NonNull
    @Override
    public EditPlanContract.IPresenter createPresenter() {
        return editPlanComponent.presenter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_edit_plan, menu);
        menu.findItem(R.id.action_delete).setVisible(planId != -1L);
        if(planId == -1L){
            getSupportActionBar().setTitle(getResources().getString(R.string.add_plan));
        }else {
            getSupportActionBar().setTitle(getResources().getString(R.string.modify_plan));
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_delete:
                DialogUtil.showDialog(this, getResources().getString(R.string.delete_plan), "确定要删除本条计划吗？", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        presenter.deletePlan();
                    }
                });
                return true;
            case R.id.action_confirm:
                if(TextUtils.isEmpty(contentET.getText()))
                    toastUtil.showToast("不能保存一条空的计划", Toast.LENGTH_SHORT);
                else
                    presenter.savePlan(startAndEndTV.getStartHour(), startAndEndTV.getStartMinute(), startAndEndTV.getEndHour(), startAndEndTV.getEndMinute(), contentET.getText().toString());
                return true;
        }
        return false;
    }

    @Override
    public void startClick() {
        DateTime now = DateTime.now();
        RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                .setOnTimeSetListener(this)
                .setDoneText(getResources().getString(R.string.action_confirm))
                .setCancelText(getResources().getString(R.string.action_cancel))
                .setThemeDark();
        rtpd.setStartTime(startAndEndTV.getStartHour() != 0 ? startAndEndTV.getStartHour() : now.getHourOfDay(), startAndEndTV.getStartMinute() != 0 ? startAndEndTV.getStartMinute() : now.getMinuteOfHour());
        rtpd.show(getSupportFragmentManager(), FRAG_TAG_TIME_PICKER_START);
    }

    @Override
    public void endClick() {
        DateTime now = DateTime.now();

        RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                .setOnTimeSetListener(this)
                .setDoneText(getResources().getString(R.string.action_confirm))
                .setCancelText(getResources().getString(R.string.action_cancel))
                .setThemeDark();
        rtpd.setStartTime(startAndEndTV.getEndHour() != 0 ? startAndEndTV.getEndHour() : now.getHourOfDay(), startAndEndTV.getEndMinute() != 0 ? startAndEndTV.getEndMinute() : now.getMinuteOfHour());
        rtpd.show(getSupportFragmentManager(), FRAG_TAG_TIME_PICKER_END);
    }

    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {

    }

    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
        switch (dialog.getTag()){
            case FRAG_TAG_TIME_PICKER_START:
                startAndEndTV.setStartTime(hourOfDay, minute);
                if(hourOfDay > startAndEndTV.getEndHour()){
                    startAndEndTV.setEndTime(hourOfDay + 1, minute);
                }
                break;
            case FRAG_TAG_TIME_PICKER_END:
                startAndEndTV.setEndTime(hourOfDay, minute);
                break;
        }
    }

    @Override
    public void showPlan(PlanDO plan) {
        startAndEndTV.setStartTime(plan.getStartHour(), plan.getStartMinute());
        startAndEndTV.setEndTime(plan.getEndHour(), plan.getEndMinute());
        contentET.setText(plan.getContent());
    }

    @Override
    public void saveSuccess() {
        finish();
    }

    @Override
    public void deleteSuccess() {
        finish();
    }

    @Override
    public void sharePlan() {

    }

}
