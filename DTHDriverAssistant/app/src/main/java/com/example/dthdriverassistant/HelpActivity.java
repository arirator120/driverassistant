package com.example.dthdriverassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class HelpActivity extends AppCompatActivity {
    ImageView imgCallHCM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        init();
        imgCallHCM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String call = "tel:0918938852";
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(call)));
            }
        });
    }
    public void init(){
        imgCallHCM= findViewById(R.id.imgCallHCM);
    }
}