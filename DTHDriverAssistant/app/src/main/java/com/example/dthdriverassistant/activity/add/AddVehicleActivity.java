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
import com.example.dthdriverassistant.fragment.HistoryFuelFragment;
import com.example.dthdriverassistant.fragment.HomeFragment;
import com.example.dthdriverassistant.model.type;
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
import java.util.Map;

public class AddVehicleActivity extends AppCompatActivity {

    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    GoogleSignInClient mGoogleSignInClient;

    Button btnSave;
    Spinner spType;
    EditText etNameCar, etSign, etNameCompany, etDateBuy, etKmCurr, etDecs;

    List<type> lstType;
    ArrayAdapter<type> adapter_type;
    String idUser;
    Bundle bundle;
    vehicle v;
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");
        //set up back button
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                Intent i = new Intent(AddVehicleActivity.this, HistoryFuelFragment.class);
                startService(i);
                finish();
            }
        });

        init();
        bundle =  getIntent().getExtras();

        etDateBuy.setText(getDateTime());

        etDateBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDay();
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
            //Log.d("id", idUser);
        }


        inItMyType();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(errorMsg() == ""){
                    setData(v);
                    finish();
                }else{
                    Toast.makeText(AddVehicleActivity.this, errorMsg(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void init(){
        etNameCar = findViewById(R.id.etNameCar);
        etSign = findViewById(R.id.etSign);
        etNameCompany = findViewById(R.id.etNameCompany);
        etDateBuy = findViewById(R.id.etDateBuy);
        etKmCurr = findViewById(R.id.etKmCurr);
        etDecs = findViewById(R.id.etDecs);
        spType = findViewById(R.id.spType);
        btnSave = findViewById(R.id.btnSave);
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        Date date = new Date();
        return dateFormat.format(date);
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
                etDateBuy.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, year, month, day); // b???t bu???c ph???i l???n l?????t l?? year, month,day ????? l???y ????ng ng??y hi???n t???i
        datePickerDialog.show();
    }

    public void inItMyType(){
        DatabaseReference dbReference = mDatabase.getReference("Types");
        lstType = new ArrayList<>();
        dbReference.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                  if (lstType != null || !lstType.isEmpty())
                      lstType.clear();

                  type defaultType = new type("type1", "Ch???n lo???i xe"); //label
                  lstType.add(0, defaultType);

                  if (snapshot.exists()) {
                      for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                          type t = dataSnapshot.getValue(type.class);
                          lstType.add(t);
                      }

                      adapter_type.notifyDataSetChanged();
                      getData(lstType); //c???n th???n
                  }


              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {

              }
        });

        //????? d??? li???u l??n spinner
        adapter_type = new ArrayAdapter(AddVehicleActivity.this, android.R.layout.simple_spinner_item, lstType);
        adapter_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spType.setAdapter(adapter_type);
    }

    public void getData(List<type> lstType){
        //n???u m?? kh c?? dl l???p t???c skip ph???n c??n l???i b??n d?????i
        //khi bundle c?? d??? li???u t???c l?? t??? viewPerson g???i qua-> flag= true
        if(bundle == null){
            return;
        }
        flag = true; //edit
        v = (vehicle) bundle.get("object_vehicle");

        etNameCar.setText(v.getName());
        etSign.setText(v.getSign());
        etNameCompany.setText(v.getCompany());
        etDateBuy.setText(v.getDateBuy());
        etKmCurr.setText(v.getCurrKm() + "");
        etDecs.setText(v.getDesc());

        //b??? qua c??i "Ch???n lo???i xe" -> ??i t??? 1
        for(int i = 1; i < lstType.size(); i++){
            if(lstType.get(i).getId().equals(v.getType().getId())) { //search trong ds xem name n??o = name t?? bundle ????a v??o
                spType.setSelection(i);
                break;
            }

        }
    }

    public void setData(vehicle v){
        //n???u ?????i t?????ng r???ng
        if(v==null)
            v = new vehicle(); //kh???i t???o object
        v.setType((type) spType.getSelectedItem()); //ch??? l???y t??n
        v.setName(etNameCar.getText().toString());
        v.setSign(etSign.getText().toString());
        v.setCompany(etNameCompany.getText().toString());
        v.setDateBuy(etDateBuy.getText().toString());
        v.setCurrKm(Double.parseDouble(etKmCurr.getText().toString()));
        v.setDesc(etDecs.getText().toString());
        v.setIdUser(idUser);

        DatabaseReference dbReference = mDatabase.getReference("UsersVehicle");

        if(flag == false){
            //add
            String id = dbReference.push().getKey();
            v.setId(id);
            dbReference.child(v.getId()).setValue(v); //t???o value cho nh??nh tr??n fb
            Toast.makeText(AddVehicleActivity.this,"Th??m d??? li???u ho??n t???t!!",Toast.LENGTH_SHORT).show();
        }
        else{
            //edit
            dbReference.child(v.getId()).setValue(v); //thay ?????i gi?? tr??? theo getdata th??ng qua bi???n ins
            Intent i = new Intent(AddVehicleActivity.this, HomeFragment.class);
            startService(i);
            Toast.makeText(AddVehicleActivity.this,"C???p nh???t ho??n t???t!!",Toast.LENGTH_SHORT).show();
        }
        syncRefuel(v);
        syncRepairParts(v);
        syncChangeOil(v);

    }

    public void syncRefuel(vehicle v){
        //thay ?????i ls ????? x??ng
        DatabaseReference dbReference = mDatabase.getReference("Refuel"); //ph???i t???o l???i dbReference
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        DatabaseReference dbReference1 = dbReference.child(dataSnapshot.getKey()).child("vehicle");
                        //c???n ki???m tra id Vehicle ????? nh???n bi???t v?? n?? l?? kh??a ch??nh
                        dbReference1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                                    String id = map.get("id").toString();
                                    if(id.equals(v.getId()))
                                        dbReference1.setValue(v);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                    adapter_type.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    public void syncChangeOil(vehicle v){
        //thay ?????i ls thay nh???t
        DatabaseReference dbReference = mDatabase.getReference("ChangeOil"); //ph???i t???o l???i dbReference
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        DatabaseReference dbReference1 = dbReference.child(dataSnapshot.getKey()).child("vehicle");
                        dbReference1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                                    String id = map.get("id").toString();
                                    if(id.equals(v.getId()))
                                        dbReference1.setValue(v);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                    adapter_type.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void syncRepairParts(vehicle v){
        //thay ?????i ls thay lk
        DatabaseReference dbReference = mDatabase.getReference("RepairParts");
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        DatabaseReference dbReference1 = dbReference.child(dataSnapshot.getKey()).child("vehicle");
                        dbReference1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                                    String id = map.get("id").toString();
                                    if(id.equals(v.getId()))
                                        dbReference1.setValue(v);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                    adapter_type.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //ktra c?? l???i hay hk
    private String errorMsg(){
        //check lenght cua price < 10 ch??? s???
        if(spType.getSelectedItem().toString().equals("Ch???n lo???i xe"))
            return "Vui l??ng ch???n ki???u xe ????? hi???n th???!";
        if(etNameCar.getText().toString().isEmpty())
            return "Vui l??ng nh???p t??n xe!";
        if(etSign.getText().toString().isEmpty())
            return "Vui l??ng nh???p bi???n s???!";
        if(etNameCompany.getText().toString().isEmpty())
            return "Vui l??ng nh???p h??ng xe!";
        if(etDateBuy.getText().toString().isEmpty())
            return "Vui l??ng nh???p ng??y mua xe!";
        if(etKmCurr.getText().toString().isEmpty())
            return "Vui l??ng nh???p s??? km hi???n t???i!";
        if(etDecs.getText().toString().isEmpty())
            return "Vui l??ng nh???p ghi ch??!";
        return "";
    }
}