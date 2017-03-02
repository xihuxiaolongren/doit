package me.xihuxiaolong.justdoit.module.editalert;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.xihuxiaolong.justdoit.R;
import me.xihuxiaolong.justdoit.common.base.BaseMvpActivity;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
import me.xihuxiaolong.justdoit.common.util.DayNightModeUtils;
import me.xihuxiaolong.justdoit.common.util.ProjectActivityUtils;
import me.xihuxiaolong.justdoit.common.widget.SingleTimeView;
import me.xihuxiaolong.library.utils.ActivityUtils;
import me.xihuxiaolong.library.utils.DialogUtils;

public class EditAlertActivity extends BaseMvpActivity<EditAlertContract.IView, EditAlertContract.IPresenter> implements EditAlertContract.IView, SingleTimeView.TimeListener, CalendarDatePickerDialogFragment.OnDateSetListener, RadialTimePickerDialogFragment.OnTimeSetListener {

    public static final String ARGUMENT_EDIT_ALERT_ID = "EDIT_ALERT_ID";
    public static final String ARGUMENT_DAY_TIME = "DAY_TIME";
    public static final String ARGUMENT_TARGET_NAME = "TARGET_NAME";
    private static final String FRAG_TAG_TIME_PICKER = "frag_tag_time_picker";

    EditAlertComponent editAlertComponent;

    Long alertId;

    long dayTime;

    String targetName;

    int repeatSelected = -1;
    Integer[] repeatSelectedArr;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.backgroundIV)
    ImageView backgroundIV;
    @BindView(R.id.singleTimeView)
    SingleTimeView singleTimeView;
    @BindView(R.id.contentET)
    MaterialEditText contentET;
    @BindView(R.id.repeatIV)
    ImageView repeatIV;
    @BindView(R.id.repeatTV)
    TextView repeatTV;
    @BindView(R.id.repeatDetailTV)
    TextView repeatDetailTV;
    @BindView(R.id.repeatRL)
    RelativeLayout repeatRL;
    @BindView(R.id.rootView)
    RelativeLayout rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        alertId = getIntent().getLongExtra(ARGUMENT_EDIT_ALERT_ID, -1L);
        dayTime = getIntent().getLongExtra(ARGUMENT_DAY_TIME, -1L);
        targetName = getIntent().getStringExtra(ARGUMENT_TARGET_NAME);
        injectDependencies();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alert);
        ButterKnife.bind(this);

        setToolbar(toolbar, true);
        contentET.requestFocus();
        singleTimeView.setTimeListener(this);
        if(alertId != -1L)
            repeatRL.setVisibility(View.GONE);
        repeatRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRepeatDialog();
            }
        });
        presenter.loadAlert();
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    protected void injectDependencies() {
        editAlertComponent = DaggerEditAlertComponent.builder().appComponent(ProjectActivityUtils.getAppComponent(this))
                .editAlertModule(new EditAlertModule(alertId, dayTime, targetName)).build();
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
        if (alertId == -1L) {
            getSupportActionBar().setTitle(getResources().getString(R.string.add_alert));
        } else {
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
                ActivityUtils.hideSoftKeyboard(this);
                if (TextUtils.isEmpty(contentET.getText()))
                    contentET.setError("提醒内容不能为空");
                else if (!TextUtils.isEmpty(targetName) && repeatSelected == -1) {
                    Snackbar.make(rootView, "请选择重复模式", Snackbar.LENGTH_SHORT).setAction("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showRepeatDialog();
                        }
                    }).show();
                } else {
                    int repeatMode = 0;
                    switch (repeatSelected) {
                        case 0:
                            for (int i = 0; i < 7; ++i)
                                repeatMode |= 1 << i;
                            break;
                        case 1:
                            for (int i = 0; i < 5; ++i)
                                repeatMode |= 1 << i;
                            break;
                        case 2:
                            for (int i : repeatSelectedArr) {
                                repeatMode |= 1 << i;
                            }
                            break;
                    }
                    presenter.saveAlert(singleTimeView.getHour(), singleTimeView.getMinute(), contentET.getText().toString(), repeatMode);
                }
                return true;
        }
        return false;
    }

    private void showRepeatDialog() {
        new MaterialDialog.Builder(EditAlertActivity.this)
                .title("重复")
                .items(R.array.repeat_arr)
                .itemsCallbackSingleChoice(repeatSelected, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        repeatSelected = which;
                        switch (which) {
                            case 0:
                                repeatDetailTV.setTag(which);
                                repeatDetailTV.setText("每天");
                                break;
                            case 1:
                                repeatDetailTV.setTag(which);
                                repeatDetailTV.setText("周一到周五");
                                break;
                            case 2:
                                showCustomRepeatDialog();
                                break;
                        }
                        return true;
                    }
                })
                .show();
    }

    private void showCustomRepeatDialog() {
        new MaterialDialog.Builder(EditAlertActivity.this)
                .title("自定义重复日期")
                .items(R.array.repeat_week_arr)
                .itemsCallbackMultiChoice(repeatSelectedArr, new MaterialDialog.ListCallbackMultiChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                        return false;
                    }
                })
                .positiveText(R.string.action_confirm)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        repeatSelectedArr = dialog.getSelectedIndices();
                        repeatDetailTV.setText("");
                        for (int i : repeatSelectedArr) {
                            CharSequence s = (getResources().getTextArray(R.array.repeat_week_arr))[i];
                            String ss = repeatDetailTV.getText().toString() + s + ",";
                            repeatDetailTV.setText(ss);
                        }
                        repeatDetailTV.setText(repeatDetailTV.getText().subSequence(0, repeatDetailTV.getText().length() - 1));
                    }
                })
                .negativeText(R.string.action_cancel)
                .show();
    }

    @Override
    public void timeClick() {
        RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                .setOnTimeSetListener(this)
                .setTitleText(getResources().getString(R.string.alert_time))
                .setDoneText(getResources().getString(R.string.action_confirm))
                .setCancelText(getResources().getString(R.string.action_cancel));
        if (DayNightModeUtils.isCurrentNight())
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
        if (alert != null) {
            singleTimeView.setTime(alert.getStartHour(), alert.getStartMinute());
            contentET.setText(alert.getContent());
        }
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
