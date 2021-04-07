package com.dhanuka.morningparcel.activity;

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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
/*import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.models.PayPalAccountNonce;
import com.braintreepayments.api.models.PaymentMethodNonce;*/
import com.dhanuka.morningparcel.AppController;
import com.dhanuka.morningparcel.Helper.CartOrderDAO;
import com.dhanuka.morningparcel.Helper.DBCartDataUpload;
import com.dhanuka.morningparcel.InterfacePackage.Addressadd;
import com.dhanuka.morningparcel.InterfacePackage.TriggerClick;
import com.dhanuka.morningparcel.adapter.PromoAdapter;
import com.dhanuka.morningparcel.beans.CartSendParams;
import com.dhanuka.morningparcel.demo.Api;
import com.dhanuka.morningparcel.demo.Checksum;
import com.dhanuka.morningparcel.demo.Constant;
import com.dhanuka.morningparcel.events.CartTextchnage;
import com.dhanuka.morningparcel.events.OnStoreSelectListener;
import com.dhanuka.morningparcel.model.PromoModel;
import com.dhanuka.morningparcel.restaurant.activity.AddLatLong;
import com.dhanuka.morningparcel.restaurant.adapters.SavedAdressadapter;
import com.dhanuka.morningparcel.restaurant.bean.GeoFenceBean;
import com.dhanuka.morningparcel.restaurant.bean.GeoFenceBeanMain;
import com.dhanuka.morningparcel.utils.DbImageUpload;
import com.dhanuka.morningparcel.Helper.ImageMasterDAO;
import com.dhanuka.morningparcel.utils.ImageUploadService;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.Helper.DbImageMaster;
import com.dhanuka.morningparcel.Helper.ImageUploadDAO;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.Helper.Timehelper;
import com.dhanuka.morningparcel.InterfacePackage.CartItemsadd;

import com.dhanuka.morningparcel.R;

import com.dhanuka.morningparcel.SqlDatabase.QueryExecutor;
import com.dhanuka.morningparcel.adapter.SelectedImagesAdapter;
import com.dhanuka.morningparcel.beans.FancyToast;
import com.dhanuka.morningparcel.adapter.CartAdapter;
import com.dhanuka.morningparcel.adapter.DeliveryHelper;
import com.dhanuka.morningparcel.adapter.Delivery_Option_Adapter;
import com.dhanuka.morningparcel.adapter.Payhelper;
import com.dhanuka.morningparcel.adapter.Payment_Option_Adapter;
import com.dhanuka.morningparcel.adapter.TimeSlotAdapter;
import com.dhanuka.morningparcel.beans.CartProduct;
import com.dhanuka.morningparcel.SqlDatabase.DatabaseManager;
import com.dhanuka.morningparcel.customViews.CartCountView;
import com.dhanuka.morningparcel.customViews.StoreView;
import com.dhanuka.morningparcel.events.CartActionListener;

import com.dhanuka.morningparcel.utils.OrderUploadService;
import com.dhanuka.morningparcel.utils.Utility;
import com.dhanuka.morningparcel.utils.log;

/*import com.google.android.gms.wallet.Cart;
import com.google.android.gms.wallet.ShippingAddressRequirements;
import com.google.android.gms.wallet.TransactionInfo;
import com.google.android.gms.wallet.WalletConstants;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.exception.AuthenticationException;*/
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.filter.entity.ImageFile;
import com.yekmer.cardlib.CreditCardEditText;
import com.yekmer.cardlib.OtherCardTextWatcher;
import com.yekmer.cardlib.TwoDigitsCardTextWatcher;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;


import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.vincent.filepicker.activity.BaseActivity.IS_NEED_FOLDER_LIST;
import static com.vincent.filepicker.activity.ImagePickActivity.IS_NEED_CAMERA;

public class CartActivity extends BaseActivity implements CartActionListener, CartItemsadd, Addressadd, TriggerClick, OnStoreSelectListener, CartTextchnage {
    com.dhanuka.morningparcel.database.DatabaseManager dbManager;
    ListView lvCart;
    Dialog dialogfinace;
    Button btnCheckout, btnContinue1;
    String date_of_installation;
    Dialog PayDialog, dialogPayment, Promodialog;
    TextView applybtn, applypromoclick;
    double dblPromoAmount = 0.0;
    Float saleuomint = 0.0f, slaerateclc = 0.0f, taxamount = 0.0f;
    Float finaltaxamount = 0.0f, finaltaxrate = 0.0f;
    String IsFinanceBlock;

    double dblPromoAmount1 = 0.0;
    Float finalpercentpromo = Float.parseFloat("0.0");
    ArrayList<String> promolist1 = new ArrayList<>();
    ArrayList<PromoModel> promolist;
    PromoModel promoModel;
    RecyclerView promocontainer;
    String oid = "0";
    private int oneWayDay, oneWayMonth, oneWayYear;
    CartAdapter adapter;
    RadioButton radioButtonpay, radioButtondelivery;
    Context context;
    LinearLayout deliveryaddll, promoll;
    TextView promotxn;
    EditText enterpromo;
    LinearLayout linearContinue;
    ArrayList<GeoFenceBean> geofencelist = new ArrayList<GeoFenceBean>();
    TextView txtGrandTotal;
    String deliveryval, payval, timeslotval, delextime;
    @Nullable
    @BindView(R.id.tvCount)
    TextView tvCount;
    String strCrncy;
    TextView txtSHop;
    RadioGroup deliverytyper, paytyper;

    Button selectcartbtn;
    ImageView pickimagebtn;
    EditText contacttxt, comment;
    int delivertType = 2;
    int pmntType = 0;
    String strTxnId = "";
    String promostr = "0";
    Preferencehelper preferencehelper;
    Dialog CameraDialog;
    String mImagePathDataBase, mImageNameDataBase, mCurrentTimeDataBase;
    int masterDataBaseId;
    String lastRowMaterTable;
    private boolean addedToMasterTable = false;
    RecyclerView rvImages;
    SelectedImagesAdapter mAdapter;
    private TextView datedelivery;
    ArrayList<String> mListLastRows = new ArrayList<>();
    RecyclerView deliveryrecyc, payrecyc, timerecyc;
    TextView chooseadd;
    LinearLayout gpsll;
    String deliveryaddlat, deliveryaddlong;
    SharedPreferences mData;
    SharedPreferences.Editor mEditor;
    SavedAdressadapter savedAdressadapter;
    String myMonth, myDay;

    TextView selectlocation, selectdelivtxt;

    private int mYear, mMonth, mDay;
    ArrayList<DeliveryHelper> deliverylist, alldeliverylist;
    ArrayList<Payhelper> paylist = new ArrayList<Payhelper>();
    ArrayList<Timehelper> timelist = new ArrayList<Timehelper>();
    double mSerAmt = 0.0;
    double mPreAmt = 0.0;
    double mDlvrAmt = 0.0;
    LinearLayout dlvryL, walletL, taxL;

    TextView msgheader, txtmSavings, txttotalitems;
    Dialog updatedetaildialog;
    TextView flatide, nameide, emailide, zipide, aptide, addaddress;
    Button savedetail;

    TextView txtPaybleAmt, txtPaybleAm1,
            txtNote,
            txtCartAmt,
            txtPreAmt,
            txtTax1,
            txtTax,
            txtSubtotal,
            txtDlvryAmt,
            txtSrvcAmt;
    ImageView imgBackBtn;
    TextView deliveryaddval;
    LinearLayout deliveryll;
    ImageView imgBackBtnNew;
    String lastInput = "";
    Button btnPay;
    BottomSheetDialog dialogbottom;
    RecyclerView savedrecysheet;
    Button savebutton;

