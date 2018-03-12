package com.zibbix.hospital.hospitalmanagement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over);

        final String appointID = getIntent().getExtras().getString("appointID");


        final TextView pres = (TextView)findViewById(R.id.presct);
        final TextView symp = (TextView)findViewById(R.id.sympt);
        DatabaseReference databaseRefConsultpres = FirebaseDatabase.getInstance().getReference().child("Consultation").child(appointID).child("Prescriptiton");
        databaseRefConsultpres.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pres.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference databaseRefConsultsymp = FirebaseDatabase.getInstance().getReference().child("Consultation").child(appointID).child("Symptoms");
        databaseRefConsultsymp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                symp.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
