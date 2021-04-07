package com.dhanuka.morningparcel.activity;

import android.Manifest;
import android.app.Activity;
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
import android.graphics.Color;
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
//import android.support.v4.app.ActivityCompat;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dhanuka.morningparcel.Helper.ImageLoadingUtils;
import com.dhanuka.morningparcel.Helper.Payfraghelper;
import com.dhanuka.morningparcel.activity.barcode.Bar;
import com.dhanuka.morningparcel.activity.retail.GRNDetail;
import com.dhanuka.morningparcel.adapter.OrderImagesAdapter;
import com.dhanuka.morningparcel.adapter.SelectedImagesAdapter;
import com.dhanuka.morningparcel.beans.DeliveryBoysBean;
import com.dhanuka.morningparcel.events.OnImageDeleteListener;
import com.dhanuka.morningparcel.utils.DbImageUpload;
import com.dhanuka.morningparcel.Helper.ImageMasterDAO;
import com.dhanuka.morningparcel.utils.ImageUploadService;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.Helper.DbImageMaster;
import com.dhanuka.morningparcel.Helper.ImageUploadDAO;
import com.dhanuka.morningparcel.Helper.Preferencehelper;

import com.dhanuka.morningparcel.R;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.dhanuka.morningparcel.SqlDatabase.QueryExecutor;
import com.dhanuka.morningparcel.beans.FancyToast;
import com.dhanuka.morningparcel.adapter.OrderItemsAdapter;
import com.dhanuka.morningparcel.beans.OrderBean;
import com.dhanuka.morningparcel.events.OnUpdateOrderListener;
import com.dhanuka.morningparcel.utils.Utility;
import com.dhanuka.morningparcel.utils.PermissionModule;
import com.dhanuka.morningparcel.utils.log;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.dhanuka.morningparcel.SqlDatabase.DatabaseManager;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.filter.entity.ImageFile;

import static com.vincent.filepicker.activity.BaseActivity.IS_NEED_FOLDER_LIST;
import static com.vincent.filepicker.activity.ImagePickActivity.IS_NEED_CAMERA;


public class OrderDetailActivity extends AppCompatActivity implements OnUpdateOrderListener, OnImageDeleteListener {
    String currency = "";
    float subtotal = 0f;
    float payableamt = 0f;
    private List<OrderBean> listclone = new ArrayList<>();
    int type_cr = 0;
    Payfraghelper payfraghelper;
    String itemcancels;
    @BindView(R.id.txtId)
    TextView txtId;
    @BindView(R.id.txtStatus)
    TextView txtStatus;
    @BindView(R.id.txtTagline)
    TextView txtTagline;
    @BindView(R.id.txtPrice)
    TextView txtPrice;
    @BindView(R.id.txtQty)
    TextView txtQty;
    @BindView(R.id.txtDate)
    TextView txtDate;

    @BindView(R.id.txtName)
    TextView txtName;
    @BindView(R.id.txtPhone)
    ImageView txtPhone;
    @BindView(R.id.txtPhone1)
    ImageView txtPhone1;

    @BindView(R.id.txtName1)
    TextView txtName1;
    @BindView(R.id.btnCancel)
    Button btncancel;
    @BindView(R.id.btnReview)
    Button btnReview;
    int mType = 1;
    String strChecked = "";
    String strReplace = "";

    @BindView(R.id.retailerLayout)
    LinearLayout retailerLayout;
    @BindView(R.id.reviewLayout)
    LinearLayout reviewLayout;


    @BindView(R.id.userLayout)
    LinearLayout userLayout;

    @BindView(R.id.ratigs)
    RatingBar ratigs;


    @BindView(R.id.txtAddress)
    TextView txtAddress;
    @BindView(R.id.txtComment1)
    TextView txtComment1;
    @BindView(R.id.txtComment)
    TextView txtComment;
    @BindView(R.id.txtDel)
    TextView txtDel;
    @BindView(R.id.PaymentMode)
    TextView PaymentMode;
    @BindView(R.id.txnId)
    TextView txnId;
    @BindView(R.id.imgCustomer)
    ImageView imgCustomer;

    OrderItemsAdapter mAdapter;
    @BindView(R.id.lvProducts)
    ListView lvProducts;
    @BindView(R.id.spinnerStatus)
    Spinner spinnerStatus;
    @BindView(R.id.btnProceed)
    Button btnProceed;
    @BindView(R.id.btnCr)
    Button btnCr;
    @BindView(R.id.btnDr)
    Button btnDr;
    @BindView(R.id.imgPayment)
    ImageView imgPayment;
    OrderBean mOrderBean;
    ArrayList<OrderBean.OrderItemsBean> list;
    Preferencehelper prefs;
    String status = "";
    int mPos;
    int acceeptcrnotevar=0;

    Dialog HelpDialog;
    TextView helpline, shopno, teamno, deliveredsort, pendingsort, holdsort, datesort;
    ImageView callteam, callshop, callhelpline;
    SharedPreferences prefsss;
    int selectedPosition = 0;
    @BindView(R.id.editupload)
    ImageView uploadbill;
    TextView vehicle_header_name, txtCartAmt, txtDlvryAmt, txtSrvcAmt, txtSubtotal, txtTax1, txtPreAmt, txtPaybleAm, txttotalitems;
    Dialog taxdialog, dialogItemDetail;

    TextView btnSubmit;
    ImageView closeHeader, closeHeader1, imageScan;
    EditText edtBarcode;
    ImageView pickimagebtn;
    RadioButton radioButtonreturn, radiocash, radiowallet, radioonline;
    RecyclerView rvImages;
    Dialog dialogAssignOrder;
    Spinner spBoys;
    String paymode, cmt;
    Button btnSubmitAssign;
    EditText edtComment;
    String sytrContactId = "";
    Button btngritem;
    Dialog crnotedialog;
    EditText edtcrcmt, edtcomment;
    RadioGroup crrgroup, rreturngroup;
    SharedPreferences mDataPreference;
    SharedPreferences.Editor mDataEditor;
    ArrayList<String> mListSelectedIamges = new ArrayList<>();


    Dialog canceldialogconfirm;
    TextView Yesdeletbtn, nodeletebtn;
    LinearLayout cmtll;
    EditText commenttxtdelte;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        verifyStoragePermissions(this);
        setContentView(R.layout.activity_order_detail);


        ButterKnife.bind(this);
        canceldialogconfirm = new Dialog(OrderDetailActivity.this);

