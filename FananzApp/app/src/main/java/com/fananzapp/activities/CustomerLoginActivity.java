package com.fananzapp.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.fananzapp.MainActivity;
import com.fananzapp.R;
import com.fananzapp.data.SubscriberDTO;
import com.fananzapp.data.UserDTO;
import com.fananzapp.data.requestdata.BaseRequestDTO;
import com.fananzapp.data.requestdata.SigninSubReqDTO;
import com.fananzapp.data.requestdata.SigninUserReqDTO;
import com.fananzapp.data.responsedata.SigninSubResDTO;
import com.fananzapp.data.responsedata.SigninUserRespDTO;
import com.fananzapp.utils.ServerRequestToken;
import com.fananzapp.utils.ServerSyncManager;
import com.fananzapp.utils.UserAuth;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class CustomerLoginActivity extends BaseActivity implements View.OnClickListener,
        ServerSyncManager.OnSuccessResultReceived, ServerSyncManager.OnErrorResultReceived {

    private static final String TAG = CustomerLoginActivity.class.getSimpleName();
    private TextView mTxtRegister;
    ImageView fbImg;
    CallbackManager callbackManager;
    private EditText edtEmail, edtPass;
    Button btnSignIn;
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);
        setTitle(getString(R.string.str_sign_in));
        mTxtRegister = (TextView) findViewById(R.id.txt_register);
        fbImg = (ImageView) findViewById(R.id.fbImg);
        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtPass = (EditText) findViewById(R.id.edt_pass);
        btnSignIn = (Button) findViewById(R.id.btn_sign_in);

        btnSignIn.setOnClickListener(this);
        fbImg.setOnClickListener(this);
        mServerSyncManager.setOnStringResultReceived(this);
        mServerSyncManager.setOnStringErrorReceived(this);

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getTheDetails(loginResult);
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "Cancel Login");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e(TAG, error.getMessage());
            }
        });

    }

    public void registerNow(View v) {
        startActivity(new Intent(this, UserRegisterActivity.class));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.fbImg:
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email,public_profile,user_birthday"));
                break;
            case R.id.btn_sign_in:
                loginUser();
                break;
        }
    }

    private void loginUser() {
        View focusView = null;
        boolean check = false;
        email = edtEmail.getText().toString();
        password = edtPass.getText().toString();
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
            SigninUserReqDTO signinUserReqDTO = new SigninUserReqDTO(email, password, 0);
            Gson gson = new Gson();
            String serializedJsonString = gson.toJson(signinUserReqDTO);
            BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
            baseRequestDTO.setData(serializedJsonString);
            mServerSyncManager.uploadDataToServer(ServerRequestToken.REQUEST_SIGN_IN_USER,
                    mSessionManager.signInUserUrl(), baseRequestDTO);
        }
    }

    private void getTheDetails(LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("LoginActivity", "##" + object.toString());
                       /* try {
                            email = object.getString("email");
                            name = object.getString("name");
                            gender = object.getString("gender");
                            AccessToken fbAccessToken = AccessToken.getCurrentAccessToken();
                            token = null;
                            if (fbAccessToken != null) {
                                token = fbAccessToken.getToken();
                            }
                            JSONObject picture = object.getJSONObject("picture");

                            if (picture != null) {
                                JSONObject pictureData = picture.getJSONObject("data");
                                if (pictureData != null) {
                                    profileImg = pictureData.getString("url");
                                }
                            }
                            dob = object.getString("birthday");
                            if (!TextUtils.isEmpty(dob)) {
                                DateUtils dateUtils = new DateUtils();
                                formattedDob = dateUtils.convertFbDateToSwedish(dob);
                            }
                            mSessionManager.setToken(token);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        callToRegister(name, email, gender, profileImg, formattedDob, token);*/
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,birthday,picture.type(large){url,height,width,is_silhouette},gender,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestToken.REQUEST_SIGN_IN_USER:
                customAlterDialog(getString(R.string.str_server_err_title), getString(R.string.str_server_err_desc));
                break;
        }
    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestToken.REQUEST_SIGN_IN_USER:
                customAlterDialog(getString(R.string.str_login_err_title), errorMessage);
                break;
        }
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestToken.REQUEST_SIGN_IN_USER:
                Log.d(TAG, "## Success Login");
                SigninUserRespDTO signinUserRespDTO = SigninUserRespDTO.deserializeJson(data);
                UserDTO subscriberDTO = new UserDTO(signinUserRespDTO.getUserId(),
                        signinUserRespDTO.getFirstName(), signinUserRespDTO.getLastName());
                UserAuth userAuth = new UserAuth();
                userAuth.saveUserInfo(subscriberDTO, getApplicationContext());
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                break;
        }
    }
}
