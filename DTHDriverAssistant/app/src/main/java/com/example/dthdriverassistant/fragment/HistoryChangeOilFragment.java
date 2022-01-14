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
import com.example.dthdriverassistant.adapter.ChangeOilAdapter;
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
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
    LinearLayout layoutReverse;
    TextView tvReverse;

    boolean flag =true; // đã đảo ngược
    //true: ngược, fasle: thuận

    Button btnExport;
    private static final int PERMISSION_REQUEST_CODE = 200;

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
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Lịch sử thay nhớt");
        rvHisChangeOil = v.findViewById(R.id.rvHisChangeOil);
        btnExport = v.findViewById(R.id.btnExport);
        layoutReverse = v.findViewById(R.id.layout_Reverse);
        tvReverse = v.findViewById(R.id.tvReverse);

        if (checkPermission()) {
            //Accepted
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
                    rvHisChangeOil.setLayoutManager(mLayoutManager);
                    rvHisChangeOil.setHasFixedSize(true);
                    adapter = new ChangeOilAdapter(lstChangeOil,v.getContext());
                    rvHisChangeOil.setAdapter(adapter);
                    tvReverse.setText("Cũ nhất");
                    return;
                }
                mLayoutManager = new LinearLayoutManager(v.getContext());
                mLayoutManager.setReverseLayout(true);
                mLayoutManager.setStackFromEnd(true);
                flag = true;
                rvHisChangeOil.setLayoutManager(mLayoutManager);
                rvHisChangeOil.setHasFixedSize(true);
                adapter = new ChangeOilAdapter(lstChangeOil,v.getContext());
                rvHisChangeOil.setAdapter(adapter);
                tvReverse.setText("Mới nhất");
            }
        });

        rvHisChangeOil.setLayoutManager(mLayoutManager);
        rvHisChangeOil.setHasFixedSize(true);
        adapter = new ChangeOilAdapter(lstChangeOil,v.getContext());
        rvHisChangeOil.setAdapter(adapter);


        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createFilePDF();

                viewPdf("HistoryChangeOil.pdf", "Dir");
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

            File file = new File(dir, "HistoryChangeOil.pdf");
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter.getInstance(document, fOut);

            //open the document
            document.open();

            PdfPTable table = new PdfPTable(3);

            //cột đề ba
            table.addCell("Ngày thay nhot");
            table.addCell("Xe thay");
            table.addCell("Gia tien");

            if(flag == false){
                for(int i = 0; i< lstChangeOil.size(); i++){
                    oil oil = lstChangeOil.get(i); //list đã đọc thanh lọc
                    table.addCell(oil.getCalFilled());
                    table.addCell(oil.getVehicle().getName());
                    String priceFormat = String.format("%,d",oil.getPrice());
                    String price = priceFormat.replace(",","."); //thay , thành .
                    table.addCell(price + "VND");

                }
            }else{
                for(int i = lstChangeOil.size() - 1; i>= 0; i--){
                    oil oil = lstChangeOil.get(i); //list đã đọc thanh lọc
                    table.addCell(oil.getCalFilled());
                    table.addCell(oil.getVehicle().getName());
                    String priceFormat = String.format("%,d",oil.getPrice());
                    String price = priceFormat.replace(",","."); //thay , thành .
                    table.addCell(price + "VND");
                }

            }

            document.add(table);
            Toast.makeText(getContext(),"Xuất file thành công!", Toast.LENGTH_SHORT).show();

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
//        Uri path = Uri.fromFile(pdfFile);
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