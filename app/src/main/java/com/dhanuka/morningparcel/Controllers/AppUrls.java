package com.dhanuka.morningparcel.Controllers;


public class AppUrls {
    public static final String URL_NOTIFICATION_REGISTER = "http://mmthinkbiz.com/MobileService.aspx?method=GCMRegister";
    public static final String URL_ALL_VEHICLE_LIST = "method=Allcompanyvehiclelist";
    public static final String URL_ALL_VENDOR_LIST = "method=fillvendor";

    public static final String GET_OPEN_WO = "method=GetDetailRequestWO";
    public static final String GET_UOM_ITEM = "method=All_ItemUOM";
    public static final String GET_PURCHASE_ORDER_ITEMS="method=ItemMargin";
    public static final String SEND_SMS = "method=SafeokartBulkSMS";
    public static final String SEND_NOTIFICATION = "method=BulkNotificationSafeokart";

    public static final String GET_USER_LIST = "method=GetUserList";

    public static final String GET_ITEM_MASTER_URL = "method=All_Item_Master_MP";
    public static final String GET_STOCK_COUNT = "method=StockCount";

    public static final String GET_REASON_LIST = "method=GetCatCode";
    public static final String URL_FILL_CONSIGNEE = "method=Fillconsignee";
    public static final String URL_ALL_SOURCE_DEST = "method=GeofenceNames";
    public static final String URL_WORK_CREATE_WORKORDER = "method=newWO";

    public static final String URL_VEHICLE_LOCATION = "method=vehiclelocation";
    public static final String URL_VEHICLE_RUNNING_DETAILS = "method=vehiclerunning";

    public static String getValTypeFromDesc(String description) {

        String valType = "10";
        if (description.equalsIgnoreCase("WO Before Status")) {
            valType = "10";
        } else if (description.equalsIgnoreCase("WO After Status")) {
            valType = "11";
        } else if (description.equalsIgnoreCase("WO Before Client Signature")) {
            valType = "12";
        } else if (description.equalsIgnoreCase("WO After Client Signature")) {
            valType = "13";
        } else if (description.equalsIgnoreCase("WO Item Image")) {
            valType = "14";
        } else if (description.equalsIgnoreCase("Client WO Created")) {
            valType = "17";
        } else if (description.equalsIgnoreCase("Coming Late")) {
            valType = "19";
        } else if (description.equalsIgnoreCase("Attendance Start")) {
            valType = "22";
        } else if (description.equalsIgnoreCase("Attendance Close")) {
            valType = "23";
        } else if (description.equalsIgnoreCase("Expenditures")) {
            valType = "26";
        }
        return valType;
    }
}
