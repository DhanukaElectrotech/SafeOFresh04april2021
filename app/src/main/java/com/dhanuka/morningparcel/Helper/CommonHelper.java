package com.dhanuka.morningparcel.Helper;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.Controllers.AppUrls;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.R;
import com.dhanuka.morningparcel.SqlDatabase.DatabaseHelper;
import com.dhanuka.morningparcel.SqlDatabase.DatabaseManager;
 import com.dhanuka.morningparcel.SqlDatabase.QueryExecutor;
 import com.dhanuka.morningparcel.SqlDatabase.UomMasterDAO;
 import com.dhanuka.morningparcel.SqlDatabase.VendorDAO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommonHelper extends Application {
    int i;
    ProgressDialog mProgressBar ;

     public void getuomData(final Context ctx,final String contactid) {


        ArrayList<ItemUomHelper> uomlist = new ArrayList<>();
        Preferencehelper prefs;
        prefs=new Preferencehelper(ctx);
        StringRequest postRequest = new StringRequest(Request.Method.POST, ctx.getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("Response", response);

                        prefs.setPrefsRunonce("0");

                        try {
                            JKHelper jkHelper= new JKHelper();
                            String responses=jkHelper.Decryptapi(response,ctx);
                            JSONObject jsonObject = new JSONObject(responses);
                            JSONArray jsonArray = jsonObject.getJSONArray("returnds");
                            if (jsonObject.getInt("success") == 1) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject newjson = jsonArray.getJSONObject(i);
                                    String ItemUOMID = newjson.getString("ItemUOMID");
                                    String ItemID = newjson.getString("ItemID");
                                    String ItemDescription = newjson.getString("ItemDescription");
                                    String UOMID = newjson.getString("UOMID");
                                    String UOMDesc = newjson.getString("UOMDesc");
                                    String Qty = newjson.getString("Qty");
                                    String PurchaseRate = newjson.getString("PurchaseRate");
                                    String SaleRate = newjson.getString("SaleRate");
                                    ItemUomHelper v = new ItemUomHelper();
                                    v.setItemID(ItemUOMID);
                                    v.setItemUOMID(ItemID);
                                    v.setItemDescription(ItemDescription);
                                    v.setUOMID(UOMID);
                                    v.setUOMDesc(UOMDesc);
                                    v.setQty(Qty);
                                    v.setPurchaseRate(PurchaseRate);
                                    v.setSaleRate(SaleRate);
                                    uomlist.add(v);


                                }
                                Log.d("masterlistUOM", String.valueOf(uomlist.size()));

                                DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                                    @Override
                                    public void run(SQLiteDatabase database) {

                                        UomMasterDAO uomdao = new UomMasterDAO(database,ctx);
                                        ArrayList<ItemUomHelper> list = uomlist;
                                        uomdao.deleteAll();
                                        uomdao.insert(list);
                                        Log.d("masterlistUOM", " uom Data saved");


                                    }
                                });


                            } else {

                            }

                        } catch (Exception e) {

                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        // error
                        //   Log.e("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();



                try {
                    String param = AppUrls.GET_UOM_ITEM+ "&ContactID=" + prefs.getPrefsContactId() ;
                    Log.d("Beforeencrption", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, ctx);
                    params.put("val", finalparam);
                    Log.d("afterencrption", finalparam);
                    return params;

                } catch (Exception e) {
                    e.printStackTrace();
                    return params;
                }




            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(ctx).add(postRequest);
    }





     public void getAllvendors(final Context ctx, final String companyid) {


        DatabaseManager.initializeInstance(new DatabaseHelper(ctx));
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();


        StringRequest postRequest = new StringRequest(Request.Method.POST,ctx.getString(R.string.URL_BASE_URL) ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response


                        try {
                            JKHelper jkHelper= new JKHelper();
                            String responses=jkHelper.Decryptapi(response,ctx);
                            Log.e("Response", responses);
                            JSONObject jsonObject = new JSONObject(responses);
                            int success = jsonObject.getInt("success");
                            //  Log.e("success" + success);
                            Log.d("success" ,String.valueOf(+ success));
                            if (success == 1) {

                                JSONArray jsonArray = jsonObject.getJSONArray("fillvendor");

                                final ArrayList<VendorHelper> vendorlist = new ArrayList<VendorHelper>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject singleObj = jsonArray.getJSONObject(i);
                                    String companyname = singleObj.getString("CompanyName");
                                    String companyid = singleObj.getString("MContactID");
                                    VendorHelper v = new VendorHelper();
                                    v.setCompanyid(companyid);
                                    v.setCompanyname(companyname);
                                    vendorlist.add(v);

                                }

                                DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                                    @Override
                                    public void run(SQLiteDatabase database) {

                                        VendorDAO vendordao = new VendorDAO(database, ctx);
                                        ArrayList<VendorHelper> list = vendorlist;
                                        vendordao.deleteAll();
                                        vendordao.insert(list);
                                    }
                                });
                                //Message.message(ctx, "Data fetched Successfuly");
                            } else if (success == 0) {
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        //   Log.e("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                try {
                    String param = AppUrls.URL_ALL_VENDOR_LIST+ "&Mid=" + companyid;
                    Log.d("Beforeencrption", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, ctx);
                    params.put("val", finalparam);
                    Log.d("afterencrption", finalparam);
                    return params;

                } catch (Exception e) {
                    e.printStackTrace();
                    return params;
                }

            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(ctx).add(postRequest);







    }


    public static  void sendRegistrationToServer(final Context ctx,final String token,final String contactId) {
        //    com.shashank.sony.fancytoastlib.FancyToast.makeText(getApplicationContext(), token+"", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();


        // TODO: Implement this method to send token to your app server.
        if (token != null && !token.isEmpty()) {


            StringRequest postRequest = new StringRequest(Request.Method.POST, AppUrls.URL_NOTIFICATION_REGISTER,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //7042741404
                            // response
                            Log.e("ResponseGCM", response);
                            String res = response;
                            //  Intent myService = new Intent(ctx, UploadFCMActivity.class);
                            // stopService(myService);
                            Toast.makeText(ctx, "welcome to Safe'O'Fresh", Toast.LENGTH_LONG).show();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.e("error Service Api", String.valueOf(error));

                            //   Log.e("Error.Response", error.getMessage());
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("contactid", contactId);
                    Log.d("contactidee", contactId);
                    params.put("GCMID", token);
                    Log.e("GGCCMM", params.toString());
                    return params;
                }
            };
            postRequest.setRetryPolicy(new DefaultRetryPolicy(
                    60000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(ctx).add(postRequest);
        } else {

            Log.e("FCCMM","token not found ");
            Toast.makeText(ctx, "FCM Token not found", Toast.LENGTH_SHORT).show();
        }
    }


}
