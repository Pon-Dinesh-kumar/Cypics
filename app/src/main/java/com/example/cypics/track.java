package com.example.cypics;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class track extends AppCompatActivity {
    Spinner spinner;
    ArrayList<String> arrType = new ArrayList<>();
    EditText dateformat;
    TextView statusshow;
    EditText Trackid;
    int year,month,day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        getSupportActionBar().hide();
        spinner = findViewById(R.id.spinner);
        Trackid =findViewById(R.id.editText_track_id);
        Intent intent = getIntent();
        String caseNumber = intent.getStringExtra("caseNumber");
        Trackid.setText(caseNumber);



        arrType.add("Andhra Pradesh");
        arrType.add("Arunachal Pradesh");
        arrType.add("Assam");
        arrType.add("Bihar");
        arrType.add("Chhattisgarh");
        arrType.add("Goa");
        arrType.add("Gujarat");
        arrType.add("Haryana");
        arrType.add("Himachal Pradesh");
        arrType.add("Jharkhand");
        arrType.add("Karnataka");
        arrType.add("Kerala");
        arrType.add("Madhya Pradesh");
        arrType.add("Maharashtra");
        arrType.add("Manipur");
        arrType.add("Meghalaya");
        arrType.add("Mizoram");
        arrType.add("Nagaland");
        arrType.add("Odisha");
        arrType.add("Punjab");
        arrType.add("Rajasthan");
        arrType.add("Sikkim");
        arrType.add("Tamil Nadu");
        arrType.add("Telangana");
        arrType.add("Tripura");
        arrType.add("Uttar Pradesh");
        arrType.add("Uttarakhand");
        arrType.add("West Bengal");
        arrType.add("Andaman and Nicobar Islands");
        arrType.add("Chandigarh");
        arrType.add("Dadra and Nagar Haveli and Daman and Diu");
        arrType.add("Delhi");
        arrType.add("Jammu and Kashmir");
        arrType.add("Ladakh");
        arrType.add("Lakshadweep ");
        arrType.add("Puducherry");


        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, arrType);
        spinner.setAdapter(spinnerAdapter);


        statusshow=findViewById(R.id.textView9);

        dateformat=findViewById(R.id.dateformatID);
        Calendar calendar=Calendar.getInstance();
        dateformat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year=calendar.get(Calendar.YEAR);
                month=calendar.get(Calendar.MONTH);
                day=calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(track.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                        //Showing the picked value in the textView
                        dateformat.setText(String.valueOf(year)+ "."+String.valueOf(month+1)+ "."+String.valueOf(day));

                    }
                }, 2023, 01, 20);
                datePickerDialog.show();
            }
        });
    }
}