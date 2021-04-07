package com.dhanuka.morningparcel.SqlDatabase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dhanuka.morningparcel.Helper.CatcodeHelper;

import java.util.ArrayList;

public class CityDAO {

    private static final String TABLE_CITY = "citydao_details";

    // Contacts Table Columns names
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_CITY_ID = "city_id";
    private static final String COLUMN_CITY_NAME = "city_name";


    private SQLiteDatabase mDatabase;
    private Context mContext;

    public CityDAO(SQLiteDatabase database, Context context) {
        mDatabase = database;
        mContext = context;
    }


    public static String getCreateBuildingTable() {
        getDropTableAlbum();
        String CREATE_VEHICLE_DETAILS_TABLE = "CREATE TABLE " + TABLE_CITY
                + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_CITY_ID + " TEXT ,"
                + COLUMN_CITY_NAME + " TEXT)";

        return CREATE_VEHICLE_DETAILS_TABLE;
    }

    public static String getDropTableAlbum() {
        return "DROP TABLE IF EXISTS " + TABLE_CITY;
    }


    public void deleteAll() {

        String delete_all_users = " DELETE "
                + " FROM "
                + TABLE_CITY;

        mDatabase.execSQL(delete_all_users);
    }


    public void insert(ArrayList<CatcodeHelper> vehicleArrayList) {

        for (CatcodeHelper address : vehicleArrayList) {


            String[] bindArgs = {
                    address.getCityid(),
                    address.getCityname(),
            };

            String insertUser = " INSERT INTO "
                    + TABLE_CITY
                    + " ( "
                    + COLUMN_CITY_ID
                    + " , "
                    + COLUMN_CITY_NAME
                    + " ) "
                    + " VALUES "
                    + " (?,?)";


            mDatabase.execSQL(insertUser, bindArgs);
        }
    }



    public ArrayList<CatcodeHelper> selectAll() {

        String getAllourceDetails = " SELECT "
                + " * "
                + " FROM "
                + TABLE_CITY;

        Cursor cursor = mDatabase.rawQuery(getAllourceDetails, null);
        ArrayList<CatcodeHelper> dataList = manageCursor(cursor);
        closeCursor(cursor);
        //9461757476
        //Mohit Tester
        return dataList;
    }

    public ArrayList<CatcodeHelper> selectids() {

        String getAllidsDetails = " SELECT "
                + COLUMN_CITY_ID
                + " FROM "
                + TABLE_CITY;

        Cursor cursor = mDatabase.rawQuery(getAllidsDetails, null);
        ArrayList<CatcodeHelper> dataList = manageCursor(cursor);
        closeCursor(cursor);

        return dataList;
    }








    protected CatcodeHelper cursorToData(Cursor cursor) {
        CatcodeHelper source = new CatcodeHelper();
        source.setmId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
        source.setCityid(cursor.getString(cursor.getColumnIndex(COLUMN_CITY_ID)));
        source.setCityname(cursor.getString(cursor.getColumnIndex(COLUMN_CITY_NAME)));

        return source;
    }

    protected void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }

    protected ArrayList<CatcodeHelper> manageCursor(Cursor cursor) {
        ArrayList<CatcodeHelper> dataList = new ArrayList<CatcodeHelper>();

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                CatcodeHelper CatcodeHelper = cursorToData(cursor);
                if (CatcodeHelper != null) {
                    dataList.add(CatcodeHelper);
                }
                cursor.moveToNext();
            }
        }
        return dataList;
    }






}
