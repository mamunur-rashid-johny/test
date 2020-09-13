package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Rgister extends AppCompatActivity {
    private EditText name,contact,email,password;
    private Button btn_reg;
    private TextView text_log;
    private ProgressBar pbar;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rgister);
        name = findViewById(R.id.input_name);
        contact = findViewById(R.id.input_phone);
        email = findViewById(R.id.input_email);
        password = findViewById(R.id.input_password);
        btn_reg = findViewById(R.id.btn_sgnup);
        text_log = findViewById(R.id.link_signin);
        pbar = findViewById(R.id.progress);

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("User");


        text_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Rgister.this,Login.class));

            }
        });

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String user = name.getText().toString().trim();
                final String phone = contact.getText().toString().trim();
                final String mail = email.getText().toString().trim();
                final String pass = password.getText().toString();

                if(user.length()==0){
                    name.setError("Please Enter Your Name!");
                }
                else if(phone.length()==0){
                    contact.setError("Please Enter Your Phone Number!");
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
                      email.setError("Please Enter Valid Email Address!");
                }
                else if (mail.length()==0){
                    email.setError("Please Enter Your Email!");
                }
                else if (pass.length()==0){
                    password.setError("Please Enter Your Password!");
                }
                else if (pass.length()<5){
                    password.setError("Please Enter More than 5 Character!");
                }
                else {
                    pbar.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(mail, pass)
                            .addOnCompleteListener(Rgister.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    pbar.setVisibility(View.INVISIBLE);
                                    if (task.isSuccessful()) {
                                        User userobj = new User(user, phone, mail, pass);
                                        reference.child(mAuth.getCurrentUser().getUid()).setValue(userobj).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(Rgister.this, "Registration Complete", Toast.LENGTH_SHORT);
                                                startActivity(new Intent(Rgister.this, Login.class));

                                            }
                                        });

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(getApplicationContext(), "Registration Fail. Try again!", Toast.LENGTH_SHORT);

                                    }
                                }
                            });
                }
            }
        });
    }
}