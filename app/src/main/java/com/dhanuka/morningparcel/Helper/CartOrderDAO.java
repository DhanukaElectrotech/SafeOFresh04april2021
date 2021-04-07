package com.dhanuka.morningparcel.Helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.util.ArrayList;


public class CartOrderDAO {

    private static final String TABLE_CART_MASTER = "cart_items";

    // Contacts Table Columns names
    private static final String COLUMN_KEY_ID = "_id";
     private static final String COLUMN_UPLOAD_STATUS = "upload_status";
     private static final String COLUMN_DESCRITPTION = "description";
 

    private SQLiteDatabase mDatabase;
    private Context mContext;

    public CartOrderDAO(SQLiteDatabase database, Context context) {
        mDatabase = database;
        mContext = context;
    }

    public static String getCreateTableCartMaster() {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_CART_MASTER
                + "("
                + COLUMN_KEY_ID + " INTEGER PRIMARY KEY,"
                 + COLUMN_UPLOAD_STATUS + " INTEGER ,"
                 + COLUMN_DESCRITPTION + " TEXT)";

        return CREATE_TABLE;
    }

    public static String getDropTableImageMaster() {
        return "DROP TABLE IF EXISTS " + TABLE_CART_MASTER;
    }

    public void deleteAll() {

        String delete_all = " DELETE "
                + " FROM "
                + TABLE_CART_MASTER;

        mDatabase.execSQL(delete_all);
    }

    public void insert(ArrayList<DBCartDataUpload> arrayList) {

        for (DBCartDataUpload singleInput : arrayList) {


            String[] bindArgs = {
                    singleInput.getmStatus()+"",
                    String.valueOf(singleInput.getmDescription()),


            };

            String insertUser = " INSERT INTO "
                    + TABLE_CART_MASTER
                    + " ( "
                    + COLUMN_UPLOAD_STATUS
                  + " , "
                    + COLUMN_DESCRITPTION
                   + " ) "
                    + " VALUES "
                    + " (?,?)";


            mDatabase.execSQL(insertUser, bindArgs);
        }
    }



    public void deleteMaterPhotoById(int id) {

        String deleteSingleRow = " DELETE "
                + " FROM "
                + TABLE_CART_MASTER
                + " WHERE "
                + COLUMN_KEY_ID
                + " = "
                + id;
        mDatabase.execSQL(deleteSingleRow);
    }


    public int getlatestinsertedid() {
        String countQuery = "SELECT  max(_id) FROM " + TABLE_CART_MASTER;
        Cursor cursor = mDatabase.rawQuery(countQuery, null);

        int maxid=0;
        if (cursor != null) {

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                maxid= cursor.getInt(0);
            }
            closeCursor(cursor);
        }
        return maxid;
        //return cursor.getCount();
    }



    public int getTodoItemCount() {


        String countQuery = "SELECT  * FROM " + TABLE_CART_MASTER;
        Cursor cursor = mDatabase.rawQuery(countQuery, null);
        int cnt=cursor.getCount();
        closeCursor(cursor);
        return cnt;
    }




    public ArrayList<DBCartDataUpload> selectAll() {

        String getAllDetails = " SELECT "
                + " * "
                + " FROM "
                + TABLE_CART_MASTER;

        Cursor cursor = mDatabase.rawQuery(getAllDetails, null);
        ArrayList<DBCartDataUpload> dataList = manageCursor(cursor);
        closeCursor(cursor);

        return dataList;
    }


    protected DBCartDataUpload cursorToData(Cursor cursor) {
        DBCartDataUpload model = new DBCartDataUpload();
        model.setmId(cursor.getInt(cursor.getColumnIndex(COLUMN_KEY_ID)));
         model.setmStatus(cursor.getInt(cursor.getColumnIndex(COLUMN_UPLOAD_STATUS)));
         model.setmDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRITPTION)));
         return model;
    }

    protected void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }

    protected ArrayList<DBCartDataUpload> manageCursor(Cursor cursor) {
        ArrayList<DBCartDataUpload> dataList = new ArrayList<DBCartDataUpload>();

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                DBCartDataUpload singleModel = cursorToData(cursor);
                if (singleModel != null) {
                    dataList.add(singleModel);
                }
                cursor.moveToNext();
            }
        }
        return dataList;
    }

}
