package com.fananzapp.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Switch;

import com.android.volley.VolleyError;
import com.fananzapp.R;
import com.fananzapp.adapters.PortfolioAdapter;
import com.fananzapp.data.responsedata.PortfolioResponse;
import com.fananzapp.utils.ServerRequestToken;
import com.fananzapp.utils.ServerSyncManager;

import java.util.ArrayList;

public class PortfolioListActivity extends BaseActivity implements ServerSyncManager.OnSuccessResultReceived,
        ServerSyncManager.OnErrorResultReceived, PortfolioAdapter.OnItemClick {

    private static final String TAG = PortfolioListActivity.class.getSimpleName();
    private ListView listPortfolio;
    private PortfolioAdapter adapter;
    private ArrayList<PortfolioResponse> portfolioResponses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio_list);
        listPortfolio = (ListView) findViewById(R.id.listPortfolio);

        progressDialog.show();
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);
        mServerSyncManager.uploadGetDataToServer(ServerRequestToken.REQUEST_PORTFOLIO_LIST,
                mSessionManager.getPortfolioListUrl());
    }

    public void addPortFolio(View v) {
        Intent addPortfolio = new Intent(getApplicationContext(), AddPortfolioDataActivity.class);
        startActivity(addPortfolio);
    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        progressDialog.dismiss();
    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {
        progressDialog.dismiss();
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        Log.d(TAG, data);
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestToken.REQUEST_PORTFOLIO_LIST:
                portfolioResponses = PortfolioResponse.deserializeToArray(data);
                adapter = new PortfolioAdapter(portfolioResponses, getApplicationContext());
                adapter.setOnItemClick(this);
                listPortfolio.setAdapter(adapter);
                break;
        }
    }

    @Override
    public void onInactiveClickListener(PortfolioResponse portfolioResponse, int position) {
        Log.d(TAG, "## Inactive click");
    }

    @Override
    public void onModifyClickListener(PortfolioResponse portfolioResponse, int position) {
        Log.d(TAG, "## Modify click");
    }
}
