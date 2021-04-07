package com.dhanuka.morningparcel.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.dhanuka.MMthinkBizUtils.DonutProgress;
import com.dhanuka.morningparcel.beans.Dynamicbranchbean;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.Helper.Preferencehelper;

import com.dhanuka.morningparcel.R;
import com.google.gson.Gson;

import com.dhanuka.morningparcel.adapter.PastOrderItemAdapter;
import com.dhanuka.morningparcel.beans.CartProduct;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.dhanuka.morningparcel.beans.FancyToast;

public class PastOrderItem extends AppCompatActivity {
    CartProduct product = new CartProduct();
    ;
    ArrayList<CartProduct> pastorderlist = new ArrayList<>();
    HashMap<String, String> branchhash = new HashMap<>();
    ArrayList<Dynamicbranchbean.beanchinnerbean> mListReportbranch = new ArrayList<>();
    PastOrderItemAdapter pastadapter;
    @BindView(R.id.pastcontainer)
    RecyclerView pastcontainer;
    @Nullable
    @BindView(R.id.spinner_branch)
    Spinner spinner_branch;

    ArrayList<String> branchlist = new ArrayList<>();
    String date_of_installation, hoursstr;
    private int mYear, mMonth, mDay;
    @BindView(R.id.datselecttxt)
    TextView datselecttxt;
    @BindView(R.id.datetwoselecttxt)
    TextView datetwoselecttxt;
    String branchid = "0";
    @BindView(R.id.submitbtn)
    Button submitbtn;

