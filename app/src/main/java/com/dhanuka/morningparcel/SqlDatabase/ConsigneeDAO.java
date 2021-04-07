package com.dhanuka.morningparcel.SqlDatabase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dhanuka.morningparcel.Helper.ConsigneeHelper;

import java.util.ArrayList;

public class ConsigneeDAO {

    private static final String TABLE_CONSIGNOR = "consignee_details";

    // Contacts Table Columns names
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_DESCRP_ID = "device_id";
    private static final String COLUMN_CONSIGNEE = "consignee";


    private SQLiteDatabase mDatabase;
    private Context mContext;

    public ConsigneeDAO(SQLiteDatabase database, Context context) {
        mDatabase = database;
        mContext = context;
    }


    public static String getCreateTableConsignee() {
        getDropTableAlbum();
        String CREATE_VEHICLE_DETAILS_TABLE = "CREATE TABLE " + TABLE_CONSIGNOR
                + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_DESCRP_ID + " TEXT ,"
                + COLUMN_CONSIGNEE + " TEXT)";

        return CREATE_VEHICLE_DETAILS_TABLE;
    }

    public static String getDropTableAlbum() {
        return "DROP TABLE IF EXISTS " + TABLE_CONSIGNOR;
    }


    public void deleteAll() {

        String delete_all_users = " DELETE "
                + " FROM "
                + TABLE_CONSIGNOR;

        mDatabase.execSQL(delete_all_users);
    }


    public void insert(ArrayList<ConsigneeHelper> vehicleArrayList) {

        for (ConsigneeHelper address : vehicleArrayList) {


            String[] bindArgs = {
                    address.getmDeviceId(),
                    address.getmConsignornames(),
            };

            String insertUser = " INSERT INTO "
                    + TABLE_CONSIGNOR
                    + " ( "
                    + COLUMN_DESCRP_ID
                    + " , "
                    + COLUMN_CONSIGNEE
                    + " ) "
                    + " VALUES "
                    + " (?,?)";


            mDatabase.execSQL(insertUser, bindArgs);
        }
    }



    public ArrayList<ConsigneeHelper> selectAll() {

        String getAllourceDetails = " SELECT "
                + " * "
                + " FROM "
                + TABLE_CONSIGNOR;

        Cursor cursor = mDatabase.rawQuery(getAllourceDetails, null);
        ArrayList<ConsigneeHelper> dataList = manageCursor(cursor);
        closeCursor(cursor);

        return dataList;
    }

    public ArrayList<ConsigneeHelper> selectids() {

        String getAllidsDetails = " SELECT "
                + COLUMN_DESCRP_ID
                + " FROM "
                + TABLE_CONSIGNOR;

        Cursor cursor = mDatabase.rawQuery(getAllidsDetails, null);
        ArrayList<ConsigneeHelper> dataList = manageCursor(cursor);
        closeCursor(cursor);

        return dataList;
    }








    protected ConsigneeHelper cursorToData(Cursor cursor) {
        ConsigneeHelper source = new ConsigneeHelper();
        source.setmId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
        source.setmDeviceId(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRP_ID)));
        source.setmConsignornames(cursor.getString(cursor.getColumnIndex(COLUMN_CONSIGNEE)));

        return source;
    }

    protected void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }

    protected ArrayList<ConsigneeHelper> manageCursor(Cursor cursor) {
        ArrayList<ConsigneeHelper> dataList = new ArrayList<ConsigneeHelper>();

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                ConsigneeHelper ConsigneeHelper = cursorToData(cursor);
                if (ConsigneeHelper != null) {
                    dataList.add(ConsigneeHelper);
                }
                cursor.moveToNext();
            }
        }
        return dataList;
    }






}
