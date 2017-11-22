package com.dicsit.android.mpandroidcharttest.internal;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.AttrRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dicsit.android.mpandroidcharttest.R;
import com.dicsit.android.mpandroidcharttest.test.Constante;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Classe d'implémentation de graphique MPAndroidChart
 *
 * Supporte les graphiques de types LineChart et BarChart uniquement.
 *
 * @version 1
 * @since 21/11/2017
 * @author Franck Bour
 *
 */

public class FBChart {

    private final Context mContext;
    private Type mType;
    private Class<? extends BarLineChartBase<?>> mClass;

    private List<ArrayList<? extends Entry>> mEntries;
    private ArrayList<Entry> mSystolEntries;
    private ArrayList<Entry> mDiastolEntries;

    private View mParent;

    private int resId;
    private int markerViewResId;
    private int mValueTextColor;

    private boolean mXAxisDateFormatEnable;
    private boolean mAnimationEnable;
    private boolean mShowLegend;

    private boolean mMaxLimitEnable;
    private boolean mMinLimitEnable;
    private boolean mNormalLimitEnable;
    private boolean mDrawFilledEnable;

    private float mMaxLimitValue;
    private float mMinLimitValue;
    private float mNormalLimitValue;

    private float mLeftAxisMaximum;
    private float mLeftAxisMinimum;

    private float mTextSize;
    private float mValueTextSize;
    private float mLineWidth;
    private float mLimitLineWidth;
    private float mBarWidth;
    private float mCircleRadius;

    private String mMaxLimitLabel;
    private String mMinLimitLabel;
    private String mNormalLimitLabel;

    private BarLineChartBase<?> mChart;

    private static final int mDefaultColor = ColorTemplate.getHoloBlue();
    private static final float mLeftAxisMinMaxOffset = 5f;

    private DataMinMax minMaxData;
    private DataMinMax getMinMaxData() {
        if (minMaxData == null)
            minMaxData = getDataMinValue();
        return minMaxData;
    }

    /**
     * Type de graphique
     */
    public enum Type {
        Line,
        Bar,
        Tension,
        UNKNOWN
    }

    /**
     * Constructeur
     * @param builder FB chart builder
     */
    private FBChart(Builder builder) {
        this.mContext = builder.context;
        this.mParent = builder.parent;
        this.resId = builder.resId;

        this.mType = builder.type;
        this.mClass = builder.clazz;
        this.mEntries = builder.entries;
        this.mSystolEntries = builder.systolEntries;
        this.mDiastolEntries = builder.diastolEntries;

        this.mXAxisDateFormatEnable = builder.xAxisDateFormatEnable;
        this.mAnimationEnable = builder.animationEnable;
        this.mDrawFilledEnable = builder.drawFilledEnable;
        this.mShowLegend = builder.showLegend;

        this.mMaxLimitEnable = builder.maxLimitEnable;
        this.mMinLimitEnable = builder.minLimitEnable;
        this.mNormalLimitEnable = builder.normalLimitEnable;
        this.mMaxLimitValue = builder.maxLimitValue;
        this.mMinLimitValue = builder.minLimitValue;
        this.mNormalLimitValue = builder.normalLimitValue;
        this.mMaxLimitLabel = builder.maxLimitLabel;
        this.mMinLimitLabel = builder.minLimitLabel;
        this.mNormalLimitLabel = builder.normalLimitLabel;

        this.markerViewResId = builder.markerViewResId;

        this.mLeftAxisMaximum = builder.customLeftAxisMaxValue ? builder.leftAxisMaximum : getDefaultLeftAxisMaxValue();
        this.mLeftAxisMinimum = builder.customLeftAxisMinValue ? builder.leftAxisMinimum : getDefaultLeftAxisMinValue();

        this.mTextSize = builder.textSize;
        this.mValueTextSize = builder.valueTextSize;
        this.mValueTextColor = builder.customValueTextColor ? builder.valueTextColor : getDefaultTextColor();
        this.mLineWidth = builder.lineWidth;
        this.mLimitLineWidth = builder.limitLineWidth;
        this.mBarWidth = builder.barWidth;
        this.mCircleRadius = builder.circleRadius;

        configure();
    }

