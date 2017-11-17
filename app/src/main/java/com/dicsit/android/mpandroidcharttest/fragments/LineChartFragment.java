package com.dicsit.android.mpandroidcharttest.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.dicsit.android.mpandroidcharttest.R;
import com.dicsit.android.mpandroidcharttest.internal.MyMarkerView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LineChartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LineChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LineChartFragment extends BaseFragment {

    View myView;
    LineChart mChart;

    final int mDefaultColor = ColorTemplate.getHoloBlue();

    public LineChartFragment() {
        // Required empty public constructor
    }

    @NonNull
    public static LineChartFragment newInstance() {
        return new LineChartFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        myView = inflater.inflate(R.layout.fragment_line_chart, container, false);
        return myView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RelativeLayout layout = myView.findViewById(R.id.linechart_container);
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
        mChart.setBackgroundColor(Color.TRANSPARENT);

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
        //mChart.setHighlightPerDragEnabled(false); // true par défaut
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

        LimitLine downLimit = new LimitLine(-20f, "Min");
        downLimit.setLineWidth(4f);
        downLimit.enableDashedLine(10f, 10f, 0f);
        downLimit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        downLimit.setTextSize(10f);

        LimitLine normalLimit = new LimitLine(15f, "Normal");
        normalLimit.setLineWidth(4f);
        normalLimit.enableDashedLine(10f, 10f, 0f);
        normalLimit.setLineColor(Color.GREEN);
        normalLimit.setTextSize(10f);

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
        //leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        //leftAxis.setTextSize(10f);
        //leftAxis.setTextColor(mDefaultColor);
        leftAxis.enableGridDashedLine(10f, 5f, 0f);
        //leftAxis.setDrawGridLines(false);
        //leftAxis.setGranularityEnabled(true);
        leftAxis.setAxisMinimum(-30f);
        leftAxis.setAxisMaximum(60f);
        //leftAxis.setYOffset(-9f);
        //leftAxis.setTextColor(Color.rgb(255, 192, 56));

        // Affiche ou non ligne du 0
        leftAxis.setDrawZeroLine(false);
        //leftAxis.setZeroLineColor(Color.RED);
        //leftAxis.setZeroLineWidth(2f);

        leftAxis.addLimitLine(upLimit);
        leftAxis.addLimitLine(downLimit);
        leftAxis.addLimitLine(normalLimit);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        // Axe Y (droit)
        mChart.getAxisRight().setEnabled(false);

        setData(10,70);

        // Possible uniquemet après avoir défini les données
        mChart.getLegend().setEnabled(false);

        mChart.invalidate();
        //mChart.animateX(500);
    }

    private void setData(int count, float range) {
        // now in hours
        long now = TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis());

        ArrayList<Entry> values = new ArrayList<>();

        float from = now;

        // count = hours
        float to = from + count ;

        // increment by 1 hour
        for (float x = from; x < to; x++) {

            float y = getRandom(range, -20);
            values.add(new Entry(x, y)); // add one entry per hour
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(values, "DataSet 1");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        // Lignes
        set1.setColor(mDefaultColor);
        set1.setLineWidth(3f);

        // Cercles
        set1.setDrawCircles(true); // true par défaut
        set1.setCircleRadius(5f);
        set1.setDrawCircleHole(true);
        set1.setCircleHoleRadius(2.5f);

        // Valeurs (par défaut)
        set1.setDrawValues(true); // true par défaut
        set1.setValueTextColor(mDefaultColor);
        set1.setValueTextSize(12f);

        // Remplissage
        set1.setDrawFilled(true); // false par défaut
        set1.setFillAlpha(50);
        set1.setFillColor(mDefaultColor);

        // Highlights
        set1.setDrawHighlightIndicators(true); // par défaut -> true
        set1.setHighLightColor(mDefaultColor);
        //set1.setHighlightLineWidth(2f);

        // create a data object with the datasets
        LineData data = new LineData(set1);
        //// Valeurs (surchargée, à afficher)
        //data.setValueTextColor(Color.RED);
        //data.setValueTextSize(9f);

        // set data
        mChart.setData(data);

        // LineDataSet  -> Ligne(s) de graphique à afficher
        // LineData     -> TOUTES (LineDataSet[]) les données à afficher
    }
}
