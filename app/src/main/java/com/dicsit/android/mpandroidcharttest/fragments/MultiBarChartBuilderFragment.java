package com.dicsit.android.mpandroidcharttest.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dicsit.android.mpandroidcharttest.R;
import com.dicsit.android.mpandroidcharttest.internal.FBChart;
import com.dicsit.android.mpandroidcharttest.test.Constante;

import java.util.ArrayList;

/**
 * Created by BourF on 21/11/2017.
 */

public class MultiBarChartBuilderFragment extends BaseFragment {

    View myView;

    public MultiBarChartBuilderFragment() {
        // Required empty public constructor
    }

    @NonNull
    public static MultiBarChartBuilderFragment newInstance() {
        return new MultiBarChartBuilderFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_chart, container, false);
        return myView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayList<Constante> constantes1 = Constante.getJeux(10, 60f, 0f);
        ArrayList<Constante> constantes2 = Constante.getJeux(10, 30f, -10f);
        FBChart chart = new FBChart.Builder(getContext(), myView, R.id.chart_container)
                .setChartType(FBChart.Type.Bar)
                .setMaxLimit(true, 60f)
                .setMinLimit(true, 10f)
                .setNormalLimit(true, 15f)
                //.setTextSize(10f)
                //.setValueTextSize(12f)
                //.setLimitLineWidth(4f)
                .setBarWidth(0.4f)
                //.setEntries(getBarEntryValues(10, 60f, 0f), getBarEntryValues(10, 30f, -10f))
                .setEntries(constantes1, constantes2)
                .showMarkers(true)
                .setAnimationEnable(true)
                .build();
        chart.show();
    }
}
