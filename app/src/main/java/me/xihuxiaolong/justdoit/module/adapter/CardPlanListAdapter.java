package me.xihuxiaolong.justdoit.module.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.percent.PercentLayoutHelper;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;

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

public class CardPlanListAdapter extends BaseMultiItemQuickAdapter<PlanDO, BaseViewHolder> {

    private int minHeight;

    PlanListOnClickListener planListOnClickListener;

    float scale;

    int textColor, vibrant;

    public CardPlanListAdapter(Context context, List<PlanDO> datas, PlanListOnClickListener planListOnClickListener) {
        super(datas);
        init(context, planListOnClickListener);
        textColor = ContextCompat.getColor(context, R.color.titleTextColor);
        vibrant = ContextCompat.getColor(context, R.color.sky);
    }

    public CardPlanListAdapter(Context context, List<PlanDO> datas, PlanListOnClickListener planListOnClickListener, int textColor, int vibrant) {
        super(datas);
        init(context, planListOnClickListener);
        this.textColor = textColor;
        this.vibrant = vibrant;
    }

    public void setTextColor(int textColor){
        this.textColor = textColor;
    }

    public void setVibrant(int vibrant){
        this.vibrant = vibrant;
    }

    private void init(Context context, PlanListOnClickListener planListOnClickListener){
        scale = context.getResources().getDisplayMetrics().density;
        minHeight = DeviceUtil.getScreenHeight();

        addItemType(PlanDO.TYPE_ALERT, R.layout.item_card_alert);
        addItemType(PlanDO.TYPE_PLAN, R.layout.item_card_plan);
        addItemType(PlanDO.TYPE_PHOTO, R.layout.item_card_photo);
        addItemType(PlanDO.TYPE_PUNCH, R.layout.item_card_punch);
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
        DateTimeFormatter builder = DateTimeFormat.forPattern("HH : mm");
        DateTime startTime = new DateTime(planDO.getDayTime()).withTime(planDO.getStartHour(), planDO.getStartMinute(), 0, 0);
        CardView cardView = holder.getView(R.id.rootView);
        cardView.setCardBackgroundColor(vibrant);
        holder.setTextColor(R.id.timeTV, textColor)
                .setTextColor(R.id.contentTV, textColor)
                .setText(R.id.timeTV, startTime.toString(builder))
                .setText(R.id.contentTV, planDO.getContent())
                .setVisible(R.id.picIV, !TextUtils.isEmpty(planDO.getPicUrls()))
                .addOnClickListener(R.id.picIV);
        setSectionTime(planDO, holder);
        ImageView typeIV = holder.getView(R.id.typeIV);
        typeIV.setColorFilter(textColor, PorterDuff.Mode.SRC_IN);
        typeIV.setAlpha(0.55f);
        ImageUtils.loadImageFromFile(mContext, (ImageView) holder.getView(R.id.picIV), planDO.getPicUrls(), ImageView.ScaleType.CENTER_CROP);
    }

    private void convertPhoto(BaseViewHolder holder, PlanDO planDO) {
        DateTimeFormatter builder = DateTimeFormat.forPattern("HH : mm");
        DateTime startTime = new DateTime(planDO.getDayTime()).withTime(planDO.getStartHour(), planDO.getStartMinute(), 0, 0);
        CardView cardView = holder.getView(R.id.rootView);
        cardView.setCardBackgroundColor(vibrant);
        holder.setTextColor(R.id.timeTV, textColor)
                .setTextColor(R.id.contentTV, textColor)
                .setText(R.id.timeTV, startTime.toString(builder))
                .setText(R.id.contentTV, planDO.getContent())
                .setVisible(R.id.picIV, !TextUtils.isEmpty(planDO.getPicUrls()))
                .addOnClickListener(R.id.picIV);
        setSectionTime(planDO, holder);
        ImageView typeIV = holder.getView(R.id.typeIV);
        typeIV.setColorFilter(textColor, PorterDuff.Mode.SRC_IN);
        typeIV.setAlpha(0.55f);
        ImageUtils.loadImageFromFile(mContext, (ImageView) holder.getView(R.id.picIV), planDO.getPicUrls(), ImageView.ScaleType.CENTER_CROP);
    }

    private void convertPunch(BaseViewHolder holder, PlanDO planDO) {
        DateTimeFormatter builder = DateTimeFormat.forPattern("HH : mm");
        DateTime startTime = new DateTime(planDO.getDayTime()).withTime(planDO.getStartHour(), planDO.getStartMinute(), 0, 0);
        CardView cardView = holder.getView(R.id.rootView);
        cardView.setCardBackgroundColor(vibrant);
        holder.setTextColor(R.id.timeTV, textColor)
                .setTextColor(R.id.contentTV, textColor)
                .setText(R.id.timeTV, startTime.toString(builder))
                .setText(R.id.contentTV, planDO.getContent())
                .setVisible(R.id.picIV, !TextUtils.isEmpty(planDO.getPicUrls()))
                .addOnClickListener(R.id.picIV);
        setSectionTime(planDO, holder);
        ImageView typeIV = holder.getView(R.id.typeIV);
        typeIV.setColorFilter(textColor, PorterDuff.Mode.SRC_IN);
        typeIV.setAlpha(0.55f);
        ImageUtils.loadImageFromFile(mContext, (ImageView) holder.getView(R.id.picIV), planDO.getPicUrls(), ImageView.ScaleType.CENTER_CROP);
    }

    void setSectionTime(PlanDO planDO, BaseViewHolder holder){
        if(planDO.getTempDayTime() == null){
            holder.setVisible(R.id.timeSectionFB, false);
        }else {
            DateTime dateTime = new DateTime(planDO.getDayTime());
            holder.setVisible(R.id.timeSectionFB, true);
            FancyButton fancyButton = holder.getView(R.id.timeSectionFB);
            fancyButton.setText(dateTime.toString(DateTimeFormat.forPattern("yyyy MM月 dd日")));
        }
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
        DateTimeFormatter builder = DateTimeFormat.forPattern("HH : mm");
        DateTime startTime = new DateTime(planDO.getDayTime()).withTime(planDO.getStartHour(), planDO.getStartMinute(), 0, 0);
        DateTime endTime = new DateTime(planDO.getDayTime()).withTime(planDO.getEndHour(), planDO.getEndMinute(), 0, 0);
        CardView cardView = holder.getView(R.id.rootView);
        cardView.setCardBackgroundColor(vibrant);
        holder.setTextColor(R.id.timeTV, textColor)
                .setTextColor(R.id.contentTV, textColor)
                .setText(R.id.timeTV, startTime.toString(builder) + " - " + endTime.toString(builder))
                .setText(R.id.contentTV, planDO.getContent())
                .setVisible(R.id.picIV, !TextUtils.isEmpty(planDO.getPicUrls()))
                .addOnClickListener(R.id.picIV);
        setSectionTime(planDO, holder);
        ImageView typeIV = holder.getView(R.id.typeIV);
        typeIV.setColorFilter(textColor, PorterDuff.Mode.SRC_IN);
        typeIV.setAlpha(0.55f);
        ImageUtils.loadImageFromFile(mContext, (ImageView) holder.getView(R.id.picIV), planDO.getPicUrls(), ImageView.ScaleType.CENTER_CROP);
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