package com.dhanuka.morningparcel.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Set;

public class Preferencehelper {

    private SharedPreferences prefs;





    /*
   <!--" ================================ "-->
   <!--          MMTHINKBIZ    Registration    -->
   <!--"====================================="-->

    */
    public final static String PREFS_REG_VAL1= "reg_val1";
    public final static String PREFS_REG_VAL2= "reg_val2";
    public final static String PREFS_REG_VAL3= "reg_val3";


    public final static String PREFS_ODOMETER_APPLIED = "odometer_applied";
    public final static String PREFS_REG_FIRST_NAME = "reg_first_name";

    public final static String PREFS_REG_LAST_NAME = "reg_last_name";
    public final static String PREFS_REG_DATE_OF_BIRTH = "reg_date_of_birth";
    public final static String PREFS_REG_GENDER = "reg_gender";
    public final static String PREFS_REG_ADDRESS = "reg_address";
    public final static String PREFS_REG_CITY = "reg_city";
    public final static String PREFS_REG_STATE = "reg_state";
    public final static String PREFS_DEFAULT_BRANCH = "reg_default_branch";
    public final static String PREFS_REG_COUNTRY = "reg_country";
    public final static String PREFS_REG_ZIP_CODE = "reg_zip_code";
    public final static String PREFS_REG_USER_ID = "reg_user_id";
    public final static String PREFS_REG_PASSWORD = "reg_password";
    public final static String PREFS_REG_MOBILE_NUMBER = "reg_mobile_number";
    public final static String PREFS_REG_ALTERNATE_NUMBER = "reg_alternate_mobile_number";
    public final static String PREFS_REG_EMAIL_ID = "reg_email_id";
    public final static String PREFS_REG_ALTERNATE_EMAIL_ID = "reg_alter_nate_email_id";
    public final static String PREFS_REG_SECURITY_QUESTION_ONE = "reg_security_qu_one";
    public final static String PREFS_REG_SECURITY_QUESTION_TWO = "reg_security_qu_two";
    public final static String PREFS_REG_SECURITY_ANS_ONE = "reg_security_ans_one";
    public final static String PREFS_REG_SECURITY_ANS_TWO = "reg_security_ans_two";
    public final static String PREFS_REG_TOTAL_DISTANCE = "total_distance";
    public final static String PREFS_REG_TOTAL_OLD_DISTANCE = "total_old_distance";

    public final static String PREFS_REG_ALARAM_FIRST_LAT = "alarm_first_lat";
    public final static String PREFS_REG_ALARAM_FIRST_LNG = "alarm_first_lng";
    public final static String PREFS_REG_ALARAM_LAST_LAT = "alarm_last_lat";
    public final static String PREFS_REG_ALARAM_LAST_LNG = "alarm_last_lng";


    public final static String PREFS_REG_ALARAM_COUNT = "alarm_count";

    public final static String PREFS_IS_UPDATE_NOTIFICATION = "isUpdateNotification";







   /*
    <!--" ================================ "-->
    <!--          MMTHINKBIZ    Registration    -->
    <!--"====================================="-->

     */







    /*
    <!--" ================================ "-->
    <!--          MMTHINKBIZ    Registration    -->
    <!--"====================================="-->

     */






    /*
    <!--" ================================ "-->
    <!--          MMTHINKBIZ                      -->
    <!--"====================================="-->

     */


    public final static String PREFS_KEY_LAT = "latitude";
    public final static String PREFS_KEY_LNG = "longitude";
    private final static String PREFS_GCM_UPLOADED = "is_gcm_id_uploaded";
    public final static String PREFS_USER_ID = "userId";
    public final static String PREFS_COMPANY_ID = "companyID";
    public final static String PREFS_USER_TYPE = "usertype";
    public final static String PREFS_PROFILE_PICTURE = "profilePicture";
    public final static String PREFS_IS_REGISTRATION_ACTIVITY_RUNNING = "isRArunning";
    public final static String PREFS_IS_VERIFICATION_ACTIVITY_RUNNING = "isVArunning";
    public final static String PREFS_IS_SIGNATURE_TAKEN = "signature_taken";
    public final static String PREFS_IS_SIGNATURE_TAKEN_AFTER = "signature_taken_after";
    public final static String PREFS_IS_SIGNATURE_ROSTER = "signature_taken";


    public final static String PREFS_IS_FEEDBACK_GIVE = "feedback_taken";

