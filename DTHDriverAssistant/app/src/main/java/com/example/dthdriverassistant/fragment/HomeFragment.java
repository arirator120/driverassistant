package com.example.dthdriverassistant.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dthdriverassistant.R;
import com.example.dthdriverassistant.activity.HomeActivity;
import com.example.dthdriverassistant.activity.add.AddVehicleActivity;
import com.example.dthdriverassistant.adapter.FuelAdapter;
import com.example.dthdriverassistant.adapter.VehicleAdapter;
import com.example.dthdriverassistant.model.fuel;
import com.example.dthdriverassistant.model.vehicle;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference _myRef;
    GoogleSignInClient mGoogleSignInClient;

    List<String> mKeys = new ArrayList<>();

    RecyclerView rvHome;
    CardView layout_item_add;
    List<vehicle> lstVehicle;
    VehicleAdapter adapter;
    String idUser;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Trang chủ");

        rvHome = v.findViewById(R.id.rvHome);
        layout_item_add = v.findViewById(R.id.layout_item_add);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
        if (acct != null) {
            idUser = acct.getId();
            //Log.d("id", idUser);
        }

        layout_item_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), AddVehicleActivity.class);
                startActivity(i);
            }
        });

        getData(); // nhân dữ liệu từ fb

        rvHome.setLayoutManager(new LinearLayoutManager(v.getContext()));
        rvHome.setHasFixedSize(true);
        adapter = new VehicleAdapter(lstVehicle,v.getContext());
        rvHome.setAdapter(adapter);

        // Inflate the layout for this fragment
        return v;
    }

    public void getData() {
        lstVehicle = new ArrayList<>();
        _myRef = mDatabase.getReference("UsersVehicle");
        //bắt sự kiện khi có bất kỳ nhánh con nào được thêm xóa sửa, thì sẽ cập nhật lại data
        _myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                vehicle v = snapshot.getValue(vehicle.class); //
                if (v != null){
                    if(v.getIdUser().equals(idUser)){
                        lstVehicle.add(v);
                        String key = snapshot.getKey();
                        mKeys.add(key);
                        adapter.notifyDataSetChanged();
                    }

                }
                //Log.d("m",g+ "");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                vehicle v = snapshot.getValue(vehicle.class);
                if(v == null || lstVehicle == null || lstVehicle.isEmpty())
                    return;
                int index = mKeys.indexOf(snapshot.getKey());
                lstVehicle.set(index, v); //tại vị trí đó, cập nhật gtri p
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                vehicle v = snapshot.getValue(vehicle.class);
                if(v == null || lstVehicle == null || lstVehicle.isEmpty())
                    return;
                int index = mKeys.indexOf(snapshot.getKey());
                if(index != -1){
                    lstVehicle.remove(index);
                    mKeys.remove(index);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(adapter!=null)
            adapter.release();
    }
}