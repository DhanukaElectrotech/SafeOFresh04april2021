package com.dhanuka.morningparcel.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class  Preferencehelper {


    private static SharedPreferences prefs;


    public final static String PREFS_CUST_ID = "cust_id";
    public final static String PREFS_ZIP_CODE = "zip_code";

    public final static String PREFS_IS_FINANCE_BLOCK = "financeblock";
    public final static String PREFS_TEMP_CONTACTID= "tempcontactid";
    public final static String PREFS_COUNTRY = "country_id";

    public final static String PREFS_ADDRESS = "addressdescription";
    public final static String PREFS_OTP = "otpid";
    public final static String PREFS_SHOP_OPEN_TIME = "shopopen";
    public final static String PREFS_SHOP_CLOSE_TIME = "shopclose";
    public final static String PREFS_PO = "p_o";
    public final static String PREFS_city = "city_id";
    public final static String PREFS_CURRENCY = "curreny_tag";
    public final static String PREFS_flatno = "flatno";
    public final static String PREFS_state = "stateid";
    public final static String PREFS_firstname = "firstname";
    public final static String PREFS_phoneno = "phoneno";
    public final static String PREFS_email2 = "email2";
    public final static String PREFS_TAG_1 = "tag1";
    public final static String PREFS_TAG_2 = "tag2";
    public final static String PREFS_TAG_3 = "tag3";

    public final static String PREFS_TAG_1_DESC = "tags1";
    public final static String PREFS_TAG_2_DESC = "tags2";
    public final static String PREFS_TAG_3_DESC = "tags3";

    public final static String PREFS_currentbal = "crtblc";

    public final static String PREFS_trialuser= "trialuser";
    public final static String PREFS_Building = "Building";
    public final static String PREFS_society = "society";
    public final static String PREFS_USERNAME = "username";
    public final static String PREFS_VIEW_TYPE = "view_type";
    public final static String PREFS_CID = "companyId";
    public final static String NEW_POP_RECYCLER = "poprecycid";
    public final static String PREFS_CONTACT_ID = "uid";
    public final static String PREFS_ALL_MAP_LIST = "maplist";
    public final static String PREFS_COMPANY_ID = "company_id";
    public final static String PREFS_LOCK_KEY = "LockKey";
    public final static String PREFS_RUNONCE = "runonce";
    public final static String PREFS_GCM_STATUS = "gcmstatus";
    public final static String PREFS_EMAIL = "email";
    public final static String PREFS_PASSWORD = "passwordd";
    public final static String PREFS_USERCATEGORY = "usercategory";
    public final static String PREFS_DISPLAYLOCKMENU = "displaylockmenu";
    public final static String PREFS_LOGIN_VALUE = "loginValue";
    public final static String PREFS_SUB_ID = "subinterid";
    public final static String PREFS_IS_FEEDBACK_GIVE = "feedback_taken";
    public final static String PREFS_INTRO = "appintro";
    public final String PREFS_TOKEN = "tokenval";


    public boolean isFeedBackGiven() {
        return prefs.getBoolean(PREFS_IS_FEEDBACK_GIVE, false);
    }

    public void setIsFeedBackGive(boolean valus) {
        prefs.edit().putBoolean(PREFS_IS_FEEDBACK_GIVE, valus).commit();
    }




    public Preferencehelper(Context ctx) {
        prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static String getPrefsEmail() {
        return prefs.getString(PREFS_EMAIL, null);
    }


    public String getPrefsCountry() {
        return prefs.getString(PREFS_COUNTRY, "India");
    }

    public void setPrefsCountry(String string) {
        prefs.edit().putString(PREFS_COUNTRY, string).commit();
    }

    public String getPrefsPaymentoption() {
        return prefs.getString(PREFS_PO, "Demo");
    }

    public void setPrefsPaymentoption(String string) {
        prefs.edit().putString(PREFS_PO, string).commit();
    }




    public String getPrefsShopOpenTime() {
        return prefs.getString(PREFS_SHOP_OPEN_TIME, "Demo");
    }

    public void setPrefsShopOpenTime(String string) {
        prefs.edit().putString(PREFS_SHOP_OPEN_TIME, string).commit();
    }


    public String getPrefsShopCloseTime() {
        return prefs.getString(PREFS_SHOP_CLOSE_TIME, "Demo");
    }

    public void setPrefsShopCloseTime(String string) {
        prefs.edit().putString(PREFS_SHOP_CLOSE_TIME, string).commit();
    }

    public String getPrefsOtp() {
        return prefs.getString(PREFS_OTP, "");
    }

    public void setPrefsOtp(String string) {
        prefs.edit().putString(PREFS_OTP, string).commit();
    }
    public String getPrefsTempContactid() {
        return prefs.getString(PREFS_TEMP_CONTACTID, "");
    }

    public void setPrefsTempContactid(String string) {
        prefs.edit().putString(PREFS_TEMP_CONTACTID, string).commit();
    }

    public String getPrefsCustId() {
        return prefs.getString(PREFS_CUST_ID, "");
    }

    public void setPrefsCustId(String string) {
        prefs.edit().putString(PREFS_CUST_ID, string).commit();
    }



    public String getPrefsIsFinanceBlock() {
        return prefs.getString(PREFS_IS_FINANCE_BLOCK, "");
    }

    public void setPrefsIsFinanceBlock(String string) {
        prefs.edit().putString(PREFS_IS_FINANCE_BLOCK, string).commit();
    }

    public String getPrefsAddress() {
        return prefs.getString(PREFS_ADDRESS, "");
    }

    public void setPrefsAddress(String string) {
        prefs.edit().putString(PREFS_ADDRESS, string).commit();
    }


    public String getPrefsLockKey() {
        return prefs.getString(PREFS_LOCK_KEY, null);
    }

    public void setPrefsLockKey(String string) {
        prefs.edit().putString(PREFS_LOCK_KEY, string).commit();
    }


    public String getPREFS_currentbal() {
        return prefs.getString(PREFS_currentbal, "0");
    }

    public void setPREFS_currentbal(String string) {
        prefs.edit().putString(PREFS_currentbal, string).commit();
    }




    public String getPREFS_trialuser() {
        return prefs.getString(PREFS_trialuser, "0");
    }

    public void setPREFS_trialuser(String string) {
        prefs.edit().putString(PREFS_trialuser, string).commit();
    }

    public String getPrefsCurrency() {
        return prefs.getString(PREFS_CURRENCY, "0");
    }

    public void setPrefsCurrency(String string) {
        prefs.edit().putString(PREFS_CURRENCY, string).commit();
    }


    public String getPREFS_state() {
        return prefs.getString(PREFS_state, null);
    }

    public void setPREFS_state(String string) {
        prefs.edit().putString(PREFS_state, string).commit();
    }








    public String getPrefsZipCode() {
        return prefs.getString(PREFS_ZIP_CODE, null);
    }

    public void setPrefsZipCode(String string) {
        prefs.edit().putString(PREFS_ZIP_CODE, string).commit();
    }




    public String getPREFS_flatno() {
        return prefs.getString(PREFS_flatno, null);
    }

    public void setPREFS_flatno(String string) {
        prefs.edit().putString(PREFS_flatno, string).commit();
    }


    public String getPREFS_phoneno() {
        return prefs.getString(PREFS_phoneno, null);
    }

    public void setPREFS_phoneno(String string) {
        prefs.edit().putString(PREFS_phoneno, string).commit();
    }


    public String getPREFS_email2() {
        return prefs.getString(PREFS_email2, null);
    }

    public void setPREFS_email2(String string) {
        prefs.edit().putString(PREFS_email2, string).commit();
    }

    public String getBannerPrefs() {
        return prefs.getString("bnnr", "0");
    }

    public void setBannerPrefs(String string) {
        prefs.edit().putString("bnnr", string).commit();
    }


    public String getPREFS_Building() {
        return prefs.getString(PREFS_Building, null);
    }

    public void setPREFS_Building(String string) {
        prefs.edit().putString(PREFS_Building, string).commit();
    }

    public String getPREFS_society() {
        return prefs.getString(PREFS_society, null);
    }

    public void setPREFS_society(String string) {
        prefs.edit().putString(PREFS_society, string).commit();
    }


    public String getPREFS_firstname() {
        return prefs.getString(PREFS_firstname, null);
    }

    public void setPREFS_firstname(String string) {
        prefs.edit().putString(PREFS_firstname, string).commit();
    }

    public String getPREFS_city() {
        return prefs.getString(PREFS_city, null);
    }

    public void setPREFS_city(String string) {
        prefs.edit().putString(PREFS_city, string).commit();
    }


    public String getDISPLAYLOCKMENU() {
        return prefs.getString(PREFS_DISPLAYLOCKMENU, null);
    }

    public void setDISPLAYLOCKMENU(String string) {
        prefs.edit().putString(PREFS_DISPLAYLOCKMENU, string).commit();
    }

    public String getPrefsRunonce() {
        return prefs.getString(PREFS_RUNONCE, "0");
    }

    public void setPrefsRunonce(String string) {
        prefs.edit().putString(PREFS_RUNONCE, string).commit();
    }


    public void setPrefsEmail(String string) {
        prefs.edit().putString(PREFS_EMAIL, string).commit();

    }

    public String getNewPopRecycler() {

        return prefs.getString(NEW_POP_RECYCLER, null);
    }

    public void setNewPopRecycler(String string) {
        prefs.edit().putString(NEW_POP_RECYCLER, string).commit();
    }

    public String getCID() {

        return prefs.getString(PREFS_CID, null);
    }

    public void setCID(String string) {
        prefs.edit().putString(PREFS_CID, string).commit();
    }

    public static String getPrefsSubId() {
        return prefs.getString(PREFS_SUB_ID, null);
    }

    public void setPrefsSubId(String string) {
        prefs.edit().putString(PREFS_SUB_ID, string).commit();
    }

    public void clear(String value) {
        prefs.edit().remove(value).commit();
    }

    public void clearAllPrefs() {
        prefs.edit().clear().commit();
    }

    public SharedPreferences getPrefs() {
        return prefs;
    }

    public void setPrefsUsername(String string) {
        prefs.edit().putString(PREFS_USERNAME, string).commit();
    }

    public String getPrefsUsername() {
        return prefs.getString(PREFS_USERNAME, null);
    }

  public void setPrefsViewType(int string) {
        prefs.edit().putInt(PREFS_VIEW_TYPE, string).commit();
    }

    public int getPrefsViewType() {
        return prefs.getInt(PREFS_VIEW_TYPE, 0);
    }


    public void setPrefsAllMapList(String string) {
        prefs.edit().putString(PREFS_ALL_MAP_LIST, string).commit();
    }

    public String getPrefsAllMapList() {
        return prefs.getString(PREFS_ALL_MAP_LIST, null);
    }


    public void setPrefsContactId(String string) {
        prefs.edit().putString(PREFS_CONTACT_ID, string).commit();
    }

    public String getPrefsContactId() {
        return prefs.getString(PREFS_CONTACT_ID, "");
    }


    public void setPrefsGcmStatus(String string) {
        prefs.edit().putString(PREFS_GCM_STATUS, string).commit();
    }

    public String getPrefsGcmStatus() {
        return prefs.getString(PREFS_GCM_STATUS, null);
    }


    public void setPrefsCompanyId(String string) {
        prefs.edit().putString(PREFS_COMPANY_ID, string).commit();
    }

    public String getPrefsCompanyId() {
        return prefs.getString(PREFS_COMPANY_ID, null);
    }


    public void setPrefsUsercategory(String string) {
        prefs.edit().putString(PREFS_USERCATEGORY, string).commit();
    }

    public String getPrefsUsercategory() {
        return prefs.getString(PREFS_USERCATEGORY, "");
    }


    public void setPrefsLoginValue(String string) {
        prefs.edit().putString(PREFS_LOGIN_VALUE, string).commit();
    }

    public String getPrefsLoginValue() {
        return prefs.getString(PREFS_LOGIN_VALUE, "");
    }


    public void setPrefs(SharedPreferences prefs) {
        this.prefs = prefs;
    }

    public String getPrefsPassword() {
        return prefs.getString(PREFS_PASSWORD, "");
    }

    public void setPrefsPassword(String string) {
        prefs.edit().putString(PREFS_PASSWORD, string).commit();
    }

    public String getPrefsIntro() {
        return prefs.getString(PREFS_INTRO, "");
    }

    public void setPrefsIntro(String string) {
        prefs.edit().putString(PREFS_INTRO, string).commit();
    }

    public void setTokenValue(String valus) {
        prefs.edit().putString(PREFS_TOKEN, valus).commit();
    }


    public String getTokenValue() {

        return prefs.getString(PREFS_TOKEN, null);
    }

    public String getPrefsTag1() {
        return prefs.getString(PREFS_TAG_1, "");
    }

    public void setprefstag1(String valus) {
        prefs.edit().putString(PREFS_TAG_1, valus).commit();
    }

    public String getPrefsTag2() {
        return prefs.getString(PREFS_TAG_2, "");
    }

    public void setprefstag2(String valus) {
        prefs.edit().putString(PREFS_TAG_2, valus).commit();
    }

    public String getPrefsTag3() {
        return prefs.getString(PREFS_TAG_3, "");
    }

    public void setprefstag3(String valus) {
        prefs.edit().putString(PREFS_TAG_3, valus).commit();
    }

    public String getPrefsTag1Desc() {
        return prefs.getString(PREFS_TAG_1_DESC, "");
    }

    public void setPrefsTag1Desc(String valus) {
        prefs.edit().putString(PREFS_TAG_1_DESC, valus).commit();
    }

    public String getPrefsTag2Desc() {
        return prefs.getString(PREFS_TAG_2_DESC, "");
    }

    public void setprefstag2Desc(String valus) {
        prefs.edit().putString(PREFS_TAG_2_DESC, valus).commit();
    }

    public String getPrefsTag3Desc() {
        return prefs.getString(PREFS_TAG_3_DESC, "");
    }

    public void setprefstag3Desc(String valus) {
        prefs.edit().putString(PREFS_TAG_3_DESC, valus).commit();
    }
}
