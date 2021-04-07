package com.dhanuka.morningparcel.SqlDatabase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dhanuka.morningparcel.Helper.ItemMasterhelper;

import java.util.ArrayList;

public class All_Item_Master {

    private static final String TABLE_ITEMMASTER = "allitemmaster";

    // Contacts Table Columns names
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_ItemID = "itemid";
    private static final String COLUMN_Companyid = "companyid";
    private static final String COLUMN_ItemName = "itemname";
    private static final String COLUMN_GroupID = "groupid";
    private static final String COLUMN_OpeningStock = "openstock";
    private static final String COLUMN_MOQ = "moq";
    private static final String COLUMN_ROQ = "roq";
    private static final String COLUMN_PurchaseUOM = "purchaseuom";
    private static final String COLUMN_PurchaseUOMId = "purchaseuomid";
    private static final String COLUMN_SaleUOM = "saleupmid";
    private static final String COLUMN_SaleUOMID= "saleuomid";
    private static final String COLUMN_PurchaseRate = "purchaserate";
    private static final String COLUMN_SaleRate= "salerate";
    private static final String COLUMN_ItemSKU = "itemsku";
    private static final String COLUMN_ItemBarcode = "itembarcode";
    private static final String COLUMN_StockUOM = "stockuom";
    private static final String COLUMN_ItemImage = "itemimage";
    private static final String COLUMN_HSNCode= "hsncode";
    private static final String COLUMN_MRP= "mrp";
    private SQLiteDatabase mDatabase;
    private Context mContext;

    public All_Item_Master(SQLiteDatabase database, Context context) {
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
                + COLUMN_Companyid + " TEXT ,"
                + COLUMN_GroupID + " TEXT ,"
                + COLUMN_OpeningStock + " TEXT ,"
                + COLUMN_MOQ + " TEXT ,"
                + COLUMN_ROQ + " TEXT ,"
                + COLUMN_PurchaseUOM + " TEXT ,"
                + COLUMN_PurchaseUOMId + " TEXT ,"
                + COLUMN_SaleUOM + " TEXT ,"
                + COLUMN_SaleUOMID + " TEXT ,"
                + COLUMN_PurchaseRate + " TEXT ,"
                + COLUMN_SaleRate + " TEXT ,"
                + COLUMN_ItemSKU + " TEXT ,"
                + COLUMN_ItemBarcode + " TEXT ,"
                + COLUMN_StockUOM + " TEXT ,"
                + COLUMN_ItemImage + " TEXT ,"
                + COLUMN_HSNCode + " TEXT,"
                + COLUMN_MRP + " TEXT)";

        return CREATE_VEHICLE_DETAILS_TABLE;
    }

    public void updateItemMAster(String HSNCode, String ItemName, String Companyid, String GroupID, String OpeningStock, String MOQ, String ROQ, String PurchaseUOM, String PurchaseUOMId,String SaleUOM,String SaleUOMID,String PurchaseRate,String  SaleRate,String ItemSKU,String ItemBarcode,String StockUOM,String ItemImage,String ItemID,String ItemMRP ) {

        String[] bindArgs = {
                HSNCode ,
                ItemName,
                Companyid,
                GroupID,
                OpeningStock,
                MOQ,
                ROQ,
                PurchaseUOM,
                PurchaseUOMId,
                SaleUOM,
                SaleUOMID,
                PurchaseRate,
                SaleRate,
                ItemSKU,
                ItemBarcode,
                StockUOM,
                ItemImage,
                ItemID,
                ItemMRP,


        };

        String update = " UPDATE "
                + TABLE_ITEMMASTER
                + " SET "
                + COLUMN_HSNCode
                + " = ?, "
                + COLUMN_ItemName
                + " = ?, "
                + COLUMN_Companyid
                + " = ?, "
                + COLUMN_GroupID
                + " = ?, "
                + COLUMN_OpeningStock
                + " = ?, "
                + COLUMN_MOQ
                + " = ?, "
                + COLUMN_ROQ
                + " = ?, "
                + COLUMN_PurchaseUOM
                + " = ?, "
                + COLUMN_PurchaseUOMId
                + " = ?, "
                + COLUMN_SaleUOM
                + " = ?, "
                + COLUMN_SaleUOMID
                + " = ?, "
                + COLUMN_PurchaseRate
                + " = ?, "
                + COLUMN_SaleRate
                + " = ?, "
                + COLUMN_ItemSKU
                + " = ?, "
                + COLUMN_ItemBarcode
                + " = ?, "
                + COLUMN_StockUOM
                + "=  ?, "
                + COLUMN_ItemImage
                + "=  ?, "
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
                    address.getCompanyid(),
                    address.getGroupID(),
                    address.getOpeningStock(),
                    address.getMOQ(),
                    address.getROQ(),
                    address.getPurchaseUOM(),
                    address.getPurchaseUOMId(),
                    address.getSaleUOM(),
                    address.getSaleUOMID(),
                    address.getPurchaseRate(),
                    address.getSaleRate(),
                    address.getItemSKU(),
                    address.getItemBarcode(),
                    address.getStockUOM(),
                    address.getItemImage(),
                    address.getHSNCode(),
                    address.getMRP(),
            };

