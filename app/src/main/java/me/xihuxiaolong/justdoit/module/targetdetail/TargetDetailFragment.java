package me.xihuxiaolong.justdoit.module.targetdetail;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.xihuxiaolong.justdoit.R;
import me.xihuxiaolong.justdoit.common.base.BaseMvpFragment;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
import me.xihuxiaolong.justdoit.common.database.localentity.RedoPlanDO;
import me.xihuxiaolong.justdoit.common.database.localentity.TargetDO;
import me.xihuxiaolong.justdoit.common.util.BusinessUtils;
import me.xihuxiaolong.justdoit.common.util.ProjectActivityUtils;
import me.xihuxiaolong.justdoit.common.widget.DayNightBackgroundView;
import me.xihuxiaolong.justdoit.module.adapter.NewPlanListWrapper;
import me.xihuxiaolong.justdoit.module.editalert.EditAlertActivity;
import me.xihuxiaolong.justdoit.module.editplan.EditPlanActivity;
import me.xihuxiaolong.justdoit.module.main.ScrollListener;
import me.xihuxiaolong.justdoit.module.redoplandetail.RedoPlanDetailActivity;
import me.xihuxiaolong.library.utils.CollectionUtils;
import me.xihuxiaolong.library.utils.ColorUtils;
import me.xihuxiaolongren.photoga.MediaChoseActivity;


/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/7/5.
 */
public class TargetDetailFragment extends BaseMvpFragment<TargetDetailContract.IView, TargetDetailContract.IPresenter> implements TargetDetailContract.IView, ObservableScrollViewCallbacks, NewPlanListWrapper.PlanListOnClickListener{

    private static final float MAX_TEXT_SCALE_DELTA = 0.5f;

    public static final String ARG_TARGET_NAME = "targetName";

    private String targetName;

    TargetDetailComponent targetDetailComponent;

    @BindView(R.id.headerFL)
    FrameLayout headerFL;
    @BindView(R.id.headerCV)
    CardView headerCV;
    @BindView(R.id.headerEmptyShadowFL)
    FrameLayout headerEmptyShadowFL;
    @BindView(R.id.recyclerView)
    ObservableRecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_background)
    View recyclerBackground;
    @BindView(R.id.day_night_background_view)
    DayNightBackgroundView dayNightBackgroundView;
    @BindView(R.id.headerIV)
    ImageView headerIV;
    @BindView(R.id.shadowFrame)
    FrameLayout shadowFrame;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.planFab)
    FloatingActionButton planFab;
    @BindView(R.id.alertFab)
    FloatingActionButton alertFab;
    @BindView(R.id.fab)
    FloatingActionMenu fab;

    private int mFlexibleSpaceImageHeight, mFlexibleRecyclerOffset, mFlexibleSpaceShowFabOffset, mFlexibleSpaceCalendarBottomOffset, mFlexibleSpaceCalendarLeftOffset, mFabSizeNormal;
    private int mActionBarSize, mStatusBarSize;

    boolean mFabIsShown = true;

    Menu menu;

    MenuItem addMenuItem;

    Drawable shadow;

    int vibrant, titleColor, textColor;

    RedoPlanAdapter redoPlanAdapter;

    ScrollListener scrollListener;

    int mSCrollY;

    public static TargetDetailFragment newInstance() {
        TargetDetailFragment fragment = new TargetDetailFragment();
        return fragment;
    }

    @Override
    public TargetDetailContract.IPresenter createPresenter() {
        return targetDetailComponent.presenter();
    }

    @Override
    protected void injectDependencies() {
        targetDetailComponent = DaggerTargetDetailComponent.builder()
                .appComponent(ProjectActivityUtils.getAppComponent(getActivity()))
                .targetDetailModule(new TargetDetailModule(targetName))
                .build();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.loadTarget();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        targetName = getActivity().getIntent().getStringExtra(ARG_TARGET_NAME);
        injectDependencies();
        View view = inflater.inflate(R.layout.fragment_target_detail, container, false);
        ButterKnife.bind(this, view);
        initToolbar(toolbar, true, false);
        setHasOptionsMenu(true);

        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mFlexibleRecyclerOffset = getResources().getDimensionPixelSize(R.dimen.flexible_recyclerview_header_height);
        mFlexibleSpaceShowFabOffset = getResources().getDimensionPixelSize(R.dimen.flexible_space_show_fab_offset);
        mFlexibleSpaceCalendarBottomOffset = getResources().getDimensionPixelSize(R.dimen.flexible_space_title_bottom_offset);
        mFlexibleSpaceCalendarLeftOffset = getResources().getDimensionPixelSize(R.dimen.flexible_space_title_left_offset);
        mFabSizeNormal = getResources().getDimensionPixelSize(R.dimen.fab_menu_size_normal);

        shadow = ContextCompat.getDrawable(getContext(), R.drawable.bottom_shadow);
        planFab.setOnClickListener(fabListener);
        alertFab.setOnClickListener(fabListener);
        fab.setClosedOnTouchOutside(true);

        ViewGroup.LayoutParams layoutParams = toolbar.getLayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mStatusBarSize = getStatusBarHeight();
        }
        mActionBarSize = layoutParams.height - mStatusBarSize;

        vibrant = ContextCompat.getColor(getContext(), R.color.sky);
