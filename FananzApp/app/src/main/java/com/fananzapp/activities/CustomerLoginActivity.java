package com.fananzapp.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.fananzapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class CustomerLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = CustomerLoginActivity.class.getSimpleName();
    private TextView mTxtRegister;
    ImageView fbImg;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);
        mTxtRegister = (TextView) findViewById(R.id.txt_register);
        fbImg = (ImageView) findViewById(R.id.fbImg);
        fbImg.setOnClickListener(this);
        setTitle(getString(R.string.str_sign_in));
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
}
