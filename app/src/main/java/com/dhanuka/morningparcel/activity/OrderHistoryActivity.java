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
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.AppController;
import com.dhanuka.morningparcel.InterfacePackage.Addressadd;
import com.dhanuka.morningparcel.InterfacePackage.CartItemsadd;
import com.dhanuka.morningparcel.adapter.DeliveryHelper;
import com.dhanuka.morningparcel.adapter.Delivery_Option_Adapter;
import com.dhanuka.morningparcel.adapter.OrderImagesAdapter;
import com.dhanuka.morningparcel.adapter.Payhelper;
import com.dhanuka.morningparcel.adapter.Payment_Option_Adapter;
import com.dhanuka.morningparcel.beans.DeliveryBoysBean;
import com.dhanuka.morningparcel.customViews.LoginView;
import com.dhanuka.morningparcel.dialog.SelectShop;
import com.dhanuka.morningparcel.events.Clickevent;
import com.dhanuka.morningparcel.events.CreatePdfClick;
import com.dhanuka.morningparcel.events.OnImageDeleteListener;
import com.dhanuka.morningparcel.pdfsupport.EnglishNumberToWords;
import com.dhanuka.morningparcel.pdfsupport.ProductModel;
import com.dhanuka.morningparcel.pdfsupport.StaticValue;
import com.dhanuka.morningparcel.utils.DbImageUpload;
import com.dhanuka.morningparcel.Helper.ImageMasterDAO;
import com.dhanuka.morningparcel.utils.ImageUploadService;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.Helper.DbImageMaster;
import com.dhanuka.morningparcel.Helper.ImageUploadDAO;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.MMthinkBizUtils.DonutProgress;
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
import com.dhanuka.morningparcel.events.OrderClicklistner;
import com.dhanuka.morningparcel.utils.EqualSpacingItemDecoration;
import com.dhanuka.morningparcel.utils.log;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.ImagePickActivity;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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

public class OrderHistoryActivity extends BaseActivity implements OnImageDeleteListener, TextWatcher, SwipeRefreshLayout.OnRefreshListener, OrderClicklistner, Clickevent, CartItemsadd, Addressadd, CreatePdfClick {
    @Nullable
    @BindView(R.id.linearContinue)
    LinearLayout linearContinue;
    @Nullable
    @BindView(R.id.txtRecurring)
    TextView txtRecurring;
    @Nullable
    @BindView(R.id.nodatall)
    LinearLayout nodatall;
    @Nullable
    @BindView(R.id.txtRegular)
    TextView txtRegular;
    @Nullable
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    Preferencehelper prefs;

    @Nullable
    @BindView(R.id.etSearch)
    EditText etSearch;

    OrderAdapter adapter;
    String path = "";
    @Nullable
    @BindView(R.id.lvProducts)
    RecyclerView lvProducts;
    List<OrderBean> list;
    List<OrderBean> list1;
    StoreView storeView;
    LoginView loginview;
    String totalamount, totalorders;
    private boolean addedToMasterTable = false;
    String getorderid, getorderamount;
    Context context;
    CartCountView countView;
    String date_of_installation, hoursstr;
    private int mYear, mMonth, mDay;

    TextView totalamountstr;

    DonutProgress totalordersstr;

    LinearLayout orderdetailsll;
    String getcatcodeid;
    String paytype;
    int mOType = 0;


    String orderId, catcodeid, catcodedesc, deliverycgarge;
    ArrayList<Payhelper> paylist = new ArrayList<Payhelper>();
    ArrayList<DeliveryHelper> deliverylist, alldeliverylist;
    RecyclerView deliverytypemode, paytypemode;
    Button btnsummitmode;
    Dialog PayDialog;


    SharedPreferences mDataPreference;
    SharedPreferences.Editor mDataEditor;

    public void makeButtonsClick(View v) {
        try {
            if (v.getId() == R.id.txtRecurring) {
                txtRegular.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                txtRecurring.setBackgroundColor(getResources().getColor(R.color.Base_color));
                mOType = 1;
                if (list1.size() > 0) {
                    lvProducts.setVisibility(View.VISIBLE);
                    nodatall.setVisibility(View.GONE);
                    mDataEditor.putString("mListData", new Gson().toJson(list1));
                    mDataEditor.commit();

                    adapter = new OrderAdapter(OrderHistoryActivity.this, list1, (OrderClicklistner) OrderHistoryActivity.this, (Clickevent) OrderHistoryActivity.this, 1);
                    lvProducts.setAdapter(adapter);
                } else {
                    nodatall.setVisibility(View.VISIBLE);
                    lvProducts.setVisibility(View.GONE);

                }


            } else if (v.getId() == R.id.txtRegular) {
                mOType = 0;
                txtRecurring.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                txtRegular.setBackgroundColor(getResources().getColor(R.color.Base_color));

                if (list.size() > 0) {
                    nodatall.setVisibility(View.GONE);
                    lvProducts.setVisibility(View.VISIBLE);
                    mDataEditor.putString("mListData", new Gson().toJson(list));
                    mDataEditor.commit();
                    adapter = new OrderAdapter(OrderHistoryActivity.this, list, (OrderClicklistner) OrderHistoryActivity.this, (Clickevent) OrderHistoryActivity.this, 1);
                    lvProducts.setAdapter(adapter);

                } else {
                    nodatall.setVisibility(View.VISIBLE);
                    lvProducts.setVisibility(View.VISIBLE);

                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
    Dialog dialogItemDetail, dialogAssignOrder;
    Button btnSubmitAssign, btnSubmitPic;
    ImageView closeHeader1, closeHeader, imageScan;
    Spinner spBoys;
    RelativeLayout llBarcode;
    EditText edtComment, edtBarcode;
    ImageView pickimagebtn;
    RecyclerView rvImages;
    String sytrContactId = "0", branchid = "-1";
    Intent mIntent;

    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_history, container_body);
        ButterKnife.bind(this);


        context = OrderHistoryActivity.this;
        prefs = new Preferencehelper(OrderHistoryActivity.this);
        sytrContactId = prefs.getPrefsContactId();
        fabhelp = findViewById(R.id.btnFilter);
        list = new ArrayList<>();
        list1 = new ArrayList<>();
        storeView = (StoreView) LayoutInflater.from(context).inflate(R.layout.action_shop, null);

        loginview = (LoginView) LayoutInflater.from(context).inflate(R.layout.action_login, null);

        swipeRefresh.setOnRefreshListener(this);


        totalamountstr = findViewById(R.id.totalamountid);
        totalordersstr = findViewById(R.id.toatalorderid);
        orderdetailsll = findViewById(R.id.orderdetailsll);
        strOpen = "history";
        mIntent = getIntent();
        mDataPreference = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);
        mDataEditor = mDataPreference.edit();
        mDataEditor.putString("mListData", "");
        mDataEditor.commit();
        if (mIntent.hasExtra("mDate")) {
            int mTime = getIntent().getIntExtra("mTime", 1);

            Log.e("Data_from_last_screen", mIntent.getStringExtra("mDate"));
            if (mTime == 2) {
                mTodayDate = mIntent.getStringExtra("mDate") + " 11:59:59";
                mStartDate = mIntent.getStringExtra("mDate") + " 00:00:00";

            } else if (mTime == 3) {
                mTodayDate = mIntent.getStringExtra("mDate") + " 16:59:59";
                mStartDate = mIntent.getStringExtra("mDate") + " 12:59:59";

            } else if (mTime == 4) {
                mTodayDate = mIntent.getStringExtra("mDate") + " 19:59:59";
                mStartDate = mIntent.getStringExtra("mDate") + " 16:59:59";

            } else if (mTime == 5) {
                mTodayDate = mIntent.getStringExtra("mDate") + " 23:59:59";
                mStartDate = mIntent.getStringExtra("mDate") + " 20:59:59";

            } else {
                mTodayDate = mIntent.getStringExtra("mDate") + " 23:59:59";
                mStartDate = mIntent.getStringExtra("mDate") + " 00:00:00";

            }

        } else if (mIntent.hasExtra("isCustomerlist")) {
            Calendar c = Calendar.getInstance();
            System.out.println("Current time => " + c.getTime());

            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat df1 = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            mTodayDate = df.format(c.getTime()) + " 23:59:59";
            // formattedDate have current date/time

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(mTodayDate));
            if (mIntent.hasExtra("isCustomerlist")) {
                sytrContactId = mIntent.getStringExtra("isCustomercid");
                calendar.add(Calendar.DAY_OF_YEAR, -30);
            } else {
                calendar.add(Calendar.DAY_OF_YEAR, -2);
            }
            Date newDate = calendar.getTime();
            mStartDate = df.format(calendar.getTime());
            mStartDate = mStartDate + " 00:00";


        } else if (mIntent.hasExtra("isCustomerbranch")) {

            int mTime1 = Integer.parseInt(getIntent().getStringExtra("mTime1"));
            Log.d("ShowmymTime12", String.valueOf(mTime1));

            if (mTime1 == 2) {
                mTodayDate = mIntent.getStringExtra("mDate1") + " 11:59";
                mStartDate = mIntent.getStringExtra("mDate1") + " 00:00";

            } else if (mTime1 == 3) {
                mTodayDate = mIntent.getStringExtra("mDate1") + " 15:59:59";
                mStartDate = mIntent.getStringExtra("mDate1") + " 12:59:59";

            } else if (mTime1 == 4) {
                mTodayDate = mIntent.getStringExtra("mDate1") + " 19:59:59";
                mStartDate = mIntent.getStringExtra("mDate1") + " 16:59:59";

            } else if (mTime1 == 5) {
                mTodayDate = mIntent.getStringExtra("mDate1") + " 23:59:59";
                mStartDate = mIntent.getStringExtra("mDate1") + " 20:59:59";

            } else {
                mTodayDate = mIntent.getStringExtra("mDate1") + " 23:59:59";
                mStartDate = mIntent.getStringExtra("mDate1") + " 00:00:00";

            }

            if (mIntent.hasExtra("branchid")) {
                branchid = getIntent().getStringExtra("branchid");
            }
            sytrContactId = prefs.getPrefsContactId();


        } else {

            Calendar c = Calendar.getInstance();
            System.out.println("Current time => " + c.getTime());

            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat df1 = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            mTodayDate = df.format(c.getTime()) + " 23:59:59";
            // formattedDate have current date/time

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(mTodayDate));
            if (mIntent.hasExtra("isCustomer")) {
                sytrContactId = mIntent.getStringExtra("isCustomer");
                calendar.add(Calendar.DAY_OF_YEAR, -15);
            } else {
                calendar.add(Calendar.DAY_OF_YEAR, -2);
            }
            Date newDate = calendar.getTime();
            mStartDate = df.format(calendar.getTime());
            mStartDate = mStartDate + " 00:00:00";

        }


