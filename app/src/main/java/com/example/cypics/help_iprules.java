package com.example.cypics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class help_iprules extends AppCompatActivity {
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_iprules);
        getSupportActionBar().setTitle("Help/IP Rules");
        button = findViewById(R.id.buttonHacking);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(help_iprules.this,Hacking.class);
                startActivity(intent);
            }
        });
        button = findViewById(R.id.buttonPhishing);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(help_iprules.this,Phishing.class);
                startActivity(intent);
            }
        });
        button = findViewById(R.id.buttonCyberstalking);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(help_iprules.this,Cyberstalking.class);
                startActivity(intent);
            }
        });
        button = findViewById(R.id.buttonIdentity_theft);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(help_iprules.this,Identity_theft.class);
                startActivity(intent);
            }
        });
        button = findViewById(R.id.buttonCyberbullying);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(help_iprules.this,Cyberbullying.class);
                startActivity(intent);
            }
        });
        button = findViewById(R.id.buttonOnline_defamation);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(help_iprules.this,Online_defamation.class);
                startActivity(intent);
            }
        });
        button = findViewById(R.id.buttonChild_pornography);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(help_iprules.this,Child_pornography.class);
                startActivity(intent);
            }
        });
        button = findViewById(R.id.buttonCyber_terrorism);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(help_iprules.this,Cyber_terrorism.class);
                startActivity(intent);
            }
        });
    }
}