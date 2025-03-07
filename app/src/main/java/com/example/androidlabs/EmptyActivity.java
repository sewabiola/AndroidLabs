package com.example.androidlabs;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Displays DetailsFragment in a separate Activity (for phones).
 */
public class EmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);

        // Get data from Intent
        String name = getIntent().getStringExtra("name");
        String height = getIntent().getStringExtra("height");
        String mass = getIntent().getStringExtra("mass");

        // Create the fragment with data
        DetailsFragment fragment = DetailsFragment.newInstance(name, height, mass);

        // Load it into the FrameLayout
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLocation, fragment)
                .commit();
    }
}
