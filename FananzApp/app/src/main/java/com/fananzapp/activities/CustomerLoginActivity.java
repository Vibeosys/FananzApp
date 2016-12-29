package com.fananzapp.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fananzapp.R;

public class CustomerLoginActivity extends AppCompatActivity {

    private TextView mTxtRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);
        mTxtRegister = (TextView) findViewById(R.id.txt_register);
        setTitle(getString(R.string.str_sign_in));
    }

    public void registerNow(View v) {
        startActivity(new Intent(this, UserRegisterActivity.class));
    }
}
