package com.fananzapp.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.VolleyError;
import com.fananzapp.R;
import com.fananzapp.adapters.CategorySpinnerAdapter;
import com.fananzapp.adapters.SubcategorySpinnerAdapter;
import com.fananzapp.data.requestdata.AddPortfolioDataReqDTO;
import com.fananzapp.data.requestdata.BaseRequestDTO;
import com.fananzapp.data.requestdata.SigninSubReqDTO;
import com.fananzapp.data.responsedata.CategoryResponseDTO;
import com.fananzapp.data.responsedata.SubcategoryDTO;
import com.fananzapp.utils.ServerRequestToken;
import com.fananzapp.utils.ServerSyncManager;
import com.google.gson.Gson;

import java.util.ArrayList;

public class AddPortfolioDataActivity extends BaseActivity implements ServerSyncManager.OnErrorResultReceived,
        ServerSyncManager.OnSuccessResultReceived, View.OnClickListener {

    private static final String TAG = AddPortfolioDataActivity.class.getSimpleName();
    private ArrayList<CategoryResponseDTO> categoryResponseDTOs = new ArrayList<>();
    private Spinner spnCategory, spnSubCategory;
    private CategorySpinnerAdapter categorySpinnerAdapter;
    private SubcategorySpinnerAdapter subcategorySpinnerAdapter;
    private EditText edtFbLink, edtYouLink, edtMinPrice, edtMaxPrice, edtDetails;
    private Button btnNext;
    private int categoryId = 0;
    private int subCategoryId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_portfolio_data);
        spnCategory = (Spinner) findViewById(R.id.spnCategory);
        spnSubCategory = (Spinner) findViewById(R.id.spnSubCategory);
        edtFbLink = (EditText) findViewById(R.id.edt_fb_link);
        edtYouLink = (EditText) findViewById(R.id.edt_you_link);
        edtMinPrice = (EditText) findViewById(R.id.edt_min_price);
        edtMaxPrice = (EditText) findViewById(R.id.edt_max_price);
        edtDetails = (EditText) findViewById(R.id.edt_details);
        btnNext = (Button) findViewById(R.id.btn_next);

        btnNext.setOnClickListener(this);
        progressDialog.show();
        mServerSyncManager.uploadGetDataToServer(ServerRequestToken.REQUEST_CATEGORY_TOKEN, mSessionManager.getCategoryListUrl());
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);

        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                CategoryResponseDTO categoryResponseDTO = (CategoryResponseDTO) categorySpinnerAdapter.getItem(position);
                categoryId = categoryResponseDTO.getCategoryId();
                if (categoryResponseDTO.getSubCategories() != null) {
                    subcategorySpinnerAdapter = new SubcategorySpinnerAdapter(getApplicationContext(),
                            categoryResponseDTO.getSubCategories());
                    spnSubCategory.setAdapter(subcategorySpinnerAdapter);
                    spnSubCategory.setVisibility(View.VISIBLE);
                } else {
                    spnSubCategory.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spnSubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                SubcategoryDTO subcategoryDTO = (SubcategoryDTO) subcategorySpinnerAdapter.getItem(position);
                subCategoryId = subcategoryDTO.getSubCategoryId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
            case ServerRequestToken.REQUEST_CATEGORY_TOKEN:
                categoryResponseDTOs = CategoryResponseDTO.deserializeToArray(data);
                categorySpinnerAdapter = new CategorySpinnerAdapter(getApplicationContext(), categoryResponseDTOs);
                spnCategory.setAdapter(categorySpinnerAdapter);
                Log.d(TAG, "##" + categoryResponseDTOs);
                break;
            case ServerRequestToken.REQUEST_ADD_PORTFOLIO:
                Log.d(TAG, "##" + data);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_next:
                addPortfolio();
                break;
        }
    }

    private void addPortfolio() {
        String strFbLink = edtFbLink.getText().toString();
        String strYouLink = edtYouLink.getText().toString();

        int minPrice = 0;
        try {
            minPrice = Integer.parseInt(edtMinPrice.getText().toString());
        } catch (Exception e) {
            minPrice = 0;
        }
        int maxPrice = 0;
        try {
            maxPrice = Integer.parseInt(edtMaxPrice.getText().toString());
        } catch (Exception e) {
            maxPrice = 0;
        }

        String strDetails = edtDetails.getText().toString();

        View focusView = null;
        boolean check = false;
        if (strFbLink.isEmpty()) {
            focusView = edtFbLink;
            check = true;
            edtFbLink.setError(getString(R.string.str_fb_empty));
        } else if (strYouLink.isEmpty()) {
            focusView = edtYouLink;
            check = true;
            edtYouLink.setError(getString(R.string.str_you_empty));
        } else if (minPrice == 0) {
            focusView = edtMinPrice;
            check = true;
            edtMinPrice.setError(getString(R.string.str_min_price_error));
        } else if (maxPrice == 0) {
            focusView = edtMaxPrice;
            check = true;
            edtMaxPrice.setError(getString(R.string.str_max_price_error));
        } else if (maxPrice < minPrice) {
            focusView = edtMaxPrice;
            check = true;
            edtMaxPrice.setError(getString(R.string.str_max_price_greater_error));
        } else if (strDetails.isEmpty()) {
            focusView = edtDetails;
            check = true;
            edtDetails.setError(getString(R.string.str_portfolio_details_empty));
        }

        if (check) {
            focusView.requestFocus();
        } else {
            progressDialog.show();
            AddPortfolioDataReqDTO addPortfolioDataReqDTO = new AddPortfolioDataReqDTO(categoryId, subCategoryId,
                    strFbLink, strYouLink, strDetails, minPrice, maxPrice);
            Gson gson = new Gson();
            String serializedJsonString = gson.toJson(addPortfolioDataReqDTO);
            BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
            baseRequestDTO.setData(serializedJsonString);
            mServerSyncManager.uploadDataToServer(ServerRequestToken.REQUEST_ADD_PORTFOLIO,
                    mSessionManager.addPortfolioUrl(), baseRequestDTO);
        }
    }
}
