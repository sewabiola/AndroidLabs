package com.example.androidlabs;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText nameEditText;
    private static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_NAME = "userName";

    // Activity Result Launcher to handle the response from NameActivity
    private final ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_CANCELED) {
                    // User wants to change their name, clear the EditText
                    nameEditText.setText("");
                } else if (result.getResultCode() == Activity.RESULT_OK) {
                    // User is happy, close the app
                    finish();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameEditText = findViewById(R.id.nameEditText);
        Button nextButton = findViewById(R.id.nextButton);

        // Load saved name from SharedPreferences if it exists
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        nameEditText.setText(prefs.getString(KEY_NAME, ""));

        // Set click listener for the "Next" button
        nextButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            if (!name.isEmpty()) {
                Intent intent = new Intent(MainActivity.this, NameActivity.class);
                intent.putExtra("user_name", name);
                activityResultLauncher.launch(intent); // Use the new API instead of startActivityForResult()
            } else {
                nameEditText.setError("Please enter a name");
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Save the current name input in SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit().putString(KEY_NAME, nameEditText.getText().toString()).apply();
    }
}
