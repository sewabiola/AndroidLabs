package com.example.androidlabs;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Don't call setContentView here. Let child activities do that.
    }

    /**
     * Call this in onCreate() of child activities after setContentView(...).
     * It sets up the Toolbar and Navigation Drawer.
     */
    protected void setupToolbarAndDrawer() {
        // Use AndroidX's Toolbar, not android.widget.Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the DrawerLayout and ActionBarToggle
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Set the NavigationView’s listener
        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
    }

    /**
     * Handle Navigation Drawer clicks.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            // Launch MainActivity if not already on it
            if (!(this instanceof MainActivity)) {
                startActivity(new Intent(this, MainActivity.class));
            }
        } else if (id == R.id.nav_dad_joke) {
            // Launch DadJoke if not already on it
            if (!(this instanceof DadJoke)) {
                startActivity(new Intent(this, DadJoke.class));
            }
        } else if (id == R.id.nav_exit) {
            finishAffinity(); // Closes all activities
        }

        // Close the drawer after handling the item
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawers();
        return true;
    }
}
