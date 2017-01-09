package com.fananzapp.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.fananzapp.R;
import com.fananzapp.adapters.SubPortfolioAdapter;
import com.fananzapp.data.requestdata.BaseRequestDTO;
import com.fananzapp.data.responsedata.PortfolioResponse;
import com.fananzapp.utils.ServerRequestToken;
import com.fananzapp.utils.ServerSyncManager;
import com.fananzapp.utils.SubscriberType;
import com.fananzapp.utils.UserType;

import java.util.ArrayList;

public class PortfolioListActivity extends BaseActivity implements ServerSyncManager.OnSuccessResultReceived,
        ServerSyncManager.OnErrorResultReceived, SubPortfolioAdapter.OnItemClick {

    private static final String TAG = PortfolioListActivity.class.getSimpleName();
    private ListView listPortfolio;
    private SubPortfolioAdapter adapter;
    private ArrayList<PortfolioResponse> portfolioResponses = new ArrayList<>();
    private LinearLayout addPortfolioLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio_list);
        listPortfolio = (ListView) findViewById(R.id.listPortfolio);
        addPortfolioLay = (LinearLayout) findViewById(R.id.addPortfolioLay);
        progressDialog.show();
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);
        BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
        if (mSessionManager.getSType().equals(SubscriberType.TYPE_FREELANCER)) {
            addPortfolioLay.setVisibility(View.GONE);
        }
        mServerSyncManager.uploadDataToServer(ServerRequestToken.REQUEST_SUB_PORTFOLIO_LIST,
                mSessionManager.getSubPortfolioListUrl(), baseRequestDTO);

    }

    public void addPortFolio(View v) {
        Intent addPortfolio = new Intent(getApplicationContext(), AddPortfolioDataActivity.class);
        startActivity(addPortfolio);
    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        progressDialog.dismiss();
        customAlterDialog(getString(R.string.str_server_err_title), getString(R.string.str_server_err_desc));
    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestToken.REQUEST_SUB_PORTFOLIO_LIST:
                customAlterDialog(getString(R.string.str_portfolio_list_err_title), errorMessage);
                if (mSessionManager.getSType().equals(SubscriberType.TYPE_FREELANCER)) {
                    addPortfolioLay.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        Log.d(TAG, data);
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestToken.REQUEST_SUB_PORTFOLIO_LIST:

                portfolioResponses = PortfolioResponse.deserializeToArray(data);
                adapter = new SubPortfolioAdapter(portfolioResponses, getApplicationContext());
                adapter.setOnItemClick(this);
                if (mSessionManager.getSType().equals(SubscriberType.TYPE_FREELANCER)) {
                    if (portfolioResponses.size() > 0) {
                        addPortfolioLay.setVisibility(View.GONE);
                    } else {
                        addPortfolioLay.setVisibility(View.VISIBLE);
                    }
                }
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
        Bundle bundle = new Bundle();
        bundle.putInt(AddPortfolioDataActivity.PORTFOLIO_DETAILS, portfolioResponse.getPortfolioId());
        Intent iAddPort = new Intent(getApplicationContext(), AddPortfolioDataActivity.class);
        iAddPort.putExtra(AddPortfolioDataActivity.PORTFOLIO_DETAILS_BUNDLE, bundle);
        startActivity(iAddPort);
    }
}
