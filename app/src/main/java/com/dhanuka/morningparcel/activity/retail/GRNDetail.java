package com.dhanuka.morningparcel.activity.retail;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
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
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.R;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.filter.entity.ImageFile;

import org.json.JSONArray;
import org.json.JSONException;
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
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import com.dhanuka.morningparcel.Helper.DbImageMaster;
import com.dhanuka.morningparcel.Helper.ImageLoadingUtils;
import com.dhanuka.morningparcel.Helper.ImageMasterDAO;
import com.dhanuka.morningparcel.Helper.ImageUploadDAO;
import com.dhanuka.morningparcel.Helper.NetworkUtil;
import com.dhanuka.morningparcel.SqlDatabase.DatabaseHelper;
import com.dhanuka.morningparcel.SqlDatabase.DatabaseManager;
import com.dhanuka.morningparcel.SqlDatabase.QueryExecutor;
import com.dhanuka.morningparcel.activity.Message;
import com.dhanuka.morningparcel.activity.barcode.Bar;
import com.dhanuka.morningparcel.activity.retail.adapter.AutoCompleteItemAdapter;
import com.dhanuka.morningparcel.activity.retail.adapter.SBillableUnderAdapter;
import com.dhanuka.morningparcel.activity.retail.beans.dbitemmaster;
import com.dhanuka.morningparcel.adapter.OrderImagesAdapter;
import com.dhanuka.morningparcel.adapter.SelectedImagesAdapter;
import com.dhanuka.morningparcel.customViews.DelayAutoCompleteTextView;
import com.dhanuka.morningparcel.database.itemmasterdao;
import com.dhanuka.morningparcel.events.OnImageDeleteListener;
import com.dhanuka.morningparcel.utils.DbImageUpload;
import com.dhanuka.morningparcel.utils.ImageUploadService;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.utils.PermissionModule;
import com.dhanuka.morningparcel.utils.PermissionUtils;
import com.dhanuka.morningparcel.utils.Preferencehelper;
import com.dhanuka.morningparcel.utils.log;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.vincent.filepicker.activity.BaseActivity.IS_NEED_FOLDER_LIST;
import static com.vincent.filepicker.activity.ImagePickActivity.IS_NEED_CAMERA;

//import com.dhanuka.morningparcel.utils.Preferencehelper;

public class GRNDetail extends AppCompatActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks, OnImageDeleteListener {
    Float ftTotalBaseAmt = 0f;
    Float ftTotalTaxAmt = 0f;
    Float ftTotalWithTaxAmt = 0f;

    Float ftTotalBaseAmt1 = 0f;
    Float ftTotalTaxAmt1 = 0f;
    Float ftTotalWithTaxAmt1 = 0f;
    Float ftTotalcess = 0f;

    TextView txtTotalBase, txtTotalcess,
            txtTotalTax,
            txtTotal;
    RadioButton rbItem,
            rbBarcode;
    RadioGroup rgSearch;
    String branchid;
    RecyclerView rvImages1;
    SelectedImagesAdapter mAdapter1;
    ArrayList<String> mListSelectedIamges1 = new ArrayList<>();
    ArrayList<String> mListLastRows1 = new ArrayList<>();

    //private TextView tvPhotoCount ;
    private ImageView pickimagebtn1;

    Dialog CameraDialog;

    @BindView(R.id.searchmanualbarid)
    ImageView searchmanualbarid;
    int txType = 1;
    ImageView pickimagebtn, closeHeader1;
    String strtax = "Without Tax";
    OrderImagesAdapter orderImagesAdapter;
    RecyclerView rvImages;
    Button btnSubmitPic;
    ArrayList<String> mListImages = new ArrayList<>();
    public static final int REQUEST_PERMISSION = 0x01;
    Dialog dialogItemDetail;
    //    PosMifareCardReader cardReader = null;
    final static int MAX_TRY_CNT = 5;
    Button btn_submit_Master;
    String lastRowMaterTable;
    LinearLayout ll_updateData;
    Button btnSubmitAssign;
    private TextView tvPhotoCount, toolbar_title;
    private ImageView ivcamera;
    private MaterialEditText et_Qty, etDate, tvcomment, et_BoxBarcode, et_item_IDs;
    DelayAutoCompleteTextView et_item_name;
    AutoCompleteTextView et_ItemBarcode;

    //   private MaterialAutoCompleteTextView et_item_name;
    MaterialEditText et_CESS;
    MaterialEditText et_CESS1;
    MaterialEditText et_TAX;
    MaterialEditText et_TAX1;
    MaterialEditText et_MRP;
    MaterialEditText et_HSN;
    MaterialEditText et_BaseAmt;

    private LinearLayout ll_manufacturingdate;
    private Button btnCancel, btnSubmit, btnGetdetail;
    private String filePath;
    private Spinner spnr_exphead;
    private ImageView iv_ItemBarcode, iv_BoxBarcode;
    private String woid, strworktype, strclientname, strvehicleno, strsupplierid;
    private final int REQUEST_CODE = 1010;
    private String PREFS_FILE_PATH = "capture_file_path";
    String mImagePathDataBase;
    String mImageNameDataBase;
    String mCurrentTimeDataBase, strvehicletype;
    private ImageLoadingUtils utils;
    private boolean addedToMasterTable = false;
    int masterDataBaseId;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    ArrayList<String> mListLastRows = new ArrayList<>();
    String serverid, itemcode, itemname, itemID = "";
    ArrayList<String> itemlist;
    //  ArrayList<Item_Quote_Model> modelitem = new ArrayList<>();
    SBillableUnderAdapter sBillableUnderAdapter;

    public ArrayAdapter<String> myAdapter;
    ArrayList<String> businesstype1;

    Spinner spnr;
    ArrayAdapter<String> adapter;
    ListView listView;
    private int oneWayDay, oneWayMonth, oneWayYear;
    ArrayList<dbitemmaster> DBItemUpload;

    @AfterPermissionGranted(REQUEST_PERMISSION)
    private void permission() {
        String[] perms = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                "com.pos.permission.SECURITY",
                "com.pos.permission.ACCESSORY_DATETIME",
                "com.pos.permission.ACCESSORY_LED",
                "com.pos.permission.ACCESSORY_BEEP",
                "com.pos.permission.ACCESSORY_RFREGISTER",
                "com.pos.permission.CARD_READER_ICC",
                "com.pos.permission.CARD_READER_PICC",
                "com.pos.permission.CARD_READER_MAG",
                "com.pos.permission.COMMUNICATION",
                "com.pos.permission.PRINTER",
                "com.pos.permission.ACCESSORY_RFREGISTER",
                "com.pos.permission.EMVCORE"
        };
       /* if (EasyPermissions.hasPermissions(this, perms)) {
            Toast.makeText(this, "Already Permission", Toast.LENGTH_SHORT).show();
            try {
                ServiceManager.getInstence().init(getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
            LogUtil.openLog();
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                    new PermissionRequest
                            .Builder(this, REQUEST_PERMISSION, perms)
                            .setRationale("Dear users\n need to apply for storage Permissions for\n your better use of this application")
                            .setNegativeButtonText("NO")
                            .setPositiveButtonText("YES")
                            .build()
            );
        }*/
    }

    String[] itemunit =
            {
                    "Box",
                    "Pieces",
                    "KG"
            };

    int isMain = 0;

    public void onBackClick(View v) {
        super.onBackPressed();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grndetail);
        ButterKnife.bind(this);
        permission();
        txtTotalcess = findViewById(R.id.txtTotalcess);

        woid = "0";
        CameraDialog = new Dialog(GRNDetail.this);
        CameraDialog.setContentView(R.layout.popup_image_select);
        branchid = getIntent().getStringExtra("branchid");
        ImageView camerabtn = CameraDialog.findViewById(R.id.camerabtn);
        ImageView gallerybtn = CameraDialog.findViewById(R.id.gallerybtn);

        gallerybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(GRNDetail.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(GRNDetail.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                } else {
                    isMain = 0;
                    Intent intent1 = new Intent(GRNDetail.this, ImagePickActivity.class);
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
                isMain = 0;
                Intent intent1 = new Intent(GRNDetail.this, ImagePickActivity.class);
                intent1.putExtra(IS_NEED_CAMERA, true);
                intent1.putExtra(com.vincent.filepicker.Constant.MAX_NUMBER, 1);
                intent1.putExtra(IS_NEED_FOLDER_LIST, false);
                startActivityForResult(intent1, com.vincent.filepicker.Constant.REQUEST_CODE_TAKE_IMAGE);

                //  openCamera();
                //     launchCameraIntent();
                CameraDialog.dismiss();
            }
        });

        searchmanualbarid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loaditemmaster(et_ItemBarcode.getText().toString());
            }
        });


        findViews();
        if (!PermissionUtils.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || !PermissionUtils.hasPermission(this, Manifest.permission.CAMERA) || !PermissionUtils.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            PermissionModule permissionModule = new PermissionModule(GRNDetail.this);
            permissionModule.checkPermissions();

        }
        Preferencehelper prefs = new Preferencehelper(GRNDetail.this);
        JKHelper jkhelper = new JKHelper();
        //  jkhelper.getEmployeeDeviceCode(prefs.getProfileId(), GRNDetail.this);
        expaccounthead();
        settingTimeAndDate();
        // tvPhotoCount.setText("0 Photos");
        utils = new ImageLoadingUtils(this);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            woid = b.getString("id");
            strclientname = b.getString("supplier");
            strsupplierid = b.getString("supplierID");
            strvehicletype = b.getString("invoicenumber");
            strvehicleno = b.getString("invoicedate");
            strworktype = b.getString("type");
            toolbar_title.setText(/*strclientname + "/" + strvehicletype + "/" + */woid);
            strtax = b.getString("tax");
            if (strtax.equalsIgnoreCase("Without Tax")) {
                txType = 1;
            } else {

                txType = 0;
            }

            //loaditemmaster();
        } else {
            woid = "1001"/*b.getString("id")*/;
            strclientname = "65647"/*b.getString("supplier")*/;
            strsupplierid = "65647"/*b.getString("supplierID")*/;
            strvehicletype = "65647"/*b.getString("invoicenumber")*/;
            strvehicleno = "65647"/*b.getString("invoicedate")*/;
            strworktype = "65647"/*b.getString("type")*/;
            toolbar_title.setText(strclientname + "/" + strvehicletype + "/" + strvehicleno);
            //  loaditemmaster();

        }
        setAutoComplete();
        setBArcodeComplete();
        dialogItemDetail = new Dialog(GRNDetail.this);
        dialogItemDetail.setContentView(R.layout.dialog_invoice_images);


        closeHeader1 = dialogItemDetail.findViewById(R.id.closeHeader);
        btnSubmitPic = dialogItemDetail.findViewById(R.id.btnSubmit);
        pickimagebtn = dialogItemDetail.findViewById(R.id.imagepick);
        rvImages = dialogItemDetail.findViewById(R.id.rvImages);
        rvImages.setLayoutManager(new LinearLayoutManager(GRNDetail.this, RecyclerView.HORIZONTAL, false));
        orderImagesAdapter = new OrderImagesAdapter(this, mListImages, this);
        rvImages.setAdapter(orderImagesAdapter);

        dialogItemDetail.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mListImages = new ArrayList<>();
                orderImagesAdapter = new OrderImagesAdapter(GRNDetail.this, mListImages, GRNDetail.this);
                rvImages.setAdapter(orderImagesAdapter);

            }
        });
        dialogItemDetail.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mListImages = new ArrayList<>();
                orderImagesAdapter = new OrderImagesAdapter(GRNDetail.this, mListImages, GRNDetail.this);
                rvImages.setAdapter(orderImagesAdapter);

            }
        });
        pickimagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera(v, 2525);
            }
        });
        closeHeader1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogItemDetail.dismiss();
            }
        });
        btnSubmitPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListImages.size() > 0) {
                    for (int a = 0; a < mListImages.size(); a++) {
                        isMain = 1;
                        new ImageCompressionAsyncTask(true).execute(mListImages.get(a));
                    }
                    asREQUEST_CODE = 2525;
                    updateserverphotoid();
                    uploadimage();

                    dialogItemDetail.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), "Please Capture atleast one Image.", Toast.LENGTH_SHORT).show();

                }

            }
        });

