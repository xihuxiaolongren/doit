package me.xihuxiaolong.justdoit.module.targetdetail;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.github.clans.fab.FloatingActionButton;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import me.xihuxiaolong.justdoit.R;
import me.xihuxiaolong.justdoit.common.base.BaseMvpFragment;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
import me.xihuxiaolong.justdoit.common.database.localentity.TargetDO;
import me.xihuxiaolong.justdoit.common.util.ImageUtils;
import me.xihuxiaolong.justdoit.common.util.ProjectActivityUtils;
import me.xihuxiaolong.justdoit.common.widget.DayNightBackgroundView;
import me.xihuxiaolong.justdoit.module.editalert.EditAlertActivity;
import me.xihuxiaolong.justdoit.module.editplan.EditPlanActivity;
import me.xihuxiaolong.justdoit.module.main.ScrollListener;
import me.xihuxiaolong.library.utils.CollectionUtils;
import me.xihuxiaolong.library.utils.DialogUtils;
import me.xihuxiaolongren.photoga.MediaChoseActivity;

import static me.xihuxiaolong.justdoit.module.targetdetail.TargetDetailActivity.ARG_TARGET;


/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/7/5.
 */
public class TargetPunchDetailFragment extends BaseMvpFragment<TargetDetailContract.IView, TargetDetailContract.IPresenter> implements TargetDetailContract.IView, ObservableScrollViewCallbacks{

    private static final float MAX_TEXT_SCALE_DELTA = 0.5f;

    public static final int REQUEST_BG = 1;
    public static final int REQUEST_PUNCH = 2;

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
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private int mFlexibleSpaceImageHeight, mFlexibleRecyclerOffset, mFlexibleSpaceShowFabOffset, mFlexibleSpaceCalendarBottomOffset, mFlexibleSpaceCalendarLeftOffset, mFabSizeNormal;
    private int mActionBarSize, mStatusBarSize;

    boolean mFabIsShown = true;

    Menu menu;

    MenuItem addMenuItem, colorMenuItem;

    Drawable shadow;

    int vibrant, textColor;

    PunchAdapter punchAdapter;

    ScrollListener scrollListener;

    int mSCrollY;

    String headerPicUri;

    TargetDO targetDO;

    public static TargetPunchDetailFragment newInstance() {
        TargetPunchDetailFragment fragment = new TargetPunchDetailFragment();
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
                .targetDetailModule(new TargetDetailModule(targetDO.getName()))
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
        targetDO = (TargetDO) getActivity().getIntent().getSerializableExtra(ARG_TARGET);
        injectDependencies();
        View view = inflater.inflate(R.layout.fragment_target_punch_detail, container, false);
        ButterKnife.bind(this, view);
        initToolbar(toolbar, true, false);
        setHasOptionsMenu(true);

        headerIV.setColorFilter(ContextCompat.getColor(getContext(), R.color.bgImageColor), PorterDuff.Mode.SRC_ATOP);
        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mFlexibleRecyclerOffset = getResources().getDimensionPixelSize(R.dimen.flexible_recyclerview_header_height);
        mFlexibleSpaceShowFabOffset = getResources().getDimensionPixelSize(R.dimen.flexible_space_show_fab_offset);
        mFlexibleSpaceCalendarBottomOffset = getResources().getDimensionPixelSize(R.dimen.flexible_space_title_bottom_offset);
        mFlexibleSpaceCalendarLeftOffset = getResources().getDimensionPixelSize(R.dimen.flexible_space_title_left_offset);
        mFabSizeNormal = getResources().getDimensionPixelSize(R.dimen.fab_menu_size_normal);

        shadow = ContextCompat.getDrawable(getContext(), R.drawable.bottom_shadow);
        fab.setOnClickListener(fabListener);

        ViewGroup.LayoutParams layoutParams = toolbar.getLayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mStatusBarSize = getStatusBarHeight();
        }
        mActionBarSize = layoutParams.height - mStatusBarSize;

        vibrant = ContextCompat.getColor(getContext(), R.color.sky);
        textColor = ContextCompat.getColor(getContext(), R.color.titleTextColor);

