package com.zibbix.hospital.hospitalmanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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


        final TextView pres = (TextView) findViewById(R.id.presct);
        final TextView symp = (TextView) findViewById(R.id.sympt);
        Button feedback = (Button) findViewById(R.id.feedb);
        Button booking = (Button) findViewById(R.id.booki);
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

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OverActivity.this,FeedbackActivity.class);
                intent.putExtra("appointID", appointID);
                startActivity(intent);
                finish();
            }
        });

        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OverActivity.this,BookingActivity.class);
                intent.putExtra("appointID", appointID);
                startActivity(intent);
                finish();
            }
        });

    }

}
