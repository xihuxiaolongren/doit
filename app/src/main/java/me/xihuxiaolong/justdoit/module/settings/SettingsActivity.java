package me.xihuxiaolong.justdoit.module.settings;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.Utils;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.RadioButton;
import com.rey.material.widget.Switch;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.xihuxiaolong.justdoit.R;
import me.xihuxiaolong.justdoit.common.base.BaseMvpActivity;
import me.xihuxiaolong.justdoit.common.cache.entity.UserSettings;
import me.xihuxiaolong.justdoit.common.event.Event;
import me.xihuxiaolong.justdoit.common.util.ProjectActivityUtils;
import me.xihuxiaolong.justdoit.common.util.DayNightModeUtils;
import me.xihuxiaolong.justdoit.common.widget.DayNightBackgroundView;
import me.xihuxiaolong.library.utils.CollectionUtils;
import me.xihuxiaolong.library.utils.ToastUtil;
import me.xihuxiaolongren.photoga.MediaChoseActivity;

public class SettingsActivity extends BaseMvpActivity<SettingsContract.IView, SettingsContract.IPresenter> implements SettingsContract.IView, ObservableScrollViewCallbacks, RadialTimePickerDialogFragment.OnTimeSetListener {

    public static final String FRAG_TAG_TIME_PICKER_DAY = "frag_tag_time_picker_day";
    public static final String FRAG_TAG_TIME_PICKER_NIGHT = "frag_tag_time_picker_night";


    @BindView(R.id.day_night_background_view)
    DayNightBackgroundView dayNightBackgroundView;
    @BindView(R.id.hpButtonTriangle)
    View hpButtonTriangle;
    @BindView(R.id.hpButton)
    RelativeLayout hpButton;
    @BindView(R.id.hpTV)
    TextView hpTV;
    @BindView(R.id.showAvatarTV)
    TextView showAvatarTV;
    @BindView(R.id.hpExpandableLayout)
    ExpandableLinearLayout hpExpandableLayout;
    @BindView(R.id.themeModelSwitch)
    Switch themeModelSwitch;
    @BindView(R.id.dayThemeStartTime)
    TextView dayThemeStartTime;
    @BindView(R.id.nightThemeStartTime)
    TextView nightThemeStartTime;
    @BindView(R.id.autoThemeRL)
    RelativeLayout autoThemeRL;
    @BindView(R.id.dayRB)
    RadioButton dayRB;
    @BindView(R.id.nightRB)
    RadioButton nightRB;
    @BindView(R.id.manualThemeRL)
    RelativeLayout manualThemeRL;
    @BindView(R.id.themeButton)
    RelativeLayout themeButton;
    @BindView(R.id.themeButtonTriangle)
    View themeButtonTriangle;
    @BindView(R.id.showAvatarSwitch)
    Switch showAvatarSwitch;
    @BindView(R.id.avatarIV)
    ImageView avatarIV;
    @BindView(R.id.mottoConfirmIV)
    ImageView mottoConfirmIV;
    @BindView(R.id.mottoET)
    MaterialEditText mottoET;
    @BindView(R.id.themeExpandableLayout)
    ExpandableLinearLayout themeExpandableLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Inject
    ToastUtil toastUtil;
    SettingsComponent settingsComponent;
    UserSettings userSettings;
    @BindView(R.id.scrollView)
    ObservableScrollView scrollView;
    @BindView(R.id.shadowFrame)
    FrameLayout shadowFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        injectDependencies();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        setToolbar(toolbar, true);

        shadow = ContextCompat.getDrawable(this, R.drawable.bottom_shadow);

