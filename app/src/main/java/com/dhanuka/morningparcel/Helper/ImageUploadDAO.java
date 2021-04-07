package com.dhanuka.morningparcel.Helper;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dhanuka.morningparcel.utils.DbImageUpload;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class ImageUploadDAO {

    private static final String TABLE_IMAGE_UPLOAD = "image_upload";
    //private static final String TABLE_IMAGE_UPLOAD1 = "image_master";

    // Contacts Table Columns names
    private static final String COLUMN_KEY_ID = "_id";
    private static final String COLUMN_IMAGE_ID = "image_id";
    private static final String COLUMN_IMAGE_NAME = "image_name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_IMAGE_PATH = "image_path";
    private static final String COLUMN_IMAGE_TYPE = "image_type";
    private static final String COLUMN_LAT = "lat";
    private static final String COLUMN_LNG = "lng";
    private static final String COLUMN_IMAGE_UPLOAD_STATUS = "upload_status";
    private static final String COLUMN_SERVER_ID = "server_id";
    private static final String COLUMN_DATE = "date";
    private SQLiteDatabase mDatabase;
    private Context mContext;

    public ImageUploadDAO(SQLiteDatabase database, Context context) {
        mDatabase = database;
        mContext = context;
    }

    public static String getCreateTableImageUpload() {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_IMAGE_UPLOAD
                + "("
                + COLUMN_KEY_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_IMAGE_ID + " INTEGER ,"
                + COLUMN_IMAGE_NAME + " TEXT ,"
                + COLUMN_DESCRIPTION + " TEXT ,"
                + COLUMN_IMAGE_PATH + " TEXT ,"
                + COLUMN_IMAGE_TYPE + " TEXT ,"
                + COLUMN_LAT + " TEXT ,"
                + COLUMN_LNG + " TEXT ,"
                + COLUMN_IMAGE_UPLOAD_STATUS + " INTEGER ,"
                + COLUMN_SERVER_ID + " TEXT ,"
                + COLUMN_DATE + " TEXT)";

        return CREATE_TABLE;
    }

    public static String getDropTableImageUpload() {
        return "DROP TABLE IF EXISTS " + TABLE_IMAGE_UPLOAD;
    }

    public void deleteAll() {

        String delete_all = " DELETE "
                + " FROM "
                + TABLE_IMAGE_UPLOAD;

        mDatabase.execSQL(delete_all);
    }

    public void insert(ArrayList<DbImageUpload> arrayList) {

        for (DbImageUpload singleInput : arrayList) {


            String[] bindArgs = {
                    String.valueOf(singleInput.getmImageId()),
                    singleInput.getmImageName(),
                    singleInput.getmDescription(),
                    singleInput.getmImagePath(),
                    singleInput.getmImageType(),
                    singleInput.getmLat(),
                    singleInput.getmLong(),
                    String.valueOf(singleInput.getmImageUploadStatus()),
                    String.valueOf(singleInput.getmServerId()),
                    singleInput.getmDate(),
            };

            String insertUser = " INSERT INTO "
                    + TABLE_IMAGE_UPLOAD
                    + " ( "
                    + COLUMN_IMAGE_ID
                    + " , "
                    + COLUMN_IMAGE_NAME
                    + " , "
                    + COLUMN_DESCRIPTION
                    + " , "
                    + COLUMN_IMAGE_PATH
                    + " , "
                    + COLUMN_IMAGE_TYPE
                    + " , "
                    + COLUMN_LAT
                    + " , "
                    + COLUMN_LNG
                    + " , "
                    + COLUMN_IMAGE_UPLOAD_STATUS
                    + " , "
                    + COLUMN_SERVER_ID
                    + " , "
                    + COLUMN_DATE
                    + " ) "
                    + " VALUES "
                    + " (?,?,?,?,?,?,?,?,?,?)";

            mDatabase.execSQL(insertUser, bindArgs);
        }
    }

    public void setWorkIdToTable(String serverId, String masterId) {
        //int checkMark = result ? 1 : 0;
        String[] bindArgs = {
                serverId,
                String.valueOf(masterId)
        };
        String update = " UPDATE "
                + TABLE_IMAGE_UPLOAD
                + " SET "
                + COLUMN_SERVER_ID
                + " = ? WHERE " + COLUMN_IMAGE_ID + "= ?";


        mDatabase.execSQL(update, bindArgs);
    }



    public void setServerDetails(int id, String serverId, int isUploaded) {

        //int checkMark = result ? 1 : 0;

        String[] bindArgs = {
                serverId,
                String.valueOf(isUploaded),
                String.valueOf(id)
        };
        String update = " UPDATE "
                + TABLE_IMAGE_UPLOAD
                + " SET "
                + COLUMN_SERVER_ID
                + " = ?, "
                + COLUMN_IMAGE_UPLOAD_STATUS
                + " = ? WHERE " + COLUMN_KEY_ID + "= ?";
        mDatabase.execSQL(update, bindArgs);

    }

    public void updateSignBitmap(String id, String imageName, String imagePath, String imageDate) {

        String[] bindArgs = {
                imageName,
                imagePath,
                imageDate,
                id,
        };

        String update = " UPDATE "
                + TABLE_IMAGE_UPLOAD
                + " SET "
                + COLUMN_IMAGE_NAME
                + " = ?, "
                + COLUMN_IMAGE_PATH
                + " = ?, "
                + COLUMN_DATE
                + " = ? WHERE "
                + COLUMN_IMAGE_ID + "= ?";

        mDatabase.execSQL(update, bindArgs);

    }

    public void deleteUploadedPhotoById(int id) {

        String deleteSingleRow = " DELETE "
                + " FROM "
                + TABLE_IMAGE_UPLOAD
                + " WHERE "
                + COLUMN_KEY_ID
                + " = "
                + id;
        mDatabase.execSQL(deleteSingleRow);
    }


    public void deleteUploadedPhotoByName(String value) {

        String deleteSingleRow = " DELETE "
                + " FROM "
                + TABLE_IMAGE_UPLOAD
                + " WHERE "
                + COLUMN_IMAGE_NAME
                + " = '"
                + value + "'";
        mDatabase.execSQL(deleteSingleRow);
    }


    public Queue<DBImageUpload> getNewUploadList(String id) {
        Queue<DBImageUpload> queue = new LinkedList<>();
        String getAllPhotoDetails = " SELECT "
                + " * "
                + " FROM "
                + TABLE_IMAGE_UPLOAD + " WHERE " + COLUMN_IMAGE_UPLOAD_STATUS + " = 0 AND " + COLUMN_IMAGE_ID + " =" + id;
        Cursor cursor = mDatabase.rawQuery(getAllPhotoDetails, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                DBImageUpload modle = cursorToData(cursor);
                if (modle != null) {
                    File file = new File(modle.getmImagePath());
                    if (file.exists()) {
                        queue.add(modle);
                    }

                }
                cursor.moveToNext();
            }
        }
        closeCursor(cursor);
        return queue;
    }


