package com.example.myecommercesystem.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myecommercesystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BookAppointmentActivity extends AppCompatActivity {
EditText edName,edDateTime,edLocattion;
String name,dateTime,location,businessId;
    private ProgressDialog mDialog;
    private FirebaseAuth mAuth;
    DatabaseReference mDatabaseRef;
    DatabaseReference newref;


    private DatabaseReference mDatabaseUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);
        edName=findViewById(R.id.ed_your_name);
        edDateTime=findViewById(R.id.ed_date_time);
        edLocattion=findViewById(R.id.ed_your_location);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDialog = new ProgressDialog(this);
        mDialog.setCancelable(false);
        mDialog.setMessage("Booking...");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        Intent intent=getIntent();
        businessId=intent.getStringExtra("businessId");
        newref=mDatabaseRef.child("Business Data").child("Appointments").child(businessId);


        mDialog.show();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference();

        DatabaseReference mRef = mDatabaseUsers.child("users").child(mAuth.getCurrentUser().getUid());
        mRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        String name = "name";
                        if (dataSnapshot.child("Name").exists())
                            name = dataSnapshot.child("Name").getValue().toString();

//            Toast.makeText(this, ""+userEmail, Toast.LENGTH_SHORT).show();
                        edName.setText(name);
                        mDialog.dismiss();


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        mDialog.dismiss();
                        //handle databaseError
                    }
                });
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        edDateTime.setText(currentDateandTime);

    }

    public void bookAppointmentClick(View view) {
         name = edName.getText().toString();
         dateTime = edDateTime.getText().toString();
         location = edLocattion.getText().toString();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(dateTime) && !TextUtils.isEmpty(location)) {
            mDialog.show();

            newref=newref.push();
            newref.child("Name").setValue(name);
            newref.child("DateTime").setValue(dateTime);
            newref.child("location").setValue(location);
            newref.child("value").setValue("Not Accepted Yet");
            Intent intent = new Intent(BookAppointmentActivity.this, ConsumerDashboard.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "Book Appointment Successfully", Toast.LENGTH_LONG).show();
            mDialog.dismiss();

        } else if (TextUtils.isEmpty(name)) {

            edName.setError("Name not Empty");
            edName.requestFocus();

        }
        else if (TextUtils.isEmpty(dateTime)) {

            edDateTime.setError("Date and timenot empty");
            edDateTime.requestFocus();

        }
        else if (TextUtils.isEmpty(location)) {

            edLocattion.setError("Enter location");
            edLocattion.requestFocus();

        }
    }
}