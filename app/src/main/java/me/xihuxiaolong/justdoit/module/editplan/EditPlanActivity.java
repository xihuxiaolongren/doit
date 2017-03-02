package me.xihuxiaolong.justdoit.module.editplan;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.google.android.flexbox.FlexboxLayout;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.xihuxiaolong.justdoit.R;
import me.xihuxiaolong.justdoit.common.base.BaseMvpActivity;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
import me.xihuxiaolong.justdoit.common.util.DayNightModeUtils;
import me.xihuxiaolong.justdoit.common.util.ProjectActivityUtils;
import me.xihuxiaolong.justdoit.common.util.ThirdAppUtils;
import me.xihuxiaolong.justdoit.common.widget.StartAndEndTimeView;
import me.xihuxiaolong.library.utils.ActivityUtils;
import me.xihuxiaolong.library.utils.CollectionUtils;
import me.xihuxiaolong.library.utils.DialogUtils;
import mehdi.sakout.fancybuttons.FancyButton;

public class EditPlanActivity extends BaseMvpActivity<EditPlanContract.IView, EditPlanContract.IPresenter> implements EditPlanContract.IView, StartAndEndTimeView.StartAndEndListener, CalendarDatePickerDialogFragment.OnDateSetListener, RadialTimePickerDialogFragment.OnTimeSetListener {

    public static final String ARGUMENT_EDIT_PLAN_ID = "EDIT_PLAN_ID";
    public static final String ARGUMENT_DAY_TIME = "DAY_TIME";
    public static final String ARGUMENT_TARGET_NAME = "TARGET_NAME";
    private static final String FRAG_TAG_TIME_PICKER_START = "frag_tag_time_picker_start";
    private static final String FRAG_TAG_TIME_PICKER_END = "frag_tag_time_picker_end";

    EditPlanComponent editPlanComponent;

    Long planId;

    long dayTime;

    String targetName;

