package com.example.activityroom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView eventsRecyclerView;
    private List<Event> eventList;
    private TextView emptyStateText;
    private EventAdapter eventAdapter;

    // This handles the result from EventFormActivity
    private ActivityResultLauncher<Intent> formLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Event newEvent = (Event) data.getSerializableExtra("newEvent");
                            if (newEvent != null) {
                                eventList.add(0, newEvent); // Add to top of list
                                eventAdapter.notifyItemInserted(0);
                                updateEmptyState();
                            }
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupRecyclerView();
        loadSampleData();
    }

    private void initializeViews() {
        eventsRecyclerView = findViewById(R.id.eventsRecyclerView);
        emptyStateText = findViewById(R.id.emptyStateText);

        FloatingActionButton fabAddEvent = findViewById(R.id.fabAddEvent);
        fabAddEvent.setOnClickListener(v -> {
            // Open the event form
            Intent intent = new Intent(MainActivity.this, EventFormActivity.class);
            formLauncher.launch(intent);
        });
    }

    private void setupRecyclerView() {
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(eventList);
        eventsRecyclerView.setAdapter(eventAdapter);
    }

    private void loadSampleData() {
        Calendar calendar = Calendar.getInstance();

        // Add sample events
        eventList.add(new Event(
                "Tech Talk: AI in Education",
                "Join us for an exciting discussion about the future of AI in educational settings.",
                calendar.getTime(),
                "2:00 PM",
                "Main Auditorium"
        ));

        calendar.add(Calendar.DAY_OF_MONTH, 2);
        eventList.add(new Event(
                "Programming Club Meeting",
                "Weekly meeting for coding enthusiasts. Bring your laptops!",
                calendar.getTime(),
                "4:30 PM",
                "Computer Lab B"
        ));

        eventAdapter.notifyDataSetChanged();
        updateEmptyState();
    }

    private void updateEmptyState() {
        if (eventList.isEmpty()) {
            emptyStateText.setVisibility(View.VISIBLE);
            eventsRecyclerView.setVisibility(View.GONE);
        } else {
            emptyStateText.setVisibility(View.GONE);
            eventsRecyclerView.setVisibility(View.VISIBLE);
        }
    }
}