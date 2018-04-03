package com.zibbix.hospital.hospitalmanagement;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class BookingActivity extends BaseActivity {

int myear,mmonth,mday;
    final List<String> Dept = new ArrayList<>();
    final List<String> Doctors = new ArrayList<>();
    Spinner s, s1;
    private String dateformat;
    ImageView edittext;
    TextView date;
     private int strdate;
    static final int DATE_PICKER_ID = 1111;
    DatabaseReference databaseRefDept = FirebaseDatabase.getInstance().getReference().child("Dept");
    protected NavigationView navigationView;
    String DoctorUID;
    String DocName;
    HashMap<String, String> DocHash=new HashMap<String, String>();


    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_booking, null, false);
        drawer.addView(contentView, 0);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_booking);
        s = (Spinner) findViewById(R.id.s);
        date=(TextView)findViewById(R.id.selectDate);
        s1 = (Spinner) findViewById(R.id.s1);
        final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

        databaseRefDept.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dSnapshot : dataSnapshot.getChildren()) {
                        String DeptName = dSnapshot.getKey();
                        Dept.add(DeptName);
                    }
                    ArrayAdapter<String> DeptAdapter = new ArrayAdapter<>(BookingActivity.this, android.R.layout.simple_spinner_item, Dept);
                    DeptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    s1.setAdapter(DeptAdapter);
                    s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            final ArrayAdapter<String> DocAdapter = new ArrayAdapter<>(BookingActivity.this, android.R.layout.simple_spinner_item, Doctors);
                            Doctors.clear();
                            String selectedDept = s1.getSelectedItem().toString();
                            DatabaseReference databaseRefDept = FirebaseDatabase.getInstance().getReference().child("Dept").child(selectedDept);
                            databaseRefDept.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot dSnapshot : dataSnapshot.getChildren()) {
                                            DoctorUID = dSnapshot.getKey();
                                            DocName = dSnapshot.getValue().toString();
                                            DocHash.put(DocName,DoctorUID);
                                            Doctors.add(DocName);
                                            DocAdapter.notifyDataSetChanged();
                                            s.setAdapter(DocAdapter);
                                            DocAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        }

                                    }
                                }//onDataChange

                                @Override
                                public void onCancelled(DatabaseError error) {

                                }//onCancelled
                            });
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }

                    });


                }
            }//onDataChange

            @Override
            public void onCancelled(DatabaseError error) {

            }//onCancelled
        });


        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DocName = s.getSelectedItem().toString();
                DoctorUID = DocHash.get(DocName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        final String currentdate = dateFormat1.format(cal.getTime());
        final String todate = new SimpleDateFormat("dd-MM-yyyy").format(cal.getTime());
        //edit
        edittext = (ImageView) findViewById(R.id.cal);
        edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                 myear = mcurrentDate.get(Calendar.YEAR);
                 mmonth = mcurrentDate.get(Calendar.MONTH);
                mday = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                showDialog(DATE_PICKER_ID);

            }
        });

        final RadioGroup noonGroup = (RadioGroup) findViewById(R.id.noon);
        Button bookButton = (Button)findViewById(R.id.book);
        bookButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                DatabaseReference databaseRefAppoint = FirebaseDatabase.getInstance().getReference().child("Appointments");
                DatabaseReference UIDkeyRef = databaseRefAppoint.push();
                final String appointID = UIDkeyRef.getKey();
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("Doctors").child(DoctorUID).child("DateCounter");
                databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int k = 0;
                        for (DataSnapshot dSnapshot : dataSnapshot.getChildren()) {
                            if (Objects.equals(dSnapshot.getKey(), todate)) {
                                k = 1;
                                long counter = (long) dSnapshot.getValue();
                                counter = counter + 1;
                                dSnapshot.getRef().setValue(counter);
                                DatabaseReference databaseRefUID = FirebaseDatabase.getInstance().getReference().child("Appointments").child(appointID);
                                databaseRefUID.child("counter").setValue(counter);
                                break;
                            }
                        }
                        if (k == 0) {
                            DatabaseReference databaseRefdate = FirebaseDatabase.getInstance().getReference().child("Doctors").child(DoctorUID).child("DateCounter");
                            databaseRefdate.child(todate).setValue(1);
                            DatabaseReference databaseRefUID = FirebaseDatabase.getInstance().getReference().child("Appointments").child(appointID);
                            databaseRefUID.child("counter").setValue(1);

                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                DatabaseReference databaseRefpat = FirebaseDatabase.getInstance().getReference().child("Users").child(currentFirebaseUser.getUid());
                databaseRefpat.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String first = "";
                        String second = "";
                        int i = 0;
                        for (DataSnapshot dSnapshot : dataSnapshot.getChildren()) {

                            if (i == 2) {
                                first = dSnapshot.getValue().toString();
                            }
                            if (i == 3) {
                                second = dSnapshot.getValue().toString();


                            }


                            i++;

                        }
                        DatabaseReference databaseRefUID = FirebaseDatabase.getInstance().getReference().child("Appointments").child(appointID);
                        databaseRefUID.child("Patient Name").setValue(first + " " + second);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                DatabaseReference databaseRefUID = FirebaseDatabase.getInstance().getReference().child("Appointments").child(appointID);
                databaseRefUID.child("DoctorUID").setValue(DoctorUID);
                databaseRefUID.child("DoctorName").setValue(DocName);
                databaseRefUID.child("Date").setValue(todate);
                int selectedradio = noonGroup.getCheckedRadioButtonId();
                Date dt = new Date();
                int hours = dt.getHours();
                int min = dt.getMinutes();
                RadioButton radioButton = (RadioButton) findViewById(selectedradio);
                if ((selectedradio==R.id.foreNoon)&&(hours>=12 || hours<=16)) {

                        Toast.makeText(BookingActivity.this, "Booking Denied!", Toast.LENGTH_SHORT).show();


                }
                else {
                    databaseRefUID.child("Session").setValue(radioButton.getText());
                    assert currentFirebaseUser != null;
                    databaseRefUID.child("PatientUID").setValue(currentFirebaseUser.getUid());
                    DatabaseReference databaseRefDoctor = FirebaseDatabase.getInstance().getReference().child("Doctors").child(DoctorUID).child("Appointment");
                    databaseRefDoctor.child(appointID).setValue(todate);
                    DatabaseReference databaseRefUser = FirebaseDatabase.getInstance().getReference().child("Users").child(currentFirebaseUser.getUid()).child("Appointment");
                    databaseRefUser.child(appointID).setValue(true);
                    Intent intent = new Intent(BookingActivity.this, SuccessActivity.class);
                    intent.putExtra("appointID", appointID);
                    startActivity(intent);

                }
            }

        });


    }
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:
                // create a new DatePickerDialog with values you want to show

                DatePickerDialog datePickerDialog = new DatePickerDialog(this, datePickerListener, myear, mmonth,mday);
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, 0); // Add 0 days to Calendar
                Date newDate = calendar.getTime();
                datePickerDialog.getDatePicker().setMinDate(newDate.getTime()-(newDate.getTime()%(24*60*60*1000)));
                return datePickerDialog;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        // the callback received when the user "sets" the Date in the
        // DatePickerDialog
        public void onDateSet(DatePicker view, int  selectedYear, int selectedMonth, int selectedDay) {
strdate=selectedDay+selectedMonth+selectedYear;
            date.setText(selectedDay + "-" + (selectedMonth + 1) + "-" + selectedYear);
        }
    };



}


