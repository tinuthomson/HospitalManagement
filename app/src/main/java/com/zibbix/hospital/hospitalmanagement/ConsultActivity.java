package com.zibbix.hospital.hospitalmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class ConsultActivity extends AppCompatActivity {
    final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

    private ValueEventListener event;
    DatabaseReference databaseRefUser = FirebaseDatabase.getInstance().getReference().child("Users").child(currentFirebaseUser.getUid()).child("Appointment");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult);
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        final TextView counter = (TextView)findViewById(R.id.textView7);
        final TextView ucounter = (TextView)findViewById(R.id.textView11);



        DatabaseReference databaseRefUser = FirebaseDatabase.getInstance().getReference().child("Users").child(currentFirebaseUser.getUid()).child("Appointment");
        databaseRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                DatabaseReference databaseRefdoc = FirebaseDatabase.getInstance().getReference().child("Doctors").child("2tHlValmqGTLHHFHyQV9GFr7NBo1").child("counter");
                event = databaseRefdoc.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dSnapshot) {
                        final long c = (long) dSnapshot.getValue();


                        for (final DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                            counter.setText(String.valueOf(c));
                            DatabaseReference databaseRefUs = FirebaseDatabase.getInstance().getReference().child("Appointments").child(Snapshot.getKey()).child("counter");
                            databaseRefUs.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot datSnapshot) {
                                    ucounter.setText(String.valueOf(datSnapshot.getValue().toString()));
                                    if(Objects.equals(Long.toString(c), datSnapshot.getValue().toString())){
                                        Intent intent = new Intent(ConsultActivity.this,WaitActivity.class);
                                        intent.putExtra("appointID", Snapshot.getKey());
                                        startActivity(intent);
                                        finish();
                                    }


                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseRefUser.removeEventListener(event);
    }
}
