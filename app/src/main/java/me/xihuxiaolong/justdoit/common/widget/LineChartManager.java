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

import java.util.ArrayList;
import java.util.List;

import me.xihuxiaolong.justdoit.R;

public class LineChartManager {

    private static String lineName = "本周打卡统计";
    private static String lineName1 = null;

    /**
     * @Description:创建两条折线
     * @param context 上下文
     * @param mLineChart 折线图控件
     * @param xyValue 折线在x,y轴的值
     */
    public static void initSingleLineChart(Context context, LineChart mLineChart, List<Entry> xyValue) {
        initDataStyle(context,mLineChart);
        int color = ContextCompat.getColor(context, R.color.titleTextColor);
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
     * @Description:创建两条折线
     * @param context 上下文
     * @param mLineChart 折线图控件
     * @param xValues 折线在x轴的值
     * @param yValue 折线在y轴的值
     * @param yValue1 另一条折线在y轴的值
     */
    public static void initDoubleLineChart(Context context, LineChart mLineChart, ArrayList<String> xValues,
                                           ArrayList<Entry> yValue, ArrayList<Entry> yValue1) {

//        initDataStyle(context,mLineChart);
//
//        LineDataSet dataSet = new LineDataSet(yValue, lineName);
//        dataSet.setColor(Color.parseColor("#576269"));
//        dataSet.setCircleColor(Color.parseColor("#576269"));
//        dataSet.setDrawValues(false);
//
//        LineDataSet dataSet1 = new LineDataSet(yValue1, lineName1);
//        dataSet1.enableDashedLine(10f, 10f, 0f);//将折线设置为曲线
//        dataSet1.setColor(Color.parseColor("#576269"));
//        dataSet1.setCircleColor(Color.parseColor("#576269"));
//        dataSet1.setDrawValues(false);
//
//        //构建一个类型为LineDataSet的ArrayList 用来存放所有 y的LineDataSet   他是构建最终加入LineChart数据集所需要的参数
//        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
//
//        //将数据加入dataSets
//        dataSets.add(dataSet);
//        dataSets.add(dataSet1);
//
//        //构建一个LineData  将dataSets放入
//        LineData lineData = new LineData(dataSets);
//        //将数据插入
//        mLineChart.setData(lineData);
//        //设置动画效果
//        mLineChart.animateY(2000, Easing.EasingOption.Linear);
//        mLineChart.animateX(2000, Easing.EasingOption.Linear);
//        mLineChart.invalidate();
    }

    /**
     *  @Description:初始化图表的样式
     * @param context
     * @param mLineChart
     */
    private static void initDataStyle(Context context, LineChart mLineChart) {
        int color = ContextCompat.getColor(context, R.color.titleTextColor);
        //设置图表是否支持触控操作
        mLineChart.setTouchEnabled(false);
        mLineChart.setScaleEnabled(false);
        mLineChart.getDescription().setEnabled(false);
        //设置点击折线点时，显示其数值
//        MyMakerView mv = new MyMakerView(context, R.layout.item_mark_layout);
//        mLineChart.setMarkerView(mv);
        //设置折线的描述的样式（默认在图表的左下角）
        Legend title = mLineChart.getLegend();
        title.setForm(Legend.LegendForm.LINE);
//        title.setEnabled(false);
        //设置x轴的样式
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setTextColor(color);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisLineColor(color);
        xAxis.setAxisLineWidth(1);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(7, true);
        xAxis.setTextSize(8f);
//        xAxis.setLabelRotationAngle(300f);
        //设置是否显示x轴
        xAxis.setEnabled(true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return "周" + ((int) value);
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

    /**
     * @Description:设置另一条折线的名称
     * @param name
     */
    public static void setLineName1(String name){
        lineName1 = name;
    }
}