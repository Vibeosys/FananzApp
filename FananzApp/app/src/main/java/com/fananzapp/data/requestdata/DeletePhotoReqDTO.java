package com.fananzapp.data.requestdata;

import com.fananzapp.data.BaseDTO;

/**
 * Created by akshay on 09-01-2017.
 */
public class DeletePhotoReqDTO extends BaseDTO {

    private long photoId;

    public DeletePhotoReqDTO(long photoId) {
        this.photoId = photoId;
    }

    public long getPhotoId() {
        return photoId;
    }

    public void setPhotoId(long photoId) {
        this.photoId = photoId;
    }
}
