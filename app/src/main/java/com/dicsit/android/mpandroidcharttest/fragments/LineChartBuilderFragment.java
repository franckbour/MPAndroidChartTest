package com.dicsit.android.mpandroidcharttest.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dicsit.android.mpandroidcharttest.R;
import com.dicsit.android.mpandroidcharttest.internal.FBChart;

/**
 * Created by BourF on 21/11/2017.
 */

public class LineChartBuilderFragment extends BaseFragment {
    View myView;

    public LineChartBuilderFragment() {
        // Required empty public constructor
    }

    @NonNull
    public static LineChartBuilderFragment newInstance() {
        return new LineChartBuilderFragment();
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
        FBChart chart = new FBChart.Builder(getContext(), myView, R.id.linechart_container)
                .setType(FBChart.Type.LINE)
                .setLeftAxisMaximum(70f)
                .setLeftAxisMinimum(0f)
                .setMaxLimit(true, 60f)
                .setMinLimit(true, 10f)
                .setNormalLimit(true, 15f)
                .setTextSize(10f)
                .setValueTextSize(12f)
                .setLimitLineWidth(4f)
                .setLineWidth(3f)
                .setEntries(getEntryValues(10, 60f, 5f))
                .setMarkerView(R.layout.custom_marker_view)
                .build();
        chart.show();
    }
}
