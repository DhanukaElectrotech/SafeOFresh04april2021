package com.dhanuka.morningparcel.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.Helper.CatcodeHelper;

import com.dhanuka.morningparcel.R;

import com.dhanuka.morningparcel.SqlDatabase.BuildingDAO;
import com.dhanuka.morningparcel.Controllers.AppUrls;
import com.dhanuka.morningparcel.SqlDatabase.CityDAO;
import com.dhanuka.morningparcel.SqlDatabase.DatabaseHelper;
import com.dhanuka.morningparcel.SqlDatabase.DatabaseManager;
import com.dhanuka.morningparcel.SqlDatabase.QueryExecutor;
import com.dhanuka.morningparcel.SqlDatabase.SocietyDao;

import com.example.librarymain.DhanukaMain;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SignUpActivity extends AppCompatActivity {


    @BindView(R.id.spinnerStatee)
    Spinner spinnerState;
    @BindView(R.id.spinnerCity)
    Spinner spinnerCity;

    @BindView(R.id.spinnerusertype)
    Spinner spinnerusertype;
    @BindView(R.id.mobile)
    MaterialEditText Mobileedt;

    @BindView(R.id.emailedt)
    MaterialEditText emailedt;
    @BindView(R.id.flat)
    MaterialEditText flat;


    @BindView(R.id.fnameedt)
    MaterialEditText fnameedt;
    @BindView(R.id.lastnedt)
    MaterialEditText lastnedt;


    @BindView(R.id.passet)
    MaterialEditText passet;

    @BindView(R.id.cpassedt)
    MaterialEditText confirmpasset;
    @BindView(R.id.backbtnicon)
    ImageView cancelbtn;
    @BindView(R.id.login_button)


    Button login_button;
    Dialog Localdialog;
    String countrystr;
    AlertDialog.Builder builder;
    Spinner societyname, countryspin;
    ArrayList<String> ids1, societylist = new ArrayList<String>();

    ArrayList<String> ids2 = new ArrayList<>();
    ArrayList<String> buildinglist = new ArrayList<>();
    ArrayList<String> citylist = new ArrayList<>();
    ArrayList<String> cityid = new ArrayList<>();
    @BindView(R.id.txtzip)
    MaterialEditText txtzip;

    HashMap<String, String> societyhash = new HashMap<>();
    HashMap<String, String> buildinghash = new HashMap<>();

    HashMap<String, String> cityhash = new HashMap<>();
    String state[] = {"HARYANA", "DELHI"};
    String userlist[] = {"CONSUMER", "RETAILER"};
    String city[] = {"FARIDABAD", "GURGAON"};
    TextView textView;
    Button clickok;
    String buildinstr, socirtystr, flatstr;
    @BindView(R.id.buildingheader)
    TextView buildingheader;
    @BindView(R.id.societyheader)
    TextView societyheader;
    @BindView(R.id.checkboxseniorst)
    CheckBox seniorsitcheck;
    @BindView(R.id.termscheck)
    CheckBox termscheck;

    String society, cityy;
    ArrayList<CatcodeHelper> listDatabasenewsourcee = new ArrayList<>();

    ArrayList<CatcodeHelper> listdatabasecity = new ArrayList<>();

    @BindView(R.id.addnewsoc)
    TextView addnewsoc;
    JKHelper jkHelper;

    public void backClick(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        /*   startActivity(new Intent(getApplicationContext(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
         */
        finish();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_grocery);

        ButterKnife.bind(this);

        jkHelper = new JKHelper();

        buildinglist.add("United States");
        buildinglist.add("India");


        cityy = getIntent().getStringExtra("city");
        society = getIntent().getStringExtra("society");
        // societylist.add("Add New  Society");
        societyname = findViewById(R.id.autoSociety);
        countryspin = findViewById(R.id.countryspin);

        Localdialog = new Dialog(SignUpActivity.this);
        builder = new AlertDialog.Builder(this);
        Localdialog.setContentView(R.layout.custom_dialogbox);
        Localdialog.setCancelable(false);
        Localdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        textView = Localdialog.findViewById(R.id.custom_dialogtext);
        clickok = Localdialog.findViewById(R.id.clickok);

        ArrayAdapter sateadapter = new ArrayAdapter(getApplicationContext(), R.layout.simple_list_item_single_choice1, state);
        ArrayAdapter usertypeadapter = new ArrayAdapter(getApplicationContext(), R.layout.simple_list_item_single_choice1, userlist);

         ArrayAdapter countryadap = new ArrayAdapter(getApplicationContext(), R.layout.simple_list_item_single_choice1, buildinglist);
       // ArrayAdapter societyadaopter = new ArrayAdapter(getApplicationContext(), R.layout.simple_list_item_single_choice1, societylist);
        sateadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countryadap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        usertypeadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerState.setAdapter(sateadapter);
        countryspin.setAdapter(countryadap);

        spinnerusertype.setAdapter(usertypeadapter);
        seniorsitcheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                } else {

                }
            }
        });

        termscheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                } else {

                }
            }
        });
        termscheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startActivity(new Intent(SignUpActivity.this, Reach_Us.class).putExtra("type", "trms"));

            }
        });

        addnewsoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddNewSociety.class).putExtra("city", spinnerCity.getSelectedItem().toString()));
            }
        });





        // CommonHelper.getbuilding("56", SignUpActivity.this);
        ArrayAdapter cityadapter = new ArrayAdapter(getApplicationContext(), R.layout.simple_list_item_single_choice1, citylist);
        cityadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(cityadapter);
        spinnerCity.setSelection(cityadapter.getPosition(cityy));
        // spinnerCity.setEnabled(false);
        spinnerusertype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerusertype.getSelectedItem().toString().equalsIgnoreCase("consumer")) {
                    //buildingname.setVisibility(View.VISIBLE);
                    buildingheader.setVisibility(View.VISIBLE);
                    flat.setVisibility(View.VISIBLE);

                } else {
                   // buildingname.setVisibility(View.GONE);
                    flat.setVisibility(View.VISIBLE);

                    buildingheader.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });


        societyname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //     getItemfromBuilding(societyhash.get(societyname.getSelectedItem().toString()));

                socirtystr = societylist.get(0);
                if (socirtystr.equalsIgnoreCase("Add New Society")) {
                    startActivity(new Intent(getApplicationContext(), AddNewSociety.class));

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        countryspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countrystr = countryspin.getSelectedItem().toString();
                if (countrystr.equalsIgnoreCase("India"))
                {
//                    buildinglist.clear();
                    citylist.clear();
                    getCity("124", SignUpActivity.this);
                }
                else if (countrystr.equalsIgnoreCase("United States"))

                {
//                    buildinglist.clear();
                    citylist.clear();
                    getCity("120", SignUpActivity.this);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpActivity.super.onBackPressed();
            }
        });


        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!spinnerusertype.getSelectedItem().toString().equalsIgnoreCase("retailer")) {

                    if (socirtystr.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "society name can,t be empty ,Please enter society", Toast.LENGTH_LONG).show();

                    }
                   /* else if (buildingname.getSelectedItem().toString().isEmpty()) {

                        Toast.makeText(getApplicationContext(), "building name can't be empty ,Please enter building", Toast.LENGTH_LONG).show();

                    }*/
                    else if (flat.getText().toString().isEmpty()) {
                        flat.setError("Address can't be empty ,Please enter flat");

                    } else if (fnameedt.getText().toString().isEmpty()) {
                        fnameedt.setError("Name can't be empty ,Please enter Name");
                    } else if (emailedt.getText().toString().isEmpty()) {
                        emailedt.setError("Email can't be empty ,Please enter Email");
                    } else if (Mobileedt.getText().toString().isEmpty()) {
                        Mobileedt.setError("Mobile no name can't be empty ,Please enter Mobile no");
                    } else if (passet.getText().toString().isEmpty()) {
                        passet.setError("Password can't be empty ,Please enter Password");
                    } else if (passet.getText().toString().length() < 6) {
                        passet.setError("Password length should be atleast 6 characters or numbers");
                    } else {
                        Signupapi();
                    }
                } else {
                    if (societyname.getSelectedItem().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "society name can,t be empty ,Please enter society", Toast.LENGTH_LONG).show();

                    } else if (fnameedt.getText().toString().isEmpty()) {
                        fnameedt.setError("Name can't be empty ,Please enter Name");
                    } else if (emailedt.getText().toString().isEmpty()) {
                        emailedt.setError("Email can't be empty ,Please enter Email");
                    } else if (Mobileedt.getText().toString().isEmpty()) {
                        Mobileedt.setError("Mobile no name can't be empty ,Please enter Mobile no");
                    } else if (passet.getText().toString().isEmpty()) {
                        passet.setError("Password can't be empty ,Please enter Password");
                    } else {
                        Signupapi();
                    }
                }


            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void makeSubmitClick(View view) {
        // makeLogin();
        startActivity(new Intent(SignUpActivity.this, HomeActivity.class).putExtra("type", "Home"));
    }

    public void getItemfromSociety(String idcheck) {
        ArrayList<String> citynames = new ArrayList<>();
        ids1 = new ArrayList<String>();
        societylist = new ArrayList<String>();
        DatabaseManager.initializeInstance(new DatabaseHelper(getApplicationContext()));
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        SocietyDao pd = new SocietyDao(database, this);
        ArrayList<CatcodeHelper> listDatabasenewsource = pd.selectAll();

        Log.d("stringdatasizelist", String.valueOf(listDatabasenewsource.size()));
        for (int i = 0; i < listDatabasenewsourcee.size(); i++) {
            if (idcheck.equalsIgnoreCase(listDatabasenewsourcee.get(i).getSoceityid())) {

                societylist.add(listDatabasenewsource.get(i).getSocietynames());
                ids1.add(listDatabasenewsource.get(i).getSoceityid());
                societyhash.put(listDatabasenewsource.get(i).getSocietynames(), listDatabasenewsource.get(i).getSoceityid());
            }

            ArrayAdapter societyadaopter = new ArrayAdapter(getApplicationContext(), R.layout.simple_list_item_single_choice1, societylist);
            societyname.setAdapter(societyadaopter);


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

//            ArrayAdapter buildingadap = new ArrayAdapter(getApplicationContext(), R.layout.simple_list_item_single_choice1, buildinglist);
//            buildingname.setAdapter(buildingadap);


        }
    }


    public void getItemFromCity() {


        DatabaseManager.initializeInstance(new DatabaseHelper(getApplicationContext()));
        SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
        CityDAO pd = new CityDAO(database, this);
        listdatabasecity = pd.selectAll();

        Log.d("stringbuildinglist", String.valueOf(listdatabasecity.size()));
        for (int i = 0; i < listdatabasecity.size(); i++) {
     /*       for (int a = 0; a < listdatabasecity.size(); a++) {
                if (citylist.size() > 0) {
                    if (listdatabasecity.get(i).getCityname().toLowerCase().contains(citylist.get(a).substring(0,3).toLowerCase())) {
                        citylist.add(listdatabasecity.get(i).getCityname());
                        cityid.add(listdatabasecity.get(i).getCityid());
                        cityhash.put(listdatabasecity.get(i).getCityname(), listdatabasecity.get(i).getCityid());

                    }

                } else {*/
            citylist.add(listdatabasecity.get(i).getCityname());
/*                    cityid.add(listdatabasecity.get(i).getCityid());
                    cityhash.put(listdatabasecity.get(i).getCityname(), listdatabasecity.get(i).getCityid());

                }
            }*/
        }
        cityy = citylist.get(0);


    }

    public void Signupapi() {
        if (spinnerusertype.getSelectedItem().toString().equalsIgnoreCase("retailer")) {
            buildinstr = "";
            flatstr = flat.getText().toString();
        } else {
            buildinstr = "";
            // buildinstr = buildingname.getSelectedItem().toString();
            socirtystr = societylist.get(0);
            flatstr = flat.getText().toString();

        }


        final ProgressDialog mProgressBar = new ProgressDialog(SignUpActivity.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();
        mProgressBar.setCancelable(false);

        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mProgressBar.dismiss();
                        try {
                            String responses = DhanukaMain.SafeOBuddyDecryptUtils(response);
                            Log.d("responseSignup", responses);

                            JSONObject jsonObject = new JSONObject(responses);
                            int successtr = Integer.parseInt(jsonObject.getString("success"));
                            if (successtr == 1) {
                                JSONArray jsonArray = jsonObject.getJSONArray("newusercreation");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject newjson = jsonArray.getJSONObject(i);
                                    String returnmessage = newjson.getString("returnmessage");
                                    try {
                                        int uid = Integer.parseInt(returnmessage);
                                        Localdialog.show();
                                        textView.setText("User Register successfully");
                                        clickok.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                startActivity(new Intent(getApplicationContext(), LoginActivity.class).putExtra("username", Mobileedt.getText().toString()).putExtra("pass", passet.getText().toString()));
                                                finish();
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Localdialog.show();
                                        textView.setText(returnmessage);
                                        clickok.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Localdialog.dismiss();
                                            }
                                        });
                                    }
                                  /*  if (!returnmessage.equalsIgnoreCase("user already registered")) {

                                        Localdialog.show();
                                        textView.setText("User Register successfully");
                                        clickok.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                                finish();
                                            }
                                        });

                                    } else {
                                        Toast.makeText(getApplicationContext(), returnmessage, Toast.LENGTH_SHORT).show();
                                    }*/

                                }


                            } else {
                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
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


                try {
                    SharedPreferences prefs1 = getSharedPreferences("MORNING_PARCEL_GROCERY",
                            MODE_PRIVATE);
                    String cntry = prefs1.getString("cntry", "India");


                    String param = com.dhanuka.morningparcel.utils.AppUrls.strSignup + "&fname=" + fnameedt.getText().toString() + "&lname=" + "" + "&gender=" + "" + "&country=" + countrystr + "&city=" +spinnerCity.getSelectedItem().toString() + "&username=" + Mobileedt.getText().toString() + "&password=" + passet.getText().toString()
                            + "&email=" + emailedt.getText().toString() + "&bday=" + "01/01/1800" + "&mobileno=" + Mobileedt.getText().toString() + "&zip=" + txtzip.getText().toString() + "&securityq=" + "" + "&securityp=" + "" + "&address=" + flatstr +
                            "&alternatephone=" + "" + "&alternatemail=" + "" + "&security2=" + "" + "&securitya2=" + "test" + "&type=" + "0" + "&state=" + spinnerState.getSelectedItem().toString() + "&servicetype=" + spinnerusertype.getSelectedItem().toString() + "&contactid=" + "0" +
                            "&flatno=" + flatstr + "&building=" + buildinstr + "&society=" + socirtystr+"&companyid=549";
                    Log.e("beforeenc", param);
                    param = jkHelper.Encryptapi(param, SignUpActivity.this);
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
                        try {
                            String responses = DhanukaMain.SafeOBuddyDecryptUtils(response);
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
                                        //    String strString="";
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
                                spinnerCity.setSelection(cityadapter.getPosition(cityy));

                                spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        getsoceitylist("5555", spinnerCity.getSelectedItem().toString(), SignUpActivity.this);

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
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SignUpActivity.this);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("tokenval");
                    editor.commit();
                    String param = AppUrls.GET_REASON_LIST + "&contactid=" + "0" + "&type=" + type;
                    Log.d("Beforeencrption", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, SignUpActivity.this);
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

        Volley.newRequestQueue(ctx).add(postRequest);
    }

    public void getsoceitylist(String type, String str, Context ctx) {
        final ProgressDialog mProgressBar = new ProgressDialog(ctx);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();
        mProgressBar.setCancelable(false);

        societylist = new ArrayList<String>();

        //  societylist.add("Add New Society");

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

                            String responses = DhanukaMain.SafeOBuddyDecryptUtils(response);
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
                                socirtystr = societylist.get(0);
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
                            ArrayAdapter societyadaopter = new ArrayAdapter(getApplicationContext(), R.layout.simple_list_item_single_choice1, societylist);
                            societyname.setAdapter(societyadaopter);
                            societyname.setSelection(societyadaopter.getPosition(society));

                        } catch (Exception e) {
                            ArrayAdapter societyadaopter = new ArrayAdapter(getApplicationContext(), R.layout.simple_list_item_single_choice1, societylist);
                            societyname.setAdapter(societyadaopter);
                            societyname.setSelection(societyadaopter.getPosition(society));

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

                        // error
                        //   Log.e("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SignUpActivity.this);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("tokenval");
                    editor.commit();
                    String param = AppUrls.GET_REASON_LIST + "&contactid=" + "0" + "&type=" + type + "&val=" + str;
                    Log.d("Beforeencrption", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, SignUpActivity.this);
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

        Volley.newRequestQueue(ctx).add(postRequest);
    }

}