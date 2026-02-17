package com.example.thomascomercs360project;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class EventList extends AppCompatActivity {

    public static final int REQUEST = 1;
    RecyclerView recyclerView;
    FloatingActionButton addButton, searchButton;
    CustomActivity adaptor;
    MainDatabase db;
    ArrayList<String> eventId, eventTitle, eventDescription, eventDate, eventTime;
    List<EventModel> allEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        recyclerView = findViewById(R.id.recyclerView);
        addButton = findViewById(R.id.add_new);
        searchButton = findViewById(R.id.search_button);

        setupHeader();

        addButton.setOnClickListener(view -> {
            Intent intent = new Intent(EventList.this, AddActivity.class);
            startActivityForResult(intent, REQUEST);
        });

        searchButton.setOnClickListener(view -> showSearchDialog());

        db = new MainDatabase(EventList.this);
        eventId = new ArrayList<>();
        eventTitle = new ArrayList<>();
        eventDescription = new ArrayList<>();
        eventDate = new ArrayList<>();
        eventTime = new ArrayList<>();
        allEvents = new ArrayList<>();

        loadAndDisplayData(null);
    }

    private void setupHeader() {
        Button calBtn = findViewById(R.id.header_calendar);
        Button eventBtn = findViewById(R.id.header_events);

        calBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, CalendarActivity.class));
            finish();
        });

        // Current screen is events, so maybe highlight or disable the button
        eventBtn.setAlpha(0.5f);
        eventBtn.setEnabled(false);
    }

    private void showSearchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_search, null);
        EditText searchEdit = view.findViewById(R.id.search_edit_text);
        
        builder.setView(view);
        AlertDialog dialog = builder.create();

        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadAndDisplayData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        dialog.show();
    }

    private void loadAndDisplayData(@Nullable String query) {
        eventId.clear();
        eventTitle.clear();
        eventDescription.clear();
        eventDate.clear();
        eventTime.clear();
        allEvents.clear();

        Cursor c;
        if (query == null || query.isEmpty()) {
            c = db.readAllData();
        } else {
            c = db.searchEvents(query);
        }

        Date now = new Date();

        if (c != null && c.getCount() > 0) {
            int idIndex = c.getColumnIndex("_id");
            int titleIndex = c.getColumnIndex("title");
            int descIndex = c.getColumnIndex("description");
            int dateIndex = c.getColumnIndex("date");
            int timeIndex = c.getColumnIndex("time");
            int persistentIndex = c.getColumnIndex("persistent");

            while (c.moveToNext()) {
                String idStr = c.getString(idIndex);
                String titleStr = c.getString(titleIndex);
                String descStr = c.getString(descIndex);
                String dateStr = c.getString(dateIndex);
                String timeStr = c.getString(timeIndex);
                boolean persistent = c.getInt(persistentIndex) == 1;

                EventModel event = new EventModel(idStr, titleStr, descStr, dateStr, timeStr, persistent);
                
                if (!event.isPersistent() && event.getDateTime().before(now)) {
                    db.deleteOneRow(idStr);
                } else {
                    allEvents.add(event);
                }
            }
            c.close();
        }

        Collections.sort(allEvents);

        for (EventModel event : allEvents) {
            eventId.add(event.getId());
            eventTitle.add(event.getTitle());
            eventDescription.add(event.getDescription());
            eventDate.add(event.getDate());
            eventTime.add(event.getTime());
        }

        if (adaptor == null) {
            adaptor = new CustomActivity(EventList.this, this, eventId, eventTitle,
                    eventDescription, eventDate, eventTime);
            recyclerView.setAdapter(adaptor);
            recyclerView.setLayoutManager(new LinearLayoutManager(EventList.this));
        } else {
            adaptor.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int request, int result, @Nullable Intent data) {
        super.onActivityResult(request, result, data);
        loadAndDisplayData(null);
    }
}
