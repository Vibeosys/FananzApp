package com.fananzapp.activities;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.fananzapp.R;
import com.fananzapp.data.requestdata.BaseRequestDTO;
import com.fananzapp.data.requestdata.GetPortfolioDetailReqDTO;
import com.fananzapp.data.responsedata.PortfolioDetailsResDTO;
import com.fananzapp.utils.ServerRequestToken;
import com.fananzapp.utils.ServerSyncManager;
import com.google.gson.Gson;

import java.util.HashMap;

public class ArtistDetailsActivity extends BaseActivity implements
        ServerSyncManager.OnErrorResultReceived, ServerSyncManager.OnSuccessResultReceived {
    public static final String PORTFOLIO_ID = "portfolioId";
    private static final String TAG = ArtistDetailsActivity.class.getSimpleName();
    private SliderLayout mDemoSlider;
    private TextView txtArtistName, txtArtistCategory, txtMinMaxPrice, txtFbLink,
            txtYoutubeLink, txtArtistDetails;
    private TextView btnRequestNow;
    private int portfolioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_details);
        portfolioId = getIntent().getExtras().getInt(PORTFOLIO_ID);
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
        txtArtistName = (TextView) findViewById(R.id.txtArtistName);
        txtArtistCategory = (TextView) findViewById(R.id.txtArtistCategory);
        txtMinMaxPrice = (TextView) findViewById(R.id.txtMinMaxPrice);
        txtFbLink = (TextView) findViewById(R.id.txtFbLink);
        txtYoutubeLink = (TextView) findViewById(R.id.txtYoutubeLink);
        txtArtistDetails = (TextView) findViewById(R.id.txtArtistDetails);
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);
        progressDialog.show();
        GetPortfolioDetailReqDTO getPortfolioReqDTO = new GetPortfolioDetailReqDTO(portfolioId);
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(getPortfolioReqDTO);
        BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
        baseRequestDTO.setData(serializedJsonString);
        mServerSyncManager.uploadDataToServer(ServerRequestToken.REQUEST_DETAILS_PORTFOLIO,
                mSessionManager.getPortfolioDetailUrl(), baseRequestDTO);

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
            case ServerRequestToken.REQUEST_SIGN_IN_SUB:
                customAlterDialog(getString(R.string.str_show_details_err_title), errorMessage);
                break;
        }
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestToken.REQUEST_DETAILS_PORTFOLIO:
                showDetails(data);
                break;
        }
    }

    public void showDetails(String data) {
        PortfolioDetailsResDTO portfolioDetailsResDTO = PortfolioDetailsResDTO.deserializeJson(data);
        txtArtistName.setText(portfolioDetailsResDTO.getSubscriberName());
        txtArtistCategory.setText(portfolioDetailsResDTO.getCategory());
        String maxMinPrice = String.format("%d-%d", portfolioDetailsResDTO.getMinPrice(), portfolioDetailsResDTO.getMaxPrice());
        txtMinMaxPrice.setText(maxMinPrice);
        txtFbLink.setText(portfolioDetailsResDTO.getFbLink());
        txtYoutubeLink.setText(portfolioDetailsResDTO.getYoutubeLink());
        txtArtistDetails.setText(portfolioDetailsResDTO.getAboutUs());
        setTitle(portfolioDetailsResDTO.getSubscriberName());
        try {
            HashMap<String, String> file_maps = new HashMap<String, String>();
            String[] photos = portfolioDetailsResDTO.getPhotos();
            for (int i = 0; i < photos.length; i++) {
                file_maps.put("Image" + i, photos[i]);
            }
            for (String name : file_maps.keySet()) {
                TextSliderView textSliderView = new TextSliderView(this);
                // initialize a SliderLayout
                textSliderView
                        .image(file_maps.get(name))
                        .setScaleType(BaseSliderView.ScaleType.Fit);
                //add your extra information
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle()
                        .putString("extra", name);

                mDemoSlider.addSlider(textSliderView);
            }
        } catch (NullPointerException e) {
            Log.e(TAG, "##Photos not found");
            HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
            for (int i = 0; i < 3; i++) {
                file_maps.put("Image" + i, R.drawable.default_img);
            }
            for (String name : file_maps.keySet()) {
                TextSliderView textSliderView = new TextSliderView(this);
                // initialize a SliderLayout
                textSliderView
                        .image(file_maps.get(name))
                        .setScaleType(BaseSliderView.ScaleType.Fit);

                //add your extra information
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle()
                        .putString("extra", name);

                mDemoSlider.addSlider(textSliderView);
            }

        }

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Fade);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        // mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
    }
}
