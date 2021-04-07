package com.dhanuka.morningparcel.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.activity.SetmarginService;
import com.dhanuka.morningparcel.adapter.GroupReportAdapter;
import com.dhanuka.morningparcel.beans.FancyToast;
import com.dhanuka.morningparcel.beans.GroupSaleBean;
import com.dhanuka.morningparcel.events.ActivityCommunicator;
import com.dhanuka.morningparcel.events.GroupClickListener;
import com.dhanuka.morningparcel.utils.JKHelper;

/**
 *
 */
public class GroupSalesFragment extends Fragment implements GroupClickListener, SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView lvProducts;
    private ProgressBar pbLoading;
    private LinearLayout linearContinue;
    List<GroupSaleBean> mListReport = new ArrayList<>();
    Context ctx;

    public static GroupSalesFragment newInstance() {
        Bundle args = new Bundle();
        GroupSalesFragment fragment = new GroupSalesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    ActivityCommunicator activityCommunicator;

    @Override
    public void onGroupClick(String grp) {
        // mCallback.onClickButton(this,grp);
        activityCommunicator.passDataToActivity(grp, mStartDate, mTodayDate);
    }


    FloatingActionButton fabhelp;
    MaterialEditText DatePickeredit, DatePickereditTwo, starttime, endtime;
    SwipeRefreshLayout swipeRefreshLayout;
    Button btnClear, FilterDate;
    RelativeLayout date_dialog;
    private int mYear, mMonth, mDay;

    Dialog Datedialog;
    AlertDialog.Builder builder;

    String mTodayDate = "";
    String mStartDate = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_sales, container, false);
        init(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }


    private void init(View view) {
        activityCommunicator = (ActivityCommunicator) getActivity();
        fabhelp = view.findViewById(R.id.btnFilter);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(this);
        lvProducts = (RecyclerView) view.findViewById(R.id.lvProducts);
        linearContinue = (LinearLayout) view.findViewById(R.id.linearContinue);
        pbLoading = (ProgressBar) view.findViewById(R.id.pbLoading);
        lvProducts.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        ctx = getActivity();
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        SimpleDateFormat df1 = new SimpleDateFormat("MM-dd-yyyy");
        mTodayDate = df1.format(c.getTime());
        // formattedDate have current date/time

        Calendar calendar = Calendar.getInstance();
        //     calendar.setTime(new Date(mTodayDate));
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        Date newDate = calendar.getTime();
        mStartDate = df.format(calendar.getTime());
        mStartDate = df1.format(c.getTime()) + " 00:00";


        getReports(mStartDate, mTodayDate);
        Datedialog = new Dialog(getActivity());
        builder = new AlertDialog.Builder(getActivity());
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
                    FancyToast.makeText(getActivity(), "Select start date.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

                } else if (DatePickeredit.getText().toString().isEmpty()) {
                    FancyToast.makeText(getActivity(), "Select start time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

                } else if (endtime.getText().toString().isEmpty()) {
                    FancyToast.makeText(getActivity(), "Select endt date.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

                } else if (DatePickereditTwo.getText().toString().isEmpty()) {
                    FancyToast.makeText(getActivity(), "Select end time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

                } else {

                    mStartDate = DatePickeredit.getText().toString() + " " + starttime.getText().toString();
                    mTodayDate = DatePickereditTwo.getText().toString() + " " + endtime.getText().toString();
                    getReports(mStartDate, mTodayDate);

                    getActivity().startService(new Intent(getActivity(), SetmarginService.class).putExtra("mstartdate", mStartDate).putExtra("menddate", mTodayDate));


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


    @Override
    public void onPause() {
        super.onPause();
    }

    public void getReports(String strtDate, String endDate) {

        pbLoading.setVisibility(View.VISIBLE);
        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            swipeRefreshLayout.setRefreshing(false);
                            Log.d("response_GroupWkise", response);
                            JKHelper jkHelper = new JKHelper();

                            String responses = jkHelper.Decryptapi(response, getActivity());
                            JSONObject jsonObject = new JSONObject(responses);
                            Log.d("response_GroupWkise", responses);

                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {


                                mListReport = new Gson().fromJson(jsonObject.getString("returnds"), new TypeToken<List<GroupSaleBean>>() {
                                }.getType());
                                setAdapterData(mListReport);
                                pbLoading.setVisibility(View.GONE);
                                linearContinue.setVisibility(View.GONE);
                                lvProducts.setVisibility(View.VISIBLE);


                            } else {
                                swipeRefreshLayout.setRefreshing(false);
                                linearContinue.setVisibility(View.VISIBLE);
                                //    com.dhanuka.morningparcel.beans.FancyToast.makeText(getActivity(), "No Data Found, try after some time.", com.dhanuka.morningparcel.beans.FancyToast.LENGTH_SHORT, com.dhanuka.morningparcel.beans.FancyToast.INFO, false).show();
                            }

                        } catch (Exception e) {
                            swipeRefreshLayout.setRefreshing(false);
                            linearContinue.setVisibility(View.VISIBLE);
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        swipeRefreshLayout.setRefreshing(false);
                        com.dhanuka.morningparcel.beans.FancyToast.makeText(getActivity(), "Something went wrong.", com.dhanuka.morningparcel.beans.FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                        linearContinue.setVisibility(View.VISIBLE);


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
                prefs = new Preferencehelper(getActivity());
                Map<String, String> params = new HashMap<String, String>();

                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);

                    String param = getString(R.string.GROUP_REPORTS) + "&contactid=" + prefs.getPrefsContactId() + "&strdt=" + strtDate + "&enddt=" + endDate;
                    Log.d("Beforeencrptionpastodr", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, getActivity());
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

        Volley.newRequestQueue(getActivity()).add(postRequest);


    }

    private void setAdapterData(List<GroupSaleBean> mListReport) {
        lvProducts.setAdapter(new GroupReportAdapter(getActivity(), mListReport, this));

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
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();
    }

    public void panelcleandatepicker(int type) {
        // Get Current Date

        final Calendar c = Calendar.getInstance();

        c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
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

    /**
     * Called when a swipe gesture triggers a refresh.
     */
    @Override
    public void onRefresh() {


        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        ctx = getActivity();
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        SimpleDateFormat df1 = new SimpleDateFormat("MM-dd-yyyy");
        mTodayDate = df1.format(c.getTime());
        // formattedDate have current date/time

        Calendar calendar = Calendar.getInstance();
        //     calendar.setTime(new Date(mTodayDate));
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        Date newDate = calendar.getTime();
        mStartDate = df.format(calendar.getTime());
        mStartDate = df1.format(c.getTime()) + " 00:00";


        getReports(mStartDate, mTodayDate);
    }


//    public void getReports(String strtDate,String endDate) {
//
//        pbLoading.setVisibility(View.VISIBLE);
//        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
//
//
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            Log.d("responseWEEK", response);
//                            JKHelper jkHelper = new JKHelper();
//                            String responses = jkHelper.Decryptapi(response, getActivity());
//                            JSONObject jsonObject = new JSONObject(responses);
//                            Log.d("responseWEEKNew", responses);
//
//                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
//
//
//                                mListReport = new Gson().fromJson(jsonObject.getString("returnds"), new TypeToken<List<ItemSalesReport>>() {
//                                }.getType());
//                               // lvProducts.setAdapter(new ItemReportAdapter(getActivity(), mListReport));
//                                pbLoading.setVisibility(View.GONE);
//                                linearContinue.setVisibility(View.GONE);
//                                lvProducts.setVisibility(View.VISIBLE);
//
//
//                            } else {
//                                linearContinue.setVisibility(View.VISIBLE);
//                                // com.dhanuka.morningparcel.beans.FancyToast.makeText(getActivity(), "No Data Found, try after some time.", com.dhanuka.morningparcel.beans.FancyToast.LENGTH_SHORT, com.dhanuka.morningparcel.beans.FancyToast.INFO, false).show();
//                            }
//
//                        } catch (Exception e) {
//                            linearContinue.setVisibility(View.VISIBLE);
//                            e.printStackTrace();
//                        }
//
//
//                    }
//                },
//                new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        com.dhanuka.morningparcel.beans.FancyToast.makeText(getActivity(), "Something went wrong.", com.dhanuka.morningparcel.beans.FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
//                        linearContinue.setVisibility(View.VISIBLE);
//
//
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams() {
//                Calendar c = Calendar.getInstance();
//                System.out.println("Current time => " + c.getTime());
//
//                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
//                String mTodayDate = df.format(c.getTime());
//                Preferencehelper prefs;
//                prefs = new Preferencehelper(getActivity());
//                Map<String, String> params = new HashMap<String, String>();
//
//                try {
//                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);
//
//                    String param = getString(R.string.ITEM_REPORTS) + "&contactid=" + prefs.getPrefsContactId()+ "&strdt=" + strtDate+ "&enddt=" + endDate+"&group_id="+groupid;
//                    Log.d("Beforeencrptionpastodr", param);
//                    JKHelper jkHelper = new JKHelper();
//                    String finalparam = jkHelper.Encryptapi(param, getActivity());
//                    params.put("val", finalparam);
//                    Log.d("afterencrptionpastodr", finalparam);
//                    return params;
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return params;
//                }
//
//
//            }
//        };
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(
//                60000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//        Volley.newRequestQueue(getActivity()).add(postRequest);
//
//
//    }


}
