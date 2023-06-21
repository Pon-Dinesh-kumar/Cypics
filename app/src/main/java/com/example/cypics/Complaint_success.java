package com.example.cypics;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Complaint_success extends AppCompatActivity {
    private Button b1;
    private TextView caseNumberTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_success);
        caseNumberTextView = findViewById(R.id.case_number_textview);
        // Get the case number from the previous activity
        Intent intent = getIntent();
        String caseNumber = intent.getStringExtra("caseNumber");
        // Set the case number as the text of the TextView
        caseNumberTextView.setText("Your case number is " + caseNumber);
        b1 = findViewById(R.id.track);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Complaint_success.this, track.class);
                intent.putExtra("caseNumber", caseNumber);
                startActivity(intent);
            }

        }
        );
    }
}