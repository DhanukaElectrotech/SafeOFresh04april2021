package com.dhanuka.morningparcel.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
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
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.MMthinkBizUtils.DonutProgress;
import com.dhanuka.morningparcel.Helper.DbImageMaster;
import com.dhanuka.morningparcel.Helper.ImageLoadingUtils;
import com.dhanuka.morningparcel.Helper.ImageMasterDAO;
import com.dhanuka.morningparcel.Helper.ImageUploadDAO;
import com.dhanuka.morningparcel.R;
import com.dhanuka.morningparcel.SqlDatabase.DatabaseManager;
import com.dhanuka.morningparcel.SqlDatabase.QueryExecutor;
import com.dhanuka.morningparcel.adapter.SelectedImagesAdapter;
import com.dhanuka.morningparcel.utils.DbImageUpload;
import com.dhanuka.morningparcel.utils.ImageUploadService;
import com.dhanuka.morningparcel.utils.log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.adapter.CollectionAdapter;
import com.dhanuka.morningparcel.beans.CollectionBean;
import com.dhanuka.morningparcel.beans.FancyToast;
import com.dhanuka.morningparcel.events.Returnlistner;
import com.dhanuka.morningparcel.utils.EqualSpacingItemDecoration;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.filter.entity.ImageFile;

import static com.vincent.filepicker.activity.BaseActivity.IS_NEED_FOLDER_LIST;
import static com.vincent.filepicker.activity.ImagePickActivity.IS_NEED_CAMERA;

public class CustomerCollection extends AppCompatActivity implements Returnlistner, SwipeRefreshLayout.OnRefreshListener {
    List<CollectionBean> mListItems = new ArrayList<>();
    String filePath;
    @Nullable
    @BindView(R.id.lvProducts)
    RecyclerView lvProducts;
    @Nullable
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    RadioGroup rsortgroup, rreturngroup;
    RadioButton radioButton, radioButtonreturn;
    Dialog returndialog;
    String radioButtonreturntxt = "CASH";
    String date_of_installation, hoursstr;
    private int mYear, mMonth, mDay;
    TextView text100, txt200, txt300, txt500, txt1000;
    TextView Timeslect, Dateselect, starttime, endtime;
    EditText edtAmount, edtcomment;

    TextView btnSubmit;
    RecyclerView rvImages;
    ImageView backbtnicon;
    ArrayList<String> mListImages = new ArrayList<>();

    CollectionAdapter collectionAdapter;

    CollectionBean collectionBean;
    @BindView(R.id.refreshtdata)
    ImageView refreshtdata;
    DonutProgress txtblctotal;
    String mtype = "0";
    String lastRowMaterTable;
    ArrayList<String> mListLastRows = new ArrayList<>();
    int masterDataBaseId;
    private ImageLoadingUtils utils;

    public void makeBackClick(View v) {
        onBackPressed();
    }

    ImageView dialogbackbtnicon;
    TextView txtCurrentBalance;

