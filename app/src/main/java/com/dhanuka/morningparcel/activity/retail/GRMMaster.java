package com.dhanuka.morningparcel.activity.retail;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

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
import com.dhanuka.morningparcel.ExampleApplication;
import com.dhanuka.morningparcel.R;
import com.google.gson.Gson;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import com.dhanuka.morningparcel.activity.retail.adapter.SBillableUnderAdapter;
import com.dhanuka.morningparcel.activity.retail.beans.BillAbleUnderDAO;
import com.dhanuka.morningparcel.activity.retail.beans.DbBillAbleUnder;
import com.dhanuka.morningparcel.adapter.SelectedImagesAdapter;
import com.dhanuka.morningparcel.beans.FancyToast;
import com.dhanuka.morningparcel.beans.NewBranchsalesbean;
import com.dhanuka.morningparcel.utils.DbImageUpload;
import com.dhanuka.morningparcel.utils.ImageUploadService;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.utils.PermissionModule;
import com.dhanuka.morningparcel.utils.PermissionUtils;
import com.dhanuka.morningparcel.utils.Preferencehelper;
import com.dhanuka.morningparcel.utils.log;

import static com.vincent.filepicker.activity.BaseActivity.IS_NEED_FOLDER_LIST;
import static com.vincent.filepicker.activity.ImagePickActivity.IS_NEED_CAMERA;

//import com.dhanuka.morningparcel.Helper.Preferencehelper;

public class GRMMaster extends AppCompatActivity implements View.OnClickListener {

    public void onBackClick(View v) {
        onBackPressed();
    }
    ArrayList<String> branchlist = new ArrayList<>();
    HashMap<String, String> branchhash = new HashMap<>();
    String mstatus = "0";
    ArrayList<NewBranchsalesbean.beanchinnerbean> mListReport = new ArrayList<>();
    String lastRowMaterTable;
    RecyclerView rvImages;
    Spinner spinner_branch;
    String branchid;
    SelectedImagesAdapter mAdapter;
    ArrayList<String> mListSelectedIamges = new ArrayList<>();
    ArrayList<String> mListLastRows = new ArrayList<>();

    //private TextView tvPhotoCount ;
    private ImageView pickimagebtn, ivcamera;
    private MaterialEditText et_invoiceno, etDate, tvcomment;
    private Spinner spnrworktype, spnrbillable, spnr_to, spnr_from;
    private Button btnCancel, btnSubmit, btnGetdetail;
    private String filePath;
    private String woid, strworktype, strclientname, strvehicleno;
    private final int REQUEST_CODE = 1010;


    private LinearLayout ll_fromto, ll_GRN;

    private String PREFS_FILE_PATH = "capture_file_path";
    String mImagePathDataBase;
    String mImageNameDataBase;
    String mCurrentTimeDataBase, strvehicletype;
    private ImageLoadingUtils utils;
    private boolean addedToMasterTable = false;
    int masterDataBaseId;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    String serverid = "";

    SBillableUnderAdapter sBillableUnderAdapter;
    String strbillables = "";
    Spinner spnrTax;
    public ArrayAdapter<String> myAdapter;
    ArrayList<String> businesstype1;
    String ConsignorID = "0";

    String[] fromtostring = {
            "WareHouse",
            "Floor"
    };

    String[] celebrities = {
            "Select WorkType",
            "Installed",
            "Uninstalled",
            "Changed",
            "Repaired",
            "Lock Open",
            "Inspection",
            "Device Lost"
    };


    String[] reason =
            {
                    "Select Reason",
                    "Wire Cut",
                    "Sim Fail",
                    "Device Physically Damaged",
                    "Device Damaged",
                    "Other",
            };

