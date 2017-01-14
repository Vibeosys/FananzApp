package com.fananz.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.fananz.R;
import com.fananz.activities.SubscriberLoginActivity;
import com.fananz.activities.SubscriberRegisterActivity;
import com.fananz.data.requestdata.BaseRequestDTO;
import com.fananz.data.requestdata.RegisterSubscriberReq;
import com.fananz.data.requestdata.UserRequestDTO;
import com.fananz.data.responsedata.InitializePaymentResDTO;
import com.fananz.data.responsedata.RegisterSubResDTO;
import com.fananz.utils.ServerRequestToken;
import com.fananz.utils.ServerSyncManager;
import com.fananz.utils.SubscriberType;
import com.fananz.utils.Validator;
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
    private SubscriberRegisterActivity activity;
    private String email, password;
    private long subId;
    private UserRequestDTO userRequestDTO;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (SubscriberRegisterActivity) getActivity();
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
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                checkTerms = b;
            }
        });
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
        password = edtPass.getText().toString();
        email = edtEmail.getText().toString();
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
        } else if (email.isEmpty()) {
            focusView = edtEmail;
            check = true;
            edtEmail.setError(getString(R.string.str_email_empty));
        } else if (!Validator.isValidMail(email)) {
            focusView = edtEmail;
            check = true;
            edtEmail.setError(getString(R.string.str_email_wrong));
        } else if (password.isEmpty()) {
            focusView = edtPass;
            check = true;
            edtPass.setError(getString(R.string.str_pass_empty));
        } else if (strMob.isEmpty()) {
            focusView = edtMob;
            check = true;
            edtMob.setError(getString(R.string.str_mob_empty));
        } else if (!Validator.isValidPhone(strMob)) {
            focusView = edtMob;
            check = true;
            edtMob.setError(getString(R.string.str_mob_wrong));
        } else if (!strPhone.isEmpty()) {
            if (!Validator.isValidTel(strPhone)) {
                focusView = edtPh;
                check = true;
                edtPh.setError(getString(R.string.str_ph_invalid));
            }
        } else if (!checkTerms) {
            focusView = checkBox;
            check = true;
            checkBox.setError(getString(R.string.str_check_box_not));
        }
        if (check) {
            focusView.requestFocus();
        } else {
            progressDialog.show();
            RegisterSubscriberReq subscriberReq = new RegisterSubscriberReq(strBusinessName, email, password, strContactPerson
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
        switch (requestToken) {
            case ServerRequestToken.REQUEST_ADD_SUBSCRIBER:
                customAlterDialog(getString(R.string.str_server_err_title), getString(R.string.str_server_err_desc));
                break;
            case ServerRequestToken.REQUEST_INITIALIZE_PAYMENT:
                customSubAlterDialog(getString(R.string.str_server_err_title), getString(R.string.str_server_err_desc));
                break;
        }
    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestToken.REQUEST_ADD_SUBSCRIBER:
                customAlterDialog(getString(R.string.str_register_err_title), errorMessage);
                break;
            case ServerRequestToken.REQUEST_INITIALIZE_PAYMENT:
                customSubAlterDialog(getString(R.string.str_subscription_error), getString(R.string.str_subscription_msg));
                break;
        }
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestToken.REQUEST_ADD_SUBSCRIBER:
                RegisterSubResDTO registerSubResDTO = RegisterSubResDTO.deserializeJson(data);
                subId = registerSubResDTO.getSubscriberId();
                showSubDialog();
                Toast.makeText(getContext(), getString(R.string.str_sub_register_success), Toast.LENGTH_SHORT).show();
                break;
            case ServerRequestToken.REQUEST_INITIALIZE_PAYMENT:
                InitializePaymentResDTO paymentResDTO = InitializePaymentResDTO.deserializeJson(data);
                activity.subscribeNow(paymentResDTO, userRequestDTO);
                break;
        }

    }

    public void showSubDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_subscription_user);
        dialog.setTitle(getString(R.string.str_subscribe));
        TextView txtSubDesc = (TextView) dialog.findViewById(R.id.txt_subscription_desc);
        Button btnSubscribeLater = (Button) dialog.findViewById(R.id.btn_subscribe_later);
        Button btnSubscribe = (Button) dialog.findViewById(R.id.btn_subscribe);
        txtSubDesc.setText(getString(R.string.str_corporate_annual_fee));
        btnSubscribeLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent iLogin = new Intent(getContext(), SubscriberLoginActivity.class);
                startActivity(iLogin);
                getActivity().finish();
            }
        });
        btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                progressDialog.show();
                userRequestDTO = new UserRequestDTO(subId, email, password);
                Gson gson = new Gson();
                String serializedJsonString = gson.toJson(userRequestDTO);
                BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
                baseRequestDTO.setUser(serializedJsonString);
                mServerSyncManager.uploadSubToServer(ServerRequestToken.REQUEST_INITIALIZE_PAYMENT,
                        mSessionManager.initPaymentUrl(), baseRequestDTO);
            }
        });
        dialog.show();
    }

    protected void customSubAlterDialog(String title, String message) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("" + title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showSubDialog();
            }
        });
        builder.show();
    }
}
