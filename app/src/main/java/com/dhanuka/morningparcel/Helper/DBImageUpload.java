package com.dhanuka.morningparcel.Helper;


import com.dhanuka.morningparcel.utils.DbImageUpload;

public class DBImageUpload extends DbImageUpload {


    private int mId;
    private int mImageId;
    private String mImageName;
    private String mDescription;
    private String mImagePath;
    private String mImageType;
    private String mLat;
    private String mLong;
    private int mImageUploadStatus;


    private String mServerId;
    private String mDate;


    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public int getmImageId() {
        return mImageId;
    }

    public void setmImageId(int mImageId) {
        this.mImageId = mImageId;
    }

    public String getmImageName() {
        return mImageName;
    }

    public void setmImageName(String mImageName) {
        this.mImageName = mImageName;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmImagePath() {
        return mImagePath;
    }

    public void setmImagePath(String mImagePath) {
        this.mImagePath = mImagePath;
    }

    public String getmImageType() {
        return mImageType;
    }

    public void setmImageType(String mImageType) {
        this.mImageType = mImageType;
    }

    public String getmLat() {
        return mLat;
    }

    public void setmLat(String mLat) {
        this.mLat = mLat;
    }

    public String getmLong() {
        return mLong;
    }

    public void setmLong(String mLong) {
        this.mLong = mLong;
    }


    public String getmServerId() {
        return mServerId;
    }

    public void setmServerId(String mServerId) {
        this.mServerId = mServerId;
    }

    public int getmImageUploadStatus() {
        return mImageUploadStatus;
    }

    public void setmImageUploadStatus(int mImageUploadStatus) {
        this.mImageUploadStatus = mImageUploadStatus;
    }



    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }
}
