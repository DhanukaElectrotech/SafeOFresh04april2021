package com.dhanuka.morningparcel.SqlDatabase;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dhanuka.morningparcel.Helper.ItemUomHelper;

import java.util.ArrayList;

public class UomMasterDAO {

    private static final String TABLE_UOM = "uommaster";

    // Contacts Table Columns names
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_ItemUOMID = "itemuomid";
    private static final String COLUMN_ItemID = "itemid";
    private static final String COLUMN_ItemDescription = "itemdesp";
    private static final String COLUMN_UOMID = "uomid";
    private static final String COLUMN_UOMDesc = "uomdesc";
    private static final String COLUMN_Qty = "qty";
    private static final String COLUMN_PurchaseRate = "uompurchaserate";
    private static final String COLUMN_SaleRate = "uomsalerate";


    private SQLiteDatabase mDatabase;
    private Context mContext;

    public UomMasterDAO(SQLiteDatabase database, Context context) {
        mDatabase = database;
        mContext = context;
    }



    public static String getCreateUomMasterTable() {
        getDropTableAlbum();
        String CREATE_VEHICLE_DETAILS_TABLE = "CREATE TABLE " + TABLE_UOM
                + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_ItemUOMID + " TEXT ,"
                + COLUMN_ItemID + " TEXT ,"
                + COLUMN_ItemDescription + " TEXT ,"
                + COLUMN_UOMID + " TEXT ,"
                + COLUMN_UOMDesc + " TEXT ,"
                + COLUMN_Qty + " TEXT ,"
                + COLUMN_PurchaseRate + " TEXT ,"
                + COLUMN_SaleRate + " TEXT)";


        return CREATE_VEHICLE_DETAILS_TABLE;
    }


    public static String getDropTableAlbum() {
        return "DROP TABLE IF EXISTS " + TABLE_UOM;
    }


    public void deleteAll() {

        String delete_all_users = " DELETE "
                + " FROM "
                + TABLE_UOM;

        mDatabase.execSQL(delete_all_users);
    }


    public void insert(ArrayList<ItemUomHelper> vehicleArrayList) {

        for (ItemUomHelper address : vehicleArrayList) {


            String[] bindArgs = {
                    address.getItemID(),
                    address.getItemUOMID(),
                    address.getItemDescription(),
                    address.getUOMID(),
                    address.getUOMDesc(),
                    address.getQty(),
                    address.getPurchaseRate(),
                    address.getSaleRate(),

            };

            String insertUser = " INSERT INTO "
                    + TABLE_UOM
                    + " ( "
                    + COLUMN_ItemID
                    + " , "
                    + COLUMN_ItemUOMID
                    + " , "
                    + COLUMN_ItemDescription
                    + " , "
                    + COLUMN_UOMID
                    + " , "
                    + COLUMN_UOMDesc
                    + " , "
                    + COLUMN_Qty
                    + " , "
                    + COLUMN_PurchaseRate
                    + " , "
                    + COLUMN_SaleRate
                    + " ) "
                    + " VALUES "
                    + " (?,?,?,?,?,?,?,?)";


            mDatabase.execSQL(insertUser, bindArgs);
        }
    }



    public ArrayList<ItemUomHelper> selectAll() {

        String getAllourceDetails = " SELECT "
                + " * "
                + " FROM "
                + TABLE_UOM;

        Cursor cursor = mDatabase.rawQuery(getAllourceDetails, null);
        ArrayList<ItemUomHelper> dataList = manageCursor(cursor);
        closeCursor(cursor);
        //9461757476
        //Mohit Tester
        return dataList;
    }

    public ArrayList<ItemUomHelper> selectids() {

        String getAllidsDetails = " SELECT "
                + COLUMN_ItemID
                + " FROM "
                + TABLE_UOM;

        Cursor cursor = mDatabase.rawQuery(getAllidsDetails, null);
        ArrayList<ItemUomHelper> dataList = manageCursor(cursor);
        closeCursor(cursor);

        return dataList;
    }








    protected ItemUomHelper cursorToData(Cursor cursor) {

        ItemUomHelper source = new ItemUomHelper();
        source.setmID(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
        source.setItemID(cursor.getString(cursor.getColumnIndex(COLUMN_ItemID)));
        source.setItemUOMID(cursor.getString(cursor.getColumnIndex(COLUMN_ItemUOMID)));
        source.setItemDescription(cursor.getString(cursor.getColumnIndex(COLUMN_ItemDescription)));
        source.setUOMID(cursor.getString(cursor.getColumnIndex(COLUMN_UOMID)));
        source.setUOMDesc(cursor.getString(cursor.getColumnIndex(COLUMN_UOMDesc)));
        source.setQty(cursor.getString(cursor.getColumnIndex(COLUMN_Qty)));
        source.setPurchaseRate(cursor.getString(cursor.getColumnIndex(COLUMN_PurchaseRate)));
        source.setSaleRate(cursor.getString(cursor.getColumnIndex(COLUMN_SaleRate)));


        return source;
    }

    protected void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }

    protected ArrayList<ItemUomHelper> manageCursor(Cursor cursor) {
        ArrayList<ItemUomHelper> dataList = new ArrayList<ItemUomHelper>();

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                ItemUomHelper ItemUomHelper = cursorToData(cursor);
                if (ItemUomHelper != null) {
                    dataList.add(ItemUomHelper);
                }
                cursor.moveToNext();
            }
        }
        return dataList;
    }






}
