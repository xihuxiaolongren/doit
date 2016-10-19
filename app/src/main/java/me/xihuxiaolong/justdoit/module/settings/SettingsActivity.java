package me.xihuxiaolong.justdoit.module.settings;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.Utils;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.Switch;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.xihuxiaolong.justdoit.R;
import me.xihuxiaolong.justdoit.common.base.BaseMvpActivity;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
import me.xihuxiaolong.justdoit.common.util.ActivityUtils;
import me.xihuxiaolong.library.utils.ToastUtil;

public class SettingsActivity extends BaseMvpActivity<SettingsContract.IView, SettingsContract.IPresenter> implements SettingsContract.IView {

    public static final String ARGUMENT_EDIT_ALERT_ID = "EDIT_ALERT_ID";
    public static final String ARGUMENT_DAY_TIME = "DAY_TIME";
    private static final String FRAG_TAG_TIME_PICKER = "frag_tag_time_picker";

    private Menu menu;

    @Inject
    ToastUtil toastUtil;
    SettingsComponent settingsComponent;
    @BindView(R.id.button)
    RelativeLayout button;
    @BindView(R.id.buttonTriangle)
    View buttonTriangle;
    @BindView(R.id.showAvatarSwitch)
    Switch showAvatarSwitch;
    @BindView(R.id.avatarIV)
    ImageView avatarIV;
    @BindView(R.id.mottoET)
    MaterialEditText mottoET;
    @BindView(R.id.mottoStartET)
    MaterialEditText mottoStartET;
    @BindView(R.id.mottoEndET)
    MaterialEditText mottoEndET;
    @BindView(R.id.expandableLayout)
    ExpandableLinearLayout expandableLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        injectDependencies();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        setToolbar(toolbar, true);

        avatarIV.setVisibility(showAvatarSwitch.isChecked() ? View.VISIBLE : View.INVISIBLE);
        showAvatarSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                avatarIV.setVisibility(checked ? View.VISIBLE : View.INVISIBLE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    protected void injectDependencies() {
        settingsComponent = DaggerSettingsComponent.builder().appComponent(ActivityUtils.getAppComponent(this))
                .settingsModule(new SettingsModule(-1L, -1L)).build();
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

    @OnClick(R.id.button)
    void expandClick(View v){
//        expandableLayout.setExpanded(!expandableLayout.isExpanded());
        if(expandableLayout.isExpanded())
            createRotateAnimator(buttonTriangle, 0f, 180f).start();
        else
            createRotateAnimator(buttonTriangle, 180f, 0f).start();
        expandableLayout.toggle();
    }



    public ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }

    @Override
    public void showAlert(PlanDO alert) {
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
