package me.xihuxiaolong.justdoit.module.editplan;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.google.android.flexbox.FlexboxLayout;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.joda.time.DateTime;

import java.util.LinkedHashSet;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.xihuxiaolong.justdoit.R;
import me.xihuxiaolong.justdoit.common.base.BaseMvpActivity;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
import me.xihuxiaolong.justdoit.common.util.ProjectActivityUtils;
import me.xihuxiaolong.justdoit.common.util.DayNightModeUtils;
import me.xihuxiaolong.justdoit.common.util.ThirdAppUtils;
import me.xihuxiaolong.justdoit.common.widget.StartAndEndTimeView;
import me.xihuxiaolong.library.utils.CollectionUtils;
import me.xihuxiaolong.library.utils.DialogUtils;
import me.xihuxiaolong.library.utils.ToastUtil;
import mehdi.sakout.fancybuttons.FancyButton;

public class EditPlanActivity extends BaseMvpActivity<EditPlanContract.IView, EditPlanContract.IPresenter> implements EditPlanContract.IView, StartAndEndTimeView.StartAndEndListener, CalendarDatePickerDialogFragment.OnDateSetListener, RadialTimePickerDialogFragment.OnTimeSetListener {

    public static final String ARGUMENT_EDIT_PLAN_ID = "EDIT_PLAN_ID";
    public static final String ARGUMENT_DAY_TIME = "DAY_TIME";
    private static final String FRAG_TAG_TIME_PICKER_START = "frag_tag_time_picker_start";
    private static final String FRAG_TAG_TIME_PICKER_END = "frag_tag_time_picker_end";

    EditPlanComponent editPlanComponent;

    @Inject
    ToastUtil toastUtil;

    Long planId;

    long dayTime;

