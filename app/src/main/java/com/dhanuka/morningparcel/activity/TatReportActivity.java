package com.dhanuka.morningparcel.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.R;
import com.dhanuka.morningparcel.adapter.TatAdapter;
import com.dhanuka.morningparcel.beans.FancyToast;
import com.dhanuka.morningparcel.beans.TatReportBean;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TatReportActivity extends AppCompatActivity {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.paycontainer)
    RecyclerView paycontainer;
    @BindView(R.id.txtWalletAmt)
    TextView txtWalletAmt;
    @BindView(R.id.btnfab)
    FloatingActionButton btnfab;
    @Nullable
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @Nullable
    @BindView(R.id.linearContinue)
    LinearLayout linearContinue;
    private int mYear, mMonth, mDay;
    String date_of_installation;
    TatAdapter tatadapter;
    TatReportBean tathelper;
    Dialog Datedialog;
    RelativeLayout date_dialog;

    FrameLayout time1, time2;
    ArrayList<TatReportBean.tatinnerbean> tatlist = new ArrayList<>();

    Button btnClear, btnSubmit, FilterDate;
    MaterialEditText DatePickeredit, DatePickereditTwo, starttime, endtime;

    String mTodayDate = "";
    String mStartDate = "";

    String isCustomer = "0";
    AlertDialog.Builder builder;
    ImageView backbtnicon;
    Preferencehelper prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tat_report);

        ButterKnife.bind(this);
        backbtnicon = findViewById(R.id.backbtnicon);
        paycontainer.setHasFixedSize(true);
        paycontainer.setLayoutManager(new LinearLayoutManager(getApplicationContext().getApplicationContext(), RecyclerView.VERTICAL, false));


        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        prefs = new Preferencehelper(getApplicationContext());

        if (isCustomer.equalsIgnoreCase("0")) {
            isCustomer = prefs.getPrefsContactId();
        }
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat df1 = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        mTodayDate = df.format(c.getTime()) + "";
        // formattedDate have current date/time

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(mTodayDate + " 00:00"));
        calendar.add(Calendar.DAY_OF_YEAR, -10);
        Date newDate = calendar.getTime();
        mStartDate = df.format(calendar.getTime());
        mStartDate = mStartDate + "";


        loadHistory();
        backbtnicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();


            }
        });
        btnfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] arr = mStartDate.split(" ");
                String[] arr1 = mTodayDate.split(" ");
                starttime.setText("23:59");
                endtime.setText("00:00");
                DatePickeredit.setText(arr[0]);
                DatePickereditTwo.setText(arr1[0]);
                Datedialog.show();

            }
        });
      /*  for (int i = 0; i < 10; i++) {
            tathelper = new tathelper();
            tathelper.setPaymentamount(getApplicationContext().getString(R.string.strpayfrag));
            tathelper.setPaystatus("Recharge of Vodafone Mobile No 8376071201");
            tathelper.setPaydate("17 Aug 2018");
            tatlist.add(tathelper);
        }*/
        tatlist = new ArrayList<>();
        tatadapter = new TatAdapter(tatlist, getApplicationContext(), "3");
        paycontainer.setAdapter(tatadapter);


        Datedialog = new Dialog(TatReportActivity.this);
        builder = new AlertDialog.Builder(getApplicationContext());
        Datedialog.setContentView(R.layout.date_dialog);

        FilterDate = Datedialog.findViewById(R.id.btnFilter);
        time1 = Datedialog.findViewById(R.id.time1);
        time2 = Datedialog.findViewById(R.id.time2);
        btnClear = Datedialog.findViewById(R.id.btnClear);
        time1.setVisibility(View.GONE);
        time2.setVisibility(View.GONE);
        starttime = Datedialog.findViewById(R.id.et_starttime);
        endtime = Datedialog.findViewById(R.id.et_endtime);
        date_dialog = Datedialog.findViewById(R.id.date_dialog);
        DatePickeredit = Datedialog.findViewById(R.id.et_sdate_month);
        DatePickereditTwo = Datedialog.findViewById(R.id.et_current_date);

        starttime.setText("00:00");
        endtime.setText("23:59");
        DatePickeredit.setText(mStartDate);
        DatePickereditTwo.setText(mTodayDate);
        starttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //   showHourPicker(1);

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
                // showHourPicker(2);

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
        FilterDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DatePickeredit.getText().toString().isEmpty()) {
                    FancyToast.makeText(getApplicationContext(), "Select start time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

                } else if (DatePickereditTwo.getText().toString().isEmpty()) {
                    FancyToast.makeText(getApplicationContext(), "Select end time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

                } else {

                    mStartDate = DatePickeredit.getText().toString();
                    mTodayDate = DatePickereditTwo.getText().toString();

                    loadHistory();
                    Datedialog.dismiss();
                }
            }
        });

    }


    public void loadHistory() {
        swipeRefresh.setRefreshing(true);
        tathelper = new TatReportBean();

      /*  final ProgressDialog mProgressBar = new ProgressDialog(getApplicationContext());
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();
*/

        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        swipeRefresh.setRefreshing(false);
                        try {
                            try {
                                tatlist = new ArrayList<>();
                                Log.d("responsiveorderhistory", response);
                                JKHelper jkHelper = new JKHelper();
                                String responses = jkHelper.Decryptapi(response, getApplicationContext());
                                Log.d("responsive", responses);
                                tathelper = new Gson().fromJson(responses, TatReportBean.class);
                                tatlist = tathelper.getBranchdatalist();
                                if (tatlist != null && tatlist.size() > 0) {
                                    tatadapter = new TatAdapter(tatlist, getApplicationContext(), "3");
                                    paycontainer.setAdapter(tatadapter);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            //   FancyToast.makeText(getApplicationContext(), "No Data Found, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


                        } catch (Exception e) {
                            e.printStackTrace();
                            txtWalletAmt.setText("Available Balance    ₹00.00");
                            //  txtWalletAmt.setText("CurrentBalance");
                        }
                        //   mProgressBar.dismiss();


                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        swipeRefresh.setRefreshing(false);
                        if (tatlist.size() > 0) {
                            linearContinue.setVisibility(View.GONE);
                        } else {
                            linearContinue.setVisibility(View.VISIBLE);
                        } //  FancyToast.makeText(getApplicationContext(), "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Preferencehelper prefs;
                prefs = new Preferencehelper(getApplicationContext());

                Map<String, String> params1 = new HashMap<String, String>();
                //   params1.put("contactid", prefs.getPrefsContactId());
                try {
                    // contactid=67322&=08-01-2020&=08-19-2020

                    String param = getString(R.string.GET_TAT_REPORT) + "&contactid=" + prefs.getPrefsContactId() + "&fdate=" +  mTodayDate.replace("/", "-") + "&tdate=" + mTodayDate.replace("/", "-") + "&type=100"+"&OrderNo=";
                    Log.d("Beforeencrptionhistory", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, getApplicationContext());
                    params1.put("val", finalparam);
                    Log.d("afterencrptionhistory", finalparam);
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

        Volley.newRequestQueue(getApplicationContext()).add(postRequest);


    }

    public void panelcleandatepicker(int type) {
        // Get Current Date

        final Calendar c = Calendar.getInstance();

        c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getApplicationContext(),
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
                        date_of_installation = (monthOfYears) + "-" + dayOfMonths + "-" + years;

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
}

