package com.dhanuka.morningparcel.SqlDatabase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dhanuka.morningparcel.Helper.ItemMasterhelper;

import java.util.ArrayList;

public class All_Item_Small {



    private static final String TABLE_ITEMMASTER = "allitemsmall";

    // Contacts Table Columns names
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_ItemID = "itemids";
    private static final String COLUMN_ItemName = "itemnames";
    private static final String COLUMN_GroupID = "groupids";
    private static final String COLUMN_SaleUOM = "saleupmids";
    private static final String COLUMN_SaleRate= "salerates";
    private static final String COLUMN_IsDeal = "isdeals";
    private static final String COLUMN_IsNewListing = "isnewlistings";
    private static final String COLUMN_ItemImage = "itemimages";
    private static final String COLUMN_RepeatOrder= "repeatorders";
    private static final String COLUMN_MRP= "mrps";
    private SQLiteDatabase mDatabase;
    private Context mContext;

    public All_Item_Small(SQLiteDatabase database, Context context) {
        mDatabase = database;
        mContext = context;
    }



    public static String getCreateMasterTable() {
        getDropTableAlbum();
        String CREATE_VEHICLE_DETAILS_TABLE = "CREATE TABLE " + TABLE_ITEMMASTER
                + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_ItemID + " TEXT ,"
                + COLUMN_ItemName + " TEXT ,"
                + COLUMN_GroupID + " TEXT ,"
                + COLUMN_SaleUOM + " TEXT ,"
                + COLUMN_SaleRate + " TEXT ,"
                + COLUMN_IsDeal + " TEXT ,"
                + COLUMN_IsNewListing + " TEXT ,"
                + COLUMN_ItemImage + " TEXT ,"
                + COLUMN_RepeatOrder + " TEXT,"
                + COLUMN_MRP + " TEXT)";

        return CREATE_VEHICLE_DETAILS_TABLE;
    }

    public void updateItemMAster(String ItemID, String ItemName, String GroupID, String SaleUOM, String SaleRate, String IsDeal, String IsNewListing, String ItemImage, String ItemMRP) {

        String[] bindArgs = {
                ItemID ,
                ItemName,
                GroupID,
                SaleUOM,
                SaleRate,
                IsDeal,
                IsNewListing,
                ItemImage,
                ItemMRP,


        };

        String update = " UPDATE "
                + TABLE_ITEMMASTER
                + " SET "
                + COLUMN_ItemName
                + " = ?, "
                + COLUMN_GroupID
                + " = ?, "
                + COLUMN_SaleUOM
                + " = ?, "
                + COLUMN_SaleRate
                + " = ?, "
                + COLUMN_IsDeal
                + " = ?, "
                + COLUMN_IsNewListing
                + " = ?, "
                + COLUMN_ItemImage
                + " = ?, "
                + COLUMN_RepeatOrder
                + " = ?, "
                + COLUMN_MRP
                + " = ? WHERE "
                + COLUMN_ItemID + "= ?";

        mDatabase.execSQL(update, bindArgs);

    }

    public static String getDropTableAlbum() {
        return "DROP TABLE IF EXISTS " + TABLE_ITEMMASTER;
    }


    public void deleteAll() {

        String delete_all_users = " DELETE "
                + " FROM "
                + TABLE_ITEMMASTER;

        mDatabase.execSQL(delete_all_users);
    }


