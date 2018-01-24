package com.zibbix.hospital.hospitalmanagement;

/**
 * Created by anoop on 15/1/18.
 *
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

public  class LoginActivity extends AppCompatActivity {


    //defining views
    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignup, txt;

    //firebase auth object
    private FirebaseAuth firebaseAuth;

    //progress dialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Target viewTarget = new ViewTarget(R.id.buttonSignin, this);
        new ShowcaseView.Builder(this)
                .setTarget(viewTarget)
                .setContentTitle("Tap  To Signin ")
                .setContentText("Easy to Login")
                .singleShot(42)
                .build();
        //getting firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        //if the objects getcurrentuser method is not null
        //means user is already logged in
        if (firebaseAuth.getCurrentUser() != null) {
            //close this activity
            finish();
            //opening profile activity
            startActivity(new Intent(getApplicationContext(), BaseActivity.class));
        }
        //initializing views
        editTextEmail = (EditText) findViewById(R.id.patient_email);
        editTextPassword = (EditText) findViewById(R.id.patient_password);
        buttonSignIn = (Button) findViewById(R.id.buttonSignin);
        textViewSignup = (TextView) findViewById(R.id.textViewSignUp);
        txt = (TextView) findViewById(R.id.textView3);
        ((ShowHidePasswordEditText) findViewById(R.id.patient_password)).setTintColor(Color.RED);
        progressDialog = new ProgressDialog(this);

        //attaching click listener

    }



    //method for user login
    private void userLogin() {
        if (isInternetOn()) {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            //checking if email and passwords are empty
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
                return;
            }
            if (password.length() < 6) {
                editTextPassword.setError("Password Length is not satisfied");
                return;
            } else  {
                //if the email and password are not empty
                //displaying a progress dialog

                progressDialog.setMessage("Verifying Details ...");
                progressDialog.setIcon(R.drawable.ok);
                progressDialog.show();
                //logging in the user
                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                //if the task is successfull
                                if (task.isSuccessful()) {
                                    //start the profile activity
                                    finish();
                                    startActivity(new Intent(getApplicationContext(), BaseActivity.class));
                                } else {
                                    Toast.makeText(LoginActivity.this, "Incorrect Credentials", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }
        else
        {
            Toast.makeText(this, "Internet Connection is required!", Toast.LENGTH_LONG).show();
        }
    }

    public void onClick1(View view) {
        if(view == buttonSignIn){
            userLogin();
        }
        if(view == textViewSignup){
            startActivity(new Intent(this, RegisterActivity.class));
        }
        if(view==txt)
        {
            Intent intent=new Intent(this,ResetPasswordActivity.class);
            startActivity(intent);
        }
    }


    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {

            // if connected with internet

            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {


            return false;
        }
        return false;
    }
    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {
            // backButtonHandler();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();// finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }


    }
}