        loadHistory();
        getDeliveryBoys();

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        lvProducts.setLayoutManager(linearLayoutManager);
        lvProducts.hasFixedSize();
        lvProducts.addItemDecoration(new EqualSpacingItemDecoration(15, LinearLayout.VERTICAL));

        dbManager = com.dhanuka.morningparcel.database.DatabaseManager.getInstance(context);
        countView = (CartCountView) LayoutInflater.from(context).inflate(R.layout.action_cart, null);

        SortDialog = new Dialog(OrderHistoryActivity.this);
        builder = new AlertDialog.Builder(OrderHistoryActivity.this, R.style.DialogSlideAnim);
        SortDialog.setContentView(R.layout.sort_popup_dialog);
        vehicle_header_name = SortDialog.findViewById(R.id.closeHeader);
        rg = SortDialog.findViewById(R.id.rg);
        rbnew = SortDialog.findViewById(R.id.rbnew);
        btnSubmit = SortDialog.findViewById(R.id.btnSubmit);
        linearLayout_bgg = SortDialog.findViewById(R.id.sortpopid);
        datesort = SortDialog.findViewById(R.id.daternge);
        SortDialog.setCancelable(true);
        Animation animation;
        animation = AnimationUtils.loadAnimation(OrderHistoryActivity.this,
                R.anim.slide_in_top);
        linearLayout_bgg.setAnimation(animation);

