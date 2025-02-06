package com.example.androidlabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class NameActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        TextView welcomeTextView = findViewById(R.id.welcomeTextView);
        Button thankYouButton = findViewById(R.id.thankYouButton);
        Button changeNameButton = findViewById(R.id.changeNameButton);

        // Get name from Intent
        String name = getIntent().getStringExtra("user_name");
        welcomeTextView.setText(getString(R.string.welcome_message, name));

        // "Thank You" button should finish the app
        thankYouButton.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            setResult(Activity.RESULT_OK, resultIntent); // Return RESULT_OK (1)
            finish(); // Closes this activity and returns to MainActivity
        });

        // "Don't Call Me That" should return to MainActivity and clear the name
        changeNameButton.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, resultIntent); // Return RESULT_CANCELED (0)
            finish(); // Closes this activity and returns to MainActivity
        });
    }
}

