package com.dhanuka.morningparcel.Helper;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class InvoiceDAO {

    private static final String TABLE_INVOICE_UPLOAD = "invoice_data";
    private static final String COLUMN_KEY_ID = "_id";
    private static final String COLUMN_ORDER_ID = "order_id";
    private static final String COLUMN_IMAGE_NAME = "image_name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_IMAGE_PATH = "image_path";
    private static final String COLUMN_IMAGE_TYPE = "image_type";
    private static final String COLUMN_INVOICE_DATE = "invoice_date";
    private static final String COLUMN_INVOICE_AMOUNT = "invoice_amount";
    private static final String COLUMN_IMAGE_UPLOAD_STATUS = "upload_status";
    private static final String COLUMN_SERVER_ID = "server_id";
    private static final String COLUMN_INVOICE_NO = "date";
    private SQLiteDatabase mDatabase;
    private Context mContext;

    public InvoiceDAO(SQLiteDatabase database, Context context) {
        mDatabase = database;
        mContext = context;
    }

    public static String getCreateInvoiceUpload() {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_INVOICE_UPLOAD
                + "("
                + COLUMN_KEY_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_ORDER_ID + " INTEGER ,"
                + COLUMN_IMAGE_NAME + " TEXT ,"
                + COLUMN_DESCRIPTION + " TEXT ,"
                + COLUMN_IMAGE_PATH + " TEXT ,"
                + COLUMN_IMAGE_TYPE + " TEXT ,"
                + COLUMN_INVOICE_DATE + " TEXT ,"
                + COLUMN_INVOICE_AMOUNT + " TEXT ,"
                + COLUMN_IMAGE_UPLOAD_STATUS + " INTEGER ,"
                + COLUMN_SERVER_ID + " TEXT ,"
                + COLUMN_INVOICE_NO + " TEXT)";

        return CREATE_TABLE;
    }

    public static String getDropTableImageUpload() {
        return "DROP TABLE IF EXISTS " + TABLE_INVOICE_UPLOAD;
    }

    public void deleteAll() {

        String delete_all = " DELETE "
                + " FROM "
                + TABLE_INVOICE_UPLOAD;

        mDatabase.execSQL(delete_all);
    }

    public void insert(ArrayList<InvoiceHelper> arrayList) {

        for (InvoiceHelper singleInput : arrayList) {


            String[] bindArgs = {
                    String.valueOf(singleInput.getOrderid()),
                    singleInput.getmImageName(),
                    singleInput.getmDescription(),
                    singleInput.getmImagePath(),
                    singleInput.getmImageType(),
                    singleInput.getInvoicedate(),
                    singleInput.getInvoiceamount(),
                    String.valueOf(singleInput.getmImageUploadStatus()),
                    String.valueOf(singleInput.getmServerId()),
                    singleInput.getInvoiceno(),
            };

            String insertUser = " INSERT INTO "
                    + TABLE_INVOICE_UPLOAD
                    + " ( "
                    + COLUMN_ORDER_ID
                    + " , "
                    + COLUMN_IMAGE_NAME
                    + " , "
                    + COLUMN_DESCRIPTION
                    + " , "
                    + COLUMN_IMAGE_PATH
                    + " , "
                    + COLUMN_IMAGE_TYPE
                    + " , "
                    + COLUMN_INVOICE_DATE
                    + " , "
                    + COLUMN_INVOICE_AMOUNT
                    + " , "
                    + COLUMN_IMAGE_UPLOAD_STATUS
                    + " , "
                    + COLUMN_SERVER_ID
                    + " , "
                    + COLUMN_INVOICE_NO
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
                + TABLE_INVOICE_UPLOAD
                + " SET "
                + COLUMN_SERVER_ID
                + " = ? WHERE " + COLUMN_ORDER_ID + "= ?";


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
                + TABLE_INVOICE_UPLOAD
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
                + TABLE_INVOICE_UPLOAD
                + " SET "
                + COLUMN_IMAGE_NAME
                + " = ?, "
                + COLUMN_IMAGE_PATH
                + " = ?, "
                + COLUMN_INVOICE_NO
                + " = ? WHERE "
                + COLUMN_ORDER_ID + "= ?";

        mDatabase.execSQL(update, bindArgs);

    }

    public void deleteUploadedPhotoById(int id) {

        String deleteSingleRow = " DELETE "
                + " FROM "
                + TABLE_INVOICE_UPLOAD
                + " WHERE "
                + COLUMN_KEY_ID
                + " = "
                + id;
        mDatabase.execSQL(deleteSingleRow);
    }


    public void deleteUploadedPhotoByName(String value) {

        String deleteSingleRow = " DELETE "
                + " FROM "
                + TABLE_INVOICE_UPLOAD
                + " WHERE "
                + COLUMN_IMAGE_NAME
                + " = '"
                + value + "'";
        mDatabase.execSQL(deleteSingleRow);
    }


    public ArrayList<InvoiceHelper> getNewUploadList(String id) {
        ArrayList<InvoiceHelper> queue = new ArrayList<>();
        String getAllPhotoDetails = " SELECT "
                + " * "
                + " FROM "
                + TABLE_INVOICE_UPLOAD + " WHERE "  + COLUMN_ORDER_ID + " =" + id;
        Cursor cursor = mDatabase.rawQuery(getAllPhotoDetails, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                InvoiceHelper modle = cursorToData(cursor);
                if (modle != null) {
                    queue.add(modle);

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
//    public Queue<InvoiceHelper> getNewUploadList() {
//        Queue<InvoiceHelper> queue = new LinkedList<>();
//        String getAllPhotoDetails = " SELECT "
//                + " * "
//                + " FROM "
//                + TABLE_INVOICE_UPLOAD + " WHERE " + COLUMN_IMAGE_UPLOAD_STATUS + " = 0 ";
//        Cursor cursor = mDatabase.rawQuery(getAllPhotoDetails, null);
//        if (cursor != null) {
//            cursor.moveToFirst();
//            while (!cursor.isAfterLast()) {
//                InvoiceHelper modle = cursorToData(cursor);
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
                + TABLE_INVOICE_UPLOAD
                + " WHERE "
                + COLUMN_ORDER_ID
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
                + TABLE_INVOICE_UPLOAD
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
                + TABLE_INVOICE_UPLOAD
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
                + TABLE_INVOICE_UPLOAD
                + " WHERE "
                + COLUMN_ORDER_ID
                + "= "+id;

        Cursor cursor = mDatabase.rawQuery(countQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            count = cursor.getInt(0);
        }
        closeCursor(cursor);
        return count;

    }


    public ArrayList<InvoiceHelper> selectAll() {

        String getAllDetails = " SELECT "
                + " * "
                + " FROM "
                + TABLE_INVOICE_UPLOAD;

        Cursor cursor = mDatabase.rawQuery(getAllDetails, null);
        ArrayList<InvoiceHelper> dataList = manageCursor(cursor);
        closeCursor(cursor);

        return dataList;
    }


    public ArrayList<InvoiceHelper> selectAllLastIdPhotos(String id) {

        String[] bindArgs = {
                id
        };

        String getAllDetails = " SELECT "
                + " * "
                + " FROM "
                + TABLE_INVOICE_UPLOAD
                + " WHERE "
                + COLUMN_ORDER_ID
                + " = ?";
        ;

        Cursor cursor = mDatabase.rawQuery(getAllDetails, bindArgs);
        ArrayList<InvoiceHelper> dataList = manageCursor(cursor);
        closeCursor(cursor);

        return dataList;
    }

    public ArrayList<InvoiceHelper> selectUploadPhotos() {

        String getAllDetails = " SELECT "
                + " * "
                + " FROM "
                + TABLE_INVOICE_UPLOAD
                + " WHERE "
                + COLUMN_IMAGE_UPLOAD_STATUS
                + " = 0 ";
        Cursor cursor = mDatabase.rawQuery(getAllDetails, null);
        ArrayList<InvoiceHelper> dataList = manageCursor(cursor);
        closeCursor(cursor);

        return dataList;
    }

    protected InvoiceHelper cursorToData(Cursor cursor) {
        InvoiceHelper model = new InvoiceHelper();
        model.setmId(cursor.getInt(cursor.getColumnIndex(COLUMN_KEY_ID)));
        model.setOrderid(cursor.getInt(cursor.getColumnIndex(COLUMN_ORDER_ID)));
        model.setmImageName(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_NAME)));
        model.setmDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
        model.setmImagePath(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_PATH)));
        model.setmImageType(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_TYPE)));
        model.setInvoicedate(cursor.getString(cursor.getColumnIndex(COLUMN_INVOICE_DATE)));
        model.setInvoiceamount(cursor.getString(cursor.getColumnIndex(COLUMN_INVOICE_AMOUNT)));
        model.setmImageUploadStatus(cursor.getInt(cursor.getColumnIndex(COLUMN_IMAGE_UPLOAD_STATUS)));
        model.setmServerId(cursor.getString(cursor.getColumnIndex(COLUMN_SERVER_ID)));
        model.setInvoiceno(cursor.getString(cursor.getColumnIndex(COLUMN_INVOICE_NO)));
        return model;
    }

    protected void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }

    protected ArrayList<InvoiceHelper> manageCursor(Cursor cursor) {
        ArrayList<InvoiceHelper> dataList = new ArrayList<InvoiceHelper>();

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                InvoiceHelper singleModel = cursorToData(cursor);
                if (singleModel != null) {
                    dataList.add(singleModel);
                }
                cursor.moveToNext();
            }
        }
        return dataList;
    }

}
