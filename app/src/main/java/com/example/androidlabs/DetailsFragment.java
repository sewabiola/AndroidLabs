package com.example.androidlabs;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A Fragment to display a single character's Name, Height, and Mass.
 */
public class DetailsFragment extends Fragment {

    // Factory method to create a fragment with the given data
    public static DetailsFragment newInstance(String name, String height, String mass) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("height", height);
        args.putString("mass", mass);
        fragment.setArguments(args);
        return fragment;
    }

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_details, container, false);

        if (getArguments() != null) {
            String name = getArguments().getString("name", "");
            String height = getArguments().getString("height", "");
            String mass = getArguments().getString("mass", "");

            TextView nameView = result.findViewById(R.id.nameTextView);
            TextView heightView = result.findViewById(R.id.heightTextView);
            TextView massView = result.findViewById(R.id.massTextView);

            nameView.setText(name);
            heightView.setText(height);
            massView.setText(mass);
        }

        return result;
    }
}
