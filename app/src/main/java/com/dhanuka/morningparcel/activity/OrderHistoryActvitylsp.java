package com.dhanuka.morningparcel.activity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;

import androidx.annotation.Nullable;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import android.support.v4.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.dhanuka.morningparcel.adapter.OrderItemsAdapter;
import com.dhanuka.morningparcel.dialog.SelectShop;
import com.dhanuka.morningparcel.events.Clickevent;
import com.dhanuka.morningparcel.events.OnUpdateOrderListener;
import com.dhanuka.morningparcel.utils.DbImageUpload;
import com.dhanuka.morningparcel.Helper.ImageMasterDAO;
import com.dhanuka.morningparcel.utils.ImageUploadService;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.Helper.DbImageMaster;
import com.dhanuka.morningparcel.Helper.ImageUploadDAO;
import com.dhanuka.morningparcel.Helper.Preferencehelper;

import com.dhanuka.morningparcel.R;

import com.dhanuka.morningparcel.SqlDatabase.QueryExecutor;
import com.dhanuka.morningparcel.beans.FancyToast;
import com.dhanuka.morningparcel.adapter.OrderAdapter;
import com.dhanuka.morningparcel.beans.CartProduct;
import com.dhanuka.morningparcel.beans.OrderBean;
import com.dhanuka.morningparcel.SqlDatabase.DatabaseManager;
import com.dhanuka.morningparcel.customViews.CartCountView;
import com.dhanuka.morningparcel.customViews.StoreView;
import com.dhanuka.morningparcel.events.SearchTextChangedEvent;
import com.dhanuka.morningparcel.utils.PermissionModule;
import com.dhanuka.morningparcel.utils.Utility;
import com.dhanuka.morningparcel.events.OrderClicklistner;
import com.dhanuka.morningparcel.utils.EqualSpacingItemDecoration;
import com.dhanuka.morningparcel.utils.log;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.ImagePickActivity;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
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

import static com.vincent.filepicker.activity.BaseActivity.IS_NEED_FOLDER_LIST;
import static com.vincent.filepicker.activity.ImagePickActivity.IS_NEED_CAMERA;

public class OrderHistoryActvitylsp extends BaseActivity implements TextWatcher, SwipeRefreshLayout.OnRefreshListener, OrderClicklistner, OnUpdateOrderListener, Clickevent {
    @Nullable
    @BindView(R.id.linearContinue)
    LinearLayout linearContinue;
    @Nullable
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    @Nullable
    @BindView(R.id.etSearch)
    EditText etSearch;
    OrderAdapter adapter;
    String path = "";
    @Nullable
    @BindView(R.id.lvProducts)
    RecyclerView lvProducts;
    List<OrderBean> list;
    StoreView storeView;
    private boolean addedToMasterTable = false;

