package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class StatisticsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        BarChart barchart = view.findViewById(R.id.barChart_view);

        ArrayList<BarEntry> expense = new ArrayList<>();
        expense.add(new BarEntry(1, 6500));
        expense.add(new BarEntry(2, 7000));
        expense.add(new BarEntry(3, 8000));
        expense.add(new BarEntry(4, 20000));
        expense.add(new BarEntry(5, 9000));
        expense.add(new BarEntry(6, 7500));
        expense.add(new BarEntry(7, 6000));
        expense.add(new BarEntry(8, 20000));

        BarDataSet barDataSet = new BarDataSet(expense, "Expense");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(12f);

        BarData barData = new BarData(barDataSet);

        barchart.setFitBars(true);
        barchart.setData(barData);
        barchart.getDescription().setText("Weekly Expense");
        barchart.animate();

        return view;
    }
}