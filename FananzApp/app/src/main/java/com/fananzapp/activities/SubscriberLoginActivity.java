package com.fananzapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.fananzapp.R;
import com.fananzapp.fragments.CorporateRegFragment;

public class SubscriberLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriber_login);

    }

    public void registerNow(View v) {
        startActivity(new Intent(this, SubscriberRegisterActivity.class));
    }
}