    public void insert(ArrayList<ItemMasterhelper> vehicleArrayList) {

        for (ItemMasterhelper address : vehicleArrayList) {


            String[] bindArgs = {
                    address.getItemID(),
                    address.getItemName(),
                    address.getGroupID(),
                    address.getSaleUOM(),
                    address.getSaleRate(),
                    address.getIsDeal(),
                    address.getIsNewListing(),
                    address.getItemImage(),
                    address.getRepeatOrder(),
                    address.getMRP(),
            };

            String insertUser = " INSERT INTO "
                    + TABLE_ITEMMASTER
                    + " ( "
                    + COLUMN_ItemID
                    + " , "
                    + COLUMN_ItemName
                    + " , "
                    + COLUMN_GroupID
                    + " , "
                    + COLUMN_SaleUOM
                    + " , "
                    + COLUMN_SaleRate
                    + " , "
                    + COLUMN_IsDeal
                    + " , "
                    + COLUMN_IsNewListing
                    + " , "
                    + COLUMN_ItemImage
                    + " , "
                    + COLUMN_RepeatOrder
                    + " , "
                    + COLUMN_MRP
                    + " ) "
                    + " VALUES "
                    + " (?,?,?,?,?,?,?,?,?,?)";


            mDatabase.execSQL(insertUser, bindArgs);
        }
    }
    public void insertSingleProduct(ItemMasterhelper address) {



        String[] bindArgs = {
                address.getItemID(),
                address.getItemName(),
                address.getGroupID(),
                address.getSaleUOM(),
                address.getSaleRate(),
                address.getIsDeal(),
                address.getIsNewListing(),
                address.getItemImage(),
                address.getRepeatOrder(),
                address.getMRP(),
        };

        String insertUser = " INSERT INTO "
                + TABLE_ITEMMASTER
                + " ( "
                + COLUMN_ItemID
                + " , "
                + COLUMN_ItemName
                + " , "
                + COLUMN_GroupID
                + " , "
                + COLUMN_SaleUOM
                + " , "
                + COLUMN_SaleRate
                + " , "
                + COLUMN_IsDeal
                + " , "
                + COLUMN_IsNewListing
                + " , "
                + COLUMN_ItemImage
                + " , "
                + COLUMN_RepeatOrder
                + " , "
                + COLUMN_MRP
                + " ) "
                + " VALUES "
                + " (?,?,?,?,?,?,?,?,?,?)";


        mDatabase.execSQL(insertUser, bindArgs);

    }


    public int getRowCount() {
        int count = 0;

        Cursor cursor = mDatabase.rawQuery("SELECT COUNT(*) FROM " + TABLE_ITEMMASTER, null);
        if (cursor.moveToNext()) {
            count = cursor.getInt(0);
        }

        return count;
    }
    public ArrayList<ItemMasterhelper> selectAll() {

        String getAllourceDetails = " SELECT "
                + " * "
                + " FROM "
                + TABLE_ITEMMASTER;

        Cursor cursor = mDatabase.rawQuery(getAllourceDetails, null);
        ArrayList<ItemMasterhelper> dataList = manageCursor(cursor);
        closeCursor(cursor);
        //9461757476
        //Mohit Tester
        return dataList;
    }

    public ArrayList<ItemMasterhelper> selectids() {

        String getAllidsDetails = " SELECT "
                + COLUMN_ItemID
                + " FROM "
                + TABLE_ITEMMASTER;

        Cursor cursor = mDatabase.rawQuery(getAllidsDetails, null);
        ArrayList<ItemMasterhelper> dataList = manageCursor(cursor);
        closeCursor(cursor);

        return dataList;
    }








    protected ItemMasterhelper cursorToData(Cursor cursor) {
        ItemMasterhelper source = new ItemMasterhelper();
        source.setmId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
        source.setItemID(cursor.getString(cursor.getColumnIndex(COLUMN_ItemID)));
        source.setItemName(cursor.getString(cursor.getColumnIndex(COLUMN_ItemName)));
        source.setGroupID(cursor.getString(cursor.getColumnIndex(COLUMN_GroupID)));
        source.setSaleUOM(cursor.getString(cursor.getColumnIndex(COLUMN_SaleUOM)));
        source.setSaleRate(cursor.getString(cursor.getColumnIndex(COLUMN_SaleRate)));
        source.setIsDeal(cursor.getString(cursor.getColumnIndex(COLUMN_IsDeal)));
        source.setIsNewListing(cursor.getString(cursor.getColumnIndex(COLUMN_IsNewListing)));
        source.setItemImage(cursor.getString(cursor.getColumnIndex(COLUMN_ItemImage)));
        source.setRepeatOrder(cursor.getString(cursor.getColumnIndex(COLUMN_RepeatOrder)));
        source.setMRP(cursor.getString(cursor.getColumnIndex(COLUMN_MRP)));

        return source;
    }

    protected void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }

    protected ArrayList<ItemMasterhelper> manageCursor(Cursor cursor) {
        ArrayList<ItemMasterhelper> dataList = new ArrayList<ItemMasterhelper>();

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                ItemMasterhelper ItemMasterhelper = cursorToData(cursor);
                if (ItemMasterhelper != null) {
                    dataList.add(ItemMasterhelper);
                }
                cursor.moveToNext();
            }
        }
        return dataList;
    }






}