        canceldialogconfirm.setTitle("Are you sure want to cancel this Order?");
        canceldialogconfirm.setContentView(R.layout.cancel_item_dialog);
        canceldialogconfirm.setCancelable(false);
        Yesdeletbtn = canceldialogconfirm.findViewById(R.id.yesdelte);
        cmtll = canceldialogconfirm.findViewById(R.id.cmtll);
        nodeletebtn = canceldialogconfirm.findViewById(R.id.nodelte);
        commenttxtdelte = canceldialogconfirm.findViewById(R.id.commenttxtdelte);
        prefs = new Preferencehelper(this);
        sytrContactId = prefs.getPrefsContactId();
        mDataPreference = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);
        mDataEditor = mDataPreference.edit();

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        try {
            mType = args.getInt("mType", 1);
            mPos = args.getInt("mPos", 1);

        } catch (Exception e) {
            e.printStackTrace();
            mType = 1;
            mPos = 0;
        }
        try {
            if (getIntent().hasExtra("mListt")) {
                listclone = (List<OrderBean>) args.getSerializable("mListt");
            } else {

                // listclone=new Gson().fromJson()
                listclone = new Gson().fromJson(mDataPreference.getString("mListData", ""), new TypeToken<List<OrderBean>>() {
                }.getType());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        mOrderBean = (OrderBean) args.getSerializable("list");
        Log.e("mOrderBean", new Gson().toJson(mOrderBean));
        SharedPreferences prefs22 = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);

        if (mOrderBean.getCurrency().equalsIgnoreCase("INR")) {
            currency = getResources().getString(R.string.rupee);
        } else {
            currency = "$";
        }
        list = mOrderBean.getmListItems();

        taxdialog = new Dialog(OrderDetailActivity.this);
        taxdialog.setContentView(R.layout.tax_calculation);
        dialogItemDetail = new Dialog(OrderDetailActivity.this);
        dialogItemDetail.setContentView(R.layout.dialog_item_detail);

        txtCartAmt = taxdialog.findViewById(R.id.txtCartAmt);
        txtDlvryAmt = taxdialog.findViewById(R.id.txtDlvryAmt);
        txtSrvcAmt = taxdialog.findViewById(R.id.txtSrvcAmt);
        txtSubtotal = taxdialog.findViewById(R.id.txtSubtotal);
        txtTax1 = taxdialog.findViewById(R.id.txtTax1);
        btngritem = findViewById(R.id.btngritem);
        txtPreAmt = taxdialog.findViewById(R.id.txtPreAmt);
        txtPaybleAm = taxdialog.findViewById(R.id.txtPaybleAm);
        txttotalitems = taxdialog.findViewById(R.id.txttotalitems);
        btnSubmit = dialogItemDetail.findViewById(R.id.btnSubmit);
        closeHeader = dialogItemDetail.findViewById(R.id.closeHeader);
        imageScan = dialogItemDetail.findViewById(R.id.imageScan);
        edtBarcode = dialogItemDetail.findViewById(R.id.edtBarcode);
        pickimagebtn = dialogItemDetail.findViewById(R.id.imagepick);
        rvImages = dialogItemDetail.findViewById(R.id.rvImages);
        vehicle_header_name = dialogItemDetail.findViewById(R.id.vehicle_header_name);
        pickimagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });
        txtPreAmt.setText(currency + " " + prefs.getPREFS_currentbal());
        closeHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogItemDetail.dismiss();
            }
        });
        dialogItemDetail.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                edtBarcode.setText("");
                mListImages = new ArrayList<>();
                orderImagesAdapter = new OrderImagesAdapter(OrderDetailActivity.this, mListImages, OrderDetailActivity.this);
                rvImages.setAdapter(orderImagesAdapter);

            }
        });
        if (mType == 0) {
            btngritem.setVisibility(View.VISIBLE);
        } else {
            btngritem.setVisibility(View.GONE);
        }

        btngritem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrderDetailActivity.this, GRNDetail.class)
                        .putExtra("id", mOrderBean.getOrderID())
                        .putExtra("tax", "")
                        .putExtra("supplier", mOrderBean.getSupplierName())
                        .putExtra("invoicenumber", mOrderBean.getPaymentTxnId())
                        .putExtra("invoicedate", mOrderBean.getOrderDate())
                        .putExtra("type", "")
                        .putExtra("supplierID", mOrderBean.getCustomerID())
                        .putExtra("branchid", mOrderBean.getCustomerRating()));
                finish();
            }
        });
        orderAcceptDialog();
        orderCompleteDialog();
        Readyfordeliverdialog();
        dialogAssignOrder = new Dialog(OrderDetailActivity.this);
        dialogAssignOrder.setContentView(R.layout.dialog_assign_order);

        closeHeader1 = dialogAssignOrder.findViewById(R.id.closeHeader);
        btnSubmitAssign = dialogAssignOrder.findViewById(R.id.btnSubmit);
        edtComment = dialogAssignOrder.findViewById(R.id.edtComment);
        spBoys = dialogAssignOrder.findViewById(R.id.spBoys);
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

        closeHeader1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAssignOrder.dismiss();
            }
        });

        getDeliveryBoys();

//Camera dialog start

        CameraDialog = new Dialog(OrderDetailActivity.this);
        CameraDialog.setContentView(R.layout.popup_image_select);
        ImageView camerabtn = CameraDialog.findViewById(R.id.camerabtn);
        ImageView gallerybtn = CameraDialog.findViewById(R.id.gallerybtn);

        gallerybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(OrderDetailActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(OrderDetailActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                } else {

                    Intent intent1 = new Intent(OrderDetailActivity.this, ImagePickActivity.class);
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
                Intent intent1 = new Intent(OrderDetailActivity.this, ImagePickActivity.class);
                intent1.putExtra(IS_NEED_CAMERA, true);
                intent1.putExtra(com.vincent.filepicker.Constant.MAX_NUMBER, 1);
                intent1.putExtra(IS_NEED_FOLDER_LIST, false);
                startActivityForResult(intent1, com.vincent.filepicker.Constant.REQUEST_CODE_TAKE_IMAGE);

                //  openCamera();
                //     launchCameraIntent();
                CameraDialog.dismiss();
            }
        });


        //camera dialog end


        dialogItemDetail.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                edtBarcode.setText("");
                mListImages = new ArrayList<>();
                orderImagesAdapter = new OrderImagesAdapter(OrderDetailActivity.this, mListImages, OrderDetailActivity.this);
                rvImages.setAdapter(orderImagesAdapter);

            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtBarcode.getText().toString().isEmpty()) {
                    if (!mBeanItems.getBarCode().equalsIgnoreCase(edtBarcode.getText().toString())) {
                        edtBarcode.setError("Wrong product, please pick right product..");
                    } else {
                        if (mListImages.size() > 0) {
                            for (int a = 0; a < mListImages.size(); a++) {
                                path = mListImages.get(a);
                                new ImageCompressionAsyncTask(true).execute(path);

                            }
                        }
                        uploadimage();
                        status = "82";
                        updateOrderStatus(mBeanItems.getOrderdetailID(), "2", "82",strReplace);

                        dialogItemDetail.dismiss();
                        //  FancyToast.makeText(OrderDetailActivity.this,"Please capture atleast one image of product",)
                    }
                    edtBarcode.setError("Please enter or scan barcode.");
                } /*else  if (!mBeanItems.getBarCode().equalsIgnoreCase(edtBarcode.getText().toString())) {
                    edtBarcode.setError("Wrong product, please pick right product..");
                }*/ else {
                    if (mListImages.size() > 0) {
                        for (int a = 0; a < mListImages.size(); a++) {
                            path = mListImages.get(a);
                            new ImageCompressionAsyncTask(true).execute(path);

                        }
                    }
                    uploadimage();
                    status = "82";
                    updateOrderStatus(mBeanItems.getOrderdetailID(), "2", "82",strReplace);

                    dialogItemDetail.dismiss();
                    //  FancyToast.makeText(OrderDetailActivity.this,"Please capture atleast one image of product",)
                }
            }
        });
        imageScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(OrderDetailActivity.this, Bar.class), 10124);
            }
        });

        try {
            txtDlvryAmt.setText(currency + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(mOrderBean.getDeliveryCharge()))));
        } catch (Exception e) {
            txtDlvryAmt.setText(currency + new DecimalFormat("##.##").format(Double.parseDouble("0.0")));

        }
        try {
            txtSrvcAmt.setText(new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(mOrderBean.getServiceCharge()))));
        } catch (Exception e) {
            txtSrvcAmt.setText("0.0");
        }


        for (int i = 0; i < list.size(); i++) {
            if (!mOrderBean.getmListItems().get(i).getStatus().equalsIgnoreCase("91")) {
                try {
                    subtotal += Float.parseFloat(mOrderBean.getmListItems().get(i).getRate()) * Float.parseFloat(mOrderBean.getmListItems().get(i).getRequestedQty());
                } catch (Exception e) {
                    subtotal += 0;
                }

            }
        }

        try {
            txtTax1.setText(currency + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(mOrderBean.getTax()))));
        } catch (Exception e) {
            e.printStackTrace();
            txtTax1.setText(currency + "0.0");
        }
        ;
        txtCartAmt.setText(currency + " " + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(subtotal))));
        Float subtotal1 = 0f;
        try {
            subtotal1 = subtotal + Float.parseFloat(txtDlvryAmt.getText().toString().replace(currency, "")) + Float.parseFloat(mOrderBean.getServiceCharge());
        } catch (Exception e) {
            subtotal1 = subtotal + Float.parseFloat(txtDlvryAmt.getText().toString().replace(currency, ""));

        }

        txtSubtotal.setText(currency + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(subtotal1))));


        //  txtPreAmt.setText("+" + currency + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(prefs.getPREFS_currentbal()))));
        try {

            payableamt = Float.parseFloat(String.valueOf(subtotal)) + Float.parseFloat(mOrderBean.getTax());
            txtPaybleAm.setText(currency + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(payableamt))));
        } catch (NumberFormatException ex) { // handle your exception

        }
