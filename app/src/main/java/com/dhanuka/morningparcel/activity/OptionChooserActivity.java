package com.dhanuka.morningparcel.activity;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.R;
import com.google.android.material.navigation.NavigationView;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.dhanuka.morningparcel.Helper.CatcodeHelper;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.adapter.BannerAdapter;
import com.dhanuka.morningparcel.adapter.BannerAdapterOne;
import com.dhanuka.morningparcel.adapter.dashboardItemsAdapter;
import com.dhanuka.morningparcel.beans.MainCatBean;
import com.dhanuka.morningparcel.customViews.CartCountView;
import com.dhanuka.morningparcel.customViews.HomeView;
import com.dhanuka.morningparcel.customViews.KKViewPager;
import com.dhanuka.morningparcel.customViews.LoginView;
import com.dhanuka.morningparcel.customViews.StoreView;
import com.dhanuka.morningparcel.dialog.SelectShop;
import com.dhanuka.morningparcel.utils.JKHelper;
import com.dhanuka.morningparcel.utils.log;

public class OptionChooserActivity extends BaseActivity {
    SharedPreferences prefsL;
    SharedPreferences.Editor mEditorL;
    @Nullable
    @BindView(R.id.container_body)
    FrameLayout container_body;
    @Nullable
    @BindView(R.id.kk_pager)
    ViewPager mPager;
    @Nullable
    @BindView(R.id.kk_pager1)
    KKViewPager mPager1;
    @Nullable
    @BindView(R.id.dots_indicate)
    DotsIndicator dotsIndicator;
    @Nullable
    @BindView(R.id.rv_category)
    RecyclerView mRecyclerView;
    CatcodeHelper catcodeHelper;
    ArrayList<CatcodeHelper> bannerlist = new ArrayList<>();
    CartCountView countView;
    StoreView storeView;
    LoginView loginView;
    HomeView homeView;
    public void openGrocery(View v) {
        mEditorL.putString("typer", "com");
        mEditorL.commit();
        // new SelectShop(OptionChooserActivity.this, 1, true).show();

        //  Utility.selectShop(OptionChooserActivity.this,1,true);
        storeType = 0;
        getAllShop();
    }

    public void openRestaurents(View v) {
        mEditorL.putString("typer", "restaurents");
        mEditorL.commit();
        storeType = 1;
        getAllShop();
    }

    public void moveToMap(View v) {
        startActivity(new Intent(OptionChooserActivity.this, ChooseLocationActivity.class));
    }

    Button btnDemo, btnLive;
    Dialog dialogPaymentMode;
    int storeType = 0;
    TextView txtLocation,
            txtAddress;

