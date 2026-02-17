package com.example.thomascomercs360project;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeActivity extends AppCompatActivity {

    Button calendarButton, eventListButton, requestButton;
    FloatingActionButton addButton;
    private static final int SMS_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Bind UI components.
        calendarButton = findViewById(R.id.calender_button);
        eventListButton = findViewById(R.id.event_list_button);
        requestButton = findViewById(R.id.request_button);
        addButton = findViewById(R.id.add_new);

        // Set up the calendar button listener.
        if (calendarButton != null) {
            calendarButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this, CalendarActivity.class);
                    startActivity(intent);
                }
            });
        }

        // Set up the event list button listener.
        if (eventListButton != null) {
            eventListButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this, EventList.class);
                    startActivity(intent);
                }
            });
        }
        
        // Set up the request permission button listener.
        if (requestButton != null) {
            requestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(HomeActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
                    } else {
                        ActivityCompat.requestPermissions(HomeActivity.this,
                                new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
                    }
                }
            });
        }

        // Set up the floating action button listener.
        if (addButton != null) {
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this, AddActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
