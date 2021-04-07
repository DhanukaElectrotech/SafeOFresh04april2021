package com.dhanuka.morningparcel.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import com.dhanuka.morningparcel.activity.retail.beans.dbitemmaster;
import com.dhanuka.morningparcel.utils.log;

public class itemmasterdao {
    private static final String TABLE_NAME= "Item_Master";

    // Contacts Table Columns names
    private static final String COLUMN_KEY_ID = "_id";
    private static final String COLUMN_ItemID="ItemID";
    private static final String COLUMN_ItemName="ItemName";
    private static final String COLUMN_companycosting="companycosting";
    private static final String COLUMN_ByItemNumber="ByItemNumber";
    private static final String COLUMN_GroupID="GroupID";
    private static final String COLUMN_RoundKG="RoundKG";
    private static final String COLUMN_AddWeightKG="AddWeightKG";
    private static final String COLUMN_AllString="AllString";

    private SQLiteDatabase mDatabase;
    private Context mContext;

    public itemmasterdao(SQLiteDatabase database, Context context) {
        mDatabase = database;
        mContext = context;
    }

    public static String getCreateTableUpload() {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
                + "("
                + COLUMN_KEY_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_ItemID  + " TEXT ,"
                + COLUMN_ItemName  + " TEXT ,"
                + COLUMN_companycosting  + " TEXT ,"
                + COLUMN_ByItemNumber  + " TEXT ,"
                + COLUMN_GroupID  + " TEXT ,"
                + COLUMN_RoundKG  + " TEXT ,"
                + COLUMN_AddWeightKG  + " TEXT ,"
                + COLUMN_AllString  + " TEXT )";

        return CREATE_TABLE;
    }

    public static String getDropTableUpload() {
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public void deleteAll() {

        String delete_all = " DELETE "
                + " FROM "
                + TABLE_NAME;
        mDatabase.execSQL(delete_all);
    }

    public void insert(ArrayList<dbitemmaster> arrayList) {

        for (dbitemmaster singleInput : arrayList) {
            String[] bindArgs = {
                    String.valueOf(singleInput.getmItemID()),
                    String.valueOf(singleInput.getmItemName()),
                    String.valueOf(singleInput.getmcompanycosting()),
                    String.valueOf(singleInput.getmByItemNumber()),
                    String.valueOf(singleInput.getmGroupID()),
                    String.valueOf(singleInput.getmRoundKG()),
                    String.valueOf(singleInput.getmAddWeightKG()),
                    String.valueOf(singleInput.getmAllString()),
            };

            String insertUser = " INSERT INTO "
                    + TABLE_NAME
                    + " ( "
                    + COLUMN_ItemID + " , "
                    + COLUMN_ItemName + " , "
                    + COLUMN_companycosting + " , "
                    + COLUMN_ByItemNumber + " , "
                    + COLUMN_GroupID + " , "
                    + COLUMN_RoundKG + " , "
                    + COLUMN_AddWeightKG + " , "
                    + COLUMN_AllString
                    + " ) "
                    + " VALUES "
                    + " (?,?,?,?,?,?,?,?)";
            mDatabase.execSQL(insertUser, bindArgs);
        }
    }


    public int getCurrentItemCount(String id) {
        log.e("id in database=="+id);
        String[] bindArgs = {
                id
        };

        String countQuery = " SELECT  * FROM "
                + TABLE_NAME
                + " WHERE "
                + COLUMN_KEY_ID
                + "= ?";

        Cursor cursor = mDatabase.rawQuery(countQuery, bindArgs);

        return cursor.getCount();
    }

    public ArrayList<dbitemmaster> selectAll() {

        String getAllDetails = " SELECT "
                + " * "
                + " FROM "
                + TABLE_NAME;

        Cursor cursor = mDatabase.rawQuery(getAllDetails, null);
        ArrayList<dbitemmaster> dataList = manageCursor(cursor);
        closeCursor(cursor);

        return dataList;
    }


    public ArrayList<dbitemmaster> selectAllLastIdPhotos(String id) {

        String[] bindArgs = {
                id
        };

        String getAllDetails = " SELECT "
                + " * "
                + " FROM "
                + TABLE_NAME
                + " WHERE "
                + COLUMN_KEY_ID
                + " = ?";
        ;

        Cursor cursor = mDatabase.rawQuery(getAllDetails, bindArgs);
        ArrayList<dbitemmaster> dataList = manageCursor(cursor);
        closeCursor(cursor);

        return dataList;
    }

    public ArrayList<dbitemmaster> selectUploadPhotos() {
        String getAllDetails = " SELECT "
                + " * "
                + " FROM "
                + TABLE_NAME
                + " WHERE "
                + COLUMN_ItemName
                + " = 0";
        Cursor cursor = mDatabase.rawQuery(getAllDetails, null);
        ArrayList<dbitemmaster> dataList = manageCursor(cursor);
        closeCursor(cursor);

        return dataList;
    }

    protected dbitemmaster cursorToData(Cursor cursor) {
        dbitemmaster model = new dbitemmaster();
        //model.setmId(cursor.getInt(cursor.getColumnIndex(COLUMN_KEY_ID)));
        model.setmItemID(cursor.getString(cursor.getColumnIndex(COLUMN_ItemID)));
        model.setmItemName(cursor.getString(cursor.getColumnIndex(COLUMN_ItemName)));
        model.setmcompanycosting(cursor.getString(cursor.getColumnIndex(COLUMN_companycosting)));
        model.setmByItemNumber(cursor.getString(cursor.getColumnIndex(COLUMN_ByItemNumber)));
        model.setmGroupID(cursor.getString(cursor.getColumnIndex(COLUMN_GroupID)));
        model.setmRoundKG(cursor.getString(cursor.getColumnIndex(COLUMN_RoundKG)));
        model.setmAddWeightKG(cursor.getString(cursor.getColumnIndex(COLUMN_AddWeightKG)));
        model.setmAllString(cursor.getString(cursor.getColumnIndex(COLUMN_AllString)));
        return model;
    }

    protected void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }

    protected ArrayList<dbitemmaster> manageCursor(Cursor cursor) {
        ArrayList<dbitemmaster> dataList = new ArrayList<dbitemmaster>();

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                dbitemmaster singleModel = cursorToData(cursor);
                if (singleModel != null) {
                    dataList.add(singleModel);
                }
                cursor.moveToNext();
            }
        }
        return dataList;
    }

}
