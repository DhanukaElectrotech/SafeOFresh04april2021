package com.dhanuka.morningparcel.activity.supplierorder;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
//import androidx.annotation.NonNull;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.dhanuka.morningparcel.Controllers.AppUrls;
import com.dhanuka.morningparcel.Helper.CatcodeHelper;
import com.dhanuka.morningparcel.Helper.ShoplistHelper;
import com.dhanuka.morningparcel.SqlDatabase.All_Item_Small;
import com.dhanuka.morningparcel.SqlDatabase.QueryExecutor;
import com.dhanuka.morningparcel.activity.HomeActivity;
import com.dhanuka.morningparcel.activity.Message;
import com.dhanuka.morningparcel.activity.Notification_Bell;
import com.dhanuka.morningparcel.activity.SplashActivity;
import com.dhanuka.morningparcel.activity.HomeStoreActivity;
import com.dhanuka.morningparcel.beans.CartProduct;
import com.dhanuka.morningparcel.customViews.LoginView;
import com.dhanuka.morningparcel.dialog.SelectShop;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.utils.NetworkMgr;
import com.dhanuka.morningparcel.Helper.ItemMasterhelper;
import com.dhanuka.morningparcel.Helper.Preferencehelper;

import com.dhanuka.morningparcel.ExampleApplication;
import com.dhanuka.morningparcel.R;

import com.dhanuka.morningparcel.SqlDatabase.DatabaseHelper;
import com.dhanuka.morningparcel.SqlDatabase.DatabaseManager;
import com.dhanuka.morningparcel.beans.ProfileBean;
import com.dhanuka.morningparcel.adapter.BannerAdapter;
import com.dhanuka.morningparcel.beans.FancyToast;
import com.dhanuka.morningparcel.beans.MainCatBean;
import com.dhanuka.morningparcel.customViews.KKViewPager;
import com.dhanuka.morningparcel.customViews.StoreView;
import com.dhanuka.morningparcel.utils.PermissionUtils;
import com.dhanuka.morningparcel.utils.log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SupplierHomeActivity extends BaseActivity {

    @Nullable
    @BindView(R.id.navigation)
    BottomNavigationView botnavcontrol;
    String shpId = "";
    @Nullable
    @BindView(R.id.kk_pager)
    KKViewPager mPager;
    @Nullable
    @BindView(R.id.dots_indicate)
    DotsIndicator dotsIndicator;
    @Nullable
    @BindView(R.id.categorylayout)
    LinearLayout categorylayout;
    @Nullable
    @BindView(R.id.rv_category)
    RecyclerView mRecyclerView;
//    @Nullable
//    @BindView(R.id.scrollbar)
//    ScrollView scrollbar;
//    @Nullable
//    @BindView(R.id.bannerll)
//    LinearLayout bannerll;

    @Nullable
    @BindView(R.id.msgheader)
    TextView msgheader;
    ArrayList<Integer> mListSliders;
    ArrayList<MainCatBean> mListCatMain;
    ArrayList<MainCatBean.CatBean> mListCat;
    BottomNavigationView.OnNavigationItemSelectedListener onbottomnavigationselect;
    ProfileBean mProfileBean;
    Preferencehelper preferencehelper;
    CatcodeHelper catcodeHelper;
    ArrayList<CatcodeHelper> bannerlist = new ArrayList<>();
    SharedPreferences.Editor mEditorL;
    SharedPreferences prefsL;
    String shopid;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {


        if (prefs.getPrefsUsercategory().equalsIgnoreCase("1058") || prefs.getPrefsUsercategory().equalsIgnoreCase("1062")) {


            prefs.setPrefsContactId(prefs.getPrefsTempContactid());
            startActivity(new Intent(getApplicationContext(), HomeStoreActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));

        } else {


            if (doubleBackToExitPressedOnce) {
                finishAffinity();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);


        }


    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_home_grocery;
    }

    SQLiteDatabase database;

    public void getItemsFromDb() {

        DatabaseManager.initializeInstance(new DatabaseHelper(getApplicationContext()));
        database = DatabaseManager.getInstance().openDatabase();
      /*  All_Item_Master pd = new All_Item_Master(database, this);
        masterlistitem= pd.selectAll();
        Log.d("listsize1",String.valueOf(masterlistitem.size()));
        masteradpter = new ItemMasterAdapter(masterlistitem,getApplicationContext());
        mastercontainer.setAdapter(masteradpter);

             getallmasterdate();
        }
*/

    }

    public void makeSearch(View v) {

        startActivity(new Intent(SupplierHomeActivity.this, SearchActivityNew.class));
        // startActivity(new Intent(HomeActivity.this, SearchActivity.class));
    }

    public void openPlaystore(View v) {
        try {
            //https://play.google.com/store/apps/details?id=nic.goi.aarogyasetu&hl=en_IN
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=nic.goi.aarogyasetu&hl=en_IN"));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Preferencehelper prefsOld;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor mEditor;
    Preferencehelper prefs1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_home_grocery, container_body);
        SharedPreferences prefs = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);
        Toast.makeText(getApplicationContext(),"supplierhome",Toast.LENGTH_LONG).show();
        //setContentView(R.layout.activity_home_grocery);
        ButterKnife.bind(this);
        PermissionUtils permissionModule = new PermissionUtils(SupplierHomeActivity.this);
        permissionModule.checkPermissions();
        ExampleApplication exampleApplication = new ExampleApplication();
        exampleApplication.initiateStripe(SupplierHomeActivity.this);
        exampleApplication.getEmpKey(SupplierHomeActivity.this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SupplierHomeActivity.this);
        mEditor = sharedPreferences.edit();
        strOpen = "Home";
        prefsOld = new Preferencehelper(this);
        mListCat = new ArrayList<>();
        mListCatMain = new ArrayList<>();
        mListSliders = new ArrayList<>();
        //   mListSliders.add(R.drawable.arogya);
        mListSliders.add(R.drawable.ban_one);
        mListSliders.add(R.drawable.ban_two);
        mListSliders.add(R.drawable.ban_three);
        mListSliders.add(R.drawable.ban_four);
        mListSliders.add(R.drawable.ban_five);

        preferencehelper = new Preferencehelper(SupplierHomeActivity.this);
        bannerlist = new ArrayList<>();
        if (sharedPreferences.getString("links", "-1").equalsIgnoreCase("-1")) {
            loadLinks();
        }
        getItemsFromDb();
        loadtaglines();
        msgheader.setText(preferencehelper.getPrefsTag3());
        msgheader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preferencehelper.getPrefsTag1Desc().isEmpty()) {


                } else {
                    JKHelper.openCommentDialog(SupplierHomeActivity.this, preferencehelper.getPrefsTag3Desc());

                }

            }
        });
        loadHistory();
