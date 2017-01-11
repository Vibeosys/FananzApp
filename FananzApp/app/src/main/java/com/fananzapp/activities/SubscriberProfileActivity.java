package com.fananzapp.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.fananzapp.R;
import com.fananzapp.utils.ServerSyncManager;
import com.fananzapp.utils.SubscriberType;

public class SubscriberProfileActivity extends BaseActivity implements View.OnClickListener,
        ServerSyncManager.OnErrorResultReceived, ServerSyncManager.OnSuccessResultReceived {

    private EditText edtName, edtEmail, edtPass, edtTel, edtMo, edtWeb, edtNick;
    private LinearLayout layNickName;
    private TextView txtSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriber_profile);
        edtName = (EditText) findViewById(R.id.edt_name);
        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtPass = (EditText) findViewById(R.id.edt_pass);
        edtTel = (EditText) findViewById(R.id.edt_telephone);
        edtMo = (EditText) findViewById(R.id.edt_mobile);
        edtWeb = (EditText) findViewById(R.id.edt_web_site);
        edtNick = (EditText) findViewById(R.id.edt_nic_name);
        txtSave = (TextView) findViewById(R.id.btn_save);
        layNickName = (LinearLayout) findViewById(R.id.nick_view);
        mServerSyncManager.setOnStringResultReceived(this);
        mServerSyncManager.setOnStringErrorReceived(this);
        txtSave.setOnClickListener(this);

        String subType = mSessionManager.getSType();
        if (subType.equals(SubscriberType.TYPE_CORPORATE)) {
            layNickName.setVisibility(View.GONE);
        } else {
            layNickName.setVisibility(View.VISIBLE);
            edtNick.setText(mSessionManager.getNickName());
        }
        edtName.setText(mSessionManager.getName());
        edtEmail.setText(mSessionManager.getEmail());
        edtEmail.setEnabled(false);
        edtPass.setText(mSessionManager.getPassword());
        // edtTel.setText(mSessionManager.getT);

    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {

    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {

    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_save:
                updateProfile();
                break;
        }
    }

    private void updateProfile() {

    }
}
