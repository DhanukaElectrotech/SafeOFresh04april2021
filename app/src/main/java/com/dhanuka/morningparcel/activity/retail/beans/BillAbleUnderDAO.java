package com.dhanuka.morningparcel.activity.retail.beans;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;


public class BillAbleUnderDAO {

    private static final String TABLE_BILLABLE_UNDER = "billable_under";

    // Contacts Table Columns names
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_CLIENT_ID = "client_id";
    private static final String COLUMN_PRINT_NAME = "print_name";
    private static final String COLUMN_CURRENT_STATUS = "CurrentStatus";
    private static final String COLUMN_BUSINESS_TYPE = "BusinessType";
    private static final String COLUMN_BRANCH_ID = "BranchID";
    private static final String COLUMN_NICK_NAME = "NickName";
    private static final String COLUMN_CODE = "branchcode";
    private static final String COLUMN_NICK_TYPE = "Type";


    private SQLiteDatabase mDatabase;
    private Context mContext;

    public BillAbleUnderDAO(SQLiteDatabase database, Context context) {
        mDatabase = database;
        mContext = context;
    }


    public static String getCreateTableBillableUnder() {
        String CREATE_WORK_DETAILS_TABLE = "CREATE TABLE " + TABLE_BILLABLE_UNDER
                + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_CLIENT_ID + " TEXT ,"
                + COLUMN_PRINT_NAME + " TEXT ,"
                + COLUMN_CURRENT_STATUS + " TEXT ,"
                + COLUMN_BRANCH_ID + " TEXT ,"
                + COLUMN_BUSINESS_TYPE + " TEXT ,"
                + COLUMN_CODE + " TEXT ,"
                + COLUMN_NICK_NAME + " TEXT "
                + ")";

        return CREATE_WORK_DETAILS_TABLE;
    }

    public static String getDropTableBillAbleUnder() {
        return "DROP TABLE IF EXISTS " + TABLE_BILLABLE_UNDER;
    }


    public void deleteAll() {

        String delete_all_users = " DELETE "
                + " FROM "
                + TABLE_BILLABLE_UNDER;

        mDatabase.execSQL(delete_all_users);
    }


    public void insert(ArrayList<DbBillAbleUnder> workArrayList) {
        for (DbBillAbleUnder address : workArrayList) {
            String[] bindArgs = {
                    address.getmClientId(),
                    address.getmPrintName(),
                    address.getmbranchid(),
                    address.getmcurrentstatus(),
                    address.getmbusinesstype(),
                    address.getmnickname(),
                    address.getmbranchcode()
            };

            String insertUser = " INSERT INTO "
                    + TABLE_BILLABLE_UNDER
                    + " ( "
                    + COLUMN_CLIENT_ID
                    + " , "
                    + COLUMN_PRINT_NAME
                    + " , "
                    + COLUMN_BRANCH_ID
                    + " , "
                    + COLUMN_CURRENT_STATUS
                    + " , "
                    + COLUMN_BUSINESS_TYPE
                    + " , "
                    + COLUMN_NICK_NAME
                    + " , "
                    + COLUMN_CODE
                    + " ) "
                    + " VALUES "
                    + " (?,?,?,?,?,?,?)";

            mDatabase.execSQL(insertUser, bindArgs);
        }
    }

    public ArrayList<DbBillAbleUnder> selectAll() {
        String getAllWorkDetails = " SELECT "
                + " * "
                + " FROM "
                + TABLE_BILLABLE_UNDER + " where print_name <>''";

        Cursor cursor = mDatabase.rawQuery(getAllWorkDetails, null);
        ArrayList<DbBillAbleUnder> dataList = manageCursor(cursor);
        closeCursor(cursor);

        return dataList;
    }

    public ArrayList<DbBillAbleUnder> selectAllLock() {
        String getAllWorkDetails = " SELECT "
                + " * "
                + " FROM "
                + TABLE_BILLABLE_UNDER + " where print_name <>'' and BranchID=1";

        Cursor cursor = mDatabase.rawQuery(getAllWorkDetails, null);
        ArrayList<DbBillAbleUnder> dataList = manageCursor(cursor);
        closeCursor(cursor);

        return dataList;
    }

