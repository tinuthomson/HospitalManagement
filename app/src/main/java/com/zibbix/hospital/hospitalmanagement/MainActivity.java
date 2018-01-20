package com.zibbix.hospital.hospitalmanagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //defining view objects
    private EditText editTextEmail, fname, lname, dob;
    private EditText editTextPassword;
    private Button buttonSignup;
    private TextView textViewSignin;

    private ProgressDialog progressDialog;
//ok

    //defining firebaseauth object
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isInternetOn();
        //shows setting a cutom font
        //initializing firebase auth object
        mAuth = FirebaseAuth.getInstance();
        //if getCurrentUser does not returns null
        if (mAuth.getCurrentUser() != null) {
            //that means user is already logged in
            //so close this activity
            finish();
            //and open profile activity
            startActivity(new Intent(this,LoginActivity.class));
        }
        //initializing views
        fname = (EditText) findViewById(R.id.editTextPassword1);
        lname = (EditText) findViewById(R.id.editTextPassword2);
        dob = (EditText) findViewById(R.id.editTextPassword100);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textViewSignin = (TextView) findViewById(R.id.textView2);
        buttonSignup = (Button) findViewById(R.id.buttonSignup);

        progressDialog = new ProgressDialog(this);

        //attaching listener to button

    }

    private void registerUser() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        //getting email and password from edit texts
        final String first_name = fname.getText().toString().trim();
        final String last_name = lname.getText().toString().trim();
        final String DOB = dob.getText().toString().trim();
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
        if (TextUtils.isEmpty(first_name)) {
            Toast.makeText(this, "Please enter First Name", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(last_name)) {
            Toast.makeText(this, "Please enter Last Name", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(DOB)) {
            Toast.makeText(this, "Please enter Dob", Toast.LENGTH_LONG).show();
            return;
        }
        if (password.length() < 8) {
            editTextPassword.setError("Minimum 8 Characters is needed");
        } else {
            //if the email and password are not empty
            //displaying a progress dialog

            progressDialog.setMessage("Registering Please Wait...");
            progressDialog.show();

            //creating a new user
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //checking if success
                            if (task.isSuccessful()) {
                                String user_id = mAuth.getCurrentUser().getUid();
                                DatabaseReference current_user_db = mDatabase.child(user_id);
                                current_user_db.child("fname").setValue(first_name);
                                current_user_db.child("lname").setValue(last_name);
                                current_user_db.child("dob").setValue(DOB);
                                progressDialog.dismiss();
                                finish();
                                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                            } else {
                                //display some message here
                                Toast.makeText(MainActivity.this, "Registration Error", Toast.LENGTH_LONG).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
        }
    }

    public void onClick1(View view) {
        if (view == buttonSignup) {
            registerUser();
        }
        if (view == textViewSignin) {
            //open login activity when user taps on the already registered textview
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING) {

            // if connected with internet

            return true;

        } else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            // if connected with internet

            return true;

        } else {
            if (
                    connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                            connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

                Toast.makeText(this, "Internet Connection is required!", Toast.LENGTH_LONG).show();
                return false;
            }
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

   /* public void backButtonHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        // Setting Dialog Title

        alertDialog.setTitle("Leave application?");

        // Setting Dialog Message

        alertDialog.setMessage("Are you sure you want to leave the application?");

        // Setting Icon to Dialog

        alertDialog.setIcon(R.drawable.ic_food);

        // Setting Positive "Yes" Button

        alertDialog.setPositiveButton("YES",

        new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                finish();

            }

        });

        // Setting Negative "NO" Button

        alertDialog.setNegativeButton("NO",

        new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                // Write your code here to invoke NO event

                dialog.cancel();

            }

        });

        // Showing Alert Message

        alertDialog.show();

    }*/

}



