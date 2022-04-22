package fi.arcada.sos_projekt_chart_sma;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Statistics {

    //Den här e för att visa hur man räknart glidande medelvärde


public static ArrayList<Double> movingAverage(ArrayList<Double> dataset, int window) {
    ArrayList<Double> ma = new ArrayList<>();

    for  (int i = window-1; i < dataset.size()-1; i++) {
        double sum = 0.0;
        for (int j=0; j < window; j++){
            sum += dataset.get(i-j);
        }
        ma.add(sum / window); // medelvärde
    }
    return ma;
}

//TESTDATA
    public static ArrayList<Double> getDataValuesTEST() {

        double[] temps = { -4.7, -4.8, -1.8, 0.7, 0.1, -6, -7.8, -7, -3.8, -10.6, -10.3, -0.3, 4.8, 2.6, 0.1, 1.2, -1.5, -2.7, 1.8, 0.2, -2, -5.5, -1.3, 2.1, -0.6, -0.9, 1, -0.5, -1.4, -1.6, -5.3, -7.7, -8.2, -9.5, -3.9, -0.4, 1, 0.8, -0.4, 0.6, 1, -1.5, -0.5, 1.4, 1.5, 1.8, 2, 1.1, -0.1, 0.1, -0.7, -0.4, -3, -6.8, 2, 1.5 };
        double[] dataTwo = {-2.0, -0.3, -1.7, -4.6, -6.9, -6.2, -7.1, -8.2, -7.1, -1.9,  2.4,  2.5, 1.3, -0.1, -1.0, -0.8, -0.2,  0.0, -2.4, -2.9, -1.6,  0.1,  0.2, -0.2, -0.1, -0.3, -1.2, -2.8 };

        // Skapa ny arraylist för Double-värden
        ArrayList<Double> dataValues = new ArrayList<>();
        // Loopa igenom dataItems och spara endast värdena i den nya arrayListen
        for (double temp: temps) {
            dataValues.add(temp);
        }
        return dataValues;
    }

}

