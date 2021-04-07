package com.dhanuka.morningparcel.activity;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.dhanuka.morningparcel.Helper.Preferencehelper;

import com.dhanuka.morningparcel.R;

import com.dhanuka.morningparcel.customViews.LoginView;
import com.dhanuka.morningparcel.utils.AppThemePreferences;
import com.dhanuka.morningparcel.utils.CustomTypefaceSpan;
import com.dhanuka.morningparcel.utils.PermissionModule;

import com.google.android.material.navigation.NavigationView;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public abstract class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ActionBarDrawerToggle actionBarDrawerToggle;
    LoginView storeView;

    @Nullable
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    CircleImageView img;
    //  @BindView(R.id.userName)
    TextView userName;
    @Nullable
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @Nullable
    @BindView(R.id.drawer)

    DrawerLayout drawerLayout;
    @Nullable
    @BindView(R.id.container_body)
    FrameLayout container_body;
    @Nullable
    @BindView(R.id.backbtn_layout)
    LinearLayout backbtn_layout;
    /* @BindView(R.id.filterlayout)
     LinearLayout filterlayout;
   */ @Nullable
    @BindView(R.id.imgback)
    ImageView imgback;
    @Nullable
    @BindView(R.id.txt_set)
    TextView txt_set;
    CoordinatorLayout coordinatorLayout;
    Menu nav_Menu;

    protected abstract int getLayoutResourceId();

    String strOpen = "Home";

    @Override
    protected void onResume() {
        super.onResume();
        try {
            nav_Menu = navigationView.getMenu();
            if (prefs.getPrefsContactId().equalsIgnoreCase("7777") || prefs.getPrefsContactId().isEmpty()) {
                nav_Menu.findItem(R.id.history_wallet).setVisible(false);
                nav_Menu.findItem(R.id.history_nav).setVisible(false);
                nav_Menu.findItem(R.id.logout).setTitle("Login");
                nav_Menu.findItem(R.id.pending_nav).setVisible(false);

            } else {
                nav_Menu.findItem(R.id.logout).setTitle("Logout");
                if (prefs.getPrefsUsercategory().equalsIgnoreCase("1057")) {
                    nav_Menu.findItem(R.id.pending_nav).setVisible(false);
                    nav_Menu.findItem(R.id.history_wallet).setVisible(true);
                    nav_Menu.findItem(R.id.ac_history).setVisible(true);
                    nav_Menu.findItem(R.id.refund).setVisible(false);
                    nav_Menu.findItem(R.id.contactus).setVisible(false);
                    nav_Menu.findItem(R.id.terms).setVisible(false);
                } else {
                    nav_Menu.findItem(R.id.ac_history).setVisible(false);
                    nav_Menu.findItem(R.id.history_wallet).setVisible(false);
                    nav_Menu.findItem(R.id.pending_nav).setVisible(true);

                }
            }
        } catch (Exception e) {

        }
        String currency = "";
        SharedPreferences prefs1 = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);


        if (prefs1.getString("Currency", "INR").equalsIgnoreCase("INR")) {
            currency = getResources().getString(R.string.rupee);
        } else {
            currency = "$";
        }

        try {
            if (!prefs.getPREFS_currentbal().isEmpty()) {
                String amt = df2.format(Double.parseDouble(prefs.getPREFS_currentbal()));
                nav_Menu.findItem(R.id.history_wallet).setTitle("Wallet(" + currency + "" + amt + ")");
            } else {
                nav_Menu.findItem(R.id.history_wallet).setTitle("Wallet(" + currency + "" + "0.0)");

            }
        } catch (Exception e) {
            nav_Menu.findItem(R.id.history_wallet).setTitle("Wallet(" + currency + "" + "0.0)");

        }
    }

    DecimalFormat df2 = new DecimalFormat("#.##");

    @Override
    public void onBackPressed() {
        try {
            if (drawerLayout!=null)
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intialiazation();
    }

    protected abstract void onSideSliderClick();

    String Lightheme;

    //    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(navigationView)) {
                    drawerLayout.closeDrawer(navigationView);
                } else {
                    drawerLayout.openDrawer(navigationView);

                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    SharedPreferences sharedPreferences;
    SharedPreferences prefs1;


    public void intialiazation() {
        prefs = new Preferencehelper(this);
        setContentView(R.layout.activity_base);
        coordinatorLayout = findViewById(R.id.cordinatorlayout);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Lightheme = sharedPreferences.getString("Theme", "");

        ButterKnife.bind(this);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle("");

        prefs1 = getSharedPreferences("MORNING_PARCEL_GROCERY",
                MODE_PRIVATE);
        setSupportActionBar(toolbar);

        userName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.userName);
        img = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.img);
        navigationView.getHeaderView(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  startActivity(new Intent(BaseActivity.this, ProfileActivity.class));
            }
        });
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new Preferencehelper(BaseActivity.this).getPrefsUsercategory().equalsIgnoreCase("1057")) {
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                }
                finish();
            }
        });
        PermissionModule permissionModule = new PermissionModule(BaseActivity.this);
        permissionModule.checkPermissions();


        Menu m = navigationView.getMenu();


        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }


        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                Menu menu = navigationView.getMenu();
                for (int i = 0; i < menu.size(); i++) {
                    if (!menu.getItem(i).isChecked()) {
                        //       menu.getItem(i).getIcon().setColorFilter(Color.parseColor("#ff9900"), PorterDuff.Mode.SRC_ATOP);
                    }
                }
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);

        for (int i = 0; i < navigationView.getMenu().size(); i++) {

            if (Lightheme.equalsIgnoreCase("1")) {

                setTextColorForMenuItem(navigationView.getMenu().getItem(i), R.color.white);

            } else {

                setTextColorForMenuItem(navigationView.getMenu().getItem(i), R.color.black1);
            }
        }

        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.menu_circle));
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(navigationView)) {
                    drawerLayout.closeDrawer(navigationView);
                } else {
                    drawerLayout.openDrawer(navigationView);

                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.setToolbarNavigationClickListener(null);

        Localdialog = new Dialog(BaseActivity.this);
        builder = new AlertDialog.Builder(this);
        Localdialog.setContentView(R.layout.custom_dialog_login);
        linearLayout_bg = Localdialog.findViewById(R.id.layout_bg);
        Localdialog.setCancelable(false);
        linearLayout_bg.setBackgroundColor(Color.parseColor("#ffffff"));

        nav_Menu = navigationView.getMenu();

    }

    public static Preferencehelper prefs;
    AlertDialog.Builder builder;
    Dialog Localdialog;
    LinearLayout linearLayout_bg;

    private void setTextColorForMenuItem(MenuItem menuItem, @ColorRes int color) {
        SpannableString spanString = new SpannableString(menuItem.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, color)), 0, spanString.length(), 0);
        menuItem.setTitle(spanString);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        switch (item.getItemId()) {
            case R.id.logout:

                if (!nav_Menu.findItem(R.id.logout).getTitle().equals("Login")) {
                    Localdialog.findViewById(R.id.btn_yes).setOnClickListener(new View.OnClickListener() {
                        @SuppressLint("ResourceAsColor")
                        public void onClick(View v) {
                            prefs1 = getSharedPreferences("MORNING_PARCEL_GROCERY",
                                    MODE_PRIVATE);
                            String strrr = prefs1.getString("cntry", "India");
                            SharedPreferences.Editor mEditor = prefs1.edit();
                            mEditor.putString("shopId", "");
                            mEditor.putString("cntry", "");
                            mEditor.putString("typer", "com");
                            mEditor.commit();
                            prefs.clearAllPrefs();
                            prefs.setPrefsLoginValue("0");
                            prefs.setPrefsEmail("");
                            prefs.setPrefsPassword("");
                            startActivity(new Intent(BaseActivity.this, StartActivityNew.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            finish();
//                            if (prefs1.getString("cntry", "India").equalsIgnoreCase("India")) {
//                                startActivity(new Intent(BaseActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
//                                finish();
//                            } else {
//                                mEditor.putString("cntry", strrr);
//                                mEditor.commit();
//
//                                finish();
//
//                            }
                        }
                    });

                    Localdialog.findViewById(R.id.btn_no).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Localdialog.dismiss();
                        }
                    });

                    Localdialog.show();
                } else {
                    prefs.clearAllPrefs();
                    //  new LoginDialog(BaseActivity.this).show();
                    startActivity(new Intent(BaseActivity.this, StartActivityNew.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                }
                break;

            case R.id.profileid:
                if (prefs.getPrefsContactId().isEmpty()) {
                    startActivity(new Intent(BaseActivity.this, StartActivityNew.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));

                } else {
                    startActivity(new Intent(BaseActivity.this, UpdateProfile.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));

                }

                break;

            case R.id.history_nav:
                // if (!strOpen.equalsIgnoreCase("history")) {
                strOpen = "history";


                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);

                float yInches = metrics.heightPixels / metrics.ydpi;
                float xInches = metrics.widthPixels / metrics.xdpi;
                double diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches);
                if (diagonalInches >= 8.0) {
                    // 8.0inch device or bigger
                    startActivity(new Intent(BaseActivity.this, OrderHistoryActvitylsp.class));
                } else {
                    // smaller device
                    startActivity(new Intent(BaseActivity.this, OrderHistoryActivity.class));
                }


                // }
                break;
            case R.id.changepas:
                startActivity(new Intent(BaseActivity.this, ChangePass.class));

                break;
            case R.id.publicationnav:
                startActivity(new Intent(BaseActivity.this, PromotionSelector.class));

                break;
            case R.id.history_wallet:

                if (prefs1.getString("cntry", "India").equalsIgnoreCase("India")) {
                    startActivityForResult(new Intent(BaseActivity.this, WalleTransactionActivity.class), 5566);
                }
                break;


            case R.id.feedback:
                strOpen = "reach";
                startActivity(new Intent(BaseActivity.this, ComplaintReason.class));

