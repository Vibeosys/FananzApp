package com.fananz.data.requestdata;

import com.fananz.data.BaseDTO;

/**
 * Created by akshay on 11-01-2017.
 */
public class ForgotPasswordReqDTO extends BaseDTO {

    private String emailId;

    public ForgotPasswordReqDTO(String emailId) {
        this.emailId = emailId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
