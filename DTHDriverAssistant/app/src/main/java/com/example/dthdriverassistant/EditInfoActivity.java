package com.example.dthdriverassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditInfoActivity extends AppCompatActivity {

    EditText etEditName, etEditPhone;
    Button btnEditSave, btnEditClose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        init();
        btnEditClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void init(){
        etEditName= findViewById(R.id.etEditName);
        etEditPhone= findViewById(R.id.etEditPhone);
        btnEditSave= findViewById(R.id.btnEditSave);
        btnEditClose= findViewById(R.id.btnEditClose);
    }
}