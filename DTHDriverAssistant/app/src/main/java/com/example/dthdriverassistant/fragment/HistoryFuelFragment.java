package com.example.dthdriverassistant.fragment;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dthdriverassistant.BuildConfig;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

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

    LinearLayout layoutReverse;
    TextView tvReverse;

    boolean flag =true; // ???? ?????o ng?????c
    //true: ng?????c, fasle: thu???n

    Button btnExport;
    private static final int PERMISSION_REQUEST_CODE = 200;

    public HistoryFuelFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history_fuel, container, false);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("L???ch s??? ????? x??ng");
        rvHisFuel = v.findViewById(R.id.rvRefuel);
        btnExport = v.findViewById(R.id.btnExport);
        layoutReverse = v.findViewById(R.id.layout_Reverse);
        tvReverse = v.findViewById(R.id.tvReverse);

        if (checkPermission()) {
            //accepted
        } else {
            requestPermission();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
        if (acct != null) {
            idUser = acct.getId();
        }

        getData(); // nh??n d??? li???u t??? fb

        //reverse list
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(v.getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        layoutReverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayoutManager mLayoutManager;
                if(flag == true){
                    //?????o ng?????c l???i khi ???????c click
                    mLayoutManager = new LinearLayoutManager(v.getContext(),  LinearLayoutManager.VERTICAL ,false);
                    flag = false;
                    rvHisFuel.setLayoutManager(mLayoutManager);
                    rvHisFuel.setHasFixedSize(true);
                    adapter = new FuelAdapter(lstFuel,v.getContext());
                    rvHisFuel.setAdapter(adapter);
                    tvReverse.setText("C?? nh???t");
                    return;
                }
                mLayoutManager = new LinearLayoutManager(v.getContext());
                mLayoutManager.setReverseLayout(true);
                mLayoutManager.setStackFromEnd(true);
                flag = true;
                rvHisFuel.setLayoutManager(mLayoutManager);
                rvHisFuel.setHasFixedSize(true);
                adapter = new FuelAdapter(lstFuel,v.getContext());
                rvHisFuel.setAdapter(adapter);
                tvReverse.setText("M???i nh???t");
            }
        });

        rvHisFuel.setLayoutManager(mLayoutManager);
        rvHisFuel.setHasFixedSize(true);
        adapter = new FuelAdapter(lstFuel,v.getContext());
        rvHisFuel.setAdapter(adapter);

        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createFilePDF();

                viewPdf("HistoryRefuel.pdf", "Dir");
            }
        });

        // Inflate the layout for this fragment
        return v;
    }

    private void createFilePDF()  {
        Document document = new Document();
        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Dir";
            File dir = new File(path);
            if(!dir.exists())
                dir.mkdirs();

            File file = new File(dir, "HistoryRefuel.pdf");
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter.getInstance(document, fOut);

            //open the document
            document.open();

            PdfPTable table = new PdfPTable(3);

            //c???t ????? ba
            table.addCell("Ngay do xang");
            table.addCell("Xe do");
            table.addCell("Gia tien");

            if(flag == false){
                for(int i = 0; i< lstFuel.size(); i++){
                    fuel fuel = lstFuel.get(i); //list ???? ?????c thanh l???c
                    table.addCell(fuel.getCalFilled());
                    table.addCell(fuel.getVehicle().getName());
                    String priceFormat = String.format("%,d",fuel.getPrice());
                    String price = priceFormat.replace(",","."); //thay , th??nh .
                    table.addCell(price + "VND");

                }
            }else{
                for(int i = lstFuel.size() - 1; i>= 0; i--){
                    fuel fuel = lstFuel.get(i); //list ???? ?????c thanh l???c
                    table.addCell(fuel.getCalFilled());
                    table.addCell(fuel.getVehicle().getName());
                    String priceFormat = String.format("%,d",fuel.getPrice());
                    String price = priceFormat.replace(",","."); //thay , th??nh .
                    table.addCell(price + "VND");
                }

            }

            document.add(table);
            Toast.makeText(getContext(),"Xu???t file th??nh c??ng!", Toast.LENGTH_SHORT).show();

        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } finally {
            document.close();
        }

    }

    private void viewPdf(String file, String directory) {

        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/" + directory + "/" + file);
        Uri uri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider",pdfFile);
        // Setting the intent for pdf reader
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(uri, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), "Can't read pdf file", Toast.LENGTH_SHORT).show();
        }
    }

    public void getData() {
        lstFuel = new ArrayList<>();
        _myRef = mDatabase.getReference("Refuel");
        //b???t s??? ki???n khi c?? b???t k??? nh??nh con n??o ???????c th??m x??a s???a, th?? s??? c???p nh???t l???i data
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
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                fuel f = snapshot.getValue(fuel.class);
                if(f == null || lstFuel == null || lstFuel.isEmpty())
                    return;
                int index = mKeys.indexOf(snapshot.getKey()); //t??m v??? tr?? c???a key trong fb t????ng ???ng key trong listPerson
                lstFuel.set(index, f); //t???i v??? tr?? ????, c???p nh???t gtri p
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

    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(getActivity(), new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(getContext(), "Permission Granted..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Permission Denined.", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            }
        }
    }
}