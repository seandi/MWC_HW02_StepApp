package com.example.stepapp.ui.report;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.stepapp.R;
import com.example.stepapp.StepAppOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class DayFragment extends Fragment {
    AnyChartView anyChartView;
    TextView numDailyStepsTextView;
    Map<String, Integer> dailySteps = null;
    int total_steps = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (container != null) {
            container.removeAllViews();
        }

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_day, container, false);

        anyChartView = root.findViewById(R.id.dayBarChart);
        anyChartView.setProgressBar(root.findViewById(R.id.loadingBarDailyChart));

        Cartesian cartesian_chart = createColumnChart();
        anyChartView.setBackgroundColor("#00000000");
        anyChartView.setChart(cartesian_chart);

        numDailyStepsTextView = root.findViewById(R.id.numDailyStepsTextView);
        numDailyStepsTextView.setText(String.valueOf(total_steps));

        return root;
    }

    public Cartesian createColumnChart(){
        List<DataEntry> chart_data = new ArrayList<>();
        Cartesian cartesian_chart = null;
        Column column_series = null;
        this.total_steps=0;

        // Retrieve map of daily numer of steps
        dailySteps = StepAppOpenHelper.loadStepsByDay(getContext());
        // Find total number of steps
        this.total_steps = dailySteps.values().stream().mapToInt(Integer::intValue).sum();

        // Create and populate the chart
        cartesian_chart = AnyChart.column();

        for(Map.Entry<String, Integer> entry: dailySteps.entrySet()){
            chart_data.add( new ValueDataEntry(entry.getKey(), entry.getValue()));
        }

        column_series = cartesian_chart.column(chart_data);

        // Set chart UI
        cartesian_chart.background().fill("#00000000");
        cartesian_chart.xAxis(0).title("Day");
        cartesian_chart.yAxis(0).title("Number of steps");
        cartesian_chart.yScale().minimum(0);

        cartesian_chart.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian_chart.interactivity().hoverMode(HoverMode.BY_X);
        cartesian_chart.animation(true);

        // Set colmn series UI
        column_series.fill("#1EB980");
        column_series.stroke("#1EB980");

        column_series.tooltip()
                .titleFormat("Day: {%X}")
                .format("Steps: {%Value}");

        column_series.tooltip()
                .anchor(Anchor.RIGHT_TOP)
                .position(Position.RIGHT_TOP)
                .offsetX(0d)
                .offsetY(5);

        return cartesian_chart;
    }

}