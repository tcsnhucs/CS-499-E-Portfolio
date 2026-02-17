package com.example.thomascomercs360project;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {

    CalendarView calendarView;
    FloatingActionButton addB;
    String selectedDate;
    RecyclerView recyclerView;
    CustomActivity adaptor;
    MainDatabase db;
    ArrayList<String> eventId, eventTitle, eventDescription, eventDate, eventTime;
    List<EventModel> filteredEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calander);

        calendarView = findViewById(R.id.calendarView);
        addB = findViewById(R.id.add_new);
        recyclerView = findViewById(R.id.calendarRecyclerView);

        setupHeader();

        db = new MainDatabase(CalendarActivity.this);
        eventId = new ArrayList<>();
        eventTitle = new ArrayList<>();
        eventDescription = new ArrayList<>();
        eventDate = new ArrayList<>();
        eventTime = new ArrayList<>();
        filteredEvents = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        selectedDate = day + "-" + (month + 1) + "-" + year;

        loadAndDisplayDayEvents(selectedDate);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate = dayOfMonth + "-" + (month + 1) + "-" + year;
                loadAndDisplayDayEvents(selectedDate);
            }
        });

        addB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, AddActivity.class);
                intent.putExtra("selected_date", selectedDate);
                startActivityForResult(intent, 1);
            }
        });
    }

    private void setupHeader() {
        Button calBtn = findViewById(R.id.header_calendar);
        Button eventBtn = findViewById(R.id.header_events);

        // Current screen is calendar, highlight/disable
        calBtn.setAlpha(0.5f);
        calBtn.setEnabled(false);

        eventBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, EventList.class));
            finish();
        });
    }

    private void loadAndDisplayDayEvents(String date) {
        eventId.clear();
        eventTitle.clear();
        eventDescription.clear();
        eventDate.clear();
        eventTime.clear();
        filteredEvents.clear();

        Cursor c = db.readAllData();
        if (c != null && c.getCount() > 0) {
            int idIndex = c.getColumnIndex("_id");
            int titleIndex = c.getColumnIndex("title");
            int descIndex = c.getColumnIndex("description");
            int dateIndex = c.getColumnIndex("date");
            int timeIndex = c.getColumnIndex("time");
            int persistentIndex = c.getColumnIndex("persistent");

            while (c.moveToNext()) {
                String dateStr = c.getString(dateIndex);
                if (dateStr.equals(date)) {
                    filteredEvents.add(new EventModel(
                            c.getString(idIndex),
                            c.getString(titleIndex),
                            c.getString(descIndex),
                            c.getString(dateIndex),
                            c.getString(timeIndex),
                            c.getInt(persistentIndex) == 1
                    ));
                }
            }
            c.close();
        }

        Collections.sort(filteredEvents);

        for (EventModel event : filteredEvents) {
            eventId.add(event.getId());
            eventTitle.add(event.getTitle());
            eventDescription.add(event.getDescription());
            eventDate.add(event.getDate());
            eventTime.add(event.getTime());
        }

        if (adaptor == null) {
            adaptor = new CustomActivity(CalendarActivity.this, this, eventId, eventTitle,
                    eventDescription, eventDate, eventTime);
            recyclerView.setAdapter(adaptor);
            recyclerView.setLayoutManager(new LinearLayoutManager(CalendarActivity.this));
        } else {
            adaptor.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadAndDisplayDayEvents(selectedDate);
    }
}