//
//
//
//
//    public Queue<DBImageUpload> getNewUploadList() {
//        Queue<DBImageUpload> queue = new LinkedList<>();
//        String getAllPhotoDetails = " SELECT "
//                + " * "
//                + " FROM "
//                + TABLE_IMAGE_UPLOAD + " WHERE " + COLUMN_IMAGE_UPLOAD_STATUS + " = 0 ";
//        Cursor cursor = mDatabase.rawQuery(getAllPhotoDetails, null);
//        if (cursor != null) {
//            cursor.moveToFirst();
//            while (!cursor.isAfterLast()) {
//                DBImageUpload modle = cursorToData(cursor);
//                if (modle != null) {
//                    File file = new File(modle.getmImagePath());
//                    if (file.exists()) {
//                        queue.add(modle);
//                    }
//
//                }
//                cursor.moveToNext();
//            }
//        }
//        closeCursor(cursor);
//        return queue;
//    }
//
//


    public int getCurrentWorkOrderImageCount(String id) {


        String[] bindArgs = {
                id
        };

        String countQuery = " SELECT  * FROM "
                + TABLE_IMAGE_UPLOAD
                + " WHERE "
                + COLUMN_IMAGE_ID
                + "= ?";

        Cursor cursor = mDatabase.rawQuery(countQuery, bindArgs);
        return cursor.getCount();
    }

    public int getlatesttypeidforupdate(String id) {

        String[] bindArgs = {
                id
        };

        int firstName=0;
        String countQuery = " SELECT  max(_id) as maxid FROM "
                + TABLE_IMAGE_UPLOAD
                + " WHERE "
                + COLUMN_IMAGE_TYPE
                + "= ?";

        Cursor cursor = mDatabase.rawQuery(countQuery, bindArgs);
        if(cursor.getCount()!=0)
        {
            if (cursor != null) {
                cursor.moveToFirst();
                firstName = cursor.getInt(cursor.getColumnIndex("maxid"));
            }
        }

        return firstName;
    }


    public int getlatesttypeidforupdate1(String id) {



        String[] bindArgs = {
                id
        };

        int firstName=0;
        String countQuery = " SELECT  max(image_id) as maxid FROM "
                + TABLE_IMAGE_UPLOAD
                + " WHERE "
                + COLUMN_IMAGE_TYPE
                + "= ?";

        Cursor cursor = mDatabase.rawQuery(countQuery, bindArgs);
        if(cursor.getCount()!=0)
        {
            if (cursor != null) {
                cursor.moveToFirst();
                firstName = cursor.getInt(cursor.getColumnIndex("maxid"));
            }
        }

        return firstName;
    }

    public int getMasterTableItems(int id) {
        int count = 0;

        String countQuery = " SELECT  count(*) FROM "
                + TABLE_IMAGE_UPLOAD
                + " WHERE "
                + COLUMN_IMAGE_ID
                + "= "+id;

        Cursor cursor = mDatabase.rawQuery(countQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            count = cursor.getInt(0);
        }
        closeCursor(cursor);
        return count;

    }


    public ArrayList<DBImageUpload> selectAll() {

        String getAllDetails = " SELECT "
                + " * "
                + " FROM "
                + TABLE_IMAGE_UPLOAD;

        Cursor cursor = mDatabase.rawQuery(getAllDetails, null);
        ArrayList<DBImageUpload> dataList = manageCursor(cursor);
        closeCursor(cursor);

        return dataList;
    }


    public ArrayList<DBImageUpload> selectAllLastIdPhotos(String id) {

        String[] bindArgs = {
                id
        };

        String getAllDetails = " SELECT "
                + " * "
                + " FROM "
                + TABLE_IMAGE_UPLOAD
                + " WHERE "
                + COLUMN_IMAGE_ID
                + " = ?";
        ;

        Cursor cursor = mDatabase.rawQuery(getAllDetails, bindArgs);
        ArrayList<DBImageUpload> dataList = manageCursor(cursor);
        closeCursor(cursor);

        return dataList;
    }

    public ArrayList<DBImageUpload> selectUploadPhotos() {

        String getAllDetails = " SELECT "
                + " * "
                + " FROM "
                + TABLE_IMAGE_UPLOAD
                + " WHERE "
                + COLUMN_IMAGE_UPLOAD_STATUS
                + " = 0 ";
        Cursor cursor = mDatabase.rawQuery(getAllDetails, null);
        ArrayList<DBImageUpload> dataList = manageCursor(cursor);
        closeCursor(cursor);

        return dataList;
    }

    protected DBImageUpload cursorToData(Cursor cursor) {
        DBImageUpload model = new DBImageUpload();
        model.setmId(cursor.getInt(cursor.getColumnIndex(COLUMN_KEY_ID)));
        model.setmImageId(cursor.getInt(cursor.getColumnIndex(COLUMN_IMAGE_ID)));
        model.setmImageName(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_NAME)));
        model.setmDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
        model.setmImagePath(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_PATH)));
        model.setmImageType(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_TYPE)));
        model.setmLat(cursor.getString(cursor.getColumnIndex(COLUMN_LAT)));
        model.setmLong(cursor.getString(cursor.getColumnIndex(COLUMN_LNG)));
        model.setmImageUploadStatus(cursor.getInt(cursor.getColumnIndex(COLUMN_IMAGE_UPLOAD_STATUS)));
        model.setmServerId(cursor.getString(cursor.getColumnIndex(COLUMN_SERVER_ID)));
        model.setmDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
        return model;
    }

    protected void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }

    protected ArrayList<DBImageUpload> manageCursor(Cursor cursor) {
        ArrayList<DBImageUpload> dataList = new ArrayList<DBImageUpload>();

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                DBImageUpload singleModel = cursorToData(cursor);
                if (singleModel != null) {
                    dataList.add(singleModel);
                }
                cursor.moveToNext();
            }
        }
        return dataList;
    }

}
