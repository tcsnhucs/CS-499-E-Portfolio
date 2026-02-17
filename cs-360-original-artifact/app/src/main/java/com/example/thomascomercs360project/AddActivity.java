package com.example.thomascomercs360project;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddActivity extends AppCompatActivity {

    Button addButton;
    Button timeButton;
    Button dateButton;
    Button requestButton;
    String alarmTime;
    private final int SMS_PERMISSION_CODE = 1;

    EditText titleInput;
    EditText descriptionInput;

    long notificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // Bind the buttons and input fields.
        titleInput = findViewById(R.id.title_input);
        descriptionInput = findViewById(R.id.description_input);
        timeButton = findViewById(R.id.btnTime);
        dateButton = findViewById(R.id.btnDate);
        addButton = findViewById(R.id.add_new);

        // Check if a date was passed from the previous activity.
        if (getIntent().hasExtra("selected_date") && dateButton != null) {
            dateButton.setText(getIntent().getStringExtra("selected_date"));
        }

        // Set listeners for time and date buttons.
        if (dateButton != null) dateButton.setOnClickListener(view -> selectDate());
        if (timeButton != null) timeButton.setOnClickListener(view -> selectTime());

        // Add text watcher to disable the add button if fields are empty.
        if (titleInput != null) titleInput.addTextChangedListener(textWatcher);
        if (descriptionInput != null) descriptionInput.addTextChangedListener(textWatcher);
        if (timeButton != null) timeButton.addTextChangedListener(textWatcher);
        if (dateButton != null) dateButton.addTextChangedListener(textWatcher);


        // Set up the add event button.
        if (addButton != null) {
            addButton.setOnClickListener(view -> {

                MainDatabase db = new MainDatabase(AddActivity.this);

                // Check for permissions.
                requestPermission();


                notificationId = db.addReminder(
                        dateButton.getText().toString().trim(),
                        timeButton.getText().toString().trim(),
                        titleInput.getText().toString().trim(),
                        descriptionInput.getText().toString().trim()
                );


                // The alarm is set here unless permission was not granted.
                if (ContextCompat.checkSelfPermission(AddActivity.this,
                        android.Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
                    try {
                        setAlarm(
                                dateButton.getText().toString().trim(),
                                timeButton.getText().toString().trim(),
                                titleInput.getText().toString().trim(),
                                descriptionInput.getText().toString().trim()
                        );
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(AddActivity.this, "For this feature you need to give permission", Toast.LENGTH_SHORT).show();
                }

                finish();

            });
        }
    }

    // Set the alarm.
    private void setAlarm(String date, String time, String text, String description) throws ParseException {

        Intent intent = new Intent(AddActivity.this, AlarmActivity.class);
        intent.putExtra("time", date);
        intent.putExtra("date", time);
        intent.putExtra("event", text);
        intent.putExtra("description", description);

        PendingIntent pending = PendingIntent.getBroadcast(
                AddActivity.this,
                (int) notificationId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        // Overwrite the event.
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        String dateTime = date + " " + alarmTime;
        DateFormat format = new SimpleDateFormat("d-M-yyyy hh:mm");
        try {
            Date dateToSet = format.parse(dateTime);
            assert dateToSet != null;

            alarmManager.set(AlarmManager.RTC_WAKEUP,
                    dateToSet.getTime(),
                    pending);
            Toast.makeText(getApplicationContext(), "Alarm set", Toast.LENGTH_SHORT).show();

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    // Select the date and time.
    private void selectTime() {
        Calendar cal = Calendar.getInstance();
        int hr = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timePicker, i, i1) -> {
            alarmTime = i + ":" + i1;
            timeButton.setText(formatTime(i, i1));
        }, hr, min, false);
        timePickerDialog.show();
    }

    private void selectDate() {
        Calendar cal = Calendar.getInstance();
        int yearVal = cal.get(Calendar.YEAR);
        int monthVal = cal.get(Calendar.MONTH);
        int dayVal = cal.get(Calendar.DAY_OF_MONTH);
        @SuppressLint("DefaultLocale") DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, (datePicker, year, month, day) ->
                dateButton.setText(String.format(
                        "%d-%d-%d", day, month + 1, year)),
                yearVal, monthVal, dayVal);
        datePickerDialog.show();
    }

    public String formatTime(int hr, int min) {

        String formattedTime;
        String minuteString;

        if (min / 10 == 0) {
            minuteString = "0" + min;
        } else {
            minuteString = "" + min;
        }

        // Reading time.
        if (hr == 0) {
            formattedTime = "12" + ":" + minuteString + " AM";
        } else if (hr < 12) {
            formattedTime = hr + ":" + minuteString + " AM";
        } else if (hr == 12) {
            formattedTime = "12" + ":" + minuteString + " PM";
        } else {
            int temp = hr - 12;
            formattedTime = temp + ":" + minuteString + " PM";
        }
        return formattedTime;
    }

    // Makes sure all text is filled in.
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (titleInput != null && descriptionInput != null && dateButton != null && timeButton != null) {
                String title = titleInput.getText().toString().trim();
                String description = descriptionInput.getText().toString().trim();
                String date = dateButton.getText().toString().trim();
                String time = timeButton.getText().toString().trim();

                if (addButton != null) {
                    addButton.setEnabled(!title.isEmpty() && !description.isEmpty() &&
                            !date.isEmpty() && !time.isEmpty());
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };


    // Check permissions.
    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_SMS)) {

            new AlertDialog.Builder(this)
                    .setTitle("SMS messages")
                    .setMessage("You need to give permissions for reminders")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(
                                    AddActivity.this,
                                    new String[]{android.Manifest.permission.READ_SMS},
                                    SMS_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_SMS},
                    SMS_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int request,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grant) {
        super.onRequestPermissionsResult(request,
                permissions,
                grant);
        if (request == SMS_PERMISSION_CODE) {
            if (grant.length > 0 && grant[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