/*    txtSrvcAmt.setText(new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(mOrderBean.getServiceCharge()))));


        for (int i = 0; i < list.size(); i++) {
            if (!mOrderBean.getmListItems().get(i).getStatus().equalsIgnoreCase("91")) {
                subtotal += Float.parseFloat(mOrderBean.getmListItems().get(i).getRate()) * Float.parseFloat(mOrderBean.getmListItems().get(i).getRequestedQty());

            }


        }

        txtTax1.setText(currency + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(mOrderBean.getTax()))));
        ;
        txtCartAmt.setText(currency + " " + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(subtotal))));
        Float subtotal1 = subtotal + Float.parseFloat(txtDlvryAmt.getText().toString().replace(currency, "")) + Float.parseFloat(mOrderBean.getServiceCharge());

        txtSubtotal.setText(currency + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(subtotal1))));


        txtPreAmt.setText("+" + currency + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(prefs.getPREFS_currentbal()))));
        try {

            payableamt = Float.parseFloat(String.valueOf(subtotal)) + Float.parseFloat(mOrderBean.getTax());
            txtPaybleAm.setText(currency + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(payableamt))));
        } catch (NumberFormatException ex) { // handle your exception

        }
*/

        taxdialog.show();
        //   txttotalitems.setText(mOrderBean.getmListItems().size());


        prefsss = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);
        SharedPreferences.Editor mEditor = prefsss.edit();
        status = mOrderBean.getStatus();
        mAdapter = new OrderItemsAdapter(this, list, status, this, type_cr, mOrderBean.getDeliveryMode());
        lvProducts.setAdapter(mAdapter);
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
        imgCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderDetailActivity.this, NewOrderActivity.class).putExtra("isCustomer", mOrderBean.getCustomerID()));
            }
        });
        if (prefs.getPrefsUsercategory().equalsIgnoreCase("1057")) {
            btnDr.setVisibility(View.GONE);
            btnCr.setVisibility(View.GONE);
        } else {
            if (mType == 1) {
                if (mOrderBean.getStatus().equalsIgnoreCase("10")) {
                    btnDr.setVisibility(View.GONE);
                    btnCr.setVisibility(View.VISIBLE);
                } else {
                    btnDr.setVisibility(View.GONE);
                    btnCr.setVisibility(View.GONE);

                }
            } else {
                btnDr.setVisibility(View.GONE);
                btnCr.setVisibility(View.VISIBLE);

            }
        }

        if (mType == 0) {
            btnCr.setText("DR Note");
        } else {
            btnCr.setText("CR Note");

        }
        btnDr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strChecked = "";
                List<OrderBean.OrderItemsBean> mListChecked = new ArrayList<>();
                mListChecked = mAdapter.getChckedProducts();
                for (int a = 0; a < mListChecked.size(); a++) {
                    if (mListChecked.get(a).getChecked()) {
                        if (strChecked.isEmpty()) {
                            strChecked = mListChecked.get(a).getOrderdetailID() + "/" + mListChecked.get(a).getQuantity();
                        } else {
                            strChecked = strChecked + "//" + mListChecked.get(a).getOrderdetailID() + "/" + mListChecked.get(a).getQuantity();

                        }
                    }
                }
///  status=/1.0000///1.0000///1.0000
                if (strChecked.isEmpty()) {
                    FancyToast.makeText(OrderDetailActivity.this, "please select atleast one product.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

                } else {


                    crnotedialog = new Dialog(OrderDetailActivity.this);
                    crnotedialog.setContentView(R.layout.cr_note_dialog);
                    crrgroup = crnotedialog.findViewById(R.id.rreturngroup);
                    edtcrcmt = crnotedialog.findViewById(R.id.edtCComment);
                    btnSubmit = crnotedialog.findViewById(R.id.btnSubmit);
                    radiocash = (RadioButton) crnotedialog.findViewById(R.id.cashslct);
                    radioonline = (RadioButton) crnotedialog.findViewById(R.id.onlineslct);
                    radiowallet = (RadioButton) crnotedialog.findViewById(R.id.walletslct);

                    crrgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {


                            int selectedId = crrgroup.getCheckedRadioButtonId();

                            // find the radiobutton by returned id
                            radioButtonreturn = (RadioButton) crrgroup.findViewById(selectedId);


                        }
                    });
                    //radiocash.setChecked(true);
                    crrgroup.check(R.id.cashslct);

                    btnSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            paymode = radioButtonreturn.getText().toString();
                            cmt = edtcrcmt.getText().toString();
                            mAdapter.changeToEdit(0);
                            type_cr = 0;
                            Log.e("strChecked", strChecked);
                            if (mType == 0) {
                                btnCr.setText("DR Note");
                                Log.d("strChecked", strChecked);
                                updateOrderStatus("0", "9", strChecked,strReplace);
                            } else {
                                btnCr.setText("CR Note");
                                Log.d("strChecked", strChecked);
                                updateOrderStatus("0", "10", strChecked,strReplace);

                            }
                            type_cr = 0;
                            btnDr.setVisibility(View.GONE);


                        }
                    });

                    crnotedialog.show();

//                    new AlertDialog.Builder(OrderDetailActivity.this)
//
//
//                            .setTitle("Safe'O'Fresh")
//
//                            .setMessage("Are you sure, you want to proceed?")
//
//                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//
//                                }
//                            })
//                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                    //  notifyDataSetChanged();
//                                    //  Toast.makeText(OrderDetailActivity.this, "You Clicked on Cancel", Toast.LENGTH_SHORT).show();
//                                }
//                            })
//
//                            .show();


                }


            }
        });
        if (mOrderBean.getDeliveryMode().equalsIgnoreCase("CR")) {
            btnCr.setVisibility(View.GONE);
        }
        btnCr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (type_cr == 0) {
                    type_cr = 1;
                    btnCr.setText("Cancel");
                    // btn.setImageResource(R.drawable.ic_baseline_close_24);
                    btnDr.setVisibility(View.VISIBLE);
                    //    btncancel.setVisibility(View.INVISIBLE);
                    //  btncancel.setEnabled(false);
                } else {
                    // editProducts.setImageResource(R.drawable.ic_edit);
                    // btncancel.setEnabled(true);

                    if (mType == 0) {
                        btnCr.setText("DR Note");
                    } else {
                        btnCr.setText("CR Note");

                    }
                    type_cr = 0;
                    btnDr.setVisibility(View.GONE);
                }
                mAdapter.changeToEdit(type_cr);

            }
        });