//        scrollbar.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//
//                    bannerll.setVisibility(View.VISIBLE);
//                } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                    bannerll.setVisibility(View.GONE);
//
//
//                    // Touch was a simple tap. Do whatever.
//
//                } else {
//
//                    // Touch was a not a simple tap.
//
//                }
//
//
//                return true;
//            }
//        });

        shpId = prefs.getString("shopId", "");
        //   botnavcontrol.setOnNavigationItemSelectedListener(onbottomnavigationselect);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        mRecyclerView.setAdapter(new com.dhanuka.morningparcel.activity.supplierorder.adapter.MainCategoryAdapter(SupplierHomeActivity.this, mListCatMain));
        initKKViewPager(bannerlist);
        //    categorylayout.removeAllViews();
        if (NetworkMgr.isNetworkAvailable(SupplierHomeActivity.this)) {
            prefsL = getSharedPreferences("MORNING_PARCEL_GROCERY",
                    MODE_PRIVATE);
            mEditorL = prefsL.edit();
            mEditorL = prefs.edit();
            mEditorL.putString("Currency", "INR");
            mEditorL.commit();
            prefs1 = new Preferencehelper(getApplicationContext());
            loadCategories("Safe'O'Fresh");
            loadSubCategories("MorningParcel1");
            getallshopindia(prefs1.getPrefsContactId());
//            if (prefsL.getString("cntry", "").equalsIgnoreCase("India")) {

//
//                if (prefs1.getPrefsContactId().isEmpty()) {
//                    loadCategories("Safe'O'Fresh");
//                    //  getAllProducts(HomeActivity.this, "66738", "67263");
//                    getallshopindia("67263");
//
//
//                } else {
//                    loadCategories("Safe'O'Fresh");
//                    // getAllProducts(HomeActivity.this, "66738", prefs1.getPrefsContactId());
//
//                }
//
//            } else {
//                prefs1 = new Preferencehelper(getApplicationContext());
//                if (prefs.getString("shopId", "").isEmpty()) {
//                    getAllShop("67266");
//                    loadCategories(prefs.getString("shopName", ""));
//                    //  getAllProducts(HomeActivity.this, prefs.getString("shopId", ""), "67266");
//
//                } else {
//                    prefs1 = new Preferencehelper(getApplicationContext());
//                    getAllShop(prefs1.getPrefsContactId());
//                    loadCategories(prefs.getString("shopName", ""));
//                    //  getAllProducts(HomeActivity.this, prefs.getString("shopId", ""), prefs1.getPrefsContactId());
//                }
//
//            }
            //   if (prefsOld.getPrefsUsercategory().equalsIgnoreCase("1057")) {

            //    }
            //if (sharedPreferences.getString("","").isEmpty()){
            Log.e("khhjfdfghj", preferencehelper.getPrefsContactId() + " as");
            if (!TextUtils.isEmpty(preferencehelper.getPrefsContactId())) {
                Log.e("khhjfdfghj11", preferencehelper.getPrefsContactId() + " as");
                loadProfileData();
            }
            getCategoryCodes(preferencehelper.getPrefsContactId(), "116");
            // }

        } else {
            try {


                JSONObject jsonObject = new JSONObject(prefs.getString("resp", ""));

                mListCatMain = new Gson().fromJson(jsonObject.getString("returnds"), new TypeToken<List<MainCatBean>>() {
                }.getType());
                mRecyclerView.setAdapter(new com.dhanuka.morningparcel.activity.supplierorder.adapter.MainCategoryAdapter(SupplierHomeActivity.this, mListCatMain));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        dbManager = com.dhanuka.morningparcel.database.DatabaseManager.getInstance(SupplierHomeActivity.this);
        countView = (SupplierCartCountView) LayoutInflater.from(SupplierHomeActivity.this).inflate(R.layout.action_supplier_cart, null);
        loginView = (LoginView) LayoutInflater.from(SupplierHomeActivity.this).inflate(R.layout.action_login, null);
        storeView = (StoreView) LayoutInflater.from(SupplierHomeActivity.this).inflate(R.layout.action_shop, null);
        setCartCount();
        userName.setText(prefsOld.getPrefsEmail());
        SharedPreferences prefsToken = getSharedPreferences("MORNING_PARCEL_TOKEN",
                MODE_PRIVATE);
        SharedPreferences.Editor mEditorprefsToken = prefsToken.edit();

  /*      CommonHelper commonHelper= new CommonHelper();
        commonHelper.sendRegistrationToServer(HomeActivity.this,prefsToken.getString("token",""),preferencehelper.getPrefsContactId());
  */    /*  if (prefsOld.getBannerPrefs().equalsIgnoreCase("0")) {
            Utility.openBannerDialog(HomeActivity.this, container_body);
            prefsOld.setBannerPrefs("1");
        }*///startActivity(new Intent(My_DashboardMain.this,UpdateUOMActivity.class));


        //  appupdate();
        // startActivity(new Intent(HomeActivity.this, MainActivity.class));


    }

    public void appupdate() {
        String version = "";
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        new ForceUpdateAsync(version, SupplierHomeActivity.this).execute();


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public class ForceUpdateAsync extends AsyncTask<String, String, JSONObject> {

        private String latestVersion;
        private String currentVersion;
        private Context context;

        public ForceUpdateAsync(String currentVersion, Context context) {
            this.currentVersion = currentVersion;
            this.context = context;
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            try {
                latestVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + context.getPackageName() + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div.hAyfc:nth-child(4) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
                        .first()
                        .ownText();
                Log.e("latestversion", "---" + latestVersion);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return new JSONObject();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (latestVersion != null) {
                if (!currentVersion.equalsIgnoreCase(latestVersion)) {
                    // Toast.makeText(context,"update is available.",Toast.LENGTH_LONG).show();
                    if (!(context instanceof SplashActivity)) {
                        if (!((Activity) context).isFinishing()) {
                            showForceUpdateDialog();
                        }
                    }
                }
            }
            super.onPostExecute(jsonObject);
        }

        public void showForceUpdateDialog() {
            showDialogUpdate(SupplierHomeActivity.this, true);


        }

    }

    public void showDialogUpdate(final Context ctx, boolean isTrue) {
        final Dialog dialog = new Dialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_update_app);
        dialog.setCancelable(isTrue);
        final Button submit = (Button) dialog.findViewById(R.id.btnupdate);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ctx.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + getPackageName())));


            }
        });

        // Display the custom alert dialog on interface
        // dialog.show();


    }


    SupplierCartCountView countView;
    StoreView storeView;
    LoginView loginView;

    com.dhanuka.morningparcel.database.DatabaseManager dbManager;

    private void setCartCount() {
        countView.setCount(dbManager.getTotalQty());
    }


    @Override
    protected void onSideSliderClick() {

    }

    int totalCount = 0;

    private void initKKViewPager(ArrayList<CatcodeHelper> sliders) {
        totalCount = sliders.size();
//      mPager = (KKViewPager) findViewById(R.id.kk_pager);
        mPager.setAdapter(new BannerAdapter(SupplierHomeActivity.this, sliders));
        mPager.setAnimationEnabled(true);
        mPager.setFadeEnabled(true);
        mPager.setFadeFactor(0.6f);
        dotsIndicator.setViewPager(mPager);
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                currentPage = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        //  botnavcontrol.getMenu().getItem(1).setChecked(true);

        // botnavcontrol.setSelectedItemId(R.id.navigation_category);
        botnavcontrol.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.navigation_home:
                        //  startActivity(new Intent(HomeActivity.this, OptionChooserActivity.class));

                        break;

                    case R.id.navigation_category:
                        break;

                    case R.id.navigation_notifications:
                        break;

                    case R.id.navigation_cart:
                        startActivity(new Intent(SupplierHomeActivity.this, SupplierCartActivity.class));


                        break;

                    case R.id.navigation_profile:

                        if (new Preferencehelper(SupplierHomeActivity.this).getPrefsViewType() == 1 && new Preferencehelper(SupplierHomeActivity.this).getPrefsUsercategory().equalsIgnoreCase("1058")) {

                        }else{
                            startActivity(new Intent(SupplierHomeActivity.this, Notification_Bell.class));

                        }

                        break;


                }
                return true;
            }
        });

    }

    int currentPage = 0;


    // Auto start of viewpager
    Handler handler = new Handler();
    Runnable Update = new Runnable() {
        public void run() {
            if (currentPage == totalCount) {
                currentPage = 0;
            } else {
                currentPage++;
            }
            mPager.setCurrentItem(currentPage, true);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        try {
            botnavcontrol.setSelectedItemId(R.id.navigation_home);
            setCartCount();
            txt_set.setText("Safe'O'Fresh");
            backbtn_layout.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadCategories(final String donutid) {

        SharedPreferences prefs = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);
        SharedPreferences.Editor mEditor = prefs.edit();

        final ProgressDialog mProgressBar = new ProgressDialog(SupplierHomeActivity.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();

        // StringRequest postRequest = new StringRequest(Request.Method.POST, "http://mmthinkbiz.com/MobileService.aspx?method=GetCategoryDetail",
        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://mmthinkbiz.com/MobileService.aspx?method=GetCategoryDetail_Store",


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("responsive", response);
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                mEditor.putString("resp", response);
                                mEditor.commit();

                                mListCatMain = new Gson().fromJson(jsonObject.getString("returnds"), new TypeToken<List<MainCatBean>>() {
                                }.getType());
                                mRecyclerView.setAdapter(new com.dhanuka.morningparcel.activity.supplierorder.adapter.MainCategoryAdapter(SupplierHomeActivity.this, mListCatMain));

                            } else {

                                FancyToast.makeText(SupplierHomeActivity.this, "No Data Found, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
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
                        FancyToast.makeText(SupplierHomeActivity.this, "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params1 = new HashMap<String, String>();
                params1.put("storename", donutid);
                return params1;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(this).add(postRequest);


    }

    public void loadSubCategories(final String donutid) {

        SharedPreferences prefs = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);
        SharedPreferences.Editor mEditor = prefs.edit();

        final ProgressDialog mProgressBar = new ProgressDialog(SupplierHomeActivity.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();

        // StringRequest postRequest = new StringRequest(Request.Method.POST, "http://mmthinkbiz.com/MobileService.aspx?method=GetCategoryDetail",
        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://mmthinkbiz.com/MobileService.aspx?method=GetCategoryDetail_Store",


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            mEditor.putString("respSubCat", "");
                            mEditor.commit();

                            Log.d("responsiverespSubCat", response);
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                mEditor.putString("respSubCat", response);
                                mEditor.commit();


                            } else {

                                FancyToast.makeText(SupplierHomeActivity.this, "No Data Found, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
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
                        FancyToast.makeText(SupplierHomeActivity.this, "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params1 = new HashMap<String, String>();
                params1.put("storename", donutid);
                return params1;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(this).add(postRequest);


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        MenuItem item = menu.findItem(R.id.cart);
        MenuItem item1 = menu.findItem(R.id.shop);
        item.setActionView(countView);
        item1.setActionView(storeView);
        MenuItem login = menu.findItem(R.id.login);
        login.setActionView(loginView);
//        if (new Preferencehelper(HomeActivity.this).getPrefsViewType() == 1 && new Preferencehelper(HomeActivity.this).getPrefsUsercategory().equalsIgnoreCase("1058")) {
//            item.setVisible(false);
//
//        }
//        if (prefs.getPREFS_trialuser().equalsIgnoreCase("0")) {
//            item.setVisible(false);
//        } else {
//
//            item.setVisible(true);
//        }

        item.setVisible(true);
        try {
            if (prefs.getPrefsContactId().isEmpty()) {
                login.setVisible(true);
            } else {
                login.setVisible(false);

            }
            prefsL = getSharedPreferences("MORNING_PARCEL_GROCERY",
                    MODE_PRIVATE);
            mEditorL = prefsL.edit();
            if (prefsL.getString("cntry", "").equalsIgnoreCase("India")) {
                item1.setVisible(false);
            } else {
                item1.setVisible(true);

            }
        } catch (Exception e) {
            login.setVisible(true);

        }
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.reachUs).setVisible(false);

        if (prefsL.getString("cntry", "India").equalsIgnoreCase("India")) {
            nav_Menu.findItem(R.id.history_wallet).setVisible(true);
        } else {
            nav_Menu.findItem(R.id.history_wallet).setVisible(true);

        }

        return true;
    }

//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_cart, menu);
//        MenuItem item = menu.findItem(R.id.cart);
//        MenuItem item1 = menu.findItem(R.id.shop);
//        item.setActionView(countView);
//        item1.setActionView(storeView);
//        MenuItem login = menu.findItem(R.id.login);
//        login.setActionView(loginView);
//        if (new Preferencehelper(SupplierHomeActivity.this).getPrefsViewType() == 1 && new Preferencehelper(SupplierHomeActivity.this).getPrefsUsercategory().equalsIgnoreCase("1058")) {
//            item.setVisible(false);
//
//        }
//        if (prefs.getPREFS_trialuser().equalsIgnoreCase("0")) {
//            item.setVisible(false);
//        } else {
//
//            item.setVisible(true);
//        }
//
//
//        item.setVisible(true);
//        try {
//            if (prefs.getPrefsContactId().isEmpty()) {
//                login.setVisible(true);
//            } else {
//                login.setVisible(false);
//
//            }
//            prefsL = getSharedPreferences("MORNING_PARCEL_GROCERY",
//                    MODE_PRIVATE);
//            mEditorL = prefsL.edit();
//            if (prefsL.getString("cntry", "").equalsIgnoreCase("India")) {
//                item1.setVisible(false);
//            } else {
//                item1.setVisible(true);
//
//            }
//        } catch (Exception e) {
//            login.setVisible(true);
//
//        }
//        navigationView = (NavigationView) findViewById(R.id.navigation_view);
//        Menu nav_Menu = navigationView.getMenu();
//        nav_Menu.findItem(R.id.reachUs).setVisible(false);
//
//        if (prefsL.getString("cntry", "India").equalsIgnoreCase("India")) {
//            nav_Menu.findItem(R.id.history_wallet).setVisible(true);
//        } else {
//            nav_Menu.findItem(R.id.history_wallet).setVisible(true);
//
//        }
//
//
//        return true;
//    }

    private void loadProfileData() {

        final ProgressDialog mProgressBar = new ProgressDialog(SupplierHomeActivity.this);
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
                            String responses = jkHelper.Decryptapi(response, SupplierHomeActivity.this);
                            JSONObject jsonObject = new JSONObject(responses);
                            int success = jsonObject.getInt("success");
                            log.e("success" + success);
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

                                    String currency = "";
                                    SharedPreferences prefs1 = getSharedPreferences("MORNING_PARCEL_GROCERY",
                                            MODE_PRIVATE);

//

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
                                Gson gson = new Gson();
                                mProfileBean = gson.fromJson(jsonArray.get(0).toString(), ProfileBean.class);
                                if (TextUtils.isEmpty((mProfileBean.getFirstname()))) {
                                    userName.setText("Hi User");
                                } else {
                                    userName.setText("Hi " + mProfileBean.getFirstname() + " " + mProfileBean.getLastname());


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
                        mProgressBar.dismiss();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                try {

                    String param = getString(R.string.URL_GET_PROFILE) + "&contactid=" + preferencehelper.getPrefsContactId() + "&userid=" + "" + "&Ntok=" + prefs.getPREFS_trialuser();

                    //    String param = getString(R.string.URL_GET_PROFILE) + "&contactid=" + preferencehelper.getPrefsContactId() + "&userid=" + "";
                    Log.d("Beforeencrptionprofile", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, SupplierHomeActivity.this);
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

    int mp = 0;

    public void getAllProducts(Context ctx, String shopId, String contactid) {
        mp = 0;

        SharedPreferences prefs = ctx.getSharedPreferences("MORNING_PARCEL_GROCERY",
                ctx.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = prefs.edit();

        ArrayList<ItemMasterhelper> masterlist = new ArrayList<>();

        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("Response393", response);

                        String res = response;
                        try {

                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, SupplierHomeActivity.this);

                            mEditor.putString("resp1", responses);
                            mEditor.commit();

                            JSONObject jsonObject = new JSONObject(responses);
                            JSONArray jsonArray = jsonObject.getJSONArray("returnds");
                            if (jsonObject.getInt("success") == 1) {
                                dbManager = com.dhanuka.morningparcel.database.DatabaseManager.getInstance(ctx);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject newjson = jsonArray.getJSONObject(i);
                                    String ItemID = newjson.getString("ItemID");
                                    String ItemName = newjson.getString("ItemName");
                                    String GroupID = newjson.getString("GroupID");
                                    String MRP = newjson.getString("MRP");
                                    String SaleUOM = newjson.getString("SaleUOM");
                                    String SaleRate = newjson.getString("SaleRate");
                                    String IsDeal = newjson.getString("IsDeal");
                                    String IsNewListing = newjson.getString("IsNewListing");
                                    String RepeatOrder = newjson.getString("RepeatOrder");
                                    String ItemImage = newjson.getString("ItemImage");
                                    ItemMasterhelper v = new ItemMasterhelper();
                                    v.setItemID(ItemID);
                                    v.setItemName(ItemName);
                                    v.setIsDeal(IsDeal);
                                    v.setGroupID(GroupID);
                                    v.setMRP(MRP);
                                    v.setIsNewListing(IsNewListing);
                                    v.setSaleUOM(SaleUOM);
                                    v.setSaleRate(SaleRate);
                                    v.setItemImage(ItemImage);
                                    v.setRepeatOrder(RepeatOrder);
                                    masterlist.add(v);
                                    //Log.d("masterlist", String.valueOf(masterlist.size()));

                                    mp = i;
                                  /*  if (dbManager != null) {
                                        // mProgressBar.dismiss();
                                        Log.e("HEHHE", "outside the db Condition");
                                        com.dhanuka.morningparcel.SqlDatabase.DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                                            @Override
                                            public void run(SQLiteDatabase database) {
                                                Log.e("HEHHE", "inside the db Condition");
                                                All_Item_Master masterdao = new All_Item_Master(database, HomeActivity.this);
                                                ArrayList<ItemMasterhelper> list = masterlist;
                                                Log.e("getRowCount", masterdao.getRowCount() + "");
                                                   if (mp == 0) {

                                                masterdao.deleteAll();

                                                }
                                                masterdao.insertSingleProduct(v);
                                                // Log.d("Savedataindb", String.valueOf(list.size()));
                                                //Toast.makeText(SearchActivity.this, "Data saved", Toast.LENGTH_LONG).show();

                                            }
                                        });
                                    } else {
                                        Log.e("HEHHE", "22 the db Condition");

                                    }*/


                                }


                            }

                            // new SaveToDB(masterlist, HomeActivity.this).execute();


                        } catch (Exception e) {
                            // mProgressBar.dismiss();

                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        // error
                        //   Log.e("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Preferencehelper prefs;
                prefs = new Preferencehelper(ctx);
                SharedPreferences prefs1 = ctx.getSharedPreferences("MORNING_PARCEL_GROCERY",
                        ctx.MODE_PRIVATE);


                Map<String, String> params = new HashMap<String, String>();
                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);

                    String param = AppUrls.GET_ITEM_MASTER_URL + "&ContactID=" + contactid + "&Type=" + "28" + "&SupplierID=" + shopId;
                    Log.e("BEFORE_PRODUCTS", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, SupplierHomeActivity.this);
                    params.put("val", finalparam);
                    Log.d("afterencrptionmaster", finalparam);
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

    public void getAllShop(String contactid) {
        final ProgressDialog mProgressBar = new ProgressDialog(SupplierHomeActivity.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.setCancelable(false);
        mProgressBar.show();
        SharedPreferences prefs = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);
        SharedPreferences.Editor mEditor = prefs.edit();


        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        mProgressBar.dismiss();

                        Log.e("ResponseSHOPS", response);
                        String res = response;
                        JKHelper jkHelper = new JKHelper();
                        String responses = jkHelper.Decryptapi(response, SupplierHomeActivity.this);
                        mEditor.putString("shopss", responses);
                        mEditor.commit();
                        if (prefs.getString("shopId", "").isEmpty()) {
                            //  if (prefsOld.getPrefsUsercategory().equalsIgnoreCase("1057")) {
                            new SelectShop(SupplierHomeActivity.this, 1, false).show();
                            //  Utility.selectShop(HomeActivity.this, 1, false);
                            // }
                        }

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
                    String contctId = "";
                    if (prefsOld.getPrefsContactId().isEmpty()) {
                        contctId = "7777";
                    } else {
                        contctId = prefsOld.getPrefsContactId();
                    }

                    String param = AppUrls.URL_FILL_CONSIGNEE + "&contactid=" + contactid + "&CId=" + "1";
                    Log.d("Beforeencrption", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, SupplierHomeActivity.this);
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
        Volley.newRequestQueue(getApplicationContext()).add(postRequest);
    }

    public void getCategoryCodes(final String contactid, final String types) {

        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        JKHelper jkHelper = new JKHelper();
                        String responses = jkHelper.Decryptapi(response, SupplierHomeActivity.this);
                        Log.e("Response", responses);

                        try {
                            bannerlist = new ArrayList<>();
                            JSONObject jsonObject = new JSONObject(responses);
                            int success = jsonObject.getInt("success");
                            log.e("success" + success);
                            if (success == 1) {

                                log.e("in success one ");
                                JSONArray jsonArray = jsonObject.getJSONArray("returnds");
                                final ArrayList<CatcodeHelper> dataList = new ArrayList<CatcodeHelper>();


                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject loopObjects = jsonArray.getJSONObject(i);
                                    String bannerurl = loopObjects.getString("Val1");
                                    String clickurl = loopObjects.getString("Val3");
                                    catcodeHelper = new CatcodeHelper();
                                    catcodeHelper.setBannerurl(bannerurl);
                                    catcodeHelper.setClickurl(clickurl);

                                    bannerlist.add(catcodeHelper);


                                }

                                mPager.setAdapter(new BannerAdapter(SupplierHomeActivity.this, bannerlist));
                                mPager.setAnimationEnabled(true);
                                mPager.setFadeEnabled(true);
                                mPager.setFadeFactor(0.6f);
                                dotsIndicator.setViewPager(mPager);
                                Timer swipeTimer = new Timer();
                                swipeTimer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        handler.post(Update);
                                    }
                                }, 3000, 3000);

//                                mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//                                    @Override
//                                    public void onPageScrolled(int i, float v, int i1) {
//
//                                    }
//
//                                    @Override
//                                    public void onPageSelected(int i) {
//                                        currentPage = i;
//                                    }
//
//                                    @Override
//                                    public void onPageScrollStateChanged(int i) {
//
//                                    }
//                                });

                            } else if (success == 0) {
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error

                        Message.message(SupplierHomeActivity.this, "Failed To Retrieve Data");
                        //   Log.e("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                try {

                    if (prefsL.getString("cntry", "India").equalsIgnoreCase("India")) {

                    } else {

                    }





                    mEditorL = prefsL.edit();
                    mEditorL.putString("cntry", "India");
                    mEditorL.commit();
                    if (prefsL.getString("cntry", "").equalsIgnoreCase("India")) {


                        //   String param = getString(R.string.URL_GET_CATEGORY_CODE) + "&contactid=" + "67263" + "&type=" + types;
                        String param = getString(R.string.URL_GET_CATEGORY_CODE) + "&contactid=" + "66738" + "&type=" + types;
                        Log.d("Beforeencrption1", param);
                        JKHelper jkHelper = new JKHelper();
                        String finalparam = jkHelper.Encryptapi(param, SupplierHomeActivity.this);
                        params.put("val", finalparam);
                        Log.d("afterencrption1", finalparam);
                        return params;
                    } else {
                        //String param = getString(R.string.URL_GET_CATEGORY_CODE) + "&contactid=" + "0" + "&type=" + types;
                        String param = getString(R.string.URL_GET_CATEGORY_CODE) + "&contactid=" + shpId + "&type=" + types;
                        Log.d("Beforeencrption2", param);
                        JKHelper jkHelper = new JKHelper();
                        String finalparam = jkHelper.Encryptapi(param, SupplierHomeActivity.this);
                        params.put("val", finalparam);
                        Log.d("afterencrption2", finalparam);
                        return params;

                    }


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

        Volley.newRequestQueue(SupplierHomeActivity.this).add(postRequest);


    }

    public void loadLinks() {

        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        JKHelper jkHelper = new JKHelper();
                        String responses = jkHelper.Decryptapi(response, SupplierHomeActivity.this);
                        Log.e("Response", responses);

                        try {
                            JSONObject jsonObject = new JSONObject(responses);
                            int success = jsonObject.getInt("success");
                            log.e("success" + success);
                            if (success == 1) {

                                log.e("in success one ");
                                JSONArray jsonArray = jsonObject.getJSONArray("returnds");


                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    if (jsonObject1.getString("CodeDescription").equalsIgnoreCase("ABOUT THIS APP")) {

                                        mEditor.putString("abt", jsonObject1.getString("Val1"));
                                        if (jsonObject1.getString("Val1").isEmpty()) {
                                            nav_Menu.findItem(R.id.about).setVisible(false);

                                        } else {
                                            nav_Menu.findItem(R.id.about).setVisible(false);


                                        }
                                    } else if (jsonObject1.getString("CodeDescription").equalsIgnoreCase("HELP")) {
                                        mEditor.putString("hlp", jsonObject1.getString("Val1"));
                                        if (jsonObject1.getString("Val1").isEmpty()) {
                                            nav_Menu.findItem(R.id.help).setVisible(false);

                                        } else {
                                            nav_Menu.findItem(R.id.help).setVisible(false);

                                        }
                                    } else if (jsonObject1.getString("CodeDescription").equalsIgnoreCase("HOW TO SHOP")) {
                                        mEditor.putString("hts", jsonObject1.getString("Val1"));
                                        if (jsonObject1.getString("Val1").isEmpty()) {
                                            nav_Menu.findItem(R.id.hts).setVisible(false);

                                        } else {
                                            nav_Menu.findItem(R.id.hts).setVisible(false);

                                        }
                                    } else if (jsonObject1.getString("CodeDescription").equalsIgnoreCase("LEGAL")) {
                                        mEditor.putString("lgl", jsonObject1.getString("Val1"));
                                        if (jsonObject1.getString("Val1").isEmpty()) {
                                            nav_Menu.findItem(R.id.legal).setVisible(false);

                                        } else {
                                            nav_Menu.findItem(R.id.legal).setVisible(false);


                                        }
                                    } else if (jsonObject1.getString("CodeDescription").equalsIgnoreCase("OUR MISSION")) {
                                        mEditor.putString("msn", jsonObject1.getString("Val1"));
                                        if (jsonObject1.getString("Val1").isEmpty()) {
                                            nav_Menu.findItem(R.id.msn).setVisible(false);

                                        } else {
                                            nav_Menu.findItem(R.id.msn).setVisible(false);

                                        }

                                    } else if (jsonObject1.getString("CodeDescription").equalsIgnoreCase("PRIVACY POLICY")) {
                                        mEditor.putString("pp", jsonObject1.getString("Val1"));
                                        if (jsonObject1.getString("Val1").isEmpty()) {
                                            nav_Menu.findItem(R.id.policy).setVisible(false);

                                        } else {
                                            nav_Menu.findItem(R.id.policy).setVisible(false);

                                        }
                                    } else if (jsonObject1.getString("CodeDescription").equalsIgnoreCase("TERMS OF SERVICE")) {
                                        mEditor.putString("toc", jsonObject1.getString("Val1"));
                                        if (jsonObject1.getString("Val1").isEmpty()) {
                                            nav_Menu.findItem(R.id.terms).setVisible(false);

                                        } else {
                                            nav_Menu.findItem(R.id.terms).setVisible(true);
                                        }
                                    } else if (jsonObject1.getString("CodeDescription").equalsIgnoreCase("REFUNDS AND CANCELLATIONS")) {
                                        mEditor.putString("refund", jsonObject1.getString("Val1"));
                                        if (jsonObject1.getString("Val1").isEmpty()) {
                                            nav_Menu.findItem(R.id.refund).setVisible(false);

                                        } else {
                                            nav_Menu.findItem(R.id.refund).setVisible(true);
                                        }
                                    } else if (jsonObject1.getString("CodeDescription").contains("CONTACT US")) {
                                        mEditor.putString("contactus", jsonObject1.getString("Val1"));
                                        if (jsonObject1.getString("Val1").isEmpty()) {
                                            nav_Menu.findItem(R.id.contactus).setVisible(false);

                                        } else {
                                            nav_Menu.findItem(R.id.contactus).setVisible(true);
                                        }
                                    }
                                    mEditor.commit();

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
                        // error

                        Message.message(SupplierHomeActivity.this, "Failed To Retrieve Data");
                        //   Log.e("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                try {
                    String param = getString(R.string.URL_GET_CATEGORY_CODE) + "&contactid=" + "0" + "&type=117";
                    Log.d("Beforeencrption", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, SupplierHomeActivity.this);
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

        Volley.newRequestQueue(SupplierHomeActivity.this).add(postRequest);


    }

    public void loadtaglines() {
        Preferencehelper prefs = new Preferencehelper(getApplicationContext());

        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        JKHelper jkHelper = new JKHelper();
                        String responses = jkHelper.Decryptapi(response, SupplierHomeActivity.this);
                        Log.e("Response", responses);

                        try {
                            JSONObject jsonObject = new JSONObject(responses);
                            int success = jsonObject.getInt("success");
                            log.e("success" + success);
                            if (success == 1) {

                                log.e("in success one ");
                                JSONArray jsonArray = jsonObject.getJSONArray("returnds");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject newjsonobject = jsonArray.getJSONObject(i);
                                    if (i == 0) {
                                        String tagline1 = jsonArray.getJSONObject(0).getString("CodeDescription");
                                        String tagdescription1 = jsonArray.getJSONObject(0).getString("Val1");
                                        prefs.setprefstag1(tagline1);
                                        prefs.setPrefsTag1Desc(tagdescription1);

                                    }
                                    if (i == 1) {
                                        String tagline2 = jsonArray.getJSONObject(1).getString("CodeDescription");
                                        String tagdescription2 = jsonArray.getJSONObject(1).getString("Val1");
                                        prefs.setprefstag2(tagline2);
                                        prefs.setprefstag2Desc(tagdescription2);

                                    }
                                    if (i == 2) {
                                        String tagline3 = jsonArray.getJSONObject(2).getString("CodeDescription");
                                        String tagdescription3 = jsonArray.getJSONObject(2).getString("Val1");
                                        prefs.setprefstag3(tagline3);
                                        prefs.setprefstag3Desc(tagdescription3);

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
                        // error

                        Message.message(SupplierHomeActivity.this, "Failed To Retrieve Data");
                        //   Log.e("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                try {
                    String param = getString(R.string.URL_GET_CATEGORY_CODE) + "&contactid=" + "0" + "&type=118";
                    Log.d("Beforeencrption", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, SupplierHomeActivity.this);
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

        Volley.newRequestQueue(SupplierHomeActivity.this).add(postRequest);


    }

    public int getDefaultSimmm(Context context) {

        Object tm = context.getSystemService(Context.TELEPHONY_SERVICE);
        Method method_getDefaultSim;
        int defaultSimm = -1;
        try {
            method_getDefaultSim = tm.getClass().getDeclaredMethod("getDefaultSim");
            method_getDefaultSim.setAccessible(true);
            defaultSimm = (Integer) method_getDefaultSim.invoke(tm);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Method method_getSmsDefaultSim;
        int smsDefaultSim = -1;
        try {
            method_getSmsDefaultSim = tm.getClass().getDeclaredMethod("getSmsDefaultSim");
            smsDefaultSim = (Integer) method_getSmsDefaultSim.invoke(tm);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return smsDefaultSim;
    }

    public void getallshopindia(String contactid) {


        final ProgressDialog mProgressBar = new ProgressDialog(SupplierHomeActivity.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.setCancelable(false);
        mProgressBar.show();
        SharedPreferences prefs = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);
        SharedPreferences.Editor mEditor = prefs.edit();


        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        mProgressBar.dismiss();

                        Log.e("ResponseSHOPS", response);
                        String res = response;
                        JKHelper jkHelper = new JKHelper();
                        String responses = jkHelper.Decryptapi(response, SupplierHomeActivity.this);
                        mEditor.putString("shopss", responses);
                        mEditor.commit();
                        try {
                            JSONObject jsonObject = new JSONObject(prefs.getString("shopss", ""));
                            int success = jsonObject.getInt("success");
                            //  Log.e("success" + success);
                            Log.d("success", String.valueOf(+success));
                            if (success == 1) {

                                JSONArray jsonArray = jsonObject.getJSONArray("AssignVehicle");


                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject singleObj = jsonArray.getJSONObject(i);
                                    String consignee = singleObj.getString("BranchName");
                                    String deviceId = singleObj.getString("BranchId");
                                    String Alartphonenumber = singleObj.getString("Alartphonenumber");
                                    String PhonePe = singleObj.getString("PhonePe");
                                    String PaytmNumber = singleObj.getString("PaytmNumber");
                                    String GooglePay = singleObj.getString("GooglePay");
                                    String Currency = singleObj.getString("Currency");
                                    String DeliveryCharge = singleObj.getString("DeliveryCharge");
                                    String ServiceFees = singleObj.getString("ServiceFees");
                                    String CheckOutMessage = singleObj.getString("CheckOutMessage");
                                    String MaxOrderAmt = singleObj.getString("MaxOrderAmt");
                                    String MinOrderAmt = singleObj.getString("MinOrderAmt");
                                    String filepath = singleObj.getString("filepath");
                                    String ImageName = singleObj.getString("ImageName");
                                    String Distance = singleObj.getString("Distance");
                                    String City = singleObj.getString("City");
                                    String Discount = singleObj.getString("Discount");
                                    String Tax = singleObj.getString("Tax");


                                    ShoplistHelper v = new ShoplistHelper();
                                    v.setAlartphonenumber(Alartphonenumber);
                                    v.setPhonePe(PhonePe);
                                    v.setMinOrderAmt(MinOrderAmt);
                                    v.setMaxOrderAmt(MaxOrderAmt);
                                    v.setPaytmNumber(PaytmNumber);
                                    v.setCheckOutMessage(CheckOutMessage);
                                    v.setGooglePay(GooglePay);
                                    v.setShopid(deviceId);
                                    v.setShopname(consignee);
                                    v.setCurrency(Currency);
                                    v.setDeliveryCharge(DeliveryCharge);
                                    v.setServiceFees(ServiceFees);
                                    v.setImageName(ImageName);
                                    v.setFilepath(filepath);
                                    v.setDiscount(Discount);
                                    v.setDistance(Distance);
                                    v.setCity(City);
                                    v.setTax(Tax);


                                    SharedPreferences prefs = getSharedPreferences("MORNING_PARCEL_GROCERY",
                                            MODE_PRIVATE);
                                    SharedPreferences.Editor mEditor = prefs.edit();
                                    mEditor.putString("shopId", deviceId);
                                    mEditor.putString("shopName", consignee);
                                    mEditor.putString("DeliveryCharge", DeliveryCharge);
                                    mEditor.putString("ServiceFees", ServiceFees);
                                    mEditor.putString("CheckOutMessage", CheckOutMessage);
                                    mEditor.putString("MaxOrderAmt", MaxOrderAmt);
                                    mEditor.putString("Currency", "INR");
                                    mEditor.putString("MinOrderAmt", MinOrderAmt);
                                    mEditor.putString("discount", Discount);
                                    mEditor.putString("Tax", Tax);
                                    mEditor.commit();


                                }


                                //Message.message(ctx, "Data fetched Successfuly");
                            } else if (success == 0) {
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (prefs.getString("shopId", "").isEmpty()) {
                            //  if (prefsOld.getPrefsUsercategory().equalsIgnoreCase("1057")) {
                            new SelectShop(SupplierHomeActivity.this, 1, false).show();
                            //  Utility.selectShop(HomeActivity.this, 1, false);
                            // }
                        }

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
                    String contctId = "";
                    if (prefsOld.getPrefsContactId().isEmpty()) {
                        contctId = "7777";
                    } else {
                        contctId = prefsOld.getPrefsContactId();
                    }

                    String param = AppUrls.URL_FILL_CONSIGNEE + "&contactid=" + contactid + "&CId=" + "1";
                    Log.d("Beforeencrption", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, SupplierHomeActivity.this);
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
        Volley.newRequestQueue(getApplicationContext()).add(postRequest);
    }

    public class SaveToDB extends AsyncTask<String, String, JSONObject> {
        ProgressDialog mProgressBar = new ProgressDialog(SupplierHomeActivity.this);


        private Context context;
        ArrayList<ItemMasterhelper> mListItems;

        public SaveToDB(ArrayList<ItemMasterhelper> mListItem, Context context) {
            this.mListItems = mListItem;
            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar = new ProgressDialog(SupplierHomeActivity.this);
            mProgressBar.setTitle("Safe'O'Fresh");
            mProgressBar.setMessage("saving...");
            mProgressBar.setCancelable(false);
            mProgressBar.show();


        }

        @Override
        protected JSONObject doInBackground(String... params) {

            try {
                if (dbManager != null) {
                    // mProgressBar.dismiss();
                    Log.e("HEHHE", "outside the db Condition");
                    com.dhanuka.morningparcel.SqlDatabase.DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                        @Override
                        public void run(SQLiteDatabase database) {
                            Log.e("HEHHE", "inside the db Condition");
                            ArrayList<ItemMasterhelper> list = mListItems;
                            All_Item_Small masterdao = new All_Item_Small(database, SupplierHomeActivity.this);
                            masterdao.deleteAll();
                            String insert = "";

                            for (int i = 0; i < mListItems.size(); i++) {

                                masterdao.insertSingleProduct(mListItems.get(i));
                            }
//                            masterdao.setTransactionSuccessful();
//                            masterdao.endTransaction();

                            Log.e("getRowCount", masterdao.getRowCount() + "");
                            // if (mp == 0) {


                            // }

                            // Log.d("Savedataindb", String.valueOf(list.size()));
                            //Toast.makeText(SearchActivity.this, "Data saved", Toast.LENGTH_LONG).show();

                        }
                    });
                } else {
                    Log.e("HEHHE", "22 the db Condition");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new JSONObject();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            mProgressBar.dismiss();
        }


    }

    public void loadHistory() {


        final ProgressDialog mProgressBar = new ProgressDialog(SupplierHomeActivity.this);
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

                            String responses = jkHelper.Decryptapi(response, SupplierHomeActivity.this);

                            Log.d("responsive", responses);
                            JSONObject jsonObject = new JSONObject(responses);
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {

                                JSONArray newjsonarr = jsonObject.getJSONArray("returnds");


                                // Log.d("cartproductprint",cartProduct.getMRP()+"\n"+product.getMRP()+"\n");

                                dbManager = com.dhanuka.morningparcel.database.DatabaseManager.getInstance(getApplicationContext());


                                for (int a = 0; a < newjsonarr.length(); a++) {
                                    JSONObject newjsonobj = newjsonarr.getJSONObject(a);

                                    CartProduct cartProduct = new CartProduct();
                                    cartProduct.setCompanyid(newjsonobj.getString("CompanyID"));
                                    JSONArray oreridarr = newjsonobj.getJSONArray("Items");
                                    dbManager.deleteAll();
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

                                    }

                                }


                            } else {

                                dbManager.deleteAll();

                                FancyToast.makeText(SupplierHomeActivity.this, "No Data Found, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
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

                        FancyToast.makeText(SupplierHomeActivity.this, "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


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
                    shopid = mData.getString("shopId", "1");

                    String param = getString(R.string.GET_ORDER_DETAIL) + "&type=" + "500" + "&tdate=" + mTodayDate + "&fdate=" + mTodayDate + "&CID=" + preferencehelper.getPrefsContactId() + "&storeId=" + shopid + "&fdate=" + shopid + "&tdate=";
                    Log.d("Beforeencrptionhistory", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, SupplierHomeActivity.this);
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


}
