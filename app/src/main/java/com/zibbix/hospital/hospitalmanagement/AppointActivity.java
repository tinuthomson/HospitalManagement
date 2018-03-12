package com.zibbix.hospital.hospitalmanagement;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AppointActivity extends AppCompatActivity {

    final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
    RecyclerView recyclerView;
    appointAdapter adapter;
    List<appoint> appointList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoint);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        appointList = new ArrayList<>();

        loadAppointment();

    }

    private void loadAppointment() {

        DatabaseReference databaseRefUser = FirebaseDatabase.getInstance().getReference().child("Users").child(currentFirebaseUser.getUid()).child("Appointment");
        databaseRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
            List<String> appointID = new ArrayList<>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dSnapshot : dataSnapshot.getChildren()) {
                    appointID.add(dSnapshot.getKey());

                }
                //   Toast.makeText(AppointActivity.this,appointID,Toast.LENGTH_SHORT).show();
                for (int j=0;j<appointID.size();j++) {
                    DatabaseReference databaseRefappoint = FirebaseDatabase.getInstance().getReference().child("Appointments").child(appointID.get(j));
                    final List<String> appointlist = new ArrayList<>();
                    databaseRefappoint.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int i = 0;
                            for (DataSnapshot dSnapshot : dataSnapshot.getChildren()) {

                                if (i == 0) {
                                    appointlist.add(dSnapshot.getValue().toString());

                                }
                                if (i == 1) {
                                    appointlist.add(dSnapshot.getValue().toString());

                                }


                                if (i == 5) {
                                    appointlist.add(dSnapshot.getValue().toString());


                                }

                                if (i == 6) {
                                    appointlist.add(dSnapshot.getValue().toString());


                                }


                                i++;

                            }
                            appoint app = new appoint(
                                    appointlist.get(0),
                                    appointlist.get(1),
                                    appointlist.get(2),
                                    appointlist.get(3)
                            );

                            appointList.add(app);
                            adapter = new appointAdapter(appointList, getApplicationContext());
                            recyclerView.setAdapter(adapter);
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
    }

