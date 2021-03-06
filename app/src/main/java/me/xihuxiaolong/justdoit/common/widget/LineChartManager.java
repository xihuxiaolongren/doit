package me.xihuxiaolong.justdoit.common.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

import me.xihuxiaolong.justdoit.R;

public class LineChartManager {

    private static String lineName = "近七日打卡次数统计";

    /**
     * @Description:创建两条折线
     * @param context 上下文
     * @param mLineChart 折线图控件
     * @param xyValue 折线在x,y轴的值
     */
    public static void initSingleLineChart(Context context, LineChart mLineChart, List<Entry> xyValue) {
        int color = ContextCompat.getColor(context, R.color.titleTextColor);
        initDataStyle(context,mLineChart, xyValue.size());
        //设置折线的样式
        LineDataSet dataSet = new LineDataSet(xyValue, lineName);
        dataSet.setValueTextColor(color);
        dataSet.setColor(color);
        dataSet.setCircleColor(color);
        dataSet.setCircleColorHole(ContextCompat.getColor(context, R.color.colorPrimary));
        dataSet.setDrawValues(true);
        dataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return "" + ((int) value);
            }
        });
        dataSet.setMode(LineDataSet.Mode.LINEAR);
//        dataSet.setDrawFilled(true);
//        dataSet.setDrawHighlightIndicators(true);

//        if (Utils.getSDKInt() >= 18) {
//            // fill drawable only supported on api level 18 and above
//            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.fade_red);
//            dataSet.setFillDrawable(drawable);
//        }
//        else {
//            dataSet.setFillColor(Color.parseColor("#80576269"));
//        }
//        dataSet.enableDashedLine(10f, 10f, 0f);//将折线设置为曲线
//        dataSet.setValueFormatter(new PercentFormatter(new DecimalFormat("%").format()));

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);

        //构建一个LineData  将dataSets放入
        LineData lineData = new LineData(dataSets);
        //将数据插入
        mLineChart.setData(lineData);

        //设置动画效果
        mLineChart.animateY(2000, Easing.EasingOption.Linear);
        mLineChart.animateX(2000, Easing.EasingOption.Linear);
        mLineChart.invalidate();
    }

    /**
     *  @Description:初始化图表的样式
     * @param context
     * @param mLineChart
     * @param size
     */
    private static void initDataStyle(Context context, LineChart mLineChart, final int size) {
        int color = ContextCompat.getColor(context, R.color.titleTextColor);
        int color1 = ContextCompat.getColor(context, R.color.lineChartBackgroundColor);
        //设置图表是否支持触控操作
        mLineChart.setTouchEnabled(false);
        mLineChart.setScaleEnabled(false);
        mLineChart.getDescription().setEnabled(false);
        mLineChart.setBackgroundColor(color1);
        //设置点击折线点时，显示其数值
//        MyMakerView mv = new MyMakerView(context, R.layout.item_mark_layout);
//        mLineChart.setMarkerView(mv);
        //设置折线的描述的样式（默认在图表的左下角）
        Legend title = mLineChart.getLegend();
        title.setForm(Legend.LegendForm.LINE);
        title.setTextColor(color);
//        title.setEnabled(false);
        //设置x轴的样式
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setTextColor(color);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisLineColor(color);
        xAxis.setAxisLineWidth(1);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(size, true);
        xAxis.setTextSize(7f);
//        xAxis.setLabelRotationAngle(300f);
        //设置是否显示x轴
        xAxis.setEnabled(true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int v = (int) ( size - value);
                if(v == 0)
                    return "今日";
                return DateTime.now().minusDays(v).toString(DateTimeFormat.forPattern("M.d"));
            }

        });

        //设置左边y轴的样式
        YAxis yAxisLeft = mLineChart.getAxisLeft();
        yAxisLeft.setAxisLineColor(ContextCompat.getColor(context, R.color.titleTextColor));
        yAxisLeft.setAxisLineWidth(1);
//        DashPathEffect effects = new DashPathEffect(new float[]{10f, 10f}, 0f);
//        yAxisLeft.setAxisLineDashedLine(effects);
        yAxisLeft.setDrawGridLines(false);
        yAxisLeft.setDrawAxisLine(false);
//        yAxisLeft.setAxisMaximum(10f);
//        yAxisLeft.setAxisMinimum(0f);
        yAxisLeft.setDrawZeroLine(false);
        yAxisLeft.setDrawLabels(false);
//        yAxisLeft.setDrawTopYLabelEntry(true);
        //设置右边y轴的样式
        YAxis yAxisRight = mLineChart.getAxisRight();
        yAxisRight.setEnabled(false);

    }

    /**
     * @Description:设置折线的名称
     * @param name
     */
    public static void setLineName(String name){
        lineName = name;
    }

}