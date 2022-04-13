package fi.arcada.sos_projekt_chart_sma;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    String currency, dateFrom, dateTo;
    LineChart chart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TEMPORÄRA VÄRDEN
        currency = "USD";
        dateFrom = "2022-01-01";
        dateTo = "2022-02-01";
        chart = (LineChart) findViewById(R.id.chart);



        // Hämta växelkurser från API
        ArrayList<Double> currencyValues = getCurrencyValues(currency, dateFrom, dateTo);
        // Skriv ut dem i konsolen
        System.out.println(currencyValues.toString());

        createSimpleGraph(currencyValues); //för in data till grafen


        //TESTSAKER!! HÖRS INTE TILL APPEN
        ArrayList<Double> temperatures = Statistics.getDataValues();
        ArrayList<Double> tempsSma = Statistics.TESTsma(temperatures, 3);

    }

    //GRAFEN
    public void createSimpleGraph (ArrayList<Double> dataSet){
    List<Entry> entries = new ArrayList<Entry>();
        for (int i = 0; i < dataSet.size(); i++) {
            entries.add(new Entry(i,dataSet.get(i).floatValue()));
        }
        LineDataSet lineDataSet = new LineDataSet(entries, "Temperatur");
        LineData LineData = new LineData(lineDataSet);

        chart.setData(LineData);
        chart.invalidate(); //refresh
    }




    // Färdig metod som hämtar växelkursdata
    public ArrayList<Double> getCurrencyValues(String currency, String from, String to) {

        CurrencyApi api = new CurrencyApi();
        ArrayList<Double> currencyData = null;

        String urlString = String.format("https://api.exchangerate.host/timeseries?start_date=%s&end_date=%s&symbols=%s",
                from.trim(),
                to.trim(),
                currency.trim());

        try {
            String jsonData = api.execute(urlString).get();

            if (jsonData != null) {
                currencyData = api.getCurrencyData(jsonData, currency.trim());
                Toast.makeText(getApplicationContext(), String.format("Hämtade %s valutakursvärden från servern", currencyData.size()), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Kunde inte hämta växelkursdata från servern: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return currencyData;
    }
}