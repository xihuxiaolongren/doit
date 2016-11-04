package me.xihuxiaolong.justdoit.module.editalert;

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

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.xihuxiaolong.justdoit.R;
import me.xihuxiaolong.justdoit.common.base.BaseMvpActivity;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
import me.xihuxiaolong.justdoit.common.util.ProjectActivityUtils;
import me.xihuxiaolong.justdoit.common.util.DayNightModeUtils;
import me.xihuxiaolong.justdoit.common.widget.SingleTimeView;
import me.xihuxiaolong.library.utils.DialogUtils;
import me.xihuxiaolong.library.utils.ToastUtil;

public class EditAlertActivity extends BaseMvpActivity<EditAlertContract.IView, EditAlertContract.IPresenter> implements EditAlertContract.IView, SingleTimeView.TimeListener, CalendarDatePickerDialogFragment.OnDateSetListener, RadialTimePickerDialogFragment.OnTimeSetListener {

    public static final String ARGUMENT_EDIT_ALERT_ID = "EDIT_ALERT_ID";
    public static final String ARGUMENT_DAY_TIME = "DAY_TIME";
    private static final String FRAG_TAG_TIME_PICKER = "frag_tag_time_picker";

    EditAlertComponent editAlertComponent;

    private Menu menu;

    @Inject
    ToastUtil toastUtil;


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.backgroundIV)
    ImageView backgroundIV;
    @BindView(R.id.singleTimeView)
    SingleTimeView singleTimeView;
    @BindView(R.id.contentET)
    MaterialEditText contentET;

    Long alertId;

    long dayTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        alertId = getIntent().getLongExtra(ARGUMENT_EDIT_ALERT_ID, -1L);
        dayTime = getIntent().getLongExtra(ARGUMENT_DAY_TIME, -1L);
        injectDependencies();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alert);
        ButterKnife.bind(this);

        setToolbar(toolbar, true);
        singleTimeView.setTimeListener(this);
        presenter.loadAlert();
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    protected void injectDependencies() {
        editAlertComponent = DaggerEditAlertComponent.builder().appComponent(ProjectActivityUtils.getAppComponent(this))
                .editAlertModule(new EditAlertModule(alertId,dayTime)).build();
    }

    @NonNull
    @Override
    public EditAlertContract.IPresenter createPresenter() {
        return editAlertComponent.presenter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_edit_alert, menu);
        menu.findItem(R.id.action_delete).setVisible(alertId != -1L);
        if(alertId == -1L){
            getSupportActionBar().setTitle(getResources().getString(R.string.add_alert));
        }else {
            getSupportActionBar().setTitle(getResources().getString(R.string.modify_alert));
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
                DialogUtils.showDialog(this, getResources().getString(R.string.delete_alert), "确定要删除本条提醒吗？", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        presenter.deleteAlert();
                    }
                });
                return true;
            case R.id.action_confirm:
                if(TextUtils.isEmpty(contentET.getText()))
                    toastUtil.showToast("不能保存一条空的提醒", Toast.LENGTH_SHORT);
                else
                    presenter.saveAlert(singleTimeView.getHour(), singleTimeView.getMinute(), contentET.getText().toString());
                return true;
        }
        return false;
    }

    @Override
    public void timeClick() {
        RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                .setOnTimeSetListener(this)
                .setTitleText(getResources().getString(R.string.alert_time))
                .setDoneText(getResources().getString(R.string.action_confirm))
                .setCancelText(getResources().getString(R.string.action_cancel));
        if(DayNightModeUtils.isCurrentNight())
            rtpd.setThemeDark();
        else
            rtpd.setThemeLight();
        rtpd.setStartTime(singleTimeView.getHour(), singleTimeView.getMinute());
        rtpd.show(getSupportFragmentManager(), FRAG_TAG_TIME_PICKER);
    }

    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {

    }

    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
        singleTimeView.setTime(hourOfDay, minute);
    }

    @Override
    public void showAlert(PlanDO alert) {
        singleTimeView.setTime(alert.getStartHour(), alert.getStartMinute());
        contentET.setText(alert.getContent());
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
    public void shareAlert() {

    }

}
