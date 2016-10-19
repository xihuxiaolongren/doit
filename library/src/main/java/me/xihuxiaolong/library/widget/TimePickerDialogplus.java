package me.xihuxiaolong.library.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import antistatic.spinnerwheel.AbstractWheel;
import antistatic.spinnerwheel.OnWheelChangedListener;
import antistatic.spinnerwheel.adapters.AbstractWheelTextAdapter;
import me.xihuxiaolong.library.R;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 * Date: 16/3/18.
 * 省市区对话框
 */
public class TimePickerDialogplus {

    private DialogPlus dialog;

    private Context mContext;

    private ConfirmListener mConfirmListener;

    DateTime mStartDate, mEndDate;

    AbstractWheel yearWV, monthWV, dayWV;

    public void getDialog(Context context, DateTime startDate, DateTime endDate, ConfirmListener confirmListener) {
        this.mContext = context;
        this.mConfirmListener = confirmListener;
        this.mStartDate = startDate;
        this.mEndDate = endDate;
        dialog = DialogPlus.newDialog(context)
                .setContentHolder(new ViewHolder(R.layout.dialog_time_picker))
                .setGravity(Gravity.BOTTOM)
                .setHeader(R.layout.header)
                .create();
        initView(dialog);
        dialog.show();
    }

    public interface ConfirmListener {
        void selectedTime(DateTime dateTime);
    }

    List<DateTime.Property> yearProperties, monthProperties, dayProperties;

    private void updateYear(AbstractWheel yearWV, DateTime startDate, DateTime endDate){
        yearProperties = new ArrayList<>();
        for(DateTime date = startDate; date.isBefore(endDate); date = date.plusYears(1)){
            yearProperties.add(date.year());
        }
        yearWV.setViewAdapter(new WheelTextAdapter(mContext, yearProperties));
    }

    private void updateMonth(AbstractWheel monthWV, DateTime startDate, DateTime endDate){
        startDate = startDate.isBefore(mStartDate) ?
                mStartDate : startDate;
        endDate = endDate.isBefore(mEndDate) ?
                endDate : mEndDate;
        monthProperties = new ArrayList<>();
        for(DateTime date = startDate; date.isBefore(endDate); date = date.plusMonths(1)){
            monthProperties.add(date.monthOfYear());
        }
        monthWV.setViewAdapter(new WheelTextAdapter(mContext, monthProperties));
    }

    private void updateDays(AbstractWheel dayWV, DateTime startDate, DateTime endDate){
        startDate = startDate.isBefore(mStartDate) ?
                mStartDate : startDate;
        endDate = endDate.isBefore(mEndDate) ?
                endDate : mEndDate;
        dayProperties =  new ArrayList<>();
        for(DateTime date = startDate; date.isBefore(endDate); date = date.plusDays(1)){
            dayProperties.add(date.dayOfMonth());
        }
        dayWV.setViewAdapter(new WheelTextAdapter(mContext, dayProperties));
    }

    private void initView(final DialogPlus dialog) {
        FancyButton confirmBtn = (FancyButton) dialog.findViewById(R.id.confirmBtn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTime.Property dayProperty = dayProperties.get(dayWV.getCurrentItem() % dayProperties.size());
                mConfirmListener.selectedTime(dayProperty.getDateTime());
                dialog.dismiss();
            }
        });
        yearWV = (AbstractWheel) dialog.findViewById(R.id.yearWV);
        yearWV.setInterpolator(new AnticipateOvershootInterpolator());
        yearWV.setCyclic(true);
        monthWV = (AbstractWheel) dialog.findViewById(R.id.monthWV);
        monthWV.setInterpolator(new AnticipateOvershootInterpolator());
        monthWV.setCyclic(true);
        dayWV = (AbstractWheel) dialog.findViewById(R.id.dayWV);
        dayWV.setInterpolator(new AnticipateOvershootInterpolator());
        dayWV.setCyclic(true);

        updateYear(yearWV, mStartDate, mEndDate);
        updateMonth(monthWV, mStartDate, mStartDate.monthOfYear().withMaximumValue());
        updateDays(dayWV, mStartDate, mStartDate.dayOfMonth().withMaximumValue());

        yearWV.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
                DateTime.Property yearProperty = yearProperties.get(yearWV.getCurrentItem() % yearProperties.size());
                DateTime startDate = yearProperty.getDateTime().monthOfYear().withMinimumValue();
                DateTime endDate = yearProperty.getDateTime().monthOfYear().withMaximumValue();
                updateMonth(monthWV, startDate, endDate);
                DateTime.Property monthProperty = monthProperties.get(monthWV.getCurrentItem() % monthProperties.size());
                DateTime startDate1 = monthProperty.getDateTime().dayOfMonth().withMinimumValue();
                DateTime endDate1 = monthProperty.getDateTime().dayOfMonth().withMaximumValue();
                updateDays(dayWV, startDate1, endDate1);
            }
        });

        monthWV.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
                DateTime.Property monthProperty = monthProperties.get(monthWV.getCurrentItem() % monthProperties.size());
                DateTime startDate = monthProperty.getDateTime().dayOfMonth().withMinimumValue();
                DateTime endDate = monthProperty.getDateTime().dayOfMonth().withMaximumValue();
                updateDays(dayWV, startDate, endDate);
            }
        });

        yearWV.setCurrentItem(1);
        monthWV.setCurrentItem(1);
        dayWV.setCurrentItem(1);
    }

    /**
     * Adapter for countries
     */
    private class WheelTextAdapter extends AbstractWheelTextAdapter {
        // Countries names
        private List<DateTime.Property> items;

        /**
         * Constructor
         */
        protected WheelTextAdapter(Context context, List<DateTime.Property> items) {
            super(context, R.layout.time_item, R.id.time_name);
            DateTime.now().year();
            this.items = items;
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return items.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            return items.get(index).getAsText();
        }
    }

}