/*
        et_BaseAmt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!et_BaseAmt.getText().toString().isEmpty() && !et_TAX.getText().toString().isEmpty()) {
                        if (strtax.equalsIgnoreCase("Without Tax")) {
                       //     et_BaseAmt.setText(new DecimalFormat("##.##").format(Double.parseDouble(Salerate)));
                            et_TAX1.setText(new DecimalFormat("##.##").format(((Double.parseDouble(et_BaseAmt.getText().toString()) * Double.parseDouble(et_TAX.getText().toString())) / 100)) + "");
                        } else {
                         //   et_BaseAmt.setText(new DecimalFormat("##.##").format((Double.parseDouble(Salerate) - ((Double.parseDouble(Salerate) * Double.parseDouble(TaxRate)) / (100 + Double.parseDouble(TaxRate))))) + "");
                            et_TAX1.setText(new DecimalFormat("##.##").format(((Double.parseDouble(et_BaseAmt.getText().toString()) * Double.parseDouble(et_TAX.getText().toString())) / (100 + Double.parseDouble(et_TAX.getText().toString())))) + "");
                        }
                    }
                }
            }
        });*/
        et_BaseAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!et_BaseAmt.getText().toString().isEmpty() && !et_TAX.getText().toString().isEmpty()) {
                    if (strtax.equalsIgnoreCase("Without Tax")) {
                        //     et_BaseAmt.setText(new DecimalFormat("##.##").format(Double.parseDouble(Salerate)));
                        et_TAX1.setText(new DecimalFormat("##.##").format(((Double.parseDouble(et_BaseAmt.getText().toString()) * Double.parseDouble(et_TAX.getText().toString())) / 100)) + "");

                        ftTotalBaseAmt1 = Float.parseFloat(et_BaseAmt.getText().toString()) + ftTotalBaseAmt;
                        ftTotalTaxAmt1 = Float.parseFloat(et_TAX1.getText().toString()) + ftTotalTaxAmt;
                        ftTotalWithTaxAmt1 = Float.parseFloat(et_BaseAmt.getText().toString()) + Float.parseFloat(et_TAX1.getText().toString()) + ftTotalWithTaxAmt;
                        txtTotalBase.setText("₹" + ftTotalBaseAmt1);
                        txtTotalTax.setText("₹" + ftTotalTaxAmt1);
                        txtTotal.setText("₹" + ftTotalWithTaxAmt1);
                    } else {

                        //   et_BaseAmt.setText(new DecimalFormat("##.##").format((Double.parseDouble(Salerate) - ((Double.parseDouble(Salerate) * Double.parseDouble(TaxRate)) / (100 + Double.parseDouble(TaxRate))))) + "");


                        if (!et_CESS.getText().toString().isEmpty() && Float.parseFloat(et_CESS.getText().toString()) > 1) {


                            Float newbase = 1f, totaltax = 0.0f, newcess, newtax;

                            totaltax = Float.parseFloat(et_TAX.getText().toString()) + Float.parseFloat(et_CESS.getText().toString()) + 100;
                            newbase = Float.parseFloat(et_BaseAmt.getText().toString()) * 100 / totaltax;

                            newcess = Float.parseFloat(et_CESS.getText().toString()) * newbase / 100;
                            newtax = Float.parseFloat(et_TAX.getText().toString()) * newbase / 100;
                            ftTotalBaseAmt1 = newbase;
                            et_CESS1.setText("" + new DecimalFormat("##.##").format(((Double.parseDouble(String.valueOf(newcess))))));

                            et_TAX1.setText("" + new DecimalFormat("##.##").format(((Double.parseDouble(String.valueOf(newtax))))));
                            ftTotalcess = Float.parseFloat(et_CESS1.getText().toString());
                            txtTotalcess.setText("₹" + ftTotalcess);
                            txtTotalBase.setText("₹" + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(ftTotalBaseAmt1))));
                            ftTotalTaxAmt1 = Float.parseFloat(et_TAX1.getText().toString()) + Float.parseFloat(et_CESS1.getText().toString()) + newbase;

                            txtTotal.setText("₹" + ftTotalTaxAmt1);
                            txtTotalTax.setText("₹" + new DecimalFormat("##.##").format(Double.parseDouble(et_TAX1.getText().toString())));

                        } else if (!et_TAX.getText().toString().isEmpty()) {
                            Float newbase = 1f, totaltax = 0.0f, newtax = 0.0f;
                            totaltax = Float.parseFloat(et_TAX.getText().toString()) + Float.parseFloat(et_CESS.getText().toString()) + 100;
                            newbase = Float.parseFloat(et_BaseAmt.getText().toString()) * 100 / totaltax;

                            newtax = Float.parseFloat(et_TAX.getText().toString()) * newbase / 100;
                            et_TAX1.setText("" + new DecimalFormat("##.##").format(((Double.parseDouble(String.valueOf(newtax))))));
                            ftTotalBaseAmt1 = newbase;
                            ftTotalBaseAmt1 = (Float.parseFloat(String.valueOf(newbase)) - Float.parseFloat(et_TAX1.getText().toString())) + ftTotalBaseAmt;
                            ftTotalTaxAmt1 = Float.parseFloat(et_TAX1.getText().toString()) + ftTotalTaxAmt;
                            ftTotalWithTaxAmt1 = Float.parseFloat(String.valueOf(newbase)) + ftTotalTaxAmt1;

                            txtTotalTax.setText("₹" + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(ftTotalTaxAmt1))));
                            txtTotalBase.setText("₹" + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(newbase))));

                            txtTotal.setText("₹" + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(ftTotalWithTaxAmt1))));

                        }


                    }
                } else {
//                    et_TAX1.setText("0.0");
//                    et_BaseAmt.setText("0.0");
//                    txtTotalTax.setText("0");
//
//                    txtTotalBase.setText("0");
//                    txtTotalcess.setText("0");
//                    txtTotal.setText("0");
                }
                if (!et_BaseAmt.getText().toString().isEmpty() && !et_CESS.getText().toString().isEmpty() && !et_CESS.getText().toString().equalsIgnoreCase("0.0")) {
                    if (strtax.equalsIgnoreCase("Without Tax")) {
                        //     et_BaseAmt.setText(new DecimalFormat("##.##").format(Double.parseDouble(Salerate)));
                        et_CESS1.setText(new DecimalFormat("##.##").format(((Double.parseDouble(et_BaseAmt.getText().toString()) * Double.parseDouble(et_CESS.getText().toString())) / 100)) + "");
                        ftTotalcess = Float.parseFloat(et_CESS1.getText().toString());
                        ftTotalWithTaxAmt1 = Float.parseFloat(et_BaseAmt.getText().toString()) + Float.parseFloat(et_TAX1.getText().toString()) + ftTotalWithTaxAmt + ftTotalcess;
                        txtTotalcess.setText("₹" + ftTotalcess);
                        txtTotal.setText("₹" + ftTotalWithTaxAmt1);


                    } else {
                        //   et_BaseAmt.setText(new DecimalFormat("##.##").format((Double.parseDouble(Salerate) - ((Double.parseDouble(Salerate) * Double.parseDouble(TaxRate)) / (100 + Double.parseDouble(TaxRate))))) + "");
//                        et_CESS1.setText(new DecimalFormat("##.##").format(((Double.parseDouble(et_BaseAmt.getText().toString()) * Double.parseDouble(et_CESS.getText().toString())) / (100 + Double.parseDouble(et_CESS.getText().toString())))) + "");
//
//                        ftTotalcess = Float.parseFloat(et_CESS1.getText().toString());
//
//                        ftTotalBaseAmt1 = Float.parseFloat(et_BaseAmt.getText().toString()) - ftTotalcess - Float.parseFloat(et_TAX1.getText().toString());
//                        txtTotalcess.setText("₹" + ftTotalcess);
//                        txtTotalBase.setText("₹" + String.valueOf(ftTotalBaseAmt1));
//
//
//                        ftTotalWithTaxAmt1 = Float.parseFloat(txtTotalBase.getText().toString().replace(getString(R.string.rupee), "")) + ftTotalcess + Float.parseFloat(txtTotalTax.getText().toString().replace(getString(R.string.rupee), ""));
//
//                        txtTotal.setText("₹" + ftTotalWithTaxAmt1);

                    }
                /*    ftTotalBaseAmt1 = Float.parseFloat(et_BaseAmt.getText().toString()) + ftTotalBaseAmt;
                    ftTotalTaxAmt1 = Float.parseFloat(et_CESS1.getText().toString()) + ftTotalTaxAmt;
                    ftTotalWithTaxAmt1 = Float.parseFloat(et_BaseAmt.getText().toString()) + Float.parseFloat(et_CESS1.getText().toString()) + ftTotalWithTaxAmt;
                    txtTotalBase.setText("₹" + ftTotalBaseAmt1);
                    txtTotalTax.setText("₹" + ftTotalTaxAmt1);
                    txtTotal.setText("₹" + ftTotalWithTaxAmt1);
         */
                }

            }
        });
        et_TAX.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!et_BaseAmt.getText().toString().isEmpty() && !et_TAX.getText().toString().isEmpty()) {
                    if (strtax.equalsIgnoreCase("Without Tax")) {
                        //     et_BaseAmt.setText(new DecimalFormat("##.##").format(Double.parseDouble(Salerate)));
                        et_TAX1.setText(new DecimalFormat("##.##").format(((Double.parseDouble(et_BaseAmt.getText().toString()) * Double.parseDouble(et_TAX.getText().toString())) / 100)) + "");

                        ftTotalBaseAmt1 = Float.parseFloat(et_BaseAmt.getText().toString()) + ftTotalBaseAmt;
                        ftTotalTaxAmt1 = Float.parseFloat(et_TAX1.getText().toString()) + ftTotalTaxAmt;
                        ftTotalWithTaxAmt1 = Float.parseFloat(et_BaseAmt.getText().toString()) + Float.parseFloat(et_TAX1.getText().toString()) + ftTotalWithTaxAmt;
                        txtTotalBase.setText("₹" + ftTotalBaseAmt1);
                        txtTotalTax.setText("₹" + ftTotalTaxAmt1);
                        txtTotal.setText("₹" + ftTotalWithTaxAmt1);
                    } else {
                        //  et_BaseAmt.setText(new DecimalFormat("##.##").format((Double.parseDouble(Salerate) - ((Double.parseDouble(Salerate) * Double.parseDouble(TaxRate)) / (100 + Double.parseDouble(TaxRate))))) + "");

                        if (!et_CESS.getText().toString().isEmpty() && Float.parseFloat(et_CESS.getText().toString()) > 1) {


                            Float newbase = 1f, totaltax = 0.0f, newcess, newtax;

                            totaltax = Float.parseFloat(et_TAX.getText().toString()) + Float.parseFloat(et_CESS.getText().toString()) + 100;
                            newbase = Float.parseFloat(et_BaseAmt.getText().toString()) * 100 / totaltax;

                            newcess = Float.parseFloat(et_CESS.getText().toString()) * newbase / 100;
                            newtax = Float.parseFloat(et_TAX.getText().toString()) * newbase / 100;
                            ftTotalBaseAmt1 = newbase;
                            et_CESS1.setText("" + new DecimalFormat("##.##").format(((Double.parseDouble(String.valueOf(newcess))))));

                            et_TAX1.setText("" + new DecimalFormat("##.##").format(((Double.parseDouble(String.valueOf(newtax))))));
                            ftTotalcess = Float.parseFloat(et_CESS1.getText().toString());
                            txtTotalcess.setText("₹" + ftTotalcess);
                            txtTotalBase.setText("₹" + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(ftTotalBaseAmt1))));
                            ftTotalTaxAmt1 = Float.parseFloat(et_TAX1.getText().toString()) + Float.parseFloat(et_CESS1.getText().toString()) + newbase;

                            txtTotal.setText("₹" + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(ftTotalTaxAmt1))));
                            txtTotalTax.setText("₹" + et_TAX1.getText().toString());


                        } else if (!et_TAX.getText().toString().isEmpty() && !et_CESS.getText().toString().isEmpty()) {
                            Float newbase = 1f, totaltax = 0.0f, newtax = 0.0f;
                            totaltax = Float.parseFloat(et_TAX.getText().toString()) + Float.parseFloat(et_CESS.getText().toString()) + 100;
                            newbase = Float.parseFloat(et_BaseAmt.getText().toString()) * 100 / totaltax;
                            newtax = Float.parseFloat(et_TAX.getText().toString()) * newbase / 100;
                            et_TAX1.setText("" + new DecimalFormat("##.##").format(((Double.parseDouble(String.valueOf(newtax))))));
                            ftTotalBaseAmt1 = newbase;

                            ftTotalTaxAmt1 = Float.parseFloat(et_TAX1.getText().toString()) + ftTotalTaxAmt;
                            ftTotalWithTaxAmt1 = Float.parseFloat(String.valueOf(newbase)) + ftTotalTaxAmt1;

                            txtTotalTax.setText("₹" + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(ftTotalTaxAmt1))));
                            txtTotalBase.setText("₹" + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(ftTotalBaseAmt1))));

                            txtTotal.setText("₹" + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(ftTotalWithTaxAmt1))));
                        }

