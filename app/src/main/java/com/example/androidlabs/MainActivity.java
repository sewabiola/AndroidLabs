package com.example.androidlabs;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Make sure activity_main.xml has a DrawerLayout and a Toolbar
        setupToolbarAndDrawer();
    }

    /**
     * Called when an item in the Nav Drawer is selected.
     * (We override here to handle home/dad_joke/exit, or pass up to BaseActivity.)
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            // Already on Home, do nothing
        } else if (id == R.id.nav_dad_joke) {
            startActivity(new Intent(this, DadJoke.class));
        } else if (id == R.id.nav_exit) {
            finishAffinity();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawers();
        return true;
    }

    /**
     * Inflate the toolbar menu (icons, overflow).
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Handle toolbar menu item clicks.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.icon_choice_1:
//                Toast.makeText(this, "You clicked on item 1", Toast.LENGTH_SHORT).show();
//                return true;
//            case R.id.icon_choice_2:
//                Toast.makeText(this, "You clicked on item 2", Toast.LENGTH_SHORT).show();
//                return true;
//        }
        int itemId = item.getItemId();

        if (itemId == R.id.icon_choice_1) {
            Toast.makeText(this, "You clicked on item 1", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.icon_choice_2) {
            Toast.makeText(this, "You clicked on item 2", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
