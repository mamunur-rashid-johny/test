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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPass extends AppCompatActivity {
    private Button btn;
    private EditText edx;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);
        btn = findViewById(R.id.btn_forget);
        edx = findViewById(R.id.forgotemail);
        tv = findViewById(R.id.btl);
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String u_mail = edx.getText().toString().trim();
                if (u_mail.length()==0){
                    edx.setError("Please Enter Mail");
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(u_mail).matches()){
                    edx.setError("please Enter Valid Mail");
                }
                else {
                    mAuth.sendPasswordResetEmail(u_mail)
                            .addOnCompleteListener(ForgetPass.this, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ForgetPass.this, "Check email to reset your password!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ForgetPass.this, "Fail to send reset password email!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgetPass.this,Login.class));
                finish();
            }
        });
    }
}