//                Utility.DialogOrderFeedback(BaseActivity.this, "", 1);
                break;
            case R.id.reachUs:
                //  if (!strOpen.equalsIgnoreCase("reach")) {
                strOpen = "reach";

                startActivity(new Intent(BaseActivity.this, Reach_Us.class).putExtra("type", "rch").putExtra("title", "Reach Us"));
                //  }
                break;
            case R.id.help:
                //  if (!strOpen.equalsIgnoreCase("reach")) {
                strOpen = "reach";

                startActivity(new Intent(BaseActivity.this, Reach_Us.class).putExtra("type", "hlp").putExtra("title", "Help"));
                //  }
                break;
            case R.id.rfearn:
                startActivity(new Intent(BaseActivity.this, RefernEarn.class));

                break;
            case R.id.about:
                //  if (!strOpen.equalsIgnoreCase("reach")) {
                strOpen = "reach";

                startActivity(new Intent(BaseActivity.this, Reach_Us.class).putExtra("type", "abt").putExtra("title", "About"));
                //  }
                break;
            case R.id.htrwrd:
                //  if (!strOpen.equalsIgnoreCase("reach")) {
                strOpen = "reach";

                startActivity(new Intent(BaseActivity.this, RewardPoints.class).putExtra("type", "abt").putExtra("title", "About"));
                //  }
                break;
            case R.id.legal:
                //  if (!strOpen.equalsIgnoreCase("reach")) {
                strOpen = "reach";

                startActivity(new Intent(BaseActivity.this, Reach_Us.class).putExtra("type", "lgl").putExtra("title", "Legal"));
                //  }
                break;
            case R.id.terms:
                //  if (!strOpen.equalsIgnoreCase("reach")) {
                strOpen = "reach";

                startActivity(new Intent(BaseActivity.this, Reach_Us.class).putExtra("type", "trms").putExtra("title", "Terms"));
                //  }
                break;
            case R.id.policy:
                //  if (!strOpen.equalsIgnoreCase("reach")) {
                strOpen = "reach";

                startActivity(new Intent(BaseActivity.this, Reach_Us.class).putExtra("type", "plc").putExtra("title", "Policy"));
                //  }
                break;

            case R.id.refund:
                //  if (!strOpen.equalsIgnoreCase("reach")) {
                strOpen = "reach";

                startActivity(new Intent(BaseActivity.this, Reach_Us.class).putExtra("type", "refund").putExtra("title", "Refund & Cancel"));
                //  }
                break;

            case R.id.contactus:
                //  if (!strOpen.equalsIgnoreCase("reach")) {
                strOpen = "reach";

                startActivity(new Intent(BaseActivity.this, Reach_Us.class).putExtra("type", "contactus").putExtra("title", "Contact us"));
                //  }
                break;
            case R.id.hts:
                //  if (!strOpen.equalsIgnoreCase("reach")) {
                strOpen = "reach";

                startActivity(new Intent(BaseActivity.this, Reach_Us.class).putExtra("type", "hts").putExtra("title", "How to shop"));
                //  }
                break;
            case R.id.msn:
                //  if (!strOpen.equalsIgnoreCase("reach")) {
                strOpen = "reach";

                startActivity(new Intent(BaseActivity.this, Reach_Us.class).putExtra("type", "msn").putExtra("title", "Our Mission"));
                //  }
                break;
            case R.id.home_nav:

                if (prefs1.getString("cntry", "India").equalsIgnoreCase("India")) {
                    //   if (!strOpen.equalsIgnoreCase("Home")) {
                    strOpen = "Home";
                    if (!prefs.getPrefsUsercategory().equalsIgnoreCase("1057")) {
                        startActivity(new Intent(BaseActivity.this, HomeStoreActivity.class));
                    } else {
                        startActivity(new Intent(BaseActivity.this, HomeActivity.class));

                    }
                } else {
                    startActivity(new Intent(BaseActivity.this, OptionChooserActivity.class));

                }

                //  }
                break;

            case R.id.pending_nav:
                //  if (!strOpen.equalsIgnoreCase("pending_nav")) {
                strOpen = "pending_nav";
                startActivity(new Intent(BaseActivity.this, PastOrderItem.class));
                //  }
                break;
            case R.id.ac_history:
                //  if (!strOpen.equalsIgnoreCase("pending_nav")) {
                strOpen = "ac_history";
                startActivity(new Intent(BaseActivity.this, NewOrderActivity.class));
                //  }
                break;
       /* switch (item.getItemId()) {
            case R.id.nav_home:

                break;
            case R.id.nav_wishlist:

                break;
            case R.id.nav_shipping_address:

                break;
            case R.id.nav_orders:

                break;
            case R.id.nav_brands:

                break;
            case R.id.nav_share:

                break;
            case R.id.nav_support:

                break;
            case R.id.nav_privacy:

                break;
            case R.id.nav_rate:

                break;
          case R.id.nav_logout:

                break;
        }*/
        }
        return true;
    }

    /*
        private void applyFontToMenuItem(MenuItem mi) {
            Typeface font;
            if (AppThemePreferences.getTheme(MainActivity.this).equalsIgnoreCase("light")) {

                font = Typeface.createFromAsset(getAssets(), "Roboto-Black.ttf");
            } else {
                font = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");

            }
            SpannableString mNewTitle = new SpannableString(mi.getTitle());
            mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            //mNewTitle.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, mNewTitle.length(), 0); Use this if you want to center the items
            mi.setTitle(mNewTitle);
        }
    */
    private void applyFontToMenuItem(MenuItem mi) {
        try {
            Typeface font;
            if (AppThemePreferences.getTheme(BaseActivity.this).equalsIgnoreCase("light")) {

                font = Typeface.createFromAsset(getAssets(), "Roboto-Black.ttf");
            } else {
                font = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");

            }
            SpannableString mNewTitle = new SpannableString(mi.getTitle());
            mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            //mNewTitle.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, mNewTitle.length(), 0); Use this if you want to center the items
            mi.setTitle(mNewTitle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /* @Override
    private void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {


            if (requestCode == 5566) {
                try {
                    String strWalletAmount = data.getStringExtra("mWalletAmount");
                    //   dblWalletAmount = Double.parseDouble(prefs.getPREFS_currentbal());
                    // calculateTotalAmount();
                    String currency = "";
                    SharedPreferences prefs1 = getSharedPreferences("MORNING_PARCEL_GROCERY",
                            MODE_PRIVATE);


                    if (prefs1.getString("Currency", "INR").equalsIgnoreCase("INR")) {
                        currency = getResources().getString(R.string.rupee);
                    } else {
                        currency = "$";
                    }

                    try {
                        if (!prefs.getPREFS_currentbal().isEmpty()) {
                            String amt = df2.format(Double.parseDouble(prefs.getPREFS_currentbal()));
                            nav_Menu.findItem(R.id.history_wallet).setTitle("Wallet(" + currency + "" + amt + ")");
                        } else {
                            nav_Menu.findItem(R.id.history_wallet).setTitle("Wallet(" + currency + "" + "0.0)");

                        }
                    } catch (Exception e) {
                        nav_Menu.findItem(R.id.history_wallet).setTitle("Wallet(" + currency + "" + "0.0)");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (resultCode != RESULT_CANCELED) {
            //  showDialog(((Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR)).getMessage());
        }

    }
*/

}
