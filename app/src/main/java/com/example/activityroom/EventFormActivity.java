package com.example.activityroom;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Calendar;

public class EventFormActivity extends AppCompatActivity {

    private TextInputEditText editEventTitle, editEventDescription, editEventDate, editEventTime, editEventVenue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_form);

        // Enable back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Add New Event");
        }

        initializeViews();
        setupEventListeners();
    }

    private void initializeViews() {
        editEventTitle = findViewById(R.id.editEventTitle);
        editEventDescription = findViewById(R.id.editEventDescription);
        editEventDate = findViewById(R.id.editEventDate);
        editEventTime = findViewById(R.id.editEventTime);
        editEventVenue = findViewById(R.id.editEventVenue);

        Button buttonSave = findViewById(R.id.buttonSave);
        Button buttonCancel = findViewById(R.id.buttonCancel);

        buttonSave.setOnClickListener(v -> saveEvent());
        buttonCancel.setOnClickListener(v -> finish());
    }

    private void setupEventListeners() {
        // Date picker
        editEventDate.setOnClickListener(v -> showDatePicker());

        // Time picker
        editEventTime.setOnClickListener(v -> showTimePicker());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, day) -> {
                    String selectedDate = (month + 1) + "/" + day + "/" + year;
                    editEventDate.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hour, minute) -> {
                    String amPm = hour < 12 ? "AM" : "PM";
                    int displayHour = hour > 12 ? hour - 12 : hour;
                    if (displayHour == 0) displayHour = 12;
                    String selectedTime = String.format("%d:%02d %s", displayHour, minute, amPm);
                    editEventTime.setText(selectedTime);
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
        );
        timePickerDialog.show();
    }

    private void saveEvent() {
        String title = editEventTitle.getText().toString().trim();
        String description = editEventDescription.getText().toString().trim();
        String date = editEventDate.getText().toString().trim();
        String time = editEventTime.getText().toString().trim();
        String venue = editEventVenue.getText().toString().trim();

        // Simple validation
        if (title.isEmpty() || date.isEmpty() || time.isEmpty() || venue.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create new event
        Event event = new Event(title, description, Calendar.getInstance().getTime(), time, venue);

        // Show success message
        Toast.makeText(this, "Event created successfully!", Toast.LENGTH_SHORT).show();

        // Return to main activity with the new event
        Intent resultIntent = new Intent();
        resultIntent.putExtra("newEvent", event);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}