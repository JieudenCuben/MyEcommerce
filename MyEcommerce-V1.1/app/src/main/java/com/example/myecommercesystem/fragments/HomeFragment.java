package com.example.myecommercesystem.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myecommercesystem.AllProductsListAdapter;
import com.example.myecommercesystem.AllProductsListModel;
import com.example.myecommercesystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private RecyclerView listRecyclerView;
    private RecyclerView.LayoutManager listLayoutManager;
    private EditText edSearch;
    AllProductsListAdapter categoryListAdapter;
    ArrayList<AllProductsListModel> arrayList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);

        edSearch=view.findViewById(R.id.ed_search);

        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("AllProducts");
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
                            String uid= dataSnapshot.child(name).child("businessId").getValue().toString();

                            arrayList.add(new AllProductsListModel(url,pname,pdesc,uid));

                        }

                        listRecyclerView = view.findViewById(R.id.recyclerview_list);
                        listRecyclerView.setHasFixedSize(true);
                        listLayoutManager = new GridLayoutManager(getContext(),2);
                         categoryListAdapter = new AllProductsListAdapter(arrayList,getContext());
                        listRecyclerView.setAdapter(categoryListAdapter);
                        listRecyclerView.setLayoutManager(listLayoutManager);
                        categoryListAdapter.notifyDataSetChanged();


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });

        return  view;
    }
    private void filter(String text) {
        ArrayList<AllProductsListModel> filteredList = new ArrayList<>();
        for (AllProductsListModel item : arrayList) {
            if (item.getProduct_name().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        categoryListAdapter.filterList(filteredList);
    }
}