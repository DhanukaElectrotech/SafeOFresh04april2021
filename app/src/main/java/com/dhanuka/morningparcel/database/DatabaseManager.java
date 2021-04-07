package com.dhanuka.morningparcel.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dhanuka.morningparcel.beans.CartProduct;

import java.util.ArrayList;
import java.util.List;

import static com.dhanuka.morningparcel.database.Constants.TABLE_CART_PRODUCTS;

/**
 *
 */
public class DatabaseManager extends SQLiteOpenHelper {

    private static DatabaseManager instance;
    private SQLiteDatabase database;

    private DatabaseManager(Context context) {
        super(context, "morning-parcel.db", null, 4);
    }

    public static DatabaseManager getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseManager(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Constants.CREATE_PRODUCTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

            sqLiteDatabase.execSQL(DatabaseManager.getDropTableAlbum());
            onCreate(sqLiteDatabase);


    }

    private void open() {
        database = getWritableDatabase();
    }

    public long insert(CartProduct product) {
        String weight = "";
        String unit = "";

        long rows = 0l;


        ContentValues values = new ContentValues();
        values.put(Constants.ItemID, product.getItemID());
        values.put(Constants.companyid, product.getCompanyid());
        values.put(Constants.ItemName, product.getItemName());
        values.put(Constants.GroupID, product.getGroupID());
        values.put(Constants.OpeningStock, product.getOpeningStock());
        values.put(Constants.MOQ, product.getMOQ());
        values.put(Constants.ROQ, product.getROQ());
        values.put(Constants.PurchaseUOM, product.getPurchaseUOM());
        values.put(Constants.PurchaseUOMId, product.getPurchaseUOMId());
        values.put(Constants.SaleUOM, product.getSaleUOM());
        values.put(Constants.SaleUOMID, product.getSaleUOMID());
        values.put(Constants.PurchaseRate, product.getPurchaseRate());
        values.put(Constants.SaleRate, product.getSaleRate());
        values.put(Constants.ItemSKU, product.getItemSKU());
        values.put(Constants.ItemBarcode, product.getItemBarcode());
        values.put(Constants.StockUOM, product.getStockUOM());
        values.put(Constants.ItemImage, product.getItemImage());
        values.put(Constants.HSNCode, product.getHSNCode());
        values.put(Constants.quantity, product.getQuantity());
        values.put(Constants.sub_cat_id, product.getSubCategory());
        values.put(Constants.shop_id, product.getShopId());
        values.put(Constants.MRP, product.getMRP());
        open();


        rows = database.insert(TABLE_CART_PRODUCTS, null, values);
        close();
        return rows;
    }

    public boolean exists(int product_id, String sku) {
        boolean exists = false;

        List<CartProduct> mList = getProducts();
        for (int a = 0; a < mList.size(); a++) {
            if (mList.get(a).getItemID() == product_id && mList.get(a).getItemSKU().equalsIgnoreCase(sku)) {
                exists = true;
            }

        }






 /*       open();
        String weight="";
         weight=weight1;

        Cursor cursor=database.rawQuery("SELECT * FROM "+Constants.TABLE_CART_PRODUCTS+" WHERE ID="+product_id+" and unit_weight="+weight,null);
        exists=cursor.moveToNext();

        close();
 */
        return exists;
    }

    public boolean exists1(String productid) {
        boolean exists = false;
        open();
        Cursor cursor = database.rawQuery("SELECT ItemID FROM " + TABLE_CART_PRODUCTS + " WHERE ItemID='"+ productid+ "'", null);
        exists = cursor.moveToNext();
        close();
        return exists;
    }

