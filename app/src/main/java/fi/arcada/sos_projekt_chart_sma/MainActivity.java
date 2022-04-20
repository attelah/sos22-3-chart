package fi.arcada.sos_projekt_chart_sma;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.sql.Array;
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
        ArrayList<ChartLine> chartLines = new ArrayList<>();

        chartLines.add(new ChartLine(currencyValues, "Valutakurs", Color.BLUE, 0));
        chartLines.add(new ChartLine(Statistics.sma(currencyValues, 3), "SMA3", Color.GREEN, 3));
        chartLines.add(new ChartLine(Statistics.sma(currencyValues, 10), "SMA10", Color.RED, 10));
        createMultilineGraph(chartLines);


        //TESTSAKER!! HÖRS INTE TILL APPEN direkt
        ArrayList<Double> temperatures = Statistics.getDataValues();
        ArrayList<Double> tempsSma = Statistics.sma(temperatures, 3);
        ArrayList<Double> tempsSma2 = Statistics.sma(temperatures, 10);

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

    public void createMultilineGraph(ArrayList<ChartLine> chartLines) {
        List<ILineDataSet> dataSeries = new ArrayList<>();

        for (ChartLine chartLine: chartLines) {
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
    public void openSettings(View view) {

        Intent intent = new Intent (this, SettingsActivity.class);
        startActivity(intent);
    }
}