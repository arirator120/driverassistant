package com.example.dthdriverassistant.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.dthdriverassistant.R;
import com.example.dthdriverassistant.activity.HomeActivity;
import com.example.dthdriverassistant.adapter.FuelAdapter;
import com.example.dthdriverassistant.model.fuel;
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
public class HistoryFuelFragment extends Fragment {
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference _myRef;
    GoogleSignInClient mGoogleSignInClient;

    List<String> mKeys = new ArrayList<>();

    RecyclerView rvHisFuel;
    List<fuel> lstFuel;
    FuelAdapter adapter;
    String idUser;

    public HistoryFuelFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history_fuel, container, false);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Lịch sử đổ xăng");
        rvHisFuel = v.findViewById(R.id.rvHisFuel);

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

        getData(); // nhân dữ liệu từ fb

        rvHisFuel.setLayoutManager(new LinearLayoutManager(v.getContext()));
        rvHisFuel.setHasFixedSize(true);
        adapter = new FuelAdapter(lstFuel,v.getContext());
        rvHisFuel.setAdapter(adapter);

        // Inflate the layout for this fragment
        return v;
    }

    public void getData() {
        lstFuel = new ArrayList<>();
        _myRef = mDatabase.getReference("Refuel");
        //bắt sự kiện khi có bất kỳ nhánh con nào được thêm xóa sửa, thì sẽ cập nhật lại data
        _myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                fuel f = snapshot.getValue(fuel.class); //
                if (f != null){
                    if(f.getIdUser().equals(idUser)){
                        lstFuel.add(f);
                        String key = snapshot.getKey();
                        mKeys.add(key);
                        adapter.notifyDataSetChanged();
                    }

                }
                //Log.d("m",g+ "");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                fuel f = snapshot.getValue(fuel.class);
                if(f == null || lstFuel == null || lstFuel.isEmpty())
                    return;
                int index = mKeys.indexOf(snapshot.getKey()); //tìm vị trí của key trong fb tương ứng key trong listPerson
                lstFuel.set(index, f); //tại vị trí đó, cập nhật gtri p
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                fuel f = snapshot.getValue(fuel.class);
                if(f == null || lstFuel == null || lstFuel.isEmpty())
                    return;
                int index = mKeys.indexOf(snapshot.getKey());
                if(index != -1){
                    lstFuel.remove(index);
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