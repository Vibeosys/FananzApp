package com.fananzapp.data.responsedata;

import android.util.Log;

import com.fananzapp.data.BaseDTO;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.util.ArrayList;

/**
 * Created by akshay on 03-01-2017.
 */
public class PortfolioDetailsResDTO extends BaseDTO {

    private static final String TAG = PortfolioDetailsResDTO.class.getSimpleName();
    private int portfolioId;
    private int categoryId;
    private int subCategoryId;
    private String category;
    private String subCategory;
    private long subscriberId;
    private String subscriberName;
    private double minPrice;
    private double maxPrice;
    private String fbLink;
    private String youtubeLink;
    private String aboutUs;
    private String coverImageUrl;
    private String[] photos;

    public PortfolioDetailsResDTO() {
    }

    public int getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(int portfolioId) {
        this.portfolioId = portfolioId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(int subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public long getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(long subscriberId) {
        this.subscriberId = subscriberId;
    }

    public String getSubscriberName() {
        return subscriberName;
    }

    public void setSubscriberName(String subscriberName) {
        this.subscriberName = subscriberName;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getFbLink() {
        return fbLink;
    }

    public void setFbLink(String fbLink) {
        this.fbLink = fbLink;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public String getAboutUs() {
        return aboutUs;
    }

    public void setAboutUs(String aboutUs) {
        this.aboutUs = aboutUs;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String[] getPhotos() {
        return photos;
    }

    public void setPhotos(String[] photos) {
        this.photos = photos;
    }

    public static PortfolioDetailsResDTO deserializeJson(String serializedString) {
        Gson gson = new Gson();
        PortfolioDetailsResDTO responseDTO = null;
        try {
            responseDTO = gson.fromJson(serializedString, PortfolioDetailsResDTO.class);
        } catch (JsonParseException e) {
            Log.d(TAG, "Exception in deserialization PortfolioDetailsResDTO" + e.toString());
        }
        return responseDTO;
    }
}
