package com.fananzapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by akshay on 30-12-2016.
 */
public class SessionManager {

    private static final String PROJECT_PREFERENCES = "com.fananzapp";


    private static SessionManager mSessionManager;
    private static SharedPreferences mProjectSharedPref = null;
    private static Context mContext = null;
    private static PropertyFileReader mPropertyFileReader = null;

    public static SessionManager getInstance(Context context) {
        if (mSessionManager != null)
            return mSessionManager;

        mContext = context;
        mPropertyFileReader = mPropertyFileReader.getInstance(context);
        loadProjectSharedPreferences();
        mSessionManager = new SessionManager();

        return mSessionManager;

    }

    public static SessionManager Instance() throws IllegalArgumentException {
        if (mSessionManager != null)
            return mSessionManager;
        else
            throw new IllegalArgumentException("No instance is yet created");
    }

    private static void loadProjectSharedPreferences() {
        if (mProjectSharedPref == null) {
            mProjectSharedPref = mContext.getSharedPreferences(PROJECT_PREFERENCES, Context.MODE_PRIVATE);
        }

        String versionNumber = mProjectSharedPref.getString(PropertyTypeConstants.VERSION_NUMBER, null);
        Float versionNoValue = versionNumber == null ? 0 : Float.valueOf(versionNumber);

        if (mPropertyFileReader.getVersion() > versionNoValue) {
            boolean sharedPrefChange = addOrUdateSharedPreferences();
            if (!sharedPrefChange)
                Log.e("SharedPref", "No shared preferences are changed");
        }
    }

    private static boolean addOrUdateSharedPreferences() {

        SharedPreferences.Editor editor = mProjectSharedPref.edit();
        editor.putString(PropertyTypeConstants.PORTFOLIO_LIST, mPropertyFileReader.getPortfolioList());
        editor.putString(PropertyTypeConstants.CATEGORY_LIST, mPropertyFileReader.getCategoryList());
        editor.putString(PropertyTypeConstants.ADD_SUBSCRIBER, mPropertyFileReader.getAddSubscriber());
        editor.putString(PropertyTypeConstants.SIGN_IN_SUBSCRIBER, mPropertyFileReader.getSignInSub());
        editor.apply();
        return true;
    }

    private SessionManager() {
    }


    private static void setValuesInSharedPrefs(String sharedPrefKey, String sharedPrefValue) {
        SharedPreferences.Editor editor = mProjectSharedPref.edit();
        editor.putString(sharedPrefKey, sharedPrefValue);
        editor.apply();
    }

    private static void setValuesInSharedPrefs(String sharedPrefKey, int sharedPrefValue) {
        SharedPreferences.Editor editor = mProjectSharedPref.edit();
        editor.putInt(sharedPrefKey, sharedPrefValue);
        editor.apply();
    }

    private static void setValuesInSharedPrefs(String sharedPrefKey, long sharedPrefValue) {
        SharedPreferences.Editor editor = mProjectSharedPref.edit();
        editor.putLong(sharedPrefKey, sharedPrefValue);
        editor.apply();
    }

    public String getPortfolioListUrl() {
        return mProjectSharedPref.getString(PropertyTypeConstants.PORTFOLIO_LIST,null);
    }

    public String getCategoryListUrl() {
        return mProjectSharedPref.getString(PropertyTypeConstants.CATEGORY_LIST,null);
    }

    public String addSubsriberUrl() {
        return mProjectSharedPref.getString(PropertyTypeConstants.ADD_SUBSCRIBER,null);
    }

    public String addSignInSubUrl() {
        return mProjectSharedPref.getString(PropertyTypeConstants.SIGN_IN_SUBSCRIBER,null);
    }
}
