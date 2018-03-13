package com.zibbix.hospital.hospitalmanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

public class FeedbackActivity extends AppCompatActivity {
    Button btn;
    RatingBar ratingBar;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        btn=(Button)findViewById(R.id.feedback_button);
        editText=(EditText)findViewById(R.id.feedbacktxt);
        ratingBar =(RatingBar)findViewById(R.id.ratingBar);
    }

public void BtnClick(View v)
{    float ratingvalue=ratingBar.getRating();
    Toast.makeText(this,"your feedback is "+ratingvalue,Toast.LENGTH_SHORT).show();
    Intent intent = new Intent(FeedbackActivity.this,BookingActivity.class);
    startActivity(intent);
    finish();
}}
