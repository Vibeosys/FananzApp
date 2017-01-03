package com.fananzapp.data;

/**
 * Created by akshay on 03-01-2017.
 */
public class SubscriberDTO {

    private long mSubscriberId;
    private String mName;
    private String mNickName;
    private String mSType;
    private String mSubscriptionDate;
    private boolean mIsSubscribed;
    private String mEmail;
    private String mPassword;

    public SubscriberDTO(long mSubscriberId, String mName, String mNickName,
                         String mSType, String mSubscriptionDate, boolean mIsSubscribed) {
        this.mSubscriberId = mSubscriberId;
        this.mName = mName;
        this.mNickName = mNickName;
        this.mSType = mSType;
        this.mSubscriptionDate = mSubscriptionDate;
        this.mIsSubscribed = mIsSubscribed;
    }

    public long getSubscriberId() {
        return mSubscriberId;
    }

    public void setSubscriberId(long mSubscriberId) {
        this.mSubscriberId = mSubscriberId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getNickName() {
        return mNickName;
    }

    public void setNickName(String mNickName) {
        this.mNickName = mNickName;
    }

    public String getSType() {
        return mSType;
    }

    public void setSType(String mSType) {
        this.mSType = mSType;
    }

    public String getSubscriptionDate() {
        return mSubscriptionDate;
    }

    public void setSubscriptionDate(String mSubscriptionDate) {
        this.mSubscriptionDate = mSubscriptionDate;
    }

    public boolean isIsSubscribed() {
        return mIsSubscribed;
    }

    public void setIsSubscribed(boolean mIsSubscribed) {
        this.mIsSubscribed = mIsSubscribed;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }
}
