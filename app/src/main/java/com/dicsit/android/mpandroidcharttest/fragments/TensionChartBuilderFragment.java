package com.dicsit.android.mpandroidcharttest.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dicsit.android.mpandroidcharttest.R;
import com.dicsit.android.mpandroidcharttest.internal.FBChart;

/**
 * Created by BourF on 21/11/2017.
 */

public class TensionChartBuilderFragment extends BaseFragment {
    protected View myView;

    public TensionChartBuilderFragment() {
    }

    public static TensionChartBuilderFragment newInstance() {
        return new TensionChartBuilderFragment();
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

        FBChart chart = new FBChart.Builder(getContext(), myView, R.id.barchart_container)
                .setType(FBChart.Type.TA)
                .setLeftAxisMaximum(70f)
                .setLeftAxisMinimum(0f)
                .setMaxLimit(true, 60f)
                .setMinLimit(true, 10f)
                .setNormalLimit(true, 15f)
                .setTextSize(10f)
                .setValueTextSize(12f)
                .setLimitLineWidth(4f)
                .setLineWidth(3f)
                .setEntries(getEntryValues(10, 60f, 0f), getEntryValues(10, 60f, 0f))
                .setMarkerView(R.layout.custom_marker_view)
                .build();
        chart.show();
    }
}
