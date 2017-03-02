package me.xihuxiaolong.justdoit.module.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.percent.PercentLayoutHelper;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.google.android.flexbox.FlexboxLayout;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import me.xihuxiaolong.justdoit.R;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
import me.xihuxiaolong.justdoit.common.util.DeviceUtil;
import me.xihuxiaolong.justdoit.common.util.ImageUtils;
import me.xihuxiaolong.justdoit.common.util.ThirdAppUtils;
import me.xihuxiaolong.justdoit.module.images.BigImageActivity;
import mehdi.sakout.fancybuttons.FancyButton;

public class PlanListAdapter extends BaseMultiItemQuickAdapter<PlanDO, BaseViewHolder> {

    private int minHeight;

    PlanListOnClickListener planListOnClickListener;

    float scale;

    public PlanListAdapter(Context context, List<PlanDO> datas, PlanListOnClickListener planListOnClickListener) {
        super(datas);
        scale = context.getResources().getDisplayMetrics().density;
        minHeight = DeviceUtil.getScreenHeight();

        addItemType(PlanDO.TYPE_ALERT, R.layout.item_alert);
        addItemType(PlanDO.TYPE_PLAN, R.layout.item_plan);
        addItemType(PlanDO.TYPE_PHOTO, R.layout.item_photo);
        addItemType(PlanDO.TYPE_PUNCH, R.layout.item_punch);
        this.planListOnClickListener = planListOnClickListener;
    }

