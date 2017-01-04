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
        editor.putString(PropertyTypeConstants.ADD_PORTFOLIO, mPropertyFileReader.addPortfolioUrl());
        editor.putString(PropertyTypeConstants.PORTFOLIO_DETAILS, mPropertyFileReader.getPortfolioDetailsUrl());
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

    private static void setValuesInSharedPrefs(String sharedPrefKey, boolean sharedPrefValue) {
        SharedPreferences.Editor editor = mProjectSharedPref.edit();
        editor.putBoolean(sharedPrefKey, sharedPrefValue);
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
        return mProjectSharedPref.getString(PropertyTypeConstants.PORTFOLIO_LIST, null);
    }

    public String getCategoryListUrl() {
        return mProjectSharedPref.getString(PropertyTypeConstants.CATEGORY_LIST, null);
    }

    public String addSubsriberUrl() {
        return mProjectSharedPref.getString(PropertyTypeConstants.ADD_SUBSCRIBER, null);
    }

    public String signInSubUrl() {
        return mProjectSharedPref.getString(PropertyTypeConstants.SIGN_IN_SUBSCRIBER, null);
    }

    public String addPortfolioUrl() {
        return mProjectSharedPref.getString(PropertyTypeConstants.ADD_PORTFOLIO, null);
    }

    public long getSubId() {
        return mProjectSharedPref.getLong(PropertyTypeConstants.USER_SUB_ID, 0);
    }

    public String getEmail() {
        return mProjectSharedPref.getString(PropertyTypeConstants.USER_EMAIL, null);
    }

    public void setSubId(long subId) {
        setValuesInSharedPrefs(PropertyTypeConstants.USER_SUB_ID, subId);
    }

    public void setName(String name) {
        setValuesInSharedPrefs(PropertyTypeConstants.USER_NAME, name);
    }


    public void setNickName(String nickName) {
        setValuesInSharedPrefs(PropertyTypeConstants.USER_NICK_NAME, nickName);
    }

    public void setEmail(String email) {
        setValuesInSharedPrefs(PropertyTypeConstants.USER_EMAIL, email);
    }

    public void setSType(String SType) {
        setValuesInSharedPrefs(PropertyTypeConstants.USER_S_TYPE, SType);
    }

    public void setSubDate(String subDate) {
        setValuesInSharedPrefs(PropertyTypeConstants.USER_SUB_DATE, subDate);
    }

    public void setIsSubscribed(boolean isSubscribed) {
        setValuesInSharedPrefs(PropertyTypeConstants.USER_IS_SUBSCRIBED, isSubscribed);
    }

    public void setPassword(String password) {
        setValuesInSharedPrefs(PropertyTypeConstants.USER_PASSWORD, password);
    }

    public String getPassword() {
        return mProjectSharedPref.getString(PropertyTypeConstants.USER_PASSWORD, null);
    }

    public String getPortfolioDetailUrl() {
        return mProjectSharedPref.getString(PropertyTypeConstants.PORTFOLIO_DETAILS, null);
    }
}
