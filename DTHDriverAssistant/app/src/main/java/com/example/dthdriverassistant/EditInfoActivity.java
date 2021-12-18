package com.example.dthdriverassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditInfoActivity extends AppCompatActivity {
    EditText etEditName, etEditPhone;
    Button btnEditSave, btnEditClose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        init();
        //getData();
        btnEditSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setData(s);
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
}