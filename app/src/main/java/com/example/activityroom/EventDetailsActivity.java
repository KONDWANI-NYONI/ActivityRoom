package com.example.activityroom;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EventDetailsActivity extends AppCompatActivity {

    private Event currentEvent;
    private ImageView eventImage;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private static final int REQUEST_PERMISSION_CODE = 100;
    private String currentImagePath;

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
        eventImage = findViewById(R.id.eventImage);

        textEventTitle.setText(currentEvent.getTitle());
        textEventDescription.setText(currentEvent.getDescription());

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        textEventDate.setText(dateFormat.format(currentEvent.getDate()));

        textEventTime.setText(currentEvent.getTime());
        textEventVenue.setText(currentEvent.getVenue());

        // Load image if exists
        if (currentEvent.getImagePath() != null && !currentEvent.getImagePath().isEmpty()) {
            File imgFile = new File(currentEvent.getImagePath());
            if (imgFile.exists()) {
                eventImage.setImageURI(Uri.fromFile(imgFile));
            }
        }

        // Set activity title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(currentEvent.getTitle());
        }
    }

    private void setupEventListeners() {
        Button buttonShare = findViewById(R.id.buttonShare);
        Button buttonAddImage = findViewById(R.id.buttonAddImage);

        buttonShare.setOnClickListener(v -> shareEvent());
        buttonAddImage.setOnClickListener(v -> checkPermissionsAndShowImageOptions());
    }

    private void checkPermissionsAndShowImageOptions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    REQUEST_PERMISSION_CODE);
        } else {
            showImageOptions();
        }
    }

    private void showImageOptions() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Image Source");
        startActivityForResult(chooserIntent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_PICK) {
                if (data != null) {
                    Uri selectedImage = data.getData();
                    eventImage.setImageURI(selectedImage);

                    // Save the image path to the event
                    currentEvent.setImagePath(getPathFromUri(selectedImage));
                    Toast.makeText(this, "Image added successfully!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private String getPathFromUri(Uri contentUri) {
        // For simplicity, returning the string representation
        // In a real app, you might want to copy the file to your app's directory
        return contentUri.toString();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showImageOptions();
            } else {
                Toast.makeText(this, "Permission denied. Cannot access gallery.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void shareEvent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        // Include image if available
        if (currentEvent.getImagePath() != null && !currentEvent.getImagePath().isEmpty()) {
            File imageFile = new File(currentEvent.getImagePath());
            if (imageFile.exists()) {
                Uri imageUri = FileProvider.getUriForFile(this,
                        "com.example.activityroom.fileprovider", imageFile);
                shareIntent.setType("image/*");
                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        } else {
            shareIntent.setType("text/plain");
        }

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