//        titleColor = ContextCompat.getColor(getContext(), R.color.titleTextColor);
        textColor = ContextCompat.getColor(getContext(), R.color.titleTextColor);

        titleTv.setText(targetName);
        titleTv.post(new Runnable() {
            @Override
            public void run() {
                animateCanlendarRl(0);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        redoPlanAdapter = new RedoPlanAdapter(R.layout.item_redo_plan_detail, new ArrayList<RedoPlanDO>());
        final View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.item_target_detail_header, recyclerView, false);
        redoPlanAdapter.addHeaderView(headerView);
        recyclerView.setAdapter(redoPlanAdapter);
        recyclerView.setScrollViewCallbacks(this);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                RedoPlanDO redoPlanDO = ((RedoPlanDO) adapter.getItem(position));
                Intent intent = new Intent(getActivity(), RedoPlanDetailActivity.class).putExtra(RedoPlanDetailActivity.ARG_TARGET, redoPlanDO)
                        .putExtra("vibrant", vibrant).putExtra("textColor", textColor);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    View view1 = view.findViewById(R.id.rootView);
                    View view2 = view.findViewById(R.id.title_tv);
                    View view3 = view.findViewById(R.id.persist_tv);
                    View view4 = view.findViewById(R.id.redo_tv);
                    View view5 = view.findViewById(R.id.time_tv);
                    View view6 = view.findViewById(R.id.typeIV);
                    Pair<View, String> p1 = Pair.create(view1, view1.getTransitionName());
                    Pair<View, String> p2 = Pair.create(view2, view2.getTransitionName());
                    Pair<View, String> p3 = Pair.create(view3, view3.getTransitionName());
                    Pair<View, String> p4 = Pair.create(view4, view4.getTransitionName());
                    Pair<View, String> p5 = Pair.create(view5, view5.getTransitionName());
                    Pair<View, String> p6 = Pair.create(view6, view6.getTransitionName());
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),p1, p2, p3, p4, p5, p6);
                    startActivity(intent, options.toBundle());
                }else {
                    startActivity(intent);
                }
            }
        });
        return view;
    }

    class RedoPlanAdapter extends BaseQuickAdapter<RedoPlanDO, BaseViewHolder> {

        public RedoPlanAdapter(int layoutId, List<RedoPlanDO> datas) {
            super(layoutId, datas);
        }

        @Override
        protected void convert(BaseViewHolder holder, RedoPlanDO redoPlanDO) {
            DateTimeFormatter builder = DateTimeFormat.forPattern("HH : mm");
            DateTime startTime = new DateTime(redoPlanDO.getDayTime()).withTime(redoPlanDO.getStartHour(), redoPlanDO.getStartMinute(), 0, 0);
            DateTime endTime = new DateTime(redoPlanDO.getDayTime()).withTime(redoPlanDO.getEndHour(), redoPlanDO.getEndMinute(), 0, 0);
            holder.setBackgroundColor(R.id.rootView, vibrant)
                    .setTextColor(R.id.title_tv, textColor)
                    .setTextColor(R.id.persist_tv, textColor)
                    .setTextColor(R.id.redo_tv, textColor)
                    .setTextColor(R.id.time_tv, textColor)
                    .setText(R.id.title_tv, redoPlanDO.getContent())
                    .setText(R.id.persist_tv, "已持续 " + Days.daysBetween(new DateTime(redoPlanDO.getCreatedTime()), DateTime.now()).getDays()  + " 天")
                    .setText(R.id.redo_tv, BusinessUtils.repeatModeStr(redoPlanDO.getRepeatMode()));
            if(PlanDO.TYPE_PLAN == redoPlanDO.getPlanType()){
                holder.setText(R.id.time_tv, startTime.toString(builder) + " - " + endTime.toString(builder));
                holder.setImageResource(R.id.typeIV, R.drawable.icon_plan);
            }else if(PlanDO.TYPE_ALERT == redoPlanDO.getPlanType()){
                holder.setText(R.id.time_tv, startTime.toString(builder));
                holder.setImageResource(R.id.typeIV, R.drawable.icon_alert);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_frament_targetdetail, menu);
        addMenuItem = menu.findItem(R.id.action_add);
        addMenuItem.setVisible(!mFabIsShown);
        this.menu = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                getActivity().supportFinishAfterTransition();
                getActivity().onBackPressed();
                return true;
            case R.id.action_set_image:
                Intent intent = new Intent(getActivity(), MediaChoseActivity.class);
                //chose_mode选择模式 0单选 1多选
                intent.putExtra("chose_mode", 0);
                //是否显示需要第一个是图片相机按钮
                intent.putExtra("isNeedfcamera", true);
                //是否剪裁图片(只有单选模式才有剪裁)
                intent.putExtra("crop", true);
                intent.putExtra("crop_image_w", headerEmptyShadowFL.getWidth());
                intent.putExtra("crop_image_h", headerEmptyShadowFL.getHeight());
                startActivityForResult(intent, MediaChoseActivity.REQUEST_CODE_CAMERA);
                return true;
            case R.id.action_add_alert:
                startActivity(new Intent(getActivity(), EditAlertActivity.class).putExtra(EditAlertActivity.ARGUMENT_DAY_TIME, DateTime.now().withTimeAtStartOfDay().getMillis())
                        .putExtra(EditAlertActivity.ARGUMENT_TARGET_NAME, targetName));
                return true;
            case R.id.action_add_plan:
                startActivity(new Intent(getActivity(), EditPlanActivity.class).putExtra(EditPlanActivity.ARGUMENT_DAY_TIME, DateTime.now().withTimeAtStartOfDay().getMillis())
                        .putExtra(EditPlanActivity.ARGUMENT_TARGET_NAME, targetName));
                return true;
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == MediaChoseActivity.REQUEST_CODE_CAMERA) {
            if (data != null && !CollectionUtils.isEmpty(data.getStringArrayListExtra("data"))) {
                ArrayList<String> uris = data.getStringArrayListExtra("data");
                setHeaderIV(uris.get(0));
                presenter.updateTarget(uris.get(0));
            }
        }
    }

    private void setHeaderIV(String uri){
        if(TextUtils.isEmpty(uri))
            return;
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), Uri.fromFile(new File(uri)));
            headerCV.setVisibility(View.VISIBLE);
            headerEmptyShadowFL.setVisibility(View.GONE);
            headerIV.setImageBitmap(bitmap);

            Palette palette = Palette.from(bitmap).generate();
            Palette.Swatch swatch = palette.getDominantSwatch();