    public final static String PREFS_IS_FROM_SENERIOBEFORESTARTACTIVITY = "isSenerioActivity";
    public final static String PREFS_KEY_LAT_VEHICLE = "latitudeVehicle";
    public final static String PREFS_KEY_LNG_VEHICLE = "longitudeVehicle";
    public final static String PREFS_KEY_FRAGMENT_NUMBER_VEHICLE = "numberVehicle";
    public final static String PREFS_REPORT_TYPE= "pressreporttype";
    public final static String PREFS_KEY_FRAGMENT_NUMBER_VEHICLE_ONE = "numberVehicleOne";
    public final static String PREFS_KEY_FRAGMENT_NUMBER_VEHICLE_TWO = "numberVehicleTwo";
    public final static String PREFS_KEY_FRAGMENT_NUMBER_VEHICLE_THREE = "numberVehicleThree";
    public final static String PREFS_KEY_FRAGMENT_NUMBER_VEHICLE_FOUR = "numberVehicleFour";
    public final static String PREFS_KEY_FRAGMENT_NUMBER_VEHICLE_FIVE = "numberVehicleFive";
    public final static String PREFS_KEY_FRAGMENT_NUMBER_VEHICLE_SIX = "numberVehicleSix";
    public final static String PREFS_KEY_FRAGMENT_NUMBER_VEHICLE_SEVEN = "numberVehicleSeven";
    public final static String PREFS_KEY_FRAGMENT_NUMBER_VEHICLE_EIGHT = "numberVehicleEight";
    public final static String PREFS_KEY_FRAGMENT_NUMBER_VEHICLE_RUN_ONLY_ONCE = "runonlyonce";
    public final static String PREFS_KEY_AF_VEHICLE_NUMBER = "aeVehicleNumber";
    public final static String PREFS_KEY_AF_KM = "aeKm";
    public final static String PREFS_KEY_AF_START_DT = "arStartDate";
    public final static String PREFS_KEY_AF_END_DT = "afEndDate";
    public final static String PREFS_MENU_ADD = "menuAdd";
    public final static String PREFS_IS_SUBMIT_CLICKED_FOR_KM = "isSubmitClickedForKm";
    public final  static String PREFS_LAT_LONG_VEHICLE = "prefsLatLongVehicle";
    public static final String PREFS_LAT_LONG_VEHICLE_ARRAY = "prefsLatLongVehicleArray";
    public final String PREFS_NEAR_BY_API_CALLED = "isNearByApiCalled";
    public final String PREFS_IS_JSON_ARRAY_FILLED = "isJsonArrayFilled";
    public final String PREFS_IS_LAT_FOR_ZOOM = "latForZoom";
    public final String PREFS_IS_LONG_FOR_ZOOM = "LongForZoom";
    public final static String PREFS_IS_LAT_START_POINT = "latForstartrouPosition";
    public final static  String PREFS_IS_LONG_START_POINT = "longForstartrouPosition";
    public final static  String PREFS_IS_LAT_WAY_POINT = "latForwayroutPosition";
    public final static  String PREFS_IS_LONG_WAY_POINT = "longForwayroutPosition";
    public final static String PREFS_IS_LAT_END_POINT = "latForEndRouPosition";
    public final static String PREFS_IS_LONG_END_POINT = "longForEndRouPosition";
    public final String PREFS_SUMBMIT_CLICK_VEHICLE_ID = "submitClickVehicleId";
    public final String PREFS_IS_MENU_CLICK = "isMenuClick";
    public final String PREFS_API_CONTEN_DATA = "isApiContainData";
    public final String PREFS_MOBILE_NUMBER = "mobileNumberPhoto";
    public final String PREFS_IS_LAT_FOR_CAMARA_POSITION = "latForCameraPosition";
    public final String PREFS_IS_LONG_FOR_CAMARA_POSITION = "longForCameraPosition";
    private final String PREFS_IS_SHOW_QUICK_BOOK_HINT = "isShowQuickBookHint";
    public final String PREFS_WORK_ORDER_PRIORITY = "workOrderPriority";
    public final String PREFS_WORK_ORDER_ADDRESS = "workOrderAddress";
    public final String PREFS_IS_ADD_TO_MASTER = "isAddToMaster";
    public final String PREFS_WORK_ORDER_ID = "workOrderId";
    private final String PREFS_IS_EDIT_FAV_WO = "isEditFavWo";
    private final String PREFS_WO_FAV_PLACE_NAME = "favPlaceNameWo";
    private final String PREFS_IS_SAVE_CLICKED_OF_FAV = "isSaveCleckedOfFav";
    private final String PREFS_IS_DAY_END_REASON = "isDayEndReason";

    private final String PREFS_EDIT_WO_LOCATION_LATITUDE = "editWoLocationLat";
    private final String PREFS_EDIT_WO_LOCATION_LONGITUDE = "editWorkLocaLong";
    private final String PREFS_EDIT_WO_ADDRESS_ = "isEditWoAddressWoLo";
    private final String PREFS_IS_WO_MAP_ICON_CLICK = "isWoMapIconClick";
    private final String PREFS_IS_EDIT_LOCATION_WO = "isEditLocationWo";
    private final String PREFS_IS_UPDATE_LOCATION_WO = "isUpdateLocationWo";
    private final String PREFS_IS_LOG_IN = "isLogin";
    public final String PREFS_ATTENDNCE_DAY_ODOMETER ="odometer";
    public final String PREFS_ATTENDNCE_DAY_START_TIME ="dayStartTime";
    private final String PREFS_IS_LATE_REASON_SUBMITTED = "isLateReasonSubmitted";
    private final String PREFS_CASHCOLLECTED = "isCASHCOLLECTED";



    public final String PREFS_ATTENDNCE_DATE ="attendancedt";
    public final String PREFS_ATTENDNCE_END_TIME ="attendanceendtime";
    public final String PREFS_ATTENDNCE_COMMENT ="attendancecomment";
    public final String PREFS_ATTENDNCE_START_TIME ="attendancestarttime";

    private final String PREFS_BITMAP_SAVED = "isBitmapSaved";

    public final static String PREFS_AttendanceID = "prefattid";


    private final String PREFS_IS_SHOW_COSTING = "isShowCoasting";
    private final String PREFS_IS_CALL_API_OF_TIMER = "isCallApiOfTimer";





    public final static String PREFS_IS_DAY_START_END = "isDayStartEnd";
    public final static String PREFS_LATEST_MASTER_DATABASE_ID = "latestmasterDataBaseId";


    public final static String PREFS_CAN_ATTENDANCE_BE_ENTERED = "canattendancebeentered";







/*
    <!--" ================================ "-->
    <!--     MMTHINKBIZ                      -->
    <!--"====================================="-->

     */





    public final static String PREFS_USERNAME = "username";
    public final static String PREFS_JOIN_DATE = "joinDate";
    public final static String PREFS_TITLE = "title";
    public final static String PREFS_FIRST_NAME = "firstName";
    public final static String PREFS_SUR_NAME = "surname";
    public final static String PREFS_ADDRESS1 = "address1";
    public final static String PREFS_ADDRESS2 = "address2";
    public final static String PREFS_TOWN = "town";
    public final static String PREFS_CITY = "city";
    public final static String PREFS_POSTCODE = "postcode";
    public final static String PREFS_EMAIL = "email";
    public final static String PREFS_AGE = "age";
    public final static String PREFS_TELEPHONE = "telephone";
    public final static String PREFS_MOBILE = "mobile";
    public final static String PREFS_TOPIC_ID_1 = "topicId1";
    public final static String PREFS_TOPIC_ID_2 = "topicId2";
    public final static String PREFS_TOPIC_ID_3 = "topicId3";

    public final static String USER_CATEGORY = "category";

/*
    <!--" ================================ "-->
    <!--          For Registration             -->
    <!--"====================================="-->

     */

