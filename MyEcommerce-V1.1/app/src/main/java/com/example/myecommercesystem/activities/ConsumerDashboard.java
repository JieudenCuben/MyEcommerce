package com.example.myecommercesystem.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myecommercesystem.R;
import com.example.myecommercesystem.fragments.HomeFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ConsumerDashboard extends AppCompatActivity {
    private NavigationView navigationView;
    ImageView menuImage;
    TextView showUser;
    private ProgressDialog mDialog;
    private FirebaseAuth mAuth;

    private DatabaseReference mDatabaseUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer_dashboard);

        navigationView=findViewById(R.id.nav_view_consumer);
        View headerView = navigationView.getHeaderView(0);
        showUser =  headerView.findViewById(R.id.header_user);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        mDialog = new ProgressDialog(this);
        mDialog.setCancelable(false);
        mDialog.setMessage("Loading...");


        mDialog.show();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference();
//        if (user != null) {
//            String userEmail = user.getEmail();
////            Toast.makeText(this, ""+userEmail, Toast.LENGTH_SHORT).show();
//            showUser.setText(userEmail);
//        } else {
//            // No user is signed in
//        }
        DatabaseReference mRef = mDatabaseUsers.child("users").child(mAuth.getCurrentUser().getUid());
        mRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        String email = "email", account = "Account", balance = "0";
                        if (dataSnapshot.child("Email").exists())
                            email = dataSnapshot.child("Name").getValue().toString();
                        if (dataSnapshot.child("Account Type").exists())
                            account = dataSnapshot.child("Account Type").getValue().toString();

//            Toast.makeText(this, ""+userEmail, Toast.LENGTH_SHORT).show();
                        showUser.setText(email+"("+account+")");
                        mDialog.dismiss();


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        mDialog.dismiss();
                        //handle databaseError
                    }
                });

        HomeFragment homeFragment=new HomeFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, homeFragment);
        ft.commit();
        menuImage=findViewById(R.id.menuBtn);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        menuImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.update:
                        Intent intent = new Intent(ConsumerDashboard.this, UpdateUserInfoActivity.class);
                        intent.putExtra("activity","consumer");
                        startActivity(intent);
                        drawer.closeDrawer(GravityCompat.START);
                        return true;

                    case R.id.logout:
                        AlertDialog alertDialog = new AlertDialog.Builder(ConsumerDashboard.this)
//set icon
                                .setIcon(android.R.drawable.ic_dialog_alert)
//set title
                                .setTitle("Are you sure to Logout")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //set what would happen when positive button is clicked
                                        FirebaseAuth auth = FirebaseAuth.getInstance();
                                        auth.signOut();

                                        Intent intent = new Intent(ConsumerDashboard.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //set what should happen when negative button is clicked
                                        drawer.closeDrawer(GravityCompat.START);

                                    }
                                })
                                .show();
                        return true;
                    default:
                        return false;
                }
            }
        });

    }
}