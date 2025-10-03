package com.example.activityroom;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class EventDetailsActivity extends AppCompatActivity {

    private Event currentEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        // Enable back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Get event from intent
        currentEvent = (Event) getIntent().getSerializableExtra("event");

        if (currentEvent != null) {
            displayEventData();
            setupEventListeners();
        }
    }

    private void displayEventData() {
        TextView textEventTitle = findViewById(R.id.textEventTitle);
        TextView textEventDescription = findViewById(R.id.textEventDescription);
        TextView textEventDate = findViewById(R.id.textEventDate);
        TextView textEventTime = findViewById(R.id.textEventTime);
        TextView textEventVenue = findViewById(R.id.textEventVenue);

        textEventTitle.setText(currentEvent.getTitle());
        textEventDescription.setText(currentEvent.getDescription());

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        textEventDate.setText(dateFormat.format(currentEvent.getDate()));

        textEventTime.setText(currentEvent.getTime());
        textEventVenue.setText(currentEvent.getVenue());

        // Set activity title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(currentEvent.getTitle());
        }
    }

    private void setupEventListeners() {
        Button buttonShare = findViewById(R.id.buttonShare);

        buttonShare.setOnClickListener(v -> shareEvent());
    }

    private void shareEvent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, currentEvent.getTitle());

        String shareText = String.format(
                "Check out this campus event: %s\n\n%s\n\nDate: %s\nTime: %s\nVenue: %s",
                currentEvent.getTitle(),
                currentEvent.getDescription(),
                new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(currentEvent.getDate()),
                currentEvent.getTime(),
                currentEvent.getVenue()
        );

        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(shareIntent, "Share Event"));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}