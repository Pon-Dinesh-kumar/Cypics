package com.example.cypics;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;


public class aftersignin extends AppCompatActivity {

    Button button,signout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aftersignin);
        getSupportActionBar().hide();
        signout=findViewById(R.id.logoff);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(aftersignin.this, MainActivity.class);
                startActivity(intent);


            }
        });

        button = findViewById(R.id.register);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(aftersignin.this, register_complaint.class);
                startActivity(intent);
            }
        });

        button = findViewById(R.id.track);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(aftersignin.this, track.class);
                startActivity(intent);
            }
        });
        button = findViewById(R.id.help);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(aftersignin.this, help_iprules.class);
                startActivity(intent);
            }
        });


        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(aftersignin.this, chat_screen_kotlin.class);
                startActivity(intent);
            }
        });
    }
}
