package com.fananzapp.data.responsedata;

import android.util.Log;

import com.fananzapp.data.BaseDTO;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

/**
 * Created by akshay on 30-12-2016.
 */
public class ResponseErrorDTO extends BaseDTO {

    private static final String TAG = ResponseErrorDTO.class.getSimpleName();
    private int errorCode;
    private String message;

    public ResponseErrorDTO() {
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static ResponseErrorDTO deserializeJson(String serializedString) {
        Gson gson = new Gson();
        ResponseErrorDTO errorDto = null;
        try {
            errorDto = gson.fromJson(serializedString, ResponseErrorDTO.class);
        } catch (JsonParseException e) {
            Log.d(TAG, "Exception in deserialization" + e.toString());
        }
        return errorDto;
    }

    @Override
    public String toString() {
        return "ResponseErrorDTO{" +
                "errorCode=" + errorCode +
                ", message='" + message + '\'' +
                '}';
    }
}
