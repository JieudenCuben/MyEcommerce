package com.example.myecommercesystem.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myecommercesystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateUserInfoActivity extends AppCompatActivity {
EditText edName,edCurrentPass,edNewPass;
String name,curPass,newPass,email;
    private ProgressDialog mDialog;
    String activity;
    FirebaseUser user;
    private FirebaseAuth mAuth;

    private DatabaseReference mDatabaseUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);

        edName=findViewById(R.id.ed_your_name_update);
        edCurrentPass=findViewById(R.id.ed_current_password);
        edNewPass=findViewById(R.id.ed_new_Password);
        mDialog = new ProgressDialog(this);
        mDialog.setCancelable(false);
        mDialog.setMessage("Updating...");
         user = FirebaseAuth.getInstance().getCurrentUser();
        email=user.getEmail().toString();
        mAuth = FirebaseAuth.getInstance();

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference();
        Intent intent=getIntent();
        activity=intent.getStringExtra("activity");
    }

    public void updateInfoClick(View view) {

        name = edName.getText().toString();
         curPass= edCurrentPass.getText().toString();
         newPass= edNewPass.getText().toString();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(curPass)&& !TextUtils.isEmpty(curPass)) {
            mDialog.show();

            AuthCredential credential = EmailAuthProvider
                    .getCredential(email, curPass);

// Prompt the user to re-provide their sign-in credentials
            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            DatabaseReference mRef = mDatabaseUsers.child("users").child(user.getUid());
                                            mRef.child("Name").setValue(name);

                                            mDialog.dismiss();
                                            if (activity.equals("business")) {
                                                Intent intent = new Intent(UpdateUserInfoActivity.this, BusinessDashboard.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                            }
                                            else if (activity.equals("consumer")) {
                                                Intent intent = new Intent(UpdateUserInfoActivity.this, ConsumerDashboard.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                            }
                                                Toast.makeText(UpdateUserInfoActivity.this, "Password Updated", Toast.LENGTH_SHORT).show();

                                            } else {
                                            mDialog.dismiss();
                                            Toast.makeText(UpdateUserInfoActivity.this, "Password Not Updated", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                mDialog.dismiss();
                                Toast.makeText(UpdateUserInfoActivity.this, "Some Error Occur", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        } else if (TextUtils.isEmpty(name)) {

            edName.setError("Please enter Name");
            edName.requestFocus();

        }
        else if (TextUtils.isEmpty(curPass)) {

            edCurrentPass.setError("Please enter current password");
            edCurrentPass.requestFocus();

        }
        else if (TextUtils.isEmpty(newPass)) {

            edNewPass.setError("Please enter new password");
            edNewPass.requestFocus();

        }
    }
}