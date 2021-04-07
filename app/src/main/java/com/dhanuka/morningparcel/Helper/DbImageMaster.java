package com.dhanuka.morningparcel.Helper;


public class DbImageMaster {


    private int mId;
    private String mImageType;
    private int mUploadStatus;
    private String mServerId;
    private String mDescription;
    private String mDate;


    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmImageType() {
        return mImageType;
    }

    public void setmImageType(String mImageType) {
        this.mImageType = mImageType;
    }

    public int getmUploadStatus() {
        return mUploadStatus;
    }

    public void setmUploadStatus(int mUploadStatus) {
        this.mUploadStatus = mUploadStatus;
    }


    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    //todo changed server id from int to string chagne other according to that
    public String getmServerId() {
        return mServerId;
    }

    public void setmServerId(String mServerId) {
        this.mServerId = mServerId;
    }
}
