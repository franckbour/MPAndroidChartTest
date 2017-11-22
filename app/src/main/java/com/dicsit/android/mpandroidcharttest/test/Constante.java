package com.dicsit.android.mpandroidcharttest.test;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Classe Constante de test
 *
 * @version 1
 * @since 22/11/2017
 * @author Franck BOUR
 *
 */

public class Constante {

    float value;
    float date;
    String unit;
    String comment;
    String taValues;

    public float getValue() {
        return value;
    }

    public float getDate() {
        return date;
    }

    public String getUnit() {
        return unit;
    }

    public String getComment() {
        return comment;
    }

    public String getTaValues() {
        return taValues;
    }

    public Constante() {
        // empty
    }

    private Constante(float value, float date, String unit, String comment) {
        this.value = value;
        this.date = date;
        this.unit = unit;
        this.comment = comment;
    }

    public Constante(String taValues, float date, String unit, String comment) {
        this.taValues = taValues;
        this.date = date;
        this.unit = unit;
        this.comment = comment;
    }

    /**
     * Retourne un jeux de test normal
     * @param count nombre de valeur
     * @param range portée de valeurs
     * @param start valeur de départ
     * @return liste
     */
    public static ArrayList<Constante> getJeux(int count, float range, float start) {
        float from = TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis());
        float to = from + count;
        ArrayList<Constante> liste = new ArrayList<>();
        for (float i = from; i < to; i++) {
            float value = (float)(Math.random() * range) + start;
            Constante c = new Constante(value, i, "kg", "commentaire " + i);
            liste.add(c);
        }
        return liste;
    }

    /**
     * Retourne un jeux de test pour des constantes tensions
     * @param count nombre de valeur
     * @param systolRange portée de valeurs systoliques
     * @param systolStart valeur systolique de départ
     * @param diastolRange portée de valeurs diastoliques
     * @param diastolStart valeur diastolique de départ
     * @return liste
     */
    public static ArrayList<Constante> getTensionJeux(int count, float systolRange, float systolStart, float diastolRange, float diastolStart) {
        float from = TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis());
        float to = from + count;
        ArrayList<Constante> liste = new ArrayList<>();
        for (float i = from; i < to; i++) {
            float value1 = (float)(Math.random() * systolRange) + systolStart;
            float value2 = (float)(Math.random() * diastolRange) + diastolStart;
            Constante c = new Constante(String.format(Locale.ENGLISH,"%f|%f", value1, value2), i, "cmHg", "commentaire " + i);
            liste.add(c);
        }
        return liste;
    }
}
