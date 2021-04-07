package com.dhanuka.morningparcel.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
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

import com.dhanuka.morningparcel.Controllers.AppUrls;
import com.dhanuka.morningparcel.Helper.CatcodeHelper;
import com.dhanuka.morningparcel.activity.retail.GRMMaster;
import com.dhanuka.morningparcel.activity.retail.NewBillActivity;
import com.dhanuka.morningparcel.activity.supplierorder.SupplierSearchActivity;
import com.dhanuka.morningparcel.adapter.DashAdapter;
import com.dhanuka.morningparcel.beans.DeliveryBoysBean;
import com.dhanuka.morningparcel.events.Dashclickevent;
import com.dhanuka.morningparcel.model.DashModel;
import com.dhanuka.morningparcel.utils.ErrorReporter;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.utils.NetworkMgr;
import com.dhanuka.morningparcel.Helper.ItemMasterhelper;
import com.dhanuka.morningparcel.Helper.Preferencehelper;

import com.dhanuka.morningparcel.R;

import com.dhanuka.morningparcel.SqlDatabase.DatabaseHelper;
import com.dhanuka.morningparcel.SqlDatabase.DatabaseManager;
import com.dhanuka.morningparcel.beans.ProfileBean;
import com.dhanuka.morningparcel.adapter.BannerAdapter;
import com.dhanuka.morningparcel.beans.FancyToast;
import com.dhanuka.morningparcel.beans.MainCatBean;
import com.dhanuka.morningparcel.customViews.CartCountView;
import com.dhanuka.morningparcel.customViews.KKViewPager;
import com.dhanuka.morningparcel.customViews.StoreView;
import com.dhanuka.morningparcel.utils.PermissionUtils;
import com.dhanuka.morningparcel.utils.log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeStoreActivity extends BaseActivity implements Dashclickevent {

    @Nullable
    @BindView(R.id.kk_pager)
    KKViewPager mPager;
    @Nullable
    @BindView(R.id.dots_indicate)
    DotsIndicator dotsIndicator;
    @Nullable
    @BindView(R.id.categorylayout)
    LinearLayout categorylayout;
    SharedPreferences prefs11;
    ArrayList<Integer> mListSliders;
    ArrayList<MainCatBean> mListCatMain;
    ArrayList<MainCatBean.CatBean> mListCat;
    ProfileBean mProfileBean;
    Preferencehelper preferencehelper;
    CatcodeHelper catcodeHelper;
    ArrayList<CatcodeHelper> bannerlist = new ArrayList<>();
    ArrayList<DeliveryBoysBean> mListDeliveryBoys = new ArrayList<>();
    ArrayList<String> result;
    @Nullable
    @BindView(R.id.etSearch)
    AutoCompleteTextView etSearch;
    @Nullable
    @BindView(R.id.etSearch1)
    TextView etSearch1;
    String[] item;
    @Nullable
    @BindView(R.id.searhlist)
    ImageView searhlist;
    HashMap<String, String> contactidhash = new HashMap<>();
    @Nullable
    @BindView(R.id.seeuserviewclk)
    ImageView seeuserviewclk;
    String finalselection;
    @Nullable
    @BindView(R.id.purchaseordrclk)
    ImageView purchaseordrclk;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_home_store;
    }

    SQLiteDatabase database;
    int posin;


    boolean doubleBackToExitPressedOnce = false;
    ArrayList<DashModel> dashfeaturelist = new ArrayList<DashModel>();

    @Override
    public void onBackPressed() {
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

    public void getItemsFromDb() {

        DatabaseManager.initializeInstance(new DatabaseHelper(getApplicationContext()));
        database = DatabaseManager.getInstance().openDatabase();

      /*
        All_Item_Master pd = new All_Item_Master(database, this);
        masterlistitem= pd.selectAll();
        Log.d("listsize1",String.valueOf(masterlistitem.size()));
        masteradpter = new ItemMasterAdapter(masterlistitem,getApplicationContext());
        mastercontainer.setAdapter(masteradpter);
        getallmasterdate();

        } */

    }


    Preferencehelper prefsOld;

    public void openProducts(View v) {
        startActivity(new Intent(HomeStoreActivity.this, HomeStoreProductsActivity.class));
    }

    public void openSales(View v) {
        startActivity(new Intent(HomeStoreActivity.this, SalesActivity.class));
    }

    public void openGr(View v) {
        startActivity(new Intent(HomeStoreActivity.this, GRSalesActivity.class));
    }

    public void storewiseclick(View v) {
        startActivity(new Intent(HomeStoreActivity.this, StoreWiseStockActivity.class));
    }

    public void openCreateGR(View v) {
        startActivity(new Intent(HomeStoreActivity.this, GRMMaster.class));
    }

    public void openNewstorewise(View v) {


        startActivity(new Intent(HomeStoreActivity.this, StoreWiseStockActivityNew.class));
    }


    public void opennewbilling(View v) {
        SharedPreferences prefs = getSharedPreferences("MORNING_PARCEL_POS",
                MODE_PRIVATE);
        SharedPreferences.Editor mEditor = prefs.edit();
        mEditor.putString("resp2", "");
        mEditor.putString("resp1", "");
        mEditor.commit();

        startActivity(new Intent(HomeStoreActivity.this, NewBillActivity.class));
    }

    public void openItems(View v) {
        startActivity(new Intent(HomeStoreActivity.this, PastOrderItem.class));
    }

    public void openProfile(View v) {
        startActivity(new Intent(HomeStoreActivity.this, UpdateProfile.class));
    }

    public void openOrders(View v) {
        startActivity(new Intent(HomeStoreActivity.this, OrderHistoryActivity.class));
    }

    public void openGR(View v) {
        startActivity(new Intent(HomeStoreActivity.this, OrderHistoryActivity.class));
    }

    public void stocktransferclick(View v) {
        startActivity(new Intent(HomeStoreActivity.this, HomeBilling.class));


    }

    SharedPreferences.Editor mEditor;
    public static Preferencehelper prefsNew;
    @Nullable
    @BindView(R.id.img99)
    ImageView img99;


    public void branchwisestockclk(View v) {
        startActivity(new Intent(HomeStoreActivity.this, BranchWiseStock.class));

    }

    public void openCustomerView(View v) {
        new Preferencehelper(HomeStoreActivity.this).setPrefsViewType(1);

        Preferencehelper prefs4 = new Preferencehelper(getApplicationContext());
        prefs4.setPREFS_trialuser("0");

        startActivity(new Intent(getApplicationContext(), CustomerSearchActivity.class));

    }

    ImageView supplierorderclk;
    @Nullable
    @BindView(R.id.pendingcard)
    CardView pendingcard;
    @Nullable
    @BindView(R.id.profilecard)
    CardView profilecard;
    @Nullable
    @BindView(R.id.customerviewcard)
    CardView customerviewcard;
    @Nullable
    @BindView(R.id.customerpurchasecard)
    CardView customerpurchasecard;
    @Nullable
    @BindView(R.id.stocktransfercard)
    CardView stocktransfercard;
    @Nullable
    @BindView(R.id.supplierordercard)
    CardView supplierordercard;
    @Nullable
    @BindView(R.id.customerordercard)
    CardView customerordercard;
    DashAdapter dashAdapter;
    @Nullable
    @BindView(R.id.dashlistcontainer)
    RecyclerView dashlistcontainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_home_store, container_body);
        SharedPreferences prefs = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);
        //setContentView(R.layout.activity_home_grocery);
        ButterKnife.bind(this);
        prefs11 = getApplicationContext().getSharedPreferences("MORNING_PARCEL_GROCERY",
                getApplicationContext().MODE_PRIVATE);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(HomeStoreActivity.this);
        mEditor = sharedPreferences.edit();
        prefsNew = new Preferencehelper(this);
        preferencehelper = new Preferencehelper(HomeStoreActivity.this);
        PermissionUtils permissionModule = new PermissionUtils(HomeStoreActivity.this);
        permissionModule.checkPermissions();
        ErrorReporter errReporter = new ErrorReporter();
        errReporter.Init(this);
        errReporter.CheckErrorAndSendMail(this);

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
        supplierorderclk = findViewById(R.id.supplierorderclk);
        if (prefsNew.getCID().equalsIgnoreCase("523")) {
            pendingcard.setVisibility(View.VISIBLE);
            profilecard.setVisibility(View.VISIBLE);
            customerviewcard.setVisibility(View.VISIBLE);
            customerpurchasecard.setVisibility(View.VISIBLE);
            stocktransfercard.setVisibility(View.VISIBLE);
            supplierordercard.setVisibility(View.VISIBLE);

            dashfeaturelist.add(new DashModel("1", "PRODUCTS", R.drawable.product_grocery));
            dashfeaturelist.add(new DashModel("2", "ORDERS", R.drawable.orders_grocery));
            dashfeaturelist.add(new DashModel("3", "SALE REPORTS", R.drawable.sale_report_icon));
            dashfeaturelist.add(new DashModel("4", "PENDING ITEMS", R.drawable.about_grocery));
            dashfeaturelist.add(new DashModel("5", "PROFILE", R.drawable.profile_grocery));
            dashfeaturelist.add(new DashModel("6", "GR REPORTS", R.drawable.gr_report_icon));
            dashfeaturelist.add(new DashModel("7", "CUSTOMER VIEW", R.drawable.userview));
            dashfeaturelist.add(new DashModel("8", "PURCHASE ORDER ", R.drawable.pruchase_order_ic));
            dashfeaturelist.add(new DashModel("9", "CREATE GR", R.drawable.ic_edit));
            dashfeaturelist.add(new DashModel("10", "STORE WISE STOCK", R.drawable.store_icon_ic));
            dashfeaturelist.add(new DashModel("11", "BILLING", R.drawable.billing_icon));
            dashfeaturelist.add(new DashModel("12", "STOCK TRANSFER ", R.drawable.stocktr_ic));
            dashfeaturelist.add(new DashModel("13", "BRANCH WISE STOCK", R.drawable.branch_stoc_ic));
            dashfeaturelist.add(new DashModel("14", "CUSTOMER ORDER", R.drawable.order_ic));
            dashfeaturelist.add(new DashModel("15", "SUPPLIER ORDER", R.drawable.supp_orderic));
            dashfeaturelist.add(new DashModel("16", "CUSTOMER COLLECTION", R.drawable.collect));
            dashfeaturelist.add(new DashModel("17", "CUSTOMER SUMMARY", R.drawable.summary));
            dashlistcontainer.setHasFixedSize(true);
            dashlistcontainer.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
            dashAdapter = new DashAdapter(getApplicationContext(), dashfeaturelist, HomeStoreActivity.this);
            dashlistcontainer.setAdapter(dashAdapter);
        } else if (prefsNew.getPrefsEmail().equalsIgnoreCase("5755200200")) {

            pendingcard.setVisibility(View.GONE);
            profilecard.setVisibility(View.GONE);
            customerviewcard.setVisibility(View.GONE);
            stocktransfercard.setVisibility(View.GONE);
            supplierordercard.setVisibility(View.GONE);
            stocktransfercard.setVisibility(View.GONE);
            customerpurchasecard.setVisibility(View.GONE);

            dashfeaturelist.add(new DashModel("1", "PRODUCTS", R.drawable.product_grocery));
            dashfeaturelist.add(new DashModel("4", "PENDING ITEMS", R.drawable.about_grocery));
            dashfeaturelist.add(new DashModel("3", "SALE REPORTS", R.drawable.sale_report_icon));
            dashfeaturelist.add(new DashModel("6", "GR REPORTS", R.drawable.gr_report_icon));

            dashfeaturelist.add(new DashModel("9", "CREATE GR", R.drawable.ic_edit));
            dashfeaturelist.add(new DashModel("10", "STORE WISE STOCK", R.drawable.store_icon_ic));
            dashfeaturelist.add(new DashModel("11", "BILLING", R.drawable.billing_icon));
            dashfeaturelist.add(new DashModel("13", "BRANCH WISE STOCK", R.drawable.branch_stoc_ic));
            dashfeaturelist.add(new DashModel("16", "CUSTOMER COLLECTION", R.drawable.collect));
            dashfeaturelist.add(new DashModel("17", "CUSTOMER SUMMARY", R.drawable.summary));

            dashlistcontainer.setHasFixedSize(true);
            dashlistcontainer.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
            dashAdapter = new DashAdapter(getApplicationContext(), dashfeaturelist, HomeStoreActivity.this);
            dashlistcontainer.setAdapter(dashAdapter);
        }
        else if (prefsNew.getCID().equalsIgnoreCase("549")&& preferencehelper.getPrefsUsercategory().equalsIgnoreCase("1062")){
            pendingcard.setVisibility(View.GONE);
            profilecard.setVisibility(View.GONE);
            customerviewcard.setVisibility(View.GONE);
            stocktransfercard.setVisibility(View.GONE);
            supplierordercard.setVisibility(View.GONE);
            stocktransfercard.setVisibility(View.GONE);
            customerpurchasecard.setVisibility(View.GONE);


            dashfeaturelist.add(new DashModel("1", "PRODUCTS", R.drawable.product_grocery));
            dashfeaturelist.add(new DashModel("3", "SALE REPORTS", R.drawable.sale_report_icon));
            dashfeaturelist.add(new DashModel("6", "GR REPORTS", R.drawable.gr_report_icon));
            dashfeaturelist.add(new DashModel("4", "PENDING ITEMS", R.drawable.about_grocery));

            dashfeaturelist.add(new DashModel("9", "CREATE GR", R.drawable.ic_edit));
            dashfeaturelist.add(new DashModel("10", "STORE WISE STOCK", R.drawable.store_icon_ic));
            dashfeaturelist.add(new DashModel("11", "BILLING", R.drawable.billing_icon));
            dashfeaturelist.add(new DashModel("13", "BRANCH WISE STOCK", R.drawable.branch_stoc_ic));
            dashfeaturelist.add(new DashModel("16", "CUSTOMER COLLECTION", R.drawable.collect));
            dashfeaturelist.add(new DashModel("17", "CUSTOMER SUMMARY", R.drawable.summary));
            dashfeaturelist.add(new DashModel("15", "ORDER FOR CUSTOMER", R.drawable.order_ic));

            dashfeaturelist.add(new DashModel("18", "DAILY ORDER", R.drawable.milk));


            dashfeaturelist.add(new DashModel("19", "MEETING LEADS", R.drawable.meeting));

            dashfeaturelist.add(new DashModel("20", "MEETING REPORT", R.drawable.sticknote));


            dashfeaturelist.add(new DashModel("21", "COLLECTION REPORT", R.drawable.bank));

            dashfeaturelist.add(new DashModel("22", "TAT REPORT", R.drawable.shopcart));



            dashlistcontainer.setHasFixedSize(true);
            dashlistcontainer.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
            dashAdapter = new DashAdapter(getApplicationContext(), dashfeaturelist, HomeStoreActivity.this);
            dashlistcontainer.setAdapter(dashAdapter);

        }


        else {
            pendingcard.setVisibility(View.GONE);
            profilecard.setVisibility(View.GONE);
            customerviewcard.setVisibility(View.GONE);
            stocktransfercard.setVisibility(View.GONE);
            supplierordercard.setVisibility(View.GONE);
            stocktransfercard.setVisibility(View.GONE);
            customerpurchasecard.setVisibility(View.GONE);


            dashfeaturelist.add(new DashModel("1", "PRODUCTS", R.drawable.product_grocery));
            dashfeaturelist.add(new DashModel("3", "SALE REPORTS", R.drawable.sale_report_icon));
            dashfeaturelist.add(new DashModel("6", "GR REPORTS", R.drawable.gr_report_icon));
            dashfeaturelist.add(new DashModel("4", "PENDING ITEMS", R.drawable.about_grocery));

            dashfeaturelist.add(new DashModel("9", "CREATE GR", R.drawable.ic_edit));
            dashfeaturelist.add(new DashModel("10", "STORE WISE STOCK", R.drawable.store_icon_ic));
            dashfeaturelist.add(new DashModel("11", "BILLING", R.drawable.billing_icon));
            dashfeaturelist.add(new DashModel("13", "BRANCH WISE STOCK", R.drawable.branch_stoc_ic));
            dashfeaturelist.add(new DashModel("16", "CUSTOMER COLLECTION", R.drawable.collect));
            dashfeaturelist.add(new DashModel("17", "CUSTOMER SUMMARY", R.drawable.summary));

            dashlistcontainer.setHasFixedSize(true);
            dashlistcontainer.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
            dashAdapter = new DashAdapter(getApplicationContext(), dashfeaturelist, HomeStoreActivity.this);
            dashlistcontainer.setAdapter(dashAdapter);

        }
        img99.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                branchwisestockclk(v);

            }
        });
        supplierorderclk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), SupplierSearchActivity.class));
            }
        });

        purchaseordrclk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PurchaseOrderActivity.class));

            }
        });
        seeuserviewclk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new Preferencehelper(HomeStoreActivity.this).setPrefsViewType(0);
                startActivity(new Intent(HomeStoreActivity.this, HomeActivity.class));

