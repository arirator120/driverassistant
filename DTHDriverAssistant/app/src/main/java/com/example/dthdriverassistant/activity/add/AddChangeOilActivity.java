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
import com.example.dthdriverassistant.model.oil;
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

public class AddChangeOilActivity extends AppCompatActivity {
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference dbReference;
    GoogleSignInClient mGoogleSignInClient;

    Button btnSave;
    Spinner spVehicles;
    EditText etCalFilled, etTimeFilled, etPrice, etType, etKmChangedOil, etDecs;

    List<vehicle> lstVehicle;
    ArrayAdapter<vehicle> adapter_vehicle;
    String idUser;
    Bundle bundle;
    oil o;

    boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_change_oil);

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
                Intent i = new Intent(AddChangeOilActivity.this, HistoryChangeOilFragment.class);
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

        //l??u dl
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(errorMsg() == ""){
                    setData(o);
                    finish();
                }else{
                    Toast.makeText(AddChangeOilActivity.this, errorMsg(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void init(){
        etCalFilled = findViewById(R.id.etCalFilled);
        etTimeFilled = findViewById(R.id.etTimeFilled);
        etPrice = findViewById(R.id.etPrice);
        etType = findViewById(R.id.etType);
        etKmChangedOil = findViewById(R.id.etKmChangedOil);
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
        }, year, month, day); // b???t bu???c ph???i l???n l?????t l?? year, month,day ????? l???y ????ng ng??y hi???n t???i
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
                calendar.set(0,0,0,i,i1); // t???o gi??? v?? ph??t
                etTimeFilled.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }

    // ?????ng b??? d??? li???u th??ng qua c??i n??y
    public void inItMyVehicles(){
        dbReference = mDatabase.getReference("UsersVehicle");
        lstVehicle = new ArrayList<>();
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(lstVehicle != null || !lstVehicle.isEmpty())
                    lstVehicle.clear();
                vehicle defaultVehicle = new vehicle("","Ch???n xe","",null,"","",0.0,"",""); //label
                lstVehicle.add(0, defaultVehicle);
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        //ki???m tra kh??a ngo???i
                        if(dataSnapshot.child("idUser").getValue().equals(idUser)){
                            vehicle v = dataSnapshot.getValue(vehicle.class);
                            lstVehicle.add(v);
                        }

                    }
                    adapter_vehicle.notifyDataSetChanged();
                    getData(lstVehicle);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //????? d??? li???u l??n spinner
        adapter_vehicle = new ArrayAdapter(AddChangeOilActivity.this, android.R.layout.simple_spinner_item, lstVehicle);
        adapter_vehicle.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spVehicles.setAdapter(adapter_vehicle);
    }

    public void getData(List<vehicle> lstVehicle){
        //n???u m?? kh c?? dl l???p t???c skip ph???n c??n l???i b??n d?????i
        if(bundle == null){
            return;
        }
        flag = true; //edit
        o = (oil) bundle.get("object_change_oil");

        etCalFilled.setText(o.getCalFilled());
        etTimeFilled.setText(o.getTimeFilled());
        etPrice.setText(o.getPrice() + "");
        etType.setText(o.getType());
        etKmChangedOil.setText(o.getKmChangedOil() + "");
        etDecs.setText(o.getDecs());

        //b??? qua c??i "Ch???n lo???i xe" -> ??i t??? 1
        for(int i = 1; i < lstVehicle.size(); i++){
            if(lstVehicle.get(i).getId().equals(o.getVehicle().getId())) { //search trong ds xem id n??o = id t?? bundle ????a v??o
                spVehicles.setSelection(i);
                break;
            }

        }
    }

    public void setData(oil o){
        //n???u ?????i t?????ng r???ng
        if(o==null)
            o = new oil(); //kh???i t???o object

        o.setVehicle((vehicle) spVehicles.getSelectedItem());
        o.setPrice(Integer.parseInt(etPrice.getText().toString()));
        o.setKmChangedOil(Double.parseDouble(etKmChangedOil.getText().toString()));
        o.setTimeFilled(etTimeFilled.getText().toString());
        o.setCalFilled(etCalFilled.getText().toString());
        o.setType(etType.getText().toString());
        o.setDecs(etDecs.getText().toString());
        o.setIdUser(idUser);

        dbReference = mDatabase.getReference("ChangeOil");

        if(flag == false){
            //add
            String id = dbReference.push().getKey();
            o.setId(id);
            dbReference.child(o.getId()).setValue(o); //t???o value cho nh??nh tr??n fb
            Toast.makeText(AddChangeOilActivity.this,"Th??m d??? li???u ho??n t???t!!",Toast.LENGTH_SHORT).show();
        }
        else{
            //edit
            dbReference.child(o.getId()).setValue(o); //thay ?????i gi?? tr??? theo getdata th??ng qua bi???n ins
            Intent i = new Intent(AddChangeOilActivity.this, HistoryChangeOilFragment.class);

            startService(i);
            //finish();
            Toast.makeText(AddChangeOilActivity.this,"C???p nh???t ho??n t???t!!",Toast.LENGTH_SHORT).show();
        }


    }

    //ktra c?? l???i hay hk
    private String errorMsg(){
        //check lenght cua price < 10 ch??? s???
        if(spVehicles.getSelectedItem().toString().equals("Ch???n xe"))
            return "Vui l??ng ch???n xe ????? hi???n th???!";
        if(etCalFilled.getText().toString().isEmpty())
            return "Vui l??ng ch???n ng??y!";
        if(etTimeFilled.getText().toString().isEmpty())
            return "Vui l??ng ch???n gi???!";
        if(etType.getText().toString().isEmpty())
            return "Vui l??ng nh???p lo???i nh???t!";
        if(etPrice.getText().toString().isEmpty())
            return "Vui l??ng nh???p ti???n nh???t!";
        if(etKmChangedOil.getText().toString().isEmpty())
            return "Vui l??ng nh???p s??? km l??c thay nh???t!";
        return "";
    }


}