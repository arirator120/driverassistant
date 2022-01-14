package com.example.dthdriverassistant.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dthdriverassistant.R;
import com.example.dthdriverassistant.model.user;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditInfoActivity extends AppCompatActivity {
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    user u;
    TextView tvEmail;
    EditText etEditName, etEditPhone;
    Button btnEditSave, btnEditClose;
    String idUser;

    Uri imageUri;
    StorageReference storageReference;
    ImageView imgEditAvatar;
    Button selectImagebtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        init();

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            idUser = acct.getId();
        }
        syncInfo(idUser);
        btnEditSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData(acct,u);
                finish();
                Toast.makeText(EditInfoActivity.this,"Cập nhật thành công!",Toast.LENGTH_SHORT).show();
            }
        });
        btnEditClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        selectImagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                selectImage();


            }
        });

    }
    public void init(){
        etEditName= findViewById(R.id.etEditName);
        etEditPhone= findViewById(R.id.etEditPhone);
        tvEmail= findViewById(R.id.tvEmail);
        btnEditSave= findViewById(R.id.btnEditSave);
        btnEditClose= findViewById(R.id.btnEditClose);
        selectImagebtn = findViewById(R.id.selectImagebtn);
        imgEditAvatar = findViewById(R.id.imgEditAvatar);
    }

    //get image
    private void selectImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,100);

    }

    public void syncInfo(String idUser){
        DatabaseReference dbReference = mDatabase.getReference("Users"); //phải tạo lại dbReference
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        user _u = dataSnapshot.getValue(user.class); //
                        if (_u != null){
                            if(_u.getId().equals(idUser)){
                                etEditName.setText(_u.getName());
                                tvEmail.setText(_u.getEmail());
                                etEditPhone.setText(_u.getPhone());
                                Glide.with(getApplicationContext())
                                        .load(String.valueOf(Uri.parse(_u.getAvatar())))
                                        .into(imgEditAvatar);
                                if(u==null){
                                    u= new user();
                                }
                                u.setAvatar(_u.getAvatar()); //lưu vào u để dùng
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    public void setData(GoogleSignInAccount acct ,user u){
        if(u==null){
            u= new user();
        }
        String email = acct.getEmail();
        Uri avatar = acct.getPhotoUrl();

        u.setEmail(email);
        u.setId(idUser);
        u.setName(etEditName.getText().toString());
        u.setPhone(etEditPhone.getText().toString());
        myRef = mDatabase.getReference("Users");
        setFirebaseImage(u);
    }

    private void setFirebaseImage(user u){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
        Date now = new Date();
        String fileName = formatter.format(now);

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReferenceProfilePic = firebaseStorage.getReference();
        storageReference = storageReferenceProfilePic.child("images/"+fileName + ".jpg");
        if(imageUri != null){
            storageReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference.getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        //khi file chưa exist trên fb
                                        public void onSuccess(Uri uri) {
                                            String uriString = uri.toString(); //chuyển uri -> string
                                            u.setAvatar(uriString); //thay đổi lại avatar
                                            myRef.child(u.getId()).setValue(u); //thay đổi giá trị theo getdata thông qua biến ins

                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Chỉ xảy ra khi ng dùng hk edit ảnh
                            Toast.makeText(EditInfoActivity.this,"Failed upload",Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            //khi người dùng hk mún up ảnh mới -> cập nhật thông tin
            myRef.child(u.getId()).setValue(u);
        }



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && data != null && data.getData() != null){
            //set image
            imageUri = data.getData();
            imgEditAvatar.setImageURI(imageUri); // đưa ảnh đã chọn lên giao diện


        }
    }

}