    int selected = 0;
    Integer[] selectedArr;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.backgroundIV)
    ImageView backgroundIV;
    @BindView(R.id.startAndEndTV)
    StartAndEndTimeView startAndEndTV;
    @BindView(R.id.contentET)
    MaterialEditText contentET;
    @BindView(R.id.repeatDetailTV)
    TextView repeatDetailTV;
    @BindView(R.id.repeatRL)
    RelativeLayout repeatRL;
    @BindView(R.id.tagRL)
    RelativeLayout tagRL;
    @BindView(R.id.tagDetailTV)
    TextView tagDetailTV;
    @BindView(R.id.linkAppDetailTV)
    TextView linkAppDetailTV;
    @BindView(R.id.linkAppRL)
    RelativeLayout linkAppRL;

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
        repeatRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRepeatDialog();
            }
        });
        tagRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.loadTags();
            }
        });
        linkAppRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showThirdAppDialog();
            }
        });
        presenter.loadPlan();
    }

    private void showRepeatDialog() {
        new MaterialDialog.Builder(EditPlanActivity.this)
                .title("重复")
                .items(R.array.repeat_arr)
                .itemsCallbackSingleChoice(selected, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        selected = which;
                        int repeatMode = 0;
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
        new MaterialDialog.Builder(EditPlanActivity.this)
                .title("自定义重复日期")
                .items(R.array.repeat_week_arr)
                .itemsCallbackMultiChoice(selectedArr, new MaterialDialog.ListCallbackMultiChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                        return false;
                    }
                })
                .positiveText(getResources().getString(R.string.action_confirm))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        selectedArr = dialog.getSelectedIndices();
                        repeatDetailTV.setText("");
                        for (int i : selectedArr) {
                            CharSequence s = (getResources().getTextArray(R.array.repeat_week_arr))[i];
                            String ss = repeatDetailTV.getText().toString() + s + "，";
                            repeatDetailTV.setText(ss);
                        }
                        repeatDetailTV.setText(repeatDetailTV.getText().subSequence(0, repeatDetailTV.getText().length() - 1));
                    }
                })
                .negativeText(getResources().getString(R.string.action_cancel))
                .show();
    }

    FlexboxLayout selectTagsFl;
    FlexboxLayout allTagsFl;
    MaterialEditText tagET;

    LinkedHashSet<String> mSelectedTags, mAllTags;

    @Override
    public void showTagDialog(LinkedHashSet<String> selectedTags, LinkedHashSet<String> allTags) {
        mSelectedTags = selectedTags;
        mAllTags = allTags;
        MaterialDialog dialog = new MaterialDialog.Builder(EditPlanActivity.this)
                .title("标签")
                .customView(R.layout.dialog_tag, true)
                .positiveText(getResources().getString(R.string.action_confirm))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        saveTagSuccess();
                    }
                })
                .negativeText(getResources().getString(R.string.action_cancel))
                .show();

        selectTagsFl = (FlexboxLayout) dialog.findViewById(R.id.select_tags_fl);
        allTagsFl = (FlexboxLayout) dialog.findViewById(R.id.all_tags_fl);
        tagET = (MaterialEditText) dialog.findViewById(R.id.tagET);
        tagET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT && v.getText().length() > 0) {
                    addTagSuccess(v.getText().toString());
                    v.setText(null);
                    return true;
                }
                return false;
            }
        });
        for (String tag : selectedTags) {
            addTagToSelectView(tag);
        }
        for (String tag : allTags) {
            addTagToUnselectView(tag);
        }
    }

    private void addTagToSelectView(String tag) {
        FancyButton fancyButton = (FancyButton) LayoutInflater.from(this).inflate(R.layout.item_selected_tag, selectTagsFl, false);
        fancyButton.setText(tag);
        fancyButton.setTag(tag);
        fancyButton.setOnClickListener(selectedTagClickListener);
        selectTagsFl.addView(fancyButton);
    }

    private void addTagToUnselectView(String tag) {
        FancyButton fancyButton = (FancyButton) LayoutInflater.from(this).inflate(R.layout.item_unselected_tag, allTagsFl, false);
        fancyButton.setText(tag);
        fancyButton.setTag(tag);
        fancyButton.setOnClickListener(unselectedTagClickListener);
        allTagsFl.addView(fancyButton);
    }

    private View.OnClickListener selectedTagClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            deleteTagSuccess((String) v.getTag());
        }
    };

    private View.OnClickListener unselectedTagClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addTagSuccess((String) v.getTag());
        }
    };

    MaterialDialog thirdAppDialog;

    @Override
    public void showThirdAppDialog() {
        thirdAppDialog = new MaterialDialog.Builder(EditPlanActivity.this)
                .title("关联第三方应用")
                .adapter(new ThirdAppAdapter(EditPlanActivity.this, R.layout.item_third_app, ThirdAppUtils.getAllApps(this)), null)
                .show();
    }

    class ThirdAppAdapter extends CommonAdapter<ThirdAppUtils.AppItem> {

        public ThirdAppAdapter(Context context, int layoutId, List<ThirdAppUtils.AppItem> datas) {
            super(context, layoutId, datas);
        }

        @Override
        protected void convert(ViewHolder holder, ThirdAppUtils.AppItem appItem, final int position) {
            holder.setImageDrawable(R.id.appIcon, appItem.getAppIcon());
            holder.setText(R.id.appName, appItem.getAppName());
            holder.setOnClickListener(R.id.rootView, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    thirdAppDialog.dismiss();
                    linkAppDetailTV.setText(mDatas.get(position).getAppName());
                    linkAppDetailTV.setTag(mDatas.get(position));
//                    ThirdAppUtils.launchapp(mContext, mDatas.get(position).getAppPackageName());
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    protected void injectDependencies() {
        editPlanComponent = DaggerEditPlanComponent.builder().appComponent(ProjectActivityUtils.getAppComponent(this))
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
        if (planId == -1L) {
            getSupportActionBar().setTitle(getResources().getString(R.string.add_plan));
        } else {
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
                DialogUtils.showDialog(this, getResources().getString(R.string.delete_plan), "确定要删除本条计划吗？", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        presenter.deletePlan();
                    }
                });
                return true;
            case R.id.action_confirm:
                if (TextUtils.isEmpty(contentET.getText()))
                    toastUtil.showToast("不能保存一条空的计划", Toast.LENGTH_SHORT);
                else {
                    String tags = null;
                    String linkAppName = null;
                    String linkAppPackageName = null;
                    if(!tagDetailTV.getText().toString().equals("无"))
                        tags = tagDetailTV.getText().toString();
                    if(linkAppDetailTV.getTag() != null){
                        ThirdAppUtils.AppItem appItem = (ThirdAppUtils.AppItem) linkAppDetailTV.getTag();
                        linkAppName = appItem.getAppName();
                        linkAppPackageName = appItem.getAppPackageName();
                    }
                    int repeatMode = 0;
                    switch (selected){
                        case 0 :
                            for(int i = 0; i < 7; ++i)
                                repeatMode |= 1 << i;
                            break;
                        case 1 :
                            for(int i = 0; i < 5; ++i)
                                repeatMode |= 1 << i;
                            break;
                        case 2 :
                            for (int i : selectedArr) {
                                repeatMode |= 1 << i;
                            }
                            break;
                    }

                    presenter.savePlan(startAndEndTV.getStartHour(), startAndEndTV.getStartMinute(),
                            startAndEndTV.getEndHour(), startAndEndTV.getEndMinute(),
                            contentET.getText().toString(), tags, linkAppName, linkAppPackageName, repeatMode);
                }
                return true;
        }
        return false;
    }

    @Override
    public void startClick() {
        DateTime now = DateTime.now();
        RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                .setOnTimeSetListener(this)
                .setTitleText(getResources().getString(R.string.plan_start_time))
                .setDoneText(getResources().getString(R.string.action_confirm))
                .setCancelText(getResources().getString(R.string.action_cancel));
        if (DayNightModeUtils.isCurrentNight())
            rtpd.setThemeDark();
        else
            rtpd.setThemeLight();
        rtpd.setStartTime(startAndEndTV.getStartHour() != 0 ? startAndEndTV.getStartHour() : now.getHourOfDay(), startAndEndTV.getStartMinute() != 0 ? startAndEndTV.getStartMinute() : now.getMinuteOfHour());
        rtpd.show(getSupportFragmentManager(), FRAG_TAG_TIME_PICKER_START);
    }

    @Override
    public void endClick() {
        DateTime now = DateTime.now();
        RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                .setOnTimeSetListener(this)
                .setTitleText(getResources().getString(R.string.plan_end_time))
                .setDoneText(getResources().getString(R.string.action_confirm))
                .setCancelText(getResources().getString(R.string.action_cancel));
        if (DayNightModeUtils.isCurrentNight())
            rtpd.setThemeDark();
        else
            rtpd.setThemeLight();
        rtpd.setStartTime(startAndEndTV.getEndHour() != 0 ? startAndEndTV.getEndHour() : now.getHourOfDay(), startAndEndTV.getEndMinute() != 0 ? startAndEndTV.getEndMinute() : now.getMinuteOfHour());
        rtpd.show(getSupportFragmentManager(), FRAG_TAG_TIME_PICKER_END);
    }

    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {

    }

    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
        switch (dialog.getTag()) {
            case FRAG_TAG_TIME_PICKER_START:
                startAndEndTV.setStartTime(hourOfDay, minute);
                if (hourOfDay > startAndEndTV.getEndHour()) {
                    startAndEndTV.setEndTime(hourOfDay + 1, minute);
                }
                break;
            case FRAG_TAG_TIME_PICKER_END:
                startAndEndTV.setEndTime(hourOfDay, minute);
                contentET.requestFocus();
                break;
        }
    }

    @Override
    public void showPlan(PlanDO plan) {
        if (plan != null) {
            startAndEndTV.setStartTime(plan.getStartHour(), plan.getStartMinute());
            startAndEndTV.setEndTime(plan.getEndHour(), plan.getEndMinute());
            contentET.setText(plan.getContent());
        }
    }

    @Override
    public void savePlanSuccess() {
        finish();
    }

    @Override
    public void deletePlanSuccess() {
        finish();
    }

    @Override
    public void sharePlan() {

    }

    public void deleteTagSuccess(String tag) {
        mSelectedTags.remove(tag);
        selectTagsFl.removeView(selectTagsFl.findViewWithTag(tag));
        if (allTagsFl.findViewWithTag(tag) != null)
            allTagsFl.findViewWithTag(tag).setVisibility(View.VISIBLE);
    }

    public void addTagSuccess(String tag) {
        if(mSelectedTags.add(tag)) {
            addTagToSelectView(tag);
            if (allTagsFl.findViewWithTag(tag) != null)
                allTagsFl.findViewWithTag(tag).setVisibility(View.GONE);
        }
    }

    public void saveTagSuccess() {
        if (!CollectionUtils.isEmpty(mSelectedTags)) {
            tagDetailTV.setText("");
            for (String tag : mSelectedTags) {
                tagDetailTV.setText(tagDetailTV.getText().toString() + tag + "，");
            }
            tagDetailTV.setText(tagDetailTV.getText().subSequence(0, tagDetailTV.getText().length() - 1));
        } else {
            tagDetailTV.setText("无");
        }
    }

}
