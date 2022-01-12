package com.example.dthdriverassistant.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dthdriverassistant.R;
import com.example.dthdriverassistant.activity.HomeActivity;
import com.example.dthdriverassistant.adapter.HelpAdapter;
import com.example.dthdriverassistant.adapter.RemindAdapter;
import com.example.dthdriverassistant.model.help;
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


public class HelpFragment extends Fragment {
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference _myRef;
    GoogleSignInClient mGoogleSignInClient;

    List<String> mKeys = new ArrayList<>();

    RecyclerView rvHelp;
    List<help> lstHelp;
    HelpAdapter adapter;
    String idUser;
    SearchView searchBar;


    public HelpFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_help, container, false);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Gọi hỗ trợ");
        rvHelp = v.findViewById(R.id.rvHelp);
        searchBar = v.findViewById(R.id.searchBar);

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

        rvHelp.setLayoutManager(new LinearLayoutManager(v.getContext()));
        rvHelp.setHasFixedSize(true);
        adapter = new HelpAdapter(lstHelp,v.getContext());
        rvHelp.setAdapter(adapter);

        if(searchBar != null){
            searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return true;
                }
            });
        }

        // Inflate the layout for this fragment
        return v;
    }
    public void search(String text){
        ArrayList<help> s = new ArrayList<>();
        for (help object: lstHelp){
            if(object.getName().toLowerCase().contains(text.toLowerCase())){
                s.add(object);
            }
        }

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvHelp.setLayoutManager(mLayoutManager);
        rvHelp.setHasFixedSize(true);
        adapter = new HelpAdapter(s,getContext());
        rvHelp.setAdapter(adapter);
    }
    public void getData() {
        lstHelp = new ArrayList<>();
        _myRef = mDatabase.getReference("Helps");
        //bắt sự kiện khi có bất kỳ nhánh con nào được thêm xóa sửa, thì sẽ cập nhật lại data
        _myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                help h = snapshot.getValue(help.class); //

                if (h != null){
//                    if(h.getIdUser().equals(idUser)){
                        lstHelp.add(h);
                        String key = snapshot.getKey();
                        mKeys.add(key);
                        adapter.notifyDataSetChanged();
//                    }
                }
                //Log.d("m",g+ "");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                help h = snapshot.getValue(help.class);
                if(h == null || lstHelp == null || lstHelp.isEmpty())
                    return;
                int index = mKeys.indexOf(snapshot.getKey()); //tìm vị trí của key trong fb tương ứng key trong listPerson
                lstHelp.set(index, h); //tại vị trí đó, cập nhật gtri p
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                help h = snapshot.getValue(help.class);
                if(h == null || lstHelp == null || lstHelp.isEmpty())
                    return;
                int index = mKeys.indexOf(snapshot.getKey());
                if(index != -1){
                    lstHelp.remove(index);
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