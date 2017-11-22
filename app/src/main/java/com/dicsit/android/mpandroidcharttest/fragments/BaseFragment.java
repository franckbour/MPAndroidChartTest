package com.dicsit.android.mpandroidcharttest.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BaseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public abstract class BaseFragment extends Fragment {

    protected OnFragmentInteractionListener mListener;

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    protected float getRandom(float range, float startsfrom) {
        return (float) (Math.random() * range) + startsfrom;
    }

    protected ArrayList<Entry> getEntryValues(int nbValues, float range, float startFrom) {
        float from = TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis());
        float to = from + nbValues;
        ArrayList<Entry> values = new ArrayList<>();
        for (float i = from; i < to;i++) {
            float value = (float)(Math.random() * range) + startFrom;
            values.add(new Entry(i, value));
        }
        return values;
    }

    protected ArrayList<BarEntry> getBarEntryValues(int nbValues, float range, float startFrom) {
        float from = TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis());
        float to = from + nbValues;
        ArrayList<BarEntry> values = new ArrayList<>();
        for (float i = from; i < to;i++) {
            float value = (float)(Math.random() * range) + startFrom;
            values.add(new BarEntry(i, value));
        }
        return values;
    }
}
