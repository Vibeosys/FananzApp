package com.fananzapp.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.fananzapp.R;
import com.fananzapp.activities.ArtistDetailsActivity;
import com.fananzapp.adapters.PortfolioAdapter;
import com.fananzapp.adapters.UserPortfolioListAdapter;
import com.fananzapp.data.responsedata.PortfolioResponse;
import com.fananzapp.utils.ServerRequestToken;
import com.fananzapp.utils.ServerSyncManager;

import java.util.ArrayList;

/**
 * Created by akshay on 04-01-2017.
 */
public class PortfolioListFragment extends BaseFragment implements ServerSyncManager.OnSuccessResultReceived,
        ServerSyncManager.OnErrorResultReceived, UserPortfolioListAdapter.OnItemClick {
    private ListView listPortfolio;
    private ArrayList<PortfolioResponse> portfolioResponses = new ArrayList<>();
    private UserPortfolioListAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_portfolio_list, container, false);
        listPortfolio = (ListView) view.findViewById(R.id.listPortfolio);
        progressDialog.show();
        mServerSyncManager.uploadGetDataToServer(ServerRequestToken.REQUEST_PORTFOLIO_LIST,
                mSessionManager.getPortfolioListUrl());
        return view;
    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {

    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {

    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestToken.REQUEST_PORTFOLIO_LIST:
                portfolioResponses = PortfolioResponse.deserializeToArray(data);
                adapter = new UserPortfolioListAdapter(portfolioResponses, getContext());
                adapter.setOnItemClick(this);
                listPortfolio.setAdapter(adapter);
                break;
        }
    }

    @Override
    public void onShowDetailsClickListener(PortfolioResponse portfolioResponse, int position) {
        Intent artistDetails = new Intent(getContext(), ArtistDetailsActivity.class);
        artistDetails.putExtra(ArtistDetailsActivity.PORTFOLIO_ID, portfolioResponse.getPortfolioId());
        startActivity(artistDetails);
    }

    @Override
    public void onRequestNowClickListener(PortfolioResponse portfolioResponse, int position) {
        Dialog dialog = new Dialog(getContext(), android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_request_now);
        dialog.show();
    }
}
