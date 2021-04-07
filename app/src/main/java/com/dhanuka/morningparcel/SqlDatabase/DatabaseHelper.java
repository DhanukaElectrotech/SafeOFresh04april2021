package com.dhanuka.morningparcel.SqlDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dhanuka.morningparcel.Helper.CartOrderDAO;
import com.dhanuka.morningparcel.Helper.ImageUploadDAO;
import com.dhanuka.morningparcel.Helper.ImageMasterDAO;
import com.dhanuka.morningparcel.Helper.InvoiceDAO;
import com.dhanuka.morningparcel.activity.retail.beans.BillAbleUnderDAO;
import com.dhanuka.morningparcel.database.DatabaseManager;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "morningparcel.db";
    public static final int DATABASE_VERSION =1;

    private Context mContext;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // create all tables
         sqLiteDatabase.execSQL(ImageUploadDAO.getCreateTableImageUpload());
        sqLiteDatabase.execSQL(ImageMasterDAO.getCreateTableImageMaster());
        sqLiteDatabase.execSQL(CartOrderDAO.getCreateTableCartMaster());
         sqLiteDatabase.execSQL(ConsigneeDAO.getCreateTableConsignee());
        sqLiteDatabase.execSQL(All_Item_Master.getCreateMasterTable());
        sqLiteDatabase.execSQL(InvoiceDAO.getCreateInvoiceUpload());
        sqLiteDatabase.execSQL(VendorDAO.getCreateTableVendor());
        sqLiteDatabase.execSQL(UomMasterDAO.getCreateUomMasterTable());
        sqLiteDatabase.execSQL(All_Item_Small.getCreateMasterTable());
         sqLiteDatabase.execSQL(BuildingDAO.getCreateBuildingTable());
        sqLiteDatabase.execSQL(SocietyDao.getCreatesocietyTable());
        sqLiteDatabase.execSQL(CityDAO.getCreateBuildingTable());
        sqLiteDatabase.execSQL(BillAbleUnderDAO.getCreateTableBillableUnder());

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

            // drop all tables
            sqLiteDatabase.execSQL(ImageUploadDAO.getDropTableImageUpload());
            sqLiteDatabase.execSQL(ImageMasterDAO.getDropTableImageMaster());
            sqLiteDatabase.execSQL(CartOrderDAO.getDropTableImageMaster());
            sqLiteDatabase.execSQL(ConsigneeDAO.getDropTableAlbum());
            sqLiteDatabase.execSQL(All_Item_Master.getDropTableAlbum());
            sqLiteDatabase.execSQL(InvoiceDAO.getDropTableImageUpload());
            sqLiteDatabase.execSQL(VendorDAO.getDropTableAlbum());
            sqLiteDatabase.execSQL(UomMasterDAO.getDropTableAlbum());
        sqLiteDatabase.execSQL(All_Item_Small.getDropTableAlbum());
            sqLiteDatabase.execSQL(BuildingDAO.getDropTableAlbum());
            sqLiteDatabase.execSQL(SocietyDao.getDropTableAlbum());
            sqLiteDatabase.execSQL(CityDAO.getDropTableAlbum());
            sqLiteDatabase.execSQL(DatabaseManager.getDropTableAlbum());
        sqLiteDatabase.execSQL(BillAbleUnderDAO.getCreateTableBillableUnder());
        //recreate all
            onCreate(sqLiteDatabase);


    }
}
