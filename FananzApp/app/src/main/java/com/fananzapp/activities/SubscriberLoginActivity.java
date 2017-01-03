package com.fananzapp.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.fananzapp.R;
import com.fananzapp.data.requestdata.BaseRequestDTO;
import com.fananzapp.data.requestdata.SigninSubReqDTO;
import com.fananzapp.fragments.CorporateRegFragment;
import com.fananzapp.utils.ServerRequestToken;
import com.fananzapp.utils.ServerSyncManager;
import com.google.gson.Gson;

public class SubscriberLoginActivity extends BaseActivity implements View.OnClickListener,
        ServerSyncManager.OnSuccessResultReceived, ServerSyncManager.OnErrorResultReceived {

    private static final String TAG = SubscriberLoginActivity.class.getSimpleName();
    private EditText edtEmail, edtPass;
    Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriber_login);
        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtPass = (EditText) findViewById(R.id.edt_pass);
        btnSignIn = (Button) findViewById(R.id.btn_sign_in);
        btnSignIn.setOnClickListener(this);
        mServerSyncManager.setOnStringResultReceived(this);
        mServerSyncManager.setOnStringErrorReceived(this);
    }

    public void registerNow(View v) {
        startActivity(new Intent(this, SubscriberRegisterActivity.class));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_sign_in:
                loginUser();
                break;
        }
    }

    private void loginUser() {
        View focusView = null;
        boolean check = false;
        String email = edtEmail.getText().toString();
        String password = edtPass.getText().toString();
        if (email.isEmpty()) {
            check = true;
            focusView = edtEmail;
            edtEmail.setError(getString(R.string.str_email_empty));
        } else if (password.isEmpty()) {
            check = true;
            focusView = edtPass;
            edtPass.setError(getString(R.string.str_pass_empty));
        }
        if (check) {
            focusView.requestFocus();
        } else {
            progressDialog.show();
            SigninSubReqDTO signinSubReqDTO = new SigninSubReqDTO(email, password);
            Gson gson = new Gson();
            String serializedJsonString = gson.toJson(signinSubReqDTO);
            BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
            baseRequestDTO.setData(serializedJsonString);
            mServerSyncManager.uploadDataToServer(ServerRequestToken.REQUEST_SIGN_IN_SUB,
                    mSessionManager.addSignInSubUrl(), baseRequestDTO);
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
            case ServerRequestToken.REQUEST_SIGN_IN_SUB:
                Log.d(TAG, "## Success Login");
                break;
        }
    }
}
