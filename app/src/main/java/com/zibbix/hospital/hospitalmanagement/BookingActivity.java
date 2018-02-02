package com.zibbix.hospital.hospitalmanagement;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;





public class BookingActivity extends BaseActivity {


    final List<String> Dept = new ArrayList<>();
    final List<String> Doctors = new ArrayList<>();
    Spinner s, s1;
    private String dateformat;
    TextView edittext;
    DatabaseReference databaseRefDept = FirebaseDatabase.getInstance().getReference().child("Dept");
    protected NavigationView navigationView;
    String DoctorUID;


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
                                           DatabaseReference databaseRefDoc = FirebaseDatabase.getInstance().getReference().child("Doctors").child(DoctorUID).child("Name");
                                            databaseRefDoc.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    String DocName = dataSnapshot.getValue(String.class);
                                                    Doctors.add(DocName);
                                                    DocAdapter.notifyDataSetChanged();
                                                    s.setAdapter(DocAdapter);
                                                    DocAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
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



        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String currentdate = dateFormat1.format(cal.getTime());
        String todate = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());

        //edit
        edittext = (TextView) findViewById(R.id.selectDate);
        edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog mDatePicker = new DatePickerDialog(BookingActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        String selectedPJPDate = String.valueOf(selectedyear) + "-" + String.valueOf(selectedmonth + 1) + "-" + String.valueOf(selectedday);

                        // String selectedPJPDate=String.valueOf(selectedday)+"-"+String.valueOf(selectedmonth+1)+"-"+String.valueOf(selectedyear);
                        try {
                            Calendar cals = Calendar.getInstance();
                            cals.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(selectedPJPDate));
                            //pjpSelectedDate = new SimpleDateFormat("dd-MM-yyyy").format(cals.getTime());

                            dateformat = new SimpleDateFormat("dd-MM-yyyy").format(cals.getTime());

                        } catch (ParseException p) {
                            p.printStackTrace();

                        }

                        edittext.setText(dateformat);

                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });
        final RadioGroup noonGroup = (RadioGroup) findViewById(R.id.noon);
        Button bookButton = (Button)findViewById(R.id.book);
        bookButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                DatabaseReference databaseRefAppoint = FirebaseDatabase.getInstance().getReference().child("Appointments");
                DatabaseReference UIDkeyRef = databaseRefAppoint.push();
                String appointID = UIDkeyRef.getKey();
                DatabaseReference databaseRefUID = FirebaseDatabase.getInstance().getReference().child("Appointments").child(appointID);
                databaseRefUID.child("DoctorUID").setValue(DoctorUID);
                databaseRefUID.child("Date").setValue(dateformat);
                int selectedradio = noonGroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(selectedradio);
                databaseRefUID.child("Session").setValue(radioButton.getText());
                assert currentFirebaseUser != null;
                databaseRefUID.child("PatientUID").setValue(currentFirebaseUser.getUid());
                DatabaseReference databaseRefDoctor = FirebaseDatabase.getInstance().getReference().child("Doctors").child(DoctorUID).child("Appointment");
                databaseRefDoctor.child(appointID).setValue(true);
                DatabaseReference databaseRefUser = FirebaseDatabase.getInstance().getReference().child("Users").child(currentFirebaseUser.getUid()).child("Appointment");
                databaseRefUser.child(appointID).setValue(true);
                Intent intent = new Intent(BookingActivity.this,SuccessActivity.class);
                intent.putExtra("appointID", appointID);
                startActivity(intent);

            }

        });


    }



}


