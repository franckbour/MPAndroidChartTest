package com.dicsit.android.mpandroidcharttest.fragments;

import android.os.Bundle;
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
        myView = inflater.inflate(R.layout.fragment_chart, container, false);
        return myView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayList<Constante> constantes = Constante.getTensionJeux(10, 60f, 30f, 30f, 0f);
        FBChart chart = new FBChart.Builder(getContext(), myView, R.id.chart_container)
                .setChartType(FBChart.Type.Tension)
                .setMaxLimit(true, 60f, "Normale haute")
                .setMinLimit(true, 10f, "Normale basse")
                .setCircleRadius(7f)
                //.setTensionEntries(getEntryValues(10, 60f, 30f), getEntryValues(10, 30f, 0f))
                //.setTensionsEntries(FBChart.convertAsTensionEntries(constantes))
                .setTensionEntries(constantes)
                .showMarkers(true)
                .build();
        chart.show();
    }
}
