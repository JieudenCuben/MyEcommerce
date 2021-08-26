package com.example.myecommercesystem.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.example.myecommercesystem.AppointmentsListAdapter;
import com.example.myecommercesystem.AppointmentsListModel;
import com.example.myecommercesystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowAppointmentsActivity extends AppCompatActivity {
    private RecyclerView listRecyclerView;
    private ProgressDialog mDialog;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private RecyclerView.LayoutManager listLayoutManager;
    AppointmentsListAdapter categoryListAdapter;
    ArrayList<AppointmentsListModel> arrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_appointments);

        mDialog = new ProgressDialog(this);
        mDialog.setCancelable(false);
        mDialog.setMessage("Loading...");
        mDialog.show();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Business Data").child("Appointments").child(mAuth.getCurrentUser().getUid());
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot

                        for (DataSnapshot child: dataSnapshot.getChildren()){
                            String name = child.getKey();
                            String Uname= dataSnapshot.child(name).child("Name").getValue().toString();
                            String date= dataSnapshot.child(name).child("DateTime").getValue().toString();
                            String location= dataSnapshot.child(name).child("location").getValue().toString();
                            String value= dataSnapshot.child(name).child("value").getValue().toString();
                            arrayList.add(new AppointmentsListModel(Uname,date,location,name,value));

                        }

                        listRecyclerView = findViewById(R.id.recyclerview_list_appointment);
                        listRecyclerView.setHasFixedSize(true);
                        listLayoutManager = new LinearLayoutManager(ShowAppointmentsActivity.this);
                         categoryListAdapter = new AppointmentsListAdapter(arrayList,ShowAppointmentsActivity.this);
                        listRecyclerView.setAdapter(categoryListAdapter);
                        listRecyclerView.setLayoutManager(listLayoutManager);
                        categoryListAdapter.notifyDataSetChanged();
                        mDialog.dismiss();


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });
    }


}