    public ArrayList<DbBillAbleUnder> selectAllbycityandtrade(Integer branchid, Integer tradetype ) {
        String getAllWorkDetails="";
        if (tradetype>0)
        {
            getAllWorkDetails = " SELECT "
                    + " billable_under.*,BRANCH.BranchCode "
                    + " FROM billable_under left join BRANCH on billable_under.BranchID=BRANCH.branchid where print_name <>''  and  billable_under.branchid<>153 and billable_under.BusinessType=" + tradetype + " and billable_under.BranchID in (" +
                    "Select branchid from BRANCH where  masterbranchid=" + branchid + " )";
        }
        else
        {
            getAllWorkDetails = " SELECT "
                    + " billable_under.*,BRANCH.BranchCode " +
                    "  FROM billable_under left join BRANCH on billable_under.BranchID=BRANCH.branchid  where billable_under.print_name <>''  and  billable_under.branchid<>153 and billable_under.BranchID in (" +
                    "Select branchid from BRANCH where  masterbranchid=" + branchid + " )";
        }
        Cursor cursor = mDatabase.rawQuery(getAllWorkDetails, null);
        ArrayList<DbBillAbleUnder> dataList = manageCursor(cursor);
        closeCursor(cursor);

        return dataList;
    }


    public ArrayList<DbBillAbleUnder> selectAllbycityandtradeAlias(Integer branchid, Integer tradetype ) {
        String getAllWorkDetails="";
        if (tradetype>0)
        {
            getAllWorkDetails = " SELECT "
                    + " a._id , a.client_id , CodeDescription + '-' +  a.print_name ,  a.CurrentStatus , a.BusinessType , a.BranchID , a.NickName   "
                    + " FROM "
                    + TABLE_BILLABLE_UNDER  + "  a , CATCODE b where print_name <>''  and  CatCodeID='24' and Val2=a.client_id and  a.branchid<>153 and BusinessType=" + tradetype + " and a.BranchID in (" +
                    "Select branchid from BRANCH where  masterbranchid=" + branchid + " )";
        }
        else
        {
            getAllWorkDetails = " SELECT "
                    + " a._id , a.client_id , CodeDescription + '-' +  a.print_name ,  a.CurrentStatus , a.BusinessType , a.BranchID , a.NickName   "
                    + " FROM "
                    + TABLE_BILLABLE_UNDER  + "  a , CATCODE b where print_name <>''  and  CatCodeID='24' and Val2=a.client_id and  a.branchid<>153 and a.BranchID in (" +
                    "Select branchid from BRANCH where  masterbranchid=" + branchid + " )";
        }

        ArrayList<DbBillAbleUnder> dataList=null;
        try {
            Cursor cursor = mDatabase.rawQuery(getAllWorkDetails, null);
            dataList = manageCursor(cursor);
            closeCursor(cursor);
        } catch (Exception e) {
            String errormessage=e.getMessage();
        }



        return dataList;
    }





    protected DbBillAbleUnder cursorToData(Cursor cursor) {
        DbBillAbleUnder work = new DbBillAbleUnder();
        work.setmId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
        work.setmClientId(cursor.getString(cursor.getColumnIndex(COLUMN_CLIENT_ID)));
        work.setmPrintName(cursor.getString(cursor.getColumnIndex(COLUMN_PRINT_NAME)));
        work.setmbranchid(cursor.getString(cursor.getColumnIndex(COLUMN_BRANCH_ID)));
        work.setmbranchcode(cursor.getString(cursor.getColumnIndex(COLUMN_CODE)));
        work.setmcurrentstatus(cursor.getString(cursor.getColumnIndex(COLUMN_CURRENT_STATUS)));
        work.setmbusinesstype(cursor.getString(cursor.getColumnIndex(COLUMN_BUSINESS_TYPE)));
        work.setmnickname(cursor.getString(cursor.getColumnIndex(COLUMN_NICK_NAME)));

        return work;
    }

    protected void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }

    protected ArrayList<DbBillAbleUnder> manageCursor(Cursor cursor) {
        ArrayList<DbBillAbleUnder> dataList = new ArrayList<DbBillAbleUnder>();
        //DbBillAbleUnder model=null;
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                DbBillAbleUnder model = cursorToData(cursor);
                if (model != null) {
                    dataList.add(model);
                }
                cursor.moveToNext();
            }
        }
        return dataList;
    }

}
