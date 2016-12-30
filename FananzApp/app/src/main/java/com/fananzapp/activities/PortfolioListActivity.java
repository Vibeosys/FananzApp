package com.fananzapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.fananzapp.R;

public class PortfolioListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio_list);
    }

    public void addPortFolio(View v) {
        Intent iLogin = new Intent(getApplicationContext(), AddPortfolioActivity.class);
        startActivity(iLogin);
    }
}