        avatarIV.setVisibility(showAvatarSwitch.isChecked() ? View.VISIBLE : View.INVISIBLE);
        showAvatarSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                avatarIV.setVisibility(checked ? View.VISIBLE : View.INVISIBLE);
                if (userSettings != null) {
                    userSettings.setShowAvatar(checked);
                    presenter.saveSettings(userSettings);
                }
            }
        });
        setEditText(mottoET, mottoConfirmIV);

        autoThemeRL.setVisibility(themeModelSwitch.isChecked() ? View.VISIBLE : View.GONE);
        manualThemeRL.setVisibility(!themeModelSwitch.isChecked() ? View.VISIBLE : View.GONE);
        themeModelSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, final boolean checked) {
                if (checked) {
                    userSettings.setDayNightTheme(2);
                    presenter.saveSettings(userSettings);
                    setThemeAutoTime();
                } else {
                    userSettings.setDayNightTheme(0);
                    presenter.saveSettings(userSettings);
                    AppCompatDelegate.setDefaultNightMode(
                            AppCompatDelegate.MODE_NIGHT_NO);
                    EventBus.getDefault().post(new Event.ChangeDayNightTheme());
                    restart();
                }
                autoThemeRL.setVisibility(themeModelSwitch.isChecked() ? View.VISIBLE : View.GONE);
                manualThemeRL.setVisibility(!themeModelSwitch.isChecked() ? View.VISIBLE : View.GONE);
            }
        });
        dayRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                nightRB.setChecked(!isChecked);
                if (isChecked && buttonView.isPressed()) {
                    userSettings.setDayNightTheme(0);
                    presenter.saveSettings(userSettings);
                    AppCompatDelegate.setDefaultNightMode(
                            AppCompatDelegate.MODE_NIGHT_NO);
                    EventBus.getDefault().post(new Event.ChangeDayNightTheme());
                    restart();
                }
            }
        });
        nightRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dayRB.setChecked(!isChecked);
                if (isChecked && buttonView.isPressed()) {
                    userSettings.setDayNightTheme(1);
                    presenter.saveSettings(userSettings);
                    AppCompatDelegate.setDefaultNightMode(
                            AppCompatDelegate.MODE_NIGHT_YES);
                    EventBus.getDefault().post(new Event.ChangeDayNightTheme());
                    restart();
                }
            }
        });

        scrollView.setScrollViewCallbacks(this);
        presenter.loadSettings();
    }

    @OnClick(R.id.dayThemeStartTime)
    void dayThemeStartTimeClick(View v){
        RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                .setOnTimeSetListener(this)
                .setDoneText(getResources().getString(R.string.action_confirm))
                .setCancelText(getResources().getString(R.string.action_cancel));
        if(DayNightModeUtils.isCurrentNight())
            rtpd.setThemeDark();
        else
            rtpd.setThemeLight();
        rtpd.setStartTime((Integer) dayThemeStartTime.getTag(R.id.tag_first), (Integer) dayThemeStartTime.getTag(R.id.tag_second));
        rtpd.show(getSupportFragmentManager(), FRAG_TAG_TIME_PICKER_DAY);
    }

    @OnClick(R.id.nightThemeStartTime)
    void nightThemeStartTimeClick(View v){
        RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                .setOnTimeSetListener(this)
                .setDoneText(getResources().getString(R.string.action_confirm))
                .setCancelText(getResources().getString(R.string.action_cancel));
        if(DayNightModeUtils.isCurrentNight())
            rtpd.setThemeDark();
        else
            rtpd.setThemeLight();
        rtpd.setStartTime((Integer) nightThemeStartTime.getTag(R.id.tag_first), (Integer) nightThemeStartTime.getTag(R.id.tag_second));
        rtpd.show(getSupportFragmentManager(), FRAG_TAG_TIME_PICKER_NIGHT);
    }

    private View.OnClickListener editConfirmListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

            switch (v.getId()) {
                case R.id.mottoConfirmIV:
                    userSettings.setMotto(mottoET.getText().toString());
                    imm.hideSoftInputFromWindow(mottoET.getWindowToken(), 0);
                    hpExpandableLayout.requestFocus();
                    mottoET.clearFocus();
                    break;
            }
            presenter.saveSettings(userSettings);
        }
    };

    private void setEditText(final MaterialEditText edittext, final View confrimView) {
        edittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    editConfirmListener.onClick(confrimView);
                    return true;
                }
                return false;
            }
        });
        edittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                confrimView.setVisibility(hasFocus ? View.VISIBLE : View.GONE);
            }
        });
        confrimView.setOnClickListener(editConfirmListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    protected void injectDependencies() {
        settingsComponent = DaggerSettingsComponent.builder().appComponent(ProjectActivityUtils.getAppComponent(this))
                .settingsModule(new SettingsModule()).build();
    }

    @NonNull
    @Override
    public SettingsContract.IPresenter createPresenter() {
        return settingsComponent.presenter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_confirm:
                return true;
        }
        return true;
    }

    @OnClick(R.id.themeButton)
    void themeButtonClick(View v) {
        if (themeExpandableLayout.isExpanded())
            createRotateAnimator(themeButtonTriangle, 0f, 180f).start();
        else
            createRotateAnimator(themeButtonTriangle, 180f, 0f).start();
        themeExpandableLayout.toggle();
    }

    @OnClick(R.id.hpButton)
    void hpButtonClick(View v) {
        if (hpExpandableLayout.isExpanded())
            createRotateAnimator(hpButtonTriangle, 0f, 180f).start();
        else
            createRotateAnimator(hpButtonTriangle, 180f, 0f).start();
        hpExpandableLayout.toggle();
    }

    @OnClick(R.id.avatarIV)
    void selectImageClick(View v) {
        Intent intent = new Intent(this, MediaChoseActivity.class);
        //chose_mode选择模式 0单选 1多选
        intent.putExtra("chose_mode", 0);
        //是否显示需要第一个是图片相机按钮
        intent.putExtra("isNeedfcamera", true);
        //是否剪裁图片(只有单选模式才有剪裁)
        intent.putExtra("crop", true);
        startActivityForResult(intent, MediaChoseActivity.REQUEST_CODE_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == MediaChoseActivity.REQUEST_CODE_CAMERA) {
            if (result != null && !CollectionUtils.isEmpty(result.getStringArrayListExtra("data"))) {
                ArrayList<String> uris = result.getStringArrayListExtra("data");
                avatarIV.setImageURI(null);
                avatarIV.setImageURI(Uri.parse(uris.get(0)));
                userSettings.setAvatarUri(uris.get(0));
                presenter.saveSettings(userSettings);
            }
        }
    }

    public ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }

    @Override
    public void showSettings(UserSettings userSettings) {
        this.userSettings = userSettings;
        showAvatarSwitch.setChecked(userSettings.isShowAvatar());
        mottoET.setText(userSettings.getMotto());
        if (userSettings.getAvatarUri() != null) {
            avatarIV.setImageURI(null);
            avatarIV.setImageURI(Uri.parse(userSettings.getAvatarUri()));
        }
        switch (userSettings.getDayNightTheme()){
            case 0:
                themeModelSwitch.setChecked(false);
                dayRB.setChecked(true);
                nightRB.setChecked(false);
                break;
            case 1:
                themeModelSwitch.setChecked(false);
                dayRB.setChecked(false);
                nightRB.setChecked(true);
                break;
            case 2:
                themeModelSwitch.setChecked(true);
                setThemeAutoTime();
                break;
        }
    }

    private void setThemeAutoTime(){
        dayThemeStartTime.setTag(R.id.tag_first, userSettings.getDayStartHour());
        dayThemeStartTime.setTag(R.id.tag_second, userSettings.getDayStartMinute());
        nightThemeStartTime.setTag(R.id.tag_first, userSettings.getNightStartHour());
        nightThemeStartTime.setTag(R.id.tag_second, userSettings.getNightStartMinute());
        dayThemeStartTime.setText(String.format("日间模式开启时间\n%02d : %02d", userSettings.getDayStartHour(), userSettings.getDayStartMinute()));
        nightThemeStartTime.setText(String.format("夜间模式开启时间\n%02d : %02d", userSettings.getNightStartHour(), userSettings.getNightStartMinute()));
        boolean isChange = DayNightModeUtils.setAutoTheme(userSettings.getDayStartHour(), userSettings.getDayStartMinute(), userSettings.getNightStartHour(), userSettings.getNightStartMinute());
        if(isChange){
            EventBus.getDefault().post(new Event.ChangeDayNightTheme());
            restart();
        }
    }

    @Override
    public void saveSuccess() {

    }

    Drawable shadow;

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        if (scrollY > 100)
            shadowFrame.setForeground(shadow);
        else
            shadowFrame.setForeground(null);
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
        switch (dialog.getTag()){
            case FRAG_TAG_TIME_PICKER_DAY:
                userSettings.setDayStartHour(hourOfDay);
                userSettings.setDayStartMinute(minute);
                presenter.saveSettings(userSettings);
                setThemeAutoTime();
//                dayThemeStartTime.setTag(R.id.tag_first, hourOfDay);
//                dayThemeStartTime.setTag(R.id.tag_second, minute);
//                dayThemeStartTime.setText(String.format("日间模式开启时间\n%02d : %02d", hourOfDay, minute));
                break;
            case FRAG_TAG_TIME_PICKER_NIGHT:
                userSettings.setNightStartHour(hourOfDay);
                userSettings.setNightStartMinute(minute);
                presenter.saveSettings(userSettings);
//                nightThemeStartTime.setTag(R.id.tag_first, hourOfDay);
//                nightThemeStartTime.setTag(R.id.tag_second, minute);
//                nightThemeStartTime.setText(String.format("夜间模式开启时间\n%02d : %02d", hourOfDay, minute));
                break;
        }
    }
}
