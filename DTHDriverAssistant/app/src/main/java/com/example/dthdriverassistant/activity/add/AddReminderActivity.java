package com.example.dthdriverassistant.activity.add;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dthdriverassistant.R;
import com.example.dthdriverassistant.fragment.RemindFragment;
import com.example.dthdriverassistant.model.remind;
import com.example.dthdriverassistant.model.vehicle;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddReminderActivity extends AppCompatActivity {
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference dbReference;
    GoogleSignInClient mGoogleSignInClient;

    Spinner spVehicles;
    EditText etAction,etDay, etNote;
    Button btnSave;

    List<vehicle> lstVehicle;
    ArrayAdapter<vehicle> adapter_vehicle;
    String idUser;
    Bundle bundle;
    remind r ;
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        inIt();
        bundle =  getIntent().getExtras();

        etDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChonNgay();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            idUser = acct.getId();
        }

        inItMyVehicles();

        //Set toolbar
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");
        //set up back button
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                Intent i = new Intent(AddReminderActivity.this, RemindFragment.class);
                startService(i);
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(errorMsg() == ""){
                    setData(r);
                    finish();
                }else{
                    Toast.makeText(AddReminderActivity.this, errorMsg(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    void inIt()
    {
        etAction = findViewById(R.id.etAction);
        etDay = findViewById(R.id.etDay);
        etNote = findViewById(R.id.etNote);
        spVehicles = findViewById(R.id.spVehicles);
        btnSave = findViewById(R.id.btnSave);
    }

    private void ChonNgay()
    {
        final Calendar calendar = Calendar.getInstance();
        int ngay = calendar.get(Calendar.DATE);
        int thang = calendar.get(Calendar.MONTH);
        int nam = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(AddReminderActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int i, int i1, int i2) {
                calendar.set(i, i1, i2);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                etDay.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, nam, thang, ngay);
        datePickerDialog.show();
    }
    public void inItMyVehicles(){
        dbReference = mDatabase.getReference("UsersVehicle");
        lstVehicle = new ArrayList<>();
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(lstVehicle != null || !lstVehicle.isEmpty())
                    lstVehicle.clear();
                vehicle defaultVehicle = new vehicle("","Chọn xe","",null,"","",0.0,"",""); //label
                lstVehicle.add(0, defaultVehicle);
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        //kiểm tra khóa ngoại
                        if(dataSnapshot.child("idUser").getValue().equals(idUser)){
                            vehicle v = dataSnapshot.getValue(vehicle.class);
                            lstVehicle.add(v);
                        }

                    }
                    adapter_vehicle.notifyDataSetChanged();
                    getData(lstVehicle); //cẩn thận
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //đổ dữ liệu lên spinner
        //Log.d("Check", lstVehicle.size() + "");
        adapter_vehicle = new ArrayAdapter(AddReminderActivity.this, android.R.layout.simple_spinner_item, lstVehicle);
        adapter_vehicle.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spVehicles.setAdapter(adapter_vehicle);
    }

    public void getData(List<vehicle> lstVehicle){
        //nếu mà kh có dl lập tức skip phần còn lại bên dưới
        if(bundle == null){
            return;
        }
        flag = true; //edit
        r = (remind) bundle.get("object_remind");

        etAction.setText(r.getTvAction());
        etDay.setText( r.getTvDay());
        etNote.setText(r.getTvNote());

        //bỏ qua cái "Chọn loại xe" -> đi từ 1
        for(int i = 1; i < lstVehicle.size(); i++){
            if(lstVehicle.get(i).getId().equals(r.getVehicle().getId())) { //search trong ds xem id nào = id tư bundle đưa vào
                spVehicles.setSelection(i);
                break;
            }

        }
    }

    public void setData(remind r ){
        //nếu đối tượng rỗng
        if(r == null)
            r = new remind(); //khởi tạo object

        r.setVehicle((vehicle) spVehicles.getSelectedItem());
        r.setTvAction(etAction.getText().toString());
        r.setTvDay(etDay.getText().toString());
        r.setTvNote(etNote.getText().toString());
        r.setIdUser(idUser);

        dbReference = mDatabase.getReference("Reminders");

        if(flag == false){
            //add
            String id = dbReference.push().getKey();
            r.setId(id);
            dbReference.child(r.getId()).setValue(r); //tạo value cho nhánh trên fb
            Toast.makeText(AddReminderActivity.this,"Thêm lời nhắc thành công!",Toast.LENGTH_SHORT).show();
        }
        else{
            //edit
            dbReference.child(r.getId()).setValue(r); //thay đổi giá trị theo getdata thông qua biến ins
            Intent i = new Intent(AddReminderActivity.this, RemindFragment.class);
            startService(i);
            //finish();
            Toast.makeText(AddReminderActivity.this,"Cập nhật thành công!",Toast.LENGTH_SHORT).show();
        }
    }

    //ktra có lỗi hay hk
    private String errorMsg(){
        //check lenght cua price < 10 chữ số
        if(spVehicles.getSelectedItem().toString().equals("Chọn xe"))
            return "Vui lòng chọn xe để hiển thị!";
        if(etAction.getText().toString().isEmpty())
            return "Vui lòng dien hành động";
        if(etDay.getText().toString().isEmpty())
            return "Vui lòng chọn ngày!";
        if(etNote.getText().toString().isEmpty())
            return "Vui lòng nhập ghi chú";
        return "";
    }
}