        Window window = SortDialog.getWindow();
        window.setWindowAnimations(R.style.DialogAnimation);


        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);
        SortDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);


        Datedialog = new Dialog(OrderHistoryActivity.this);
        builder = new AlertDialog.Builder(this);
        Datedialog.setContentView(R.layout.date_dialog);

        FilterDate = Datedialog.findViewById(R.id.btnFilter);
        btnClear = Datedialog.findViewById(R.id.btnClear);

        starttime = Datedialog.findViewById(R.id.et_starttime);
        endtime = Datedialog.findViewById(R.id.et_endtime);
        date_dialog = Datedialog.findViewById(R.id.date_dialog);
        DatePickeredit = Datedialog.findViewById(R.id.et_sdate_month);
        DatePickereditTwo = Datedialog.findViewById(R.id.et_current_date);

        starttime.setText("00:00:00");
        endtime.setText("23:59:59");
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


                if (rbnew.getCheckedRadioButtonId() == R.id.rball1) {
                    mType = "99";
                    loadHistory();
                    SortDialog.dismiss();

                } else if (rbnew.getCheckedRadioButtonId() == R.id.rbordrrec) {
                    mType = "0";
                    loadHistory();
                    SortDialog.dismiss();

                } else if (rbnew.getCheckedRadioButtonId() == R.id.rbouteliver) {

                    mType = "83";
                    loadHistory();
                    SortDialog.dismiss();

                } else if (rbnew.getCheckedRadioButtonId() == R.id.rbdeliver) {
                    mType = "10";
                    loadHistory();
                    SortDialog.dismiss();

                } else if (rbnew.getCheckedRadioButtonId() == R.id.rbcancel) {
                    mType = "91";
                    loadHistory();
                    SortDialog.dismiss();

                } else if (rbnew.getCheckedRadioButtonId() == R.id.rbaccpt) {
                    mType = "1";
                    loadHistory();
                    SortDialog.dismiss();

                } else if (rbnew.getCheckedRadioButtonId() == R.id.rbpending) {
                    mType = "0";
                    loadHistory();
                    SortDialog.dismiss();

                } else if (rbnew.getCheckedRadioButtonId() == R.id.rbready) {
                    mType = "82";
                    loadHistory();
                    SortDialog.dismiss();

                } else if (rbnew.getCheckedRadioButtonId() == R.id.rbready) {
                    mType = "81";
                    loadHistory();
                    SortDialog.dismiss();

                }

            }
        });
        FilterDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (starttime.getText().toString().isEmpty()) {
                    FancyToast.makeText(OrderHistoryActivity.this, "Select start date.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

                } else if (DatePickeredit.getText().toString().isEmpty()) {
                    FancyToast.makeText(OrderHistoryActivity.this, "Select start time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

                } else if (endtime.getText().toString().isEmpty()) {
                    FancyToast.makeText(OrderHistoryActivity.this, "Select endt date.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

                } else if (DatePickereditTwo.getText().toString().isEmpty()) {
                    FancyToast.makeText(OrderHistoryActivity.this, "Select end time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

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

        dialogAssignOrder = new Dialog(OrderHistoryActivity.this);
        dialogAssignOrder.setContentView(R.layout.dialog_assign_order);

        closeHeader = dialogAssignOrder.findViewById(R.id.closeHeader);
        btnSubmitAssign = dialogAssignOrder.findViewById(R.id.btnSubmit);
        edtComment = dialogAssignOrder.findViewById(R.id.edtComment);
        spBoys = dialogAssignOrder.findViewById(R.id.spBoys);

        dialogItemDetail = new Dialog(OrderHistoryActivity.this);
        dialogItemDetail.setContentView(R.layout.dialog_item_detail);
        btnSubmitAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spBoys.getSelectedItemPosition() == 0) {
                    com.shashank.sony.fancytoastlib.FancyToast.makeText(getApplicationContext(), "Please Select Delivery Boy.", com.shashank.sony.fancytoastlib.FancyToast.LENGTH_SHORT, com.shashank.sony.fancytoastlib.FancyToast.ERROR, false).show();
                } else {
                    assignOrder(mListDeliveryBoys.get(spBoys.getSelectedItemPosition() - 1).getContactID());
                }
            }
        });

        closeHeader1 = dialogItemDetail.findViewById(R.id.closeHeader);
        llBarcode = dialogItemDetail.findViewById(R.id.llBarcode);
        btnSubmitPic = dialogItemDetail.findViewById(R.id.btnSubmit);
        imageScan = dialogItemDetail.findViewById(R.id.imageScan);
        edtBarcode = dialogItemDetail.findViewById(R.id.edtBarcode);
        pickimagebtn = dialogItemDetail.findViewById(R.id.imagepick);
        rvImages = dialogItemDetail.findViewById(R.id.rvImages);
        llBarcode.setVisibility(View.GONE);

        dialogItemDetail.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                edtBarcode.setText("");
                mListImages = new ArrayList<>();
                orderImagesAdapter = new OrderImagesAdapter(OrderHistoryActivity.this, mListImages, OrderHistoryActivity.this);
                rvImages.setAdapter(orderImagesAdapter);

            }
        });

        dialogItemDetail.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                edtBarcode.setText("");
                mListImages = new ArrayList<>();
                orderImagesAdapter = new OrderImagesAdapter(OrderHistoryActivity.this, mListImages, OrderHistoryActivity.this);
                rvImages.setAdapter(orderImagesAdapter);

            }
        });
        pickimagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });
        closeHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAssignOrder.dismiss();
            }
        });
        closeHeader1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogItemDetail.dismiss();
            }
        });

        btnSubmitPic.setText("Submit");
        btnSubmitPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mListImages.size() > 0) {
                    if (mListImages.size() > 0) {
                        for (int a = 0; a < mListImages.size(); a++) {
                            path = mListImages.get(a);
                            new ImageCompressionAsyncTask(true).execute(path);

                        }
                    }
                }
                uploadimage();

                dialogItemDetail.dismiss();
                //  FancyToast.makeText(OrderHistoryActivity.this,"Please capture atleast one image of product",)

            }
        });

     /*   btnSubmitAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogAssignOrder.dismiss();
                //  FancyToast.makeText(OrderHistoryActivity.this,"Please capture atleast one image of product",)

            }
        });*/
        rvImages.setLayoutManager(new LinearLayoutManager(OrderHistoryActivity.this, RecyclerView.HORIZONTAL, false));
        orderImagesAdapter = new OrderImagesAdapter(this, mListImages, this);
        rvImages.setAdapter(orderImagesAdapter);

        PayDialog = new Dialog(context);
        PayDialog.setContentView(R.layout.change_payment_type_dialog);
        deliverytypemode = PayDialog.findViewById(R.id.delivtypemode);
        paytypemode = PayDialog.findViewById(R.id.paytypemode);
        btnsummitmode = PayDialog.findViewById(R.id.btnsummitmode);
        deliverytypemode.setHasFixedSize(true);
        paytypemode.setHasFixedSize(true);
        deliverytypemode.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        paytypemode.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        btnsummitmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                double wallet = Double.parseDouble(prefs.getPREFS_currentbal().trim());
                double orderamount = Double.parseDouble(getorderamount.trim());


                if (wallet < orderamount) {
                    Dialog dialog = new Dialog(OrderHistoryActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.setContentView(R.layout.dialog_wallet_amount);
                    dialog.setCancelable(true);
                    final Button submit = (Button) dialog.findViewById(R.id.btnupdate);
                    final TextView txtWalletAmtShow = (TextView) dialog.findViewById(R.id.txtWalletAmtShow);
                    final TextView tvdesc = (TextView) dialog.findViewById(R.id.tvdesc);
                    tvdesc.setText("You have insufficient amount in your wallet to make an order, Your wallet  amount should have atleast Rs.1000.Please add money to wallet and try again.");
                    txtWalletAmtShow.setText("₹" + prefs.getPREFS_currentbal());
                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            startActivityForResult(new Intent(OrderHistoryActivity.this, WalleTransactionActivity.class), 5566);
                            //startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + getPackageName())));


                        }
                    });

                    // Display the custom alert dialog on interface
                    dialog.show();

                } else {
                    submitchnagepaymode(getorderid);
                }


            }
        });
   /*     orderCompleteDialog();

        strChangeStatus = "10";
       // txtStatus.setText("COMPLETED");
        deliverOrder.show();*/

    }

    private void setCartCount() {
        countView.setCount(dbManager.getTotalQty());
    }

    @Override
    protected void onSideSliderClick() {

    }

    private void filter(String text) {
        ArrayList<OrderBean> filteredList = new ArrayList<>();
        if (mOType == 0) {
            for (OrderBean product : list) {
                if (product.getSupplierName().toLowerCase().contains(text) || product.getOrderID().toLowerCase().contains(text) || product.getBuilding().toLowerCase().contains(text) || product.getSociety().toLowerCase().contains(text) || product.getCustomerName().toLowerCase().contains(text) || product.getCustomerPhone().toLowerCase().contains(text)) {
                    filteredList.add(product);
                }
            }
        } else {
            for (OrderBean product : list1) {
                if (product.getSupplierName().toLowerCase().contains(text) || product.getOrderID().toLowerCase().contains(text) || product.getBuilding().toLowerCase().contains(text) || product.getSociety().toLowerCase().contains(text) || product.getCustomerName().toLowerCase().contains(text) || product.getCustomerPhone().toLowerCase().contains(text)) {
                    filteredList.add(product);
                }
            }
        }
        adapter.filterList(filteredList);
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            txt_set.setText("Safe'O'Fresh");
            backbtn_layout.setVisibility(View.VISIBLE);
            // txt_set.setVisibility(View.VISIBLE);
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
        MenuItem item3 = menu.findItem(R.id.shop);
        item.setActionView(countView);
        item1.setActionView(storeView);

        // if (!prefs.getPrefsUsercategory().equalsIgnoreCase("1057")) {
        item.setVisible(false);
        item1.setVisible(false);
        item3.setVisible(false);
        // }


        return true;
    }

    public void loadHistory() {
        swipeRefresh.setRefreshing(true);

        final ProgressDialog mProgressBar = new ProgressDialog(OrderHistoryActivity.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        swipeRefresh.setRefreshing(false);
                        try {
                            ArrayList<OrderBean> listOrders = new ArrayList<>();
                            list = new ArrayList<>();
                            list1 = new ArrayList<>();
                            Log.d("responsiveorderhistory", response);
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, OrderHistoryActivity.this);
                            Log.d("responsive", responses);
                            JSONObject jsonObject = new JSONObject(responses);
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                nodatall.setVisibility(View.GONE);

                                listOrders = new Gson().fromJson(jsonObject.getString("returnds"), new TypeToken<List<OrderBean>>() {
                                }.getType());
                                setorderamount();
                                if (listOrders.size() > 0) {
                                    linearContinue.setVisibility(View.GONE);
                                    orderdetailsll.setVisibility(View.GONE);

                                } else {
                                    nodatall.setVisibility(View.GONE);

                                    if (prefs.getPrefsUsercategory().equalsIgnoreCase("1058")) {
//                                        orderdetailsll.setVisibility(View.VISIBLE);
                                    } else {
//                                        orderdetailsll.setVisibility(View.GONE);

                                    }

                                }
                                for (int a = 0; a < listOrders.size(); a++) {
                                    if (listOrders.get(a).getOrderType().equalsIgnoreCase("RO")) {
                                        list1.add(listOrders.get(a));
                                    } else {
                                        list.add(listOrders.get(a));

                                    }
                                }
                                if (mOType == 0) {
                                    txtRecurring.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                                    txtRegular.setBackgroundColor(getResources().getColor(R.color.Base_color));
                                    mDataEditor.putString("mListData", new Gson().toJson(list));
                                    mDataEditor.commit();

                                    adapter = new OrderAdapter(OrderHistoryActivity.this, list, (OrderClicklistner) OrderHistoryActivity.this, (Clickevent) OrderHistoryActivity.this, 1);
                                    lvProducts.setAdapter(adapter);
                                } else {
                                    txtRegular.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                                    txtRecurring.setBackgroundColor(getResources().getColor(R.color.Base_color));
                                    mDataEditor.putString("mListData", new Gson().toJson(list1));
                                    mDataEditor.commit();

                                    adapter = new OrderAdapter(OrderHistoryActivity.this, list1, (OrderClicklistner) OrderHistoryActivity.this, (Clickevent) OrderHistoryActivity.this, 1);
                                    lvProducts.setAdapter(adapter);

                                }
                                totalordersstr.setText(String.valueOf(listOrders.size()));


                            } else {

                                orderdetailsll.setVisibility(View.GONE);
                                nodatall.setVisibility(View.VISIBLE);
                                FancyToast.makeText(OrderHistoryActivity.this, "No Data Found, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            mProgressBar.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressBar.dismiss();
                        swipeRefresh.setRefreshing(false);

                        FancyToast.makeText(OrderHistoryActivity.this, "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params1 = new HashMap<String, String>();
                //   params1.put("contactid", prefs.getPrefsContactId());
                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);
//                    if (prefs.getPrefsUsercategory().equalsIgnoreCase("1062")) {
//                        mType = "99";
//                    }

                    String param;
//                    if (mType.equalsIgnoreCase("99"))
//                    {
//                        mType="100";
//                    }
                    if (mIntent.hasExtra("isCustomerbranch")) {
                        param = getString(R.string.GET_ORDER_DETAIL1) + "&CID=" + sytrContactId + "&fdate=" + mStartDate + "&tdate=" + mTodayDate + "&type=" + mType + "&BranchID=" + branchid;

                    } else {
                        param = getString(R.string.GET_ORDER_DETAIL1) + "&CID=" + sytrContactId + "&fdate=" + mStartDate + "&tdate=" + mTodayDate + "&type=" + mType;

                    }


                    Log.d("order_params", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, OrderHistoryActivity.this);
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
    TextView datesort;
    RadioGroup rg, rbnew;
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


        DatePickerDialog datePickerDialog = new DatePickerDialog(OrderHistoryActivity.this,
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
        TimePickerDialog timePickerDialog = new TimePickerDialog(OrderHistoryActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, true);
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


            Intent intent1 = new Intent(OrderHistoryActivity.this, ImagePickActivity.class);
            intent1.putExtra(IS_NEED_CAMERA, true);
            intent1.putExtra(Constant.MAX_NUMBER, 1);
            intent1.putExtra(IS_NEED_FOLDER_LIST, false);
            startActivityForResult(intent1, Constant.REQUEST_CODE_TAKE_IMAGE);

//            openCamera();   //  launchCameraIntent();
        } else if (type.equalsIgnoreCase("2")) {
            if (ActivityCompat.checkSelfPermission(OrderHistoryActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(OrderHistoryActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            } else {
             /*   Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 66);
*/
                Intent intent1 = new Intent(OrderHistoryActivity.this, ImagePickActivity.class);
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

                            // repeatOrderToCart(mBeanOrder.getmListItems());
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
                            new SelectShop(OrderHistoryActivity.this, 0, false).show();

                            // Utility.selectShop(OrderHistoryActivity.this, 0,false);
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

    OrderBean mBeanOrder;

    @Override
    public void onPopOpen(int mPosition, OrderBean mBeanOrder) {
        this.mBeanOrder = mBeanOrder;
        dialogItemDetail.show();

    }

    Boolean isTrue = false;

    @Override
    public void onAssignOrder(int mPosition, OrderBean mBeanOrder) {
        this.mBeanOrder = mBeanOrder;
        if (mListDeliveryBoys.size() > 0) {

            dialogAssignOrder.show();
        } else {
            isTrue = true;

            getDeliveryBoys();
        }
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
                cartProduct.setMRP(mListItems.get(a).getMRP());
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
                try {

                    cartProduct.setQuantity(Integer.parseInt(mListItems.get(a).getRequestedQty() + ""));
                } catch (Exception e) {
                    e.printStackTrace();
                    cartProduct.setQuantity((int) Double.parseDouble(mListItems.get(a).getRequestedQty()));
                }

                cartProduct.setSubCategory("");


                dbManager.insert(cartProduct);
            }
        }
        setCartCount();
        startActivity(new Intent(OrderHistoryActivity.this, CartActivity.class));
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
        prefs = PreferenceManager.getDefaultSharedPreferences(OrderHistoryActivity.this);
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
    OrderImagesAdapter orderImagesAdapter;
    ArrayList<String> mListImages = new ArrayList<>();
    String cancelitems;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {


            if (requestCode == REQUEST_CODE) {

                SharedPreferences prefs;
                prefs = PreferenceManager.getDefaultSharedPreferences(OrderHistoryActivity.this);
                final String path = prefs.getString(PREFS_FILE_PATH, filePath);
                Log.d("filep", String.valueOf(filePath));
                mListImages.add(path);
                orderImagesAdapter.notifyDataSetChanged();
                // orderImagesAdapter.addItems(path);
                //     new ImageCompressionAsyncTask(true).execute(path);

            }
            if (requestCode == 5566) {

                double wallet = Double.parseDouble(prefs.getPREFS_currentbal().trim());
                double orderamount = Double.parseDouble(getorderamount.trim());
                if (wallet < orderamount) {
                    Dialog dialog = new Dialog(OrderHistoryActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.setContentView(R.layout.dialog_wallet_amount);
                    dialog.setCancelable(true);
                    final Button submit = (Button) dialog.findViewById(R.id.btnupdate);
                    final TextView txtWalletAmtShow = (TextView) dialog.findViewById(R.id.txtWalletAmtShow);
                    final TextView tvdesc = (TextView) dialog.findViewById(R.id.tvdesc);
                    tvdesc.setText("You have insufficient amount in your wallet to make an order, Your wallet  amount should have atleast Rs.1000.Please add money to wallet and try again.");
                    txtWalletAmtShow.setText("₹" + prefs.getPREFS_currentbal());
                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            startActivityForResult(new Intent(OrderHistoryActivity.this, WalleTransactionActivity.class), 5566);
                            //startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + getPackageName())));


                        }
                    });

                    // Display the custom alert dialog on interface
                    dialog.show();

                } else {
                    submitchnagepaymode(getorderid);
                }
            }
            if (requestCode == 122) {
                Bundle bundle = data.getExtras();

                list = new Gson().fromJson(mDataPreference.getString("mListData", ""), new TypeToken<List<OrderBean>>() {
                }.getType());

                adapter = new OrderAdapter(OrderHistoryActivity.this, list, (OrderClicklistner) OrderHistoryActivity.this, (Clickevent) OrderHistoryActivity.this, 1);
                lvProducts.setAdapter(adapter);
                lvProducts.scrollToPosition(bundle.getInt("mpos"));

            }





            /*  if (requestCode == REQUEST_IMAGE) {
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getParcelableExtra("path");
                    try {
                        final String path = uri.getPath();
                        mImages.add(path);
                        mAdapter.addItems(mImages);
                        new ImageCompressionAsyncTask(true).execute(path);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
*/
        }
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        //    prefs.setNearByApiStatus(true);
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "User chose not to make required location settings changes.");
                        break;
                }
                break;
        }
    }

    @Override
    public void senddata(OrderBean orderBean, int pos) {

        createPDF(getApplicationContext(),"NewJatinpdf");


        getorderid = orderBean.getOrderID();
        getorderamount = orderBean.getRAmount();


        PayDialog.show();
        if (paylist.size() > 0) {
            paytypemode.setAdapter(new Payment_Option_Adapter(getApplicationContext(), paylist, (CartItemsadd) OrderHistoryActivity.this));
        } else {
            getdeliverylist();

        }

    }

    @Override
    public void onImageDelete(int mPosition) {
        mListImages.remove(mPosition);
        orderImagesAdapter.notifyDataSetChanged();

    }

    @Override
    public void selectaddress(DeliveryHelper type) {

    }

    @Override
    public void sendpayval(Payhelper res) {
        getcatcodeid = res.getPayid();
        paytype = res.getStringpaytype();

    }

    @Override
    public void sendtimeslotval(String res, String delverrate) {

    }


    @Override
    public void senddeliveryval(DeliveryHelper res) {


        getpaylist(res.getDeliveryid());


    }

    @Override
    public void clickpdfdata() {

    }


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
                modle.setmServerId(mBeanOrder.getOrderID());
                modle.setmDescription("order_master");
                modle.setmImageType("order_master");
                arrayList.add(modle);

                DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                    @Override
                    public void run(SQLiteDatabase database) {

                        ImageMasterDAO dao = new ImageMasterDAO(database, OrderHistoryActivity.this);
                        ArrayList<DbImageMaster> list = arrayList;
                        dao.insert(list);
                        addedToMasterTable = true;
                    }
                });
            }


            DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                @Override
                public void run(SQLiteDatabase database) {
                    ImageMasterDAO dao = new ImageMasterDAO(database, OrderHistoryActivity.this);
                    dao.getlatestinsertedid();
                    String.valueOf(mBeanOrder.getOrderID());
                }
            });


            final ArrayList<DbImageUpload> arrayListUpload = new ArrayList<>();
            DbImageUpload modle = new DbImageUpload();
            modle.setmDate(JKHelper.getCurrentDate());
            modle.setmImageUploadStatus(0);
            modle.setmDescription("order_master");
            modle.setmImageType("order_master");
            modle.setmServerId(mBeanOrder.getOrderID());
           /* modle.setmDescription("Document_Master");
            modle.setmImageType("Document_Master");
        */
            modle.setmImageId(Integer.parseInt(mBeanOrder.getOrderID()));
            modle.setmImagePath(path);
            modle.setmImageName(path);
            arrayListUpload.add(modle);


            DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                @Override
                public void run(SQLiteDatabase database) {
                    ImageUploadDAO dao = new ImageUploadDAO(database, OrderHistoryActivity.this);
                    ArrayList<DbImageUpload> list = arrayListUpload;
                    dao.insert(list);

                    log.e("photo inserted ");
                }
            });

            //   setPhotoCount();
        }
    }

    private void uploadimage() {
        if (JKHelper.isConnectedToNetwork(OrderHistoryActivity.this) && !JKHelper.isServiceRunning(OrderHistoryActivity.this, ImageUploadService.class)) {
            JKHelper.showLog("Start Service");
            startService(new Intent(OrderHistoryActivity.this, ImageUploadService.class));
        } else {
            stopService(new Intent(OrderHistoryActivity.this, ImageUploadService.class));
            startService(new Intent(OrderHistoryActivity.this, ImageUploadService.class));
        }
    }

    private void updateserverphotoid() {
        if (lastRowMaterTable != null) {
            SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
            ImageUploadDAO pd = new ImageUploadDAO(database, OrderHistoryActivity.this);
            pd.setWorkIdToTable(String.valueOf(universalorderid), lastRowMaterTable);
            ImageMasterDAO pds = new ImageMasterDAO(database, OrderHistoryActivity.this);
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
        Intent intent = new Intent(OrderHistoryActivity.this, ImagePickerActivity.class);
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
        Intent intent = new Intent(OrderHistoryActivity.this, ImagePickerActivity.class);
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

            progressDialog1 = ProgressDialog.show(OrderHistoryActivity.this, "", "loading...");

        }

        @Override
        protected String doInBackground(Void... params) {
            Preferencehelper prefs;
            prefs = new Preferencehelper(OrderHistoryActivity.this);
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
        Intent intent = new Intent(OrderHistoryActivity.this, ImagePickerActivity.class);
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

    public void setorderamount() {
        for (int i = 0; i < list.size(); i++) {
            String currency;
            if (list.get(i).getCurrency().equalsIgnoreCase("INR")) {
                currency = context.getResources().getString(R.string.rupee);

                totalamountstr.setText(currency + " " + new DecimalFormat("##.##").format(Double.parseDouble(list.get(i).getRAmount())));

            } else {
                currency = "$";
                totalamountstr.setText(currency + " " + new DecimalFormat("##.##").format(Double.parseDouble(list.get(i).getRAmount())));

            }
        }
    }


    ArrayList<DeliveryBoysBean> mListDeliveryBoys = new ArrayList<>();

    public void getDeliveryBoys() {
        final ProgressDialog mProgressBar = new ProgressDialog(OrderHistoryActivity.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");

        if (isTrue) {
            mProgressBar.show();

        }
        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (isTrue) {
                            mProgressBar.dismiss();

                        }


                        try {
                            list = new ArrayList<>();
                            Log.d("resp_delivery", response);
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, OrderHistoryActivity.this);
                            Log.d("resp_delivery", responses);
                            JSONObject jsonObject = new JSONObject(responses);
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                mListDeliveryBoys = new Gson().fromJson(jsonObject.getString("returnds"), new TypeToken<List<DeliveryBoysBean>>() {
                                }.getType());
                                if (mListDeliveryBoys.size() > 0) {
                                    ArrayList<String> mListBoys = new ArrayList<>();
                                    mListBoys.add("Select Delive Boy");
                                    for (int a = 0; a < mListDeliveryBoys.size(); a++) {
                                       /* if (mListDeliveryBoys.get(a).getFirstName().isEmpty()) {
                                            mListDeliveryBoys.remove(a);
                                        } else {*/
                                        mListBoys.add(mListDeliveryBoys.get(a).getFirstName());
                                        // }
                                    }
                                    spBoys.setAdapter(new ArrayAdapter<String>(OrderHistoryActivity.this, android.R.layout.simple_list_item_1, mListBoys));
                                    //dialogAssignOrder.show();
                                }
                                if (isTrue) {
                                    if (mListDeliveryBoys.size() > 0) {
                                        dialogAssignOrder.show();
                                    } else {
                                        isTrue = true;
                                        getDeliveryBoys();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (isTrue) {
                            mProgressBar.dismiss();

                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params1 = new HashMap<String, String>();
                //   params1.put("contactid", prefs.getPrefsContactId());
                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);

                    String param = getString(R.string.GET_DELIVERY_BOYS) + "&companyid=" + prefs.getCID() + "&contactid=" + sytrContactId/*+ "&fdate=" + mStartDate + "&tdate=" + mTodayDate + "&type=" + mType*/;
                    Log.d("before_enc_dlvry", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, OrderHistoryActivity.this);
                    params1.put("val", finalparam);
                    Log.d("after_enc_dlvry", finalparam);
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
//http://www.mmthinkbiz.com/mobileservice.aspx?method=updateorderstatus_web&orderid=35&status=ashwani%20sahu&type=7&contactId=59298

    }

    public void assignOrder(String userId) {
        final ProgressDialog mProgressBar = new ProgressDialog(OrderHistoryActivity.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");


        mProgressBar.show();


        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        mProgressBar.dismiss();


                        try {
                            list = new ArrayList<>();
                            Log.d("resp_delivery", response);
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, OrderHistoryActivity.this);
                            Log.d("resp_delivery", responses);
                            JSONObject jsonObject = new JSONObject(responses);
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                FancyToast.makeText(OrderHistoryActivity.this, "Order Assigned Successfully to " + mListDeliveryBoys.get(spBoys.getSelectedItemPosition() - 1).getFirstName(), FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                                dialogAssignOrder.dismiss();

                            } else {
                                FancyToast.makeText(OrderHistoryActivity.this, "failed to assign order, try again.", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();

                            }
                        } catch (Exception e) {
                            FancyToast.makeText(OrderHistoryActivity.this, "failed to assign order, try again.", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        mProgressBar.dismiss();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Preferencehelper prefs;
                prefs = new Preferencehelper(OrderHistoryActivity.this);

                Map<String, String> params1 = new HashMap<String, String>();
                //   params1.put("contactid", prefs.getPrefsContactId());
                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);
//=35&=ashwani%20sahu&=7&=59298
                    String param = getString(R.string.UPDATE_ORDER_STATUS) + "&type=7" + "&orderid=" + mBeanOrder.getOrderID()
                            + "&status=" + mListDeliveryBoys.get(spBoys.getSelectedItemPosition() - 1).getFirstName() + "&contactId=" + userId /*+ "&fdate=" + mStartDate + "&tdate=" + mTodayDate + "&type=" + mType*/;
                    Log.d("before_enc_odr", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, OrderHistoryActivity.this);
                    params1.put("val", finalparam);
                    Log.d("after_enc_odr", finalparam);
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


    public void getdeliverylist() {

        Log.e("dsfkjjfk", AppController.getValTypeFromDesc("order_master"));
        final ProgressDialog mProgressBar = new ProgressDialog(OrderHistoryActivity.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();
        SharedPreferences mData = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);

        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("responsive", response);
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, getApplicationContext());
                            Log.e("responses123", responses);
                            JSONObject jsonObject = new JSONObject(responses);

                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                //for (int a=0;a<mArrayList.size();a++){
                                //"returnds":[{"OrderID":"6","successmsg":"Successful"}]
                                deliverylist = new ArrayList<DeliveryHelper>();
                                alldeliverylist = new ArrayList<>();
                                deliverylist.clear();
                                JSONArray jsonArray = jsonObject.getJSONArray("returnds");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    catcodeid = jsonObject1.getString("CatCodeID");
                                    catcodedesc = jsonObject1.getString("CodeDescription");
                                    DeliveryHelper deliveryHelper2 = new DeliveryHelper();
                                    deliveryHelper2.setDeliveryid(catcodeid);
                                    deliveryHelper2.setDeliverytype(catcodedesc);
                                    alldeliverylist.add(deliveryHelper2);

                                    // if (jsonObject1.getString("Val1").equalsIgnoreCase(mData.getString("shopId", "1"))) {
                                    catcodeid = jsonObject1.getString("CatCodeID");
                                    catcodedesc = jsonObject1.getString("CodeDescription");
                                    DeliveryHelper deliveryHelper = new DeliveryHelper();
                                    if (deliverylist.size() > 0) {


                                        for (int l = 0; l < alldeliverylist.size(); l++) {
                                            if (alldeliverylist.get(i).getDeliveryid().equalsIgnoreCase("")) {

                                                catcodeid = alldeliverylist.get(i).getDeliveryid();
                                                catcodedesc = alldeliverylist.get(i).getDeliverytype();
                                                DeliveryHelper deliveryHelper1 = new DeliveryHelper();
                                                deliveryHelper1.setDeliveryid(catcodeid);
                                                deliveryHelper1.setDeliverytype(catcodedesc);
                                                deliverylist.add(deliveryHelper);
                                            }


                                        }


                                    } else {
                                        deliveryHelper.setDeliveryid(catcodeid);
                                        deliveryHelper.setDeliverytype(catcodedesc);
                                        deliverylist.add(deliveryHelper);

                                    }
                                    //}


                                }


                                deliverytypemode.setAdapter(new Delivery_Option_Adapter(getApplicationContext(), deliverylist, OrderHistoryActivity.this, OrderHistoryActivity.this));

//}

                            } else {

                                FancyToast.makeText(getApplicationContext(), "No Data Found, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
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
                        FancyToast.makeText(getApplicationContext(), "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                String mTodayDate = df.format(c.getTime());

                SharedPreferences mData = getSharedPreferences("MORNING_PARCEL_GROCERY",
                        MODE_PRIVATE);


                Preferencehelper prefs;
                prefs = new Preferencehelper(getApplicationContext());
                Map<String, String> params = new HashMap<String, String>();


                try {
                    String param = getString(R.string.URL_GET_CATEGORY_CODE) + "&type=" + "112" + "&contactid=" + prefs.getPrefsContactId() + "&supplierid=0";
                    Log.d("Beforeencrption", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, getApplicationContext());
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

        Volley.newRequestQueue(context).add(postRequest);


    }

    public void getpaylist(String delivyvalid) {

        Log.e("dsfkjjfk11", AppController.getValTypeFromDesc("order_master"));
        final ProgressDialog mProgressBar = new ProgressDialog(OrderHistoryActivity.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("responsivepaylist", response);
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, getApplicationContext());
                            Log.e("otherresponses123", responses);
                            JSONObject jsonObject = new JSONObject(responses);
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                //for (int a=0;a<mArrayList.size();a++){
                                //"returnds":[{"OrderID":"6","successmsg":"Successful"}]
                                JSONArray jsonArray = jsonObject.getJSONArray("returnds");
                                paylist.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    Log.d("valsss", jsonObject1.getString("Val1") + " = " + delivyvalid);

                                    if (jsonObject1.getString("Val1").equalsIgnoreCase(delivyvalid)) {
                                        catcodeid = jsonObject1.getString("CatCodeID");
                                        catcodedesc = jsonObject1.getString("CodeDescription");
                                        if (!catcodedesc.equalsIgnoreCase("COD")) {
                                            Payhelper payhelper = new Payhelper();
                                            payhelper.setPayid(catcodeid);
                                            payhelper.setStringpaytype(catcodedesc);
                                            paylist.add(payhelper);
                                            Log.e("OKOK", "" + catcodedesc);
                                            Log.e("HJHJJH", new Gson().toJson(paylist));
                                            paytypemode.setAdapter(new Payment_Option_Adapter(getApplicationContext(), paylist, (CartItemsadd) OrderHistoryActivity.this));


                                        }


                                    }

                                }

//}

                            } else {

                                FancyToast.makeText(OrderHistoryActivity.this, "No Data Found, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
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
                        FancyToast.makeText(OrderHistoryActivity.this, "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                String mTodayDate = df.format(c.getTime());

                SharedPreferences mData = getSharedPreferences("MORNING_PARCEL_GROCERY",
                        MODE_PRIVATE);


                Preferencehelper prefs;
                prefs = new Preferencehelper(OrderHistoryActivity.this);
                Map<String, String> params = new HashMap<String, String>();


                try {

                    String param = getString(R.string.URL_GET_CATEGORY_CODE) + "&type=" + "113" + "&contactid=" + prefs.getPrefsContactId() + "&supplierid=0";
                    Log.d("Beforeencrptionpay", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, OrderHistoryActivity.this);
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

        Volley.newRequestQueue(OrderHistoryActivity.this).add(postRequest);


    }


    public void submitchnagepaymode(String orderIdd) {

        Log.e("dsfkjjfk11", AppController.getValTypeFromDesc("order_master"));
        final ProgressDialog mProgressBar = new ProgressDialog(OrderHistoryActivity.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("responsivepaylist", response);
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, OrderHistoryActivity.this);
                            Log.e("otherresponses123", responses);
                            JSONObject jsonObject = new JSONObject(responses);
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                //for (int a=0;a<mArrayList.size();a++){
                                //"returnds":[{"OrderID":"6","successmsg":"Successful"}]
                                PayDialog.dismiss();

                                FancyToast.makeText(OrderHistoryActivity.this, "Payment mode changed successfully", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                                finish();
                                startActivity(new Intent(getApplicationContext(), OrderHistoryActivity.class));

                            } else {

                                FancyToast.makeText(OrderHistoryActivity.this, "No Data Found, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
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
                        FancyToast.makeText(OrderHistoryActivity.this, "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                String mTodayDate = df.format(c.getTime());

                SharedPreferences mData = getSharedPreferences("MORNING_PARCEL_GROCERY",
                        MODE_PRIVATE);


                Preferencehelper prefs;
                prefs = new Preferencehelper(OrderHistoryActivity.this);
                Map<String, String> params = new HashMap<String, String>();


                try {

                    String param = getString(R.string.UPDATE_ORDER_STATUS) + "&orderid=" + orderIdd + "&status=" + paytype + "&type=11" + "&contactId=" + getcatcodeid;
                    Log.d("Beforeencrptionpay", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, OrderHistoryActivity.this);
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

        Volley.newRequestQueue(OrderHistoryActivity.this).add(postRequest);


    }


    Dialog deliverOrder;
    EditText edtNote;
    Button btnDeliver;
    ImageView imgPhoto, imgSign, deliveryClose;
    LinearLayout signLayout, photoLayout;
    String strChangeStatus = "";

    int imageType = 0;

    public void orderCompleteDialog() {
        deliverOrder = new Dialog(OrderHistoryActivity.this);
        deliverOrder.setContentView(R.layout.dialog_complete_order);

        photoLayout = deliverOrder.findViewById(R.id.photoLayout);
        signLayout = deliverOrder.findViewById(R.id.signLayout);
        edtNote = deliverOrder.findViewById(R.id.edtNote);

        imgSign = deliverOrder.findViewById(R.id.imgSign);
        imgPhoto = deliverOrder.findViewById(R.id.imgPhoto);
        btnDeliver = deliverOrder.findViewById(R.id.btnSubmit);
        deliveryClose = deliverOrder.findViewById(R.id.closeHeader);
        mClearButton = deliverOrder.findViewById(R.id.clear_button);
        mSignaturePad = deliverOrder.findViewById(R.id.signature);


        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {


            }

            @Override
            public void onSigned() {
                mClearButton.setEnabled(true);


            }

            @Override
            public void onClear() {


                mClearButton.setEnabled(false);


            }
        });


        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignaturePad.clear();
            }
        });

        photoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageType = 1;
                if (ActivityCompat.checkSelfPermission(OrderHistoryActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(OrderHistoryActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                } else {

                    Intent intent1 = new Intent(OrderHistoryActivity.this, ImagePickActivity.class);
                    intent1.putExtra(IS_NEED_CAMERA, true);
                    intent1.putExtra(com.vincent.filepicker.Constant.MAX_NUMBER, 1);
                    intent1.putExtra(IS_NEED_FOLDER_LIST, true);
                    startActivityForResult(intent1, com.vincent.filepicker.Constant.REQUEST_CODE_PICK_IMAGE);
                }
            }
        });
        signLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageType = 2;

                if (ActivityCompat.checkSelfPermission(OrderHistoryActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(OrderHistoryActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                } else {

                    Intent intent1 = new Intent(OrderHistoryActivity.this, ImagePickActivity.class);
                    intent1.putExtra(IS_NEED_CAMERA, true);
                    intent1.putExtra(com.vincent.filepicker.Constant.MAX_NUMBER, 1);
                    intent1.putExtra(IS_NEED_FOLDER_LIST, true);
                    startActivityForResult(intent1, com.vincent.filepicker.Constant.REQUEST_CODE_PICK_IMAGE);
                }
            }
        });
        btnDeliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageType = 2;
                //this api will hit on this click
//                updateOrderStatus(mOrderBean.getOrderID(), "1", strChangeStatus);
                File mFile1;
                StringBuilder builder = new StringBuilder();
                ArrayList<String> mImages = new ArrayList<>();
                Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
                String path = createpng(OrderHistoryActivity.this, 99, signatureBitmap).getPath();
                mFile1 = new File(path);
                builder.append(path + "\n");
                mImages.add(path);

//                    mAdapter1.addItems(mImages);
                log.e("file path in request code true==" + path);
                //  if (!mBean.getStrrmasterid().isEmpty()) {
               /* if (addJpgSignatureToGallery(signatureBitmap)) {
                    Toast.makeText(OrderHistoryActivity.this, "Signature saved into the Gallery", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OrderHistoryActivity.this, "Unable to store the signature", Toast.LENGTH_SHORT).show();
                }*/

                new ImageCompressionAsyncTask(true).execute(path);
                /*
                if (addSvgSignatureToGallery(mSignaturePad.getSignatureSvg())) {
                   Toast.makeText(OrderHistoryActivity.this, "SVG Signature saved into the Gallery", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OrderHistoryActivity.this, "Unable to store the SVG signature", Toast.LENGTH_SHORT).show();
                }
*/
                deliverOrder.dismiss();
                strChangeStatus = "";
            }
        });
        deliveryClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deliverOrder.dismiss();
                strChangeStatus = "";

            }
        });
    }


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private SignaturePad mSignaturePad;
    private Button mClearButton;


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(OrderHistoryActivity.this, "Cannot write images to external storage", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created");
        }
        return file;
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }

    public boolean addJpgSignatureToGallery(Bitmap signature) {
        boolean result = false;
        try {
            File photo = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.jpg", System.currentTimeMillis()));
            saveBitmapToJPG(signature, photo);
            scanMediaFile(photo);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }

    public boolean addSvgSignatureToGallery(String signatureSvg) {
        boolean result = false;
        try {
            File svgFile = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.svg", System.currentTimeMillis()));
            OutputStream stream = new FileOutputStream(svgFile);
            OutputStreamWriter writer = new OutputStreamWriter(stream);
            writer.write(signatureSvg);
            writer.close();
            stream.flush();
            stream.close();
            scanMediaFile(svgFile);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Checks if the app has permission to write to device storage
     * <p/>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity the activity from which permissions are checked
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    File createpng(Context context, int position, Bitmap bitmapcrs) {
        //create a file to write bitmap data
        File f = new File(context.getCacheDir(), "signaturee+" + position);
        try {
            f.createNewFile();
            //Convert bitmap to byte array
            Bitmap bitmap = bitmapcrs;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            return f;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;


    }

    ;


    private PdfWriter pdfWriter;

    //we will add some products to arrayListRProductModel to show in the PDF document
    private static ArrayList<ProductModel> arrayListRProductModel = new ArrayList<ProductModel>();

    public boolean createPDF(Context context, String reportName) {

        try {
            //creating a directory in SD card


            File mydir = new File(Environment.getExternalStorageDirectory()
                    + "/SIAS/REPORT_PRODUCT/"); //PATH_PRODUCT_REPORT="/SIAS/REPORT_PRODUCT/"
            if (!mydir.exists()) {
                mydir.mkdirs();
            }

            //getting the full path of the PDF report name
            String mPath = Environment.getExternalStorageDirectory().toString()
                    + "/SIAS/REPORT_PRODUCT/" //PATH_PRODUCT_REPORT="/SIAS/REPORT_PRODUCT/"
                    + reportName + ".pdf"; //reportName could be any name

            //constructing the PDF file
            File pdfFile = new File(mPath);

            //Creating a Document with size A4. Document class is available at  com.itextpdf.text.Document
            Document document = new Document(PageSize.A4);

            //assigning a PdfWriter instance to pdfWriter
            pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));

            //PageFooter is an inner class of this class which is responsible to create Header and Footer
            PageHeaderFooter event = new PageHeaderFooter();
            pdfWriter.setPageEvent(event);

            //Before writing anything to a document it should be opened first
            document.open();

            //Adding meta-data to the document
            addMetaData(document);
            //Adding Title(s) of the document
            addTitlePage(document);
            //Adding main contents of the document
            addContent(document);


            //Closing the document
            document.close();
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
        return true;
    }

    /**
     * iText allows to add metadata to the PDF which can be viewed in your Adobe Reader. If you right click
     * on the file and to to properties then you can see all these information.
     *
     * @param document
     */
    private static void addMetaData(Document document) {
        document.addTitle("All Product Names");
        document.addSubject("none");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("SIAS ERP");
        document.addCreator("Jatin Sharma");
    }

    /**
     * In this method title(s) of the document is added.
     *
     * @param document
     * @throws DocumentException
     */
    private static void addTitlePage(Document document)
            throws DocumentException {
        Paragraph paragraph = new Paragraph();

        // Adding several title of the document. Paragraph class is available in  com.itextpdf.text.Paragraph
        Paragraph childParagraph = new Paragraph("Safe'O'Kart SuperMart", StaticValue.FONT_TITLE); //public static Font FONT_TITLE = new Font(Font.FontFamily.TIMES_ROMAN, 22,Font.BOLD);
        childParagraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(childParagraph);

        childParagraph = new Paragraph("E-Mail:morningparcel@morningparcel.com", StaticValue.FONT_SUBTITLE); //public static Font FONT_SUBTITLE = new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.BOLD);
        childParagraph.setAlignment(Element.ALIGN_CENTER);
        childParagraph.setSpacingBefore(20);
        paragraph.add(childParagraph);


        childParagraph = new Paragraph("Phone : +911111116666", StaticValue.FONT_SUBTITLE); //public static Font FONT_SUBTITLE = new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.BOLD);
        childParagraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(childParagraph);

        childParagraph = new Paragraph("Tax Invoice", StaticValue.FONT_SUBTITLE);
        childParagraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(childParagraph);

        document.add(paragraph);
        // LINE SEPARATOR
        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setAlignment(Element.ALIGN_TOP);
        lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));
        document.add(new Chunk(lineSeparator));
        //End of adding several titles

    }

    /**
     * In this method the main contents of the documents are added
     *
     * @param document
     * @throws DocumentException
     */

    private void addContent(Document document) throws DocumentException {

        Paragraph reportBody = new Paragraph();
        Paragraph reportBodytwo = new Paragraph();

        reportBody.setFont(StaticValue.FONT_BODY); //public static Font FONT_BODY = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL);


        BaseFont baseFont = null;
        try {
            baseFont = BaseFont.createFont("res/font/robotolight.ttf", "UTF-8", BaseFont.EMBEDDED);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        Font regularHead = new Font(baseFont, 15, Font.NORMAL, BaseColor.WHITE);
        Font regularReport = new Font(baseFont, 15, Font.NORMAL);
        Font regularName = new Font(baseFont, 15, Font.NORMAL, BaseColor.BLACK);
        Font regularAddress = new Font(baseFont, 15, Font.NORMAL, BaseColor.BLACK);
        Font regularSub = new Font(baseFont, 15);
        Font regularTotal = new Font(baseFont, 15, Font.NORMAL, BaseColor.BLACK);


        PdfPTable tableHeading = new PdfPTable(2);
        tableHeading.setSpacingBefore(2);
        tableHeading.setSpacingAfter(2);
        tableHeading.setWidthPercentage(95);

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat dfh = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = df.format(c);
        String formattedtime = dfh.format(c);

        String theName = "", theAddress = "";

        theName = "Customer :haisb";
        theAddress = "Mobile:  9876543210";
        String thdate = "Date: " + formattedDate;
        String thebillno = "Bill No:85564";


        String thetime = "Time: " + formattedtime;
        String theusername = "User: haisb";

        PdfPCell preName = new PdfPCell(new Phrase(theName, regularName));
        PdfPCell preAddress = new PdfPCell(new Phrase(theAddress, regularAddress));
        PdfPCell preDate = new PdfPCell(new Phrase(thdate, regularAddress));
        PdfPCell preBill = new PdfPCell(new Phrase(thebillno, regularAddress));

        PdfPCell preuser = new PdfPCell(new Phrase(theusername, regularAddress));
        PdfPCell pretime = new PdfPCell(new Phrase(thetime, regularAddress));

        preBill.setVerticalAlignment(Element.ALIGN_BOTTOM);
        preBill.setHorizontalAlignment(Element.ALIGN_RIGHT);


        pretime.setVerticalAlignment(Element.ALIGN_BOTTOM);
        pretime.setHorizontalAlignment(Element.ALIGN_RIGHT);

        preDate.setVerticalAlignment(Element.ALIGN_BOTTOM);
        preDate.setHorizontalAlignment(Element.ALIGN_RIGHT);


        preName.setBorder(Rectangle.NO_BORDER);
        preAddress.setBorder(Rectangle.NO_BORDER);
        preDate.setBorder(Rectangle.NO_BORDER);
        preuser.setBorder(Rectangle.NO_BORDER);
        pretime.setBorder(Rectangle.NO_BORDER);
        preBill.setBorder(Rectangle.NO_BORDER);

        try {
            tableHeading.addCell(preName);
            tableHeading.addCell(preBill);
            tableHeading.addCell(preAddress);
            tableHeading.addCell(preDate);
            tableHeading.addCell(preuser);
            tableHeading.addCell(pretime);
            document.add(tableHeading);
            // LINE SEPARATOR
            LineSeparator lineSeparator = new LineSeparator();
            lineSeparator.setAlignment(Element.ALIGN_TOP);
            lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));
            document.add(new Chunk(lineSeparator));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        //   Creating a table
        createTable(reportBody);


        // now add all this to the document


        document.add(reportBody);


        // LINE SEPARATOR
        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setAlignment(Element.ALIGN_TOP);
        lineSeparator.setLineColor(new BaseColor(0, 0, 0, 100));
        document.add(new Chunk(lineSeparator));

        botheader("Item: "+list.get(1).getmListItems().size(), "Free Qty: 0", "Round Off:      " + "", "         " + "0", document);

        botheader("SubTotal:", "        " + "0", "     " + "", "         " + "", document);

        botheader("CGST:", "        " + "0", "              " + "", "         " + "", document);

        botheader("SGST:", "        " + "0", "     " + "", "         " + "", document);

        botheader("IGST:", "        " + "0", "      " + "", "         " + "", document);

        botheadertwo("Cust GSTIN:", "        ", document);


        botheadertwo("Delivery Add:", "H.N-O089/Block B ,Old faridabad,Sector 18,      " + "", document);

        createTableTwo(reportBodytwo);
        document.add(reportBodytwo);

        BaseFont baseFontnu = null;
        try {
            baseFontnu = BaseFont.createFont("res/font/robotolight.ttf", "UTF-8", BaseFont.EMBEDDED);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Font mOrderIdFont = new Font(baseFontnu, 15, Font.NORMAL, BaseColor.BLACK);


        Paragraph newpara = new Paragraph("You Have saved on T.AMT Rs.0", mOrderIdFont); //public static Font FONT_SUBTITLE = new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.BOLD);
        newpara.setAlignment(Element.ALIGN_LEFT);
        newpara.setSpacingBefore(5);

        document.add(newpara);

        // LINE SEPARATOR
        LineSeparator lineSeparatorfinal = new LineSeparator();
        lineSeparatorfinal.setAlignment(Element.ALIGN_TOP);
        lineSeparatorfinal.setLineColor(new BaseColor(0, 0, 0, 68));
        document.add(new Chunk(lineSeparatorfinal));

        Font mOrderIdFontbold = new Font(baseFontnu, 15, Font.BOLD, BaseColor.BLACK);

        Paragraph newparam = new Paragraph( EnglishNumberToWords.convert(15789), mOrderIdFontbold); //public static Font FONT_SUBTITLE = new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.BOLD);
        newparam.setAlignment(Element.ALIGN_LEFT);
        newparam.setSpacingBefore(5);

        document.add(newparam);
        // LINE SEPARATOR
        LineSeparator lineSeparatorfinalm = new LineSeparator();
        lineSeparatorfinalm.setAlignment(Element.ALIGN_TOP);
        lineSeparatorfinalm.setLineColor(new BaseColor(0, 0, 0, 68));
        document.add(new Chunk(lineSeparatorfinalm));

        singlepara("TERMS & Conditions", document);
        singlepara("1.TOTAL SALE PRICE INCLUSIVE OF GST", document);
        singlepara("2.EXCHANGE WITH IN 7 DAYS OF PURCHASE", document);
        singlepara("3.PLEASE RETAIN THE BILL FOR EXCHANGE", document);
        singlepara("4.OUR CARRY BAGS ARE REFUNDABLE", document);
        singlepara("E.& O.E Safe'O'Kart Supermarket ", document);


    }

    public void singlepara(String basestr, Document document) {


        BaseFont baseFont = null;
        try {
            baseFont = BaseFont.createFont("res/font/robotolight.ttf", "UTF-8", BaseFont.EMBEDDED);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Font mOrderIdFontbold = new Font(baseFont, 15, Font.NORMAL, BaseColor.BLACK);

        Paragraph newparamagain = new Paragraph(basestr, mOrderIdFontbold); //public static Font FONT_SUBTITLE = new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.BOLD);
        newparamagain.setAlignment(Element.ALIGN_LEFT);
        newparamagain.setSpacingBefore(5);

        try {
            document.add(newparamagain);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is responsible to add table using iText
     *
     * @param reportBody
     * @throws BadElementException
     */
    private  void createTable(Paragraph reportBody)
            throws BadElementException {

        float[] columnWidths = {3, 6, 4, 4, 4, 4, 4, 3}; //total 4 columns and their width. The first three columns will take the same width and the fourth one will be 5/2.
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100); //set table with 100% (full page)
        table.getDefaultCell().setUseAscender(true);
        table.setSpacingBefore(10);


        //Adding table headers
        PdfPCell cell = new PdfPCell(new Phrase("SR.No", //Table Header
                StaticValue.FONT_TABLE_HEADER)); //Public static Font FONT_TABLE_HEADER = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER); //alignment
        //  cell.setBackgroundColor(new GrayColor(0.75f)); //cell background color
        cell.setFixedHeight(30); //cell height
        table.addCell(cell);

        //Adding table headers
        cell = new PdfPCell(new Phrase("Item Description", //Table Header
                StaticValue.FONT_TABLE_HEADER)); //Public static Font FONT_TABLE_HEADER = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER); //alignment
        //  cell.setBackgroundColor(new GrayColor(0.75f)); //cell background color
        cell.setFixedHeight(30); //cell height
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("HSN CODE",
                StaticValue.FONT_TABLE_HEADER));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        // cell.setBackgroundColor(new GrayColor(0.75f));
        cell.setFixedHeight(30);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Qty",
                StaticValue.FONT_TABLE_HEADER));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //cell.setBackgroundColor(new GrayColor(0.75f));
        cell.setFixedHeight(30);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("MRP",
                StaticValue.FONT_TABLE_HEADER));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //   cell.setBackgroundColor(new GrayColor(0.75f));
        cell.setFixedHeight(30);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Rate",
                StaticValue.FONT_TABLE_HEADER));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //   cell.setBackgroundColor(new GrayColor(0.75f));
        cell.setFixedHeight(30);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Amount",
                StaticValue.FONT_TABLE_HEADER));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //   cell.setBackgroundColor(new GrayColor(0.75f));
        cell.setFixedHeight(30);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Status",
                StaticValue.FONT_TABLE_HEADER));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //   cell.setBackgroundColor(new GrayColor(0.75f));
        cell.setFixedHeight(30);
        table.addCell(cell);

        //End of adding table headers

        //This method will generate some static data for the table
        generateTableData();

        //Adding data into table
        for (int i = 0; i < arrayListRProductModel.size(); i++) { //

            cell = new PdfPCell(new Phrase(String.valueOf(i + 1)));
            cell.setFixedHeight(28);
            cell.setPaddingLeft(20);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(arrayListRProductModel.get(i).getName()));
            cell.setFixedHeight(28);
            cell.setPaddingLeft(20);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(""));
            cell.setFixedHeight(28);
            cell.setPaddingLeft(20);
            table.addCell(cell);


            cell = new PdfPCell(new Phrase(arrayListRProductModel.get(i).getQty()));
            cell.setFixedHeight(28);
            cell.setPaddingLeft(20);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(arrayListRProductModel.get(i).getMrp()));
            cell.setFixedHeight(28);
            cell.setPaddingLeft(20);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(arrayListRProductModel.get(i).getRate()));
            cell.setFixedHeight(28);
            cell.setPaddingLeft(20);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(arrayListRProductModel.get(i).getAmount()));
            cell.setFixedHeight(28);
            cell.setPaddingLeft(20);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(""));
            cell.setFixedHeight(28);
            cell.setPaddingLeft(20);
            table.addCell(cell);
        }

        reportBody.add(table);


    }

    private  void createTableTwo(Paragraph reportBody)
            throws BadElementException {

        float[] columnWidths = {2, 6, 4, 4, 4, 4}; //total 4 columns and their width. The first three columns will take the same width and the fourth one will be 5/2.
        PdfPTable tabletwo = new PdfPTable(columnWidths);
        tabletwo.setWidthPercentage(100); //set table with 100% (full page)
        tabletwo.getDefaultCell().setUseAscender(true);
        tabletwo.setSpacingBefore(30);


        //Adding table headers
        PdfPCell cell = new PdfPCell(new Phrase("Base Amount", //Table Header
                StaticValue.FONT_TABLE_HEADER)); //Public static Font FONT_TABLE_HEADER = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER); //alignment
        //  cell.setBackgroundColor(new GrayColor(0.75f)); //cell background color
        cell.setFixedHeight(30); //cell height
        tabletwo.addCell(cell);

        //Adding table headers
        cell = new PdfPCell(new Phrase("Description", //Table Header
                StaticValue.FONT_TABLE_HEADER)); //Public static Font FONT_TABLE_HEADER = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER); //alignment
        //  cell.setBackgroundColor(new GrayColor(0.75f)); //cell background color
        cell.setFixedHeight(30); //cell height
        tabletwo.addCell(cell);

        cell = new PdfPCell(new Phrase("CGST",
                StaticValue.FONT_TABLE_HEADER));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        // cell.setBackgroundColor(new GrayColor(0.75f));
        cell.setFixedHeight(30);
        tabletwo.addCell(cell);

        cell = new PdfPCell(new Phrase("SGST",
                StaticValue.FONT_TABLE_HEADER));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //cell.setBackgroundColor(new GrayColor(0.75f));
        cell.setFixedHeight(30);
        tabletwo.addCell(cell);

        cell = new PdfPCell(new Phrase("IGST",
                StaticValue.FONT_TABLE_HEADER));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //   cell.setBackgroundColor(new GrayColor(0.75f));
        cell.setFixedHeight(30);
        tabletwo.addCell(cell);

        cell = new PdfPCell(new Phrase("Total Max",
                StaticValue.FONT_TABLE_HEADER));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //   cell.setBackgroundColor(new GrayColor(0.75f));
        cell.setFixedHeight(30);
        tabletwo.addCell(cell);


        //End of adding table headers

        //This method will generate some static data for the table
        generateTableData();

        //Adding data into table
        for (int i = 0; i < 1; i++) { //

            cell = new PdfPCell(new Phrase(String.valueOf(i + 1)));
            cell.setFixedHeight(28);
            cell.setPaddingLeft(20);
            tabletwo.addCell(cell);

            cell = new PdfPCell(new Phrase("0"));
            cell.setFixedHeight(28);
            cell.setPaddingLeft(20);
            tabletwo.addCell(cell);

            cell = new PdfPCell(new Phrase("0"));
            cell.setFixedHeight(28);
            cell.setPaddingLeft(20);
            tabletwo.addCell(cell);


            cell = new PdfPCell(new Phrase("0"));
            cell.setFixedHeight(28);
            cell.setPaddingLeft(20);
            tabletwo.addCell(cell);

            cell = new PdfPCell(new Phrase("0"));
            cell.setFixedHeight(28);
            cell.setPaddingLeft(20);
            tabletwo.addCell(cell);

            cell = new PdfPCell(new Phrase("0"));
            cell.setFixedHeight(28);
            cell.setPaddingLeft(20);
            tabletwo.addCell(cell);


        }

        reportBody.add(tabletwo);


    }

    /**
     * This method is used to add empty lines in the document
     *
     * @param paragraph
     * @param number
     */
    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    /**
     * This is an inner class which is used to create header and footer
     *
     * @author XYZ
     */

    class PageHeaderFooter extends PdfPageEventHelper {
        Font ffont = new Font(Font.FontFamily.UNDEFINED, 5, Font.ITALIC);

        public void onEndPage(PdfWriter writer, Document document) {

            /**
             * PdfContentByte is an object containing the user positioned text and graphic contents
             * of a page. It knows how to apply the proper font encoding.
             */
            PdfContentByte cb = writer.getDirectContent();

            /**
             * In iText a Phrase is a series of Chunks.
             * A chunk is the smallest significant part of text that can be added to a document.
             *  Most elements can be divided in one or more Chunks. A chunk is a String with a certain Font
             */
            Phrase footer_poweredBy = new Phrase("Powered By SIAS ERP", StaticValue.FONT_HEADER_FOOTER); //public static Font FONT_HEADER_FOOTER = new Font(Font.FontFamily.UNDEFINED, 7, Font.ITALIC);
            Phrase footer_pageNumber = new Phrase("Page " + document.getPageNumber(), StaticValue.FONT_HEADER_FOOTER);

            // Header
            // ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT, header,
            // (document.getPageSize().getWidth()-10),
            // document.top() + 10, 0);

            // footer: show page number in the bottom right corner of each age
            ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT,
                    footer_pageNumber,
                    (document.getPageSize().getWidth() - 10),
                    document.bottom() - 10, 0);
//			// footer: show page number in the bottom right corner of each age
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                    footer_poweredBy, (document.right() - document.left()) / 2
                            + document.leftMargin(), document.bottom() - 10, 0);
        }
    }

    /**
     * Generate static data for table
     */

    private void generateTableData() {

        ProductModel productModel;
        for (int i = 0; i < list.get(1).getmListItems().size(); i++) {
            productModel = new ProductModel(list.get(1).getmListItems().get(i).getItemDescription(), "", String.valueOf(list.get(1).getmListItems().get(i).getQuantity()), list.get(1).getmListItems().get(i).getMRP(), list.get(1).getmListItems().get(i).getRate(), list.get(1).getmListItems().get(i).getRate(), "");
            arrayListRProductModel.add(productModel);

        }


    }

    public void botheader(String first, String second, String third, String four, Document document) {

        BaseFont baseFont = null;
        try {
            baseFont = BaseFont.createFont("res/font/robotolight.ttf", "UTF-8", BaseFont.EMBEDDED);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        PdfPTable tablenew = new PdfPTable(5);
        tablenew.setWidthPercentage(95);
        tablenew.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        PdfPCell cell1 = new PdfPCell(new Paragraph("Cell 1"));
        PdfPCell cell2 = new PdfPCell(new Paragraph("Cell 2"));
        PdfPCell cell3 = new PdfPCell(new Paragraph(""));
        PdfPCell cell4 = new PdfPCell(new Paragraph("Cell 4"));
        PdfPCell cell5 = new PdfPCell(new Paragraph("Cell 5"));

        Font boldfont = new Font(baseFont, 15, Font.NORMAL, BaseColor.BLACK);


        cell1 = new PdfPCell(new Paragraph(first, boldfont));
        cell1.setBorder(0);
        cell2 = new PdfPCell(new Paragraph(second, boldfont));
        cell2.setBorder(0);
        cell4 = new PdfPCell(new Paragraph(third, boldfont));
        cell4.setBorder(0);
        cell5 = new PdfPCell(new Paragraph(four, boldfont));
        cell5.setBorder(0);
        cell3.setBorder(0);
        tablenew.addCell(cell1);
        tablenew.addCell(cell2);
        tablenew.addCell(cell3);
        tablenew.addCell(cell4);
        tablenew.addCell(cell5);

        try {
            document.add(tablenew);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }


    public void botheadertwo(String first, String second, Document document) {

        BaseFont baseFont = null;
        try {
            baseFont = BaseFont.createFont("res/font/robotolight.ttf", "UTF-8", BaseFont.EMBEDDED);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        float[] columnWidths = {2, 7};
        PdfPTable tablenew = new PdfPTable(columnWidths);
        tablenew.setWidthPercentage(95);
        tablenew.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        PdfPCell cell1 = new PdfPCell(new Paragraph("Cell 1"));
        PdfPCell cell2 = new PdfPCell(new Paragraph("Cell 2"));

        Font boldfont = new Font(baseFont, 15, Font.NORMAL, BaseColor.BLACK);


        cell1 = new PdfPCell(new Paragraph(first, boldfont));
        cell1.setBorder(0);
        cell2 = new PdfPCell(new Paragraph(second, boldfont));
        cell2.setBorder(0);

        tablenew.addCell(cell1);
        tablenew.addCell(cell2);

        try {
            document.add(tablenew);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}