//                Preferencehelper prefs4 = new Preferencehelper(getApplicationContext());
//                prefs4.setPREFS_trialuser("0");
//                startActivity(new Intent(HomeStoreActivity.this, HomeActivity.class).putExtra("signup", "1").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("cityname", "GURUGRAM"));
//

            }
        });


        searhlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Log.d("Userautcontid",contactidhash.get(etSearch.getText().toString()));
                if (!etSearch.getText().toString().isEmpty()) {
                    startActivity(new Intent(HomeStoreActivity.this, OrderHistoryActivity.class).putExtra("isCustomerlist", "1").putExtra("isCustomercid", contactidhash.get(etSearch.getText().toString())));
                }

            }
        });


        etSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                startActivity(new Intent(HomeStoreActivity.this, OrderHistoryActivity.class).putExtra("isCustomerlist", "1").putExtra("isCustomercid", contactidhash.get(finalselection)));
            }
        });



        bannerlist = new ArrayList<>();
        if (sharedPreferences.getString("links", "-1").equalsIgnoreCase("-1")) {
            loadLinks();
        }

    /*    mListSliders.add("https://image.shutterstock.com/image-photo/shopping-cart-vegetables-over-green-260nw-258890981.jpg");
        mListSliders.add("https://www.orlandosentinel.com/resizer/6LZXEfH5Or5WND0rzv4g1imUzbM=/800x450/top/arc-anglerfish-arc2-prod-tronc.s3.amazonaws.com/public/FHGG2JT52JHLTGQNBLTVLCT6MY.jpg");
        mListSliders.add("https://image.shutterstock.com/image-photo/shopping-cart-vegetables-over-green-260nw-258890981.jpg");
        mListSliders.add("https://www.orlandosentinel.com/resizer/6LZXEfH5Or5WND0rzv4g1imUzbM=/800x450/top/arc-anglerfish-arc2-prod-tronc.s3.amazonaws.com/public/FHGG2JT52JHLTGQNBLTVLCT6MY.jpg");
 */
        getItemsFromDb();

        initKKViewPager(bannerlist);
        //    categorylayout.removeAllViews();
        if (NetworkMgr.isNetworkAvailable(HomeStoreActivity.this)) {

            if (prefs.getString("categorysup", "").isEmpty()) {

                loadCategories("");
                loadProfileData();
                getCategoryCodes("", "116");
            } else {
                loadCategories("");
                loadProfileData();
                getCategoryCodes("", "116");
            }


        } else {
            try {


                JSONObject jsonObject = new JSONObject(prefs.getString("resp", ""));

                mListCatMain = new Gson().fromJson(jsonObject.getString("returnds"), new TypeToken<List<MainCatBean>>() {
                }.getType());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // if (prefs.getString("resp1", "-1").equalsIgnoreCase("-1")) {
        //  getAllProducts();

        if (prefsNew.getPrefsCountry().equalsIgnoreCase("India")) {
            getAllProductsindia();

        } else {
            getAllProducts();
        }

        //  }


     /*   for (int a = 0; a < 5; a++) {
            View vi;
            vi = getLayoutInflater().inflate(R.layout.item_home_grocery, null);
            RecyclerView mRecyclerView = (RecyclerView) vi.findViewById(R.id.rv_category);
            TextView txtCat = (TextView) vi.findViewById(R.id.txtCatName);


            mListCat.add(new MainCatBean.CatBean("1", "1", "https://image.shutterstock.com/image-photo/sliced-white-bread-260nw-352819853.jpg", "Breads"));
            mListCat.add(new MainCatBean.CatBean("1", "1", "https://image.shutterstock.com/image-photo/three-eggs-isolated-on-white-260nw-110803370.jpg", "Eggs"));
            mListCat.add(new MainCatBean.CatBean("1", "1", "https://image.shutterstock.com/image-photo/breakfast-cereal-table-setting-260nw-600324050.jpg", "Cereals"));
            mListCat.add(new MainCatBean.CatBean("1", "1", "https://image.shutterstock.com/image-photo/cheese-paneer-260nw-1096061933.jpg", "Paneer"));
            mListCat.add(new MainCatBean.CatBean("1", "1", "https://image.shutterstock.com/image-photo/steamed-fragrance-white-rice-bowl-260nw-1562092570.jpg", "Rice"));
            mListCat.add(new MainCatBean.CatBean("1", "1", "https://image.shutterstock.com/image-photo/bowl-spoon-full-sugar-on-260nw-613073507.jpg", "Sugar"));
            //  mListCatMain.add(new MainCatBean("1", "Main Category", mListCat));
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
            mRecyclerView.setAdapter(new CategoryAdapter(HomeStoreActivity.this, mListCat));
            categorylayout.addView(vi);
        }*/
        dbManager = com.dhanuka.morningparcel.database.DatabaseManager.getInstance(HomeStoreActivity.this);
        countView = (CartCountView) LayoutInflater.from(HomeStoreActivity.this).inflate(R.layout.action_cart, null);
        storeView = (StoreView) LayoutInflater.from(HomeStoreActivity.this).inflate(R.layout.action_shop, null);
        setCartCount();
        userName.setText(prefsOld.getPrefsEmail());


        SharedPreferences prefsToken = getSharedPreferences("MORNING_PARCEL_TOKEN",
                MODE_PRIVATE);
        SharedPreferences.Editor mEditorprefsToken = prefsToken.edit();

       /* CommonHelper commonHelper = new CommonHelper();
        commonHelper.sendRegistrationToServer(HomeStoreActivity.this, prefsToken.getString("token", ""), preferencehelper.getPrefsContactId());
*/
        if (prefsOld.getBannerPrefs().equalsIgnoreCase("0")) {
            //    Utility.openBannerDialog(HomeStoreActivity.this, container_body);
            //     prefsOld.setBannerPrefs("1");
        }//startActivity(new Intent(My_DashboardMain.this,UpdateUOMActivity.class));


        etSearch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CustomerSearchActivity.class).putExtra("type", "3"));
            }
        });

    }

    CartCountView countView;
    StoreView storeView;

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
        mPager.setAdapter(new BannerAdapter(HomeStoreActivity.this, sliders));
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
        new Preferencehelper(HomeStoreActivity.this).setPrefsViewType(0);

        try {
            txt_set.setText("Safe'O'Fresh");
            backbtn_layout.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SharedPreferences prefsss = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);
        mEditor = prefsss.edit();
        mEditor.putString("isIntent", "0");
        mEditor.commit();

    }

    public void loadCategories(final String donutid) {
        String strPostfix = "";
        if (preferencehelper.getPrefsCountry().equalsIgnoreCase("India")) {
            strPostfix = "http://mmthinkbiz.com/MobileService.aspx?method=GetCategoryDetail_Store_Web&storename=SafeOKart";
        } else {
            strPostfix = "http://mmthinkbiz.com/MobileService.aspx?method=GetCategoryDetail";
        }
        SharedPreferences prefs = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);
        SharedPreferences.Editor mEditor = prefs.edit();

        final ProgressDialog mProgressBar = new ProgressDialog(HomeStoreActivity.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, strPostfix,


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("responsiveCate", response);

                            SharedPreferences.Editor mEditor = prefs11.edit();

                            mEditor.putString("categorysup", response);
                            mEditor.apply();
                            JSONObject jsonObject;
                            if (prefs11.getString("categorysup", "").isEmpty()) {
                                jsonObject = new JSONObject(response);
                            } else {
                                jsonObject = new JSONObject(prefs11.getString("categorysup", ""));
                            }


                            if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                                mEditor.putString("resp", response);
                                mEditor.commit();

                                mListCatMain = new Gson().fromJson(jsonObject.getString("returnds"), new TypeToken<List<MainCatBean>>() {
                                }.getType());

                            } else {

                                FancyToast.makeText(HomeStoreActivity.this, "No Data Found, try after some time.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
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
                        FancyToast.makeText(HomeStoreActivity.this, "Something went wrong.", FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params1 = new HashMap<String, String>();
                params1.put("", "");
                return params1;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(this).add(postRequest);


    }

    public void getAllProducts() {
        final ProgressDialog mProgressBar = new ProgressDialog(HomeStoreActivity.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.setCancelable(false);
        mProgressBar.show();
        SharedPreferences prefs = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);
        SharedPreferences.Editor mEditor = prefs.edit();

        ArrayList<ItemMasterhelper> masterlist = new ArrayList<>();
        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("Response393", response);

                        String res = response;
                        mProgressBar.dismiss();
                        try {
                            JKHelper jkHelper = new JKHelper();
                            String responses = jkHelper.Decryptapi(response, HomeStoreActivity.this);

                            mEditor.putString("resp1", responses);
                            mEditor.commit();
                            JSONObject jsonObject = new JSONObject(responses);

                        } catch (Exception e) {
                            // mProgressBar.dismiss();

                            e.printStackTrace();
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
                Preferencehelper prefs;
                prefs = new Preferencehelper(HomeStoreActivity.this);

                Map<String, String> params = new HashMap<String, String>();


                try {
                    // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);

                    String param = AppUrls.GET_ITEM_MASTER_URL + "&ContactID=" + prefsOld.getPrefsContactId() + "&type=" + "28";
                    Log.d("Beforeencrption", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, HomeStoreActivity.this);
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


    private void loadProfileData() {

        final ProgressDialog mProgressBar = new ProgressDialog(HomeStoreActivity.this);
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

                            String responses = jkHelper.Decryptapi(response, HomeStoreActivity.this);

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
                                    preferencehelper.setPREFS_city(city);
                                    preferencehelper.setPREFS_society(society);
                                    preferencehelper.setPREFS_Building(Building);
                                    preferencehelper.setPREFS_firstname(firstname);
                                    preferencehelper.setPREFS_email2(email2);
                                    preferencehelper.setPREFS_phoneno(phoneno);
                                    preferencehelper.setPREFS_flatno(flatno);
                                    preferencehelper.setPREFS_state(state);


                                }
                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(HomeStoreActivity.this);
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

                    String param = getString(R.string.URL_GET_PROFILE) + "&contactid=" + preferencehelper.getPrefsContactId() + "&userid=" + "";
                    ;
                    Log.d("Beforeencrption", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, HomeStoreActivity.this);
                    params.put("val", finalparam);
                    Log.d("afterencrption", finalparam);
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

    public void getCategoryCodes(final String contactid, final String types) {

        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        JKHelper jkHelper = new JKHelper();
                        String responses = jkHelper.Decryptapi(response, HomeStoreActivity.this);
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

                                mPager.setAdapter(new BannerAdapter(HomeStoreActivity.this, bannerlist));
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

                        Message.message(HomeStoreActivity.this, "Failed To Retrieve Data");
                        //   Log.e("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                try {
                    String param = getString(R.string.URL_GET_CATEGORY_CODE) + "&contactid=" + "0" + "&type=" + types;
                    Log.d("BeforeencrptionCat", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, HomeStoreActivity.this);
                    params.put("val", finalparam);
                    Log.d("afterencrptionCat", finalparam);
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

        Volley.newRequestQueue(HomeStoreActivity.this).add(postRequest);


    }

    public void loadLinks() {

        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        JKHelper jkHelper = new JKHelper();
                        String responses = jkHelper.Decryptapi(response, HomeStoreActivity.this);
                        Log.e("Response", responses);

                        try {
                            bannerlist = new ArrayList<>();
                            JSONObject jsonObject = new JSONObject(responses);
                            int success = jsonObject.getInt("success");
                            log.e("success" + success);
                            if (success == 1) {

                                log.e("in success one ");
                                JSONArray jsonArray = jsonObject.getJSONArray("returnds");


                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    nav_Menu.findItem(R.id.publicationnav).setVisible(false);
                                    if (jsonObject1.getString("CodeDescription").equalsIgnoreCase("ABOUT THIS APP")) {
                                        mEditor.putString("abt", jsonObject1.getString("Val1"));
                                        if (jsonObject1.getString("Val1").isEmpty()) {
                                            nav_Menu.findItem(R.id.about).setVisible(false);

                                        } else {
                                            nav_Menu.findItem(R.id.about).setVisible(true);

                                        }
                                    } else if (jsonObject1.getString("CodeDescription").equalsIgnoreCase("HELP")) {
                                        mEditor.putString("hlp", jsonObject1.getString("Val1"));
                                        if (jsonObject1.getString("Val1").isEmpty()) {
                                            nav_Menu.findItem(R.id.help).setVisible(false);

                                        } else {
                                            nav_Menu.findItem(R.id.help).setVisible(true);

                                        }
                                    } else if (jsonObject1.getString("CodeDescription").equalsIgnoreCase("HOW TO SHOP")) {
                                        mEditor.putString("hts", jsonObject1.getString("Val1"));
                                        if (jsonObject1.getString("Val1").isEmpty()) {
                                            nav_Menu.findItem(R.id.hts).setVisible(false);

                                        } else {
                                            nav_Menu.findItem(R.id.hts).setVisible(true);

                                        }
                                    } else if (jsonObject1.getString("CodeDescription").equalsIgnoreCase("LEGAL")) {
                                        mEditor.putString("lgl", jsonObject1.getString("Val1"));
                                        if (jsonObject1.getString("Val1").isEmpty()) {
                                            nav_Menu.findItem(R.id.legal).setVisible(false);

                                        } else {
                                            nav_Menu.findItem(R.id.legal).setVisible(true);

                                        }
                                    } else if (jsonObject1.getString("CodeDescription").equalsIgnoreCase("OUR MISSION")) {
                                        mEditor.putString("msn", jsonObject1.getString("Val1"));
                                        if (jsonObject1.getString("Val1").isEmpty()) {
                                            nav_Menu.findItem(R.id.msn).setVisible(false);

                                        } else {
                                            nav_Menu.findItem(R.id.msn).setVisible(true);

                                        }
                                    } else if (jsonObject1.getString("CodeDescription").equalsIgnoreCase("PRIVACY POLICY")) {
                                        mEditor.putString("pp", jsonObject1.getString("Val1"));
                                        if (jsonObject1.getString("Val1").isEmpty()) {
                                            nav_Menu.findItem(R.id.policy).setVisible(false);

                                        } else {
                                            nav_Menu.findItem(R.id.policy).setVisible(true);

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
                                            if (preferencehelper.getPrefsUsercategory().equalsIgnoreCase("523")) {
                                                nav_Menu.findItem(R.id.refund).setVisible(true);
                                            } else {
                                                nav_Menu.findItem(R.id.refund).setVisible(false);
                                            }

                                        } else {
                                            nav_Menu.findItem(R.id.refund).setVisible(true);
                                        }
                                    } else if (jsonObject1.getString("CodeDescription").equalsIgnoreCase("CONTACT")) {
                                        mEditor.putString("contactus", jsonObject1.getString("Val1"));
                                        if (jsonObject1.getString("Val1").isEmpty()) {
                                            nav_Menu.findItem(R.id.contactus).setVisible(false);

                                        } else {
                                            nav_Menu.findItem(R.id.contactus).setVisible(true);
                                        }
                                    }
                                    if (prefs.getPrefsUsercategory().equalsIgnoreCase("1062") || prefs.getPrefsUsercategory().equalsIgnoreCase("1058") && prefs.getPrefsEmail().equalsIgnoreCase("8826000395") || preferencehelper.getPrefsUsercategory().equalsIgnoreCase("523")) {
                                        nav_Menu.findItem(R.id.publicationnav).setVisible(true);


                                    } else {
                                        nav_Menu.findItem(R.id.publicationnav).setVisible(false);

                                    }

                                    if (preferencehelper.getPrefsUsercategory().equalsIgnoreCase("523")) {
                                        nav_Menu.findItem(R.id.history_nav).setVisible(true);
                                        nav_Menu.findItem(R.id.htrwrd).setVisible(true);
                                        nav_Menu.findItem(R.id.pending_nav).setVisible(true);
                                        nav_Menu.findItem(R.id.rfearn).setVisible(true);
                                        nav_Menu.findItem(R.id.publicationnav).setVisible(true);

                                    } else {
                                        nav_Menu.findItem(R.id.history_nav).setVisible(false);
                                        nav_Menu.findItem(R.id.htrwrd).setVisible(false);
                                        nav_Menu.findItem(R.id.pending_nav).setVisible(false);
                                        nav_Menu.findItem(R.id.publicationnav).setVisible(false);
                                        nav_Menu.findItem(R.id.rfearn).setVisible(false);
                                    }
                                    nav_Menu.findItem(R.id.refund).setVisible(false);
                                    nav_Menu.findItem(R.id.contactus).setVisible(false);
                                    nav_Menu.findItem(R.id.terms).setVisible(false);


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

                        Message.message(HomeStoreActivity.this, "Failed To Retrieve Data");
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
                    String finalparam = jkHelper.Encryptapi(param, HomeStoreActivity.this);
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

        Volley.newRequestQueue(HomeStoreActivity.this).add(postRequest);


    }

    public void getAllProductsindia() {
        //     swipeRefresh.setRefreshing(true);
        final ProgressDialog mProgressBar = new ProgressDialog(HomeStoreActivity.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.setCancelable(false);
        mProgressBar.show();
        SharedPreferences prefs = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);
        SharedPreferences.Editor mEditor = prefs.edit();

        ArrayList<ItemMasterhelper> masterlist = new ArrayList<>();
        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responses) {
                        // response
                        //      swipeRefresh.setRefreshing(false);
                        Log.e("Response393", responses);


                        mProgressBar.dismiss();
                        try {
                            JKHelper jkHelper = new JKHelper();
                            String response=jkHelper.Decryptapi(responses,HomeStoreActivity.this);


                            mEditor.putString("resp1", response);
                            mEditor.commit();

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("returnds");
                            if (jsonObject.getInt("success") == 1) {


                            } else {

                            }

                        } catch (Exception e) {
                            // mProgressBar.dismiss();

                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressBar.dismiss();

                        // swipeRefresh.setRefreshing(false);
                        // error
                        //   Log.e("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Preferencehelper prefs;
                prefs = new Preferencehelper(HomeStoreActivity.this);

                Map<String, String> params = new HashMap<String, String>();
                // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);

//                    String param = AppUrls.GET_ITEM_MASTER_URL+"&ContactID=" +prefs.getPrefsContactId() + "&Type=" +"805";
//                    Log.d("Beforeencrptionproduct", param);
//                    JKHelper jkHelper = new JKHelper();
//                    String finalparam = jkHelper.Encryptapi(param, HomeStoreProductsActivity.this);
                    if (prefs.getPrefsCountry().equalsIgnoreCase("India")) {
                        try {
                            // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);

                            String param = AppUrls.GET_ITEM_MASTER_URL+"&supplierid=66738"
                                    + "&Type=" +"0"+ "&ContactID=" +prefs.getPrefsContactId();
                            Log.d("Beforeencrptionproduct", param);
                            JKHelper jkHelper = new JKHelper();
                            String finalparam = jkHelper.Encryptapi(param, HomeStoreActivity.this);
                            params.put("val", finalparam);
                            Log.d("afterencrptionproduct", finalparam);
                            return params;

                        } catch (Exception e) {
                            e.printStackTrace();
                            return params;
                        }




                    } else {
                        try {
                            // String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);

                            String param = AppUrls.GET_ITEM_MASTER_URL+"&supplierid="+ prefs.getPrefsContactId()
                                    + "&Type=" +"0"+ "&ContactID=" +prefs.getPrefsContactId();
                            Log.d("Beforeencrptionproduct", param);
                            JKHelper jkHelper = new JKHelper();
                            String finalparam = jkHelper.Encryptapi(param, HomeStoreActivity.this);
                            params.put("val", finalparam);
                            Log.d("afterencrptionproduct", finalparam);
                            return params;

                        } catch (Exception e) {
                            e.printStackTrace();
                            return params;
                        }


                    }






            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(getApplicationContext()).add(postRequest);
    }

    public String[] getitemnamesearch(String searchTerm) {
        result = new ArrayList<>();
        for (int z = 0; z < mListDeliveryBoys.size(); z++) {
            if (mListDeliveryBoys.get(z).getFirstName().toLowerCase().contains(searchTerm.toLowerCase())) {
                result.add(mListDeliveryBoys.get(z).getFirstName() + "(" + mListDeliveryBoys.get(z).getAlartphonenumber() + ")");
                contactidhash.put(mListDeliveryBoys.get(z).getFirstName() + "(" + mListDeliveryBoys.get(z).getAlartphonenumber() + ")", mListDeliveryBoys.get(z).getContactID());

            } else if (mListDeliveryBoys.get(z).getAlartphonenumber().toLowerCase().contains(searchTerm.toLowerCase())) {
                result.add(mListDeliveryBoys.get(z).getFirstName() + "(" + mListDeliveryBoys.get(z).getAlartphonenumber() + ")");
                contactidhash.put(mListDeliveryBoys.get(z).getFirstName() + "(" + mListDeliveryBoys.get(z).getAlartphonenumber() + ")", mListDeliveryBoys.get(z).getContactID());
            }
        }
        String item[] = result.toArray(new String[result.size()]);
        return item;
    }


    @Override
    public void dashboardclick(int clickid) {
        if (clickid == 1) {


            startActivity(new Intent(HomeStoreActivity.this, HomeStoreProductsActivity.class));
        }
        if (clickid == 2) {
            startActivity(new Intent(HomeStoreActivity.this, OrderHistoryActivity.class));

        }
        if (clickid == 3) {
            startActivity(new Intent(HomeStoreActivity.this, SalesActivity.class));

        }
        if (clickid == 4) {
            startActivity(new Intent(HomeStoreActivity.this, PastOrderItem.class));

        }
        if (clickid == 5) {
            startActivity(new Intent(HomeStoreActivity.this, UpdateProfile.class));


        }
        if (clickid == 6) {
            startActivity(new Intent(HomeStoreActivity.this, GRSalesActivity.class));

        }
        if (clickid == 7) {
            new Preferencehelper(HomeStoreActivity.this).setPrefsViewType(0);
            startActivity(new Intent(HomeStoreActivity.this, HomeActivity.class));


        }
        if (clickid == 8) {
            startActivity(new Intent(getApplicationContext(), PurchaseOrderActivity.class));

        }
        if (clickid == 9) {

            startActivity(new Intent(HomeStoreActivity.this, GRMMaster.class));
        }
        if (clickid == 10) {
            startActivity(new Intent(HomeStoreActivity.this, StoreWiseStockActivityNew.class));

        }
        if (clickid == 11) {

            SharedPreferences prefs = getSharedPreferences("MORNING_PARCEL_POS",
                    MODE_PRIVATE);
            SharedPreferences.Editor mEditor = prefs.edit();
            mEditor.putString("resp2", "");
            mEditor.putString("resp1", "");
            mEditor.commit();

            startActivity(new Intent(HomeStoreActivity.this, NewBillActivity.class));

        }
        if (clickid == 12) {
            startActivity(new Intent(HomeStoreActivity.this, HomeBilling.class));


        }
        if (clickid == 13) {
            startActivity(new Intent(HomeStoreActivity.this, BranchWiseStock.class));


        }
        if (clickid == 14) {
            new Preferencehelper(HomeStoreActivity.this).setPrefsViewType(1);

            Preferencehelper prefs4 = new Preferencehelper(getApplicationContext());
            prefs4.setPREFS_trialuser("0");

            startActivity(new Intent(getApplicationContext(), CustomerSearchActivity.class).putExtra("type", "1"));


        }
        if (clickid == 15) {
            startActivity(new Intent(getApplicationContext(), SupplierSearchActivity.class));


        }
        if (clickid == 16) {
            startActivity(new Intent(getApplicationContext(), CustomerCollection.class));


        }
        if (clickid == 17) {
            Preferencehelper prefs4 = new Preferencehelper(getApplicationContext());
            prefs4.setPREFS_trialuser("2");
            startActivity(new Intent(getApplicationContext(), CustomerSearchActivity.class).putExtra("type", "2"));

        }

        if (clickid == 18) {

            startActivity(new Intent(getApplicationContext(), DailyOrder.class));

        }


        if (clickid == 19) {

            startActivity(new Intent(getApplicationContext(), Activity_MettingPage.class));

        }


        if (clickid == 20) {

            startActivity(new Intent(getApplicationContext(), MeetingReport.class));

        }


        if (clickid == 21) {

            startActivity(new Intent(getApplicationContext(), CollectionReport.class));

        }

        if (clickid==22)
        {
            startActivity(new Intent(getApplicationContext(), TatReportActivity.class));


        }
    }
}
