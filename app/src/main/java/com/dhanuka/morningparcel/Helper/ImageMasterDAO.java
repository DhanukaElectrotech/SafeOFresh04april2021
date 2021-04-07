package com.dhanuka.morningparcel.Helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dhanuka.morningparcel.utils.JKHelper;

import java.util.ArrayList;


public class ImageMasterDAO {

    private static final String TABLE_IMAGE_MASTER = "image_master";

    // Contacts Table Columns names
    private static final String COLUMN_KEY_ID = "_id";
    private static final String COLUMN_IMAGE_TYPE = "type";
    private static final String COLUMN_UPLOAD_STATUS = "upload_status";
    private static final String COLUMN_SERVER_ID = "server_id";
    private static final String COLUMN_DESCRITPTION = "description";
    private static final String COLUMN_DATE = "date";


    private SQLiteDatabase mDatabase;
    private Context mContext;

    public ImageMasterDAO(SQLiteDatabase database, Context context) {
        mDatabase = database;
        mContext = context;
    }

    public static String getCreateTableImageMaster() {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_IMAGE_MASTER
                + "("
                + COLUMN_KEY_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_IMAGE_TYPE + " TEXT ,"
                + COLUMN_UPLOAD_STATUS + " INTEGER ,"
                + COLUMN_SERVER_ID + " TEXT ,"
                + COLUMN_DESCRITPTION + " TEXT ,"
                + COLUMN_DATE + " TEXT)";

        return CREATE_TABLE;
    }

    public static String getDropTableImageMaster() {
        return "DROP TABLE IF EXISTS " + TABLE_IMAGE_MASTER;
    }

    public void deleteAll() {

        String delete_all = " DELETE "
                + " FROM "
                + TABLE_IMAGE_MASTER;

        mDatabase.execSQL(delete_all);
    }

    public void insert(ArrayList<DbImageMaster> arrayList) {

        for (DbImageMaster singleInput : arrayList) {


            String[] bindArgs = {
                    singleInput.getmImageType(),
                    String.valueOf(singleInput.getmUploadStatus()),
                    String.valueOf(singleInput.getmServerId()),
                    singleInput.getmDescription(),
                    singleInput.getmDate(),

            };

            String insertUser = " INSERT INTO "
                    + TABLE_IMAGE_MASTER
                    + " ( "
                    + COLUMN_IMAGE_TYPE
                    + " , "
                    + COLUMN_UPLOAD_STATUS
                    + " , "
                    + COLUMN_SERVER_ID
                    + " , "
                    + COLUMN_DESCRITPTION
                    + " , "
                    + COLUMN_DATE
                    + " ) "
                    + " VALUES "
                    + " (?,?,?,?,?)";


            mDatabase.execSQL(insertUser, bindArgs);
        }
    }

    public void setServerDetails(int id, int serverId, int isUploaded) {

        //int checkMark = result ? 1 : 0;

        String[] bindArgs = {
                String.valueOf(serverId),
                String.valueOf(isUploaded),
                String.valueOf(id)
        };
        String update = " UPDATE "
                + TABLE_IMAGE_MASTER
                + " SET "
                + COLUMN_SERVER_ID
                + " = ?, "
                + COLUMN_UPLOAD_STATUS
                + " = ? WHERE " + COLUMN_KEY_ID + "= ?";
        mDatabase.execSQL(update, bindArgs);

    }


    public void deleteMaterPhotoById(int id) {

        String deleteSingleRow = " DELETE "
                + " FROM "
                + TABLE_IMAGE_MASTER
                + " WHERE "
                + COLUMN_KEY_ID
                + " = "
                + id;
        mDatabase.execSQL(deleteSingleRow);
    }


    public int getlatestinsertedid() {
        String countQuery = "SELECT  max(_id) FROM " + TABLE_IMAGE_MASTER;
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


        String countQuery = "SELECT  * FROM " + TABLE_IMAGE_MASTER;
        Cursor cursor = mDatabase.rawQuery(countQuery, null);
        int cnt=cursor.getCount();
        closeCursor(cursor);
        return cnt;
    }


    public int getAvailableMasterId(String serverId, String type, String description) {


        String count = " SELECT "
                + COLUMN_KEY_ID
                + " FROM "
                + TABLE_IMAGE_MASTER
                + " WHERE "
                + COLUMN_SERVER_ID
                + " = '"
                + serverId
                + "' AND "
                + COLUMN_IMAGE_TYPE
                + " = '"
                + type
                + "' AND "
                + COLUMN_DESCRITPTION
                + " = '"
                + description + "'";


        Cursor cursor = mDatabase.rawQuery(count, null);
        int cnt = 0;
        if (cursor != null) {

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                cnt = cursor.getInt(0);
            }
            closeCursor(cursor);
        }

        return cnt;
    }


    public ArrayList<DbImageMaster> selectAll() {

        String getAllDetails = " SELECT "
                + " * "
                + " FROM "
                + TABLE_IMAGE_MASTER;

        Cursor cursor = mDatabase.rawQuery(getAllDetails, null);
        ArrayList<DbImageMaster> dataList = manageCursor(cursor);
        closeCursor(cursor);

        return dataList;
    }

    public String getServerIdById(int id) {

        String serverId = "";
        String getAllDetails = " SELECT "
                + COLUMN_SERVER_ID
                + " FROM "
                + TABLE_IMAGE_MASTER + " WHERE " + COLUMN_KEY_ID + " = " + id;

        if(mDatabase!=null){
            JKHelper.showLog("NOT NULL");
        }else{
            JKHelper.showLog("NULL");
        }
        Cursor cursor = mDatabase.rawQuery(getAllDetails, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                serverId = cursor.getString(0);
            }
        }
        closeCursor(cursor);
        JKHelper.showLog("SERVER ID"+serverId);
        return serverId;
    }

    protected DbImageMaster cursorToData(Cursor cursor) {
        DbImageMaster model = new DbImageMaster();
        model.setmId(cursor.getInt(cursor.getColumnIndex(COLUMN_KEY_ID)));
        model.setmImageType(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_TYPE)));
        model.setmUploadStatus(cursor.getInt(cursor.getColumnIndex(COLUMN_UPLOAD_STATUS)));
        model.setmServerId(cursor.getString(cursor.getColumnIndex(COLUMN_SERVER_ID)));
        model.setmDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRITPTION)));
        model.setmDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
        return model;
    }

    protected void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }

    protected ArrayList<DbImageMaster> manageCursor(Cursor cursor) {
        ArrayList<DbImageMaster> dataList = new ArrayList<DbImageMaster>();

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                DbImageMaster singleModel = cursorToData(cursor);
                if (singleModel != null) {
                    dataList.add(singleModel);
                }
                cursor.moveToNext();
            }
        }
        return dataList;
    }

}
