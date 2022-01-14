package com.example.dthdriverassistant.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.dthdriverassistant.R;
import com.example.dthdriverassistant.activity.HomeActivity;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class LocationFragment extends Fragment {
    private Button btnLocation;

    public LocationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_location, container, false);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Google Maps");

        btnLocation = v.findViewById(R.id.btnLocation);
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("geo:10.732621995208257, 106.69942367891035"));
                Intent chooser = Intent.createChooser(i, "Find Locations");
                startActivity(chooser);
            }
        });
        return v;
    }
}