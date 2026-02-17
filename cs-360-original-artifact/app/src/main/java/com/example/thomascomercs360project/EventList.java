package com.example.thomascomercs360project;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class EventList extends AppCompatActivity {

    public static final int REQUEST = 1;
    RecyclerView recyclerView;
    FloatingActionButton addButton;
    CustomActivity adaptor;
    MainDatabase db;
    ArrayList<String> eventId, eventTitle, eventDescription, eventDate, eventTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        // Build a recycler view to display events from the database.
        recyclerView = findViewById(R.id.recyclerView);
        addButton = findViewById(R.id.add_new);

        // Action button to add an event.
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventList.this, AddActivity.class);
                startActivityForResult(intent, REQUEST);

            }
        });

        // Initialize the database and array lists.
        db = new MainDatabase(EventList.this);
        eventId = new ArrayList<>();
        eventTitle = new ArrayList<>();
        eventDescription = new ArrayList<>();
        eventDate = new ArrayList<>();
        eventTime = new ArrayList<>();

        // Store data in the arrays.
        storeDataInArrays();

        // Builds the rows.
        adaptor = new CustomActivity(EventList.this, this, eventId, eventTitle,
                eventDescription, eventDate, eventTime);
        recyclerView.setAdapter(adaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(EventList.this));

    }

    @Override
    protected void onActivityResult(int request, int result, @Nullable Intent data) {
        super.onActivityResult(request, result, data);
        if (request == 1) {
            recreate();
        }
    }

    // Creates rows for the recycler view.
    void storeDataInArrays() {
        Cursor c = db.readAllData();
        if (c == null) {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        } else if (c.getCount() == 0) {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        } else {
            int idIndex = c.getColumnIndex("_id");
            int titleIndex = c.getColumnIndex("title");
            int descIndex = c.getColumnIndex("description");
            int dateIndex = c.getColumnIndex("date");
            int timeIndex = c.getColumnIndex("time");

            // Verify if columns exist to prevent a crash if there is a schema mismatch.
            if (idIndex == -1 || titleIndex == -1 || descIndex == -1 || dateIndex == -1 || timeIndex == -1) {
                Toast.makeText(this, "Database schema mismatch. Please reinstall app.", Toast.LENGTH_LONG).show();
                return;
            }

            while (c.moveToNext()) {
                eventId.add(c.getString(idIndex));
                eventTitle.add(c.getString(titleIndex));
                eventDescription.add(c.getString(descIndex));
                eventDate.add(c.getString(dateIndex));
                eventTime.add(c.getString(timeIndex));
            }
        }
    }
}
