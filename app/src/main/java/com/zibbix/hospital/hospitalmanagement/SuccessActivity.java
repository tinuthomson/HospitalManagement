package com.zibbix.hospital.hospitalmanagement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SuccessActivity extends BaseActivity {

    protected NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_success, null, false);
        drawer.addView(contentView, 0);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_booking);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TextView DocName = (TextView)findViewById(R.id.docNamedit);
        final TextView Date = (TextView)findViewById(R.id.datedit);
        final TextView Session = (TextView)findViewById(R.id.session);
        String appointID = getIntent().getExtras().getString("appointID");
        assert appointID != null;

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

                        i++;

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
