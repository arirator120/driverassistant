package com.example.dthdriverassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dthdriverassistant.model.user;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditInfoActivity extends AppCompatActivity {
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    user u;
    EditText etEditName, etEditPhone;
    Button btnEditSave, btnEditClose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        init();
        getData();
        btnEditSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData(u);
                finish();
            }
        });
        btnEditClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void init(){
        etEditName= findViewById(R.id.etEditName);
        etEditPhone= findViewById(R.id.etEditPhone);
        btnEditSave= findViewById(R.id.btnEditSave);
        btnEditClose= findViewById(R.id.btnEditClose);
    }

    public void getData(){
        Bundle bundle= getIntent().getExtras();
        u = (user) bundle.get("Users");
        etEditName.setText(u.getName());
        //etEditPhone.setText(u.get);

    }
    public void setData(user u){
        u.setName(etEditName.getText().toString());
        myRef = mDatabase.getReference("Users");

        myRef.child(u.getId()).setValue(u);
        Intent i= new Intent(this, EditInfoActivity.class);
        Bundle bundle= new Bundle();
        bundle.putSerializable("Users",u);
        i.putExtras(bundle);
        startActivity(i);
        Toast.makeText(EditInfoActivity.this,"Đã cập nhật thông tin khách hàng "+u.getName(),Toast.LENGTH_LONG).show();
    }
//    public void getData(){
//        Bundle bundle= getIntent().getExtras();
//        if(bundle == null){
//            return; //flag=false -> add Student
//        }
//        flag=true;
//        s= (Student) bundle.get("objectStudent");
//        etAddName.setText(s.getFullName());
//        etAddStudentId.setText(s.getStudentId());
//        etAddClass.setText(s.getStdClass());
//        etAddPhone.setText(s.getPhone());
//        if(s.isGender()){
//            radioMale.isChecked();
//            rgAddGene.check(radioMale.getId());
//        }
//        else{
//            radioFemale.isChecked();
//            rgAddGene.check(radioFemale.getId());
//        }
//        for(int i=0; i<majorList.size(); i++){
//            if(majorList.get(i).getName().equals(s.getMajor().getName())){
//                spMajor.setSelection(i);
//                break;
//            }
//        }
//    }
//    public void setData(Student s){
//        if(s==null){
//            s= new Student();
//        }
//        s.setFullName(etAddName.getText().toString());
//        s.setPhone(etAddPhone.getText().toString());
//        s.setStudentId(etAddStudentId.getText().toString());
//        s.setStdClass(etAddClass.getText().toString());
//        if(radioFemale.isChecked()){
//            s.setGender(false);
//            s.setPicture(R.drawable.nu);
//        }
//        else {
//            s.setGender(true);
//            s.setPicture(R.drawable.nam);
//        }
//
//        s.setMajor((Major) spMajor.getSelectedItem());
//        myRef= mDatabase.getReference("Students");//tham chieu den node stds
//        if(flag==false){ // add std
//            String id= myRef.push().getKey();
//            s.setId(id);
//            myRef.child(id).setValue(s);
//            Toast.makeText(DetailStudentActivity.this,"Đã Add sinh viên "+s.getFullName(),Toast.LENGTH_LONG).show();
//        }
//        else{//edit
//            myRef.child(s.getId()).setValue(s);
//            Intent i= new Intent(this, ViewStudentActivity.class);
//            Bundle bundle= new Bundle();
//            bundle.putSerializable("objectStudent",s);
//            i.putExtras(bundle);
//            startActivity(i);
//            Toast.makeText(DetailStudentActivity.this,"Đã Edit sinh viên "+s.getFullName(),Toast.LENGTH_LONG).show();
//        }
//
//    }
    //private void onClickUpdate(Student s){
    //    Intent i= new Intent(this, DetailStudentActivity.class);
    //    Bundle bundle= new Bundle();
    //    bundle.putSerializable("objectStudent",s);
    //    i.putExtras(bundle);
    //    startActivity(i);
    //}
//  private void onClickDetail(Student s) {
//    Intent i = new Intent(mContext, ViewStudentActivity.class);
//    Bundle bundle = new Bundle();
//    bundle.putSerializable("objectStudent", s);
//    i.putExtras(bundle);
//    mContext.startActivity(i);
//  }
}