package com.example.myecommercesystem.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.myecommercesystem.R;

import static com.example.myecommercesystem.Utils.store;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void consumerClick(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        store("AccountType", "Consumer Account");
        startActivity(intent);

    }

    public void businessClick(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        store("AccountType", "Business Account");
        startActivity(intent);
    }
}