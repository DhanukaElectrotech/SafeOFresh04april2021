package com.dhanuka.morningparcel.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dhanuka.morningparcel.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.filter.entity.ImageFile;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
import com.dhanuka.morningparcel.AppController;
import com.dhanuka.morningparcel.Helper.CartOrderDAO;
import com.dhanuka.morningparcel.Helper.DBCartDataUpload;
import com.dhanuka.morningparcel.Helper.DayHelper;
import com.dhanuka.morningparcel.Helper.DbImageMaster;
import com.dhanuka.morningparcel.Helper.ImageMasterDAO;
import com.dhanuka.morningparcel.Helper.ImageUploadDAO;
import com.dhanuka.morningparcel.Helper.ItemMasterhelper;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.Helper.Timehelper;
import com.dhanuka.morningparcel.InterfacePackage.Addressadd;
import com.dhanuka.morningparcel.InterfacePackage.CartItemsadd;
import com.dhanuka.morningparcel.InterfacePackage.TriggerClick;
import com.dhanuka.morningparcel.SqlDatabase.DatabaseManager;
import com.dhanuka.morningparcel.SqlDatabase.QueryExecutor;
import com.dhanuka.morningparcel.adapter.DeliveryHelper;
import com.dhanuka.morningparcel.adapter.Delivery_Option_Adapter;
import com.dhanuka.morningparcel.adapter.Payhelper;
import com.dhanuka.morningparcel.adapter.Payment_Option_Adapter;
import com.dhanuka.morningparcel.adapter.SelectedImagesAdapter;
import com.dhanuka.morningparcel.adapter.TimeSlotAdapter;
import com.dhanuka.morningparcel.adapter.WeeklyAdapter;
import com.dhanuka.morningparcel.beans.CartSendParams;
import com.dhanuka.morningparcel.beans.FancyToast;
import com.dhanuka.morningparcel.beans.OrderBean;
import com.dhanuka.morningparcel.restaurant.activity.AddLatLong;
import com.dhanuka.morningparcel.restaurant.adapters.SavedAdressadapter;
import com.dhanuka.morningparcel.restaurant.bean.GeoFenceBean;
import com.dhanuka.morningparcel.restaurant.bean.GeoFenceBeanMain;
import com.dhanuka.morningparcel.utils.DbImageUpload;
import com.dhanuka.morningparcel.utils.ImageUploadService;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.utils.OrderUploadService;
import com.dhanuka.morningparcel.utils.log;

import static com.vincent.filepicker.activity.BaseActivity.IS_NEED_FOLDER_LIST;
import static com.vincent.filepicker.activity.ImagePickActivity.IS_NEED_CAMERA;

public class Recurring_order extends AppCompatActivity implements CartItemsadd, Addressadd, TriggerClick {
    String catcodeid, catcodedesc, deliverycgarge;
    double PreviousAdjustedAmount = 0.0;
    ArrayList<String> mListLastRows = new ArrayList<>();
    GeoFenceBeanMain Mgeofencebeanmain;
    ArrayList<GeoFenceBean> geofencelist = new ArrayList<GeoFenceBean>();
    SavedAdressadapter savedAdressadapter;
    double dblPromoAmount = 0.0;
    ArrayList<DeliveryHelper> deliverylist, alldeliverylist;
    ArrayList<Payhelper> paylist = new ArrayList<Payhelper>();
    ArrayList<Timehelper> timelist = new ArrayList<Timehelper>();

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
String orderIdOld="";
    String TAG = "SAFEOBUDDYIMAGE";
    public static final int REQUEST_IMAGE = 100;


    String strDeliveryType = "";
    String strTimeSlot = "";
    String orderId = "";
    String strDate = "";
    String strTxnId = "";
    String strPaymentMode = "";
    double dblDeliveryCharges = 0.0;
    double dblWalletAmount = 0.0;
    double dblProcessingCharges = 0.0;
    double dblTax = 0.0;
    double dblBasePrice = 0.0;
    String startDate = "";
    ArrayList<String> mListSelectedIamges = new ArrayList<>();


    int delivertType = 2;
    String deliveryval, payval, timeslotval, delextime;
    Preferencehelper preferencehelper;
    RadioGroup deliverytyper, paytyper;
    LinearLayout deliveryaddll;
    TextView txtmSavings, txttotalitems;
    RecyclerView deliveryrecyc, payrecyc, timerecyc;
    Button selectcartbtn;
    ImageView pickimagebtn;
    EditText contacttxt, comment;
    TextView chooseadd;
    TextView datedelivery, deliveryaddval, txtPaybleAm1, txtPaybleAmt, txtCartAmt, txtSubtotal, txtPreAmt, txtTax1, txtTax, txtNote, txtDlvryAmt, txtSrvcAmt;
    LinearLayout deliveryll;
    LinearLayout dlvryL, walletL, taxL;
    ImageView imgBackBtn;
    double mSerAmt = 0.0;
    double mPreAmt = 0.0;
    double mDlvrAmt = 0.0;
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    SharedPreferences.Editor mEditor;


    String mImagePathDataBase, mImageNameDataBase, mCurrentTimeDataBase;
    int masterDataBaseId;
    String lastRowMaterTable;
    private boolean addedToMasterTable = false;
    RecyclerView rvImages;
    SelectedImagesAdapter mAdapter;


    @BindView(R.id.calendercontainer)
    RecyclerView calendercontainer;
    @BindView(R.id.recurimg)
    ImageView recurimg;
    @BindView(R.id.recurpname)
    TextView recurpname;
    @BindView(R.id.recurprice)
    TextView recurprice;
    DayHelper dayHelper;
    ArrayList<DayHelper> daylist;
    String[] arr = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    ItemMasterhelper mData;
    WeeklyAdapter weeklyAdapter;
    SharedPreferences mData1;
    Dialog PayDialog, CameraDialog;
    String type;

    public void makeBackClick(View view) {
        onBackPressed();
    }

