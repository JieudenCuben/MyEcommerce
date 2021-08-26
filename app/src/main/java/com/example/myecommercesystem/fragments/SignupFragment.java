package com.example.myecommercesystem.fragments;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myecommercesystem.activities.ConsumerDashboard;
import com.example.myecommercesystem.R;
import com.example.myecommercesystem.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static com.example.myecommercesystem.Utils.store;


public class SignupFragment extends Fragment {

    EditText email,password,nameFull;
    Spinner gender;
    String emailStr,passwordStr,genderSt,fullName;
    private ProgressDialog mDialog;
    String Storage_Path = "All_Image_Uploads/";
    private FirebaseAuth mAuth;

    private DatabaseReference mDatabaseUsers;
    Uri FilePathUri;
    Button signUpBt;
    int Image_Request_Code = 7;
    StorageReference storageReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_signup, container, false);
        email=view.findViewById(R.id.email_signup);
        password=view.findViewById(R.id.password_signup);
        gender=view.findViewById(R.id.gender);
        nameFull=view.findViewById(R.id.full_name_signup);
        signUpBt=view.findViewById(R.id.signup_bt);
        storageReference = FirebaseStorage.getInstance().getReference();

        mDialog = new ProgressDialog(getContext());
        mDialog.setCancelable(false);
        mDialog.setMessage("Signing you in...");
        mAuth = FirebaseAuth.getInstance();

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference();
        mDatabaseUsers.keepSynced(true);
        signUpBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStatusOfEditTexts();
            }
        });
        return view;
    }
    private void checkStatusOfEditTexts() {

        // Getting strings from edit texts

        emailStr = email.getText().toString();
        passwordStr = password.getText().toString();
        genderSt = gender.getSelectedItem().toString();
        fullName=nameFull.getText().toString();


        // Checking if Fields are empty or not
        if (!TextUtils.isEmpty(fullName) && !TextUtils.isEmpty(emailStr) && !TextUtils.isEmpty(passwordStr)) {

            // Checking if passwordStr is equal to confirmed Password


                // Signing up user
                signUpUserWithNameAndPassword();


            // User Name is Empty
        }
        else if (TextUtils.isEmpty(fullName)) {


            email.setError("Please provide your Name");
            email.requestFocus();


            // Password is Empty
        }
        else if (TextUtils.isEmpty(emailStr)) {


            email.setError("Please provide a emailStr");
            email.requestFocus();


            // Password is Empty
        }
        else if (TextUtils.isEmpty(passwordStr)) {

            password.setError("Please provide a passwordStr");
            password.requestFocus();


            // Confirm Password is Empty
        }

    }
    private void signUpUserWithNameAndPassword() {
        mDialog.show();
        if (!Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
            //if Email Address is Invalid..

            mDialog.dismiss();
            email.setError("Please enter a valid email with no spaces and special characters included");
            email.requestFocus();
        } else {

            mAuth.createUserWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        addUserDetailsToDatabase();

                    } else {

                        mDialog.dismiss();
                        Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
//        } else {
//
//            mDialog.dismiss();
//            Toast.makeText(this, "You are not online", Toast.LENGTH_SHORT).show();
//        }
    }
    private void addUserDetailsToDatabase() {
        String accountType= Utils.getString("AccountType");
        DatabaseReference mRef = mDatabaseUsers.child("users").child(mAuth.getCurrentUser().getUid());
        mRef.child("Name").setValue(fullName);
        mRef.child("Email").setValue(emailStr);
        mRef.child("Password").setValue(passwordStr);
        mRef.child("gender").setValue(genderSt);
        mRef.child("Account Type").setValue(accountType);
        store("emailStr", emailStr);
        store("passwordStr", passwordStr);

        mDialog.dismiss();
        if (accountType.equals("Consumer Account")) {
            Intent intent = new Intent(getContext(), ConsumerDashboard.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
        }


        getActivity().

                finish();

        Toast.makeText(

                getContext(), "You are signed up!", Toast.LENGTH_SHORT).

                show();


    }
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getActivity().getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }
}