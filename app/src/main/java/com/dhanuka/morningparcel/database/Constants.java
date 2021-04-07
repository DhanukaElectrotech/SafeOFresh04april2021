package com.dhanuka.morningparcel.database;

/**
 * Created by Hitesh on 11/25/2016.
 */
public class Constants {

    public static final String GOOGLE_API_KEY = "AIzaSyC1ijjKXJwYV-oNmNsXAKI88ol-eiunTk8";

    //Table names
    public static final String TABLE_CART_PRODUCTS = "cart_products";

    public static final String ItemID = "ItemID";
    public static final String companyid = "companyid";
    public static final String ItemName = "ItemName";
    public static final String GroupID = "GroupID";
    public static final String OpeningStock = "OpeningStock";
    public static final String MOQ = "MOQ";
    public static final String ROQ = "ROQ";
    public static final String PurchaseUOM = "PurchaseUOM";
    public static final String PurchaseUOMId = "PurchaseUOMId";
    public static final String SaleUOM = "SaleUOM";
    public static final String SaleUOMID = "SaleUOMID";
    public static final String PurchaseRate = "PurchaseRate";
    public static final String SaleRate = "SaleRate";
    public static final String ItemSKU = "ItemSKU";
    public static final String ItemBarcode = "ItemBarcode";
    public static final String StockUOM = "StockUOM";
    public static final String ItemImage = "ItemImage";
    public static final String HSNCode = "HSNCode";
    public static final String quantity = "quantity";
    public static final String sub_cat_id = "sub_cat_id";
    public static final String shop_id = "shop_id";
    public static final String MRP = "MRP";


    //Query for creating products table
    public static final String CREATE_PRODUCTS = "CREATE TABLE IF NOT EXISTS " + TABLE_CART_PRODUCTS
            + " (" + ItemID
            + " INTEGER, "
            + companyid
            + " VARCHAR, "
            + ItemName
            + " VARCHAR, "
            + GroupID
            + " VARCHAR, "
            + OpeningStock
            + " TEXT, "
            + MOQ
            + " VARCHAR, "
            + ROQ
            + " VARCHAR, "
            + PurchaseUOM
            + " VARCHAR, "
            + PurchaseUOMId
            + " VARCHAR, "
            + SaleUOM
            + " VARCHAR, "
            + SaleUOMID
            + " VARCHAR, "
            + PurchaseRate
            + " VARCHAR, "
            + SaleRate
            + " VARCHAR, "
            + ItemSKU
            + " VARCHAR, "
            + ItemBarcode
            + " VARCHAR, "
            + StockUOM
            + " VARCHAR, "
            + ItemImage
            + " VARCHAR, "
            + HSNCode
            + " VARCHAR, "
            + quantity
            + " INTEGER, "
            + sub_cat_id
            + " VARCHAR, "
            + shop_id
            + " VARCHAR,"
            + MRP
            + " VARCHAR)";

    public static final String ALTER_PRODUCTS_1 = "ALTER TABLE " + TABLE_CART_PRODUCTS
            + " ADD " + sub_cat_id + " VARCHAR";
    public static final String ALTER_PRODUCTS_2 = "ALTER TABLE " + TABLE_CART_PRODUCTS
            + " ADD " + MRP + " VARCHAR";
}