package com.fananzapp.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.fananzapp.R;
import com.fananzapp.data.requestdata.BaseRequestDTO;
import com.fananzapp.data.requestdata.RegisterSubscriberReq;
import com.fananzapp.utils.ServerRequestToken;
import com.fananzapp.utils.ServerSyncManager;
import com.fananzapp.utils.SubscriberType;
import com.google.gson.Gson;

/**
 * Created by akshay on 29-12-2016.
 */
public class CorporateRegFragment extends BaseFragment implements View.OnClickListener,
        ServerSyncManager.OnErrorResultReceived, ServerSyncManager.OnSuccessResultReceived {
    private TextView mTxtTerms;
    private static final String TAG = CorporateRegFragment.class.getSimpleName();
    private EditText edtBusinessName, edtContactPerson, edtEmail, edtPh, edtMob, edtWeb, edtCountry, edtPass;
    private Button btnSubmit;
    private CheckBox checkBox;
    private boolean checkTerms = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_corporate_reg, container, false);
        mTxtTerms = (TextView) view.findViewById(R.id.txt_terms);
        edtBusinessName = (EditText) view.findViewById(R.id.edt_business_name);
        edtContactPerson = (EditText) view.findViewById(R.id.edt_represet_name);
        edtEmail = (EditText) view.findViewById(R.id.edt_email);
        edtPass = (EditText) view.findViewById(R.id.edt_pass);
        edtPh = (EditText) view.findViewById(R.id.edt_phone);
        edtMob = (EditText) view.findViewById(R.id.edt_mob_no);
        edtWeb = (EditText) view.findViewById(R.id.edt_web_site);
        edtCountry = (EditText) view.findViewById(R.id.edt_country_of_residence);
        checkBox = (CheckBox) view.findViewById(R.id.chk_terms);
        btnSubmit = (Button) view.findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(this);
        mServerSyncManager.setOnStringResultReceived(this);
        mServerSyncManager.setOnStringErrorReceived(this);
        mTxtTerms.setMovementMethod(LinkMovementMethod.getInstance());
        SpannableStringBuilder ssWebsite = new SpannableStringBuilder(getString(R.string.str_user_agreement));
        ssWebsite.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                showTermsDialog();
            }
        }, 11, 30, 0);
        mTxtTerms.setText(ssWebsite, TextView.BufferType.SPANNABLE);
        return view;

    }

    private void showTermsDialog() {
        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_show_terms);
        WebView txtTerms = (WebView) dialog.findViewById(R.id.txt_terms_and_conditions);
        txtTerms.loadUrl("file:///android_res/raw/term.html");
        txtTerms.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }
        });
        Button btnClose = (Button) dialog.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_submit:
                registerUser();
                break;
        }
    }

    private void registerUser() {

        String strBusinessName = edtBusinessName.getText().toString();
        String strContactPerson = edtContactPerson.getText().toString();
        String strPassword = edtPass.getText().toString();
        String strEmail = edtEmail.getText().toString();
        String strPhone = edtPh.getText().toString();
        String strMob = edtMob.getText().toString();
        String strWeb = edtWeb.getText().toString();
        String strCountry = edtCountry.getText().toString();

        View focusView = null;
        boolean check = false;
        if (strBusinessName.isEmpty()) {
            edtBusinessName.setError(getString(R.string.str_name_empty_error));
            focusView = edtBusinessName;
            check = true;
        } else if (strEmail.isEmpty()) {
            focusView = edtEmail;
            check = true;
            edtEmail.setError(getString(R.string.str_email_empty));
        } else if (strPassword.isEmpty()) {
            focusView = edtPass;
            check = true;
            edtPass.setError(getString(R.string.str_pass_empty));
        } else if (strMob.isEmpty()) {
            focusView = edtMob;
            check = true;
            edtMob.setError(getString(R.string.str_mob_empty));
        } else if (checkTerms) {
            focusView = checkBox;
            check = true;
            checkBox.setError(getString(R.string.str_check_box_not));
        }
        if (check) {
            focusView.requestFocus();
        } else {
            progressDialog.show();
            RegisterSubscriberReq subscriberReq = new RegisterSubscriberReq(strBusinessName, strEmail, strPassword, strContactPerson
                    , SubscriberType.TYPE_CORPORATE, strPhone, strMob, strWeb, strCountry, "");
            Gson gson = new Gson();
            String serializedJsonString = gson.toJson(subscriberReq);
            BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
            baseRequestDTO.setData(serializedJsonString);
            mServerSyncManager.uploadDataToServer(ServerRequestToken.REQUEST_ADD_SUBSCRIBER,
                    mSessionManager.addSubsriberUrl(), baseRequestDTO);
        }

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
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestToken.REQUEST_ADD_SUBSCRIBER:
                Log.d(TAG, "## Success Register");
                break;
        }

    }
}
