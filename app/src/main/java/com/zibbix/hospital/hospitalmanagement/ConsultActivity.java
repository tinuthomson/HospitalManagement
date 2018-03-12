package com.zibbix.hospital.hospitalmanagement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

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
import java.util.List;

public class ConsultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult);
        final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        final TextView counter = (TextView)findViewById(R.id.textView7);
        final TextView ucounter = (TextView)findViewById(R.id.textView11);



        DatabaseReference databaseRefUser = FirebaseDatabase.getInstance().getReference().child("Users").child(currentFirebaseUser.getUid()).child("Appointment");
        databaseRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                DatabaseReference databaseRefdoc = FirebaseDatabase.getInstance().getReference().child("Doctors").child("2tHlValmqGTLHHFHyQV9GFr7NBo1").child("counter");
                databaseRefdoc.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dSnapshot) {
                        long c = (long) dSnapshot.getValue();
                        for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                            counter.setText(String.valueOf(c));
                            ucounter.setText(String.valueOf(Snapshot.getValue().toString()));

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


    }
}
