# MPAndroidChartTest

Création d'une classe d'utilisation de la librairie [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart).

Pour l'instant, cette classe ne supporte que les types de graphiques suivants :

* LineChart
* BarChart

###### Utilisation de la classe FBChart

```java
/**
 * Constructeur
 * @param context contexte
 * @param layout vue parente
 * @param resId id du container du graphique
 */
public FBChart.Builder(Context context, View layout, int resId)
```

###### Exemple simple d'utilisation (BarChart) :

```java
FBChart chart = new FBChart.Builder(getContext(), getView(), R.id.chart_container)
                .setChartType(FBChart.Type.Bar) // BarChart
                .setEntries(entries)
                .build();
chart.show();
```
