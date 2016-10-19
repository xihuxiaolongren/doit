package me.xihuxiaolong.justdoit.common.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import me.xihuxiaolong.justdoit.R;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/9/26.
 */
public class SingleTimeView extends FrameLayout {

    TextView timeTV;

    TimeListener timeListener;

    int hour, minute;

    public SingleTimeView(Context context) {
        super(context);
        init(context);
    }

    public SingleTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SingleTimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SingleTimeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(final Context context) {
        LayoutInflater.from(context).inflate(R.layout.single_time_view, this);
        timeTV = (TextView) findViewById(R.id.timeTV);
        timeTV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timeListener != null)
                    timeListener.timeClick();
            }
        });
    }

    public void setTimeListener(TimeListener timeListener){
        this.timeListener = timeListener;
    }

    public void setTime(String s){
        timeTV.setText(s);
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setTime(int hour, int minute){
        this.hour = hour;
        this.minute = minute;
        timeTV.setText(String.format("%02d : %02d", hour, minute));
    }

    public interface TimeListener{
        void timeClick();
    }

}