    private String PREFS_SET_IMAGE_PATH_PERSON_PHOTO = "person_photo";



    public Preferencehelper(Context ctx) {
        prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static Preferencehelper getPlanPreferences(Context ctx, String name) {
        return new Preferencehelper(ctx, name);
    }

    public Preferencehelper(Context ctx, String name) {
        prefs = ctx.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    /*
    <!--" ================================ "-->
    <!--     MMTHINKBIZ                      -->
    <!--"====================================="-->

     */



    public void setProfileId(String id) {
        prefs.edit().putString(PREFS_USER_ID, id).commit();
    }

    public String getProfileId() {
        return prefs.getString(PREFS_USER_ID, null);
    }

    public void setCompanyId(String id) {
        prefs.edit().putString(PREFS_COMPANY_ID, id).commit();
    }

    public String getCompanyId() {
        return prefs.getString(PREFS_COMPANY_ID, null);
    }


    public void setWorkOrderFavPlaceName(String id) {
        prefs.edit().putString(PREFS_WO_FAV_PLACE_NAME, id).commit();
    }

    public String getWorkOrderFavPlaceName() {
        return prefs.getString(PREFS_WO_FAV_PLACE_NAME, null);
    }

    public void setPrefsUserType(String id) {
        prefs.edit().putString(PREFS_USER_TYPE, id).commit();
    }

    public String getPrefsUserType() {
        return prefs.getString(PREFS_USER_TYPE, null);
    }



    public void setProfilePicture(String id) {
        prefs.edit().putString(PREFS_PROFILE_PICTURE, id).commit();
    }

    public String getProfilePicture() {
        return prefs.getString(PREFS_PROFILE_PICTURE, null);
    }

    public LatLng getLatLongForVehicleMap() {
        double lat = Double.longBitsToDouble(prefs.getLong(PREFS_KEY_LAT_VEHICLE, 0));
        double lng = Double.longBitsToDouble(prefs.getLong(PREFS_KEY_LNG_VEHICLE, 0));
        return new LatLng(lat, lng);
    }

    public void setLatLongForVehicleMap(double lat, double lng) {
        prefs.edit().putLong(PREFS_KEY_LAT_VEHICLE, Double.doubleToLongBits(lat)).commit();
        prefs.edit().putLong(PREFS_KEY_LNG_VEHICLE, Double.doubleToLongBits(lng)).commit();
    }


    public int getVehicleFragmentNumber() {
        return prefs.getInt(PREFS_KEY_FRAGMENT_NUMBER_VEHICLE, 0);
    }

    public void setVehicleFragmentNumber(int valus) {
        prefs.edit().putInt(PREFS_KEY_FRAGMENT_NUMBER_VEHICLE, valus).commit();
    }

    public int getcurrentreporttype() {
        return prefs.getInt(PREFS_REPORT_TYPE, 0);
    }

    public void setcurrentreporttype(int valus) {
        prefs.edit().putInt(PREFS_KEY_FRAGMENT_NUMBER_VEHICLE, valus).commit();
    }

    public boolean isMenuShow() {
        return prefs.getBoolean(PREFS_MENU_ADD, false);
    }
    public void setMenuShow(boolean valus) {
        prefs.edit().putBoolean(PREFS_MENU_ADD, valus).commit();
    }

    public boolean isSubmitClickedForKmClicke() {
        return prefs.getBoolean(PREFS_IS_SUBMIT_CLICKED_FOR_KM, false);
    }
    public void setSumbmitClickedForkm(boolean valus) {
        prefs.edit().putBoolean(PREFS_IS_SUBMIT_CLICKED_FOR_KM, valus).commit();
    }


    public Set<String> getLatLongList() {
        return prefs.getStringSet(PREFS_LAT_LONG_VEHICLE, null);
    }
    public void setLatLongSet(Set<String> valus) {
        prefs.edit().putStringSet(PREFS_LAT_LONG_VEHICLE, valus).commit();
    }



    public boolean getSubmitOneClicked() {
        return prefs.getBoolean(PREFS_KEY_FRAGMENT_NUMBER_VEHICLE_ONE, false);
    }
    public void setSubmitOneClicked(boolean valus) {
        prefs.edit().putBoolean(PREFS_KEY_FRAGMENT_NUMBER_VEHICLE_ONE, valus).commit();
    }
    public boolean getSubmitTwoClicked() {
        return prefs.getBoolean(PREFS_KEY_FRAGMENT_NUMBER_VEHICLE_TWO, false);
    }
    public void setSubmitTwoClicked(boolean valus) {
        prefs.edit().putBoolean(PREFS_KEY_FRAGMENT_NUMBER_VEHICLE_TWO, valus).commit();
    }
    public boolean getSubmitThreeClciked() {
        return prefs.getBoolean(PREFS_KEY_FRAGMENT_NUMBER_VEHICLE_THREE, false);
    }


    public void setusercategory(String musercategory) {
        prefs.edit().putString(USER_CATEGORY, musercategory).commit();
    }
    public String getusercategory() {
        return prefs.getString(USER_CATEGORY, null);
    }


    public void setSubmittThreeClicked(boolean valus) {
        prefs.edit().putBoolean(PREFS_KEY_FRAGMENT_NUMBER_VEHICLE_THREE, valus).commit();
    }
    public boolean getSubmitFourClicked() {
        return prefs.getBoolean(PREFS_KEY_FRAGMENT_NUMBER_VEHICLE_FOUR, false);
    }
    public void setSubmitFourClicked(boolean valus) {
        prefs.edit().putBoolean(PREFS_KEY_FRAGMENT_NUMBER_VEHICLE_FOUR, valus).commit();
    }
    public boolean getSubmitFiveClciked() {
        return prefs.getBoolean(PREFS_KEY_FRAGMENT_NUMBER_VEHICLE_FIVE, false);
    }
    public void setSubmitFiveClicked(boolean valus) {
        prefs.edit().putBoolean(PREFS_KEY_FRAGMENT_NUMBER_VEHICLE_FIVE, valus).commit();
    }
    public boolean getSubmitSixClicked() {
        return prefs.getBoolean(PREFS_KEY_FRAGMENT_NUMBER_VEHICLE_SIX, false);
    }
    public void setSubmitSixClicked(boolean valus) {
        prefs.edit().putBoolean(PREFS_KEY_FRAGMENT_NUMBER_VEHICLE_SIX, valus).commit();
    }

    public boolean getSubmittSevenClicked() {
        return prefs.getBoolean(PREFS_KEY_FRAGMENT_NUMBER_VEHICLE_SEVEN, false);
    }
    public void setSubmittSevenClicked(boolean valus) {
        prefs.edit().putBoolean(PREFS_KEY_FRAGMENT_NUMBER_VEHICLE_SEVEN, valus).commit();
    }
    public boolean getSubmitEightClicked() {
        return prefs.getBoolean(PREFS_KEY_FRAGMENT_NUMBER_VEHICLE_EIGHT, false);
    }
    public void setSubmitEightClicked(boolean valus) {
        prefs.edit().putBoolean(PREFS_KEY_FRAGMENT_NUMBER_VEHICLE_EIGHT, valus).commit();
    }


    public boolean isJsonArrayFilled() {
        return prefs.getBoolean(PREFS_IS_JSON_ARRAY_FILLED, false);
    }
    public void setJsonArrayFilled(boolean valus) {
        prefs.edit().putBoolean(PREFS_IS_JSON_ARRAY_FILLED, valus).commit();
    }


    public boolean getRunOnlyOnceVehicle() {
        return prefs.getBoolean(PREFS_KEY_FRAGMENT_NUMBER_VEHICLE_RUN_ONLY_ONCE, false);
    }

    public void setRunOnlyOnceVehicle(boolean valus) {
        prefs.edit().putBoolean(PREFS_KEY_FRAGMENT_NUMBER_VEHICLE_RUN_ONLY_ONCE, valus).commit();
    }


    public void setAeVehicleNo(String id) {
        prefs.edit().putString(PREFS_KEY_AF_VEHICLE_NUMBER, id).commit();
    }

    public String getAeVehicleNo() {
        return prefs.getString(PREFS_KEY_AF_VEHICLE_NUMBER, null);
    }

    public void setAeKm(String id) {
        prefs.edit().putString(PREFS_KEY_AF_KM, id).commit();
    }

    public String getAeKm() {
        return prefs.getString(PREFS_KEY_AF_KM, null);
    }
    public void setAeStartDate(String id) {
        prefs.edit().putString(PREFS_KEY_AF_START_DT, id).commit();
    }

    public String getAeStartDate() {
        return prefs.getString(PREFS_KEY_AF_START_DT, null);
    }
    public void setAeEndDate(String id) {
        prefs.edit().putString(PREFS_KEY_AF_END_DT, id).commit();
    }

    public String getAeEndDate() {
        return prefs.getString(PREFS_KEY_AF_END_DT, null);
    }


    public boolean isRunNearByApi() {
        return prefs.getBoolean(PREFS_NEAR_BY_API_CALLED, false);
    }
    public void setNearByApiStatus(boolean valus) {
        prefs.edit().putBoolean(PREFS_NEAR_BY_API_CALLED, valus).commit();
    }



    public LatLng getLatLongForCameraPosition() {
        double lat = Double.longBitsToDouble(prefs.getLong(PREFS_IS_LAT_FOR_CAMARA_POSITION, 0));
        double lng = Double.longBitsToDouble(prefs.getLong(PREFS_IS_LONG_FOR_CAMARA_POSITION, 0));
        return new LatLng(lat, lng);
    }

    public void setLatLongForCameraPosition(double lat, double lng) {
        prefs.edit().putLong(PREFS_IS_LAT_FOR_CAMARA_POSITION, Double.doubleToLongBits(lat)).commit();
        prefs.edit().putLong(PREFS_IS_LONG_FOR_CAMARA_POSITION, Double.doubleToLongBits(lng)).commit();
    }

    public LatLng getLatLongForVehicleMapZoom() {
        double lat = Double.longBitsToDouble(prefs.getLong(PREFS_IS_LAT_FOR_ZOOM, 0));
        double lng = Double.longBitsToDouble(prefs.getLong(PREFS_IS_LONG_FOR_ZOOM, 0));
        return new LatLng(lat, lng);
    }

    public void setLatLongForVehicleMapZoom(double lat, double lng) {
        prefs.edit().putLong(PREFS_IS_LAT_FOR_ZOOM, Double.doubleToLongBits(lat)).commit();
        prefs.edit().putLong(PREFS_IS_LONG_FOR_ZOOM, Double.doubleToLongBits(lng)).commit();
    }


    public LatLng getLatLongStartRout() {
        double lat = Double.longBitsToDouble(prefs.getLong(PREFS_IS_LAT_START_POINT, 0));
        double lng = Double.longBitsToDouble(prefs.getLong(PREFS_IS_LONG_START_POINT, 0));
        return new LatLng(lat, lng);
    }

    public void setLatLongStartRout(double lat, double lng) {
        prefs.edit().putLong(PREFS_IS_LAT_START_POINT, Double.doubleToLongBits(lat)).commit();
        prefs.edit().putLong(PREFS_IS_LONG_START_POINT, Double.doubleToLongBits(lng)).commit();
    }


    public LatLng getLatLongWayRout() {
        double lat = Double.longBitsToDouble(prefs.getLong(PREFS_IS_LAT_WAY_POINT, 0));
        double lng = Double.longBitsToDouble(prefs.getLong(PREFS_IS_LONG_WAY_POINT, 0));
        return new LatLng(lat, lng);
    }

    public void setLatLongWayRout(double lat, double lng) {
        prefs.edit().putLong(PREFS_IS_LAT_WAY_POINT, Double.doubleToLongBits(lat)).commit();
        prefs.edit().putLong(PREFS_IS_LONG_WAY_POINT, Double.doubleToLongBits(lng)).commit();
    }

    public LatLng getLatLongEndRout() {
        double lat = Double.longBitsToDouble(prefs.getLong(PREFS_IS_LAT_END_POINT, 0));
        double lng = Double.longBitsToDouble(prefs.getLong(PREFS_IS_LONG_END_POINT, 0));
        return new LatLng(lat, lng);
    }

    public void setLatLongEndRout(double lat, double lng) {
        prefs.edit().putLong(PREFS_IS_LAT_END_POINT, Double.doubleToLongBits(lat)).commit();
        prefs.edit().putLong(PREFS_IS_LONG_END_POINT, Double.doubleToLongBits(lng)).commit();
    }



    public void setVehicleIdForSubmitOne(String id) {
        prefs.edit().putString(PREFS_SUMBMIT_CLICK_VEHICLE_ID, id).commit();
    }

    public String getVehicleIdForSubmitOne() {
        return prefs.getString(PREFS_SUMBMIT_CLICK_VEHICLE_ID, null);
    }




    public boolean isMenuClick() {
        return prefs.getBoolean(PREFS_IS_MENU_CLICK, false);
    }
    public void setIsMenuClick(boolean valus) {
        prefs.edit().putBoolean(PREFS_IS_MENU_CLICK, valus).commit();
    }


    public boolean isApiContainData() {
        return prefs.getBoolean(PREFS_API_CONTEN_DATA, false);
    }
    public void setIsApiContainData(boolean valus) {
        prefs.edit().putBoolean(PREFS_API_CONTEN_DATA, valus).commit();
    }


    public void setMobileForPhotoUpload(String id) {
        prefs.edit().putString(PREFS_MOBILE_NUMBER, id).commit();
    }

    public String getMobileForPhotoUpload() {
        return prefs.getString(PREFS_MOBILE_NUMBER, null);
    }


    public int getWorkOrderPriority() {
        return prefs.getInt(PREFS_WORK_ORDER_PRIORITY, 3);
    }
    public void setWorkOrderPriority(int valus) {
        prefs.edit().putInt(PREFS_WORK_ORDER_PRIORITY, valus).commit();
    }

    public void setWorkOrderAddress(String id) {
        prefs.edit().putString(PREFS_WORK_ORDER_ADDRESS, id).commit();
    }

    public String getWorkOrderAddress() {
        return prefs.getString(PREFS_WORK_ORDER_ADDRESS, null);
    }

    public void setOdometerday(String id) {
        prefs.edit().putString(PREFS_ATTENDNCE_DAY_ODOMETER, id).commit();
    }

    public String getOdometerday() {
        return prefs.getString(PREFS_ATTENDNCE_DAY_ODOMETER, null);
    }


    public void setDayAttendenceTime(String id) {
        prefs.edit().putString(PREFS_ATTENDNCE_DAY_START_TIME, id).commit();
    }

    public String getDayAttendenceTime() {
        return prefs.getString(PREFS_ATTENDNCE_DAY_START_TIME, null);
    }


    public void setDayAttendencedt(String dt) {
        prefs.edit().putString(PREFS_ATTENDNCE_DATE, dt).commit();
    }

    public String getDayAttendencedt() {
        return prefs.getString(PREFS_ATTENDNCE_DATE, null);
    }


    public void setDayAttendenceendtime(String endtime) {
        prefs.edit().putString(PREFS_ATTENDNCE_END_TIME, endtime).commit();
    }


    public String getattendancestarttime() {
        return prefs.getString(PREFS_ATTENDNCE_START_TIME, null);
    }


    public void setattendancestarttime(String starttime) {
        prefs.edit().putString(PREFS_ATTENDNCE_START_TIME, starttime).commit();
    }


    public String getDayAttendenceendtime() {
        return prefs.getString(PREFS_ATTENDNCE_END_TIME, null);
    }

    public void setDayAttendenceendcomment(String comment) {
        prefs.edit().putString(PREFS_ATTENDNCE_COMMENT, comment).commit();
    }

    public String getDayAttendencecomment() {
        return prefs.getString(PREFS_ATTENDNCE_COMMENT, null);
    }

    public void setattendanceid(int valus) {
        prefs.edit().putInt(PREFS_AttendanceID, valus).commit();
    }

    public int getattendanceid() {
        return prefs.getInt(PREFS_AttendanceID, 0);
    }


    public void setWorkOrderId(String id) {
        prefs.edit().putString(PREFS_WORK_ORDER_ID, id).commit();
    }

    public String getWorkOrderId() {
        return prefs.getString(PREFS_WORK_ORDER_ID, null);
    }



    public void setImagePathPersonPhoto(String value) {
        prefs.edit().putString(PREFS_SET_IMAGE_PATH_PERSON_PHOTO, value).commit();
    }




    public String getImagePathPersonPhoto() {
        return prefs.getString(PREFS_SET_IMAGE_PATH_PERSON_PHOTO, null);
    }


    public boolean getISSignatureTaken() {
        return prefs.getBoolean(PREFS_IS_SIGNATURE_TAKEN, false);
    }
    public void setIsSignatureTaker(boolean valus) {
        prefs.edit().putBoolean(PREFS_IS_SIGNATURE_TAKEN, valus).commit();
    }

    public boolean getISrostersigndone() {
        return prefs.getBoolean(PREFS_IS_SIGNATURE_ROSTER, false);
    }
    public void settISrostersigndone(boolean valus) {
        prefs.edit().putBoolean(PREFS_IS_SIGNATURE_ROSTER, valus).commit();
    }
    public void settIStransportcollectionsigndone(boolean valus) {
        prefs.edit().putBoolean(PREFS_IS_SIGNATURE_ROSTER, valus).commit();
    }


    public boolean getISSignatureTakenAfter() {
        return prefs.getBoolean(PREFS_IS_SIGNATURE_TAKEN_AFTER, false);
    }
    public void setIsSignatureTakerAfter(boolean valus) {
        prefs.edit().putBoolean(PREFS_IS_SIGNATURE_TAKEN_AFTER, valus).commit();
    }

    public boolean isFeedBackGiven() {
        return prefs.getBoolean(PREFS_IS_FEEDBACK_GIVE, false);
    }
    public void setIsFeedBackGive(boolean valus) {
        prefs.edit().putBoolean(PREFS_IS_FEEDBACK_GIVE, valus).commit();
    }


    public String getlatestmasterDataBaseId() {
        return prefs.getString(PREFS_IS_DAY_START_END, null);
    }
    public void setlatestmasterDataBaseId(String valus) {
        prefs.edit().putString(PREFS_IS_DAY_START_END, valus).commit();
    }


    public boolean isDayStart() {
        return prefs.getBoolean(PREFS_IS_DAY_START_END, true);
    }
    public void setIsDayStart(boolean valus) {
        prefs.edit().putBoolean(PREFS_IS_DAY_START_END, valus).commit();
    }

    public boolean canattendancebeentered() {
        return prefs.getBoolean(PREFS_CAN_ATTENDANCE_BE_ENTERED, true);
    }
    public void setcanattendancebeentered(boolean valus) {
        prefs.edit().putBoolean(PREFS_CAN_ATTENDANCE_BE_ENTERED, valus).commit();
    }


    public boolean getISRegActiRunning() {
        return prefs.getBoolean(PREFS_IS_REGISTRATION_ACTIVITY_RUNNING, false);
    }
    public void setIsRegActiRunning(boolean valus) {
        prefs.edit().putBoolean(PREFS_IS_REGISTRATION_ACTIVITY_RUNNING, valus).commit();
    }

    public boolean getISVerActiRunning() {
        return prefs.getBoolean(PREFS_IS_VERIFICATION_ACTIVITY_RUNNING, false);
    }
    public void setIsVerActiRunning(boolean valus) {
        prefs.edit().putBoolean(PREFS_IS_VERIFICATION_ACTIVITY_RUNNING, valus).commit();
    }


    public LatLng getGPSLocation() {
        double lat = Double.longBitsToDouble(prefs.getLong(PREFS_KEY_LAT, 0));
        double lng = Double.longBitsToDouble(prefs.getLong(PREFS_KEY_LNG, 0));
        return new LatLng(lat, lng);
    }

    public void setGPSLocation(double lat, double lng) {
        prefs.edit().putLong(PREFS_KEY_LAT, Double.doubleToLongBits(lat)).commit();
        prefs.edit().putLong(PREFS_KEY_LNG, Double.doubleToLongBits(lng)).commit();
    }

    public boolean isGCMIDUploaded() {
        return prefs.getBoolean(PREFS_GCM_UPLOADED, false);
    }

    public void setGCMIDUploaded(boolean value) {
        prefs.edit().putBoolean(PREFS_GCM_UPLOADED, value).commit();
    }

    public boolean getIsFromSenerioBeforeActivity() {
        return prefs.getBoolean(PREFS_IS_FROM_SENERIOBEFORESTARTACTIVITY, false);
    }
    public void setIsFromSenerioBrforeActivity(boolean valus) {
        prefs.edit().putBoolean(PREFS_IS_FROM_SENERIOBEFORESTARTACTIVITY, valus).commit();
    }




    public void setLatLongVehicle(JSONArray arr) {
        prefs.edit().putString(PREFS_LAT_LONG_VEHICLE_ARRAY, arr.toString()).commit();
    }

    public JSONArray getLatLongVehicle() {
        String text = prefs.getString(PREFS_LAT_LONG_VEHICLE_ARRAY, "");
        try {
            return new JSONArray(text.toString());
        } catch (JSONException e) {
            return null;
        }

    }




    public void setIsShowQuicBookHint(boolean value) {
        prefs.edit().putBoolean(PREFS_IS_SHOW_QUICK_BOOK_HINT, value).commit();
    }

    public boolean getIsShowQuickBookHint() {
        return prefs.getBoolean(PREFS_IS_SHOW_QUICK_BOOK_HINT, false);
    }


    public boolean isSaveOfFavClicked() {
        return prefs.getBoolean(PREFS_IS_SAVE_CLICKED_OF_FAV, false);
    }
    public void setSaveOfFavClicked(boolean valus) {
        prefs.edit().putBoolean(PREFS_IS_SAVE_CLICKED_OF_FAV, valus).commit();
    }


    public boolean isEditDetailsWorkOrder() {
        return prefs.getBoolean(PREFS_IS_EDIT_FAV_WO, false);
    }
    public void setEditDetailsWorkOrder(boolean valus) {
        prefs.edit().putBoolean(PREFS_IS_EDIT_FAV_WO, valus).commit();
    }


    public boolean isdayendreasonset() {
        return prefs.getBoolean(PREFS_IS_DAY_END_REASON, false);
    }

    public void setdayendreason(boolean valus) {
        prefs.edit().putBoolean(PREFS_IS_DAY_END_REASON, valus).commit();
    }



    public boolean isEditLocationWorkOrder() {
        return prefs.getBoolean(PREFS_IS_EDIT_LOCATION_WO, false);
    }
    public void setEditLocationWorkOrder(boolean valus) {
        prefs.edit().putBoolean(PREFS_IS_EDIT_LOCATION_WO, valus).commit();
    }

    public boolean isUpdateLocationWorkOrder() {
        return prefs.getBoolean(PREFS_IS_UPDATE_LOCATION_WO, false);
    }
    public void setUpdateLocationWorkOrder(boolean valus) {
        prefs.edit().putBoolean(PREFS_IS_UPDATE_LOCATION_WO, valus).commit();
    }

    public boolean IsLogin() {
        return prefs.getBoolean(PREFS_IS_LOG_IN, false);
    }
    public void setLogin(boolean valus) {
        prefs.edit().putBoolean(PREFS_IS_LOG_IN, valus).commit();
    }
    public void setLateSubmited(boolean valus) {
        prefs.edit().putBoolean(PREFS_IS_LATE_REASON_SUBMITTED, valus).commit();
    }

    public boolean IsLateSubmitted() {
        return prefs.getBoolean(PREFS_IS_LATE_REASON_SUBMITTED, false);
    }


    public void setcashcollected(boolean valus) {
        prefs.edit().putBoolean(PREFS_CASHCOLLECTED, valus).commit();
    }

    public boolean Iscashcollected() {
        return prefs.getBoolean(PREFS_CASHCOLLECTED, false);
    }


    public void setIsShowCoasting(boolean valus) {
        prefs.edit().putBoolean(PREFS_IS_SHOW_COSTING, valus).commit();
    }

    public boolean IsShowCoasting() {
        return prefs.getBoolean(PREFS_IS_SHOW_COSTING, false);
    }


    public void setCallTimer(boolean valus) {
        prefs.edit().putBoolean(PREFS_IS_CALL_API_OF_TIMER, valus).commit();
    }

    public boolean isCallTimer() {
        return prefs.getBoolean(PREFS_IS_CALL_API_OF_TIMER, false);
    }



    public boolean IsBitmapSaved() {
        return prefs.getBoolean(PREFS_BITMAP_SAVED, false);
    }
    public void setIsBitmapSaved(boolean valus) {
        prefs.edit().putBoolean(PREFS_BITMAP_SAVED, valus).commit();
    }

    public boolean isWoMapIconClick() {
        return prefs.getBoolean(PREFS_IS_WO_MAP_ICON_CLICK, false);
    }
    public void setWoMapIconClick(boolean valus) {
        prefs.edit().putBoolean(PREFS_IS_WO_MAP_ICON_CLICK, valus).commit();
    }



    /*
    <!--" ================================ "-->
    <!--     MMTHINKBIZ                      -->
    <!--"====================================="-->

     */

    public boolean IsUpdateNotification() {
        return prefs.getBoolean(PREFS_IS_UPDATE_NOTIFICATION, false);
    }
    public void setIsUpdateNotification(boolean valus) {
        prefs.edit().putBoolean(PREFS_IS_UPDATE_NOTIFICATION, valus).commit();
    }


    /*
    <!--" ================================ "-->
    <!--     MMTHINKBIZ    Registration        -->
    <!--"====================================="-->
     */


    public void setFirstName(String value) {
        prefs.edit().putString(PREFS_REG_FIRST_NAME, value).commit();
    }
    public String getFirstName() {
        return prefs.getString(PREFS_REG_FIRST_NAME, null);
    }


    public void setval1(String value) {
        prefs.edit().putString(PREFS_REG_VAL1, value).commit();
    }
    public String getval1() {
        return prefs.getString(PREFS_REG_VAL1, null);
    }
    public void setval2(String value) {
        prefs.edit().putString(PREFS_REG_VAL2, value).commit();
    }
    public String getval2() {
        return prefs.getString(PREFS_REG_VAL2, null);
    }
    public void setval3(String value) {
        prefs.edit().putString(PREFS_REG_VAL3, value).commit();
    }
    public String getval3() {
        return prefs.getString(PREFS_REG_VAL3, null);
    }

    public void setOdometerapplied(String value) {
        prefs.edit().putString(PREFS_ODOMETER_APPLIED, value).commit();
    }

    public String getodometerapplied() {
        return prefs.getString(PREFS_ODOMETER_APPLIED, null);

    }



    public void setLastName(String value) {
        prefs.edit().putString(PREFS_REG_LAST_NAME, value).commit();
    }

    public String getLastName() {
        return prefs.getString(PREFS_REG_LAST_NAME, null);
    }


    public void setDateOfBirth(String value) {
        prefs.edit().putString(PREFS_REG_DATE_OF_BIRTH, value).commit();
    }

    public String getDateOfBirth() {
        return prefs.getString(PREFS_REG_DATE_OF_BIRTH, null);
    }

    public void setGender(String value) {
        prefs.edit().putString(PREFS_REG_GENDER, value).commit();
    }

    public String getGender() {
        return prefs.getString(PREFS_REG_GENDER, null);
    }

    public void setAddress(String value) {
        prefs.edit().putString(PREFS_REG_ADDRESS, value).commit();
    }

    public String getAddress() {
        return prefs.getString(PREFS_REG_ADDRESS, null);
    }



    public void setCity(String value) {
        prefs.edit().putString(PREFS_REG_CITY, value).commit();
    }

    public String getCity() {
        return prefs.getString(PREFS_REG_CITY, null);
    }


    public void setState(String value) {
        prefs.edit().putString(PREFS_REG_STATE, value).commit();
    }

    public String getState() {
        return prefs.getString(PREFS_REG_STATE, null);
    }


    public void setdefaultbranch(String value) {
        prefs.edit().putString(PREFS_DEFAULT_BRANCH, value).commit();
    }

    public String getdefaultbranch() {
        return prefs.getString(PREFS_DEFAULT_BRANCH, null);
    }


    public void setCountry(String value) {
        prefs.edit().putString(PREFS_REG_COUNTRY, value).commit();
    }

    public String getCountry() {
        return prefs.getString(PREFS_REG_COUNTRY, null);
    }

    public void setZipCode(String value) {
        prefs.edit().putString(PREFS_REG_ZIP_CODE, value).commit();
    }

    public String getZipCode() {
        return prefs.getString(PREFS_REG_ZIP_CODE, null);
    }



    public void setUserId(String value) {
        prefs.edit().putString(PREFS_REG_USER_ID, value).commit();
    }

    public String getUserId() {
        return prefs.getString(PREFS_REG_USER_ID, null);
    }


    public void setPassword(String value) {
        prefs.edit().putString(PREFS_REG_PASSWORD, value).commit();
    }

    public String getPassword() {
        return prefs.getString(PREFS_REG_PASSWORD, null);
    }

    public void setMobileNumber(String value) {
        prefs.edit().putString(PREFS_REG_MOBILE_NUMBER, value).commit();
    }

    public String getMobileNumber() {
        return prefs.getString(PREFS_REG_MOBILE_NUMBER, null);
    }

    public void setAlterNateMobileNumber(String value) {
        prefs.edit().putString(PREFS_REG_ALTERNATE_NUMBER, value).commit();
    }

    public String getAlterNateMobileNumber() {
        return prefs.getString(PREFS_REG_ALTERNATE_NUMBER, null);
    }

    public void setEmailId(String value) {
        prefs.edit().putString(PREFS_REG_EMAIL_ID, value).commit();
    }

    public String getEmailId() {
        return prefs.getString(PREFS_REG_EMAIL_ID, null);
    }

    public void setAlternateEmailId(String value) {
        prefs.edit().putString(PREFS_REG_ALTERNATE_EMAIL_ID, value).commit();
    }

    public String getAlternateEmailId() {
        return prefs.getString(PREFS_REG_ALTERNATE_EMAIL_ID, null);
    }


    public void setSecurityQuestionOne(String value) {
        prefs.edit().putString(PREFS_REG_SECURITY_QUESTION_ONE, value).commit();
    }

    public String getSecurityQuestionOne() {
        return prefs.getString(PREFS_REG_SECURITY_QUESTION_ONE, null);
    }


    public void setSecurityQuestionTwo(String value) {
        prefs.edit().putString(PREFS_REG_SECURITY_QUESTION_TWO, value).commit();
    }

    public String getSecurityQuestionTwo() {
        return prefs.getString(PREFS_REG_SECURITY_QUESTION_TWO, null);
    }




    public void setSecurityAnswerOne(String value) {
        prefs.edit().putString(PREFS_REG_SECURITY_ANS_ONE, value).commit();
    }

    public String getSecurityAnswerOne() {
        return prefs.getString(PREFS_REG_SECURITY_ANS_ONE, null);
    }


    public void setSecurityAnsTwo(String value) {
        prefs.edit().putString(PREFS_REG_SECURITY_ANS_TWO, value).commit();
    }

    public String getSecurityAnswerTwo() {
        return prefs.getString(PREFS_REG_SECURITY_ANS_TWO, null);
    }




    public String getWoEditLolat() {
        return prefs.getString(PREFS_EDIT_WO_LOCATION_LATITUDE, null);
    }


    public void setWoEditLolat(String id) {
        prefs.edit().putString(PREFS_EDIT_WO_LOCATION_LATITUDE, id).commit();
    }

    public String getWoEditLoLong() {
        return prefs.getString(PREFS_EDIT_WO_LOCATION_LONGITUDE, null);
    }


    public void setWoEditLoLong(String id) {
        prefs.edit().putString(PREFS_EDIT_WO_LOCATION_LONGITUDE, id).commit();
    }


    public String getWoEditLoAddress() {
        return prefs.getString(PREFS_EDIT_WO_ADDRESS_, null);
    }


    public void setWoEditLoAddress(String id) {
        prefs.edit().putString(PREFS_EDIT_WO_ADDRESS_, id).commit();
    }




    public LatLng getLatLongEditWoLo() {
        double lat = Double.longBitsToDouble(prefs.getLong(PREFS_EDIT_WO_LOCATION_LATITUDE, 0));
        double lng = Double.longBitsToDouble(prefs.getLong(PREFS_EDIT_WO_LOCATION_LONGITUDE, 0));
        return new LatLng(lat, lng);
    }

    public void setLatLongEditWoLo(double lat, double lng) {
        prefs.edit().putLong(PREFS_EDIT_WO_LOCATION_LATITUDE, Double.doubleToLongBits(lat)).commit();
        prefs.edit().putLong(PREFS_EDIT_WO_LOCATION_LONGITUDE, Double.doubleToLongBits(lng)).commit();
    }


    public LatLng getFirstLatLngEmployeeTracking() {
        double lat = Double.longBitsToDouble(prefs.getLong(PREFS_REG_ALARAM_FIRST_LAT, 0));
        double lng = Double.longBitsToDouble(prefs.getLong(PREFS_REG_ALARAM_FIRST_LNG, 0));
        return new LatLng(lat, lng);
    }

    public void setFirstLatLngEmployeeTracking(double lat, double lng) {
        prefs.edit().putLong(PREFS_REG_ALARAM_FIRST_LAT, Double.doubleToLongBits(lat)).commit();
        prefs.edit().putLong(PREFS_REG_ALARAM_FIRST_LNG, Double.doubleToLongBits(lng)).commit();
    }


    public LatLng getLatLongForEmployeeTracking() {
        double lat = Double.longBitsToDouble(prefs.getLong(PREFS_REG_ALARAM_LAST_LAT, 0));
        double lng = Double.longBitsToDouble(prefs.getLong(PREFS_REG_ALARAM_LAST_LNG, 0));
        return new LatLng(lat, lng);
    }

    public void setLatLongForEmployeeTracking(double lat, double lng) {
        prefs.edit().putLong(PREFS_REG_ALARAM_LAST_LAT, Double.doubleToLongBits(lat)).commit();
        prefs.edit().putLong(PREFS_REG_ALARAM_LAST_LNG, Double.doubleToLongBits(lng)).commit();
    }


    public int getIntervalCount() {
        return prefs.getInt(PREFS_REG_ALARAM_COUNT, 0);
    }
    public void setIntervalCount(int valus) {
        prefs.edit().putInt(PREFS_REG_ALARAM_COUNT, valus).commit();
    }

    public String getTotalDistance() {
        return prefs.getString(PREFS_REG_TOTAL_DISTANCE, "0");
    }
    public void setTotalDistance(String valus) {
        prefs.edit().putString(PREFS_REG_TOTAL_DISTANCE, valus).commit();
    }



    public String getTotalOldDistance() {
        return prefs.getString(PREFS_REG_TOTAL_OLD_DISTANCE, "0");
    }
    public void setTotalOldDistance(String valus) {
        prefs.edit().putString(PREFS_REG_TOTAL_OLD_DISTANCE, valus).commit();
    }























    /*
    <!--" ================================ "-->
    <!--     MMTHINKBIZ    Registration        -->
    <!--"====================================="-->
     */



    public void setTitle(String string) {
        prefs.edit().putString(PREFS_TITLE, string).commit();
    }
    public String getTitle() {
        return prefs.getString(PREFS_TITLE, null);
    }
    public void setEmail(String string) {
        prefs.edit().putString(PREFS_EMAIL, string).commit();
    }
    public String getEmail() {
        return prefs.getString(PREFS_EMAIL, null);
    }
    public void clear(String value) {
        prefs.edit().remove(value).commit();
    }
    public void clearAllPrefs() {
        prefs.edit().clear().commit();
    }



}
