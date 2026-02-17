package com.example.thomascomercs360project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity {

    CalendarView calendarView;
    FloatingActionButton addB;
    String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calander);

        calendarView = findViewById(R.id.calendarView);
        addB = findViewById(R.id.add_new);

        // Initialize selectedDate with current date in correct format (d-M-yyyy).
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        selectedDate = day + "-" + (month + 1) + "-" + year;

        // Listen for date changes.
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Update selectedDate when user clicks a new day.
                selectedDate = dayOfMonth + "-" + (month + 1) + "-" + year;
            }
        });

        // Add button passes the selected date to the Add activity.
        addB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, AddActivity.class);
                intent.putExtra("selected_date", selectedDate);
                startActivity(intent);
            }
        });
    }
}
