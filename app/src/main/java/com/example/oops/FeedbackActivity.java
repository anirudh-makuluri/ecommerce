package com.example.oops;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oops.customer.CustomerOrderActivity;

public class FeedbackActivity extends AppCompatActivity {
    private TextView feedbacktxt;
    private RatingBar feedbackratingbar;
    private Button feedbacksubmitbtn;
    private float ratingvalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        feedbacktxt=findViewById(R.id.feedback_text);
        feedbackratingbar=findViewById(R.id.feedback_rating_bar);
        feedbacksubmitbtn=findViewById(R.id.feedback_submit_btn);
        String pname=getIntent().getStringExtra("ref");
        feedbackratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                 ratingvalue=rating;

            }
        });
        feedbacktxt.setText(pname);

        feedbacksubmitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FeedbackActivity.this, "You gave "+ratingvalue+" to "+pname, Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(), CustomerOrderActivity.class);
                startActivity(intent);
            }
        });
    }
}