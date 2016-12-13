package me.xihuxiaolong.justdoit.module.redoplandetail;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.xihuxiaolong.justdoit.R;
import me.xihuxiaolong.justdoit.common.database.localentity.PlanDO;
import me.xihuxiaolong.justdoit.common.database.localentity.RedoPlanDO;
import me.xihuxiaolong.justdoit.common.util.BusinessUtils;

public class RedoPlanDetailActivity extends AppCompatActivity {

    public static final String ARG_TARGET = "target";

    int vibrant, titleColor, textColor;

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.persist_tv)
    TextView persistTv;
    @BindView(R.id.redo_tv)
    TextView redoTv;
    @BindView(R.id.time_tv)
    TextView timeTv;
    @BindView(R.id.rootView)
    CardView rootView;

    RedoPlanDO redoPlanDO;
    @BindView(R.id.typeIV)
    ImageView typeIV;
    @BindView(R.id.closeIV)
    ImageView closeIV;
    @BindView(R.id.deleteIV)
    ImageView deleteIV;
    @BindView(R.id.rootFL)
    FrameLayout rootFL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redo_plan_detail);
        ButterKnife.bind(this);
        redoPlanDO = (RedoPlanDO) getIntent().getSerializableExtra(ARG_TARGET);
        vibrant = getIntent().getIntExtra("vibrant", ContextCompat.getColor(this, R.color.sky));
        textColor = getIntent().getIntExtra("textColor", ContextCompat.getColor(this, R.color.titleTextColor));
        redoPlanDO = (RedoPlanDO) getIntent().getSerializableExtra(ARG_TARGET);
        DateTimeFormatter builder = DateTimeFormat.forPattern("HH : mm");
        DateTime startTime = new DateTime(redoPlanDO.getDayTime()).withTime(redoPlanDO.getStartHour(), redoPlanDO.getStartMinute(), 0, 0);
        DateTime endTime = new DateTime(redoPlanDO.getDayTime()).withTime(redoPlanDO.getEndHour(), redoPlanDO.getEndMinute(), 0, 0);
        if (PlanDO.TYPE_PLAN == redoPlanDO.getPlanType()) {
            timeTv.setText(startTime.toString(builder) + " - " + endTime.toString(builder));
            typeIV.setImageResource(R.drawable.icon_plan);
        } else if (PlanDO.TYPE_ALERT == redoPlanDO.getPlanType()) {
            timeTv.setText(startTime.toString(builder));
            typeIV.setImageResource(R.drawable.icon_alert);
        }
        titleTv.setText(redoPlanDO.getContent());
        persistTv.setText("已持续 " + Days.daysBetween(new DateTime(redoPlanDO.getCreatedTime()), DateTime.now()).getDays() + " 天");
        redoTv.setText(BusinessUtils.repeatModeStr(redoPlanDO.getRepeatMode()));
        titleTv.setText(redoPlanDO.getContent());
        closeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                supportFinishAfterTransition();
            }
        });
        deleteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDialog materialDialog = new MaterialDialog.Builder(RedoPlanDetailActivity.this)
                        .content("确认删除该计划吗？")
                        .negativeText("取消")
                        .positiveText("确定")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                                supportFinishAfterTransition();
                            }
                        }).build();
                materialDialog.show();
            }
        });
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        rootFL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                supportFinishAfterTransition();
            }
        });
    }
}