        titleTv.setText(targetDO.getName());
        titleTv.post(new Runnable() {
            @Override
            public void run() {
                animateCanlendarRl(0);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        punchAdapter = new PunchAdapter(R.layout.item_punch_target, new ArrayList<PlanDO>());
        final View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.item_target_detail_header, recyclerView, false);
        punchAdapter.addHeaderView(headerView);
        recyclerView.setAdapter(punchAdapter);
        recyclerView.setScrollViewCallbacks(this);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        return view;
    }

    class PunchAdapter extends BaseQuickAdapter<PlanDO, BaseViewHolder> {

        public PunchAdapter(int layoutId, List<PlanDO> datas) {
            super(layoutId, datas);
        }

        @Override
        protected void convert(BaseViewHolder holder, PlanDO punch) {
            DateTimeFormatter builder = DateTimeFormat.forPattern("HH : mm");
            DateTime startTime = new DateTime(punch.getDayTime()).withTime(punch.getStartHour(), punch.getStartMinute(), 0, 0);
            holder.setBackgroundColor(R.id.rootView, vibrant)
                    .setTextColor(R.id.timeTV, textColor)
                    .setTextColor(R.id.dateTV, textColor)
                    .setTextColor(R.id.contentTV, textColor)
                    .setText(R.id.timeTV, startTime.toString(builder))
                    .setText(R.id.contentTV, punch.getContent())
                    .setVisible(R.id.picIV, !TextUtils.isEmpty(punch.getPicUrls()));
            ImageView okIV = holder.getView(R.id.okIV);
            okIV.setColorFilter(textColor, PorterDuff.Mode.SRC_IN);
            okIV.setAlpha(0.75f);
            ImageUtils.loadImageFromFile(getContext(), (ImageView) holder.getView(R.id.picIV), punch.getPicUrls(), ImageView.ScaleType.CENTER_CROP);
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
        colorMenuItem = menu.findItem(R.id.action_set_color);
        addMenuItem.setVisible(!mFabIsShown);
        colorMenuItem.setVisible(!TextUtils.isEmpty(headerPicUri));
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
            case R.id.action_set_color:
                openColorDialog(headerPicUri);
                break;
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
                startActivityForResult(intent, REQUEST_BG);
                return true;
            case R.id.action_add_alert:
                startActivity(new Intent(getActivity(), EditAlertActivity.class).putExtra(EditAlertActivity.ARGUMENT_DAY_TIME, DateTime.now().withTimeAtStartOfDay().getMillis())
                        .putExtra(EditAlertActivity.ARGUMENT_TARGET_NAME, targetDO.getName()));
                return true;
            case R.id.action_add_plan:
                startActivity(new Intent(getActivity(), EditPlanActivity.class).putExtra(EditPlanActivity.ARGUMENT_DAY_TIME, DateTime.now().withTimeAtStartOfDay().getMillis())
                        .putExtra(EditPlanActivity.ARGUMENT_TARGET_NAME, targetDO.getName()));
                return true;
            case R.id.action_delete:
                DialogUtils.showDialog(getContext(), getResources().getString(R.string.delete_target), "确定要删除该目标吗？", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        presenter.deleteTarget();
                    }
                });
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setHeaderIV(String uri) {
        headerCV.setVisibility(View.VISIBLE);
        headerEmptyShadowFL.setVisibility(View.GONE);
        ImageUtils.loadImageFromFile(getActivity(), headerIV, uri, ImageView.ScaleType.CENTER_CROP);
    }

    private void updateTheme() {
//        titleTv.setTextColor(textColor);
//        titleTv.setShadowLayer(0, 0, 0, vibrant);
//        float alpha1 = Math.min(1, (float) mSCrollY / (mFlexibleRecyclerOffset - 20));
//        toolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha1, vibrant));
//        setAllMenuColor(menu, toolbar, textColor);
        dayNightBackgroundView.setRootBackgroundColor(vibrant);
        fab.setColorNormal(vibrant);
        fab.setColorPressed(vibrant);
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.menu_target_1);
        drawable = drawable.mutate();
        drawable.setColorFilter(textColor, PorterDuff.Mode.SRC_IN);
        fab.setImageDrawable(drawable);
        punchAdapter.notifyDataSetChanged();
    }

    private MaterialDialog colorDialog;

    private void openColorDialog(String picUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), Uri.fromFile(new File(picUri)));
            Palette palette = Palette.from(bitmap).generate();
            Set<Palette.Swatch> colorList = new LinkedHashSet<>();
            colorList.add(null);
            if (palette.getDominantSwatch() != null) colorList.add(palette.getDominantSwatch());
            if (palette.getMutedSwatch() != null) colorList.add(palette.getMutedSwatch());
            if (palette.getVibrantSwatch() != null) colorList.add(palette.getVibrantSwatch());
            if (palette.getDarkMutedSwatch() != null) colorList.add(palette.getDarkMutedSwatch());
            if (palette.getDarkVibrantSwatch() != null) colorList.add(palette.getDarkVibrantSwatch());
            if (palette.getLightMutedSwatch() != null) colorList.add(palette.getLightMutedSwatch());
            if (palette.getLightVibrantSwatch() != null) colorList.add(palette.getLightVibrantSwatch());
            List<Palette.Swatch> list = new ArrayList<>();
            list.addAll(colorList);
            final ColorAdapter colorAdapter = new ColorAdapter(R.layout.item_color, list);
            colorDialog = new MaterialDialog.Builder(getActivity())
                    .iconRes(R.drawable.icon_color_1)
                    .title(R.string.color_dialog_title)
