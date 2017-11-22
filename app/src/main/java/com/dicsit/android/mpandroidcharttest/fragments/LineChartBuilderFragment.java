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

        ArrayList<Constante> constantes = Constante.getJeux(10, 50f, -20f);
        //ArrayList<Entry> list = FBChart.convertAsEntry(constantes, Entry.class);

        FBChart chart = new FBChart.Builder(getContext(), myView, R.id.linechart_container)
                .setChartType(FBChart.Type.LINE)
                //.setEntries(getEntryValues(10, 60f, 5f))
                //.setEntries(list)
                .setEntries(constantes)
                .setMarkerView(R.layout.custom_marker_view)
                .build();
        chart.show();
    }
}
