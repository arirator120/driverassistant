package com.example.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.main.CuuHo.CallHelp;
import com.example.main.DiaDiem.MapsActivity;
import com.example.main.LichSu.LichsuActivity;
import com.example.main.NhacNho.NhacNho;
import com.example.main.XuatFile.ExportExcel;

public class MainActivity extends AppCompatActivity {

    private Button btn_lichsu;
    private Button btn_nhacnho;
    private Button btn_timvitri;
    private Button btn_keugoicuuho;
    private Button btn_xuatfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_lichsu = findViewById(R.id.btn_lichsu);
        btn_nhacnho = findViewById(R.id.btn_nhacnho);
        btn_timvitri = findViewById(R.id.btn_timvitri);
        btn_keugoicuuho = findViewById(R.id.btn_keugoicuuho);
        btn_xuatfile = findViewById(R.id.btn_xuatfile);

        btn_lichsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LichsuActivity.class);
                startActivity(intent);
            }
        });

        btn_nhacnho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NhacNho.class);
                startActivity(intent);
            }
        });

        btn_timvitri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        btn_keugoicuuho.setOnClickListener(new View.OnClickListener() {
            @Override
             public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CallHelp.class);
                startActivity(intent);
             }
        });

        btn_xuatfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExportExcel.class);
                startActivity(intent);
            }
        });
    }
}