    private View.OnClickListener planListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PlanDO planDO = (PlanDO) v.getTag();
            if (planListOnClickListener != null)
                planListOnClickListener.planClick(planDO);
        }
    };

    private View.OnClickListener alertListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PlanDO planDO = (PlanDO) v.getTag();
            if (planListOnClickListener != null)
                planListOnClickListener.alertClick(planDO);
        }
    };

    private View.OnClickListener photoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PlanDO planDO = (PlanDO) v.getTag();
            if (!TextUtils.isEmpty(planDO.getPicUrls()) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && mContext instanceof Activity) {
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext, v, v.getTransitionName());
                mContext.startActivity(new Intent(mContext, BigImageActivity.class).putExtra("imageUrl", planDO.getPicUrls()), options.toBundle());
            } else {
                mContext.startActivity(new Intent(mContext, BigImageActivity.class).putExtra("imageUrl", planDO.getPicUrls()));
            }
        }
    };

    @Override
    protected void convert(BaseViewHolder holder, PlanDO planDO) {
        switch (planDO.getItemType()) {
            case PlanDO.TYPE_ALERT:
                convertAlert(holder, planDO);
                break;
            case PlanDO.TYPE_PLAN:
                convertPlan(holder, planDO);
                break;
            case PlanDO.TYPE_PHOTO:
                convertPhoto(holder, planDO);
                break;
            case PlanDO.TYPE_PUNCH:
                convertPunch(holder, planDO);
                break;
        }
    }

    private void convertAlert(BaseViewHolder holder, PlanDO planDO) {
        holder.getView(R.id.rootView).setMinimumHeight(minHeight / getItemCount());
        DateTime dateTime = new DateTime(planDO.getDayTime()).withTime(planDO.getStartHour(), planDO.getStartMinute(), 0, 0);
        DateTimeFormatter builder = DateTimeFormat.forPattern("HH : mm");
        holder.setText(R.id.startTimeTV, dateTime.toString(builder))
                .setText(R.id.contentTV, planDO.getContent())
                .setVisible(R.id.contentTV, TextUtils.isEmpty(planDO.getContent()));
        if (dateTime.isBeforeNow()) {
            holder.setText(R.id.doTV, "已结束");
            holder.setVisible(R.id.doTV, true);
        } else {
            holder.setText(R.id.doTV, "未开始");
            holder.setVisible(R.id.doTV, true);
        }
        holder.setTag(R.id.rootView, planDO);
        holder.setOnClickListener(R.id.rootView, alertListener);
        setTag((FlexboxLayout) holder.getView(R.id.tags_fl), planDO.getTags(), planDO.getTargetName());
    }

    private void convertPhoto(BaseViewHolder holder, PlanDO planDO) {
        final ImageView picIV = holder.getView(R.id.picIV);
        holder.getView(R.id.rootView).setMinimumHeight(minHeight / getItemCount());
        DateTime dateTime = new DateTime(planDO.getDayTime()).withTime(planDO.getStartHour(), planDO.getStartMinute(), 0, 0);
        DateTimeFormatter builder = DateTimeFormat.forPattern("HH : mm");
        holder.setText(R.id.startTimeTV, dateTime.toString(builder))
                .setText(R.id.contentTV, planDO.getContent())
                .setVisible(R.id.contentTV, !TextUtils.isEmpty(planDO.getContent()))
                .setVisible(R.id.picPRL, !TextUtils.isEmpty(planDO.getPicUrls()));
        setSingleImage(planDO.getPicUrls(), picIV);
        ImageUtils.loadImageFromFile(mContext, picIV, planDO.getPicUrls(), ImageView.ScaleType.FIT_CENTER);
        holder.setTag(R.id.picIV, planDO);
        holder.setOnClickListener(R.id.picIV, photoListener);
        holder.setTag(R.id.moreIV, planDO);
        holder.setOnClickListener(R.id.moreIV, moreClickListener);
        setTag((FlexboxLayout) holder.getView(R.id.tags_fl), planDO.getTags(), planDO.getTargetName());
//        setTag((FlexboxLayout) holder.getView(R.id.tags_fl), planDO.getTags());
    }

    private void convertPunch(BaseViewHolder holder, PlanDO planDO) {
        final ImageView picIV = holder.getView(R.id.picIV);
        holder.getView(R.id.rootView).setMinimumHeight(minHeight / getItemCount());
        DateTime dateTime = new DateTime(planDO.getDayTime()).withTime(planDO.getStartHour(), planDO.getStartMinute(), 0, 0);
        DateTimeFormatter builder = DateTimeFormat.forPattern("HH : mm");
        holder.setText(R.id.startTimeTV, dateTime.toString(builder))
                .setText(R.id.contentTV, planDO.getContent())
                .setVisible(R.id.contentTV, !TextUtils.isEmpty(planDO.getContent()))
                .setVisible(R.id.picPRL, !TextUtils.isEmpty(planDO.getPicUrls()));
        setSingleImage(planDO.getPicUrls(), picIV);
        ImageUtils.loadImageFromFile(mContext, picIV, planDO.getPicUrls(), ImageView.ScaleType.FIT_CENTER);
        holder.setTag(R.id.picIV, planDO);
        holder.setOnClickListener(R.id.picIV, photoListener);
        holder.setTag(R.id.moreIV, planDO);
        holder.setOnClickListener(R.id.moreIV, moreClickListener);
        setTag((FlexboxLayout) holder.getView(R.id.tags_fl), planDO.getTags(), planDO.getTargetName());
    }

    private void setSingleImage(String filepath, ImageView imageView) {
        int mWidth = 0, mHeight = 0;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filepath, options);
            mWidth = options.outWidth;
            mHeight = options.outHeight;
        } catch (Exception e) {

        }
        PercentRelativeLayout.LayoutParams params = (PercentRelativeLayout.LayoutParams) imageView.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo info = params.getPercentLayoutInfo();
        if (mWidth > mHeight) {
            info.widthPercent = 0.6f;
            info.aspectRatio = 1.5f;
        } else {
            info.widthPercent = 0.5f;
            info.aspectRatio = 0.66f;
        }
        info.aspectRatio = ((float) mWidth) / mHeight;
        info.widthPercent = Math.min(0.8f, Math.max(0.2f, ((float) mWidth) / mHeight * 0.6f));
        imageView.setLayoutParams(params);
    }

    private void convertPlan(BaseViewHolder holder, PlanDO planDO) {
        holder.getView(R.id.rootView).setMinimumHeight(minHeight / getItemCount());
        DateTimeFormatter builder = DateTimeFormat.forPattern("HH : mm");
        DateTime startTime = new DateTime(planDO.getDayTime()).withTime(planDO.getStartHour(), planDO.getStartMinute(), 0, 0);
        holder.setText(R.id.startTimeTV, startTime.toString(builder));
        DateTime endTime = new DateTime(planDO.getDayTime()).withTime(planDO.getEndHour(), planDO.getEndMinute(), 0, 0);
        holder.setText(R.id.endTimeTV, endTime.toString(builder));
        holder.setText(R.id.contentTV, planDO.getContent());
        TextView contentTV = holder.getView(R.id.contentTV);
        ImageView timelineIV = holder.getView(R.id.timelineIV);
        if (endTime.isBeforeNow()) {
            contentTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
            holder.setText(R.id.doTV, "已结束");
            holder.setVisible(R.id.doTV, true);
            timelineIV.setColorFilter(ContextCompat.getColor(mContext, R.color.titleTextColor));
        } else if (startTime.isAfterNow()) {
            holder.setText(R.id.doTV, "未开始");
            holder.setVisible(R.id.doTV, true);
            contentTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
            timelineIV.setImageResource(R.drawable.timeline_plan_other);
        } else {
            holder.setText(R.id.doTV, "进行中");
            holder.setVisible(R.id.doTV, true);
            contentTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
            timelineIV.setImageResource(R.drawable.timeline_plan_doing);
        }
        FancyButton linkAppFB = holder.getView(R.id.linkAppFB);
        linkAppFB.setTag(planDO);
        linkAppFB.setOnClickListener(linkAppClickListener);
        if (planDO.getLinkAppName() != null) {
            linkAppFB.setIconResource(ThirdAppUtils.getIcon(mContext, planDO.getLinkAppPackageName()));
            linkAppFB.getIconImageObject().setLayoutParams(new LinearLayout.LayoutParams((int) (28 * scale + 0.5f), (int) (28 * scale + 0.5f)));
            linkAppFB.setText(planDO.getLinkAppName());
            linkAppFB.setVisibility(View.VISIBLE);
        } else {
            linkAppFB.setVisibility(View.GONE);
        }
        setTag((FlexboxLayout) holder.getView(R.id.tags_fl), planDO.getTags(), planDO.getTargetName());

        holder.setTag(R.id.rootView, planDO);
        holder.setOnClickListener(R.id.rootView, planListener);
    }

    void setTag(FlexboxLayout flexboxLayout, String tags, String targetName){
        if (!TextUtils.isEmpty(targetName) || !TextUtils.isEmpty(tags)) {
            flexboxLayout.removeAllViews();
            flexboxLayout.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(targetName)) {
                TextView textView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_plan_tags, flexboxLayout, false);
                textView.setText("# " + targetName);
                flexboxLayout.addView(textView);
            }
            if (!TextUtils.isEmpty(tags)) {
                for (String tag : tags.split(",")) {
                    TextView textView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_plan_tags, flexboxLayout, false);
                    textView.setText("# " + tag);
                    flexboxLayout.addView(textView);
                }
            }
        } else {
            flexboxLayout.setVisibility(View.GONE);
        }

    }

    private View.OnClickListener linkAppClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PlanDO planDO = (PlanDO) v.getTag();
            ThirdAppUtils.launchapp(mContext, planDO.getLinkAppPackageName());
        }
    };

    private View.OnClickListener moreClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final PlanDO planDO = (PlanDO) v.getTag();
            PopupMenu popup = new PopupMenu(mContext, v);
            //Inflating the Popup using xml file
            popup.getMenuInflater()
                    .inflate(R.menu.popup_menu_plan, popup.getMenu());

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.delete:
                            planListOnClickListener.deleteClick(planDO);
                            break;
                        case R.id.share:
                            planListOnClickListener.shareClick(planDO);
                            break;
                    }
                    return true;
                }
            });

            popup.show(); //s
        }
    };

    private class PhotoTarget extends ViewTarget<PorterShapeImageView, Bitmap> {

        public PhotoTarget(PorterShapeImageView view) {
            super(view);
        }

        @Override
        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
            this.view.setImageBitmap(resource);
        }
    }

    public interface PlanListOnClickListener {
        void planClick(PlanDO planDO);

        void alertClick(PlanDO planDO);

        void deleteClick(PlanDO planDO);

        void shareClick(PlanDO planDO);

    }
}