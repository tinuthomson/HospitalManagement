package com.zibbix.hospital.hospitalmanagement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AppointActivity extends AppCompatActivity {

    final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoint);

        final TextView DocName = (TextView)findViewById(R.id.docNamedit);
        final TextView Date = (TextView)findViewById(R.id.datedit);
        final TextView Session = (TextView)findViewById(R.id.session);
        final TextView TicketNumber =(TextView)findViewById(R.id.textView8) ;


        DatabaseReference databaseRefUser = FirebaseDatabase.getInstance().getReference().child("Users").child(currentFirebaseUser.getUid()).child("Appointment");
        databaseRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
            String appointID;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dSnapshot : dataSnapshot.getChildren()) {
                    appointID = dSnapshot.getKey();

                }
                Toast.makeText(AppointActivity.this,appointID,Toast.LENGTH_SHORT).show();
                DatabaseReference databaseRefappoint = FirebaseDatabase.getInstance().getReference().child("Appointments").child(appointID);
                databaseRefappoint.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int i = 0;
                        for (DataSnapshot dSnapshot : dataSnapshot.getChildren()) {
                            if (i == 1) {
                                DatabaseReference databaseRefDoc = FirebaseDatabase.getInstance().getReference().child("Doctors").child(dSnapshot.getValue().toString()).child("Name");
                                databaseRefDoc.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        DocName.setText(dataSnapshot.getValue().toString());

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                            if (i == 0) {
                                Date.setText(dSnapshot.getValue().toString());
                            }
                            if (i == 3)
                                Session.setText(dSnapshot.getValue().toString());
                            if(i==4){
                                TicketNumber.setText(dSnapshot.getValue().toString());
                            }


                            i++;

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