            String insertUser = " INSERT INTO "
                    + TABLE_ITEMMASTER
                    + " ( "
                    + COLUMN_ItemID
                    + " , "
                    + COLUMN_ItemName
                    + " , "
                    + COLUMN_Companyid
                    + " , "
                    + COLUMN_GroupID
                    + " , "
                    + COLUMN_OpeningStock
                    + " , "
                    + COLUMN_MOQ
                    + " , "
                    + COLUMN_ROQ
                    + " , "
                    + COLUMN_PurchaseUOM
                    + " , "
                    + COLUMN_PurchaseUOMId
                    + " , "
                    + COLUMN_SaleUOM
                    + " , "
                    + COLUMN_SaleUOMID
                    + " , "
                    + COLUMN_PurchaseRate
                    + " , "
                    + COLUMN_SaleRate
                    + " , "
                    + COLUMN_ItemSKU
                    + " , "
                    + COLUMN_ItemBarcode
                    + " , "
                    + COLUMN_StockUOM
                    + " , "
                    + COLUMN_ItemImage
                    + " , "
                    + COLUMN_HSNCode
                    + " , "
                    + COLUMN_MRP
                    + " ) "
                    + " VALUES "
                    + " (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


            mDatabase.execSQL(insertUser, bindArgs);
        }
    }
    public void insertSingleProduct(ItemMasterhelper address) {



            String[] bindArgs = {
                    address.getItemID(),
                    address.getItemName(),
                    address.getCompanyid(),
                    address.getGroupID(),
                    address.getOpeningStock(),
                    address.getMOQ(),
                    address.getROQ(),
                    address.getPurchaseUOM(),
                    address.getPurchaseUOMId(),
                    address.getSaleUOM(),
                    address.getSaleUOMID(),
                    address.getPurchaseRate(),
                    address.getSaleRate(),
                    address.getItemSKU(),
                    address.getItemBarcode(),
                    address.getStockUOM(),
                    address.getItemImage(),
                    address.getHSNCode(),
                    address.getMRP(),
            };

            String insertUser = " INSERT INTO "
                    + TABLE_ITEMMASTER
                    + " ( "
                    + COLUMN_ItemID
                    + " , "
                    + COLUMN_ItemName
                    + " , "
                    + COLUMN_Companyid
                    + " , "
                    + COLUMN_GroupID
                    + " , "
                    + COLUMN_OpeningStock
                    + " , "
                    + COLUMN_MOQ
                    + " , "
                    + COLUMN_ROQ
                    + " , "
                    + COLUMN_PurchaseUOM
                    + " , "
                    + COLUMN_PurchaseUOMId
                    + " , "
                    + COLUMN_SaleUOM
                    + " , "
                    + COLUMN_SaleUOMID
                    + " , "
                    + COLUMN_PurchaseRate
                    + " , "
                    + COLUMN_SaleRate
                    + " , "
                    + COLUMN_ItemSKU
                    + " , "
                    + COLUMN_ItemBarcode
                    + " , "
                    + COLUMN_StockUOM
                    + " , "
                    + COLUMN_ItemImage
                    + " , "
                    + COLUMN_HSNCode
                    + " , "
                    + COLUMN_MRP
                    + " ) "
                    + " VALUES "
                    + " (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


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
        source.setCompanyid(cursor.getString(cursor.getColumnIndex(COLUMN_Companyid)));
        source.setItemID(cursor.getString(cursor.getColumnIndex(COLUMN_ItemID)));
        source.setItemName(cursor.getString(cursor.getColumnIndex(COLUMN_ItemName)));
        source.setGroupID(cursor.getString(cursor.getColumnIndex(COLUMN_GroupID)));
        source.setOpeningStock(cursor.getString(cursor.getColumnIndex(COLUMN_OpeningStock)));
        source.setMOQ(cursor.getString(cursor.getColumnIndex(COLUMN_MOQ)));
        source.setROQ(cursor.getString(cursor.getColumnIndex(COLUMN_ROQ)));
        source.setPurchaseUOM(cursor.getString(cursor.getColumnIndex(COLUMN_PurchaseUOM)));
        source.setPurchaseUOMId(cursor.getString(cursor.getColumnIndex(COLUMN_PurchaseUOMId)));
        source.setSaleUOM(cursor.getString(cursor.getColumnIndex(COLUMN_SaleUOM)));
        source.setSaleUOMID(cursor.getString(cursor.getColumnIndex(COLUMN_SaleUOMID)));
        source.setPurchaseRate(cursor.getString(cursor.getColumnIndex(COLUMN_PurchaseRate)));
        source.setSaleRate(cursor.getString(cursor.getColumnIndex(COLUMN_SaleRate)));
        source.setItemSKU(cursor.getString(cursor.getColumnIndex(COLUMN_ItemSKU)));
        source.setItemBarcode(cursor.getString(cursor.getColumnIndex(COLUMN_ItemBarcode)));
        source.setStockUOM(cursor.getString(cursor.getColumnIndex(COLUMN_StockUOM)));
        source.setItemImage(cursor.getString(cursor.getColumnIndex(COLUMN_ItemImage)));
        source.setHSNCode(cursor.getString(cursor.getColumnIndex(COLUMN_HSNCode)));
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
