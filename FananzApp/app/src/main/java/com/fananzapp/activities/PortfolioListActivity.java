package com.fananzapp.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import com.android.volley.VolleyError;
import com.fananzapp.R;
import com.fananzapp.utils.ServerRequestToken;
import com.fananzapp.utils.ServerSyncManager;

public class PortfolioListActivity extends BaseActivity implements ServerSyncManager.OnSuccessResultReceived,
        ServerSyncManager.OnErrorResultReceived {

    private static final String TAG = PortfolioListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio_list);
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);
        mServerSyncManager.uploadGetDataToServer(ServerRequestToken.REQUEST_PORTFOLIO_LIST,
                mSessionManager.getPortfolioListUrl());
    }

    public void addPortFolio(View v) {
        Intent iLogin = new Intent(getApplicationContext(), AddPortfolioActivity.class);
        startActivity(iLogin);
    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {

    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {

    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        Log.d(TAG, data);
    }
}
