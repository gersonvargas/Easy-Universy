package com.herprogramacion.geekyweb;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.widget.Chronometer;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class ActividadResultado extends Base {
    VariablesGlobales vg = VariablesGlobales.getInstance();
    RelativeLayout panelprincipal;
    private PieChart grafico;
    float inc = vg.getIncorretas();
    float correctas = vg.getCorrectas();

    private float ydata[] = {correctas, inc};
    private String[] Xdata = {"Correctas", "Incorrectas"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_resultado);


        vg.setCorrectas(0);
        vg.setIncorretas(0);
        panelprincipal = (RelativeLayout) findViewById(R.id.panelgrafico);
      /*  ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(4f, 0));
        entries.add(new BarEntry(8f, 1));
        entries.add(new BarEntry(6f, 2));
        entries.add(new BarEntry(12f, 3));
        entries.add(new BarEntry(18f, 4));
        entries.add(new BarEntry(9f, 5));
        BarDataSet dataset = new BarDataSet(entries, "# of Calls");
        ArrayList<String> labels = new ArrayList<String>();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");
        BarChart chart = new BarChart(this);
        setContentView(chart);
        BarData data = new BarData(labels, dataset);
        chart.setData(data);
        chart.setDescription("# of times Alice called Bob");*/
        grafico = new PieChart(this);
        setContentView(grafico);
        grafico.setBackgroundColor(Color.rgb(44, 140, 211));
        grafico.setUsePercentValues(true);
        grafico.setDescription("Puntos del examen.");
        grafico.setHoleColor(Color.TRANSPARENT);
        grafico.setDrawHoleEnabled(true);
        grafico.setHoleRadius(7);
        grafico.setTransparentCircleRadius(10);
        grafico.setRotationAngle(0);
        grafico.setRotationEnabled(true);
        grafico.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, int i, Highlight highlight) {
                if (entry == null)
                    return;
                Mensaje("# preguntas: " + entry.getVal());
            }

            @Override
            public void onNothingSelected() {

            }
        });
        addData();
        Legend l = grafico.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);
    }

    private void addData() {
        ArrayList<Entry> yVal = new ArrayList<Entry>();
        for (int i = 0; i < ydata.length; i++)
            yVal.add(new Entry(ydata[i], i));
        ArrayList<String> xVal = new ArrayList<String>();
        for (int i = 0; i < Xdata.length; i++)
            xVal.add(Xdata[i]);
        PieDataSet dataset = new PieDataSet(yVal, "Nota: " + String.valueOf((correctas / vg.getCantidadPreguntas()) * 100));
        dataset.setSliceSpace(3);
        dataset.setSelectionShift(5);

        ArrayList<Integer> color = new ArrayList<Integer>();
        for (int o : ColorTemplate.VORDIPLOM_COLORS) {
            color.add(o);
        }
        for (int o : ColorTemplate.JOYFUL_COLORS) {
            color.add(o);
        }
        for (int o : ColorTemplate.COLORFUL_COLORS) {
            color.add(o);
        }
        for (int o : ColorTemplate.LIBERTY_COLORS) {
            color.add(o);
        }
        for (int o : ColorTemplate.PASTEL_COLORS) {
            color.add(o);
        }
        color.add(ColorTemplate.getHoloBlue());
        dataset.setColors(color);

        PieData data = new PieData(xVal, dataset);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(16f);
        data.setValueTextColor(Color.BLACK);
        grafico.setData(data);

        grafico.highlightValues(null);

        grafico.invalidate();
    }

    @Override
    public void onBackPressed() {
        DialogoSiNo();
    }

    public void DialogoSiNo() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("¿Estas seguro de salir?");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Sí",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intento = new Intent(getApplicationContext(), Main.class);
                        startActivity(intento);
                    }
                });
        builder1.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Mensaje("negativo");
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    ;

}
