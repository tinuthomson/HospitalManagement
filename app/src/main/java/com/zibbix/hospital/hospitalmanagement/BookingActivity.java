package com.zibbix.hospital.hospitalmanagement;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
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
import java.util.Locale;





public class BookingActivity extends BaseActivity {


    final List<String> Dept = new ArrayList<String>();
    Spinner s,s1;
    private String currentdate,fromdate,todate,dateformat;
    Calendar myCalendar = Calendar.getInstance(Locale.getDefault());
    TextView edittext;
    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Dept");
    protected NavigationView navigationView;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        @SuppressLint("InflateParams") View contentView = inflater.inflate(R.layout.activity_booking, null, false);
        drawer.addView(contentView, 0);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_booking);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dSnapshot: dataSnapshot.getChildren()) {
                        String DeptName = dSnapshot.getKey();
                        Dept.add(DeptName);
                    }
                    s=(Spinner)findViewById(R.id.s);
                    s1=(Spinner)findViewById(R.id.s1);
                    ArrayAdapter<String> DeptAdapter = new ArrayAdapter<String>(BookingActivity.this, android.R.layout.simple_spinner_item, Dept);
                    DeptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    s1.setAdapter(DeptAdapter);
                }
            }//onDataChange

            @Override
            public void onCancelled(DatabaseError error) {

            }//onCancelled
        });

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        currentdate = dateFormat1.format(cal.getTime());
        fromdate=new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        todate=new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());

        //edit
        edittext = (TextView) findViewById(R.id.selectDate);
        edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate=Calendar.getInstance();
                int mYear=mcurrentDate.get(Calendar.YEAR);
                int mMonth=mcurrentDate.get(Calendar.MONTH);
                int mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog mDatePicker=new DatePickerDialog(BookingActivity.this, new DatePickerDialog.OnDateSetListener() {
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

                        edittext.setText(dateformat);
                    }
                },mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });


/*

        final ArrayAdapter<String>ia=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,Dept);

        final ArrayAdapter<String>ma=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,m);

        final ArrayAdapter<String>ba=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,b);

        final ArrayAdapter<String>ua=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,u);

        ia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(ia);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (parent.getId()){
                    case R.id.s:{
                        if(Dept[position].equals("Gyno")){
                            ma.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            s1.setAdapter(ma);
                        }
                        if(Dept[position].equals("Nuero")){

                            ba.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            s1.setAdapter(ba);
                        }
                        if(Dept[position].equals("Mri")){

                            ua.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            s1.setAdapter(ua);
                        }

                    }


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        */
    }
/*
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {


        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };
    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

    }
    public void booknow(View view)
    {

    }
    */
}