    Context context;
    String currency = "";
    String date_of_installation = "";
    String myMonth, myDay;
    private int mYear, mMonth, mDay;
    OrderBean.OrderItemsBean orderItemsBean;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.llButtons)
    LinearLayout llButtons;
  @BindView(R.id.btnCancel)
    Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recurring_order);
        ButterKnife.bind(this);
        preferencehelper = new Preferencehelper(getApplicationContext());

        mData1 = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);
        mEditor = mData1.edit();

        context = Recurring_order.this;
        SharedPreferences prefs1 = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);


        if (prefs1.getString("Currency", "INR").equalsIgnoreCase("INR")) {
            currency = getResources().getString(R.string.rupee);
        } else {
            currency = "$";
        }
        type = getIntent().getStringExtra("type");
        if (getIntent().getStringExtra("type").equalsIgnoreCase("2")) {

            orderItemsBean = (OrderBean.OrderItemsBean) getIntent().getSerializableExtra("mData");

            calendercontainer.setHasFixedSize(true);
            calendercontainer.setLayoutManager(new GridLayoutManager(getApplicationContext(), 7));
            daylist = new ArrayList<>();
            recurpname.setText(orderItemsBean.getItemDescription());

            recurprice.setText(currency + "" + new DecimalFormat("##.##").format(Double.parseDouble(orderItemsBean.getRate())));
            Glide.with(this)
                    .load("http://mmthinkbiz.com/ImageHandler.ashx?image=" + orderItemsBean.getFileName() + "&filePath=" + orderItemsBean.getFilepath())
                    .apply(new RequestOptions().placeholder(R.drawable.no_image).error(R.drawable.no_image))
                    .into(recurimg);


            String realvalsplit[] = orderItemsBean.getVal1().split(";");


            for (int i = 0; i < realvalsplit.length; i++) {
                String daycountsplit[] = realvalsplit[i].split("//");
                dayHelper = new DayHelper();

                dayHelper.setDayname(arr[i]);
                dayHelper.setDayid((i + 1) + "");
                dayHelper.setAddval(daycountsplit[1]);
                daylist.add(dayHelper);


            }
            orderIdOld=getIntent().getStringExtra("orderId");
            if (!getIntent().getStringExtra("status").equalsIgnoreCase("91")) {
                llButtons.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.VISIBLE);
            } else {
                llButtons.setVisibility(View.GONE);

            }
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelOrder(orderIdOld, "1", "91");
                }
            });
            weeklyAdapter = new WeeklyAdapter(getApplicationContext(), daylist);
            calendercontainer.setAdapter(weeklyAdapter);
        } else {

            mData = (ItemMasterhelper) getIntent().getSerializableExtra("mData");
            calendercontainer.setHasFixedSize(true);
            calendercontainer.setLayoutManager(new GridLayoutManager(getApplicationContext(), 7));
            daylist = new ArrayList<>();
            recurpname.setText(mData.getItemName());

            recurprice.setText(currency + "" + new DecimalFormat("##.##").format(Double.parseDouble(mData.getSaleRate())));
            Glide.with(this)
                    .load("http://mmthinkbiz.com/ImageHandler.ashx?image=" + mData.getFileName() + "&filePath=" + mData.getFilepath())
                    .apply(new RequestOptions().placeholder(R.drawable.no_image).error(R.drawable.no_image))
                    .into(recurimg);

            for (int i = 0; i < 7; i++) {
                dayHelper = new DayHelper();

                dayHelper.setDayname(arr[i]);
                dayHelper.setDayid((i + 1) + "");
                dayHelper.setAddval("0");
                daylist.add(dayHelper);


            }


            weeklyAdapter = new WeeklyAdapter(getApplicationContext(), daylist);
            calendercontainer.setAdapter(weeklyAdapter);
            setUpDialogS();
        }


    }

    public void setUpDialogS() {

        PayDialog = new Dialog(Recurring_order.this, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        PayDialog.setContentView(R.layout.dialog_checkout_recurring);

        deliverytyper = PayDialog.findViewById(R.id.deliverytype);
        deliveryaddll = PayDialog.findViewById(R.id.deliveryaddchange);
        paytyper = PayDialog.findViewById(R.id.paymenttype);

        txttotalitems = PayDialog.findViewById(R.id.txttotalitems);
        txtmSavings = PayDialog.findViewById(R.id.txtmSavings);

        selectcartbtn = PayDialog.findViewById(R.id.btnsummit);
        contacttxt = PayDialog.findViewById(R.id.contactno);
        comment = PayDialog.findViewById(R.id.comment);
        deliveryrecyc = PayDialog.findViewById(R.id.delivreycontainer);
        payrecyc = PayDialog.findViewById(R.id.paycontainer);
        timerecyc = PayDialog.findViewById(R.id.timecontainer);

        chooseadd = PayDialog.findViewById(R.id.chooseadd);

        chooseadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseadd.getText().toString().equalsIgnoreCase("change")) {
                    showaddressselect();
                    if (deliveryaddval.getText().toString().equalsIgnoreCase("No address availalble")) {
                        deliveryaddval.setText(deliveryaddval.getText().toString());
                    }

                } else if (chooseadd.getText().toString().equalsIgnoreCase("add")) {
                    startActivity(new Intent(getApplicationContext(), AddLatLong.class).putExtra("type", "5"));
                }


                //   startActivity(new Intent(getApplicationContext(),SavedAddress.class).putExtra("apptype","2"));
            }
        });


        datedelivery = PayDialog.findViewById(R.id.dateselect);
        pickimagebtn = PayDialog.findViewById(R.id.imagepick);
        rvImages = PayDialog.findViewById(R.id.rvImages);
        dlvryL = (LinearLayout) PayDialog.findViewById(R.id.dlvryL);
        walletL = (LinearLayout) PayDialog.findViewById(R.id.walletL);
        taxL = (LinearLayout) PayDialog.findViewById(R.id.taxL);
        txtPaybleAm1 = (TextView) PayDialog.findViewById(R.id.txtPaybleAm1);
        txtPaybleAmt = (TextView) PayDialog.findViewById(R.id.txtPaybleAm);
        txtCartAmt = (TextView) PayDialog.findViewById(R.id.txtCartAmt);
        txtSubtotal = (TextView) PayDialog.findViewById(R.id.txtSubtotal);
        txtPreAmt = (TextView) PayDialog.findViewById(R.id.txtPreAmt);
        txtTax1 = (TextView) PayDialog.findViewById(R.id.txtTax1);
        txtTax = (TextView) PayDialog.findViewById(R.id.txtTax);
        txtNote = (TextView) PayDialog.findViewById(R.id.txtNote);
        txtDlvryAmt = (TextView) PayDialog.findViewById(R.id.txtDlvryAmt);
        txtSrvcAmt = (TextView) PayDialog.findViewById(R.id.txtSrvcAmt);
        imgBackBtn = (ImageView) PayDialog.findViewById(R.id.imgBackBtn);
        deliveryaddval = (TextView) PayDialog.findViewById(R.id.deliveryaddval);
        deliveryll = PayDialog.findViewById(R.id.deliveryll);
        deliveryaddval.setText("Choose Delivery address");


        if (TextUtils.isEmpty(getIntent().getStringExtra("addrstr"))) {
            String deladd = getIntent().getStringExtra("addrstr");
            deliveryaddval.setText(deladd);

        } else {
            showaddresslist();
        }
        imgBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayDialog.dismiss();
            }
        });
        txtNote.setText(mData1.getString("CheckOutMessage", " - "));
        mSerAmt = Double.parseDouble(df2.format(Double.parseDouble(mData1.getString("ServiceFees", "0.0"))));


        pickimagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  openCamera();


                CameraDialog.show();

            }
        });
        contacttxt.setText(preferencehelper.getPREFS_phoneno());
        selectcartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (contacttxt.getText().toString().isEmpty()) {
                    FancyToast.makeText(Recurring_order.this, "Enter phone number.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

                } else if (deliveryaddval.getText().toString().equalsIgnoreCase("")) {
                    FancyToast.makeText(Recurring_order.this, "Please Select address in order to check out", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

                } else if (contacttxt.getText().toString().length() < 10) {
                    FancyToast.makeText(Recurring_order.this, "Enter valid phone number.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

                } else {

                    timeslotval =/*datedelivery.getText().toString()+" "+*/timeslotval;
                    int selectedId = deliverytyper.getCheckedRadioButtonId();

                    int selectedIdpay = paytyper.getCheckedRadioButtonId();
                    if (selectedId == R.id.homedelivr) {


                    } else if (selectedId == R.id.loby) {
                        delivertType = 1;
                    } else {
                        delivertType = 0;
                    }//1111111111 123456

                    // if (mPayhelper.getStringpaytype().equalsIgnoreCase("ONLINE PAYMENT-1")) {
                    if (mPayhelper.getStringpaytype().equalsIgnoreCase("ONLINE PAYMENT")) {
                        amt = mTotalPrice - mPreAmt + "";
                        amt = df2.format(mTotalPrice - mPreAmt + mDlvrAmt + Double.parseDouble(mData1.getString("ServiceFees", "0.0")));
                        mEditor.putString("mAmount", amt + "");
                        mEditor.commit();
                        //  startActivityForResult(getDropInRequest().getIntent(Recurring_order.this), DROP_IN_REQUEST);

                        String payableamount = txtPaybleAmt.getText().toString().replace("$", "").replace("₹", "");
                        if (currency.equalsIgnoreCase("$")) {
                            if (strTimeSlot == null || strTimeSlot.equalsIgnoreCase("")) {
                                FancyToast.makeText(Recurring_order.this, "time slot is not select or either there is no time slot available for this delivery", FancyToast.LENGTH_LONG, FancyToast.INFO, false).show();
                            } else {
                          /*  if (Double.parseDouble(txtPaybleAmt.getText().toString().replace(currency, "")) > 1) {
                                startActivity(new Intent(Recurring_order.this, CreateCardPaymentMethodActivity.class).putExtra("price", new DecimalFormat("##.##").format(Double.parseDouble(txtPaybleAmt.getText().toString().replace(currency, "")))));
                            } else {*/
                                strTxnId = "";
                                mTXNID = "";
                                checkoutOrder();
                                //   }
                            }
                        } else {
                            loadProfileData();
                        }
                        //  dialogPayment.show();
                    } else {
                        if (currency.equalsIgnoreCase("$")) {
                            strTxnId = "";
                            mTXNID = "";
                            checkoutOrder();
                        } else {
                            loadProfileData();
                        }

                    }

                }
            }
        });

        if (mData1.getString("Currency", "INR").equalsIgnoreCase("INR")) {
            currency = getResources().getString(R.string.rupee);
        } else {
            currency = "$";
        }
        final Calendar c = Calendar.getInstance();
        c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH) + 1;
        mDay = c.get(Calendar.DAY_OF_MONTH);
        if (String.valueOf(mMonth).length() == 1) {
            myMonth = "0" + mMonth;
        } else {
            myMonth = String.valueOf(mMonth);

        }
        if (String.valueOf(mDay).length() == 1) {
            myDay = "0" + mDay;

        } else {
            myDay = String.valueOf(mDay);
        }


        if (datedelivery.getText().toString().isEmpty()) {

            date_of_installation = myMonth + "/" + myDay + "/" + mYear;

            datedelivery.setText(date_of_installation);
        }

        CameraDialog = new Dialog(Recurring_order.this);
        CameraDialog.setContentView(R.layout.popup_image_select);
        payrecyc.setLayoutManager(new LinearLayoutManager(Recurring_order.this));
        deliveryrecyc.setLayoutManager(new LinearLayoutManager(Recurring_order.this));
        payrecyc.setHasFixedSize(true);
        deliveryrecyc.setHasFixedSize(true);
        timerecyc.setLayoutManager(new LinearLayoutManager(Recurring_order.this));
        timerecyc.setHasFixedSize(true);

        ImageView camerabtn = CameraDialog.findViewById(R.id.camerabtn);
        ImageView gallerybtn = CameraDialog.findViewById(R.id.gallerybtn);
        rvImages.setLayoutManager(new LinearLayoutManager(Recurring_order.this, RecyclerView.HORIZONTAL, false));
        mAdapter = new SelectedImagesAdapter(this, mListSelectedIamges);
        rvImages.setAdapter(mAdapter);

        gallerybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(Recurring_order.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Recurring_order.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                } else {
                    Intent intent1 = new Intent(Recurring_order.this, ImagePickActivity.class);
                    intent1.putExtra(IS_NEED_CAMERA, true);
                    intent1.putExtra(com.vincent.filepicker.Constant.MAX_NUMBER, 1);
                    intent1.putExtra(IS_NEED_FOLDER_LIST, true);
                    startActivityForResult(intent1, com.vincent.filepicker.Constant.REQUEST_CODE_PICK_IMAGE);

                    //   launchGalleryIntent();
                    /*          Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, 66);
          */
                    CameraDialog.dismiss();
                }

            }
        });
        datedelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!startDate.isEmpty()) {
                    try {
                        String strSplit[] = startDate.split("/");
                        mDay = Integer.parseInt(strSplit[0]);
                        mMonth = Integer.parseInt(strSplit[1]);
                        mYear = Integer.parseInt(strSplit[2]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    panelcleandatepicker();
                } else {
                    getDate(mDeliveryHelper.getDeliverytype());
                }
            }
        });
        camerabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Recurring_order.this, ImagePickActivity.class);
                intent1.putExtra(IS_NEED_CAMERA, true);
                intent1.putExtra(com.vincent.filepicker.Constant.MAX_NUMBER, 1);
                intent1.putExtra(IS_NEED_FOLDER_LIST, false);
                startActivityForResult(intent1, com.vincent.filepicker.Constant.REQUEST_CODE_TAKE_IMAGE);

                //  openCamera();
                //     launchCameraIntent();
                CameraDialog.dismiss();
            }
        });

        txtmSavings.setText("");
        txtCartAmt.setText("");
        txtSrvcAmt.setText("");
        txtTax.setText("");
        txtTax1.setText("");
        txtSubtotal.setText("");
        txtPreAmt.setText("");
        txtPaybleAmt.setText("");

        showaddresslist();


    }

    /*
    0//12;1//1;2//21;3//33;4//34;5//45;6//56;7//77
    (Day1//Quantity1;Day2//Quantity2;Day3//Quantity3;Day4//Quantity4;Day5//Quantity5;Day6//Quantity6;Day7//Quantity7)
     Day 0 for Sunday,1 for Monday,2 for Tuesday
    */
    int qty = 0;
    double dblAmt = 0.0;
    List<DayHelper> mListOrder = new ArrayList<>();
    String strParams = "";

    public void makeOrder(View view) {

        if (type.equalsIgnoreCase("2")) {
            updateOrderStatus(orderItemsBean);
        } else {
            dblAmt = 0.0;
            qty = 0;
            strParams = "";
            int isValid = 0;
            mListOrder = weeklyAdapter.makeOrder();
            for (int a = 0; a < mListOrder.size(); a++) {
                if (Integer.parseInt(mListOrder.get(a).getAddval()) > 0) {
                    isValid = 1;
                    qty = Integer.parseInt(mListOrder.get(a).getAddval());
                    dblAmt =/*(Integer.parseInt(mListOrder.get(a).getAddval()) * */Double.parseDouble(mData.getSaleRate())/*)*/;

                }

                // qty = qty + Integer.parseInt(mListOrder.get(a).getAddval());
                //   dblAmt = dblAmt + (Integer.parseInt(mListOrder.get(a).getAddval()) * Double.parseDouble(mData.getSaleRate()));
                if (strParams.isEmpty()) {
                    strParams = a + "//" + mListOrder.get(a).getAddval();
                } else {
                    strParams = strParams + ";" + a + "//" + mListOrder.get(a).getAddval();

                }
            }
            if (isValid == 1) {
                getpaylist(deliveryval);
                //getTimeslot();
                getdeliverylist();
                calculateTotal();
                PayDialog.show();

            } else {
                FancyToast.makeText(Recurring_order.this, "Order can not be proceed as per all days have less than 1 Qty. of Item", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();

            }

        }


    }

    String strItems = "";

    public void checkoutOrder() {


        strItems = "";
        ArrayList<CartSendParams> arrayList = new ArrayList<>();

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        //    for (int checkoutTime = 0; checkoutTime < mArrayList.size(); checkoutTime++) {
        String finalprice = "0";
        try {
            if (!mData.getMRP().isEmpty()) {
                if (Float.parseFloat(mData.getMRP()) > Float.parseFloat(mData.getSaleRate())) {
                    finalprice = "" + (Float.parseFloat(mData.getMRP()) - Float.parseFloat(mData.getSaleRate()));
                } else {
                    finalprice = "0";
                }
            } else {
                finalprice = "0";
            }
        } catch (Exception e) {
            finalprice = "0";
            e.printStackTrace();
        }
        try {
            jsonObject.put("ItemDesc", mData.getItemName());
            jsonObject.put("itemId", mData.getItemID() + "");
            //   jsonObject.put("CreatedBy", preferencehelper.getPrefsContactId());
            jsonObject.put("DetailID", "0");
            //   jsonObject.put("CID", preferencehelper.getPrefsContactId());
            jsonObject.put("OID", "0");
            jsonObject.put("Discount", finalprice);
            jsonObject.put("Type", "0");
            jsonObject.put("val1", strParams);
            jsonObject.put("Status", "0");
            jsonObject.put("sku", mData.getItemSKU() + "");
            jsonObject.put("RAmount", mData.getSaleRate() + "");
            jsonObject.put("Rate", mData.getSaleRate());
            jsonObject.put("barcode", mData.getItemBarcode());
            jsonObject.put("qty", qty + "");
            jsonObject.put("MRP", mData.getMRP());
        } catch (Exception e) {
            e.printStackTrace();
        }

        jsonArray.put(jsonObject);
        CartSendParams modle = new CartSendParams();
        modle.setItemDesc(mData.getItemName());
        modle.setItemId(mData.getItemID() + "");
//            modle.setCreatedBy(preferencehelper.getPrefsContactId());
        modle.setDetailID("0");
//            modle.setCID( preferencehelper.getPrefsContactId());
        modle.setOID("0");
        modle.setDiscount(finalprice);
        modle.setType("0");
        modle.setStatus("0");
        modle.setSku(mData.getItemSKU() + "");
        modle.setRAmount(mData.getSaleRate());
        modle.setRate(mData.getSaleRate());
        modle.setBarcode(mData.getItemBarcode());
        modle.setQty(qty + "");
        modle.setVal1(strParams + "");
        modle.setMRP(mData.getMRP());
        //  modle.setmDescription(finalparam);
        arrayList.add(modle);
        //  }
        strItems = jsonArray.toString();// new Gson().toJson(arrayList);
        Log.e("strItems", strItems);
        Log.e("strDattta", strItems);


        Log.e("dsfkjjfk", AppController.getValTypeFromDesc("order_master"));
        final ProgressDialog mProgressBar = new ProgressDialog(Recurring_order.this);
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
                            String responses = jkHelper.Decryptapi(response, Recurring_order.this);
                            Log.e("delivresponses123", responses);
                            JSONObject jsonObject = new JSONObject(responses);
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("returnds");
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                orderId = jsonObject1.getString("OrderID");
                                //  if (!mDeliveryHelper.getDeliverytype().equalsIgnoreCase("CASH ON DELIVERY")) {
                                //     String strrr = "" + (dblWalletAmount - (Double.parseDouble(txtTax1.getText().toString().replace(currency, "")) + Double.parseDouble(txtSubtotal.getText().toString().replace(currency, ""))));
                                //  Log.e("strrrstrrr", strrr);
                                //   preferencehelper.setPREFS_currentbal(strrr);
                                //  Utility.addMoneyToWallet(Recurring_order.this, PreviousAdjustedAmount + "", orderId, "2");
                                //  }

                                if (orderId != null) {
                                    if (mListLastRows.size() > 0) {

                                        updateserverphotoid();
                                        uploadimage();
                                    }
                                }

                                ArrayList<DBCartDataUpload> arrayList = new ArrayList<>();


                                FancyToast.makeText(Recurring_order.this, "Order Created Successfully.", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();

                                startActivity(new Intent(Recurring_order.this, OrderSuccessActivity.class).putExtra("orderId", orderId).putExtra("totalamount", txtPaybleAm1.getText().toString()));
                                finish();
                                //  checkoutOrderDetails();
//}

                            } else {

                                FancyToast.makeText(Recurring_order.this, "No Data Found, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
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
                        FancyToast.makeText(Recurring_order.this, "Something went wrong with checkout.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


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
                prefs = new Preferencehelper(Recurring_order.this);
                Map<String, String> params = new HashMap<String, String>();
                String tax, discount, servicecharge;

                String serviceFees = mData1.getString("ServiceFees", "");
                String discountt = mData1.getString("discount", "");

                try {
                    DecimalFormat formey = new DecimalFormat("0.00");


                    String param = getString(R.string.CREATE_MASTER_ORDER) + "&OID=" + "0" + "&CID=" + prefs.getPrefsContactId()
                            + "&CustID=" + prefs.getPrefsContactId() + "&CompID=" + mData1.getString("shopId", "1") + "&CreatedBy=" + prefs.getPrefsContactId()
                            + "&RItemCount=" + qty + "&RAmount=" + dblAmt + ""
                            + "&Status=" + "0" + "&OType=" + "RO" + "&OrderDate=" + mTodayDate + "&Type=" + "0" + "&RandNumber=" + "0" + "&DeliveryMode=" + deliveryval + ""
                            + "&PaymentMode=" + payval + "" + "&PhoneforOTP=" + contacttxt.getText().toString() + "" + "&Comment=" + comment.getText().toString().replace("\n", "") +
                            "" + "&TimeSlotDate=" + datedelivery.getText().toString() + "&TimeSLotTime=" + strTimeSlot + "&PreviousAdjustedAmount=" + PreviousAdjustedAmount + "&DeliveryCharge=" +
                            dblDeliveryCharges + "&CurrentPaidAmount=" + txtPaybleAm1.getText().toString().replace(currency, "") + "&PaymentTxnId=" + strTxnId +
                            "&Tax=" + txtTax1.getText().toString().replace("$", "").replace("₹", "").trim() + "&Discount=" + discountt + "&ServiceCharge=" + serviceFees +
                            "&Address=" + deliveryaddval.getText().toString() + "&PaymentType=" + strPaymentMode + "&EntryFrom=2" + "&PromoCode=" + "&items=" + strItems;
                    ;
                    Log.d("BeforeencrptionMaster", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, Recurring_order.this);
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

        Volley.newRequestQueue(this).add(postRequest);


    }

    String strCrncy;
    Double mTotalPrice = 0.00;
    Double mSavings = 0.00;

    public void calculateTotal() {
        strCrncy = context.getResources().getString(R.string.rupee);
        if (mData1.getString("Currency", "INR").equalsIgnoreCase("INR")) {
            strCrncy = context.getResources().getString(R.string.rupee);
        } else {
            strCrncy = "$";
        }
        //  mArrayList = mArrayListCart;
        // mCount = mArrayListCart.size();
        //Log.e("mHHH2", "mHEEE" + mArrayListCart.size());


        mTotalPrice = 0.0;
        mSavings = 0.0;


        for (int a = 0; a < mListOrder.size(); a++) {
//            Log.e("Iammrp" , mArrayListCart.get(a).getMRP());
            mTotalPrice = mTotalPrice + (Float.parseFloat(mData.getSaleRate()) * Integer.parseInt(mListOrder.get(a).getAddval()));
            try {
                if (mData.getMRP() != null) {
                    if (Float.parseFloat(mData.getMRP()) > Float.parseFloat(mData.getSaleRate())) {
                        mSavings = mSavings + ((Float.parseFloat(mData.getMRP()) - Float.parseFloat(mData.getSaleRate())) * Integer.parseInt(mListOrder.get(a).getAddval()));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
//mSavings
        txtmSavings.setText(currency + (new DecimalFormat("##.##").format(mSavings)));
        //  txtGrandTotal.setText(currency + (new DecimalFormat("##.##").format(mTotalPrice)));


        txtCartAmt.setText(currency + "" + df2.format(mTotalPrice));
        txtSrvcAmt.setText(currency + "" + df2.format(mSerAmt));

        double mTax = 0.0;
        mTax = Double.parseDouble(mData1.getString("Tax", "10.0"));
        //  mTax=10.0;
        txtTax.setText("Tax (" + df2.format(mTax) + "%)");
        if (mTax > 0) {
            //  taxL.setVisibility(View.VISIBLE);
        } else {
            //  taxL.setVisibility(View.GONE);
        }
        mTax = ((mTotalPrice + mDlvrAmt + Double.parseDouble(mData1.getString("ServiceFees", "0.0"))) * mTax) / 100;
        txtTax1.setText(strCrncy + df2.format(mTax));
        mTaxmTax = mTax;

        txtSubtotal.setText(strCrncy + "" + df2.format(mTotalPrice + mDlvrAmt + Double.parseDouble(mData1.getString("ServiceFees", "0.0"))));

        if (mPreAmt > 0) {
            txtPreAmt.setText(currency + "" + df2.format(mPreAmt).replace("-", "") + "");
            txtPaybleAmt.setText(strCrncy + "" + df2.format(mTax + mTotalPrice + mDlvrAmt + Double.parseDouble(mData1.getString("ServiceFees", "0.0")) - mPreAmt));
            amt = df2.format(mTax + mTotalPrice + mDlvrAmt + Double.parseDouble(mData1.getString("ServiceFees", "0.0")) - mPreAmt);
        } else {
            txtPreAmt.setText("-" + currency + "" + df2.format(mPreAmt).replace("-", "") + "");
            txtPaybleAmt.setText(strCrncy + "" + df2.format(mTax + mTotalPrice + mPreAmt + mDlvrAmt + Double.parseDouble(mData1.getString("ServiceFees", "0.0"))));
            amt = df2.format(mTax + mTotalPrice + mPreAmt + mDlvrAmt + Double.parseDouble(mData1.getString("ServiceFees", "0.0")));

        }

//        amt = df2.format(mTotalPrice - mPreAmt);
        mEditor.putString("mAmount", amt + "");
        mEditor.commit();


    }

    BottomSheetDialog dialogbottom;
    Button savebutton;
    RecyclerView savedrecysheet;
    TextView addnewadd;
    TextView selectlocation, selectdelivtxt;

    public void showaddressselect() {
        delivertType = 2;

        View view = getLayoutInflater().inflate(R.layout.bottomsheet_addressselection, null);
        dialogbottom = new BottomSheetDialog(this, R.style.BottomSheetDialog); // Style here
        dialogbottom.setContentView(view);
        savedrecysheet = dialogbottom.findViewById(R.id.saveaddcontainer);
        savebutton = dialogbottom.findViewById(R.id.btn_submit);
        addnewadd = dialogbottom.findViewById(R.id.addnewadd);

        selectdelivtxt = dialogbottom.findViewById(R.id.selectdelivtxt);
        selectlocation = dialogbottom.findViewById(R.id.selectlocation);

        Typeface boldfont = Typeface.createFromAsset(
                getApplicationContext().getAssets(),
                "fonts/Roboto-Bold.ttf");
        selectdelivtxt.setTypeface(boldfont);
        addnewadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddLatLong.class).putExtra("type", "5"));

            }
        });
        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        selectlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Recurring_order.this, AddLatLong.class).putExtra("type", "1").putExtra("tripdids", "").setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));

            }
        });
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        savedrecysheet.setHasFixedSize(true);
        savedrecysheet.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        showaddresslist();
        dialogbottom.show();
    }

    public void showaddresslist() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        final ProgressDialog mProgressBar = new ProgressDialog(Recurring_order.this);
        mProgressBar.setTitle("SafeOBuddy");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("responsive", response);
                        try {
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, Recurring_order.this);
                            mProgressBar.dismiss();

                            GeoFenceBeanMain mgeofencebeanmain = new Gson().fromJson(responses, GeoFenceBeanMain.class);
                            Mgeofencebeanmain = mgeofencebeanmain;


                            if (mgeofencebeanmain.getStrSuccess() == 1) {
                                geofencelist = mgeofencebeanmain.getStrreturns();

//                                String splithome[] = adressact.getStrdescription().split(":");
//                                viewHolder.completeaddress.setText(splithome[0]);
                                deliveryaddval.setText(geofencelist.get(0).getStrdescription());
                                savedAdressadapter = new SavedAdressadapter(getApplicationContext(), geofencelist, 2, (TriggerClick) Recurring_order.this);
                                savedrecysheet.setAdapter(savedAdressadapter);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                    }
                }
                ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressBar.dismiss();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params1 = new HashMap<String, String>();
                Preferencehelper prefs = new Preferencehelper(getApplicationContext());


                try {

                    String param = getString(R.string.URL_GEOFENCE) + "&ClientID=" + prefs.getPrefsContactId();
                    Log.e("beforeenc", param);
                    JKHelper jkHelper = new JKHelper();
                    param = jkHelper.Encryptapi(param, Recurring_order.this);
                    params1.put("val", param);
                    Log.e("afterenc", param);
                    return params1;

                } catch (Exception e) {
                    e.printStackTrace();
                    return params1;
                }

            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                10 * 60 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(Recurring_order.this).add(postRequest);


    }

    public void ErrorMsg(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Recurring_order.this);
        builder.setMessage(msg);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }


    private String PREFS_FILE_PATH = "capture_file_path";
    private final int REQUEST_CODE = 1010;

    private String filePath;
    SharedPreferences prefs11;

    public void openCamera() {
        SharedPreferences prefs;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        filePath = getOutputMediaFileUri(); // create a file to save the image
        prefs = PreferenceManager.getDefaultSharedPreferences(Recurring_order.this);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {


            ArrayList<String> mImages = new ArrayList<>();
            if (requestCode == com.vincent.filepicker.Constant.REQUEST_CODE_TAKE_IMAGE) {
                log.e("request code true  ");
                ArrayList<ImageFile> list = data.getParcelableArrayListExtra(com.vincent.filepicker.Constant.RESULT_PICK_IMAGE);
                StringBuilder builder = new StringBuilder();
                for (ImageFile file : list) {
                    String path = file.getPath();
                    mFile1 = new File(path);
                    builder.append(path + "\n");
                    mImages.add(path);
                    mAdapter.addItems(mImages);
                    log.e("file path in request code true==" + filePath);
                    //  if (!mBean.getStrrmasterid().isEmpty()) {
                    new ImageCompressionAsyncTask(true).execute(path);
                }
                //   }
            }

            Bitmap bitmap = null;

            if (requestCode == com.vincent.filepicker.Constant.REQUEST_CODE_PICK_IMAGE) {
                log.e("request code true  ");
                ArrayList<ImageFile> list = data.getParcelableArrayListExtra(com.vincent.filepicker.Constant.RESULT_PICK_IMAGE);
                StringBuilder builder = new StringBuilder();
                for (ImageFile file : list) {
                    String path = file.getPath();
                    mFile1 = new File(path);
                    builder.append(path + "\n");
                    mImages.add(path);
                    mAdapter.addItems(mImages);
                    log.e("file path in request code true==" + filePath);
                    //  if (!mBean.getStrrmasterid().isEmpty()) {
                    new ImageCompressionAsyncTask(true).execute(path);
                }
                //   }
            }
            if (requestCode == 5566) {
                try {
                    String strWalletAmount = data.getStringExtra("mWalletAmount");
                    dblWalletAmount = Double.parseDouble(preferencehelper.getPREFS_currentbal());
                    calculateTotalAmount();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (requestCode == 8766) {
                mTXNID = data.getStringExtra("transactionId");
                Log.e("transactionIdddd", mTXNID);
                if (!mTXNID.equalsIgnoreCase("failed")) {

                    logdata();
                    checkoutOrder();
                } else {

                }
            }
          /*  if (requestCode == DROP_IN_REQUEST) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                Log.e("SDA", result.getPaymentMethodType().getCanonicalName());
                displayNonce(result.getPaymentMethodNonce(), result.getDeviceData());
            }*/
        } else if (resultCode != RESULT_CANCELED) {
            //  showDialog(((Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR)).getMessage());
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

    //txtPreAmt
//    txttotalitems
    double str1 = 0.0;
    double str2 = 0.0;

    double mTaxmTax = 0.0;

    @Override
    public void sendtimeslotval(String res, String deliveryrate) {
        timeslotval = res;
        delextime = deliveryrate;


        str2 = Double.parseDouble(deliveryrate);
        String strr = txtSubtotal.getText().toString();
        String strr1 = txtPaybleAmt.getText().toString();
        Log.e("strrstrr", strr + " " + strr);
        try {
            double newSubT = Double.parseDouble(strr.replace("₹", "").replace("$", "")) - str1;
            double newPT = Double.parseDouble(strr.replace("₹", "").replace("$", "")) - str1;
            Log.e("NYUGU", "before = " + (newSubT) + " " + (newPT) + "\n after = " + (newSubT + str2) + " " + (newPT + str2));
            txtSubtotal.setText(strCrncy + "" + (newSubT + str2));
            txtPaybleAmt.setText(strCrncy + "" + (newPT + str2 + mTaxmTax));
            str1 = str2;
        } catch (Exception e) {
            e.printStackTrace();
        }
        txtDlvryAmt.setText(currency + "" + deliveryrate);
        // Toast.makeText(getApplicationContext(), deliveryrate, Toast.LENGTH_LONG).show();
        strTimeSlot = timeslotval;
        dblDeliveryCharges = Double.parseDouble(deliveryrate);
        dblWalletAmount = 0.0;
        calculateTotalAmount();

    }

    Payhelper mPayhelper;
    String amt = "0.00";
    static final String EXTRA_PAYMENT_RESULT = "payment_result";
    static final String EXTRA_DEVICE_DATA = "device_data";
    static final String EXTRA_COLLECT_DEVICE_DATA = "collect_device_data";
    private static final String KEY_NONCE = "nonce";
    private static final int DROP_IN_REQUEST = 9782;
    /*
        private PaymentMethodNonce mNonce;
    */
    String mTXNID = "";
    String payValue = "ONLINE PAYMENT";

    @Override
    public void sendpayval(Payhelper res) {
        mPayhelper = res;
        payValue = mPayhelper.getStringpaytype();
        payval = mPayhelper.getPayid();
        if (!mPayhelper.getStringpaytype().equalsIgnoreCase("CASH ON DELIVERY")) {
            try {
                mPreAmt = Double.parseDouble(preferencehelper.getPREFS_currentbal());
            } catch (Exception e) {
                mPreAmt = 0.0;
            }
            deliveryaddll.setVisibility(View.VISIBLE);
            //   walletL.setVisibility(View.VISIBLE);
            if (payValue.equalsIgnoreCase("Online Payment")) {
                selectcartbtn.setText("Make Payment");

            } else {
                selectcartbtn.setText("CheckOut");
            }
        } else {

            mPreAmt = 0.0;
            //  walletL.setVisibility(View.GONE);
            selectcartbtn.setText("Submit Order");
            deliveryaddll.setVisibility(View.GONE);

        }
        strPaymentMode = mPayhelper.getStringpaytype();
        calculateTotalAmount();


    }

    public void panelcleandatepicker() {


        DatePickerDialog datePickerDialog = new DatePickerDialog(Recurring_order.this,
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
                        datedelivery.setText(date_of_installation);
                        strTimeSlot = "";
                        dblDeliveryCharges = 0.0;
                        dblWalletAmount = 0.0;
                        calculateTotalAmount();
                        getTimeslotNew();
                    }
                }, mYear, mMonth - 1, mDay);

        datePickerDialog.show();
        Calendar c = Calendar.getInstance();
        c.set(mYear, mMonth - 1, mDay);
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

    public void getDate(String dlrvytyp) {

        final ProgressDialog mProgressBar = new ProgressDialog(Recurring_order.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            mProgressBar.dismiss();
                            Log.d("responsivepaylist", response);
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, Recurring_order.this);
                            Log.e("responsestimeslot", responses);
                            JSONObject jsonObject = new JSONObject(responses);
                            if (jsonObject.getInt("success") == 1) {
                                JSONArray jsonArray = jsonObject.getJSONArray("returnmessage");
                                startDate = jsonArray.getJSONObject(0).getString("Slotdate");
                            } else {
                                startDate = "";
                            }
                            strDate = startDate;
                            getTimeslotNew();

                        } catch (Exception e) {
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
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                String mTodayDate = df.format(c.getTime());

                SharedPreferences mData = getSharedPreferences("MORNING_PARCEL_GROCERY",
                        MODE_PRIVATE);


                Preferencehelper prefs;
                prefs = new Preferencehelper(Recurring_order.this);
                Map<String, String> params = new HashMap<String, String>();


                try {

                    String param = getString(R.string.GET_DELIVERY_DATE) + "&storeId=" + mData1.getString("shopId", "1") + "&dtype=" + dlrvytyp;
                    Log.d("Beforeencrptionpay", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, Recurring_order.this);
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

        Volley.newRequestQueue(this).add(postRequest);


    }


    DeliveryHelper mDeliveryHelper;

    @Override
    public void senddeliveryval(DeliveryHelper res) {
        mDeliveryHelper = res;
        deliveryval = res.getDeliveryid();
        if (mDeliveryHelper.getDeliverytype().equalsIgnoreCase("HOME DELIVERY")) {
            //   dlvryL.setVisibility(View.VISIBLE);
            // deliveryaddll.setVisibility(View.VISIBLE);
            mDlvrAmt = Double.parseDouble(df2.format(Double.parseDouble(mData1.getString("DeliveryCharge", "0.0"))));
            txtSubtotal.setText(strCrncy + "" + df2.format(mTotalPrice + mDlvrAmt + Double.parseDouble(mData1.getString("ServiceFees", "0.0"))));
            // setData();
        } else {
            // dlvryL.setVisibility(View.GONE);
            mDlvrAmt = 0.0;
            // deliveryaddll.setVisibility(View.GONE);
        }
        strDeliveryType = mDeliveryHelper.getDeliverytype();
        strTimeSlot = "";
        strDate = "";
        dblDeliveryCharges = 0.0;
        dblWalletAmount = 0.0;
        calculateTotalAmount();
        getpaylist(res.getDeliveryid());
        getDate(mDeliveryHelper.getDeliverytype());

    }

    public void getpaylist(String delivyvalid) {

        Log.e("dsfkjjfk11", AppController.getValTypeFromDesc("order_master"));
        final ProgressDialog mProgressBar = new ProgressDialog(Recurring_order.this);
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
                            String responses = jkHelper.Decryptapi(response, Recurring_order.this);
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
                                        Payhelper payhelper = new Payhelper();
                                        payhelper.setPayid(catcodeid);
                                        payhelper.setStringpaytype(catcodedesc);
                                        paylist.add(payhelper);

                                    }

                                }
                                payrecyc.setAdapter(new Payment_Option_Adapter(Recurring_order.this, paylist, Recurring_order.this));

//}

                            } else {

                                FancyToast.makeText(Recurring_order.this, "No Data Found, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
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
                        FancyToast.makeText(Recurring_order.this, "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


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
                prefs = new Preferencehelper(Recurring_order.this);
                Map<String, String> params = new HashMap<String, String>();


                try {

                    String param = getString(R.string.URL_GET_CATEGORY_CODE) + "&type=" + "113" + "&contactid=" + prefs.getPrefsContactId() + "&supplierid=0";
                    Log.d("Beforeencrptionpay", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, Recurring_order.this);
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

        Volley.newRequestQueue(this).add(postRequest);


    }

    public void getdeliverylist() {

        Log.e("dsfkjjfk", AppController.getValTypeFromDesc("order_master"));
        final ProgressDialog mProgressBar = new ProgressDialog(Recurring_order.this);
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
                            String responses = jkHelper.Decryptapi(response, Recurring_order.this);
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

                                    if (jsonObject1.getString("Val1").equalsIgnoreCase(mData1.getString("shopId", "1"))) {
                                        catcodeid = jsonObject1.getString("CatCodeID");
                                        catcodedesc = jsonObject1.getString("CodeDescription");
                                        DeliveryHelper deliveryHelper = new DeliveryHelper();
                                        deliveryHelper.setDeliveryid(catcodeid);
                                        deliveryHelper.setDeliverytype(catcodedesc);
                                        deliverylist.add(deliveryHelper);
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


                                        }
                                    }


                                }


                                deliveryrecyc.setAdapter(new Delivery_Option_Adapter(Recurring_order.this, deliverylist, Recurring_order.this, Recurring_order.this));
                                PayDialog.show();
//}

                            } else {

                                FancyToast.makeText(Recurring_order.this, "No Data Found, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
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
                        FancyToast.makeText(Recurring_order.this, "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


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
                prefs = new Preferencehelper(Recurring_order.this);
                Map<String, String> params = new HashMap<String, String>();


                try {
                    String param = getString(R.string.URL_GET_CATEGORY_CODE) + "&type=" + "112" + "&contactid=" + prefs.getPrefsContactId() + "&supplierid=0";
                    Log.d("Beforeencrption", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, Recurring_order.this);
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

        Volley.newRequestQueue(this).add(postRequest);


    }

    @Override
    public void selectaddress(DeliveryHelper type) {

        if (type.getDeliverytype().equalsIgnoreCase("home delivery")) {
            //   deliveryaddval.setVisibility(View.VISIBLE);
            deliveryaddll.setVisibility(View.VISIBLE);
//            showaddressselect();
            if (deliveryaddval.getText().toString().equalsIgnoreCase("No address availalble")) ;
            {
                deliveryaddval.setText(deliveryaddval.getText().toString());
            }

        } else {
            //   deliveryaddval.setVisibility(View.GONE);
            deliveryaddll.setVisibility(View.GONE);
        }

    }


    @Override
    public void makeclick(String click, String lat, String longg) {
        dialogbottom.dismiss();
        if (TextUtils.isEmpty(preferencehelper.getPrefsZipCode())) {

            deliveryaddval.setText(click);

        } else {

            deliveryaddval.setText(click);
        }

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
                modle.setmDescription("order_master");
                modle.setmImageType("order_master");
                arrayList.add(modle);

                DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                    @Override
                    public void run(SQLiteDatabase database) {

                        ImageMasterDAO dao = new ImageMasterDAO(database, Recurring_order.this);
                        ArrayList<DbImageMaster> list = arrayList;
                        dao.insert(list);
                        addedToMasterTable = true;
                    }
                });
            }


            DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                @Override
                public void run(SQLiteDatabase database) {
                    ImageMasterDAO dao = new ImageMasterDAO(database, Recurring_order.this);
                    masterDataBaseId = dao.getlatestinsertedid();
                    lastRowMaterTable = String.valueOf(masterDataBaseId);
                    mListLastRows.add(lastRowMaterTable);
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
                    ImageUploadDAO dao = new ImageUploadDAO(database, Recurring_order.this);
                    ArrayList<DbImageUpload> list = arrayListUpload;
                    dao.insert(list);

                    log.e("photo inserted ");
                }
            });
            //    setPhotoCount();


        }
    }

    private void uploadimage() {
        if (JKHelper.isConnectedToNetwork(Recurring_order.this) && !JKHelper.isServiceRunning(Recurring_order.this, ImageUploadService.class)) {
            JKHelper.showLog("Start Service");
            startService(new Intent(Recurring_order.this, ImageUploadService.class));
        } else {
            stopService(new Intent(Recurring_order.this, ImageUploadService.class));
            startService(new Intent(Recurring_order.this, ImageUploadService.class));
        }
    }

    private void updateserverphotoid() {
        for (int a = 0; a < mListLastRows.size(); a++) {
            if (mListLastRows.get(a) != null) {
                SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
                ImageUploadDAO pd = new ImageUploadDAO(database, Recurring_order.this);
                pd.setWorkIdToTable(String.valueOf(orderId), mListLastRows.get(a));
                ImageMasterDAO pds = new ImageMasterDAO(database, Recurring_order.this);
                pds.setServerDetails(Integer.parseInt(mListLastRows.get(a)), Integer.valueOf(orderId), 0);
                ImageMasterDAO iDao = new ImageMasterDAO(database, getApplicationContext());
                String serverId = iDao.getServerIdById(1);
                DatabaseManager.getInstance().closeDatabase();
                log.e("in last row master table inserting  ");
            }


        }


    }

    private void addItemsToDB(ArrayList<DBCartDataUpload> arrayList) {


        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {

                CartOrderDAO dao = new CartOrderDAO(database, Recurring_order.this);
                ArrayList<DBCartDataUpload> list = arrayList;
                dao.insert(list);
                addedToMasterTable = true;
            }
        });


        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                CartOrderDAO dao = new CartOrderDAO(database, Recurring_order.this);
                log.e("item string id ============== " + dao.getlatestinsertedid());
            }
        });


    }

    private void uploadOrderItems() {
        if (JKHelper.isConnectedToNetwork(Recurring_order.this) && !JKHelper.isServiceRunning(Recurring_order.this, OrderUploadService.class)) {
            JKHelper.showLog("Start Service");
            startService(new Intent(Recurring_order.this, OrderUploadService.class));
        } else {
            stopService(new Intent(Recurring_order.this, OrderUploadService.class));
            startService(new Intent(Recurring_order.this, OrderUploadService.class));
        }
    }

    public void calculateTotalAmount() {
        if (mData1.getString("Currency", "INR").equalsIgnoreCase("INR")) {
            currency = getResources().getString(R.string.rupee);
        } else {
            currency = "$";
        }

        if (!strDeliveryType.equalsIgnoreCase("HOME DELIVERY")) {
            dblDeliveryCharges = 0.0;
        }
        if (!strPaymentMode.equalsIgnoreCase("CASH ON DELIVERY")) {
            try {
                dblWalletAmount = Double.parseDouble(preferencehelper.getPREFS_currentbal());
            } catch (Exception er) {
                dblWalletAmount = 0.0;

            }
        }
        try {
            dblProcessingCharges = Double.parseDouble(mData1.getString("ServiceFees", "0.0"));
        } catch (Exception er) {
            dblProcessingCharges = 0.0;

        }
        try {
            dblTax = Double.parseDouble(mData1.getString("Tax", "10.0"));
        } catch (Exception er) {
            dblTax = 0.0;

        }


        //   ArrayList<CartProduct> list = (ArrayList<CartProduct>) dbManager.getProducts();
        dblBasePrice = 0.0;

        dblBasePrice = (Float.parseFloat(mData.getSaleRate()) * qty);


        txtCartAmt.setText(currency + " " + new DecimalFormat("##.##").format(dblBasePrice));
        txtDlvryAmt.setText(currency + " " + new DecimalFormat("##.##").format(dblDeliveryCharges));
        txtSrvcAmt.setText(currency + " " + new DecimalFormat("##.##").format(dblProcessingCharges));
        txtTax1.setText(currency + " " + new DecimalFormat("##.##").format(dblProcessingCharges));
        txtSubtotal.setText(currency + " " + new DecimalFormat("##.##").format(dblProcessingCharges + dblDeliveryCharges + dblBasePrice));
        double baseAmt = dblProcessingCharges + dblDeliveryCharges + dblBasePrice;
        if (dblTax > 0) {
            txtTax1.setText(currency + " " + new DecimalFormat("##.##").format((baseAmt * dblTax) / 100));
        } else {
            txtTax1.setText(currency + " 0.0");
        }
        //
        txtTax.setText("Tax(" + new DecimalFormat("##.##").format(dblTax) + "%)");
        txtPreAmt.setText(currency + " " + new DecimalFormat("##.##").format(dblWalletAmount));
        double paybleAmt = 0.0;


        paybleAmt = Double.parseDouble(txtTax1.getText().toString().replace(currency, "")) + Double.parseDouble(txtSubtotal.getText().toString().replace(currency, ""));

        double dblll = Double.parseDouble(txtTax1.getText().toString().replace(currency, "")) + Double.parseDouble(txtSubtotal.getText().toString().replace(currency, ""));

        txtPaybleAmt.setText(currency + " " + new DecimalFormat("##.##").format(paybleAmt));
        txtPaybleAm1.setText(currency + " " + new DecimalFormat("##.##").format(dblll));
        txtPaybleAm1.setText(currency + " " + new DecimalFormat("##.##").format(dblll));
        if (dblPromoAmount > 0) {
            double mFinalAmt = 0.0;
            if (dblPromoAmount < paybleAmt) {
                mFinalAmt = paybleAmt - dblPromoAmount;
            } else {
                mFinalAmt = 0.0;
            }

            txtPaybleAmt.setText(currency + " " + new DecimalFormat("##.##").format(mFinalAmt));

            if (dblll > dblPromoAmount) {
                dblll = dblll - dblPromoAmount;
            } else {
                dblll = 0.0;
            }

            txtPaybleAm1.setText(currency + " " + new DecimalFormat("##.##").format(dblll));

        }


    }

    public void logdata() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String mTodayDate = df.format(c.getTime());


        Preferencehelper prefs;
        prefs = new Preferencehelper(Recurring_order.this);
        Map<String, String> params = new HashMap<String, String>();


        try {
            DecimalFormat formey = new DecimalFormat("0.00");


            String param = getString(R.string.CREATE_MASTER_ORDER) + "&OID=" + "0" + "&CID=" + prefs.getPrefsContactId()
                    + "&CustID=" + prefs.getPrefsContactId() + "&CompID=" + mData1.getString("shopId", "1") + "&CreatedBy=" + prefs.getPrefsContactId()
                    + "&RItemCount=" + qty + "&RAmount=" + formey.format(mTotalPrice) + ""
                    + "&Status=" + "0" + "&OType=" + "RO" + "&OrderDate=" + mTodayDate + "&Type=" + "0" + "&RandNumber=" + "0" + "&DeliveryMode=" + deliveryval + ""
                    + "&PaymentMode=" + payval + "" + "&PhoneforOTP=" + contacttxt.getText().toString() + "" + "&Comment=" + comment.getText().toString() + "" + "&TimeSlotDate=" + datedelivery.getText().toString() + "&TimeSLotTime=" + timeslotval + "&PreviousAdjustedAmount=" + mPreAmt + "&DeliveryCharge=" + mDlvrAmt + "&CurrentPaidAmount=" + amt;
            Log.e("myparamsb", param);
            JKHelper jkHelper = new JKHelper();
            String finalparam = jkHelper.Encryptapi(param, Recurring_order.this);
            params.put("val", finalparam);
            Log.e("myparamsa", finalparam);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getTimeslotNew() {
        timelist.clear();
        timerecyc.setAdapter(new TimeSlotAdapter(Recurring_order.this, timelist, Recurring_order.this));

        Log.e("dsfkjjfk", AppController.getValTypeFromDesc("order_master"));
        final ProgressDialog mProgressBar = new ProgressDialog(Recurring_order.this);
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
                            String responses = jkHelper.Decryptapi(response, Recurring_order.this);
                            Log.e("responsestimeslot", responses);
                            JSONObject jsonObject = new JSONObject(responses);
                            timelist.clear();
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                //for (int a=0;a<mArrayList.size();a++){
                                //"returnds":[{"OrderID":"6","successmsg":"Successful"}]
                                JSONArray jsonArray = jsonObject.getJSONArray("returnmessage");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    catcodeid = jsonObject1.getString("SlotId");
                                    catcodedesc = jsonObject1.getString("Slottime");
                                    //  if (mDeliveryHelper.getDeliverytype().equalsIgnoreCase("Home Delivery")) {
                                    deliverycgarge = jsonObject1.getString("DeliveryCharges");
                                   /* } else {
                                        deliverycgarge = "0.0";
                                    }*/
                                    Timehelper payhelper = new Timehelper();
                                    payhelper.setTimeid(catcodeid);
                                    payhelper.setTimestr(catcodedesc);
                                    payhelper.setVal1str(deliverycgarge);


                                    timelist.add(payhelper);
                                    if (i == 0) {
                                        str2 = Double.parseDouble(jsonArray.getJSONObject(0).getString("DeliveryCharges"));
                                        String strr = txtSubtotal.getText().toString();
                                        String strr1 = txtPaybleAmt.getText().toString();
                                        Log.e("strrstrr", strr + " " + strr1);
                                        try {
                                            double newSubT = Double.parseDouble(strr.replace("₹", "").replace("$", "")) - str1;
                                            double newPT = Double.parseDouble(strr1.replace("₹", "").replace("$", "")) - str1;
                                            Log.e("NYUGU", "before = " + (newSubT) + " " + (newPT) + "\n after = " + (newSubT + str2) + " " + (newPT + str2));
                                            txtSubtotal.setText(strCrncy + "" + (newSubT + str2));
                                            txtPaybleAmt.setText(strCrncy + "" + (newPT + str2));
                                            str1 = str2;
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        txtDlvryAmt.setText(currency + "" + Double.parseDouble(jsonArray.getJSONObject(0).getString("DeliveryCharges")));
                                    }
                                }

                                timerecyc.setAdapter(new TimeSlotAdapter(Recurring_order.this, timelist, Recurring_order.this));

//}

                            } else {

                                FancyToast.makeText(Recurring_order.this, "No Data Found, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
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
                        FancyToast.makeText(Recurring_order.this, "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


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
                prefs = new Preferencehelper(Recurring_order.this);
                Map<String, String> params = new HashMap<String, String>();


                try {

                    String param = getString(R.string.GET_TIME_SLOT_NEW) + "&storeId=" + mData1.getString("shopId", "1")
                            + "&dtype=" + mDeliveryHelper.getDeliverytype() + "&tdate=" + datedelivery.getText().toString();
                    Log.d("Beforeencrptionpay", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, Recurring_order.this);
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

        Volley.newRequestQueue(this).add(postRequest);


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

    private void loadProfileData() {

        final ProgressDialog mProgressBar = new ProgressDialog(Recurring_order.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.setCancelable(false);
        mProgressBar.show();


        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String res = response;
                        mProgressBar.dismiss();
                        Log.e("ressss", res.toString());
                        try {
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, Recurring_order.this);
                            JSONObject jsonObject = new JSONObject(responses);
                            int success = jsonObject.getInt("success");
                            log.e("responsessuccess" + responses);
                            if (success == 1) {
                                JSONArray jsonArray = jsonObject.getJSONArray("returnds");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonobject = jsonArray.getJSONObject(i);
                                    String city = jsonobject.getString("City");
                                    String firstname = jsonobject.getString("firstname");
                                    String phoneno = jsonobject.getString("phoneno");
                                    String email2 = jsonobject.getString("email");
                                    String flatno = jsonobject.getString("FlatNo");
                                    String state = jsonobject.getString("state");
                                    String Building = jsonobject.getString("Building");
                                    String society = jsonobject.getString("society");
                                    String zipcode = jsonobject.getString("Zipcode");
                                    String CurrentBalance = jsonobject.getString("CurrentBalance");

                                    preferencehelper.setPREFS_currentbal(CurrentBalance);
                                    preferencehelper.setPREFS_city(city);
                                    preferencehelper.setPREFS_society(society);
                                    preferencehelper.setPREFS_Building(Building);
                                    preferencehelper.setPREFS_firstname(firstname);
                                    preferencehelper.setPREFS_email2(email2);
                                    preferencehelper.setPREFS_phoneno(phoneno);
                                    preferencehelper.setPREFS_flatno(flatno);
                                    preferencehelper.setPREFS_state(state);
                                    preferencehelper.setPrefsZipCode(zipcode);

                                    SharedPreferences prefs1 = getSharedPreferences("MORNING_PARCEL_GROCERY",
                                            MODE_PRIVATE);


                                    if (prefs1.getString("Currency", "INR").equalsIgnoreCase("INR")) {
                                        currency = getResources().getString(R.string.rupee);
                                    } else {
                                        currency = "$";
                                    }


                                }

                                indianCheckout();
                            }
                        } catch (Exception e) {
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
                Map<String, String> params = new HashMap<String, String>();

                try {

                    String param = getString(R.string.URL_GET_PROFILE) + "&contactid=" + preferencehelper.getPrefsContactId() + "&userid=" + "";
                    Log.d("Beforeencrptionprofile", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, Recurring_order.this);
                    params.put("val", finalparam);
                    Log.d("afterencrptionprofile", finalparam);
                } catch (Exception e) {
                    e.printStackTrace();

                }
                return params;

            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(this).add(postRequest);
    }

    public void indianCheckout() {


        amt = Float.parseFloat(new DecimalFormat("##.##").format((Double.parseDouble(txtSubtotal.getText().toString().replace(currency, "")) + Double.parseDouble(txtTax1.getText().toString().replace(currency, ""))))) + "";


        //mTXNID = "iugfchjk";
        //strTxnId = "iugfchjk";

        // checkoutOrder();

        if (strTimeSlot == null || strTimeSlot.equalsIgnoreCase("")) {
            FancyToast.makeText(Recurring_order.this, "time slot is not select or either there is no time slot available for this delivery", FancyToast.LENGTH_LONG, FancyToast.INFO, false).show();
        } else {
//                                if (Float.parseFloat(new DecimalFormat("##.##").format(Double.parseDouble(payableamount))) > 0) {
            PreviousAdjustedAmount = (Double.parseDouble(txtSubtotal.getText().toString().replace(currency, "")) + Double.parseDouble(txtTax1.getText().toString().replace(currency, "")));
            if ((Double.parseDouble(txtSubtotal.getText().toString().replace(currency, "")) + Double.parseDouble(txtTax1.getText().toString().replace(currency, ""))) > 0) {
                Log.e("my_condition = ", "1");

//                                    dblWalletAmount = ;

                if (Double.parseDouble(preferencehelper.getPREFS_currentbal()) >= 1000) {
                    strTxnId = "WALLET AMOUNT USED";
                    checkoutOrder();
                    Log.e("my_condition = ", "2");
//                                        moveToTransection();
                } else {
                    Log.e("my_condition = ", "3");


                    Dialog dialog = new Dialog(Recurring_order.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.setContentView(R.layout.dialog_wallet_amount);
                    dialog.setCancelable(true);
                    final Button submit = (Button) dialog.findViewById(R.id.btnupdate);
                    final TextView txtWalletAmtShow = (TextView) dialog.findViewById(R.id.txtWalletAmtShow);
                    final TextView tvdesc = (TextView) dialog.findViewById(R.id.tvdesc);
                    tvdesc.setText("You have insufficient amount in your wallet to make an order, Your wallet  amount should have atleast Rs.1000.Please add money to wallet and try again.");
                    txtWalletAmtShow.setText("₹" + preferencehelper.getPREFS_currentbal());
                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            startActivityForResult(new Intent(Recurring_order.this, WalleTransactionActivity.class), 5566);
                            //startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + getPackageName())));


                        }
                    });

                    // Display the custom alert dialog on interface
                    dialog.show();

                }
            } else {
                Log.e("my_condition = ", "4");
                strTxnId = "WALLET AMOUNT USED";
                checkoutOrder();
            }

            //  strTxnId = "76565678576465656";
            // checkoutOrder();
//if ()
        }


    }

    public void updateOrderStatus(OrderBean.OrderItemsBean orderItemsBean) {


        mListOrder = weeklyAdapter.makeOrder();
        for (int a = 0; a < mListOrder.size(); a++) {
            if (Integer.parseInt(mListOrder.get(a).getAddval()) > 0) {

                qty = Integer.parseInt(mListOrder.get(a).getAddval());
                dblAmt =/*(Integer.parseInt(mListOrder.get(a).getAddval()) * */Double.parseDouble(orderItemsBean.getRate())/*)*/;

            }

            // qty = qty + Integer.parseInt(mListOrder.get(a).getAddval());
            //   dblAmt = dblAmt + (Integer.parseInt(mListOrder.get(a).getAddval()) * Double.parseDouble(mData.getSaleRate()));
            if (strParams.isEmpty()) {
                strParams = a + "//" + mListOrder.get(a).getAddval();
            } else {
                strParams = strParams + ";" + a + "//" + mListOrder.get(a).getAddval();

            }
        }
        final ProgressDialog mProgressBar = new ProgressDialog(Recurring_order.this);
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
                            String responses = jkHelper.Decryptapi(response, Recurring_order.this);
                            JSONObject jsonObject = new JSONObject(responses);
                            String orderupdate = jsonObject.getString("orderupdate");

                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                //  txtStatus.setText("CANCELLED");


                                FancyToast.makeText(Recurring_order.this, "Order Updated Successfully.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                                finish();
                            } else {

                                FancyToast.makeText(Recurring_order.this, orderupdate, FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
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
                        FancyToast.makeText(Recurring_order.this, "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {


                Preferencehelper prefs;
                prefs = new Preferencehelper(Recurring_order.this);
                Map<String, String> params = new HashMap<String, String>();
                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);

                    String param = getString(R.string.UPDATE_ORDER_STATUS) + "&orderid=" + orderItemsBean.getOrderdetailID() + "&status=" + strParams + "&type=" + "8" + "&contactId=" + prefs.getPrefsContactId();
                    Log.d("Beforeencrptionupdate", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, Recurring_order.this);
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

    public void cancelOrder(String oid, String type, String status) {


        final ProgressDialog mProgressBar = new ProgressDialog(Recurring_order.this);
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
                            String responses = jkHelper.Decryptapi(response, Recurring_order.this);
                            JSONObject jsonObject = new JSONObject(responses);
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                //  txtStatus.setText("CANCELLED");
                              llButtons.setVisibility(View.GONE);
                            } else {

                                FancyToast.makeText(Recurring_order.this, "failed to update the order, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
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
                        FancyToast.makeText(Recurring_order.this, "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


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
                prefs = new Preferencehelper(Recurring_order.this);
                Map<String, String> params = new HashMap<String, String>();
                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);

                    String param = getString(R.string.UPDATE_ORDER_STATUS) + "&orderid=" + oid + "&status=" + status + "&type=" + type + "&contactId=" + prefs.getPrefsContactId();
                    Log.d("Beforeencrptionupdate", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, Recurring_order.this);
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

}