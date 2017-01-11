package com.fananzapp.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.FrameLayout;

import com.android.volley.VolleyError;
import com.fananzapp.R;
import com.fananzapp.data.requestdata.BaseRequestDTO;
import com.fananzapp.data.requestdata.PortfolioPhotosReqDTO;
import com.fananzapp.data.responsedata.ImageDataReqDTO;
import com.fananzapp.data.responsedata.PortfolioResponse;
import com.fananzapp.fragments.UploadImageFragment;
import com.fananzapp.utils.ServerRequestToken;
import com.fananzapp.utils.ServerSyncManager;
import com.fananzapp.views.ImageUploadView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;

public class AddPortfolioPhotosActivity extends BaseActivity implements
        ServerSyncManager.OnSuccessResultReceived, ServerSyncManager.OnErrorResultReceived {
    public static final String TAG = AddPortfolioPhotosActivity.class.getSimpleName();
    public static final String PORTFOLIO_ID = "portfolioId";
    private boolean isPhotoAvail;
    FrameLayout frameCover, frameImg1, frameImg2, frameImg3;
    private long portfolioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_portfolio_photos);
        setTitle(getString(R.string.str_add_portfolio_title));
        frameCover = (FrameLayout) findViewById(R.id.frame_cover);
        frameImg1 = (FrameLayout) findViewById(R.id.frame_img1);
        frameImg2 = (FrameLayout) findViewById(R.id.frame_img2);
        frameImg3 = (FrameLayout) findViewById(R.id.frame_img3);
        portfolioId = getIntent().getExtras().getLong(PORTFOLIO_ID);
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);
        callPhotosFromServer();

        /*UploadImageFragment upl = new UploadImageFragment();
        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_frame_lay, upl, "Upload").commit();
        UploadImageFragment upl1 = new UploadImageFragment();
        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_frame_lay1, upl1, "Upload").commit();*/
    }

    private void callPhotosFromServer() {
        progressDialog.show();
        PortfolioPhotosReqDTO portfolioPhotosReqDTO = new PortfolioPhotosReqDTO(portfolioId);
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(portfolioPhotosReqDTO);
        BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
        baseRequestDTO.setData(serializedJsonString);
        mServerSyncManager.uploadDataToServer(ServerRequestToken.REQUEST_PORT_PHOTOS,
                mSessionManager.getPhotosUrl(), baseRequestDTO);
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
            case ServerRequestToken.REQUEST_PORT_PHOTOS:
                setUpUi(null);
                break;
        }
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestToken.REQUEST_PORT_PHOTOS:
                ArrayList<ImageDataReqDTO> images = ImageDataReqDTO.deserializeToArray(data);
                setUpUi(images);
                break;
        }
    }

    public void setUpUi(ArrayList<ImageDataReqDTO> images) {

        if (images != null) {
            Collections.sort(images, Collections.reverseOrder(new ImageDataReqDTO.IsCoverComparator()));

            UploadImageFragment coverImage = new UploadImageFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(UploadImageFragment.IMAGE_DATA, images.get(0));
            bundle.putLong(UploadImageFragment.PORTFOLIO_ID, portfolioId);
            coverImage.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.frame_cover, coverImage, "Upload").commit();
            if (images.size() >= 1) {
                UploadImageFragment image1 = new UploadImageFragment();
                Bundle bundleImg1 = new Bundle();
                bundleImg1.putSerializable(UploadImageFragment.IMAGE_DATA, images.get(1));
                bundle.putLong(UploadImageFragment.PORTFOLIO_ID, portfolioId);
                image1.setArguments(bundleImg1);
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.frame_img1, image1, "img1").commit();

            } else {
                UploadImageFragment image2 = new UploadImageFragment();
                Bundle bundleImg2 = new Bundle();
                bundleImg2.putLong(UploadImageFragment.PORTFOLIO_ID, portfolioId);
                bundleImg2.putBoolean(UploadImageFragment.IS_NEW_DATA, true);
                bundleImg2.putBoolean(UploadImageFragment.IS_COVER_IMG, false);
                image2.setArguments(bundleImg2);
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.frame_img2, image2, "img2").commit();

                UploadImageFragment image3 = new UploadImageFragment();
                Bundle bundleImg3 = new Bundle();
                bundleImg3.putLong(UploadImageFragment.PORTFOLIO_ID, portfolioId);
                bundleImg3.putBoolean(UploadImageFragment.IS_NEW_DATA, true);
                bundleImg3.putBoolean(UploadImageFragment.IS_COVER_IMG, false);
                image3.setArguments(bundleImg3);
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.frame_img3, image3, "img3").commit();
            }
            if (images.size() >= 2) {
                UploadImageFragment image2 = new UploadImageFragment();
                Bundle bundleImg2 = new Bundle();
                bundleImg2.putSerializable(UploadImageFragment.IMAGE_DATA, images.get(2));
                bundle.putLong(UploadImageFragment.PORTFOLIO_ID, portfolioId);
                image2.setArguments(bundleImg2);
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.frame_img2, image2, "img2").commit();

            } else {
                UploadImageFragment image3 = new UploadImageFragment();
                Bundle bundleImg3 = new Bundle();
                bundleImg3.putLong(UploadImageFragment.PORTFOLIO_ID, portfolioId);
                bundleImg3.putBoolean(UploadImageFragment.IS_NEW_DATA, true);
                bundleImg3.putBoolean(UploadImageFragment.IS_COVER_IMG, false);
                image3.setArguments(bundleImg3);
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.frame_img3, image3, "img3").commit();
            }
            if (images.size() > 3) {
                UploadImageFragment image3 = new UploadImageFragment();
                Bundle bundleImg3 = new Bundle();
                bundleImg3.putSerializable(UploadImageFragment.IMAGE_DATA, images.get(3));
                bundle.putLong(UploadImageFragment.PORTFOLIO_ID, portfolioId);
                image3.setArguments(bundleImg3);
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.frame_img3, image3, "img3").commit();
            }


        } else {
            UploadImageFragment coverImage = new UploadImageFragment();
            Bundle bundle = new Bundle();
            bundle.putLong(UploadImageFragment.PORTFOLIO_ID, portfolioId);
            bundle.putBoolean(UploadImageFragment.IS_NEW_DATA, true);
            bundle.putBoolean(UploadImageFragment.IS_COVER_IMG, true);
            coverImage.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.frame_cover, coverImage, "Upload").commit();
            UploadImageFragment image1 = new UploadImageFragment();

            Bundle bundleImg1 = new Bundle();
            bundleImg1.putLong(UploadImageFragment.PORTFOLIO_ID, portfolioId);
            bundleImg1.putBoolean(UploadImageFragment.IS_NEW_DATA, true);
            bundleImg1.putBoolean(UploadImageFragment.IS_COVER_IMG, false);
            image1.setArguments(bundleImg1);
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.frame_img1, image1, "img1").commit();

            UploadImageFragment image2 = new UploadImageFragment();
            Bundle bundleImg2 = new Bundle();
            bundleImg2.putLong(UploadImageFragment.PORTFOLIO_ID, portfolioId);
            bundleImg2.putBoolean(UploadImageFragment.IS_NEW_DATA, true);
            bundleImg2.putBoolean(UploadImageFragment.IS_COVER_IMG, false);
            image2.setArguments(bundleImg2);
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.frame_img2, image2, "img2").commit();

            UploadImageFragment image3 = new UploadImageFragment();
            Bundle bundleImg3 = new Bundle();
            bundleImg3.putLong(UploadImageFragment.PORTFOLIO_ID, portfolioId);
            bundleImg3.putBoolean(UploadImageFragment.IS_NEW_DATA, true);
            bundleImg3.putBoolean(UploadImageFragment.IS_COVER_IMG, false);
            image3.setArguments(bundleImg3);
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.frame_img3, image3, "img3").commit();
        }

    }
}