    @BindView(R.id.txtpendingqtytotl)
    DonutProgress txtpendingqtytotl;
    @Nullable
    @BindView(R.id.whtsappshare)
    ImageView whtsappshare;
    String wtsappstr="";
    ArrayList<CartProduct> ordrlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_past_order_item);
        ButterKnife.bind(this);





        whtsappshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ordrlist= (ArrayList<CartProduct>) pastadapter.getWhatsapporder();
                for (int l=0;l<ordrlist.size();l++)
                {
                    wtsappstr = wtsappstr + "Item - " + ordrlist.get(l).getItemName() + "\nQty - " + ordrlist.get(l).getQuantity() + "\nn*******"; // Replace with your own message.


                }


                if (wtsappstr.isEmpty())
                {

                }
                else
                {



                    PackageManager pm = getPackageManager();
                    try {
                        Intent waIntent = new Intent(Intent.ACTION_SEND);
                        waIntent.setType("text/plain");
                        String text = "";




                        PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                        //Check if package exists or not. If not then code
                        //in catch block will be called
                        waIntent.setPackage("com.whatsapp");
                        waIntent.putExtra(Intent.EXTRA_TEXT, wtsappstr);
                        startActivity(Intent.createChooser(waIntent, "Share Pending Item Info :"));
                    } catch (PackageManager.NameNotFoundException e) {
                        com.shashank.sony.fancytoastlib.FancyToast.makeText(getApplicationContext(), "WhatsApp not Installed", com.shashank.sony.fancytoastlib.FancyToast.LENGTH_SHORT, com.shashank.sony.fancytoastlib.FancyToast.INFO, false)
                                .show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        pastcontainer.setHasFixedSize(true);
        pastcontainer.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        getBranch();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df1 = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String mTodayDate1 = df1.format(calendar.getTime());
        String arr[] = mTodayDate1.split(" ");
        String mTodayDate = arr[0];
        String mTodayTime = arr[1];
        datselecttxt.setText(mTodayDate);
        datetwoselecttxt.setText(mTodayDate);

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pastorderlist.clear();
                pastadapter = new PastOrderItemAdapter(getApplicationContext(), pastorderlist);
                pastcontainer.setAdapter(pastadapter);
                Pastorderitem();

            }
        });
        datetwoselecttxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                panelcleandatepicker(2);
            }
        });
        datselecttxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                panelcleandatepicker(1);
            }
        });

        spinner_branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                branchid = branchhash.get(spinner_branch.getSelectedItem().toString());

                //  getAllProducts();

                // if (position > 0) {

                // }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Pastorderitem();
    }

    public void panelcleandatepicker(int type) {

        final Calendar calendercheck = Calendar.getInstance();

        calendercheck.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
        mYear = calendercheck.get(Calendar.YEAR);
        mMonth = calendercheck.get(Calendar.MONTH);
        mDay = calendercheck.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(PastOrderItem.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String monthOfYears = String.valueOf(monthOfYear + 1);
                        String years = String.valueOf(year);
                        String dayOfMonths = String.valueOf(dayOfMonth);


                        if (dayOfMonths.length() == 1)
                            dayOfMonths = "0" + dayOfMonths;

                        if (years.length() == 1)
                            years = "0" + years;

                        if (monthOfYears.length() == 1)
                            monthOfYears = "0" + monthOfYears;
                        date_of_installation = monthOfYears + "/" + (dayOfMonths) + "/" + years;
                        //   date_of_stoppage =   (monthOfYears) + "/" + dayOfMonths + "/" + years;

                        if (type == 1) {
                            datselecttxt.setText(date_of_installation);

                        } else {

                            datetwoselecttxt.setText(date_of_installation);
                        }


                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.show();
        Calendar c = Calendar.getInstance();
        c.set(mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());


        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String mTodayDate = df.format(c.getTime());
        // formattedDate have current date/time
//
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(mTodayDate));
        calendar.add(Calendar.DAY_OF_YEAR, +29);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());


    }

    public void makeBackClick(View v) {
        onBackPressed();
    }


    public void getBranch() {


        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String mTodayDate = df.format(c.getTime());
        Preferencehelper prefs;
        prefs = new Preferencehelper(getApplicationContext());
        branchlist.clear();
        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("responseWEEK", response);
                            // JKHelper jkHelper = new JKHelper();
                            //  String responses = jkHelper.Decryptapi(response, getApplicationContext());
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, getApplicationContext());
                            JSONObject jsonObject = new JSONObject(responses);
                            Log.d("responseWEEKNew", response);


                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                Dynamicbranchbean branchSalesBean = new Gson().fromJson(responses, Dynamicbranchbean.class);

                                mListReportbranch = branchSalesBean.getBranchdatalist();

                                for (int i = 0; i < mListReportbranch.size(); i++) {

                                    branchhash.put(branchSalesBean.getBranchdatalist().get(i).getBranchName(), branchSalesBean.getBranchdatalist().get(i).getBranchId());

                                    branchlist.add(branchSalesBean.getBranchdatalist().get(i).getBranchName());


                                }

                                ArrayAdapter branchadapter = new ArrayAdapter(PastOrderItem.this, android.R.layout.simple_list_item_1, branchlist);
                                spinner_branch.setAdapter(branchadapter);


                                branchadapter = new ArrayAdapter(getApplicationContext(), R.layout.simple_list_item_single_choice1, branchlist);
                                branchadapter.setDropDownViewResource(R.layout.simple_list_item_single_choice1);
                                SharedPreferences prefs2 = getSharedPreferences("MORNING_PARCEL_GROCERY",
                                        MODE_PRIVATE);


                            } else {

                                //    com.dhanuka.morningparcel.beans.FancyToast.makeText(getApplicationContext(), "No Data Found, try after some time.", com.dhanuka.morningparcel.beans.FancyToast.LENGTH_SHORT, com.dhanuka.morningparcel.beans.FancyToast.INFO, false).show();
                            }

                        } catch (Exception e) {

                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        com.dhanuka.morningparcel.beans.FancyToast.makeText(getApplicationContext(), "Something went wrong.", com.dhanuka.morningparcel.beans.FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                Preferencehelper prefs = new com.dhanuka.morningparcel.Helper.Preferencehelper(getApplicationContext());
                ;
                try {
                    String param = getString(R.string.URL_GET_BRANCH) + "&contactid=" + prefs.getPrefsContactId();
                    Log.d("Beforeencrptionpay", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, PastOrderItem.this);
                    params.put("val", finalparam);
                    Log.d("afterencrptionpay", finalparam);
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

    public void Pastorderitem() {
        final ProgressDialog mProgressBar = new ProgressDialog(PastOrderItem.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("responsepastorder", response);
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, PastOrderItem.this);
                            JSONObject jsonObject = new JSONObject(responses);
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                int totalqty = 0;
                                JSONArray jsonArray = jsonObject.getJSONArray("orderitemdetail");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String ItemId = jsonObject1.getString("ItemId");
                                    String ItemDescription = jsonObject1.getString("ItemDescription");
                                    String Description = jsonObject1.getString("Description");
                                    String Qty = jsonObject1.getString("Qty");
                                    String FileName = jsonObject1.getString("FileName");
                                    String filepath = jsonObject1.getString("filepath");
                                    String MainCategory = jsonObject1.getString("MainCategory");
                                    String SubCategory = jsonObject1.getString("SubCategory");
                                    product = new CartProduct();
                                    product.setItemID(Integer.parseInt(ItemId));
                                    product.setItemName(ItemDescription);
                                    product.setSaleRate(Description);
                                    try {
                                        product.setQuantity(Integer.parseInt(Qty));
                                    } catch (Exception e) {
                                        product.setQuantity((int) Double.parseDouble(Qty));

                                    }
                                    //  product.setItemImage(ItemDescription);
                                    //   product.setQuantity(Integer.parseInt(Qty));
                                    product.setHSNCode(FileName);
                                    product.setItemBarcode(filepath);
                                    product.setGroupID(MainCategory);
                                    product.setItemSKU(SubCategory);
                                    pastorderlist.add(product);


                                    totalqty = totalqty + pastorderlist.get(i).getQuantity();


                                }
                                pastadapter = new PastOrderItemAdapter(getApplicationContext(), pastorderlist);
                                pastcontainer.setAdapter(pastadapter);
                                txtpendingqtytotl.setText(String.valueOf(totalqty));


                            } else {

                                FancyToast.makeText(PastOrderItem.this, "No Data Found, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mProgressBar.dismiss();


                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressBar.dismiss();
                        FancyToast.makeText(PastOrderItem.this, "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


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
                prefs = new Preferencehelper(PastOrderItem.this);
                Map<String, String> params = new HashMap<String, String>();

                try {


                    String param = getString(R.string.ORDER_ITEM_DETAIL) + "&CID=" + prefs.getPrefsContactId() + "&BranchId=" + branchid + "&Fdate=" + datselecttxt.getText().toString() + "&Tdate=" + datetwoselecttxt.getText().toString();
                    Log.d("Beforeencrptionpastodr", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, PastOrderItem.this);
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

        Volley.newRequestQueue(this).add(postRequest);


    }
}
