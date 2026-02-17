package com.example.thomascomercs360project;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UpdateActivity extends AppCompatActivity {

    EditText titleInput, descriptionInput;
    String id, title, description, date, time;
    Button updateButton, deleteButton, timeButton, dateButton;
    String alarmTime;
    int notificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        // Bind button IDs.
        titleInput = findViewById(R.id.title_input_update);
        descriptionInput = findViewById(R.id.description_input_update);
        dateButton = findViewById(R.id.btn_Date_Update);
        timeButton = findViewById(R.id.btn_Time_Update);
        updateButton = findViewById(R.id.update_event);
        deleteButton = findViewById(R.id.edit_button);

        // Get intent data.
        getAndSetIntentData();

        // Set date and time input listeners.
        if (timeButton != null) timeButton.setOnClickListener(view -> {
            selectTime();
        });
        if (dateButton != null) dateButton.setOnClickListener(view -> selectDate());

        // Disable update button if fields are empty.
        if (titleInput != null) titleInput.addTextChangedListener(textWatcher);
        if (descriptionInput != null) descriptionInput.addTextChangedListener(textWatcher);
        if (timeButton != null) timeButton.addTextChangedListener(textWatcher);
        if (dateButton != null) dateButton.addTextChangedListener(textWatcher);

        // Set up the update event button.
        if (updateButton != null) {
            updateButton.setOnClickListener(view -> {

                MainDatabase db = new MainDatabase(UpdateActivity.this);
                db.updateData(id, titleInput.getText().toString().trim(),
                        descriptionInput.getText().toString().trim(),
                        dateButton.getText().toString().trim(),
                        timeButton.getText().toString().trim());

                // Set alarm if permissions are granted.
                if (ContextCompat.checkSelfPermission(UpdateActivity.this,
                        Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
                    try {
                        setAlarm(titleInput.getText().toString().trim(),
                                descriptionInput.getText().toString().trim(),
                                dateButton.getText().toString().trim(),
                                timeButton.getText().toString().trim());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(UpdateActivity.this, "For this feature you need to give permission",
                            Toast.LENGTH_SHORT).show();
                }

                finish();

            });
        }
        if (deleteButton != null) deleteButton.setOnClickListener(view -> confirmDialog());

    }

    // Makes sure everything is filled.
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (titleInput != null && descriptionInput != null && dateButton != null && timeButton != null && updateButton != null) {
                String date = dateButton.getText().toString().trim();
                String time = timeButton.getText().toString().trim();
                String title = titleInput.getText().toString().trim();
                String description = descriptionInput.getText().toString().trim();

                updateButton.setEnabled(!title.isEmpty() && !description.isEmpty() &&
                        !date.isEmpty() && !time.isEmpty());
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    // Confirm deletion of event.
    void confirmDialog() {
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle("Delete " + title + "?");
        build.setMessage("You want to delete? " + title + "?");
        build.setPositiveButton("Yes", (dialogInterface, i) -> {
            MainDatabase db = new MainDatabase(UpdateActivity.this);
            db.deleteOneRow(id);
            finish();
        });
        build.setNegativeButton("No", (dialogInterface, i) -> {

        });
        build.create().show();

    }

    // Setting data for the buttons and text from intent.
    void getAndSetIntentData() {
        if (getIntent().hasExtra("id") && getIntent().hasExtra("title") &&
                getIntent().hasExtra("description") && getIntent().hasExtra("date") &&
                getIntent().hasExtra("time")) {

            // Gets data from intent.
            id = getIntent().getStringExtra("id");
            // Parsing ID to int for notification.
            try {
                int i = Integer.parseInt(id);
                notificationId = (int) i;
            } catch (NumberFormatException e) {
                notificationId = 0;
            }

            date = getIntent().getStringExtra("date");
            time = getIntent().getStringExtra("time");
            title = getIntent().getStringExtra("title");
            description = getIntent().getStringExtra("description");

            // Sets intent data.
            if (dateButton != null) dateButton.setText(date);
            if (timeButton != null) timeButton.setText(time);
            if (titleInput != null) titleInput.setText(title);
            if (descriptionInput != null) descriptionInput.setText(description);

        } else {
            Toast.makeText(this, "Nothing to update", Toast.LENGTH_SHORT).show();
        }
    }

    // Sets alarm and notifications.
    private void setAlarm(String title, String description, String date, String time) throws ParseException {

        Intent intent = new Intent(UpdateActivity.this,
                AlarmActivity.class);
        intent.putExtra("event", title);
        intent.putExtra("description", description);
        intent.putExtra("time", time);
        intent.putExtra("date", date);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(UpdateActivity.this,
                notificationId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager Manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        String timeStr = timeButton.getText().toString();

        String dateTime = date + " " + time;

        DateFormat format = new SimpleDateFormat("d-M-yyyy hh:mm a");

        Date dateToSet = null;

        try {
            dateToSet = format.parse(dateTime);
            assert dateToSet != null;

            Manager.set(AlarmManager.RTC_WAKEUP,
                    dateToSet.getTime(),
                    pendingIntent);
            Toast.makeText(getApplicationContext(), "Alarm is set", Toast.LENGTH_SHORT).show();

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    // Selects date and time.
    private void selectTime() {
        Calendar cal = Calendar.getInstance();
        int hr = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        TimePickerDialog PickerDialog = new TimePickerDialog(this, (timePicker, i, i1) -> {
            alarmTime = i + ":" + i1;
            timeButton.setText(formatTime(i, i1));
        }, hr, min, false);
        PickerDialog.show();
    }

    private void selectDate() {
        Calendar cal = Calendar.getInstance();
        int Y = cal.get(Calendar.YEAR);
        int M = cal.get(Calendar.MONTH);
        int D = cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog PickerDialog = new DatePickerDialog(this, this::onDateSet, Y, M, D);
        PickerDialog.show();
    }

    public String formatTime(int hr, int min) {

        String T;
        String RM;

        if (min / 10 == 0) {
            RM = "0" + min;
        } else {
            RM = "" + min;
        }

        if (hr == 0) {
            time = "12" + ":" + RM + " AM";
        } else if (hr < 12) {
            time = hr + ":" + RM + " AM";
        } else if (hr == 12) {
            time = "12" + ":" + RM + " PM";
        } else {
            int temp = hr - 12;
            time = temp + ":" + RM + " PM";
        }
        return time;
    }

    @SuppressLint("SetTextI18n")
    private void onDateSet(DatePicker datePicker, int year, int month, int day) {
        dateButton.setText(String.format("%d-%d-%d", day, month + 1, year));
    }
}
