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
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    //defining views
    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignup,txt;

    //firebase auth object
    private FirebaseAuth firebaseAuth;

    //progress dialog
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_new);
        Target viewTarget = new ViewTarget(R.id.buttonSignin, this);
        new ShowcaseView.Builder(this)
                .setTarget(viewTarget)
                .setContentTitle("Tap  To Signin ")
                .setContentText("Easy to Login")
                .singleShot(42)
                .build();
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        //getting firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        //if the objects getcurrentuser method is not null
        //means user is already logged in
        if(firebaseAuth.getCurrentUser() != null){
            //close this activity
            finish();
            //opening profile activity
            startActivity(new Intent(getApplicationContext(), Main2Activity.class));
        }
        //initializing views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonSignIn = (Button) findViewById(R.id.buttonSignin);
        textViewSignup  = (TextView) findViewById(R.id.textViewSignUp);
        txt  = (TextView) findViewById(R.id.textView3);
        ((ShowHidePasswordEditText) findViewById(R.id.editTextPassword)).setTintColor(Color.RED);
        progressDialog = new ProgressDialog(this);

        //attaching click listener
        buttonSignIn.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);
        txt.setOnClickListener(this);
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
            if (password.length() < 8) {
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
                                    startActivity(new Intent(getApplicationContext(), Main2Activity.class));
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

    @Override
    public void onClick(View view) {
        if(view == buttonSignIn){
            userLogin();

        }

        if(view == textViewSignup){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
        if(view==txt)
        {
            Intent intent=new Intent(this,ResetPasswordActivity.class);
            startActivity(intent);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Write your logic here
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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




}