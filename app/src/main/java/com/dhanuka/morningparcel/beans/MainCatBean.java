package com.dhanuka.morningparcel.beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class MainCatBean implements Serializable {
    @SerializedName("GroupId")
    String strId;
    @SerializedName("Description")
    String strName;
    @SerializedName("ImageName")
    String strImageName;
    @SerializedName("Weight")
    String strGroupId;
    @SerializedName("Detail")
    ArrayList<CatBean> mListCategories;

    /*
    [{
        "Description": "Fresh",
                "ImageName": "",
                "Weight": "",
                "GroupId": "1031",
                "Detail": [{
            "GroupId": "1033",
                    "Description": "Fruits",
                    "ImageName": "",
                    "Weight": "",
                    "GroupType": "1031"
        }, {
            "GroupId": "1034",
                    "Description": "Vegetables",
                    "ImageName": "",
                    "Weight": "",
                    "GroupType": "1031"
        }]
*/
    public MainCatBean() {
    }

    public MainCatBean(String strId, String strName, ArrayList<CatBean> mListCategories) {
        this.strId = strId;
        this.strName = strName;
        this.mListCategories = mListCategories;
    }

    public String getStrId() {
        return strId;
    }

    public void setStrId(String strId) {
        this.strId = strId;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public ArrayList<CatBean> getmListCategories() {
        return mListCategories;
    }

    public void setmListCategories(ArrayList<CatBean> mListCategories) {
        this.mListCategories = mListCategories;
    }

    public String getStrImageName() {
        return strImageName;
    }

    public void setStrImageName(String strImageName) {
        this.strImageName = strImageName;
    }

    public String getStrGroupId() {
        return strGroupId;
    }

    public void setStrGroupId(String strGroupId) {
        this.strGroupId = strGroupId;
    }

    public static class CatBean implements Serializable {
        @SerializedName("GroupId")
        String strId;
        @SerializedName("GroupType")
        String strParentId;
        @SerializedName("ImageName")
        String strImage;
        @SerializedName("Description")
        String strName;
        @SerializedName("Weight")
        String strWeight;
        @SerializedName("filepath")
        String filepath;

        int ProductCount=0;

        public int getProductCount() {
            return ProductCount;
        }

        public void setProductCount(int productCount) {
            ProductCount = productCount;
        }

        public String getFilepath() {
            return filepath;
        }

        public void setFilepath(String filepath) {
            this.filepath = filepath;
        }

        public String getStrWeight() {
            return strWeight;
        }

        public void setStrWeight(String strWeight) {
            this.strWeight = strWeight;
        }

        public CatBean(String strId, String strParentId, String strImage, String strName) {
            this.strId = strId;
            this.strParentId = strParentId;
            this.strImage = strImage;
            this.strName = strName;
        }

        public CatBean() {
        }

        public String getStrId() {
            return strId;
        }

        public void setStrId(String strId) {
            this.strId = strId;
        }

        public String getStrParentId() {
            return strParentId;
        }

        public void setStrParentId(String strParentId) {
            this.strParentId = strParentId;
        }

        public String getStrImage() {
            return strImage;
        }

        public void setStrImage(String strImage) {
            this.strImage = strImage;
        }

        public String getStrName() {
            return strName;
        }

        public void setStrName(String strName) {
            this.strName = strName;
        }
    }
}
