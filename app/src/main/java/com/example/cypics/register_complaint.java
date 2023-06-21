package com.example.cypics;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class register_complaint extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText e1,e2,e3,e4,e5,e6;
    Button b1;
    Button b2;
    Spinner categorySpinner,subCategoryspinner;
    int year, month, day;
    TextView predictionTextView;
    TextView prediction1TextView;
    TextView prediction1ResultTextView;
    TextView prediction2TextView;
    TextView prediction2ResultTextView;
    TextView ensemblePredictionTextView;
    TextView ensemblePredictionResultTextView;
    private MyProgressDialog mProgressDialog;


    private TextView serverStatusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_complaint);
        getSupportActionBar().hide();
        mProgressDialog = new MyProgressDialog(this);


        ProgressBar progressBar = findViewById(R.id.progressBar3);


        e3 = findViewById(R.id.dateformatID);
        e6= findViewById(R.id.scenerio);
        b2 = findViewById(R.id.onpresspredict);
        categorySpinner = findViewById(R.id.Categoryspinner);
        b1 = findViewById(R.id.Submit);
        e1 = findViewById(R.id.Incident_Occured_In);
        e2 = findViewById(R.id.editTime);
        subCategoryspinner = findViewById(R.id.sub_Categoryspinner);
        e4 = findViewById(R.id.Repoting_Delay);
        e5 = findViewById(R.id.Additional_Info);

        serverStatusTextView = findViewById(R.id.serverStatusTextView);



        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressDialog.showProgress();
                String a1 = e1.getText().toString();
                String b1 = e2.getText().toString();
                String c1 = e3.getText().toString();
                String d1 = e4.getText().toString();
                String f1 = e5.getText().toString();
                String p1 = e6.getText().toString();
                String selectedCategory = categorySpinner.getSelectedItem().toString();
                String selectedSubCategory = subCategoryspinner.getSelectedItem().toString();

                if(a1.equals("") || b1.equals("") || c1.equals("") || d1.equals("") || f1.equals("")|| p1.equals("")||selectedCategory.equals("select")||selectedSubCategory.equals("select")){
                    mProgressDialog.hideProgress(); Toast.makeText(register_complaint.this, "Please fill all the details.", Toast.LENGTH_SHORT).show();
                }else{

                    FirebaseAuth auth= FirebaseAuth.getInstance();
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    writecomplaint storecomplaint =new writecomplaint(a1,b1,c1,d1,f1,p1,selectedCategory,selectedSubCategory);
                    DatabaseReference referenceprofile = FirebaseDatabase.getInstance().getReference("Complaints");
                    referenceprofile.child(firebaseUser.getUid()).setValue(storecomplaint).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                // Generate case number and store in database
                                String caseNumber = generateCaseNumber();
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Complaints")
                                        .child(firebaseUser.getUid()).child("caseNumber");
                                reference.setValue(caseNumber);
                                // Display case number to user on next screen
                                Intent intent = new Intent(register_complaint.this, Complaint_success.class);
                                intent.putExtra("caseNumber", caseNumber);
                                startActivity(intent);
                                Toast.makeText(register_complaint.this, "Complaint sent successfully.", Toast.LENGTH_SHORT).show();

                                e1.setText("");
                                e2.setText("");
                                e3.setText("");
                                e4.setText("");
                                e5.setText("");
                                e6.setText("");
                                mProgressDialog.hideProgress();

                            }else {
                                mProgressDialog.hideProgress();
                                Toast.makeText(register_complaint.this, "Failed to send complaint. Please try again.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
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
        //spinner setup
        String[] categoryOptions = {"select","Online financial fraud", "Hacking/Damage to computer", "Cryptocurrency fraud", "Cyber terrorism", "Ransomware", "Online gambling", "Online cybertrafficking", "Any other cybercrime"};
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(register_complaint.this, android.R.layout.simple_spinner_item, categoryOptions);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
        // Define a map of sub-category options for each category option
        Map<String, String[]> subCategoryOptionsMap = new HashMap<>();
        subCategoryOptionsMap.put("select",new String[]{"select"});
        subCategoryOptionsMap.put("Online financial fraud", new String[]{"select","Buisness email compromise/email takeover", "Debit/credit-card fraud/sim swap fraud", "Demat/depositry fraud", "E-wallet related fraud", "Fraud call/vishing", "Internet banking related fraud", "UPI related fraud"});
        subCategoryOptionsMap.put("Hacking/Damage to computer", new String[]{"select","Hacking/Damage to computer", "Email hacking", "Tampering with computer source documents", "Unauthorised Access", "Website defacement"});
        subCategoryOptionsMap.put("Cryptocurrency fraud", new String[]{"select","Cryptocurrency fraud"});
        subCategoryOptionsMap.put("Cyber terrorism", new String[]{"select","Cyber terrorism"});
        subCategoryOptionsMap.put("Ransomware", new String[]{"select","Ransomware"});
        subCategoryOptionsMap.put("Online gambling", new String[]{"select","Online gambling"});
        subCategoryOptionsMap.put("Online cybertrafficking", new String[]{"select","Online trafficking"});
        subCategoryOptionsMap.put("Any other cybercrime", new String[]{"select","Other"});

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String selectedCategory = categorySpinner.getSelectedItem().toString();
                String[] subCategoryOptions = subCategoryOptionsMap.get(selectedCategory);
                ArrayAdapter<String> subCategoryAdapter = new ArrayAdapter<>(register_complaint.this, android.R.layout.simple_spinner_item, subCategoryOptions);
                subCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subCategoryspinner.setAdapter(subCategoryAdapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });







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
                String userInput = e6.getText().toString();
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
                    @SuppressLint("NewApi")
                    @Override
                    public void onResponse(JSONObject response) {
                        // Update the UI with the response
                        try {
                            String prediction1 = response.getString("prediction_1");
                            String prediction2 = response.getString("prediction_2");
                            String prediction_ensemble = response.getString("prediction_ensemble");
                            // Create a map of prediction numbers to labels
                            HashMap<String, String> predictionLabels = new HashMap<>();
                            predictionLabels.put("1.0", "Cyberbullying");
                            predictionLabels.put("2.0", "Cyber-stalking");
                            predictionLabels.put("3.0", "Malware attacks");
                            predictionLabels.put("4.0", "Online frauds");
                            predictionLabels.put("5.0", "Identity theft");
                            predictionLabels.put("6.0", "Phishing attacks");
                            predictionLabels.put("7.0", "Hacking");
                            predictionLabels.put("8.0", "Tampering of computer source codes");
                            predictionLabels.put("9.0", "Cybersexting");
                            predictionLabels.put("10.0", "Online defamation");
                            predictionLabels.put("11.0", "Child abuse");
                            predictionLabels.put("12.0", "Cyber terrorism");
                            predictionLabels.put("13.0", "ATM POS crimes");
                            predictionLabels.put("14.0", "DNS Cache poisoning");
                            //update the UI
                            prediction1ResultTextView.setText(predictionLabels.getOrDefault(prediction1, "N/A"));
                            prediction2ResultTextView.setText(predictionLabels.getOrDefault(prediction2, "N/A"));
                            ensemblePredictionResultTextView.setText(predictionLabels.getOrDefault(prediction_ensemble, "N/A"));



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
                            Toast.makeText(register_complaint.this, "Something went wrong", Toast.LENGTH_SHORT).show();

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
                e6.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        // Check if EditText is empty or not
                        if (e6.getText().toString().trim().isEmpty()) {
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


        // Create a new TimePickerDialog object
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Set the selected time on the EditText view
                        e2.setText(String.format("%02d:%02d", hourOfDay, minute));
                    }
                },
                // Set the default time to 12:00 AM
                0, 0,
                // Use the 24-hour format for the time picker
                true
        );
        e2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog.show();
            }
        });



        e3.setOnClickListener(new View.OnClickListener() {
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
                                e3.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

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

    private String generateCaseNumber() {
        long timeMillis = System.currentTimeMillis();
        Random random = new Random();
        int randomNumber = random.nextInt(9000) + 1000;
        String caseNumber = "CCR" + timeMillis + "-" + randomNumber;
        return caseNumber;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
