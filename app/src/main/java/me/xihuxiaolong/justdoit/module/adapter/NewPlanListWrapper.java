package me.xihuxiaolong.justdoit.module.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.flexbox.FlexboxLayout;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.ArrayList;
import java.util.List;

import me.xihuxiaolong.justdoit.R;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
import me.xihuxiaolong.justdoit.common.util.ThirdAppUtils;
import me.xihuxiaolong.library.utils.ActivityUtils;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/10/14.
 */

public class NewPlanListWrapper {

    private RecyclerView mRecyclerView;
    private PlanListAdapter adapter;

    public NewPlanListWrapper(Context context, RecyclerView recyclerView, PlanListOnClickListener planListOnClickListener){
        this.mRecyclerView = recyclerView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        adapter = new PlanListAdapter(context, new ArrayList<PlanDO>(), planListOnClickListener);
        adapter.setEmptyView(LayoutInflater.from(context).inflate(R.layout.empty_view_planlist, (ViewGroup) mRecyclerView.getParent(), false));
        adapter.setHeaderFooterEmpty(true, true);
        final View footView = LayoutInflater.from(context).inflate(R.layout.item_plan_bottom, mRecyclerView, false);
        adapter.addFooterView(footView);
        mRecyclerView.setAdapter(adapter);
    }

    public void addHeaderView(View headerView){
        adapter.addHeaderView(headerView);
    }

    public void setEmptyView(View emptyView, boolean isHeadAndEmpty, boolean isFootAndEmpty){
        adapter.setEmptyView(emptyView);
        adapter.setHeaderFooterEmpty(isHeadAndEmpty, isFootAndEmpty);
    }

    public void setItems(List<PlanDO> items) {
        adapter.setNewData(items);
    }

    public static class PlanListAdapter extends BaseMultiItemQuickAdapter<PlanDO, BaseViewHolder> {

        PlanListOnClickListener planListOnClickListener;

        float scale;

        public PlanListAdapter(Context context, List<PlanDO> datas, PlanListOnClickListener planListOnClickListener) {
            super(datas);
            scale = context.getResources().getDisplayMetrics().density;
            addItemType(PlanDO.TYPE_ALERT, R.layout.item_alert);
            addItemType(PlanDO.TYPE_PLAN, R.layout.item_plan);
            this.planListOnClickListener = planListOnClickListener;
        }

