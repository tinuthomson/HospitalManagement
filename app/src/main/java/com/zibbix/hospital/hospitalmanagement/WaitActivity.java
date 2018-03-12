package com.zibbix.hospital.hospitalmanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WaitActivity extends AppCompatActivity {
    DatabaseReference databaseRefUs = FirebaseDatabase.getInstance().getReference().child("Doctors").child("2tHlValmqGTLHHFHyQV9GFr7NBo1");

    private ChildEventListener event;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);

        final String appointID = getIntent().getExtras().getString("appointID");

            event = databaseRefUs.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                Intent intent = new Intent(WaitActivity.this,OverActivity.class);
                intent.putExtra("appointID", appointID);
                startActivity(intent);
                finish();


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseRefUs.removeEventListener(event);
    }
}
