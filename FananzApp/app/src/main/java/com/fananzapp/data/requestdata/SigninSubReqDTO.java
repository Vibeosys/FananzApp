package com.fananzapp.data.requestdata;

import com.fananzapp.data.BaseDTO;

/**
 * Created by akshay on 03-01-2017.
 */
public class SigninSubReqDTO extends BaseDTO {

    private String emailId;
    private String password;

    public SigninSubReqDTO() {
    }

    public SigninSubReqDTO(String password, String emailId) {
        this.password = password;
        this.emailId = emailId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
