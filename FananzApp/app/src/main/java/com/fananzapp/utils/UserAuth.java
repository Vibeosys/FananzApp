package com.fananzapp.utils;

import android.content.Context;
import android.content.Intent;

import com.fananzapp.activities.SubscriberLoginActivity;
import com.fananzapp.data.SubscriberDTO;
import com.fananzapp.data.UserDTO;

/**
 * Created by akshay on 03-01-2017.
 */
public class UserAuth {

    public static boolean isSubscriberLoggedIn(Context context, String userName, String password) {
        if (password == null || password == "" || userName == null || userName == "") {
            Intent theLoginIntent = new Intent(context, SubscriberLoginActivity.class);
            //theLoginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            theLoginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            theLoginIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(theLoginIntent);
            return false;
        }
        return true;
    }


    public static boolean isSubscriberLoggedIn() {
        long subscriberId = SessionManager.Instance().getSubId();
        String theUserEmail = SessionManager.Instance().getEmail();
        //String theUserPhotoURL = SessionManager.Instance().getUserPhotoUrl();
        if (subscriberId == 0 || theUserEmail == null || theUserEmail == "") {
            return false;
        }
        return true;
    }

    public void saveSubscriberInfo(SubscriberDTO userInfo, final Context context) {
        if (userInfo == null)
            return;

        if (userInfo.getEmail() == null || userInfo.getEmail() == "" ||
                userInfo.getName() == null || userInfo.getName() == "")
            return;

        SessionManager theSessionManager = SessionManager.getInstance(context);
        theSessionManager.setSubId(userInfo.getSubscriberId());
        theSessionManager.setName(userInfo.getName());
        theSessionManager.setNickName(userInfo.getNickName());
        theSessionManager.setEmail(userInfo.getEmail());
        theSessionManager.setSType(userInfo.getSType());
        theSessionManager.setSubDate(userInfo.getSubscriptionDate());
        theSessionManager.setIsSubscribed(userInfo.isIsSubscribed());
        theSessionManager.setPassword(userInfo.getPassword());
    }

    public static boolean CleanAuthenticationInfo() {

        SessionManager theSessionManager = SessionManager.Instance();
        theSessionManager.setSubId(0);
        theSessionManager.setName(null);
        theSessionManager.setNickName(null);
        theSessionManager.setEmail(null);
        theSessionManager.setSType(null);
        theSessionManager.setSubDate(null);
        theSessionManager.setIsSubscribed(false);
        theSessionManager.setPassword(null);
        return true;
    }

    public void saveUserInfo(UserDTO subscriberDTO, Context context) {
        if (subscriberDTO == null)
            return;

        if (subscriberDTO.getEmail() == null || subscriberDTO.getEmail() == "" ||
                subscriberDTO.getFirstName() == null || subscriberDTO.getFirstName() == "")
            return;

        SessionManager theSessionManager = SessionManager.getInstance(context);
        theSessionManager.setUserId(subscriberDTO.getUserId());
        theSessionManager.setUserFName(subscriberDTO.getFirstName());
        theSessionManager.setUserLName(subscriberDTO.getLastName());
        theSessionManager.setUserEmail(subscriberDTO.getEmail());
        theSessionManager.setUserPass(subscriberDTO.getPassword());
    }
}
