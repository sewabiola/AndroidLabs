package com.example.androidlabs;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_linear);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        Button button = findViewById(R.id.button);
        EditText editText = findViewById(R.id.editText);
        TextView textView = findViewById(R.id.textView);

        button.setOnClickListener(v -> {
            String text = editText.getText().toString();
            textView.setText(text);

            Toast.makeText(MainActivity.this,
                    getResources().getString(R.string.toast_message),
                    Toast.LENGTH_SHORT).show();
        });

        CheckBox checkBox = findViewById(R.id.checkbox);

        checkBox.setOnCheckedChangeListener((cb, isChecked) -> {
            Snackbar snackbar = Snackbar.make(cb,
                    "The checkbox is now " + (isChecked ? "on" : "off"),
                    Snackbar.LENGTH_LONG);

            snackbar.setAction("Undo", v -> cb.setChecked(!isChecked));
            snackbar.show();
        });
    }

}