package com.example.myecommercesystem.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myecommercesystem.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class AddProductActivity extends AppCompatActivity {
    EditText edName,edDesc;
    ImageView imgProduct;
    int Image_Request_Code = 7;
    Uri FilePathUri;
    private ProgressDialog mDialog;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    DatabaseReference mDatabaseRef;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String name,description;
    DatabaseReference newref;
    String Storage_Path = "All_Image_Uploads/";
    public static final String Database_Path = "All_Image_Uploads_Database";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        edDesc=findViewById(R.id.ed_product_desc);
        edName=findViewById(R.id.ed_product_name);
        imgProduct=findViewById(R.id.image_product);
        mDialog = new ProgressDialog(this);
        mDialog.setCancelable(false);
        mDialog.setMessage("Adding...");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
         newref=mDatabaseRef.child("Business Data").child("Products").child(mAuth.getCurrentUser().getUid());

//         mDatabaseRef=mDatabaseRef.child("Videos").child(category1).push();
        // Assign FirebaseStorage instance to storageReference.
        storageReference = FirebaseStorage.getInstance().getReference();

        // Assign FirebaseDatabase instance with root database name.
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);

        imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                // Setting intent type as image to select image from phone storage.
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);

            }
        });
    }

    public void addProductClick(View view) {
        checkStatusOfEditTexts();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {

                // Getting selected image into Bitmap.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);

                // Setting up bitmap selected image into ImageView.
                imgProduct.setImageBitmap(bitmap);

                // After selecting image change choose button above text.


            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }
    public void UploadImageFileToFirebaseStorage() {


        // Checking whether FilePathUri Is empty or not.
        if (FilePathUri != null) {

            mDialog.show();

            // Creating second StorageReference.
            StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            // Getting image name from EditText and store into string variable.


                            // Hiding the progressDialog after done uploading.
                            mDialog.dismiss();
                            storageReference2nd.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUrl = uri;

                                    mDatabaseRef=mDatabaseRef.child("AllProducts").push();
                                    String key=mDatabaseRef.getKey();
//
                                    mDatabaseRef.child("product name").setValue(name);
                                    mDatabaseRef.child("product description").setValue(description);
                                    mDatabaseRef.child("url").setValue(downloadUrl.toString());
                                    mDatabaseRef.child("businessId").setValue(mAuth.getCurrentUser().getUid());

                                    newref=newref.child(key);
                                    newref.child("product name").setValue(name);
                                    newref.child("product description").setValue(description);
                                    newref.child("url").setValue(downloadUrl.toString());
                                    /*Intent intent = new Intent(AddProductActivity.this, BusinessDashboard.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);*/
                                    Toast.makeText(getApplicationContext(), " Add Successfully ", Toast.LENGTH_LONG).show();
                                    //Do what you want with the url
                                }
                            });
                            // Showing toast message after done uploading.

//                            String url=taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
//                            mDatabaseRef=mDatabaseRef.child("Categories").child(TempImageName);
//                            mDatabaseRef.child("url").setValue(url);
//                            @SuppressWarnings("VisibleForTests")
//                            ImageUploadInfo imageUploadInfo = new ImageUploadInfo(TempImageName, taskSnapshot.getDownloadUrl().toString());
//
//                            // Getting image upload ID.
//                            String ImageUploadId = databaseReference.push().getKey();
//
//                            // Adding image upload id s child element into databaseReference.
//                            databaseReference.child(ImageUploadId).setValue(imageUploadInfo);
                        }
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            // Hiding the progressDialog.
                            mDialog.dismiss();

                            // Showing exception erro message.
                            Toast.makeText(AddProductActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })

                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            // Setting progressDialog Title.


                        }
                    });
        }
        else {

            Toast.makeText(AddProductActivity.this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

        }
    }
    private void checkStatusOfEditTexts() {

        // Getting strings from edit texts

        name = edName.getText().toString();
         description= edDesc.getText().toString();



        // Checking if Fields are empty or not
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(description) ) {

            // Checking if passwordStr is equal to confirmed Password


            // Signing up user

            UploadImageFileToFirebaseStorage();

            // User Name is Empty
        }
        else if (TextUtils.isEmpty(name)) {


            edName.setError("Please provide product Name");
            edName.requestFocus();


            // Password is Empty
        }
        else if (TextUtils.isEmpty(description)) {


            edDesc.setError("Please provide a product description");
            edDesc.requestFocus();


            // Password is Empty
        }


    }
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }
}