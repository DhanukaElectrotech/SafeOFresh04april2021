package com.dhanuka.morningparcel.activity;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.BuildConfig;
import com.dhanuka.morningparcel.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.beans.FancyToast;
import com.dhanuka.morningparcel.beans.ItemSalesReport;
import com.dhanuka.morningparcel.events.Margindata;
import com.dhanuka.morningparcel.events.Setmargin;
import com.dhanuka.morningparcel.utils.JKHelper;

public class SetmarginService extends Service implements Setmargin {

    List<ItemSalesReport> mListReport = new ArrayList<>();
    Margindata margindata;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        getReports(SetmarginService.this, intent.getStringExtra("mstartdate"), intent.getStringExtra("menddate"));


        return super.onStartCommand(intent, flags, startId);
    }

    public void getReports(Context context, String strtDate, String endDate) {


        String finalStrtDate = strtDate;
        String finalEndDate = endDate;
        StringRequest postRequest = new StringRequest(Request.Method.POST, context.getString(R.string.URL_BASE_URL),


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            Log.d("response_itemWkise", response);
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, context);
                            JSONObject jsonObject = new JSONObject(responses);
                            Log.d("response_itemWkise", responses);
                            //   String marintotal=jsonObject.getString("TotalMargin");


                            float marintotal = (float) 0.0;

                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {

                                mListReport = new Gson().fromJson(jsonObject.getString("returnds"), new TypeToken<List<ItemSalesReport>>() {
                                }.getType());


                                for (int i = 0; i < mListReport.size(); i++) {
                                    if (!mListReport.get(i).getMargin().isEmpty()) {


                                        float numb = Float.parseFloat(mListReport.get(i).getMargin());
                                        marintotal = marintotal + numb;
                                        Log.d("marginnumber-:", String.valueOf(numb) + " " + mListReport.get(i).getItemDescription());
                                        Log.d("margintotal-:", String.valueOf(marintotal));


                                    }
                                }

                                Intent customBroadcastIntent = new Intent("Marginupdates");
                                customBroadcastIntent.putExtra("margindata", String.valueOf(marintotal));
                                Bundle b = new Bundle();
                                customBroadcastIntent.putExtra("margindata8", b);

                                LocalBroadcastManager.getInstance(context).sendBroadcast(customBroadcastIntent);


                                stopService(new Intent(context, SetmarginService.class));


                            } else {


                                com.dhanuka.morningparcel.beans.FancyToast.makeText(context, "No Data Found, try after some time.", com.dhanuka.morningparcel.beans.FancyToast.LENGTH_SHORT, com.dhanuka.morningparcel.beans.FancyToast.INFO, false).show();


                            }

                        } catch (Exception e) {


                            e.printStackTrace();

                        }


                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        com.dhanuka.morningparcel.beans.FancyToast.makeText(context, "Something went wrong.", com.dhanuka.morningparcel.beans.FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");

                Preferencehelper prefs;
                prefs = new Preferencehelper(context);
                Map<String, String> params = new HashMap<String, String>();

                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);

                    String param = getString(R.string.ITEM_REPORTS) + "&contactid=" + prefs.getPrefsContactId() + "&strdt=" + finalStrtDate + "&enddt=" + finalEndDate + "&group_id=" + "";
                    Log.d("Beforeencrptionpastodr", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, context);
                    params.put("val", finalparam);
                    Log.d("afterencrptionpastodr", finalparam);
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

        Volley.newRequestQueue(getApplicationContext()).add(postRequest);


    }


    @Override
    public void Setmargindata(Context context, String mstartdate, String menddate) {


    }
}
