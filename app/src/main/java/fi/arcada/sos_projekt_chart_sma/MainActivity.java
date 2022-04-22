package fi.arcada.sos_projekt_chart_sma;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    TextView textMain;
    String currency, dateFrom, dateTo;
    LineChart chart;
    boolean sma10Added = false;
    boolean sma30Added = false;

    SharedPreferences sharedPref;
    SharedPreferences.Editor prefEditor;

    // Hämta växelkurser från API

    ArrayList<ChartLine> chartLines = new ArrayList<>();
    ChartLine currencyLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        chart = (LineChart) findViewById(R.id.chart);
        textMain = findViewById(R.id.textView);

        calculate();

        // Hämta växelkurser från API
        ArrayList<Double> currencyValues = getCurrencyValues(currency, dateFrom, dateTo);
        // Skriv ut dem i konsolen
        System.out.println(currencyValues.toString());

        // Skriv ut valuta charten
        currencyLine = new ChartLine(currencyValues, "Valutakurs", Color.BLUE, 0);

        chartLines.add(currencyLine);

        createMultilineGraph(chartLines);
    }

    public void buttonClick(View view) {

        chartLines = new ArrayList<>();
        chartLines.add(currencyLine);

        currency = sharedPref.getString("Currency", "USD");
        dateFrom = sharedPref.getString("dateFrom", "2022-01-01");
        dateTo = sharedPref.getString("dateTo", "2022-04-01");
        ArrayList<Double> currencyValues = getCurrencyValues(currency, dateFrom, dateTo);

        if (view.getId() == R.id.button1) {
           sma10Added = sma10Added ? false : true;
        }
        if (sma10Added) {
            chartLines.add(new ChartLine(Statistics.movingAverage(currencyValues, 10), "SMA 10", Color.GREEN, 10));
        }

        if (view.getId() == R.id.button2) {
            sma30Added = sma30Added ? false : true;
        }
        if (sma30Added) {
            chartLines.add(new ChartLine(Statistics.movingAverage(currencyValues, 30), "SMA 30", Color.RED, 30));
        }

        calculate();
        createMultilineGraph(chartLines);
    }

    //GRAFEN (enkel graf test vid behov)
    /*public void createSimpleGraph (ArrayList<Double> dataSet){
    List<Entry> entries = new ArrayList<Entry>();
        for (int i = 0; i < dataSet.size(); i++) {
            entries.add(new Entry(i,dataSet.get(i).floatValue()));
        }
        LineDataSet lineDataSet = new LineDataSet(entries, "Temperatur");
        LineData LineData = new LineData(lineDataSet);

        chart.setData(LineData);
        chart.invalidate(); //refresh
    }*/

    public void createMultilineGraph(ArrayList<ChartLine> chartLines) {
        List<ILineDataSet> dataSeries = new ArrayList<>();

        for (ChartLine chartLine : chartLines) {
            LineDataSet lineDataSet = new LineDataSet(chartLine.getEntries(), chartLine.getLabel());

            lineDataSet.setColor(chartLine.getColor());
            lineDataSet.setDrawCircles(false);
            lineDataSet.setDrawValues(false);
            dataSeries.add(lineDataSet);
        }

        LineData lineData = new LineData(dataSeries);
        chart.setData(lineData);
        chart.invalidate(); // refresh

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

    public void calculate() {

        currency = sharedPref.getString("Currency", "USD");
        dateFrom = sharedPref.getString("dateFrom", "2022-01-01");
        dateTo = sharedPref.getString("dateTo", "2022-04-01");

        textMain.setText(String.format("%s | %s - %s",
                currency,
                dateFrom,
                dateTo));
    }

    public void openSettings(View view) {

        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}