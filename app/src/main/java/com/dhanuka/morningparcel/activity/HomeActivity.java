package com.dhanuka.morningparcel.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
//import androidx.annotation.NonNull;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.dhanuka.morningparcel.Controllers.AppUrls;
import com.dhanuka.morningparcel.Helper.CatcodeHelper;
import com.dhanuka.morningparcel.Helper.DbImageMaster;
import com.dhanuka.morningparcel.Helper.ImageLoadingUtils;
import com.dhanuka.morningparcel.Helper.ImageMasterDAO;
import com.dhanuka.morningparcel.Helper.ImageUploadDAO;
import com.dhanuka.morningparcel.Helper.ShoplistHelper;
import com.dhanuka.morningparcel.SqlDatabase.All_Item_Small;
import com.dhanuka.morningparcel.SqlDatabase.QueryExecutor;
import com.dhanuka.morningparcel.beans.CartProduct;
import com.dhanuka.morningparcel.customViews.LoginView;
import com.dhanuka.morningparcel.dialog.SelectShop;
import com.dhanuka.morningparcel.utils.DbImageUpload;
import com.dhanuka.morningparcel.utils.ImageUploadService;
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
import com.dhanuka.morningparcel.adapter.MainCategoryAdapter;
import com.dhanuka.morningparcel.beans.FancyToast;
import com.dhanuka.morningparcel.beans.MainCatBean;
import com.dhanuka.morningparcel.customViews.CartCountView;
import com.dhanuka.morningparcel.customViews.KKViewPager;
import com.dhanuka.morningparcel.customViews.StoreView;
import com.dhanuka.morningparcel.utils.PermissionUtils;
import com.dhanuka.morningparcel.utils.log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.filter.entity.ImageFile;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

import static com.vincent.filepicker.activity.BaseActivity.IS_NEED_FOLDER_LIST;
import static com.vincent.filepicker.activity.ImagePickActivity.IS_NEED_CAMERA;

public class HomeActivity extends BaseActivity {

    @Nullable
    @BindView(R.id.navigation)
    BottomNavigationView botnavcontrol;
    String shpId = "";
    String oid = "0";
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


    String strOpen = "Home";

    private final int REQUEST_CODE = 1080;
    int asREQUEST_CODE = 1030;
    File mFile1;
    private String PREFS_FILE_PATH = "capture_file_path";
    String mImagePathDataBase;
    String mImageNameDataBase;
    String mCurrentTimeDataBase;
    private boolean addedToMasterTable = false;
    int masterDataBaseId;
    private ImageLoadingUtils utils;

    Dialog CameraDialog;

    private String filePath;
    String lastRowMaterTable;
    ArrayList<String> mListLastRows = new ArrayList<>();