//                    Palette.Swatch swatch = palette.getVibrantSwatch();
            //rgb颜色值
            vibrant = swatch.getRgb();
            //对应的标题字体颜色
//            titleColor = swatch.getTitleTextColor();
            //对应的正文字体颜色
            textColor = swatch.getBodyTextColor();

            int imageTextColor = ColorUtils.contrastColor(vibrant);

            titleTv.setTextColor(imageTextColor);
            titleTv.setShadowLayer(1, 1, 1, vibrant);
            dayNightBackgroundView.setRootBackgroundColor(vibrant);
            float alpha1 = Math.min(1, (float) mSCrollY / (mFlexibleRecyclerOffset - 20));
            toolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha1, vibrant));
            fab.setMenuButtonColorNormal(palette.getVibrantColor(fab.getMenuButtonColorNormal()));
            fab.setMenuButtonColorPressed(palette.getVibrantColor(fab.getMenuButtonColorNormal()));
            redoPlanAdapter.notifyDataSetChanged();

            setAllMenuColor(menu, toolbar, imageTextColor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        mSCrollY = scrollY;
        if (scrollListener != null)
            scrollListener.onScrollChanged(scrollY, firstScroll, dragging);

        // Translate imageView parallax
        ViewHelper.setTranslationY(headerFL, -scrollY);
        ViewHelper.setTranslationY(recyclerBackground, Math.max(0, -scrollY + mFlexibleSpaceImageHeight));

        animateCanlendarRl(scrollY);

        // Translate FAB
        int maxFabTranslationY = mFlexibleSpaceImageHeight - mFabSizeNormal / 2;

        float fabTranslationY = ScrollUtils.getFloat(
                -scrollY + mFlexibleSpaceImageHeight - mFabSizeNormal / 2,
                mActionBarSize,
                maxFabTranslationY);
        ViewHelper.setTranslationY(fab, fabTranslationY);
        // Show/hide FAB
        if (fabTranslationY < mFlexibleSpaceShowFabOffset) {
            hideFab();
        } else {
            showFab();
        }

//        ViewHelper.setTranslationY(fbsLl, -scrollY);

        // Translate toolbar
        float alpha1 = Math.min(1, (float) scrollY / (mFlexibleRecyclerOffset - 20));
        toolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha1, vibrant));

        // Translate toolbar
        if (mFlexibleSpaceImageHeight - mActionBarSize < scrollY)
            shadowFrame.setForeground(shadow);
        else
            shadowFrame.setForeground(null);

    }

    // Scale calendarRl
    void animateCanlendarRl(int scrollY) {
        float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
        float scale = 1 + ScrollUtils.getFloat((flexibleRange - scrollY) / flexibleRange, 0, MAX_TEXT_SCALE_DELTA);
        ViewHelper.setPivotX(titleTv, 0);
        ViewHelper.setPivotY(titleTv, 0);
        ViewHelper.setScaleX(titleTv, scale);
        ViewHelper.setScaleY(titleTv, scale);

        // Translate calendarRl
        int maxTitleTranslationY = (int) (mFlexibleSpaceImageHeight - titleTv.getHeight() * scale - mFlexibleSpaceCalendarBottomOffset);
        int titleTranslationY = maxTitleTranslationY - scrollY;
        ViewHelper.setTranslationY(titleTv, ScrollUtils.getFloat(titleTranslationY, (mActionBarSize - titleTv.getHeight()) / 2 + mStatusBarSize,
                maxTitleTranslationY));
        ViewHelper.setTranslationX(titleTv, ScrollUtils.getFloat(scrollY, 0,
                mFlexibleSpaceCalendarLeftOffset));
    }

    private void showFab() {
        if (!mFabIsShown) {
            for (int i = 0; i < fab.getChildCount(); ++i) {
                ViewPropertyAnimator.animate(fab.getChildAt(i)).cancel();
                ViewPropertyAnimator.animate(fab.getChildAt(i)).scaleX(1).scaleY(1).setDuration(200).start();
            }
            getActivity().invalidateOptionsMenu();
            mFabIsShown = true;
        }
    }

    private void hideFab() {
        if (mFabIsShown) {
            for (int i = 0; i < fab.getChildCount(); ++i) {
                ViewPropertyAnimator.animate(fab.getChildAt(i)).cancel();
                ViewPropertyAnimator.animate(fab.getChildAt(i)).scaleX(0).scaleY(0).setDuration(200).start();
            }
            getActivity().invalidateOptionsMenu();
            mFabIsShown = false;
        }
    }

    private View.OnClickListener fabListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.planFab:
                    startActivity(new Intent(getActivity(), EditPlanActivity.class).putExtra(EditPlanActivity.ARGUMENT_DAY_TIME, DateTime.now().withTimeAtStartOfDay().getMillis())
                            .putExtra(EditPlanActivity.ARGUMENT_TARGET_NAME, targetName));
                    break;
                case R.id.alertFab:
                    startActivity(new Intent(getActivity(), EditAlertActivity.class).putExtra(EditAlertActivity.ARGUMENT_DAY_TIME, DateTime.now().withTimeAtStartOfDay().getMillis())
                            .putExtra(EditPlanActivity.ARGUMENT_TARGET_NAME, targetName));
                    break;
            }
            fab.close(true);
        }
    };

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void planListenr(PlanDO planDO) {
        startActivity(new Intent(getActivity(), EditPlanActivity.class).putExtra(EditPlanActivity.ARGUMENT_EDIT_PLAN_ID, planDO.getId()));
    }

    @Override
    public void alertListenr(PlanDO planDO) {
        startActivity(new Intent(getActivity(), EditAlertActivity.class).putExtra(EditAlertActivity.ARGUMENT_EDIT_ALERT_ID, planDO.getId()));
    }

    @Override
    public void removePlanItem(long targetId) {

    }

    @Override
    public void addPlanItem(TargetDO targetDO) {

    }

    @Override
    public void updatePlanItem(TargetDO targetDO) {

    }

    @Override
    public void showTarget(final TargetDO targetDO) {
        if (targetDO != null) {
            setHeaderIV(targetDO.getHeaderImageUri());
            redoPlanAdapter.setNewData(targetDO.getRedoPlanDOList());
        }
    }

    @Override
    public void updateTargetSuccess() {

    }

}
