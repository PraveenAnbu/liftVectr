package com.example.liftvectr.util;

import android.content.Intent;
import android.graphics.Color;

import com.example.liftvectr.activities.ExerciseHistoryActivity;
import com.example.liftvectr.data.Exercise;
import com.example.liftvectr.data.IMUData;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Date;

public class ChartDisplay {

    private static int colors[] = {Color.RED, Color.GREEN, Color.BLUE, Color.CYAN, Color.MAGENTA, Color.YELLOW};
    // -------------------- Public Utility Functions --------------------

    public static void displaySingleLineChart(LineChart chart, List<Float> xValues, List<Float> yValues,
                                              String lineLabel, String chartDescription) {
        if (chart == null) {
            throw new IllegalArgumentException("Error: LineChart is null");
        }
        if (xValues.size() == 0 || yValues.size() == 0) {
            throw new IllegalArgumentException("An inputted coordinate array is empty");
        }

        setChartStyling(chart, chartDescription);
        LineDataSet line = createLine(xValues, yValues, lineLabel, Color.YELLOW);

        LineData data = new LineData(line);
        chart.setData(data);
        chart.invalidate();
    }

//    public static void displaySingleLineChart(LineChart chart, List<Date> xValues, List<Float> yValues,
//                                              String lineLabel, String chartDescription) {
//        if (chart == null) {
//            throw new IllegalArgumentException("Error: LineChart is null");
//        }
//        if (xValues.size() == 0 || yValues.size() == 0) {
//            throw new IllegalArgumentException("An inputted coordinate array is empty");
//        }
//
//        setChartStyling(chart, chartDescription);
//        LineDataSet line = createLine(xValues, yValues, lineLabel, Color.BLUE);
//
//        LineData data = new LineData(line);
//        chart.setData(data);
//        chart.invalidate();
//



    public static void displayMultiLineChart(LineChart chart,
                                             List<Float> xValues,
                                             List<List<Float>> yValues,
                                             List<String> lineLabels,
                                             String chartDescription) {
        if (chart == null) {
            throw new IllegalArgumentException("Error: LineChart is null");
        }
        if (xValues.size() == 0 || yValues.size() == 0) {
            throw new IllegalArgumentException("An inputted coordinate array is empty");
        }

        setChartStyling(chart, chartDescription);
        List<ILineDataSet> lines = createMultipleLines(xValues, yValues, lineLabels);

        LineData data = new LineData(lines);
        chart.setData(data);
        chart.invalidate();
    }

    public static void displayIMUDataChart(Exercise exercise,
                                                LineChart chart,
                                                String config,
                                                String chartDescription,
                                                boolean live) {
        setChartStyling(chart, chartDescription);
        List<ILineDataSet> lines = createMultipleLinesFromIMUData(exercise, config, live);

        LineData data = new LineData(lines);
        chart.setData(data);
        chart.invalidate();
    }

    // -------------------- Private Helpers - Do not use --------------------

    private static LineDataSet createLine(List<Float> xValues, List<Float> yValues, String lineLabel, int color) {
        if (xValues.size() != yValues.size()) {
            throw new IllegalArgumentException(
                    "Provided an unequal about of x and y values: "
                            + "x=" + xValues.size()
                            + "y=" + yValues.size());
        }

        LineDataSet line = new LineDataSet(convertXYValuesToCoords(xValues, yValues), lineLabel);
        setLineStyling(line, color);

        return line;
    }

    private static LineDataSet createLineWithCoords(List<Entry> coords, String lineLabel, int color) {
        LineDataSet line = new LineDataSet(coords, lineLabel);
        setLineStyling(line, color);

        return line;
    }

    private static List<ILineDataSet> createMultipleLines(List<Float> xValues, List<List<Float>> yValues, List<String> lineLabels) {
        List<ILineDataSet> lines = new ArrayList<>();
        for(int i = 0; i < yValues.size(); i++) {
            lines.add(createLine(xValues, yValues.get(i), lineLabels.get(i), colors[i]));
        }
        return lines;
    }

    private static List<ILineDataSet> createMultipleLinesFromIMUData(Exercise exercise, String config, boolean live) {
        List<Entry> xLinAcc_ms = new ArrayList<>();
        List<Entry> yLinAcc_ms = new ArrayList<>();
        List<Entry> zLinAcc_ms = new ArrayList<>();
        List<Entry> xAngVel_ms = new ArrayList<>();
        List<Entry> yAngVel_ms = new ArrayList<>();
        List<Entry> zAngVel_ms = new ArrayList<>();

        List<IMUData> data = exercise.getData();
        int startingIndex = 0;
        int chartWidth = 20;
        if (live && data.size() > chartWidth) {
            startingIndex = data.size()-20;
        }

        for (int i = startingIndex; i < data.size(); i++) {
            xLinAcc_ms.add(new Entry(data.get(i).micros, data.get(i).x_lin_acc));
            yLinAcc_ms.add(new Entry(data.get(i).micros, data.get(i).y_lin_acc));
            zLinAcc_ms.add(new Entry(data.get(i).micros, data.get(i).z_lin_acc));
            xAngVel_ms.add(new Entry(data.get(i).micros, data.get(i).x_ang_vel));
            yAngVel_ms.add(new Entry(data.get(i).micros, data.get(i).y_ang_vel));
            zAngVel_ms.add(new Entry(data.get(i).micros, data.get(i).z_ang_vel));
        }

        List<ILineDataSet> lines = new ArrayList<>();
        if (config == "a_only" || config == "both") {
            lines.add(createLineWithCoords(xLinAcc_ms, "X", colors[0]));
            lines.add(createLineWithCoords(yLinAcc_ms, "Y", colors[1]));
            lines.add(createLineWithCoords(zLinAcc_ms, "Z", colors[2]));
        }
        if (config == "g_only" || config == "both") {
            lines.add(createLineWithCoords(xAngVel_ms, "X", colors[0]));
            lines.add(createLineWithCoords(yAngVel_ms, "Y", colors[1]));
            lines.add(createLineWithCoords(zAngVel_ms, "Z", colors[2]));
        }

        return lines;
    }

    private static List<Entry> convertXYValuesToCoords(List<Float> xValues, List<Float> yValues) {
        List<Entry> coords = new ArrayList<>();
        for (int i = 0; i < xValues.size(); i++) {
            coords.add(new Entry(xValues.get(i), yValues.get(i)));
        }
        return coords;
    }

    private static void setLineStyling(LineDataSet line, int color) {
        line.setDrawCircles(false);
        line.setDrawValues(false);
        line.setColor(color);
    }

    private static void setChartStyling(LineChart chart, String description) {
        XAxis xAxis = chart.getXAxis();
        YAxis yAxis = chart.getAxisLeft();
        chart.getAxisRight().setEnabled(false); // Disable right y axis

        xAxis.setDrawGridLines(false);
        yAxis.setDrawGridLines(false);

        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        chart.setBackgroundColor(Color.BLACK);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setAxisLineColor(Color.WHITE);
        xAxis.setDrawLabels(true);
        yAxis.setTextColor(Color.WHITE);
        yAxis.setAxisLineColor(Color.WHITE);

        Legend l = chart.getLegend();
        l.setTextColor(Color.WHITE);

        Description desc = new Description();
        desc.setText(description);
        chart.setDescription(desc);
    }

    private static int getRandomColor() {
        Random rand = new Random();
        float hue = rand.nextFloat();
        float saturation = 0.9f;
        float luminance = 1.0f;
        return Color.HSVToColor(new float[]{hue, saturation, luminance});
    }
}