    public boolean isShopIdExists(String shopId) {
        boolean exists = false;
        open();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_CART_PRODUCTS + " WHERE shop_id='" + shopId + "'", null);
        exists = cursor.moveToNext();
        close();
        return exists;
    }


    public static String getDropTableAlbum() {
        return "DROP TABLE IF EXISTS " + TABLE_CART_PRODUCTS;
    }


    public long update(CartProduct product) {
        long rows = 0l;


        ContentValues values = new ContentValues();
        values.put(Constants.ItemID, product.getItemID());
        values.put(Constants.companyid, product.getCompanyid());
        values.put(Constants.ItemName, product.getItemName());
        values.put(Constants.GroupID, product.getGroupID());
        values.put(Constants.OpeningStock, product.getOpeningStock());
        values.put(Constants.MOQ, product.getMOQ());
        values.put(Constants.ROQ, product.getROQ());
        values.put(Constants.PurchaseUOM, product.getPurchaseUOM());
        values.put(Constants.PurchaseUOMId, product.getPurchaseUOMId());
        values.put(Constants.SaleUOM, product.getSaleUOM());
        values.put(Constants.SaleUOMID, product.getSaleUOMID());
        values.put(Constants.PurchaseRate, product.getPurchaseRate());
        values.put(Constants.SaleRate, product.getSaleRate());
        values.put(Constants.ItemSKU, product.getItemSKU());
        values.put(Constants.ItemBarcode, product.getItemBarcode());
        values.put(Constants.StockUOM, product.getStockUOM());
        values.put(Constants.ItemImage, product.getItemImage());
        values.put(Constants.HSNCode, product.getHSNCode());
        values.put(Constants.quantity, product.getQuantity());
        values.put(Constants.sub_cat_id, product.getSubCategory());
        values.put(Constants.shop_id, product.getShopId());
        values.put(Constants.MRP, product.getMRP());

        open();
        rows = database.update(TABLE_CART_PRODUCTS, values, Constants.ItemID + "=?" + " AND " + Constants.ItemSKU + "=?", new String[]{product.getItemID() + "", product.getItemSKU()});
        close();
        return rows;
    }

    public int delete(int product_id) {
        int rows = 0;
        open();
        rows = database.delete(TABLE_CART_PRODUCTS, "ItemID=?", new String[]{product_id + ""}/*new String[]{product_id+"",weight}*/);
        close();
        return rows;
    }

    public int deleteAll() {
        int rows = 0;
        open();
        rows = database.delete(TABLE_CART_PRODUCTS, null, null);
        close();
        return rows;
    }

    public ArrayList<CartProduct> getProducts() {
        ArrayList<CartProduct> list = new ArrayList<>();
        open();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_CART_PRODUCTS, null);
        while (cursor.moveToNext()) {

            CartProduct product = new CartProduct();

            product.setItemID(cursor.getInt(0));
            product.setCompanyid(cursor.getString(1));
            product.setItemName(cursor.getString(2));
            product.setGroupID(cursor.getString(3));
            product.setOpeningStock(cursor.getString(4));
            product.setMOQ(cursor.getString(5));
            product.setROQ(cursor.getString(6));
            product.setPurchaseUOM(cursor.getString(7));
            product.setPurchaseUOMId(cursor.getString(8));
            product.setSaleUOM(cursor.getString(9));
            product.setSaleUOMID(cursor.getString(10));
            product.setPurchaseRate(cursor.getString(11));
            product.setSaleRate(cursor.getString(12));
            product.setItemSKU(cursor.getString(13));
            product.setItemBarcode(cursor.getString(14));
            product.setStockUOM(cursor.getString(15));
            product.setItemImage(cursor.getString(16));
            product.setHSNCode(cursor.getString(17));
            product.setQuantity(cursor.getInt(18));
            product.setSubCategory(cursor.getString(19));
            product.setShopId(cursor.getString(20));
            product.setMRP(cursor.getString(21));
           // Log.d("cursorMrp",cursor.getString(20));
          //  Log.d("cursorAlldata",cursor.toString());

             list.add(product);
        }
        close();
        return list;
    }

    public CartProduct getProduct(int product_id, String weight) {
        CartProduct product = null;
        open();

        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_CART_PRODUCTS + " WHERE ItemID=?" + " AND ItemSKU=?", new String[]{product_id + "", weight});
        while (cursor.moveToNext()) {
            product = new CartProduct();
            product.setItemID(cursor.getInt(0));
            product.setCompanyid(cursor.getString(1));
            product.setItemName(cursor.getString(2));
            product.setGroupID(cursor.getString(3));
            product.setOpeningStock(cursor.getString(4));
            product.setMOQ(cursor.getString(5));
            product.setROQ(cursor.getString(6));
            product.setPurchaseUOM(cursor.getString(7));
            product.setPurchaseUOMId(cursor.getString(8));
            product.setSaleUOM(cursor.getString(9));
            product.setSaleUOMID(cursor.getString(10));
            product.setPurchaseRate(cursor.getString(11));
            product.setSaleRate(cursor.getString(12));
            product.setItemSKU(cursor.getString(13));
            product.setItemBarcode(cursor.getString(14));
            product.setStockUOM(cursor.getString(15));
            product.setItemImage(cursor.getString(16));
            product.setHSNCode(cursor.getString(17));
            product.setQuantity(cursor.getInt(18));
            product.setSubCategory(cursor.getString(19));
            product.setShopId(cursor.getString(20));
            product.setMRP(cursor.getString(21));

        }

        close();
        return product;
    }

    public CartProduct getProductFromItem(int product_id) {
        CartProduct product = null;
        open();

        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_CART_PRODUCTS + " WHERE ItemID='"+ product_id+ "'", null);

     //   Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_CART_PRODUCTS + " WHERE ItemID=?" , new String[]{product_id + ""});
        while (cursor.moveToNext()) {
            product = new CartProduct();
            product.setItemID(cursor.getInt(0));
            product.setCompanyid(cursor.getString(1));
            product.setItemName(cursor.getString(2));
            product.setGroupID(cursor.getString(3));
            product.setOpeningStock(cursor.getString(4));
            product.setMOQ(cursor.getString(5));
            product.setROQ(cursor.getString(6));
            product.setPurchaseUOM(cursor.getString(7));
            product.setPurchaseUOMId(cursor.getString(8));
            product.setSaleUOM(cursor.getString(9));
            product.setSaleUOMID(cursor.getString(10));
            product.setPurchaseRate(cursor.getString(11));
            product.setSaleRate(cursor.getString(12));
            product.setItemSKU(cursor.getString(13));
            product.setItemBarcode(cursor.getString(14));
            product.setStockUOM(cursor.getString(15));
            product.setItemImage(cursor.getString(16));
            product.setHSNCode(cursor.getString(17));
            product.setQuantity(cursor.getInt(18));
            product.setSubCategory(cursor.getString(19));
            product.setShopId(cursor.getString(20));
            product.setMRP(cursor.getString(21));

        }

        close();
        return product;
    }

    public int getRowCount() {
        int count = 0;
        open();
        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM " + TABLE_CART_PRODUCTS, null);
        if (cursor.moveToNext()) {
            count = cursor.getInt(0);
        }
        close();
        return count;
    }

    public int getTotalQty() {
        int qty = 0;
        open();
      try {
          Cursor cursor = database.rawQuery("SELECT SUM(quantity) FROM " + TABLE_CART_PRODUCTS, null);
          if (cursor.moveToNext()) {
              qty = cursor.getInt(0);
          }
      }catch (Exception e){

      }
        close();
        return qty;
    }

    public String getTotalAmount() {
        String value = "";
        open();
        Cursor cursor = database.rawQuery("SELECT SUM(unit_price*quantity) FROM " + TABLE_CART_PRODUCTS, null);
        if (cursor.moveToNext()) {
            value = cursor.getString(0);
        }
        close();
        if (value == null) {
            return "0.00";
        }
        value = String.format("%.2f", Float.parseFloat(value));
        return value;
    }


}
