package com.example.dthdriverassistant.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.dthdriverassistant.R;
import com.example.dthdriverassistant.activity.HomeActivity;
import com.example.dthdriverassistant.activity.add.AddChangeOilActivity;
import com.example.dthdriverassistant.activity.add.AddFuelActivity;
import com.example.dthdriverassistant.activity.add.AddReminderActivity;
import com.example.dthdriverassistant.activity.add.AddRepairPartsActivity;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class AddDataFragment extends Fragment implements View.OnClickListener {

    ImageButton imgBtnPetrol, imgBtnOil, imgBtnParts, imgBtnReminder;

    public AddDataFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_add_data, container, false);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Thêm dữ liệu");
        init(view);

        //khi click vao icon
        imgBtnPetrol.setOnClickListener(this);
        imgBtnOil.setOnClickListener(this);
        imgBtnParts.setOnClickListener(this);
        imgBtnReminder.setOnClickListener(this);

        return view;
    }

    private void init(View view){
        imgBtnPetrol = view.findViewById(R.id.imgBtnPetrol);
        imgBtnOil = view.findViewById(R.id.imgBtnOil);
        imgBtnParts = view.findViewById(R.id.imgBtnParts);
        imgBtnReminder = view.findViewById(R.id.imgBtnReminder);
    }

    @Override
    public void onClick(View view) {
        Intent i;
        switch(view.getId()){
            case R.id.imgBtnPetrol:
                i = new Intent(getContext(), AddFuelActivity.class);
                startActivity(i);
                break;
            case R.id.imgBtnOil:
                i = new Intent(getContext(), AddChangeOilActivity.class);
                startActivity(i);
                break;
            case R.id.imgBtnParts:
                i = new Intent(getContext(), AddRepairPartsActivity.class);
                startActivity(i);
                break;
            case R.id.imgBtnReminder:
                i = new Intent(getContext(), AddReminderActivity.class);
                startActivity(i);
                break;
        }
    }
}