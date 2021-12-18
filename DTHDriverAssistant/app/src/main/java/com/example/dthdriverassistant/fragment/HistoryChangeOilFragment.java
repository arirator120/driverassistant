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

import com.example.dthdriverassistant.R;
import com.example.dthdriverassistant.activity.HomeActivity;
import com.example.dthdriverassistant.adapter.ChangeOilAdapter;
import com.example.dthdriverassistant.adapter.FuelAdapter;
import com.example.dthdriverassistant.model.fuel;
import com.example.dthdriverassistant.model.oil;
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
public class HistoryChangeOilFragment extends Fragment {
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference _myRef;
    GoogleSignInClient mGoogleSignInClient;

    List<String> mKeys = new ArrayList<>();

    RecyclerView rvHisChangeOil;
    List<oil> lstChangeOil;
    ChangeOilAdapter adapter;
    String idUser;
    public HistoryChangeOilFragment() {
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
        View v = inflater.inflate(R.layout.fragment_history_change_oil, container, false);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Lịch sử thay gel");
        rvHisChangeOil = v.findViewById(R.id.rvHisChangeOil);

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

        rvHisChangeOil.setLayoutManager(new LinearLayoutManager(v.getContext()));
        rvHisChangeOil.setHasFixedSize(true);
        adapter = new ChangeOilAdapter(lstChangeOil,v.getContext());
        rvHisChangeOil.setAdapter(adapter);

        return v;
    }

    public void getData() {
        lstChangeOil = new ArrayList<>();
        _myRef = mDatabase.getReference("ChangeOil"); //tham chiếu tới path
        //bắt sự kiện khi có bất kỳ nhánh con nào được thêm xóa sửa, thì sẽ cập nhật lại data
        _myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                oil o = snapshot.getValue(oil.class); //
                if (o != null){
                    if(o.getIdUser().equals(idUser)){
                        lstChangeOil.add(o);
                        String key = snapshot.getKey();
                        mKeys.add(key);
                        adapter.notifyDataSetChanged();
                    }

                }
                //Log.d("m",g+ "");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                oil o = snapshot.getValue(oil.class);
                if(o == null || lstChangeOil == null || lstChangeOil.isEmpty())
                    return;
                int index = mKeys.indexOf(snapshot.getKey()); //tìm vị trí của key trong fb tương ứng key trong listPerson
                lstChangeOil.set(index, o);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                oil o = snapshot.getValue(oil.class);
                if(o == null || lstChangeOil == null || lstChangeOil.isEmpty())
                    return;
                int index = mKeys.indexOf(snapshot.getKey());
                if(index != -1){
                    lstChangeOil.remove(index);
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