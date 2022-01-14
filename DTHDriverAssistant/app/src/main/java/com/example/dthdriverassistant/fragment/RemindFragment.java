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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dthdriverassistant.R;
import com.example.dthdriverassistant.activity.HomeActivity;
import com.example.dthdriverassistant.adapter.RemindAdapter;
import com.example.dthdriverassistant.model.remind;
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


public class RemindFragment extends Fragment {
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference _myRef;
    GoogleSignInClient mGoogleSignInClient;

    List<String> mKeys = new ArrayList<>();

    RecyclerView rvRemind;
    List<remind> lstRemind;
    RemindAdapter adapter;
    String idUser;

    LinearLayout layoutReverse;
    TextView tvReverse;

    boolean flag =true; // đã đảo ngược
    //true: ngược, fasle: thuận

    public RemindFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_remind, container, false);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Danh sách nhắc nhở");
        rvRemind = v.findViewById(R.id.rvRemind);
        layoutReverse = v.findViewById(R.id.layout_Reverse);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
        if (acct != null) {
            idUser = acct.getId();
        }

        getData(); // nhân dữ liệu từ fb

        //reverse list
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(v.getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        layoutReverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayoutManager mLayoutManager;
                if(flag == true){
                    //đảo ngược lại khi được click
                    mLayoutManager = new LinearLayoutManager(v.getContext(),  LinearLayoutManager.VERTICAL ,false);
                    flag = false;
                    rvRemind.setLayoutManager(mLayoutManager);
                    rvRemind.setHasFixedSize(true);
                    adapter = new RemindAdapter(lstRemind ,v.getContext());
                    rvRemind.setAdapter(adapter);
                    tvReverse = v.findViewById(R.id.tvReverse);
                    tvReverse.setText("Cũ nhất");
                    rvRemind.setLayoutManager(mLayoutManager);
                    rvRemind.setHasFixedSize(true);
                    adapter = new RemindAdapter(lstRemind,v.getContext());
                    rvRemind.setAdapter(adapter);
                    tvReverse = v.findViewById(R.id.tvReverse);
                    return;
                }
                mLayoutManager = new LinearLayoutManager(v.getContext());
                mLayoutManager.setReverseLayout(true);
                mLayoutManager.setStackFromEnd(true);
                flag = true;
                rvRemind.setLayoutManager(mLayoutManager);
                rvRemind.setHasFixedSize(true);
                adapter = new RemindAdapter(lstRemind,v.getContext());
                rvRemind.setAdapter(adapter);
                tvReverse.setText("Mới nhất");
            }
        });

        rvRemind.setLayoutManager(mLayoutManager);
        rvRemind.setHasFixedSize(true);
        adapter = new RemindAdapter(lstRemind,v.getContext());
        rvRemind.setAdapter(adapter);

        return v;
    }

    public void getData() {
        lstRemind = new ArrayList<>();
        _myRef = mDatabase.getReference("Reminders");
        //bắt sự kiện khi có bất kỳ nhánh con nào được thêm xóa sửa, thì sẽ cập nhật lại data
        _myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                remind r = snapshot.getValue(remind.class);
                if (r != null){
                    if(r.getIdUser().equals(idUser)){
                        lstRemind.add(r);
                        String key = snapshot.getKey();
                        mKeys.add(key);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                remind r = snapshot.getValue(remind.class);
                if(r == null || lstRemind == null || lstRemind.isEmpty())
                    return;
                int index = mKeys.indexOf(snapshot.getKey()); //tìm vị trí của key trong fb tương ứng key trong listPerson
                lstRemind.set(index, r); //tại vị trí đó, cập nhật gtri p
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                remind r = snapshot.getValue(remind.class);
                if(r == null || lstRemind == null || lstRemind.isEmpty())
                    return;
                int index = mKeys.indexOf(snapshot.getKey());
                if(index != -1){
                    lstRemind.remove(index);
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