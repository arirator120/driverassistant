package com.example.dthdriverassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dthdriverassistant.activity.HomeActivity;
import com.example.dthdriverassistant.activity.LoginActivity;
import com.example.dthdriverassistant.model.fuel;
import com.example.dthdriverassistant.model.user;
import com.example.dthdriverassistant.model.vehicle;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class EditInfoActivity extends AppCompatActivity {
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    user u;
    EditText etEditName, etEditPhone;
    Button btnEditSave, btnEditClose;
    String idUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        init();

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            idUser = acct.getId();
            //Log.d("id", idUser);
        }
        syncInfo(idUser);
        btnEditSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData(acct,u);
                finish();
                Toast.makeText(EditInfoActivity.this,"Cập nhật thành công!",Toast.LENGTH_SHORT).show();
            }
        });
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
    public void syncInfo(String idUser){
        DatabaseReference dbReference = mDatabase.getReference("Users"); //phải tạo lại dbReference
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        user _u = dataSnapshot.getValue(user.class); //
                        if (_u != null){
                            if(_u.getId().equals(idUser)){
                                etEditName.setText(_u.getName());
                                Log.d("fb doo",""+_u.getId());
                            }

                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    public void setData(GoogleSignInAccount acct ,user u){
        if(u==null){
            u= new user();
        }
        String email = acct.getEmail();
        Uri avatar = acct.getPhotoUrl();

        if(avatar != null)
            u.setAvatar(avatar.toString());
        u.setEmail(email);
        u.setId(idUser);
        u.setName(etEditName.getText()+"");
        DatabaseReference myRef = mDatabase.getReference("Users");
        myRef.child(u.getId()).setValue(u);
    }

}