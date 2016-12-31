package com.fananzapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.fananzapp.R;
/*import com.paypal.merchant.sdk.PayPalHereSDK;*/

public class SubscriptionActivity extends AppCompatActivity {
    private static final String MID_TIER_URL_FOR_SANDBOX = "http://pph-retail-sdk-sample.herokuapp.com/toPayPal/sandbox";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        /*PayPalHereSDK.init(getApplicationContext(), PayPalHereSDK.Sandbox);*/
    }

    public void subscribeNow(View v) {

    }
}
