package com.fananzapp;

import android.app.Application;

import com.facebook.FacebookSdk;

/**
 * Created by akshay on 03-01-2017.
 */
public class FananzAppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}
