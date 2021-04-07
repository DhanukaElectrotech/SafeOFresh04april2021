package com.dhanuka.morningparcel.restaurant.models;

import java.io.Serializable;

public class Categoryhelper implements  Serializable {

        String maintext,imagelink;



        public String getMaintext() {
            return maintext;
        }

        public void setMaintext(String maintext) {
            this.maintext = maintext;
        }

        public String getImagelink() {
            return imagelink;
        }

        public void setImagelink(String imagelink) {
            this.imagelink = imagelink;
        }


}
