package com.example.cypics;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class Login_firebase extends AppCompatActivity {

    private EditText email_login, password_login;
    private Button btnlogin,forgotpass;
    private FirebaseAuth authprofile;
    private boolean isLoginButtonEnabled = true;

    private LoginProgressDialog mProgressDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        mProgressDialog = new LoginProgressDialog(this);
        email_login = (EditText) findViewById(R.id.username1);
        password_login = (EditText) findViewById(R.id.password1);
        forgotpass=findViewById(R.id.forgotpass);
        btnlogin = (Button) findViewById(R.id.btnsignin1);
        authprofile =FirebaseAuth.getInstance();
        //Show hide password
        ImageView imageViewshowhidepwd =findViewById(R.id.hide_show_pass);
        imageViewshowhidepwd.setImageResource(R.drawable.ic_hide_pwd);
        imageViewshowhidepwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password_login.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    //if pass is visible then hide it
                    password_login.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    //change icon
                    imageViewshowhidepwd.setImageResource(R.drawable.ic_hide_pwd);
                } else {
                    password_login.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageViewshowhidepwd.setImageResource(R.drawable.ic_show_pwd);
                }
            }
        });
        //forgot_password
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Login_firebase.this, "You can reset your Password now!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Login_firebase.this,forgot_pwd.class);
                startActivity(i);
            }
        });


        //login

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressDialog.showProgress();
                if (isLoginButtonEnabled){
                    isLoginButtonEnabled=false;
                    String textEmail=email_login.getText().toString();
                    String textpwd=password_login.getText().toString();

                    if (TextUtils.isEmpty(textEmail)){
                        Toast.makeText(Login_firebase.this, "Please enter your email id", Toast.LENGTH_SHORT).show();
                        email_login.setError("email is required");
                        email_login.requestFocus();
                        mProgressDialog.hideProgress();
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                        Toast.makeText(Login_firebase.this, "Re-enter your email id ", Toast.LENGTH_SHORT).show();
                        email_login.setError("Enter a valid email id ");
                        email_login.requestFocus();
                        mProgressDialog.hideProgress();
                    } else if (TextUtils.isEmpty(textpwd)) {
                        Toast.makeText(Login_firebase.this, "Please enter your password ", Toast.LENGTH_SHORT).show();
                        password_login.setError("Password is Required");
                        password_login.requestFocus();
                        mProgressDialog.hideProgress();
                    }else {
                        mProgressDialog.hideProgress();
                        //progress bar logic
                        Loginuser(textEmail,textpwd);

                    }
                    //disable button for 2 sec
                    new Handler().postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                    isLoginButtonEnabled=true;
                                }
                            },
                            2000);

                }

            }
        });
    }

    private void Loginuser(String textEmail, String textpwd) {
        authprofile.signInWithEmailAndPassword(textEmail, textpwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //get instance
                    FirebaseUser firebaseUser = authprofile.getCurrentUser();
                    //check if email is verified
                    if (firebaseUser.isEmailVerified()){
                        Toast.makeText(Login_firebase.this, "You are Logged in now", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Login_firebase.this,aftersignin.class);
                        startActivity(i);
                    }else {
                        //sign off the user
                        showalertdialog();
                    }


                }else {
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e){
                        email_login.setError("User does not exist or no longer Valid please Register");
                        email_login.requestFocus();
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        password_login.setError("invalid password");
                        password_login.requestFocus();
                        Toast.makeText(Login_firebase.this, "please reset the password in case you forgotten", Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Toast.makeText(Login_firebase.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
                //turn off logic progress bar
            }
        });
    }


    private void showalertdialog() {
        //setup alert
        AlertDialog.Builder builder= new AlertDialog.Builder(Login_firebase.this);
        builder.setTitle("Email not Verified ");
        builder.setMessage("please verify your email address and try again");

        //open email apps
        builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent =new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //to email app in a new window
                startActivity(intent);
            }
        });
        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }
}