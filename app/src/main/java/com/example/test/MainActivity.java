package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN=5000;
    private Animation top,bottom,side;
    private TextView mtext,stext;
    private View line;
    private FirebaseUser muser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        mtext = findViewById(R.id.ptxt);
        stext = findViewById(R.id.stxt);
        line = findViewById(R.id.line);
        top = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottom = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        side = AnimationUtils.loadAnimation(this,R.anim.side_animation);
        mtext.setAnimation(top);
        stext.setAnimation(bottom);
        line.setAnimation(side);
        muser = FirebaseAuth.getInstance().getCurrentUser();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (muser !=null){
                    startActivity(new Intent(MainActivity.this,profile.class));
                }
                startActivity(new Intent(MainActivity.this,Login.class));
                finish();
            }
        },SPLASH_SCREEN);





    }
}