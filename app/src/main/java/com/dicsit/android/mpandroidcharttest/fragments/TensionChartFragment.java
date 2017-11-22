package com.dicsit.android.mpandroidcharttest.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.dicsit.android.mpandroidcharttest.R;
import com.dicsit.android.mpandroidcharttest.internal.MyMarkerView;
import com.github.mikephil.charting.charts.LineChart;;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


/**
 * Created by BourF on 20/11/2017.
 */

public class TensionChartFragment extends BaseFragment {

    protected View myView;
    protected LineChart mChart;

    final int mDefaultColor = ColorTemplate.getHoloBlue();
    private int mFillColor = Color.argb(150, 51, 181, 229);

    public TensionChartFragment() {
    }

    public static TensionChartFragment newInstance() {
        return new TensionChartFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        myView = inflater.inflate(R.layout.fragment_bar_chart, container, false);
        return myView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RelativeLayout layout = myView.findViewById(R.id.barchart_container);
        mChart = new LineChart(getContext());
        showChartInLayout(layout);
    }

    private void showChartInLayout(RelativeLayout layout) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mChart.setLayoutParams(params);

        layout.addView(mChart);

//        Description description = new Description();
//        description.setText("Description du graphique");
//        mChart.setDescription(description);
        mChart.getDescription().setEnabled(false);

        //mChart.setDrawGridBackground(false); // ??
        //mChart.setViewPortOffsets(0f, 0f, 0f, 0f); // ??

        // Couleur de fond
        mChart.setGridBackgroundColor(Color.TRANSPARENT);
        //mChart.setGridBackgroundColor(mDefaultColor);
        //mChart.setGridBackgroundColor(mFillColor);
        mChart.setDrawGridBackground(true);

        // Activation de l'interactivité
        mChart.setTouchEnabled(true); // true par défaut

        // Active/désactive la navigation dans le graphique
        mChart.setDragEnabled(true);

        // Active/désactive les zoom
        mChart.setScaleEnabled(true);
        //mChart.setScaleXEnabled(false);
        //mChart.setScaleYEnabled(false);
        mChart.setPinchZoom(true);

        //mChart.setDragDecelerationEnabled(true); // true par défaut
        //mChart.setDragDecelerationFrictionCoef(0.9f);

        // Highlights
        mChart.setHighlightPerDragEnabled(false); // true par défaut
        //mChart.setHighlightPerTapEnabled(true); // true par défaut
        //mChart.setMaxHighlightDistance(500f); // 500dp par défaut

        MyMarkerView markerView = new MyMarkerView(getContext(), R.layout.custom_marker_view);
        markerView.setChartView(mChart);
        mChart.setMarker(markerView);

        // Définitions des limites
        LimitLine upLimit = new LimitLine(50f, "Max");
        upLimit.setLineWidth(4f);
        upLimit.enableDashedLine(10f, 10f, 0f);
        upLimit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        upLimit.setTextSize(10f);
        upLimit.setTextColor(upLimit.getLineColor());

        LimitLine downLimit = new LimitLine(-20f, "Min");
        downLimit.setLineWidth(4f);
        downLimit.enableDashedLine(10f, 10f, 0f);
        downLimit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        downLimit.setTextSize(10f);
        downLimit.setTextColor(downLimit.getLineColor());

        LimitLine normalLimit = new LimitLine(15f, "Normal");
        normalLimit.setLineWidth(4f);
        normalLimit.enableDashedLine(10f, 10f, 0f);
        normalLimit.setLineColor(Color.GREEN);
        normalLimit.setTextSize(10f);
        normalLimit.setTextColor(normalLimit.getLineColor());

        // Axe X
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(mDefaultColor);
        xAxis.enableGridDashedLine(10f, 5f, 0f);

        // Affiche ou non la ligne de l'axe
        //xAxis.setDrawAxisLine(true); // true par défaut

        // Affiche ou non les lignes de la grille
        //xAxis.setDrawGridLines(true); // true par défaut

