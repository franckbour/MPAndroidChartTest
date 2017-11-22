package com.dicsit.android.mpandroidcharttest.internal;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

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

    private View mParent;

    private int resId;
    private int markerViewResId;

    private boolean mXAxisDateFormatEnable;
    private boolean mAnimationEnable;

    private boolean mMaxLimitEnable;
    private boolean mMinLimitEnable;
    private boolean mNormalLimitEnable;

    private float maxLimitValue;
    private float minLimitValue;
    private float normalLimitValue;

    private float mLeftAxisMaximum;
    private float mLeftAxisMinimum;

    private float mTextSize;
    private float mValueTextSize;
    private float mLineWidth;
    private float mLimitLineWidth;
    private float mBarWidth;

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
        LINE,
        BAR,
        TA,
        UNKNOWN
    }

    /**
     * Constructeur
     * @param builder Dicsit chart builder
     */
    private FBChart(Builder builder) {
        this.mContext = builder.context;
        this.mParent = builder.parent;
        this.resId = builder.resId;

        this.mType = builder.type;
        this.mClass = builder.clazz;
        this.mEntries = builder.entries;

        this.mXAxisDateFormatEnable = builder.xAxisDateFormatEnable;
        this.mAnimationEnable = builder.animationEnable;

        this.mMaxLimitEnable = builder.maxLimitEnable;
        this.mMinLimitEnable = builder.minLimitEnable;
        this.mNormalLimitEnable = builder.normalLimitEnable;
        this.maxLimitValue = builder.maxLimitValue;
        this.minLimitValue = builder.minLimitValue;
        this.normalLimitValue = builder.normalLimitValue;

        this.markerViewResId = builder.markerViewResId;

        this.mLeftAxisMaximum = builder.customLeftAxisMaxValue ? builder.leftAxisMaximum : getDefaultLeftAxisMaxValue();
        this.mLeftAxisMinimum = builder.customLeftAxisMinValue ? builder.leftAxisMinimum : getDefaultLeftAxisMinValue();

        this.mTextSize = builder.textSize;
        this.mValueTextSize = builder.valueTextSize;
        this.mLineWidth = builder.lineWidth;
        this.mLimitLineWidth = builder.limitLineWidth;
        this.mBarWidth = builder.barWidth;

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
            MyMarkerView markerView = new MyMarkerView(mContext, markerViewResId);
            markerView.setChartView(mChart);
            mChart.setMarker(markerView);
        }

        // Définitions des limites
        LimitLine upLimit = new LimitLine(maxLimitValue, "Max");
        upLimit.setLineWidth(mLimitLineWidth);
        upLimit.enableDashedLine(10f, 10f, 0f);
        upLimit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        upLimit.setTextSize(mTextSize);
        upLimit.setTextColor(upLimit.getLineColor());

        LimitLine downLimit = new LimitLine(minLimitValue, "Min");
        downLimit.setLineWidth(mLimitLineWidth);
        downLimit.enableDashedLine(10f, 10f, 0f);
        downLimit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        downLimit.setTextSize(mTextSize);
        downLimit.setTextColor(downLimit.getLineColor());

        LimitLine normalLimit = new LimitLine(normalLimitValue, "Normal");
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
        if (mEntries == null) return;
        if (mChart instanceof LineChart) {
            setLineData(mType == Type.TA, mEntries.toArray(new ArrayList[mEntries.size()]));
        } else if (mChart instanceof BarChart) {
            setBarData(mEntries.toArray(new ArrayList[mEntries.size()]));
        }
    }

    /**
     * Insertion des données dans le graphique de type LINE
     * @param isTA tension artérielle
     * @param lists listes des données
     */
    @SuppressWarnings("unchecked")
    private void setLineData(boolean isTA, ArrayList<Entry>... lists) {
        if (lists == null || lists.length == 0) return;
        List<ILineDataSet> datasets = new ArrayList<>();
        int index = 0;
        for (ArrayList<Entry> list : lists) {
            LineDataSet set = new LineDataSet(list, "DataSet "+ index++);
            set.setAxisDependency(YAxis.AxisDependency.LEFT);

            // Lignes
            set.setColor(mDefaultColor);
            set.setLineWidth(mLineWidth);

            // Cercles
            set.setDrawCircles(true); // true par défaut
            set.setCircleRadius(5f);
            set.setDrawCircleHole(true);
            set.setCircleHoleRadius(2.5f);

            // Valeurs (par défaut)
            set.setDrawValues(true); // true par défaut
            set.setValueTextColor(mDefaultColor);
            set.setValueTextSize(mValueTextSize);

            // Remplissage
            set.setDrawFilled(true); // false par défaut
            set.setFillAlpha(50);
            set.setFillColor(mDefaultColor);

            // Highlights
            set.setDrawHighlightIndicators(true); // par défaut -> true
            set.setHighLightColor(mDefaultColor);
            //set1.setHighlightLineWidth(2f);

            if (isTA) {
                final boolean even = (index -1 ) % 2 == 0;
                set.setFillFormatter(new IFillFormatter() {
                    @Override
                    public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                        return even ? mChart.getAxisLeft().getAxisMaximum() : mChart.getAxisLeft().getAxisMinimum();
                    }
                });
            }

            datasets.add(set);

            // Si la constante n'est pas une tension artérielle, on ne garde que les premières valeurs
            if (!isTA)
                break;
        }

        // create a data object with the datasets
        LineData data = new LineData(datasets);

        // set data
        ((LineChart) mChart).setData(data);
    }

    /**
     * Insertion des données dans le graphique de type BAR
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
            set.setValueTextColor(mDefaultColor);
            set.setValueTextSize(mValueTextSize);
            set.setHighLightColor(even ? mDefaultColor : Color.RED);
            dataSets.add(set);
        }

        // create a data object with the datasets
        BarData data = new BarData(dataSets);

        BarChart barChart = (BarChart) mChart;

        // set data
        barChart.setData(data);

        // Configuration spécifique
        barChart.getBarData().setBarWidth(mBarWidth);
        if (lists.length > 1) {
            barChart.groupBars(lists[0].get(0).getX(), 0.08f, 0.03f);
        }
    }

    /**
     * Affiche le graphique
     */
    public void show() {
        // Possible uniquemet après avoir défini les données
        if (mChart != null) {
            mChart.getLegend().setEnabled(false);

            if (this.mAnimationEnable) {
                mChart.animateX(500);
            } else {
                mChart.invalidate();
            }
        } else {
            Log.e("FBChart", "L'objet mChart est à la valeur NULL !");
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
    @Nullable
    private DataMinMax getDataMinValue() {
        if (mEntries == null) return null;

        List<Entry> minimums = new ArrayList<>();
        List<Entry> maximums = new ArrayList<>();

        for (ArrayList<? extends Entry> values : mEntries) {
            Entry minEntry = Collections.min(values, entryComparator);
            Entry maxEntry = Collections.max(values, entryComparator);
            minimums.add(minEntry);
            maximums.add(maxEntry);
        }

        float minValue = 0f;
        float maxValue = 0f;
        if (!minimums.isEmpty()) {
            Entry e = Collections.min(minimums, entryComparator);
            minValue = e.getY();
        }

        if (!maximums.isEmpty()) {
            Entry e = Collections.max(maximums, entryComparator);
            maxValue = e.getY();
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
     * Dicsit Chart Builder
     */
    public static class Builder {
        private final Context context;
        private Type type;
        private Class<? extends BarLineChartBase<?>> clazz;
        private List<ArrayList<? extends Entry>> entries;

        private View parent;
        private int resId;

        private int markerViewResId = -1;

        private boolean xAxisDateFormatEnable = true;
        private boolean animationEnable = false;
        private boolean maxLimitEnable = false;
        private boolean minLimitEnable = false;
        private boolean normalLimitEnable = false;

        private boolean customLeftAxisMinValue = false;
        private boolean customLeftAxisMaxValue = false;

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
        public Builder setType(Type type) {
            this.type = type;
            this.clazz = getClassByType(type);
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
         * Construit le graphique
         * @return Dicsit Chart
         */
        public FBChart build() {
            return new FBChart(this);
        }

        /**
         * Retourne le type de classe de graphique à utiliser en fonction du type
         * @param type type
         * @return Classe
         */
        private Class<? extends BarLineChartBase<?>> getClassByType(Type type) {
            Class<? extends BarLineChartBase<?>> clazz = null;
            switch (type) {
                case LINE:
                case TA:
                    clazz = LineChart.class;
                    break;
                case BAR:
                    clazz = BarChart.class;
                    break;
                case UNKNOWN:
                default:
                    break;
            }
            return clazz;
        }

    }
}