    TextView addnewadd;
    GeoFenceBeanMain Mgeofencebeanmain;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_cart;
    }

    ArrayList<String> mListSelectedIamges = new ArrayList<>();
    String currency = "";
    //  Stripe stripe;
    String name = "testt";
   /* Card card;
    Token tok;*/

    EditText mEditTextCardCVV;
    private CreditCardEditText creditCardEditText;
    private EditText expirationEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_cart, container_body);

        context = CartActivity.this;
        mData = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);
        ButterKnife.bind(this);
        prefs11 = getSharedPreferences("payment_prfs",
                MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs11.edit();

        editor.putString("isOK", "0");
        editor.putString("mid", "0");
        editor.commit();


        mEditor = mData.edit();
        preferencehelper = new Preferencehelper(getApplicationContext());
        updatedetaildialog = new Dialog(CartActivity.this, R.style.full_screen_dialog);
        updatedetaildialog.setContentView(R.layout.update_personaldetails);
        flatide = updatedetaildialog.findViewById(R.id.flatide);
        nameide = updatedetaildialog.findViewById(R.id.nameide);
        emailide = updatedetaildialog.findViewById(R.id.emailide);
        zipide = updatedetaildialog.findViewById(R.id.zipide);
        aptide = updatedetaildialog.findViewById(R.id.aptid);
        addaddress = updatedetaildialog.findViewById(R.id.addaddress);
        savedetail = updatedetaildialog.findViewById(R.id.savedetail);
        updatedetaildialog.setCancelable(false);


        addaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showaddressselect();

            }
        });
        savedetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Signupapi();

            }
        });
        dialogPayment = new Dialog(CartActivity.this, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        dialogPayment.setContentView(R.layout.dialog_card);
        btnPay = dialogPayment.findViewById(R.id.btnPay);
        mEditTextCardCVV = dialogPayment.findViewById(R.id.cardCVV);
        imgBackBtnNew = dialogPayment.findViewById(R.id.imgBackBtnNew);
        creditCardEditText = (CreditCardEditText) dialogPayment.findViewById(R.id.credit_card_edit_text);
        expirationEditText = (EditText) dialogPayment.findViewById(R.id.et_fragment_add_credit_card_expiry);

        creditCardEditText.addTextChangedListener(new OtherCardTextWatcher(creditCardEditText));
        expirationEditText.addTextChangedListener(new TwoDigitsCardTextWatcher(expirationEditText));

        imgBackBtnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPayment.dismiss();
            }
        });
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitCard(v);


            }
        });


        Promodialog = new Dialog(CartActivity.this, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        Promodialog.setContentView(R.layout.promodialog);
        promocontainer = Promodialog.findViewById(R.id.promocontainer);
        promocontainer.setHasFixedSize(true);
        promocontainer.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));


        PayDialog = new Dialog(CartActivity.this, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        PayDialog.setContentView(R.layout.delivery_mode_dialog);
        promoll = PayDialog.findViewById(R.id.promol);
        applybtn = PayDialog.findViewById(R.id.applybtn);
        enterpromo = PayDialog.findViewById(R.id.enterpromo);

        promotxn = PayDialog.findViewById(R.id.promotxn);

        deliverytyper = PayDialog.findViewById(R.id.deliverytype);
        deliveryaddll = PayDialog.findViewById(R.id.deliveryaddchange);
        paytyper = PayDialog.findViewById(R.id.paymenttype);

        msgheader = findViewById(R.id.msgheader);
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
        if (TextUtils.isEmpty(prefs.getPrefsZipCode())) {
            // updatedetaildialog.show();
        }

        datedelivery = PayDialog.findViewById(R.id.dateselect);
        pickimagebtn = PayDialog.findViewById(R.id.imagepick);
        rvImages = PayDialog.findViewById(R.id.rvImages);

        CameraDialog = new Dialog(CartActivity.this);
        CameraDialog.setContentView(R.layout.popup_image_select);
        payrecyc.setLayoutManager(new LinearLayoutManager(CartActivity.this));
        deliveryrecyc.setLayoutManager(new LinearLayoutManager(CartActivity.this));
        payrecyc.setHasFixedSize(true);
        deliveryrecyc.setHasFixedSize(true);
        timerecyc.setLayoutManager(new LinearLayoutManager(CartActivity.this));
        timerecyc.setHasFixedSize(true);


        SharedPreferences prefs22 = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);

       /* try {
            stripe = new Stripe("pk_test_EbCpH358aTH3p8TSFGJf1aCe00VuQ0Q8qB");
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }
*/
        if (prefs22.getString("Currency", "INR").equalsIgnoreCase("INR")) {
            currency = getResources().getString(R.string.rupee);
        } else {
            currency = "$";
        }
        final Calendar c = Calendar.getInstance();
        c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());


        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        Date dt = new Date();
        Calendar cn = Calendar.getInstance();
        cn.setTime(dt);
        cn.add(Calendar.DATE, 1);
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String mTodayDate = df.format(cn.getTime());
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

        date_of_installation = mTodayDate;
        datedelivery.setText(date_of_installation);

        ImageView camerabtn = CameraDialog.findViewById(R.id.camerabtn);
        ImageView gallerybtn = CameraDialog.findViewById(R.id.gallerybtn);
        rvImages.setLayoutManager(new LinearLayoutManager(CartActivity.this, RecyclerView.HORIZONTAL, false));
        mAdapter = new SelectedImagesAdapter(this, mListSelectedIamges);
        rvImages.setAdapter(mAdapter);
        msgheader.setText(preferencehelper.getPrefsTag1());
        msgheader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preferencehelper.getPrefsTag1Desc().isEmpty()) {


                } else {
                    JKHelper.openCommentDialog(CartActivity.this, preferencehelper.getPrefsTag1Desc());

                }

            }
        });
        gallerybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(CartActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CartActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                } else {
                    Intent intent1 = new Intent(CartActivity.this, ImagePickActivity.class);
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
        camerabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(CartActivity.this, ImagePickActivity.class);
                intent1.putExtra(IS_NEED_CAMERA, true);
                intent1.putExtra(com.vincent.filepicker.Constant.MAX_NUMBER, 1);
                intent1.putExtra(IS_NEED_FOLDER_LIST, false);
                startActivityForResult(intent1, com.vincent.filepicker.Constant.REQUEST_CODE_TAKE_IMAGE);

                //  openCamera();
                //     launchCameraIntent();
                CameraDialog.dismiss();
            }
        });
        datedelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!startDate.isEmpty()) {
                    try {
                        String strSplit[] = startDate.split("/");
                        mDay = Integer.parseInt(strSplit[0] + 1);
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

        strOpen = "cart";
        dbManager = com.dhanuka.morningparcel.database.DatabaseManager.getInstance(context);

        SharedPreferences prefs1 = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);
        lvCart = (ListView) view.findViewById(R.id.lvCart);
        btnCheckout = (Button) view.findViewById(R.id.btnProceed);

        tvCount = (TextView) view.findViewById(R.id.tvCount);
        linearContinue = (LinearLayout) view.findViewById(R.id.linearContinue);
        txtSHop = (TextView) view.findViewById(R.id.txtSHop);
        txtGrandTotal = (TextView) view.findViewById(R.id.tvPrice);
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
        applypromoclick = PayDialog.findViewById(R.id.applypromoclick);
        deliveryll = PayDialog.findViewById(R.id.deliveryll);
        deliveryaddval.setText("Choose Delivery address");
        loadHistory();
        applypromocode();

        loadProfileDataUser();

        applypromoclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Promodialog.show();
            }
        });
        applybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isValid = false;
                PromoModel promoModel = null;
                for (int a = 0; a < promolist.size(); a++) {
                    Log.e("p_r_m_o" + a, enterpromo.getText().toString() + " == " + promolist.get(a).getCodeDescription());
                    if (promolist.get(a).getCodeDescription().equalsIgnoreCase(enterpromo.getText().toString())) {
                        isValid = true;
                        promoModel = promolist.get(a);
//                        promolist1.remove(a);
                        promolist.get(a).setUsed(true);
                        Log.e("p_datas", new Gson().toJson(promoModel));
                    } else {
                        promolist.get(a).setUsed(false);

                    }
                }
                dblPromoAmount = -dblPromoAmount1;
                calculateTotalAmount();

                if (isValid && promoModel != null) {


                    //  Log.d("Listsizepromo", String.valueOf(promolist.size()) + " " + promoModel.getCodeDescription());

                    if (promoModel.getCodeDescription().equalsIgnoreCase(enterpromo.getText().toString())) {

                        finalpercentpromo = Float.parseFloat(String.valueOf(txtPaybleAmt.getText().toString()).trim().replace(currency, "")) * Float.parseFloat(promoModel.getVal1()) / 100.0f;


                        if (Float.parseFloat(String.valueOf(finalpercentpromo)) > Float.parseFloat(String.valueOf(promoModel.getVal2()))) {
                            try {
                                dblPromoAmount = Double.parseDouble(String.valueOf(promoModel.getVal2()));
                                dblPromoAmount1 = Double.parseDouble(String.valueOf(promoModel.getVal2()));
                                promostr = enterpromo.getText().toString() + "//" + finalpercentpromo + "//" + promoModel.getVal1() + "//" + promoModel.getVal2();
                            } catch (Exception e) {
                                e.printStackTrace();
                                dblPromoAmount = 0.0;
                            }
                            //        Float finalamountpromo = Float.parseFloat(txtPaybleAmt.getText().toString().trim().replace(currency, "").replace(" ", "")) - Float.parseFloat(String.valueOf(promoModel.getVal2()));
                            //    txtPaybleAmt.setText(String.valueOf(finalamountpromo));
                            promotxn.setText(currency + "-" + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(promoModel.getVal2()))));
                            calculateTotalAmount();
                            // break;


                        } else {

                            try {
                                dblPromoAmount = Double.parseDouble(String.valueOf(finalpercentpromo));
                                promostr = enterpromo.getText().toString() + "//" + finalpercentpromo + "//" + promoModel.getVal1() + "//" + promoModel.getVal2();
                            } catch (Exception e) {
                                e.printStackTrace();
                                dblPromoAmount = 0.0;
                            }
                            Float finalamoutapplied = Float.parseFloat(String.valueOf(txtPaybleAmt.getText().toString().trim().replace(currency, "").replace(" ", ""))) - Float.parseFloat("" + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(finalpercentpromo))));
                            //  txtPaybleAmt.setText(currency + " " + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(finalamoutapplied))));
                            promotxn.setText(currency + "-" + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(finalpercentpromo))));
                            calculateTotalAmount();

                        }
                    } else {


                    }
                    FancyToast.makeText(CartActivity.this, "Coupon Applied Successfully.", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();


                } else {
                    FancyToast.makeText(CartActivity.this, "Invalid Coupon Code.", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();


                }


            }
        });


        if (TextUtils.isEmpty(getIntent().getStringExtra("addrstr"))) {
            String deladd = getIntent().getStringExtra("addrstr");
            deliveryaddval.setText(deladd);

        } else {
            showaddresslist();
            loadProfileDataUser();
        }
        imgBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayDialog.dismiss();
            }
        });
        try {
            mPreAmt = Double.parseDouble(prefs.getPREFS_currentbal());
        } catch (Exception er) {
            mPreAmt = 0.0;

        }
        txtNote.setText(mData.getString("CheckOutMessage", " - "));
        //  Log.e("ServiceFeesas",mData.getString("ServiceFees", "0.0"));
        mSerAmt = Double.parseDouble(df2.format(Double.parseDouble(mData.getString("ServiceFees", "0.0"))));


        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finaltaxamount = 0.0f;
                finaltaxrate = 0.0f;
                taxamount = 0.0f;

                if (Double.parseDouble(mData.getString("MinOrderAmt", "0")) > 0 && mTotalPrice < Double.parseDouble(mData.getString("MinOrderAmt", "0"))) {
                    FancyToast.makeText(CartActivity.this, "Minimum required cart value is " + mData.getString("MinOrderAmt", "0"), FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

                } else if (Double.parseDouble(mData.getString("MaxOrderAmt", "0")) > 0 && mTotalPrice > Double.parseDouble(mData.getString("MaxOrderAmt", "0"))) {
                    FancyToast.makeText(CartActivity.this, "Maximum required cart value is " + mData.getString("MaxOrderAmt", "0"), FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

                } else {
                    loadHistory();
                    getpaylist(deliveryval);
                    getTimeslot();
                    getdeliverylist();
                    PayDialog.show();
                }
            }
        });
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
                    FancyToast.makeText(CartActivity.this, "Enter phone number.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

                } else if (deliveryaddval.getText().toString().equalsIgnoreCase("")) {
                    FancyToast.makeText(CartActivity.this, "Please Select address in order to check out", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

                } else if (contacttxt.getText().toString().length() < 10) {
                    FancyToast.makeText(CartActivity.this, "Enter valid phone number.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

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
                        amt = df2.format(mTotalPrice - mPreAmt + mDlvrAmt + Double.parseDouble(mData.getString("ServiceFees", "0.0")));
                        mEditor.putString("mAmount", amt + "");
                        mEditor.commit();
                        //  startActivityForResult(getDropInRequest().getIntent(CartActivity.this), DROP_IN_REQUEST);

                        String payableamount = txtPaybleAmt.getText().toString().replace("$", "").replace("₹", "");
                        if (currency.equalsIgnoreCase("$")) {
                            if (strTimeSlot == null || strTimeSlot.equalsIgnoreCase("")) {
                                FancyToast.makeText(CartActivity.this, "time slot is not select or either there is no time slot available for this delivery", FancyToast.LENGTH_LONG, FancyToast.INFO, false).show();
                            } else {
                                if (Double.parseDouble(txtPaybleAmt.getText().toString().replace(currency, "")) > 1) {
                                    startActivity(new Intent(CartActivity.this, CreateCardPaymentMethodActivity.class).putExtra("price", new DecimalFormat("##.##").format(Double.parseDouble(txtPaybleAmt.getText().toString().replace(currency, "")))));
                                } else {
                                    strTxnId = "";
                                    mTXNID = "";
                                    checkoutOrder();
                                }


                            }
                        } else {

                            loadProfileData();
                        }
                        //  dialogPayment.show();
                    } else {

                        if (preferencehelper.getPrefsIsFinanceBlock().equalsIgnoreCase("1")) {
                            dialogfinace.show();
                        } else {
                            checkoutOrder();
                        }


                    }

                }
            }
        });

        countView = (CartCountView) LayoutInflater.from(context).inflate(R.layout.action_cart, null);
        storeView = (StoreView) LayoutInflater.from(context).inflate(R.layout.action_shop, null);

        txtSHop.setText(prefs1.getString("shopName", ""));
        if (savedInstanceState != null) {
          /*  if (savedInstanceState.containsKey(KEY_NONCE)) {
                mNonce = savedInstanceState.getParcelable(KEY_NONCE);
            }*/
        }


      /*  AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
        builder.setMessage("Proceed to pay Amt. :\nRs. " + df2.format(25.54789255) + "\n request id : 21545");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
*/
        //getDate();

    }

    private void setCartCount() {
        try {
            tvCount.setText(dbManager.getTotalQty() + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        countView.setCount(dbManager.getTotalQty());
        txttotalitems.setText(dbManager.getTotalQty() + "");

    }

    int mCount = 0;

    //
    private void setData() {
        ArrayList<CartProduct> list = (ArrayList<CartProduct>) dbManager.getProducts();
        if (list.isEmpty()) {
            linearContinue.setVisibility(View.VISIBLE);
        } else {
            linearContinue.setVisibility(View.GONE);
            adapter = new CartAdapter(context, list, this, this);
            lvCart.setAdapter(adapter);
            calculateTotal(list);
            //  setCartValue();
        }
    }

    @Override
    protected void onSideSliderClick() {

    }

    @Override
    public void onMinusClick(ArrayList<CartProduct> cartList, int position) {
        CartProduct product = cartList.get(position);
        //   adapter.getItem(event.getPosition()).setQty(event.getProduct().getQty());
        adapter.notifyDataSetChanged();
        dbManager.update(product);

        calculateTotal(cartList);
        Utility.checkoutOrder(cartList.get(position), preferencehelper.getPrefsContactId(), getApplicationContext(), "2");
        setCartCount();
    }

    @Override
    public void onAddClick(ArrayList<CartProduct> cartList, int position) {
        CartProduct product = cartList.get(position);
        //   adapter.getItem(event.getPosition()).setQty(event.getProduct().getQty());
        adapter.notifyDataSetChanged();
        dbManager.update(product);
        Log.d("product mrp", product.getMRP());
        dbManager.update(product);
        calculateTotal(cartList);
        countView.setCount(dbManager.getTotalQty());
        Utility.checkoutOrder(cartList.get(position), preferencehelper.getPrefsContactId(), getApplicationContext(), "2");

        setCartCount();

    }

    @Override
    public void onDeleteClick(ArrayList<CartProduct> cartList, int position) {


        countView.setCount(dbManager.getTotalQty());

        Utility.checkoutOrder(cartList.get(position), preferencehelper.getPrefsContactId(), getApplicationContext(), "3");

        dbManager.delete(cartList.get(position).getItemID());
        cartList.remove(position);
        calculateTotal(cartList);
        setCartCount();


    }

    int checkoutTime = 0;
    String orderId, catcodeid, catcodedesc, deliverycgarge;
    Double mTotalPrice = 0.00;
    Double mSavings = 0.00;
    ArrayList<CartProduct> mArrayList;

    public void calculateTotal(ArrayList<CartProduct> mArrayListCart) {
        strCrncy = context.getResources().getString(R.string.rupee);
        if (mData.getString("Currency", "INR").equalsIgnoreCase("INR")) {
            strCrncy = context.getResources().getString(R.string.rupee);
        } else {
            strCrncy = "$";
        }
        mArrayList = mArrayListCart;
        mCount = mArrayListCart.size();
        Log.e("mHHH2", "mHEEE" + mArrayListCart.size());


        mTotalPrice = 0.0;


        for (int a = 0; a < mArrayListCart.size(); a++) {
//            Log.e("Iammrp" , mArrayListCart.get(a).getMRP());
            mTotalPrice = mTotalPrice + (Float.parseFloat(mArrayListCart.get(a).getSaleRate()) * mArrayListCart.get(a).getQuantity());
            try {
                if (mArrayListCart.get(a).getMRP() != null) {
                    if (Float.parseFloat(mArrayListCart.get(a).getMRP()) > Float.parseFloat(mArrayListCart.get(a).getSaleRate())) {
                        mSavings = mSavings + ((Float.parseFloat(mArrayListCart.get(a).getMRP()) - Float.parseFloat(mArrayListCart.get(a).getSaleRate())) * mArrayListCart.get(a).getQuantity());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
//mSavings
        txtmSavings.setText(currency + (new DecimalFormat("##.##").format(mSavings)));
        txtGrandTotal.setText(currency + (new DecimalFormat("##.##").format(mTotalPrice)));
        if (mArrayListCart.size() < 1) {
            linearContinue.setVisibility(View.VISIBLE);
        } else {
            linearContinue.setVisibility(View.GONE);
        }

        txtCartAmt.setText(currency + "" + df2.format(mTotalPrice));
        txtSrvcAmt.setText(currency + "" + df2.format(mSerAmt));

        double mTax = 0.0;
        mTax = Double.parseDouble(mData.getString("Tax", "10.0"));
        //  mTax=10.0;
        txtTax.setText("Tax (" + df2.format(mTax) + "%)");
        if (mTax > 0) {
            //  taxL.setVisibility(View.VISIBLE);
        } else {
            //  taxL.setVisibility(View.GONE);
        }
        mTax = ((mTotalPrice + mDlvrAmt + Double.parseDouble(mData.getString("ServiceFees", "0.0"))) * mTax) / 100;
        txtTax1.setText(strCrncy + df2.format(mTax));
        mTaxmTax = mTax;

        txtSubtotal.setText(strCrncy + "" + df2.format(mTotalPrice + mDlvrAmt + Double.parseDouble(mData.getString("ServiceFees", "0.0"))));

        if (mPreAmt > 0) {
            txtPreAmt.setText(currency + "" + df2.format(mPreAmt).replace("-", "") + "");
            txtPaybleAmt.setText(strCrncy + "" + df2.format(mTax + mTotalPrice + mDlvrAmt + Double.parseDouble(mData.getString("ServiceFees", "0.0")) - mPreAmt));
            amt = df2.format(mTax + mTotalPrice + mDlvrAmt + Double.parseDouble(mData.getString("ServiceFees", "0.0")) - mPreAmt);
        } else {
            txtPreAmt.setText("-" + currency + "" + df2.format(mPreAmt).replace("-", "") + "");
            txtPaybleAmt.setText(strCrncy + "" + df2.format(mTax + mTotalPrice + mPreAmt + mDlvrAmt + Double.parseDouble(mData.getString("ServiceFees", "0.0"))));
            amt = df2.format(mTax + mTotalPrice + mPreAmt + mDlvrAmt + Double.parseDouble(mData.getString("ServiceFees", "0.0")));

        }

//        amt = df2.format(mTotalPrice - mPreAmt);
        mEditor.putString("mAmount", amt + "");
        mEditor.commit();


    }

    public void getpaylist(String delivyvalid) {

        Log.e("dsfkjjfk11", AppController.getValTypeFromDesc("order_master"));
        final ProgressDialog mProgressBar = new ProgressDialog(CartActivity.this);
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
                            String responses = jkHelper.Decryptapi(response, CartActivity.this);
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
                                        Log.e("OKOK", "" + catcodedesc);
                                    }

                                }
                                Log.e("HJHJJH", new Gson().toJson(paylist));
                                payrecyc.setAdapter(new Payment_Option_Adapter(CartActivity.this, paylist, CartActivity.this));

//}

                            } else {

                                FancyToast.makeText(CartActivity.this, "No Data Found, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
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
                        FancyToast.makeText(CartActivity.this, "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


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
                prefs = new Preferencehelper(CartActivity.this);
                Map<String, String> params = new HashMap<String, String>();


                try {

                    String param = getString(R.string.URL_GET_CATEGORY_CODE) + "&type=" + "113" + "&contactid=" + prefs.getPrefsContactId() + "&supplierid=" + prefs.getCID();
                    Log.d("Beforeencrptionpay", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, CartActivity.this);
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

    public void getTimeslot() {

        Log.e("dsfkjjfk", AppController.getValTypeFromDesc("order_master"));
        final ProgressDialog mProgressBar = new ProgressDialog(CartActivity.this);
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
                            String responses = jkHelper.Decryptapi(response, CartActivity.this);
                            Log.e("responsestimeslot", responses);
                            JSONObject jsonObject = new JSONObject(responses);
                            timelist.clear();
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                //for (int a=0;a<mArrayList.size();a++){
                                //"returnds":[{"OrderID":"6","successmsg":"Successful"}]
                                JSONArray jsonArray = jsonObject.getJSONArray("returnds");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    catcodeid = jsonObject1.getString("CatCodeID");
                                    catcodedesc = jsonObject1.getString("CodeDescription");
                                    deliverycgarge = jsonObject1.getString("Val1");
                                    Timehelper payhelper = new Timehelper();
                                    payhelper.setTimeid(catcodeid);
                                    payhelper.setTimestr(catcodedesc);
                                    payhelper.setVal1str(deliverycgarge);


                                    timelist.add(payhelper);
                                    if (i == 0) {
                                        str2 = Double.parseDouble(jsonArray.getJSONObject(0).getString("Val1"));
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
                                        txtDlvryAmt.setText(currency + "" + jsonArray.getJSONObject(0).getString("Val1"));
                                    }
                                }

                                timerecyc.setAdapter(new TimeSlotAdapter(CartActivity.this, timelist, CartActivity.this));

//}

                            } else {

                                FancyToast.makeText(CartActivity.this, "No Data Found, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
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
                        FancyToast.makeText(CartActivity.this, "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


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
                prefs = new Preferencehelper(CartActivity.this);
                Map<String, String> params = new HashMap<String, String>();


                try {

                    String param = getString(R.string.URL_GET_CATEGORY_CODE) + "&type=" + "114" + "&contactid=" + prefs.getPrefsContactId() + "&supplierid=" + prefs.getCID();
                    Log.d("Beforeencrptionpay", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, CartActivity.this);
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

    public void getTimeslotNew() {
        timelist.clear();
        timerecyc.setAdapter(new TimeSlotAdapter(CartActivity.this, timelist, CartActivity.this));

        Log.e("dsfkjjfk", AppController.getValTypeFromDesc("order_master"));
        final ProgressDialog mProgressBar = new ProgressDialog(CartActivity.this);
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
                            String responses = jkHelper.Decryptapi(response, CartActivity.this);
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

                                timerecyc.setAdapter(new TimeSlotAdapter(CartActivity.this, timelist, CartActivity.this));

//}

                            } else {

                                FancyToast.makeText(CartActivity.this, "No Data Found, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
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
                        FancyToast.makeText(CartActivity.this, "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


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
                prefs = new Preferencehelper(CartActivity.this);
                Map<String, String> params = new HashMap<String, String>();


                try {

                    String param = getString(R.string.GET_TIME_SLOT_NEW) + "&storeId=" + prefs.getCID()
                            + "&dtype=" + mDeliveryHelper.getDeliverytype() + "&tdate=" + datedelivery.getText().toString();
                    Log.d("Beforeencrptionpay", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, CartActivity.this);
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
        final ProgressDialog mProgressBar = new ProgressDialog(CartActivity.this);
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
                            String responses = jkHelper.Decryptapi(response, CartActivity.this);
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

                                    if (jsonObject1.getString("Val1").equalsIgnoreCase(prefs.getCID())) {
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


                                try {
                                    if (deliverylist.size() > 0) {
                                        deliveryrecyc.setAdapter(new Delivery_Option_Adapter(CartActivity.this, deliverylist, CartActivity.this, CartActivity.this));

                                    }


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                PayDialog.show();
//}

                            } else {

                                FancyToast.makeText(CartActivity.this, "No Data Found, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
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
                        FancyToast.makeText(CartActivity.this, "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


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
                prefs = new Preferencehelper(CartActivity.this);
                Map<String, String> params = new HashMap<String, String>();


                try {
                    String param = getString(R.string.URL_GET_CATEGORY_CODE) + "&type=" + "112" + "&contactid=" + prefs.getPrefsContactId() + "&supplierid=" + prefs.getCID();
                    Log.d("Beforeencrption", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, CartActivity.this);
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

    double PreviousAdjustedAmount = 0.0;
    String strItems = "";

    public void checkoutOrder() {

        for (int a = 0; a < mArrayList.size(); a++) {


        }

        strItems = "";
        ArrayList<CartSendParams> arrayList = new ArrayList<>();

        JSONArray jsonArray = new JSONArray();
        for (int checkoutTime = 0; checkoutTime < mArrayList.size(); checkoutTime++) {
            JSONObject jsonObject = new JSONObject();
            String finalprice = "0";
            try {
                if (!mArrayList.get(checkoutTime).getMRP().isEmpty()) {
                    if (Float.parseFloat(mArrayList.get(checkoutTime).getMRP()) > Float.parseFloat(mArrayList.get(checkoutTime).getSaleRate())) {
                        finalprice = "" + (Float.parseFloat(mArrayList.get(checkoutTime).getMRP()) - Float.parseFloat(mArrayList.get(checkoutTime).getSaleRate()));
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

            ;
            CartSendParams modle = new CartSendParams();
            try {
                jsonObject.put("ItemDesc", mArrayList.get(checkoutTime).getItemName());
                jsonObject.put("itemId", mArrayList.get(checkoutTime).getItemID() + "");
                jsonObject.put("CreatedBy", prefs.getPrefsContactId());
                jsonObject.put("DetailID", "0");
                jsonObject.put("CID", prefs.getPrefsContactId());
                jsonObject.put("OID", "0");
                jsonObject.put("Discount", finalprice);
                jsonObject.put("Type", "0");
                jsonObject.put("val1", "");
                jsonObject.put("Status", "0");
                jsonObject.put("sku", mArrayList.get(checkoutTime).getItemSKU() + "");
                jsonObject.put("RAmount", (Float.parseFloat(mArrayList.get(checkoutTime).getSaleRate()) * mArrayList.get(checkoutTime).getQuantity()) + "");
                jsonObject.put("Rate", mArrayList.get(checkoutTime).getSaleRate());
                jsonObject.put("barcode", mArrayList.get(checkoutTime).getItemBarcode());
                jsonObject.put("qty", mArrayList.get(checkoutTime).getQuantity() + "");
                jsonObject.put("MRP", mArrayList.get(checkoutTime).getMRP());
            } catch (Exception e) {
                e.printStackTrace();
            }

            jsonArray.put(jsonObject);
            modle.setItemDesc(mArrayList.get(checkoutTime).getItemName());
            modle.setItemId(mArrayList.get(checkoutTime).getItemID() + "");
            modle.setCreatedBy(prefs.getPrefsContactId());
            modle.setDetailID("0");
            modle.setCID(prefs.getPrefsContactId());
            modle.setOID("0");
            modle.setDiscount(finalprice);
            modle.setType("0");
            modle.setVal1("");
            modle.setStatus("0");
            modle.setSku(mArrayList.get(checkoutTime).getItemSKU() + "");
            modle.setRAmount((Float.parseFloat(mArrayList.get(checkoutTime).getSaleRate()) * mArrayList.get(checkoutTime).getQuantity()) + "");
            modle.setRate(mArrayList.get(checkoutTime).getSaleRate());
            modle.setBarcode(mArrayList.get(checkoutTime).getItemBarcode());
            modle.setQty(mArrayList.get(checkoutTime).getQuantity() + "");
            modle.setMRP(mArrayList.get(checkoutTime).getMRP());
            //  modle.setmDescription(finalparam);
            arrayList.add(modle);


        }
        strItems = jsonArray.toString()/*new Gson().toJson(arrayList)*/;
        Log.e("strddta", strItems);
        Log.e("strItems", strItems);
      /*  addItemsToDB(arrayList);

        uploadOrderItems();

*/


        Log.e("dsfkjjfk", AppController.getValTypeFromDesc("order_master"));
        final ProgressDialog mProgressBar = new ProgressDialog(CartActivity.this);
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
                            String responses = jkHelper.Decryptapi(response, CartActivity.this);
                            Log.e("delivresponses123", responses);
                            JSONObject jsonObject = new JSONObject(responses);
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                //for (int a=0;a<mArrayList.size();a++){
                                //"returnds":[{"OrderID":"6","successmsg":"Successful"}]
                                JSONArray jsonArray = jsonObject.getJSONArray("returnds");
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                orderId = jsonObject1.getString("OrderID");
                                if (!mDeliveryHelper.getDeliverytype().equalsIgnoreCase("CASH ON DELIVERY")) {
                                    PreviousAdjustedAmount = Double.parseDouble(txtPaybleAm1.getText().toString().replace(currency, ""));
//                                    String strrr = "" + (dblWalletAmount - (Double.parseDouble(txtTax1.getText().toString().replace(currency, "")) + Double.parseDouble(txtSubtotal.getText().toString().replace(currency, ""))) - Double.parseDouble(promotxn.getText().toString().replace(" ", "").replace(currency, "").replace("₹", "").replace("$", "")));
                                    String strrr = "" + (Double.parseDouble(prefs.getPREFS_currentbal()) - PreviousAdjustedAmount);
                                    Log.e("strrrstrrr", strrr);
                                    prefs.setPREFS_currentbal(strrr);
                                    dbManager.deleteAll();
                                    if (mPayhelper.getStringpaytype().equalsIgnoreCase("ONLINE PAYMENT")) {

                                        //   Toast.makeText(getApplicationContext(),mDeliveryHelper.getDeliverytype(),Toast.LENGTH_LONG).show();

                                        Utility.addMoneyToWallet(CartActivity.this, PreviousAdjustedAmount + "", orderId, "2");

                                    } else {
                                        //   Toast.makeText(getApplicationContext(),"Not hit",Toast.LENGTH_LONG).show();
                                    }
                                    //  prefs.setPREFS_currentbal("0.0");
                                }
//10+10 = 38
                                if (orderId != null) {
                                    if (mListLastRows.size() > 0) {

                                        updateserverphotoid();
                                        uploadimage();
                                    }
                                }

                                FancyToast.makeText(CartActivity.this, "Order Created Successfully.", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();

                                startActivity(new Intent(CartActivity.this, OrderSuccessActivity.class).putExtra("orderId", orderId).putExtra("totalamount", txtPaybleAm1.getText().toString()));
                                finish();
                                //  checkoutOrderDetails();
//}

                            } else {

                                FancyToast.makeText(CartActivity.this, "No Data Found, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
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
                        FancyToast.makeText(CartActivity.this, "Something went wrong with checkout.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


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
                prefs = new Preferencehelper(CartActivity.this);
                Map<String, String> params = new HashMap<String, String>();
                String tax, discount, servicecharge;

                String taxx = mData.getString("tax", "");
                String serviceFees = mData.getString("ServiceFees", "");
                String discountt = mData.getString("discount", "");

                try {
                    DecimalFormat formey = new DecimalFormat("0.00");

                    // test

//                    String param = getString(R.string.CREATE_MASTER_ORDER) + "&OID=" + "0" + "&CID=" + prefs.getPrefsContactId()
//                            + "&CustID=" + prefs.getPrefsContactId() + "&CompID=" + mData.getString("shopId", "1") + "&CreatedBy=" + prefs.getPrefsContactId()
//                            + "&RItemCount=" + dbManager.getTotalQty() + "&RAmount=" + txtPaybleAmt.getText().toString().replace(currency, "") + ""
//                            + "&Status=" + "0" + "&OType=" + "SO" + "&OrderDate=" + mTodayDate + "&Type=" + "0" + "&RandNumber=" + "0" + "&DeliveryMode=" + deliveryval + ""
//                            + "&PaymentMode=" + "817417" + "" + "&PhoneforOTP=" + contacttxt.getText().toString() + "" + "&Comment=" + comment.getText().toString().replace("\n", "") + "" + "&TimeSlotDate=" + datedelivery.getText().toString() + "&TimeSLotTime=" + strTimeSlot + "&PreviousAdjustedAmount=" + dblWalletAmount + "&DeliveryCharge=" + dblDeliveryCharges + "&CurrentPaidAmount=" + txtPaybleAmt.getText().toString().replace(currency, "") + "&PaymentTxnId=" + "0" + "&Tax=" + txtTax1.getText().toString().replace("$", "").replace("₹", "").trim() + "&Discount=" + discountt + "&ServiceCharge=" + serviceFees + "&Address=" + deliveryaddval.getText().toString() + "&PaymentType=" + strPaymentMode;
//                    ;


                    //Production

                    String param = getString(R.string.CREATE_MASTER_ORDER) + "&OID=" + oid + "&CID=" + prefs.getPrefsContactId()
                            + "&CustID=" + prefs.getPrefsContactId() + "&CompID=" + prefs.getCID() + "&CreatedBy=" + prefs.getPrefsContactId()
                            + "&RItemCount=" + dbManager.getTotalQty() + "&RAmount=" + txtPaybleAm1.getText().toString().replace(currency, "") + ""
                            + "&Status=" + "0" + "&OType=" + "SO" + "&OrderDate=" + mTodayDate + "&Type=" + "2" + "&RandNumber=" + "0" + "&DeliveryMode=" + deliveryval + ""
                            + "&PaymentMode=" + payval + "" + "&PhoneforOTP=" + contacttxt.getText().toString() + "" + "&Comment=" + comment.getText().toString().replace("\n", "") +
                            "" + "&TimeSlotDate=" + datedelivery.getText().toString() + "&TimeSLotTime=" + strTimeSlot + "&PreviousAdjustedAmount=" + PreviousAdjustedAmount + "&DeliveryCharge=" +
                            dblDeliveryCharges + "&CurrentPaidAmount=" + txtPaybleAm1.getText().toString().replace(currency, "") + "&PaymentTxnId=" + strTxnId +
                            "&Tax=" + txtTax1.getText().toString().replace("$", "").replace("₹", "").trim() + "&Discount=" + txtmSavings.getText().toString().replace(currency, "")/*discountt*/ + "&ServiceCharge=" + serviceFees +
                            "&Address=" + deliveryaddval.getText().toString() + "&PaymentType=" + strPaymentMode + "&EntryFrom=2" + "&PromoCode=" + promostr + "&DeliveryAddLat=" + deliveryaddlat + "&DeliveryAddLong=" + deliveryaddlong + "&ADiscount=" + finalpercentpromo + "&HeaderTotalTaxAmount=" + finaltaxamount.toString() + "&HeaderTotalTaxRate=" + finaltaxrate.toString();
                    ;
                    Log.d("BeforeencrptionCHECKOUT", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, CartActivity.this);
                    params.put("val", finalparam);
                    Log.d("afterencrptionCHECKOUT", finalparam);
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

    public void logdata() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String mTodayDate = df.format(c.getTime());


        Preferencehelper prefs;
        prefs = new Preferencehelper(CartActivity.this);
        Map<String, String> params = new HashMap<String, String>();


        try {
            DecimalFormat formey = new DecimalFormat("0.00");


            String param = getString(R.string.CREATE_MASTER_ORDER) + "&OID=" + "0" + "&CID=" + prefs.getPrefsContactId()
                    + "&CustID=" + prefs.getPrefsContactId() + "&CompID=" + mData.getString("shopId", "1") + "&CreatedBy=" + prefs.getPrefsContactId()
                    + "&RItemCount=" + dbManager.getTotalQty() + "&RAmount=" + formey.format(mTotalPrice) + ""
                    + "&Status=" + "0" + "&OType=" + "SO" + "&OrderDate=" + mTodayDate + "&Type=" + "0" + "&RandNumber=" + "0" + "&DeliveryMode=" + deliveryval + ""
                    + "&PaymentMode=" + payval + "" + "&PhoneforOTP=" + contacttxt.getText().toString() + "" + "&Comment=" + comment.getText().toString() + "" + "&TimeSlotDate=" + datedelivery.getText().toString() + "&TimeSLotTime=" + timeslotval + "&PreviousAdjustedAmount=" + mPreAmt + "&DeliveryCharge=" + mDlvrAmt + "&CurrentPaidAmount=" + amt + "&HeaderTotalTaxAmount=" + finaltaxamount.toString() + "&HeaderTotalTaxRate=" + finaltaxrate.toString();
            Log.e("myparamsb", param);
            JKHelper jkHelper = new JKHelper();
            String finalparam = jkHelper.Encryptapi(param, CartActivity.this);
            params.put("val", finalparam);
            Log.e("myparamsa", finalparam);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkoutOrderDetails() {


        final ProgressDialog mProgressBar = new ProgressDialog(CartActivity.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, CartActivity.this);
                            Log.e("KJHASJ", responses);
                            JSONObject jsonObject = new JSONObject(responses);
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                checkoutTime++;
                                if (checkoutTime < mArrayList.size()) {
                                    checkoutOrderDetails();
                                } else {
                                    mProgressBar.dismiss();
                                    dbManager.deleteAll();
                                    FancyToast.makeText(CartActivity.this, "Order Created Successfully.", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();

                                    startActivity(new Intent(CartActivity.this, OrderSuccessActivity.class).putExtra("orderId", orderId));
                                    finish();

                                       /*  final Dialog dialog = new Dialog(CartActivity.this);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                    dialog.setContentView(R.layout.dialog_update_app);
                                    dialog.setCancelable(false);
                                    final Button submit = (Button) dialog.findViewById(R.id.btnupdate);
                                    final TextView tvdesc = (TextView) dialog.findViewById(R.id.tvdesc);
                                    tvdesc.setText("Order created Successfully #" + orderId);
                                    submit.setText("Continue");
                                    submit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            //  startActivity(new Intent(CartActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                            finish();
                                        }
                                    });

                                    // Display the custom alert dialog on interface
                                    dialog.show();

*/
                                }
                            } else {
                                checkoutOrderDetails();

                                FancyToast.makeText(CartActivity.this, "No Data Found, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                            }

                        } catch (Exception e) {
                            checkoutOrderDetails();
                            e.printStackTrace();
                            mProgressBar.dismiss();
                        }


                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressBar.dismiss();
                        FancyToast.makeText(CartActivity.this, "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Preferencehelper prefs;
                prefs = new Preferencehelper(CartActivity.this);

                Map<String, String> params = new HashMap<String, String>();
                String finalprice = "0";
                try {
                    if (mArrayList.get(checkoutTime).getMRP().isEmpty()) {
                        if (Float.parseFloat(mArrayList.get(checkoutTime).getMRP()) > Float.parseFloat(mArrayList.get(checkoutTime).getSaleRate())) {
                            finalprice = "" + (Float.parseFloat(mArrayList.get(checkoutTime).getMRP()) - Float.parseFloat(mArrayList.get(checkoutTime).getSaleRate()));
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
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);

                    String param = getString(R.string.CREATE_ORDER_DETAIL) + "&OID=" + orderId + "&CID=" + prefs.getPrefsContactId()
                            + "&DetailID=" + "0" + "&CreatedBy=" + prefs.getPrefsContactId() + "&ItemID=" + mArrayList.get(checkoutTime).getItemID() + ""
                            + "&ItemDesc=" + mArrayList.get(checkoutTime).getItemName() + "" + "&RQty=" + mArrayList.get(checkoutTime).getQuantity() + ""
                            + "&MRP=" + mArrayList.get(checkoutTime).getMRP() + "&UOM=" + mArrayList.get(checkoutTime).getSaleUOM() + "&Rate=" + mArrayList.get(checkoutTime).getSaleRate() +
                            "&RAmount=" + (Float.parseFloat(mArrayList.get(checkoutTime).getSaleRate()) * mArrayList.get(checkoutTime).getQuantity()) + ""
                            + "&Status=" + "0" + "&Type=" + "0" + "&Discount=" + finalprice;
                    Log.d("Beforeencrptioncreate", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, CartActivity.this);
                    params.put("val", finalparam);
                    Log.d("afterencrptioncreate", finalparam);
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

    public void makeBackClick(View v) {
        startActivity(new Intent(CartActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }

    public void makeContinueClick(View v) {
        startActivity(new Intent(CartActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }

    CartCountView countView;
    StoreView storeView;


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        MenuItem item = menu.findItem(R.id.cart);
        MenuItem item1 = menu.findItem(R.id.shop);
        item.setActionView(countView);
        item1.setActionView(storeView);
        item1.setVisible(false);
        MenuItem login = menu.findItem(R.id.login);
        try {
            if (prefs.getPrefsPaymentoption().equalsIgnoreCase("1")) {

            } else {

            }
            if (prefs.getPrefsContactId().isEmpty()) {
                login.setVisible(true);
            } else {
                login.setVisible(false);

            }
        } catch (Exception e) {
            login.setVisible(true);

        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        showaddresslist();
        loadProfileDataUser();
        try {
            txt_set.setText("Safe'O'Fresh");
            backbtn_layout.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        prefs11 = getSharedPreferences("payment_prfs",
                MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs11.edit();
        Log.e("ISSOKKK", prefs11.getString("isOK", "0"));

        if (prefs11.getString("isOK", "0").equalsIgnoreCase("1")) {
            Log.e("PAYMENTIDDD", prefs11.getString("mid", "0"));
            String payableamount = txtPaybleAmt.getText().toString().replace("$", "").replace("₹", "");
            loadLinks(prefs11.getString("mid", "0"), new DecimalFormat("##.##").format(Double.parseDouble(payableamount)));
        }


    }

    public void loadLinks(String src, String val1) {
        ProgressDialog progressDialog = new ProgressDialog(CartActivity.this);
        progressDialog.setTitle("PAYMENT");
        progressDialog.setMessage("loading...");
        progressDialog.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, "https://Vehicletrack.membocool.com?method=CreatePaymentWithSource ",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        progressDialog.dismiss();
                        Log.e("Response", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            mTXNID = jsonObject.getJSONArray("returnmessage").getJSONObject(0).getString("PaymentId");
                            strTxnId = jsonObject.getJSONArray("returnmessage").getJSONObject(0).getString("PaymentId");
                       /*     androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(CartActivity.this);
                            builder.setMessage("Payment Done Successfully\n Txn Id : " + jsonObject.getString("e_key"))
                                    .setCancelable(false)
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            startActivity(new Intent(CartActivity.this,MainActivity.class));
                                            finish();
                                            dialogInterface.dismiss();

                                        }
                                    })
                                    .create()
                                    .show();*/


                            checkoutOrder();


                        } catch (Exception e) {
                            e.printStackTrace();


                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(CartActivity.this);
                            builder.setMessage("failed to make payment")
                                    .setCancelable(false)
                                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            startActivity(new Intent(CartActivity.this, CreateCardPaymentMethodActivity.class).putExtra("price", new DecimalFormat("##.##").format(Double.parseDouble(txtPaybleAmt.getText().toString().replace(currency, "")))));

                                        }
                                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();

                                }
                            })
                                    .create()
                                    .show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();       // error

                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(CartActivity.this);
                        builder.setMessage("failed to make payment")
                                .setCancelable(false)
                                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();

                            }
                        })
                                .create()
                                .show();
                        //   Log.e("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("amt", val1);
                params.put("custId", preferencehelper.getPrefsCustId());
                params.put("source", src);
                params.put("paycat", preferencehelper.getPrefsPaymentoption());
                params.put("currency", "USD");
/*
   params.put("amount", val1);
                params.put("cust_id", "cus_HJ1CkRMUM6HqG7");
                params.put("source", src);
                params.put("currency", "USD");
*/

                return params;

            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(CartActivity.this).add(postRequest);


    }

    ProgressDialog progressDialog;


    public void ErrorMsg(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
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
        prefs = PreferenceManager.getDefaultSharedPreferences(CartActivity.this);
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
                    dblWalletAmount = Double.parseDouble(prefs.getPREFS_currentbal());
                    calculateTotalAmount();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (requestCode == 5567) {
                try {
                    String strWalletAmount = data.getStringExtra("mWalletAmount");
                    dblWalletAmount = Double.parseDouble(prefs.getPREFS_currentbal());
                    calculateTotalAmount();
                    loadProfileDataUser();
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
                mPreAmt = Double.parseDouble(prefs.getPREFS_currentbal());
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
      /*  if (mNonce != null) {
            outState.putParcelable(KEY_NONCE, mNonce);
        }*/
    }

    DeliveryHelper mDeliveryHelper;

    @Override
    public void senddeliveryval(DeliveryHelper res) {
        mDeliveryHelper = res;
        deliveryval = res.getDeliveryid();


        if (mDeliveryHelper.getDeliverytype().equalsIgnoreCase("HOME DELIVERY")) {
            //   dlvryL.setVisibility(View.VISIBLE);
            // deliveryaddll.setVisibility(View.VISIBLE);
            mDlvrAmt = Double.parseDouble(df2.format(Double.parseDouble(mData.getString("DeliveryCharge", "0.0"))));
            txtSubtotal.setText(strCrncy + "" + df2.format(mTotalPrice + mDlvrAmt + Double.parseDouble(mData.getString("ServiceFees", "0.0"))));
            setData();
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

    public static void initiateStripe1() {
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
    public void makeclick(String click, String latstr, String longstr) {

        dialogbottom.dismiss();
        if (click.isEmpty()) {

        } else {

            if (TextUtils.isEmpty(prefs.getPrefsZipCode())) {

                deliveryaddval.setText(click);
                deliveryaddlat = latstr;
                deliveryaddlong = longstr;


            } else {

                deliveryaddval.setText(click);
            }
        }


    }

    @Override
    public void changetextcart(ArrayList<CartProduct> cartList, int pos) {

        CartProduct product = cartList.get(pos);


        adapter.notifyDataSetChanged();
        dbManager.update(product);
        Log.d("product mrp", product.getMRP());

        calculateTotal(cartList);
        try {
            Utility.checkoutOrder(cartList.get(pos), preferencehelper.getPrefsContactId(), getApplicationContext(), "2");


            countView.setCount(Integer.parseInt(txttotalitems.getText().toString()) + cartList.get(pos).getQuantity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // txttotalitems.setText(dbManager.getTotalQty()+cartList.get(pos).getQuantity());
        setCartCount();

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

                        ImageMasterDAO dao = new ImageMasterDAO(database, CartActivity.this);
                        ArrayList<DbImageMaster> list = arrayList;
                        dao.insert(list);
                        addedToMasterTable = true;
                    }
                });
            }


            DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                @Override
                public void run(SQLiteDatabase database) {
                    ImageMasterDAO dao = new ImageMasterDAO(database, CartActivity.this);
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
                    ImageUploadDAO dao = new ImageUploadDAO(database, CartActivity.this);
                    ArrayList<DbImageUpload> list = arrayListUpload;
                    dao.insert(list);

                    log.e("photo inserted ");
                }
            });
            //    setPhotoCount();


        }
    }

    private void uploadimage() {
        if (JKHelper.isConnectedToNetwork(CartActivity.this) && !JKHelper.isServiceRunning(CartActivity.this, ImageUploadService.class)) {
            JKHelper.showLog("Start Service");
            startService(new Intent(CartActivity.this, ImageUploadService.class));
        } else {
            stopService(new Intent(CartActivity.this, ImageUploadService.class));
            startService(new Intent(CartActivity.this, ImageUploadService.class));
        }
    }

    private void updateserverphotoid() {
        for (int a = 0; a < mListLastRows.size(); a++) {
            if (mListLastRows.get(a) != null) {
                SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
                ImageUploadDAO pd = new ImageUploadDAO(database, CartActivity.this);
                pd.setWorkIdToTable(String.valueOf(orderId), mListLastRows.get(a));
                ImageMasterDAO pds = new ImageMasterDAO(database, CartActivity.this);
                pds.setServerDetails(Integer.parseInt(mListLastRows.get(a)), Integer.valueOf(orderId), 0);
                ImageMasterDAO iDao = new ImageMasterDAO(database, getApplicationContext());
                String serverId = iDao.getServerIdById(1);
                DatabaseManager.getInstance().closeDatabase();
                log.e("in last row master table inserting  ");
            }


        }


    }


    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    String TAG = "SAFEOBUDDYIMAGE";
    public static final int REQUEST_IMAGE = 100;


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

    //Open gallery To Select Image
    private void launchGalleryIntent() {
        Intent intent = new Intent(CartActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);
        startActivityForResult(intent, REQUEST_IMAGE);
    }


    public void panelcleandatepicker() {


        DatePickerDialog datePickerDialog = new DatePickerDialog(CartActivity.this,
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
                }, mYear, mMonth , mDay);

        datePickerDialog.show();
        Calendar c = Calendar.getInstance();
        c.set(mYear, mMonth , mDay);
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

    private static DecimalFormat df2 = new DecimalFormat("#.##");
/*

    private DropInRequest getDropInRequest() {
        amt = df2.format(mTotalPrice - mPreAmt + mDlvrAmt + Double.parseDouble(mData.getString("ServiceFees", "0.0")));
        //  Log.e("mAuthorization",mAuthorization);
        GooglePaymentRequest googlePaymentRequest = new GooglePaymentRequest()
                .transactionInfo(TransactionInfo.newBuilder()
                        .setCurrencyCode(Settings.getGooglePaymentCurrency(this))
                        .setTotalPrice(amt)
                        .setTotalPriceStatus(WalletConstants.TOTAL_PRICE_STATUS_FINAL)
                        .build())
                .allowPrepaidCards(Settings.areGooglePaymentPrepaidCardsAllowed(this))
                .billingAddressFormat(WalletConstants.BILLING_ADDRESS_FORMAT_FULL)
                .billingAddressRequired(Settings.isGooglePaymentBillingAddressRequired(this))
                .emailRequired(Settings.isGooglePaymentEmailRequired(this))
                .phoneNumberRequired(Settings.isGooglePaymentPhoneNumberRequired(this))
                .shippingAddressRequired(Settings.isGooglePaymentShippingAddressRequired(this))
                .shippingAddressRequirements(ShippingAddressRequirements.newBuilder()
                        .addAllowedCountryCodes(Settings.getGooglePaymentAllowedCountriesForShipping(this))
                        .build())
                .googleMerchantId(Settings.getGooglePaymentMerchantId(this));

        return new DropInRequest()
                .amount(amt)
                .clientToken("sandbox_w39t9n4p_xz96ptbw85h9k33y")
                .collectDeviceData(Settings.shouldCollectDeviceData(this))
                .requestThreeDSecureVerification(Settings.isThreeDSecureEnabled(this))
                .googlePaymentRequest(googlePaymentRequest);
    }
*/

    protected void showDialog(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    /*   private void displayNonce(PaymentMethodNonce paymentMethodNonce, String deviceData) {
           mNonce = paymentMethodNonce;
           String strr = "Nonce : " + mNonce.getNonce() + "\n";
           String details = "";

           details = PayPalActivity.getDisplayString((PayPalAccountNonce) mNonce);


           strr = strr + "details : " + details + "\ndevice data : " + deviceData + "\nAmount : " + amt;
           Log.e("mNNCE", strr);
           //txt.setText(strr);


         *//*  AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
        builder.setMessage(strr);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
*//*
        amt = mTotalPrice - mPreAmt + "";
        amt = df2.format(mTotalPrice - mPreAmt + mDlvrAmt + Double.parseDouble(mData.getString("ServiceFees", "0.0")));
        Intent intent = new Intent(CartActivity.this, CreateTransactionActivity.class).putExtra("amtamt", amt)
                .putExtra(CreateTransactionActivity.EXTRA_PAYMENT_METHOD_NONCE, mNonce);
        startActivityForResult(intent, 8766);


      *//*      }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
*//*

        //  clearNonce();
    }
*/
    private void addItemsToDB(ArrayList<DBCartDataUpload> arrayList) {


        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {

                CartOrderDAO dao = new CartOrderDAO(database, CartActivity.this);
                ArrayList<DBCartDataUpload> list = arrayList;
                dao.insert(list);
                addedToMasterTable = true;
            }
        });


        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                CartOrderDAO dao = new CartOrderDAO(database, CartActivity.this);
                log.e("item string id ============== " + dao.getlatestinsertedid());
            }
        });


    }

    private void uploadOrderItems() {
        if (JKHelper.isConnectedToNetwork(CartActivity.this) && !JKHelper.isServiceRunning(CartActivity.this, OrderUploadService.class)) {
            JKHelper.showLog("Start Service");
            startService(new Intent(CartActivity.this, OrderUploadService.class));
        } else {
            stopService(new Intent(CartActivity.this, OrderUploadService.class));
            startService(new Intent(CartActivity.this, OrderUploadService.class));
        }
    }

    public void submitCard(View view) {
        if (creditCardEditText.getText().toString().isEmpty() || creditCardEditText.getText().toString().length() < 16) {
            FancyToast.makeText(CartActivity.this, "Please enter valid card detail", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

        } else if (expirationEditText.getText().toString().isEmpty() || expirationEditText.getText().toString().length() < 5) {
            FancyToast.makeText(CartActivity.this, "Please enter valid card detail", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

        } else if (mEditTextCardCVV.getText().toString().isEmpty() || mEditTextCardCVV.getText().toString().length() < 3) {
            FancyToast.makeText(CartActivity.this, "Please enter valid cvv number", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

        } else {
            String payableamount = txtPaybleAmt.getText().toString().replace("$", "");
            if (strTimeSlot == null || strTimeSlot.equalsIgnoreCase("")) {
                FancyToast.makeText(CartActivity.this, "time slot is not select or either there is no time slot available for this delivery", FancyToast.LENGTH_LONG, FancyToast.INFO, false).show();
            } else {
                startActivity(new Intent(CartActivity.this, CreateCardPaymentMethodActivity.class).putExtra("price", new DecimalFormat("##.##").format(Double.parseDouble(txtPaybleAmt.getText().toString().replace(currency, "")))));

            }
            //  mTXNID = "pm_1GkSloH1guOjtEKoHcxvmRHN";
            //   strTxnId ="pm_1GkSloH1guOjtEKoHcxvmRHN";
            //   checkoutOrder();

/*
      final ProgressDialog mProgressBar = new ProgressDialog(CartActivity.this);
            mProgressBar.setTitle("Safe'O'Fresh");
            mProgressBar.setMessage("Loading...");
            mProgressBar.show();
            String strrr[] = expirationEditText.getText().toString().split("/");

            // TODO: replace with your own test key

            card = new Card(
                    creditCardEditText.getText().toString(),
                    Integer.valueOf(strrr[0]),
                    Integer.valueOf(strrr[1]),
                    mEditTextCardCVV.getText().toString()
            );

            card.setCurrency("usd");
            card.setName(preferencehelper.getPREFS_firstname());
            card.setAddressZip("302013");


            stripe.createToken(card, "pk_test_EbCpH358aTH3p8TSFGJf1aCe00VuQ0Q8qB", new TokenCallback() {
                public void onSuccess(Token token) {
                    // TODO: Send Token information to your backend to initiate a charge
                    // Toast.makeText(getApplicationContext(), "Token created: " + token.getId(), Toast.LENGTH_LONG).show();
                    tok = token;
                    Log.e("JHGHJ", tok.getId() + " " + amt);
                    mProgressBar.dismiss();
                    String payableamount=txtPaybleAmt.getText().toString().replace("$","");
                    DecimalFormat formey = new DecimalFormat("0.00");
                    makePayment(token.getId(),new DecimalFormat("##.##").format(Double.parseDouble(payableamount)));

                }

                public void onError(Exception error) {
                    mProgressBar.dismiss();
                    Log.d("Stripe", error.getLocalizedMessage());
                }
            });
*/
        }
    }


    public void makePayment(String mToken, String payabval) {

        final ProgressDialog mProgressBar = new ProgressDialog(CartActivity.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://mindmuddy.com/PayServ.aspx?method=PService",


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //   Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();

//                        mProgressBar.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("returnmessage");
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                mTXNID = jsonObject1.getString("BalanceTransactionId");
                                strTxnId = jsonObject1.getString("BalanceTransactionId");
                                checkoutOrder();
                            } else {
                                FancyToast.makeText(CartActivity.this, "Payment failed try again .", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

                            }
                        } catch (Exception e) {
                        }


                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressBar.dismiss();
                        FancyToast.makeText(CartActivity.this, "Something went wrong with pay api.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                params.put("stripeToken", mToken);
                params.put("strAmount", payabval);
                params.put("stripeEmail", preferencehelper.getPREFS_email2().toString());
                Log.d("afterencrption", params.toString());
                return params;


            }
        };
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(
//                60000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(this).add(postRequest);


    }

    ///////////////////////////////Paytm /////////////////////////////
    public String initOrderId() {
        Random r = new Random(System.currentTimeMillis());
        String orderId = "ORDER" + (1 + r.nextInt(2)) * 10000
                + r.nextInt(10000);

        return orderId;
    }

    public void moveToTransection() {
        mOrderId = initOrderId() + "_user_" + new Preferencehelper(CartActivity.this).getPrefsContactId();
        mCustId = new Preferencehelper(CartActivity.this).getPrefsContactId();
        generateCheckSum();

    }

    String mOrderId = "";
    String mCustId = "";


    public void onStartTransaction(String checksumHash, com.dhanuka.morningparcel.demo.Paytm paytm) {
        PaytmPGService Service = PaytmPGService.getProductionService();

        /* PaytmPGService Service = PaytmPGService.getStagingService();
         */
        HashMap<String, String> paramMap = new HashMap<String, String>();

        // these are mandatory parameters
        paramMap.put("MID", Constant.M_ID);
        paramMap.put("ORDER_ID", mOrderId);
        paramMap.put("CUST_ID", mCustId);
        paramMap.put("CHANNEL_ID", paytm.getChannelId());
        paramMap.put("TXN_AMOUNT", paytm.getTxnAmount());
        paramMap.put("WEBSITE", paytm.getWebsite());
        paramMap.put("CALLBACK_URL", paytm.getCallBackUrl() + mOrderId);
        paramMap.put("INDUSTRY_TYPE_ID", paytm.getIndustryTypeId());
        paramMap.put("CHECKSUMHASH", checksumHash);


        PaytmOrder Order = new PaytmOrder(paramMap);
        Log.e("TRANS_PRMS", paramMap.toString());





/*

        paramMap.put("MID" , "WorldP64425807474247");
        paramMap.put("ORDER_ID" , "210lkldfka2a27");
        paramMap.put("CUST_ID" , "mkjNYC1227");
        paramMap.put("INDUSTRY_TYPE_ID" , "Retail");
        paramMap.put("CHANNEL_ID" , "WAP");
        paramMap.put("TXN_AMOUNT" , "1");
        paramMap.put("WEBSITE" , "worldpressplg");
        paramMap.put("CALLBACK_URL" , "https://pguat.paytm.com/paytmchecksum/paytmCheckSumVerify.jsp");*/



		/*PaytmMerchant Merchant = new PaytmMerchant(
				"https://pguat.paytm.com/paytmchecksum/paytmCheckSumGenerator.jsp",
				"https://pguat.paytm.com/paytmchecksum/paytmCheckSumVerify.jsp");*/

        Service.initialize(Order, null);
/*

Array ( [MID] => ShriRa53937508289530
[ORDER_ID] => testab123 [CUST_ID] => CUST1234 [INDUSTRY_TYPE_ID] => Retail [CHANNEL_ID] => WAP [TXN_AMOUNT] => 1.00
 [WEBSITE] => APPSTAGING
 [CALLBACK_URL] => https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=testab123
 [CHECKSUMHASH] => INbxKqQWzUov2mAPGxj9CINwo4eTQ+x1oY0M4Ko01ET3bZna57tk8o1xfed4R6mm8HQb9FqiU9j1KprImdUC0E9PPDKj/9QrGZrWpu4wlX4= )







        {MID=ShriRa53937508289530,
        CALLBACK_URL=https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=c4512e1de13440a6901c2dbea4dee85f,
         TXN_AMOUNT=1, ORDER_ID=c4512e1de13440a6901c2dbea4dee85f, WEBSITE=APPSTAGING ,
          INDUSTRY_TYPE_ID=Retail, CHECKSUMHASH=IYDZws4WV2ewhs8O9pGCsMjzc9JGuYacCGpiZDu5CKMhNRMf8lLyvraOiqeAerD1ftI4iKTFDk+QyM7L2UNQeM26R3YMsRrmqMZRuyYnk5c=,
          CHANNEL_ID=WAP, CUST_ID=db77ade9cca449ada3aea7551ca4a718}
*/

        Service.startPaymentTransaction(CartActivity.this, true, true,
                new PaytmPaymentTransactionCallback() {
                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {
                        // Some UI Error Occurred in Payment Gateway Activity.
                        // // This may be due to initialization of views in
                        // Payment Gateway Activity or may be due to //
                        // initialization of webview. // Error Message details
                        // the error occurred.
                    }

					/*@Override
					public void onTransactionSuccess(Bundle inResponse) {
						// After successful transaction this method gets called.
						// // Response bundle contains the merchant response
						// parameters.
						Log.d("LOG", "Payment Transaction is successful " + inResponse);
						Toast.makeText(getApplicationContext(), "Payment Transaction is successful ", Toast.LENGTH_LONG).show();
					}

					@Override
					public void onTransactionFailure(String inErrorMessage,
							Bundle inResponse) {
						// This method gets called if transaction failed. //
						// Here in this case transaction is completed, but with
						// a failure. // Error Message describes the reason for
						// failure. // Response bundle contains the merchant
						// response parameters.
						Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);
						Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
					}*/

                    @Override
                    public void onTransactionResponse(Bundle inResponse) {
                        Log.d("LOG", "Payment Transaction is successful " + inResponse);
//                        Toast.makeText(getApplicationContext(), "Payment Transaction response " + inResponse.toString(), Toast.LENGTH_LONG).show();

                        try {
                      /*      String resp = inResponse.toString();
                            JSONArray jsonArray = new JSONArray(resp.replace("Bundle", ""));
                            JSONObject object = jsonArray.getJSONObject(0);
*/


                            if (inResponse.get("STATUS").toString().equalsIgnoreCase("TXN_SUCCESS")) {
                                strTxnId = inResponse.get("TXNID").toString();

                                checkoutOrder();
                            } else {
                                ErrorMsg(inResponse.get("RESPMSG").toString());

                            }       //[{STATUS=TXN_SUCCESS, BANKNAME=STATE BANK OF INDIA, ORDERID=ORDER10000211, TXNAMOUNT=1.00, TXNDATE=2019-01-28 14:38:08.0, MID=ShriRa53937508289530, TXNID=20190128111212800110168492800195667, RESPCODE=01, PAYMENTMODE=DC, BANKTXNID=4036217121962950, CURRENCY=INR, GATEWAYNAME=HDFC, RESPMSG=Txn Success}]


                        } catch (Exception e) {
                            e.printStackTrace();
                            ErrorMsg("Failed to Proceed The Order. please Contact The Admin");

                        }
                    }

                    @Override
                    public void networkNotAvailable() { // If network is not
                        // available, then this
                        // method gets called.
                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {
                        // This method gets called if client authentication
                        // failed. // Failure may be due to following reasons //
                        // 1. Server error or downtime. // 2. Server unable to
                        // generate checksum or checksum response is not in
                        // proper format. // 3. Server failed to authenticate
                        // that client. That is value of payt_STATUS is 2. //
                        // Error Message describes the reason for failure.
//                        Toast.makeText(getApplicationContext(), "Payment Transaction response " + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
                        ErrorMsg("Transection Failed, Unable to Proceed The Order.");


                    }

                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode,
                                                      String inErrorMessage, String inFailingUrl) {
                        // Error Message describes the reason for failure.
//                        Toast.makeText(CheckoutActivity.this, "Payment Transaction response " + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
                        ErrorMsg("Transection Failed, Unable to Proceed The Order.");

                    }

                    // had to be added: NOTE
                    @Override
                    public void onBackPressedCancelTransaction() {
                        Toast.makeText(CartActivity.this, "Back pressed. Transaction cancelled", Toast.LENGTH_LONG).show();
                        ErrorMsg("Transaction cancelled By you");
                    }

                    @Override
                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                        Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);

                        ErrorMsg("Transection Failed, Unable to Proceed The Order.");
                        //Toast.makeText(CheckoutActivity.this, "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
                    }

                });
    }


    /*   public void generateCheckSum() {

        //getting the tax amount first.
        //    progressDialog = ProgressDialog.show(CartActivity.this, "FarmKey", "Processing...");
        //  progressDialog.setCancelable(false);

        //creating a retrofit object.
         //creating the retrofit api service
        //  Api apiService = retrofit.create(Api.class);

        //creating paytm object
        //containing all the values required
        final Paytm paytm = new Paytm(
                Constant.M_ID,
                Constant.CHANNEL_ID,
                txtPaybleAmt.getText().toString().replace(currency, ""),
                Constant.WEBSITE,
                Constant.CALLBACK_URL + mOrderId,
                Constant.INDUSTRY_TYPE_ID
        );

        Log.e("CHK_PRM", paytm.getmId() + "\n" +
                mOrderId + "\n" +
                mCustId + "\n" +
                paytm.getChannelId() + "\n" +
                paytm.getTxnAmount() + "\n" +
                paytm.getWebsite() + "\n" +
                paytm.getCallBackUrl() + mOrderId + "\n" +
                paytm.getIndustryTypeId());


        final ProgressDialog mProgressBar = new ProgressDialog(CartActivity.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://app-editor.farmykey.in/agriculture/admin799/api/generateChecksumG.php",
*//*
        StringRequest postRequest = new StringRequest(Request.Method.POST,
                "https://Vehicletrack.membocool.com?method=GetCheckSUM",
*//*


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //   Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();

                        mProgressBar.dismiss();

                        try {
                            Log.e("CHCKRESP", "&MID=" + paytm.getmId() +
                                    "&ORDER_ID=" + mOrderId +
                                    "&CUST_ID=" + mCustId +
                                    "&CHANNEL_ID=" + paytm.getChannelId() +
                                    "&TXN_AMOUNT=" + paytm.getTxnAmount() +
                                    "&WEBSITE=" + paytm.getWebsite() +
                                    "&CALLBACK_URL=" + paytm.getCallBackUrl() + mOrderId +
                                    "&INDUSTRY_TYPE_ID=" + paytm.getIndustryTypeId());
                            JSONObject jsonObject = new JSONObject(response);

                            onStartTransaction(jsonObject.getString("CHECKSUMHASH"), paytm);


                        } catch (Exception e) {
                        }


                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressBar.dismiss();
                        FancyToast.makeText(CartActivity.this, "Something went wrong with pay api.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                 params.put("MID", paytm.getmId());
                params.put("ORDER_ID", mOrderId);
                params.put("CUST_ID", mCustId);
                params.put("CHANNEL_ID", paytm.getChannelId());
                params.put("TXN_AMOUNT", paytm.getTxnAmount());
                params.put("WEBSITE", paytm.getWebsite());
                params.put("CALLBACK_URL", paytm.getCallBackUrl());
                params.put("INDUSTRY_TYPE_ID", paytm.getIndustryTypeId());


 

                Log.d("afterencrption", params.toString());
                return params;


            }
        };

        Volley.newRequestQueue(this).add(postRequest);


    }


    public void onStartTransaction(String checksumHash, Paytm paytm) {
        PaytmPGService Service = PaytmPGService.getProductionService();

       
        HashMap<String, String> paramMap = new HashMap<String, String>();

        // these are mandatory parameters
        paramMap.put("MID", Constant.M_ID);
        paramMap.put("ORDER_ID", mOrderId);
        paramMap.put("CUST_ID", mCustId);
        paramMap.put("CHANNEL_ID", paytm.getChannelId());
        paramMap.put("TXN_AMOUNT", paytm.getTxnAmount());
        paramMap.put("WEBSITE", paytm.getWebsite());
        paramMap.put("CALLBACK_URL", paytm.getCallBackUrl() + mOrderId);
        paramMap.put("INDUSTRY_TYPE_ID", paytm.getIndustryTypeId());
        paramMap.put("CHECKSUMHASH", checksumHash);


         PaytmOrder Order = new PaytmOrder(paramMap);
        Log.e("TRANS_PRMS", paramMap.toString());


 

        Service.initialize(Order, null);
        

        Service.startPaymentTransaction(CartActivity.this, true, true,
                new PaytmPaymentTransactionCallback() {
                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {
                        // Some UI Error Occurred in Payment Gateway Activity.
                        // // This may be due to initialization of views in
                        // Payment Gateway Activity or may be due to //
                        // initialization of webview. // Error Message details
                        // the error occurred.
                    }

 
                    @Override
                    public void onTransactionResponse(Bundle inResponse) {
                        Log.d("LOG", "Payment Transaction is successful " + inResponse);
//                        Toast.makeText(getApplicationContext(), "Payment Transaction response " + inResponse.toString(), Toast.LENGTH_LONG).show();

                        try {
 

                            if (inResponse.get("STATUS").toString().equalsIgnoreCase("TXN_SUCCESS")) {
                                //  strTxnId = inResponse.get("TXNID").toString();
                                //  new AsyncSubmitOrder().execute();
                                strTxnId = inResponse.get("TXNID").toString();
                                mTXNID = inResponse.get("TXNID").toString();
                                //       ErrorMsg("Your Transaction id : "+inResponse.get("TXNID").toString());
                                checkoutOrder();
                            } else {
                                ErrorMsg(inResponse.get("RESPMSG").toString());

                            }       //[{STATUS=TXN_SUCCESS, BANKNAME=STATE BANK OF INDIA, ORDERID=ORDER10000211, TXNAMOUNT=1.00, TXNDATE=2019-01-28 14:38:08.0, MID=ShriRa53937508289530, TXNID=20190128111212800110168492800195667, RESPCODE=01, PAYMENTMODE=DC, BANKTXNID=4036217121962950, CURRENCY=INR, GATEWAYNAME=HDFC, RESPMSG=Txn Success}]


                        } catch (Exception e) {
                            e.printStackTrace();
                            ErrorMsg("Failed to Proceed The Order. please Contact The Admin");

                        }
                    }

                    @Override
                    public void networkNotAvailable() { // If network is not
                        // available, then this
                        // method gets called.
                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {
                        // This method gets called if client authentication
                        // failed. // Failure may be due to following reasons //
                        // 1. Server error or downtime. // 2. Server unable to
                        // generate checksum or checksum response is not in
                        // proper format. // 3. Server failed to authenticate
                        // that client. That is value of payt_STATUS is 2. //
                        // Error Message describes the reason for failure.
//                        Toast.makeText(getApplicationContext(), "Payment Transaction response " + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
                        ErrorMsg("Transection Failed, Unable to Proceed The Order.");


                    }

                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode,
                                                      String inErrorMessage, String inFailingUrl) {
                        // Error Message describes the reason for failure.
//                        Toast.makeText(CartActivity.this, "Payment Transaction response " + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
                        ErrorMsg("Transection Failed, Unable to Proceed The Order.");

                    }

                    // had to be added: NOTE
                    @Override
                    public void onBackPressedCancelTransaction() {
                        Toast.makeText(CartActivity.this, "Back pressed. Transaction cancelled", Toast.LENGTH_LONG).show();
                        ErrorMsg("Transaction cancelled By you");
                    }

                    @Override
                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                        Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);

                        ErrorMsg("Transection Failed, Unable to Proceed The Order.");
                        //Toast.makeText(CartActivity.this, "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
                    }

                });
    }
*/
    String startDate = "";

    public void getDate(String dlrvytyp) {

        final ProgressDialog mProgressBar = new ProgressDialog(CartActivity.this);
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
                            String responses = jkHelper.Decryptapi(response, CartActivity.this);
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
                prefs = new Preferencehelper(CartActivity.this);
                Map<String, String> params = new HashMap<String, String>();


                try {

                    String param = getString(R.string.GET_DELIVERY_DATE) + "&storeId=" + prefs.getCID() + "&dtype=" + dlrvytyp;
                    Log.d("Beforeencrptionpay", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, CartActivity.this);
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

    String strDeliveryType = "";
    String strTimeSlot = "";
    String strDate = "";
    String strPaymentMode = "";
    double dblDeliveryCharges = 0.0;
    double dblWalletAmount = 0.0;
    double dblProcessingCharges = 0.0;
    double dblTax = 0.0;
    double dblBasePrice = 0.0;


    public void calculateTotalAmount() {
        if (mData.getString("Currency", "INR").equalsIgnoreCase("INR")) {
            currency = getResources().getString(R.string.rupee);
        } else {
            currency = "$";
        }

        if (!strDeliveryType.equalsIgnoreCase("HOME DELIVERY")) {
            dblDeliveryCharges = 0.0;
        }
        if (!strPaymentMode.equalsIgnoreCase("CASH ON DELIVERY")) {
            try {
                dblWalletAmount = Double.parseDouble(prefs.getPREFS_currentbal());
            } catch (Exception er) {
                dblWalletAmount = 0.0;

            }
        }
        try {
            dblProcessingCharges = Double.parseDouble(mData.getString("ServiceFees", "0.0"));
        } catch (Exception er) {
            dblProcessingCharges = 0.0;

        }
        try {
            dblTax = Double.parseDouble(mData.getString("Tax", "10.0"));
        } catch (Exception er) {
            dblTax = 0.0;

        }


        ArrayList<CartProduct> list = (ArrayList<CartProduct>) dbManager.getProducts();
        dblBasePrice = 0.0;


        for (int a = 0; a < list.size(); a++) {
            dblBasePrice = dblBasePrice + (Float.parseFloat(list.get(a).getSaleRate()) * list.get(a).getQuantity());
        }

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
        txtPreAmt.setText(currency + " " + new DecimalFormat("##.##").format(Double.parseDouble(prefs.getPREFS_currentbal())));
        double paybleAmt = 0.0;
        dblWalletAmount = 0.0;
        if (dblWalletAmount > 0) {
            if (dblWalletAmount > (Double.parseDouble(txtTax1.getText().toString().replace(currency, "")) + Double.parseDouble(txtSubtotal.getText().toString().replace(currency, "")))) {

                paybleAmt = 0.0;

            } else {
                paybleAmt = (Double.parseDouble(txtTax1.getText().toString().replace(currency, "")) + Double.parseDouble(txtSubtotal.getText().toString().replace(currency, ""))) - dblWalletAmount;

            }
            //   paybleAmt = Double.parseDouble(txtTax1.getText().toString().replace(currency, "")) + dblWalletAmount + Double.parseDouble(txtSubtotal.getText().toString().replace(currency, ""));
        } else {
            paybleAmt = Double.parseDouble(txtTax1.getText().toString().replace(currency, "")) + dblWalletAmount + Double.parseDouble(txtSubtotal.getText().toString().replace(currency, ""));
        }

        double dblll = Double.parseDouble(txtTax1.getText().toString().replace(currency, "")) + Double.parseDouble(txtSubtotal.getText().toString().replace(currency, ""));
        promotxn.setText(currency + " " + new DecimalFormat("##.##").format(dblPromoAmount));

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
                startActivity(new Intent(CartActivity.this, AddLatLong.class).putExtra("type", "1").putExtra("tripdids", "").setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));

            }
        });
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        savedrecysheet.setHasFixedSize(true);
        savedrecysheet.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        showaddresslist();
        loadProfileDataUser();
        dialogbottom.show();
    }

    public void showaddresslist() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        final ProgressDialog mProgressBar = new ProgressDialog(CartActivity.this);
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
                            String responses = jkHelper.Decryptapi(response, CartActivity.this);
                            mProgressBar.dismiss();

                            GeoFenceBeanMain mgeofencebeanmain = new Gson().fromJson(responses, GeoFenceBeanMain.class);
                            Mgeofencebeanmain = mgeofencebeanmain;


                            if (mgeofencebeanmain.getStrSuccess() == 1) {
                                geofencelist = mgeofencebeanmain.getStrreturns();

//                                String splithome[] = adressact.getStrdescription().split(":");
//                                viewHolder.completeaddress.setText(splithome[0]);
                                deliveryaddval.setText(geofencelist.get(0).getStrdescription());
                                deliveryaddlat = geofencelist.get(0).getStrlat();
                                deliveryaddlong = geofencelist.get(0).getStrlong();
                                savedAdressadapter = new SavedAdressadapter(CartActivity.this, geofencelist, 2, (TriggerClick) CartActivity.this);
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
                    param = jkHelper.Encryptapi(param, CartActivity.this);
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

        Volley.newRequestQueue(CartActivity.this).add(postRequest);


    }

    public void Signupapi() {


        Preferencehelper prefs = new Preferencehelper(CartActivity.this);

        final ProgressDialog mProgressBar = new ProgressDialog(CartActivity.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Registering...");
        mProgressBar.show();
        mProgressBar.setCancelable(false);

        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseSignup", response);
                        mProgressBar.dismiss();


                        try {
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, CartActivity.this);
                            JSONObject jsonObject = new JSONObject(responses);
                            int successtr = Integer.parseInt(jsonObject.getString("success"));
                            if (successtr == 1) {
                                JSONArray jsonArray = jsonObject.getJSONArray("newusercreation");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject newjson = jsonArray.getJSONObject(i);
                                    String returnmessage = newjson.getString("returnmessage");

                                    if (!returnmessage.equalsIgnoreCase("user already registered")) {

                                        preferencehelper.setPREFS_firstname(nameide.getText().toString());
                                        preferencehelper.setPREFS_email2(emailide.getText().toString());
                                        // preferencehelper.setPREFS_phoneno(p);
                                        preferencehelper.setPREFS_flatno(aptide.getText().toString());
                                        // preferencehelper.setPREFS_state(state);
                                        preferencehelper.setPrefsZipCode(zipide.getText().toString());
                                        //  Localdialog.show();
                                        Toast.makeText(getApplicationContext(), "Details saved successfully", Toast.LENGTH_LONG).show();
                                        updatedetaildialog.dismiss();

                                    } else {
                                    }

                                }


                            } else {
                            }

                        } catch (Exception e) {
                            mProgressBar.dismiss();

                            e.printStackTrace();
                        }


                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        try {

                            mProgressBar.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                String servicetype;

                if (preferencehelper.getPrefsUsercategory().equalsIgnoreCase("1057")) {
                    servicetype = "CONSUMER";

                } else {
                    servicetype = "RETAILER";


                }
                try {
                    SharedPreferences prefs1 = getSharedPreferences("MORNING_PARCEL_GROCERY",
                            MODE_PRIVATE);
                    String cntry = prefs1.getString("cntry", "India");

                    JKHelper jkHelper = new JKHelper();
                    String param = com.dhanuka.morningparcel.utils.AppUrls.strSignup + "&fname=" + nameide.getText().toString() + "&lname=" + "" + "&gender=" + "" + "&city=" + preferencehelper.getPREFS_city()
                            + "&username=" + nameide.getText().toString() + "&password=" + "" + "&bday=" + "01/01/1800" + "&mobileno=" + preferencehelper.getPREFS_phoneno() +
                            "&email=" + emailide.getText().toString() + "&zip=" + zipide.getText().toString() + "&securityq=" + "" + "&country=" + cntry + "&securityp=" + "" + "&address=" + flatide.getText().toString() + aptide.getText().toString()
                            + "&alternatephone=" + prefs.getPREFS_phoneno() + "&alternatemail=" + emailide.getText().toString() + "" + "&security2=" + "" + "&securitya2=" + "test" + "&state=" + preferencehelper.getPREFS_state()
                            + "&servicetype=" + servicetype + "&contactid=" + prefs.getPrefsContactId() + "&flatno=" + aptide.getText().toString() + "&building=" + "" + "&society=" + "" + "&type=" + "1";
                    Log.e("beforeenc", param);
                    param = jkHelper.Encryptapi(param, CartActivity.this);
                    params.put("val", param);
                    Log.e("afterenc", param);
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


    double totalCartValue = 1.0;

    public void generateCheckSum() {
        totalCartValue = Double.parseDouble(txtPaybleAmt.getText().toString().replace(" ", "").replace(currency, "").replace("₹", "").replace("$", ""));
        //getting the tax amount first.
        progressDialog = ProgressDialog.show(CartActivity.this, getString(R.string.app_name), "Processing...");
        progressDialog.setCancelable(false);

        //creating a retrofit object.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //creating the retrofit api service
        Api apiService = retrofit.create(Api.class);

        //creating paytm object
        //containing all the values required
        final com.dhanuka.morningparcel.demo.Paytm paytm = new com.dhanuka.morningparcel.demo.Paytm(
                Constant.M_ID,
                Constant.CHANNEL_ID,
                totalCartValue + "",
                Constant.WEBSITE,
                Constant.CALLBACK_URL,
                Constant.INDUSTRY_TYPE_ID
        );

        Log.e("CHK_PRM", paytm.getmId() + "\n" +
                mOrderId + "\n" +
                mCustId + "\n" +
                paytm.getChannelId() + "\n" +
                paytm.getTxnAmount() + "\n" +
                paytm.getWebsite() + "\n" +
                paytm.getCallBackUrl() + mOrderId + "\n" +
                paytm.getIndustryTypeId());

        //creating a call object from the apiService
        Call<Checksum> call = apiService.getChecksum(
                paytm.getmId(),
                mOrderId,
                mCustId,
                paytm.getChannelId(),
                paytm.getTxnAmount(),
                paytm.getWebsite(),
                paytm.getCallBackUrl()
                        + mOrderId,
                paytm.getIndustryTypeId()
        );

        //making the call to generate checksum
        call.enqueue(new Callback<Checksum>() {
            @Override
            public void onResponse(Call<Checksum> call, retrofit2.Response<Checksum> response) {

                //once we get the checksum we will initiailize the payment.
                //the method is taking the checksum we got and the paytm object as the parameter
                //          initializePaytmPayment(response.body().getChecksumHash(), paytm);
                try {
                    Log.e("chcksm", new Gson().toJson(response.body()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
//                {"CHECKSUMHASH":"Rnm3BlsA=","ORDER_ID":"ORDER100001284_user_1","payt_STATUS":"1","PAYTM_MERCHANT_KEYE":"_N%76LsTYDJFzvNm"}

                onStartTransaction(response.body().getChecksumHash(), paytm);
            }

            @Override
            public void onFailure(Call<Checksum> call, Throwable t) {

            }
        });
    }

    public void applypromocode() {

        Log.e("dsfkjjfk11", AppController.getValTypeFromDesc("order_master"));
        final ProgressDialog mProgressBar = new ProgressDialog(CartActivity.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            promolist = new ArrayList<>();
                            Log.d("promolist", response);
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, CartActivity.this);
                            Log.e("promolist", responses);
                            JSONObject jsonObject = new JSONObject(responses);
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                //for (int a=0;a<mArrayList.size();a++){
                                //"returnds":[{"OrderID":"6","successmsg":"Successful"}]
                                JSONArray jsonArray = jsonObject.getJSONArray("returnds");

                                for (int startcount = 0; startcount < jsonArray.length(); startcount++) {

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(startcount);

                                    String CatCodeID = jsonObject1.getString("CatCodeID");

                                    String CompanyID = jsonObject1.getString("CompanyID");
                                    String CodeDescription = jsonObject1.getString("CodeDescription");
                                    String Percent = jsonObject1.getString("Val1");
                                    int Maxamount = Integer.parseInt(jsonObject1.getString("Val2"));
                                    promoModel = new PromoModel();
                                    promoModel.setCatCodeID(CatCodeID);
                                    promoModel.setCompanyID(CompanyID);
                                    promoModel.setCodeDescription(CodeDescription);
                                    promoModel.setVal1(Percent);
                                    promoModel.setVal2(String.valueOf(Maxamount));
                                    promolist.add(promoModel);
                                    promolist1.add(CodeDescription);


                                }
                                PromoAdapter promoAdapter = new PromoAdapter(getApplicationContext(), promolist, CartActivity.this);
                                promocontainer.setAdapter(promoAdapter);


                            } else {

                                FancyToast.makeText(CartActivity.this, "No Data Found, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
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
                        FancyToast.makeText(CartActivity.this, "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


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
                prefs = new Preferencehelper(CartActivity.this);
                Map<String, String> params = new HashMap<String, String>();


                try {

                    String param = getString(R.string.URL_GET_CATEGORY_CODE) + "&type=" + "125" + "&contactid=" + prefs.getPrefsContactId();
                    Log.d("Beforeencrptionpay", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, CartActivity.this);
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

    @Override
    public void onStoreSelect() {

    }

    @Override
    public void onPromoSelect(String promocode) {
        Promodialog.dismiss();
        enterpromo.setText("");
        enterpromo.setText(promocode);

    }


    private void loadProfileData() {

        final ProgressDialog mProgressBar = new ProgressDialog(CartActivity.this);
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
                            String responses = jkHelper.Decryptapi(response, CartActivity.this);
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
                                    IsFinanceBlock = jsonobject.getString("IsFinanceBlock");


                                    preferencehelper.setPREFS_currentbal(CurrentBalance);
                                    preferencehelper.setPREFS_city(city);
                                    preferencehelper.setPREFS_society(society);
                                    preferencehelper.setPrefsIsFinanceBlock(IsFinanceBlock);
                                    preferencehelper.setPREFS_Building(Building);
                                    preferencehelper.setPREFS_firstname(firstname);
                                    preferencehelper.setPREFS_email2(email2);
                                    preferencehelper.setPREFS_phoneno(phoneno);
                                    preferencehelper.setPREFS_flatno(flatno);
                                    preferencehelper.setPREFS_state(state);
                                    preferencehelper.setPrefsZipCode(zipcode);

                                    String currency = "";
                                    SharedPreferences prefs1 = getSharedPreferences("MORNING_PARCEL_GROCERY",
                                            MODE_PRIVATE);


                                    if (prefs1.getString("Currency", "INR").equalsIgnoreCase("INR")) {
                                        currency = getResources().getString(R.string.rupee);
                                    } else {
                                        currency = "$";
                                    }


                                    if (!prefs.getPREFS_currentbal().isEmpty()) {
                                        String amt = df2.format(Double.parseDouble(preferencehelper.getPREFS_currentbal()));
                                        nav_Menu.findItem(R.id.history_wallet).setTitle("Wallet(" + currency + "" + amt + ")");
                                    } else {
                                        nav_Menu.findItem(R.id.history_wallet).setTitle("Wallet(" + currency + "" + "0.0)");

                                    }

                                }
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("mProfile", jsonArray.get(0).toString());
                                editor.commit();

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
                    String finalparam = jkHelper.Encryptapi(param, CartActivity.this);
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

    private void loadProfileDataUser() {

        final ProgressDialog mProgressBar = new ProgressDialog(CartActivity.this);
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
                            String responses = jkHelper.Decryptapi(response, CartActivity.this);
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
                                    IsFinanceBlock = jsonobject.getString("IsFinanceBlock");


                                    preferencehelper.setPREFS_currentbal(CurrentBalance);
                                    preferencehelper.setPREFS_city(city);
                                    preferencehelper.setPREFS_society(society);
                                    preferencehelper.setPrefsIsFinanceBlock(IsFinanceBlock);
                                    preferencehelper.setPREFS_Building(Building);
                                    preferencehelper.setPREFS_firstname(firstname);
                                    preferencehelper.setPREFS_email2(email2);
                                    preferencehelper.setPREFS_phoneno(phoneno);
                                    preferencehelper.setPREFS_flatno(flatno);
                                    preferencehelper.setPREFS_state(state);
                                    preferencehelper.setPrefsZipCode(zipcode);

                                    String currency = "";
                                    SharedPreferences prefs1 = getSharedPreferences("MORNING_PARCEL_GROCERY",
                                            MODE_PRIVATE);


                                    if (prefs1.getString("Currency", "INR").equalsIgnoreCase("INR")) {
                                        currency = getResources().getString(R.string.rupee);
                                    } else {
                                        currency = "$";
                                    }


                                    if (!prefs.getPREFS_currentbal().isEmpty()) {
                                        String amt = df2.format(Double.parseDouble(preferencehelper.getPREFS_currentbal()));
                                        nav_Menu.findItem(R.id.history_wallet).setTitle("Wallet(" + currency + "" + amt + ")");
                                    } else {
                                        nav_Menu.findItem(R.id.history_wallet).setTitle("Wallet(" + currency + "" + "0.0)");

                                    }

                                }
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("mProfile", jsonArray.get(0).toString());
                                editor.commit();
                                financebloack();
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
                    String finalparam = jkHelper.Encryptapi(param, CartActivity.this);
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


        amt = Float.parseFloat(new DecimalFormat("##.##").format((Double.parseDouble(txtSubtotal.getText().toString().replace(currency, "")) + Double.parseDouble(txtTax1.getText().toString().replace(currency, "")) - Double.parseDouble(promotxn.getText().toString().replace(currency, ""))))) + "";


        //mTXNID = "iugfchjk";
        //strTxnId = "iugfchjk";

        // checkoutOrder();

        if (strTimeSlot == null || strTimeSlot.equalsIgnoreCase("")) {
            FancyToast.makeText(CartActivity.this, "time slot is not select or either there is no time slot available for this delivery", FancyToast.LENGTH_LONG, FancyToast.INFO, false).show();
        } else {
//                                if (Float.parseFloat(new DecimalFormat("##.##").format(Double.parseDouble(payableamount))) > 0) {
            PreviousAdjustedAmount = (Double.parseDouble(txtSubtotal.getText().toString().replace(currency, "")) + Double.parseDouble(txtTax1.getText().toString().replace(currency, "")) - Double.parseDouble(promotxn.getText().toString().replace(currency, "")));
            if ((Double.parseDouble(txtSubtotal.getText().toString().replace(currency, "")) + Double.parseDouble(txtTax1.getText().toString().replace(currency, "")) - Double.parseDouble(promotxn.getText().toString().replace(currency, ""))) > 0) {
                Log.e("my_condition = ", "1");

//                                    dblWalletAmount = ;

                if (Double.parseDouble(prefs.getPREFS_currentbal()) >= (Double.parseDouble(txtSubtotal.getText().toString().replace(currency, "")) + Double.parseDouble(txtTax1.getText().toString().replace(currency, "")) - Double.parseDouble(promotxn.getText().toString().replace(currency, "")))) {
                    strTxnId = "WALLET AMOUNT USED";
                    checkoutOrder();
                    Log.e("my_condition = ", "2");
//                                        moveToTransection();
                } else {
                    Log.e("my_condition = ", "3");


                    Dialog dialog = new Dialog(CartActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.setContentView(R.layout.dialog_wallet_amount);
                    dialog.setCancelable(true);
                    final Button submit = (Button) dialog.findViewById(R.id.btnupdate);
                    final TextView txtWalletAmtShow = (TextView) dialog.findViewById(R.id.txtWalletAmtShow);
                    txtWalletAmtShow.setText("₹" + prefs.getPREFS_currentbal());
                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            startActivityForResult(new Intent(CartActivity.this, WalleTransactionActivity.class), 5566);
                            //startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + getPackageName())));


                        }
                    });

                    // Display the custom alert dialog on interface
                    dialog.show();

                }
            } else {
                Log.e("my_condition = ", "4");
                strTxnId = "WALLET AMOUNT USED";

                if (preferencehelper.getPrefsIsFinanceBlock().equalsIgnoreCase("1")) {
                    dialogfinace.show();
                } else {
                    checkoutOrder();
                }

            }

            //  strTxnId = "76565678576465656";
            // checkoutOrder();
//if ()
        }


    }

    public void loadHistory() {


        final ProgressDialog mProgressBar = new ProgressDialog(CartActivity.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();
        //  "http://mmthinkbiz.com/MobileService.aspx?method=GetGRDetail_Web" + "&type=" + "100" + "&tdate=" + mTodayDate + "&fdate=" + mStartDate + "&CID=" + sytrContactId
        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL)
                /* getString(R.string.URL_BASE_URL)*/,


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            ArrayList<CartProduct> listOrders = new ArrayList<>();


                            Log.d("responsiveorderhistory", response);
                            JKHelper jkHelper = new JKHelper();

                            String responses = jkHelper.Decryptapi(response, CartActivity.this);

                            Log.d("responsive", responses);
                            JSONObject jsonObject = new JSONObject(responses);
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {

                                JSONArray newjsonarr = jsonObject.getJSONArray("returnds");


                                // Log.d("cartproductprint",cartProduct.getMRP()+"\n"+product.getMRP()+"\n");

                                dbManager = com.dhanuka.morningparcel.database.DatabaseManager.getInstance(getApplicationContext());

                                dbManager.deleteAll();
                                for (int a = 0; a < newjsonarr.length(); a++) {
                                    JSONObject newjsonobj = newjsonarr.getJSONObject(a);

                                    CartProduct cartProduct = new CartProduct();
                                    cartProduct.setCompanyid(newjsonobj.getString("CompanyID"));
                                    oid = newjsonobj.getString("OrderID");
                                    JSONArray oreridarr = newjsonobj.getJSONArray("Items");

                                    for (int k = 0; k < oreridarr.length(); k++) {
                                        JSONObject orderobj = oreridarr.getJSONObject(k);


                                        cartProduct.setItemID(Integer.parseInt(orderobj.getString("ItemID")));

                                        cartProduct.setItemName(orderobj.getString("ItemDescription"));
                                        cartProduct.setGroupID("");
                                        cartProduct.setOpeningStock("");
                                        cartProduct.setMOQ("");
                                        cartProduct.setROQ("");
                                        cartProduct.setPurchaseUOM("");
                                        cartProduct.setPurchaseUOMId("");
                                        cartProduct.setSaleUOM("");
                                        cartProduct.setItemTaxRate(orderobj.getString("ItemTaxRate"));
                                        cartProduct.setItemTaxAmount(orderobj.getString("ItemTaxAmount"));

                                        cartProduct.setSaleUOM("");
                                        cartProduct.setSaleUOMID("");
                                        cartProduct.setPurchaseRate("");
                                        cartProduct.setSaleRate(orderobj.getString("Rate"));
                                        cartProduct.setItemSKU("");
                                        cartProduct.setItemBarcode(orderobj.getString("BarCode"));
                                        cartProduct.setStockUOM("");
                                        cartProduct.setItemImage(orderobj.getString("FileName") + "&filePath=" + orderobj.getString("filepath"));
                                        cartProduct.setHSNCode("");
                                        DecimalFormat precision = new DecimalFormat("0");
                                        int finalquant = (int) (Math.round(Float.parseFloat(orderobj.getString("RequestedQty"))));
                                        cartProduct.setQuantity(finalquant);
                                        cartProduct.setSubCategory("");
                                        cartProduct.setMRP(orderobj.getString("MRP"));
                                        dbManager.insert(cartProduct);
                                        countView.setCount(dbManager.getTotalQty());
                                        Log.d("totaltaxamountashjwani", cartProduct.getItemTaxAmount());

                                        if (TextUtils.isEmpty(cartProduct.getItemTaxRate())) {

                                            finaltaxamount = 0.0f;
                                            finaltaxrate = 0.0f;
                                            taxamount = 0.0f;
                                        } else {


                                            finaltaxamount = Float.parseFloat(cartProduct.getItemTaxAmount()) + finaltaxamount;
                                            finaltaxrate = Float.parseFloat(cartProduct.getItemTaxRate()) + finaltaxrate;

                                        }


                                        setData();
                                        setCartCount();
                                    }
                                    Log.d("finaltaxamount", finaltaxamount.toString());
                                    Log.d("finaltaxrate", finaltaxrate.toString());

                                }


                            } else {

                                dbManager.deleteAll();
                                setCartCount();
                                setData();
                                FancyToast.makeText(CartActivity.this, "No Data Found, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
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


                        FancyToast.makeText(CartActivity.this, "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params1 = new HashMap<String, String>();

                Calendar c = Calendar.getInstance();
                System.out.println("Current time => " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                String mTodayDate = df.format(c.getTime());

                try {

                    SharedPreferences mData = getSharedPreferences("MORNING_PARCEL_GROCERY",
                            MODE_PRIVATE);
                    String shopid = mData.getString("shopId", "1");

                    String param = getString(R.string.GET_ORDER_DETAIL) + "&type=" + "500" + "&tdate=" + mTodayDate + "&CID=" + preferencehelper.getPrefsContactId() + "&storeId=" + shopid + "&fdate=" + prefs.getCID() + "&tdate=";
                    Log.d("Beforeencrptionhistory", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, CartActivity.this);
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


    public void financebloack() {
        dialogfinace = new Dialog(CartActivity.this);
        dialogfinace.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogfinace.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogfinace.setContentView(R.layout.dialog_finance);
        dialogfinace.setCancelable(true);
        final Button addfinance = (Button) dialogfinace.findViewById(R.id.addfinance);
        final Button cancelfinc = (Button) dialogfinace.findViewById(R.id.cancelfinance);
        final TextView txtWalletAmtShow = (TextView) dialogfinace.findViewById(R.id.txtWalletAmtShow);
        txtWalletAmtShow.setText("₹" + prefs.getPREFS_currentbal());
        addfinance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogfinace.dismiss();
                startActivityForResult(new Intent(CartActivity.this, WalleTransactionActivity.class), 5567);
                //startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + getPackageName())));


            }
        });
        cancelfinc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogfinace.dismiss();
            }
        });

        // Display the custom alert dialog on interface

    }


}


