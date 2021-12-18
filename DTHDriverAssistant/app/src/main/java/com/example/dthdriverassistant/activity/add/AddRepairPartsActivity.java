package com.example.dthdriverassistant.activity.add;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.dthdriverassistant.R;
import com.example.dthdriverassistant.fragment.HistoryChangeOilFragment;
import com.example.dthdriverassistant.fragment.HistoryRepairPartsFragment;
import com.example.dthdriverassistant.model.part;
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
import java.util.Date;
import java.util.List;

public class AddRepairPartsActivity extends AppCompatActivity {

    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference dbReference;
    GoogleSignInClient mGoogleSignInClient;

    Button btnSave;
    Spinner spVehicles;
    EditText etCalFilled, etTimeFilled, etPrice, etNamePart, etAmount, etDecs;

    List<vehicle> lstVehicle;
    ArrayAdapter<vehicle> adapter_vehicle;
    String idUser;
    Bundle bundle;
    part p;

    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_repair_parts);

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
                Intent i = new Intent(AddRepairPartsActivity.this, HistoryChangeOilFragment.class);
                startService(i);
                finish();
            }
        });

        init();
        bundle =  getIntent().getExtras();

        //Get date and time now
        etCalFilled.setText(getDateTime());
        etTimeFilled.setText(getTime());

        etCalFilled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDay();
            }
        });

        etTimeFilled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickTime();
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
        //spinner
        inItMyVehicles();

        //lưu dl
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(errorMsg() == ""){
                    setData(p);
                    finish();
                }else{
                    Toast.makeText(AddRepairPartsActivity.this, errorMsg(), Toast.LENGTH_LONG).show();
                }
            }
        });



    }


    private void init(){
        etCalFilled = findViewById(R.id.etCalFilled);
        etTimeFilled = findViewById(R.id.etTimeFilled);
        etPrice = findViewById(R.id.etPrice);
        etNamePart = findViewById(R.id.etNamePart);
        etAmount = findViewById(R.id.etAmount);
        etDecs = findViewById(R.id.etDecs);
        spVehicles = findViewById(R.id.spVehicles);
        btnSave = findViewById(R.id.btnSave);
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        return simpleDateFormat.format(date);
    }

    private void pickDay(){
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog;
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(i,i1,i2);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
                etCalFilled.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, year, month, day); // bắt buộc phải lần lượt là year, month,day để lấy đúng ngày hiện tại
        datePickerDialog.show();
    }

    private void pickTime(){
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                calendar.set(0,0,0,i,i1); // tạo giờ và phút
                etTimeFilled.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }

    // đồng bộ dữ liệu thông qua cái này
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
        adapter_vehicle = new ArrayAdapter(AddRepairPartsActivity.this, android.R.layout.simple_spinner_item, lstVehicle);
        adapter_vehicle.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spVehicles.setAdapter(adapter_vehicle);
    }

    public void getData(List<vehicle> lstVehicle){
        //nếu mà kh có dl lập tức skip phần còn lại bên dưới
        if(bundle == null){
            return;
        }
        flag = true; //edit
        p = (part) bundle.get("object_repair_parts");

        etCalFilled.setText(p.getCalFilled());
        etTimeFilled.setText(p.getTimeFilled());
        etPrice.setText(p.getPrice() + "");
        etNamePart.setText(p.getNamePart());
        etAmount.setText(p.getAmount() + "");
        etDecs.setText(p.getDecs());

        //bỏ qua cái "Chọn loại xe" -> đi từ 1
        for(int i = 1; i < lstVehicle.size(); i++){
            if(lstVehicle.get(i).getId().equals(p.getVehicle().getId())) { //search trong ds xem id nào = id tư bundle đưa vào
                spVehicles.setSelection(i);
                break;
            }

        }
    }

    public void setData(part p){
        //nếu đối tượng rỗng
        if(p==null)
            p = new part(); //khởi tạo object

        p.setVehicle((vehicle) spVehicles.getSelectedItem());
        p.setPrice(Integer.parseInt(etPrice.getText().toString()));
        p.setAmount(Integer.parseInt(etAmount.getText().toString()));
        p.setTimeFilled(etTimeFilled.getText().toString());
        p.setCalFilled(etCalFilled.getText().toString());
        p.setNamePart(etNamePart.getText().toString());
        p.setDecs(etDecs.getText().toString());
        p.setIdUser(idUser);

        dbReference = mDatabase.getReference("RepairParts");

        if(flag == false){
            //add
            String id = dbReference.push().getKey();
            p.setId(id);
            dbReference.child(p.getId()).setValue(p); //tạo value cho nhánh trên fb
            Toast.makeText(AddRepairPartsActivity.this,"Thêm dữ liệu hoàn tất!!",Toast.LENGTH_SHORT).show();
        }
        else{
            //edit
            dbReference.child(p.getId()).setValue(p); //thay đổi giá trị theo getdata thông qua biến ins
            Intent i = new Intent(AddRepairPartsActivity.this, HistoryRepairPartsFragment.class);

            startService(i);
            //finish();
            Toast.makeText(AddRepairPartsActivity.this,"Cập nhật hoàn tất!!",Toast.LENGTH_SHORT).show();
        }


    }

    //ktra có lỗi hay hk
    private String errorMsg(){
        //check lenght cua price < 10 chữ số
        if(spVehicles.getSelectedItem().toString().equals("Chọn loại xe"))
            return "Vui lòng chọn xe để hiển thị!";
        if(etCalFilled.getText().toString().isEmpty())
            return "Vui lòng chọn ngày!";
        if(etTimeFilled.getText().toString().isEmpty())
            return "Vui lòng chọn giờ!";
        if(etPrice.getText().toString().isEmpty())
            return "Vui lòng nhập tiền nhớt!";
        if(etNamePart.getText().toString().isEmpty())
            return "Vui lòng nhập tên linh kiện!";
        if(etAmount.getText().toString().isEmpty())
            return "Vui lòng nhập số lượng linh kiện đã thay!";
        return "";
    }
}