    /**
     * Création de l'instance du graphique
     * @return instance
     */
    @Nullable
    private BarLineChartBase<?> getChartInstance() {
        if (mClass == null)
            return null;

        Object instance = null;
        try {
            instance = mClass.getConstructor(Context.class).newInstance(mContext);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return (BarLineChartBase<?>) instance;
    }

    /**
     * Configure le graphique
     */
    private void configure() {
        this.mChart = getChartInstance();
        if (this.mChart != null) {
            configureAxes();
        }
    }

    /**
     * Configure les axes du graphique
     */
    private void configureAxes() {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mChart.setLayoutParams(params);

        ViewGroup layout = mParent.findViewById(resId);
        layout.addView(mChart);

        mChart.getDescription().setEnabled(false);

        // Couleur de fond
        mChart.setBackgroundColor(Color.TRANSPARENT);
        //mChart.setDrawGridBackground(false);

        // Activation de l'interactivité
        mChart.setTouchEnabled(true); // true par défaut

        // Active/désactive la navigation dans le graphique
        mChart.setDragEnabled(true);

        // Active/désactive les zoom
        mChart.setScaleEnabled(true);
        mChart.setPinchZoom(true);

        // Highlights
        mChart.setHighlightPerDragEnabled(false); // true par défaut
        //mChart.setHighlightPerTapEnabled(true); // true par défaut
        //mChart.setMaxHighlightDistance(500f); // 500dp par défaut

        if (markerViewResId != -1) {
            FBMarkerView markerView = new FBMarkerView(mContext, markerViewResId);
            markerView.setChartView(mChart);
            mChart.setMarker(markerView);
        }

        // Définitions des limites
        LimitLine upLimit = new LimitLine(mMaxLimitValue, mMaxLimitLabel);
        upLimit.setLineWidth(mLimitLineWidth);
        upLimit.enableDashedLine(10f, 10f, 0f);
        upLimit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        upLimit.setTextSize(mTextSize);
        upLimit.setTextColor(upLimit.getLineColor());

        LimitLine downLimit = new LimitLine(mMinLimitValue, mMinLimitLabel);
        downLimit.setLineWidth(mLimitLineWidth);
        downLimit.enableDashedLine(10f, 10f, 0f);
        downLimit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        downLimit.setTextSize(mTextSize);
        downLimit.setTextColor(downLimit.getLineColor());

        LimitLine normalLimit = new LimitLine(mNormalLimitValue, mNormalLimitLabel);
        normalLimit.setLineWidth(mLimitLineWidth);
        normalLimit.enableDashedLine(10f, 10f, 0f);
        normalLimit.setLineColor(Color.GREEN);
        normalLimit.setTextSize(mTextSize);
        normalLimit.setTextColor(normalLimit.getLineColor());

        // Axe X
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(mTextSize);
        xAxis.setTextColor(mDefaultColor);
        xAxis.enableGridDashedLine(10f, 5f, 0f);

        // Centre les labels sur l'axe
        xAxis.setCenterAxisLabels(true); // false par défaut

        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(1f); // one hour

        if (mXAxisDateFormatEnable) {
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                private SimpleDateFormat mFormat = new SimpleDateFormat("dd/MM HH:mm", Locale.getDefault());
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    long millis = TimeUnit.HOURS.toMillis((long) value);
                    return mFormat.format(new Date(millis));
                }
            });
        }

        // Axe Y (gauche)
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.setTextSize(mTextSize);
        leftAxis.setTextColor(mDefaultColor);
        leftAxis.enableGridDashedLine(10f, 5f, 0f);
        leftAxis.setAxisMinimum(mLeftAxisMinimum);
        leftAxis.setAxisMaximum(mLeftAxisMaximum);

        // Affiche ou non ligne du 0
        leftAxis.setDrawZeroLine(false);

        if (mMaxLimitEnable) leftAxis.addLimitLine(upLimit);
        if (mMinLimitEnable) leftAxis.addLimitLine(downLimit);
        if (mNormalLimitEnable) leftAxis.addLimitLine(normalLimit);

        // limit lines are drawn behind data (and not on top)
        //leftAxis.setDrawLimitLinesBehindData(true);

        // Axe Y (droit)
        mChart.getAxisRight().setEnabled(false);

        // Affecte les données
        setData();
    }

    /**
     * Insertion des données dans le graphique
     */
    @SuppressWarnings("unchecked")
    private void setData() {
        if (mType == Type.Tension) {
            if (mSystolEntries == null || mDiastolEntries == null) return;
            setTensionData(mSystolEntries, mDiastolEntries);
        } else {
            if (mEntries == null) return;
            if (mChart instanceof LineChart) {
                setLineData(mEntries.toArray(new ArrayList[mEntries.size()]));
            } else if (mChart instanceof BarChart) {
                setBarData(mEntries.toArray(new ArrayList[mEntries.size()]));
            }
        }
    }

    /**
     * Insertion des données dans le graphique de type Tension
     * @param entriesSystolic entrée systoliques
     * @param entriesDiastolic entrée diastoliques
     */
    @SuppressWarnings("unchecked")
    private void setTensionData(ArrayList<Entry> entriesSystolic, ArrayList<Entry> entriesDiastolic) {
        if (entriesDiastolic == null || entriesSystolic == null) return;
        List<ILineDataSet> datasets = new ArrayList<>();

        // Systolic
        LineDataSet setSystolic = new LineDataSet(entriesSystolic, "Systolique");
        setDefaultLineConfiguration(setSystolic, true, true);

        // Diastolic
        LineDataSet setDiastolic = new LineDataSet(entriesDiastolic, "Diastolique");
        setDefaultLineConfiguration(setDiastolic, true, false);

        datasets.add(setSystolic);
        datasets.add(setDiastolic);

        // create a data object with the datasets
        LineData data = new LineData(datasets);

        // set data
        ((LineChart) mChart).setData(data);
    }

    /**
     * Insertion des données dans le graphique de type Line
     * @param lists listes des données
     */
    @SuppressWarnings("unchecked")
    private void setLineData(ArrayList<Entry>... lists) {
        if (lists == null || lists.length == 0) return;
        List<ILineDataSet> datasets = new ArrayList<>();
        int index = 0;
        for (ArrayList<Entry> list : lists) {
            LineDataSet set = new LineDataSet(list, "DataSet "+ (index + 1));
            setDefaultLineConfiguration(set);
            datasets.add(set);
            index++;
        }

        // create a data object with the datasets
        LineData data = new LineData(datasets);

        // set data
        ((LineChart) mChart).setData(data);
    }

    /**
     * Insertion des données dans le graphique de type Bar
     * @param lists listes des données
     */
    @SuppressWarnings("unchecked")
    private void setBarData(ArrayList<BarEntry>... lists) {
        if (lists == null || lists.length == 0) return;
        List<IBarDataSet> dataSets = new ArrayList<>();
        int index = 0;
        for (ArrayList<BarEntry> list : lists) {
            boolean even = index % 2 == 0;
            BarDataSet set = new BarDataSet(list, "DataSet " + index++);
            set.setAxisDependency(YAxis.AxisDependency.LEFT);
            set.setColor(even ? mDefaultColor :  Color.RED, 150); // chaque bar avec une couleur différente
            set.setBarShadowColor(Color.RED);
            set.setHighLightAlpha(255);
            set.setDrawValues(true); // true par défaut
            set.setValueTextColor(mValueTextColor);
            set.setValueTextSize(mValueTextSize);
            set.setHighLightColor(even ? mDefaultColor : Color.RED);
            dataSets.add(set);
        }

        // create a data object with the datasets
        BarData data = new BarData(dataSets);

        // set data
        BarChart barChart = (BarChart) mChart;
        barChart.setData(data);

        // Configuration spécifique
        barChart.getBarData().setBarWidth(mBarWidth);
        if (lists.length > 1) {
            barChart.groupBars(lists[0].get(0).getX(), 0.08f, 0.03f);
        }
    }

    /**
     * Configuration par défaut des lignes pour les graphique de type LineChart
     * @param set LineDataSet
     */
    private void setDefaultLineConfiguration(LineDataSet set) {
        setDefaultLineConfiguration(set, false, false);
    }

    /**
     * Configuration par défaut des lignes pour les graphique de type LineChart
     * @param set LineDataSet
     */
    private void setDefaultLineConfiguration(LineDataSet set, boolean isTension, final boolean isSystolic) {
        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        // Lignes
        set.setColor(mDefaultColor);
        set.setLineWidth(mLineWidth);

        // Cercles
        set.setDrawCircles(true); // true par défaut
        set.setCircleRadius(mCircleRadius);
        set.setDrawCircleHole(true);
        set.setCircleHoleRadius(mCircleRadius / 2);

        // Valeurs (par défaut)
        set.setDrawValues(true); // true par défaut
        set.setValueTextColor(mValueTextColor);
        set.setValueTextSize(mValueTextSize);

        // Highlights
        set.setDrawHighlightIndicators(true); // par défaut -> true
        set.setHighLightColor(Color.argb(255,255,87,34));
        set.setHighlightLineWidth(1f);

        // Remplissage
        if (isTension) {
            set.setDrawFilled(true);
            set.setFillAlpha(255);
            set.setFillColor(getDefaultBackgroundColor());
            set.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return isSystolic ? mChart.getAxisLeft().getAxisMaximum() : mChart.getAxisLeft().getAxisMinimum();
                }
            });
            mChart.setGridBackgroundColor(mDefaultColor);
            mChart.setDrawGridBackground(true);
        } else {
            set.setDrawFilled(mDrawFilledEnable); // false par défaut
            set.setFillAlpha(50);
            set.setFillColor(mDefaultColor);
        }
    }

    /**
     * Affiche le graphique
     */
    public void show() {
        // Possible uniquemet après avoir défini les données
        if (mChart != null) {
            // Legend (uniquement après affectation des données)
            mChart.getLegend().setEnabled(mShowLegend);
            mChart.getLegend().setTextColor(getDefaultTextColor());
            if (this.mAnimationEnable) {
                mChart.animateX(500);
            } else {
                mChart.invalidate();
            }
        } else {
            String error = "Aucun type de graphique défini";
            Log.w("FBChart", error);
            if (mParent instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) mParent;
                TextView textView = new TextView(mContext);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                textView.setLayoutParams(params);
                textView.setText(error);
                textView.setTextColor(mDefaultColor);
                textView.setGravity(Gravity.CENTER);
                viewGroup.addView(textView);
            }
        }
    }

    /**
     * Retourne la valeur minimale de l'axe de l'ordonnée
     * @return minimale
     */
    private float getDefaultLeftAxisMinValue() {
        return getMinMaxData() != null ? getMinMaxData().getMin() - mLeftAxisMinMaxOffset : 0f;
    }

    /**
     * Retourne la valeur maximale de l'axe de l'ordonnée
     * @return maximale
     */
    private float getDefaultLeftAxisMaxValue() {
        return getMinMaxData() != null ? getMinMaxData().getMax() + mLeftAxisMinMaxOffset : 0f;
    }

    /**
     * Retourne les valeurs minimale et maximales de l'axe de l'ordonnée
     * @return Objet DataMinMax
     */
    @NonNull
    private DataMinMax getDataMinValue() {
        float minValue = 0f;
        float maxValue = 0f;

        if (mType == Type.Tension && mSystolEntries != null && mDiastolEntries != null) {
            Entry minEntry = Collections.min(mDiastolEntries, entryComparator);
            Entry maxEntry = Collections.max(mSystolEntries, entryComparator);
            minValue = minEntry.getY();
            maxValue = maxEntry.getY();
        } else if (mEntries != null) {
            List<Entry> minimums = new ArrayList<>();
            List<Entry> maximums = new ArrayList<>();

            for (ArrayList<? extends Entry> values : mEntries) {
                Entry minEntry = Collections.min(values, entryComparator);
                Entry maxEntry = Collections.max(values, entryComparator);
                minimums.add(minEntry);
                maximums.add(maxEntry);
            }

            if (!minimums.isEmpty()) {
                Entry e = Collections.min(minimums, entryComparator);
                minValue = e.getY();
            }

            if (!maximums.isEmpty()) {
                Entry e = Collections.max(maximums, entryComparator);
                maxValue = e.getY();
            }
        }
        return new DataMinMax(minValue, maxValue);
    }

    /**
     * Comparateur de tri d'objet Entry ( tri sur getY() )
     */
    private Comparator<Entry> entryComparator = new Comparator<Entry>() {
        @Override
        public int compare(Entry o1, Entry o2) {
            int value = 0;
            if (o1.getY() > o2.getY()) {
                value = 1;
            } else if (o1.getY() < o2.getY()) {
                value = -1;
            }
            return value;
        }
    };

    /**
     * Retourne la couleur du background de la fenêtre
     * @return couleur
     */
    private int getDefaultBackgroundColor() {
        return getThemeColor(android.R.attr.windowBackground);
    }

    /**
     * Retourne la couleur du texte par défaut
     * @return couleur
     */
    private int getDefaultTextColor() {
        return new TextView(mContext).getCurrentTextColor();
    }

    /**
     * Retourne la couleur associée à un attribut Android
     * @param resId id attribut
     * @return couleur
     */
    private int getThemeColor(@AttrRes int resId) {
        TypedValue a = new TypedValue();
        mContext.getTheme().resolveAttribute(resId, a ,true);
        if (a.type >= TypedValue.TYPE_FIRST_COLOR_INT && a.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            return a.data;
        }
        return -1;
    }

    /**
     * Objet DataMinMax pour stocker les valeurs minimales et maximales
     */
    private class DataMinMax {
        private float min;
        private float max;

        private float getMin() {
            return min;
        }
        private float getMax() {
            return max;
        }

        private DataMinMax() { /* empty constructor */ }
        private DataMinMax(float min, float max) {
            this.min = min;
            this.max = max;
        }
    }

    /**
     * FB Custom Marker view
     */
    private class FBMarkerView extends MarkerView {

        TextView tvValue;
        TextView tvDate;
        TextView tvComment;

        /**
         * Constructor. Sets up the MarkerView with a custom layout resource.
         *
         * @param context context
         * @param layoutResource the layout resource to use for the MarkerView
         */
        public FBMarkerView(Context context, int layoutResource) {
            super(context, layoutResource);
            tvValue = findViewById(R.id.tvValue);
            tvDate = findViewById(R.id.tvDate);
            tvComment = findViewById(R.id.tvComment);
        }

        @Override
        public void refreshContent(Entry e, Highlight highlight) {

            if (e instanceof CandleEntry) {
                CandleEntry ce = (CandleEntry) e;
                tvValue.setText(String.format("%s (unit)", Utils.formatNumber(ce.getHigh(), 2, false)));
            } else {
                tvValue.setText(String.format("%s (unit)", Utils.formatNumber(e.getY(), 2, false)));
                tvDate.setText(formatDateValue(e.getX()));
                tvComment.setText("Test commentaire sur 5 lignes max");
            }

            tvValue.setTextSize(16f);
            tvValue.setTypeface(tvValue.getTypeface(), Typeface.BOLD);
            tvDate.setTypeface(tvDate.getTypeface(), Typeface.BOLD_ITALIC);
            tvComment.setTypeface(tvComment.getTypeface(), Typeface.ITALIC);

            super.refreshContent(e, highlight);
        }

        private String formatDateValue(float value) {
            SimpleDateFormat format = new SimpleDateFormat("'Le' dd/MM/yyyy 'à' HH:mm", Locale.getDefault());
            long millis = TimeUnit.HOURS.toMillis((long) value);
            return format.format(new Date(millis));
        }

        @Override
        public MPPointF getOffset() {
            return new MPPointF(-(getWidth() / 2 ), -(getHeight() + 75f));
        }
    }

    /**
     * Converti une liste de constante en Entry
     * @param arrayList liste de constante
     * @param clazz class of Entry
     * @param <T> Type of Entry
     * @return liste of Entry
     */
    private static <T extends Entry> ArrayList<T> convertAsEntry(List<Constante> arrayList, Class<T> clazz) {
        ArrayList<T> list = new ArrayList<>();
        if (arrayList != null && arrayList.size() > 0) {
            for (Constante c : arrayList) {
                T item = null;
                try {
                    Constructor<T> constructor = clazz.getConstructor(float.class, float.class);
                    item = constructor.newInstance(c.getDate(), c.getValue());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                if (item != null) {
                    item.setData(c);
                    item.setX(c.getDate());
                    item.setY(c.getValue());
                    list.add(item);
                }
            }
        }
        return list;
    }

    /**
     * Converti une liste de constante en objet TensionEntries
     * @param arrayList liste de constante tension
     * @return tensionEntries
     */
    private static TensionEntries convertAsTensionEntries(List<Constante> arrayList) {
        TensionEntries entries = new TensionEntries();
        if (arrayList != null && arrayList.size() > 0) {
            int index = 0;
            for (Constante constante : arrayList) {
                Entry systol = new Entry();
                Entry diastol = new Entry();

                String[] splitValues = constante.getTaValues().split("\\|");

                float vSystol = Float.parseFloat(splitValues[0]);
                float vDiastol = Float.parseFloat(splitValues[1]);

                systol.setX(constante.getDate());
                systol.setY(vSystol);
                systol.setData(constante);

                diastol.setX(constante.getDate());
                diastol.setY(vDiastol);
                diastol.setData(constante);

                entries.addEntries(index, systol, diastol);
                index++;
            }
        }
        return entries;
    }

    /**
     * Classe TensionEntries
     */
    private static class TensionEntries {
        ArrayList<Entry> systolics = new ArrayList<>();
        ArrayList<Entry> diastolics = new ArrayList<>();

        private ArrayList<Entry> getSystolics() {
            return systolics;
        }
        private ArrayList<Entry> getDiastolics() {
            return diastolics;
        }

        private TensionEntries() { /* empty constuctor */}

        /**
         * Ajoute des entrées
         * @param index index
         * @param systol systoliques
         * @param diastol diastoliques
         */
        private void addEntries(int index, Entry systol, Entry diastol) {
            systolics.add(index, systol);
            diastolics.add(index, diastol);
        }
    }

    /**
     * FB Chart Builder
     */
    public static class Builder {
        private final Context context;
        private Type type;

        private Class<? extends BarLineChartBase<?>> clazz;
        private Class<? extends Entry> entryClazz;

        private List<ArrayList<? extends Entry>> entries;
        private ArrayList<Entry> systolEntries;
        private ArrayList<Entry> diastolEntries;

        private View parent;
        private int resId;

        private int markerViewResId = -1;
        private int valueTextColor;

        private boolean xAxisDateFormatEnable = true;
        private boolean animationEnable = false;
        private boolean maxLimitEnable = false;
        private boolean minLimitEnable = false;
        private boolean normalLimitEnable = false;
        private boolean drawFilledEnable = false;

        private boolean customLeftAxisMinValue = false;
        private boolean customLeftAxisMaxValue = false;
        private boolean customValueTextColor = false;
        private boolean showLegend = false;

        private float leftAxisMaximum;
        private float leftAxisMinimum;
        private float maxLimitValue;
        private float minLimitValue;
        private float normalLimitValue;
        private float textSize = 10f;
        private float valueTextSize = 12f;
        private float lineWidth = 3f;
        private float limitLineWidth = 4f;
        private float barWidth = 0.5f;
        private float circleRadius = 5f;

        private String maxLimitLabel = "Max";
        private String minLimitLabel = "Min";
        private String normalLimitLabel = "Normal";

        /**
         * Définit le type de graphique
         * @param type type
         */
        private void setType(Type type) {
            this.type = type;
            if (this.type == Type.Tension) this.entries = null;
            setClassesByType(this.type);
        }

        /**
         * Constructeur
         * @param context contexte
         * @param layout vue parente
         * @param resId id du container du graphique
         */
        public Builder(Context context, View layout, @IdRes int resId) {
            this.context = context;
            this.parent = layout;
            this.resId = resId;
        }

        /**
         * Définit le type de graphique
         * @param type type
         * @return builder
         */
        public Builder setChartType(Type type) {
            setType(type);
            return this;
        }

        /**
         * Définit les données à afficher
         * @param entries listes des données
         * @return builder
         */
        @SafeVarargs
        public final Builder setEntries(ArrayList<? extends Entry>... entries) {
            List<ArrayList<? extends Entry>> list = new ArrayList<>();
            if (entries != null && entries.length > 0) {
                Collections.addAll(list, entries);
            }
            this.entries = list;
            return this;
        }

        /**
         * Définir les données à afficher
         * @param constantes listes des constantes
         * @return builder
         */
        @SafeVarargs
        public final Builder setEntries(List<Constante>... constantes) {
            List<ArrayList<? extends Entry>> list = new ArrayList<>();
            if (constantes != null && constantes.length > 0) {
                for(List<Constante> liste : constantes) {
                    list.add(convertAsEntry(liste, this.entryClazz));
                }
            }
            this.entries = list;
            return this;
        }

        /**
         * Définit les données de tension à afficher
         * @param systolics données systoliques
         * @param diastolics données diastoliques
         * @return builder
         */
        public Builder setTensionEntries(ArrayList<Entry> systolics, ArrayList<Entry> diastolics) {
            setType(Type.Tension);
            this.systolEntries = systolics;
            this.diastolEntries = diastolics;
            return this;
        }

        /**
         * Définit les données de tension à afficher
         * @param tensionEntries données de tension
         * @return builder
         */
        public Builder setTensionEntries(TensionEntries tensionEntries) {
            setType(Type.Tension);
            this.systolEntries = tensionEntries.getSystolics();
            this.diastolEntries = tensionEntries.getDiastolics();
            return this;
        }

        /**
         * Définit les données de tension à afficher
         * @param constantes liste des constantes tension
         * @return builder
         */
        public Builder setTensionEntries(List<Constante> constantes) {
            setType(Type.Tension);
            TensionEntries tensionEntries = convertAsTensionEntries(constantes);
            this.systolEntries = tensionEntries.getSystolics();
            this.diastolEntries = tensionEntries.getDiastolics();
            return this;
        }

        /**
         * Définit si le format de l'axe de l'abscisse est de type date
         * @param enable active/désactive
         * @return builder
         */
        public Builder setXAxisDateFormatEnable(boolean enable) {
            this.xAxisDateFormatEnable = enable;
            return this;
        }

        /**
         * Activation des animations
         * @param enable active/désactive
         * @return builder
         */
        public Builder setAnimationEnable(boolean enable) {
            this.animationEnable = enable;
            return this;
        }

        /**
         * Définit la limite maximale
         * @param enable active/désactive
         * @param value valeur de la limite
         * @return builder
         */
        public Builder setMaxLimit(boolean enable, float value) {
            this.maxLimitEnable = enable;
            this.maxLimitValue = value;
            return this;
        }

        /**
         * Définit la limite maximale
         * @param enable active/désactive
         * @param value valeur de la limite
         * @param label label de la limite
         * @return builder
         */
        public Builder setMaxLimit(boolean enable, float value, String label) {
            this.maxLimitEnable = enable;
            this.maxLimitValue = value;
            this.maxLimitLabel = label;
            return this;
        }

        /**
         * Définit la limite minimale
         * @param enable active/désactive
         * @param value valeur de la limite
         * @return builder
         */
        public Builder setMinLimit(boolean enable, float value) {
            this.minLimitEnable = enable;
            this.minLimitValue = value;
            return this;
        }

        /**
         * Définit la limite minimale
         * @param enable active/désactive
         * @param value valeur de la limite
         * @param label label de la limite
         * @return builder
         */
        public Builder setMinLimit(boolean enable, float value, String label) {
            this.minLimitEnable = enable;
            this.minLimitValue = value;
            this.minLimitLabel = label;
            return this;
        }

        /**
         * Définit la limite normale
         * @param enable active/désactive
         * @param value valeur de la limite
         * @return builder
         */
        public Builder setNormalLimit(boolean enable, float value) {
            this.normalLimitEnable = enable;
            this.normalLimitValue = value;
            return this;
        }

        /**
         * Définit la limite normale
         * @param enable active/désactive
         * @param value valeur de la limite
         * @param label label de la limite
         * @return builder
         */
        public Builder setNormalLimit(boolean enable, float value, String label) {
            this.normalLimitEnable = enable;
            this.normalLimitValue = value;
            this.normalLimitLabel = label;
            return this;
        }

        /**
         * Définition de la vue des marqueurs
         * @param resId id de la ressource
         * @return builder
         */
        public Builder setMarkerView(@LayoutRes int resId) {
            this.markerViewResId = resId;
            return this;
        }

        /**
         * Définit la valeur maximale de l'axe de l'ordonnée
         * @param value maximale
         * @return builder
         */
        public Builder setLeftAxisMaximum(float value) {
            this.customLeftAxisMaxValue = true;
            this.leftAxisMaximum = value;
            return this;
        }

        /**
         * Définit la valeur minimale de l'axe de l'ordonnée
         * @param value minimale
         * @return builder
         */
        public Builder setLeftAxisMinimum(float value) {
            this.customLeftAxisMinValue = true;
            this.leftAxisMinimum = value;
            return this;
        }

        /**
         * Définit la taille du texte des valeurs
         * @param size taille
         * @return builder
         */
        public Builder setValueTextSize(float size) {
            this.valueTextSize = size;
            return this;
        }

        /**
         * Définit la taille du texte
         * @param size taille
         * @return builder
         */
        public Builder setTextSize(float size) {
            this.textSize = size;
            return this;
        }

        /**
         * Définit l'épaisseur des lignes
         * @param size épaisseur
         * @return builder
         */
        public Builder setLineWidth(float size) {
            this.lineWidth = size;
            return this;
        }

        /**
         * Définit l'épaisseur des lignes des limites
         * @param size épaisseur
         * @return builder
         */
        public Builder setLimitLineWidth(float size) {
            this.limitLineWidth = size;
            return this;
        }

        /**
         * Définit l'épaisseur des barres
         * @param size épaisseur
         * @return builder
         */
        public Builder setBarWidth(float size) {
            this.barWidth = size;
            return this;
        }

        /**
         * Affichage du remplissage
         * @param enable active/désactive
         * @return builder
         */
        public Builder setDrawFilled(boolean enable) {
            this.drawFilledEnable = enable;
            return this;
        }

        /**
         * Définit le radius des cercles
         * @param value radius
         * @return builder
         */
        public Builder setCircleRadius(float value) {
            this.circleRadius = value;
            return this;
        }

        /**
         * Définit la couleur du texte des valeurs
         * @param color couleur
         * @return builder
         */
        public Builder setValueTextColor(int color) {
            this.customValueTextColor = true;
            this.valueTextColor = color;
            return this;
        }

        /**
         * Affichage de la légénde
         * @param enable active/désactive
         * @return builder
         */
        public Builder showLegend(boolean enable) {
            this.showLegend = enable;
            return this;
        }

        /**
         * Construit le graphique
         * @return Dicsit Chart
         */
        public FBChart build() {
            return new FBChart(this);
        }

        /**
         * Définit les types de classes à utiliser en fonction du type
         * @param type type
         */
        private void setClassesByType(Type type) {
            switch (type) {
                case Line:
                case Tension:
                    this.clazz = LineChart.class;
                    this.entryClazz = Entry.class;
                    break;
                case Bar:
                    this.clazz = BarChart.class;
                    this.entryClazz = BarEntry.class;
                    break;
                case UNKNOWN:
                default:
                    break;
            }
        }

    }
}
