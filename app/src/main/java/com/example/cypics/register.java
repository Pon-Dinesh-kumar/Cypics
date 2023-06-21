package com.example.cypics;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class register extends AppCompatActivity  {
    private RadioGroup RadioGroupRegisterGender;
    private DatePickerDialog picker;
    private RadioButton RadioButtonRegisterGenderSelected;
    private EditText registerfullname,registeremail,registerdob,registermobile,registerpassword,registerrepassword;
    private TextView registergender;
    private ProgressBar progressBar;
    private Button btnlogin,verify;

    private boolean isRegistering = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        Toast.makeText(register.this, "You can register now", Toast.LENGTH_SHORT).show();

        registerfullname = findViewById(R.id.register_full_name);
        registeremail=findViewById(R.id.register_email);
        registerdob=findViewById(R.id.register_dob);
        registergender=findViewById(R.id.register_gender);
        registermobile=findViewById(R.id.register_mobile);
        registerpassword=findViewById(R.id.register_password);
        registerrepassword=findViewById(R.id.register_repassword);
        progressBar=findViewById(R.id.register_progressbar);
        btnlogin=findViewById(R.id.btn_login);
        verify=findViewById(R.id.verify_button);
        //radiobutton for gender
        RadioGroupRegisterGender=findViewById(R.id.radiogroup_gender);
        RadioGroupRegisterGender.clearCheck();


        //date window
        registerdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month  = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                picker= new DatePickerDialog(register.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month , int dayofmonth) {
                        registerdob.setText(dayofmonth+"/"+(month+1)+ "/"+year);

                    }
                },year,month,day);
                picker.show();
            }
        });


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(register.this,Login_firebase.class);
                startActivity(i);
            }
        });

        Button ButtonRegister=findViewById(R.id.btn_register);
        ButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Check if registration is already in progress
                if (isRegistering) {
                    return;
                }
                // Registration is not in progress, so set the flag and disable the button
                isRegistering = true;
                ButtonRegister.setEnabled(false);
                // Start a new thread to re-enable the button after a time interval
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ButtonRegister.setEnabled(true);
                        isRegistering = false;
                    }
                }, 2000); // 2 seconds delay before re-enabling the button
                int selectedGenderId=RadioGroupRegisterGender.getCheckedRadioButtonId();
                RadioButtonRegisterGenderSelected= findViewById(selectedGenderId);
                //obtain the entered data
                String textfullname=registerfullname.getText().toString();
                String email=registeremail.getText().toString();
                String dateofbirth=registerdob.getText().toString();
                String mobile=registermobile.getText().toString();
                String password=registerpassword.getText().toString();
                String repassword=registerrepassword.getText().toString();
                String textgender;
                if (TextUtils.isEmpty(textfullname)){
                    Toast.makeText(register.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                    registerfullname.setError("Name is Required");
                    registerfullname.requestFocus();
                } else if (TextUtils.isEmpty(email)) {
                    Toast.makeText(register.this, "Please enter your email id ", Toast.LENGTH_SHORT).show();
                    registeremail.setError("email id  is Required");
                    registeremail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(register.this, "Please re-enter your email id ", Toast.LENGTH_SHORT).show();
                    registeremail.setError("valid email is Required");
                    registeremail.requestFocus();
                } else if (TextUtils.isEmpty(dateofbirth)) {
                    Toast.makeText(register.this, "Please enter your Date Of Birth ", Toast.LENGTH_SHORT).show();
                    registerdob.setError("Date Of Birth is required");
                    registerdob.requestFocus();
                } else if (RadioGroupRegisterGender.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(register.this, "Please select your Gender ", Toast.LENGTH_SHORT).show();
                    RadioButtonRegisterGenderSelected.setError("Gender is required");
                    RadioButtonRegisterGenderSelected.requestFocus();
                } else if (TextUtils.isEmpty(mobile)) {
                    Toast.makeText(register.this, "Please enter your mobile number ", Toast.LENGTH_SHORT).show();
                    registermobile.setError("Mobile Number is required");
                    registermobile.requestFocus();
                } else if (mobile.length()!=10) {
                    Toast.makeText(register.this, "Please re-enter your mobile number ", Toast.LENGTH_SHORT).show();
                    registermobile.setError("Mobile Number should be 10 digit");
                    registermobile.requestFocus();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(register.this, "Please enter your password ", Toast.LENGTH_SHORT).show();
                    registerpassword.setError("Password is Required");
                    registerpassword.requestFocus();
                } else if (password.length() < 6 ) {
                    Toast.makeText(register.this, "Password should be at least 6 digit ", Toast.LENGTH_SHORT).show();
                    registerpassword.setError("Password is too weak");
                    registerpassword.requestFocus();
                }else if (TextUtils.isEmpty(repassword)) {
                    Toast.makeText(register.this, "Please confirm your password ", Toast.LENGTH_SHORT).show();
                    registerrepassword.setError("Password Confirmation is Required");
                    registerrepassword.requestFocus();
                } else if (!password.equals(repassword)) {
                    Toast.makeText(register.this, "Please enter the same password", Toast.LENGTH_SHORT).show();
                    registerrepassword.setError("same Password is Required");
                    registerrepassword.requestFocus();
                    //clear the entered password
                    registerrepassword.clearComposingText();
                    registerrepassword.clearComposingText();
                }else {
                    textgender=RadioButtonRegisterGenderSelected.getText().toString();
                    progressBar.setVisibility(view.VISIBLE);
                    RegisterUser(textfullname,email,dateofbirth,mobile,textgender,password);
                }
            };





        });
    }


//Register user with credinatial

    private void RegisterUser(String textfullname, String email, String dateofbirth, String mobile, String textgender, String password) {
        FirebaseAuth auth=FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(register.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            //Enter user data to the firebase realtime database

                            Readwriteuserdetails writeuserdetails = new Readwriteuserdetails(textfullname,dateofbirth,mobile,textgender);

                            //Extra User reference from Firebase
                            DatabaseReference referenceprofile = FirebaseDatabase.getInstance().getReference("Registered Users");

                            referenceprofile.child(firebaseUser.getUid()).setValue(writeuserdetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        //send verification
                                        firebaseUser.sendEmailVerification();
                                        Toast.makeText(register.this, "User Registered successfully.Please check your inbox for email verification", Toast.LENGTH_LONG).show();

                                        //open afterlogin
                                        Intent intent = new Intent(register.this,Login_firebase.class);
                                        startActivity(intent);
                                        //this helps user to going back once again to user activity
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                |Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish(); //close

                                    }else {
                                        try {
                                            throw task.getException();
                                        }catch (FirebaseAuthWeakPasswordException e){
                                            registerpassword.setError("weak password");
                                            registerpassword.requestFocus();
                                        }catch (Exception e){
                                            Toast.makeText(register.this, "User Registered failed.Please Try again later", Toast.LENGTH_LONG).show();
                                            progressBar.setVisibility(View.GONE);
                                        }



                                    }

                                }
                            });


                        }
                    }
                });
    }
}