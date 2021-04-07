package com.dhanuka.morningparcel.SqlDatabase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dhanuka.morningparcel.Helper.CatcodeHelper;

import java.util.ArrayList;

public class SocietyDao {

    private static final String TABLE_STATE_NAME = "societydao_details";

    // Contacts Table Columns names
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_SOCIETY_ID = "soceity_id";
    private static final String COLUMN_SOCIETY_NAME = "society_name";


    private SQLiteDatabase mDatabase;
    private Context mContext;

    public SocietyDao(SQLiteDatabase database, Context context) {
        mDatabase = database;
        mContext = context;
    }


    public static String getCreatesocietyTable() {
        getDropTableAlbum();
        String CREATE_VEHICLE_DETAILS_TABLE = "CREATE TABLE " + TABLE_STATE_NAME
                + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_SOCIETY_ID + " TEXT ,"
                + COLUMN_SOCIETY_NAME + " TEXT)";

        return CREATE_VEHICLE_DETAILS_TABLE;
    }

    public static String getDropTableAlbum() {
        return "DROP TABLE IF EXISTS " + TABLE_STATE_NAME;
    }


    public void deleteAll() {

        String delete_all_users = " DELETE "
                + " FROM "
                + TABLE_STATE_NAME;

        mDatabase.execSQL(delete_all_users);
    }


    public void insert(ArrayList<CatcodeHelper> vehicleArrayList) {

        for (CatcodeHelper address : vehicleArrayList) {


            String[] bindArgs = {
                    address.getSoceityid(),
                    address.getSocietynames(),
            };

            String insertUser = " INSERT INTO "
                    + TABLE_STATE_NAME
                    + " ( "
                    + COLUMN_SOCIETY_ID
                    + " , "
                    + COLUMN_SOCIETY_NAME
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
                + TABLE_STATE_NAME;

        Cursor cursor = mDatabase.rawQuery(getAllourceDetails, null);
        ArrayList<CatcodeHelper> dataList = manageCursor(cursor);
        closeCursor(cursor);
        //9461757476
        //Mohit Tester
        return dataList;
    }

    public ArrayList<CatcodeHelper> selectids() {

        String getAllidsDetails = " SELECT "
                + COLUMN_SOCIETY_ID
                + " FROM "
                + TABLE_STATE_NAME;

        Cursor cursor = mDatabase.rawQuery(getAllidsDetails, null);
        ArrayList<CatcodeHelper> dataList = manageCursor(cursor);
        closeCursor(cursor);

        return dataList;
    }








    protected CatcodeHelper cursorToData(Cursor cursor) {
        CatcodeHelper source = new CatcodeHelper();
        source.setmId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
        source.setSoceityid(cursor.getString(cursor.getColumnIndex(COLUMN_SOCIETY_ID)));
        source.setSocietynames(cursor.getString(cursor.getColumnIndex(COLUMN_SOCIETY_NAME)));

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
