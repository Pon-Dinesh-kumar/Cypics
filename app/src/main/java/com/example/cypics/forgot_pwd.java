package com.example.cypics;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class forgot_pwd extends AppCompatActivity {
    private EditText textemail;
    private Button btnreset;
    private FirebaseAuth authprofile;

    private boolean isButtonClicked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pwd);
        getSupportActionBar().hide();
        textemail= findViewById(R.id.email_pass_reset);
        btnreset=findViewById(R.id.btnresetpwd);
        authprofile=FirebaseAuth.getInstance();
        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isButtonClicked){
                    isButtonClicked=true;
                    btnreset.setEnabled(false);
                    FirebaseUser user = authprofile.getCurrentUser();

                    String text_email=textemail.getText().toString();


                    if (TextUtils.isEmpty(text_email)){
                        Toast.makeText(forgot_pwd.this, "Please enter your email id", Toast.LENGTH_SHORT).show();
                        textemail.setError("email is required");
                        textemail.requestFocus();
                        isButtonClicked = false;
                        btnreset.setEnabled(true);
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(text_email).matches()) {
                        Toast.makeText(forgot_pwd.this, "Please Re-enter Your email id", Toast.LENGTH_SHORT).show();
                        textemail.setError("enter a valid email");
                        textemail.requestFocus();
                        isButtonClicked = false;
                        btnreset.setEnabled(true);
                    } else if (user != null && user.isEmailVerified()) {
                        Toast.makeText(forgot_pwd.this, "Please verify your email address first", Toast.LENGTH_SHORT).show();
                        isButtonClicked = false;
                        btnreset.setEnabled(true);
                        
                    }else {
                        Forgotpass(text_email);
                    }
                }



            }
        });
    }

    private void Forgotpass(String text_email) {
        authprofile.sendPasswordResetEmail(text_email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(forgot_pwd.this, "Check your email for password reset instruction", Toast.LENGTH_SHORT).show();
                    Intent intent =new Intent(forgot_pwd.this,Login_firebase.class);
                    startActivity(intent);
                    //this helps user to going back once again to user activity
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK
                            |Intent.FLAG_ACTIVITY_NEW_TASK);

                }else{
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e){
                        textemail.setError("user doesn't exist Please Register ");
                        textemail.requestFocus();
                    }catch (Exception e){
                        Toast.makeText(forgot_pwd.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                    isButtonClicked = false;
                    btnreset.setEnabled(true);
                }
            }
        });

    }
}