    Context context;
    CartCountView countView;
    String date_of_installation, hoursstr;
    private int mYear, mMonth, mDay;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_history;
    }

    String mTodayDate = "";
    String mStartDate = "";
    com.dhanuka.morningparcel.database.DatabaseManager dbManager;
    FloatingActionButton fabhelp;
    TextView helpline, shopno, teamno;
    String mImagePathDataBase, mImageNameDataBase, mCurrentTimeDataBase;
    int masterDataBaseId;
    String lastRowMaterTable;
    int universalorderid;


    int type_cr = 0;


    String currency = "";
    @Nullable
    @BindView(R.id.txtId1)
    TextView txtId1;

    @Nullable
    @BindView(R.id.txtStatus1)
    TextView txtStatus1;

    @Nullable
    @BindView(R.id.txtTagline1)
    TextView txtTagline1;

    @Nullable
    @BindView(R.id.txtPrice)
    TextView txtPrice;

    @Nullable
    @BindView(R.id.txtQty)
    TextView txtQty;

    @Nullable
    @BindView(R.id.txtDate)
    TextView txtDate;

    @Nullable
    @BindView(R.id.txtName)
    TextView txtName;

    @Nullable
    @BindView(R.id.txtPhone)
    ImageView txtPhone;

    @Nullable
    @BindView(R.id.txtPhone1)
    ImageView txtPhone1;

    @Nullable

    @BindView(R.id.txtName1)
    TextView txtName1;

    @Nullable
    @BindView(R.id.btnCancel)
    Button btncancel;

    @Nullable
    @BindView(R.id.btnReview)
    Button btnReview;

    @Nullable
    @BindView(R.id.retailerLayout)
    LinearLayout retailerLayout;

    @Nullable
    @BindView(R.id.reviewLayout)
    LinearLayout reviewLayout;

    @Nullable
    @BindView(R.id.userLayout)
    LinearLayout userLayout;

    @Nullable
    @BindView(R.id.ratigs)
    RatingBar ratigs;


    @Nullable
    @BindView(R.id.txtAddress)
    TextView txtAddress;

    @Nullable
    @BindView(R.id.txtComment1)
    TextView txtComment1;

    @Nullable
    @BindView(R.id.txtComment)
    TextView txtComment;

    @Nullable
    @BindView(R.id.txtDel)
    TextView txtDel;

    @Nullable
    @BindView(R.id.PaymentMode)
    TextView PaymentMode;

    @Nullable
    @BindView(R.id.txnId)
    TextView txnId;
    OrderItemsAdapter mAdapter;
    @Nullable
    @BindView(R.id.lvProducts1)
    ListView lvProducts1;
    @Nullable
    @BindView(R.id.spinnerStatus)
    Spinner spinnerStatus;
    @Nullable
    @BindView(R.id.btnProceed)
    Button btnProceed;
    @Nullable
    @BindView(R.id.imgPayment)
    ImageView imgPayment;
    OrderBean mOrderBean;
    ArrayList<OrderBean.OrderItemsBean> list1;
    Preferencehelper prefs;
    String status = "";

    Dialog HelpDialog;
    TextView helpline1, shopno1, teamno1, deliveredsort, pendingsort, holdsort, datesort;
    ImageView callteam, callshop, callhelpline;
    SharedPreferences prefsss;
    int selectedPosition = 0;
    @Nullable
    @BindView(R.id.editupload)
    ImageView uploadbill;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_order_historylsp, container_body);
        ButterKnife.bind(this);
        context = OrderHistoryActvitylsp.this;
        fabhelp = findViewById(R.id.btnFilter);
        list = new ArrayList<>();
        storeView = (StoreView) LayoutInflater.from(context).inflate(R.layout.action_shop, null);
        swipeRefresh.setOnRefreshListener(this);


        strOpen = "history";

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat df1 = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        mTodayDate = df.format(c.getTime()) + " 23:59";
        // formattedDate have current date/time

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(mTodayDate));
        calendar.add(Calendar.DAY_OF_YEAR, -2);
        Date newDate = calendar.getTime();
        mStartDate = df.format(calendar.getTime());
        mStartDate = mStartDate + " 00:00";
        loadHistory();


        lvProducts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        lvProducts.hasFixedSize();
        lvProducts.addItemDecoration(new EqualSpacingItemDecoration(15, LinearLayout.VERTICAL));

        dbManager = com.dhanuka.morningparcel.database.DatabaseManager.getInstance(context);
        countView = (CartCountView) LayoutInflater.from(context).inflate(R.layout.action_cart, null);

        SortDialog = new Dialog(OrderHistoryActvitylsp.this);
        builder = new AlertDialog.Builder(OrderHistoryActvitylsp.this, R.style.DialogSlideAnim);
        SortDialog.setContentView(R.layout.sort_popup_dialog);
        vehicle_header_name = SortDialog.findViewById(R.id.closeHeader);
        rg = SortDialog.findViewById(R.id.rg);
        btnSubmit = SortDialog.findViewById(R.id.btnSubmit);
        linearLayout_bgg = SortDialog.findViewById(R.id.sortpopid);
        datesort = SortDialog.findViewById(R.id.daternge);
        SortDialog.setCancelable(true);
        Animation animation;
        animation = AnimationUtils.loadAnimation(OrderHistoryActvitylsp.this,
                R.anim.slide_in_top);
        linearLayout_bgg.setAnimation(animation);

        Window window = SortDialog.getWindow();
        window.setWindowAnimations(R.style.DialogAnimation);


        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);
        SortDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);


        Datedialog = new Dialog(OrderHistoryActvitylsp.this);
        builder = new AlertDialog.Builder(this);
        Datedialog.setContentView(R.layout.date_dialog);

        FilterDate = Datedialog.findViewById(R.id.btnFilter);
        btnClear = Datedialog.findViewById(R.id.btnClear);

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

        //   SortDialog.show();

        datesort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] arr = mStartDate.split(" ");
                String[] arr1 = mTodayDate.split(" ");
                starttime.setText(arr[1]);
                endtime.setText(arr1[1]);
                DatePickeredit.setText(arr[0]);
                DatePickereditTwo.setText(arr1[0]);
                Datedialog.show();

            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rg.getCheckedRadioButtonId() == R.id.rbAll) {
                    mType = "99";
                    loadHistory();
                    SortDialog.dismiss();

                } else if (rg.getCheckedRadioButtonId() == R.id.rbP) {
                    mType = "0";
                    loadHistory();
                    SortDialog.dismiss();

                } else if (rg.getCheckedRadioButtonId() == R.id.rbH) {

                    mType = "2";
                    loadHistory();
                    SortDialog.dismiss();

                } else if (rg.getCheckedRadioButtonId() == R.id.rbC) {
                    mType = "91";
                    loadHistory();
                    SortDialog.dismiss();

                } else if (rg.getCheckedRadioButtonId() == R.id.rbCm) {
                    mType = "10";
                    loadHistory();
                    SortDialog.dismiss();

                }

            }
        });
        FilterDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (starttime.getText().toString().isEmpty()) {
                    FancyToast.makeText(OrderHistoryActvitylsp.this, "Select start date.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

                } else if (DatePickeredit.getText().toString().isEmpty()) {
                    FancyToast.makeText(OrderHistoryActvitylsp.this, "Select start time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

                } else if (endtime.getText().toString().isEmpty()) {
                    FancyToast.makeText(OrderHistoryActvitylsp.this, "Select endt date.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

                } else if (DatePickereditTwo.getText().toString().isEmpty()) {
                    FancyToast.makeText(OrderHistoryActvitylsp.this, "Select end time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

                } else {

                    mStartDate = DatePickeredit.getText().toString() + " " + starttime.getText().toString();
                    mTodayDate = DatePickereditTwo.getText().toString() + " " + endtime.getText().toString();

                    mType = "100";
                    loadHistory();
                    SortDialog.dismiss();
                    Datedialog.dismiss();
                }
            }
        });

        vehicle_header_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SortDialog.dismiss();
            }
        });

        fabhelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SortDialog.show();
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (adapter != null) {
                    filter(s.toString());
                }
            }
        });


        HelpDialog = new Dialog(this);
        HelpDialog.setContentView(R.layout.helplayout);
        helpline1 = HelpDialog.findViewById(R.id.helpline);
        teamno1 = HelpDialog.findViewById(R.id.teamcontact);
        callhelpline = HelpDialog.findViewById(R.id.cllhelpline);
        callshop = HelpDialog.findViewById(R.id.callph);
        callteam = HelpDialog.findViewById(R.id.callteam);
        shopno1 = HelpDialog.findViewById(R.id.shopno);
        //   shopno1.setText(mOrderBean.getSupplierPhone());
        callshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                makeCall(mOrderBean.getSupplierPhone());

            }
        });
        callteam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                makeCall("9910004715");


            }
        });
        callhelpline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall("9910004715");


            }
        });

    }

    private void setCartCount() {
        countView.setCount(dbManager.getTotalQty());
    }

    @Override
    protected void onSideSliderClick() {

    }

    private void filter(String text) {
        ArrayList<OrderBean> filteredList = new ArrayList<>();

        for (OrderBean product : list) {
            if (product.getSupplierName().toLowerCase().contains(text) || product.getOrderID().toLowerCase().contains(text) || product.getBuilding().toLowerCase().contains(text) || product.getSociety().toLowerCase().contains(text) || product.getCustomerName().toLowerCase().contains(text) || product.getCustomerPhone().toLowerCase().contains(text)) {
                filteredList.add(product);
            }
        }

        adapter.filterList(filteredList);
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            txt_set.setText("Safe'O'Fresh");
            backbtn_layout.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setCartCount();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        EventBus.getDefault().post(new SearchTextChangedEvent(editable.toString()));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        MenuItem item = menu.findItem(R.id.cart);
        MenuItem item1 = menu.findItem(R.id.shop);
        item.setActionView(countView);
        item1.setActionView(storeView);
        // if (!prefs.getPrefsUsercategory().equalsIgnoreCase("1057")) {
        item.setVisible(false);
        item1.setVisible(false);
        // }


        return true;
    }

    public void loadHistory() {
        swipeRefresh.setRefreshing(true);

        final ProgressDialog mProgressBar = new ProgressDialog(OrderHistoryActvitylsp.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        swipeRefresh.setRefreshing(false);
                        try {
                            list = new ArrayList<>();
                            Log.d("responsiveorderhistory", response);
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, OrderHistoryActvitylsp.this);
                            Log.d("responsive", responses);
                            JSONObject jsonObject = new JSONObject(responses);
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                list = new Gson().fromJson(jsonObject.getString("returnds"), new TypeToken<List<OrderBean>>() {
                                }.getType());
                                adapter = new OrderAdapter(OrderHistoryActvitylsp.this, list, (OrderClicklistner) OrderHistoryActvitylsp.this, (Clickevent) OrderHistoryActvitylsp.this,1);
                                lvProducts.setAdapter(adapter);
                                if (list.size() > 0) {
                                    linearContinue.setVisibility(View.GONE);

                                    setFragment(list.get(0), 0);

                                } else {
                                    linearContinue.setVisibility(View.VISIBLE);

                                }
                            } else {
                                linearContinue.setVisibility(View.VISIBLE);
                                FancyToast.makeText(OrderHistoryActvitylsp.this, "No Data Found, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
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
                        FancyToast.makeText(OrderHistoryActvitylsp.this, "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Preferencehelper prefs;
                prefs = new Preferencehelper(OrderHistoryActvitylsp.this);

                Map<String, String> params1 = new HashMap<String, String>();
                //   params1.put("contactid", prefs.getPrefsContactId());
                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);

                    String param = getString(R.string.GET_ORDER_DETAIL) + "&CID=" + prefs.getPrefsContactId() + "&fdate=" + mStartDate + "&tdate=" + mTodayDate + "&type=" + mType;
                    Log.d("Beforeencrptionhistory", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, OrderHistoryActvitylsp.this);
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

        Volley.newRequestQueue(this).add(postRequest);


    }

    String mType = "99";
    MaterialEditText DatePickeredit, DatePickereditTwo, starttime, endtime;

    LinearLayout linearLayout_bgg;
    RelativeLayout date_dialog;
    Dialog SortDialog, Datedialog;

    RadioGroup rg;
    ImageView vehicle_header_name;
    Button btnClear, btnSubmit, FilterDate;

    public void openFilterDialog(View v) {

        SortDialog.show();
//        loadHistory();
    }


    public void panelcleandatepicker(int type) {
        // Get Current Date

        final Calendar c = Calendar.getInstance();

        c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(OrderHistoryActvitylsp.this,
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
        TimePickerDialog timePickerDialog = new TimePickerDialog(OrderHistoryActvitylsp.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();
    }


    @Override
    public void onRefresh() {
        loadHistory();

    }


    @Override
    public void onCameraClick(String type, int orderid) {
        universalorderid = orderid;
        Log.e("orderidggg", orderid + "");
        if (type.equalsIgnoreCase("1")) {


            Intent intent1 = new Intent(OrderHistoryActvitylsp.this, ImagePickActivity.class);
            intent1.putExtra(IS_NEED_CAMERA, true);
            intent1.putExtra(Constant.MAX_NUMBER, 1);
            intent1.putExtra(IS_NEED_FOLDER_LIST, false);
            startActivityForResult(intent1, Constant.REQUEST_CODE_TAKE_IMAGE);

//            openCamera();   //  launchCameraIntent();
        } else if (type.equalsIgnoreCase("2")) {
            if (ActivityCompat.checkSelfPermission(OrderHistoryActvitylsp.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(OrderHistoryActvitylsp.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            } else {
             /*   Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 66);
*/
                Intent intent1 = new Intent(OrderHistoryActvitylsp.this, ImagePickActivity.class);
                intent1.putExtra(IS_NEED_CAMERA, true);
                intent1.putExtra(Constant.MAX_NUMBER, 1);
                intent1.putExtra(IS_NEED_FOLDER_LIST, true);
                startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE);

//                launchGalleryIntent();
            }

        }

    }

    @Override
    public void onRepeatOrder(String orderId, OrderBean mBeanOrder) {
        SharedPreferences mData = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);
        if (mBeanOrder.getCompanyID().equalsIgnoreCase(mData.getString("shopId", "1"))) {

            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure you want to repeat this order?");
            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            arg0.dismiss();

                            repeatOrderToCart(mBeanOrder.getmListItems());
                            //    updateOrderStatus(orderId, arg0);
                        }
                    });

            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            alertDialogBuilder.create();
            alertDialogBuilder.show();
        } else {
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Your selected store is different than the ordered store please change the store and try again.");
            alertDialogBuilder.setPositiveButton("Change",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            arg0.dismiss();
                            new SelectShop(OrderHistoryActvitylsp.this, 0, false).show();

                            // Utility.selectShop(OrderHistoryActvitylsp.this, 0,false);
                    /*        repeatOrderToCart(mBeanOrder.getmListItems());
                            updateOrderStatus(orderId, arg0);
               */
                        }
                    });

            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            alertDialogBuilder.create();
            alertDialogBuilder.show();

        }

    }

    @Override
    public void onPopOpen(int mPosition, OrderBean mBeanOrder) {

    }

    @Override
    public void onAssignOrder(int mPosition, OrderBean mBeanOrder) {

    }

    public void repeatOrderToCart(ArrayList<OrderBean.OrderItemsBean> mListItems) {
        for (int a = 0; a < mListItems.size(); a++) {
            if (dbManager.exists(Integer.parseInt(mListItems.get(a).getItemID()), "")) {
                CartProduct cartProduct = dbManager.getProduct(Integer.parseInt(mListItems.get(a).getItemID()), "");

                cartProduct.setQuantity(cartProduct.getQuantity() + 1);

                dbManager.update(cartProduct);
            } else {
                CartProduct cartProduct = new CartProduct();
                cartProduct.setItemID(Integer.parseInt(mListItems.get(a).getItemID()));
                cartProduct.setCompanyid(mListItems.get(a).getCompanyID());
                cartProduct.setItemName(mListItems.get(a).getItemDescription());
                cartProduct.setGroupID("");
                cartProduct.setOpeningStock("");
                cartProduct.setMOQ("");
                cartProduct.setROQ("");
                cartProduct.setPurchaseUOM("");
                cartProduct.setPurchaseUOMId("");
                cartProduct.setSaleUOM("");
                cartProduct.setSaleUOMID("");
                cartProduct.setPurchaseRate("");
                cartProduct.setSaleRate(mListItems.get(a).getRate());
                cartProduct.setItemSKU("");
                cartProduct.setItemBarcode("");
                cartProduct.setStockUOM("");
                cartProduct.setItemImage(mListItems.get(a).getFileName() + "&filePath=" + mListItems.get(a).getFilepath());
                cartProduct.setHSNCode("");
                cartProduct.setQuantity(Integer.parseInt(mListItems.get(a).getRequestedQty()));
                cartProduct.setSubCategory("");
                cartProduct.setMRP("88");


                dbManager.insert(cartProduct);
            }
        }
        setCartCount();
        startActivity(new Intent(OrderHistoryActvitylsp.this, CartActivity.class));
    }

    private String PREFS_FILE_PATH = "capture_file_path";
    private final int REQUEST_CODE = 1010;

    private String filePath;

    public void openCamera() {
        SharedPreferences prefs;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        filePath = getOutputMediaFileUri(); // create a file to save the image
        prefs = PreferenceManager.getDefaultSharedPreferences(OrderHistoryActvitylsp.this);
        prefs.edit().putString(PREFS_FILE_PATH, filePath).commit();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(filePath))); // set the image file name
        log.e("file path in open camera==" + filePath);
        startActivityForResult(intent, REQUEST_CODE);
        log.e("open camera is called");
    }

    public String getOutputMediaFileUri() {
        return getOutputMediaFile().getAbsolutePath();
    }

    private File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Mmthinkbiz");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "Mtb_" + timeStamp + ".jpg");
        return mediaFile;
    }

    File mFile1;

    @Override
    public void senddata(OrderBean orderBean, int pos) {
        setFragment(orderBean, pos);

    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//
//
//            ArrayList<String> mImages = new ArrayList<>();
//            if (requestCode == Constant.REQUEST_CODE_TAKE_IMAGE) {
//                log.e("request code true  ");
//                ArrayList<ImageFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_IMAGE);
//                StringBuilder builder = new StringBuilder();
//                for (ImageFile file : list) {
//                    String path = file.getPath();
//                    mFile1 = new File(path);
//                    builder.append(path + "\n");
//                    mImages.add(path);
//                    log.e("file path in request code true==" + filePath);
//                    //  if (!mBean.getStrrmasterid().isEmpty()) {
//                    new ImageCompressionAsyncTask(true).execute(path);
//                }
//                //   }
//            }
//
//            Bitmap bitmap = null;
//
//            if (requestCode == Constant.REQUEST_CODE_PICK_IMAGE) {
//                log.e("request code true  ");
//                ArrayList<ImageFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_IMAGE);
//                StringBuilder builder = new StringBuilder();
//                for (ImageFile file : list) {
//                    String path = file.getPath();
//                    mFile1 = new File(path);
//                    builder.append(path + "\n");
//                    mImages.add(path);
//                    log.e("file path in request code true==" + filePath);
//                    //  if (!mBean.getStrrmasterid().isEmpty()) {
//                    new ImageCompressionAsyncTask(true).execute(path);
//                }
//                //   }
//            }
//
//            /*  if (requestCode == REQUEST_IMAGE) {
//                if (resultCode == Activity.RESULT_OK) {
//                    Uri uri = data.getParcelableExtra("path");
//                    try {
//                        final String path = uri.getPath();
//                        mImages.add(path);
//                        mAdapter.addItems(mImages);
//                        new ImageCompressionAsyncTask(true).execute(path);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//*/
//        }
//        switch (requestCode) {
//            // Check for the integer request code originally supplied to startResolutionForResult().
//            case REQUEST_CHECK_SETTINGS:
//                switch (resultCode) {
//                    case Activity.RESULT_OK:
//                        Log.i(TAG, "User agreed to make required location settings changes.");
//                        //    prefs.setNearByApiStatus(true);
//                        break;
//                    case Activity.RESULT_CANCELED:
//                        Log.i(TAG, "User chose not to make required location settings changes.");
//                        break;
//                }
//                break;
//        }
//    }

    class ImageCompressionAsyncTask extends AsyncTask<String, Void, String> {
        private boolean fromGallery;

        public ImageCompressionAsyncTask(boolean fromGallery) {
            this.fromGallery = fromGallery;
        }

        @Override
        protected String doInBackground(String... params) {
            String filePath = null;
            try {
                filePath = compressImage(params[0]);
            } catch (Exception e) {
                Log.e("exception", e.getMessage());

            }

            return filePath;
        }

        public String compressImage(String imageUri) {

            String filePath = getRealPathFromURI(imageUri);
            Bitmap scaledBitmap = null;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

            int actualHeight = options.outHeight;
            int actualWidth = options.outWidth;
            float maxHeight = 816.0f;
            float maxWidth = 612.0f;
            float imgRatio = actualWidth / actualHeight;
            float maxRatio = maxWidth / maxHeight;

            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) maxHeight;
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) maxWidth;
                } else {
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;

                }
            }

            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16 * 1024];

            try {
                bmp = BitmapFactory.decodeFile(filePath, options);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();

            }
            try {
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }

            float ratioX = actualWidth / (float) options.outWidth;
            float ratioY = actualHeight / (float) options.outHeight;
            float middleX = actualWidth / 2.0f;
            float middleY = actualHeight / 2.0f;

            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));


            ExifInterface exif;
            try {
                exif = new ExifInterface(filePath);

                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
                Log.d("EXIF", "Exif: " + orientation);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                    Log.d("EXIF", "Exif: " + orientation);
                }
                scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileOutputStream out = null;
            String filename = getFilename(imageUri);
            try {
                out = new FileOutputStream(filename);
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return filename;

        }

        private String getRealPathFromURI(String contentURI) {
            Uri contentUri = Uri.parse(contentURI);
            Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
            if (cursor == null) {
                return contentUri.getPath();
            } else {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                return cursor.getString(idx);
            }
        }

        public String getFilename(String imageUri) {
            File file = getApplicationContext().getExternalFilesDir("MmThinkBiz/Images");

            if (!file.exists()) {
                file.mkdirs();
            }

            String filename = imageUri.substring(imageUri.lastIndexOf("/") + 1);

            log.e("file name in compress image== " + filename);

            String uriSting = (file.getAbsolutePath() + "/" + filename);
            log.e("uri string compress image ==" + uriSting);

            mImagePathDataBase = uriSting;
            mImageNameDataBase = filename;
            mCurrentTimeDataBase = JKHelper.getCurrentDate();

            log.e("mimage path database==" + mImagePathDataBase);
            log.e("image name==" + mImageNameDataBase);
            log.e("current image type==" + mCurrentTimeDataBase);

            return uriSting;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            log.e("post excute  ");
            addToDatabase();
        }

        private void addToDatabase() {

            if (!addedToMasterTable) {
                final ArrayList<DbImageMaster> arrayList = new ArrayList<>();
                DbImageMaster modle = new DbImageMaster();
                modle.setmDate(JKHelper.getCurrentDate());
                modle.setmUploadStatus(0);
                modle.setmDescription("order_master");
                modle.setmImageType("order_master");
                arrayList.add(modle);

                DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                    @Override
                    public void run(SQLiteDatabase database) {

                        ImageMasterDAO dao = new ImageMasterDAO(database, OrderHistoryActvitylsp.this);
                        ArrayList<DbImageMaster> list = arrayList;
                        dao.insert(list);
                        addedToMasterTable = true;
                    }
                });
            }


            DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                @Override
                public void run(SQLiteDatabase database) {
                    ImageMasterDAO dao = new ImageMasterDAO(database, OrderHistoryActvitylsp.this);
                    masterDataBaseId = dao.getlatestinsertedid();
                    lastRowMaterTable = String.valueOf(masterDataBaseId);
                    log.e("string id ============== " + masterDataBaseId);
                }
            });


            final ArrayList<DbImageUpload> arrayListUpload = new ArrayList<>();
            DbImageUpload modle = new DbImageUpload();
            modle.setmDate(mCurrentTimeDataBase);
            modle.setmImageUploadStatus(0);
            modle.setmDescription("order_master");
            modle.setmImageType("order_master");
            modle.setmImageId(masterDataBaseId);
            modle.setmImagePath(mImagePathDataBase);
            modle.setmImageName(mImageNameDataBase);
            arrayListUpload.add(modle);


            DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                @Override
                public void run(SQLiteDatabase database) {
                    ImageUploadDAO dao = new ImageUploadDAO(database, OrderHistoryActvitylsp.this);
                    ArrayList<DbImageUpload> list = arrayListUpload;
                    dao.insert(list);

                    log.e("photo inserted ");
                }
            });
            //    setPhotoCount();


            updateserverphotoid();
            uploadimage();


        }
    }

    private void uploadimage() {
        if (JKHelper.isConnectedToNetwork(OrderHistoryActvitylsp.this) && !JKHelper.isServiceRunning(OrderHistoryActvitylsp.this, ImageUploadService.class)) {
            JKHelper.showLog("Start Service");
            startService(new Intent(OrderHistoryActvitylsp.this, ImageUploadService.class));
        } else {
            stopService(new Intent(OrderHistoryActvitylsp.this, ImageUploadService.class));
            startService(new Intent(OrderHistoryActvitylsp.this, ImageUploadService.class));
        }
    }

    private void updateserverphotoid() {
        if (lastRowMaterTable != null) {
            SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
            ImageUploadDAO pd = new ImageUploadDAO(database, OrderHistoryActvitylsp.this);
            pd.setWorkIdToTable(String.valueOf(universalorderid), lastRowMaterTable);
            ImageMasterDAO pds = new ImageMasterDAO(database, OrderHistoryActvitylsp.this);
            pds.setServerDetails(Integer.parseInt(lastRowMaterTable), Integer.valueOf(universalorderid), 0);
            ImageMasterDAO iDao = new ImageMasterDAO(database, getApplicationContext());
            String serverId = iDao.getServerIdById(1);
            DatabaseManager.getInstance().closeDatabase();
            log.e("in last row master table inserting  ");
        }
    }

    /*   @Override
       protected void onActivityResult(int requestCode, int resultCode, Intent data) {

           if (requestCode == 1) {
               if (resultCode == Activity.RESULT_OK) {
                   //     String result=data.getStringExtra("result");


               }
               if (resultCode == Activity.RESULT_CANCELED) {
                   //Write your code if there's no result
               }
           }


           Bitmap bitmap = null;

           if (requestCode == REQUEST_IMAGE) {
               if (resultCode == Activity.RESULT_OK) {
                   Uri uri = data.getParcelableExtra("path");
                   try {
                       bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                     //  mFile = new File(uri.getPath());
                    String strname=   getFilename(uri.getPath());
                       mFile=new File(strname);
                       new UploadToServer().execute();
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }
           }

       }//onActivityResult
       public String getFilename(String imageUri) {
        *//*   File file = new File(Environment.getExternalStorageDirectory().getPath(), "MmThinkBiz/Images");

        if (!file.exists()) {
            file.mkdirs();
        }
*//*
        String filename = imageUri.substring(imageUri.lastIndexOf("/") + 1);

        log.e("file name in compress image== " + filename);

        String uriSting =  imageUri;
        log.e("uri string compress image ==" + uriSting);

        mImagePathDataBase = uriSting;
        mImageNameDataBase = filename;
        mCurrentTimeDataBase = JKHelper.getCurrentDate();

        log.e("mimage path database==" + mImagePathDataBase);
        log.e("image name==" + mImageNameDataBase);
        log.e("current image type==" + mCurrentTimeDataBase);
      *//*   File file1= new File(Environment.getExternalStorageDirectory() + "MmThinkBiz/Images"+mImageNameDataBase);


        if (!file1.exists()) {
            try {
                file1.createNewFile();
                FileChannel src = new FileInputStream(imageUri).getChannel();
                FileChannel dst = new FileOutputStream(file1).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }*//*
        return uriSting;
    }
    File mFile;

    private void launchCameraIntent() {
        Intent intent = new Intent(OrderHistoryActvitylsp.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
       *//* intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
*//*
        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
      *//*  intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);
*//*
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    //Open gallery To Select Image
    private void launchGalleryIntent() {
        Intent intent = new Intent(OrderHistoryActvitylsp.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

      *//*  // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);*//*
        startActivityForResult(intent, REQUEST_IMAGE);
    }


    String TAG = "SAFEOBUDDYIMAGE";
    public static final int REQUEST_IMAGE = 100;


    ProgressDialog progressDialog1;

    private class UploadToServer extends AsyncTask<Void, Void, String> {
        String userResponse;

        private UploadToServer() {
        }

        protected void onPreExecute() {


           *//* Dialog.setMessage("Processing...");
            Dialog.show();*//*


            //p.AlertDialogLogout();

            progressDialog1 = ProgressDialog.show(OrderHistoryActvitylsp.this, "", "loading...");

        }

        @Override
        protected String doInBackground(Void... params) {
            Preferencehelper prefs;
            prefs = new Preferencehelper(OrderHistoryActvitylsp.this);
            String msg = null;
            *//*if (NetworkMgr.Online(context)) {
     *//*
     *//*urlSuffix = urlSuffix.replace(" ", "%20");
            urlSuffix = urlSuffix.replace("<!", "%20");*//*
            userResponse = NetworkMgr.uploadOrderImage(mFile, universalorderid+"", mImageNameDataBase,mImagePathDataBase);

            *//*Log.d("userRespnose", ApiUrl.RegistrationURL + urlSuffix);*//*

            Log.d("Registration", "Response: " + userResponse);



            *//*} else {
                msg = "check internet";

            }
*//*

            return userResponse;

        }

        protected void onPostExecute(String result) {
            try {
                progressDialog1.dismiss();
                Log.e("ASAS", userResponse);
                JSONObject jsonArray = new JSONObject(userResponse);
//                JSONObject jsonObject = jsonArray.getJSONObject("info");

            } catch (Exception e) {
                e.printStackTrace();

                //      Utility.AlertError(SignUpActivity.this, "SignUp", userResponse.replace("[", "").replace("]", ""), true);
            }
        }
    }

*/
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    String TAG = "SAFEOBUDDYIMAGE";
    public static final int REQUEST_IMAGE = 100;

    private void launchGalleryIntent() {
        Intent intent = new Intent(OrderHistoryActvitylsp.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);


        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    protected void setFragment(OrderBean orderBean, int pos) {

        mOrderBean = orderBean;
        Log.e("mOrderBean", new Gson().toJson(mOrderBean));

        SharedPreferences prefs22 = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);

        if (mOrderBean.getCurrency().equalsIgnoreCase("INR")) {
            currency = getResources().getString(R.string.rupee);
        } else {
            currency = "$";
        }

        prefs = new Preferencehelper(context);
        SharedPreferences prefs1 = context.getSharedPreferences("MORNING_PARCEL_GROCERY",
                context.MODE_PRIVATE);
        prefs.setPrefsCurrency(list.get(pos).getCurrency());


        if (list.get(pos).getCurrency().equalsIgnoreCase("INR")) {
            currency = context.getResources().getString(R.string.rupee);
        } else {
            currency = "$";
        }


        list1 = mOrderBean.getmListItems();
        prefsss = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);
        SharedPreferences.Editor mEditor = prefsss.edit();
        status = mOrderBean.getStatus();
        mAdapter = new OrderItemsAdapter(this, list1, status, (OnUpdateOrderListener) OrderHistoryActvitylsp.this, type_cr,mOrderBean.getDeliveryMode());
        lvProducts1.setAdapter(mAdapter);
        uploadbill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        if (mOrderBean.getComment().isEmpty()) {
            txtComment.setVisibility(View.GONE);
            txtComment1.setVisibility(View.GONE);
        } else {
            txtComment.setVisibility(View.VISIBLE);
            txtComment.setText(mOrderBean.getComment());
            txtComment1.setVisibility(View.VISIBLE);


        }

        if (prefs.getPrefsUsercategory().equalsIgnoreCase("1057")) {
            try {
                if (!mOrderBean.getStatus().equalsIgnoreCase("10")) {
                    userLayout.setVisibility(View.VISIBLE);
                } else {
                    userLayout.setVisibility(View.GONE);

                }
            } catch (Exception e) {
                e.printStackTrace();
                userLayout.setVisibility(View.VISIBLE);
            }
            retailerLayout.setVisibility(View.GONE);
            uploadbill.setVisibility(View.VISIBLE);
        } else {
            uploadbill.setVisibility(View.GONE);
            userLayout.setVisibility(View.GONE);
            retailerLayout.setVisibility(View.VISIBLE);

        }

        if (mOrderBean.getStatus().equalsIgnoreCase("10")) {
            if (prefs.getPrefsUsercategory().equalsIgnoreCase("1057")) {
                if (mOrderBean.getCustomerRating().isEmpty()) {
                    reviewLayout.setVisibility(View.VISIBLE);
                    ratigs.setVisibility(View.GONE);

                } else {
                    ratigs.setEnabled(false);
                    ratigs.setRating(Float.parseFloat(mOrderBean.getCustomerRating()));
                    reviewLayout.setVisibility(View.GONE);
                    ratigs.setVisibility(View.VISIBLE);

                }
            } else {

                if (mOrderBean.getSupplierRating().isEmpty()) {
                    reviewLayout.setVisibility(View.VISIBLE);
                    ratigs.setVisibility(View.GONE);

                } else {
                    ratigs.setEnabled(false);
                    ratigs.setRating(Float.parseFloat(mOrderBean.getSupplierRating()));
                    reviewLayout.setVisibility(View.GONE);
                    ratigs.setVisibility(View.VISIBLE);

                }
            }
        } else {
            ratigs.setVisibility(View.GONE);
            reviewLayout.setVisibility(View.GONE);

        }


        String mdt = mOrderBean.getDevliveryDate();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
            Date date1 = sdf.parse(mOrderBean.getDevliveryDate());
            mdt = sdf1.format(date1);

        } catch (Exception er) {
            er.printStackTrace();
            mdt = mOrderBean.getOrderDate();
        }


        /*       try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
            Date date1 = sdf.parse(mOrderBean.getDevliveryDate());
            mdt = sdf1.format(date1);

        } catch (Exception er) {
            er.printStackTrace();
            mdt = mOrderBean.getDevliveryDate();
        }
*/
        txtDel.setText("Delivery Mode : " + mOrderBean.getDeliveryMode());
        PaymentMode.setText("Payment Mode : " + mOrderBean.getPaymentMode());

        if (mOrderBean.getDeliveryMode().equalsIgnoreCase("ONLINE DELIVERY")) {
            txnId.setText("Transaction ID : " + mOrderBean.getPaymentTxnId());
            txnId.setVisibility(View.VISIBLE);
        } else {
            txnId.setVisibility(View.GONE);

        }
        String dat1 = "";

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM yyyy HH:mm a", Locale.ENGLISH);
            Date date1 = sdf.parse(mOrderBean.getOrderDate());
            dat1 = sdf1.format(date1);

        } catch (Exception er) {
            er.printStackTrace();
            dat1 = mOrderBean.getOrderDate();
        }
        txtId1.setText("#00" + mOrderBean.getOrderID());
        txtName1.setText(mOrderBean.getSupplierName());
        txtName.setText(mOrderBean.getCustomerName());

        // txtAddress.setText("Flat no. - " + mOrderBean.getFlatNo() + ", Building - " + mOrderBean.getBuilding() + ", society - " + mOrderBean.getSociety());
        txtAddress.setText("Address - " + mOrderBean.getFlatNo() /*+ ", Building - " + mOrderBean.getBuilding()*/ + ", society - " + mOrderBean.getSociety());
        txtDate.setText(dat1);
        txtQty.setText(mOrderBean.getRItemCOunt());

        txtPrice.setText(currency + " " + new DecimalFormat("##.##").format(Double.parseDouble(mOrderBean.getRAmount())));
        txtPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   if (prefs.getPrefsUsercategory().equalsIgnoreCase("1057")) {

                makeCall(mOrderBean.getSupplierPhone());
/*
                } else {
                    makeCall(mOrderBean.getCustomerPhone());

                }*/

            }
        });
        txtPhone1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   if (prefs.getPrefsUsercategory().equalsIgnoreCase("1057")) {

                makeCall(mOrderBean.getCustomerPhone());
/*
                } else {
                    makeCall(mOrderBean.getCustomerPhone());

                }*/

            }
        });
        try {
            if (mOrderBean.getStatus().equalsIgnoreCase("0") || mOrderBean.getStatus().equalsIgnoreCase(null)) {
                selectedPosition = 1;
                txtStatus1.setText("PROCESSING");
                txtStatus1.setTextColor(getResources().getColor(R.color.black));
                if (mOrderBean.getDevliveryDate().isEmpty()) {
                    txtTagline1.setText("Your Order will be delivered soon");

                } else {
                    txtTagline1.setText("Delivery Time : " + mdt + " between " + mOrderBean.getDeliveryTime());

                }
                //   txtTagline1.setText("Your Order will be delivered soon");
                btncancel.setVisibility(View.VISIBLE);

            } else if (mOrderBean.getStatus().equalsIgnoreCase("91")) {
                txtStatus1.setText("CANCELLED");
                txtTagline1.setText("Your Order has been Cancelled");
                txtStatus1.setTextColor(getResources().getColor(R.color.red));
                selectedPosition = 0;
                btncancel.setVisibility(View.GONE);
                retailerLayout.setVisibility(View.GONE);

            } else if (mOrderBean.getStatus().equalsIgnoreCase("1")) {
                txtStatus1.setText("ACCEPTED BY STORE");
                selectedPosition = 1;
                txtStatus1.setTextColor(getResources().getColor(R.color.black));
                btncancel.setVisibility(View.GONE);
                if (mOrderBean.getDevliveryDate().isEmpty()) {
                    txtTagline1.setText("Your Order will be delivered soon");

                } else {
                    txtTagline1.setText("Delivery Time : " + mdt + " between " + mOrderBean.getDeliveryTime());

                }
            } else if (mOrderBean.getStatus().equalsIgnoreCase("2")) {
                txtStatus1.setText("ON HOLD");
                selectedPosition = 1;
                txtStatus1.setTextColor(getResources().getColor(R.color.colorAccent));
                btncancel.setVisibility(View.VISIBLE);
                txtTagline1.setText("Your Order is on hold will be delivered soon");
            } else if (mOrderBean.getStatus().equalsIgnoreCase("10")) {
                txtTagline1.setText("Your order has beed delivered ");
                selectedPosition = 2;
                txtStatus1.setText("COMPLETED");
                btncancel.setVisibility(View.GONE);
                txtStatus1.setTextColor(getResources().getColor(R.color.green));
                retailerLayout.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            txtStatus1.setTextColor(getResources().getColor(R.color.black));
            txtStatus1.setText("PROCESSING");
            if (mOrderBean.getDevliveryDate().isEmpty()) {
                txtTagline1.setText("Your Order will be delivered soon");

            } else {
                txtTagline1.setText("Delivery Time : " + mdt + " between " + mOrderBean.getDeliveryTime());

            }
        }
        spinnerStatus.setSelection(selectedPosition);
        if (prefs.getPrefsUsercategory().equalsIgnoreCase("1057")) {
            imgPayment.setVisibility(View.VISIBLE);
        } else {
            imgPayment.setVisibility(View.GONE);
        }


        imgPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.opePaymentInfo(OrderHistoryActvitylsp.this, mOrderBean.getCompanyID());
            }
        });

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mStts = "0";
                String status = spinnerStatus.getSelectedItem().toString();
                if (status.equalsIgnoreCase("PROCESSING")) {
                    txtStatus1.setText("PROCESSING");
                    mStts = "0";
                } else if (status.equalsIgnoreCase("CANCELLED")) {
                    mStts = "91";
                    txtStatus1.setText("CANCELLED");
                } else if (status.equalsIgnoreCase("ACCEPTED")) {
                    mStts = "1";
                    txtStatus1.setText("ACCEPTED BY STORE");
                } else if (status.equalsIgnoreCase("hold")) {
                    mStts = "2";
                    txtStatus1.setText("ON HOLD");
                } else if (status.equalsIgnoreCase("COMPLETED")) {
                    mStts = "10";
                    txtStatus1.setText("COMPLETED");
                }

                updateOrderStatus(mOrderBean.getOrderID(), "1", mStts);
            }
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE) {

                SharedPreferences prefs;
                prefs = PreferenceManager.getDefaultSharedPreferences(OrderHistoryActvitylsp.this);
                final String path = prefs.getString(PREFS_FILE_PATH, filePath);
                Log.d("filep", String.valueOf(filePath));
                new ImageCompressionAsyncTask(true).execute(path);
            }
        }


    }

    public void makeHelp(View v) {
        HelpDialog.show();
    }

    public void cancelOrder(View v) {
        status = "91";
        updateOrderStatus(mOrderBean.getOrderID(), "1", "91");

    }

    public void giveReview(View v) {
        Utility.DialogOrderFeedback(OrderHistoryActvitylsp.this, mOrderBean.getOrderID(), 0);
    }

    public void makeBackClick(View v) {
        onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


    public void updateOrderStatus(String oid, String type, String status) {


        final ProgressDialog mProgressBar = new ProgressDialog(OrderHistoryActvitylsp.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("responsive", response);
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, OrderHistoryActvitylsp.this);
                            JSONObject jsonObject = new JSONObject(responses);
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                //  txtStatus1.setText("CANCELLED");
                                mAdapter.notifyDataSetChanged();
                                mAdapter = new OrderItemsAdapter(OrderHistoryActvitylsp.this, list1, status, (OnUpdateOrderListener) OrderHistoryActvitylsp.this, type_cr,mOrderBean.getDeliveryMode());
                                lvProducts1.setAdapter(mAdapter);

                                userLayout.setVisibility(View.GONE);
                            } else {

                                FancyToast.makeText(OrderHistoryActvitylsp.this, "failed to update the order, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
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
                        FancyToast.makeText(OrderHistoryActvitylsp.this, "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


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
                prefs = new Preferencehelper(OrderHistoryActvitylsp.this);
                Map<String, String> params = new HashMap<String, String>();
                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);

                    String param = getString(R.string.UPDATE_ORDER_STATUS) + "&orderid=" + oid + "&status=" + status + "&type=" + type + "&contactId=" + prefs.getPrefsContactId();
                    Log.d("Beforeencrptionupdate", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, OrderHistoryActvitylsp.this);
                    params.put("val", finalparam);
                    Log.d("afterencrptionupdate", finalparam);
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


    public void makeCall(String str) {
        try {
            if (str != null && !str.isEmpty() && str.length() > 2) {
                try {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + str));

                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    ) {
                        PermissionModule permissionModule = new PermissionModule(getApplicationContext());
                        permissionModule.checkPermissions();

                    } else {
                        startActivity(callIntent);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    FancyToast.makeText(getApplicationContext(), "Phone Number is not available", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                }

            } else {
                FancyToast.makeText(getApplicationContext(), "Phone Number is not available", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
            }
        } catch (Exception e) {
            FancyToast.makeText(getApplicationContext(), "Phone Number is not available", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onCancelItem(OrderBean.OrderItemsBean beanData) {

    }

    @Override
    public void onUpdateOrder(List<OrderBean.OrderItemsBean> orderItem) {
        int qty = 0;
        double price = 0.0;
        for (int a = 0; a < orderItem.size(); a++) {
            qty = qty + Integer.parseInt(orderItem.get(a).getRequestedQty());
            price = price + Double.parseDouble(orderItem.get(a).getOrderRequestedAmount());
        }
        txtQty.setText(qty + "");

        txtPrice.setText(currency + " " + new DecimalFormat("##.##").format(price));

    }

    @Override
    public void onPopupListener(int mPosition, OrderBean.OrderItemsBean mBeanItems) {

    }

    @Override
    public void Cancelclk(boolean cancelclk, OrderBean.OrderItemsBean orderItemsBean, int itemposition) {

    }


    Bitmap bitmap;
    Uri imageuri;

    final int CROP_PIC = 2;
    private Uri picUri;
    public static final int CROP_IMAGE_ACTIVITY_REQUEST_CODE = 203;
    String url, imageFilePath;
    private static final int REQUEST_CAPTURE_IMAGE = 100;


    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }


    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }


    private void addToDatabase() {


        if (!addedToMasterTable) {
            final ArrayList<DbImageMaster> arrayList = new ArrayList<>();

            DbImageMaster modle = new DbImageMaster();
            modle.setmDate(JKHelper.getCurrentDate());
            modle.setmUploadStatus(0);
            modle.setmServerId(mOrderBean.getCustomerID());
            modle.setmDescription("Grocery_Order_MAster");
            modle.setmImageType("Grocery_Order_MAster");
            arrayList.add(modle);

            DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                @Override
                public void run(SQLiteDatabase database) {

                    ImageMasterDAO dao = new ImageMasterDAO(database, OrderHistoryActvitylsp.this);
                    ArrayList<DbImageMaster> list = arrayList;
                    dao.insert(list);
                    addedToMasterTable = true;
                }
            });
        }


        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                ImageMasterDAO dao = new ImageMasterDAO(database, OrderHistoryActvitylsp.this);
                dao.getlatestinsertedid();
                String.valueOf(mOrderBean.getOrderID());
            }
        });


        final ArrayList<DbImageUpload> arrayListUpload = new ArrayList<>();
        DbImageUpload modle = new DbImageUpload();
        modle.setmDate(JKHelper.getCurrentDate());
        modle.setmImageUploadStatus(0);
        modle.setmDescription("Roster_Closed_MAster");
        modle.setmImageType("Roster_Closed_MAster");
        modle.setmServerId(mOrderBean.getOrderID());
           /* modle.setmDescription("Document_Master");
            modle.setmImageType("Document_Master");
        */
        modle.setmImageId(Integer.parseInt(mOrderBean.getOrderID()));
        modle.setmImagePath(path);
        modle.setmImageName(path);
        arrayListUpload.add(modle);


        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                ImageUploadDAO dao = new ImageUploadDAO(database, OrderHistoryActvitylsp.this);
                ArrayList<DbImageUpload> list = arrayListUpload;
                dao.insert(list);

                log.e("photo inserted ");
            }
        });

        //   setPhotoCount();
    }

}

