package com.fananzapp.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by akshay on 22-06-2016.
 */
public class PropertyFileReader {

    private static PropertyFileReader mPropertyFileReader = null;
    private static Context mContext;
    protected static Properties mProperties;

    public static PropertyFileReader getInstance(Context context) {
        if (mPropertyFileReader != null)
            return mPropertyFileReader;

        mContext = context;
        mProperties = getProperties();
        mPropertyFileReader = new PropertyFileReader();
        return mPropertyFileReader;
    }

    protected static Properties getProperties() {
        try {
            AssetManager assetManager = mContext.getAssets();
            InputStream inputStream = assetManager.open("app.properties");
            mProperties = new Properties();
            mProperties.load(inputStream);

        } catch (Exception e) {
            System.out.print(e.getMessage());
        }

        return mProperties;
    }

    protected String getEndPointUri() {
        return mProperties.getProperty(PropertyTypeConstants.API_ENDPOINT_URI);
    }

    public Float getVersion() {
        String versionNumber = mProperties.getProperty(PropertyTypeConstants.VERSION_NUMBER);
        return Float.valueOf(versionNumber);
    }

    public String getPortfolioList() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.PORTFOLIO_LIST);
    }

    public String getCategoryList() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.CATEGORY_LIST);
    }

    public String getAddSubscriber() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.ADD_SUBSCRIBER);
    }

    public String getSignInSub() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.SIGN_IN_SUBSCRIBER);
    }

    public String addPortfolioUrl() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.ADD_PORTFOLIO);
    }

    public String getPortfolioDetailsUrl() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.PORTFOLIO_DETAILS);
    }


    public String getSubPortfolioListUrl() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.SUB_PORTFOLIO_LIST);
    }

    public String getSigninUserUrl() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.SIGN_IN_USER);
    }

    public String addUserUrl() {
        return getEndPointUri() + mProperties.getProperty(PropertyTypeConstants.ADD_USER_URL);
    }
}
