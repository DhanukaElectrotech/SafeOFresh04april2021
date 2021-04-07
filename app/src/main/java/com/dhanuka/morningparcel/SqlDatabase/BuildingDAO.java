package com.dhanuka.morningparcel.SqlDatabase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dhanuka.morningparcel.Helper.CatcodeHelper;

import java.util.ArrayList;

public class BuildingDAO {

    private static final String TABLE_BUILDING = "buildingdao_details";

    // Contacts Table Columns names
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_BUILDING_ID = "building_id";
    private static final String COLUMN_BUILDING_NAME = "building_name";


    private SQLiteDatabase mDatabase;
    private Context mContext;

    public BuildingDAO(SQLiteDatabase database, Context context) {
        mDatabase = database;
        mContext = context;
    }


    public static String getCreateBuildingTable() {
        getDropTableAlbum();
        String CREATE_VEHICLE_DETAILS_TABLE = "CREATE TABLE " + TABLE_BUILDING
                + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_BUILDING_ID + " TEXT ,"
                + COLUMN_BUILDING_NAME + " TEXT)";

        return CREATE_VEHICLE_DETAILS_TABLE;
    }

    public static String getDropTableAlbum() {
        return "DROP TABLE IF EXISTS " + TABLE_BUILDING;
    }


    public void deleteAll() {

        String delete_all_users = " DELETE "
                + " FROM "
                + TABLE_BUILDING;

        mDatabase.execSQL(delete_all_users);
    }


    public void insert(ArrayList<CatcodeHelper> vehicleArrayList) {

        for (CatcodeHelper address : vehicleArrayList) {


            String[] bindArgs = {
                    address.getBuildingid(),
                    address.getBuildingname(),
            };

            String insertUser = " INSERT INTO "
                    + TABLE_BUILDING
                    + " ( "
                    + COLUMN_BUILDING_ID
                    + " , "
                    + COLUMN_BUILDING_NAME
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
                + TABLE_BUILDING;

        Cursor cursor = mDatabase.rawQuery(getAllourceDetails, null);
        ArrayList<CatcodeHelper> dataList = manageCursor(cursor);
        closeCursor(cursor);
        //9461757476
        //Mohit Tester
        return dataList;
    }

    public ArrayList<CatcodeHelper> selectids() {

        String getAllidsDetails = " SELECT "
                + COLUMN_BUILDING_ID
                + " FROM "
                + TABLE_BUILDING;

        Cursor cursor = mDatabase.rawQuery(getAllidsDetails, null);
        ArrayList<CatcodeHelper> dataList = manageCursor(cursor);
        closeCursor(cursor);

        return dataList;
    }








    protected CatcodeHelper cursorToData(Cursor cursor) {
        CatcodeHelper source = new CatcodeHelper();
        source.setmId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
        source.setBuildingid(cursor.getString(cursor.getColumnIndex(COLUMN_BUILDING_ID)));
        source.setBuildingname(cursor.getString(cursor.getColumnIndex(COLUMN_BUILDING_NAME)));

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