    List<MainCatBean.CatBean> list;



    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_option_chooser;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_option_chooser);

        View view = getLayoutInflater().inflate(R.layout.activity_option_chooser, container_body);
        ButterKnife.bind(this);
        countView = (CartCountView) LayoutInflater.from(OptionChooserActivity.this).inflate(R.layout.action_cart, null);
        loginView = (LoginView) LayoutInflater.from(OptionChooserActivity.this).inflate(R.layout.action_login, null);
        homeView = (HomeView) LayoutInflater.from(OptionChooserActivity.this).inflate(R.layout.item_home, null);
        storeView = (StoreView) LayoutInflater.from(OptionChooserActivity.this).inflate(R.layout.action_shop, null);
        txtLocation = (TextView) findViewById(R.id.txtLocation);
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        prefs = new Preferencehelper(this);
        prefsL = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);
        mEditorL = prefsL.edit();
        list = new ArrayList<>();
        list.add(new MainCatBean.CatBean());
        list.add(new MainCatBean.CatBean());
        list.add(new MainCatBean.CatBean());
        list.add(new MainCatBean.CatBean());
        list.add(new MainCatBean.CatBean());
        list.add(new MainCatBean.CatBean());
        list.add(new MainCatBean.CatBean());
        list.add(new MainCatBean.CatBean());
        list.add(new MainCatBean.CatBean());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        mRecyclerView.setAdapter(new dashboardItemsAdapter(this, list));
        txtLocation.setText(prefsL.getString("cntry", "India"));
        txtAddress.setText(prefsL.getString("addrs", ""));
        mEditorL.putString("shopId", "");
        mEditorL.commit();
        ArrayList<String> sliders = new ArrayList<>();
        sliders.add("");
        sliders.add("");
        sliders.add("");
        mPager.setAdapter(new BannerAdapterOne(OptionChooserActivity.this, sliders));
        // mPager.setAnimationEnabled(true);
        //   mPager.setFadeEnabled(true);
        //   mPager.setFadeFactor(1.5f);
        getCategoryCodes("67266", "116");

        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                // currentPage = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


        dialogPaymentMode = new Dialog(OptionChooserActivity.this);
        dialogPaymentMode.setContentView(R.layout.dialog_payment_selection);
        dialogPaymentMode.setCancelable(false);
        btnDemo = dialogPaymentMode.findViewById(R.id.clickok);
        btnLive = dialogPaymentMode.findViewById(R.id.clickok1);


        if (prefs.getPrefsPaymentoption().equalsIgnoreCase("Both") && !prefs.getPrefsContactId().isEmpty()) {
            dialogPaymentMode.show();
        }
        btnLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs.setPrefsPaymentoption("2");
                dialogPaymentMode.dismiss();
            }
        });
        btnDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs.setPrefsPaymentoption("1");
                dialogPaymentMode.dismiss();
            }
        });

    }

    @Override
    protected void onSideSliderClick() {

    }


    public static Preferencehelper prefs;

    public void getCategoryCodes(final String contactid, final String types) {

        StringRequest postRequest = new StringRequest(Request.Method.POST, getString(R.string.URL_BASE_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        JKHelper jkHelper = new JKHelper();
                        String responses = jkHelper.Decryptapi(response, OptionChooserActivity.this);
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

                                mPager1.setAdapter(new BannerAdapter(OptionChooserActivity.this, bannerlist));
                                mPager1.setAnimationEnabled(true);
                                mPager1.setFadeEnabled(true);
                                mPager1.setFadeFactor(0.2f);
                                dotsIndicator.setViewPager(mPager1);


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

                        Message.message(OptionChooserActivity.this, "Failed To Retrieve Data");
                        //   Log.e("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                try {
                    String param = getString(R.string.URL_GET_CATEGORY_CODE) + "&contactid=" + "0" + "&type=" + types;
                    Log.d("Beforeencrption", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, OptionChooserActivity.this);
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

        Volley.newRequestQueue(OptionChooserActivity.this).add(postRequest);


    }


    public void getAllShop() {
        final ProgressDialog mProgressBar = new ProgressDialog(OptionChooserActivity.this);
        mProgressBar.setTitle("Safe'O'Fresh");
        mProgressBar.setMessage("Loading...");
        mProgressBar.setCancelable(false);
        mProgressBar.show();
        String contctId = "";
        if (prefs.getPrefsContactId().isEmpty()) {
            contctId = "67266";
        } else {
            contctId = prefs.getPrefsContactId();
        }
        Log.e("HJGFJKHGF", "http://mmthinkbiz.com/MobileService.aspx/getds?method=Fillconsignee_Web&contactid=" + contctId + "&CId=1&storetype=" + storeType);
        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://mmthinkbiz.com/MobileService.aspx/getds?method=Fillconsignee_Web&contactid=" + contctId + "&CId=1&storetype=" + storeType,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        mProgressBar.dismiss();
                        JKHelper jkHelper = new JKHelper();
                        Log.e("hjgfdfghj", response);
                        String responses = response/*jkHelper.Decryptapi(response,OptionChooserActivity.this)*/;
                        mEditorL.putString("shopss", responses);
                        mEditorL.commit();

                        // if (prefs.getString("shopId", "").isEmpty()) {
                        //   if (prefs.getPrefsUsercategory().equalsIgnoreCase("1057")) {
                        new SelectShop(OptionChooserActivity.this, 1, true).show();

                        // Utility.selectShop(OptionChooserActivity.this,1,true);
                        // }
                        //  }

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

                   /* String param =  AppUrls.URL_FILL_CONSIGNEE+ "&contactid=7777" +"&CId=" + "1&storetype="+storeType;
                    Log.d("Beforeencrption", param);
                    JKHelper jkHelper = new JKHelper();
                    String finalparam = jkHelper.Encryptapi(param, OptionChooserActivity.this);
             */
                    params.put("", "");
                    //    Log.d("afterencrption", finalparam);
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
        Volley.newRequestQueue(OptionChooserActivity.this).add(postRequest);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        MenuItem item = menu.findItem(R.id.cart);
        MenuItem item1 = menu.findItem(R.id.shop);
        MenuItem itemhome = menu.findItem(R.id.home1);
        item.setActionView(countView);
        item1.setActionView(storeView);
        item.setVisible(false);
        item1.setVisible(false);
        MenuItem login = menu.findItem(R.id.login);
        login.setActionView(loginView);
        itemhome.setActionView(homeView);
        try {
            if (prefs.getPrefsContactId().isEmpty()) {
                login.setVisible(true);
            } else {
                login.setVisible(false);

            }
        } catch (Exception e) {
            login.setVisible(true);

        }
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.reachUs).setVisible(false);


        return true;
    }


}