//                        ftTotalBaseAmt1 = (Float.parseFloat(et_BaseAmt.getText().toString()) - Float.parseFloat(et_TAX1.getText().toString())) + ftTotalBaseAmt;
//                        ftTotalTaxAmt1 = Float.parseFloat(et_TAX1.getText().toString()) + ftTotalTaxAmt;
//                        ftTotalWithTaxAmt1 = Float.parseFloat(et_BaseAmt.getText().toString()) + ftTotalWithTaxAmt;
//                        txtTotalBase.setText("₹" + ftTotalBaseAmt1);
//                        txtTotalTax.setText("₹" + ftTotalTaxAmt1);
//                        txtTotal.setText("₹" + ftTotalWithTaxAmt1);
                    }
                  /*  ftTotalBaseAmt1 = Float.parseFloat(et_BaseAmt.getText().toString()) + ftTotalBaseAmt;
                    ftTotalTaxAmt1 = Float.parseFloat(et_TAX1.getText().toString()) + ftTotalTaxAmt;
                    ftTotalWithTaxAmt1 = Float.parseFloat(et_BaseAmt.getText().toString()) + Float.parseFloat(et_TAX1.getText().toString()) + ftTotalWithTaxAmt;
                    txtTotalBase.setText("₹" + ftTotalBaseAmt1);
                    txtTotalTax.setText("₹" + ftTotalTaxAmt1);
                    txtTotal.setText("₹" + ftTotalWithTaxAmt1);*/

                } else {
                    et_TAX1.setText("0.0");
                    txtTotalTax.setText("0.0");
                    //   et_BaseAmt.setText("0.0");
                }


            }
        });
