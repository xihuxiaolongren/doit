package me.xihuxiaolong.justdoit.common.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.BindView;
import me.xihuxiaolong.justdoit.R;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/9/26.
 */
public class StartAndEndTimeView extends FrameLayout {

    TextView startTimeTV;
    TextView endTimeTV;

    StartAndEndListener startAndEndListener;

    int startHour, startMinute, endHour, endMinute;

    public StartAndEndTimeView(Context context) {
        super(context);
        init(context);
    }

    public StartAndEndTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StartAndEndTimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public StartAndEndTimeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(final Context context) {
        LayoutInflater.from(context).inflate(R.layout.start_and_end_time_view, this);
        startTimeTV = (TextView) findViewById(R.id.startTimeTV);
        endTimeTV = (TextView) findViewById(R.id.endTimeTV);
        startTimeTV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(startAndEndListener != null)
                    startAndEndListener.startClick();
            }
        });
        endTimeTV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(startAndEndListener != null)
                    startAndEndListener.endClick();
            }
        });
    }

    public void setStartAndEndListener(StartAndEndListener startAndEndListener){
        this.startAndEndListener = startAndEndListener;
    }

    public void setStartTime(String s){
        startTimeTV.setText(s);
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }

    public void setStartTime(int hour, int minute){
        this.startHour = hour;
        this.startMinute = minute;
        startTimeTV.setText(String.format("%02d : %02d", startHour, startMinute));
    }

    public void setEndTime(String s){
        endTimeTV.setText(s);
    }

    public void setEndTime(int hour, int minute){
        this.endHour = hour;
        this.endMinute = minute;
        endTimeTV.setText(String.format("%02d : %02d", endHour, endMinute));
    }

    public interface StartAndEndListener{
        void startClick();
        void endClick();
    }

}
