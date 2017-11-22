package com.dicsit.android.mpandroidcharttest.internal;

import android.content.Context;
import android.widget.TextView;

import com.dicsit.android.mpandroidcharttest.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by BourF on 17/11/2017.
 */

public class MyMarkerView extends MarkerView {

    TextView tvValue;
    TextView tvDate;
    TextView tvComment;

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        tvValue = findViewById(R.id.tvValue);
        tvDate = findViewById(R.id.tvDate);
        tvComment = findViewById(R.id.tvComment);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        if (e instanceof CandleEntry) {
            CandleEntry ce = (CandleEntry) e;
            tvValue.setText("" + Utils.formatNumber(ce.getHigh(), 0, true));
        } else {
            tvValue.setText("" + Utils.formatNumber(e.getY(), 1, true));
            tvDate.setText(formatDateValue(e.getX()));
            tvComment.setText("Commentaire sur 5 lignes max");
        }

        super.refreshContent(e, highlight);
    }

    private String formatDateValue(float value) {
        SimpleDateFormat format = new SimpleDateFormat("'Le' dd/MM/yyyy 'à' HH:mm");
        long millis = TimeUnit.HOURS.toMillis((long) value);
        return format.format(new Date(millis));
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2 ), -(getHeight() + 75f));
    }
}
