package com.example.cypics;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.text.TextWatcher;
import android.text.Editable;
import android.widget.Toast;

import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class register_complaint extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText dateformat;
    EditText e1,e2,e3,e4,e5;
    EditText editText;
    Button b1;
    Button b2;
    Spinner s1;
    String[] ss1 = {"Ministry of Home Affairs", "Indian Cybercrime Coordination Center"};
    ArrayAdapter aa;
    int year, month, day;
    TextView predictionTextView;
    TextView prediction1TextView;
    TextView prediction1ResultTextView;
    TextView prediction2TextView;
    TextView prediction2ResultTextView;
    TextView ensemblePredictionTextView;
    TextView ensemblePredictionResultTextView;

    private TextView serverStatusTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_complaint);
        getSupportActionBar().hide();

        dateformat = findViewById(R.id.dateformatID);
        editText = findViewById(R.id.editTextpredict);
        b2 = findViewById(R.id.onpresspredict);
        s1 = findViewById(R.id.spinner);
        b1 = findViewById(R.id.button);
        e1 = findViewById(R.id.editTextTextPersonName);
        e2 = findViewById(R.id.editTextTextPersonName2);
        e3 = findViewById(R.id.editTextTextPersonName3);
        e4 = findViewById(R.id.editTextTextPersonName4);
        e5 = findViewById(R.id.editTextTextMultiLine);
        serverStatusTextView = findViewById(R.id.serverStatusTextView);

        s1 = findViewById(R.id.spinner);

        s1.setOnItemSelectedListener(this);
        aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item,ss1);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(aa);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a1 = e1.getText().toString();
                String b1 = e2.getText().toString();
                String c1 = e3.getText().toString();
                String d1 = e4.getText().toString();
                String f1 = e5.getText().toString();

                if(a1.equals("") || b1.equals("") || c1.equals("") || d1.equals("") || f1.equals("")){
                    Toast.makeText(register_complaint.this, "Please fill all the details.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(register_complaint.this, "Complaint sent successfully.", Toast.LENGTH_SHORT).show();
                    e1.setText("");
                    e2.setText("");
                    e3.setText("");
                    e4.setText("");
                    e5.setText("");
                }
            }
        });


        predictionTextView = findViewById(R.id.predictionTextView);
        prediction1TextView = findViewById(R.id.prediction1TextView);
        prediction1ResultTextView = findViewById(R.id.prediction1ResultTextView);
        prediction2TextView = findViewById(R.id.prediction2TextView);
        prediction2ResultTextView = findViewById(R.id.prediction2ResultTextView);
        ensemblePredictionTextView = findViewById(R.id.ensemblePredictionTextView);
        ensemblePredictionResultTextView = findViewById(R.id.ensemblePredictionResultTextView);
        int initialTimeoutMs = 5000;
        int maxNumRetries = 3;
        float backoffMultiplier = 1.5f;

        if (prediction1ResultTextView.getText().toString().equals("N/A")) {
            predictionTextView.setVisibility(View.GONE);
            prediction1TextView.setVisibility(View.GONE);
            prediction1ResultTextView.setVisibility(View.GONE);
            prediction2TextView.setVisibility(View.GONE);
            prediction2ResultTextView.setVisibility(View.GONE);
            ensemblePredictionTextView.setVisibility(View.GONE);
            ensemblePredictionResultTextView.setVisibility(View.GONE);
        } else {
            predictionTextView.setVisibility(View.VISIBLE);
            prediction1TextView.setVisibility(View.VISIBLE);
            prediction1ResultTextView.setVisibility(View.VISIBLE);
            prediction2TextView.setVisibility(View.VISIBLE);
            prediction2ResultTextView.setVisibility(View.VISIBLE);
            ensemblePredictionTextView.setVisibility(View.VISIBLE);
            ensemblePredictionResultTextView.setVisibility(View.VISIBLE);
        }
        //status checker
        // Make a request to the server to check its status
        String url = "https://flask-ml-model-epics.azurewebsites.net/predict";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // The server is ready, set the status to green
                try {
                    if (response != null && response.getInt("status") == 405) {
                        serverStatusTextView.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                    } else {
                        serverStatusTextView.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // There was an error connecting to the server, set the status to red
                serverStatusTextView.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
            }
        });

// Add the request to the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
        // Set an OnClickListener for the button
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the user input from the EditText view
                String userInput = editText.getText().toString();
                // Create a JSON object with the message parameter
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("message", userInput);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                //create a request with volley
                String url = "https://flask-ml-model-epics.azurewebsites.net/predict";
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Update the UI with the response
                        try {
                            String prediction1 = response.getString("prediction_1");
                            String prediction2 = response.getString("prediction_2");
                            String prediction_ensemble = response.getString("prediction_ensemble");
                            prediction1ResultTextView.setText(prediction1);
                            prediction2ResultTextView.setText(prediction2);
                            ensemblePredictionResultTextView.setText(prediction_ensemble);

                            if (prediction1ResultTextView.getText().toString().equals("N/A")) {
                                predictionTextView.setVisibility(View.GONE);
                                prediction1TextView.setVisibility(View.GONE);
                                prediction1ResultTextView.setVisibility(View.GONE);
                                prediction2TextView.setVisibility(View.GONE);
                                prediction2ResultTextView.setVisibility(View.GONE);
                                ensemblePredictionTextView.setVisibility(View.GONE);
                                ensemblePredictionResultTextView.setVisibility(View.GONE);
                            } else {
                                predictionTextView.setVisibility(View.VISIBLE);
                                prediction1TextView.setVisibility(View.VISIBLE);
                                prediction1ResultTextView.setVisibility(View.VISIBLE);
                                prediction2TextView.setVisibility(View.VISIBLE);
                                prediction2ResultTextView.setVisibility(View.VISIBLE);
                                ensemblePredictionTextView.setVisibility(View.VISIBLE);
                                ensemblePredictionResultTextView.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                        new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
                request.setRetryPolicy(new DefaultRetryPolicy(
                        initialTimeoutMs,
                        maxNumRetries,
                        backoffMultiplier
                ));

                // Add the request to the Volley request queue
                Volley.newRequestQueue(register_complaint.this).add(request);

                // Disable the button initially
                b2.setEnabled(false);

                // Add text change listener to the EditText
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        // Check if EditText is empty or not
                        if (editText.getText().toString().trim().isEmpty()) {
                            // Disable the Button if EditText is empty
                            b2.setEnabled(false);
                        } else {
                            // Enable the Button if EditText is not empty
                            b2.setEnabled(true);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                    }
                });

            }
        });
        dateformat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting
                // the instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        register_complaint.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our edit text.
                                dateformat.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