    Spinner spnr;
    ArrayAdapter<String> adapter;
    ListView listView;
    private int oneWayDay, oneWayMonth, oneWayYear;
    ArrayList<DbBillAbleUnder> dataList;
    Button btnPOS;
    Dialog CameraDialog;
    SharedPreferences prefs1;
    Preferencehelper prefs;
    SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grnmasters);
        spinner_branch = findViewById(R.id.spinner_branch55);
        woid = "0";
        prefs1 = getSharedPreferences("MORNING_PARCEL_POS",
                MODE_PRIVATE);
        mEditor = prefs1.edit();
        mEditor.putString("mValues", "");
        mEditor.commit();
        branchlist.add("Select Branch");
        branchhash.put("Select Branch","0");
        getReports();
        CameraDialog = new Dialog(GRMMaster.this);
        CameraDialog.setContentView(R.layout.popup_image_select);
        ImageView camerabtn = CameraDialog.findViewById(R.id.camerabtn);
        ImageView gallerybtn = CameraDialog.findViewById(R.id.gallerybtn);
        gallerybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(GRMMaster.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(GRMMaster.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                } else {
                    Intent intent1 = new Intent(GRMMaster.this, ImagePickActivity.class);
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
        spinner_branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if((position)>0)
                {
                    branchid= branchhash.get(spinner_branch.getSelectedItem().toString());

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        camerabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(GRMMaster.this, ImagePickActivity.class);
                intent1.putExtra(IS_NEED_CAMERA, true);
                intent1.putExtra(com.vincent.filepicker.Constant.MAX_NUMBER, 1);
                intent1.putExtra(IS_NEED_FOLDER_LIST, false);
                startActivityForResult(intent1, com.vincent.filepicker.Constant.REQUEST_CODE_TAKE_IMAGE);

                //  openCamera();
                //     launchCameraIntent();
                CameraDialog.dismiss();
            }
        });
        findViews();
        if (!PermissionUtils.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || !PermissionUtils.hasPermission(this, Manifest.permission.CAMERA) || !PermissionUtils.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            PermissionModule permissionModule = new PermissionModule(GRMMaster.this);
            permissionModule.checkPermissions();

        }
        Preferencehelper prefs = new Preferencehelper(GRMMaster.this);
        JKHelper jkhelper = new JKHelper();
        //jkhelper.getEmployeeDeviceCode(prefs.getProfileId(),GRMMaster.this);
        //uploadworktype(); // worktype
        settingTimeAndDate();
        //tvPhotoCount.setText("0 Photos");
        utils = new ImageLoadingUtils(this);

        Bundle b = getIntent().getExtras();

        ll_GRN.setVisibility(View.VISIBLE);
        ll_fromto.setVisibility(View.GONE);
        getBillableUnder("9999");
        if (b != null) {
            strworktype = b.getString("type");
            if (strworktype.equalsIgnoreCase("2"))//1 - GRN, 2- Stock Transfer
            {
                loadfromto();
                ll_fromto.setVisibility(View.VISIBLE);
                ll_GRN.setVisibility(View.GONE);
            } else {
                ll_GRN.setVisibility(View.VISIBLE);
                ll_fromto.setVisibility(View.GONE);
                getBillableUnder("9999");
            }
        }
    }

    public void getBillableUnder(final String contactid) {
        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("Response", response);
                        String res = response;
                        try {

                            Log.d("responseWEEK", response);
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, getApplicationContext());
                            JSONObject jsonObject = new JSONObject(responses);
                            int success = jsonObject.getInt("success");
                            log.e("success" + success);
                            if (success == 1) {

                                log.e("in success one ");
                                JSONArray jsonArray = jsonObject.getJSONArray("returnds");
                                dataList = new ArrayList<DbBillAbleUnder>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject singleObj = jsonArray.getJSONObject(i);
                                    String clientId = singleObj.getString("clientid");
                                    String clientName = singleObj.getString("printname");
                                    DbBillAbleUnder v = new DbBillAbleUnder();
                                    v.setmClientId(clientId);
                                    v.setmPrintName(clientName);
                                    v.setmbranchcode("");
                                    v.setmbranchid(singleObj.getString("branchid"));
                                    v.setmbusinesstype("");
                                    v.setmcurrentstatus("");
                                    v.setmnickname("");
                                    dataList.add(v);
                                }
                                DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                                    @Override
                                    public void run(SQLiteDatabase database) {

                                        log.e("adding to database bill able under");
                                        BillAbleUnderDAO dao = new BillAbleUnderDAO(database, GRMMaster.this);
                                        ArrayList<DbBillAbleUnder> list = dataList;
                                        dao.deleteAll();
                                        dao.insert(list);
                                        loadclientdata(); //billable
                                        //  Message.message(ctx, "Data fetched Successfuly");
                                    }
                                });
                            } else if (success == 0) {
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
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                com.dhanuka.morningparcel.Helper.Preferencehelper preferencehelper=new com.dhanuka.morningparcel.Helper.Preferencehelper(getApplicationContext());

                String param = getString(R.string.URL_GET_CLIENT) + "&contactid=" + preferencehelper.getPrefsContactId();
                Log.d("Beforeencrptionpay", param);
                JKHelper jkHelper = new JKHelper();
                String finalparam = jkHelper.Encryptapi(param, GRMMaster.this);
                params.put("val", finalparam);
                Log.d("afterencrptionpay", finalparam);
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(GRMMaster.this).add(postRequest);
    }


    private void loadclientdata() {
        // database handler
        DatabaseManager.initializeInstance(new DatabaseHelper(GRMMaster.this));
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        BillAbleUnderDAO dao = new BillAbleUnderDAO(database, GRMMaster.this);
        ArrayList<DbBillAbleUnder> listDatabaseNew = dao.selectAll();
        DbBillAbleUnder Selectclient = new DbBillAbleUnder();
        Selectclient.setmPrintName("Select Client");
        listDatabaseNew.add(Selectclient);

        sBillableUnderAdapter = new SBillableUnderAdapter(this, listDatabaseNew);
        spnrbillable.setAdapter(sBillableUnderAdapter);
        if (listDatabaseNew.size() > 0) {
            //spnrpaidby.setSelection(sBillableUnderAdapter.getItem(0).getmPrintName());
            //spnrvehicletype.setSelection(getIndex(spnrvehicletype, "Select Client"));
            spnrbillable.setSelection(getIndex(spnrbillable, sBillableUnderAdapter.getItem(listDatabaseNew.size() - 1).getmPrintName()));
            //spnrpaidby.setSelection(sBillableUnderAdapter.getItem(0).getmWorkTypes());
        }
    }

    private void worktypechangeevent(String val) {
        /*if (val == "Installed") {
            tvcomment.setVisibility(View.VISIBLE);
        } else if (val == "Uninstalled") {
            tvcomment.setVisibility(View.VISIBLE);
        }*/
    }

    public void loadfromto() {
        adapter = new ArrayAdapter<String>(GRMMaster.this, android.R.layout.simple_spinner_item, fromtostring);
        spnr_from.setAdapter(adapter);
        spnr_to.setAdapter(adapter);
    }


    public void uploadworktype() {
        adapter = new ArrayAdapter<String>(GRMMaster.this, android.R.layout.simple_spinner_item, celebrities);
        spnrworktype.setAdapter(adapter);
        spnrworktype.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        int position = spnrworktype.getSelectedItemPosition();
                        String val = spnrworktype.getSelectedItem().toString();
                        worktypechangeevent(val);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                }
        );
    }

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

    private void findViews() {
        //tvPhotoCount = (TextView)findViewById( R.id.tv_photo_count );
        etDate = (MaterialEditText) findViewById(R.id.et_date);
        spnrbillable = (Spinner) findViewById(R.id.spinner_billable);
        spnrworktype = (Spinner) findViewById(R.id.spnrworktype);
        spnrTax = (Spinner) findViewById(R.id.spnr_tax);
        spnr_to = (Spinner) findViewById(R.id.spnr_to);
        spnr_from = (Spinner) findViewById(R.id.spnr_from);
        tvcomment = (MaterialEditText) findViewById(R.id.et_comment);
        ll_fromto = (LinearLayout) findViewById(R.id.ll_fromto);
        ll_GRN = (LinearLayout) findViewById(R.id.ll_GRN);
        et_invoiceno = (MaterialEditText) findViewById(R.id.et_invoiceno);

        btnPOS = (Button) findViewById(R.id.btnPOS);
        btnGetdetail = (Button) findViewById(R.id.btngetdetail);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnSubmit = (Button) findViewById(R.id.btn_submit);

        btnPOS.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnGetdetail.setOnClickListener(this);

        rvImages = findViewById(R.id.rvImages);
        pickimagebtn = (ImageView) findViewById(R.id.imagepick);
        ivcamera = (ImageView) findViewById(R.id.iv_camera);
        etDate.setOnClickListener(this);
        //tvPhotoCount.setOnClickListener(this);
        rvImages.setLayoutManager(new LinearLayoutManager(GRMMaster.this, RecyclerView.HORIZONTAL, false));
        mAdapter = new SelectedImagesAdapter(this, mListSelectedIamges);
        rvImages.setAdapter(mAdapter);
        ivcamera.setOnClickListener(this);
        pickimagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  openCamera();
                CameraDialog.show();

            }
        });
    }

    private void uploadFieldwork() {
        final ExampleApplication globalVariable = (ExampleApplication) getApplicationContext();
        //   Date latestdate = globalVariable.getgdate();
        int errorfound = 0;
        {
            if (JKHelper.isEmpty(et_invoiceno)) {
                et_invoiceno.setError("Please enter Invoice Number");
                et_invoiceno.requestFocus();
                errorfound = 1;
            }

            strbillables = spnrbillable.getSelectedItem().toString();
            if (strbillables == "Select Client") {
                Toast.makeText(getApplicationContext(), "Select Client", Toast.LENGTH_SHORT).show();
                errorfound = 1;
                log.e("in error");
            } else {
                //spnrdebitaccount.setError(null);
                spnrbillable.clearFocus();
                log.e("in success");
            }

/*
            if (strworktype.equals("Installed") )
            {
                // If installed, then client name, vehicle number, vehicle type,device code is must, installation date
                String strdevicecode= spnrdevicecode.getSelectedItem().toString();
                if (strdevicecode.equals("") || strdevicecode.equals("Select DeviceCode"))
                {
                    errorfound=1;
                    Toast.makeText(getApplicationContext(), "Device Code is must in Install", Toast.LENGTH_SHORT).show();
                }
            }
            else if (strworktype.equals("Uninstalled") ) {
                if (JKHelper.isEmpty(etnewdevicecode) || etnewdevicecode.getText().toString().equals("Select DeviceCode")) {
                    errorfound=1;
                    Toast.makeText(getApplicationContext(), "Please Enter Uninstalled Device Code", Toast.LENGTH_SHORT).show();
                }
            }
            else if (strworktype.equals("Repaired") || strworktype.equals("Inspection") || strworktype.equals("Lock Open") ) {
                if (JKHelper.isEmpty(etnewdevicecode) )
                {
                    errorfound=1;
                    Toast.makeText(getApplicationContext(), "Please Enter Repaired Device Code ", Toast.LENGTH_SHORT).show();
                    etnewdevicecode.requestFocus();
                }
                else if (JKHelper.isEmpty(tvcomment) )
                {
                    errorfound=1;
                    Toast.makeText(getApplicationContext(), "Please Enter Comments", Toast.LENGTH_SHORT).show();
                    tvcomment.requestFocus();
                }
                else if (spnrreason.getSelectedItem().toString()=="Select Reason")
                {
                    errorfound=1;
                    Toast.makeText(getApplicationContext(), "Please Select Reason", Toast.LENGTH_SHORT).show();
                    spnrreason.requestFocus();
                }
            }
            else if (strworktype.equals("Changed") ) {
                String strdevicecode= spnrdevicecode.getSelectedItem().toString();
                if (JKHelper.isEmpty(etnewdevicecode) )
                {
                    errorfound=1;
                    Toast.makeText(getApplicationContext(), "Please Enter Repaired Device Code ", Toast.LENGTH_SHORT).show();
                    etnewdevicecode.requestFocus();
                }
                else if (strdevicecode.equals("") || strdevicecode.equals("Select DeviceCode"))
                {
                    errorfound=1;
                    Toast.makeText(getApplicationContext(), "Device Code is must in Install", Toast.LENGTH_SHORT).show();
                }
                else if (JKHelper.isEmpty(tvcomment) )
                {
                    errorfound=1;
                    Toast.makeText(getApplicationContext(), "Please Enter Comments", Toast.LENGTH_SHORT).show();
                    tvcomment.requestFocus();
                }
                else if (spnrreason.getSelectedItem().toString()=="Select Reason")
                {
                    errorfound=1;
                    Toast.makeText(getApplicationContext(), "Please Select Reason", Toast.LENGTH_SHORT).show();
                    spnrreason.requestFocus();
                }

            }
*/
            if (errorfound == 1) {
                return;
            }


            final ProgressDialog prgDialog = new ProgressDialog(GRMMaster.this);
            prgDialog.setMessage("Please Wait.....");
            prgDialog.setCancelable(false);
            prgDialog.show();

            StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_UPLOAD_GRMASTER),
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
                                    String phoneno, deviceid, action, messagetosend = "";
                                    JSONArray jsonArray = jsonObject.getJSONArray("newusercreation");
                                    //  for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject loopObjects = jsonArray.getJSONObject(0);
                                    serverid = loopObjects.getString("uid");
                                    //  }

                                    Toast.makeText(getApplicationContext(), "GR Created Successfully!!!", Toast.LENGTH_SHORT).show();
                                    if (mListLastRows.size() > 0) {

                                        updateserverphotoid();
                                        uploadimage();
                                    }
                                    /*  updateserverphotoid();
                                    uploadimage();*/
                                    startActivity(new Intent(GRMMaster.this, GRNDetail.class)
                                            .putExtra("id", serverid)
                                            .putExtra("tax", spnrTax.getSelectedItem().toString())
                                            .putExtra("supplier", strbillables)
                                            .putExtra("invoicenumber", et_invoiceno.getText().toString())
                                            .putExtra("invoicedate", etDate.getText().toString())
                                            .putExtra("type", "")
                                            .putExtra("supplierID", ConsignorID)
                                             .putExtra("branchid",branchid)); // 0=normal, 1=priority -branchid
                                    clearContols();

                                    log.e("Uploaded Fieldwork Successful = " + spnrTax.getSelectedItem().toString());
                                    //JSONArray jsonArray = jsonObject.getJSONArray("returnds");
                                } else if (success == 0) {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Fieldwork was not updated", Toast.LENGTH_SHORT);
                                    toast.show();
                                    Message.message(GRMMaster.this, "Error on Fieldwork Upload");
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

                            Message.message(GRMMaster.this, "Failed To Retrieve Data");
                            //   Log.e("Error.Response", error.getMessage());
                            //  updateserverphotoid();
                            //uploadimage();
                            clearContols();
                            finish();
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
            Volley.newRequestQueue(GRMMaster.this).add(postRequest);
        }
    }

    private Map<String, String> generateUploadParams() {
        Map<String, String> params = new HashMap<>();
        com.dhanuka.morningparcel.Helper.Preferencehelper prefs = new com.dhanuka.morningparcel.Helper.Preferencehelper(GRMMaster.this);
        params.put("status", 0 + "");
        params.put("type", "1");// 1 = GRN, 2 = Stock transfer
        if (prefs.getPrefsContactId() != null) {
            params.put("contactid", prefs.getPrefsContactId());
        } else {
            params.put("contactid", "7508");
        }
        params.put("mobiledate", JKHelper.getCurrentDate());
        params.put("battery", JKHelper.getBatteryLevel(GRMMaster.this) + "");
        params.put("comments", tvcomment.getText().toString());
        params.put("worktype", "");
        params.put("MainBranch", branchid);

//        if (strworktype.equalsIgnoreCase("2"))//1 - GRN, 2- Stock Transfer
//        {
//            params.put("from", spnr_from.getSelectedItem().toString());
//            params.put("to", spnr_to.getSelectedItem().toString());
//            params.put("clientname", "");
//            params.put("ConsignorID", "0");
//        }

//        else {
        params.put("from", "");
        params.put("to", "");
        params.put("clientname", spnrbillable.getSelectedItem().toString());
        if (dataList != null) {
            for (int i = 0; i < dataList.size(); i++) {
                if (spnrbillable.getSelectedItem().toString().equalsIgnoreCase(dataList.get(i).getmPrintName())) {
                    ConsignorID = dataList.get(i).getmClientId().toString();
                }
            }
        }
        params.put("ConsignorID", ConsignorID);
//        }


        params.put("docID", et_invoiceno.getText().toString());
        params.put("createddate", JKHelper.getCurrentDate());
        params.put("fieldworkdate", etDate.getText().toString());
        Log.d("pramsGRmaster",params.toString());

        return params;
    }

    public void clearContols() {
        etDate.setText("");

        //tvPhotoCount.setText("0 Photos");
        et_invoiceno.setText("");
        tvcomment.setText("");
        spnrworktype.setSelection(getIndex(spnrworktype, "Select WorkType"));
        lastRowMaterTable = null;
    }

    public void onClick(View v) {
        if (v == etDate) {
            showOneWayDatePicker();
        } else if (v == btnCancel) {
            clearContols();
        } else if (v == btnPOS) {
            SharedPreferences prefs = getSharedPreferences("MORNING_PARCEL_POS",
                    MODE_PRIVATE);
            SharedPreferences.Editor mEditor = prefs.edit();
            mEditor.putString("resp2", "");
            mEditor.putString("resp1", "");
            mEditor.commit();

            startActivity(new Intent(GRMMaster.this, NewBillActivity.class));
        }
/*
        else if (v == tvPhotoCount) {

            if (lastRowMaterTable == null) {
                Toast.makeText(GRMMaster.this, "No Photos Clicked Yet", Toast.LENGTH_LONG).show();
                return;
            }
            startActivity(new Intent(GRMMaster.this, PhotosScreen.class).putExtra("photo_count", tvPhotoCount.getText().toString()).putExtra("id", lastRowMaterTable));
        }
*/
        else if (v == ivcamera) {
            if (!PermissionUtils.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || !PermissionUtils.hasPermission(this, Manifest.permission.CAMERA) || !PermissionUtils.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                PermissionModule permissionModule = new PermissionModule(GRMMaster.this);
                permissionModule.checkPermissions();

            } else {
                openCamera(v);
                JKHelper.closeKeyboard(GRMMaster.this, v);
            }
        } else if (v == btnSubmit) {
            {
                if (NetworkUtil.isConnectedToNetwork(GRMMaster.this)) {
                    //startstoplocservice(1);
                    uploadFieldwork();
                } else {
                    Crouton.showText(GRMMaster.this, "Please Connect To Internet", Style.ALERT);
                }
                JKHelper.closeKeyboard(GRMMaster.this, v);
            }
            log.e("api day attendence called====");
        } else if (v == btnGetdetail) {
            // requestForVehicleDetail();
        }
    }

    /*
        private void requestForVehicleDetail() {
            final ProgressDialog prgDialog = new ProgressDialog(GRMMaster.this);
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
                                Message.message(GRMMaster.this, "No Data Exist");
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

                        Message.message(GRMMaster.this, "Failed To Retrieve Data");
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
        Volley.newRequestQueue(GRMMaster.this).add(postRequest);
    }
*/
    private void uploadimage() {
        if (JKHelper.isConnectedToNetwork(GRMMaster.this) && !JKHelper.isServiceRunning(GRMMaster.this, ImageUploadService.class)) {
            JKHelper.showLog("Start Service");
            startService(new Intent(GRMMaster.this, ImageUploadService.class));
        } else {
            stopService(new Intent(GRMMaster.this, ImageUploadService.class));
            startService(new Intent(GRMMaster.this, ImageUploadService.class));
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

    public void openCamera(View v) {
        SharedPreferences prefs;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        filePath = getOutputMediaFileUri(); // create a file to save the image
        prefs = PreferenceManager.getDefaultSharedPreferences(GRMMaster.this);
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
            if (requestCode == REQUEST_CODE) {
                log.e("request code true  ");
                SharedPreferences prefs;
                prefs = PreferenceManager.getDefaultSharedPreferences(GRMMaster.this);
                final String path = prefs.getString(PREFS_FILE_PATH, filePath);
                log.e("file path in request code true==" + filePath);
                new GRMMaster.ImageCompressionAsyncTask(true).execute(path);
            } else if (requestCode == 1) {
                String message = data.getStringExtra("result");
                //etchequeno.setText(message);
            }
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

    protected String TAG = "GRM";

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
                modle.setmDescription("GR_Master");
                modle.setmImageType("GR_Master");
                arrayList.add(modle);

                DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                    @Override
                    public void run(SQLiteDatabase database) {

                        ImageMasterDAO dao = new ImageMasterDAO(database, GRMMaster.this);
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
                    ImageMasterDAO dao = new ImageMasterDAO(database, GRMMaster.this);
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
            modle.setmDescription("GR_Master");
            modle.setmImageType("GR_Master");
            modle.setmImageId(masterDataBaseId);
            modle.setmImagePath(mImagePathDataBase);
            modle.setmImageName(mImageNameDataBase);
            arrayListUpload.add(modle);


            DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                @Override
                public void run(SQLiteDatabase database) {
                    ImageUploadDAO dao = new ImageUploadDAO(database, GRMMaster.this);
                    ArrayList<DbImageUpload> list = arrayListUpload;
                    dao.insert(list);

                    log.e("photo inserted ");
                }
            });
            setPhotoCount();
        }
    }

    private void setPhotoCount() {
        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                ImageUploadDAO dao = new ImageUploadDAO(database, GRMMaster.this);
                int count = dao.getCurrentWorkOrderImageCount(lastRowMaterTable);
              /*  if (prefs.isDayStart()) {
                    //tvPhotoCount.setText(count + " Photos");
                }*/
                log.e("photo inserted count==" + count);
            }
        });

    }

    private void updateserverphotoid() {
        for (int a = 0; a < mListLastRows.size(); a++) {
            if (mListLastRows.get(a) != null) {
                SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
                ImageUploadDAO pd = new ImageUploadDAO(database, GRMMaster.this);
                pd.setWorkIdToTable(String.valueOf(serverid), mListLastRows.get(a));
                ImageMasterDAO pds = new ImageMasterDAO(database, GRMMaster.this);
                pds.setServerDetails(Integer.parseInt(mListLastRows.get(a)), Integer.valueOf(serverid), 0);
                ImageMasterDAO iDao = new ImageMasterDAO(database, getApplicationContext());
                String serverId = iDao.getServerIdById(1);
                DatabaseManager.getInstance().closeDatabase();
                log.e("in last row master table inserting  ");
            }


        }


    }

    private void updateserverphotoid1() {
        // change it for server exp id
        if (lastRowMaterTable != null) {
            SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
            ImageUploadDAO pd = new ImageUploadDAO(database, GRMMaster.this);
            pd.setWorkIdToTable(String.valueOf(serverid), lastRowMaterTable);
            ImageMasterDAO pds = new ImageMasterDAO(database, GRMMaster.this);
            pds.setServerDetails(Integer.parseInt(lastRowMaterTable), Integer.valueOf(serverid), 0);
            ImageMasterDAO iDao = new ImageMasterDAO(database, getApplicationContext());
            String serverId = iDao.getServerIdById(1);
            DatabaseManager.getInstance().closeDatabase();
            log.e("in last row master table inserting  ");
        }
    }

    public void moveToGRreport(View bview) {
        //startActivity(new Intent(GRMMaster.this, OrderHistoryActivity.class));
    }

    public void getReports() {


        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String mTodayDate = df.format(c.getTime());


        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("responseWEEK", response);
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, getApplicationContext());
                            JSONObject jsonObject = new JSONObject(responses);
                            Log.d("responseWEEKNew", responses);

                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                NewBranchsalesbean storewiseBean = new Gson().fromJson(responses, NewBranchsalesbean.class);
                                mListReport = storewiseBean.getBranchdatalist();

                                for (int i = 0; i < mListReport.size(); i++) {
                                    branchhash.put(storewiseBean.getBranchdatalist().get(i).getBranchName(), storewiseBean.getBranchdatalist().get(i).getBranchId());

                                    branchlist.add(storewiseBean.getBranchdatalist().get(i).getBranchName());
                                }
                                ArrayAdapter branchadapter = new ArrayAdapter(getApplicationContext(), R.layout.simple_list_item_single_choice1, branchlist);
                                branchadapter.setDropDownViewResource(R.layout.simple_list_item_single_choice1);


                                spinner_branch.setAdapter(branchadapter);


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
        ) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                com.dhanuka.morningparcel.Helper.Preferencehelper prefs = new com.dhanuka.morningparcel.Helper.Preferencehelper(getApplicationContext());
                ;
                try {
                    String param = getString(R.string.URL_GET_BRANCH) + "&contactid=" + prefs.getPrefsContactId();
                    Log.d("Beforeencrptionpay", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, GRMMaster.this);
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
}

