package com.dhanuka.morningparcel;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import com.dhanuka.morningparcel.Helper.ItemMasterhelper;
import com.dhanuka.morningparcel.SqlDatabase.All_Item_Small;
import com.dhanuka.morningparcel.SqlDatabase.QueryExecutor;

public class DataSave extends Service {
    public DataSave() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {

            ArrayList<ItemMasterhelper> masterlist = new ArrayList<>();

            SharedPreferences prefs11 = getApplicationContext().getSharedPreferences("MORNING_PARCEL_GROCERY",
                    getApplicationContext().MODE_PRIVATE);

            JSONObject jsonObject = new JSONObject(prefs11.getString("resp1", "-1"));
            JSONArray jsonArray = jsonObject.getJSONArray("returnds");
            if (jsonObject.getInt("success") == 1) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject newjson = jsonArray.getJSONObject(i);
                    String ItemID = newjson.getString("ItemID");
                    String ItemName = newjson.getString("ItemName");
                    String companyid = newjson.getString("companyid");
                    String GroupID = newjson.getString("GroupID");
                    String OpeningStock = newjson.getString("OpeningStock");
                    String MRP = newjson.getString("MRP");
                    String PurchaseRate = newjson.getString("PurchaseRate");
                    String SaleRate = newjson.getString("SaleRate");
                    String StockUOM = newjson.getString("StockUOM");
                    String ItemImage = newjson.getString("ItemImage");

                    ItemMasterhelper v = new ItemMasterhelper();
                    v.setItemID(ItemID);
                    v.setItemName(ItemName);
                    v.setCompanyid(companyid);
                    v.setGroupID(GroupID);
                    v.setOpeningStock(OpeningStock);
                    v.setMRP(MRP);
                    v.setPurchaseRate(PurchaseRate);
                    v.setSaleRate(SaleRate);
                    v.setStockUOM(StockUOM);
                    v.setItemImage(ItemImage);
                    masterlist.add(v);
                    //Log.d("masterlist", String.valueOf(masterlist.size()));



                                        // mProgressBar.dismiss();

                                        com.dhanuka.morningparcel.SqlDatabase.DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                                            @Override
                                            public void run(SQLiteDatabase database) {

                                                All_Item_Small masterdao = new All_Item_Small(database, getApplicationContext());
                                                ArrayList<ItemMasterhelper> list = masterlist;

                                                masterdao.deleteAll();
                                                masterdao.insert(masterlist);
                                                Log.d("Datainsertingdb",String.valueOf(masterlist.size()));
                                                // Log.d("Savedataindb", String.valueOf(list.size()));
                                                //Toast.makeText(SearchActivity.this, "Data saved", Toast.LENGTH_LONG).show();

                                            }
                                        });



                }


            }






        } catch (Exception e) {
            // mProgressBar.dismiss();

            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