        // Centre les labels sur l'axe
        //xAxis.setCenterAxisLabels(true); // false par défaut

        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(1f); // one hour

//        xAxis.setValueFormatter(new IAxisValueFormatter() {
//
//            private SimpleDateFormat mFormat = new SimpleDateFormat("dd/MM/yyyy");
//
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//
//                long millis = TimeUnit.HOURS.toMillis((long) value);
//                return mFormat.format(new Date(millis));
//            }
//        });

        // Axe Y (gauche)
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.setTextSize(10f);
        leftAxis.setTextColor(mDefaultColor);
        //leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        //leftAxis.setTextSize(10f);
        //leftAxis.setTextColor(mDefaultColor);
        leftAxis.enableGridDashedLine(10f, 5f, 0f);
        //leftAxis.setDrawGridLines(false);
        //leftAxis.setGranularityEnabled(true);
        //leftAxis.setAxisMinimum(-30f);
        //leftAxis.setAxisMaximum(60f);
        //leftAxis.setYOffset(-9f);
        //leftAxis.setTextColor(Color.rgb(255, 192, 56));

        // Affiche ou non ligne du 0
        //leftAxis.setDrawZeroLine(false);
        //leftAxis.setDrawGridLines(false);
        //leftAxis.setDrawAxisLine(true);

        //leftAxis.setZeroLineColor(Color.RED);
        //leftAxis.setZeroLineWidth(2f);

        //leftAxis.addLimitLine(upLimit);
        //leftAxis.addLimitLine(downLimit);
        //leftAxis.addLimitLine(normalLimit);

        // limit lines are drawn behind data (and not on top)
        //leftAxis.setDrawLimitLinesBehindData(true);

        // Axe Y (droit)
        mChart.getAxisRight().setEnabled(false);

        setData(10,70);

        // Possible uniquemet après avoir défini les données
        mChart.getLegend().setEnabled(false);

        mChart.invalidate();
        //mChart.animateX(500);
    }

    private void setData(int count, float range) {
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * range) + 50;// + (float)
            // ((mult *
            // 0.1) / 10);
            yVals1.add(new Entry(i, val));
        }

        ArrayList<Entry> yVals2 = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * range) + 150; // + (float)
            // ((mult *
            // 0.1) / 10);
            yVals2.add(new Entry(i, val));
        }

        LineDataSet set1, set2;

        set1 = new LineDataSet(yVals1, "DataSet 1");

        set1.setDrawValues(true);
        set1.setValueTextColor(mDefaultColor);
        set1.setValueTextSize(12f);

        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(mDefaultColor);
        set1.setDrawCircles(true);
        set1.setLineWidth(3f);
        set1.setCircleRadius(5f);
        set1.setDrawCircleHole(true);
        set1.setCircleHoleRadius(2.5f);

        //set1.setDrawFilled(true);
        //set1.setFillAlpha(255);
        //set1.setFillColor(getBackgroundColor());

        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return mChart.getAxisLeft().getAxisMinimum();
            }
        });

        // create a dataset and give it a type
        set2 = new LineDataSet(yVals2, "DataSet 2");

        set2.setDrawValues(true);
        set2.setValueTextColor(mDefaultColor);
        set2.setValueTextSize(12f);

        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
        set2.setColor(mDefaultColor);
        set2.setDrawCircles(true);
        set2.setLineWidth(3f);
        set2.setCircleRadius(5f);
        set2.setDrawCircleHole(true);
        set2.setCircleHoleRadius(2.5f);

        //set2.setDrawFilled(true);
        //set2.setFillAlpha(255);
        //set2.setFillColor(getBackgroundColor());

        set2.setHighLightColor(Color.rgb(244, 117, 117));
        set2.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return mChart.getAxisLeft().getAxisMaximum();
            }
        });

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets
        dataSets.add(set2);

        // create a data object with the datasets
        LineData data = new LineData(dataSets);
        //data.setDrawValues(false);

        // set data
        mChart.setData(data);

    }

    private int getBackgroundColor() {
        return getThemeColor(android.R.attr.windowBackground);
    }

    private int getThemeColor(int resId) {
        TypedValue a = new TypedValue();
        getContext().getTheme().resolveAttribute(resId, a ,true);
        if (a.type >= TypedValue.TYPE_FIRST_COLOR_INT && a.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            return a.data;
        }
        return -1;
    }
}