        private View.OnClickListener planListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlanDO planDO = (PlanDO) v.getTag();
                if(planListOnClickListener != null)
                    planListOnClickListener.planListenr(planDO);
            }
        };

        private View.OnClickListener alertListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlanDO planDO = (PlanDO) v.getTag();
                if(planListOnClickListener != null)
                    planListOnClickListener.alertListenr(planDO);
            }
        };

        @Override
        protected void convert(BaseViewHolder holder, PlanDO planDO) {
            switch (holder.getItemViewType()) {
                case PlanDO.TYPE_ALERT:
                    convertAlert(holder, planDO);
                    break;
                case PlanDO.TYPE_PLAN:
                    convertPlan(holder, planDO);
                    break;
            }
        }

        private void convertAlert(BaseViewHolder holder, PlanDO planDO){
            holder.getView(R.id.rootView).setMinimumHeight(ActivityUtils.dpToPx(400 / getItemCount()));
            DateTime dateTime = new DateTime(planDO.getDayTime()).withTime(planDO.getStartHour(), planDO.getStartMinute(), 0, 0);
//                DateTime dateTime = DateTime.now().withTimeAtStartOfDay().withTime(planDO.getStartHour(), planDO.getStartMinute(), 0, 0);
            DateTimeFormatter builder = DateTimeFormat.forPattern("HH : mm");
            holder.setText(R.id.startTimeTV, dateTime.toString(builder));
            holder.setText(R.id.contentTV, planDO.getContent());
            if(dateTime.isBeforeNow()){
                holder.setText(R.id.doTV, "已完成");
                holder.setVisible(R.id.doTV, true);
            }else{
                holder.setText(R.id.doTV, "未开始");
                holder.setVisible(R.id.doTV, true);
            }
//                ViewHelper.setAlpha(holder.getView(R.id.contentTV), 0.3f);
            holder.setTag(R.id.rootView, planDO);
            holder.setOnClickListener(R.id.rootView, alertListener);
        }

        private void convertPlan(BaseViewHolder holder, PlanDO planDO){
            holder.getView(R.id.rootView).setMinimumHeight(ActivityUtils.dpToPx(400 / getItemCount()));
            DateTimeFormatter builder = DateTimeFormat.forPattern("HH : mm");
            DateTime startTime = new DateTime(planDO.getDayTime()).withTime(planDO.getStartHour(), planDO.getStartMinute(), 0, 0);
//                DateTime startTime = new DateTime().withTimeAtStartOfDay().withTime(planDO.getStartHour(), planDO.getStartMinute(), 0, 0);
            holder.setText(R.id.startTimeTV, startTime.toString(builder));
            DateTime endTime = new DateTime(planDO.getDayTime()).withTime(planDO.getEndHour(), planDO.getEndMinute(), 0, 0);
//                DateTime endTime = new DateTime().withTimeAtStartOfDay().withTime(planDO.getEndHour(), planDO.getEndMinute(), 0, 0);
            holder.setText(R.id.endTimeTV, endTime.toString(builder));
            holder.setText(R.id.contentTV, planDO.getContent());
            Interval interv = new Interval(startTime, endTime);
            PeriodFormatter formatter = new PeriodFormatterBuilder()
                    .appendHours()
                    .appendSuffix(" 小时 ")
                    .appendMinutes()
                    .appendSuffix(" 分 ")
                    .toFormatter();
//                holder.setText(R.id.periodTV, interv.toPeriod().toString(formatter));

            TextView contentTV = holder.getView(R.id.contentTV);
            ImageView timelineIV = holder.getView(R.id.timelineIV);
            if(endTime.isBeforeNow()){
                contentTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
                holder.setText(R.id.doTV, "已完成");
                holder.setVisible(R.id.doTV, true);
                timelineIV.setColorFilter(ContextCompat.getColor(mContext, R.color.titleTextColor));
//                    ViewHelper.setAlpha(contentTV, 0.3f);
            }else if(startTime.isAfterNow()){
                holder.setText(R.id.doTV, "未开始");
                holder.setVisible(R.id.doTV, true);
                contentTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
//                    timelineIV.setColorFilter(ContextCompat.getColor(mContext, R.color.titleTextColor));
                timelineIV.setImageResource(R.drawable.timeline_plan_other);
//                    ViewHelper.setAlpha(contentTV, 0.3f);
            }else{
                holder.setText(R.id.doTV, "进行中");
                holder.setVisible(R.id.doTV, true);
                contentTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
//                    timelineIV.setColorFilter(ContextCompat.getColor(mContext, R.color.accent));
                timelineIV.setImageResource(R.drawable.timeline_plan_doing);
//                    ViewHelper.setAlpha(contentTV, 1f);
            }
            FancyButton linkAppFB = holder.getView(R.id.linkAppFB);
            linkAppFB.setTag(planDO);
            linkAppFB.setOnClickListener(linkAppClickListener);
            if(planDO.getLinkAppName() != null) {
                linkAppFB.setIconResource(ThirdAppUtils.getIcon(mContext, planDO.getLinkAppPackageName()));
                linkAppFB.getIconImageObject().setLayoutParams(new LinearLayout.LayoutParams((int)(28 * scale + 0.5f), (int)(28 * scale + 0.5f)));
                linkAppFB.setText(planDO.getLinkAppName());
                linkAppFB.setVisibility(View.VISIBLE);
            }else{
                linkAppFB.setVisibility(View.GONE);
            }
            FlexboxLayout flexboxLayout = holder.getView(R.id.tags_fl);
            if(!TextUtils.isEmpty(planDO.getTags())){
                flexboxLayout.removeAllViews();
                flexboxLayout.setVisibility(View.VISIBLE);
                for(String tag : planDO.getTags().split(",")){
                    TextView textView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_plan_tags, flexboxLayout, false);
                    textView.setText("# " + tag);
                    flexboxLayout.addView(textView);
                }
            }else{
                flexboxLayout.setVisibility(View.GONE);
            }

            holder.setTag(R.id.rootView, planDO);
            holder.setOnClickListener(R.id.rootView, planListener);
        }

        private View.OnClickListener linkAppClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlanDO planDO = (PlanDO) v.getTag();
                ThirdAppUtils.launchapp(mContext, planDO.getLinkAppPackageName());
            }
        };
    }

    public interface PlanListOnClickListener{
        void planListenr(PlanDO planDO);
        void alertListenr(PlanDO planDO);
    }

}
