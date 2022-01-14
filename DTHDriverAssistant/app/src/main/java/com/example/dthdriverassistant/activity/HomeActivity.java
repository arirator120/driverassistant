package com.example.dthdriverassistant.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dthdriverassistant.R;
import com.example.dthdriverassistant.fragment.AddDataFragment;
import com.example.dthdriverassistant.fragment.CalendarFragment;
import com.example.dthdriverassistant.fragment.HelpFragment;
import com.example.dthdriverassistant.fragment.HistoryChangeOilFragment;
import com.example.dthdriverassistant.fragment.HistoryFuelFragment;
import com.example.dthdriverassistant.fragment.HistoryRepairPartsFragment;
import com.example.dthdriverassistant.fragment.HomeFragment;
import com.example.dthdriverassistant.fragment.LocationFragment;
import com.example.dthdriverassistant.fragment.RemindFragment;
import com.example.dthdriverassistant.model.user;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity{
    Button signOut;
    GoogleSignInClient mGoogleSignInClient;
    DrawerLayout drawer;
    NavigationView navigationView;

    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference dbReference;
    user u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new HomeFragment());
        fragmentTransaction.commit();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                displayView(item);

//                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

                return true;
            }
        });

        //phải ở dưới navigation
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //get data from google
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            setUserFireBase(acct);
            View headerView = navigationView.getHeaderView(0);
            TextView name = headerView.findViewById(R.id.nameUser);
            ImageView image = headerView.findViewById(R.id.imageAvatar);
            Button editInfo = headerView.findViewById(R.id.editInfo);

            dbReference = mDatabase.getReference("Users");
            dbReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            user u= dataSnapshot.getValue(user.class);
                            if(u.getId().equals(acct.getId())){
                                name.setText(u.getName()); //dc
                                Glide.with(getApplicationContext())
                                        .load(String.valueOf(Uri.parse(u.getAvatar())))
                                        .circleCrop()
                                        .into(image);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            editInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(HomeActivity.this, EditInfoActivity.class);
                    startActivity(i);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void init(){
        drawer = findViewById(R.id.drawer_layout);
        signOut = findViewById(R.id.signOut);
        navigationView = findViewById(R.id.nav_view);
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
                        startActivity(intent);
                        Toast.makeText(HomeActivity.this, "Đăng xuất thành công!",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

    }

    private void setUserFireBase(GoogleSignInAccount acct){
        dbReference = mDatabase.getReference("Users");
        String id = acct.getId();
        String name = acct.getDisplayName();
        String email = acct.getEmail();
        Uri avatar = acct.getPhotoUrl();

        //nếu đối tượng rỗng
        if(u==null)
            u = new user(); //khởi tạo object để dùng trong app
        if(avatar != null)
            u.setAvatar(avatar.toString());
        u.setName(name);
        u.setEmail(email);
        u.setId(id);

        dbReference.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //nếu nhánh id hk tồn tại
                if(!snapshot.exists()){
                    dbReference.child(u.getId()).setValue(u);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG",error.getMessage());
            }
        });
    }

    public void displayView(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_map:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new LocationFragment()).commit();
                break;
            case R.id.nav_home:
               getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
                break;

            case R.id.nav_calendar:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CalendarFragment()).commit();
                break;

            case R.id.nav_add:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AddDataFragment()).commit();
                break;

            case R.id.nav_help:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HelpFragment()).commit();
                break;

            case R.id.nav_his_fuel:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HistoryFuelFragment()).commit();
                break;

            case R.id.nav_his_ch_oil:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HistoryChangeOilFragment()).commit();
                break;

            case R.id.nav_his_re_parts:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HistoryRepairPartsFragment()).commit();
                break;

            case R.id.nav_reminder:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new RemindFragment()).commit();
                break;

            case R.id.signOut:
                finish();
                signOut();
                break;
        }

        item.setChecked(true);

    }
}