package com.example.myecommercesystem;

import android.app.Application;

public class Context extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }
}
