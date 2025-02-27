package com.example.androidlabs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * MainActivity for randomly fetching cat images from cataas.
 */
public class MainActivity extends AppCompatActivity {

    private ImageView imageViewCat;
    private ProgressBar progressBarTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageViewCat = findViewById(R.id.imageViewCat);
        progressBarTimer = findViewById(R.id.progressBarTimer);

        // Start the AsyncTask
        new CatImages(this).execute();
        Log.d("MainActivity", "AsyncTask started!");
    }

    /**
     * CatImages AsyncTask that continually fetches new cat images in the background.
     */
    private static class CatImages extends AsyncTask<String, Integer, String> {

        private final WeakReference<MainActivity> activityRef;
        private Bitmap currentCatBitmap;

        CatImages(MainActivity activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        protected String doInBackground(String... params) {
            // Keep fetching cat images forever
            while (true) {
                try {
                    fetchAndLoadCatImage();

                    // Display the cat for ~3 seconds (0..99 on progress bar)
                    for (int i = 0; i < 100; i++) {
                        publishProgress(i);
                        Thread.sleep(30);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            MainActivity activity = activityRef.get();
            if (activity == null) return; // if the activity no longer exists

            // Update the progress bar
            int progressVal = values[0];
            activity.progressBarTimer.setProgress(progressVal);

            // Every time we reset to 0, set the new cat image
            if (progressVal == 0 && currentCatBitmap != null) {
                activity.imageViewCat.setImageBitmap(currentCatBitmap);
            }
        }

        /**
         * Fetches the random cat JSON, checks local cache, downloads if needed,
         * and updates currentCatBitmap.
         */
        private void fetchAndLoadCatImage() {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                // 1. Get random cat JSON from cataas
                URL catJsonUrl = new URL("https://cataas.com/cat?json=true");
                urlConnection = (HttpURLConnection) catJsonUrl.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read JSON response
                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream == null) {
                    return;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder jsonBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line).append("\n");
                }
                String jsonStr = jsonBuilder.toString();

                // 2. Parse 'id' and 'url' from JSON
                JSONObject jsonObject = new JSONObject(jsonStr);
                String catId = jsonObject.getString("id");
                String catUrl = jsonObject.getString("url");

                // Some cataas results may already have "https://..." in 'url'.
                // So check if it starts with "http". If not, prepend "https://cataas.com".
                if (!catUrl.startsWith("http")) {
                    catUrl = "https://cataas.com" + catUrl;
                }

                // Retrieve our Activity reference & local files dir
                MainActivity activity = activityRef.get();
                if (activity == null) {
                    return; // activity no longer valid
                }
                File directory = activity.getFilesDir();
                File catImageFile = new File(directory, catId + ".jpg");

                // Download only if not cached
                if (!catImageFile.exists()) {
                    downloadCatImage(catUrl, catImageFile);
                }

                // Log file info
                Log.d("CatImages", "Starting download for cat ID: " + catId);
                Log.d("CatImages", "Wrote file: " + catImageFile.getAbsolutePath()
                        + ", size = " + catImageFile.length());

                // Decode into a Bitmap
                Log.d("CatImages", "Decoding file: " + catImageFile.getAbsolutePath());
                currentCatBitmap = BitmapFactory.decodeFile(catImageFile.getAbsolutePath());
                Log.d("CatImages", "Bitmap is null? " + (currentCatBitmap == null));

                // Immediately trigger onProgressUpdate(0) to show the new image
                publishProgress(0);

            } catch (Exception e) {
                Log.e("CatImages", "Error fetching cat image", e);
            } finally {
                // Cleanup
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e("CatImages", "Error closing reader", e);
                    }
                }
            }
        }

        /**
         * Downloads the cat image from the given URL and saves to the given File.
         */
        private void downloadCatImage(String imageUrl, File outFile) {
            HttpURLConnection connection = null;
            InputStream input = null;
            FileOutputStream output = null;

            try {
                URL url = new URL(imageUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // If not 200 OK, abort
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return;
                }

                input = connection.getInputStream();
                output = new FileOutputStream(outFile);

                byte[] data = new byte[4096];
                int count;
                while ((count = input.read(data)) != -1) {
                    output.write(data, 0, count);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (output != null) output.close();
                    if (input != null) input.close();
                } catch (IOException ignored) { }

                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
    }
}
