package com.dhanuka.morningparcel.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.R;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.dhanuka.morningparcel.Helper.InvoiceDAO;
import com.dhanuka.morningparcel.Helper.InvoiceHelper;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.SqlDatabase.DatabaseHelper;
import com.dhanuka.morningparcel.SqlDatabase.DatabaseManager;
import com.dhanuka.morningparcel.beans.FancyToast;
import com.dhanuka.morningparcel.utils.JKHelper;

public class UpdateInvoice extends Service {
    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getInvoiceDetail(intent.getStringExtra("orderid"));
        Toast.makeText(getApplicationContext(),intent.getStringExtra("orderid"),Toast.LENGTH_LONG).show();


        return super.onStartCommand(intent, flags, startId);
    }

    public void getInvoiceDetail(String orderid) {
        ArrayList<InvoiceHelper> invoicegetlist=new ArrayList<>();
        ArrayList<InvoiceHelper> invoicegetlist2=new ArrayList<>();


        DatabaseManager.initializeInstance(new DatabaseHelper(getApplicationContext()));
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        InvoiceDAO invoiceDAO = new InvoiceDAO(database, this);


        ArrayList<InvoiceHelper> testqueue= new ArrayList<>();
        InvoiceHelper invoiceHelper= new InvoiceHelper();
        testqueue=invoiceDAO.getNewUploadList(orderid);




        Log.d("stringbuildinglist", String.valueOf(invoicegetlist.size()));
        for (int i = 0; i < testqueue.size(); i++) {

            updateOrderStatus(UpdateInvoice.this,String.valueOf(testqueue.get(i).getOrderid()),"5",testqueue.get(i).getInvoiceno(),testqueue.get(i).getInvoiceamount(),testqueue.get(i).getInvoicedate());


            // Toast.makeText(getApplicationContext(),testqueue.get(i).getInvoicedate(),Toast.LENGTH_LONG).show();
        }


    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void updateOrderStatus(Context ctx, String oid, String type, String invoiceno,String invoiceamount,String invoicedate)
    {


//        final ProgressDialog mProgressBar = new ProgressDialog(ctx);
//        mProgressBar.setTitle("Safe'O'Fresh");
//        mProgressBar.setMessage("Loading...");
//        mProgressBar.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST,  ctx.getString(R.string.URL_BASE_URL),


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JKHelper jkHelper= new JKHelper();
                            String responses=jkHelper.Decryptapi(response,ctx);
                            Log.d("responsive", responses);
                            JSONObject jsonObject = new JSONObject(responses);
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                FancyToast.makeText(ctx, "invoice upload", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


                            } else {

                                FancyToast.makeText(ctx, "failed to update the order, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            FancyToast.makeText(ctx, "failed to update the order, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

                        }
                        //mProgressBar.dismiss();


                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        FancyToast.makeText(ctx, "failed to update the order, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                        FancyToast.makeText(ctx, "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                String mTodayDate = df.format(c.getTime());


                Preferencehelper prefs;
                prefs = new Preferencehelper(ctx);
                Map<String, String> params = new HashMap<String, String>();
                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);

                    String param = ctx.getString(R.string.UPDATE_ORDER_STATUS)+"&orderid=" +oid + "&status=" +"1"+"&type="+type+"&contactId="+prefs.getPrefsContactId()
                            +"&InvoiceDate="+invoicedate+"&InvoiceNumber="+invoiceno+"&InvoiceAmount="+invoiceamount;
                    Log.d("Beforeencrptionmeupdate", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, ctx);
                    params.put("val", finalparam);
                    Log.d("afterencrptionmeupdate", finalparam);
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
}
