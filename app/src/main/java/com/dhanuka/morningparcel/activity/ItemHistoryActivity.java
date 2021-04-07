package com.dhanuka.morningparcel.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.adapter.ItemHistoryAdapter;
import com.dhanuka.morningparcel.beans.Dynamicbranchbean;
import com.dhanuka.morningparcel.beans.FancyToast;
import com.dhanuka.morningparcel.beans.ItemHistoryBean;
import com.dhanuka.morningparcel.beans.ItemHistoryBean1;
import com.dhanuka.morningparcel.utils.EqualSpacingItemDecoration;
import com.dhanuka.morningparcel.utils.JKHelper;

public class ItemHistoryActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    @Nullable
    @BindView(R.id.spinner_branch)
    Spinner spinner_branch;
    HashMap<String, String> branchhash = new HashMap<>();
    ArrayList<Dynamicbranchbean.beanchinnerbean> mListReportbranch = new ArrayList<>();
    SharedPreferences.Editor mEditor;
    @Nullable
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @Nullable
    @BindView(R.id.fabhelp)
    FloatingActionButton fabhelp;

    @Nullable
    @BindView(R.id.fabsort)
    FloatingActionButton fabsort;

    @Nullable
    @BindView(R.id.etSearch)
    EditText etSearch;
    @Nullable
    @BindView(R.id.lvProducts)
    RecyclerView lvProducts;
    @Nullable
    @BindView(R.id.llDropDown)
    LinearLayout llDropDown;

    @BindView(R.id.llradiogroup)
    LinearLayout llradiogroup;
    String mTodayDate = "";
    String mStartDate = "";
    String itemId;
    List<ItemHistoryBean> mListItems = new ArrayList<>();
    MaterialEditText DatePickeredit, DatePickereditTwo, starttime, endtime;
    Button btnClear, FilterDate, filtersort;
    RelativeLayout date_dialog;
    private int mYear, mMonth, mDay;

    Dialog Datedialog, Sortdialog;
    AlertDialog.Builder builder;
    Map<String, String> myMap = new HashMap<>();
    String image = "";
    String branchname = "";
    String branchid = "";
    int mIntentType = 0;
    ArrayList<String> branchlist = new ArrayList<>();
    RadioGroup rsortgroup;
    RadioButton radioButton;
    ItemHistoryBean itembean;

    ArrayList<ItemHistoryBean> listsale = new ArrayList<>();
    ArrayList<ItemHistoryBean> listpurchase = new ArrayList<>();
    ItemHistoryBean1 itehistrybean1;

    String mtype;
    String type = "1";

    public void fabsortclk(View v) {
        Sortdialog = new Dialog(ItemHistoryActivity.this);
        Sortdialog.setContentView(R.layout.sort_itemdialog);
        builder = new AlertDialog.Builder(ItemHistoryActivity.this);

        Sortdialog.show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_history);
        ButterKnife.bind(this);


        rsortgroup = findViewById(R.id.rsortgroup);

        try {
            mtype=getIntent().getStringExtra("mtype");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }



        rsortgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                radioButton = findViewById(checkedId);


                if (radioButton.getText().toString().equalsIgnoreCase("Purchase")) {
                    type = "2";
                    loadHistory();
                } else if (radioButton.getText().toString().equalsIgnoreCase("Sale")) {
                    type = "1";
                    loadHistory();
                } else if (radioButton.getText().toString().equalsIgnoreCase("All")) {
                    type = "0";
                    loadHistory();

                }

            }
        });

        if (getIntent().hasExtra("image")) {
            image = getIntent().getStringExtra("image");

        }
        if (getIntent().hasExtra("branchData")) {
            mIntentType = 1;
            SharedPreferences prefsss = getSharedPreferences("MORNING_PARCEL_GROCERY",
                    MODE_PRIVATE);
            branchname = prefsss.getString("branchname", "");
            branchid = prefsss.getString("branchid", "");
            try {
                branchlist = new Gson().fromJson(prefsss.getString("branchlist", ""), new TypeToken<ArrayList<String>>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {

                Type mapType = new TypeToken<Map<String, String>>() {
                }.getType();
                myMap = new Gson().fromJson(prefsss.getString("map", ""), mapType);
                branchlist = new Gson().fromJson(prefsss.getString("branchlist", ""), new TypeToken<ArrayList<String>>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
            getBranch();

//            ArrayAdapter branchadapter = new ArrayAdapter(ItemHistoryActivity.this, android.R.layout.simple_list_item_1, branchlist);
//            spinner_branch.setAdapter(branchadapter);
//
//            for (int a = 0; a < branchlist.size(); a++) {
//                Log.e("DATA", branchlist.get(a) + "  \n" + branchname);
//                if (branchlist.get(a).toString().equalsIgnoreCase(branchname)) {
//                    spinner_branch.setSelection(a);
//                }
//            }

            spinner_branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    branchid = myMap.get(spinner_branch.getSelectedItem().toString());

                    //  getAllProducts();

                    // if (position > 0) {
                    loadHistory();
                    // }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            llDropDown.setVisibility(View.VISIBLE);
            llradiogroup.setVisibility(View.VISIBLE);
            type = "2";
        } else {
            type = "0";
            llDropDown.setVisibility(View.GONE);
            llradiogroup.setVisibility(View.GONE);

        }
        llradiogroup.setVisibility(View.VISIBLE);
        itemId = getIntent().getStringExtra("mData");
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat df1 = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        mTodayDate = df.format(c.getTime()) + " 23:59";
        // formattedDate have current date/time

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(mTodayDate));
        calendar.add(Calendar.DAY_OF_YEAR, -15);
        Date newDate = calendar.getTime();
        mStartDate = df.format(calendar.getTime());
        mStartDate = mStartDate + " 00:00";
        rsortgroup.check(R.id.purchasesort);
        if (mtype.equalsIgnoreCase("1"))
        {
            mStartDate =  df.format(c.getTime()) + " 00:00";
            rsortgroup.check(R.id.salesort);

        }



        lvProducts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        lvProducts.hasFixedSize();
        lvProducts.addItemDecoration(new EqualSpacingItemDecoration(15, LinearLayout.VERTICAL));

        loadHistory();
        swipeRefresh.setOnRefreshListener(this);
        Datedialog = new Dialog(ItemHistoryActivity.this);
        builder = new AlertDialog.Builder(ItemHistoryActivity.this);
        Datedialog.setContentView(R.layout.date_dialog);

        FilterDate = Datedialog.findViewById(R.id.btnFilter);
        btnClear = Datedialog.findViewById(R.id.btnClear);

        starttime = Datedialog.findViewById(R.id.et_starttime);
        endtime = Datedialog.findViewById(R.id.et_endtime);
        date_dialog = Datedialog.findViewById(R.id.date_dialog);
        DatePickeredit = Datedialog.findViewById(R.id.et_sdate_month);
        DatePickereditTwo = Datedialog.findViewById(R.id.et_current_date);
        FilterDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (starttime.getText().toString().isEmpty()) {
                    FancyToast.makeText(ItemHistoryActivity.this, "Select start date.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

                } else if (DatePickeredit.getText().toString().isEmpty()) {
                    FancyToast.makeText(ItemHistoryActivity.this, "Select start time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

                } else if (endtime.getText().toString().isEmpty()) {
                    FancyToast.makeText(ItemHistoryActivity.this, "Select endt date.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

                } else if (DatePickereditTwo.getText().toString().isEmpty()) {
                    FancyToast.makeText(ItemHistoryActivity.this, "Select end time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

                } else {

                    mStartDate = DatePickeredit.getText().toString() + " " + starttime.getText().toString();
                    mTodayDate = DatePickereditTwo.getText().toString() + " " + endtime.getText().toString();
                    // getReports(mStartDate,mTodayDate);
                    loadHistory();
                    //  loadHistory();
                    Datedialog.dismiss();
                }
            }
        });

        starttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showHourPicker(1);

            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Datedialog.dismiss();

            }
        });
        endtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHourPicker(2);

            }
        });
        DatePickeredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                panelcleandatepicker(1);
            }
        });
        DatePickereditTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                panelcleandatepicker(2);
            }
        });


        fabhelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] arr = mStartDate.split(" ");
                String[] arr1 = mTodayDate.split(" ");
                DatePickeredit.setText(arr[0]);
                DatePickereditTwo.setText(arr1[0]);
                starttime.setText("00:00");
                endtime.setText("23:59");
                Datedialog.show();

            }
        });


    }

    public void makeBackClick(View v) {
        onBackPressed();
    }

    public void loadHistory() {
        swipeRefresh.setRefreshing(true);

        final ProgressDialog mProgressBar = new ProgressDialog(ItemHistoryActivity.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();
        //   http://mmthinkbiz.com/mobileservice.aspx?method=Itemorderhistory_Web&strdt=07-01-2020&enddt=08-31-2020&contactid=66373&itemid=16921
        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        swipeRefresh.setRefreshing(false);
                        try {
                            mListItems = new ArrayList<>();

                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, ItemHistoryActivity.this);
                            Log.d("responsiveitemhistory", responses);
                            JSONObject jsonObject = new JSONObject(responses);



                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {


                                mListItems = new Gson().fromJson(jsonObject.getString("returnds"), new TypeToken<List<ItemHistoryBean>>() {
                                }.getType());

                                lvProducts.setAdapter(new ItemHistoryAdapter(ItemHistoryActivity.this, mListItems, image));

                            } else {
                                //FancyToast.makeText(ItemHistoryActivity.this, "No Data Found, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
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
                        swipeRefresh.setRefreshing(false);
                        FancyToast.makeText(ItemHistoryActivity.this, "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Preferencehelper prefs;
                prefs = new Preferencehelper(ItemHistoryActivity.this);

                Map<String, String> params1 = new HashMap<String, String>();
                //   params1.put("contactid", prefs.getPrefsContactId());
                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);
                    //  =07-01-2020&=08-31-2020&=66373&=16921
                    //String param = getString(R.string.GET_ORDER_DETAIL) + "&CID=" + prefs.getPrefsContactId() + "&fdate=" + mStartDate + "&tdate=" + mTodayDate ;
                    String branchid = "";


                    if (mIntentType == 1) {
                        branchid = branchid;

                    } else {
                        branchid = "0";


                    }

                    String param;

                    if (type.equalsIgnoreCase("3")) {
                        param = getString(R.string.GET_ITEM_HISTORY) + "&strdt=" + mStartDate + "&enddt=" + mTodayDate + "&contactid=" + prefs.getPrefsContactId() + "&itemid=" + itemId + "&BranchId=" + branchid;


                    } else {
                        param = getString(R.string.GET_ITEM_HISTORY) + "&strdt=" + mStartDate + "&enddt=" + mTodayDate + "&contactid=" + prefs.getPrefsContactId() + "&itemid=" + itemId + "&BranchId=" + branchid + "&Type=" + type;

                    }

                    Log.d("withoutencrypt",param.toString());
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, ItemHistoryActivity.this);
                    params1.put("val", finalparam);
                    Log.d("params1params1", params1.toString());
                    return params1;


                } catch (Exception e) {
                    e.printStackTrace();
                    return params1;
                }

            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(this).add(postRequest);


    }

    String date_of_installation, hoursstr;

    public void showHourPicker(int type) {
        final Calendar myCalender = Calendar.getInstance();
        int hour = myCalender.get(Calendar.HOUR_OF_DAY);
        int minute = myCalender.get(Calendar.MINUTE);
        TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hoursstr = Integer.toString(hourOfDay);


                String minutestr = Integer.toString(minute);
                if (view.isShown()) {

                    myCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    myCalender.set(Calendar.MINUTE, minute);
                }


                SimpleDateFormat format = new SimpleDateFormat("HH", Locale.US);
                String hour = format.format(new Date());


                Calendar calendar = Calendar.getInstance();
                int hourOfDayy = calendar.get(Calendar.HOUR_OF_DAY);

                if (hourOfDay < 10) {


                    hoursstr = "0" + hourOfDay;

                }
                if (minute < 10) {

                    minutestr = "0" + minutestr;
                }

                if (type == 1) {
                    starttime.setText(hoursstr + ":" + minutestr);
                    starttime.setError(null);

                } else if (type == 2) {
                    endtime.setText(hoursstr + ":" + minutestr);
                    endtime.setError(null);
                }


                //    Toast.makeText(getApplicationContext(),String.valueOf(hourOfDayy)+"more",Toast.LENGTH_LONG).show();

                //   timePicker1.setText(String.valueOf(hourOfDay) + ":" + String.valueOf(minute));
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(ItemHistoryActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();
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

                                ArrayAdapter branchadapter = new ArrayAdapter(ItemHistoryActivity.this, android.R.layout.simple_list_item_1, branchlist);
                                spinner_branch.setAdapter(branchadapter);

                                for (int a = 0; a < branchlist.size(); a++) {
                                    Log.e("DATA", branchlist.get(a) + "  \n" + branchname);
                                    if (branchlist.get(a).toString().equalsIgnoreCase(branchname)) {
                                        spinner_branch.setSelection(a);
                                    }
                                }

                                 branchadapter = new ArrayAdapter(getApplicationContext(), R.layout.simple_list_item_single_choice1, branchlist);
                                branchadapter.setDropDownViewResource(R.layout.simple_list_item_single_choice1);
                             SharedPreferences   prefs2 = getSharedPreferences("MORNING_PARCEL_GROCERY",
                                        MODE_PRIVATE);
                                mEditor = prefs2.edit();
                                mEditor.putString("branchlist", new Gson().toJson(branchlist));
                                mEditor.putString("map", new Gson().toJson(branchhash));
                                mEditor.putString("isIntent", "1");
                                mEditor.commit();


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
        )
        {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                Preferencehelper prefs = new com.dhanuka.morningparcel.Helper.Preferencehelper(getApplicationContext());
                ;
                try {
                    String param = getString(R.string.URL_GET_BRANCH) + "&contactid=" + prefs.getPrefsContactId();
                    Log.d("Beforeencrptionpay", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, ItemHistoryActivity.this);
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

    public void panelcleandatepicker(int type) {
        // Get Current Date

        final Calendar c = Calendar.getInstance();

        c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(ItemHistoryActivity.this,
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
                        date_of_installation = (monthOfYears) + "/" + dayOfMonths + "/" + years;

                        if (type == 1) {
                            DatePickeredit.setText(date_of_installation);

                        } else if (type == 2) {
                            DatePickereditTwo.setText(date_of_installation);
                        }


                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.show();

        //  datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

//       datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    @Override
    public void onRefresh() {
        loadHistory();

    }



}