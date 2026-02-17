package com.example.thomascomercs360project;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {

    Button addButton;
    Button timeButton;
    Button dateButton;
    CheckBox persistentCheckbox;
    String alarmTime;

    EditText titleInput;
    EditText descriptionInput;

    long eventId;
    private static final String CHANNEL_ID = "event_notifications";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        TextView headerTitle = findViewById(R.id.header_title);
        if (headerTitle != null) {
            headerTitle.setText("Create Event");
        }

        // Bind the buttons and input fields.
        titleInput = findViewById(R.id.title_input);
        descriptionInput = findViewById(R.id.description_input);
        timeButton = findViewById(R.id.btnTime);
        dateButton = findViewById(R.id.btnDate);
        persistentCheckbox = findViewById(R.id.checkbox_persistent);
        addButton = findViewById(R.id.add_new);

        if (getIntent().hasExtra("selected_date") && dateButton != null) {
            dateButton.setText(getIntent().getStringExtra("selected_date"));
        }

        if (dateButton != null) dateButton.setOnClickListener(view -> selectDate());
        if (timeButton != null) timeButton.setOnClickListener(view -> selectTime());

        if (titleInput != null) titleInput.addTextChangedListener(textWatcher);
        if (descriptionInput != null) descriptionInput.addTextChangedListener(textWatcher);
        if (timeButton != null) timeButton.addTextChangedListener(textWatcher);
        if (dateButton != null) dateButton.addTextChangedListener(textWatcher);


        if (addButton != null) {
            addButton.setOnClickListener(view -> {
                MainDatabase db = new MainDatabase(AddActivity.this);
                boolean isPersistent = persistentCheckbox.isChecked();

                eventId = db.addReminder(
                        dateButton.getText().toString().trim(),
                        timeButton.getText().toString().trim(),
                        titleInput.getText().toString().trim(),
                        descriptionInput.getText().toString().trim(),
                        isPersistent
                );

                if (eventId != -1) {
                    showInstantNotification(
                            titleInput.getText().toString().trim(),
                            dateButton.getText().toString().trim(),
                            timeButton.getText().toString().trim()
                    );

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
                }
                finish();
            });
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

    private void showInstantNotification(String title, String date, String time) {
        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Event Created")
                .setContentText("Event created for " + time + " " + date)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify((int) System.currentTimeMillis(), builder.build());
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Event Notifications";
            String description = "Notifications for event reminders";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void scheduleNotifications(String date, String time24h, String title, String description) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("d-M-yyyy HH:mm", Locale.getDefault());
        String dateTimeStr = date + " " + time24h;
        Date eventDate = format.parse(dateTimeStr);
        if (eventDate == null) return;

        long eventTimeInMillis = eventDate.getTime();
        long oneHourBeforeInMillis = eventTimeInMillis - (60 * 60 * 1000);

        setAlarm(eventTimeInMillis, title, "Event starting now: " + description, (int) eventId);
        if (oneHourBeforeInMillis > System.currentTimeMillis()) {
            setAlarm(oneHourBeforeInMillis, title, "Event starting in 1 hour: " + description, (int) eventId + 10000);
        }
    }

    private void setAlarm(long timeInMillis, String title, String description, int notificationId) {
        Intent intent = new Intent(AddActivity.this, AlarmActivity.class);
        intent.putExtra("event", title);
        intent.putExtra("description", description);
        intent.putExtra("notificationId", notificationId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                AddActivity.this,
                notificationId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
        }
    }

    private void selectTime() {
        Calendar cal = Calendar.getInstance();
        int hr = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timePicker, h, m) -> {
            alarmTime = String.format(Locale.getDefault(), "%02d:%02d", h, m);
            timeButton.setText(formatTime(h, m));
        }, hr, min, false);
        timePickerDialog.show();
    }

    private void selectDate() {
        Calendar cal = Calendar.getInstance();
        int yearVal = cal.get(Calendar.YEAR);
        int monthVal = cal.get(Calendar.MONTH);
        int dayVal = cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, (datePicker, year, month, day) ->
                dateButton.setText(String.format(Locale.getDefault(), "%d-%d-%d", day, month + 1, year)),
                yearVal, monthVal, dayVal);
        datePickerDialog.show();
    }

    public String formatTime(int hr, int min) {
        String minuteString = (min < 10) ? "0" + min : "" + min;
        if (hr == 0) return "12:" + minuteString + " AM";
        if (hr < 12) return hr + ":" + minuteString + " AM";
        if (hr == 12) return "12:" + minuteString + " PM";
        return (hr - 12) + ":" + minuteString + " PM";
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String title = titleInput.getText().toString().trim();
            String description = descriptionInput.getText().toString().trim();
            String date = dateButton.getText().toString().trim();
            String time = timeButton.getText().toString().trim();
            addButton.setEnabled(!title.isEmpty() && !description.isEmpty() && !date.isEmpty() && !time.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    };
}
