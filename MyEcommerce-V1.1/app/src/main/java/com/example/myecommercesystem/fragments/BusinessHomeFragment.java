package com.example.myecommercesystem.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myecommercesystem.BusineesProductsListAdapter;
import com.example.myecommercesystem.BusinessProductsListModel;
import com.example.myecommercesystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class BusinessHomeFragment extends Fragment {

    private RecyclerView listRecyclerView;
    private ProgressDialog mDialog;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private RecyclerView.LayoutManager listLayoutManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.business_fragment_home, container, false);
        ArrayList<BusinessProductsListModel> arrayList = new ArrayList<>();
        mDialog = new ProgressDialog(getContext());
        mDialog.setCancelable(false);
        mDialog.setMessage("Loading...");
        mDialog.show();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Business Data").child("Products").child(mAuth.getCurrentUser().getUid());
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot

                        for (DataSnapshot child: dataSnapshot.getChildren()){
                            String name = child.getKey();
                            String pname= dataSnapshot.child(name).child("product name").getValue().toString();
                            String pdesc= dataSnapshot.child(name).child("product description").getValue().toString();
                            String url= dataSnapshot.child(name).child("url").getValue().toString();
                            arrayList.add(new BusinessProductsListModel(url,pname,pdesc,name));

                        }

                        listRecyclerView = view.findViewById(R.id.recyclerview_list_business);
                        listRecyclerView.setHasFixedSize(true);
                        listLayoutManager = new LinearLayoutManager(getContext());
                        BusineesProductsListAdapter categoryListAdapter = new BusineesProductsListAdapter(arrayList,getContext());
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

        return  view;
    }
}