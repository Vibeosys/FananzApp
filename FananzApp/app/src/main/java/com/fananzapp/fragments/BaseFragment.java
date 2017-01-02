package com.fananzapp.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.fananzapp.utils.DialogUtils;
import com.fananzapp.utils.ServerSyncManager;
import com.fananzapp.utils.SessionManager;

/**
 * Created by akshay on 02-01-2017.
 */
public class BaseFragment extends Fragment {

    protected ServerSyncManager mServerSyncManager = null;
    protected static SessionManager mSessionManager = null;
    protected ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSessionManager = SessionManager.getInstance(getActivity().getApplicationContext());
        mServerSyncManager = new ServerSyncManager(getActivity().getApplicationContext(), mSessionManager);
        progressDialog = DialogUtils.getProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }
}