/*
        if (prefs.getPrefsUsercategory().equalsIgnoreCase("1057")) {
            imgCustomer.setVisibility(View.GONE);
            try {
                if (!mOrderBean.getStatus().equalsIgnoreCase("10")) {
                    userLayout.setVisibility(View.VISIBLE);
                } else if(mOrderBean.getStatus().equalsIgnoreCase("10")){
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
            imgCustomer.setVisibility(View.VISIBLE);
            uploadbill.setVisibility(View.GONE);
            userLayout.setVisibility(View.GONE);
            retailerLayout.setVisibility(View.VISIBLE);

        }

*/
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
        }

        if (mOrderBean.getStatus().equalsIgnoreCase("11")) {
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
                btncancel.setVisibility(View.GONE);
                txtStatus.setText("DELIVERED");
                txtStatus.setTextColor(getResources().getColor(R.color.green));
                txtTagline.setText("Your Order has been accepted by customer");
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
        }

        if (mOrderBean.getStatus().equalsIgnoreCase("12")) {
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
                btncancel.setVisibility(View.GONE);
                txtStatus.setText("DELIVERED");
                txtStatus.setTextColor(getResources().getColor(R.color.green));
                txtTagline.setText("Your Order has been accepted by customer");
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
        } else if (mOrderBean.getOrderType().equalsIgnoreCase("CN")) {
            btncancel.setVisibility(View.GONE);

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
        txtId.setText("#00" + mOrderBean.getOrderID());
        txtName1.setText(mOrderBean.getSupplierName());
        txtName.setText(mOrderBean.getCustomerName());

        // txtAddress.setText("Flat no. - " + mOrderBean.getFlatNo() + ", Building - " + mOrderBean.getBuilding() + ", society - " + mOrderBean.getSociety());
        txtAddress.setText("Address - " + mOrderBean.getFlatNo() /*+ ", Building - " + mOrderBean.getBuilding()*/ + ", society - " + mOrderBean.getSociety());
        txtDate.setText(dat1);
        txtQty.setText(mOrderBean.getRItemCOunt());

        try {
            txtPrice.setText(currency + " " + new DecimalFormat("##.##").format(Double.parseDouble(mOrderBean.getRAmount())));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                txtStatus.setText("PROCESSING");
                txtStatus.setTextColor(getResources().getColor(R.color.black));
                if (mOrderBean.getDevliveryDate().isEmpty()) {
                    txtTagline.setText("Your Order will be delivered soon");

                } else {
                    txtTagline.setText("Delivery Time : " + mdt + " between " + mOrderBean.getDeliveryTime());

                }
                //   txtTagline.setText("Your Order will be delivered soon");
                btncancel.setVisibility(View.VISIBLE);
                if (mOrderBean.getOrderType().equalsIgnoreCase("CN")) {
                    btncancel.setVisibility(View.GONE);

                }

            } else if (mOrderBean.getStatus().equalsIgnoreCase("91")) {
                txtStatus.setText("CANCELLED");
                txtTagline.setText("Your Order has been Cancelled");
                txtStatus.setTextColor(getResources().getColor(R.color.red));
                selectedPosition = 0;
                btncancel.setVisibility(View.GONE);
                retailerLayout.setVisibility(View.GONE);

            } else if (mOrderBean.getStatus().equalsIgnoreCase("1")) {
                txtStatus.setText("ACCEPTED BY STORE");
                selectedPosition = 1;
                txtStatus.setTextColor(getResources().getColor(R.color.black));
                btncancel.setVisibility(View.GONE);
                if (mOrderBean.getDevliveryDate().isEmpty()) {
                    txtTagline.setText("Your Order will be delivered soon");

                } else {
                    txtTagline.setText("Delivery Time : " + mdt + " between " + mOrderBean.getDeliveryTime());

                }
            } else if (mOrderBean.getStatus().equalsIgnoreCase("2")) {
                txtStatus.setText("ON HOLD");
                selectedPosition = 1;
                txtStatus.setTextColor(getResources().getColor(R.color.colorAccent));
                btncancel.setVisibility(View.VISIBLE);
                if (mOrderBean.getOrderType().equalsIgnoreCase("CN")) {
                    btncancel.setVisibility(View.GONE);

                }
                txtTagline.setText("Your Order is on hold will be delivered soon");
            } else if (mOrderBean.getStatus().equalsIgnoreCase("10")) {
                txtTagline.setText("Your order has beed delivered ");
                selectedPosition = 2;
                txtStatus.setText("COMPLETED");
                btncancel.setVisibility(View.GONE);
                txtStatus.setTextColor(getResources().getColor(R.color.green));
                retailerLayout.setVisibility(View.GONE);
            }

            else if (mOrderBean.getStatus().equalsIgnoreCase("81")) {
                txtTagline.setText("Your order is Packed ");
                selectedPosition = 2;
                txtStatus.setText("Packed");
                btncancel.setVisibility(View.GONE);
                txtStatus.setTextColor(getResources().getColor(R.color.green));
                retailerLayout.setVisibility(View.GONE);
            }

            else if (mOrderBean.getStatus().equalsIgnoreCase("84")) {
                txtTagline.setText("Your order Parcel is Packed");
                selectedPosition = 2;
                txtStatus.setText("Parcel Packed");
                btncancel.setVisibility(View.GONE);
                txtStatus.setTextColor(getResources().getColor(R.color.green));
                retailerLayout.setVisibility(View.GONE);
            }

            else if (mOrderBean.getStatus().equalsIgnoreCase("82")) {
                txtTagline.setText("Your order is Ready for delivery");
                selectedPosition = 2;
                txtStatus.setText("Ready for delivery");
                btncancel.setVisibility(View.GONE);
                txtStatus.setTextColor(getResources().getColor(R.color.green));
                retailerLayout.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            txtStatus.setTextColor(getResources().getColor(R.color.black));
            txtStatus.setText("PROCESSING");
            if (mOrderBean.getDevliveryDate().isEmpty()) {
                txtTagline.setText("Your Order will be delivered soon");

            } else {
                txtTagline.setText("Delivery Time : " + mdt + " between " + mOrderBean.getDeliveryTime());

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
                taxdialog.show();
                // Utility.opePaymentInfo(OrderDetailActivity.this, mOrderBean.getCompanyID());
            }
        });
        //523