//                .customView(R.layout.dialog_color_select, true)
                    .positiveText(R.string.action_confirm)
                    .adapter(colorAdapter, new GridLayoutManager(getContext(), 4))
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            presenter.updateTarget(targetDO.getCustomTheme(), vibrant, textColor);
                        }
                    })
                    .negativeText(R.string.action_cancel)
                    .show();
            WindowManager.LayoutParams lp = colorDialog.getWindow().getAttributes();
            lp.dimAmount = 0.1f;
            colorDialog.getWindow().setAttributes(lp);
            colorDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            RecyclerView colorRecyclerView = colorDialog.getRecyclerView();
            colorRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
                @Override
                public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                    if(position == 0){
                        vibrant = ContextCompat.getColor(getContext(), R.color.sky);
                        textColor = ContextCompat.getColor(getContext(), R.color.titleTextColor);
                        updateTheme();
                        targetDO.setCustomTheme(false);
                    }else {
                        Palette.Swatch swatch = (Palette.Swatch) baseQuickAdapter.getItem(position);
                        vibrant = swatch.getRgb();
                        textColor = swatch.getBodyTextColor();
                        updateTheme();
                        targetDO.setCustomTheme(true);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    class ColorAdapter extends BaseQuickAdapter<Palette.Swatch, BaseViewHolder> {

        public ColorAdapter(int layoutId, List<Palette.Swatch> datas) {
            super(layoutId, datas);
        }

        @Override
        protected void convert(BaseViewHolder holder, Palette.Swatch color) {
            CircleImageView circleImageView = holder.getView(R.id.color);
            if(holder.getAdapterPosition() == 0){
                holder.setVisible(R.id.tips, true);
                circleImageView.setFillColor(ContextCompat.getColor(getContext(), R.color.sky));
            }else {
                holder.setVisible(R.id.tips, false);
                circleImageView.setFillColor(color.getRgb());
            }
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
            ViewPropertyAnimator.animate(fab).cancel();
            ViewPropertyAnimator.animate(fab).scaleX(1).scaleY(1).setDuration(200).start();
            getActivity().invalidateOptionsMenu();
            mFabIsShown = true;
        }
    }

    private void hideFab() {
        if (mFabIsShown) {
            ViewPropertyAnimator.animate(fab).cancel();
            ViewPropertyAnimator.animate(fab).scaleX(0).scaleY(0).setDuration(200).start();
            getActivity().invalidateOptionsMenu();
            mFabIsShown = false;
        }
    }

    private View.OnClickListener fabListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openPunch();
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
            this.targetDO = targetDO;
            headerPicUri = targetDO.getHeaderImageUri();
            if(!TextUtils.isEmpty(headerPicUri)) {
                setHeaderIV(targetDO.getHeaderImageUri());
                getActivity().invalidateOptionsMenu();
                if (targetDO.getCustomTheme()) {
                    vibrant = targetDO.getThemeColor();
                    textColor = targetDO.getTextColor();
                }
                updateTheme();
            }
            punchAdapter.setNewData(targetDO.getPunchList());
        }
    }

    @Override
    public void updateTargetSuccess() {

    }

    @Override
    public void deleteTargetSuccess() {
        getActivity().finish();
    }

    private MaterialDialog addPunchDialog;
    private MaterialEditText addPunchET;
    private ImageView picIV;
    String picUri;

    private void openPunch() {
        addPunchDialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.add_punch_title)
                .widgetColorRes(R.color.colorAccent)
                .customView(R.layout.dialog_add_punch_targetdetail, true)
                .positiveText(R.string.action_confirm)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        presenter.savePunch(addPunchET.getText().toString(), picUri);
                    }
                })
                .negativeText(R.string.action_cancel)
                .show();
        addPunchET = (MaterialEditText) addPunchDialog.findViewById(R.id.addPunchET);
        picIV = (ImageView) addPunchDialog.findViewById(R.id.picIV);
        picIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MediaChoseActivity.class);
                //chose_mode选择模式 0单选 1多选
                intent.putExtra("chose_mode", 0);
                //是否显示需要第一个是图片相机按钮
                intent.putExtra("isNeedfcamera", true);
                startActivityForResult(intent, REQUEST_PUNCH);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && !CollectionUtils.isEmpty(data.getStringArrayListExtra("data"))) {
            ArrayList<String> uris = data.getStringArrayListExtra("data");
            if (requestCode == REQUEST_BG) {
                headerPicUri = uris.get(0);
                if (TextUtils.isEmpty(headerPicUri))
                    return;
                setHeaderIV(headerPicUri);
                getActivity().invalidateOptionsMenu();
                openColorDialog(headerPicUri);
                presenter.updateTarget(headerPicUri);
            } else if (requestCode == REQUEST_PUNCH) {
                picUri = uris.get(0);
                ImageUtils.loadImageFromFile(getActivity(), picIV, picUri, ImageView.ScaleType.CENTER_CROP);
            }
        }
    }
}