//
//        et_CESS1.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                if(et_CESS.getText().toString().equalsIgnoreCase("0")||et_CESS.getText().toString().equalsIgnoreCase("0.0"))
//                {
//                    if (!et_BaseAmt.getText().toString().isEmpty() && !et_CESS.getText().toString().isEmpty()) {
//                        if (strtax.equalsIgnoreCase("Without Tax")) {
//                            //  et_BaseAmt.setText(new DecimalFormat("##.##").format(Double.parseDouble(Salerate)));
//                            et_CESS.setText(new DecimalFormat("##.##").format(((Double.parseDouble(et_BaseAmt.getText().toString()) * Double.parseDouble(et_CESS1.getText().toString())) * 100 / Double.parseDouble(et_BaseAmt.getText().toString()))));
//
//                            ftTotalcess = Float.parseFloat(et_CESS.getText().toString());
//                            txtTotalcess.setText("₹" + ftTotalcess);
//                            ftTotalWithTaxAmt1 = Float.parseFloat(et_BaseAmt.getText().toString()) + Float.parseFloat(et_TAX1.getText().toString()) + ftTotalWithTaxAmt + ftTotalcess;
//
//                        } else {
//                            //  et_BaseAmt.setText(new DecimalFormat("##.##").format((Double.parseDouble(Salerate) - ((Double.parseDouble(Salerate) * Double.parseDouble(TaxRate)) / (100 + Double.parseDouble(TaxRate))))) + "");
//                            et_CESS.setText(new DecimalFormat("##.##").format(((Double.parseDouble(et_BaseAmt.getText().toString()) * Double.parseDouble(et_CESS1.getText().toString())) * 100 / Double.parseDouble(et_BaseAmt.getText().toString()))));
//                            ftTotalcess = Float.parseFloat(et_CESS.getText().toString());
//                            txtTotalcess.setText("₹" + ftTotalcess);
//                            ftTotalWithTaxAmt1 = Float.parseFloat(et_BaseAmt.getText().toString()) + ftTotalWithTaxAmt + ftTotalcess;
//
//
//                        }
//                  /*  ftTotalBaseAmt1 = Float.parseFloat(et_BaseAmt.getText().toString()) + ftTotalBaseAmt;
//                    ftTotalTaxAmt1 = Float.parseFloat(et_CESS.getText().toString()) + ftTotalTaxAmt;
//                    ftTotalWithTaxAmt1 = Float.parseFloat(et_BaseAmt.getText().toString()) + Float.parseFloat(et_CESS1.getText().toString()) + ftTotalWithTaxAmt;
//                    txtTotalBase.setText("₹" + ftTotalBaseAmt1);
//                    txtTotalTax.setText("₹" + ftTotalTaxAmt1);
//                    txtTotal.setText("₹" + ftTotalWithTaxAmt1);
//*/
//                    } else {
//                        // et_TAX1.setText("0.0");
//                        //   et_BaseAmt.setText("0.0");
//                    }
//                }
//
//
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//
//            }
//        });
        et_CESS.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                if (!et_BaseAmt.getText().toString().isEmpty() && !et_CESS.getText().toString().isEmpty()) {

                    if (strtax.equalsIgnoreCase("Without Tax")) {
                        //     et_BaseAmt.setText(new DecimalFormat("##.##").format(Double.parseDouble(Salerate)));
                        et_CESS1.setText(new DecimalFormat("##.##").format(((Double.parseDouble(et_BaseAmt.getText().toString()) * Double.parseDouble(et_CESS.getText().toString())) / 100)) + "");
                        ftTotalcess = Float.parseFloat(et_CESS1.getText().toString());
                        ftTotalWithTaxAmt1 = Float.parseFloat(et_BaseAmt.getText().toString()) + Float.parseFloat(et_TAX1.getText().toString()) + ftTotalWithTaxAmt + ftTotalcess;
                        txtTotalcess.setText("₹" + ftTotalcess);
                        txtTotal.setText("₹" + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(ftTotalWithTaxAmt1))));


                    } else if (!et_CESS.getText().toString().isEmpty() && Float.parseFloat(et_CESS.getText().toString()) >= 1) {


                        Float newbase = 1f, totaltax = 0.0f, newcess, newtax;

                        totaltax = Float.parseFloat(et_TAX.getText().toString()) + Float.parseFloat(et_CESS.getText().toString()) + 100;
                        newbase = Float.parseFloat(et_BaseAmt.getText().toString()) * 100 / totaltax;

                        newcess = Float.parseFloat(et_CESS.getText().toString()) * newbase / 100;
                        newtax = Float.parseFloat(et_TAX.getText().toString()) * newbase / 100;
                        ftTotalBaseAmt1 = newbase;
                        et_CESS1.setText("" + new DecimalFormat("##.##").format(((Double.parseDouble(String.valueOf(newcess))))));

                        et_TAX1.setText("" + new DecimalFormat("##.##").format(((Double.parseDouble(String.valueOf(newtax))))));
                        ftTotalcess = Float.parseFloat(et_CESS1.getText().toString());
                        txtTotalcess.setText("₹" + ftTotalcess);
                        txtTotalBase.setText("₹" + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(ftTotalBaseAmt1))));
                        ftTotalTaxAmt1 = Float.parseFloat(et_TAX1.getText().toString()) + Float.parseFloat(et_CESS1.getText().toString()) + newbase;


                        txtTotalTax.setText("₹" + et_TAX1.getText().toString());
                        txtTotal.setText("₹" + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(ftTotalTaxAmt1))));


                    } else if (!et_TAX.getText().toString().isEmpty()) {
                        Float newbase = 1f, totaltax = 0.0f, newcess, newtax;
                        totaltax = Float.parseFloat(et_TAX.getText().toString()) + Float.parseFloat(et_CESS.getText().toString()) + 100;
                        newbase = Float.parseFloat(et_BaseAmt.getText().toString()) * 100 / totaltax;
                        newtax = Float.parseFloat(et_TAX.getText().toString()) * newbase / 100;
                        et_TAX1.setText("" + new DecimalFormat("##.##").format(((Double.parseDouble(String.valueOf(newtax))))));

                        ftTotalBaseAmt1 = newbase;
                        ftTotalTaxAmt1 = Float.parseFloat(et_TAX1.getText().toString());
                        ftTotalWithTaxAmt1 = Float.parseFloat(String.valueOf(newbase)) + ftTotalTaxAmt1;
                        txtTotalBase.setText("₹" + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(ftTotalBaseAmt1))));

                        txtTotalTax.setText("₹" + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(ftTotalTaxAmt1))));

                        txtTotal.setText("₹" + new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(ftTotalWithTaxAmt1))));

                    } else if (Float.parseFloat(et_CESS.getText().toString()) < 1) {
                        et_CESS.setText("0.0");

                    }
                /*    ftTotalBaseAmt1 = Float.parseFloat(et_BaseAmt.getText().toString()) + ftTotalBaseAmt;
                    ftTotalTaxAmt1 = Float.parseFloat(et_CESS1.getText().toString()) + ftTotalTaxAmt;
                    ftTotalWithTaxAmt1 = Float.parseFloat(et_BaseAmt.getText().toString()) + Float.parseFloat(et_CESS1.getText().toString()) + ftTotalWithTaxAmt;
                    txtTotalBase.setText("₹" + ftTotalBaseAmt1);
                    txtTotalTax.setText("₹" + ftTotalTaxAmt1);
                    txtTotal.setText("₹" + ftTotalWithTaxAmt1);
         */
                } else {
                    et_CESS1.setText("0.0");
                    txtTotalcess.setText("0.0");

                }


            }
        });

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
        super.onBackPressed();
    }

    private void loaditemmaster(String strBar) {
        JKHelper jkhelper = new JKHelper();
        if (NetworkUtil.isConnectedToNetwork(GRNDetail.this)) {
            getItemMaster("9999" + strsupplierid, GRNDetail.this, strBar);
        } else {
            Crouton.showText(GRNDetail.this, "Please Connect To Internet", Style.ALERT);
        }
    }

    public void setAutoComplete() {

        et_item_name.setAdapter(new AutoCompleteItemAdapter(this, strsupplierid, SearchType)); // 'this' is Activity instance
        et_item_name.setLoadingIndicator(
                (android.widget.ProgressBar) findViewById(R.id.pb_loading_indicator));
        et_item_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                dbitemmaster book = (dbitemmaster) adapterView.getItemAtPosition(position);
                et_item_name.setText(book.getmItemName());

                isEditable = false;

                new CountDownTimer(3000, 1000) {

                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        isEditable = true;
                        cancel();
                    }
                }.start();

                String mItemID = book.getmItemID();
                String mItemName = book.getmItemName();
                String mcompanycosting = book.getmcompanycosting();// box barcode
                String mByItemNumber = book.getmByItemNumber(); // item barcode
                String mGroupID = book.getmGroupID();
                String HSNCode = book.getHSNCode();
                String TaxRate = book.getTaxRate();
                String Salerate = book.getSalerate();
                String MRP = book.getMRP();
                String mAllString = mItemName;
                et_item_name.setText(mItemName);
                //et_ItemBarcode.setText(mByItemNumber);
                et_item_IDs.setText(mItemID);
                strMatchedbarcode = mByItemNumber;
                et_HSN.setText(HSNCode);
                et_TAX.setText(new DecimalFormat("##.##").format(Double.parseDouble(TaxRate)));

                et_BaseAmt.setText("");
                if (strtax.equalsIgnoreCase("Without Tax")) {
                    //    et_BaseAmt.setText(new DecimalFormat("##.##").format(Double.parseDouble("0.0"/*Salerate*/)));
                    if (!et_BaseAmt.getText().toString().isEmpty()) {
                        et_TAX1.setText(new DecimalFormat("##.##").format(((Double.parseDouble("0.0"/*Salerate*/) * Double.parseDouble(TaxRate)) / 100)) + "");
                    }
                } else {
                    //  et_BaseAmt.setText(new DecimalFormat("##.##").format((Double.parseDouble("0.0"/*Salerate*/) - ((Double.parseDouble("0.0"/*Salerate*/) * Double.parseDouble(TaxRate)) / (100 + Double.parseDouble(TaxRate))))) + "");
                    if (!et_BaseAmt.getText().toString().isEmpty()) {
                        et_TAX1.setText(new DecimalFormat("##.##").format(((Double.parseDouble("0.0"/*Salerate*/) * Double.parseDouble(TaxRate)) / (100 + Double.parseDouble(TaxRate)))) + "");
                    }
                }

                et_Qty.requestFocus();
                et_MRP.setText(new DecimalFormat("##.##").format(Double.parseDouble(MRP)));
                if (strMatchedbarcode.length() > 0) {
                    isEditable = true;
                    ll_updateData.setVisibility(View.VISIBLE);
                } else {
                    et_item_IDs.setText("0");
                    ll_updateData.setVisibility(View.VISIBLE);
                    isEditable = false;
                    //  et_BaseAmt.setText(mByItemNumber);


                }


              /*  product.setFocusableInTouchMode(true);
                product.requestFocus();*/
            }
        });

    }

    public void setBArcodeComplete() {

        et_ItemBarcode.setAdapter(new AutoCompleteItemAdapter(this, strsupplierid, SearchType)); // 'this' is Activity instance
//        et_ItemBarcode.setLoadingIndicator(
//                (android.widget.ProgressBar) findViewById(R.id.pb_loading_indicator));
        et_ItemBarcode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                dbitemmaster book = (dbitemmaster) adapterView.getItemAtPosition(position);
                et_ItemBarcode.setText(book.getmItemName());

                isEditable = false;

                new CountDownTimer(3000, 1000) {

                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        isEditable = true;
                        cancel();
                    }
                }.start();

                String mItemID = book.getmItemID();
                String mItemName = book.getmItemName();
                String mcompanycosting = book.getmcompanycosting();// box barcode
                String mByItemNumber = book.getmByItemNumber(); // item barcode
                String mGroupID = book.getmGroupID();
                String HSNCode = book.getHSNCode();
                String TaxRate = book.getTaxRate();
                String Salerate = book.getSalerate();
                String MRP = book.getMRP();
                String mAllString = mItemName;
                et_ItemBarcode.setText(mItemName);
                //et_ItemBarcode.setText(mByItemNumber);
                et_item_IDs.setText(mItemID);
                strMatchedbarcode = mByItemNumber;
                et_HSN.setText(HSNCode);
                et_TAX.setText(new DecimalFormat("##.##").format(Double.parseDouble(TaxRate)));

                et_BaseAmt.setText("");
                if (strtax.equalsIgnoreCase("Without Tax")) {
                    //    et_BaseAmt.setText(new DecimalFormat("##.##").format(Double.parseDouble("0.0"/*Salerate*/)));
                    if (!et_BaseAmt.getText().toString().isEmpty()) {
                        et_TAX1.setText(new DecimalFormat("##.##").format(((Double.parseDouble("0.0"/*Salerate*/) * Double.parseDouble(TaxRate)) / 100)) + "");
                    }
                } else {
                    //  et_BaseAmt.setText(new DecimalFormat("##.##").format((Double.parseDouble("0.0"/*Salerate*/) - ((Double.parseDouble("0.0"/*Salerate*/) * Double.parseDouble(TaxRate)) / (100 + Double.parseDouble(TaxRate))))) + "");
                    if (!et_BaseAmt.getText().toString().isEmpty()) {
                        et_TAX1.setText(new DecimalFormat("##.##").format(((Double.parseDouble("0.0"/*Salerate*/) * Double.parseDouble(TaxRate)) / (100 + Double.parseDouble(TaxRate)))) + "");
                    }
                }

                et_Qty.requestFocus();
                et_MRP.setText(new DecimalFormat("##.##").format(Double.parseDouble(MRP)));
                if (strMatchedbarcode.length() > 0) {
                    isEditable = true;
                    ll_updateData.setVisibility(View.VISIBLE);
                } else {
                    et_item_IDs.setText("0");
                    ll_updateData.setVisibility(View.VISIBLE);
                    isEditable = false;
                    //  et_BaseAmt.setText(mByItemNumber);


                }


              /*  product.setFocusableInTouchMode(true);
                product.requestFocus();*/
            }
        });

    }

    String strMatchedbarcode = "";

    public void getItemMaster(final String contactid, final Context ctx, final String strBar) {
        final ProgressDialog prgDialog = new ProgressDialog(GRNDetail.this);
        prgDialog.setMessage("Please Wait.....");
        prgDialog.setCancelable(false);
        if (SearchType == 0) {
//            prgDialog.show();
        }
        strMatchedbarcode = "";
        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://mmthinkbiz.com/MobileService.aspx/getds?method=All_Item_Master",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        if (SearchType == 0) {
                            prgDialog.dismiss();
                        }
                        Log.e("Response1233DATA", response);
                        String res = response;
                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            int success = jsonObject.getInt("success");
                            log.e("success==" + success);
                            if (success == 1) {

                                JSONArray jsonArray = jsonObject.getJSONArray("returnds");
                                //   DBItemUpload = new ArrayList<dbitemmaster>();
                                if (SearchType == 1) {
                                    ArrayList<String> masterlist = new ArrayList<>();
                                    DBItemUpload = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject loopObjects = jsonArray.getJSONObject(i);
                                        String mItemID = loopObjects.getString("ItemID");
                                        String mItemName = loopObjects.getString("ItemName");
                                        String mcompanycosting = loopObjects.getString("ItemBarcode");// box barcode
                                        String mByItemNumber = loopObjects.getString("ItemBarcode"); // item barcode
                                        String mGroupID = loopObjects.getString("GroupID");
                                        // String mRoundKG = loopObjects.getString("RoundKG");
                                        // String mAddWeightKG = loopObjects.getString("AddWeightKG");
                                        String HSNCode = loopObjects.getString("HSNCode");
                                        String TaxRate = loopObjects.getString("StockUOM");
                                        String Salerate = loopObjects.getString("SaleRate");
                                        String MRP = loopObjects.getString("MRP");
                                        String mAllString = mItemName;

                                        dbitemmaster model = new dbitemmaster();
                                        model.setmItemID(mItemID);
                                        model.setmItemName(mItemName);
                                        model.setmcompanycosting(mcompanycosting);
                                        model.setmByItemNumber(mByItemNumber);
                                        model.setmGroupID(mGroupID);
                                        //     model.setmRoundKG(mRoundKG);
                                        //   model.setmAddWeightKG(mAddWeightKG);
                                        model.setmAllString(mAllString);
                                        model.setMRP(MRP);
                                        model.setSalerate(Salerate);
                                        model.setHSNCode(HSNCode);
                                        model.setTaxRate(TaxRate);
                                        DBItemUpload.add(model);
                                        masterlist.add(mItemName);
                                    }


                                    myAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item_single_choice1, masterlist);
                                    et_item_name.setAdapter(myAdapter);
                                    et_ItemBarcode.setAdapter(myAdapter);

                                    isEditable = false;
                                    et_item_name.setText(strBar);

                                    new CountDownTimer(3000, 1000) {

                                        public void onTick(long millisUntilFinished) {
                                        }

                                        public void onFinish() {
                                            isEditable = true;
                                            cancel();
                                        }
                                    }.start();

                                } else {

                                    // for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject loopObjects = jsonArray.getJSONObject(0);
                                    String mItemID = loopObjects.getString("ItemID");
                                    String mItemName = loopObjects.getString("ItemName");
                                    String mcompanycosting = loopObjects.getString("ItemBarcode");// box barcode
                                    String mByItemNumber = loopObjects.getString("ItemBarcode"); // item barcode
                                    String mGroupID = loopObjects.getString("GroupID");
                                    // String mRoundKG = loopObjects.getString("RoundKG");
                                    // String mAddWeightKG = loopObjects.getString("AddWeightKG");
                                    String HSNCode = loopObjects.getString("HSNCode");
                                    String TaxRate = loopObjects.getString("StockUOM");
                                    String Salerate = loopObjects.getString("SaleRate");
                                    String MRP = loopObjects.getString("MRP");
                                    String mAllString = mItemName;

                                    if (strtax.equalsIgnoreCase("Without Tax")) {
                                        if (!et_BaseAmt.getText().toString().isEmpty()) {
                                            //   et_BaseAmt.setText(new DecimalFormat("##.##").format(Double.parseDouble("0.0"/*Salerate*/)));
                                            et_TAX1.setText(new DecimalFormat("##.##").format(((Double.parseDouble("0.0"/*Salerate*/) * Double.parseDouble(TaxRate)) / 100)) + "");
                                        }
                                    } else {
                                        //   et_BaseAmt.setText(new DecimalFormat("##.##").format((Double.parseDouble("0.0"/*Salerate*/) - ((Double.parseDouble("0.0"/*Salerate*/) * Double.parseDouble(TaxRate)) / (100 + Double.parseDouble(TaxRate))))) + "");
                                        if (!et_BaseAmt.getText().toString().isEmpty()) {
                                            et_TAX1.setText(new DecimalFormat("##.##").format(((Double.parseDouble("0.0"/*Salerate*/) * Double.parseDouble(TaxRate)) / (100 + Double.parseDouble(TaxRate)))) + "");
                                        }
                                    }

                                    et_MRP.setText(new DecimalFormat("##.##").format(Double.parseDouble(MRP)));
                                    if (strMatchedbarcode.length() > 0) {
                                        isEditable = true;
                                        ll_updateData.setVisibility(View.VISIBLE);
                                    } else {
                                        et_item_IDs.setText("0");
                                        ll_updateData.setVisibility(View.VISIBLE);
                                        isEditable = false;
                                        //  et_BaseAmt.setText(mByItemNumber);


                                    }
                                    try {

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        et_item_name.setText(mItemName);
                                        //et_ItemBarcode.setText(mByItemNumber);
                                        et_item_IDs.setText(mItemID);
                                        strMatchedbarcode = mByItemNumber;
                                        et_HSN.setText(HSNCode);
                                        et_TAX.setText(new DecimalFormat("##.##").format(Double.parseDouble(TaxRate)));
                                        et_BaseAmt.setText("");
                                    }
                                }

                          /*          dbitemmaster model = new dbitemmaster();
                                    model.setmItemID(mItemID);
                                    model.setmItemName(mItemName);
                                    model.setmcompanycosting(mcompanycosting);
                                    model.setmByItemNumber(mByItemNumber);
                                    model.setmGroupID(mGroupID);
                               //     model.setmRoundKG(mRoundKG);
                                 //   model.setmAddWeightKG(mAddWeightKG);
                                    model.setmAllString(mAllString);
                                    model.setMRP(MRP);
                                    model.setSalerate(Salerate);
                                    model.setHSNCode(HSNCode);
                                    model.setTaxRate(TaxRate);
                                    DBItemUpload.add(model);
                                //}

                                DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                                    @Override
                                    public void run(SQLiteDatabase database) {
                                        itemmasterdao dao = new itemmasterdao(database, ctx);
                                        ArrayList<dbitemmaster> list = DBItemUpload;
                                        dao.deleteAll();
                                        dao.insert(list);
                                        getitemlist();
                                        //modelitem.add(list);
                                    }
                                });*/
                                //Message.message(ctx, "Data fetched Successfuly");
                                //   checkBarcode("8906064651733", 2);
                            } else {
                                Log.e("condition=", "00");
                                et_item_name.setText("");
                                // et_ItemBarcode.setText("");
                                et_item_IDs.setText("0");
                                strMatchedbarcode = "";
                                et_BaseAmt.setText("");
                                et_HSN.setText("");
                                et_TAX.setText("");
                                et_Qty.requestFocus();
                                et_TAX1.setText("");
                                et_MRP.setText("");
                                if (strMatchedbarcode.length() > 0) {
                                    isEditable = true;
                                    ll_updateData.setVisibility(View.VISIBLE);
                                } else {
                                    et_item_IDs.setText("0");
                                    ll_updateData.setVisibility(View.VISIBLE);
                                    isEditable = false;
                                    et_BaseAmt.setText("");


                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (SearchType == 0) {
                            prgDialog.dismiss();
                        }// error
                        String ss = "";
                        //   Log.e("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                com.dhanuka.morningparcel.Helper.Preferencehelper prefs2 = new com.dhanuka.morningparcel.Helper.Preferencehelper(getApplicationContext());
                Map<String, String> params = new HashMap<String, String>();
                params.put("contactid", contactid);
                params.put("SupplierID", prefs2.getCID());
                params.put("cat", "706");
                params.put("filterItemName", strBar);
                //_web&=999966879&=&=&=8831234271902
                Log.e("ppp", params.toString());
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(ctx).add(postRequest);
    }

    private void getitemlist() {
        DatabaseManager.initializeInstance(new DatabaseHelper(getApplicationContext()));
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        itemmasterdao pd = new itemmasterdao(database, this);
        ArrayList<dbitemmaster> listDatabase = pd.selectAll();
        itemlist = new ArrayList<>();
        itemlist.clear();

        itemlist.add("Select Item");
        if (DBItemUpload != null) {
            for (int i = 0; i < DBItemUpload.size(); i++) {
                itemlist.add(DBItemUpload.get(i).getmItemName());
            }
        }
        myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, itemlist);
/*
        et_item_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String strErr = "";
                 *//*   if (isEditable) {
                        if (DBItemUpload != null) {
                            for (int z = 0; z < DBItemUpload.size(); z++) {
                                if (!et_item_name.getText().toString().isEmpty()) {
                                    if (DBItemUpload.get(z).getmItemName().toLowerCase().contains(et_item_name.getText().toString().toLowerCase())) {
                                        //if (DBItemUpload.get(z).getmContentMode().equalsIgnoreCase("BULKY"))  // Light

                                        et_item_IDs.setText(DBItemUpload.get(z).getmItemID().toString());
                                        if (et_BoxBarcode.getText().toString().length() < 2) {
                                            et_BoxBarcode.setText(DBItemUpload.get(z).getmcompanycosting());
                                            strErr = DBItemUpload.get(z).getmcompanycosting();
                                            checkBarcode(DBItemUpload.get(z).getmcompanycosting(), 1);

                                        }
                                        Log.e("asdassf", DBItemUpload.get(z).getmByItemNumber());
                                        //if (et_ItemBarcode.getText().toString().length() < 2) {
                                        et_ItemBarcode.setText(DBItemUpload.get(z).getmByItemNumber());
                                        strErr = DBItemUpload.get(z).getmByItemNumber();
                                        checkBarcode(DBItemUpload.get(z).getmByItemNumber(), 2);
                                        // }
                                        break;

                                    }
                                }
                            }

                       *//**//* if (strErr.isEmpty() && !et_item_name.getText().toString().isEmpty()){
                            checkBarcode();

                        }*//**//*
                        }
                    }
            *//*
                }
            }
        });*/


        et_ItemBarcode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                isEditable = false;

                new CountDownTimer(3000, 1000) {

                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        isEditable = true;
                        cancel();
                    }
                }.start();

                String mItemID = DBItemUpload.get(position).getmItemID();
                String mItemName = DBItemUpload.get(position).getmItemName();
                String mcompanycosting = DBItemUpload.get(position).getmcompanycosting();// box barcode
                String mByItemNumber = DBItemUpload.get(position).getmByItemNumber(); // item barcode
                String mGroupID = DBItemUpload.get(position).getmGroupID();
                String HSNCode = DBItemUpload.get(position).getHSNCode();
                String TaxRate = DBItemUpload.get(position).getTaxRate();
                String Salerate = DBItemUpload.get(position).getSalerate();
                String MRP = DBItemUpload.get(position).getMRP();
                String mAllString = mItemName;
                et_ItemBarcode.setText(mItemName);
                //et_ItemBarcode.setText(mByItemNumber);
                et_item_IDs.setText(mItemID);
                strMatchedbarcode = mByItemNumber;
                et_HSN.setText(HSNCode);
                et_TAX.setText(new DecimalFormat("##.##").format(Double.parseDouble(TaxRate)));
                if (strtax.equalsIgnoreCase("Without Tax")) {
                    et_BaseAmt.setText(new DecimalFormat("##.##").format(Double.parseDouble("0.0"/*Salerate*/)));
                    et_TAX1.setText(new DecimalFormat("##.##").format(((Double.parseDouble("0.0"/*Salerate*/) * Double.parseDouble(TaxRate)) / 100)) + "");
                } else {
                    et_BaseAmt.setText(new DecimalFormat("##.##").format((Double.parseDouble("0.0"/*Salerate*/) - ((Double.parseDouble("0.0"/*Salerate*/) * Double.parseDouble(TaxRate)) / (100 + Double.parseDouble(TaxRate))))) + "");
                    et_TAX1.setText(new DecimalFormat("##.##").format(((Double.parseDouble("0.0"/*Salerate*/) * Double.parseDouble(TaxRate)) / (100 + Double.parseDouble(TaxRate)))) + "");
                }
                et_Qty.requestFocus();
                et_MRP.setText(new DecimalFormat("##.##").format(Double.parseDouble(MRP)));
                if (strMatchedbarcode.length() > 0) {
                    isEditable = true;
                    ll_updateData.setVisibility(View.VISIBLE);
                } else {
                    et_item_IDs.setText("0");
                    ll_updateData.setVisibility(View.VISIBLE);
                    isEditable = false;
                    //  et_BaseAmt.setText(mByItemNumber);


                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        et_item_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                isEditable = false;

                new CountDownTimer(3000, 1000) {

                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        isEditable = true;
                        cancel();
                    }
                }.start();

                String mItemID = DBItemUpload.get(position).getmItemID();
                String mItemName = DBItemUpload.get(position).getmItemName();
                String mcompanycosting = DBItemUpload.get(position).getmcompanycosting();// box barcode
                String mByItemNumber = DBItemUpload.get(position).getmByItemNumber(); // item barcode
                String mGroupID = DBItemUpload.get(position).getmGroupID();
                String HSNCode = DBItemUpload.get(position).getHSNCode();
                String TaxRate = DBItemUpload.get(position).getTaxRate();
                String Salerate = DBItemUpload.get(position).getSalerate();
                String MRP = DBItemUpload.get(position).getMRP();
                String mAllString = mItemName;
                et_item_name.setText(mItemName);
                //et_ItemBarcode.setText(mByItemNumber);
                et_item_IDs.setText(mItemID);
                strMatchedbarcode = mByItemNumber;
                et_HSN.setText(HSNCode);
                et_TAX.setText(new DecimalFormat("##.##").format(Double.parseDouble(TaxRate)));
                if (strtax.equalsIgnoreCase("Without Tax")) {
                    et_BaseAmt.setText(new DecimalFormat("##.##").format(Double.parseDouble("0.0"/*Salerate*/)));
                    et_TAX1.setText(new DecimalFormat("##.##").format(((Double.parseDouble("0.0"/*Salerate*/) * Double.parseDouble(TaxRate)) / 100)) + "");
                } else {
                    et_BaseAmt.setText(new DecimalFormat("##.##").format((Double.parseDouble("0.0"/*Salerate*/) - ((Double.parseDouble("0.0"/*Salerate*/) * Double.parseDouble(TaxRate)) / (100 + Double.parseDouble(TaxRate))))) + "");
                    et_TAX1.setText(new DecimalFormat("##.##").format(((Double.parseDouble("0.0"/*Salerate*/) * Double.parseDouble(TaxRate)) / (100 + Double.parseDouble(TaxRate)))) + "");
                }
                et_Qty.requestFocus();
                et_MRP.setText(new DecimalFormat("##.##").format(Double.parseDouble(MRP)));
                if (strMatchedbarcode.length() > 0) {
                    isEditable = true;
                    ll_updateData.setVisibility(View.VISIBLE);
                } else {
                    et_item_IDs.setText("0");
                    ll_updateData.setVisibility(View.VISIBLE);
                    isEditable = false;
                    //  et_BaseAmt.setText(mByItemNumber);


                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        myAdapter = new ArrayAdapter<String>(this, R.layout.simple_list_item_single_choice1, itemlist);
        et_item_name.setAdapter(myAdapter);
        et_ItemBarcode.setAdapter(myAdapter);

        et_ItemBarcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence editable, int i, int i1, int i2) {
           /*     String[] item = getitemnamesearch(charSequence.toString());
                myAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item_single_choice1, item);
                et_item_name.setAdapter(myAdapter);
       */

                    if (editable.length() > 2) {
//                        Log.e("onqueryinside", editable.toString());
//
//                        ArrayList<String> masterlist = new ArrayList<>();
//                        clearContols();
//                        loaditemmaster(editable.toString());
                        Log.e("onqueryoutside", editable.toString());
                        ArrayList<String> masterlist = new ArrayList<>();
                        myAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item_single_choice1, masterlist);
                        et_ItemBarcode.setAdapter(myAdapter);

                    } else {
                        Log.e("onqueryoutside", editable.toString());
                        ArrayList<String> masterlist = new ArrayList<>();
                        myAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item_single_choice1, masterlist);
                        et_ItemBarcode.setAdapter(myAdapter);
                    }

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        et_item_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence editable, int i, int i1, int i2) {
           /*     String[] item = getitemnamesearch(charSequence.toString());
                myAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item_single_choice1, item);
                et_item_name.setAdapter(myAdapter);
       */
                if (isEditable && SearchType == 1) {
                    if (editable.length() > 2) {
                        Log.e("onqueryinside", editable.toString());

                        ArrayList<String> masterlist = new ArrayList<>();
                        clearContols();
                        loaditemmaster(editable.toString());
                    } else {
                        Log.e("onqueryoutside", editable.toString());
                        ArrayList<String> masterlist = new ArrayList<>();
                        myAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item_single_choice1, masterlist);
                        et_item_name.setAdapter(myAdapter);

                    }
                } else {
                    Log.e("onqueryoutside1", editable.toString());

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    public String[] getitemnamesearch(String searchTerm) {
        ArrayList<String> result = new ArrayList<>();
        for (int z = 0; z < DBItemUpload.size(); z++) {
            if (DBItemUpload.get(z).getmItemName().toLowerCase().contains(searchTerm.toLowerCase())) {
                result.add(DBItemUpload.get(z).getmItemName());
            }
        }
        String item[] = result.toArray(new String[result.size()]);
        return item;
    }


    private void worktypechangeevent(String val) {
        /*if (val == "Installed") {
            tvcomment.setVisibility(View.VISIBLE);
        } else if (val == "Uninstalled") {
            tvcomment.setVisibility(View.VISIBLE);
        }*/
    }

    //
    private void settingTimeAndDate() {
        Calendar mcurrentTime = Calendar.getInstance();
        //mcurrentTime.add(Calendar.HOUR_OF_DAY, 1);
        oneWayDay = mcurrentTime.get(Calendar.DAY_OF_MONTH);
        oneWayMonth = mcurrentTime.get(Calendar.MONTH) + 1;
        oneWayYear = mcurrentTime.get(Calendar.YEAR);
        //Calendar forEndTime = Calendar.getInstance();
        //forEndTime.add(Calendar.HOUR_OF_DAY, 2);

        String displayDay = String.format("%02d", oneWayDay);
        String displayMonth = String.format("%02d", oneWayMonth);
        etDate.setText(displayMonth + "/" + displayDay + "/" + oneWayYear);
    }

    private int getIndex(Spinner spinner, String myString) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            String vals = spinner.getItemAtPosition(i).toString();
            if (vals.equalsIgnoreCase(myString)) {
                index = i;
                break;
            }
        }
        return index;
    }

    private void showOneWayDatePicker() {
        DatePickerDialog mDatePicker1;
        mDatePicker1 = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker arg0, int selectedYear, int selectedMonth, int selectedDay) {
                oneWayYear = selectedYear;
                oneWayMonth = selectedMonth + 1;
                oneWayDay = selectedDay;
                String displayDay = String.format("%02d", oneWayDay);
                String displayMonth = String.format("%02d", oneWayMonth);
                etDate.setText(displayMonth + "/" + displayDay + "/" + oneWayYear);
            }
        }, oneWayYear, oneWayMonth - 1, oneWayDay);
        mDatePicker1.setTitle("Select Date");
        mDatePicker1.show();
    }

    int SearchType = 0;

    private void findViews() {
        txtTotalBase = (TextView) findViewById(R.id.txtTotalBase);
        txtTotalTax = (TextView) findViewById(R.id.txtTotalTax);
        txtTotal = (TextView) findViewById(R.id.txtTotal);
        rbBarcode = (RadioButton) findViewById(R.id.rbBarcode);
        rbItem = (RadioButton) findViewById(R.id.rbItem);
        rgSearch = (RadioGroup) findViewById(R.id.rgSearch);


        ll_updateData = (LinearLayout) findViewById(R.id.ll_updateData);
        tvPhotoCount = (TextView) findViewById(R.id.tv_photo_count);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        // et_item_name = (MaterialAutoCompleteTextView) findViewById(R.id.et_item_name);
        et_item_name = (DelayAutoCompleteTextView) findViewById(R.id.et_item_name);
        etDate = (MaterialEditText) findViewById(R.id.et_date);
        tvcomment = (MaterialEditText) findViewById(R.id.et_comment);
        et_BoxBarcode = (MaterialEditText) findViewById(R.id.et_BoxBarcode);
        et_ItemBarcode = findViewById(R.id.et_ItemBarcode);
        et_item_IDs = (MaterialEditText) findViewById(R.id.et_item_IDs);
        iv_ItemBarcode = (ImageView) findViewById(R.id.iv_ItemBarcode);
        iv_BoxBarcode = (ImageView) findViewById(R.id.iv_BoxBarcode);

        et_Qty = (MaterialEditText) findViewById(R.id.et_Qty);
        et_MRP = (MaterialEditText) findViewById(R.id.et_MRP);
        et_BaseAmt = (MaterialEditText) findViewById(R.id.et_BaseAmt);
        et_HSN = (MaterialEditText) findViewById(R.id.et_HSN);
        et_TAX = (MaterialEditText) findViewById(R.id.et_TAX);
        et_TAX1 = (MaterialEditText) findViewById(R.id.et_TAX1);
        et_CESS = (MaterialEditText) findViewById(R.id.et_CESS);
        et_CESS1 = (MaterialEditText) findViewById(R.id.et_CESS1);

        btnGetdetail = (Button) findViewById(R.id.btngetdetail);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        btn_submit_Master = (Button) findViewById(R.id.btn_submit_Master);
        rvImages1 = findViewById(R.id.rvImages);
        pickimagebtn1 = (ImageView) findViewById(R.id.imagepick);

        rvImages1.setLayoutManager(new LinearLayoutManager(GRNDetail.this, RecyclerView.HORIZONTAL, false));
        mAdapter1 = new SelectedImagesAdapter(this, mListSelectedIamges1);
        rvImages1.setAdapter(mAdapter1);
        pickimagebtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  openCamera();
                CameraDialog.show();

            }
        });
        rgSearch.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbItem) {
                    SearchType = 1;
                    iv_ItemBarcode.setVisibility(View.GONE);
                } else {
                    SearchType = 0;
                    iv_ItemBarcode.setVisibility(View.VISIBLE);
                }
                setAutoComplete();

            }
        });

        if (et_ItemBarcode.getText().toString().equalsIgnoreCase("")) {
            et_item_name.setVisibility(View.VISIBLE);
        } else {
            et_item_name.setVisibility(View.GONE);

        }


        et_ItemBarcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 3) {


                    loaditemmaster(et_ItemBarcode.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnCancel.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btn_submit_Master.setOnClickListener(this);
        //btnGetdetail.setOnClickListener(this);
        iv_ItemBarcode.setOnClickListener(this);
        iv_BoxBarcode.setOnClickListener(this);

        ivcamera = (ImageView) findViewById(R.id.iv_camera);
        spnr_exphead = (Spinner) findViewById(R.id.spnr_exphead);
        etDate.setOnClickListener(this);
        //tvPhotoCount.setOnClickListener(this);
        ivcamera.setOnClickListener(this);

    /*    et_item_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence editable, int i, int i1, int i2) {
                 if (isEditable && SearchType == 1) {
                    if (editable.length() > 2) {
                        Log.e("onqueryinside", editable.toString());

                        ArrayList<String> masterlist = new ArrayList<>();
                        clearContols();
                        loaditemmaster(editable.toString());
                    } else {
                        Log.e("onqueryoutside", editable.toString());
                        ArrayList<String> masterlist = new ArrayList<>();
                        myAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item_single_choice1, masterlist);
                        et_item_name.setAdapter(myAdapter);
                    }
                } else {
                    Log.e("onqueryoutside1", editable.toString());

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        et_item_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

 */
    }

    public void expaccounthead() {
        spnr = (Spinner) findViewById(R.id.spnr_exphead);
        // adapter = new ArrayAdapter<String>(GRNDetail.this, android.R.layout.simple_spinner_item, itemunit);
        //spnr.setAdapter(adapter);
        spnr.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        int position = spnr.getSelectedItemPosition();
                        //EditText txtnewdevicevehicle = (EditText) findViewById(R.id.txtnewdevicecode);
                        //Button btngetdetail=(Button) findViewById(R.id.btngetdetail);
                        //  spnr = (Spinner) findViewById(R.id.spnr_paidby);
                 /*       String val=spnr.getSelectedItem().toString();
                        if (val=="Cash")
                        {
                            // If Cash then do not show
                            //txtreason.setVisibility(View.INVISIBLE);
                        }
                        else if (val=="Credit")
                        {
                            //txtreason.setVisibility(View.INVISIBLE);
                            //spnrreason.setVisibility(View.INVISIBLE);
                        }*/
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                }
        );
    }


    private void uploadFieldwork() {
        // final AppClass globalVariable = (AppClass) getApplicationContext();
        // Date latestdate = globalVariable.getgdate();
        int errorfound = 0;
        {
            if (JKHelper.isEmpty(et_Qty)) {
                et_Qty.setError("Please enter Qty");
                et_Qty.requestFocus();
                errorfound = 1;
            } else if (Integer.parseInt(et_Qty.getText().toString()) < 1) {
                et_Qty.setError("Please enter valid Qty");
                et_Qty.requestFocus();
                errorfound = 1;
            } else if (et_item_name.getText().toString().isEmpty()) {
                et_item_name.setError("Please enter item name");
                et_item_name.requestFocus();
                errorfound = 1;
            } else if (et_MRP.getText().toString().isEmpty()) {
                et_MRP.setError("Please enter MRP");
                et_MRP.requestFocus();
                errorfound = 1;
            } else if (et_HSN.getText().toString().isEmpty()) {
                et_HSN.setError("Please enter HSN code");
                et_HSN.requestFocus();
                errorfound = 1;
            } else if (et_TAX.getText().toString().isEmpty()) {
                et_TAX.setError("Please enter Tax rate");
                et_TAX.requestFocus();
                errorfound = 1;
            } else if (et_TAX1.getText().toString().isEmpty()) {
                et_TAX1.setError("Please enter Tax rate");
                et_TAX1.requestFocus();
                errorfound = 1;
            } else if (et_CESS.getText().toString().isEmpty()) {
                et_CESS.setError("Please enter CESS rate");
                et_CESS.requestFocus();
                errorfound = 1;
            } else if (et_CESS1.getText().toString().isEmpty()) {
                et_CESS1.setError("Please enter CESS rate");
                et_CESS1.requestFocus();
                errorfound = 1;
            } else if (et_BaseAmt.getText().toString().isEmpty()) {
                et_BaseAmt.setError("Please enter sale rate");
                et_BaseAmt.requestFocus();
                errorfound = 1;
            } else if (Float.parseFloat(et_BaseAmt.getText().toString()) < 1) {
                et_BaseAmt.setError("Please enter valid sale rate");
                et_BaseAmt.requestFocus();
                errorfound = 1;
            }
            if (errorfound == 1) {
                return;
            }

            final ProgressDialog prgDialog = new ProgressDialog(GRNDetail.this);
            prgDialog.setMessage("Please Wait.....");
            prgDialog.setCancelable(false);
            prgDialog.show();
            String strUrl = "";
            if (et_item_IDs.getText().toString().equalsIgnoreCase("0")) {
                strUrl = "http://mmthinkbiz.com/MobileService.aspx/getds?method=CreateGR_Detail_Edit";
            } else {

            }


            StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_UPLOAD_GRDETAIL),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.e("Response", response);
                            String res = response;
                            prgDialog.dismiss();

                            if (res.length() > 0) {
                                log.e("res ateendfsad " + res);
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(res);
                                int success = jsonObject.getInt("success");
                                log.e("success" + success);
                                if (success == 1) {
                                    isEditable = true;
                                    String phoneno, deviceid, action, messagetosend = "";
                                    JSONArray jsonArray = jsonObject.getJSONArray("newusercreation");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject loopObjects = jsonArray.getJSONObject(i);
                                        serverid = loopObjects.getString("uid");
                                    }

                                    Toast.makeText(getApplicationContext(), "Item Added Successfully!!!", Toast.LENGTH_SHORT).show();

                                    updateserverphotoid1();
                                    uploadimage();

                                    clearContols();
                                    ll_updateData.setVisibility(View.GONE);
                                    if (strtax.equalsIgnoreCase("Without Tax")) {
                                        ftTotalBaseAmt = Float.parseFloat(et_BaseAmt.getText().toString()) + ftTotalBaseAmt;
                                    } else {
                                        ftTotalBaseAmt = (Float.parseFloat(et_BaseAmt.getText().toString()) - Float.parseFloat(et_TAX1.getText().toString())) + ftTotalBaseAmt;

                                    }
                                    ftTotalTaxAmt = Float.parseFloat(et_TAX1.getText().toString()) + ftTotalTaxAmt;
                                    ftTotalWithTaxAmt = Float.parseFloat(et_BaseAmt.getText().toString()) + Float.parseFloat(et_TAX1.getText().toString()) + ftTotalWithTaxAmt;
                                    txtTotalBase.setText("₹" + ftTotalBaseAmt);
                                    txtTotalTax.setText("₹" + ftTotalTaxAmt);
                                    txtTotal.setText("₹" + ftTotalWithTaxAmt);
                                    ftTotalTaxAmt = 0.0f;
                                    ftTotalWithTaxAmt = 0.0f;
                                    ftTotalBaseAmt = 0.0f;
                                    ftTotalBaseAmt1 = 0.0f;
                                    ftTotalTaxAmt1 = 0.0f;
                                    ftTotalWithTaxAmt1 = 0.0f;
                                    txtTotalBase.setText("₹" + "");
                                    txtTotalcess.setText("₹" + "");
                                    et_CESS.setText("0");
                                    et_CESS1.setText("0");
                                    et_TAX.setText("0");
                                    et_TAX1.setText("0");
                                    txtTotalTax.setText("₹" + "");
                                    txtTotal.setText("₹" + "");




                                    /*
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(GRNDetail.this );
                                    alertDialog.setTitle("Do you want to Add Items to this Fieldwork?");
                                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(GRNDetail.this, fieldworkadditem.class).putExtra("fieldworkid", serverid).putExtra("type", "0"));
                                            finish();
                                        }
                                    });
                                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    });
                                    alertDialog.show();
*/
                                    //JSONArray jsonArray = jsonObject.getJSONArray("returnds");
                                } else if (success == 0) {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Item was not updated", Toast.LENGTH_SHORT);
                                    toast.show();
                                    Message.message(GRNDetail.this, "Error on Fieldwork Upload");
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

                            Message.message(GRNDetail.this, "Failed To Retrieve Data");
                            //   Log.e("Error.Response", error.getMessage());
                            //updateserverphotoid();
                            //uploadimage();
                            //clearContols();
                            //finish();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params = generateUploadParams();
                    return params;
                }
            };
            postRequest.setRetryPolicy(new DefaultRetryPolicy(
                    60000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(GRNDetail.this).add(postRequest);
        }
    }

    private Map<String, String> generateUploadParams() {
        Map<String, String> params = new HashMap<>();
        Preferencehelper prefs = new Preferencehelper(GRNDetail.this);
        com.dhanuka.morningparcel.Helper.Preferencehelper prefss1 = new com.dhanuka.morningparcel.Helper.Preferencehelper(getApplicationContext());
        //  final AppClass globalVariable = (AppClass) getApplicationContext();
        params.put("status", 0 + "");
        params.put("type", "Purchase");
        params.put("contactid", prefss1.getPrefsContactId());
        String amountwithtax = "";
        String taxamt = "";
        taxamt = (Float.parseFloat(et_BaseAmt.getText().toString()) * Float.parseFloat(et_TAX.getText().toString()) / 100) + "";
        amountwithtax = (Float.parseFloat(et_BaseAmt.getText().toString()) + Float.parseFloat(et_TAX1.getText().toString())) + "";
        params.put("mobiledate", JKHelper.getCurrentDate());
        params.put("battery", JKHelper.getBatteryLevel(GRNDetail.this) + "");
        params.put("comments", tvcomment.getText().toString());
        params.put("masterid", woid);
        params.put("tax_type", txType + "");
        params.put("taxamt", et_TAX1.getText().toString());
        params.put("amountwithtax", txtTotal.getText().toString().replace(getString(R.string.rupee), ""));
        params.put("itemID", et_item_IDs.getText().toString());
        params.put("itembarcode", et_ItemBarcode.getText().toString());
        params.put("boxcode", et_BoxBarcode.getText().toString());
        params.put("itemname", et_item_name.getText().toString());
        params.put("cessper", et_CESS.getText().toString());
        params.put("cessAmt", txtTotalcess.getText().toString().replace(getString(R.string.rupee), ""));
        params.put("MRP", et_MRP.getText().toString());
        params.put("tax", et_TAX.getText().toString());
        params.put("Amount", txtTotalBase.getText().toString().replace(getString(R.string.rupee), ""));
        params.put("HSNCode", et_HSN.getText().toString());
        params.put("qty", et_Qty.getText().toString());
        params.put("manufacturingdate", etDate.getText().toString());
        params.put("expired", "0");
        params.put("damaged", "0");
        params.put("createddate", JKHelper.getCurrentDate());
        params.put("unit", spnr_exphead.getSelectedItem().toString());
        params.put("MainBranch", branchid);
        //(,,,,amountwithtax,taxamt)

        Log.e("paramsparams", params.toString());
        return params;
    }

    public void clearContols() {
        ll_updateData.setVisibility(View.GONE);
        etDate.setText("");
        isEditable = true;
        //tvPhotoCount.setText("0 Photos");
        et_Qty.setText("");
        et_item_name.setText("");
        tvcomment.setText("");
        lastRowMaterTable = null;
        et_ItemBarcode.setText("");
        et_BoxBarcode.setText("");
        et_BoxBarcode.requestFocus();
    }

    public void onClick(View v) {
        if (v == etDate) {
            showOneWayDatePicker();
        } else if (v == btnCancel) {
            txtTotalBase.setText("₹" + ftTotalBaseAmt1);
            txtTotalTax.setText("₹" + ftTotalTaxAmt1);
            txtTotal.setText("₹" + ftTotalWithTaxAmt1);


            clearContols();
        } else if (v == ivcamera) {
            openCamera(v, REQUEST_CODE);
        } else if (v == btn_submit_Master) {
            mListImages = new ArrayList<>();
            orderImagesAdapter = new OrderImagesAdapter(GRNDetail.this, mListImages, GRNDetail.this);
            rvImages.setAdapter(orderImagesAdapter);

            dialogItemDetail.show();
        }
/*
        else if (v == tvPhotoCount) {

            if (lastRowMaterTable == null) {
                Toast.makeText(GRNDetail.this, "No Photos Clicked Yet", Toast.LENGTH_LONG).show();
                return;
            }
            startActivity(new Intent(GRNDetail.this, PhotosScreen.class).putExtra("photo_count", tvPhotoCount.getText().toString()).putExtra("id", lastRowMaterTable));
        }
*/
        else if (v == btnSubmit) {
            {
                if (NetworkUtil.isConnectedToNetwork(GRNDetail.this)) {
                    //startstoplocservice(1);
                    uploadFieldwork();
                } else {
                    Crouton.showText(GRNDetail.this, "Please Connect To Internet", Style.ALERT);
                }
                JKHelper.closeKeyboard(GRNDetail.this, v);
            }
            log.e("api day attendence called====");
        } else if (v == btnGetdetail) {
            // requestForVehicleDetail();
        } else if (v == iv_ItemBarcode) {
            codeScanner1(iv_ItemBarcode);

            //scanCode(2);
            // requestForVehicleDetail();
        } else if (v == iv_BoxBarcode) {
            codeScanner(iv_BoxBarcode);
// scanCode(1);
            // requestForVehicleDetail();
        }
    }

    /*
        private void requestForVehicleDetail() {
            final ProgressDialog prgDialog = new ProgressDialog(GRNDetail.this);
            prgDialog.setMessage("Please Wait.....");
            prgDialog.setCancelable(false);
            prgDialog.show();

            StringRequest postRequest = new StringRequest(Request.Method.POST,getString(R.string.URL_GET_FIELDWORK_VEHICLEDETAIL),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.e("Response", response);
                            String res = response;
                            prgDialog.dismiss();

                            try {
                                JSONObject jsonObject = new JSONObject(res);
                                int success = jsonObject.getInt("success");
                                log.e("success" + success);
                                if (success == 1) {
    */
