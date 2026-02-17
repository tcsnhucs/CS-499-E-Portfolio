package com.example.thomascomercs360project;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UpdateActivity extends AppCompatActivity {

    EditText titleInput, descriptionInput;
    String id, title, description, date, time;
    Button updateButton, deleteButton, timeButton, dateButton;
    CheckBox persistentCheckbox;
    String alarmTime;
    int notificationId;
    boolean isPersistent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        TextView headerTitle = findViewById(R.id.header_title);
        if (headerTitle != null) {
            headerTitle.setText("Update Event");
        }

        titleInput = findViewById(R.id.title_input_update);
        descriptionInput = findViewById(R.id.description_input_update);
        dateButton = findViewById(R.id.btn_Date_Update);
        timeButton = findViewById(R.id.btn_Time_Update);
        persistentCheckbox = findViewById(R.id.checkbox_persistent_update);
        updateButton = findViewById(R.id.update_event);
        deleteButton = findViewById(R.id.edit_button);

        getAndSetIntentData();

        if (timeButton != null) timeButton.setOnClickListener(view -> selectTime());
        if (dateButton != null) dateButton.setOnClickListener(view -> selectDate());

        if (titleInput != null) titleInput.addTextChangedListener(textWatcher);
        if (descriptionInput != null) descriptionInput.addTextChangedListener(textWatcher);
        if (timeButton != null) timeButton.addTextChangedListener(textWatcher);
        if (dateButton != null) dateButton.addTextChangedListener(textWatcher);

        if (updateButton != null) {
            updateButton.setOnClickListener(view -> {
                MainDatabase db = new MainDatabase(UpdateActivity.this);
                db.updateData(id, titleInput.getText().toString().trim(),
                        descriptionInput.getText().toString().trim(),
                        dateButton.getText().toString().trim(),
                        timeButton.getText().toString().trim(),
                        persistentCheckbox.isChecked());

                try {
                    scheduleNotifications(
                            dateButton.getText().toString().trim(),
                            alarmTime != null ? alarmTime : convertTo24h(timeButton.getText().toString().trim()),
                            titleInput.getText().toString().trim(),
                            descriptionInput.getText().toString().trim()
                    );
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                finish();
            });
        }
        if (deleteButton != null) deleteButton.setOnClickListener(view -> confirmDialog());
    }

    private void scheduleNotifications(String date, String time24h, String title, String description) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("d-M-yyyy HH:mm", Locale.getDefault());
        String dateTimeStr = date + " " + time24h;
        Date eventDate = format.parse(dateTimeStr);
        if (eventDate == null) return;

        long eventTimeInMillis = eventDate.getTime();
        long oneHourBeforeInMillis = eventTimeInMillis - (60 * 60 * 1000);

        setAlarm(eventTimeInMillis, title, "Event starting now: " + description, notificationId);
        if (oneHourBeforeInMillis > System.currentTimeMillis()) {
            setAlarm(oneHourBeforeInMillis, title, "Event starting in 1 hour: " + description, notificationId + 10000);
        }
    }

    private void setAlarm(long timeInMillis, String title, String description, int notificationId) {
        Intent intent = new Intent(UpdateActivity.this, AlarmActivity.class);
        intent.putExtra("event", title);
        intent.putExtra("description", description);
        intent.putExtra("notificationId", notificationId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                UpdateActivity.this,
                notificationId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
        }
    }

    private String convertTo24h(String time12h) {
        try {
            SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            SimpleDateFormat parseFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date date = displayFormat.parse(time12h);
            return parseFormat.format(date);
        } catch (ParseException e) {
            return "00:00";
        }
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String dateText = dateButton.getText().toString().trim();
            String timeText = timeButton.getText().toString().trim();
            String titleText = titleInput.getText().toString().trim();
            String descText = descriptionInput.getText().toString().trim();
            updateButton.setEnabled(!titleText.isEmpty() && !descText.isEmpty() && !dateText.isEmpty() && !timeText.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    };

    void confirmDialog() {
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle("Delete " + title + "?");
        build.setMessage("Are you sure you want to delete " + title + "?");
        build.setPositiveButton("Yes", (dialogInterface, i) -> {
            MainDatabase db = new MainDatabase(UpdateActivity.this);
            db.deleteOneRow(id);
            cancelAlarm(notificationId);
            cancelAlarm(notificationId + 10000);
            finish();
        });
        build.setNegativeButton("No", (dialogInterface, i) -> {});
        build.create().show();
    }

    private void cancelAlarm(int id) {
        Intent intent = new Intent(this, AlarmActivity.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_IMMUTABLE);
        if (pendingIntent != null) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }

    void getAndSetIntentData() {
        if (getIntent().hasExtra("id") && getIntent().hasExtra("title") &&
                getIntent().hasExtra("description") && getIntent().hasExtra("date") &&
                getIntent().hasExtra("time")) {

            id = getIntent().getStringExtra("id");
            try {
                notificationId = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                notificationId = 0;
            }

            date = getIntent().getStringExtra("date");
            time = getIntent().getStringExtra("time");
            title = getIntent().getStringExtra("title");
            description = getIntent().getStringExtra("description");
            isPersistent = getIntent().getBooleanExtra("persistent", false);

            if (dateButton != null) dateButton.setText(date);
            if (timeButton != null) timeButton.setText(time);
            if (titleInput != null) titleInput.setText(title);
            if (descriptionInput != null) descriptionInput.setText(description);
            if (persistentCheckbox != null) persistentCheckbox.setChecked(isPersistent);

        } else {
            Toast.makeText(this, "Nothing to update", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectTime() {
        Calendar cal = Calendar.getInstance();
        int hr = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        TimePickerDialog PickerDialog = new TimePickerDialog(this, (timePicker, i, i1) -> {
            alarmTime = String.format(Locale.getDefault(), "%02d:%02d", i, i1);
            timeButton.setText(formatTime(i, i1));
        }, hr, min, false);
        PickerDialog.show();
    }

    private void selectDate() {
        Calendar cal = Calendar.getInstance();
        int Y = cal.get(Calendar.YEAR);
        int M = cal.get(Calendar.MONTH);
        int D = cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog PickerDialog = new DatePickerDialog(this, (datePicker, year, month, day) -> 
            dateButton.setText(String.format(Locale.getDefault(), "%d-%d-%d", day, month + 1, year)), Y, M, D);
        PickerDialog.show();
    }

    public String formatTime(int hr, int min) {
        String RM = (min < 10) ? "0" + min : "" + min;
        if (hr == 0) return "12:" + RM + " AM";
        if (hr < 12) return hr + ":" + RM + " AM";
        if (hr == 12) return "12:" + RM + " PM";
        return (hr - 12) + ":" + RM + " PM";
    }
}
