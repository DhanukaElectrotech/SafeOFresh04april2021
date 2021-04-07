package com.dhanuka.morningparcel.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;/*
import com.dhanuka.mmthinkbiz.R;
import com.dhanuka.mmthinkbiz.activities.BaseActivity;
import com.dhanuka.mmthinkbiz.adapter.ComplaintReportAdapter;
import com.dhanuka.mmthinkbiz.model.DbComplaintReportModel;
import com.dhanuka.mmthinkbiz.utils.NetworkUtil;
import com.dhanuka.mmthinkbiz.utils.log;*/
import com.dhanuka.morningparcel.Helper.DbComplaintReportModel;
import com.dhanuka.morningparcel.Helper.NetworkUtil;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.MainActivity;
import com.dhanuka.morningparcel.R;
import com.dhanuka.morningparcel.adapter.ComplaintReportAdapter;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.utils.log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class complaintreport extends MainActivity implements View.OnClickListener {
    private TextView toolbarTitle, tvToday;
    private ImageView ivTodayTringle;
    private TextView tvTommorow;
    private ImageView ivTomorrowTringle;
    private TextView tvOpen;
    private ImageView ivOpenTringle;
    private TextView tvClosed;
    private ImageView ivClosedTringle;
    private SwipeRefreshLayout swipeContainer;
    private RecyclerView recyclerviewNewsFragment;
    private LinearLayout emptyView;
    boolean isToday = true, isTomorrow = false, isOpen = true, isClosed = false;
    String type = "1";
    public static Preferencehelper prefs;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //   final View view = getLayoutInflater().inflate(R.layout.activity_cheque_received_report);


        setContentView(R.layout.activity_cheque_received_report);
        overridePendingTransition(0, 0);
        ButterKnife.bind(this);

        prefs = new Preferencehelper(complaintreport.this);

        findViews();
        Bundle b = getIntent().getExtras();

        if (b != null) {
            type = b.getString("type");
            if (type.equalsIgnoreCase("1")) // 1 = Panic , 2 = No Show , 3 Complaint , 4 = Diesel Request
            {
               // toolbarTitle.setText("Panic Button Pressed Report");
            } else if (type.equalsIgnoreCase("2")) // 1 = Panic , 2 = No Show , 3 Complaint , 4 = Diesel Request
            {
               // toolbarTitle.setText("No Show Report");
            } else if (type.equalsIgnoreCase("3")) // 1 = Panic , 2 = No Show , 3 Complaint , 4 = Diesel Request
            {
              //  toolbarTitle.setText("Complaint Received Report");
            }
        }

        final LinearLayoutManager layoutManager = new LinearLayoutManager(complaintreport.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerviewNewsFragment.setLayoutManager(layoutManager);
        recyclerviewNewsFragment.setHasFixedSize(true);

        if (NetworkUtil.isConnectedToNetwork(complaintreport.this)) {
            requestForData(type);

        } else {
            Crouton.showText(complaintreport.this, "Please Connect To Internet", Style.ALERT);

        }
        tvTommorow.setOnClickListener(this);
        tvToday.setOnClickListener(this);
        tvOpen.setOnClickListener(this);
        tvClosed.setOnClickListener(this);
    }

    private void findViews() {
        toolbarTitle = (TextView) findViewById(R.id.txt_action);
        tvToday = (TextView) findViewById(R.id.tv_today);
        ivTodayTringle = (ImageView) findViewById(R.id.iv_today_tringle);
        tvTommorow = (TextView) findViewById(R.id.tv_tommorow);
        ivTomorrowTringle = (ImageView) findViewById(R.id.iv_tomorrow_tringle);
        tvOpen = (TextView) findViewById(R.id.tv_open);
        ivOpenTringle = (ImageView) findViewById(R.id.iv_open_tringle);
        tvClosed = (TextView) findViewById(R.id.tv_closed);
        ivClosedTringle = (ImageView) findViewById(R.id.iv_closed_tringle);
        // swipeContainer = (SwipeRefreshLayout)findViewById( R.id.swipeContainer );
        recyclerviewNewsFragment = (RecyclerView) findViewById(R.id.recyclerview_news_fragment);
        emptyView = (LinearLayout) findViewById(R.id.empty_view);
    }

    Map<String, String> maps;

    private void requestForData(final String type) {
        final ProgressDialog prgDialog = new ProgressDialog(complaintreport.this);
        prgDialog.setMessage("Please Wait.....");
        prgDialog.setCancelable(false);
        prgDialog.show();
        String dt, enddt = "";
        String apinm = "";
        if (type.equalsIgnoreCase("1") || type.equalsIgnoreCase("2") || type.equalsIgnoreCase("3")) // 1 = Panic , 2 = No Show , 3 Complaint , 4 = Diesel Request
        {
            SimpleDateFormat mFormatter = new SimpleDateFormat("MM/dd/yyyy");
            dt = mFormatter.format(new Date());
            enddt = dt;
            SimpleDateFormat mFormatter1 = new SimpleDateFormat("MM/01/yyyy");
            dt = mFormatter1.format(new Date());
            apinm = getString(R.string.URL_BASE_URL);
        } else // for management
        {
            SimpleDateFormat mFormatter = new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat mFormatter1 = new SimpleDateFormat("MM/01/yyyy");
            enddt = mFormatter.format(new Date());
            dt = mFormatter1.format(new Date());
            apinm = getString(R.string.URL_BASE_URL);
        }

        maps = new HashMap<>();


        try {
            String param = getString(R.string.URL_GET_COMPLAINT_REPORT) + "&contactid=" + prefs.getPrefsContactId() + "&dt=" + dt
                    + "&type=" + type + "&enddt=" + enddt;

            Log.d("Beforeencrption", param);
            JKHelper jkHelper = new JKHelper();
            String finalparam = jkHelper.Encryptapi(param, complaintreport.this);
            maps.put("val", finalparam);
            Log.d("afterencrption", finalparam);

        } catch (Exception e) {
            e.printStackTrace();

        }
        StringRequest postRequest = new StringRequest(Request.Method.POST, apinm,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        JKHelper jkHelper = new JKHelper();
                        String responses = jkHelper.Decryptapi(response, complaintreport.this);
                        prgDialog.dismiss();
                        if (responses.length() > 0) {
                            log.e("res" + responses);
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(responses);
                            int success = jsonObject.getInt("success");
                            log.e("success" + success);
                            if (success == 1) {
                                showRecycleView();
                                JSONArray jsonArray = jsonObject.getJSONArray("returnds");
                                ArrayList<DbComplaintReportModel> DBArrayList = new ArrayList<DbComplaintReportModel>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject loopObjects = jsonArray.getJSONObject(i);
                                    String mComplaintID = loopObjects.getString("ComplaintID");
                                    String mFullname = loopObjects.getString("Fullname");
                                    String mVehiclenumber = loopObjects.getString("Vehiclenumber");
                                    String mDrivername = loopObjects.getString("Drivername");
                                    String mDATE = loopObjects.getString("DATE");
                                    String mCreatedDate = loopObjects.getString("CreatedDate");
                                    String mCreatedBy = loopObjects.getString("CreatedBy");
                                    String mComment = loopObjects.getString("Comment");
                                    String mActionTaken = loopObjects.getString("ActionTaken");
                                    String mTYPE = loopObjects.getString("TYPE");
                                    String mAddress = loopObjects.getString("Address");
                                    String mLat = loopObjects.getString("Lat");
                                    String mLong = loopObjects.getString("Long");
                                    String mEmpPhoneNo = loopObjects.getString("EmpPhoneNo");
                                    String mVendorName = loopObjects.getString("VendorName");
                                    String mDriverNo = loopObjects.getString("DriverNo");
                                    String mDept = loopObjects.getString("Dept");
                                    String mReason = loopObjects.getString("Reason");


                                    DbComplaintReportModel model = new DbComplaintReportModel();
                                    model.setmComplaintID(mComplaintID);
                                    model.setmFullname(mFullname);
                                    model.setmVehiclenumber(mVehiclenumber);
                                    model.setmDrivername(mDrivername);
                                    model.setmDATE(mDATE);
                                    model.setmCreatedDate(mCreatedDate);
                                    model.setmCreatedBy(mCreatedBy);
                                    model.setmComment(mComment);
                                    model.setmActionTaken(mActionTaken);
                                    model.setmTYPE(mTYPE);
                                    model.setmAddress(mAddress);
                                    model.setmLat(mLat);
                                    model.setmLong(mLong);
                                    model.setmEmpPhoneNo(mEmpPhoneNo);
                                    model.setmVendorName(mVendorName);
                                    model.setmDriverNo(mDriverNo);
                                    model.setmDept(mDept);
                                    model.setmReason(mReason);
                                    DBArrayList.add(model);
                                }

                                ComplaintReportAdapter adapter = new ComplaintReportAdapter(DBArrayList);
                                recyclerviewNewsFragment.setAdapter(adapter);

                            } else if (success == 0) {

                                Message.message(complaintreport.this, "No Data Exist");
                                showEmptyView();
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
                        prgDialog.dismiss();

                        showEmptyView();
                        Message.message(complaintreport.this, "Failed to Upload Status");
                        //   Log.e("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params = maps;
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);
    }

    public void showEmptyView() {
        recyclerviewNewsFragment.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
    }

    public void showRecycleView() {
        recyclerviewNewsFragment.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        if (view == tvOpen) {
            tvOpen.setBackgroundColor(getResources().getColor(R.color.work_order_managmenht_tv_light_cayn));
            tvClosed.setBackgroundColor(getResources().getColor(R.color.work_order_managmenht_tv_dark_cayn));
            ivOpenTringle.setVisibility(View.VISIBLE);
            ivClosedTringle.setVisibility(View.INVISIBLE);
            recyclerviewNewsFragment.setVisibility(View.VISIBLE);
            isOpen = true;
            isClosed = false;
            if (isToday) {
                requestForData("1");
                log.e("tvopen istoday");
            } else if (isTomorrow) {
                requestForData("3");
                log.e("tvopen istomorrow");
            }
        } else if (view == tvClosed) {
            tvOpen.setBackgroundColor(getResources().getColor(R.color.work_order_managmenht_tv_dark_cayn));
            tvClosed.setBackgroundColor(getResources().getColor(R.color.work_order_managmenht_tv_light_cayn));
            ivOpenTringle.setVisibility(View.INVISIBLE);
            ivClosedTringle.setVisibility(View.VISIBLE);
            recyclerviewNewsFragment.setVisibility(View.INVISIBLE);
            isOpen = false;
            isClosed = true;
            if (isToday) {
                requestForData("2");
                log.e("tvclose istoday");
            } else if (isTomorrow) {
                requestForData("4");
                log.e("tvclose istommorow");
            }
        } else if (view == tvToday) {
            tvToday.setBackgroundColor(getResources().getColor(R.color.work_order_managmenht_tv_light_blue));
            tvTommorow.setBackgroundColor(getResources().getColor(R.color.work_order_managmenht_tv_dark_blue));
            ivTodayTringle.setVisibility(View.VISIBLE);
            ivTomorrowTringle.setVisibility(View.INVISIBLE);
            isToday = true;
            isTomorrow = false;
            if (isOpen) {
                requestForData("1");
                log.e("tvToday isopen");
            } else if (isClosed) {
                requestForData("2");
                log.e("tvToday isClose");
            }
        } else if (view == tvTommorow) {
            tvToday.setBackgroundColor(getResources().getColor(R.color.work_order_managmenht_tv_dark_blue));
            tvTommorow.setBackgroundColor(getResources().getColor(R.color.work_order_managmenht_tv_light_blue));
            ivTodayTringle.setVisibility(View.INVISIBLE);
            ivTomorrowTringle.setVisibility(View.VISIBLE);
            isToday = false;
            isTomorrow = true;
            if (isOpen) {
                requestForData("3");
                log.e("tvTommorow isOpen");
            } else if (isClosed) {
                requestForData("4");
                log.e("tvTomoorow isClose");
            }
        }
    }
}

