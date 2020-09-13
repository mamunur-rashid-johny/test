package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    private EditText inputemail,inputpassword;
    private TextView notmember,forgetpass;
    private Button btn_login;
    private ProgressBar sPbar;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inputemail = findViewById(R.id.login_email);
        inputpassword = findViewById(R.id.login_password);
        notmember = findViewById(R.id.link_signup);
        forgetpass = findViewById(R.id.link_forgetpass);
        btn_login = findViewById(R.id.btn_login);
        sPbar = findViewById(R.id.signinpbar);

        mAuth = FirebaseAuth.getInstance();



        notmember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,Rgister.class));
            }
        });
        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,ForgetPass.class));
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input_user = inputemail.getText().toString();
                String input_pass = inputpassword.getText().toString();
                if (input_user.length()==0){
                    inputemail.setError("Email Needed!");
                }else if(input_pass.length()==0){
                    inputpassword.setError("Password Needed!");
                }
                else{
                    sPbar.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(input_user, input_pass)
                            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    sPbar.setVisibility(View.INVISIBLE);
                                    if (task.isSuccessful()) {
                                        reference = FirebaseDatabase.getInstance().getReference().child("User").child(mAuth.getCurrentUser().getUid());
                                        reference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                String user_name = dataSnapshot.child("name").getValue().toString();
                                                String user_contact = dataSnapshot.child("phone").getValue().toString();
                                                String user_mail = dataSnapshot.child("email").getValue().toString();
                                                Intent intent = new Intent(Login.this,profile.class);
                                                intent.putExtra("name",user_name);
                                                intent.putExtra("phone",user_contact);
                                                intent.putExtra("email",user_mail);
                                                Toast.makeText(getApplicationContext(),"Successfully Login",Toast.LENGTH_SHORT);
                                                startActivity(intent);
                                            }



                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });


                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(getApplicationContext(),"Login fail.Try agaim.",Toast.LENGTH_SHORT);

                                    }


                                }
                            });

                }
            }
        });
    }
}