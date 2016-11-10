package me.xihuxiaolong.justdoit.module.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.EmptyWrapper;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

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

public class PlanListWrapper {

    RecyclerView recyclerView;
    PlanListAdapter adapter;
    EmptyWrapper mEmptyWrapper;
    HeaderAndFooterWrapper mHeaderAndFooterWrapper;

    public PlanListWrapper(Context context, RecyclerView recyclerView, PlanListOnClickListener planListOnClickListener){
        this.recyclerView = recyclerView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new PlanListAdapter(context, new ArrayList<PlanDO>(), planListOnClickListener);
        mEmptyWrapper = new EmptyWrapper(adapter);
        mEmptyWrapper.setEmptyView(LayoutInflater.from(context).inflate(R.layout.recycler_empty, recyclerView, false));
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mEmptyWrapper);
        final View footView = LayoutInflater.from(context).inflate(R.layout.item_plan_bottom, recyclerView, false);
        mHeaderAndFooterWrapper.addFootView(footView);
        recyclerView.setAdapter(mHeaderAndFooterWrapper);
    }

    public void addHeaderView(View headerView){
        mHeaderAndFooterWrapper.addHeaderView(headerView);
    }

    public void setItems(List<PlanDO> items) {
        adapter.setItems(items);
        adapter.notifyDataSetChanged();
        mEmptyWrapper.notifyDataSetChanged();
        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }

    public static class PlanListAdapter extends MultiItemTypeAdapter<PlanDO> {

        PlanListOnClickListener planListOnClickListener;

        float scale;

        public PlanListAdapter(Context context, List<PlanDO> datas, PlanListOnClickListener planListOnClickListener) {
            super(context, datas);
            scale = context.getResources().getDisplayMetrics().density;
            addItemViewDelegate(new AlertItemDelegate());
            addItemViewDelegate(new PlanItemDelegate());
            this.planListOnClickListener = planListOnClickListener;
        }

        public void setItems(List<PlanDO> items) {
            this.mDatas = items;
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

        public class AlertItemDelegate implements ItemViewDelegate<PlanDO> {

            @Override
            public int getItemViewLayoutId() {
                return R.layout.item_alert;
            }

            @Override
            public boolean isForViewType(PlanDO item, int position) {
                return item.getType() == PlanDO.TYPE_ALERT;
            }

            @Override
            public void convert(ViewHolder holder, PlanDO planDO, int position) {
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
        }

        public class PlanItemDelegate implements ItemViewDelegate<PlanDO> {

            @Override
            public int getItemViewLayoutId() {
                return R.layout.item_plan;
            }

            @Override
            public boolean isForViewType(PlanDO item, int position) {
                return item.getType() == PlanDO.TYPE_PLAN;
            }

            @Override
            public void convert(ViewHolder holder, PlanDO planDO, int position) {
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
