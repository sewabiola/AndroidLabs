package com.example.androidlabs;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Main screen: Shows a list of Star Wars characters from SWAPI.
 * On tablets, displays details in a fragment.
 * On phones, starts EmptyActivity to show details.
 */
public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<CharacterData> charactersList = new ArrayList<>();
    private CharacterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // For tablets, if you have a res/layout-sw720dp/activity_main.xml,
        // that file will be used automatically.

        // Setup ListView
        listView = findViewById(R.id.characterListView);
        adapter = new CharacterAdapter();
        listView.setAdapter(adapter);

        // Fetch data from SWAPI
        new FetchStarWarsTask().execute("https://swapi.dev/api/people/?format=json");

        // Handle list clicks
        listView.setOnItemClickListener((parent, view, position, id) -> {
            CharacterData selected = charactersList.get(position);

            // Check if tablet layout (FrameLayout present)
            View fragmentContainer = findViewById(R.id.fragmentLocation);
            if (fragmentContainer == null) {
                // PHONE: Launch EmptyActivity with extras
                Intent intent = new Intent(MainActivity.this, EmptyActivity.class);
                intent.putExtra("name", selected.name);
                intent.putExtra("height", selected.height);
                intent.putExtra("mass", selected.mass);
                startActivity(intent);
            } else {
                // TABLET: Replace fragment in the same activity
                DetailsFragment fragment = DetailsFragment.newInstance(
                        selected.name,
                        selected.height,
                        selected.mass
                );
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentLocation, fragment)
                        .commit();
            }
        });
    }

    /**
     * AsyncTask to fetch Star Wars characters from SWAPI.
     */
    private class FetchStarWarsTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                if (conn.getResponseCode() == 200) {
                    InputStream inputStream = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    result = sb.toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String jsonText) {
            super.onPostExecute(jsonText);
            try {
                JSONObject jsonObject = new JSONObject(jsonText);
                JSONArray results = jsonObject.getJSONArray("results");

                for (int i = 0; i < results.length(); i++) {
                    JSONObject obj = results.getJSONObject(i);
                    String name = obj.getString("name");
                    String height = obj.getString("height");
                    String mass = obj.getString("mass");
                    charactersList.add(new CharacterData(name, height, mass));
                }
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Simple data class for character info.
     */
    private static class CharacterData {
        String name;
        String height;
        String mass;

        CharacterData(String name, String height, String mass) {
            this.name = name;
            this.height = height;
            this.mass = mass;
        }
    }

    /**
     * Adapter to display names in the ListView.
     */
    private class CharacterAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return charactersList.size();
        }

        @Override
        public Object getItem(int position) {
            return charactersList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                // Use a simple built-in layout
                convertView = getLayoutInflater().inflate(
                        android.R.layout.simple_list_item_1, parent, false
                );
            }
            TextView text = convertView.findViewById(android.R.id.text1);
            text.setText(charactersList.get(position).name);
            return convertView;
        }
    }
}
