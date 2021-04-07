package com.dhanuka.morningparcel.SqlDatabase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dhanuka.morningparcel.Helper.VendorHelper;

import java.util.ArrayList;

public class VendorDAO {

    private static final String TABLE_VENDOR = "vendor_details";

    // Contacts Table Columns names
    private static final String COLUMN_ID = "_id";

    private static final String COLUMN_COMPANY_ID = "company_id";
    private static final String COLUMN_COMPANY_NAME = "company_name";


    private SQLiteDatabase mDatabase;
    private Context mContext;

    public VendorDAO(SQLiteDatabase database, Context context) {
        mDatabase = database;
        mContext = context;
    }


    public static String getCreateTableVendor() {
        getDropTableAlbum();
        String CREATE_VEHICLE_DETAILS_TABLE = "CREATE TABLE " + TABLE_VENDOR
                + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_COMPANY_ID + " TEXT ,"
                + COLUMN_COMPANY_NAME + " TEXT)";

        return CREATE_VEHICLE_DETAILS_TABLE;
    }

    public static String getDropTableAlbum() {
        return "DROP TABLE IF EXISTS " + TABLE_VENDOR;
    }


    public void deleteAll() {

        String delete_all_users = " DELETE "
                + " FROM "
                + TABLE_VENDOR;

        mDatabase.execSQL(delete_all_users);
    }


    public void insert(ArrayList<VendorHelper> vendorlist) {

        for (VendorHelper address : vendorlist) {


            String[] bindArgs = {
                    address.getCompanyid(),
                    address.getCompanyname(),
            };

            String insertUser = " INSERT INTO "
                    + TABLE_VENDOR
                    + " ( "
                    + COLUMN_COMPANY_ID
                    + " , "
                    + COLUMN_COMPANY_NAME
                    + " ) "
                    + " VALUES "
                    + " (?,?)";


            mDatabase.execSQL(insertUser, bindArgs);
        }
    }



    public ArrayList<VendorHelper> selectAll() {

        String getAllourceDetails = " SELECT "
                + " * "
                + " FROM "
                + TABLE_VENDOR;

        Cursor cursor = mDatabase.rawQuery(getAllourceDetails, null);
        ArrayList<VendorHelper> dataList = manageCursor(cursor);


        closeCursor(cursor);

        return dataList;
    }

    public ArrayList<VendorHelper> selectids() {

        String getAllidsDetails = " SELECT "
                + COLUMN_COMPANY_ID
                + " FROM "
                + TABLE_VENDOR;

        Cursor cursor = mDatabase.rawQuery(getAllidsDetails, null);
        ArrayList<VendorHelper> dataList = manageCursor(cursor);
        closeCursor(cursor);

        return dataList;
    }








    protected VendorHelper cursorToData(Cursor cursor) {
        VendorHelper source = new VendorHelper();
        source.setmId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
        source.setCompanyid(cursor.getString(cursor.getColumnIndex(COLUMN_COMPANY_ID)));
        source.setCompanyname(cursor.getString(cursor.getColumnIndex(COLUMN_COMPANY_NAME)));

        return source;
    }

    protected void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }

    protected ArrayList<VendorHelper> manageCursor(Cursor cursor) {
        ArrayList<VendorHelper> dataList = new ArrayList<VendorHelper>();

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                VendorHelper VendorHelper = cursorToData(cursor);
                if (VendorHelper != null) {
                    dataList.add(VendorHelper);
                }
                cursor.moveToNext();
            }
        }
        return dataList;
    }

}
