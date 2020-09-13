package com.example.test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class profile extends AppCompatActivity {
    private ImageView image;
    private TextView pro_name, pro_phone, pro_mail;
    private Button logout;
    private FirebaseUser muser;
    private FirebaseAuth mAuth;
    private int TAKE_IMAGE_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        image = findViewById(R.id.image);
        //camera = findViewById(R.id.camera);
        pro_name = findViewById(R.id.pro_name);
        pro_phone = findViewById(R.id.pro_phone);
        pro_mail = findViewById(R.id.pro_mail);
        logout = findViewById(R.id.btn_logout);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(fuser.getPhotoUrl()!=null){
            Glide.with(this).load(fuser.getPhotoUrl()).into(image);
        }
        showUserData();
        Log.e("user id", FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(profile.this, Login.class));
                finish();
            }
        });

    }

    private void showUserData() {
        Intent intent = getIntent();
        String u_n = intent.getStringExtra("name");
        String u_p = intent.getStringExtra("phone");
        String u_m = intent.getStringExtra("email");
        pro_name.setText("Name : " + u_n);
        pro_mail.setText("E@il : " + u_m);
        pro_phone.setText("Phone No. : " + u_p);
    }


    public void showImage(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, TAKE_IMAGE_CODE);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_IMAGE_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    image.setImageBitmap(bitmap);
                    handleUpload(bitmap);
                case RESULT_CANCELED:
                    Toast.makeText(profile.this,"Photo does not Saves",Toast.LENGTH_SHORT);
                    break;
                default:
                    break;
            }

        }
    }

    private void handleUpload(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        final StorageReference reference = FirebaseStorage.getInstance().getReference()
                .child("profile image").child(user_id+".jpeg");
        reference.putBytes(baos.toByteArray()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                getDownloadurl(reference);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("profile","on Fail",e.getCause());

            }
        });
    }

    private void getDownloadurl(StorageReference reference) {
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.e("profile","on succes"+uri);
                setUserProfileUrl(uri);

            }
        });
    }

    private void setUserProfileUrl(Uri uri) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setPhotoUri(uri).build();
        user.updateProfile(request).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"Photo is updated",Toast.LENGTH_SHORT);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Image is failed to update!",Toast.LENGTH_SHORT);
            }
        });
    }
}