    @Nullable
    @BindView(R.id.searchcollctn)
    EditText searchcollctn;
    Dialog CameraDialog;
    private static final int CAMERA_REQUEST = 1888;
    private ImageView collectimgclk;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    RecyclerView rvImagescollect;
    SelectedImagesAdapter selectedImagesAdapter;
    ArrayList<String> mListSelectedIamges = new ArrayList<>();
    SelectedImagesAdapter mAdapter;
    ImageView gallerybtn, camerabtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_collection);
        ButterKnife.bind(this);
        txtblctotal = findViewById(R.id.txtblctotal);
        returndialog = new Dialog(CustomerCollection.this, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        returndialog.setContentView(R.layout.return_money_pop);
        btnSubmit = returndialog.findViewById(R.id.btnSubmit);
        collectimgclk = returndialog.findViewById(R.id.collectclk);
        rvImagescollect = returndialog.findViewById(R.id.rvImagescollect);
        rvImagescollect.setLayoutManager(new LinearLayoutManager(CustomerCollection.this, RecyclerView.HORIZONTAL, false));
        mListSelectedIamges.clear();
        selectedImagesAdapter = new SelectedImagesAdapter(this, mListSelectedIamges);

        rvImagescollect.setAdapter(selectedImagesAdapter);
        dialogbackbtnicon = returndialog.findViewById(R.id.backbtnicon);
        Dateselect = returndialog.findViewById(R.id.datselecttxt);
        Timeslect = returndialog.findViewById(R.id.timeselect);
        edtAmount = returndialog.findViewById(R.id.edtAmount);
        txtCurrentBalance = returndialog.findViewById(R.id.txtCurrentBalance);
        rreturngroup = returndialog.findViewById(R.id.rreturngroup);
        edtcomment = returndialog.findViewById(R.id.edtCComment);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df1 = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String mTodayDate1 = df1.format(calendar.getTime());
        String arr[] = mTodayDate1.split(" ");
        String mTodayDate = arr[0];
        String mTodayTime = arr[1];

        // formattedDate have current date/time

        swipeRefresh.setOnRefreshListener(this);
        calendar.setTime(new Date(mTodayDate));

        CameraDialog = new Dialog(CustomerCollection.this);
        CameraDialog.setContentView(R.layout.popup_image_select);
        camerabtn = CameraDialog.findViewById(R.id.camerabtn);
        gallerybtn = CameraDialog.findViewById(R.id.gallerybtn);

        collectimgclk.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {


                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);

                } else {
                    CameraDialog.show();
                }
            }
        });

        camerabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(CustomerCollection.this, ImagePickActivity.class);
                intent1.putExtra(IS_NEED_CAMERA, true);
                intent1.putExtra(com.vincent.filepicker.Constant.MAX_NUMBER, 1);
                intent1.putExtra(IS_NEED_FOLDER_LIST, false);
                startActivityForResult(intent1, com.vincent.filepicker.Constant.REQUEST_CODE_TAKE_IMAGE);

                //  openCamera();
                //     launchCameraIntent();
                CameraDialog.dismiss();
            }
        });
        refreshtdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtblctotal.setVisibility(View.VISIBLE);
                mListItems.clear();
                lvProducts.setAdapter(new CollectionAdapter(CustomerCollection.this, mListItems, "image", CustomerCollection.this));

                loadHistory("1");

            }
        });

        Dateselect.setText(mTodayDate);
        Timeslect.setText(mTodayTime);
        Dateselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                panelcleandatepicker();
            }
        });
        dialogbackbtnicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returndialog.dismiss();
            }
        });

        Timeslect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHourPicker();
            }
        });


        text100 = returndialog.findViewById(R.id.txt100);
        txt200 = returndialog.findViewById(R.id.txt200);
        txt500 = returndialog.findViewById(R.id.txt500);
        txt1000 = returndialog.findViewById(R.id.txt1000);
        text100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtAmount.setText("100");

            }
        });
        txt200.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtAmount.setText("200");

            }
        });
        txt500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtAmount.setText("500");

            }
        });
        txt1000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtAmount.setText("1000");

            }
        });


        rreturngroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                int selectedId = rreturngroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radioButtonreturn = (RadioButton) rreturngroup.findViewById(selectedId);
                radioButtonreturntxt = radioButtonreturn.getText().toString();


            }
        });

        rreturngroup.check(R.id.cashslct);
        searchcollctn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (lvProducts != null) {
                    filter(s.toString());
                }
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateserverphotoid1();
                uploadimage();

                Creditreturn(radioButtonreturntxt, collectionBean.getCustomerID());


            }
        });

        rsortgroup = findViewById(R.id.rsortgroup);
        rsortgroup.check(R.id.allsort);
        lvProducts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        lvProducts.hasFixedSize();
        lvProducts.addItemDecoration(new EqualSpacingItemDecoration(15, LinearLayout.VERTICAL));
        loadHistory("1");
        rsortgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                radioButton = findViewById(checkedId);
                txtblctotal.setText(String.valueOf(0.0));

                mListItems = new ArrayList<>();
                if (radioButton.getText().toString().equalsIgnoreCase("All")) {

                    txtblctotal.setVisibility(View.GONE);
                    mListItems.clear();

                    lvProducts.setAdapter(new CollectionAdapter(CustomerCollection.this, mListItems, "image", CustomerCollection.this));
                    mtype = "0";

                    loadHistory("0");
                } else if (radioButton.getText().toString().equalsIgnoreCase("Outstanding")) {
                    txtblctotal.setVisibility(View.VISIBLE);
                    mListItems.clear();
                    mtype = "1";
                    lvProducts.setAdapter(new CollectionAdapter(CustomerCollection.this, mListItems, "image", CustomerCollection.this));

                    loadHistory("1");
                } else if (radioButton.getText().toString().equalsIgnoreCase("Balance")) {

                    txtblctotal.setVisibility(View.GONE);
                    mListItems.clear();
                    mtype = "2";
                    lvProducts.setAdapter(new CollectionAdapter(CustomerCollection.this, mListItems, "image", CustomerCollection.this));

                    loadHistory("2");

                }

            }
        });

        edtAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                setBalance(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    public void setBalance(String s) {
        Double blnc = 0.0;
        try {
            blnc = Double.parseDouble(collectionBean.getBalance());
            if (s.toString().length() > 0) {
                blnc = blnc + Double.parseDouble(s.toString());
            }
            txtCurrentBalance.setText(blnc + "");

            if (blnc > 0) {
                txtCurrentBalance.setTextColor(getResources().getColor(R.color.green));
            } else if (blnc < 0) {
                txtCurrentBalance.setTextColor(getResources().getColor(R.color.red));

            } else {
                txtCurrentBalance.setTextColor(getResources().getColor(R.color.black));

            }

        } catch (Exception e) {
            e.printStackTrace();

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
        if (returndialog != null && returndialog.isShowing()) {
            returndialog.dismiss();
        } else {
            super.onBackPressed();
        }

    }


    public void Creditreturn(String credittytpe, String customerid) {


        final ProgressDialog mProgressBar = new ProgressDialog(CustomerCollection.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();
        //   http://mmthinkbiz.com/mobileservice.aspx?method=Itemorderhistory_Web&strdt=07-01-2020&enddt=08-31-2020&contactid=66373&itemid=16921
        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.d("rawresponse", response);
                            mListItems = new ArrayList<>();

                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, CustomerCollection.this);
                            Log.d("responsiveitemhistory", responses);
                            JSONObject jsonObject = new JSONObject(responses);


                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {


                                Toast.makeText(getApplicationContext(), "Update Successfully !!", Toast.LENGTH_LONG).show();
                                returndialog.dismiss();
                                loadHistory("2");


                            } else {
                                //FancyToast.makeText(CustomerCollection.this, "No Data Found, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
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

                        FancyToast.makeText(CustomerCollection.this, "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Preferencehelper prefs;
                prefs = new Preferencehelper(CustomerCollection.this);

                Map<String, String> params1 = new HashMap<String, String>();

                try {
                    String param = getString(R.string.CREDIT_RETURN) + "&contactId=" + prefs.getPrefsTempContactid() + "&CustomerId=" + customerid + "&Amt=" + edtAmount.getText().toString() + "&comm=" + edtcomment.getText().toString() + "&dt=" + Dateselect.getText().toString() + " " + Timeslect.getText().toString() + "&PaymentType=" + credittytpe;
                    Log.d("withoutencryptionreturn", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, CustomerCollection.this);
                    params1.put("val", finalparam);
                    Log.d("paramsreturn", params1.toString());
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

    public void loadHistory(String credittytpe) {

        swipeRefresh.setRefreshing(true);
        final ProgressDialog mProgressBar = new ProgressDialog(CustomerCollection.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();
        //   http://mmthinkbiz.com/mobileservice.aspx?method=Itemorderhistory_Web&strdt=07-01-2020&enddt=08-31-2020&contactid=66373&itemid=16921
        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        swipeRefresh.setRefreshing(false);

                        try {
                            Log.d("rawresponse", response);
                            mListItems = new ArrayList<>();

                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, CustomerCollection.this);
                            Log.d("responsiveitemhistory", responses);
                            JSONObject jsonObject = new JSONObject(responses);


                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {


                                mListItems = new Gson().fromJson(jsonObject.getString("returnds"), new TypeToken<List<CollectionBean>>() {
                                }.getType());

                                collectionAdapter = new CollectionAdapter(CustomerCollection.this, mListItems, "image", CustomerCollection.this);

                                lvProducts.setAdapter(collectionAdapter);

                                float outstandtotal = (float) 0.0;
                                float numb = 0;
                                for (int i = 0; i < mListItems.size(); i++) {
                                    numb = Float.parseFloat(mListItems.get(i).getBalance());

                                    outstandtotal = numb + outstandtotal;
                                }
                                txtblctotal.setText(String.valueOf(outstandtotal).trim());

                            } else {
                                //FancyToast.makeText(CustomerCollection.this, "No Data Found, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
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

                        FancyToast.makeText(CustomerCollection.this, "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Preferencehelper prefs;
                prefs = new Preferencehelper(CustomerCollection.this);

                Map<String, String> params1 = new HashMap<String, String>();

                try {
                    String param = getString(R.string.ORDER_CREDIT) + "&ContactID=" + prefs.getPrefsContactId() + "&Credittype=" + credittytpe + "&custId=" + "" + "&Rptype=0";
                    Log.d("withoutencryption", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, CustomerCollection.this);
                    params1.put("val", finalparam);
                    Log.d("params1params1", params1.toString());
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

    public void showHourPicker() {
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


                Timeslect.setText(hoursstr + ":" + minutestr);


                //    Toast.makeText(getApplicationContext(),String.valueOf(hourOfDayy)+"more",Toast.LENGTH_LONG).show();

                //   timePicker1.setText(String.valueOf(hourOfDay) + ":" + String.valueOf(minute));
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(CustomerCollection.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();
    }


    public void filter(String text) {
        ArrayList<CollectionBean> filteredList = new ArrayList<>();

        for (CollectionBean product : mListItems) {
            if (product.getBalance().toLowerCase().contains(text) || product.getContactNo().toLowerCase().contains(text) || product.getCustomerName().toLowerCase().contains(text) || product.getFlatNo().toLowerCase().contains(text)) {
                filteredList.add(product);
            }
        }

        collectionAdapter.filterList(filteredList);
    }

    public void panelcleandatepicker() {
        // Get Current Date

        final Calendar c = Calendar.getInstance();

        c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(CustomerCollection.this,
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


                        Dateselect.setText(date_of_installation);


                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.show();

        //  datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

//       datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    @Override
    public void returnmoneyluster(CollectionBean collectionBean) {

        this.collectionBean = collectionBean;
        setBalance("0");
        edtAmount.setText("0");
        setBalance(collectionBean.getBalance());
        edtAmount.setText("");


        mListSelectedIamges.clear();
        returndialog.show();
        if (edtAmount != null) {
            edtAmount.requestFocus();
        }

    }

    @Override
    public void showreturndialog(boolean showme) {


    }


    @Override
    public void onRefresh() {

        txtblctotal.setText(String.valueOf(0.0).trim());
        mListItems = new ArrayList<>();
        loadHistory(mtype);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, String.format("collection_img%d", System.currentTimeMillis()), null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    private void updateserverphotoid1() {
        for (int a = 0; a < mListLastRows.size(); a++) {
            if (mListLastRows.get(a) != null) {
                SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
                ImageUploadDAO pd = new ImageUploadDAO(database, CustomerCollection.this);
                pd.setWorkIdToTable(String.valueOf(collectionBean.getCustomerID()), mListLastRows.get(a));
                ImageMasterDAO pds = new ImageMasterDAO(database, CustomerCollection.this);
                pds.setServerDetails(Integer.parseInt(mListLastRows.get(a)), Integer.valueOf(collectionBean.getCustomerID()), 0);
                ImageMasterDAO iDao = new ImageMasterDAO(database, getApplicationContext());
                String serverId = iDao.getServerIdById(1);
                DatabaseManager.getInstance().closeDatabase();
                log.e("in last row master table inserting  ");
            }


        }


    }

    File mFile1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                selectedImagesAdapter.addItems(mImages);
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


    String mImagePathDataBase, mImageNameDataBase, mCurrentTimeDataBase;


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


    String path = "";
    private boolean addedToMasterTable = false;

    private void addToDatabase() {


        if (!addedToMasterTable) {
            final ArrayList<DbImageMaster> arrayList = new ArrayList<>();

            DbImageMaster modle = new DbImageMaster();
            modle.setmDate(JKHelper.getCurrentDate());
            modle.setmUploadStatus(0);
            modle.setmServerId(collectionBean.getCustomerID());

            modle.setmDescription("order_master");
            modle.setmImageType("order_master");


            arrayList.add(modle);

            DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                @Override
                public void run(SQLiteDatabase database) {

                    ImageMasterDAO dao = new ImageMasterDAO(database, CustomerCollection.this);
                    ArrayList<DbImageMaster> list = arrayList;
                    dao.insert(list);
                    addedToMasterTable = true;
                }
            });
        }


        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                ImageMasterDAO dao = new ImageMasterDAO(database, CustomerCollection.this);
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

        modle.setmDescription("order_master");
        modle.setmImageType("order_master");
        modle.setmServerId(collectionBean.getCustomerID());
        modle.setmImageId(Integer.parseInt(collectionBean.getCustomerID()));


        modle.setmImagePath(mImagePathDataBase);
        modle.setmImageName(mImageNameDataBase);
        arrayListUpload.add(modle);


        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
            @Override
            public void run(SQLiteDatabase database) {
                ImageUploadDAO dao = new ImageUploadDAO(database, CustomerCollection.this);
                ArrayList<DbImageUpload> list = arrayListUpload;
                dao.insert(list);

                log.e("photo inserted ");
            }
        });

        //   setPhotoCount();
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

    //fetch
    //order_detail

    private void uploadimage() {
        if (JKHelper.isConnectedToNetwork(CustomerCollection.this) && !JKHelper.isServiceRunning(CustomerCollection.this, ImageUploadService.class)) {
            JKHelper.showLog("Start Service");
            startService(new Intent(CustomerCollection.this, ImageUploadService.class));
        } else {
            stopService(new Intent(CustomerCollection.this, ImageUploadService.class));
            startService(new Intent(CustomerCollection.this, ImageUploadService.class));
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


}