    int repeatSelected = -1;
    Integer[] repeatSelectedArr;

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
    @BindView(R.id.rootView)
    RelativeLayout rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        planId = getIntent().getLongExtra(ARGUMENT_EDIT_PLAN_ID, -1L);
        dayTime = getIntent().getLongExtra(ARGUMENT_DAY_TIME, -1L);
        targetName = getIntent().getStringExtra(ARGUMENT_TARGET_NAME);
        injectDependencies();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plan);
        ButterKnife.bind(this);

        setToolbar(toolbar, true);

        contentET.requestFocus();
        contentET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                    ActivityUtils.hideSoftKeyboard(contentET);
            }
        });
        startAndEndTV.setStartAndEndListener(this);
        if(planId != -1L)
            repeatRL.setVisibility(View.GONE);
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
        new MaterialDialog.Builder(EditPlanActivity.this)
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

    FlexboxLayout selectTagsFl;
    FlexboxLayout allTagsFl;
    MaterialEditText tagET;
    View addTagIV;
    View tagPositive;

    LinkedHashSet<String> mSelectedTags, mAllTags;

    MaterialDialog tagDialog;

    @Override
    public void showTagDialog(LinkedHashSet<String> selectedTags, LinkedHashSet<String> allTags) {
        mSelectedTags = selectedTags;
        mAllTags = allTags;
        if(tagDialog == null){
            tagDialog = new MaterialDialog.Builder(EditPlanActivity.this)
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
                    .dismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            ActivityUtils.hideSoftKeyboard(EditPlanActivity.this);
                        }
                    })
                    .build();
            tagPositive = tagDialog.getActionButton(DialogAction.POSITIVE);
            tagPositive.setEnabled(false);
            selectTagsFl = (FlexboxLayout) tagDialog.findViewById(R.id.select_tags_fl);
            allTagsFl = (FlexboxLayout) tagDialog.findViewById(R.id.all_tags_fl);
            tagET = (MaterialEditText) tagDialog.findViewById(R.id.tagET);
            tagET.requestFocus();
            tagDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            addTagIV = tagDialog.findViewById(R.id.addTagIV);
            addTagIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(tagET.getText())) {
                        addTagSuccess(tagET.getText().toString());
                    }
                }
            });
            tagET.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    addTagIV.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);

                }
            });
            tagET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_NEXT && !TextUtils.isEmpty(v.getText())) {
                        addTagSuccess(v.getText().toString());
                        return true;
                    }
                    return false;
                }
            });
        }
        tagDialog.show();

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
    RecyclerView thirdAppRecyclerView;
    MaterialEditText searchET;
    List<ThirdAppUtils.AppItem> appItems;

    @Override
    public void showThirdAppDialog() {
        thirdAppDialog = new MaterialDialog.Builder(EditPlanActivity.this)
                .title("关联第三方应用")
                .customView(R.layout.dialog_third_app, false)
//                .adapter(new ThirdAppAdapter(EditPlanActivity.this, R.layout.item_third_app, ThirdAppUtils.getAllApps(this)), null)
                .show();
        searchET = (MaterialEditText) thirdAppDialog.findViewById(R.id.searchET);
        searchET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT && v.getText().length() > 0) {
                    List<ThirdAppUtils.AppItem> appItems1 = new ArrayList<ThirdAppUtils.AppItem>();
                    for (ThirdAppUtils.AppItem appItem : appItems) {
                        if (appItem.getAppName().contains(v.getText().toString()))
                            appItems1.add(appItem);
                    }
                    thirdAppRecyclerView.setAdapter(new ThirdAppAdapter(EditPlanActivity.this, R.layout.item_third_app, appItems1));
                    return true;
                } else {
                    thirdAppRecyclerView.setAdapter(new ThirdAppAdapter(EditPlanActivity.this, R.layout.item_third_app, appItems));
                }
                return false;
            }
        });
        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString().trim())) {
                    List<ThirdAppUtils.AppItem> appItems1 = new ArrayList<ThirdAppUtils.AppItem>();
                    for (ThirdAppUtils.AppItem appItem : appItems) {
                        if (appItem.getAppName().toLowerCase().contains(s.toString().trim().toLowerCase()))
                            appItems1.add(appItem);
                    }
                    thirdAppRecyclerView.setAdapter(new ThirdAppAdapter(EditPlanActivity.this, R.layout.item_third_app, appItems1));
                } else {
                    thirdAppRecyclerView.setAdapter(new ThirdAppAdapter(EditPlanActivity.this, R.layout.item_third_app, appItems));
                }
            }
        });
        thirdAppRecyclerView = (RecyclerView) thirdAppDialog.findViewById(R.id.recyclerView);
        thirdAppRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (appItems == null)
            appItems = ThirdAppUtils.getAllApps(this);
        thirdAppRecyclerView.setAdapter(new ThirdAppAdapter(EditPlanActivity.this, R.layout.item_third_app, appItems));
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
                    if (searchET.hasFocus())
                        ActivityUtils.hideSoftKeyboard(searchET);
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
                .editPlanModule(new EditPlanModule(planId, dayTime, targetName)).build();
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
                ActivityUtils.hideSoftKeyboard(this);
                if (TextUtils.isEmpty(contentET.getText()))
                    contentET.setError("计划不能为空");
                else if (!TextUtils.isEmpty(targetName) && repeatSelected == -1) {
                    Snackbar.make(rootView, "请选择重复模式", Snackbar.LENGTH_SHORT).setAction("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showRepeatDialog();
                        }
                    }).show();
                } else {
                    String tags = null;
                    String linkAppName = null;
                    String linkAppPackageName = null;
                    if (!tagDetailTV.getText().toString().equals("无"))
                        tags = tagDetailTV.getText().toString();
                    if (linkAppDetailTV.getTag() != null) {
                        ThirdAppUtils.AppItem appItem = (ThirdAppUtils.AppItem) linkAppDetailTV.getTag();
                        linkAppName = appItem.getAppName();
                        linkAppPackageName = appItem.getAppPackageName();
                    }
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
                endClick();
                break;
            case FRAG_TAG_TIME_PICKER_END:
                startAndEndTV.setEndTime(hourOfDay, minute);
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
        if(mSelectedTags.size() <= 0)
            tagPositive.setEnabled(false);
        selectTagsFl.removeView(selectTagsFl.findViewWithTag(tag));
        if (allTagsFl.findViewWithTag(tag) != null)
            allTagsFl.findViewWithTag(tag).setVisibility(View.VISIBLE);
    }

    public void addTagSuccess(String tag) {
        if (mSelectedTags.add(tag)) {
            addTagToSelectView(tag);
            if (allTagsFl.findViewWithTag(tag) != null)
                allTagsFl.findViewWithTag(tag).setVisibility(View.GONE);
        }
        tagET.setText(null);
        tagPositive.setEnabled(true);
    }

    public void saveTagSuccess() {
//        if(!TextUtils.isEmpty(tagET.getText())) {
//            addTagSuccess(tagET.getText().toString());
//            tagET.setText(null);
//        }
        if (!CollectionUtils.isEmpty(mSelectedTags)) {
            tagDetailTV.setText("");
            for (String tag : mSelectedTags) {
                tagDetailTV.setText(tagDetailTV.getText().toString() + tag + ",");
            }
            tagDetailTV.setText(tagDetailTV.getText().subSequence(0, tagDetailTV.getText().length() - 1));
        } else {
            tagDetailTV.setText("无");
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

}
