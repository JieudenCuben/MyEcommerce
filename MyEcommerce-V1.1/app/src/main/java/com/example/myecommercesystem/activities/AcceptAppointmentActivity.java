package com.example.myecommercesystem.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myecommercesystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AcceptAppointmentActivity extends AppCompatActivity {
    EditText edName,edDateTime,edLocattion;
    String name,dateTime,location,pushId;
    private ProgressDialog mDialog;
    private FirebaseAuth mAuth;
    DatabaseReference mDatabaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_appointment);
        edName=findViewById(R.id.ed_your_name);
        edDateTime=findViewById(R.id.ed_date_time);
        edLocattion=findViewById(R.id.ed_your_location);
        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mDialog = new ProgressDialog(this);
        mDialog.setCancelable(false);
        mDialog.setMessage("Accepting...");

        Intent intent=getIntent();
        name=intent.getStringExtra("name");
        dateTime=intent.getStringExtra("date");
        location=intent.getStringExtra("location");
        pushId=intent.getStringExtra("push");

        edName.setText(name);
        edDateTime.setText(dateTime);
        edLocattion.setText(location);

    }

    public void acceptAppointmentClick(View view) {
        mDialog.show();

        mDatabaseRef=mDatabaseRef.child("Business Data").child("Appointments").child(mAuth.getUid());
        mDatabaseRef.child(pushId).child("value").setValue("Accepted");
        Intent intent = new Intent(AcceptAppointmentActivity.this, ShowAppointmentsActivity.class);
        startActivity(intent);
        finish();
        mDialog.dismiss();
        Toast.makeText(getApplicationContext(), "Accept Appointment Successfully", Toast.LENGTH_LONG).show();
    }
}