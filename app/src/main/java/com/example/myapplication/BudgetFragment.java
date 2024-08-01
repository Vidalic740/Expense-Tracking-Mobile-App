package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BudgetFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_budget, container, false);

        Button button = view.findViewById(R.id.save);
        PieChart pieChart = view.findViewById(R.id.pieChart_view);

        TextView textDaily = view.findViewById(R.id.daily_display);
        TextView textWeekly = view.findViewById(R.id.weekly_display);
        TextView textMonthly = view.findViewById(R.id.monthly_display);

        button.setOnClickListener(view1 -> {

            EditText daily = view.findViewById(R.id.daily_budget);
            EditText weekly = view.findViewById(R.id.weekly_budget);
            EditText monthly = view.findViewById(R.id.monthly_budget);

            String daily1 = daily.getText().toString();
            String weekly1 = weekly.getText().toString();
            String monthly1 = monthly.getText().toString();

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("budget");

            databaseReference.child("daily").setValue(daily1);
            databaseReference.child("weekly").setValue(weekly1);
            databaseReference.child("monthly").setValue(monthly1);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String retrievedDaily = dataSnapshot.child("daily").getValue(String.class);
                    String retrievedWeekly = dataSnapshot.child("weekly").getValue(String.class);
                    String retrievedMonthly = dataSnapshot.child("monthly").getValue(String.class);

                    textDaily.setText(retrievedDaily);
                    textWeekly.setText(retrievedWeekly);
                    textMonthly.setText(retrievedMonthly);

                    float dailyBudget = Float.parseFloat(daily1);
                    float weeklyBudget = Float.parseFloat(weekly1);
                    float monthlyBudget = Float.parseFloat(monthly1);

                    ArrayList<PieEntry> expense = new ArrayList<>();
                    expense.add(new PieEntry(dailyBudget, "Daily"));
                    expense.add(new PieEntry(weeklyBudget, "Weekly"));
                    expense.add(new PieEntry(monthlyBudget, "Monthly"));

                    PieDataSet pieDataSet = new PieDataSet(expense, "Expense");
                    pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                    pieDataSet.setValueTextColor(Color.BLACK);
                    pieDataSet.setValueTextSize(12f);

                    PieData pieData = new PieData(pieDataSet);

                    pieChart.setData(pieData);
                    pieChart.getDescription().setEnabled(false);
                    pieChart.setCenterText("Expense");
                    pieChart.animate();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle potential errors
                }

            });
        });
        return view;
    }
}