/*
                                showRecycleView();
*//*

                                JSONArray jsonArray = jsonObject.getJSONArray("returnds");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject loopObjects = jsonArray.getJSONObject(i);
                                    String VehicleGroup = loopObjects.getString("vehiclegroup");
                                    String DeviceCode = loopObjects.getString("devicecode");
                                    String DriverNo= loopObjects.getString("driverphoneno");
                                    String VehicleType = loopObjects.getString("vehicletype");

                                    int vals=getIndex(spnrbillable, VehicleGroup);
                                    if (vals>-1)
                                    {
                                        spnrbillable.setSelection(getIndex(spnrbillable, VehicleGroup));
                                    }
                                    else
                                    {
                                        spnrbillable.setSelection(getIndex(spnrbillable, "Select Client"));
                                    }
                                    vals=getIndex(spnrvehicletype, VehicleType);
                                    if (vals>-1)
                                    {
                                        spnrvehicletype.setSelection(getIndex(spnrvehicletype, VehicleType));
                                    }
                                    else
                                    {
                                        String sss=spnrvehicletype.getItemAtPosition(10).toString();
                                        vals=getIndex(spnrvehicletype, "Select Vehicle Type");
                                        spnrvehicletype.setSelection(vals);
                                    }
                                    etnewdevicecode.setText(DeviceCode);
                                    //etdriverno.setText(DriverNo);
                                }
                            } else if (success == 0) {
                                Message.message(GRNDetail.this, "No Data Exist");
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

                        Message.message(GRNDetail.this, "Failed To Retrieve Data");
                        //   Log.e("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("contactid", prefs.getProfileId());
                params.put("vehiclenumber", etvehicleno.getText().toString());

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(GRNDetail.this).add(postRequest);
    }
*/
    private void uploadimage() {
        if (JKHelper.isConnectedToNetwork(GRNDetail.this) && !JKHelper.isServiceRunning(GRNDetail.this, ImageUploadService.class)) {
            JKHelper.showLog("Start Service");
            startService(new Intent(GRNDetail.this, ImageUploadService.class));
        } else {
            stopService(new Intent(GRNDetail.this, ImageUploadService.class));
            startService(new Intent(GRNDetail.this, ImageUploadService.class));
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void openCamera(View v, int mREQUEST_CODE) {
        if (!PermissionUtils.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || !PermissionUtils.hasPermission(this, Manifest.permission.CAMERA) || !PermissionUtils.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            PermissionModule permissionModule = new PermissionModule(GRNDetail.this);
            permissionModule.checkPermissions();

        } else {
            SharedPreferences prefs;
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            filePath = getOutputMediaFileUri(); // create a file to save the image
            prefs = PreferenceManager.getDefaultSharedPreferences(GRNDetail.this);
            prefs.edit().putString(PREFS_FILE_PATH, filePath).commit();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(filePath))); // set the image file name
            log.e("file path in open camera==" + filePath);
            startActivityForResult(intent, mREQUEST_CODE);
            log.e("open camera is called");
        }
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

    int asREQUEST_CODE = 1010;
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
                    mAdapter1.addItems(mImages);
                    log.e("file path in request code true==" + filePath);
                    //  if (!mBean.getStrrmasterid().isEmpty()) {
                    isMain = 0;
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
                    mAdapter1.addItems(mImages);
                    log.e("file path in request code true==" + filePath);
                    //  if (!mBean.getStrrmasterid().isEmpty()) {
                    isMain = 0;
                    new ImageCompressionAsyncTask(true).execute(path);
                }
                //   }
            }

            if (requestCode == 10124) {
                try {
                    String strEditText = data.getStringExtra("mBar");
                    et_BoxBarcode.setText(strEditText);
                    et_ItemBarcode.setText(strEditText);
                    loaditemmaster(et_BoxBarcode.getText().toString());

                    //     checkBarcode(strEditText, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 10125) {
                try {
                    String strEditText = data.getStringExtra("mBar");
                    et_BoxBarcode.setText(strEditText);
                    et_ItemBarcode.setText(strEditText);
                    loaditemmaster(et_ItemBarcode.getText().toString());
//  checkBarcode(strEditText, 2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_CODE) {
                asREQUEST_CODE = requestCode;
                log.e("request code true  ");
                SharedPreferences prefs;
                prefs = PreferenceManager.getDefaultSharedPreferences(GRNDetail.this);
                final String path = prefs.getString(PREFS_FILE_PATH, filePath);
                log.e("file path in request code true==" + filePath);
                isMain = 1;
                new ImageCompressionAsyncTask(true).execute(path);
            } else if (requestCode == 2525) {
                asREQUEST_CODE = requestCode;
                log.e("request code true  ");
                SharedPreferences prefs;
                prefs = PreferenceManager.getDefaultSharedPreferences(GRNDetail.this);
//                final String path = prefs.getString(PREFS_FILE_PATH, filePath);
                log.e("file path in request code true==" + filePath);
                //  new GRNDetail.ImageCompressionAsyncTask(true).execute(path);

                final String path = prefs.getString(PREFS_FILE_PATH, filePath);
                Log.d("filep", String.valueOf(filePath));
                mListImages.add(path);
                orderImagesAdapter.notifyDataSetChanged();


            } else if (requestCode == 1) {
                String message = data.getStringExtra("result");
                //etchequeno.setText(message);
            }
        }
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        prefs.setNearByApiStatus(true);
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "User chose not to make required location settings changes.");
                        break;
                }
                break;
        }
    }

    String TAG = "GRM";
    Preferencehelper prefs;

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

            options.inSampleSize = utils.calculateInSampleSize(options, actualWidth, actualHeight);
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
            File file = new File(Environment.getExternalStorageDirectory().getPath(), "MmThinkBiz/Images");

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
                if (isMain == 0) {
                    modle.setmDescription("Gr_detail");
                    modle.setmImageType("Gr_detail");
                } else {
                    modle.setmDescription("GR_Master");
                    modle.setmImageType("GR_Master");

                }
                arrayList.add(modle);

                DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                    @Override
                    public void run(SQLiteDatabase database) {

                        ImageMasterDAO dao = new ImageMasterDAO(database, GRNDetail.this);
                        ArrayList<DbImageMaster> list = arrayList;
                        dao.insert(list);
                        addedToMasterTable = true;
                    }
                });
            }


            DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                @Override
                public void run(SQLiteDatabase database) {
// need to change this logic to use max number from database, instead of todocount -- Gunjan 11/04/2015
                    ImageMasterDAO dao = new ImageMasterDAO(database, GRNDetail.this);
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
            if (asREQUEST_CODE == 1010) {
                modle.setmDescription("Gr_detail");
                modle.setmImageType("Gr_detail");
            } else {
                modle.setmDescription("GR_Master");
                modle.setmImageType("GR_Master");
            }
            modle.setmImageId(masterDataBaseId);
            modle.setmImagePath(mImagePathDataBase);
            modle.setmImageName(mImageNameDataBase);
            arrayListUpload.add(modle);


            DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                @Override
                public void run(SQLiteDatabase database) {
                    ImageUploadDAO dao = new ImageUploadDAO(database, GRNDetail.this);
                    ArrayList<DbImageUpload> list = arrayListUpload;
                    dao.insert(list);

                    log.e("photo inserted ");
                }
            });
            setPhotoCount();
        }
    }

    private void setPhotoCount() {
/*
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                ImageUploadDAO dao = new ImageUploadDAO(database, GRNDetail.this);
                int count = dao.getCurrentWorkOrderImageCount(lastRowMaterTable);
                if (prefs.isDayStart()) {
                    tvPhotoCount.setText(count + " Photos");
                }
                log.e("photo inserted count==" + count);
            }
        });
*/

    }

    private void updateserverphotoid() {
        // change it for server exp id
        if (lastRowMaterTable != null) {
            SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
            ImageUploadDAO pd = new ImageUploadDAO(database, GRNDetail.this);

            if (isMain == 0) {
                pd.setWorkIdToTable(String.valueOf(serverid), lastRowMaterTable);
            } else {
                pd.setWorkIdToTable(String.valueOf(woid), lastRowMaterTable);

            }
            ImageMasterDAO pds = new ImageMasterDAO(database, GRNDetail.this);
            pds.setServerDetails(Integer.parseInt(lastRowMaterTable), Integer.valueOf(serverid), 0);
            ImageMasterDAO iDao = new ImageMasterDAO(database, getApplicationContext());
            String serverId = iDao.getServerIdById(1);
            DatabaseManager.getInstance().closeDatabase();
            log.e("in last row master table inserting  ");
        }
    }


    private void updateserverphotoid1() {
        for (int a = 0; a < mListLastRows.size(); a++) {
            if (mListLastRows.get(a) != null) {
                SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
                ImageUploadDAO pd = new ImageUploadDAO(database, GRNDetail.this);
                pd.setWorkIdToTable(String.valueOf(serverid), mListLastRows.get(a));
                ImageMasterDAO pds = new ImageMasterDAO(database, GRNDetail.this);
                pds.setServerDetails(Integer.parseInt(mListLastRows.get(a)), Integer.valueOf(serverid), 0);
                ImageMasterDAO iDao = new ImageMasterDAO(database, getApplicationContext());
                String serverId = iDao.getServerIdById(1);
                DatabaseManager.getInstance().closeDatabase();
                log.e("in last row master table inserting  ");
            }


        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("Granted", "onRequestPermissionsResult:" + requestCode);
        if (requestCode == 1) {
            try {
//                ServiceManager.getInstence().init(getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
            //LogUtil.openLog();
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    public void codeScanner(View v) {

        // startActivityForResult(new Intent(GRNDetail.this, Bar.class), 10124);
        if (et_BoxBarcode.getText().toString().length() >= 1) {
            checkBarcode(et_BoxBarcode.getText().toString(), 1);
        } else {
            etDate.setText("");

            //tvPhotoCount.setText("0 Photos");
            et_Qty.setText("");
            et_item_name.setText("");
            tvcomment.setText("");
            lastRowMaterTable = null;
            et_item_IDs.setText("0");
            //   et_BoxBarcode.setText("");
            //  et_BoxBarcode.requestFocus();

        }
//        scanCode(1);
    }

    public void codeScanner1(View v) {
        if (et_ItemBarcode.getText().toString().length() >= 1) {
            loaditemmaster(et_ItemBarcode.getText().toString());


            //  checkBarcode(et_ItemBarcode.getText().toString(), 2);
        } else {
            etDate.setText("");

            //tvPhotoCount.setText("0 Photos");
            et_Qty.setText("");
            et_item_name.setText("");
            tvcomment.setText("");
            lastRowMaterTable = null;
            et_item_IDs.setText("0");
            //   et_BoxBarcode.setText("");
            //  et_BoxBarcode.requestFocus();

        }
        //  scanCode(2);
        if (!PermissionUtils.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || !PermissionUtils.hasPermission(this, Manifest.permission.CAMERA) || !PermissionUtils.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            PermissionModule permissionModule = new PermissionModule(GRNDetail.this);
            permissionModule.checkPermissions();

        } else {
            startActivityForResult(new Intent(GRNDetail.this, Bar.class), 10125);
        }
    }

/*
    public void scanCode(final int type) {
        clearContols();
        AppUtil.setProp(ScanType.scansupport, "true");
        try {
//            ServiceManager.getInstence().getScan().setContinueScanTimeout(1000);
            ServiceManager.getInstence().getScan().setIfneedInvert(true);
            ServiceManager.getInstence().getScan().startScan(getTimeOut(), new OnBarcodeCallBack() {
                @Override
                public void onScanResult(String s) {
                    if (type == 1) {
                        et_BoxBarcode.setText(s);
                        checkBarcode(s, 1);


                    } else if (type == 2) {
                        et_ItemBarcode.setText(s);
                        checkBarcode(s, 2);
                    }//                    Toast.makeText(getApplicationContext(), "Result : " + s, Toast.LENGTH_SHORT).show();
                */
/*    AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
                    alert.setTitle("SCAN RESULT");
                    alert.setMessage(s);
                    alert.setCancelable(true);
                    alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = alert.create();
                    dialog.show();

                    *//*


                }

                @Override
                public void onFinish(int code, String msg) {
                    Toast.makeText(getApplicationContext(), "Scanning Failed", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/

    Boolean isEditable = true;

    public void checkBarcode(String strBarcode, int type) {
//        String strMatchedbarcode = "";
//        et_item_IDs.setText("0");
//        et_ItemBarcode.setText("");
//        et_item_name.setText("");
        //99
        if (DBItemUpload != null) {
            for (int i = 0; i < DBItemUpload.size(); i++) {
                if (type == 1) {
                    if (strBarcode.toString().equalsIgnoreCase(DBItemUpload.get(i).getmcompanycosting())) {
                        et_item_name.setText(DBItemUpload.get(i).getmItemName().toString());
                        et_BoxBarcode.setText(DBItemUpload.get(i).getmByItemNumber().toString());
                        strMatchedbarcode = DBItemUpload.get(i).getmByItemNumber().toString();
                        et_item_IDs.setText(DBItemUpload.get(i).getmItemID());
                        et_BaseAmt.setText(DBItemUpload.get(i).getSalerate());
                        et_HSN.setText(DBItemUpload.get(i).getHSNCode());
                        et_TAX.setText(DBItemUpload.get(i).getTaxRate());
                        et_MRP.setText(DBItemUpload.get(i).getMRP());
                    }
                } else {
                    if (strBarcode.toString().equalsIgnoreCase(DBItemUpload.get(i).getmByItemNumber())) {
                        et_item_name.setText(DBItemUpload.get(i).getmItemName().toString());
                        et_ItemBarcode.setText(DBItemUpload.get(i).getmByItemNumber().toString());
                        et_item_IDs.setText(DBItemUpload.get(i).getmItemID());
                        strMatchedbarcode = DBItemUpload.get(i).getmByItemNumber().toString();
                        et_BaseAmt.setText(DBItemUpload.get(i).getSalerate());
                        et_HSN.setText(DBItemUpload.get(i).getHSNCode());
                        et_TAX.setText(DBItemUpload.get(i).getTaxRate());
                        et_MRP.setText(DBItemUpload.get(i).getMRP());


                    }
                }

            /*    et_item_name.setText(DBItemUpload.get(i).getmItemName().toString()) ;
                if (DBItemUpload.get(i).getmByItemNumber().toString().length()>2)
                {
                    et_ItemBarcode.setText(DBItemUpload.get(i).getmByItemNumber().toString()) ;
                }
                et_item_IDs.setText(DBItemUpload.get(i).getmItemID().toString()) ;
*/
            }

            if (strMatchedbarcode.length() > 0) {
                isEditable = true;
                ll_updateData.setVisibility(View.VISIBLE);
            } else {
                et_item_IDs.setText("0");
                ll_updateData.setVisibility(View.VISIBLE);
                isEditable = false;
                if (type == 1) {
                    et_BoxBarcode.setText(strBarcode);
                } else {
                    et_ItemBarcode.setText(strBarcode);

                }

            }
        }

    }

    private int getTimeOut() {
        String time = "50";
        int timeout = 0;
        if (TextUtils.isEmpty(time)) {
            timeout = 10;
        } else {
            try {
                timeout = Integer.parseInt(time);
            } catch (Exception e) {
                timeout = 10;
                System.out.println(e.getMessage());
            }
        }
        return timeout;
    }

    public void printBarcode(View view) {
        if (!et_ItemBarcode.getText().toString().isEmpty()) {
            String msg = woid;//et_ItemBarcode.getText().toString()
            /*"powercraft.in"*/
            ;
      /*      PrinterClass printerClass = new PrinterClass(getApplicationContext());
            printerClass.printerInit();
            printerClass.printBarcode(msg);
            printerClass.paperFeed(3);
 */
        }
    }

/*
    public void printSampleBill(View view) {
        PrinterClass printerClass = new PrinterClass(getApplicationContext());
        printerClass.printerInit();
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.pcelogo);
        printerClass.printBitmapImage(bitmap, PrintLine.CENTER);
        printerClass.paperFeed(1);
        //RAM : Put here your data/variables which you want to print
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("       SAMPLE BILL      ");
        stringBuilder.append("      BILL DETAILS      ");
        stringBuilder.append("--------------------------------");
        stringBuilder.append("PRODUCT NAME               RATE ");
        stringBuilder.append("QTY      UNIT               AMT ");
        stringBuilder.append("--------------------------------");
        stringBuilder.append("XXXXXXXXXXXXXX     XXXXXXXXX.XX ");
        stringBuilder.append("XXXX.XX  XXX       XXXXXXXXX.XX ");
        stringBuilder.append("--------------------------------");
        stringBuilder.append("XXXXXXXXXXXXXX     XXXXXXXXX.XX ");
        stringBuilder.append("XXXX.XX  XXX       XXXXXXXXX.XX ");
        stringBuilder.append("--------------------------------");
        stringBuilder.append("XXXXXXXXXXXXXX     XXXXXXXXX.XX ");
        stringBuilder.append("XXXX.XX  XXX       XXXXXXXXX.XX ");
        stringBuilder.append("--------------------------------");
        stringBuilder.append("TOTAL    XXXXXXXXXXX.XX ");
        printerClass.leftAlignedPrintText(stringBuilder.toString(), 24, false);
        printerClass.paperFeed(1, TextPrintLine.FONT_SMALL);
        printerClass.centerAlignedPrintText("THANKYOU", TextPrintLine.FONT_SMALL, true);//48 Chars
        printerClass.paperFeed(3);
    }
*/

    @Override
    public void onImageDelete(int mPosition) {
        mListImages.remove(mPosition);
        orderImagesAdapter.notifyDataSetChanged();

    }

}



