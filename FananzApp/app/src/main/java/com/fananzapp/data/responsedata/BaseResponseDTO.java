package com.fananzapp.data.responsedata;

import android.util.Log;


import com.fananzapp.data.BaseDTO;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

/**
 * Created by akshay on 30-12-2016.
 */
public class BaseResponseDTO extends BaseDTO {

    private static final String TAG = BaseResponseDTO.class.getSimpleName();
    private String error;
    private String data;

    public BaseResponseDTO() {
    }

    public BaseResponseDTO(String error, String data) {
        this.error = error;
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public static BaseResponseDTO deserializeJson(String serializedString) {
        Gson gson = new Gson();
        BaseResponseDTO resonceDto = null;
        try {
            resonceDto = gson.fromJson(serializedString, BaseResponseDTO.class);
        } catch (JsonParseException e) {
            Log.d(TAG, "Exception in deserialization" + e.toString());
        }
        return resonceDto;
    }
}
