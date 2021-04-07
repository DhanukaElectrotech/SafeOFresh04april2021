package com.dhanuka.morningparcel.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.Helper.CatcodeHelper;
import com.dhanuka.morningparcel.Helper.Preferencehelper;

import com.dhanuka.morningparcel.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import com.dhanuka.morningparcel.SqlDatabase.BuildingDAO;
import com.dhanuka.morningparcel.Controllers.AppUrls;
import com.dhanuka.morningparcel.SqlDatabase.CityDAO;
import com.dhanuka.morningparcel.SqlDatabase.DatabaseHelper;
import com.dhanuka.morningparcel.SqlDatabase.DatabaseManager;
import com.dhanuka.morningparcel.SqlDatabase.QueryExecutor;
import com.dhanuka.morningparcel.SqlDatabase.SocietyDao;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class UpdateProfile extends AppCompatActivity {


    @BindView(R.id.mobile)
    TextView Mobileedt;

    @BindView(R.id.emailedt)
    EditText emailedt;
    @BindView(R.id.flat)
    EditText flat;
    @BindView(R.id.fnameedt)
    EditText fnameedt;
    @BindView(R.id.lastnedt)
    EditText lastnedt;


    @BindView(R.id.passet)
    EditText passet;

    @BindView(R.id.cpassedt)
    EditText confirmpasset;
    @BindView(R.id.backbtnicon)
    ImageView cancelbtn;
    @BindView(R.id.login_button)
    Button login_button;
    Dialog Localdialog;
    @BindView(R.id.txtzip)
    MaterialEditText txtzip;

    @BindView(R.id.changepass)
    Button changepass;

    Spinner societyname, buildingname;
    ArrayList<String> ids1, societylist = new ArrayList<String>();

    ArrayList<String> ids2 = new ArrayList<>();
    ArrayList<String> buildinglist = new ArrayList<>();
    HashMap<String, String> societyhash = new HashMap<>();
    HashMap<String, String> buildinghash = new HashMap<>();
    String state[] = {"HARYANA", "DELHI"};
    String userlist[] = {"CONSUMER", "RETAILER"};
    String city[] = {"FARIDABAD", "GURGAON"};
    String building[] = {"Select Building"};
    AlertDialog.Builder builder;
    TextView textView;
    Button clickok;
    Preferencehelper preferencehelper;
    ArrayAdapter buildingadap;
    @BindView(R.id.buildingheader)
    TextView buildingheader;
    @BindView(R.id.societyheader)
    TextView societyheader;
    @BindView(R.id.flatheader)
    TextView flatheader;
    @BindView(R.id.updatelatlong)
    Button updatelatlong;
    String buildinstr, socirtystr, flatstr;
    ArrayList<CatcodeHelper> listDatabasenewsourcee = new ArrayList<>();

    public void backClick(View view) {
        onBackPressed();
    }

    @BindView(R.id.spinnerCity)
    Spinner spinnerCity;
    int selct = 0;
    @Nullable
    @BindView(R.id.opentimeudt)
    EditText opentimeudt;
    @Nullable
    @BindView(R.id.closetimeudt)
    EditText closetimeudt;
    String date_of_installation, hoursstr;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        preferencehelper = new Preferencehelper(UpdateProfile.this);
        ButterKnife.bind(this);
        buildinglist.add("Select Building");
        societylist.add("Select Society");
        societyname = findViewById(R.id.autoSociety);
        buildingname = findViewById(R.id.building);
        Localdialog = new Dialog(UpdateProfile.this);
        builder = new AlertDialog.Builder(this);
        Localdialog.setContentView(R.layout.custom_dialogbox);
        Localdialog.setCancelable(false);
        Localdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        textView = Localdialog.findViewById(R.id.custom_dialogtext);
        clickok = Localdialog.findViewById(R.id.clickok);
        Mobileedt.setText(preferencehelper.getPREFS_phoneno());
        emailedt.setText(preferencehelper.getPREFS_email2());
        fnameedt.setText(preferencehelper.getPREFS_firstname());
        flat.setText(preferencehelper.getPREFS_flatno());


        preferencehelper.getPREFS_city();
        preferencehelper.getPREFS_society();
        preferencehelper.getPREFS_Building();
        preferencehelper.getPREFS_state();
        txtzip.setText(preferencehelper.getPrefsZipCode());
        opentimeudt.setText(preferencehelper.getPrefsShopOpenTime());
        closetimeudt.setText(preferencehelper.getPrefsShopCloseTime());

        if (preferencehelper.getPrefsUsercategory().equalsIgnoreCase("1057")) {
            buildingname.setVisibility(View.VISIBLE);
            societyname.setVisibility(View.VISIBLE);
            buildingheader.setVisibility(View.VISIBLE);
            societyheader.setVisibility(View.VISIBLE);
            flat.setVisibility(View.VISIBLE);
            flatheader.setVisibility(View.VISIBLE);

        } else {
            buildingname.setVisibility(View.GONE);
            societyname.setVisibility(View.VISIBLE);
            flat.setVisibility(View.VISIBLE);
            flatheader.setVisibility(View.VISIBLE);
            buildingheader.setVisibility(View.GONE);
            societyheader.setVisibility(View.VISIBLE);
        }

        getCity("11114", UpdateProfile.this);
        // CommonHelper.getbuilding("56", SignUpActivity.this);


        //getItemfromSociety();


        opentimeudt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHourPicker(1);
            }
        });

        closetimeudt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHourPicker(2);
            }
        });


        buildingadap = new ArrayAdapter(getApplicationContext(), R.layout.simple_list_item_single_choice1, building);
        ArrayAdapter societyadaopter = new ArrayAdapter(getApplicationContext(), R.layout.simple_list_item_single_choice1, societylist);
        societyadaopter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        buildingadap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        societyadaopter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        buildingname.setAdapter(buildingadap);
        // societyname.setAdapter(societyadaopter);
        //  societyname.setSelection(societyadaopter.getPosition(preferencehelper.getPREFS_society()));


      /*  societyname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                getItemfromBuilding(societyhash.get(societyname.getSelectedItem().toString()));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });*/

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateProfile.super.onBackPressed();
            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flat.getText().toString().isEmpty()) {
                    flat.setError("flat name can't be empty ,Please enter flat");

                } else if (Mobileedt.getText().toString().isEmpty()) {
                    Mobileedt.setError("Mobile no name can't be empty ,Please enter Mobile no");


                } else {
                    Signupapi();
                }


            }
        });
        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateProfile.this, ChangePass.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));


            }
        });
        updatelatlong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(UpdateProfile.this, AddLatLong.class).putExtra("type", "1").putExtra("tripdids", "").setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));


            }
        });


    }

    public void makeSubmitClick(View view) {
        // makeLogin();
        startActivity(new Intent(UpdateProfile.this, HomeActivity.class).putExtra("type", "Home"));
    }

    public void getItemfromSociety() {
        ArrayList<String> citynames = new ArrayList<>();
        ids1 = new ArrayList<String>();
        societylist = new ArrayList<String>();
        DatabaseManager.initializeInstance(new DatabaseHelper(getApplicationContext()));
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        SocietyDao pd = new SocietyDao(database, this);
        ArrayList<CatcodeHelper> listDatabasenewsource = pd.selectAll();

        Log.d("stringdatasizelist", String.valueOf(listDatabasenewsource.size()));
        for (int i = 0; i < listDatabasenewsource.size(); i++) {
            societylist.add(listDatabasenewsource.get(i).getSocietynames());
            ids1.add(listDatabasenewsource.get(i).getSoceityid());
            societyhash.put(listDatabasenewsource.get(i).getSocietynames(), listDatabasenewsource.get(i).getSoceityid());

        }


    }

    public void getItemfromBuilding(String idcheck) {


        DatabaseManager.initializeInstance(new DatabaseHelper(getApplicationContext()));
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        BuildingDAO pd = new BuildingDAO(database, this);
        listDatabasenewsourcee = pd.selectAll();
        buildinglist.clear();
        Log.d("stringbuildinglist", String.valueOf(listDatabasenewsourcee.size()));
        for (int i = 0; i < listDatabasenewsourcee.size(); i++) {
            if (idcheck.equalsIgnoreCase(listDatabasenewsourcee.get(i).getBuildingid())) {

                buildinglist.add(listDatabasenewsourcee.get(i).getBuildingname());
                ids2.add(listDatabasenewsourcee.get(i).getBuildingid());
                buildinghash.put(listDatabasenewsourcee.get(i).getBuildingname(), listDatabasenewsourcee.get(i).getBuildingid());


            }

            buildingadap = new ArrayAdapter(getApplicationContext(), R.layout.simple_list_item_single_choice1, buildinglist);
            buildingname.setAdapter(buildingadap);
            buildingname.setSelection(buildingadap.getPosition(preferencehelper.getPREFS_Building()));

        }


    }


    public void Signupapi() {

        if (preferencehelper.getPrefsUsercategory().equalsIgnoreCase("1058")) {
            buildinstr = "";
            socirtystr = "";
            flatstr = "";
        } else {
            buildinstr = "";
            //  buildinstr = buildingname.getSelectedItem().toString();
            socirtystr = societyname.getSelectedItem().toString();
            flatstr = flat.getText().toString();

        }
        Preferencehelper prefs = new Preferencehelper(UpdateProfile.this);

        final ProgressDialog mProgressBar = new ProgressDialog(UpdateProfile.this);
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
                            String responses = jkHelper.Decryptapi(response, UpdateProfile.this);
                            JSONObject jsonObject = new JSONObject(responses);
                            int successtr = Integer.parseInt(jsonObject.getString("success"));
                            if (successtr == 1) {
                                JSONArray jsonArray = jsonObject.getJSONArray("newusercreation");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject newjson = jsonArray.getJSONObject(i);
                                    String returnmessage = newjson.getString("returnmessage");

                                    if (!returnmessage.equalsIgnoreCase("user already registered")) {
                                        Localdialog.show();
                                        textView.setText("Profile Updated successfully");
                                        clickok.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                preferencehelper.setPREFS_society(societyname.getSelectedItem().toString());
                                                preferencehelper.setPREFS_Building(buildingname.getSelectedItem().toString());
                                                preferencehelper.setPREFS_firstname(fnameedt.getText().toString());
                                                preferencehelper.setPREFS_email2(emailedt.getText().toString());
                                                preferencehelper.setPrefsUsercategory(preferencehelper.getPrefsUsercategory());
                                                preferencehelper.setPREFS_city(spinnerCity.getSelectedItem().toString());
                                                preferencehelper.setPREFS_flatno(flat.getText().toString());

                                                if (prefs.getPrefsUsercategory().equalsIgnoreCase("1057")) {
                                                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));

                                                } else {
                                                    startActivity(new Intent(getApplicationContext(), HomeStoreActivity.class));

                                                }
                                                finish();
                                            }
                                        });

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
                    String param = com.dhanuka.morningparcel.utils.AppUrls.strSignup + "&fname=" + fnameedt.getText().toString() + "&lname=" + "" + "&gender=" + "" + "&city=" + preferencehelper.getPREFS_city()
                            + "&username=" + Mobileedt.getText().toString() + "&password=" + "" + "&bday=" + "01/01/1800" + "&mobileno=" + preferencehelper.getPREFS_phoneno() +
                            "&email=" + emailedt.getText().toString() + "&zip=" + txtzip.getText().toString() + "&securityq=" + "" + "&country=" + cntry + "&securityp=" + "" + "&address=" + flat.getText().toString()
                            + "&alternatephone=" + "" + "&alternatemail=" + emailedt.getText().toString() + "" + "&security2=" + "" + "&securitya2=" + "test" + "&state=" + preferencehelper.getPREFS_state()
                            + "&servicetype=" + servicetype + "&contactid=" + prefs.getPrefsContactId() + "&flatno=" + flat.getText().toString() + "&building=" + "" + "&society=" + societyname.getSelectedItem().toString() + "&type=" + "1" + "&StoreOpenTime="+opentimeudt.getText().toString() +"&StoreCloseTime="+closetimeudt.getText().toString();
                    Log.e("beforeenc", param);
                    param = jkHelper.Encryptapi(param, UpdateProfile.this);
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


    public void getCity(String type, Context ctx) {

        final ProgressDialog mProgressBar = new ProgressDialog(ctx);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();
        mProgressBar.setCancelable(false);

        ArrayList<CatcodeHelper> catlist = new ArrayList<>();
        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("Response", response);

                        String res = response;


                        try {
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, UpdateProfile.this);
                            JSONObject jsonObject = new JSONObject(responses);
                            JSONArray jsonArray = jsonObject.getJSONArray("returnds");
                            if (jsonObject.getInt("success") == 1) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject newjson = jsonArray.getJSONObject(i);
                                    String catcodeid = newjson.getString("CatCodeID");
                                    String catcodedes = newjson.getString("CodeDescription");
                                    CatcodeHelper v = new CatcodeHelper();
                                    v.setCityname(catcodedes);
                                    v.setCityid(catcodeid);
                                    catlist.add(v);
                                    Log.d("catcodelist", String.valueOf(catlist.size()));

                                }
                                DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                                    @Override
                                    public void run(SQLiteDatabase database) {

                                        CityDAO citydao = new CityDAO(database, ctx);
                                        ArrayList<CatcodeHelper> list = catlist;
                                        citydao.deleteAll();
                                        citydao.insert(list);
                                    }
                                });
                                getItemFromCity();

                                int count = citylist.size();

                                for (int i = 0; i < count; i++) {
                                    for (int j = i + 1; j < count; j++) {
                                        //    String strStringoi0="";
                                        // if (citylist.get(j).length()>4){
                                        String[] strString = citylist.get(j).split(" ");

                                        // }else{
                                        //    strString=citylist.get(j);
//
                                        //  }

                                        if (citylist.get(i).contains(strString[0])) {
                                            citylist.remove(j--);
                                            count--;
                                        }
                                    }
                                }
                                ArrayAdapter cityadapter = new ArrayAdapter(getApplicationContext(), R.layout.simple_list_item_single_choice1, citylist);
                                cityadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinnerCity.setAdapter(cityadapter);

                                for (int a = 0; a < citylist.size(); a++) {
                                    if (citylist.get(a).equalsIgnoreCase(preferencehelper.getPREFS_city())) {
                                        spinnerCity.setSelection(a);

                                    }
                                }
                                //  spinnerCity.setEnabled(false);
                                spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        getsoceitylist("5555", spinnerCity.getSelectedItem().toString(), UpdateProfile.this);

                                        // getItemfromSociety(cityhash.get(spinnerCity.getSelectedItem().toString()));


                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });


                            } else {

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
                        // error
                        //   Log.e("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                try {
                    JKHelper jkHelper = new JKHelper();
                    String param = AppUrls.GET_REASON_LIST + "&contactid=" + "0" + "&type=" + type;
                    Log.e("beforeenc", param);
                    param = jkHelper.Encryptapi(param, UpdateProfile.this);
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

        Volley.newRequestQueue(ctx).add(postRequest);
    }

    public void getsoceitylist(String type, String str, Context ctx) {
        final ProgressDialog mProgressBar = new ProgressDialog(ctx);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();
        mProgressBar.setCancelable(false);

        societylist = new ArrayList<String>();
        societylist.add("Select Society");
        // societylist.add("Add New Society");

        ArrayList<CatcodeHelper> catlist = new ArrayList<>();
        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("Response" + type, response);
                        mProgressBar.dismiss();
                        String res = response;


                        try {
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, UpdateProfile.this);
                            JSONObject jsonObject = new JSONObject(responses);
                            JSONArray jsonArray = jsonObject.getJSONArray("returnds");
                            if (jsonObject.getInt("success") == 1) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject newjson = jsonArray.getJSONObject(i);
                                    //  String catcodeid = newjson.getString("Val1");
                                    String catcodedes = newjson.getString("CodeDescription");
                                    CatcodeHelper v = new CatcodeHelper();
                                    //  v.setSocietynames(catcodedes);
                                    //  v.setSoceityid(catcodeid);
                                    //  catlist.add(v);
                                    //   Log.d("catcodelist",String.valueOf(catlist.size()));
                                    societylist.add(catcodedes);
                                }
                            /*    DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                                    @Override
                                    public void run(SQLiteDatabase database) {

                                        SocietyDao societydao = new SocietyDao(database, ctx);
                                        ArrayList<CatcodeHelper> list = catlist;
                                        societydao.deleteAll();
                                        societydao.insert(list);
                                    }
                                });


                                DatabaseManager.initializeInstance(new DatabaseHelper(getApplicationContext()));
                                SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
                                SocietyDao pd = new SocietyDao(database, ctx);
                                ArrayList<CatcodeHelper> listDatabasenewsource = pd.selectAll();
*/


                            } else {
                                //  societylist.add("Select Society");
                                //  societylist.add("Add New Society");

                            }
                            selct = 0;
                            for (int a = 0; a < societylist.size(); a++) {
                                if (societylist.get(a).equalsIgnoreCase(preferencehelper.getPREFS_society())) {
                                    selct = a;
                                }
                            }


                            ArrayAdapter societyadaopter = new ArrayAdapter(getApplicationContext(), R.layout.simple_list_item_single_choice1, societylist);
                            societyname.setAdapter(societyadaopter);
                            societyname.setSelection(societyadaopter.getPosition(preferencehelper.getPREFS_society()));
                            societyname.setSelection(selct);

                        } catch (Exception e) {
                            ArrayAdapter societyadaopter = new ArrayAdapter(getApplicationContext(), R.layout.simple_list_item_single_choice1, societylist);
                            societyname.setAdapter(societyadaopter);
                            societyname.setSelection(societyadaopter.getPosition(preferencehelper.getPREFS_society()));

                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressBar.dismiss();
                        ArrayAdapter societyadaopter = new ArrayAdapter(getApplicationContext(), R.layout.simple_list_item_single_choice1, societylist);
                        societyname.setAdapter(societyadaopter);
                        societyname.setSelection(societyadaopter.getPosition(preferencehelper.getPREFS_society()));


                        // error
                        //   Log.e("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                try {
                    JKHelper jkHelper = new JKHelper();
                    String param = AppUrls.GET_REASON_LIST + "&contactid=" + "0" + "&type=" + type + "&val=" + str;
                    Log.e("beforeenc", param);
                    param = jkHelper.Encryptapi(param, UpdateProfile.this);
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

        Volley.newRequestQueue(ctx).add(postRequest);
    }

    ArrayList<String> citylist = new ArrayList<>();
    ArrayList<CatcodeHelper> listdatabasecity = new ArrayList<>();

    public void getItemFromCity() {


        DatabaseManager.initializeInstance(new DatabaseHelper(getApplicationContext()));
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        CityDAO pd = new CityDAO(database, this);
        listdatabasecity = pd.selectAll();
        citylist = new ArrayList<>();
        Log.d("stringbuildinglist", String.valueOf(listdatabasecity.size()));
        for (int i = 0; i < listdatabasecity.size(); i++) {
         /*   for (int a = 0; a < listdatabasecity.size(); a++) {
                if (citylist.size() > 0) {
                    if (listdatabasecity.get(i).getCityname().toLowerCase().contains(citylist.get(a).substring(0,3).toLowerCase())) {
                        citylist.add(listdatabasecity.get(i).getCityname());

                    }

                } else {*/
            citylist.add(listdatabasecity.get(i).getCityname());

         /*       }
            }*/
        }


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
                    opentimeudt.setText(hoursstr + ":" + minutestr);


                } else if (type == 2) {
                    closetimeudt.setText(hoursstr + ":" + minutestr);

                }


                //    Toast.makeText(getApplicationContext(),String.valueOf(hourOfDayy)+"more",Toast.LENGTH_LONG).show();

                //   timePicker1.setText(String.valueOf(hourOfDay) + ":" + String.valueOf(minute));
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(UpdateProfile.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();
    }

}