    @Override
    public void onBackPressed() {


        if (prefs.getPrefsUsercategory().equalsIgnoreCase("1058") || prefs.getPrefsUsercategory().equalsIgnoreCase("1062")) {


            prefs.setPrefsContactId(prefs.getPrefsTempContactid());
            finish();
            super.onBackPressed();

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

        startActivity(new Intent(HomeActivity.this, SearchActivityNew.class));
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

        //setContentView(R.layout.activity_home_grocery);
        ButterKnife.bind(this);
        PermissionUtils permissionModule = new PermissionUtils(HomeActivity.this);
        permissionModule.checkPermissions();
        ExampleApplication exampleApplication = new ExampleApplication();
        exampleApplication.initiateStripe(HomeActivity.this);
        exampleApplication.getEmpKey(HomeActivity.this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
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

        preferencehelper = new Preferencehelper(HomeActivity.this);
        bannerlist = new ArrayList<>();
        Toast.makeText(getApplicationContext(), String.valueOf(getDefaultSimmm(getApplicationContext())), Toast.LENGTH_LONG).show();
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
                    JKHelper.openCommentDialog(HomeActivity.this, preferencehelper.getPrefsTag3Desc());

                }

            }
        });
        loadHistory();
        shpId = prefs.getString("shopId", "");
        //   botnavcontrol.setOnNavigationItemSelectedListener(onbottomnavigationselect);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        mRecyclerView.setAdapter(new MainCategoryAdapter(HomeActivity.this, mListCatMain));
        initKKViewPager(bannerlist);
        //    categorylayout.removeAllViews();
        if (NetworkMgr.isNetworkAvailable(HomeActivity.this)) {
            prefsL = getSharedPreferences("MORNING_PARCEL_GROCERY",
                    MODE_PRIVATE);
            mEditorL = prefsL.edit();
            prefs1 = new Preferencehelper(getApplicationContext());
            if (prefsL.getString("cntry", "").equalsIgnoreCase("India")) {
                loadSubCategories("safeofresh");

                if (prefs1.getPrefsContactId().isEmpty()) {
                    loadCategories("safeofresh");
                    //  getAllProducts(HomeActivity.this, "66738", "67263");
                    getallshopindia("67263");


                } else {
                    loadCategories("safeofresh");
                    getallshopindia(prefs1.getPrefsContactId());
                }

            } else {
                prefs1 = new Preferencehelper(getApplicationContext());
                if (prefs.getString("shopId", "").isEmpty()) {
                    getAllShop("67266");
                    loadCategories(prefs.getString("shopName", ""));
                    //  getAllProducts(HomeActivity.this, prefs.getString("shopId", ""), "67266");

                } else {
                    prefs1 = new Preferencehelper(getApplicationContext());
                    getAllShop(prefs1.getPrefsContactId());
                    loadCategories(prefs.getString("shopName", ""));
                    //  getAllProducts(HomeActivity.this, prefs.getString("shopId", ""), prefs1.getPrefsContactId());
                }

            }
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
                mRecyclerView.setAdapter(new MainCategoryAdapter(HomeActivity.this, mListCatMain));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        dbManager = com.dhanuka.morningparcel.database.DatabaseManager.getInstance(HomeActivity.this);
        countView = (CartCountView) LayoutInflater.from(HomeActivity.this).inflate(R.layout.action_cart, null);
        loginView = (LoginView) LayoutInflater.from(HomeActivity.this).inflate(R.layout.action_login, null);
        storeView = (StoreView) LayoutInflater.from(HomeActivity.this).inflate(R.layout.action_shop, null);
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

        CameraDialog = new Dialog(HomeActivity.this);
        CameraDialog.setContentView(R.layout.popup_image_select);
        ImageView camerabtn = CameraDialog.findViewById(R.id.camerabtn);
        ImageView gallerybtn = CameraDialog.findViewById(R.id.gallerybtn);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraDialog.show();

            }
        });

        camerabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                } else {

                    Intent intent1 = new Intent(HomeActivity.this, ImagePickActivity.class);
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
        imgback.setVisibility(View.GONE);
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new Preferencehelper(HomeActivity.this).getPrefsUsercategory().equalsIgnoreCase("1057")) {
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                }
                finish();
            }
        });


    }

    public void appupdate() {
        String version = "";
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        new ForceUpdateAsync(version, HomeActivity.this).execute();


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
            showDialogUpdate(HomeActivity.this, true);


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


    CartCountView countView;
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
        mPager.setAdapter(new BannerAdapter(HomeActivity.this, sliders));
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
                        if (new Preferencehelper(HomeActivity.this).getPrefsViewType() == 1 && new Preferencehelper(HomeActivity.this).getPrefsUsercategory().equalsIgnoreCase("1058")) {

                        } else {
                            startActivity(new Intent(HomeActivity.this, CartActivity.class));

                        }

                        break;

                    case R.id.navigation_profile:

                        if (new Preferencehelper(HomeActivity.this).getPrefsViewType() == 1 && new Preferencehelper(HomeActivity.this).getPrefsUsercategory().equalsIgnoreCase("1058")) {

                        } else {
                            startActivity(new Intent(HomeActivity.this, Notification_Bell.class));

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
        loadHistory();
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

        final ProgressDialog mProgressBar = new ProgressDialog(HomeActivity.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();

        // StringRequest postRequest = new StringRequest(Request.Method.POST, "http://mmthinkbiz.com/MobileService.aspx?method=GetCategoryDetail",
        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://mmthinkbiz.com/MobileService.aspx?method=GetCategoryDetail_Store",


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("responsiveCatMain", response);
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                mEditor.putString("resp", response);
                                mEditor.commit();

                                mListCatMain = new Gson().fromJson(jsonObject.getString("returnds"), new TypeToken<List<MainCatBean>>() {
                                }.getType());
                                mRecyclerView.setAdapter(new MainCategoryAdapter(HomeActivity.this, mListCatMain));

                            } else {

                                FancyToast.makeText(HomeActivity.this, "No Data Found, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
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
                        FancyToast.makeText(HomeActivity.this, "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


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

        final ProgressDialog mProgressBar = new ProgressDialog(HomeActivity.this);
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

                                FancyToast.makeText(HomeActivity.this, "No Data Found, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
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
                        FancyToast.makeText(HomeActivity.this, "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


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
        if (new Preferencehelper(HomeActivity.this).getPrefsViewType() == 1 && new Preferencehelper(HomeActivity.this).getPrefsUsercategory().equalsIgnoreCase("1058")) {
            item.setVisible(false);

        }
        if (prefs.getPREFS_trialuser().equalsIgnoreCase("0")) {
            item.setVisible(false);
        } else {

            item.setVisible(true);
        }

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

    private void loadProfileData() {

        final ProgressDialog mProgressBar = new ProgressDialog(HomeActivity.this);
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
                            String responses = jkHelper.Decryptapi(response, HomeActivity.this);
                            JSONObject jsonObject = new JSONObject(responses);
                            int success = jsonObject.getInt("success");
                            log.e("success" + success);
                            String val1 = "";
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
                                    String storeopentime = jsonobject.getString("StoreOpenTime");
                                    String storeclosetime = jsonobject.getString("StoreCloseTime");
                                    String society = jsonobject.getString("society");
                                    val1 = jsonobject.getString("val1");
                                    String zipcode = jsonobject.getString("Zipcode");
                                    String CurrentBalance = jsonobject.getString("CurrentBalance");
                                    String IsFinanceBlock = jsonobject.getString("IsFinanceBlock");

                                    preferencehelper.setPREFS_currentbal(CurrentBalance);
                                    preferencehelper.setPrefsIsFinanceBlock(IsFinanceBlock);
                                    preferencehelper.setPREFS_city(city);
                                    preferencehelper.setPREFS_society(society);
                                    preferencehelper.setPREFS_Building(Building);
                                    preferencehelper.setPREFS_firstname(firstname);
                                    preferencehelper.setPREFS_email2(email2);
                                    preferencehelper.setPREFS_phoneno(phoneno);
                                    preferencehelper.setPREFS_flatno(flatno);
                                    preferencehelper.setPREFS_state(state);
                                    preferencehelper.setPrefsZipCode(zipcode);
                                    preferencehelper.setPrefsShopCloseTime(storeclosetime);
                                    preferencehelper.setPrefsShopOpenTime(storeopentime);


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
                                Gson gson = new Gson();
                                mProfileBean = gson.fromJson(jsonArray.get(0).toString(), ProfileBean.class);
                                if (TextUtils.isEmpty((mProfileBean.getFirstname()))) {
                                    userName.setText("Hi User");
                                } else {
                                    userName.setText("Hi " + mProfileBean.getFirstname() + " " + mProfileBean.getLastname());


                                }

                                try {
                                    val1 = val1.replace("\u0026", "&");

                                    Picasso.with(getApplicationContext()).load(val1).placeholder(R.drawable.no_image).into(img);

                                } catch (Exception e) {
                                    e.printStackTrace();

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
                    String finalparam = jkHelper.Encryptapi(param, HomeActivity.this);
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
                            String responses = jkHelper.Decryptapi(response, HomeActivity.this);

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
                                    String Margin = newjson.getString("Margin");
                                    String IsNewListing = newjson.getString("IsNewListing");
                                    String RepeatOrder = newjson.getString("RepeatOrder");
                                    String ItemImage = newjson.getString("ItemImage");
                                    ItemMasterhelper v = new ItemMasterhelper();
                                    v.setItemID(ItemID);
                                    v.setItemName(ItemName);
                                    v.setIsDeal(IsDeal);
                                    v.setMargin(Margin);
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
                    String finalparam = jkHelper.Encryptapi(param, HomeActivity.this);
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
        final ProgressDialog mProgressBar = new ProgressDialog(HomeActivity.this);
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
                        String responses = jkHelper.Decryptapi(response, HomeActivity.this);
                        mEditor.putString("shopss", responses);
                        mEditor.commit();
                        if (prefs.getString("shopId", "").isEmpty()) {
                            //  if (prefsOld.getPrefsUsercategory().equalsIgnoreCase("1057")) {
                            new SelectShop(HomeActivity.this, 1, false).show();
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
                    String finalparam = jkHelper.Encryptapi(param, HomeActivity.this);
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
                        String responses = jkHelper.Decryptapi(response, HomeActivity.this);
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

                                mPager.setAdapter(new BannerAdapter(HomeActivity.this, bannerlist));
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

                        Message.message(HomeActivity.this, "Failed To Retrieve Data");
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


                    if (prefsL.getString("cntry", "").equalsIgnoreCase("India")) {


                        //   String param = getString(R.string.URL_GET_CATEGORY_CODE) + "&contactid=" + "67263" + "&type=" + types;
                        String param = getString(R.string.URL_GET_CATEGORY_CODE) + "&contactid=" + "66738" + "&type=" + types;
                        Log.d("Beforeencrption1", param);
                        JKHelper jkHelper = new JKHelper();
                        String finalparam = jkHelper.Encryptapi(param, HomeActivity.this);
                        params.put("val", finalparam);
                        Log.d("afterencrption1", finalparam);
                        return params;
                    } else {
                        //String param = getString(R.string.URL_GET_CATEGORY_CODE) + "&contactid=" + "0" + "&type=" + types;
                        String param = getString(R.string.URL_GET_CATEGORY_CODE) + "&contactid=" + shpId + "&type=" + types;
                        Log.d("Beforeencrption2", param);
                        JKHelper jkHelper = new JKHelper();
                        String finalparam = jkHelper.Encryptapi(param, HomeActivity.this);
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

        Volley.newRequestQueue(HomeActivity.this).add(postRequest);


    }

    public void loadLinks() {

        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        JKHelper jkHelper = new JKHelper();
                        String responses = jkHelper.Decryptapi(response, HomeActivity.this);
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

                        Message.message(HomeActivity.this, "Failed To Retrieve Data");
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
                    String finalparam = jkHelper.Encryptapi(param, HomeActivity.this);
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

        Volley.newRequestQueue(HomeActivity.this).add(postRequest);


    }

    public void loadtaglines() {
        Preferencehelper prefs = new Preferencehelper(getApplicationContext());

        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        JKHelper jkHelper = new JKHelper();
                        String responses = jkHelper.Decryptapi(response, HomeActivity.this);
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

                        Message.message(HomeActivity.this, "Failed To Retrieve Data");
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
                    String finalparam = jkHelper.Encryptapi(param, HomeActivity.this);
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

        Volley.newRequestQueue(HomeActivity.this).add(postRequest);


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


        final ProgressDialog mProgressBar = new ProgressDialog(HomeActivity.this);
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
                        String responses = jkHelper.Decryptapi(response, HomeActivity.this);
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
                                    mEditor.putString("Currency", Currency);
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
                            new SelectShop(HomeActivity.this, 1, false).show();
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

                    Preferencehelper prefs = new Preferencehelper(getApplicationContext());
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);
                    String contctId = "";
                    if (prefsOld.getPrefsContactId().isEmpty()) {
                        contctId = "7777";
                    } else {
                        contctId = prefsOld.getPrefsContactId();
                    }

                    String param = AppUrls.URL_FILL_CONSIGNEE + "&contactid=" + contactid + "&CId=" + prefs.getCID()+"&StoreName=SafeOfresh";
                    Log.d("Beforeencrption", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, HomeActivity.this);
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
        ProgressDialog mProgressBar = new ProgressDialog(HomeActivity.this);


        private Context context;
        ArrayList<ItemMasterhelper> mListItems;

        public SaveToDB(ArrayList<ItemMasterhelper> mListItem, Context context) {
            this.mListItems = mListItem;
            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar = new ProgressDialog(HomeActivity.this);
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
                            All_Item_Small masterdao = new All_Item_Small(database, HomeActivity.this);
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

//    public void loadHistory() {
//
//
//        final ProgressDialog mProgressBar = new ProgressDialog(HomeActivity.this);
//        mProgressBar.setTitle("Safe'O'Fresh");
//        mProgressBar.setMessage("Loading...");
//        mProgressBar.show();
//        //  "http://mmthinkbiz.com/MobileService.aspx?method=GetGRDetail_Web" + "&type=" + "100" + "&tdate=" + mTodayDate + "&fdate=" + mStartDate + "&CID=" + sytrContactId
//        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL)
//                /* getString(R.string.URL_BASE_URL)*/,
//
//
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        try {
//                            ArrayList<CartProduct> listOrders = new ArrayList<>();
//
//
//                            Log.d("responsiveorderhistory", response);
//                            JKHelper jkHelper = new JKHelper();
//
//                            String responses = jkHelper.Decryptapi(response, HomeActivity.this);
//
//                            Log.d("responsive", responses);
//                            JSONObject jsonObject = new JSONObject(responses);
//                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
//
//                                JSONArray newjsonarr = jsonObject.getJSONArray("returnds");
//
//
//                                // Log.d("cartproductprint",cartProduct.getMRP()+"\n"+product.getMRP()+"\n");
//
//                                dbManager = com.dhanuka.morningparcel.database.DatabaseManager.getInstance(getApplicationContext());
//
//
//                                for (int a = 0; a < newjsonarr.length(); a++) {
//                                    JSONObject newjsonobj = newjsonarr.getJSONObject(a);
//
//                                    CartProduct cartProduct = new CartProduct();
//                                    cartProduct.setCompanyid(newjsonobj.getString("CompanyID"));
//                                    JSONArray oreridarr = newjsonobj.getJSONArray("Items");
//                                    dbManager.deleteAll();
//                                    for (int k = 0; k < oreridarr.length(); k++) {
//                                        JSONObject orderobj = oreridarr.getJSONObject(k);
//
//
//                                        cartProduct.setItemID(Integer.parseInt(orderobj.getString("ItemID")));
//
//                                        cartProduct.setItemName(orderobj.getString("ItemDescription"));
//                                        cartProduct.setGroupID("");
//                                        cartProduct.setOpeningStock("");
//                                        cartProduct.setMOQ("");
//                                        cartProduct.setROQ("");
//                                        cartProduct.setPurchaseUOM("");
//                                        cartProduct.setPurchaseUOMId("");
//                                        cartProduct.setSaleUOM("");
//                                        cartProduct.setSaleUOMID("");
//                                        cartProduct.setPurchaseRate("");
//                                        cartProduct.setSaleRate(orderobj.getString("Rate"));
//                                        cartProduct.setItemSKU("");
//                                        cartProduct.setItemBarcode(orderobj.getString("BarCode"));
//                                        cartProduct.setStockUOM(orderobj.getString("StockUOM"));
//                                        cartProduct.setItemImage(orderobj.getString("FileName") + "&filePath=" + orderobj.getString("filepath"));
//                                        cartProduct.setHSNCode("");
//                                        DecimalFormat precision = new DecimalFormat("0");
//                                        int finalquant = (int) (Math.round(Float.parseFloat(orderobj.getString("RequestedQty"))));
//                                        cartProduct.setQuantity(finalquant);
//                                        cartProduct.setSubCategory("");
//                                        cartProduct.setMRP(orderobj.getString("MRP"));
//
//                                        dbManager.insert(cartProduct);
//                                        countView.setCount(dbManager.getTotalQty());
//
//                                    }
//
//                                }
//
//
//                            } else {
//
//                                dbManager.deleteAll();
//
//                                FancyToast.makeText(HomeActivity.this, "No Data Found, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
//                            }
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        mProgressBar.dismiss();
//
//
//                    }
//                },
//                new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        mProgressBar.dismiss();
//
//                        FancyToast.makeText(HomeActivity.this, "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
//
//
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams() {
//
//                Map<String, String> params1 = new HashMap<String, String>();
//
//                Calendar c = Calendar.getInstance();
//                System.out.println("Current time => " + c.getTime());
//
//                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
//                String mTodayDate = df.format(c.getTime());
//
//                try {
//
//                    SharedPreferences mData = getSharedPreferences("MORNING_PARCEL_GROCERY",
//                            MODE_PRIVATE);
//                    shopid = mData.getString("shopId", "1");
//
//                    String param = getString(R.string.GET_ORDER_DETAIL) + "&type=" + "500" + "&tdate=" + mTodayDate + "&fdate=" + mTodayDate + "&CID=" + preferencehelper.getPrefsContactId() + "&storeId=" + shopid + "&fdate=" + shopid + "&tdate=";
//                    Log.d("Beforeencrptionhistory", param);
//                    JKHelper jkHelper = new JKHelper();
//                    String finalparam = jkHelper.Encryptapi(param, HomeActivity.this);
//                    params1.put("val", finalparam);
//                    Log.d("afterencrptionhistory", finalparam);
//                    return params1;
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return params1;
//                }
//
//            }
//        };
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(
//                60000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//        Volley.newRequestQueue(this).add(postRequest);
//
//
//    }


    public void loadHistory() {


        final ProgressDialog mProgressBar = new ProgressDialog(HomeActivity.this);
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

                            String responses = jkHelper.Decryptapi(response, HomeActivity.this);

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


                                        setCartCount();
                                    }


                                }


                            } else {

                                dbManager.deleteAll();
                                setCartCount();

                                FancyToast.makeText(HomeActivity.this, "No Data Found, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
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


                        FancyToast.makeText(HomeActivity.this, "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


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
                    String finalparam = jkHelper.Encryptapi(param, HomeActivity.this);
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


    public void loadImage(String str) {
        Glide.with(HomeActivity.this)
                .load(str)
                .apply(new RequestOptions().placeholder(R.drawable.no_image).error(R.drawable.no_image))
                .into(img);

    }

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
                    loadImage(path);
//                    mAdapter1.addItems(mImages);
                    log.e("file path in request code true==" + filePath);
                    //  if (!mBean.getStrrmasterid().isEmpty()) {
                    new HomeActivity.ImageCompressionAsyncTask(true).execute(path);
                    updateserverphotoid();
                    uploadimage();


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
//                    mAdapter1.addItems(mImages);
                    loadImage(path);
//                    //  if (!mBean.getStrrmasterid().isEmpty()) {

                    new HomeActivity.ImageCompressionAsyncTask(true).execute(path);
                    updateserverphotoid();
                    uploadimage();
                }
                //   }
            }
            if (requestCode == REQUEST_CODE) {
                asREQUEST_CODE = requestCode;
                log.e("request code true  ");
                SharedPreferences prefs;
                prefs = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
                final String path = prefs.getString(PREFS_FILE_PATH, filePath);
                log.e("file path in request code true==" + filePath);
                new HomeActivity.ImageCompressionAsyncTask(true).execute(path);


            }

        }

    }

    String TAG = "GRM";


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
                modle.setmDescription("ContactInfo");
                modle.setmImageType("ContactInfo");
                modle.setmServerId(preferencehelper.getPrefsContactId());
                arrayList.add(modle);

                DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                    @Override
                    public void run(SQLiteDatabase database) {

                        ImageMasterDAO dao = new ImageMasterDAO(database, HomeActivity.this);
                        ArrayList<DbImageMaster> list = arrayList;
                        dao.insert(list);
                        addedToMasterTable = true;
                    }
                });
            }


            DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                @Override
                public void run(SQLiteDatabase database) {
                    ImageMasterDAO dao = new ImageMasterDAO(database, HomeActivity.this);
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
            modle.setmDescription("ContactInfo");
            modle.setmImageType("ContactInfo");
            modle.setmImageId(Integer.parseInt(preferencehelper.getPrefsContactId()));
            modle.setmImagePath(mImagePathDataBase);
            modle.setmImageName(mImageNameDataBase);
            arrayListUpload.add(modle);


            DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
                @Override
                public void run(SQLiteDatabase database) {
                    ImageUploadDAO dao = new ImageUploadDAO(database, HomeActivity.this);
                    ArrayList<DbImageUpload> list = arrayListUpload;
                    dao.insert(list);

                    log.e("photo inserted ");
                }
            });
            //    setPhotoCount();


        }
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


    String orderId, catcodeid, catcodedesc, deliverycgarge;

    private void uploadimage() {
        if (JKHelper.isConnectedToNetwork(HomeActivity.this) && !JKHelper.isServiceRunning(HomeActivity.this, ImageUploadService.class)) {
            JKHelper.showLog("Start Service");
            startService(new Intent(HomeActivity.this, ImageUploadService.class));
        } else {
            stopService(new Intent(HomeActivity.this, ImageUploadService.class));
            startService(new Intent(HomeActivity.this, ImageUploadService.class));
        }
    }

    private void updateserverphotoid() {
        for (int a = 0; a < mListLastRows.size(); a++) {
            if (mListLastRows.get(a) != null) {
                SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
                ImageUploadDAO pd = new ImageUploadDAO(database, HomeActivity.this);
                pd.setWorkIdToTable(String.valueOf("11112"), mListLastRows.get(a));
                ImageMasterDAO pds = new ImageMasterDAO(database, HomeActivity.this);
                pds.setServerDetails(Integer.parseInt(mListLastRows.get(a)), Integer.parseInt(preferencehelper.getPrefsContactId()), 0);
                ImageMasterDAO iDao = new ImageMasterDAO(database, getApplicationContext());
                String serverId = iDao.getServerIdById(1);
                DatabaseManager.getInstance().closeDatabase();
                log.e("in last row master table inserting  ");
            }


        }


    }


//    private void addToDatabase() {
//
//        if (!addedToMasterTable) {
//            final ArrayList<DbImageMaster> arrayList = new ArrayList<>();
//            DbImageMaster modle = new DbImageMaster();
//            modle.setmDate(JKHelper.getCurrentDate());
//            modle.setmUploadStatus(0);
//            modle.setmDescription("Contact_Complaint");
//            modle.setmImageType("Contact_Complaint");
//            arrayList.add(modle);
//
//            DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
//                @Override
//                public void run(SQLiteDatabase database) {
//
//                    ImageMasterDAO dao = new ImageMasterDAO(database, ComplaintReason.this);
//                    ArrayList<DbImageMaster> list = arrayList;
//                    dao.insert(list);
//                    addedToMasterTable = true;
//                }
//            });
//        }
//
//
//        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
//            @Override
//            public void run(SQLiteDatabase database) {
//                ImageMasterDAO dao = new ImageMasterDAO(database, ComplaintReason.this);
//                masterDataBaseId = dao.getlatestinsertedid();
//                lastRowMaterTable = String.valueOf(masterDataBaseId);
//                mListLastRows.add(lastRowMaterTable);
//                log.e("string id ============== " + masterDataBaseId);
//            }
//        });
//
//
//        final ArrayList<DbImageUpload> arrayListUpload = new ArrayList<>();
//        DbImageUpload modle = new DbImageUpload();
//        modle.setmDate(mCurrentTimeDataBase);
//        modle.setmImageUploadStatus(0);
//        modle.setmDescription("Contact_Complaint");
//        modle.setmImageType("Contact_Complaint");
//        modle.setmImageId(masterDataBaseId);
//        modle.setmImagePath(mImagePathDataBase);
//        modle.setmImageName(mImageNameDataBase);
//        arrayListUpload.add(modle);
//
//
//        DatabaseManager.getInstance().executeQuery(new QueryExecutor() {
//            @Override
//            public void run(SQLiteDatabase database) {
//                ImageUploadDAO dao = new ImageUploadDAO(database, ComplaintReason.this);
//                ArrayList<DbImageUpload> list = arrayListUpload;
//                dao.insert(list);
//
//                log.e("photo inserted ");
//            }
//        });
//        //    setPhotoCount();
//
//
//    }


}