//item picked = 82
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mStts = "0";
                String status = spinnerStatus.getSelectedItem().toString();

                if (status.equalsIgnoreCase("COMPLETED")) {
                    mStts = "10";
                    strChangeStatus = "10";
                    txtStatus.setText("COMPLETED");
                    deliverOrder.show();
                } else if (status.equalsIgnoreCase("Packed")) {
                    mStts = "81";
//                        txtStatus.setText("Packed");

                    strChangeStatus="81";
                    readdeliverydialog.show();
                }

                else if (status.equalsIgnoreCase("Parcel Packed")) {
                    mStts = "84";
//                        txtStatus.setText("Packed");
                    vehicle_header_namedeliv.setText("Parcel Packed");

                    strChangeStatus="84";
                    readdeliverydialog.show();
                }

                else if (status.equalsIgnoreCase("Ready for delivery")) {
                    mStts = "82";
                    strChangeStatus="82";
                    vehicle_header_namedeliv.setText("Ready For Delivery");
                readdeliverydialog.show();
                }
                else if (status.equalsIgnoreCase("Delivered")) {
                    mStts = "10";
                    strChangeStatus="10";
                    vehicle_header_namedeliv.setText("Delivered");
                    readdeliverydialog.show();
                }

                else if (status.equalsIgnoreCase("Cancelled")) {
                    mStts = "91";
                    txtStatus.setText("CANCELLED");
                    canceldialogconfirm.show();



                    Yesdeletbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            cmt = commenttxtdelte.getText().toString();
                            Log.d("mylistsize", String.valueOf(list.size()));


                            if (cmt.equalsIgnoreCase(""))
                            {
                                FancyToast.makeText(OrderDetailActivity.this, "please enter reason for cancellation", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


                            }
                            else
                            {
                                canceldialogconfirm.dismiss();
                                updateOrderStatus(mOrderBean.getOrderID(), "1", "91",strReplace);

                            }



                        }
                    });
                    nodeletebtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            canceldialogconfirm.dismiss();
                        }
                    });


                }
                else {
                    if (status.equalsIgnoreCase("PROCESSING")) {
                        txtStatus.setText("PROCESSING");
                        mStts = "0";
                    } else if (status.equalsIgnoreCase("CANCELLED")) {
                        mStts = "91";
                        txtStatus.setText("CANCELLED");
                    } else if (status.equalsIgnoreCase("ACCEPTED")) {
                        mStts = "1";
                        txtStatus.setText("ACCEPTED BY STORE");
                    } else if (status.equalsIgnoreCase("hold")) {
                        mStts = "2";
                        txtStatus.setText("ON HOLD");
                    } else if (status.equalsIgnoreCase("Out For Delivery")) {
                        mStts = "83";
                        txtStatus.setText("Out For Delivery");
                    }







                    updateOrderStatus(mOrderBean.getOrderID(), "1", mStts,strReplace);
                }
            }
        });


        HelpDialog = new Dialog(this);
        HelpDialog.setContentView(R.layout.helplayout);
        helpline = HelpDialog.findViewById(R.id.helpline);
        teamno = HelpDialog.findViewById(R.id.teamcontact);
        callhelpline = HelpDialog.findViewById(R.id.cllhelpline);
        callshop = HelpDialog.findViewById(R.id.callph);
        callteam = HelpDialog.findViewById(R.id.callteam);
        shopno = HelpDialog.findViewById(R.id.shopno);
        shopno.setText(mOrderBean.getSupplierPhone());
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
        rvImages.setLayoutManager(new LinearLayoutManager(OrderDetailActivity.this, RecyclerView.HORIZONTAL, false));
        orderImagesAdapter = new OrderImagesAdapter(this, mListImages, this);
        rvImages.setAdapter(orderImagesAdapter);

        if (prefs.getPrefsUsercategory().equalsIgnoreCase("1057")) {
            imgCustomer.setVisibility(View.GONE);
            try {
                if (!mOrderBean.getStatus().equalsIgnoreCase("10")) {
                    userLayout.setVisibility(View.VISIBLE);
                    btncancel.setText("Cancel");
                } else if (mOrderBean.getStatus().equalsIgnoreCase("10")) {
                    userLayout.setVisibility(View.VISIBLE);
                    btncancel.setVisibility(View.VISIBLE);
                    btncancel.setText("Accept Order");
                    if (mOrderBean.getOrderType().equalsIgnoreCase("CN")) {
                        btncancel.setVisibility(View.GONE);

                    }

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
            imgCustomer.setVisibility(View.VISIBLE);
            uploadbill.setVisibility(View.GONE);
            userLayout.setVisibility(View.GONE);
            retailerLayout.setVisibility(View.VISIBLE);

        }


    }

    /**
     * Called when the activity has detected the user's press of the back
     * key. The {@link #getOnBackPressedDispatcher() OnBackPressedDispatcher} will be given a
     * chance to handle the back button before the default behavior of
     * {@link Activity#onBackPressed()} is invoked.
     *
     * @see #getOnBackPressedDispatcher()
     */

    @Override
    public void onBackPressed() {

        Intent intent = new Intent();
        Bundle args = new Bundle();
        args.putInt("mpos", mPos);
        mDataEditor.putString("mListData", new Gson().toJson(listclone));
        mDataEditor.commit();
        intent.putExtras(args);
        setResult(RESULT_OK, intent);


        super.onBackPressed();
    }

    OrderImagesAdapter orderImagesAdapter;
    ArrayList<String> mListImages = new ArrayList<>();
    Boolean isOrderImage = false;



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK) {
            ArrayList<String> mImages = new ArrayList<>();
            if (requestCode == com.vincent.filepicker.Constant.REQUEST_CODE_TAKE_IMAGE) {
                log.e("request code true  ");
                ArrayList<ImageFile> list = data.getParcelableArrayListExtra(com.vincent.filepicker.Constant.RESULT_PICK_IMAGE);
                StringBuilder builder = new StringBuilder();
                for (ImageFile file : list) {
                    String path = file.getPath();
                    Log.e("pathpath", path + "");
                    builder.append(path + "\n");
                    mImages.add(path);
                    selectedImagesAdapter.addItems(mImages);
                    isOrderImage = true;
                    if (imageType == 1) {
                        Glide.with(OrderDetailActivity.this)
                                .load(path)
                                .apply(new RequestOptions().placeholder(R.drawable.no_image).error(R.drawable.no_image))
                                .into(imgPhoto);

                    } else if (imageType == 2) {

                    } else {
                        Glide.with(OrderDetailActivity.this)
                                .load(path)
                                .apply(new RequestOptions().placeholder(R.drawable.no_image).error(R.drawable.no_image))
                                .into(imgPayment);

                    }
                    // loadImage(path);
//                    mAdapter1.addItems(mImages);
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
                    Log.e("pathpath1", path + "");
                    builder.append(path + "\n");
                    mImages.add(path);
                    isOrderImage = true;
                    selectedImagesAdapter.addItems(mImages);

                    if (imageType == 1) {


//                        Glide.with(OrderDetailActivity.this)
//                                .load(path)
//                                .apply(new RequestOptions().placeholder(R.drawable.no_image).error(R.drawable.no_image))
//                                .into(imgPhoto);

                    } else if (imageType == 2) {
                        Glide.with(OrderDetailActivity.this)
                                .load(path)
                                .apply(new RequestOptions().placeholder(R.drawable.no_image).error(R.drawable.no_image))
                                .into(imgSign);

                    } else {

                        Glide.with(OrderDetailActivity.this)
                                .load(path)
                                .apply(new RequestOptions().placeholder(R.drawable.no_image).error(R.drawable.no_image))
                                .into(imgPayment);

                    }

//                    mAdapter1.addItems(mImages);
                    //  loadImage(path);
//                    //  if (!mBean.getStrrmasterid().isEmpty()) {

                    new ImageCompressionAsyncTask(true).execute(path);
                }
                //   }
            }

            if (requestCode == REQUEST_CODE) {

                SharedPreferences prefs;
                prefs = PreferenceManager.getDefaultSharedPreferences(OrderDetailActivity.this);
                final String path = prefs.getString(PREFS_FILE_PATH, filePath);
                Log.d("filep", String.valueOf(filePath));
                mListImages.add(path);
                orderImagesAdapter.notifyDataSetChanged();
                //      orderImagesAdapter.addItems(path);
                //     new ImageCompressionAsyncTask(true).execute(path);

            } else if (requestCode == 10124) {
                try {
                    String strEditText = data.getStringExtra("mBar");
                    edtBarcode.setText(strEditText);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public void makeHelp(View v) {
        HelpDialog.show();
    }

    public void cancelOrder(View v) {
        if (btncancel.getText().toString().equalsIgnoreCase("Accept Order")) {
            acceptDialog.show();

        } else {
            status = "91";
            Log.d("mylistsize", String.valueOf(list.size()));
        }




        if (prefs.getPrefsUsercategory().equalsIgnoreCase("1057")) {

            cmtll.setVisibility(View.VISIBLE);
            commenttxtdelte.setVisibility(View.VISIBLE);
            canceldialogconfirm.show();
        } else {

            cmtll.setVisibility(View.VISIBLE);
            commenttxtdelte.setVisibility(View.VISIBLE);
            canceldialogconfirm.show();

        }
        Yesdeletbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = "91";

                cmt = commenttxtdelte.getText().toString();
                Log.d("mylistsize", String.valueOf(list.size()));



                            if (cmt.equalsIgnoreCase(""))
                            {
                                FancyToast.makeText(OrderDetailActivity.this, "please enter reason for cancellation", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


                            }
                            else
                            {
                                canceldialogconfirm.dismiss();
                                updateOrderStatus(mOrderBean.getOrderID(), "1", "91",strReplace);

                            }



            }
        });
        nodeletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                canceldialogconfirm.dismiss();
            }
        });

    }

    public void giveReview(View v) {
        Utility.DialogOrderFeedback(OrderDetailActivity.this, mOrderBean.getOrderID(), 0);
    }

    public void makeBackClick(View v) {


        Intent intent = new Intent();
        Bundle args = new Bundle();
        args.putSerializable("Listt", (Serializable) listclone);

        intent.putExtras(args);
        setResult(RESULT_OK, intent);


        onBackPressed();

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


    public void updateOrderStatus(String oid, String type, String status,String replaceitems) {


        final ProgressDialog mProgressBar = new ProgressDialog(OrderDetailActivity.this);
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
                            String responses = jkHelper.Decryptapi(response, OrderDetailActivity.this);
                            JSONObject jsonObject = new JSONObject(responses);
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                //  txtStatus.setText("CANCELLED");
                                Double mTotal1 = 0.0, totalamountcr = 0.0;

                                mAdapter.notifyDataSetChanged();

                                mAdapter = new OrderItemsAdapter(OrderDetailActivity.this, mOrderBean.getmListItems(), status, OrderDetailActivity.this, type_cr, mOrderBean.getDeliveryMode());
                                lvProducts.setAdapter(mAdapter);
                                if (mOrderBean.getDeliveryMode().equalsIgnoreCase("COD")) {

                                } else {
                                    for (int k = 0; k < mOrderBean.getmListItems().size(); k++) {


                                        if (!mOrderBean.getmListItems().get(k).getStatus().equalsIgnoreCase("91")) {
                                            mTotal1 = (Double.parseDouble(mOrderBean.getmListItems().get(k).getRate()) * Double.parseDouble(mOrderBean.getmListItems().get(k).getRequestedQty())) + mTotal1;


                                        } else {
                                            listclone.get(mPos).getmListItems().get(k).setStatus("91");
                                        }


                                    }
                                    totalamountcr = Double.parseDouble(prefs.getPREFS_currentbal()) + mTotal1;
                                    prefs.setPREFS_currentbal(String.valueOf(totalamountcr));
                                }


                                userLayout.setVisibility(View.GONE);
                                if (status.equalsIgnoreCase("83")) {
                                    if (mListDeliveryBoys.size() > 0) {

                                        dialogAssignOrder.show();
                                    } else {
                                        isTrue = true;

                                        getDeliveryBoys();
                                    }
                                }

                                try {
                                    Toast.makeText(getApplicationContext(), "Updated Successfully !!", Toast.LENGTH_LONG).show();

                                    if (prefs.getPrefsUsercategory().equalsIgnoreCase("1057"))
                                    {
                                        if (acceeptcrnotevar==1)
                                        {
                                            startActivity(new Intent(getApplicationContext(),OrderHistoryActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK));
                                        }

                                    }

                                    crnotedialog.dismiss();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
//                                if (status.equalsIgnoreCase("91")) {
//                                    if (!mOrderBean.getOrderType().equalsIgnoreCase("RO")) {
//                                        if (mOrderBean.getPaymentMode().equalsIgnoreCase("Online Payment")) {
//                                            Utility.addMoneyToWallet(OrderDetailActivity.this, mOrderBean.getRAmount() + "", mOrderBean.getOrderID(), "3");
//                                        }
//                                    }
//                                }
                                uploadimage();
                            } else {

                                FancyToast.makeText(OrderDetailActivity.this, "failed to update the order, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
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
                        FancyToast.makeText(OrderDetailActivity.this, "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


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
                prefs = new Preferencehelper(OrderDetailActivity.this);
                Map<String, String> params = new HashMap<String, String>();

                if (status.equalsIgnoreCase("12"))
                {
                    strReplace="";

                }
                Log.d("inmethodesbeforeparam",strReplace);

                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);

                    String param = getString(R.string.UPDATE_ORDER_STATUS) + "&orderid=" + oid + "&status=" + status + "&type=" + type + "&contactId=" + prefs.getPrefsContactId() + "&PaymentMode=" + paymode + "&Comment=" + cmt+"&ReplaceItems="+replaceitems;
                    Log.d("Beforeencrptionupdate", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, OrderDetailActivity.this);
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
//        if (!mOrderBean.getOrderType().equalsIgnoreCase("RO")) {
//            if (mOrderBean.getPaymentMode().equalsIgnoreCase("Online Payment")) {
             //   Utility.addMoneyToWallet(OrderDetailActivity.this, beanData.getRAmount() + "", mOrderBean.getOrderID(), "3");
//            }
//
//        }
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

    String lastRowMaterTable;
    ArrayList<String> mListLastRows = new ArrayList<>();
    String serverid = "";
    OrderBean.OrderItemsBean mBeanItems;

    private void updateserverphotoid1() {
        for (int a = 0; a < mListLastRows.size(); a++) {
            if (mListLastRows.get(a) != null) {
                SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
                ImageUploadDAO pd = new ImageUploadDAO(database, OrderDetailActivity.this);
                pd.setWorkIdToTable(String.valueOf(mOrderBean.getOrderID()), mListLastRows.get(a));
                ImageMasterDAO pds = new ImageMasterDAO(database, OrderDetailActivity.this);
                pds.setServerDetails(Integer.parseInt(mListLastRows.get(a)), Integer.valueOf(mOrderBean.getOrderID()), 0);
                ImageMasterDAO iDao = new ImageMasterDAO(database, getApplicationContext());
                String serverId = iDao.getServerIdById(1);
                DatabaseManager.getInstance().closeDatabase();
                log.e("in last row master table inserting  ");
            }


        }


    }

    @Override
    public void onPopupListener(int mPosition, OrderBean.OrderItemsBean mBeanItems) {
        this.mBeanItems = mBeanItems;
        vehicle_header_name.setText(mBeanItems.getItemDescription());
      /*  edtBarcode.setText("");
        mListImages=new ArrayList<>();
        orderImagesAdapter.notifyDataSetChanged();
  */
        dialogItemDetail.show();
    }

    @Override
    public void Cancelclk(boolean cancelclk, OrderBean.OrderItemsBean mBeanItems, int posi) {


        if (cancelclk) {
            btncancel.setVisibility(View.GONE);
            listclone.get(mPos).getmListItems().get(posi).setStatus("91");
            //  listclone.get(mPos).setRAmount(txtPrice.getText().toString().replace(currency,""));


        } else {
            double finalramount = Double.parseDouble(txtPrice.getText().toString().replace(currency, "")) - Double.parseDouble(mBeanItems.getRate());
            txtPrice.setText(currency + " " + new DecimalFormat("##.##").format(finalramount));
            listclone.get(mPos).getmListItems().get(posi).setStatus("91");
            if (mOrderBean.getDeliveryMode().equalsIgnoreCase("COD")) {

            } else {
                listclone.get(mPos).setRAmount(txtPrice.getText().toString().replace(currency, ""));

            }


        }


    }


    Bitmap bitmap;
    Uri imageuri;
    String filePath;
    final int CROP_PIC = 2;
    private Uri picUri;
    public static final int CROP_IMAGE_ACTIVITY_REQUEST_CODE = 203;
    String url, imageFilePath;
    private static final int REQUEST_CAPTURE_IMAGE = 100;

    private final int REQUEST_CODE = 1010;
    private String PREFS_FILE_PATH = "capture_file_path";

    public void openCamera() {
        SharedPreferences prefs;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        filePath = getOutputMediaFileUri(); // create a file to save the image
        prefs = PreferenceManager.getDefaultSharedPreferences(OrderDetailActivity.this);
        prefs.edit().putString(PREFS_FILE_PATH, filePath).commit();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(filePath))); // set the image file name
        Log.d("file path in open", String.valueOf(filePath));
        startActivityForResult(intent, REQUEST_CODE);
        Log.d("open camera is called", "called");
    }

    public String getOutputMediaFileUri() {
        return getOutputMediaFile().getAbsolutePath();
    }

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

    @Override
    public void onImageDelete(int mPosition) {
        mListImages.remove(mPosition);
        orderImagesAdapter.notifyDataSetChanged();

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
            addToDatabase();
        }

    }

    String mImagePathDataBase, mImageNameDataBase, mCurrentTimeDataBase;

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

    //fetch
    //order_detail

    private void uploadimage() {
        if (JKHelper.isConnectedToNetwork(OrderDetailActivity.this) && !JKHelper.isServiceRunning(OrderDetailActivity.this, ImageUploadService.class)) {
            JKHelper.showLog("Start Service");
            startService(new Intent(OrderDetailActivity.this, ImageUploadService.class));
        } else {
            stopService(new Intent(OrderDetailActivity.this, ImageUploadService.class));
            startService(new Intent(OrderDetailActivity.this, ImageUploadService.class));
        }
    }

    private File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Safe'O'Fresh");
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


    String path = "";
    private boolean addedToMasterTable = false;

    int masterDataBaseId;
    private ImageLoadingUtils utils;

    TextView txtacceptinfo;
    Dialog CameraDialog;
    Dialog acceptDialog;
    ListView itmsList;
    RadioGroup rgChangeQty;
    RadioButton rbYes;
    RadioButton rbNo;
    RelativeLayout qtyLayout;
    EditText edtQty;
    Button btnAccept;
    ImageView acceptClose;

    public void orderAcceptDialog() {
        acceptDialog = new Dialog(OrderDetailActivity.this);
        acceptDialog.setContentView(R.layout.dialog_accept_order);
        rgChangeQty = acceptDialog.findViewById(R.id.rgChangeQty);
        itmsList = acceptDialog.findViewById(R.id.lvProducts);
        rbYes = acceptDialog.findViewById(R.id.rbYes);
        rbNo = acceptDialog.findViewById(R.id.rbNo);
        qtyLayout = acceptDialog.findViewById(R.id.qtyLayout);
        edtQty = acceptDialog.findViewById(R.id.edtQty);
        txtacceptinfo = acceptDialog.findViewById(R.id.txtinfoaccept);
        btnAccept = acceptDialog.findViewById(R.id.btnSubmit);
        acceptClose = acceptDialog.findViewById(R.id.closeHeader);
        mAdapter = new OrderItemsAdapter(OrderDetailActivity.this, list, status, OrderDetailActivity.this, 55, mOrderBean.getDeliveryMode());
        itmsList.setAdapter(mAdapter);
        rgChangeQty.check(R.id.rbNo);
        itmsList.setVisibility(View.GONE);
        txtacceptinfo.setVisibility(View.GONE);


        rgChangeQty.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbYes) {
                    type_cr = 1;
                    qtyLayout.setVisibility(View.VISIBLE);
                    mAdapter.changeToEdit(type_cr);
                    itmsList.setVisibility(View.VISIBLE);
                    txtacceptinfo.setVisibility(View.VISIBLE);


                } else {
                    qtyLayout.setVisibility(View.GONE);
                    type_cr = 0;
                    itmsList.setVisibility(View.GONE);
                    mAdapter.changeToEdit(type_cr);
                    txtacceptinfo.setVisibility(View.GONE);


                }

            }
        });
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rgChangeQty.getCheckedRadioButtonId() == R.id.rbYes) {
                    strReplace = "";
                    acceeptcrnotevar=1;


                    updateOrderStatus(mOrderBean.getOrderID(), "1", "12",strReplace);


                    strChecked = "";

                    List<OrderBean.OrderItemsBean> mlistreplace = new ArrayList<>();
                    List<OrderBean.OrderItemsBean> mListChecked = new ArrayList<>();
                    mListChecked = mAdapter.getChckedProductsAccept();
                    for (int a = 0; a < mListChecked.size(); a++) {


                        if (strChecked.isEmpty()) {


                            strChecked = mListChecked.get(a).getOrderdetailID() + "/" + mListChecked.get(a).getQuantity();
                        } else {
                            strChecked = strChecked + "//" + mListChecked.get(a).getOrderdetailID() + "/" + mListChecked.get(a).getQuantity();

                        }


                    }


                    mlistreplace = mAdapter.getReplace();
                    Log.d("Replacelistsize",String.valueOf(mlistreplace.size()));
                    for (int a = 0; a < mlistreplace.size(); a++) {


                        if (strReplace.isEmpty()) {


                            strReplace = mlistreplace.get(a).getOrderdetailID() + "/" + mlistreplace.get(a).getQuantity();
                        } else {
                            strReplace = strReplace + "//" + mlistreplace.get(a).getOrderdetailID() + "/" + mlistreplace.get(a).getQuantity();

                        }


                    }

                    Log.d("mstrreplace",strReplace);



                    if (strChecked.isEmpty()) {

                        Log.d("insideloopstrreplace",strReplace);
                        updateOrderStatus(mOrderBean.getOrderID(), "10", strChecked,strReplace);
                        // FancyToast.makeText(OrderDetailActivity.this, "please select atleast one product.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();

                    } else {


                        updateOrderStatus(mOrderBean.getOrderID(), "10", strChecked,strReplace);
                    }
                } else {
                    acceeptcrnotevar=1;
                    updateOrderStatus(mOrderBean.getOrderID(), "1", "11",strReplace);

                }

                acceptDialog.dismiss();
            }
        });
        acceptClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                acceptDialog.dismiss();

            }
        });
    }

    Dialog deliverOrder,readdeliverydialog;
    EditText edtNote,edtNotedeliv;
    Button btnDeliver,btnDeliverdeliv;
    ImageView imgPhoto, imgSign, deliveryClose,imgSigndeliv,imgPhotodeliv,deliveryClosedeliv;
    LinearLayout signLayout, photoLayout,signLayoutdeliv,photoLayoutdeliv;
    String strChangeStatus = "";

    int imageType = 0;
    RecyclerView rvImagescomp,rvImagesdeliv;
    TextView vehicle_header_namedeliv;

    SelectedImagesAdapter selectedImagesAdapter,selectedimagedeliv;
    public void orderCompleteDialog() {
        deliverOrder = new Dialog(OrderDetailActivity.this);
        deliverOrder.setContentView(R.layout.dialog_complete_order);

        rvImagescomp= deliverOrder.findViewById(R.id.rvImagescomp);
        photoLayout = deliverOrder.findViewById(R.id.photoLayout);
        signLayout = deliverOrder.findViewById(R.id.signLayout);
        edtNote = deliverOrder.findViewById(R.id.edtNote);

        imgSign = deliverOrder.findViewById(R.id.imgSign);
        imgPhoto = deliverOrder.findViewById(R.id.imgPhoto);
        btnDeliver = deliverOrder.findViewById(R.id.btnSubmit);
        deliveryClose = deliverOrder.findViewById(R.id.closeHeader);
        mClearButton =   deliverOrder.findViewById(R.id.clear_button);
        mSignaturePad = deliverOrder.findViewById(R.id.signature);
        rvImagescomp.setLayoutManager(new LinearLayoutManager(OrderDetailActivity.this, RecyclerView.HORIZONTAL, false));
        selectedImagesAdapter = new SelectedImagesAdapter(this, mListSelectedIamges);
        rvImagescomp.setAdapter(selectedImagesAdapter);

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
                if (ActivityCompat.checkSelfPermission(OrderDetailActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(OrderDetailActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                } else {

                    Intent intent1 = new Intent(OrderDetailActivity.this, ImagePickActivity.class);
                    intent1.putExtra(IS_NEED_CAMERA, true);
                    intent1.putExtra(com.vincent.filepicker.Constant.MAX_NUMBER, 100);
                    intent1.putExtra(IS_NEED_FOLDER_LIST, true);
                    startActivityForResult(intent1, com.vincent.filepicker.Constant.REQUEST_CODE_PICK_IMAGE);
                }
            }
        });
        signLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageType = 2;

                if (ActivityCompat.checkSelfPermission(OrderDetailActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(OrderDetailActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                } else {

                    Intent intent1 = new Intent(OrderDetailActivity.this, ImagePickActivity.class);
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
                cmt = edtNote.getText().toString();

                Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();

                imageType = 1;


                String imgfile = addpngSignatureToGallery(signatureBitmap);
                log.e("file path in request code true==" + imgfile);
                //  if (!mBean.getStrrmasterid().isEmpty()) {
                if (!imgfile.isEmpty()) {
                    //  Toast.makeText(OrderDetailActivity.this, "Signature saved into the Gallery", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(OrderDetailActivity.this, "Unable to store the signature", Toast.LENGTH_SHORT).show();
                }
                new ImageCompressionAsyncTask(true).execute(imgfile);
                updateOrderStatus(mOrderBean.getOrderID(), "1", strChangeStatus,strReplace);

                updateserverphotoid1();
                uploadimage();


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

    public void Readyfordeliverdialog() {
        readdeliverydialog = new Dialog(OrderDetailActivity.this);
        readdeliverydialog.setContentView(R.layout.dialog_ready_delivery);


        rvImagesdeliv= readdeliverydialog.findViewById(R.id.rvImagesdeliv);
        vehicle_header_namedeliv=readdeliverydialog.findViewById(R.id.vehicle_header_namedeliv);
        photoLayoutdeliv = readdeliverydialog.findViewById(R.id.photoLayoutdeliv);
        signLayoutdeliv = readdeliverydialog.findViewById(R.id.signLayoutdeliv);
        edtNotedeliv = readdeliverydialog.findViewById(R.id.edtNotedeliv);

        imgSigndeliv = readdeliverydialog.findViewById(R.id.imgSigndeliv);
        imgPhotodeliv = readdeliverydialog.findViewById(R.id.imgPhotodeliv);
        btnDeliverdeliv = readdeliverydialog.findViewById(R.id.btnSubmitdeliv);
        deliveryClosedeliv = readdeliverydialog.findViewById(R.id.closeHeaderdeliv);
        mClearButtondeliv = readdeliverydialog.findViewById(R.id.clear_buttondeliv);
        mSignaturePaddeliv = readdeliverydialog.findViewById(R.id.signaturedeliv);
        rvImagesdeliv.setLayoutManager(new LinearLayoutManager(OrderDetailActivity.this, RecyclerView.HORIZONTAL, false));
        selectedImagesAdapter = new SelectedImagesAdapter(this, mListSelectedIamges);
        rvImagesdeliv.setAdapter(selectedImagesAdapter);

        mSignaturePaddeliv.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
            }

            @Override
            public void onSigned() {
                mClearButtondeliv.setEnabled(true);
            }

            @Override
            public void onClear() {
                mClearButtondeliv.setEnabled(false);
            }
        });


        mClearButtondeliv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignaturePaddeliv.clear();
            }
        });

        photoLayoutdeliv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageType = 1;
                if (ActivityCompat.checkSelfPermission(OrderDetailActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(OrderDetailActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                } else {

                    Intent intent1 = new Intent(OrderDetailActivity.this, ImagePickActivity.class);
                    intent1.putExtra(IS_NEED_CAMERA, true);
                    intent1.putExtra(com.vincent.filepicker.Constant.MAX_NUMBER, 100);
                    intent1.putExtra(IS_NEED_FOLDER_LIST, true);
                    startActivityForResult(intent1, com.vincent.filepicker.Constant.REQUEST_CODE_PICK_IMAGE);
                }
            }
        });
        signLayoutdeliv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageType = 2;

                if (ActivityCompat.checkSelfPermission(OrderDetailActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(OrderDetailActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                } else {

                    Intent intent1 = new Intent(OrderDetailActivity.this, ImagePickActivity.class);
                    intent1.putExtra(IS_NEED_CAMERA, true);
                    intent1.putExtra(com.vincent.filepicker.Constant.MAX_NUMBER, 1);
                    intent1.putExtra(IS_NEED_FOLDER_LIST, true);
                    startActivityForResult(intent1, com.vincent.filepicker.Constant.REQUEST_CODE_PICK_IMAGE);
                }
            }
        });
        btnDeliverdeliv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmt = edtNotedeliv.getText().toString();

                Bitmap signatureBitmap = mSignaturePaddeliv.getSignatureBitmap();




                String imgfile = addpngSignatureToGallery(signatureBitmap);
                log.e("file path in request code true==" + imgfile);
                //  if (!mBean.getStrrmasterid().isEmpty()) {
                if (!imgfile.isEmpty()) {
                    //  Toast.makeText(OrderDetailActivity.this, "Signature saved into the Gallery", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(OrderDetailActivity.this, "Unable to store the signature", Toast.LENGTH_SHORT).show();
                }
                new ImageCompressionAsyncTask(true).execute(imgfile);
                updateOrderStatus(mOrderBean.getOrderID(), "1", strChangeStatus,strReplace);

                updateserverphotoid1();
                uploadimage();

                readdeliverydialog.dismiss();
                strChangeStatus = "";
            }
        });
        deliveryClosedeliv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                readdeliverydialog.dismiss();
                strChangeStatus = "";

            }
        });
    }



    private void addToDatabase() {


        if (!addedToMasterTable) {
            final ArrayList<DbImageMaster> arrayList = new ArrayList<>();

            DbImageMaster modle = new DbImageMaster();
            modle.setmDate(JKHelper.getCurrentDate());
            modle.setmUploadStatus(0);
            modle.setmServerId(mOrderBean.getOrderID());
            if (imageType == 1) {
                modle.setmDescription("order_master");
                modle.setmImageType("order_master");


            } else if (imageType == 2) {
                modle.setmDescription("order_master");
                modle.setmImageType("order_master");

            } else {
                modle.setmDescription("order_item_master");
                modle.setmImageType("order_item_master");
            }
            arrayList.add(modle);

            DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                @Override
                public void run(SQLiteDatabase database) {

                    ImageMasterDAO dao = new ImageMasterDAO(database, OrderDetailActivity.this);
                    ArrayList<DbImageMaster> list = arrayList;
                    dao.insert(list);
                    addedToMasterTable = true;
                }
            });
        }


        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                ImageMasterDAO dao = new ImageMasterDAO(database, OrderDetailActivity.this);
                dao.getlatestinsertedid();
                masterDataBaseId = dao.getlatestinsertedid();
                lastRowMaterTable = String.valueOf(masterDataBaseId);
                mListLastRows.add(lastRowMaterTable);
                // String.valueOf(mBeanItems.getOrderdetailID());
            }
        });


        final ArrayList<DbImageUpload> arrayListUpload = new ArrayList<>();
        DbImageUpload modle = new DbImageUpload();
        modle.setmDate(JKHelper.getCurrentDate());
        modle.setmImageUploadStatus(0);
        if (imageType == 1) {
            modle.setmDescription("order_master");
            modle.setmImageType("order_master");
            modle.setmServerId(mOrderBean.getOrderID());
            modle.setmImageId(Integer.parseInt(mOrderBean.getOrderID()));

        } else if (imageType == 2) {
            modle.setmDescription("order_master");
            modle.setmImageType("order_master");
            modle.setmServerId(mOrderBean.getOrderID());
            modle.setmImageId(Integer.parseInt(mOrderBean.getOrderID()));

        } else {
            modle.setmDescription("order_item_master");
            modle.setmImageType("order_item_master");
            modle.setmServerId(mBeanItems.getOrderdetailID());
            modle.setmImageId(Integer.parseInt(mBeanItems.getOrderdetailID()));
        }
        modle.setmImagePath(mImagePathDataBase);
        modle.setmImageName(mImageNameDataBase);
        arrayListUpload.add(modle);


        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                ImageUploadDAO dao = new ImageUploadDAO(database, OrderDetailActivity.this);
                ArrayList<DbImageUpload> list = arrayListUpload;
                dao.insert(list);

                log.e("photo inserted ");
            }
        });

        //   setPhotoCount();
    }


    ArrayList<DeliveryBoysBean> mListDeliveryBoys = new ArrayList<>();
    Boolean isTrue = false;

    public void getDeliveryBoys() {
        final ProgressDialog mProgressBar = new ProgressDialog(OrderDetailActivity.this);
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
                            String responses = jkHelper.Decryptapi(response, OrderDetailActivity.this);
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
                                    spBoys.setAdapter(new ArrayAdapter<String>(OrderDetailActivity.this, android.R.layout.simple_list_item_1, mListBoys));
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
                    String finalparam = jkHelper.Encryptapi(param, OrderDetailActivity.this);
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
        final ProgressDialog mProgressBar = new ProgressDialog(OrderDetailActivity.this);
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
                            String responses = jkHelper.Decryptapi(response, OrderDetailActivity.this);
                            Log.d("resp_delivery", responses);
                            JSONObject jsonObject = new JSONObject(responses);
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                FancyToast.makeText(OrderDetailActivity.this, "Order Assigned Successfully to " + mListDeliveryBoys.get(spBoys.getSelectedItemPosition() - 1).getFirstName(), FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                                dialogAssignOrder.dismiss();

                            } else {
                                FancyToast.makeText(OrderDetailActivity.this, "failed to assign order, try again.", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();

                            }
                        } catch (Exception e) {
                            FancyToast.makeText(OrderDetailActivity.this, "failed to assign order, try again.", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
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
                prefs = new Preferencehelper(OrderDetailActivity.this);

                Map<String, String> params1 = new HashMap<String, String>();
                //   params1.put("contactid", prefs.getPrefsContactId());
                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);
//=35&=ashwani%20sahu&=7&=59298
                    String param = getString(R.string.UPDATE_ORDER_STATUS) + "&type=7" + "&orderid=" + mOrderBean.getOrderID() + "&status=" + mListDeliveryBoys.get(spBoys.getSelectedItemPosition() - 1).getFirstName() + "&contactId=" + userId /*+ "&fdate=" + mStartDate + "&tdate=" + mTodayDate + "&type=" + mType*/;
                    Log.d("before_enc_odr", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, OrderDetailActivity.this);
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

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private SignaturePad mSignaturePad,mSignaturePaddeliv;
    private Button mClearButton,mClearButtondeliv;


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(OrderDetailActivity.this, "Cannot write images to external storage", Toast.LENGTH_SHORT).show();
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

    public String addpngSignatureToGallery(Bitmap signature) {
        String result = "";
        try {
            File photo = new File(getAlbumStorageDir("SignatureMp"), String.format("Signature_m_parcel%d.png", System.currentTimeMillis()));
            saveBitmapToJPG(signature, photo);
            scanMediaFile(photo);
            Log.e("signaturephotopath", photo.getPath());
            result = photo.getAbsolutePath() + "";
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
        File f = new File(context.getCacheDir(), "signaturee+" + position + ".png");
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

}