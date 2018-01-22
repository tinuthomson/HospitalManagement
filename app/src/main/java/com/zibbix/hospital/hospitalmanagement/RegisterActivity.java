package com.zibbix.hospital.hospitalmanagement;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //defining view objects
    private EditText editTextEmail, fname, lname;
    private TextView dob;
    private EditText editTextPassword;
    private Button buttonSignup;
    private TextView textViewSignin;
    private ProgressDialog progressDialog;
    //defining firebaseauth object
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextView tv_fromdate,tv_todate;
    private String currentdate,fromdate,todate,dateformat;
    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        currentdate = dateFormat1.format(cal.getTime());
        fromdate=new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        todate=new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        isInternetOn();
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
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
        dob = (TextView) findViewById(R.id.editTextPassword100);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textViewSignin = (TextView) findViewById(R.id.textView2);
        buttonSignup = (Button) findViewById(R.id.buttonSignup);

        progressDialog = new ProgressDialog(this);
dob.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Calendar mcurrentDate=Calendar.getInstance();
        int mYear=mcurrentDate.get(Calendar.YEAR);
        int mMonth=mcurrentDate.get(Calendar.MONTH);
        int mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog mDatePicker=new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
// TODO Auto-generated method stub
                String selectedPJPDate=String.valueOf(selectedyear)+"-"+String.valueOf(selectedmonth+1)+"-"+String.valueOf(selectedday);

// String selectedPJPDate=String.valueOf(selectedday)+"-"+String.valueOf(selectedmonth+1)+"-"+String.valueOf(selectedyear);
                try {
                    Calendar cals = Calendar.getInstance();
                    cals.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(selectedPJPDate));
//pjpSelectedDate = new SimpleDateFormat("dd-MM-yyyy").format(cals.getTime());
                    fromdate=new SimpleDateFormat("yyyy-MM-dd").format(cals.getTime());

                    dateformat=new SimpleDateFormat("dd-MM-yyyy").format(cals.getTime());

                }
                catch (ParseException p)
                {
                    p.printStackTrace();
                }

                dob.setText(dateformat);
            }
        },mYear, mMonth, mDay);
        mDatePicker.setTitle("Select date");
        mDatePicker.show();
    }
});

    }

        //attaching listener to button



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
                                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                            } else {
                                //display some message here
                                Toast.makeText(RegisterActivity.this, "Registration Error", Toast.LENGTH_LONG).show();
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
   /* public void backButtonHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegisterActivity.this);

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



