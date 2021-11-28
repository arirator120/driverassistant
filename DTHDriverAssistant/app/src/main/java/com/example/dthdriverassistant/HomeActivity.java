package com.example.dthdriverassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class HomeActivity extends AppCompatActivity {
    ImageView avatarUser;
    TextView nameUser, emailUser, idUser;
    Button signOut;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    // ...
                    case R.id.signOut:
                        signOut();
                        break;
                    // ...
                }
            }
        });

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String name = acct.getDisplayName();
            String email = acct.getEmail();
            String id = acct.getId();
            Uri avatar = acct.getPhotoUrl();

            nameUser.setText(name);
            emailUser.setText(email);
            idUser.setText(id);
            Glide.with(this)
                    .load(String.valueOf(avatar))
                    .into(avatarUser);
            //avatarUser.setImageResource(avatar);
        }
    }
    private void init(){
        avatarUser = findViewById(R.id.avatarUser);
        nameUser = findViewById(R.id.nameUser);
        emailUser = findViewById(R.id.emailUser);
        idUser = findViewById(R.id.idUser);
        signOut = findViewById(R.id.signOut);
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(HomeActivity.this, "Signed out successfully",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
}